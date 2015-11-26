package com.peiban.vo;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 
 * 功能： 临时会话用户列表 <br />
 * 日期：2013-6-6<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
@Table(name = "tempCustomer")
public class TempCustomerVo {
	private String uid;  // 用户的uid
	private String customer;  // 用户的信息
	public String getUid() {
		return uid;
	}
	public String getCustomer() {
		return customer;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	
}
