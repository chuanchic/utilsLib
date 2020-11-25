package com.github.chuanchic.utilslibrary;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import com.github.chuanchic.utilslibrary.threadutil.FileProxy;
import com.github.chuanchic.utilslibrary.threadutil.SingleThreadProxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 图片操作类
 */
public class ImageUtil {

	public static BitmapFactory.Options getBitmapFactoryOptions(){
		BitmapFactory.Options outOptions = new BitmapFactory.Options();
		outOptions.inSampleSize = 1;
		outOptions.inPurgeable = true;
		outOptions.inInputShareable = true;
		outOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		return outOptions;
	}

	public static BitmapFactory.Options getBitmapFactoryOptions(Context context, int res, int reqWidth, int reqHeight){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中
		try {
			BitmapFactory.decodeResource(context.getResources(), res, options);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		setBitmapFactoryOptions(options, reqWidth, reqHeight);
		return options;
	}

	public static BitmapFactory.Options getBitmapFactoryOptions(String imagePath, int reqWidth, int reqHeight){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中
		try {
			BitmapFactory.decodeFile(imagePath, options);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		setBitmapFactoryOptions(options, reqWidth, reqHeight);
		return options;
	}

	public static BitmapFactory.Options getBitmapFactoryOptions(String imagePath){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中
		try {
			BitmapFactory.decodeFile(imagePath, options);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		int halfWidth = options.outWidth;
		int halfHeight = options.outHeight;
		while (halfWidth > 800 && halfHeight > 800){
			halfWidth = halfWidth / 2;
			halfHeight = halfHeight / 2;
		}

		setBitmapFactoryOptions(options, halfWidth, halfHeight);
		return options;
	}

	public static void setBitmapFactoryOptions(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int inSampleSize = 1;
		int originalWidth = options.outWidth;
		int originalHeight = options.outHeight;
		if (originalWidth > reqWidth || originalHeight > reqHeight) {
			int halfWidth = originalWidth / 2;
			int halfHeight = originalHeight / 2;
			while ((halfWidth / inSampleSize > reqWidth) && (halfHeight / inSampleSize > reqHeight)) {
				inSampleSize *= 2;
			}
		}
		options.inSampleSize =  inSampleSize;
		options.inJustDecodeBounds = false;// 重新设置该属性为false，加载图片返回
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
	}

	/**
	 * 保存bitmap
	 */
	public static void saveBitmap(Bitmap bitmap, File file){
		saveBitmap(bitmap, file, Bitmap.CompressFormat.JPEG, 60);
	}

	/**
	 * 保存bitmap
	 */
	public static void saveBitmapIgnoreExists(Bitmap bitmap, File file){
		saveBitmapIgnoreExists(bitmap, file, Bitmap.CompressFormat.JPEG, 60);
	}

	/**
	 * 保存bitmap
	 */
	public static void saveBitmap(Bitmap bitmap, File file, Bitmap.CompressFormat format, int quality){
		saveBitmap(bitmap, file, format, quality, true);
	}

	/**
	 * 保存bitmap
	 */
	public static void saveBitmapIgnoreExists(Bitmap bitmap, File file, Bitmap.CompressFormat format, int quality){
		saveBitmap(bitmap, file, format, quality, false);
	}

	/**
	 * 保存bitmap
	 */
	public static void saveBitmap(Bitmap bitmap, File file, Bitmap.CompressFormat format, int quality, boolean judgeExists){
		if (bitmap == null || file == null){
			return;
		}
		if(judgeExists && file.exists()){
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(format, quality, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取图片
	 */
	public static Bitmap getBitmap(Context context, int res) {
		if(context == null || res <= 0){
			return null;
		}
		try {
			BitmapFactory.Options options = getBitmapFactoryOptions();
			return BitmapFactory.decodeResource(context.getResources(), res, options);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取图片
	 */
	public static Bitmap getBitmap(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		try {
			BitmapFactory.Options options = getBitmapFactoryOptions();
			return BitmapFactory.decodeFile(path, options);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取 指定大小的 图片
	 */
	public static Bitmap getSpecialBitmap(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		try {
			BitmapFactory.Options options = getBitmapFactoryOptions(path);
			return BitmapFactory.decodeFile(path, options);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取图片
	 */
	public static Bitmap getBitmap(Context context, int res, int reqWidth, int reqHeight) {
		if(context == null || res <= 0){
			return null;
		}
		try {
			BitmapFactory.Options options = getBitmapFactoryOptions(context, res, reqWidth, reqHeight);
			return BitmapFactory.decodeResource(context.getResources(), res, options);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取图片
	 */
	public static Bitmap getBitmap(String path, int reqWidth, int reqHeight) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		try {
			BitmapFactory.Options options = getBitmapFactoryOptions(path, reqWidth, reqHeight);
			return BitmapFactory.decodeFile(path, options);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 图片缩放（固定比例）
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, float newWidth, float newHeight) {
		if(bitmap == null){
			return null;
		}
		if(newWidth <= 0 || newHeight <= 0){
			return bitmap;
		}
		float oriWidth = bitmap.getWidth();
		float oriHeight = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(newWidth / oriWidth, newHeight / oriHeight);// 缩放图片
		return Bitmap.createBitmap(bitmap, 0, 0, (int) oriWidth, (int) oriHeight, matrix, true);
	}

	/**
	 * 图片缩放（等比例）
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, float newWidth) {
		if(bitmap == null){
			return null;
		}
		if(newWidth <= 0){
			return bitmap;
		}
		float oriWidth = bitmap.getWidth();
		float oriHeight = bitmap.getHeight();
		float newHeight = oriHeight * newWidth / oriWidth;
		Matrix matrix = new Matrix();
		matrix.postScale(newWidth / oriWidth, newHeight / oriHeight);// 缩放图片
		return Bitmap.createBitmap(bitmap, 0, 0, (int) oriWidth, (int) oriHeight, matrix, true);
	}

	/**
	 * 压缩图片大小decode
	 * @param options 选项
	 * @param minSideLength 最小长度
	 * @param maxNumOfPixels 大小
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	/**
	 * 压缩图片大小
	 * @param options 选项
	 * @param minSideLength 最小长度
	 * @param maxNumOfPixels 大小
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

    /**
     * 旋转图片
     */
	public static Bitmap rotaingBitmap(Bitmap bitmap, int degrees) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		int x = 0;
		int y = 0;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
        boolean filter = true;
		return Bitmap.createBitmap(bitmap, x, y, width, height, matrix, filter);
	}

	public static int readPictureDegree(String path) {
		int degrees = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			String tag = ExifInterface.TAG_ORIENTATION;
			int defaultValue = ExifInterface.ORIENTATION_NORMAL;
			int orientation = exifInterface.getAttributeInt(tag, defaultValue);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degrees = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degrees = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degrees = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degrees;
	}

	/**
	 * 把图片经过Base64编码为String类型的数据
	 */
	public static String encodeBitmap(Bitmap bitmap){
		return encodeBitmap(bitmap, Bitmap.CompressFormat.PNG, 100);
	}

	/**
	 * 把图片经过Base64编码为String类型的数据
	 */
	public static String encodeBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int quality){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(format, quality, baos);
		return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
	}

	/**
	 * 截屏
	 */
	public static Bitmap screenShot(View view){
		if(view == null){
			return null;
		}

		//创建bitmap
		int width = view.getMeasuredWidth();
		int height = view.getMeasuredHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		//使用Canvas，调用View控件的onDraw方法，绘制图片
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	/**
	 * 保存图片的回调
	 */
	public interface SaveBitmapCallback {
		void onResult(String uriString);
	}

	/**
	 * 保存图片到本地
	 * @param activity 上下文
	 * @param imgStr 图片描述
	 * @param bitmap 图片数据
	 * @param callback 回调
	 */
	public static void saveBitmap(final Activity activity, final String imgStr, final Bitmap bitmap, final SaveBitmapCallback callback){
		FileProxy.getInstance().addProxyRunnable(new SingleThreadProxy.ProxyRunnable() {
			@Override
			public void run() {
				String uriString = null;
				if(activity == null || TextUtils.isEmpty(imgStr) || bitmap == null){
				}else{
					String displayName = MD5Util.md5(imgStr) + ".jpeg";
					ContentValues values = new ContentValues();
					values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
					values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
						values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
					} else {
						String external = Environment.getExternalStorageDirectory().getAbsolutePath();
						String dataPath = external + "/" + Environment.DIRECTORY_DCIM + "/" + displayName;
						values.put(MediaStore.MediaColumns.DATA, dataPath);
					}

					ContentResolver contentResolver = activity.getContentResolver();
					Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
					try {
						OutputStream outputStream = contentResolver.openOutputStream(uri);
						if (outputStream != null) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
							outputStream.close();
							uriString = uri.toString();
						}
					}catch (Exception e){
					}
				}
				final String uriTemp = uriString;
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(callback != null){
							callback.onResult(uriTemp);
						}
					}
				});
			}
		});
	}

	/**
	 * 通过 uri 获取图片
	 * @param context 上下文
	 * @param uri 图片uri
	 * @return bitmap
	 */
	public static Bitmap getBitmapFromUri(Context context, Uri uri) {
		if(context == null || uri == null){
			return null;
		}
		try {
			ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
			Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
			pfd.close();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}