package com.peiban.app.ui;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.peiban.R;


import com.peiban.adapter.ObjectBaseAdapter;
import com.peiban.app.Constants;
import com.peiban.app.action.ImageInfoAction;
import com.peiban.app.action.ImageInfoAction.OnBitmapListener;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.ProjectApi;
import com.peiban.app.ui.LocationActivity.ListItemOnClick;
import com.peiban.app.ui.LocationActivity.MyAdapter;
import com.peiban.app.ui.LocationActivity.PostLocationInfo;
import com.peiban.app.ui.LocationActivity.ViewHolder;
import com.peiban.app.vo.ProjectVo;
import com.peiban.command.TextdescTool;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.LocationVo;
import com.shangwupanlv.widget.XListView;








import com.shangwupanlv.widget.XListView.IXListViewListener;




















import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
//import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class UserBaseActivity extends BaseActivity implements IXListViewListener{
	/**
	 * 组件相关
	 * */
	@ViewInject(id = R.id.project_list)
	private XListView listView;
	private MyAdapter adapter;
	private int currPage;
	private int pageSize;
	List<CustomerVo> lists=new ArrayList<CustomerVo>();
	private ImageInfoAction imageInfoAction;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.details_user);
		this.baseInit();
		//getNetworkAlbum();
		//重要，iListView，第一次拖动不执行onRefresh
		this.onRefresh();
	}
	

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleContent(R.string.details_title);
	}
	
	@Override
	protected void baseInit(){
		super.baseInit();
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setXListViewListener(this);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setDivider(null);
		//
	
//		List<CustomerVo> customerVos = new ArrayList<CustomerVo>();
//		CustomerVo vo = new CustomerVo();
//		vo.setCity("bbc"+currPage);
//		currPage++;
//		customerVos.add(vo);
//		CustomerVo vo2 = new CustomerVo();
//		vo2.setCity("bbc"+currPage);
//		customerVos.add(vo2);
//		currPage++;
//		CustomerVo vo4 = new CustomerVo();
//		vo4.setCity("bbc"+currPage);
//		customerVos.add(vo4);
//		
//		
//		currPage++;
//		adapter.addList(customerVos);
		
		
		listView.setOnItemClickListener(new ListItemOnClick());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==101){
			if(resultCode==10){
				onRefresh();
			}
		}
		else{
		
			if(this.imageInfoAction!=null){
				imageInfoAction.onActivityResult(requestCode, resultCode, data);
			}
		}
		

	}
	
	//需要嵌入头像
	class MyAdapter extends ObjectBaseAdapter<ProjectVo> {
		@Override
		public void addList(List<ProjectVo> lists) {
			if(lists == null){
				return;
			}
			getLists().addAll(lists);
			
		}

		@Override
		public void addObject(ProjectVo t) {
			lists.add(t);
		}
		public List<ProjectVo> getLists(){
			return lists;
		}
		@Override
		public ProjectVo getItem(int position) {
			return getLists().get(position);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup group) {
			//ViewHolder viewHolder = null;
			ProjectVo customerVo = getItem(position);
			ProjectVo hist = null;
			if(convertView!=null){
				hist = (ProjectVo) convertView.getTag();
			}else{
				hist=customerVo;
			}
			
			
			if (customerVo.getType()!=null && customerVo.getType().equals("header")){
				convertView = LayoutInflater.from(getBaseContext()).inflate(
						R.layout.details_list_header, null);
			}
			else if(customerVo.getType()!=null && customerVo.getType().equals("today")){
				convertView = LayoutInflater.from(getBaseContext()).inflate(
						R.layout.details_list_todayrow, null);
			}
			else{
				if (convertView != null ) {
					//viewHolder = (ViewHolder) convertView.getTag();
					if (hist.getType().equals("header")){
						convertView = LayoutInflater.from(getBaseContext()).inflate(
								R.layout.details_list_item, null);
					}
					
				} else {
						convertView = LayoutInflater.from(getBaseContext()).inflate(
							R.layout.details_list_item, null);
				
					//viewHolder = ViewHolder.getInstance(convertView);
				}
			}
			convertView.setTag(hist);
			//标题和文字
			//convertView.setTag(viewHolder);
			//bindView(viewHolder, position);
			TextView title=(TextView)convertView.findViewById(R.id.detail_project_title);
			if(title!=null){
				title.setText(customerVo.getName());
			}
			
			TextView description= (TextView) convertView.findViewById(R.id.detail_project_desc);
			if(description!=null)
			{
				description.setText(customerVo.getDescription());
			}
			//用户头像（只有第一次会触发）
			String[] strs=customerVo.getImages();
			if(customerVo.getType().equals("header")){
				if (strs != null){
					for(int i=0;i<strs.length;i++){
						if(i==0){
							ImageView img=(ImageView)convertView.findViewById(R.id.detail_project_title_img);
							if(img!=null){
							Resources res = getResources();
							
							
							int imageResId = res.getIdentifier(strs[i], "drawable", "com.peiban");//这里需要注意，如果项目名字改掉了（尽管可能性很少）
							img.setImageDrawable(getResources().getDrawable(imageResId));
							}
						}else if(i==1){
							ImageView img=(ImageView)convertView.findViewById(R.id.detail_project_title_headicon);
							if (img!=null){
								Resources res = getResources();
								int imageResId = res.getIdentifier(strs[i], "drawable", "com.peiban");//这里需要注意，如果项目名字改掉了（尽管可能性很少）
								img.setImageDrawable(getResources().getDrawable(imageResId));
							}
						}
					}
				}
			}
			else if(customerVo.getType().equals("today")){
				if (strs!=null && strs.length>=1){
					ImageView img=(ImageView)convertView.findViewById(R.id.detail_project_title_img);
					if(img!=null){
						Resources res = getResources();
						int imageResId = res.getIdentifier(strs[1], "drawable", "com.peiban");//这里需要注意，如果项目名字改掉了（尽管可能性很少）
						img.setImageDrawable(getResources().getDrawable(imageResId));
					}
				}
			}
			
			
			
			//日期
			if(customerVo.getType()==null || customerVo.getType().equals("")){
				TextView txtMonth = (TextView)convertView.findViewById(R.id.detail_title_month);
				txtMonth.setText(String.valueOf(customerVo.getCreateMonth())+"月");
				TextView txtDay = (TextView)convertView.findViewById(R.id.detail_title_day);
				txtDay.setText(String.valueOf(customerVo.getCreateDay()));
			}
			return convertView;
			
		}
		void bindView(ViewHolder holder,int position){
			
		}
	
	}
	/**
	 * 初始化个人文档
	 * 或者可能包含当天一个项目录入按钮
	 */
	public void initUserInfor(){
		adapter.getLists().clear();
		ProjectVo vo = new ProjectVo();
		vo.setType("header");
		String[] strs=new String[]{"nvshen","photho_ioc"};
		vo.setImages(strs);
		vo.setName("等一个人咖啡");
		vo.setDescription("生命自有它的出路");
		adapter.addObject(vo);
		
		//如果查看自己
		ProjectVo add = new ProjectVo();
		add.setType("today");
		add.setImages(new String[]{"btn_chat_box_camera"});
		adapter.addObject(add);
		
	}
	@Override
	public void onRefresh() {
		listView.setPullLoadEnable(true);
		//adapter.getLists().clear();
		initUserInfor();
		currPage=1;
		pullProjectInfo();
		adapter.notifyDataSetInvalidated();
	}


	@Override
	public void onLoadMore() {
		currPage ++;
		
		pullProjectInfo();
		//adapter.notifyDataSetInvalidated();
	
	}
	void pullProjectInfo(){
		Map<String, String> param = new HashMap<String, String>();
		//param.put("sortName", sortName);
		//param.put("sort", sort);
		
		ProjectApi api=new ProjectApi();
		api.getList(getUserInfoVo().getUid(), currPage, pageSize , param, new PostProjectInfo());
//		for(int i=1;i<=3;i++){
//			
//			if (i==1 ){
//				List<ProjectVo> customerVos = new ArrayList<ProjectVo>();
//				ProjectVo vo = new ProjectVo();
//				vo.setTitle("bbc"+currPage);
//				currPage++;
//				customerVos.add(vo);
//				adapter.addList(customerVos);
//				//adapter.notifyDataSetChanged();
//			}
//			else{
//				List<ProjectVo> customerVos = new ArrayList<ProjectVo>();
//				ProjectVo vo = new ProjectVo();
//				vo.setTitle("bbc"+currPage);
//				vo.setDesc("aac");
//				currPage++;
//				customerVos.add(vo);
//				ProjectVo vo2 = new ProjectVo();
//				vo2.setTitle("bbc"+currPage);
//				vo2.setDesc("bbc");
//				customerVos.add(vo2);
//				currPage++;
//				ProjectVo vo3 = new ProjectVo();
//				vo3.setTitle("bbc"+currPage);
//				customerVos.add(vo3);
//				currPage++;
//				ProjectVo vo4 = new ProjectVo();
//				vo4.setTitle("bbc"+currPage);
//				customerVos.add(vo4);
//				currPage++;
//				ProjectVo vo5 = new ProjectVo();
//				vo5.setTitle("bbc"+currPage);
//				customerVos.add(vo5);
//				currPage++;
//				adapter.addList(customerVos);
//			}
//		}
//		stopLoad();
	}
	
	/**
	 * 
	 * 功能：异步获取用户信息 <br />
	 * 日期：2013-5-31<br />
	 * 地点：淘高手网<br />
	 * 版本：ver 1.0<br />
	 * 
	 * @author fighter
	 * @since
	 */
	class PostProjectInfo extends AjaxCallBack<String> {

		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			String data = ErrorCode.getData(getBaseContext(), t);
			if(data != null){
				String currTime = System.currentTimeMillis()+"";				
				listView.setRefreshTime("上次刷新:" + TextdescTool.getTime(currTime));
				try {
					List<ProjectVo> customerVos = JSON.parseArray(data, ProjectVo.class);
					if (currPage <= 1) {
						//adapter.getLists().clear();
						initUserInfor();
					
					}
					if (customerVos.size()==0){
						listView.setPullLoadEnable(false);
					}
					else
					{
						adapter.addList(customerVos);
					}
				} catch (Exception e) {
					stopLoad();
				}
				
//				try {
//					data = JSONArray.toJSON(adapter.getLists()).toString();
//					locationVo.setLocations(data);
//				} catch (Exception e) {
//				}
//				
//				if(0 == locationVo.getState()){
//					getFinalDb().save(locationVo);
//				}else{
//					getFinalDb().update(locationVo);	
//				}
				
			}else{
				//stopLoad();
				listView.setPullLoadEnable(false);
			}
			stopLoad();
			
		}
		@Override
		public void onFailure(Throwable t, String strMsg) {
			super.onFailure(t, strMsg);
			showToast("发生错误!");
			stopLoad();
		}
	}
	// 停止加载
	private void stopLoad(){
		listView.stopLoadMore();
		listView.stopRefresh();
	}
	
	class ListItemOnClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ProjectVo customerVo = adapter.getItem(position - 1);
			
			if(customerVo != null){
				if (customerVo.getType().equals("header"))
				{}
				else if(customerVo.getType().equals("today"))
				{
//					TodayrowDialog dialog=new TodayrowDialog(UserBaseActivity.this);
//					dialog.show();
					OnBitmapListener imageBitmapListener = new OnBitmapListener() {

						@Override
						public void getToBitmap(int bimType, Bitmap bm) {
							Intent intent = new Intent(UserBaseActivity.this,NewProjectAlbumActivity.class);
							ByteArrayOutputStream baos=new ByteArrayOutputStream();  
							bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
							byte [] bitmapByte =baos.toByteArray();  
							intent.putExtra("bitmap", bitmapByte);  
							//intent.putExtra("bitmap", bm);
							//startActivity(intent);
							startActivityForResult(intent, 101);
						}
					};
					imageInfoAction = new ImageInfoAction(UserBaseActivity.this);
					imageInfoAction.setOnBitmapListener(imageBitmapListener);
					imageInfoAction.setOutHeight(320);
					imageInfoAction.setOutWidth(240);
					NewProjectAlbumSelectClick dialog=new NewProjectAlbumSelectClick(UserBaseActivity.this,imageInfoAction);
					dialog.onClick(null);
//					Intent intent = new Intent(UserBaseActivity.this,
//							NewAlbumActivity.class);
//					intent.putExtra("tag", "1");
//					startActivity(intent);
				}
				else if(customerVo.getType().equals(""))
				{
				Intent intent = null;
//				if(Constants.CustomerType.CHATTING.equals(customerVo.getCustomertype()) && "1".equals(customerVo.getFriend())){
//					//intent = new Intent(LocationActivity.this, FriendChattingActivity.class);
//					intent = new Intent(UserBaseActivity.this, UserBaseActivity.class);
//				}else if(Constants.CustomerType.WITHCHAT.equals(customerVo.getCustomertype()) && "1".equals(customerVo.getFriend())){
//					intent = new Intent(UserBaseActivity.this, FriendFindPartnerActivity.class);
//				}else if(Constants.CustomerType.WITHCHAT.equals(customerVo.getCustomertype())){
//					//intent = new Intent(LocationActivity.this, StrangerFindPartnerActivity.class);
//					intent = new Intent(UserBaseActivity.this, UserBaseActivity.class);
//				}else{
//					//intent = new Intent(LocationActivity.this, StrangerChattingActivity.class);
//					intent = new Intent(LocationActivity.this, UserBaseActivity.class);
//				}
//				intent.putExtra("data", customerVo);
//				
//				startActivity(intent);
				}
			}
		}
		
	}
	
}
