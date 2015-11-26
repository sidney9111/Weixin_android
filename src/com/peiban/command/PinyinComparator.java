package com.peiban.command;

import java.util.Comparator;

import com.peiban.vo.CustomerVo;

import android.text.TextUtils;

public class PinyinComparator implements Comparator<CustomerVo>{

	@Override
	public int compare(CustomerVo cv1, CustomerVo cv2) {
		String o1 = TextdescTool.getCustomerName(cv1);
		String o2 = TextdescTool.getCustomerName(cv2);
		if(TextUtils.isEmpty(o1))
			o1 = "#";
		if (TextUtils.isEmpty(o2))
			o2 = "#";
		 String str1 = PingYinUtil.getPingYin(o1);
	     String str2 = PingYinUtil.getPingYin(o2);
	     return str1.compareTo(str2);
	}

}
