package com.peiban.app.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.peiban.R;

public class RotateActivity extends BaseActivity implements OnClickListener{
	private ImageView imgView;
	private Button btnRotale, btnSubmit;
	private Bitmap bitmap;
	private Matrix matrix = new Matrix();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.rotale);
		bitmap = getShangwupanlvApplication().getBitmap();
		if(bitmap == null)
		{
			finish();
			return;
		}
		
		initWidget();
		initViewListener();
		imgView.setImageBitmap(bitmap);
	}


	private void initViewListener() {
		btnRotale.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
	}


	private void initWidget() {
//		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.d1);
		imgView = (ImageView) this.findViewById(R.id.img);
		btnRotale = (Button) this.findViewById(R.id.btn_rotate);
		btnSubmit = (Button) this.findViewById(R.id.btn_submit_rotate);
		matrix.setRotate(90);
	}


	@Override
	protected void initTitle() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_rotate:
			rotale();
			break;
		case R.id.btn_submit_rotate:
			submit();
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK == keyCode){
			submit();
		}
		return super.onKeyDown(keyCode, event);
	}


	/** 确定 */
	private void submit(){
		getShangwupanlvApplication().setBitmap(bitmap);
		setResult(Activity.RESULT_OK);
		finish();
	}
	
	/** 旋转 */
	private void rotale(){
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		imgView.setImageBitmap(bitmap);
	}

}
