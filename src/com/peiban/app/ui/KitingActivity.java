package com.peiban.app.ui;

import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peiban.R;
import com.peiban.adapter.ObjectBaseAdapter;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.KitingRecordApi;
import com.peiban.app.ui.common.RecordEcode;
import com.peiban.command.TextdescTool;
import com.shangwupanlv.widget.XListView;
import com.shangwupanlv.widget.XListView.IXListViewListener;

public class KitingActivity extends BaseActivity implements IXListViewListener{
	@ViewInject(id = R.id.pb_list)
	private XListView xListView;
	
	private int currPage = 0;
	private int pageSize = 30;
	
	private MyAdpater adapter;
	
	private KitingRecordApi mKitingRecordApi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.kiting_list);
		this.baseInit();
	}

	@Override
	protected void baseInit() {
		super.baseInit();
		mKitingRecordApi = new KitingRecordApi();
		adapter = new MyAdpater();
		xListView.setAdapter(adapter);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
		
		xListView.startLoadMore();
	}

	@Override
	protected void initTitle() {
		this.setTitleContent("积分记录");
		this.setBtnBack();
	}

	@Override
	public void onRefresh() {
		currPage = 1;
		getRecord();
	}

	@Override
	public void onLoadMore() {
		currPage ++;
		getRecord();
	}
	
	private void getRecord(){
		mKitingRecordApi.getCreditRecord(getUserInfoVo().getUid(), currPage + "", pageSize + "", new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				stopLoad();
				xListView.setRefreshTime(TextdescTool.getDate(System.currentTimeMillis()));
				try {
					String data = ErrorCode.getData(getBaseContext(), t);
					if(TextUtils.isEmpty(data)){
						xListView.setPullLoadEnable(false);
						return;
					}
					List<String> strs = RecordEcode.ecodeRecord(data);
					if(currPage <= 1){
						adapter.removeAll();
					}
					adapter.addList(strs);
				} catch (Exception e) {
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				showToast("访问网络失败!");
			}
			
		});
	}
	
	private void stopLoad(){
		xListView.stopLoadMore();
		xListView.stopRefresh();
	}

	class MyAdpater extends ObjectBaseAdapter<String>{
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.kiting_list_item, null);
			}
			((TextView) convertView).setText(getItem(position));
			
			return convertView;
		}
		
	}
}
