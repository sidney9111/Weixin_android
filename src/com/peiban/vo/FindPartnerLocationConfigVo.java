package com.peiban.vo;

/**
 * 
 * 功能： 寻伴附近条件配置 <br />
 * glory  荣誉
 * 日期：2013-5-31<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class FindPartnerLocationConfigVo extends LocationConfigVo{
	private String gloryNumOne;
	private String gloryNumTwo;
	
	private String vipState;

	/**
	 * @return the gloryNumOne
	 */
	public String getGloryNumOne() {
		return gloryNumOne;
	}

	/**
	 * @return the gloryNumTwo
	 */
	public String getGloryNumTwo() {
		return gloryNumTwo;
	}

	/**
	 * @return the vipState
	 */
	public String getVipState() {
		return vipState;
	}

	/**
	 * @param gloryNumOne the gloryNumOne to set
	 */
	public void setGloryNumOne(String gloryNumOne) {
		this.gloryNumOne = gloryNumOne;
	}

	/**
	 * @param gloryNumTwo the gloryNumTwo to set
	 */
	public void setGloryNumTwo(String gloryNumTwo) {
		this.gloryNumTwo = gloryNumTwo;
	}

	/**
	 * @param vipState the vipState to set
	 */
	public void setVipState(String vipState) {
		this.vipState = vipState;
	}
	
	
}
