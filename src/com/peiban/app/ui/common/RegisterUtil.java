package com.peiban.app.ui.common;

public class RegisterUtil {
	public static boolean validatePassword(String str){
		boolean flag = false;
		int length = str.length();
		
		if(length > 5 && length <= 16){
			flag = true;
		}
		
		return flag;
	}
	
	public static boolean validatePhone(String str){
		boolean flag = false;
		if(str.matches("^(18|13|15|14)\\d{9}$")){
			flag = true;
		}
		return flag;
	}
	
	public static boolean validate(String str1, String str2){
		boolean flag = false;
		if(str1.equals(str2)){
			flag = true;
		}
		return flag;
	}
}
