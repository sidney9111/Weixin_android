package com.peiban.app.ui;

import java.util.List;
import java.util.Map;

import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.FriendApi;
import com.peiban.app.api.UserInfoApi;
import com.peiban.app.control.AlbumControl;
import com.peiban.app.control.DialogMark;
import com.peiban.app.ui.common.ChatPrompt;
import com.peiban.app.ui.common.ChatPrompt.ChatPromptLisenter;
import com.peiban.app.ui.common.FinalOnloadBitmap;
import com.peiban.command.TextdescTool;
import com.peiban.vo.AlbumVo;
import com.peiban.vo.CustomerVo;

/**
 * 
 * 功能： 详细资料 <br />
 * 日期：2013-5-28<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public abstract class DetailActivity extends BaseActivity {
	private static final String TAG = DetailActivity.class.getCanonicalName();
	private static final String FRIEND = "好友可见";
	private static final String VIP_TAG = "VIP/认证可见";
	/** 头像 */
	private ImageView imgHead;
	/** 角标 */
	private ImageView imgSubscript;
	/** 认证提示 */
	private TextView txtAuth;

	/** 认证与 成为 VIP 按钮 */
	private Button btnAuth;
	private Button btnVip;
	
	/** 用户名 */
	private TextView txtUserName;
	/** 性别 */
	private ImageView imgSex;
	/** 年龄 */
	private TextView txtAge;
	/** 在线状态 */
	private TextView txtLineState;
	/** 签名 */
	private TextView txtSign;
	/** 相册 */
	private LinearLayout layoutAlbum;
	private TextView textAlbum;
	/**
	 * 项目列表
	 */
	private LinearLayout layoutProject;
	/** QQ */
	private TextView txtQq;
	/** 身高 */
	private TextView txtHeight;
	/** 体重 */
	private TextView txtBody;
	/** 职业 */
	private TextView txtProfession;
	/** 手机号码 */
	private TextView txtPhone;
	/** 薪资意愿 */
	private TextView txtWages;
	/** 兴趣爱好 */
	private TextView txtHobby;

	private Button btnSayHello;
	private Button btnAddFriend;

	private Button btnApply1;
	private Button btnApply2;

	private LinearLayout mDetailsInfo;
	private LinearLayout mDetailsInfoHide;
	private LinearLayout mApply;

	private CustomerVo customer;

	private UserInfoApi userInfoApi;

	private FriendApi friendApi;

	private DialogMark dialogMark;

	@Override
	protected void initTitle() {
		setBtnBack();
		setTitleContent(R.string.details_title);
	}

	@Override
	protected void baseInit() {
		super.baseInit();
		customer = (CustomerVo) getIntent().getSerializableExtra("data");
		if (customer == null) {
			Toast.makeText(getBaseContext(), "操作错误.", Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}

		dialogMark = new DialogMark(DetailActivity.this, customer) {

			@Override
			protected void markName(String markName) {
				DetailActivity.this.markName(markName);
			}
		};

		userInfoApi = new UserInfoApi();
		friendApi = new FriendApi();
		mDetailsInfo = (LinearLayout) this.findViewById(R.id.details_info);
		mApply = (LinearLayout) this.findViewById(R.id.details_apply);
		this.imgHead = (ImageView) this
				.findViewById(R.id.details_head_img_head);
		this.imgSex = (ImageView) this.findViewById(R.id.details_head_img_sex);
		this.imgSubscript = (ImageView) this
				.findViewById(R.id.details_head_img_subscript);

		this.txtUserName = (TextView) this
				.findViewById(R.id.details_head_txt_username);
		this.txtAge = (TextView) this.findViewById(R.id.details_head_txt_age);
		this.txtLineState = (TextView) this
				.findViewById(R.id.details_head_txt_line_state);
		this.txtAuth = (TextView) this
				.findViewById(R.id.details_head_txt_auth_state);
		this.textAlbum = (TextView) this.findViewById(R.id.details_layout_albums_tag);

		this.btnAuth = (Button) this.findViewById(R.id.details_head_btn_auth);
		this.btnVip = (Button) this.findViewById(R.id.details_head_btn_vip);
		
		
		this.txtSign = (TextView) this.findViewById(R.id.detail_info_signature);
		this.txtHeight = (TextView) this.findViewById(R.id.detail_info_tall);
		this.txtBody = (TextView) this.findViewById(R.id.detail_info_weight);
		this.txtProfession = (TextView) this.findViewById(R.id.detail_info_job);
		this.txtPhone = (TextView) this
				.findViewById(R.id.detail_info_mobilephone);
		this.txtQq = (TextView) this.findViewById(R.id.detail_info_qq);
		this.txtWages = (TextView) this.findViewById(R.id.detail_info_salary);
		this.txtHobby = (TextView) this.findViewById(R.id.detail_info_savor);

		this.layoutAlbum = (LinearLayout) this
				.findViewById(R.id.details_layout_albums);
		//this.layoutProject = (LinearLayout)this.findViewById(R.id.details_layout_projects);
		
		this.btnApply1 = (Button) this.findViewById(R.id.details_apply_btn1);
		this.btnApply2 = (Button) this.findViewById(R.id.details_apply_btn2);
		this.btnAddFriend = (Button) this
				.findViewById(R.id.details_btn_add_friend);
		this.btnSayHello = (Button) this
				.findViewById(R.id.details_btn_say_hello);
		this.mDetailsInfoHide = (LinearLayout) this
				.findViewById(R.id.details_info_hide);
		addLitener();
		showHeadInfo();
		getScore();
		
		getApplyAuth();
	}

	private void addLitener() {
		View.OnClickListener l = new DetailOnClick();
		getBtnAddFriend().setOnClickListener(l);
		getBtnSayHello().setOnClickListener(l);
		getBtnApply1().setOnClickListener(l);
		getBtnApply2().setOnClickListener(l);
		getImgHead().setOnClickListener(l);
	}
	
	/**
	 * 更新我的信息
	 * 
	 * @author fighter <br />
	 * 创建时间:2013-6-19<br />
	 * 修改时间:<br />
	 */
	protected void updateHeadInfo() {
		CustomerVo tempVo = getMyCustomerVo();
		if(tempVo != null){
			customer = tempVo;
		}
		
		showHeadInfo();
		showInfo();
	}
	
	protected void showHeadInfo() {
		if ("1".equals(customer.getSex())) {
			this.imgSex.setImageResource(R.drawable.sex_man);
		} else {
			this.imgSex.setImageResource(R.drawable.sex_woman);
		}
		
		if(Constants.CustomerType.CHATTING.equals(customer.getCustomertype())){
			if("1".equals(customer.getAgent())){
				imgSubscript.setImageResource(R.drawable.subscript_economic);
				imgSubscript.setVisibility(View.VISIBLE);
			}else
			if("1".equals(customer.getHeadattest())){
				imgSubscript.setVisibility(View.VISIBLE);
				imgSubscript.setBackgroundResource(R.drawable.subscript_auth);
			}else{
				imgSubscript.setVisibility(View.GONE);
			}
		}else{
			if("1".equals(customer.getVip())){
				imgSubscript.setVisibility(View.VISIBLE);
				imgSubscript.setBackgroundResource(R.drawable.subscript_vip);
			}else{
				imgSubscript.setVisibility(View.GONE);
			}
		}
		
		FinalOnloadBitmap.finalDisplay(getBaseContext(), customer, imgHead,
				getHeadBitmap());

		this.txtWages.setText(customer.getSalary());

		String name = TextdescTool.getCustomerName(customer);

		this.txtUserName.setText(name);
		this.txtAge.setText(TextdescTool.dateToAge(customer.getBirthday())
				+ "岁");
		
		showInfo();
	}

	/**
	 * 展现信息
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-28<br />
	 * 修改时间:<br />
	 */
	public void showInfo() {
		mDetailsInfo.setVisibility(View.VISIBLE);
		mDetailsInfoHide.setVisibility(View.GONE);

		this.txtSign.setText(customer.getSign());
		this.txtQq.setText(customer.getQq());
		this.txtBody.setText(customer.getWeight());
		this.txtHeight.setText(customer.getHeight());
		this.txtPhone.setText(customer.getPhone());
		this.txtProfession.setText(customer.getProfession());
		this.txtHobby.setText(customer.getInterest());
	}

	public void hideInfo() {
//		mDetailsInfo.setVisibility(View.GONE);
//		mDetailsInfoHide.setVisibility(View.VISIBLE);
	}

	public void hideApply() {
		mApply.setVisibility(View.GONE);
	}

	public void showApply() {
		mApply.setVisibility(View.VISIBLE);
	}
	protected void markName(final String markName) {
		if (!checkNetWork()) {
			return;
		}
		friendApi.markFriend(getUserInfoVo().getUid(), customer.getUid(), markName,
				new AjaxCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
						getWaitDialog().setMessage("修改中...");
						getWaitDialog().show();
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						getWaitDialog().cancel();
						String data = ErrorCode.getData(getBaseContext(), t);
						if (data != null) {
							if ("1".equals(data)) {
								markNameSuccess(markName);
							}
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						getWaitDialog().cancel();
						showToast(strMsg);
					}

				});
	}

	protected void markNameSuccess(String markName) {
		getPromptDialog().addCannel();
		getPromptDialog().removeConfirm();
		getPromptDialog().setCannelText("确定");
		getPromptDialog().setMessage("修改成功!");
		getPromptDialog().show();
		customer.setMarkName(markName);
		showHeadInfo();
	}
	protected void getNetworkAlum(){
		
	}
	/**
	 * 从服务器获取相册信息
	 * */
	protected void getNetworkAlbum() {
		// TODO 从服务器获取相册信息
		if (!checkNetWork()) {
			showToast(getResources().getString(R.string.toast_network));
		}else {
			AlbumApi albumApi = new AlbumApi();
			String uid = getUserInfoVo().getUid();
			albumApi.getAlbum(uid, getCustomer().getUid(), new AjaxCallBack<String>() {
				@Override
				public void onStart() {
					// TODO 准备从服务器获取
					super.onStart();
				}
				@Override
				public void onSuccess(String t) {
					// TODO 服务器获取成功
					super.onSuccess(t);
					String data = ErrorCode.getData(t);
					if (!"".equals(data)) {
						showAlbum(data);
					} else {
						getLayoutAlbum().setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(getUserInfoVo().getUid().equals(getCustomer().getUid())){
									Intent intent=new Intent(DetailActivity.this,AlbumActivity.class);
									startActivity(intent);		    
									}else{
										Intent intent=new Intent(DetailActivity.this,FriendAlbumActivity.class);
										intent.putExtra("touid", getCustomer().getUid());
										startActivity(intent);
									}
							}
						});
						getWaitDialog().dismiss();
					}
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					// TODO 服务器获取失败
					super.onFailure(t, strMsg);
				}
			});
		}
	}

	/**
	 * 显示相册信息
	 * 
	 * */
	private void showAlbum(String data) {
		if(DEBUG){
			Log.e("相册:", data);
		}
		if(FRIEND.equals(data)){
			textAlbum.setVisibility(View.VISIBLE);
			textAlbum.setText(FRIEND);
			layoutAlbum.setVisibility(View.GONE);
		}else if(VIP_TAG.equals(data)){
			textAlbum.setVisibility(View.VISIBLE);
			textAlbum.setText(VIP_TAG);
			layoutAlbum.setVisibility(View.GONE);
		}else{
			textAlbum.setVisibility(View.GONE);
			layoutAlbum.setVisibility(View.VISIBLE);
			try {
				List<AlbumVo> albumList=null;
				albumList=JSONArray.parseArray(data, AlbumVo.class);
				AlbumControl albumControl=new AlbumControl(DetailActivity.this,getLayoutAlbum(),albumList);
				albumControl.showAlbum(getUserInfoVo().getUid(),getCustomer().getUid());	
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		}
	/**
	 * 获取积分
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-28<br />
	 * 修改时间:<br />
	 */
	protected void getScore() {
		getUserInfoApi().getScore(getUserInfoVo().getUid(),

		getCustomer().getUid(), new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				int errorCode = ErrorCode.getError(t);
				if (errorCode == 0) {
					Map<String, String> data = (Map<String, String>) JSON
							.parse(ErrorCode.getData(t));
					scoreUploadSuccess(data);
				} else {
					scoreUploadError();
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				scoreUploadError();
			}

		});
	}

	/**
	 * 获取评分成功.
	 * 
	 * @param data
	 *            ascore 外表评分 sscore 服务评分 usercp 等级 作者:fighter <br />
	 *            创建时间:2013-5-28<br />
	 *            修改时间:<br />
	 */
	protected abstract void scoreUploadSuccess(Map<String, String> data);

	/**
	 * 评分获取失败
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-28<br />
	 * 修改时间:<br />
	 */
	protected abstract void scoreUploadError();

	/**
	 * @return the 头像
	 */
	public ImageView getImgHead() {
		return imgHead;
	}

	/**
	 * @return the 用户名
	 */
	public TextView getTxtUserName() {
		return txtUserName;
	}

	/**
	 * @return the 性别
	 */
	public ImageView getImgSex() {
		return imgSex;
	}

	/**
	 * @return the 年龄
	 */
	public TextView getTxtAge() {
		return txtAge;
	}

	/**
	 * @return the 在线状态
	 */
	public TextView getTxtLineState() {
		return txtLineState;
	}

	/**
	 * @return the 个性签名
	 */
	public TextView getTxtSign() {
		return txtSign;
	}

	/**
	 * @return the 相册
	 */
	public LinearLayout getLayoutAlbum() {
		return layoutAlbum;
	}

	/**
	 * @return the qq
	 */
	public TextView getTxtQq() {
		return txtQq;
	}

	/**
	 * @return the 身高
	 */
	public TextView getTxtHeight() {
		return txtHeight;
	}

	/**
	 * @return the 体重
	 */
	public TextView getTxtBody() {
		return txtBody;
	}

	/**
	 * @return the 职业
	 */
	public TextView getTxtProfession() {
		return txtProfession;
	}

	/**
	 * @return the 手机号
	 */
	public TextView getTxtPhone() {
		return txtPhone;
	}

	/**
	 * @return the 资金意愿
	 */
	public TextView getTxtWages() {
		return txtWages;
	}

	/**
	 * @return the 兴趣
	 */
	public TextView getTxtHobby() {
		return txtHobby;
	}

	/**
	 * @return the 角标
	 */
	public ImageView getImgSubscript() {
		return imgSubscript;
	}

	public DialogMark getDialogMark() {
		return dialogMark;
	}

	/**
	 * @return the 认证按钮
	 */
	public Button getBtnAuth() {
		return btnAuth;
	}

	
	public Button getBtnVip() {
		return btnVip;
	}

	/**
	 * @return the 认证提示
	 */
	public TextView getTxtAuth() {
		return txtAuth;
	}


	/**
	 * @return 打招呼按钮
	 */
	public Button getBtnSayHello() {
		return btnSayHello;
	}

	/**
	 * @return 添加好友按钮
	 */
	public Button getBtnAddFriend() {
		return btnAddFriend;
	}

	/**
	 * @return the customer
	 */
	public CustomerVo getCustomer() {
		return customer;
	}

	/**
	 * @return the btnApply1
	 */
	public Button getBtnApply1() {
		return btnApply1;
	}

	/**
	 * @return the btnApply2
	 */
	public Button getBtnApply2() {
		return btnApply2;
	}

	/**
	 * @return the 用户信息接口
	 */
	public UserInfoApi getUserInfoApi() {
		return userInfoApi;
	}

	/**
	 * 获取是否能查看好友信息权限.
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-29<br />
	 * 修改时间:<br />
	 */
	protected void applyAuth() {
		if (!checkNetWork())
			return;

	}

	/***
	 * 获取是否有查看权限.
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-6-14<br />
	 * 修改时间:<br />
	 */
	protected void getApplyAuth() {
		
	}
	
	/**
	 * 申请查看
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-29<br />
	 * 修改时间:<br />
	 */
	protected void apply1Action() {
		if(!checkCustomerType()){
			return;
		}
		getUserInfoApi().applyAuth(getUserInfoVo().getUid(), customer.getUid(), new ApplyAuthCallback());
	}

	protected void apply2Action() {
		if (!checkNetWork()) {
			showToast(getResources().getString(R.string.toast_network));
			return;
		}
		System.out.println("评价::;;");
		if (Constants.CustomerType.CHATTING.equals(getCustomer()
				.getCustomertype())) {
			Intent intent = new Intent(DetailActivity.this,
					EvaluateActivity.class);
			intent.putExtra("data", getCustomer());
			startActivity(intent);
		} else {
			evaluateSorce();
		}
	}

	/**
	 * 判断用户类型评分
	 * */
	public void evaluateSorce() {
		if (Constants.CustomerType.CHATTING.equals(getCustomer()
				.getCustomertype())) {
			editSorce(getCustomer().getAscore(), getCustomer().getSscore());
		} else {
			editSorce("0", "0");
		}
	}

	/**
	 * 评分 Param asorce:外貌评分，ssorce:服务评分 ps:外貌评分和服务评分都是0即上传魅力值
	 * */
	public void editSorce(String asorce, String ssorce) {
		userInfoApi.editScore(getUserInfoVo().getUid(), getCustomer().getUid(),
				asorce, ssorce, new AjaxCallBack<String>() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						getWaitDialog().setMessage("提交请求");
						getWaitDialog().show();
					}

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						String data = ErrorCode.getData(getBaseContext(), t);

						if (data != null && "1".equals(data)) {
							getWaitDialog().setMessage("提交成功");
						} else {
							getWaitDialog().setMessage("提交失败");
						}

						getWaitDialog().cancel();
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						getWaitDialog().setMessage("提交失败:" + strMsg);
						getWaitDialog().cancel();
					}

				});
	}

	protected void addFriendAction() {
		//20151119 暂时不验证用户
//		if(!checkCustomerType()){
//			return;
//		}
		
		if (!checkNetWork())
			return;

		getFriendApi().toFriend(getUserInfoVo().getUid(), getCustomer().getUid(),
				new AddFriendCallback());
	}

	protected void sayHelloAction() {
		//20151119
//		if(!checkCustomerType()){
//			return;
//		}
		
		// 1. 判断本地是否有该用户.
		CustomerVo tempCustomerVo = null;
		try {
			tempCustomerVo = getFinalDb().findById(getCustomer().getUid(),
					CustomerVo.class);
		} catch (Exception e) {
		}
		// 2. 没有就保存.
		if (tempCustomerVo == null) {
			try {
				getCustomer().setFriend("0");
				getFinalDb().save(getCustomer());
			} catch (Exception e) {
			}
		}
		
//		if(!ChatPrompt.isShow(getBaseContext())){
//			ChatPrompt.showPrompt(this, new ChatPromptLisenter(this){
//
//				@Override
//				public void onClick(View v) {
//					super.onClick(v);
//					Intent intent = new Intent(DetailActivity.this, ChatMainActivity.class);
//					intent.putExtra("data", getCustomer());
//					startActivity(intent);
//				}
//				
//			});
//		}else{
			Intent intent = new Intent(DetailActivity.this, ChatMainActivity.class);
			intent.putExtra("data", getCustomer());
			startActivity(intent);
//		}

		
	}

	class DetailOnClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == getBtnAddFriend()) {
				addFriendAction();
			} else if (v == getBtnSayHello()) {
				sayHelloAction();
			} else if (v == getBtnApply1()) {
				apply1Action();
			} else if (v == getBtnApply2()) {
				apply2Action();
			} else if (v.getId() == R.id.detail_layout_albums) {
				if (getUserInfoVo().getUid() != getCustomer().getUid()) {
					Intent intent = new Intent(DetailActivity.this,
							FriendAlbumActivity.class);
					intent.putExtra("fid", getCustomer().getUid());
					startActivity(intent);
				}
			}else if(v == getImgHead()){
				Intent intent = new Intent(DetailActivity.this, ShowHeadActivity.class);
				intent.putExtra("data", customer.getHead());
				startActivity(intent);
			}

		}

	}

	/**
	 * @return the friendApi
	 */
	public FriendApi getFriendApi() {
		return friendApi;
	}
	
	/***
	 * 效验用户是否给我授权权限查看用户信息
	 * 
	 * @author fighter <br />
	 * 创建时间:2013-6-20<br />
	 * 修改时间:<br />
	 */
	protected void checkUserAuth(){
		if("1".equals(customer.getFriend())){
			return;
		}
		getUserInfoApi().checkAuth(getUserInfoVo().getUid(), customer.getUid(), new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					String data = ErrorCode.getData(t);
					if(data != null){
						// 如果没有权限 显示申请查看信息.
						if(!"1".equals(data)){
							getBtnApply1().setVisibility(View.VISIBLE);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		
		});
	}
	
	/**
	 * 获取用户在服务器上的信息
	 * 
	 * @author fighter <br />
	 * 创建时间:2013-6-27<br />
	 * 修改时间:<br />
	 */
	protected void getRefresh() {
		Log.d(TAG, "getRefresh()");
		getUserInfoApi().getInfo(getMyCustomerVo().getUid(), customer.getUid(), new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Log.v(TAG, t);
				try {
					String data = ErrorCode.getData(t);
					if(!TextUtils.isEmpty(data)){
						CustomerVo customerVo = JSONObject.toJavaObject(JSONObject.parseObject(data), CustomerVo.class);
						customer = customerVo;
						showHeadInfo();
						showInfo();
						
						System.out.println("显示成功!");
					}
				} catch (Exception e) {
					Log.e(TAG, "getRefresh()", e);
				}
			}
			
		});
	}
	
	private boolean checkUserType(){
		boolean flag = false;
		if("1".equals(customer.getFriend())){
			flag = true;
		}else{
			// 如果是陪聊用户  检查是否是认证
			if(Constants.CustomerType.CHATTING.equals(getMyCustomerVo().getCustomertype())){
				if("1".equals(getMyCustomerVo().getHeadattest())){
					flag = true;
				}else{
					flag = false;
				}
			}
			// 如果是寻伴用户 检查是否是VIP
			if(Constants.CustomerType.WITHCHAT.equals(getMyCustomerVo().getCustomertype())){
				if("1".equals(getMyCustomerVo().getVip())){
					flag = true;
				}else{
					flag = false;
				}
			}
		}
		return flag;
	}
	
	private boolean checkCustomerType(){
		boolean flag = false;
		if("1".equals(customer.getFriend())){
			flag = true;
		}else{
			// 如果是陪聊用户  检查是否是认证
			if(Constants.CustomerType.CHATTING.equals(getMyCustomerVo().getCustomertype())){
				if("1".equals(getMyCustomerVo().getHeadattest())){
					flag = true;
				}else{
					flag = false;
					gotoAuth();
				}
			}
			// 如果是寻伴用户 检查是否是VIP
			if(Constants.CustomerType.WITHCHAT.equals(getMyCustomerVo().getCustomertype())){
				if("1".equals(getMyCustomerVo().getVip())){
					flag = true;
				}else{
					flag = false;
					gotoVip();
				}
			}
		}
		return flag;
	}
	
	/**
	 * 我要认证
	 * 
	 * @author fighter <br />
	 * 创建时间:2013-6-20<br />
	 * 修改时间:<br />
	 */
	private void gotoAuth(){
		getPromptDialog().addCannel();
		getPromptDialog().addConfirm(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 去认证头像.
				getPromptDialog().cancel();
				Intent intent = new Intent(DetailActivity.this, HeadAuthActivity.class);
				startActivity(intent);
			}
		});
		getPromptDialog().setMessage("成为VIP/认证用户！");
		
		getPromptDialog().setCannelText("以后在说");
		getPromptDialog().setConfirmText("我要认证");
		
		getPromptDialog().show();
	}
	
	/***
	 * 提示是否成为VIP
	 * 
	 * @author fighter <br />
	 * 创建时间:2013-6-20<br />
	 * 修改时间:<br />
	 */
	private void gotoVip(){
		getPromptDialog().addCannel();
		getPromptDialog().addConfirm(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 去认证头像.
				getPromptDialog().cancel();
				Intent intent = new Intent(DetailActivity.this,
						MyPointActivity.class);
				startActivity(intent);
			}
		});
		
		getPromptDialog().setMessage("成为VIP用户！");
		
		getPromptDialog().setCannelText("以后在说");
		getPromptDialog().setConfirmText("成为VIP");
		
		getPromptDialog().show();
	}

}
