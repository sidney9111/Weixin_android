package com.peiban.app.ui;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.peiban.R;
import com.peiban.app.FinalFactory;
import com.peiban.app.PromptDialog;
import com.peiban.app.api.ErrorCode;
import com.peiban.application.PeibanApplication;
import com.peiban.command.FileTool;
import com.peiban.command.NetworkUtils;
import com.peiban.service.type.XmppTypeManager;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.UserInfoVo;

public abstract class BaseActivity extends FinalActivity{
	public static final String ACTION_EXIT = "com.intent.ACTION_EXIT";
	protected static final boolean DEBUG = true;
	private View viewTitle;
	
	private Button btnTitleLeft;
	private Button btnTitleRight;
	private TextView txtTitle;
	private TextView txtTitleNum;
	private Button btnTitleSelect;
	private Button btnTitleBack;
	private ImageView imgTitle;  
	private ProgressDialog waitDialog;
	
	private ProgressBar waitBar;
	
	private PromptDialog promptDialog;
	
	private UserInfoVo userInfoVo;
	
	private FinalHttp finalHttp = new FinalHttp();
	private FinalDb finalDb;
	
	private View.OnClickListener titleOnClickListener;
	
	private NotificationManager notificationManager;
	
	private PeibanApplication application;
	
	private BaseBroadcast recever;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		waitDialog = new ProgressDialog(BaseActivity.this);
		promptDialog = new PromptDialog(BaseActivity.this);
		
		application = (PeibanApplication) getApplication();
		registerReceiver();
	}
	
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver();
	}
	
	protected void baseInit() {
		initTitleWidget();
		initTitle();
		
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		setOnClick();
	}
	
	private void setOnClick() {
		if(txtTitle == null){
			return;
		}
		titleOnClickListener = new TitleOnClickLisener();
		btnTitleBack.setOnClickListener(titleOnClickListener);
		btnTitleRight.setOnClickListener(titleOnClickListener);
		btnTitleLeft.setOnClickListener(titleOnClickListener);
		btnTitleSelect.setOnClickListener(titleOnClickListener);
		txtTitle.setOnClickListener(titleOnClickListener);
	}

	protected abstract void initTitle();
	
	protected void initTitleWidget() {
		viewTitle = findViewById(R.id.nav_title);
		btnTitleLeft = (Button) findViewById(R.id.title_left);
		btnTitleRight = (Button) findViewById(R.id.title_right);
		btnTitleSelect = (Button) findViewById(R.id.title_select);
		txtTitle = (TextView) findViewById(R.id.title);
		txtTitleNum = (TextView) findViewById(R.id.title_num);
		btnTitleBack = (Button) findViewById(R.id.title_back);
		imgTitle=(ImageView)findViewById(R.id.title_img);
		waitBar = (ProgressBar) findViewById(R.id.progressBar);
	}
	
	/**
	 * 添加返回按钮
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-14<br />
	 * 修改时间:<br />
	 */
	public void setBtnBack(){
		btnTitleBack.setVisibility(View.VISIBLE);
	}

	public void hideSoftKeyboard(){
		hideSoftKeyboard(getCurrentFocus());
	}
	public void hideSoftKeyboard(View view){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if(view != null){
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	
	public void hideTitle(){
		txtTitle.setVisibility(View.GONE);
	}
	
	public void showTitle(){
		txtTitle.setVisibility(View.VISIBLE);
	}
	
	public void setTitleBack(String str){
		setViewText(btnTitleBack, str);
	}
	
	public void setTitleLeft(int id){
		String content = getResources().getString(id);
		setTitleLeft(content);
	}
	public void setTitleLeft(String str){
		setViewText(btnTitleLeft, str);
	}
	
	public void setTitleRight(int id){
		String content = getResources().getString(id);
		setTitleRight(content);
	}
	
	public void setTitleRight(String str){
		setViewText(btnTitleRight, str);
	}
	
	public void setTitleContent(int id){
		String content = getResources().getString(id);
		setTitleContent(content);
	}
	
	public void setTitleContent(String str){
		setViewText(txtTitle, str);
	}
	
	public void setTitleNum(String str){
		setViewText(txtTitleNum, str);
	}
	public void setTitleImg(int id){
		imgTitle.setVisibility(View.VISIBLE);
		imgTitle.setBackgroundResource(id);
	}
	private void setViewText(TextView textView, String str){
		textView.setVisibility(View.VISIBLE);
		textView.setText(str + "");
	}
	
	/**
	 * 检查是否有网络
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-30<br />
	 * 修改时间:<br />
	 */
	public boolean checkNetWork(){
		if(!NetworkUtils.isnetWorkAvilable(getBaseContext())){
			showToast(getResources().getString(R.string.toast_network));
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 检查是否有网络并且本地磁盘是否可以用
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-30<br />
	 * 修改时间:<br />
	 */
	public boolean checkNetWorkOrSdcard(){
		if(!FileTool.isMounted()){
			showToast(getResources().getString(R.string.toast_sdcard_mounted));
			return false;
		}else if(!FileTool.isSDCardAvailable()){
			showToast(getResources().getString(R.string.toast_sdcard_available));
			return false;
		}else {
			return checkNetWork();
		}
	}
	
	public boolean checkSdcard(){
		if(!FileTool.isMounted()){
			showToast(getResources().getString(R.string.toast_sdcard_mounted));
			return false;
		}else if(!FileTool.isSDCardAvailable()){
			showToast(getResources().getString(R.string.toast_sdcard_available));
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 标题左边按钮
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-9<br />
	 * 修改时间:<br />
	 */
	public Button getBtnTitleLeft() {
		return btnTitleLeft;
	}
	
	/**
	 * 标题右边的按钮
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-9<br />
	 * 修改时间:<br />
	 */
	public Button getBtnTitleRight() {
		return btnTitleRight;
	}
	
	/**
	 * 标题的内容
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-9<br />
	 * 修改时间:<br />
	 */
	public TextView getTxtTitle() {
		return txtTitle;
	}
	public FinalBitmap getPhotoBitmap() {
		return FinalFactory.createFinalAlbumBitmap(getBaseContext());
	}

	/**
	 * 标题中列表中的显示条数
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-9<br />
	 * 修改时间:<br />
	 */
	public TextView getTxtTitleNum() {
		return txtTitleNum;
	}
	
	/**
	 * 标题栏中需要使用的选择按钮
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-9<br />
	 * 修改时间:<br />
	 */
	public Button getBtnTitleSelect() {
		return btnTitleSelect;
	}
	
	protected void titleBtnRight(){}
	
	protected void titleBtnLeft() {
		
	}
	
	protected void titleOnClick() {
		
	}
	
	public View getViewTitle() {
		return viewTitle;
	}

	public FinalBitmap getHeadBitmap() {
		return FinalFactory.createFinalBitmap(getBaseContext());
	}

	public ProgressBar getWaitBar() {
		return waitBar;
	}

	protected void titleBtnSelect() {
		
	}
	
	protected void titleBtnBack() {
		finish();
	}
	
	public NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public XmppTypeManager getXmppTypeManager() {
		return new XmppTypeManager(getBaseContext());
	}

	public FinalHttp getFinalHttp() {
		return finalHttp;
	}

	public FinalDb getFinalDb() {
		if(finalDb == null){
			finalDb = FinalFactory.createFinalDb(getBaseContext(), getUserInfoVo());
			if(finalDb == null){
				showToast("该系统不允许操作Sqlit");
				System.exit(0);
			}
		}
		return finalDb;
	}

	public UserInfoVo getUserInfoVo() {
		if(userInfoVo == null){
			userInfoVo = getShangwupanlvApplication().getUserInfoVo();
			
			if(userInfoVo == null){
				showToast("该系统不支持缓存功能！");
				System.exit(0);
			}
		}
		return userInfoVo;
	}

	public CustomerVo getMyCustomerVo() {
		return getShangwupanlvApplication().getCustomerVo();
	}

	public ProgressDialog getWaitDialog() {
		return waitDialog;
	}

	public PromptDialog getPromptDialog() {
		return promptDialog;
	}
	
	public void showToast(String str){
		Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
	}

	
	public PeibanApplication getShangwupanlvApplication(){
		return application;
	}
	class TitleOnClickLisener implements OnClickListener{

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.title_back:
				titleBtnBack();
				break;
			case R.id.title_left:
				titleBtnLeft();
				break;
			case R.id.title_right:
				titleBtnRight();
				break;
			case R.id.title_select:
				titleBtnSelect();
				break;
			case R.id.title:
				titleOnClick();
				break;

			default:
				break;
			}
		}
		
	}
	
	class AddFriendCallback extends AjaxCallBack<String>{
		@Override
		public void onStart() {
			super.onStart();
			getWaitDialog().setMessage("提交请求");
			getWaitDialog().show();
		}

		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			String data = ErrorCode.getData(getBaseContext(), t);
			if (data != null) {
				getWaitDialog().setMessage("提交成功");
			}

			getWaitDialog().cancel();
		}

		@Override
		public void onFailure(Throwable t, String strMsg) {
			super.onFailure(t, strMsg);
			getWaitDialog().cancel();
			showToast("服务器失败响应失败!");
		}

	}
	
	/**
	 * 
	 * 功能：申请查看回调<br />
	 * 日期：2013-6-14<br />
	 * 地点：淘高手网<br />
	 * 版本：ver 1.0<br />
	 * 
	 * @author fighter
	 * @since
	 */
	class ApplyAuthCallback extends AjaxCallBack<String>{
		@Override
		public void onStart() {
			super.onStart();
			getWaitDialog().setMessage("提交请求");
			getWaitDialog().show();
		}

		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			String data = ErrorCode.getData(getBaseContext(), t);
			if (data != null) {
				if("1".equals(data)){
					showToast("申请成功！");
				}else{
					showToast("申请失败！");
				}
			}

			getWaitDialog().cancel();
		}

		@Override
		public void onFailure(Throwable t, String strMsg) {
			super.onFailure(t, strMsg);
			getWaitDialog().cancel();
			showToast("服务器失败响应失败!");
		}
	}
	
	private void registerReceiver(){
		if (recever == null) {
			recever = new BaseBroadcast();
		}
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_EXIT);
		
		registerReceiver(recever, filter);
	}
	
	private void unregisterReceiver(){
		unregisterReceiver(recever);
	}
	
	class BaseBroadcast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(ACTION_EXIT.equals(intent.getAction())){
				finish();
			}
		}
		
	}
}
