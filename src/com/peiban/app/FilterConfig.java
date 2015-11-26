/**
 * @Title: FilterAuth.java 
 * @Package com.shangwupanlv.app.api 
 * @Description: TODO条件筛选的配置文件
 * @author lcy   
 * @date 2013-5-21 上午10:21:16 
 * @version V1.0
 */
package com.peiban.app;

import com.peiban.command.TextdescTool;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class FilterConfig {
	/**
	 * 类相关
	 */

	private Context context;// 上下文
	private SharedPreferences filterShared;
	private Editor filterEditor;
	/**
	 * 配置文件名相关
	 * */
	public static final String FILTER_NAME = "Filtername";// 配置文件名
	/**
	 * 配置文件key相关
	 * */
	public static final String FILTER_SEX = "Filtersex";// 性别选择
	public static final String FILTER_SERVICE = "Filterservice";// 服务场合
	public static final String FILTER_LOGINTIME = "Filterlogintime";// 登录时间
	public static final String FILTER_AUTH = "Filterauth";// 是否认证
	public static final String FILTER_BROKER = "Filterbroker";// 是否经纪人
	public static final String FILTER_COMPOSITE = "Filtercomposite";// 是否有综合评分
	public static final String FILTER_AGEONE = "Filterageone";// 年龄1
	public static final String FILTER_AGETWO = "Filteragetwo";// 年龄2
	public static final String FILTER_TALLONE = "Filtertallone";// 身高1
	public static final String FILTER_TALLTWO = "Filtertalltwo";// 身高2
	public static final String FILTER_WEIGHTONE = "Filterweightone";// 体重1
	public static final String FILTER_WEIGHTTWO = "Filterweighttwo";// 体重2
	public static final String FILTER_VIP = "FilterVip";// 是否vip
	public static final String FILTER_HONORONE = "FilterHonorone";// 荣誉值1
	public static final String FILTER_HONORTWO = "FilterHonortwo";// 荣誉值2
	public static final String FILTER_DISTANCEONE = "Filterdistanceone";// 距离1
	public static final String FILTER_DISTANCETWO = "Filterdistancetwo";// 距离2
	/**
	 * 属性相关
	 * */
	private String sex;// 性别
	private String service;// 服务场合
	private String logintime;// 登录时间
	private String auth;// 认证
	private String broker;// 经纪人
	private String composite;// 综合评分
	private String ageone;// 年龄1
	private String agetwo;// 年龄2
	private String tallone;// 身高1
	private String talltwo;// 身高2
	private String weightone;// 体重1
	private String weighttwo;// 体重2
	private String vip;// vip
	private String honorone;// 荣誉值1
	private String honortwo;// 荣誉值2
	private String distanceone;// 距离1
	private String distancetwo;// 距离2

	public FilterConfig(Context context) {
		this.context = context;
		filterShared = this.context.getSharedPreferences(FILTER_NAME,
				Context.MODE_PRIVATE);
		filterEditor = filterShared.edit();
	}

	/**
	 * getter and setter
	 * */

	public String getSex() {
		if (TextUtils.isEmpty(sex)) {
			this.sex = filterShared.getString(FILTER_SEX, "");
		}
		return sex;
	}

	public String getDistanceone() {
		if (TextUtils.isEmpty(distanceone)) {
			this.distanceone = filterShared.getString(FILTER_DISTANCEONE, "0");
		}
		return distanceone;
	}

	public String getDistancetwo() {
		if (TextUtils.isEmpty(distancetwo)) {
			this.distancetwo = filterShared.getString(FILTER_DISTANCETWO, "0");
		}
		return distancetwo;
	}

	public void setDistanceone(String distanceone) {
		filterEditor.putString(FILTER_DISTANCEONE, distanceone);
		this.distanceone = distanceone;
	}

	public void setDistancetwo(String distancetwo) {
		filterEditor.putString(FILTER_DISTANCETWO, distancetwo);
		this.distancetwo = distancetwo;
	}

	public String getVip() {
		if (TextUtils.isEmpty(vip)) {
			this.vip = filterShared.getString(FILTER_VIP, "");
		}
		return vip;

	}

	public String getHonorone() {
		if (TextUtils.isEmpty(honorone)) {
			this.honorone = filterShared.getString(FILTER_HONORONE, "0");
		}
		return honorone;
	}

	public String getHonortwo() {
		if (TextUtils.isEmpty(honortwo)) {
			this.honortwo = filterShared.getString(FILTER_HONORTWO, "0");
		}
		return honortwo;
	}

	public String getService() {
		if (TextUtils.isEmpty(service)) {
			this.service = filterShared.getString(FILTER_SERVICE, "3");
		}
		return service;
	}

	public String getLogintime() {
		if (TextUtils.isEmpty(logintime)) {
			this.logintime = filterShared.getString(FILTER_LOGINTIME, "4");
		}
		return logintime;
	}

	/**
	 * 1, 改为  30 分钟
	 * 2, 改为 1天内
	 * 3, 改为  3天内
	 * @return
	 * @author fighter <br />
	 * 创建时间:2013-7-12<br />
	 * 修改时间:<br />
	 */
	public String getDayTime() {
		String time = null;
		FilterConfig filterConfig = new FilterConfig(context);
		if ("1".equals(filterConfig.getLogintime())) {
//			time = "0";
			time = 60 * 30 + "";
		} else if ("2".equals(filterConfig.getLogintime())) {
			time = TextdescTool.dayBefore(1f);
		} else if ("3".equals(filterConfig.getLogintime())) {
			time = TextdescTool.dayBefore(3f);
		} else if ("4".equals(filterConfig.getLogintime())) {
			time = "";
		}
		return time;
	}

	public String getAuth() {
		if (TextUtils.isEmpty(auth)) {
			this.auth = filterShared.getString(FILTER_AUTH, "");
		}
		return auth;
	}

	public String getBroker() {
		this.broker = filterShared.getString(FILTER_BROKER, "");
		return broker;
	}

	public String getComposite() {
		this.composite = filterShared.getString(FILTER_COMPOSITE, "");
		return composite;
	}

	public String getAgeone() {
		if (TextUtils.isEmpty(ageone)) {
			this.ageone = filterShared.getString(FILTER_AGEONE, "0");
		}
		return ageone;
	}

	public String getAgetwo() {
		if (TextUtils.isEmpty(agetwo)) {
			this.agetwo = filterShared.getString(FILTER_AGETWO, "0");
		}
		return agetwo;
	}

	public String getTallone() {
		if (TextUtils.isEmpty(tallone)) {
			this.tallone = filterShared.getString(FILTER_TALLONE, "0");
		}
		return tallone;
	}

	public String getTalltwo() {
		if (TextUtils.isEmpty(talltwo)) {
			this.talltwo = filterShared.getString(FILTER_TALLTWO, "0");
		}
		return talltwo;
	}

	public String getWeightone() {
		if (TextUtils.isEmpty(weightone)) {
			this.weightone = filterShared.getString(FILTER_WEIGHTONE, "0");
		}
		return weightone;
	}

	public String getWeighttwo() {
		if (TextUtils.isEmpty(weighttwo)) {
			this.weighttwo = filterShared.getString(FILTER_WEIGHTTWO, "0");
		}
		return weighttwo;
	}

	public void setSex(String sex) {
		filterEditor.putString(FILTER_SEX, sex);
		this.sex = sex;
	}

	public void setService(String service) {
		if (!TextUtils.isEmpty(service)) {
			filterEditor.putString(FILTER_SERVICE, service);
		}
		this.service = service;
	}

	public void setLogintime(String logintime) {
		filterEditor.putString(FILTER_LOGINTIME, logintime);
		this.logintime = logintime;
	}

	public void setAuth(String auth) {
		filterEditor.putString(FILTER_AUTH, auth);
		this.auth = auth;
	}

	public void setBroker(String broker) {
		filterEditor.putString(FILTER_BROKER, broker);
		this.broker = broker;
	}

	public void setComposite(String composite) {
			filterEditor.putString(FILTER_COMPOSITE, composite);
		this.composite = composite;
	}

	public void setAgeone(String ageone) {
		if (!TextUtils.isEmpty(ageone)) {
			filterEditor.putString(FILTER_AGEONE, ageone);
		}
		this.ageone = ageone;
	}

	public void setAgetwo(String agetwo) {
		if (!TextUtils.isEmpty(agetwo)) {
			filterEditor.putString(FILTER_AGETWO, agetwo);
		}
		this.agetwo = agetwo;
	}

	public void setTallone(String tallone) {
		if (!TextUtils.isEmpty(tallone)) {
			filterEditor.putString(FILTER_TALLONE, tallone);
		}
		this.tallone = tallone;
	}

	public void setTalltwo(String talltwo) {
		if (!TextUtils.isEmpty(talltwo)) {
			filterEditor.putString(FILTER_TALLTWO, talltwo);
		}
		this.talltwo = talltwo;
	}

	public void setWeightone(String weightone) {
		if (!TextUtils.isEmpty(weightone)) {
			filterEditor.putString(FILTER_WEIGHTONE, weightone);
		}
		this.weightone = weightone;
	}

	public void setWeighttwo(String weighttwo) {
		if (!TextUtils.isEmpty(weighttwo)) {
			filterEditor.putString(FILTER_WEIGHTTWO, weighttwo);
		}
		this.weighttwo = weighttwo;
	}

	public void setVip(String vip) {
		filterEditor.putString(FILTER_VIP, vip);
		this.vip = vip;
	}

	public void setHonorone(String honorone) {
		if (!TextUtils.isEmpty(honorone)) {
			filterEditor.putString(FILTER_HONORONE, honorone);
		}
		this.honorone = honorone;
	}

	public void setHonortwo(String honortwo) {
		if (!TextUtils.isEmpty(honortwo)) {
			filterEditor.putString(FILTER_HONORTWO, honortwo);
		}
		this.honortwo = honortwo;
	}

	public void FilterCommit() {
		filterEditor.commit();
	}
}
