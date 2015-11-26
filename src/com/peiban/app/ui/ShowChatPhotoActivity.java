package com.peiban.app.ui;

import java.io.File;
import java.io.FileOutputStream;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.bitmap.core.BitmapCommonUtils;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.peiban.R;
import com.peiban.app.action.ImageInfoAction;

/**
 * 
 * 功能： 显示聊天中的会话图片 <br />
 * 日期：2013-6-26<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class ShowChatPhotoActivity extends BaseActivity {
	private static final String TAG = ShowChatPhotoActivity.class
			.getCanonicalName();

	private Bitmap drawable;
	@ViewInject(id = R.id.show_bitmap)
	private ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.bitmap_show);
		baseInit();
		
		try {
			String url = getIntent().getStringExtra("data");
			getBitmapUrl(url);
		} catch (NullPointerException e) {
			finish();
			return;
		}
		
	}
	
	
	private void  getBitmapUrl(final String url) {
		new AsyncTask<Void, Void, Bitmap>(){
			
			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				if(result == null){
					getWaitDialog().setMessage("加载失败");
					finish();
				}else{
					drawable = result;
					imageView.setImageBitmap(drawable);
				}
				getWaitDialog().cancel();
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				getWaitDialog().setMessage("加载");
				getWaitDialog().show();
			}

			@Override
			protected Bitmap doInBackground(Void... params) {
				try {
					Bitmap bitmap = getPhotoBitmap().getBitmapFromDiskCache(url);
					return bitmap;
				} catch (Exception e) {
					return null;
				}
				
			}
			
		}.execute();
	}

	protected void baseInit() {
		super.baseInit();
//		imageView.setOnTouchListener(new MulitPointTouchListener(imageView));
	}

	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		saveBitmap();
	}

	private void saveBitmap() {
		if (!checkSdcard()) {
			return;
		}

		if (drawable != null) {
			File file = new File(BitmapCommonUtils.getExternalCacheDir(
					getBaseContext()).getParentFile(), "chatImg");

			if (!file.exists()) {
				if (!file.mkdirs()) {
					showToast("创建目录失败!");
				}
				;
			}

			File file2 = new File(file, ImageInfoAction.getPhotoFileName());
			FileOutputStream fos = null;

			try {
				fos = new FileOutputStream(file2);
				boolean flag = drawable.compress(CompressFormat.JPEG, 100, fos);
				if (flag) {
					showToast("保存成功:" + file2.getAbsolutePath());
				} else {
					showToast("保存失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				showToast("保存失败");
			}

		} else {
			showToast("保存失败!");
		}
	}

	@Override
	protected void initTitle() {
		setTitleContent("");
		setBtnBack();
		setTitleRight("保存本地");
	}

	public class MulitPointTouchListener implements OnTouchListener {

		Matrix matrix = new Matrix();
		Matrix savedMatrix = new Matrix();

		public ImageView image;
		/** 无 */
		static final int NONE = 0;
		/** 拖动 */
		static final int DRAG = 1;
		/** 放大 */
		static final int ZOOM = 2;
		int mode = NONE;  // 默认为无效果

		/** point 点，一个点的x y
		 * 坐标， 开始点 */
		PointF start = new PointF();
		PointF mid = new PointF();
		float oldDist = 1f;

		public MulitPointTouchListener(ImageView image) {
			super();
			this.image = image;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			this.image.setScaleType(ScaleType.MATRIX);

			ImageView view = (ImageView) v;
			// dumpEvent(event);

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			
			// 按下
			case MotionEvent.ACTION_DOWN:

				Log.w(TAG, "ACTION_DOWN");
				// 获取View 的坐标变换矩阵
				matrix.set(view.getImageMatrix());
				// 保存View 的坐标矩阵
				savedMatrix.set(matrix);
				// 保存手指点击的坐标位置
				start.set(event.getX(), event.getY());
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				Log.w("FLAG", "ACTION_POINTER_DOWN");
				// 获取两点之间的距离
				oldDist = spacing(event);
				// 如果两点距离大于 10
				if (oldDist > 10f) {
					// 使用新的矩阵
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_UP:
				Log.w(TAG, "ACTION_UP");
			case MotionEvent.ACTION_POINTER_UP:
				Log.w(TAG, "ACTION_POINTER_UP");
				mode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:
				Log.w(TAG, "ACTION_MOVE");
				if (mode == DRAG) {  // 如果为拖动
					matrix.set(savedMatrix);   // 矩阵使用保存的
					// 移动当前矩阵的位置
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);
				} else if (mode == ZOOM) {
					// 放大
					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}

			view.setImageMatrix(matrix);
			return true;
		}
		
		// 获取两点之间的距离
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			// 
			return FloatMath.sqrt(x * x + y * y);
		}
		
		
		// 获取两点的中心点位置
		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}
	}
	
	
	protected void onDestroy() {
		super.onDestroy();
		if(drawable != null && !drawable.isRecycled()){
			drawable.recycle();
		}
		System.gc();
	}
}
