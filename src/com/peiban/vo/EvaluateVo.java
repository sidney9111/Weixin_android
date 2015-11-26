/**
 * @Title: CustomerVo.java 
 * @Package com.shangwupanlv.vo 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-6 下午2:14:57 
 * @version V1.0
 */
package com.peiban.vo;

import java.io.Serializable;

import com.peiban.command.TextdescTool;

import android.text.TextUtils;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

@Table(name = "customer")
public class EvaluateVo implements Serializable, Cloneable {
	@Transient
	private static final long serialVersionUID = -5094078474942062093L;
	@Id
	private String uid; // 用户id
	private String head;// 头像
	private String sscore;// 服务评分
	private String ascore;// 外形评分
	private String time; // 评价时间

	public String getUid() {
		return uid;
	}

	public String getHead() {
		return head == null ? "" : head;
	}

	public String getSscore() {
		return sscore == null ? "" : sscore;
	}

	public String getAscore() {
		return ascore == null ? "" : ascore;
	}

	public String getTime() {
		return time == null ? "" : time;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public void setSscore(String sscore) {
		this.sscore = sscore;
	}

	public void setAscore(String ascore) {
		this.ascore = ascore;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
