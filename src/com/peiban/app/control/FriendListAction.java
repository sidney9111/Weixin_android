package com.peiban.app.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.peiban.app.FinalFactory;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.FriendApi;
import com.peiban.app.ui.FriendListActivity;
import com.peiban.command.PinyinComparator;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.FriendListVo;
import com.peiban.vo.UserInfoVo;

/**
 * 
 * 功能： 好友列表获取信息 <br />
 * 日期：2013-5-31<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class FriendListAction {
	private static final String TAG = "friendList";
	
	private Context context;
	private FinalDb finalDb;
	private FriendListVo friendListVo;
	private UserInfoVo userInfoVo;
	private Handler handler;

	private FriendApi friendApi;

	private List<CustomerVo> customerVos;
	
	private  PinyinComparator pinyinComparator;
	
	private boolean localState;

	public FriendListAction(Context context, UserInfoVo userInfo,
			Handler handler) {
		super();
		this.context = context;
		this.userInfoVo = userInfo;
		this.handler = handler;
		finalDb = FinalFactory.createFinalDb(context, userInfo);
		friendApi = new FriendApi();
		pinyinComparator = new PinyinComparator();
	}

	/**
	 * 判断本地是否有好友列表信息
	 * 
	 * @return true 有, false 没有 作者:fighter <br />
	 *         创建时间:2013-5-31<br />
	 *         修改时间:<br />
	 */
	public synchronized boolean isLocalFriendInfo() {
		Log.d(TAG, "isLocalFriendInfo()");
		if (localState) {
			return true;
		}
		friendListVo = finalDb
				.findById(userInfoVo.getUid(), FriendListVo.class);
		if (friendListVo == null) {
			localState = false;
		} else {
			localState = true;
		}

		return localState;
	}

	/**
	 * 获取好友列表信息
	 * 
	 * @return 作者:fighter <br />
	 *         创建时间:2013-5-31<br />
	 *         修改时间:<br />
	 */
	public synchronized List<CustomerVo> getFriendList() {
		Log.d(TAG, "getFriendList()");
		if (customerVos != null) {
			return customerVos;
		}
		if (friendListVo != null) {
			try {
				List<CustomerVo> cVos = finalDb.findAllByWhere(CustomerVo.class, "uid in(" + friendListVo.getFriends() + ")");
				if(cVos != null){
					Collections.sort(cVos, pinyinComparator);
				}else{
					cVos = new ArrayList<CustomerVo>();
				}
				checkFriendListIsUpdate();
				this.customerVos = cVos;
				return this.customerVos;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/***
	 * 检查朋友列表是否需要更新
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-31<br />
	 * 修改时间:<br />
	 */
	private void checkFriendListIsUpdate() {
		Log.d(TAG, "checkFriendListIsUpdate()");
//		new Thread() {
//			@Override
//			public void run() {
//				super.run();
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				pushFriendList();
//			}
//
//		}.start();
	}

//	public void asyPushFriendList() {
//		new Thread() {
//			@Override
//			public void run() {
//				pushFriendList();
//			}
//
//		}.start();
//	}

	/**
	 * 保存好友列表. 请在后台操作。
	 * 
	 * @param customerVos
	 * @return 作者:fighter <br />
	 *         创建时间:2013-5-31<br />
	 *         修改时间:<br />
	 */
	public boolean saveFriendList(List<CustomerVo> customerVos) {
		Log.d(TAG, "saveFriendList()");
		try {
			friendListVo = new FriendListVo();
			friendListVo.setUid(userInfoVo.getUid());
			friendListVo.setUpdateTime(System.currentTimeMillis() + "");
			
			StringBuffer buffer = new StringBuffer();
			int index = customerVos.size();
			for (int i = 0; i < index; i++) {
				CustomerVo customerVo = customerVos.get(i);
				customerVo.setFriend("1");
				buffer.append("'").append(customerVo.getUid()).append("'");
				if(i == (index-1)){
					continue;
				}
				buffer.append(",");
			}
			String friends = buffer.toString();
			
			friendListVo.setFriends(friends);
			finalDb.delete(friendListVo);
			finalDb.save(friendListVo);
			finalDb.deleteByWhere(CustomerVo.class, "uid in (" + friends + ")");
			finalDb.saveList(customerVos);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新好友列表.
	 * 
	 * @param customerVos
	 * @return 作者:fighter <br />
	 *         创建时间:2013-5-31<br />
	 *         修改时间:<br />
	 */
	public boolean updateFriendList(List<CustomerVo> customerVos) {
		Log.d(TAG, "updateFriendList()");
		String friends = JSON.toJSONString(customerVos);
		try {
			finalDb.deleteByWhere(CustomerVo.class, "uid != '" + userInfoVo.getUid() + "'");
			finalDb.saveList(customerVos);
		} catch (Exception e) {
			return false;
		}
		
		
		return saveFriendListDb(friends);
	}

	private boolean saveFriendListDb(String cvos) {
		Log.d(TAG, "saveFriendListDb()");
		try {
			friendListVo = new FriendListVo();
			friendListVo.setUid(userInfoVo.getUid());
			friendListVo.setUpdateTime(System.currentTimeMillis() + "");

			friendListVo.setFriends(cvos);
			finalDb.update(friendListVo);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 从网络拉取好友列表
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-31<br />
	 * 修改时间:<br />
	 */
	public void pushFriendList() {
		Log.d(TAG, "pushFriendList()");
		friendApi.getFriendList(userInfoVo.getUid(),
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						System.out.println("--------:" + t);
						String data = ErrorCode.getData(t);
						if (data != null) {
							List<CustomerVo> vos = null;
							if("".equals(data)){
								vos = new ArrayList<CustomerVo>();
							}else{
								vos = JSON.parseArray(data,
										CustomerVo.class);
							}
							
							saveFriendList(vos);
							Collections.sort(vos, pinyinComparator);
							
							Message message = new Message();
							message.what = FriendListActivity.HEADLER_SUCCESS;
							message.obj = vos;
							customerVos = vos;
							sendMessage(message);
						}else{
							Message message = new Message();
							message.what = FriendListActivity.HEADLER_ERR;
							sendMessage(message);
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						Message message = new Message();
						message.what = FriendListActivity.HEADLER_ERR;
						sendMessage(message);
					}

				});
	}

	private void sendMessage(Message message) {
		if (handler == null) {
			return;
		}
		handler.sendMessage(message);
	}

	/**
	 * @param handler
	 *            the handler to set
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
