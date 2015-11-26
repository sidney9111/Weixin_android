package com.peiban.app.api;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public final class ErrorCode {
	private static final Map<Integer, String> errCode = new HashMap<Integer, String>();
	
	private static final String TAG = "ERRORCODE";
	
	static{
		errCode.put(-1, "网络请求超时");
		errCode.put(101, "uid为空");
		errCode.put(102, "没有数据");
		errCode.put(103, "action请求错误");
		errCode.put(104, "手机号码为空");
		errCode.put(105, "密码为空");
		errCode.put(106, "手机号或密码错误");
		errCode.put(107, "手机号已经存在");
		errCode.put(108, "验证码为空");
		errCode.put(109, "验证码错误");
		errCode.put(110, "原始密码错误!");
		errCode.put(111, "图片上传类型type错误");
		errCode.put(112, "文件名 错误");
		errCode.put(113, "图片尺寸过大");
		errCode.put(114, "文件类型（后缀名）错误");
		errCode.put(115, "文件上传未知错误");
		errCode.put(116, "文件上传失败");
		errCode.put(117, "相册id为空");
		errCode.put(118, "图片路径为空");
		errCode.put(119, "保存图片信息失败");
		errCode.put(120, "pid为空");
		errCode.put(121, "上传文件空值");
		errCode.put(122, "lat为空");
		errCode.put(123, "lng为空");
		errCode.put(124, "目标为空");
		errCode.put(125, "最后一页");
		errCode.put(126, "对象为空");
		errCode.put(127, "已经是好友，无需添加");
		errCode.put(128, "注册失败 code:129");
		errCode.put(129, "相册新建失败");
		errCode.put(130, "半径为空");
		errCode.put(131, "形象评分为空");
		errCode.put(132, "服务评分为空");
		errCode.put(133, "积分为空");
		errCode.put(134, "积分对应值（rmb/购买会员时长）为空");
		errCode.put(135, "积分不足");
		errCode.put(137, "起始时间为空");
		errCode.put(138, "结束时间为空");
		errCode.put(139, "搜索条件为空");
		errCode.put(140, "已经评价过");
		errCode.put(141, "是否被搜索参数为空");
		errCode.put(142, "隐私参数为空");
		errCode.put(143, "已经授权");
		errCode.put(150, "字段为空!");
		errCode.put(151, "账号为空!");
		errCode.put(152, "字段为空!");
	}
	
	/**
	 * 得到正确的数据
	 * @param result 请求返回的数据
	 * @return null 没有数据
	 * 作者:fighter <br />
	 * 创建时间:2013-5-21<br />
	 * 修改时间:<br />
	 */
	public static String getData(Context context, String result){
		if(result == null){
			Toast.makeText(context, "网络请求超时!", Toast.LENGTH_SHORT).show();
			return null;
		}
		
		try {
			JSONObject jsonObject = JSON.parseObject(result);
			int errorCode = jsonObject.getIntValue("error");
			if(errorCode == 0){
				return jsonObject.getString("data");
			}else{
				String info = getErrCodeDescribe(errorCode);
				if(TextUtils.isEmpty(info)){
					Toast.makeText(context, "出错啦!"+errorCode, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, info , Toast.LENGTH_SHORT).show();
				}
				return null;
			}
		} catch (Exception e) {
			Toast.makeText(context, "发生未知错误!", Toast.LENGTH_SHORT).show();
			Log.e(TAG, "getData()", e);
			Log.e(TAG,  result,e);
			return null;
		}
		
	}
	
	/**
	 * 获取数据
	 * @param t
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public static String getData(String t){
		try {
			JSONObject jsonObject = JSON.parseObject(t);
			return jsonObject.getString("data");
		} catch (Exception e) {
			Log.e(TAG, "getData()"+t, e);
			return "";
		}
	}
	
	/**
	 * 显示错误码
	 * @param context
	 * @param error
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public static void toastError(Context context, int error){
		String info = getErrCodeDescribe(error);
		if(TextUtils.isEmpty(info)){
			Toast.makeText(context, "出现未知错误!", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, info , Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 获取错误码!
	 * @param t
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-27<br />
	 * 修改时间:<br />
	 */
	public static int getError(String t){
		if(t == null){
			return -1;
		}
		try {
			JSONObject jsonObject = JSON.parseObject(t);
			int errorCode = jsonObject.getIntValue("error");
			
			return errorCode;
		} catch (Exception e) {
			Log.e(TAG, "getError()" + t, e);
			return -1;
		}
		
	}
	
	/**
	 * 获取错误信息的描述
	 * @param code 错误代号
	 * @return  错误代号的描述
	 * 作者:fighter <br />
	 * 创建时间:2013-5-21<br />
	 * 修改时间:<br />
	 */
	public static String getErrCodeDescribe(int code){
		return errCode.get(code);
	}
}
