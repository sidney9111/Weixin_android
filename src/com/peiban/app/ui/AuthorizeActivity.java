package com.peiban.app.ui;

import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.peiban.R;
import com.peiban.adapter.ObjectBaseAdapter;
import com.peiban.app.Constants;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.UserInfoApi;
import com.peiban.app.ui.common.FinalOnloadBitmap;
import com.peiban.command.TextdescTool;
import com.peiban.vo.CustomerVo;
import com.shangwupanlv.widget.XListView;
import com.shangwupanlv.widget.XListView.IXListViewListener;

/**
 * 
 * 功能：授权用户 <br />
 * 日期：2013-5-17<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class AuthorizeActivity extends BaseActivity implements IXListViewListener{
	@ViewInject(id = R.id.authorize_list_lv)
	private XListView listMsgView;

	private MyAdapter adapter;
	
	private UserInfoApi mUserInfoApi;
	
	private int currPage = 0;
	private int pageSize = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authorize);
		
		baseInit();
	}

	protected void baseInit() {
		super.baseInit();
		adapter = new MyAdapter();
		listMsgView.setAdapter(adapter);
		listMsgView.setXListViewListener(this);
		listMsgView.setPullLoadEnable(true);
		listMsgView.setPullRefreshEnable(true);
		listMsgView.startLoadMore();
	}
	
	@Override
	protected void initTitle() {
		setTitleContent(R.string.authorize_title);
		setBtnBack();
	}

	class MyAdapter extends ObjectBaseAdapter<CustomerVo> {
		@Override
		public void addList(List<CustomerVo> lists) {
			if(lists == null){
				return;
			}
			
			super.lists.addAll(lists);
			notifyDataSetInvalidated();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			if (convertView != null) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {
				convertView = getLayoutInflater().inflate(
						R.layout.authorize_list_item, null);
				viewHolder = ViewHolder.getInstance(convertView);
			}

			convertView.setTag(viewHolder);
			bindView(viewHolder, position);
			return convertView;
		}
		private void bindView(ViewHolder viewHolder, int position){
			CustomerVo customerVo = getItem(position);
			
			FinalOnloadBitmap.finalDisplay(getBaseContext(), customerVo, viewHolder.imgHead, getHeadBitmap());
			viewHolder.imgHead.setOnClickListener(authListener);
			viewHolder.imgHead.setTag(customerVo);
			String name = TextdescTool.getCustomerName(customerVo);
			
			if("1".equals(customerVo.getSex())){
				viewHolder.imgSex.setImageResource(R.drawable.sex_man);
			}else{
				viewHolder.imgSex.setImageResource(R.drawable.sex_woman);
			}
			
			String userType = customerVo.getCustomertype();
			if(Constants.CustomerType.CHATTING.equals(userType)){
				// 认证头像
				if("1".equals(customerVo.getAgent())){
					viewHolder.imgSubscript.setImageResource(R.drawable.subscript_economic);
					viewHolder.imgSubscript.setVisibility(View.VISIBLE);
				}else
				if("1".equals(customerVo.getHeadattest())){
					viewHolder.imgSubscript.setImageResource(R.drawable.subscript_auth);
					viewHolder.imgSubscript.setVisibility(View.VISIBLE);
				}else{
					viewHolder.imgSubscript.setVisibility(View.GONE);
					
				}
			}else{
				// 是否是VIP
				if("1".equals(customerVo.getVip())){
					viewHolder.imgSubscript.setImageResource(R.drawable.subscript_vip);
					viewHolder.imgSubscript.setVisibility(View.VISIBLE);
				}else{
					viewHolder.imgSubscript.setVisibility(View.GONE);
					
				}
			}
			
			String age = customerVo.getBirthday();
			String location = customerVo.getLocal();
			String line = customerVo.getOnline();
			String sign = customerVo.getInterest();
			
			viewHolder.txtUsername.setText(name);
			viewHolder.txtLocation.setText(location);
			
			viewHolder.txtAge.setText(TextdescTool.dateToAge(age) + "岁");
			viewHolder.txtLocation.setText(location);
			viewHolder.txtOnline.setText(line);
			
			viewHolder.txtSign.setText(sign);
			viewHolder.btnCanel.setTag(customerVo);
			viewHolder.btnCanel.setOnClickListener(authListener);
		}

	}
	
	static class ViewHolder {
		/** 角标 */
		public ImageView imgSubscript; // 角标
		/** 头像 */
		public ImageView imgHead; // 头像
		/** 年龄 */
		public ImageView imgSex;
		
		/** 昵称 */
		public TextView txtUsername;
		/** 年龄 */
		public TextView txtAge;
		
		/** 距离 */
		public TextView txtLocation;
		/** 是否在线 */
		public TextView txtOnline;
		/** 签名 */
		public TextView txtSign;
		
		public Button btnCanel;

		public static ViewHolder getInstance(View view) {
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.txtAge = (TextView) view.findViewById(R.id.list_txt_age);
			viewHolder.txtOnline = (TextView) view
					.findViewById(R.id.list_txt_state);
			viewHolder.txtUsername = (TextView) view
					.findViewById(R.id.list_txt_title);
			viewHolder.txtSign = (TextView) view
					.findViewById(R.id.list_txt_message_info);
			viewHolder.txtLocation = (TextView) view
					.findViewById(R.id.list_txt_location);
			
			
			viewHolder.imgHead = (ImageView) view
					.findViewById(R.id.list_item_img_head);
			viewHolder.imgSex = (ImageView) view
					.findViewById(R.id.list_img_sex);
			viewHolder.imgSubscript = (ImageView) view
					.findViewById(R.id.list_item_img_subscript);

			viewHolder.btnCanel = (Button) view.findViewById(R.id.list_btn_cannel_authorize);
			return viewHolder;
		}
	}
	// 停止加载
	private void stopLoad(){
		listMsgView.stopLoadMore();
		listMsgView.stopRefresh();
	}
	@Override
	public void onRefresh() {
		currPage = 1;
		pullAuthList();
	}

	@Override
	public void onLoadMore() {
		currPage ++;
		pullAuthList();
		
	}
	
	private void pullAuthList(){
		if (!checkNetWork()) {
			stopLoad();
			return;
		}
		if(mUserInfoApi == null)
			mUserInfoApi = new UserInfoApi();
		
		mUserInfoApi.authUser(getUserInfoVo().getUid(), (currPage) + "", pageSize + "", new AjaxCallBack<String>() {
		
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Log.v("授权", t);
				stopLoad();
				try {
					String data = ErrorCode.getData(getBaseContext(), t);
					if(!TextUtils.isEmpty(data)){
						try {
							List<CustomerVo> customerVos = JSON.parseArray(data, CustomerVo.class);
							if(currPage <= 1){
								adapter.removeAll();
							}
							adapter.addList(customerVos);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						listMsgView.setPullLoadEnable(false);
					}
				} catch (Exception e) {
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				stopLoad();
				showToast("服务器响应失败!");
			}
			
		});
	}
	
	private void delAuthUser(final CustomerVo customerVo){
		if (!checkNetWork()) {
			stopLoad();
			return;
		}
		
		if(mUserInfoApi == null)
			mUserInfoApi = new UserInfoApi();
		
		mUserInfoApi.delAuthUser(getUserInfoVo().getUid(), customerVo.getUid(), new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				stopLoad();
				String data = ErrorCode.getData(getBaseContext(), t);
				if(data != null){
					if("1".equals(data)){
						showToast("取消成功!");
						adapter.removeObject(customerVo);
					}else{
						showToast("取消失败!");
					}
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				stopLoad();
				showToast("服务器响应失败!");
			}
			
		});
	}
	
	private View.OnClickListener authListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			CustomerVo customerVo = null;
			switch (v.getId()) {
			case R.id.list_item_img_head:
				customerVo = (CustomerVo) v.getTag();
				if(customerVo != null)
				{
					showAuthUser(customerVo);
				}
				break;
			case R.id.list_btn_cannel_authorize:
				customerVo = (CustomerVo) v.getTag();
				if(customerVo != null)
				{
					delAuthUser(customerVo);
				}
				break;

			default:
				break;
			}
			
		}

		private void showAuthUser(CustomerVo customerVo) {
			Intent intent = null;
			if(Constants.CustomerType.CHATTING.equals(customerVo.getCustomertype()) && "1".equals(customerVo.getFriend())){
				intent = new Intent(AuthorizeActivity.this, FriendChattingActivity.class);
			}else if(Constants.CustomerType.WITHCHAT.equals(customerVo.getCustomertype()) && "1".equals(customerVo.getFriend())){
				intent = new Intent(AuthorizeActivity.this, FriendFindPartnerActivity.class);
			}else if(Constants.CustomerType.WITHCHAT.equals(customerVo.getCustomertype())){
				intent = new Intent(AuthorizeActivity.this, StrangerFindPartnerActivity.class);
			}else{
				intent = new Intent(AuthorizeActivity.this, StrangerChattingActivity.class);
			}
			intent.putExtra("data", customerVo);
			
			startActivity(intent);
		}
	};
}
