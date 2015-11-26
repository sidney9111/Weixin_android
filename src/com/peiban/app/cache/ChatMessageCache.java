package com.peiban.app.cache;

import java.util.List;

import net.tsz.afinal.FinalDb;

import com.peiban.vo.MessageInfo;

public class ChatMessageCache {
	private static ChatMessageCache instence;
	
	private FinalDb finalDb;
	
	private ChatMessageCache(FinalDb finalDb){
		this.finalDb = finalDb;
	}
	
	public synchronized static ChatMessageCache getInstance(FinalDb finalDb){
		if(instence == null){
			instence = new ChatMessageCache(finalDb);
		}
		return instence;
	}
	
	public List<MessageInfo> getChatListMessage(String chatId, String fchatId){
		StringBuffer strWheres = new StringBuffer();
		strWheres.append("(fromId = '").append(fchatId)
				.append("' and toId = '").append(chatId).append("') or ") 
				.append("(fromId = '").append(chatId)
				.append("' and toId = '").append(fchatId)
				.append("') order by id asc");
		return finalDb.findAllByWhere(
				MessageInfo.class, strWheres.toString());
	}
}
