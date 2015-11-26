package com.peiban.service;

import java.util.List;

import net.tsz.afinal.FinalDb;

import android.content.Context;

import com.peiban.app.FinalFactory;
import com.peiban.vo.MessageInfo;
import com.peiban.vo.SessionList;
import com.peiban.vo.UserInfoVo;

public class SessionListUtil {
	
	/**
	 * 
	 * @param context
	 * @param info
	 * @param userInfo
	 * @return null 没有列表
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public static SessionList toDbMessage(Context context, MessageInfo info, UserInfoVo userInfo){
		FinalDb finalDb = FinalFactory.createFinalDb(context, userInfo);
		
		String fuid = toSessionFuid(info, userInfo);
		
		List<SessionList> sessions = finalDb.findAllByWhere(SessionList.class, "fuid = '" + fuid + "'");
		if(sessions != null && !sessions.isEmpty()){
			return sessions.get(0);
		}
		
		return null;
	}
	
	/**
	 * 通过消息和个人信息得到 会话对象id
	 * @param info
	 * @param userInfo
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public static String toSessionFuid(MessageInfo info, UserInfoVo userInfo){
		String mPhone = userInfo.getUid();
		String fPhone = null;
		if(mPhone.equals(info.getToId())){
			fPhone = info.getFromId();
		}else{
			fPhone = info.getToId();
		}
		
		return fPhone;
	}
	
	/**
	 * 得到fid的会话列表
	 * @param info
	 * @param userInfo
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public static SessionList toMessage(MessageInfo info, UserInfoVo userInfo){
		String fPhone = null;
		fPhone = toSessionFuid(info, userInfo);
		SessionList sessionList = new SessionList();
		sessionList.setFuid(fPhone);
		
		return sessionList;
	}
}
