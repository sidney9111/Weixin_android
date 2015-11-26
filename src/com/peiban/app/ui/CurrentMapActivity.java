package com.peiban.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.peiban.R;
import com.peiban.app.LocationShared;
import com.peiban.application.PeibanApplication;

/**
 * 功能：好友位置显示<br />
 * 日期：2013-5-9<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author lcy
 * @since
 */
public class CurrentMapActivity extends BaseActivity {
	// 地图相关
	@ViewInject(id = R.id.bmapView)
	private MapView mMapView = null;
	private MapController mMapController = null;
	private MKMapViewListener mMapListener = null;
	private MKSearch mksearch = null;
	private GeoPoint gp = null;
	// 定位相关
	private LocationClient mLocClient;
	private MyLocationOverlay myLocationOverlay = null;
	private LocationData locData = null;
	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	// 全局变量
	private PeibanApplication app;
	// 组件相关
	@ViewInject(id = R.id.myloc_txt)
	private TextView myloc;
	// 属性
	private String city, address, lat, lon, tag;// tag---"1"查看自己定位，""查看好友发的定位图
	private Handler handler = new Handler();
//	private String mapbit;  // 地图的图片位置
	private LocationShared locationShared;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (PeibanApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(PeibanApplication.strKey,
					new PeibanApplication.MyGeneralListener());
		}
		setContentView(R.layout.activity_map);
		baseInit();
		initMap();
		drawloc();
	}

	/** 画出位置 */
	private void drawloc() {
		// TODO Auto-generated method stub
		mMapView.getOverlays().clear();
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
		handler.postDelayed(runnable, 1000);
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				mMapView.getOverlays().clear();
				locData.latitude = Double.valueOf(lat);
				locData.longitude = Double.valueOf(lon);
				setTitleContent(city);
				myloc.setText(address);
				myLocationOverlay.setData(locData);
				myLocationOverlay.enableCompass();
				mMapView.getOverlays().add(myLocationOverlay);
				mMapView.refresh();
				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
			} catch (Exception e) {
			}
		}
	};

	/** 初始化地图相关 */
	private void initMap() {
		locationShared = LocationShared.getInstance(CurrentMapActivity.this);
//		friendname = getIntent().getExtras().getString("friendname");
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			tag = bundle.getString("tag");
		}
		
		if ("1".equals(tag)) {
			lat = getIntent().getExtras().getString("lat");
			lon = getIntent().getExtras().getString("lon");
			city = getIntent().getExtras().getString("city");
			address = getIntent().getExtras().getString("address");
		} else {
			lat = locationShared.getLocationlat();
			lon = locationShared.getLocationlon();
			city = locationShared.getLocationcity();
			address = locationShared.getLocationaddr();
		}
		
		if (!"1".equals(tag)) {
			setTitleRight(getResources().getString(R.string.map_send));
		}
		
		myloc.setText(address);
		mMapController = mMapView.getController();
		mMapController.setZoom(14);
		mMapController.enableClick(true);
		/** 监听mapview */
		mMapView.setLongClickable(true);
		mMapView.regMapViewListener(
				PeibanApplication.getInstance().mBMapManager,
				mMapListener);
		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(CurrentMapActivity.this, title,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
			}

			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub

			}
		};
	}
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		if (mLocClient != null)
			mLocClient.stop();
		mMapView.destroy();
		PeibanApplication app = (PeibanApplication) this
				.getApplication();
		if (app.mBMapManager != null) {
			app.mBMapManager.destroy();
			app.mBMapManager = null;
		}
		super.onDestroy();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		
		setTitleContent("");
		setBtnBack();
	}

	@Override
	protected void titleBtnBack() {
		super.titleBtnBack();
	}

	/**
	 * 发送我的位置信息
	 * */
	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
//		if(mMapView.getCurrentMap()){
//			mapbit = new StringBuilder()
//				.append(com.baidu.mapapi.utils.a.h())
//				.append("/BaiduMapSDK/capture.png").toString();
//				
//		}
//		if(TextUtils.isEmpty(mapbit)){
//			showToast("获取地图图片错误!");
//			return;
//		}
		Intent intent = new Intent();
		intent.putExtra("city", city);
		intent.putExtra("addr", address);
//		intent.putExtra("mapbit", mapbit);
		intent.putExtra("lat", lat);
		intent.putExtra("lon", lon);
		setResult(RESULT_OK, intent);
		finish();
	}
}