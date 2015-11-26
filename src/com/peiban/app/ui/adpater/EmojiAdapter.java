package com.peiban.app.ui.adpater;

import java.lang.reflect.Field;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.peiban.R;

public class EmojiAdapter extends BaseAdapter{
	private List<String> emojiList;
	private LayoutInflater mInflater;
	private Context context;
	
	public EmojiAdapter(Context context, List<String> emojiList) {
		super();
		this.emojiList = emojiList;
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return emojiList.size();
	}

	@Override
	public String getItem(int position) {
		return emojiList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.emoji_item, null);
		}
		
		try {
			String name = getItem(position);
			Field field = R.drawable.class.getDeclaredField(name);
			int resId = field.getInt(null);
			Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId);
			
			if(convertView instanceof ImageView){
				((ImageView) convertView).setImageBitmap(bm);
			}
		} catch (Exception e) {
		}
		
		return convertView;
	}

}
