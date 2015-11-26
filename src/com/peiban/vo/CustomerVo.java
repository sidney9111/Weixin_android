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
@Table(name= "customer1")
public class CustomerVo implements Serializable, Cloneable {
	@Transient
	private static final long serialVersionUID = -5094078474942062093L;
	@Id
	private String uid; // 用户id
	private String head;// 头像
	private String name;// 昵称
	private String sex;// 性别
	private String age;// 年龄
	private String customertype;// 用户类型(陪聊，寻伴)
	private String local; // 位置
	private String headattest;// 头像认证
	private String online; // 在线
	private String sign; // 签名
	private String qq;// qq号
	private String salary;// 薪资意愿
	private String albums;// 相册
	private String height;// 身高
	private String weight;// 体重
	private String profession;// 职业
	private String interest;// 兴趣
	private String sscore;// 服务评分
	private String ascore;// 外形评分
	private String vip;// 是否VIP
	private String usercp;// 魅力值
	private String birthday;// 生日
	private String occasions;// 服务场合
	private String markName; // 备注
	private String score;// 综合评分
	private String phone; // 联系号码
	private String agent; // 经济人
	
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	@Transient
	private String time;
	
	private String friend = "0";	// 好友标识(1 为好友, 0 陌生人.)
	
	@Transient
	private String distance;   // 距离(附近的人)
	@Transient
	private String city;       // 城市()
	@Transient
	private String lat;        // 精度
	@Transient
	private String lng;        // 纬度
	@Transient
	private String province;   // 省
	
	public String getDistance() {
		return distance;
	}

	public String getCity() {
		return city;
	}

	public String getLat() {
		return lat;
	}

	public String getLng() {
		return lng;
	}

	public String getProvince() {
		return province;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getFriend() {
		return this.friend;
	}

	public void setFriend(String friend) {
		this.friend = friend;
	}

	public String getUid() {
		return uid == null ? "" : uid;
	}

	public String getHead() {
		return head == null ? "" : head;
	}

	public String getName() {
		return name == null ? "" : name;
	}

	public String getSex() {
		return sex == null ? "" : sex;
	}

	public String getAge() {
		return age == null ? "" : age;
	}

	public String getCustomertype() {
		return customertype == null ? "" : customertype;
	}

	public String getLocal() {
		return local == null ? "" : local;
	}
	
	/**
	 * 头像认证  1 0
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public String getHeadattest() {
		return headattest == null ? "" : headattest;
	}

	public String getOnline() {
		return online == null ? "" : online;
	}

	public String getSign() {
		return sign == null ? "" : sign;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getQq() {
		return qq == null ? "" : qq;
	}

	public String getSalary() {
		return salary == null ? "" : salary;
	}

	public String getAlbums() {
		return albums == null ? "" : albums;
	}

	public String getHeight() {
		return (TextUtils.isEmpty(height) ? "0" : height);
	}

	public String getWeight() {
		return (TextUtils.isEmpty(weight) ? "0" : weight);
	}

	public String getProfession() {
		return profession == null ? "" : profession;
	}

	public String getInterest() {
		return interest == null ? "" : interest;
	}

	public String getSscore() {
		return TextUtils.isEmpty(sscore) ? "0" : TextdescTool.floatMac(sscore);
	}

	public String getAscore() {
		return TextUtils.isEmpty(ascore) ? "0" : TextdescTool.floatMac(ascore);
	}

	public String getVip() {
		return TextUtils.isEmpty(vip) ? "0" : vip;
	}

	public String getUsercp() {
		return TextUtils.isEmpty(usercp) ? "0" : usercp;
	}

	public String getBirthday() {
		return birthday == null ? "" : birthday;
	}

	public String getOccasions() {
		return occasions == null ? "" : occasions;
	}

	public String getMarkName() {
		return markName == null ? "" : markName;
	}

	public String getScore() {
		return  TextUtils.isEmpty(score) ? "0" : TextdescTool.floatMac(score);
	}

	public String getPhone() {
		return phone == null ? "" : phone;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setCustomertype(String customertype) {
		this.customertype = customertype;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public void setHeadattest(String headattest) {
		this.headattest = headattest;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public void setAlbums(String albums) {
		this.albums = albums;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public void setSscore(String sscore) {
		this.sscore = sscore;
	}

	public void setAscore(String ascore) {
		this.ascore = ascore;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public void setUsercp(String usercp) {
		this.usercp = usercp;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setOccasions(String occasions) {
		this.occasions = occasions;
	}

	public void setMarkName(String markName) {
		this.markName = markName;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customertype == null) ? 0 : customertype.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
		CustomerVo other = (CustomerVo) obj;
		if (customertype == null) {
			if (other.customertype != null)
				return false;
		} else if (!customertype.equals(other.customertype))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

	@Override
	public CustomerVo clone() {
		try {
			return (CustomerVo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
