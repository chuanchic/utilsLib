package com.github.chuanchic.utilslibrary;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

	public static BitmapFactory.Options getBitmapFactoryOptions(Context context, int res, int thumbWidth, int extraScaling){
		Resources resources = context.getResources();
		BitmapFactory.Options outOptions = new BitmapFactory.Options();
		outOptions.inJustDecodeBounds = true;// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中
		try {
			BitmapFactory.decodeResource(resources, res, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (outOptions.outWidth > thumbWidth) {
			outOptions.inSampleSize = outOptions.outWidth / thumbWidth + 1 + extraScaling;
			outOptions.outWidth = thumbWidth;
			outOptions.outHeight = outOptions.outHeight / outOptions.inSampleSize;
		}
		outOptions.inJustDecodeBounds = false;// 重新设置该属性为false，加载图片返回
		outOptions.inPurgeable = true;
		outOptions.inInputShareable = true;
		outOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		return outOptions;
	}

	public static BitmapFactory.Options getBitmapFactoryOptions(String imagePath, int thumbWidth, int extraScaling){
		BitmapFactory.Options outOptions = new BitmapFactory.Options();
		outOptions.inJustDecodeBounds = true;// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中
		try {
			BitmapFactory.decodeFile(imagePath, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (outOptions.outWidth > thumbWidth) {
			outOptions.inSampleSize = outOptions.outWidth / thumbWidth + 1 + extraScaling;
			outOptions.outWidth = thumbWidth;
			outOptions.outHeight = outOptions.outHeight / outOptions.inSampleSize;
		}
		outOptions.inJustDecodeBounds = false;// 重新设置该属性为false，加载图片返回
		outOptions.inPurgeable = true;
		outOptions.inInputShareable = true;
		outOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		return outOptions;
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
	public static void saveBitmap(Bitmap bitmap, File file, Bitmap.CompressFormat format, int quality){
		if (bitmap == null || file == null || file.exists()){
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
	 * @param res 资源文件drawable
	 */
	public static Bitmap getBitmap(Context context, int res) {
		if(context == null || res <= 0){
			return null;
		}
		try {
			Resources resources = context.getResources();
			BitmapFactory.Options outOptions = getBitmapFactoryOptions();
			return BitmapFactory.decodeResource(resources, res, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取图片
	 * @param path 图片的路径
	 */
	public static Bitmap getBitmap(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		try {
			BitmapFactory.Options outOptions = getBitmapFactoryOptions();
			return BitmapFactory.decodeFile(path, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取图片的缩略图
	 * @param res 资源文件drawable
	 * @param thumbWidth 缩略图的宽
	 * @param extraScaling 额外可以加的缩放比例
	 */
	public static Bitmap getThumbBitmap(Context context, int res, int thumbWidth, int extraScaling) {
		if(context == null || res <= 0){
			return null;
		}
		try {
			Resources resources = context.getResources();
			BitmapFactory.Options outOptions = getBitmapFactoryOptions(context, res, thumbWidth, extraScaling);
			return BitmapFactory.decodeResource(resources, res, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取图片的缩略图
	 * @param path 图片的路径
	 * @param thumbWidth 缩略图的宽
	 * @param extraScaling 额外可以加的缩放比例
	 */
	public static Bitmap getThumbBitmap(String path, int thumbWidth, int extraScaling) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		try {
			BitmapFactory.Options outOptions = getBitmapFactoryOptions(path, thumbWidth, extraScaling);
			return BitmapFactory.decodeFile(path, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 图片缩放
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, float newWidth, float newHeight) {
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);// 缩放图片
		return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
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
}