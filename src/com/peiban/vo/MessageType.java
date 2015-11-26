package com.peiban.vo;


public final class MessageType {
	public static final int PICTURE = 1;
	public static final int VOICE = 2;
	public static final int TEXT = 3;
	public static final int MAP = 4;
	
	
	public static String timeUid(){
		return System.currentTimeMillis() + "";
	}
}
