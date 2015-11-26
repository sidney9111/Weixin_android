package com.peiban.app.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.peiban.R;
import com.peiban.app.ui.RotateActivity;
import com.peiban.application.PeibanApplication;
import com.peiban.command.FileTool;

import net.tsz.afinal.bitmap.core.BitmapDecoder;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import android.widget.Toast;

public class ImageInfoAction extends BaseAction {
	static final String TAG = ("ImageInfoAction");

	public static final int PHOTO_LOCAL = 1;
	public static final int PHOTO_LOCAL_CROP = 2;
	public static final int PHOTO_CAMERA = 3;
	public static final int PHOTO_CAMERA_CROP = 4;

	private static final int PHOTO_CAMERA_CROP_TWO = 5;
	
	/** 图片旋转 */
	private static final int PHOTO_ROTALE = 456;
	
	
	private File cameraDir;

	protected Activity activity;
	private PeibanApplication application;

	private int outputX = 150;
	private int outputY = 150;
	
	private int outWidth = 240;
	private int outHeight = 320;

	protected File currCameraPhotoFile; // 调用系统相机拍的照片存放的位置!
	
	private OnBitmapListener onBitmapListener;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (null == onBitmapListener)
				return;

			onBitmapListener.getToBitmap(msg.what, null == msg.obj ? null
					: (Bitmap) msg.obj);
		}

	};

	public ImageInfoAction(Activity activity) {
		super(activity);
		this.activity = activity;
		application = (PeibanApplication) this.activity.getApplication();
		cameraDir = new File(android.os.Environment.getExternalStorageDirectory(), "/Android/data/" + activity.getPackageName() + "/Camera");
		init();
	}
	
	private void init(){
		if((FileTool.isMounted() && !cameraDir.exists())){
			if(!cameraDir.mkdirs()){
				Toast.makeText(this.activity, this.activity.getResources().getString(R.string.toast_sdcard_mounted), Toast.LENGTH_SHORT).show();
			}
		}
	}

	/** 获取本地图片Intent */
	protected Intent localImgIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				Media.EXTERNAL_CONTENT_URI);
		return intent;
	}

	/** 获取本地直接剪切Intent 过时*/
	@Deprecated
	protected Intent localCropImgIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", true);
		return intent;
	}

	/** 获取系统剪切图片的Intent */
	protected Intent getSystemCropIntent(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri,"image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", true);
		return intent;
	}

	/**
	 * 获取相机图片Intent <br/>
	 * 并将图片存放在 mnt/sdcard/DCIM/Camera/ 下<br />
	 * 
	 * @return 作者:fighter <br />
	 *         创建时间:2013-1-29<br />
	 *         修改时间:<br />
	 */
	protected Intent getCameraIntent(){
		currCameraPhotoFile = new File(cameraDir, getPhotoFileName());
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(currCameraPhotoFile));
		return intent;
	}

	/** 获取本地图片 
	 * @throws SDCardException */
	public void getLocolPhoto(){
		Intent intent = localImgIntent();
		activity.startActivityForResult(intent, PHOTO_LOCAL);
	}

	/** 获取本地剪切图片 
	 * @throws SDCardException */
	public void getCropLocolPhoto(){
		// 获取本地图片
		Intent intent = localImgIntent();
		// 获取本地图片进行剪切
//		Intent intent = localCropImgIntent();
		activity.startActivityForResult(intent, PHOTO_LOCAL_CROP);
	}

	/** 获取照片剪切图片 
	 * @throws IOException 
	 * @throws SDCardException */
	public void getCropCameraPhoto(){
		Intent intent = getCameraIntent();
		activity.startActivityForResult(intent, PHOTO_CAMERA_CROP);
	}

	/** 获取相机的图片 
	 * @throws IOException 
	 * @throws SDCardException */
	public void getCameraPhoto(){
		Intent intent = getCameraIntent();
		activity.startActivityForResult(intent, PHOTO_CAMERA);
	}

	private Bitmap getDataSource(Intent data) {
		Bitmap bm = data.getParcelableExtra("data");
		return bm;
	}

	private void cropCameraPhoto() {
		Uri data = Uri.fromFile(currCameraPhotoFile);
		Intent intent = getSystemCropIntent(data);
		activity.startActivityForResult(intent, PHOTO_CAMERA_CROP_TWO);
	}
	
	private void cropLocalPhoto(Intent data){
		String imgPath = getImgPath(data);
		Uri uri = Uri.fromFile(new File(imgPath));
		Intent intent = getSystemCropIntent(uri);
		activity.startActivityForResult(intent, PHOTO_CAMERA_CROP_TWO);
	}

	private Bitmap getLocalSource(Intent data) {
		String imgPath = getImgPath(data);
		Bitmap bm = BitmapDecoder.decodeSampledBitmapFromFile(imgPath, outWidth, outHeight);
		return bm;
	}
	
	private String getImgPath(Intent data){
		Uri picSource = data.getData();
		// 列表
		String[] picList = { MediaColumns.DATA };

		Cursor imgData = activity.getContentResolver().query(picSource,
				picList, null, null, null);
		imgData.moveToFirst();
		String imgPath = imgData.getString(0);
		imgData.close();
		return imgPath;
	}

	private Bitmap getCameraSource() {
		Bitmap bm = BitmapDecoder.decodeSampledBitmapFromFile(currCameraPhotoFile.getPath(), outWidth, outHeight);
		return bm;
	}

	public void setOutWidth(int outWidth) {
		this.outWidth = outWidth;
	}

	public void setOutHeight(int outHeight) {
		this.outHeight = outHeight;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (Activity.RESULT_OK == resultCode) {
				Bitmap bm = null;
				switch (requestCode) {
				case PHOTO_LOCAL:
					bm = getLocalSource(data);
					startRotaleActivity(bm);
					
//					handler.sendMessage(handler.obtainMessage(PHOTO_LOCAL, bm));
					break;
				case PHOTO_LOCAL_CROP:
					// 获取本地图片接收
					cropLocalPhoto(data);
//					bm = getLocalSource(data);
//					startRotaleActivity(bm);
//					handler.sendMessage(handler.obtainMessage(PHOTO_LOCAL_CROP, bm));
					break;
				case PHOTO_CAMERA:
					bm = getCameraSource();
					startRotaleActivity(bm);
//					handler.sendMessage(handler.obtainMessage(PHOTO_CAMERA, bm));
					break;
				case PHOTO_CAMERA_CROP:
					cropCameraPhoto();
					break;
				case PHOTO_CAMERA_CROP_TWO:
					bm = getDataSource(data);
					startRotaleActivity(bm);
//					handler.sendMessage(handler
//							.obtainMessage(PHOTO_CAMERA_CROP, bm));
					break;
				
				case PHOTO_ROTALE:
					bm = application.getBitmap();
					handler.sendMessage(handler
							.obtainMessage(PHOTO_ROTALE, bm));
					break;
				default:
					break;
				}
			}else {
//				handler.sendMessage(handler
//						.obtainMessage(requestCode, null));
			}
		} catch (Exception e) {
//			handler.sendMessage(handler
//					.obtainMessage(requestCode, null));
		}
		

	}
	
	private void startRotaleActivity(Bitmap bm){
		application.setBitmap(bm);
		Intent intent = new Intent(this.activity, RotateActivity.class);
		this.activity.startActivityForResult(intent, PHOTO_ROTALE);
	}
	
	public int getOutputX() {
		return outputX;
	}

	public int getOutputY() {
		return outputY;
	}

	public void setOutputX(int outputX) {
		this.outputX = outputX;
	}

	public void setOutputY(int outputY) {
		this.outputY = outputY;
	}

	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";

	}

	public void setOnBitmapListener(OnBitmapListener onBitmapListener) {
		this.onBitmapListener = onBitmapListener;
	}

	public interface OnBitmapListener {
		public void getToBitmap(int bimType, Bitmap bm);
	}
}
