package com.peiban.app.ui.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class BitmapUtils {
	/**
	 * 图片圆角
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output); // 得到一个画布

		final int color = 0xff424242; // 设置颜料
		final Paint paint = new Paint(); // 获取一只画笔
		// 画出一块区域
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect); // RectF 与 Rect的区别, Rect 使用int类型,
												// RectF使用 float类型
		final float roundPx = 16; // 圆角幅度 4 px

		paint.setAntiAlias(true); // 是否去除锯齿,让边距平滑。
		canvas.drawARGB(0, 0, 0, 0); // 添加画布颜色
		paint.setColor(color); // 使用画笔添加涂料
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint); // 规划出画布可以使用的区间

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
//		bitmap.recycle();
		return output;
	}

	/**
	 * 压缩图片
	 * 
	 * @param bitmap
	 * @param w
	 *            如 320
	 * @param h
	 *            如 460
	 * @return 作者:fighter <br />
	 *         创建时间:2013-3-7<br />
	 *         修改时间:<br />
	 */
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {

		// load the origial Bitmap
		Bitmap BitmapOrg = bitmap;

		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		// calculate the scale
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the Bitmap
		matrix.postScale(scaleWidth, scaleHeight);

		// if you want to rotate the Bitmap
		// matrix.postRotate(45);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);

		// make a Drawable from Bitmap to allow to set the Bitmap
		// to the ImageView, ImageButton or what ever
		return resizedBitmap;

	}
}
