package com.peiban.vo;

/**
 * 
 * 功能： 系统通知信息 <br />
 * 客户端收到的通知格式 {"type":"1","content":"0","sent":"{xxxx}"} type 信息类型：
 * 1-系统消息，2-好友申请，3-申请查看资料，4-被评论,5-查看资料,6-删除好友 
 * 	content 消息内容：针对系统消息
	其余5种类型没有值
 *  sent
 * 发送人 用户信息. 
 * 日期：2013-5-29<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class NotifiyType {
	
	public static final String APPLY_SEE = "申请查看了您的资料";
	public static final String APPLY_SEE_SUCCESS = "同意了您查看资料";
	public static final String RESFUEDATA = "拒绝您的资料查看请求";
	public static final String APPLYFRIEND = "申请加您为好友";
	public static final String FRIENDED = "同意添加您为好友";
	public static final String RESFUEFRIEND = "拒绝添加您为好友";
	public static final String RESFUE_DATA = "驳回了你的查看信息请求";
	public static final String DELFRIEND = "对您解除好友关系";
	public static final String HEAD_AUTH_SUCCESS_TAG = "恭喜您，头像认证成功!";
	
	/** 系统消息 */
	public static final int SYSTEM_MSG = 1;
	/** 好友申请 */
	public static final int BE_FRIEND = 2;
	/** 申请查看资料 */
	public static final int APPLY_DATA = 5;
	/** 被评论 */
	public static final int COMMENTED = 4;
	/** 查看资料 */
	public static final int SEE_DATA = 3;
	/** 删除好友 */
	public static final int DEL_FRIEND = 6;
	/** 添加好友成功 */
	public static final int ADDFRIENDED = 7;
	/** 拒绝好友 */
	public static final int RESFUEFRIENDED = 8;
	/** 同意查看用户信息*/
	public static final int APPLYAUTHED = 9;
	/** 拒绝你的资料查看请求 */
	public static final int RESFUEF_DATE = 10;
	/** 头像认证成功 */
	public static final int HEAD_AUTH_SUCCESS = 11;
	/** VIP状态发生变化 */
	public static final int CHANGE_VIP_STATE = 12;
}