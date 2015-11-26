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
import com.peiban.vo.PhotoVo;

public class PhotoDb {
	private FinalDb photoDb;

	public PhotoDb(Context context, FinalDb finalDb) {
		photoDb = finalDb;
	}

	/**
	 * 保存相片到数据库
	 * */
	public void PhotoSave(PhotoVo photoVo) {
		if (photoVo != null) {
			photoDb.save(photoVo);
		}
	}

	/**
	 * 删除相片
	 **/
	public void photoDeleteByAid(String aid) {
		if (!"".equals(aid) || aid != null) {
			photoDb.deleteByWhere(PhotoVo.class, "aid='" + aid + "'");
		}
	}

	/**
	 * 删除全部相片
	 **/
	public void photoDeleteAll() {
		photoDb.deleteByWhere(PhotoVo.class,null);

	}

	/**
	 * 根据主键找图片
	 * */
	public PhotoVo photofindByPid(String pid) {
		PhotoVo photoVo = null;
		if (!"".equals(pid) || pid != null) {
			photoVo = photoDb.findById(pid, PhotoVo.class);
		}
		return photoVo;
	}

	public void photoDeleteByPid(String pid) {
		if (!"".equals(pid) || pid != null) {
			photoDb.deleteByWhere(PhotoVo.class, "pid='" + pid + "'");
		}
	}

	/**
	 * 保存相册列表到数据库
	 * */

	public void PhotoListSave(String data) {
		if (!"".equals(data)) {
			List<PhotoVo> photoVoList = JSON.parseArray(data, PhotoVo.class);
			photoDb.saveList(photoVoList);
		}
	}

	/**
	 * 从数据库查询出相册信息
	 * */
	public List<PhotoVo> findPhotoList(String aid) {
		return photoDb.findAllByWhere(PhotoVo.class, "aid = '" + aid + "'");
	}
}
