package com.peiban.app.vo;
/**
 * 
 * 功能： 提现申请信息<br />
 * 日期：2013-7-3<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class AccountVo extends BaseVo{
	private static final long serialVersionUID = -2619261121152788663L;
	
	private String bank;
	private String accountName;
	private String account;
	private String phone;
	
	public AccountVo() {
		super();
	}
	public AccountVo(String bank, String accountName, String account,
			String phone) {
		super();
		this.bank = bank;
		this.accountName = accountName;
		this.account = account;
		this.phone = phone;
	}
	/** 银行 */
	public String getBank() {
		return null == bank ? "" : bank;
	}
	/** 开户名 */
	public String getAccountName() {
		return null == accountName ? "" : accountName;
	}
	/** 账户 */
	public String getAccount() {
		return null == account ? "" : account;
	}
	/** 电话 */
	public String getPhone() {
		return null == phone ? "" : phone;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
