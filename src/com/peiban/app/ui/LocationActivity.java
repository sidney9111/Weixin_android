package com.peiban.app.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.peiban.R;
import com.peiban.SharedStorage;
import com.peiban.adapter.ObjectBaseAdapter;
import com.peiban.app.Constants;
import com.peiban.app.FilterConfig;
import com.peiban.app.LocationShared;
import com.peiban.app.action.LocationAction;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.LocationApi;
import com.peiban.app.control.DialogEditDataModel;
import com.peiban.app.ui.common.DistanceShared;
import com.peiban.app.ui.common.FinalOnloadBitmap;
import com.peiban.command.NetworkUtils;
import com.peiban.command.TextdescTool;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.LocationVo;
import com.shangwupanlv.widget.XListView;
import com.shangwupanlv.widget.XListView.IXListViewListener;

/**
 * 
 * 功能： <br />
 * 判断用户类型，根据不同的用户类型获取附近用户信息不同。 <br/>
 * 寻伴用户只能获取陪聊信息(可以根据(综合评分, 外貌评分, 服务评分 拉取)) <br />
 * 陪聊拉取用户信息时候.(荣誉值) <br />
 * 日期：2013-5-31<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 */
public class LocationActivity extends BaseActivity implements IXListViewListener{
	public static final int REQUEST_TAG = 0x20;
	
	private static final String TAG = "LOCATION_ACTIVITY";
	private static final String ZHPF = "综合评分";
	private static final String FWPF = "服务评分";
	private static final String WMPF = "外貌评分";
	private static final String RYZ = "荣誉值";
	
	private int pageSize = 5;
	
	private int currPage = 0;
	
	@ViewInject(id = R.id.location_txt_comprehensivescore)
	private TextView txtSelectSroce;
	@ViewInject(id = R.id.view_select_distance)
	private View viewSelectDistance;
	@ViewInject(id = R.id.view_select_score)
	private View viewSelectScore;
	
	/**
	 * 组件相关
	 * */
	@ViewInject(id = R.id.location_list)
	private XListView listView;
	@ViewInject(id = R.id.btn_select_location)
	private ImageView btnSelectLocation;
	@ViewInject(id = R.id.location_txt_distance)
	private TextView txtLocationDistance;
	@ViewInject(id=R.id.location_txt_comprehensivescore)
	private TextView txtLocationSocre;
	@ViewInject(id = R.id.location_img_sort)
	private ImageView imgLocaionSort;
	@ViewInject(id=R.id.location_img_ssort)
	private ImageView imgSorceSort;
	private RelativeLayout locationRelativeDistance;
	private LinearLayout locationLinearSelect;
	@ViewInject(id = R.id.location_distance_0)
	private Button locationDistance0;
	@ViewInject(id = R.id.location_distance_5)
	private Button locationDistance5;
	@ViewInject(id = R.id.location_distance_10)
	private Button locationDistance10;
	@ViewInject(id = R.id.location_distance_20)
	private Button locationDistance20;
	@ViewInject(id = R.id.location_distance_50)
	private Button locationDistance50;
	@ViewInject(id = R.id.btn_select_srcoe)
	private ImageView btnSelectSroce;
	@ViewInject(id = R.id.location_value)
	private TextView locationValue;
	private Onclick l;
	/** 距离配置 */
	private DistanceShared distanceShared;
	private DialogSroceSelect dialogSroceSelect;

	private MyAdapter adapter;

	private LocationApi locationApi;
	private String isRunning;//获取位置信息的线程控制开关
    private Handler handler=new Handler();
	private LocationVo locationVo;
	
	private MyLocationShared mLocationShared;
	private LocationAction locationAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		baseInit();
		

	}
	
	protected void onDestroy() {
		super.onDestroy();
		mLocationShared.commit();
		try {
			handler.removeCallbacks(runnable);
		} catch (Exception e) {
		}
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		isRunning="1";
		if (NetworkUtils.haveInternet(LocationActivity.this)) {
			handler=new Handler();
			handler.post(runnable);
		} else {
			showToast(getResources().getString(R.string.toast_network));
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(KeyEvent.KEYCODE_BACK == keyCode)
		{
			try {
//				IndexTabActivity.getInstance().pointExit();
				sendBroadcast(new Intent(IndexTabActivity.ACTION_EXIT_PEIBAN));
				return false;
			} catch (Exception e) {
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	Runnable runnable=new Runnable() {
		@Override
		public void run() {
			if("1".equals(isRunning)){
				getLocation();
				handler.postDelayed(runnable, 1000);
			}else{
				handler.removeCallbacks(runnable);
			}			
		}
	};
	
	/**
	 * 获取位置信息
	 * 
	 * */
	private void getLocation() {
		final LocationShared locationShared = LocationShared.getInstance(getBaseContext());
		
		if(locationAction == null){
			locationAction = new LocationAction(
				getApplicationContext(), LocationActivity.this);
		}
		
		if (!"".equals(locationShared.getLocationaddr())) {
		    locationValue.setText(locationShared.getLocationaddr());
		    if(locationVo == null){
		    	if(adapter.getCount() == 0){
		    		listView.startLoadMore();
		    	}
		    }
			locationAction.locationStop();
			isRunning="2";
		} else {
			locationAction.locationStart();
		}
	}

	// 公共信息
	@Override
	protected void baseInit() {
		super.baseInit();
		btnSelectLocation.setVisibility(View.GONE);
		
		mLocationShared = new MyLocationShared(getBaseContext());
		locationApi = new LocationApi();
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setXListViewListener(this);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setOnItemClickListener(new ListItemOnClick());
		distanceShared = new DistanceShared(LocationActivity.this);
		locationRelativeDistance = (RelativeLayout) findViewById(R.id.location_relative_distance);
		locationLinearSelect = (LinearLayout) findViewById(R.id.location_linear_select);
		
		
		initUserType();
		
		initSelectView();
		l = new Onclick();
		OnClick();
		initListView();
	}

	/** 初始化根据用户类型初始化用户显示的界面 */
	private void initUserType() {
		if (Constants.CustomerType.CHATTING
				.equals(getMyCustomerVo().getCustomertype())) {
			findPartnerUser();
		} else {
			chttingUser();
		}
	}

	/** 陪聊用户入口 */
	private void chttingUser() {
		dialogSroceSelect = new DialogSroceSelect(LocationActivity.this,
				txtSelectSroce);
		String sroce = mLocationShared.getScoreStr();
		String tag = mLocationShared.getTag();
		btnSelectSroce.setOnClickListener(new SroceSelectOnClick());
		txtSelectSroce.setText(sroce);
		txtSelectSroce.setTag(tag);
	}

	/** 寻伴用户入口 */
	private void findPartnerUser() {
		txtSelectSroce.setText(RYZ);
		txtSelectSroce.setTag("usercp");
		btnSelectSroce.setVisibility(View.GONE);
	}

	/**
	 * 初始化距离筛选控件
	 * */
	private void initDistance() {
		String distancevalue = distanceShared.getDistancevalue();
		if (distancevalue.equals("5")) {
			locationDistance5.setBackgroundResource(R.drawable.distance_press);
			locationDistance10
					.setBackgroundResource(R.drawable.distance_normal);
			locationDistance20
					.setBackgroundResource(R.drawable.distance_normal);
		} else if (distancevalue.equals("10")) {
			locationDistance5.setBackgroundResource(R.drawable.distance_normal);
			locationDistance10.setBackgroundResource(R.drawable.distance_press);
			locationDistance20
					.setBackgroundResource(R.drawable.distance_normal);
		} else if (distancevalue.equals("20")) {
			locationDistance5.setBackgroundResource(R.drawable.distance_normal);
			locationDistance10
					.setBackgroundResource(R.drawable.distance_normal);
			locationDistance20.setBackgroundResource(R.drawable.distance_press);
		} else if (distancevalue.equals("50")) {
			locationDistance5.setBackgroundResource(R.drawable.distance_normal);
			locationDistance10
					.setBackgroundResource(R.drawable.distance_normal);
			locationDistance20
					.setBackgroundResource(R.drawable.distance_normal);
		} else if (distancevalue.equals("0")) {
			locationDistance5.setBackgroundResource(R.drawable.distance_normal);
			locationDistance10
					.setBackgroundResource(R.drawable.distance_normal);
			locationDistance20
					.setBackgroundResource(R.drawable.distance_normal);
		}

	}
	
	private void initSelectView(){
		if(mLocationShared.getLocationCurrModel() == 0){
			useDistanceView();
		}else{
			useScoreView();
		}
	}

	
	/**
	 * 点击事件集合
	 * */
	private void OnClick() {
		btnSelectLocation.setOnClickListener(l);
		txtLocationDistance.setOnClickListener(l);
		txtLocationSocre.setOnClickListener(l);
		locationDistance0.setOnClickListener(l);
		locationDistance5.setOnClickListener(l);
		locationDistance10.setOnClickListener(l);
		locationDistance20.setOnClickListener(l);
		locationDistance50.setOnClickListener(l);
		
	}

	private void initListView() {
		locationVo = getFinalDb().findById(getUserInfoVo().getUid(), LocationVo.class);
		if(locationVo == null){
//			listView.onRefresh();	
		}else{
			try {
				List<CustomerVo> customerVos = JSONArray.parseArray(locationVo.getLocations(), CustomerVo.class);
				adapter.addList(customerVos);
				currPage = (customerVos.size() / pageSize);
				if(currPage == 0){
					currPage = 1;
				}
				listView.setRefreshTime(TextdescTool.getTime(locationVo.getUpdateTime()));
			} catch (Exception e) {
				// 停止加载
			}
		}
	}
	
	/** 按距离拉取 
	 * 当 CURR_PAGE 为 0 时，清除列表数据再插入*/
	private void pullLocationInfo(){
		Log.d(TAG, "pullLocationInfo()");
		if(!checkNetWork()){
			listView.stopRefresh();
			return;
		}
		// 获取本地位置信息.
		LocationShared shared = LocationShared.getInstance(getBaseContext());
		String lat = shared.getLocationlat();
		String lng = shared.getLocationlon();
		//
		if(TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)){
			showToast("没有获取到你的位置信息!");
			stopLoad();
			return;
		}
		// 获取筛选条件
		
		String sort = "asc";
		if(1 == mLocationShared.getDistance()){
			sort = "desc";
		}
		Map<String, String> param = getFilter();
		param.put("sortName", "distance");
		param.put("sort", sort);
		
		locationApi.getDistanceList(getUserInfoVo().getUid(), lat,
				lng, (currPage) + "", pageSize + "", param, new PostLocationInfo());
	}
	
	
	/** 按条件拉取 **/
	private void pullPfLocationInfo(){
		Log.d(TAG, "pullPfLocationInfo()");
		if(!checkNetWork()){
			return;
		}
		
		String sort = "asc";
		if(1 == mLocationShared.getScore()){
			sort = "desc";
		}
		LocationShared shared = LocationShared.getInstance(getBaseContext());
		String lat = shared.getLocationlat();
		String lng = shared.getLocationlon();
		
		if(TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)){
			showToast("没有获取到你的位置信息!");
			stopLoad();
			return;
		}
		
		String sortName = (String) txtSelectSroce.getTag();
		Map<String, String> param = getFilter();
		param.put("sortName", sortName);
		param.put("sort", sort);
		locationApi.getScoreList(getUserInfoVo().getUid(), lat, lng, (currPage) + "", pageSize + "", param, new PostLocationInfo());
		
	}

	@Override
	protected void initTitle() {
		setTitleContent(R.string.location_title);
		setTitleLeft(R.string.location_right);
		setTitleRight(R.string.location_left);
	}

	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		Intent intent = new Intent(this, MapActivity.class);
		startActivity(intent);
		
		
	}
	@Override
	protected void titleBtnLeft() {
		super.titleBtnLeft();
		if(Constants.CustomerType.CHATTING.equals(getMyCustomerVo().getCustomertype())){
			Intent intent = new Intent(this, ConditionFilterVipActivity.class);
			startActivityForResult(intent, REQUEST_TAG);
		}else{
			Intent intent = new Intent(this, ConditionFilterActivity.class);
			startActivityForResult(intent, REQUEST_TAG);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(REQUEST_TAG == requestCode && Activity.RESULT_OK == resultCode){
			onRefresh();
		}
	}

	private void useDistanceView(){
		viewSelectDistance.setBackgroundResource(R.drawable.btn_cz_normal);
		if (1 == mLocationShared.getDistance()) {
			imgLocaionSort.setImageResource(R.drawable.location_desc);
		} else{
			imgLocaionSort.setImageResource(R.drawable.location_asc);
		}
		
		unuseScoreView();
	}
	
	private void useScoreView(){
		viewSelectScore.setBackgroundResource(R.drawable.btn_cz_normal);
		if (1 == mLocationShared.getScore()) {
			imgSorceSort.setImageResource(R.drawable.location_desc);
		} else{
			imgSorceSort.setImageResource(R.drawable.location_asc);
		}
		
		unuseDistanceView();
	}
	
	private void unuseDistanceView(){
		viewSelectDistance.setBackgroundResource(R.drawable.btn_cz_pressed);
		if (1 == mLocationShared.getDistance()) {
			imgLocaionSort.setImageResource(R.drawable.location_desc_h);
		} else{
			imgLocaionSort.setImageResource(R.drawable.location_asc_h);
		}
	}
	
	private void unuseScoreView(){
		viewSelectScore.setBackgroundResource(R.drawable.btn_cz_pressed);
		if (1 == mLocationShared.getScore()) {
			imgSorceSort.setImageResource(R.drawable.location_desc_h);
		} else{
			imgSorceSort.setImageResource(R.drawable.location_asc_h);
		}
	}
	

	/**
	 * 
	 * 功能： 评分选择<br />
	 * 日期：2013-5-31<br />
	 * 地点：淘高手网<br />
	 * 版本：ver 1.0<br />
	 * 
	 * @author fighter
	 * @since
	 */
	class DialogSroceSelect extends DialogEditDataModel {
		private TextView texSex;
		private int[] ids = new int[] { R.id.dialog_sex_select_btn_man,
				R.id.dialog_sex_select_btn_woman,
				R.id.dialog_account_type_btn_chatting };
		private int[] strs = new int[] { R.string.location_zhpf,
				R.string.location_fwpf, R.string.location_wmpf };

		private View.OnClickListener l = new DialogSexSelectBtnLiener();

		/**
		 * 
		 * @param context
		 *            上下文
		 * @param textSex
		 *            选择后文字显示的位置
		 */
		public DialogSroceSelect(Context context, TextView textSex) {
			super(context);
			this.texSex = textSex;
			this.setTitle("选择评分类型");
			initWidget();
		}

		private void initWidget() {
			makeButton(ids, strs, l);
		}

		void zhpfAction(Button btn) {
			texSex.setText(btn.getText());
			texSex.setTag("score");
			
			useScoreView();
			onRefresh();
			cancel();
		}

		void fwpfAction(Button btn) {
			texSex.setText(btn.getText());
			texSex.setTag("sscore");
			useScoreView();
			onRefresh();
			cancel();
		}

		void wmpfAction(Button btn) {
			texSex.setText(btn.getText());
			texSex.setTag("ascore");
			useScoreView();
			onRefresh();
			cancel();
		}

		class DialogSexSelectBtnLiener implements View.OnClickListener {

			@Override
			public void onClick(View v) {
				Button btn = (Button) v;
				int id = v.getId();
				switch (id) {
				case R.id.dialog_sex_select_btn_man:
					zhpfAction(btn);
					break;
				case R.id.dialog_sex_select_btn_woman:
					fwpfAction(btn);
					break;
				case R.id.dialog_account_type_btn_chatting:
					wmpfAction(btn);
					break;

				default:
					break;
				}
			}

		}
	}

	class MyAdapter extends ObjectBaseAdapter<CustomerVo> {

		@Override
		public void addList(List<CustomerVo> lists) {
			if(lists == null){
				return;
			}
			getLists().addAll(lists);
			notifyDataSetInvalidated();
			
			// TODO  在adpater中添加数据及时判断是否更新
//			int page = currPage + 1;
//			if(adapter.getCount() < (page * pageSize -1) && adapter.getCount() != 0){
//				listView.setPullLoadEnable(false);
//			}else{
//				listView.setPullLoadEnable(true);
//			}
		}

		@Override
		public void addObject(CustomerVo t) {
			lists.add(t);
		}
		
		@Override
		public CustomerVo getItem(int position) {
			return super.getItem(position);
		}
		
		public List<CustomerVo> getLists(){
			return lists;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			if (convertView != null) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(getBaseContext()).inflate(
						R.layout.location_list_item, null);
				viewHolder = ViewHolder.getInstance(convertView);
			}

			convertView.setTag(viewHolder);
			bindView(viewHolder, position);
			return convertView;
		}

		private void bindView(ViewHolder viewHolder, int position) {
			CustomerVo customerVo = getItem(position);
			if("1".equals(customerVo.getSex())){
				viewHolder.imgSex.setImageResource(R.drawable.sex_man);
			}else{
				viewHolder.imgSex.setImageResource(R.drawable.sex_woman);
			}
			
			FinalOnloadBitmap.finalDisplay(getBaseContext(), customerVo, viewHolder.imgHead, getHeadBitmap());
			
			String userName = TextdescTool.getCustomerName(customerVo);
			
			String age = customerVo.getBirthday();
			String location = customerVo.getLocal();
			String line = customerVo.getOnline();
			String sign = customerVo.getSign();
			String wages = customerVo.getSalary();
			
			viewHolder.txtUsername.setText(userName);
			viewHolder.txtLocation.setText(location);
			viewHolder.txtWages.setText(wages);
			viewHolder.txtAge.setText(TextdescTool.dateToAge(age) + "岁");
			viewHolder.txtLocation.setText(location);
//			boolean flag = TextdescTool.getOnline(line);
			viewHolder.txtOnline.setText(line);
			String distance = "0km";
			try {
				distance = (TextdescTool.floatMac1(Float.parseFloat(customerVo.getDistance()) / 1000)) + "km";
			} catch (Exception e) {
			}
			viewHolder.txtLocation.setText(distance);
			String height = customerVo.getHeight();
			String weight = customerVo.getWeight();
			
			if(!TextUtils.isEmpty(height) && !"0".equals(height)){
				viewHolder.txtHeight.setText(height + "cm");
			}
			
			if(!TextUtils.isEmpty(weight) && !"0".equals(weight)){
				viewHolder.txtBody.setText(weight + "kg");
			}
			
			String type = Constants.CustomerType.getServiceType(customerVo.getOccasions());
			if ("社交".equals(type)) {
				viewHolder.txtType.setTextColor(getResources().getColor(R.color.green));
			}else{
				viewHolder.txtType.setTextColor(getResources().getColor(R.color.fense));
			}
			viewHolder.txtType.setText(type);
			
			viewHolder.txtSign.setText(sign);
			
			
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
				String mscore = "0";
				String sscore = "0";
				String ascore = "0";
				String score = "0";
				
				try {
					sscore = TextdescTool.floatMac(customerVo.getSscore());
				} catch (Exception e) {
				}
				try {
					ascore = TextdescTool.floatMac(customerVo.getAscore());
				} catch (Exception e) {
				}
				
				try {
					score = TextdescTool.floatMac(customerVo.getScore());
				} catch (Exception e) {
				}
				
				
				String serviceType = (String) txtSelectSroce.getTag();
				if("sscore".equals(serviceType)){
					mscore = sscore;
				}else if("ascore".equals(serviceType)){
					mscore = ascore;
				}else{
					mscore = score;
				}
				viewHolder.ratingSorce.setVisibility(View.VISIBLE);
				viewHolder.txtSorce.setVisibility(View.VISIBLE);
				
				viewHolder.txtClassSorce.setVisibility(View.GONE);
				viewHolder.linearLayout.setVisibility(View.GONE);
				try {
					viewHolder.ratingSorce.setRating(Float.parseFloat(mscore));
				} catch (Exception e) {
					viewHolder.ratingSorce.setRating(0);
				}
				
				viewHolder.txtSorce.setText(mscore);
			}else{
				// 是否是VIP
				if("1".equals(customerVo.getVip())){
					viewHolder.imgSubscript.setImageResource(R.drawable.subscript_vip);
					viewHolder.imgSubscript.setVisibility(View.VISIBLE);
				}else{
					viewHolder.imgSubscript.setVisibility(View.GONE);
					
				}
				int score = 0;
				try {
					score = Integer.parseInt(customerVo.getUsercp());
				} catch (Exception e) {
				}
				viewHolder.linearLayout.removeAllViews();
				
				viewHolder.txtClassSorce.setVisibility(View.VISIBLE);
				viewHolder.linearLayout.setVisibility(View.VISIBLE);
				
				viewHolder.ratingSorce.setVisibility(View.GONE);
				viewHolder.txtSorce.setVisibility(View.GONE);
				
				viewHolder.txtClassSorce.setText(score + "");
				int usercpNum = Integer.parseInt(customerVo.getUsercp()) / 5;
				int usercpMod = Integer.parseInt(customerVo.getUsercp()) % 5;
				int hg = usercpNum / 20;
				int hz = usercpNum % 20 / 5;
				int xx = usercpNum % 20 % 5;
				
				if(usercpMod > 0){
					xx = xx + 1;
				}
				
				for (int i = 0; i < hg; i++) {
					ImageView imageView = getClassImage();
					imageView.setImageResource(R.drawable.class0);
					if(!addClassImage(viewHolder, imageView)){
						return;
					};
				}
				for (int i = 0; i < hz; i++) {
					ImageView imageView = getClassImage();
					imageView.setImageResource(R.drawable.class1);
					if(!addClassImage(viewHolder, imageView)){
						return;
					};
				}
				for (int i = 0; i < xx; i++) {
					ImageView imageView = getClassImage();
					imageView.setImageResource(R.drawable.class2);
					if(!addClassImage(viewHolder, imageView)){
						return;
					};
				}
				
			}
		}
		
		private boolean addClassImage(ViewHolder viewHolder, ImageView image){
			int index = viewHolder.linearLayout.getChildCount();
			if(index < 6){
				viewHolder.linearLayout.addView(image);
				return true;
			}else{
				return false;
			}
		}
		private ImageView getClassImage(){
			ImageView imageView = (ImageView) LayoutInflater.from(getBaseContext()).inflate(R.layout.class_image, null);
		
			return imageView;
		}
	}

	static class ViewHolder {
		/** 角标 */
		public ImageView imgSubscript; // 角标
		/** 头像 */
		public ImageView imgHead; // 头像
		/** 昵称 */
		public TextView txtUsername;
		/** 聊天类型 */
		public TextView txtType;
		/** 分数 */
		public TextView txtSorce, txtClassSorce;
		/** 年龄 */
		public ImageView imgSex;
		/** 年龄 */
		public TextView txtAge;
		/** 身高 */
		public TextView txtHeight;
		/** 体重 */
		public TextView txtBody;
		/** 距离 */
		public TextView txtLocation;
		/** 是否在线 */
		public TextView txtOnline;
		/** 签名 */
		public TextView txtSign, txtWages;

		public RatingBar ratingSorce;
		
		public LinearLayout linearLayout;

		public static ViewHolder getInstance(View view) {
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.txtAge = (TextView) view.findViewById(R.id.list_txt_age);
			viewHolder.txtOnline = (TextView) view
					.findViewById(R.id.list_txt_state);
			viewHolder.txtUsername = (TextView) view
					.findViewById(R.id.list_txt_title);
			viewHolder.txtSign = (TextView) view
					.findViewById(R.id.list_txt_message_info);
			viewHolder.txtHeight = (TextView) view
					.findViewById(R.id.list_txt_height);
			viewHolder.txtBody = (TextView) view
					.findViewById(R.id.list_txt_body);
			viewHolder.txtWages = (TextView) view
					.findViewById(R.id.list_txt_wages);
			viewHolder.txtLocation = (TextView) view
					.findViewById(R.id.list_txt_location);
			viewHolder.txtType = (TextView) view
					.findViewById(R.id.list_txt_type);
			viewHolder.txtSorce = (TextView) view
			.findViewById(R.id.list_txt_fen);
			viewHolder.txtClassSorce = (TextView) view
			.findViewById(R.id.list_txt_classfen);
			
			viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.list_layout_class);
			
			viewHolder.imgHead = (ImageView) view
					.findViewById(R.id.list_item_img_head);
			viewHolder.imgSex = (ImageView) view
					.findViewById(R.id.list_img_sex);
			viewHolder.imgSubscript = (ImageView) view
					.findViewById(R.id.list_item_img_subscript);

			viewHolder.ratingSorce = (RatingBar) view
					.findViewById(R.id.list_layout_evaluate);

			return viewHolder;
		}

		/** 评分条 */
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
	class PostLocationInfo extends AjaxCallBack<String> {

		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
			stopLoad();
			
			String data = ErrorCode.getData(getBaseContext(), t);
			Log.d(TAG, "DATA:"+data);
			if(data != null){
				if("".equals(data)){
					data = "[]";
					listView.setPullLoadEnable(false);
				}
				String currTime = System.currentTimeMillis()+"";
				if(locationVo == null){
					locationVo = new LocationVo();
					locationVo.setUid(getUserInfoVo().getUid());
					locationVo.setCreateTime(currTime);
					locationVo.setState(0);
				}else{
					locationVo.setState(1);
				}
				locationVo.setUpdateTime(currTime);
				locationVo.setLocations(data);
				
				listView.setRefreshTime("上次刷新:" + TextdescTool.getTime(currTime));
				try {
					List<CustomerVo> customerVos = JSON.parseArray(data, CustomerVo.class);
					if (currPage <= 1) {
						adapter.getLists().clear();
					}
					adapter.addList(customerVos);
				} catch (Exception e) {
					stopLoad();
				}
				
				try {
					data = JSONArray.toJSON(adapter.getLists()).toString();
					locationVo.setLocations(data);
				} catch (Exception e) {
				}
				
				if(0 == locationVo.getState()){
					getFinalDb().save(locationVo);
				}else{
					getFinalDb().update(locationVo);	
				}
				
			}else{
				stopLoad();
				listView.setPullLoadEnable(false);
			}
			
		}
		@Override
		public void onFailure(Throwable t, String strMsg) {
			super.onFailure(t, strMsg);
			showToast("发生错误!");
			stopLoad();
		}

	}

	class Onclick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_select_location:
//				initDistance();
//				locationLinearSelect.setVisibility(View.GONE);
//				locationRelativeDistance.setVisibility(View.VISIBLE);
				break;
			case R.id.location_txt_distance:
				onClickDistance();
				break;
			case R.id.location_txt_comprehensivescore:
				onClickScore();
				break;
//			case R.id.location_distance_5:
//				filterDistance("5");
//				break;
//			case R.id.location_distance_10:
//				filterDistance("10");
//				break;
//			case R.id.location_distance_20:
//				filterDistance("20");
//				break;
//			case R.id.location_distance_50:
//				filterDistance("50");
//				break;
			}
		}
		
		private void filterDistance(String num){
			locationLinearSelect.setVisibility(View.VISIBLE);
			locationRelativeDistance.setVisibility(View.GONE);
			distanceShared.setDistancevalue(num);
			
			mLocationShared.setLocationCurrModel(0);
			useDistanceView();
			onRefresh();
		}
		
		// 点击距离
		private void onClickDistance(){
			// 如果当前的模式为
			if(0 == mLocationShared.getLocationCurrModel()){
				setDistanceSortBg();
			}else{
				useDistanceView();
				mLocationShared.setLocationCurrModel(0);
			}
			
			// 开始距离搜索.
			onRefresh();
		}
		
		// 点击评分
		private void onClickScore(){
			if(1 == mLocationShared.getLocationCurrModel()){
				setScoreSortBg();
			}else{
				useScoreView();
				mLocationShared.setLocationCurrModel(1);
			}
			
			String scoreStr = txtSelectSroce.getText().toString();
			String tag = (String) txtSelectSroce.getTag();
			
			if(!RYZ.equals(scoreStr)){
				mLocationShared.setScoreStr(scoreStr);
				mLocationShared.setTag(tag);
			}
			
			// 开始评分搜索
			onRefresh();
		}

	}
	/**
	 * 根据点击次数 显示升序降序按钮 相应排序 也在此处
	 * */
	public void setScoreSortBg() {
		if (0 == mLocationShared.getScore()) {
			imgSorceSort.setImageResource(R.drawable.location_desc);
			mLocationShared.setScore(1);
		} else {
			imgSorceSort.setImageResource(R.drawable.location_asc);
			mLocationShared.setScore(0);
		}
	}
	
	/**
	 * 根据点击次数 显示升序降序按钮 相应排序 也在此处
	 * */
	public void setDistanceSortBg() {
		if (0 == mLocationShared.getDistance()) {
			imgLocaionSort.setImageResource(R.drawable.location_desc);
			mLocationShared.setDistance(1);
		} else{
			imgLocaionSort.setImageResource(R.drawable.location_asc);
			mLocationShared.setDistance(0);
		}
	}

	class SroceSelectOnClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == btnSelectSroce) {
				dialogSroceSelect.show();
			}
		}

	}

	class ListItemOnClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CustomerVo customerVo = adapter.getItem(position - 1);
			if(customerVo != null){
				Intent intent = null;
				if(Constants.CustomerType.CHATTING.equals(customerVo.getCustomertype()) && "1".equals(customerVo.getFriend())){
					intent = new Intent(LocationActivity.this, FriendChattingActivity.class);
					//intent = new Intent(LocationActivity.this, UserBaseActivity.class);
				}else if(Constants.CustomerType.WITHCHAT.equals(customerVo.getCustomertype()) && "1".equals(customerVo.getFriend())){
					intent = new Intent(LocationActivity.this, FriendFindPartnerActivity.class);
				}else if(Constants.CustomerType.WITHCHAT.equals(customerVo.getCustomertype())){
					intent = new Intent(LocationActivity.this, StrangerFindPartnerActivity.class);
					//intent = new Intent(LocationActivity.this, UserBaseActivity.class);
				}else{
					//intent = new Intent(LocationActivity.this, StrangerChattingActivity.class);
					intent = new Intent(LocationActivity.this, UserBaseActivity.class);
				}
				intent.putExtra("data", customerVo);
				
				startActivity(intent);
			}
		}
		
	}
	
	/**
	 * 获取距离筛选条件 
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-6-8<br />
	 * 修改时间:<br />
	 */
	private String getDistance(){
		DistanceShared distanceShared = new DistanceShared(getBaseContext());
		return distanceShared.getDistancevalue();
	}
	
	/**
	 * 获取筛选条件
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-6-7<br />
	 * 修改时间:<br />
	 */
	private Map<String, String> getFilter(){
		FilterConfig filterConfig = new FilterConfig(getBaseContext());
		Map<String, String> parmas = new HashMap<String, String>();
		// 公用部分
		// 性别
		String sex = filterConfig.getSex();
		parmas.put("sex", sex);
		// 登录时间
		String onlinetime = filterConfig.getDayTime();
		parmas.put("onlinetime", onlinetime);
		
		String radius = filterConfig.getDistancetwo();
		String sradius = filterConfig.getDistanceone();
		if(TextUtils.isEmpty(radius) || "0".equals(radius)){
			radius = "50000";
		}else{
			radius = radius + "000";
		}
		
		if(TextUtils.isEmpty(sradius) || "0".equals(sradius)){
			sradius = "0";
		}else{
			sradius = sradius + "000";
		}
		parmas.put("radius", radius);
		parmas.put("sradius", sradius);
		
		
		if (Constants.CustomerType.WITHCHAT.equals(getMyCustomerVo()
				.getCustomertype())) {
			// 陪聊用户
			String occasions = filterConfig.getService();
			parmas.put("occasions", occasions);
			String headattest = filterConfig.getAuth();
			parmas.put("headattest", headattest);
			String age = filterConfig.getAgeone() + ","
					+ filterConfig.getAgetwo();
			getFilter2Filter(parmas, "age", age);

			String height = filterConfig.getTallone() + ","
					+ filterConfig.getTalltwo();
			getFilter2Filter(parmas, "height", height);
			String weight = filterConfig.getWeightone() + ","
					+ filterConfig.getWeighttwo();
			getFilter2Filter(parmas, "weight", weight);
			String agent = filterConfig.getBroker();
			getFilter2Filter(parmas, "agent", agent);
			String score = filterConfig.getComposite();
			getFilter2Filter(parmas, "score", score);
		} else {
			// 寻伴
			String vip = filterConfig.getVip();
			parmas.put("vip", vip);
			String usercp = filterConfig.getHonorone() + ","
					+ filterConfig.getHonortwo();
			getFilter2Filter(parmas, "usercp", usercp);
		}
		return parmas;
	}
	
	private void getFilter2Filter(Map<String, String> param, String key, String value){
		if(!"0,0".equals(value)){
			param.put(key, value);
		}
	}
	// 停止加载
	private void stopLoad(){
		listView.stopLoadMore();
		listView.stopRefresh();
	}
	
	@Override
	public void onLoadMore() {
		getPhotoBitmap().flushCache();
//		int page = currPage + 1;
//		if(adapter.getCount() < (page * pageSize -1) && adapter.getCount() != 0){
//			stopLoad();
//			return;
//		}
//		
//		int index = (adapter.getCount() / pageSize - 1);
//		if(currPage >= index && index != 0){
//			stopLoad();
//			return;
//		}
//		
//		currPage = index;
		currPage ++;
		
		if(0 == mLocationShared.getLocationCurrModel()){
			pullLocationInfo();
		}else{
			pullPfLocationInfo();
		}
	}
	
	@Override
	public void onRefresh() {
		getPhotoBitmap().flushCache();
		currPage = 1;
		listView.setPullLoadEnable(true);
		if(0 == mLocationShared.getLocationCurrModel()){
			pullLocationInfo();
		}else{
			pullPfLocationInfo();
		}
		
	}
	
	class MyLocationShared{
		private static final String LOCATION_MODEL_NAME = "location_model";
		/** 当前选择的模式 0 距离, 1 评分 */
		protected static final String LOCATION_CURR_SELECT_KEY = "lcskey";
		/** 距离的升序和降序模式 */
		protected static final String LOCATION_DISTANCE_MODEL = "ldm";
		/** 评分升序和降序 */
		protected static final String LOCATION_SCORE_MODEL = "lsm";
		protected static final String LOCATION_SCORE_STR = "lss";
		protected static final String LOCATION_SCORE_TAG = "lst";
		
		private SharedPreferences sharedPreferences;
		private Editor editor;
		public MyLocationShared(Context context) {
			super();
			this.sharedPreferences = SharedStorage.getSharedPreferences(context, LOCATION_MODEL_NAME);
			this.editor = this.sharedPreferences.edit();
			
			init();
		}
		
		private int locationCurrModel;		// 当前选择的模式
		private int distance;				// 距离的排序 0 升序， 1 为降序
		private int score;					// 综合评分 0 , 1
		
		private String scoreStr;
		private String tag;
		
		private void init(){
			this.locationCurrModel = sharedPreferences.getInt(LOCATION_CURR_SELECT_KEY, 0);
			this.distance = sharedPreferences.getInt(LOCATION_DISTANCE_MODEL, 0);
			this.score = sharedPreferences.getInt(LOCATION_SCORE_MODEL, 0);
			this.scoreStr = sharedPreferences.getString(LOCATION_SCORE_STR, "综合评分");
			this.tag = sharedPreferences.getString(LOCATION_SCORE_TAG, "score");
		}
		public Editor getEditor() {
			return editor;
		}
		public int getLocationCurrModel() {
			return locationCurrModel;
		}
		public int getDistance() {
			return distance;
		}
		public int getScore() {
			return score;
		}
		

		public String getScoreStr() {
			return scoreStr;
		}
		public String getTag() {
			return tag;
		}
		public void setScoreStr(String scoreStr) {
			this.scoreStr = scoreStr;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		public void setEditor(Editor editor) {
			this.editor = editor;
		}

		public void setLocationCurrModel(int locationCurrModel) {
			this.locationCurrModel = locationCurrModel;
		}
		
		public void setDistance(int distance) {
			this.distance = distance;
		}
		
		public void setScore(int score) {
			this.score = score;
		}
		
		public boolean commit(){
			this.editor.putInt(LOCATION_CURR_SELECT_KEY, this.locationCurrModel);
			this.editor.putInt(LOCATION_SCORE_MODEL, this.score);
			this.editor.putInt(LOCATION_DISTANCE_MODEL, this.distance);
			this.editor.putString(LOCATION_SCORE_STR, scoreStr);
			this.editor.putString(LOCATION_SCORE_TAG, tag);
//			System.out.println("locationCurrModel:" + locationCurrModel);
//			System.out.println("score:" + score);
//			System.out.println("distance:" + distance);
//			System.out.println("scoreStr:" + scoreStr);
//			System.out.println("locationCurrModel:" + locationCurrModel);
			return editor.commit();
		}
		
	}
	
}
