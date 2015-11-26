package com.peiban.vo;
/**
 * 
 * 功能： 附近拉取好友条件Vo <br />
 * 日期：2013-5-31<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class LocationConfigVo {
	/** 位置 */
	private String lat;
	private String lng;
	
	// 上线
	private String radius;
	// 下线
	private String sradius;
	
	/*****     评分    ******/
	private String sroce;
	
	private String sex;
	private String onLineState;
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @return the onLineState
	 */
	public String getOnLineState() {
		return onLineState;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @param onLineState the onLineState to set
	 */
	public void setOnLineState(String onLineState) {
		this.onLineState = onLineState;
	}

	/**
	 * @return 精度
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * @return 纬度
	 */
	public String getLng() {
		return lng;
	}

	/**
	 * @return 半径上线
	 */
	public String getRadius() {
		return radius;
	}

	/**
	 * @return 半径下线
	 */
	public String getSradius() {
		return sradius;
	}

	/**
	 * @return 评分
	 */
	public String getSroce() {
		return sroce;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * @param lng the lng to set
	 */
	public void setLng(String lng) {
		this.lng = lng;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(String radius) {
		this.radius = radius;
	}

	/**
	 * @param sradius the sradius to set
	 */
	public void setSradius(String sradius) {
		this.sradius = sradius;
	}

	/**
	 * @param sroce the sroce to set
	 */
	public void setSroce(String sroce) {
		this.sroce = sroce;
	}
	
}
