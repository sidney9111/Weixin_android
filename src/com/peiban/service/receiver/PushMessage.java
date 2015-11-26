package com.peiban.service.receiver;

import com.peiban.vo.MessageInfo;

/**
 * 
 * 功能：推送信息,好友之间的信息推送. <br />
 * 日期：2013-4-27<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public interface PushMessage {
	void pushMessage(MessageInfo msg);
}
