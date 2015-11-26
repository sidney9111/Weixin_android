package com.peiban.command;

import java.util.Comparator;

import com.peiban.app.vo.SessionVo;

public class SessionComparator implements Comparator<SessionVo>{

	@Override
	public int compare(SessionVo cv1, SessionVo cv2) {
		try {
			int index = 0;
			Long o1 = cv1.getSessionList().getUpdateTime();
			Long o2 = cv2.getSessionList().getUpdateTime();
			if(o1 < o2){
				index = 1;
			}else if(o1 > o2){
				index = -1;
			}
		     return index;
		} catch (Exception e) {

			return 0;
		}
		
	}

}
