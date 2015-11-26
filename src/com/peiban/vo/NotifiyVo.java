package com.peiban.vo;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

/**
 * 
 * 功能： 系统通知信息 <br />
 * 	客户端收到的通知格式
	{"type":"1","content":"0","sent":"{xxxx}"}
	type 信息类型：
	1-系统消息，2-好友申请，3-申请查看资料，4-被评论,5-查看资料,6-删除好友
	content 消息内容：针对系统消息  其余5种类型没有值
	sent  发送人 用户信息.
 * 日期：2013-5-29<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
@Table(name = "system1_notifiy")
public class NotifiyVo extends SNSMessage{
	/** 消息完成了的状态 */
	@Transient
	public static final String STATE_FINISH = "finish";
	/** 消息为完成的状态 */
	@Transient
	public static final String STATE_NO_FINISH = "nofinish";
	@Transient
	private static final long serialVersionUID = -5731925495114017054L;

	@Id
	private int id;
	
	private int type;
	private String content;
	private String sent;
	
	private String time;
	
	private String state = STATE_NO_FINISH;
	
	
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @return the sent
	 */
	public String getSent() {
		return sent;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @param sent the sent to set
	 */
	public void setSent(String sent) {
		this.sent = sent;
	}
	
	
}
