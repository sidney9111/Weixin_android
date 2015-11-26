package com.shangwupanlv.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.peiban.R;
import com.peiban.application.PeibanApplication;

public class SclaeView extends ImageView implements OnTouchListener {
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private int mode = NONE;
	private float oldDist;
	private PointF start = new PointF();
	private PointF mid = new PointF();

	// This is the base transformation which is used to show the image
	// initially. The current computation for this shows the image in
	// it's entirety, letterboxing as needed. One could choose to
	// show the image as cropped instead.
	//
	// This matrix is recomputed when we go from the thumbnail image to
	// the full size image.
	protected Matrix mBaseMatrix = new Matrix();

	// This is the supplementary transformation which reflects what
	// the user has done in terms of zooming and panning.
	//
	// This matrix remains the same when we go from the thumbnail image
	// to the full size image.
	protected Matrix mSuppMatrix = new Matrix();

	// This is the final matrix which is computed as the concatentation
	// of the base matrix and the supplementary matrix.
	private final Matrix mDisplayMatrix = new Matrix();

	// Temporary buffer used for getting the values out of a matrix.
	private final float[] mMatrixValues = new float[9];

	protected Bitmap image = null;

	int mThisWidth = -1, mThisHeight = -1;

	float mMaxZoom = 3.5f;// 最大缩放
	float mMinZoom;// 最小缩放

	private int imageWidth;// 图片的宽度
	private int imageHeight;// 图片的高度

	private float scaleRate;// 缩放比列

	private int screenWidth; // 屏幕的宽度
	private int screenHeight; // 屏幕的高度

	public SclaeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setOnTouchListener(this);
	}

	public SclaeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnTouchListener(this);
		init();
	}

	public SclaeView(Context context) {
		super(context);
		this.setOnTouchListener(this);
	}

	private void init() {
		System.out.println("=======================imageInit");
		Activity activity = PeibanApplication.getInstance().getActivity();
		screenHeight = activity.getWindow().getWindowManager()
				.getDefaultDisplay().getHeight() - getResources().getDimensionPixelSize(R.dimen.title_height);
		screenWidth = activity.getWindow().getWindowManager()
				.getDefaultDisplay().getWidth();
	}

	/**
	 * 得到实际缩放的比列
	 */
	private void arithScaleRate() {
		float scaleWidth = screenWidth / (float) imageWidth;
		float scaleHeight = screenHeight / (float) imageHeight;
		scaleRate = Math.min(scaleWidth, scaleHeight);
		System.out.println("实际缩放比列:" + scaleRate);
	}

	public float getScaleRate() {
		return scaleRate;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
	
	public Bitmap getImage() {
		return image;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
				&& !event.isCanceled()) {
			if (getScale() > 1.0f) {
				// If we're zoomed in, pressing Back jumps out to show the
				// entire image, otherwise Back returns the user to the gallery.
				zoomTo(1.0f);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	protected Handler mHandler = new Handler();

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			setBitmap(bitmapDrawable);
			
		} else if(drawable instanceof TransitionDrawable){
			TransitionDrawable transitionDrawable = (TransitionDrawable) drawable;
			System.out.println("-------------------:::::" + transitionDrawable.getNumberOfLayers());
			Drawable mDrawable = transitionDrawable.getDrawable(1);
			if(mDrawable != null ){
				System.out.println(mDrawable.getClass().getCanonicalName());
			}
			
			if (mDrawable != null && mDrawable instanceof BitmapDrawable) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) mDrawable;
				setBitmap(bitmapDrawable);
				
			}
		}
	}
	
	private void setBitmap(BitmapDrawable bitmapDrawable){
		Bitmap bitmap = bitmapDrawable.getBitmap();
		if (bitmap != null) {
			imageWidth = bitmap.getWidth();
			imageHeight = bitmap.getHeight();
			image = bitmap;
			initImageView();
		}
	}
	
	private void initImageView(){
		// 获取实际缩放的比列
		arithScaleRate();
		// 开始放大
		zoomTo(scaleRate, 0, 0);
		// 开始移动
		layoutToCenter();
	}
	
	

	/**
	 * 
	 */
	public void layoutToCenter() {
		// 获取当前的缩放
		float scale = getScale();
		System.out.println("屏幕:" + screenWidth + "  ---  " + screenHeight);
		// 得到图片当前的矩阵信息
		float width = imageWidth * scale;
		float height = imageHeight * scale;
		System.out.println("图片:" + width + "  ---  " + height);
		
		// 图片到边界的距离
		float fill_width = screenWidth - width;
		float fill_height = screenHeight - height;
		System.out.println("边界:" + fill_width + "  ---  " + fill_height);
		
		// 移动的距离
		float tran_width = 0f;
		float tran_height = 0f;
		
		tran_width = fill_width / 2;
		tran_height = fill_height / 2;
		System.out.println("移动:" + tran_width + " -- " + tran_height);
		// 还原所有的移动
		mSuppMatrix.getValues(mMatrixValues);
		mMatrixValues[Matrix.MTRANS_X] = 0;
		mMatrixValues[Matrix.MTRANS_Y] = 0;
		mSuppMatrix.setValues(mMatrixValues);
		// 居中移动
		postTranslate(tran_width, tran_height);
		setImageMatrix(getImageViewMatrix());
	}

	// 求两点距离
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 求两点间中点
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	protected float getValue(Matrix matrix, int whichValue) {
		matrix.getValues(mMatrixValues);
		mMinZoom = (screenWidth / 2f) / imageWidth;

		return mMatrixValues[whichValue];
	}

	// Get the scale factor out of the matrix.
	protected float getScale(Matrix matrix) {
		return getValue(matrix, Matrix.MSCALE_X);
	}

	protected float getScale() {
		return getScale(mSuppMatrix);
	}

	// Combine the base matrix and the supp matrix to make the final matrix.
	protected Matrix getImageViewMatrix() {
		// The final matrix is computed as the concatentation of the base matrix
		// and the supplementary matrix.
		mDisplayMatrix.set(mBaseMatrix);
		mDisplayMatrix.postConcat(mSuppMatrix);
		return mDisplayMatrix;
	}

	static final float SCALE_RATE = 1.25F;

	// Sets the maximum zoom, which is a scale relative to the base matrix. It
	// is calculated to show the image at 400% zoom regardless of screen or
	// image orientation. If in the future we decode the full 3 megapixel image,
	// rather than the current 1024x768, this should be changed down to 200%.
	protected float maxZoom() {
		if (image == null) {
			return 1F;
		}

		float fw = (float) image.getWidth() / (float) mThisWidth;
		float fh = (float) image.getHeight() / (float) mThisHeight;
		float max = Math.max(fw, fh) * 4;
		return max;
	}

	protected void zoomTo(float scale, float centerX, float centerY) {
//		if (scale > mMaxZoom) {
//			scale = mMaxZoom;
//		} else if (scale < mMinZoom) {
//			scale = mMinZoom;
//		}
		float oldScale = getScale();
		float deltaScale = scale / oldScale;

		mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	protected void zoomTo(final float scale, final float centerX,
			final float centerY, final float durationMs) {
		final float incrementPerMs = (scale - getScale()) / durationMs;
		final float oldScale = getScale();
		final long startTime = System.currentTimeMillis();

		mHandler.post(new Runnable() {
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);
				float target = oldScale + (incrementPerMs * currentMs);
				zoomTo(target, centerX, centerY);
				if (currentMs < durationMs) {
					mHandler.post(this);
				}
			}
		});
	}

	protected void zoomTo(float scale) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		zoomTo(scale, cx, cy);
	}

	protected void zoomToPoint(float scale, float pointX, float pointY) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		panBy(cx - pointX, cy - pointY);
		zoomTo(scale, cx, cy);
	}

	protected void zoomIn() {
		zoomIn(SCALE_RATE);
	}

	protected void zoomOut() {
		zoomOut(SCALE_RATE);
	}

	protected void zoomIn(float rate) {
		if (getScale() >= mMaxZoom) {
			return; // Don't let the user zoom into the molecular level.
		} else if (getScale() <= mMinZoom) {
			return;
		}
		if (image == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		mSuppMatrix.postScale(rate, rate, cx, cy);
		setImageMatrix(getImageViewMatrix());
	}

	protected void zoomOut(float rate) {
		if (image == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		// Zoom out to at most 1x.
		Matrix tmp = new Matrix(mSuppMatrix);
		tmp.postScale(1F / rate, 1F / rate, cx, cy);

		if (getScale(tmp) < 1F) {
			mSuppMatrix.setScale(1F, 1F, cx, cy);
		} else {
			mSuppMatrix.postScale(1F / rate, 1F / rate, cx, cy);
		}
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	protected void center(boolean horizontal, boolean vertical) {
		// if (mBitmapDisplayed.getBitmap() == null) {
		// return;
		// }
		if (image == null) {
			return;
		}

		Matrix m = getImageViewMatrix();

		RectF rect = new RectF(0, 0, imageWidth, imageHeight);
		// RectF rect = new RectF(0, 0, imageWidth*getScale(),
		// imageHeight*getScale());

		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			int viewHeight = getHeight();
			if (height < viewHeight) {
				deltaY = (viewHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < viewHeight) {
				deltaY = getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int viewWidth = getWidth();
			if (width < viewWidth) {
				deltaX = (viewWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < viewWidth) {
				deltaX = viewWidth - rect.right;
			}
		}

		postTranslate(deltaX, deltaY);
		setImageMatrix(getImageViewMatrix());
	}

	public void postTranslate(float dx, float dy) {
		mSuppMatrix.postTranslate(dx, dy);
		setImageMatrix(getImageViewMatrix());
	}

	float _dy = 0.0f;

	protected void postTranslateDur(final float dy, final float durationMs) {
		_dy = 0.0f;
		final float incrementPerMs = dy / durationMs;
		final long startTime = System.currentTimeMillis();
		mHandler.post(new Runnable() {
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);

				postTranslate(0, incrementPerMs * currentMs - _dy);
				_dy = incrementPerMs * currentMs;

				if (currentMs < durationMs) {
					mHandler.post(this);
				}
			}
		});
	}

	protected void panBy(float dx, float dy) {
		postTranslate(dx, dy);
		setImageMatrix(getImageViewMatrix());
	}
	
	
	/**
	 * 判断图片是否可以托拽<br/>
	 * 判断图片大小是否比 view 的大小 小
	 * @return true 可以托
	 * @author fighter <br />
	 * 创建时间:2013-7-6<br />
	 * 修改时间:<br />
	 */
	private boolean isDrag(){
		// 获取当前图片的大小
		float width = imageWidth * getScale();
		float height = imageHeight * getScale();
		
		System.out.println("宽:" + width + "   长:" + height);
		
		if (width <= screenWidth && height <= screenHeight) {
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mDisplayMatrix.set(mSuppMatrix); // 把原始 Matrix对象保存起来
			start.set(event.getX(), event.getY()); // 设置x,y坐标
			mode = DRAG;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			// 如果超出了放大和缩小的范围，就不进行缩放操作
			if(getScale() < mMinZoom){
				mHandler.postAtTime(new Runnable() {
					
					@Override
					public void run() {
						initImageView();
					}
				}, 500);
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				mDisplayMatrix.set(mSuppMatrix);
				midPoint(mid, event); // 求出手指两点的中点
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				mSuppMatrix.set(mDisplayMatrix);
				mSuppMatrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
				if(!isDrag()){
					layoutToCenter();
				}
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					mSuppMatrix.set(mDisplayMatrix);
					float scale = newDist / oldDist;
					// System.out.println("缩放的比例:" + scale);
					mSuppMatrix.postScale(scale, scale, mid.x, mid.y);
					
//					layoutToCenter();
				}
			}
			break;
		}

		setImageMatrix(mSuppMatrix);
		return true;
	}
}
