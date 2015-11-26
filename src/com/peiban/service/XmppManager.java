package com.peiban.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import android.util.Log;

import com.peiban.service.type.XmppType;
import com.xmpp.push.sns.ConnectionConfiguration;
import com.xmpp.push.sns.ConnectionConfiguration.SecurityMode;
import com.xmpp.push.sns.ConnectionListener;
import com.xmpp.push.sns.XMPPConnection;
import com.xmpp.push.sns.XMPPException;

/**
 * 
 * 功能： 连接IM服务器.. <br />
 * 日期：2013-4-16<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class XmppManager {
	public static final String LOGTAG = "XmppManager";
	/** 连接状态 */
	public static final int XMPP_CONNECT_STATE = 0x1;
	/** 登录状态 */
	public static final int XMPP_LOGINING_STATE = 0x2;
	/** 登录成功状态 */
	public static final int XMPP_LOGINED_STATE = 0x3;
	/** 认证失败 */
	public static final int XMPP_AUTH_ERR = 0xc;
	
	//private static final String HOST = "115.236.32.231";
	//private static final String HOST = "120.25.158.30";
	//private static final String HOST = "192.168.0.103";
	private static final String HOST = "192.168.1.215";
	private static final int PORT = 5222;
	private static final String XMPP_RESOURCE_NAME = "CHAT";
	
	private String username;
	private String password;
	
	private boolean running = false;
	private Future<?> futureTask;
	private XMPPConnection connection;
	private List<Runnable> taskList;
	
	private SNSMessageManager snsMessageLisener;
	private SNSRosterLisenerImpl snsRosterLisenerImpl;
	
	private SnsService.TaskSubmitter taskSubmitter;
	private SnsService.TaskTracker taskTracker;
	private ConnectionListener connectionListener;
	
	private SnsService snsService;

	private PhpServiceThread phpServiceThread;
	
	private int connectState = 0x0;
	
	private XmppManager(SnsService snsService){
		this.snsService = snsService;
		taskSubmitter = snsService.getTaskSubmitter();
		taskTracker = snsService.getTaskTracker();
		taskList = new ArrayList<Runnable>();
		System.setProperty("smack.debugEnabled", "true");
	}
	
	public XmppManager(SnsService snsService, String username, String password) {

		this(snsService);
		this.username = username;
		this.password = password;
		
		connectionListener = new PersistentConnectionListener(this);
		snsMessageLisener = new SNSMessageManager(XmppManager.this);
	}
	
	public void connect() {
		Log.d(LOGTAG, "connect()..."+"username="+username + "|"+password);
		// 提交登录任务.
		submitLoginTask();
	}
	
	public void disconnect() {
		Log.d(LOGTAG, "disconnect()...");
		if(phpServiceThread != null){
			phpServiceThread.runState = false;
			phpServiceThread = null;
		}
		// 取消登录
		getSnsService().saveXmppType(XmppType.XMPP_STATE_REAUTH);
		terminatePersistentConnection();
	}

	public Future<?> getFutureTask() {
		return futureTask;
	}

	public List<Runnable> getTaskList() {
		return taskList;
	}

	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	public SNSMessageManager getSnsMessageLisener() {
		return snsMessageLisener;
	}

	public boolean isAuthenticated() {
		return connection != null && connection.isConnected()
				&& connection.isAuthenticated();
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	/**
	 * 开始重新连接
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-4-16<br />
	 * 修改时间:<br />
	 */
	public void startReconnectionThread() {
		Log.d(LOGTAG, "重新连接");
		taskList.clear();
		taskTracker.count = taskList.size();
		addTask(new Runnable() {
			
			
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					// TODO: handle exception
				}
//				terminatePersistentConnection(); // 断开连接
				
				Log.d(LOGTAG, "开始连接");
				connect() ;
				runTask();
			}
		});
		
		runTask();
	}
	
	/**
	 * 终止连接
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-4-16<br />
	 * 修改时间:<br />
	 */
	public void terminatePersistentConnection() {
		Log.d(LOGTAG, "terminatePersistentConnection()...");
		Runnable runnable = new Runnable() {

			final XmppManager xmppManager = XmppManager.this;

			@Override
			public void run() {
				if (xmppManager.isConnected()) {
					Log.d(LOGTAG, "terminatePersistentConnection()... run()");
					xmppManager.getConnection().disconnect();
				}
				xmppManager.runTask();
			}

		};
		addTask(runnable);
	}
	
	public void runTask() {
		Log.d(LOGTAG, "runTask()...");
		synchronized (taskList) {
			running = false;
			futureTask = null;
			if (!taskList.isEmpty()) {
				Runnable runnable = taskList.get(0);
				taskList.remove(0);
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			}
		}
		taskTracker.decrease();
		Log.d(LOGTAG, "runTask()...done");
	}
	
	// 开始连接.
	private void submitConnectTask() {
		Log.d(LOGTAG, "submitConnectTask()...");
		addTask(new ConnectTask());
	}

	private void submitLoginTask() {
		Log.d(LOGTAG, "submitLoginTask()...");
		// 连接
		submitConnectTask();
		addTask(new LoginTask());
	}
	
	private void addTask(Runnable runnable) {
		Log.d(LOGTAG, "addTask(runnable)...");
		taskTracker.increase();
		synchronized (taskList) {
			if (taskList.isEmpty() && !running) {
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			} else {
				taskList.add(runnable);
			}
		}
		Log.d(LOGTAG, "addTask(runnable)... done");
	}
	
	/**
	 * A runnable task to connect the server.
	 */
	private class ConnectTask implements Runnable {

		final XmppManager xmppManager;

		private ConnectTask() {
			this.xmppManager = XmppManager.this;
		}

		@Override
		public void run() {
			Log.i(LOGTAG, "ConnectTask.run()...");
			connectState = XMPP_CONNECT_STATE;
			if (!xmppManager.isConnected()) {
				// Create the configuration for this new connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(
						HOST, PORT);
				// connConfig.setSecurityMode(SecurityMode.disabled);
				connConfig.setSecurityMode(SecurityMode.disabled); // 设置安全模式
				connConfig.setSASLAuthenticationEnabled(false); // 不启用sasl 认证启用
				connConfig.setCompressionEnabled(false); // 压缩不启用

				XMPPConnection connection = new XMPPConnection(connConfig);
				xmppManager.setConnection(connection);

				try {
					// Connect to the server
					connection.connect();
					// 连接成功,开始登录
					xmppManager.runTask();
					Log.i(LOGTAG, "XMPP connected successfully");
				} catch (XMPPException e) {
					Log.e(LOGTAG, "XMPP connection failed", e);
//					terminatePersistentConnection();
					startReconnectionThread();
				}
				
			} else {
				Log.i(LOGTAG, "XMPP connected already");
				xmppManager.runTask();
			}
		}
	}
	
	/**
	 * A runnable task to log into the server.
	 */
	private class LoginTask implements Runnable {
		final XmppManager xmppManager;

		private LoginTask() {
			this.xmppManager = XmppManager.this;
		}

		@Override
		public void run() {
			Log.i(LOGTAG, "LoginTask.run()...");
			connectState = XMPP_LOGINING_STATE;
			// 1. 是否登录的
			if (!xmppManager.isAuthenticated()) {
				try {
					// 2.登录
					xmppManager.getConnection().login(
							xmppManager.getUsername(),
							xmppManager.getPassword(), XMPP_RESOURCE_NAME);
					Log.d(LOGTAG, "Loggedn in successfully");
					loginSuccess();
					xmppManager.runTask();

				} catch (XMPPException e) {
					Log.e(LOGTAG, "LoginTask.run()... xmpp error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
							+ e.getMessage());
					String INVALID_CREDENTIALS_ERROR_CODE = "401";
					String errorMessage = e.getMessage();
					if (errorMessage != null
							&& errorMessage
									.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
						// TODO 还没有注册或者用户名密码错误!
						connectState = XMPP_AUTH_ERR;
						// 认证错误.
						getSnsService().saveXmppType(XmppType.XMPP_STATE_AUTHERR);
					}
					xmppManager.startReconnectionThread();

				} catch (Exception e) {
					Log.e(LOGTAG, "LoginTask.run()... other error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
							+ e.getMessage());
					xmppManager.startReconnectionThread();
				}

			} else {
				Log.i(LOGTAG, "Logged in already");
				xmppManager.runTask();
				connectState = XMPP_LOGINED_STATE;
				
				getSnsService().saveXmppType(XmppType.XMPP_STATE_AUTHENTICATION);
			}
		}
	}
	
	
	public SnsService getSnsService() {
		return snsService;
	}

	private void loginSuccess()
	{
		connectState = XMPP_LOGINED_STATE;
		// 认证成功.
		getSnsService().saveXmppType(XmppType.XMPP_STATE_AUTHENTICATION);
		// connection listener
		if (XmppManager.this.getConnectionListener() != null) {
			XmppManager.this.getConnection().addConnectionListener(
					XmppManager.this.getConnectionListener());
		}
		
		getConnection().getChatManager().addChatListener(snsMessageLisener);
		if(snsRosterLisenerImpl == null){
			snsRosterLisenerImpl = new SNSRosterLisenerImpl(XmppManager.this);
		}
		
//		getConnection().getRoster().addRosterListener(snsRosterLisenerImpl);
		
		if(phpServiceThread == null){
			phpServiceThread = new PhpServiceThread(getSnsService().getXmppTypeManager(), getSnsService().getUserInfoVo());
			new Thread(new PhpServiceThread(getSnsService().getXmppTypeManager(), getSnsService().getUserInfoVo()))
			.start();
		}
	}

	public int getConnectState() {
		return connectState;
	}

	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}
}
