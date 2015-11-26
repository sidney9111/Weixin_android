package com.peiban.command;

import java.io.File;
import java.io.IOException;

import net.tsz.afinal.bitmap.core.BitmapCommonUtils;

import com.peiban.application.PeibanApplication;

import android.content.Context;
import android.os.Environment;

public class FileUtil {
	private static File updateDir = null;
	private static File updateFile = null;

	/***
	 * 创建文件
	 */
	public static File createFile(Context context, String name) {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			if(updateFile != null){
				return updateFile;
			}
			
			updateDir = new File(BitmapCommonUtils.getExternalCacheDir(context).getParentFile()
					+ "/" + PeibanApplication.downloadDir);
			updateFile = new File(updateDir + "/" + name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
		return updateFile;
	}
}
