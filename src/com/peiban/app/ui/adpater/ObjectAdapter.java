package com.peiban.app.ui.adpater;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ObjectAdapter<T> extends BaseAdapter{
	private List<T> mData;
	private LayoutInflater mInflater;
	private int sourceId;		// 布局id.
	private ViewBinder binder;
	
	public ObjectAdapter(Context context, List<T> mData, int sourceId,
			ViewBinder binder) {
		super();
		this.mData = mData;
		this.mInflater = LayoutInflater.from(context);
		this.sourceId = sourceId;
		this.binder = binder;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position,convertView, parent, sourceId);
	}

	private View createViewFromResource(int position, View convertView,
            ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }

        bindView(position, v);

        return v;
    }
	
	private void bindView(int position, View view) {
		binder.setViewValue(view, mData.get(position));
	}
	
	 public static interface ViewBinder {
		 boolean setViewValue(View view, Object data);
	 }
}
