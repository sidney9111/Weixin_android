/**
 * @Title: AlbumDb.java 
 * @Package com.shangwupanlv.app.ui.common 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-28 上午10:48:09 
 * @version V1.0
 */
package com.peiban.app.ui.common;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.peiban.vo.AlbumVo;
import com.peiban.vo.PhotoVo;

public class AlbumDb {
	private FinalDb albumDb;
    private Context context;
	public AlbumDb(Context context, FinalDb finalDb) {
		albumDb = finalDb;
		this.context=context;
	}

	public void albumDeleteAll(){
	
		albumDb.deleteByWhere(AlbumVo.class,null);
	}
	/**
	 * 保存相册列表到数据库
	 * */
	public void albumSave(String data) {
		if (data != null) {
			JSONObject json = JSON.parseObject(data);
			AlbumVo albumVo = JSON.toJavaObject(json, AlbumVo.class);
			albumDb.save(albumVo);
		}
	}
	/**
	 * 保存相册列表到数据库
	 * */
	public void albumUpdate(String data) {
		if (data != null) {
			List<AlbumVo> albumVoList = JSON.parseArray(data,
					AlbumVo.class);
			for (AlbumVo albumVo : albumVoList) {
			albumDb.update(albumVo);	
			}
		}
	}
	/**
	 * 修改相册
	 * */
	public void albumUpdate(AlbumVo albumVo){
		albumDb.update(albumVo);
	}


	/**
	 * 删除相册
	 * */
	public void albumDelete(String aid) {
		if (!"".equals(aid) || aid != null) {
			albumDb.deleteByWhere(AlbumVo.class,"aid='"+aid+"'");
			PhotoDb photoDb=new PhotoDb(context, albumDb);
			photoDb.photoDeleteByAid(aid);
		}
	}

	/**
	 * 保存相册列表到数据库
	 * */

	public void albumListSave(String data) {
		if (!"".equals(data)) {
			List<AlbumVo> albumVoList = JSON.parseArray(data,
					AlbumVo.class);
			albumDb.saveList(albumVoList);
		}
	}
	/**
	 * 根据主键找图片
	 * */
	public AlbumVo albumfindByAid(String aid) {
		AlbumVo albumVo = null;
		if (!"".equals(aid) || aid != null) {
			albumVo = albumDb.findById(aid, AlbumVo.class);
		}
		return albumVo;
	}
	/**
	 * 从数据库查询出相册信息
	 * */
	public List<AlbumVo> findAlbumList() {
		return albumDb.findAll(AlbumVo.class);
	}
}
