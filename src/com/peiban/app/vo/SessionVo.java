package com.peiban.app.vo;

import com.peiban.vo.CustomerVo;
import com.peiban.vo.SessionList;

public class SessionVo {
	private SessionList sessionList;
	private CustomerVo customerVo;
	/**
	 * @return 会话列表信息
	 */
	public SessionList getSessionList() {
		return sessionList;
	}
	/**
	 * @return 会话列表对话对象
	 */
	public CustomerVo getCustomerVo() {
		return customerVo;
	}
	/**
	 * @param sessionList the sessionList to set
	 */
	public void setSessionList(SessionList sessionList) {
		this.sessionList = sessionList;
	}
	/**
	 * @param customerVo the customerVo to set
	 */
	public void setCustomerVo(CustomerVo customerVo) {
		this.customerVo = customerVo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sessionList == null) ? 0 : sessionList.hashCode());
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
		SessionVo other = (SessionVo) obj;
		if (sessionList == null) {
			if (other.sessionList != null)
				return false;
		} else if (!sessionList.equals(other.sessionList))
			return false;
		return true;
	}
}
