package com.peiban.app.ui;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalDb;
import android.app.NotificationManager;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.peiban.R;
import com.peiban.app.FinalFactory;
import com.peiban.app.LocationShared;
import com.peiban.app.MainBroadcastReceiver;
import com.peiban.app.PromptDialog;
import com.peiban.app.control.FriendListAction;
import com.peiban.application.PeibanApplication;
import com.peiban.service.SnsService;
import com.peiban.service.receiver.NotifyChatMessage;
import com.peiban.service.receiver.NotifySystemMessage;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.NotifiyVo;
import com.peiban.vo.SessionList;
import com.peiban.vo.UserInfoVo;


public class IndexTabActivity extends TabActivity{
	/** 通过广播属性列表 */
	public static final String ACTION_REFRESH_SESSION = "intent.action.ACTION_REFRESH_SESSION";
	
	/** 通过广播刷新通知 */
	public static final String ACTION_REFRESH_NOTIFIY = "intent.action.ACTION_REFRESH_NOTIFIY";
	/** 切换到首页 */
	public static final String ACTION_CALLBACK = "intent.action.ACTION_CALLBACK";
	/** 退出 */
	public static final String ACTION_EXIT_PEIBAN = "intent.action.ACTION_EXIT_PEIBAN";
	/** 刷新好友列表 */
	public static final String ACTION_REFRESH_FRIEND = "intent.action.ACTION_REFRESH_FRIEND";
	/** 注销 */
	public static final String ACTION_LOGIN_OUT = "intent.action.ACTION_LOGIN_OUT";
	
	
	private TabHost mTabHost;
	private FriendListAction friendListAction;
	private String[] tabTags = new String[]{"about", "chat", "friend","notation","grzx"};
	private String[] tabDescs = new String[]{"附近", "会话", "好友","通知","个人中心"};
	private int[] tabImgIds = new int[]{R.drawable.tab_item_about, R.drawable.tab_item_chat, R.drawable.tab_item_friend,R.drawable.tab_item_notation,R.drawable.tab_item_grzx};
	
	private String currTag = tabTags[0];
	private static IndexTabActivity instance;
	private UserInfoVo userInfoVo;
	
	private MainBroadcastReceiver receiver;
	
	private Handler handler = new MyHandler();
	
	private PromptDialog promptDialog;
	
	private TextView sessionPrompt;			// 会话提示框
	private TextView notifyPrompt;			// 通知提示框
	
	private FinalDb finalDb;
	private PeibanApplication application;
	
	private StartServiceTask serviceTask;
	private Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.index_tab_mini);
		application = (PeibanApplication) getApplication();
		receiver = new MainBroadcastReceiver(getBaseContext(), handler);
		registerRecevier();
		userInfoVo = application.getUserInfoVo();
		if(userInfoVo == null){
			Toast.makeText(getBaseContext(), "内存不足!", Toast.LENGTH_SHORT).show();
			finish();
			System.exit(0);
			return;
		}
		finalDb = FinalFactory.createFinalDb(getBaseContext(), userInfoVo);
		friendListAction = new FriendListAction(getBaseContext(), userInfoVo, null);
		application.setFriendListAction(friendListAction);
		initIndexInfo();
		initWidget();
		getTab();
		
		checkIntent(getIntent());
		serviceTask = new StartServiceTask(IndexTabActivity.this);
		timer = new Timer("启动服务.");
		timer.scheduleAtFixedRate(serviceTask, 0, 5000);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
//		instance = this;
		checkIntent(intent);
	}




	private void checkIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			String tag = bundle.getString("tag");
			if(!TextUtils.isEmpty(tag)){
				currTag = tag;
				mTabHost.setCurrentTabByTag(currTag);
			}
		}
		
	}

	private void initIndexInfo() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				if(friendListAction.isLocalFriendInfo()){
					friendListAction.getFriendList();
				};
				return null;
			}
		}.execute();
	}

//	public static IndexTabActivity getInstance(){
//		return instance;
//	}
	
	private void initWidget() {
		mTabHost = getTabHost();
		startService(new Intent(getBaseContext(), SnsService.class));
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(KeyEvent.KEYCODE_BACK == keyCode)
		{
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	// 退出程序提示
	void pointExit()
	{
		if(promptDialog == null)
		{
			promptDialog = new PromptDialog(IndexTabActivity.this);
			promptDialog.setMessage("是否要退出陪伴应用！");
			promptDialog.addCannel();
			promptDialog.addConfirm(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
					sendBroadcast(new Intent(BaseActivity.ACTION_EXIT));
					promptDialog.cancel();
				}
			});
		}
		
		promptDialog.show();
	}
	
	
	void callbackLocation(){
		currTag = tabTags[0];
		mTabHost.setCurrentTabByTag(currTag);
	}
	
	/**
	 * 添加tabhost菜单
	 */
	
	private void getTab(){
		Intent[] tabContents = new Intent[]{
				new Intent(this,  LocationActivity.class),
				new Intent(this,  SessionActivity.class),
				new Intent(this,  FriendListActivity.class),
				new Intent(this,  NotifyActivity.class),
				new Intent(this,  IndividualCenterActivity.class)
		};
		
		for (int i = 0; i < tabTags.length; i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.tab_mini, null);
			ImageView img = (ImageView) view.findViewById(R.id.tab_img);
			
			if(1 == i){
				sessionPrompt = (TextView) view.findViewById(R.id.txt_subscript);
			}
			
			if(3 == i){
				notifyPrompt = (TextView) view.findViewById(R.id.txt_subscript);
			}
			
			img.setBackgroundResource(tabImgIds[i]);
			TextView desc = (TextView) view.findViewById(R.id.tab_desc);
			desc.setText(tabDescs[i]);
			mTabHost.addTab(mTabHost.newTabSpec(tabTags[i])
					.setIndicator(view).setContent(tabContents[i]))
			;
		}
		
		sessionPromptUpdate();
		notifyPromptUpdate();
		
		mTabHost.setCurrentTabByTag(currTag);
	}
	
	/**
	 * 
	 * @author fighter <br />
	 * 创建时间:2013-6-19<br />
	 * 修改时间:<br />
	 */
	public void sessionPromptUpdate(){
		try {
			List<SessionList> vos = finalDb.findAllByWhere(SessionList.class, "notReadNum != 0 and notReadNum != '0'");
			if(vos == null || vos.isEmpty()){
				sessionPrompt.setVisibility(View.GONE);
			}else{
				int num = 0;
				for (SessionList session : vos) {
					num = num + session.getNotReadNum();
				}
				if(0 == num){
					sessionPrompt.setVisibility(View.GONE);
				}else{
					sessionPrompt.setVisibility(View.VISIBLE);
					sessionPrompt.setText("" + num);
				}
			}
			
			
		} catch (Exception e) {
			sessionPrompt.setVisibility(View.GONE);
		}
	}
	
	public void notifyPromptUpdate(){
		try {
			List<NotifiyVo> vos = finalDb.findAllByWhere(NotifiyVo.class, "state = '" + NotifiyVo.STATE_NO_FINISH + "'");
			if(vos == null || vos.isEmpty()){
				notifyPrompt.setVisibility(View.GONE);
			}else{
				int num = vos.size();
				notifyPrompt.setVisibility(View.VISIBLE);
				notifyPrompt.setText("" + num);
			}
		} catch (Exception e) {
			notifyPrompt.setVisibility(View.GONE);
		}
	}
	
	private void registerRecevier(){
		IntentFilter filter = new IntentFilter(NotifyChatMessage.ACTION_NOTIFY_CHAT_MESSAGE);
		filter.addAction(NotifySystemMessage.ACTION_NOTIFY_SYSTEM_MESSAGE);
		registerReceiver(receiver, filter);
		
		filter = new IntentFilter();
		filter.addAction(ACTION_REFRESH_NOTIFIY);
		filter.addAction(ACTION_REFRESH_SESSION);
		filter.addAction(ACTION_CALLBACK);
		filter.addAction(ACTION_EXIT_PEIBAN);
		filter.addAction(ACTION_REFRESH_FRIEND);
		filter.addAction(ACTION_LOGIN_OUT);
		filter.addAction(NotifySystemMessage.ACTION_VIP_STATE);
		registerReceiver(refreshUiReceiver, filter);
	}
	private void unregisterRecevier(){
		unregisterReceiver(receiver);
		unregisterReceiver(refreshUiReceiver);
	}
	
	@Override
	protected void onDestroy(){
		try {
			timer.cancel();
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancelAll();
		} catch (Exception e) {
		}
		super.onDestroy();
		unregisterRecevier();
		FinalFactory.createFinalBitmap(getBaseContext()).clearMemoryCache();
		LocationShared locationShared= LocationShared.getInstance(IndexTabActivity.this);
		locationShared.setLocationaddr("");
		locationShared.setLocationlat("");
		locationShared.setLocationlon("");
		locationShared.commitLoc();
	}
	
	/**
	 * @return the friendListAction
	 */
	public FriendListAction getFriendListAction() {
		return friendListAction;
	}
	
	class MyHandler extends Handler{
		
	}

	private final class StartServiceTask extends TimerTask{
		private Context context;
		StartServiceTask(Context context){
			this.context = context;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getBaseContext(), SnsService.class);
			this.context.startService(intent);
		}
	}
	
	/** 使用广播通知进行修改UI */
	private BroadcastReceiver refreshUiReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if(ACTION_REFRESH_SESSION.equals(action)){
				sessionPromptUpdate();
			}else if(ACTION_REFRESH_NOTIFIY.equals(action)){
				notifyPromptUpdate();
			}else if(ACTION_CALLBACK.equals(action)){
				callbackLocation();
			}else if(ACTION_REFRESH_FRIEND.equals(action)){
				getFriendListAction().pushFriendList();
			}else if(ACTION_EXIT_PEIBAN.equals(action)){
				pointExit();
			}else if(ACTION_LOGIN_OUT.equals(action)){
				try {
					timer.cancel();
				} catch (Exception e) {
				}
			}else if(NotifySystemMessage.ACTION_VIP_STATE.equals(action)){
				CustomerVo customerVo = (CustomerVo) intent.getSerializableExtra(NotifySystemMessage.EXTRAS_VIP);
				application.setCustomerVo(customerVo);
			}
		}
	};
}
