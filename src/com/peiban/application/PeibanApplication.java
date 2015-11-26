package com.peiban.application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.peiban.app.FinalFactory;
import com.peiban.app.cache.UserInfoCache;
import com.peiban.app.control.FriendListAction;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.UserInfoVo;

public class PeibanApplication extends Application {

	private static PeibanApplication mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;

	private int localVersion = -1;
	
	public static final String strKey = "EB780B06F1C4BFCEBF70BD985313C05D6C6B5DEF";

	// private Map<String, Object> extras = new HashMap<String, Object>();
	private UserInfoVo userInfoVo;
	private CustomerVo customerVo;
	public static String downloadDir = "shangwupanlv/";// 安装目录
	
	private Bitmap bitmap;   // 使用application 传递 bitmap
	private FriendListAction friendListAction;
	
	private List<Activity> history = new ArrayList<Activity>() {

		@Override
		public boolean add(Activity object) {
			if (this.contains(object)) {
				return true;
			}
			return super.add(object);
		}
	};
	
	private Activity activity;
	
	
	public Activity getActivity() {
		return activity;
	}


	public void setActivity(Activity activity) {
		mInstance = this;
		this.activity = activity;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initEngineManager(this);
	}

	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onTerminate();
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			Toast.makeText(getApplicationContext(), "BMapManager  初始化错误!",
					Toast.LENGTH_LONG).show();
		}
	}

	public static PeibanApplication getInstance() {
		return mInstance;
	}

	
	
	// public void putExtras(String key, Object value){
	// extras.put(key, value);
	// }
	//
	// public Object getExtra(String key){
	// return extras.get(key);
	// }

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getLocalVersion() {
		try {
			PackageManager packageManager = this.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			localVersion = packageInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localVersion;
	}

	/**
	 * 添加历史页!
	 * 
	 * @param activity
	 *            作者:fighter <br />
	 *            创建时间:2013-5-24<br />
	 *            修改时间:<br />
	 */
	public void addHistoryActivity(Activity activity) {
		if (activity.isFinishing()) {
			return;
		}
		history.add(activity);
	}

	public void removeHistoryActivity(Activity activity) {
		activity.finish();
		history.remove(activity);
	}

	public void removeAllHistoryActivity() {
		Iterator<Activity> iter = history.iterator();
		while (iter.hasNext()) {
			Activity activity = iter.next();
			history.remove(activity);
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}

	public UserInfoVo getUserInfoVo() {
		if (userInfoVo == null) {
			userInfoVo = new UserInfoCache(getBaseContext()).getCacheUserInfo();
		}
		return userInfoVo;
	}

	public void setUserInfoVo(UserInfoVo userInfoVo) {
		this.userInfoVo = userInfoVo;
	}

	public CustomerVo getCustomerVo() {
		if(customerVo == null){
			customerVo = FinalFactory.createFinalDb(getBaseContext(),
					getUserInfoVo()).findById(getUserInfoVo().getUid(), CustomerVo.class);
		}
		return customerVo;
	}

	public void setCustomerVo(CustomerVo customerVo) {
		this.customerVo = customerVo;
	}

	public FriendListAction getFriendListAction() {
		if(friendListAction == null){
			friendListAction = new FriendListAction(getBaseContext(), getUserInfoVo(), null);
		}
		return friendListAction;
	}

	public void setFriendListAction(FriendListAction friendListAction) {
		this.friendListAction = friendListAction;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(
						PeibanApplication.getInstance()
								.getApplicationContext(), "您的网络出错啦！",
						Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(
						PeibanApplication.getInstance()
								.getApplicationContext(), "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(
						PeibanApplication.getInstance()
								.getApplicationContext(),
						"请在ShangwupanlvApplication.java文件输入正确的授权Key！",
						Toast.LENGTH_LONG).show();
				PeibanApplication.getInstance().m_bKeyRight = false;
			}
		}
	}
}