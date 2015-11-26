package com.peiban.app.ui.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;

import com.peiban.SharedStorage;
import com.peiban.app.ui.BaseActivity;

/**
 * 
 * 功能： 聊天提示 <br />
 * 日期：2013-7-9<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class ChatPrompt {
	public static final String KEY = "chat_prompt";
	/**
	 * 显示
	 * @param activity
	 * @param l
	 * @author fighter <br />
	 * 创建时间:2013-7-9<br />
	 * 修改时间:<br />
	 */
	public static final void showPrompt(BaseActivity activity, ChatPromptLisenter l){
		activity.getPromptDialog().addConfirm(l);
		activity.getPromptDialog().setConfirmText("确定");
		activity.getPromptDialog().setMessage("请不要发送色情或其他不良信息,一经发现,其帐号将会被删除.");
		activity.getPromptDialog().removeCannel();

		activity.getPromptDialog().show();
	}
	
//	public static final boolean isShow(Context context){
//		SharedPreferences sharedPreferences = SharedStorage.getConfigSharedPreferences(context);
//		boolean isShow = sharedPreferences.getBoolean(KEY, false);
//		return isShow;
//	}
	
	public static class ChatPromptLisenter implements View.OnClickListener{
		private BaseActivity activity;
		
		public ChatPromptLisenter(BaseActivity activity) {
			super();
			this.activity = activity;
		}

		@Override
		public void onClick(View v) {
			this.activity.getPromptDialog().cancel();
//			SharedPreferences sharedPreferences = SharedStorage.getConfigSharedPreferences(activity);
//			Editor editor = sharedPreferences.edit();
//			editor.putBoolean(KEY, true);
//			editor.commit();
		}
		
	}
}
