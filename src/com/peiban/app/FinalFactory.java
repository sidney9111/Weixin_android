package com.peiban.app;

import com.peiban.R;
import com.peiban.vo.UserInfoVo;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.bitmap.core.BitmapCommonUtils;
import android.content.Context;

public class FinalFactory {
	
	/**
	 * 
	 * @param context
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-24<br />
	 * 修改时间:<br />
	 */
	public static FinalBitmap createFinalBitmap(Context context){
		FinalBitmap finalBitmap = FinalBitmap.create(context, BitmapCommonUtils
				.getDiskCacheDir(context, "panlv/cache").getAbsolutePath());
		finalBitmap.configCalculateBitmapSizeWhenDecode(true);
		return finalBitmap;
	}
	
	public static FinalBitmap createFinalAlbumBitmap(Context context){
		FinalBitmap finalBitmap = FinalBitmap.create(context, BitmapCommonUtils
				.getDiskCacheDir(context, "panlv/cache").getAbsolutePath());
		finalBitmap.configCalculateBitmapSizeWhenDecode(true);
		finalBitmap.configBitmapMaxHeight(350);
		finalBitmap.configBitmapMaxWidth(350);
		finalBitmap.configLoadingImage(R.drawable.shakepic_noimage);
		finalBitmap.configLoadfailImage(R.drawable.shakepic_noimage);
		return finalBitmap;
	}

	public static FinalDb createFinalDb(Context context, UserInfoVo info) {
		return FinalDb.create(context, info.getPhone(), false);
	}
}
