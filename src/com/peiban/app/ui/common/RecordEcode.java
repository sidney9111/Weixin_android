package com.peiban.app.ui.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.alibaba.fastjson.JSONObject;

public class RecordEcode {
	public static List<String> ecodeRecord(String data) {
		List<String> strList = new ArrayList<String>();
		try {
			JSONObject object = JSONObject.parseObject(data);
			int count = object.getIntValue("count");
			for (int i = 0; i < count; i++) {
				String str = ecodeRecordStr(object.getString("" + i));
				if (str != null) {
					strList.add(str);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strList;
	}

	public static String ecodeRecordStr(String content) {
		String str = null;
		try {
			JSONObject json = JSONObject.parseObject(content);
			StringBuffer buffer = new StringBuffer(140);
			String type = json.getString("type");
			String time = json.getString("time");
			String credit = json.getString("credit");
			String tag = json.getString("tag");
			
			if("1".equals(type)){
				if("admin".equals(tag)){
					buffer.append("管理员操作您所得");
				}else if("reg".equals(tag)){
					buffer.append("欢迎使用陪伴得到 ");
				}else{
					buffer.append("您作为推荐人获取");
				}
				// 充值
				buffer.append(credit)
				.append("个积分。")
				;
			}else if("2".equals(type)){
				// 购买
				buffer.append("购买")
				.append(getVipTime(tag.split(",")[0] + "000")).append("-").append(getVipTime(tag.split(",")[1] + "000"))
				.append("会员成功。")
				;
			}else if("3".equals(type)){
				// 返回积分
				buffer.append("恭喜你获得可提现积分").append(credit).append("分。");
			}else if("4".equals(type)){
				// 提现
				buffer.append("你已经提现").append(credit).append("分。");
			}
			
			buffer.append("(").append(getDate(time + "000")).append(")");
			str = buffer.toString();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return str;
	}
	
//	static String chongzhi(){}
//	
//	static String backRecord(){}
//	
//	static String tixian(){
//		
//	}
	public static String getVipTime(String currTime) {
		long time = 0;
		try {
			time = Long.parseLong(currTime);
		} catch (Exception e) {
			time = System.currentTimeMillis();
		}

		return getVipTime(time);
	}

	public static String getVipTime(long currTime) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(currTime);

		int yaer = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		StringBuffer buffer = new StringBuffer();
		buffer.append(yaer).append(".")
				.append(month < 10 ? "0" + month : month).append(".")
				.append(day < 10 ? "0" + day : day)
				;

		return buffer.toString();
	}
	
	public static String getDate(String currTime) {
		long time = 0;
		try {
			time = Long.parseLong(currTime);
		} catch (Exception e) {
			time = System.currentTimeMillis();
		}

		return getDate(time);
	}

	public static String getDate(long currTime) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(currTime);

		int yaer = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		StringBuffer buffer = new StringBuffer();
		buffer.append(yaer).append("-")
				.append(month < 10 ? "0" + month : month).append("-")
				.append(day < 10 ? "0" + day : day).append(" ")
				.append(hour < 10 ? "0" + hour : hour).append(":")
				.append(minute < 10 ? "0" + minute : minute).append(":")
				.append(second < 10 ? "0" + second : second);

		return buffer.toString();
	}
}
