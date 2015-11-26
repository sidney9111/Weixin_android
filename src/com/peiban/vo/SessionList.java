package com.peiban.vo;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

/**
 * 
 * 功能：会话列表 <br />
 * 日期：2013-5-27<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
@Table(name= "session")
public class SessionList implements Serializable{
	@Transient
	private static final long serialVersionUID = 5389219102904727377L;
	@Id
	private int id;		// 列表标识
	private String fuid;	// 通过fuid与uid 得到列表
	private int notReadNum;		// 列表中包含未读条数
	private int listType;		// 列表的类型。
	private long createTime;	// 创建时间
	private long updateTime;	// 最后更新的时间
	private String lastContent; // 最后一条信息.
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fuid == null) ? 0 : fuid.hashCode());
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
		SessionList other = (SessionList) obj;
		if (fuid == null) {
			if (other.fuid != null)
				return false;
		} else if (!fuid.equals(other.fuid))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	/**
	 * @return the lastContent
	 */
	public String getLastContent() {
		return lastContent;
	}
	/**
	 * @param lastContent the lastContent to set
	 */
	public void setLastContent(String lastContent) {
		this.lastContent = lastContent;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the fuid
	 */
	public String getFuid() {
		return fuid;
	}
	/**
	 * @return the notReadNum
	 */
	public int getNotReadNum() {
		return notReadNum;
	}
	/**
	 * @return the listType
	 */
	public int getListType() {
		return listType;
	}
	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}
	/**
	 * @return the updateTime
	 */
	public long getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @param fuid the fuid to set
	 */
	public void setFuid(String fuid) {
		this.fuid = fuid;
	}
	/**
	 * @param notReadNum the notReadNum to set
	 */
	public void setNotReadNum(int notReadNum) {
		this.notReadNum = notReadNum;
	}
	/**
	 * @param listType the listType to set
	 */
	public void setListType(int listType) {
		this.listType = listType;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
}
