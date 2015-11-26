package com.peiban.adapter;

import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;

public abstract class ObjectBaseAdapter<T> extends BaseAdapter{
	protected List<T> lists = new ArrayList<T>();
	
	public void addList(List<T> lists){
		this.lists.addAll(lists);
		notifyDataSetInvalidated();
	}
	
	public void addObject(T t){
		if(this.lists.contains(t)){
			return;
		}
		lists.add(0, t);
		notifyDataSetChanged();
	}
	
	public void removeObject(T t){
		lists.remove(t);
		notifyDataSetInvalidated();
	}
	
	public void removeAll(){
		
		lists.clear();
		notifyDataSetInvalidated();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.lists.size();
	}

	@Override
	public T getItem(int position) {
		try {
			return this.lists.get(position);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
