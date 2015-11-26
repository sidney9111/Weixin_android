package com.peiban.vo;
/**
 * 
 * 功能： 陪聊附近配置文件 <br />
 * 日期：2013-5-31<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class ChattingLocationConfigVo extends LocationConfigVo{
	
	private String serverLocation;
	private String authState;
	/**
	 *  经纪人
	 * 	经纪人认证：是后台可以把某个用户设为经纪人，<br/>
	 *  经纪人也是陪聊 寻聊里面的， 只是会有个是否是经纪人的标记 如果是经纪人 <br/>
	 *  在附近那个列表里面有个经纪人的标记
	 **/
	private String economiMan;
	
	private String ageOne;
	private String ageTwo;
	private String heightOne;
	private String heightTwo;
	private String bodyOne;
	private String bodyTwo;
	
	/**
	 * @return the serverLocation
	 */
	public String getServerLocation() {
		return serverLocation;
	}
	/**
	 * @return the authState
	 */
	public String getAuthState() {
		return authState;
	}
	/**
	 * @return the economiMan
	 */
	public String getEconomiMan() {
		return economiMan;
	}
	/**
	 * @return the ageOne
	 */
	public String getAgeOne() {
		return ageOne;
	}
	/**
	 * @return the ageTwo
	 */
	public String getAgeTwo() {
		return ageTwo;
	}
	/**
	 * @return the heightOne
	 */
	public String getHeightOne() {
		return heightOne;
	}
	/**
	 * @return the heightTwo
	 */
	public String getHeightTwo() {
		return heightTwo;
	}
	/**
	 * @return the bodyOne
	 */
	public String getBodyOne() {
		return bodyOne;
	}
	/**
	 * @return the bodyTwo
	 */
	public String getBodyTwo() {
		return bodyTwo;
	}
	/**
	 * @param serverLocation the serverLocation to set
	 */
	public void setServerLocation(String serverLocation) {
		this.serverLocation = serverLocation;
	}
	/**
	 * @param authState the authState to set
	 */
	public void setAuthState(String authState) {
		this.authState = authState;
	}
	/**
	 * @param economiMan the economiMan to set
	 */
	public void setEconomiMan(String economiMan) {
		this.economiMan = economiMan;
	}
	/**
	 * @param ageOne the ageOne to set
	 */
	public void setAgeOne(String ageOne) {
		this.ageOne = ageOne;
	}
	/**
	 * @param ageTwo the ageTwo to set
	 */
	public void setAgeTwo(String ageTwo) {
		this.ageTwo = ageTwo;
	}
	/**
	 * @param heightOne the heightOne to set
	 */
	public void setHeightOne(String heightOne) {
		this.heightOne = heightOne;
	}
	/**
	 * @param heightTwo the heightTwo to set
	 */
	public void setHeightTwo(String heightTwo) {
		this.heightTwo = heightTwo;
	}
	/**
	 * @param bodyOne the bodyOne to set
	 */
	public void setBodyOne(String bodyOne) {
		this.bodyOne = bodyOne;
	}
	/**
	 * @param bodyTwo the bodyTwo to set
	 */
	public void setBodyTwo(String bodyTwo) {
		this.bodyTwo = bodyTwo;
	}
	
	
}
