package com.peiban.app.ui;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.peiban.R;
import com.peiban.app.action.ImageInfoAction;
import com.peiban.app.control.DialogImageSelect;
import com.peiban.command.FileTool;

public class NewProjectAlbumSelectClick implements View.OnClickListener {
		private ImageInfoAction imageInfoAction;
		private Context context;
		public NewProjectAlbumSelectClick(Context context,ImageInfoAction action)
		{
			this.imageInfoAction = action;
			this.context=context;
		}
		@Override
		public void onClick(View v) {

				if (!FileTool.isMounted()) {
					Toast.makeText(
							this.context,
							this.context.getResources().getString(
									R.string.toast_sdcard_mounted),
							Toast.LENGTH_SHORT).show();
				} else if (!FileTool.isSDCardAvailable()) {
					Toast.makeText(
							this.context,
							this.context.getResources().getString(
									R.string.toast_sdcard_available),
							Toast.LENGTH_SHORT).show();
				} else {
					final DialogImageSelect dialogImageSelect=new DialogImageSelect(this.context);
					dialogImageSelect.show();
					//选择本地相册图片
					dialogImageSelect.getBtnLocalImage().setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialogImageSelect.dismiss();
							//selectPhoto();
							imageInfoAction.getLocolPhoto();
						}
					});
					//选择本地相机
					dialogImageSelect.getBtnCamera().setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialogImageSelect.dismiss();
							//cameraPhoto();
							imageInfoAction.getCameraPhoto();
						}
					});
				}

			
		}

	
	
}
