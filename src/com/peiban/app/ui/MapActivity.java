package com.peiban.app.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.peiban.R;
import com.peiban.app.LocationShared;
import com.peiban.app.action.LocationAction;
import com.peiban.application.PeibanApplication;

/**
 * 功能：百度地图 <br />
 * 日期：2013-5-9<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author lcy
 * @since
 */
public class MapActivity extends BaseActivity {
	// 地图相关
	@ViewInject(id = R.id.bmapView)
	private MapView mMapView = null;
	private MapController mMapController = null;
	private MKMapViewListener mMapListener = null;
	private MKSearch mksearch = null;
	private GeoPoint gp = null;
	// 定位相关

	private LocationClient mLocClient;
	private LocationClientOption option;
	private MyLocationOverlay myLocationOverlay = null;
	private LocationData locData = null;
	public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	// 全局变量
	private PeibanApplication app;
	// 组件相关
	@ViewInject(id = R.id.myloc_txt)
	private TextView myloc;
	// 属性
	private String city, address;
	private Handler handler = new Handler();
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		LocationAction locationAction = new LocationAction(
				getApplicationContext(), MapActivity.this);
		locationAction.locationStop();
		int x = (int) event.getX();
		int y = (int) event.getY();
		gp = mMapView.getProjection().fromPixels(x, y);
		GeoPoint drawgp = mMapView.getProjection().fromPixels(x, y - 150);
		drawEventLoc(drawgp);
		mksearch.reverseGeocode(drawgp);
		return super.onTouchEvent(event);
	}

	/** 根据点击点显示位置 */
	private void drawEventLoc(GeoPoint gp) {
		try {
			mMapView.getOverlays().clear();
			Drawable marker = getResources().getDrawable(R.drawable.icon_marka);
			ItemizedOverlay<OverlayItem> iov = new ItemizedOverlay<OverlayItem>(
					marker, mMapView);
			iov.removeAll();
			OverlayItem ovitem = new OverlayItem(gp, "", "");
			iov.addItem(ovitem);
			mMapView.getOverlays().add(iov);
			mMapView.refresh();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/** 画出位置 */
	private void drawloc() {
		// TODO Auto-generated method stub
		locationShared = LocationShared.getInstance(MapActivity.this);
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
				locData.latitude = Double.valueOf(locationShared
						.getLocationlat());
				locData.longitude = Double.valueOf(locationShared
						.getLocationlon());
				setTitleContent(locationShared.getLocationcity());
				myloc.setText(locationShared.getLocationaddr());
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
					Toast.makeText(MapActivity.this, title, Toast.LENGTH_SHORT)
							.show();
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub

			}
		};
		mksearch = new MKSearch();
		mksearch.init(app.mBMapManager, new MymksearchListener());
	}

	/** 获得城市回调信息 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			if (data != null) {
				city = data.getExtras().getString("city");
				locData.longitude=Double.valueOf(data.getExtras().getString("lon"));
				locData.latitude=Double.valueOf(data.getExtras().getString("lat"));
				if (!TextUtils.isEmpty(city)) {
					GeoPoint geoPoint=new GeoPoint((int)(locData.latitude*1e6), (int)(locData.longitude*1e6));
					System.out.println("geoPoint:"+geoPoint);
					mksearch.reverseGeocode(geoPoint);
				} else {
					showToast(getResources().getString(R.string.toast_nocity));
				}
			}

		}
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

	/**
	 * 监听函数，将查询结果显示在地图上
	 */
	public class MymksearchListener implements MKSearchListener {
		/** 获得地址信息 */
		@Override
		public void onGetAddrResult(MKAddrInfo res, int error) {
			// TODO Auto-generated method stub
			if (res == null) {
				return;
			}
			if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
				if (TextUtils.isEmpty(res.addressComponents.city)) {
					setTitleContent("");
				} else {
					setTitleContent(res.addressComponents.city);
				}
				myloc.setText(res.strAddr);
				locData.latitude = res.geoPt.getLatitudeE6() / 1e6;
				locData.longitude = res.geoPt.getLongitudeE6() / 1e6;
				city = res.addressComponents.city;
				address = res.strAddr;
				mMapView.refresh();
				mMapView.getController().animateTo(res.geoPt);
			} else{
				System.out.println("geopt:"+res.geoPt);
				mksearch.reverseGeocode(res.geoPt);
				mMapView.refresh();
				mMapView.getController().animateTo(res.geoPt);
				
			}
		}

		/** 获得公交线路信息 */
		@Override
		public void onGetBusDetailResult(MKBusLineResult res, int error) {
			// TODO Auto-generated method stub
		}

		/** 获得驾车信息 */
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
		}

		/** 获得兴趣点详细信息 */
		@Override
		public void onGetPoiDetailSearchResult(int res, int error) {
			// TODO Auto-generated method stub
		}

		/** 获得兴趣点信息 */
		@Override
		public void onGetPoiResult(MKPoiResult res, int error, int error2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult res, int error) {
			// TODO Auto-generated method stub

		}

		/** 获得公交信息 */
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
			// TODO Auto-generated method stub
		}

		/** 获得步行信息 */
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleContent("");
		setTitleLeft(getResources().getString(R.string.map_myloc));
		setTitleRight(getResources().getString(R.string.map_customloc));
		setTitleImg(R.drawable.city);
	}

	@Override
	protected void titleBtnLeft() {
		super.titleBtnLeft();
		LocationAction locationAction = new LocationAction(getApplication(),
				MapActivity.this);
		locationAction.locationStart();
		drawloc();
	}

	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		LocationShared locationShared = LocationShared.getInstance(MapActivity.this);
		locationShared.setLocationaddr(address);
		locationShared.setLocationlat(String.valueOf(locData.latitude));
		locationShared.setLocationlon(String.valueOf(locData.longitude));
		locationShared.setLocationcity(city);
		locationShared.commitLoc();
		finish();
	}

	@Override
	protected void titleOnClick() {
		super.titleOnClick();
		Intent intent = new Intent(MapActivity.this, CityActivity.class);
		startActivityForResult(intent, 1);
	}
}
