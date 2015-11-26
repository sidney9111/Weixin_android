package com.peiban.command;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.peiban.service.PhpServiceThread;
import com.peiban.vo.CustomerVo;
public class TextdescTool {
	
	public static String getDate(String currTime){
		long time = 0;
		try {
			time = Long.parseLong(currTime);
		} catch (Exception e) {
			time = System.currentTimeMillis();
		}
		
		return getDate(time);
	}
	
	
	public static String getDate(long currTime){
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(currTime);
		
		int yaer = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(yaer).append("年").append(month + 1).append("月").append(day).append("日");
		
		return buffer.toString();
	}
	
	public static String getCustomerName(CustomerVo customer){
		String markName = customer.getMarkName();
		String myName = customer.getName();
		String name = "";
		if(!TextUtils.isEmpty(markName)){
			name = markName + "(" + myName + ")";
		}else{
			name = myName;
		}
		if(TextUtils.isEmpty(name)){
			name = "*";
		}
		return name;
	}
	
	/**
	 * 保留一位小数点
	 * @param f
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-6-13<br />
	 * 修改时间:<br />
	 */
	public static String floatMac1(float f){
		DecimalFormat decimalFormat = new DecimalFormat("####.#");
		try {
			return decimalFormat.format(f);
		} catch (Exception e) {
			return f + "";
		}
	}
	
	public static String floatMac(String floatStr){
		DecimalFormat decimalFormat = new DecimalFormat("####.#");
		try {
			float f = Float.parseFloat(floatStr);
			return decimalFormat.format(f);
		} catch (Exception e) {
			return floatStr;
		}
	}
	
	/**
	 * 获取几天以前的秒数
	 * @param day
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-6-7<br />
	 * 修改时间:<br />
	 */
	public static String dayBefore(float day){
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.DAY_OF_WEEK, 0 - day);
//		return (calendar.getTimeInMillis() / 1000) + "";
		long time = (long) (60 * 60 * 24 * day);
		
		return time + "";
		
	}
	
	public static Calendar getCalendar(String brithDate) {
		Calendar calendar = Calendar.getInstance();
		if (TextUtils.isEmpty(brithDate)) {
			return calendar;
		}
		String birth = brithDate;
		try {
			Date date = new Date(birth); // 出生日期d1
			calendar.setTime(date);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return calendar;
	}
	
	public static String getTime(String currTime){
		long time = 0;
		try {
			time = Long.parseLong(currTime);
		} catch (Exception e) {
			time = System.currentTimeMillis();
		}
		
		return getTime(time);
	}
	
	public static String getTime(long currTime){
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(currTime);
		String str = timeDifference(calendar);
		Date date = calendar.getTime();
		SimpleDateFormat format = null;
		if(str.endsWith("分钟前") || str.endsWith("小时前")){
			format = new SimpleDateFormat("HH:mm:ss");
		}else if(str.endsWith("天前") || str.endsWith("很久以前")){
			format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		}
		
		return format.format(date);
	}
	
	public static boolean getOnline(String online){
		if(TextUtils.isEmpty(online)){
			return false;
		}
		
		try {
			long mtime = Long.parseLong(online) * 1000;
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			long currTime = calendar.getTimeInMillis() - mtime;
			if(currTime > PhpServiceThread.TIME){
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 出生日期转换为年龄
	 * @param brithDate
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-3-26<br />
	 * 修改时间:<br />
	 */
	public static int dateToAge(String brithDate){
		if(TextUtils.isEmpty(brithDate)){
			return 0;
		}
		int age = 0;
		try {
			Calendar cal = Calendar.getInstance();
			String birth = brithDate;
			String now = (cal.get(Calendar.YEAR) + "/"
					+ cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DATE));
			Date d1 = new Date(birth); // 出生日期d1
			Date d2 = new Date(now); // 当前日期d2
			long i = (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24);
			int g = (int) i;
			age = g / 365;
		} catch (IllegalArgumentException e) {
		}
		
		return age;
	}
	
	public static Map<String, String> objectToMap(Object strVo) {
		return (Map<String, String>) JSON.parse(JSON.toJSONString(strVo));
	}
	
	public static String timeDifference(long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		
		return timeDifference(calendar);
	}
	
	
	public static String timeOnlie(String online){
		try {
			long mtime = Long.parseLong(online) * 1000;
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			calendar.setTimeInMillis(mtime);
			return timeOnlie(calendar);
		} catch (Exception e) {
			return "";
		}
		
	}
	public static String timeOnlie(Calendar calendar){
		long cTime = calendar.getTimeInMillis();
		calendar.setTimeInMillis(cTime + PhpServiceThread.TIME);
		String info = "";
		Calendar currCalendar = Calendar.getInstance();
		long second = (currCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / 1000;
		int index = 0;
		if(second < (60 * 60)){
			index = 60;
		} else if(second < (24 * 60 * 60)){
			index = 60 * 60;
		}else if(second < (30 * (24 * 60 * 60))){
			index = (24 * 60 * 60);
		}
		info = secondOnlie(second, index, 1);
		
		return info;
	}
	
	private static String secondOnlie(long second, int index, int num){
		String info = "";
		if(index == 60){
			info = "分钟";
		} else if(index == (60 * 60)){
			info = "小时";
		} else if(index == (24 * 60 * 60)){
			info = "天";
		} else {
			return "30天以及更久";
		}
		
		if(second < index * num){
			return num + info;
		}else{
			return secondOnlie(second, index, ++num);
		}
	}
	
	public static String timeDifference(String currTime){
		Calendar calendar = Calendar.getInstance();
		try {
			long curr = Long.parseLong(currTime);
			calendar.setTimeInMillis(curr);
		} catch (Exception e) {
		}
		
		return timeDifference(calendar);
	}
	
	/**
	 * 判断时间与当前时间的差距， 给予字符提示.
	 * @param calendar
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-4-9<br />
	 * 修改时间:<br />
	 */
	public static String timeDifference(Calendar calendar){
		String info = "";
		Calendar currCalendar = Calendar.getInstance();
		long second = (currCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / 1000;
		int index = 0;
		if(second < (60 * 60)){
			index = 60;
		} else if(second < (24 * 60 * 60)){
			index = 60 * 60;
		}else if(second < (30 * (24 * 60 * 60))){
			index = (24 * 60 * 60);
		}
		info = second(second, index, 1);
		
		return info;
	}
	
	private static String second(long second, int index, int num){
		String info = "";
		if(index == 60){
			info = "分钟前";
		} else if(index == (60 * 60)){
			info = "小时前";
		} else if(index == (24 * 60 * 60)){
			info = "天前";
		} else {
			return "很久以前";
		}
		
		if(second < index * num){
			return num + info;
		}else{
			return second(second, index, ++num);
		}
	}
	
}
