package com.peiban.app.vo;

import com.alibaba.fastjson.JSONObject;

/**
 * 功能：<br />
 * 日期：2013-6-20<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class MapInfo {
	private String ctiy;
	private String addr;
	private String lat;
	private String lon;

	public static MapInfo getInfo(String json) {
		try {
			return JSONObject.toJavaObject(JSONObject.parseObject(json),
					MapInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getInfo(MapInfo info) {
		String json = JSONObject.toJSON(info).toString();
		return json;
	}

	public String getCtiy() {
		return ctiy;
	}

	public String getAddr() {
		return addr;
	}

	public String getLat() {
		return lat;
	}

	public String getLon() {
		return lon;
	}

	public void setCtiy(String ctiy) {
		this.ctiy = ctiy;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	@Override
	public String toString() {
		return "MapInfo [ctiy=" + ctiy + ", addr=" + addr + ", lat=" + lat
				+ ", lon=" + lon + "]";
	}

}