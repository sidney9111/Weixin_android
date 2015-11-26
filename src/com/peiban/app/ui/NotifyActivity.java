package com.peiban.app.ui;

import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.peiban.R;
import com.peiban.adapter.ObjectBaseAdapter;
import com.peiban.app.Constants;
import com.peiban.app.FinalFactory;
import com.peiban.app.SystemNotifiy;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.FriendApi;
import com.peiban.app.api.UserInfoApi;
import com.peiban.command.TextdescTool;
import com.peiban.service.receiver.NotifySystemMessage;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.NotifiyType;
import com.peiban.vo.NotifiyVo;
import com.peiban.vo.UserInfoVo;

/**
 * 
 * 功能：通知 <br />
 * 日期：2013-5-17<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class NotifyActivity extends BaseActivity {
	private static final String EDIT = "编辑";
	private static final String END = "完成";
	@ViewInject(id = R.id.notify_list_lv)
	private ListView listMsgView;

	private MyAdapter adapter;
	private BroadcastReceiver receiver;
	
	private UserInfoVo userInfoVo;
	private Handler handler = new Handler();
	private FinalDb finalDb;
	private UserInfoApi mUserInfoApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify);
		baseInit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(KeyEvent.KEYCODE_BACK == keyCode)
		{
			try {
				sendBroadcast(new Intent(IndexTabActivity.ACTION_CALLBACK));
				return false;
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void baseInit() {
		super.baseInit();
		receiver = new MyBroadcast();
		registerReceiver();
		userInfoVo = getUserInfoVo();
		finalDb = FinalFactory.createFinalDb(getBaseContext(), userInfoVo);
		adapter = new MyAdapter();
		listMsgView.setAdapter(adapter);
		listMsgView.setOnItemClickListener(new NotifiyOnClick());
//		listMsgView.setOnItemLongClickListener(new NotifiyOnLongClick());
		initAdaperView();
	}

	@Override
	protected void initTitle() {
		setTitleContent(R.string.notify_title);
		setTitleRight(EDIT);
	}
	
	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		String action = getBtnTitleRight().getText().toString();

		if (EDIT.equals(action)) {
			setTitleRight(END);
			editSessionList();
		} else {
			setTitleRight(EDIT);
			endEdit();
		}
	}

	private void initAdaperView() {
		removeNotify();
		List<NotifiyVo> notifiyVos = finalDb.findAllDESC(NotifiyVo.class, "id");
		if(notifiyVos != null){
			adapter.addList(notifiyVos);
		}
	}
	
	private void registerReceiver(){
		IntentFilter filter = new IntentFilter(NotifySystemMessage.ACTION_NOTIFY_SYSTEM_MESSAGE);
		registerReceiver(receiver, filter);
	}
	
	private void unregisterReceiver(){
		unregisterReceiver(receiver);
	}
	
	// 编辑
	private void editSessionList() {
		adapter.edit();
	}

	private void endEdit() {
		adapter.end();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver();
	}
	
	/** 通知面板更新通知页面 */
	void notifiyIndexUpdateNotify(){
		adapter.notifyDataSetInvalidated();
		sendBroadcast(new Intent(IndexTabActivity.ACTION_REFRESH_NOTIFIY));
	}
	
	void modifyNotifiyItem(NotifiyVo notifiyVo){
		try {
			finalDb.update(notifiyVo);
		} catch (Exception e) {
		}
		notifiyIndexUpdateNotify();
	}
	
	void delNotifiyItem(NotifiyVo notifiyVo){
		adapter.removeObject(notifiyVo);
		
		try {
			finalDb.delete(notifiyVo);
		} catch (Exception e) {
		}
		
		try {
//			IndexTabActivity.getInstance().notifyPromptUpdate();
			notifiyIndexUpdateNotify();
		} catch (Exception e) {
		}
	}
	
	class NotifiyOnLongClick implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			final NotifiyVo notifiyVo = adapter.getItem(position);
			getPromptDialog().addCannel();
			getPromptDialog().setMessage("是否要删除该条通知!");
			getPromptDialog().setConfirmText("删除");
			getPromptDialog().addConfirm(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					delNotifiyItem(notifiyVo);
					getPromptDialog().cancel();
				}
			});
			getPromptDialog().show();
			return true;
		}
	}
	
	private View.OnClickListener delNotifiy = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			NotifiyVo notifiyVo = (NotifiyVo) v.getTag();
			if(notifiyVo != null){
				delNotifiyItem(notifiyVo);
			}
		}
		
	};
	
	class NotifiyOnClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			NotifiyVo notifiyVo = adapter.getItem(position);
			getNotificationManager().cancel(SystemNotifiy.NOTION_ID);
			int type = notifiyVo.getType();
			String sent = notifiyVo.getSent();
			String name = "";
			if(!TextUtils.isEmpty(sent)){
				try {
					JSONObject jsonObject = JSONObject.parseObject(sent);
					name = jsonObject.getString("name");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if(NotifiyType.SYSTEM_MSG != type && NotifiyVo.STATE_FINISH.equals(notifiyVo.getState())){
				return;
			}
			
			switch (type) {
			case NotifiyType.APPLY_DATA:
				showApplyData(notifiyVo);
				break;
			case NotifiyType.SYSTEM_MSG:
				showSystem(notifiyVo);
				break;
			case NotifiyType.BE_FRIEND:
				showAddFriend(notifiyVo);
				break;
			case NotifiyType.COMMENTED:
				showCommented(name);
				break;
			case NotifiyType.SEE_DATA:
				showSeeData(name);
				break;
			case NotifiyType.DEL_FRIEND:
				showDelFriend(name);
				break;
			case NotifiyType.ADDFRIENDED:
				showAddFriended(name);
				break;
			case NotifiyType.RESFUEFRIENDED:
				showRefuseFriended(name);
				break;
			case NotifiyType.HEAD_AUTH_SUCCESS:
				showSystem(notifiyVo);
				break;
			case NotifiyType.RESFUEF_DATE:
				showResfuefData(name);
				break;

			default:
				break;
			}
			
			if(NotifiyType.SYSTEM_MSG == type){
				return;
			}
			
//			delNotifiyItem(notifiyVo);
			notifiyVo.setState(NotifiyVo.STATE_FINISH);
			modifyNotifiyItem(notifiyVo);
		}
		
		void showCommented(String name){
			show(name + "评论了你!");
		}
		
		void showSeeData(String name){
			show(name + NotifiyType.APPLY_SEE_SUCCESS);
		}
		void showResfuefData(String name){
			show(name + NotifiyType.RESFUEDATA);
		}
		
		void showApplyData(final NotifiyVo notifiyVo){
			final CustomerVo customerVo = JSON.toJavaObject(JSON.parseObject(notifiyVo.getSent()), CustomerVo.class);
			// 取消
			getPromptDialog().addCannel(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!checkNetWork()){
						return;
					}
					if (mUserInfoApi == null) {
						mUserInfoApi = new UserInfoApi();
					}
					getPromptDialog().cancel();
//					delNotifiyItem(notifiyVo);
					mUserInfoApi.refApp(userInfoVo.getUid(), customerVo.getUid(), new AjaxCallBack<String>(){});
				}
			});
			getPromptDialog().setMessage("同意" + customerVo.getName() + "查看我的个人信息?");
			getPromptDialog().setConfirmText("同意");
			getPromptDialog().setCannelText("拒绝");
			getPromptDialog().addConfirm(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					getPromptDialog().cancel();
					if (mUserInfoApi == null) {
						mUserInfoApi = new UserInfoApi();
					}
					
					mUserInfoApi.agAuth(getUserInfoVo().getUid(), customerVo.getUid(), new AjaxCallBack<String>() {

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							String data = ErrorCode.getData(t);
							if(data != null){
								if ("1".equals(data)) {
									showToast("授权成功!");
								}else {
									showToast("授权失败!");
								}
							}
						}

						@Override
						public void onFailure(Throwable t, String strMsg) {
							super.onFailure(t, strMsg);
							showToast("服务器未响应!");
						}
					});
				}
				
			});
			getPromptDialog().show();
		}
		
		void showDelFriend(String name){
			show(name + "与你解除了好友关系!");
			try {
//				IndexTabActivity.getInstance().getFriendListAction().pushFriendList();
				notifiyIndexUpdateNotify();
			} catch (Exception e) {
			}
		}
		void showAddFriended(String name){
			show(name + "同意添加你为好友!");
			try {
//				IndexTabActivity.getInstance().getFriendListAction().pushFriendList();
				notifiyIndexUpdateNotify();
			} catch (Exception e) {
			}
		}
		
		void showRefuseFriended(String name){
			show(name + "驳回了你的好友请求!");
		}
		
		void showApplyAuthed(String name){
			show(name + "同意了你的查看资料请求!");
		}
		
		void showAddFriend(final NotifiyVo notifiyVo){
			final CustomerVo customerVo = JSON.toJavaObject(JSON.parseObject(notifiyVo.getSent()), CustomerVo.class);
			// 取消
			getPromptDialog().addCannel(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getPromptDialog().cancel();
					if(!checkNetWork()){
						return;
					}
//					delNotifiyItem(notifiyVo);
					// 取消加好友!
					new FriendApi().refuseFriend(userInfoVo.getUid(), customerVo.getUid(), new AjaxCallBack<String>(){});
				}
			});
			getPromptDialog().setCannelText("拒绝");
			getPromptDialog().setMessage("是否同意与" + customerVo.getName() +"成为好友?");
			getPromptDialog().setConfirmText("同意");
			getPromptDialog().addConfirm(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getPromptDialog().cancel();
					if(!checkNetWork()){
						return;
					}
//					delNotifiyItem(notifiyVo);
					new FriendApi().beFriend(userInfoVo.getUid(), customerVo.getUid(), new AjaxCallBack<String>() {
						@Override
						public void onStart() {
							super.onStart();
							getWaitDialog().setMessage("同意...");
							getWaitDialog().show();
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							getWaitDialog().cancel();
							String data = ErrorCode.getData(t);
							if(data != null){
								if("1".equals(data)){
									beFriendSuccess(customerVo);
								}else{
									beFriended(customerVo);
								}
							}
						}

						@Override
						public void onFailure(Throwable t, String strMsg) {
							super.onFailure(t, strMsg);
							getWaitDialog().cancel();
							showToast(strMsg);
						}
						
						/**
						 * 添加好友成功!
						 * 
						 * 作者:fighter <br />
						 * 创建时间:2013-6-1<br />
						 * 修改时间:<br />
						 */
						void beFriendSuccess(CustomerVo customerVo){
							try {
//								IndexTabActivity.getInstance().getFriendListAction().pushFriendList();
								notifiyIndexUpdateNotify();
							} catch (Exception e) {
								e.printStackTrace();
							}
//							delNotifiyItem(notifiyVo);
							getPromptDialog().setMessage("添加成功,如果好友没有出现在列表中,你可用通过刷新好友列表得到!");
							getPromptDialog().removeCannel();
							getPromptDialog().setConfirmText("确定");
							getPromptDialog().addConfirm(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									getPromptDialog().cancel();
								}
							});
							
							getPromptDialog().show();
						}
						
						/**
						 * 已经是好友!
						 * 
						 * 作者:fighter <br />
						 * 创建时间:2013-6-1<br />
						 * 修改时间:<br />
						 */
						void beFriended(CustomerVo customerVo){
//							delNotifiyItem(notifiyVo);
							getPromptDialog().setMessage("已经是好友不用在添加!");
							getPromptDialog().removeCannel();
							getPromptDialog().setConfirmText("确定");
							getPromptDialog().addConfirm(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									getPromptDialog().cancel();
								}
							});
							
							getPromptDialog().show();
						}
					});
					
					
					
				}
			});
			getPromptDialog().show();
		}
		
		void showSystem(NotifiyVo notifiyVo){
			Intent intent = new Intent(NotifyActivity.this, SystemInfoShowActivity.class);
			intent.putExtra("data", notifiyVo);
			
			startActivity(intent);
			
//			delNotifiyItem(notifiyVo);
			notifiyVo.setState(NotifiyVo.STATE_FINISH);
			modifyNotifiyItem(notifiyVo);
		}
		
		void show(String msg){
			getPromptDialog().setMessage(msg);
			getPromptDialog().removeCannel();
			getPromptDialog().setConfirmText("确定");
			getPromptDialog().addConfirm(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getPromptDialog().cancel();
				}
			});
			
			getPromptDialog().show();
		}
	}
	
	class MyAdapter extends ObjectBaseAdapter<NotifiyVo> {
		private boolean isEdit = false; // 当前是否是编辑的状态
		
		/**
		 * 编辑
		 * 
		 * 作者:fighter <br />
		 * 创建时间:2013-6-9<br />
		 * 修改时间:<br />
		 */
		public void edit() {
			isEdit = true;
			notifyDataSetInvalidated();
		}

		/** 完成编辑 */
		public void end() {
			isEdit = false;
			notifyDataSetInvalidated();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			if (convertView != null) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(getBaseContext()).inflate(
						R.layout.notifiy_list_item, null);
				viewHolder = ViewHolder.getInstance(convertView);
			}

			convertView.setTag(viewHolder);
			bindView(viewHolder, position);
			return convertView;
		}

		private void bindView(ViewHolder viewHolder, int position) {
			NotifiyVo notifiyVo = getItem(position);

			if (isEdit) {
				viewHolder.txtTime.setVisibility(View.GONE);
				viewHolder.btnDel.setVisibility(View.VISIBLE);
				viewHolder.btnDel.setOnClickListener(delNotifiy);
				viewHolder.btnDel.setTag(notifiyVo);
			} else {
				viewHolder.btnDel.setVisibility(View.GONE);
				viewHolder.txtTime.setVisibility(View.VISIBLE);
			}
			
			if(NotifiyVo.STATE_NO_FINISH.equals(notifiyVo.getState())){
				viewHolder.imgState.setVisibility(View.VISIBLE);
			}else{
				viewHolder.imgState.setVisibility(View.GONE);
			}
			
			int type = notifiyVo.getType();
			switch (type) {
			case NotifiyType.APPLY_DATA:
				applyDataNotifiy(viewHolder, notifiyVo);
				break;
			case NotifiyType.SYSTEM_MSG:
				systemNotifiy(viewHolder, notifiyVo);
				break;
			case NotifiyType.BE_FRIEND:
				addFriendNotifiy(viewHolder, notifiyVo);
				break;
			case NotifiyType.COMMENTED:
				commentedNotifiy(viewHolder, notifiyVo);
				break;
			case NotifiyType.SEE_DATA:
				seeDataNotifiy(viewHolder, notifiyVo);
				break;
			case NotifiyType.DEL_FRIEND:
				delFriendNotifiy(viewHolder, notifiyVo);
				break;
			case NotifiyType.ADDFRIENDED:
				addFriendedNotifiy(viewHolder, notifiyVo);
				break;
			case NotifiyType.APPLYAUTHED:
				applyAuthedNotifiy(viewHolder, notifiyVo);
				break;
			case NotifiyType.RESFUEF_DATE:
				applyResfuefdateNotifiy(viewHolder, notifiyVo);
				break;
			case NotifiyType.RESFUEFRIENDED:
				applyResuefriendNotifiy(viewHolder, notifiyVo);
				break;
			case NotifiyType.HEAD_AUTH_SUCCESS:
				systemNotifiy(viewHolder, notifiyVo);
				break;

			default:
				break;
			}
		}

		private void applyResuefriendNotifiy(ViewHolder viewHolder,
				NotifiyVo notifiyVo) {
			String msg = NotifiyType.RESFUEFRIEND;
			user(viewHolder, notifiyVo, msg);
			
		}

		private void applyResfuefdateNotifiy(ViewHolder viewHolder,
				NotifiyVo notifiyVo) {
			String msg = NotifiyType.RESFUEDATA;
			user(viewHolder, notifiyVo, msg);
			
		}

		private void commentedNotifiy(ViewHolder viewHolder, NotifiyVo notifiyVo) {
			String msg = "用户对你进行了评价!";
			user(viewHolder, notifiyVo, msg);
		}

		private void systemNotifiy(ViewHolder viewHolder, NotifiyVo notifiyVo) {
			viewHolder.imgHead.setImageResource(R.drawable.head_systemnotifiy);
			viewHolder.txtUsername.setText("系统通知");
			
			viewHolder.txtSign.setText(notifiyVo.getContent());
			viewHolder.txtTime.setText(TextdescTool.timeDifference(notifiyVo.getTime()+"000"));
			
			viewHolder.imgSubscript.setVisibility(View.GONE);
			viewHolder.imgSex.setVisibility(View.GONE);
			viewHolder.txtAge.setVisibility(View.GONE);
			viewHolder.txtOnline.setVisibility(View.GONE);
		}

		private void addFriendNotifiy(ViewHolder viewHolder, NotifiyVo notifiyVo) {
			String msg = NotifiyType.APPLYFRIEND;
			user(viewHolder, notifiyVo, msg);
		}
		private void addFriendedNotifiy(ViewHolder viewHolder, NotifiyVo notifiyVo) {
			String msg = "有新朋友了!";
			user(viewHolder, notifiyVo, msg);
		}

		private void delFriendNotifiy(ViewHolder viewHolder, NotifiyVo notifiyVo) {
			String msg = NotifiyType.DELFRIEND;
			user(viewHolder, notifiyVo, msg);
		}

		private void applyDataNotifiy(ViewHolder viewHolder, NotifiyVo notifiyVo) {
			String msg = NotifiyType.APPLY_SEE;
			user(viewHolder, notifiyVo, msg);
		}

		private void seeDataNotifiy(ViewHolder viewHolder, NotifiyVo notifiyVo) {
			String msg = "同意您的资料查看请求!";
			user(viewHolder, notifiyVo, msg);
		}
		
		private void applyAuthedNotifiy(ViewHolder viewHolder, NotifiyVo notifiyVo) {
			String msg = "同意您的资料查看请求!";
			user(viewHolder, notifiyVo, msg);
		}

		/**
		 * 关于用户的操作
		 * 
		 * @param viewHolder
		 * @param notifiyVo
		 * @param msg
		 *            作者:fighter <br />
		 *            创建时间:2013-6-1<br />
		 *            修改时间:<br />
		 */
		private void user(ViewHolder viewHolder, NotifiyVo notifiyVo, String msg) {
			viewHolder.imgSubscript.setVisibility(View.VISIBLE);
			viewHolder.imgSex.setVisibility(View.VISIBLE);
			viewHolder.txtAge.setVisibility(View.VISIBLE);
			viewHolder.txtOnline.setVisibility(View.VISIBLE);
			
			CustomerVo customerVo = JSON.toJavaObject(
					JSON.parseObject(notifiyVo.getSent()),
					CustomerVo.class);
			if(customerVo == null){
				return;
			}
			String head = customerVo.getHead();
			String name = customerVo.getName();
			String sex = customerVo.getSex();
			String age = TextdescTool.dateToAge(customerVo.getBirthday())
					+ "岁";
			String online = customerVo.getOnline();

			viewHolder.txtAge.setText(age);
			viewHolder.txtUsername.setText(name);
			viewHolder.txtTime.setText(TextdescTool.timeDifference(notifiyVo.getTime() + "000"));
			viewHolder.txtOnline.setText(online);
			viewHolder.txtSign.setText(msg);

			if ("1".equals(sex)) {
				viewHolder.imgSex.setImageResource(R.drawable.sex_man);
				viewHolder.imgHead.setImageResource(R.drawable.head_man);
			} else {
				viewHolder.imgHead.setImageResource(R.drawable.head_woman);
				viewHolder.imgSex.setImageResource(R.drawable.sex_woman);
			}

			if (!TextUtils.isEmpty(head)) {
				FinalFactory.createFinalBitmap(getBaseContext()).display(
						viewHolder.imgHead, head);
			}
			String type = customerVo.getCustomertype();
			if (Constants.CustomerType.CHATTING.equals(type)) {
				if("1".equals(customerVo.getAgent())){
					viewHolder.imgSubscript.setImageResource(R.drawable.subscript_economic);
					viewHolder.imgSubscript.setVisibility(View.VISIBLE);
				}else
				if ("1".equals(customerVo.getHeadattest())) {
					viewHolder.imgSubscript
							.setImageResource(R.drawable.subscript_auth);
					viewHolder.imgSubscript.setVisibility(View.VISIBLE);
				}else{
					viewHolder.imgSubscript.setVisibility(View.GONE);
					
				}
			} else {
				if ("1".equals(customerVo.getVip())) {
					viewHolder.imgSubscript
							.setImageResource(R.drawable.subscript_vip);
					viewHolder.imgSubscript.setVisibility(View.VISIBLE);
				}else{
					viewHolder.imgSubscript.setVisibility(View.GONE);
				}
			}
		}
	}

	static class ViewHolder {
		ImageView imgSubscript, imgHead, imgSex, imgState;

		TextView txtUsername, txtAge, txtOnline, txtSign, txtTime;

		Button btnDel;
		
		public static ViewHolder getInstance(View view) {
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.txtAge = (TextView) view.findViewById(R.id.list_txt_age);
			viewHolder.txtOnline = (TextView) view
					.findViewById(R.id.list_txt_state);
			viewHolder.txtUsername = (TextView) view
					.findViewById(R.id.list_txt_title);
			viewHolder.txtSign = (TextView) view
					.findViewById(R.id.list_txt_message_info);
			viewHolder.txtTime = (TextView) view
					.findViewById(R.id.list_txt_time);

			viewHolder.imgHead = (ImageView) view
					.findViewById(R.id.list_item_img_head);
			viewHolder.imgSex = (ImageView) view
					.findViewById(R.id.list_img_sex);
			viewHolder.imgSubscript = (ImageView) view
					.findViewById(R.id.list_item_img_subscript);
			viewHolder.btnDel = (Button) view
					.findViewById(R.id.list_btn_del);
			viewHolder.imgState = (ImageView) view.findViewById(R.id.list_item_read_state);
			return viewHolder;
		}
	}
	
	void removeNotify(){
		getNotificationManager().cancel(SystemNotifiy.NOTION_ID);
	}
	
	class MyBroadcast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// 得到系统通知
			if(NotifySystemMessage.ACTION_NOTIFY_SYSTEM_MESSAGE.equals(intent.getAction())){
				final NotifiyVo notifiyVo = (NotifiyVo) intent.getSerializableExtra(NotifySystemMessage.EXTRAS_NOTIFY_SYSTEM_MESSAGE);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if(NotifiyType.HEAD_AUTH_SUCCESS == notifiyVo.getType()){
							getShangwupanlvApplication().getCustomerVo().setHeadattest("1");
						}
						adapter.addObject(notifiyVo);
						removeNotify();
					}
				});
			}
		}
		
	}
}
