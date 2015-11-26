package com.peiban.service.type;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.peiban.SharedStorage;

public class XmppTypeManager {
	private SharedPreferences sharedPreferences;
	public XmppTypeManager(Context context){
		sharedPreferences = SharedStorage.getCoreSharedPreferences(context);
	}
	
	 /**
     * 保存xmpp状态
     * @param type  {@link XmppType}
     * 作者:fighter <br />
     * 创建时间:2013-6-1<br />
     * 修改时间:<br />
     */
    public void saveXmppType(String type){
    	Editor editor
    	 = sharedPreferences.edit();
    	editor.putString(XmppType.XMPP_KEY, type);
    	System.out.println("提交状态:" + editor.commit());;
    }
    
    public String getXmppType(){
    	return sharedPreferences.getString(XmppType.XMPP_KEY, XmppType.XMPP_STATE_STOP);
    }
    
    /**
     * 是否认证成功了的!
     * @return
     * 作者:fighter <br />
     * 创建时间:2013-6-3<br />
     * 修改时间:<br />
     */
    public boolean isSend(){
    	System.out.println("XmppType:" + getXmppType());
    	if(XmppType.XMPP_STATE_AUTHENTICATION.equals(getXmppType())){
    		return true;
    	}else{
    		return false;
    	}
    }
}
