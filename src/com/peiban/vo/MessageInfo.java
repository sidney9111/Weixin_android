package com.peiban.vo;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

@Table(name = "message")
public class MessageInfo extends SNSMessage{
	@Transient
	private static final long serialVersionUID = -4274108350647182194L;

	private int id;
	private int sessionId;  // 会话列表id

	private String toId;   // 本消息发送给谁

	private String fromId; // 本消息来自 谁

	private int type;       // 该消息的类型为什么.
	private String content; // 内容()
	private int voiceTime;  // 录音的时间
	
	private int sendState; // 消息发送成功与否的状态  1 成功, 2 正在发送， 4， 正在下载。0 失败
	private int readState; // 读取消息的状态.
	
	private String sendTime;   // 对方发送的时间
	private String pullTime;	// 得到消息的时间

	

	public String getSendTime() {
		return sendTime;
	}

	public String getPullTime() {
		return pullTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public void setPullTime(String pullTime) {
		this.pullTime = pullTime;
	}


	public String getToId() {
		return toId;
	}

	public String getFromId() {
		return fromId;
	}

	public int getType() {
		return type;
	}

	public String getContent() {
		return content;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public int getSendState() {
		return sendState;
	}

	public int getReadState() {
		return readState;
	}

	public void setSendState(int sendState) {
		this.sendState = sendState;
	}

	public void setReadState(int readState) {
		this.readState = readState;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(int voiceTime) {
		this.voiceTime = voiceTime;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageInfo other = (MessageInfo) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
