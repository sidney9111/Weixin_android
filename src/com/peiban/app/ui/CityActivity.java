/**
 * @Title: CityActivity.java 
 * @Package com.example.baidumap 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-9 下午1:44:07 
 * @version V1.0
 */
package com.peiban.app.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.peiban.R;

public class CityActivity extends Activity {
	/** 组件相关 */
	private ListView cityList;
	/** 属性 */
	private String[] city;
	private String[] lon,lat;
	private ArrayList<HashMap<String, Object>> listItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		initData();
		initList();
		initView();
		setOnClick();
	}

	/** 点击事件 */
	private void setOnClick() {
		// TODO Auto-generated method stub
		cityList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, Object> map = (HashMap<String, Object>) cityList
						.getItemAtPosition(pos);
				Intent intent = new Intent();
				intent.putExtra("city", map.get("city").toString());
				intent.putExtra("lon", map.get("lon").toString());
				intent.putExtra("lat", map.get("lat").toString());
				setResult(0, intent);
				finish();
			}
		});
	}

	/** 初始化城市数据 */
	private void initData() {
		city = new String[] { "成都", "北京", "上海" ,"广州","深圳","重庆","杭州","南京","天津","武汉","长沙"};
		lon = new String[]{"104.067923","116.395645","121.487899","113.30765","114.025974","106.530635","120.219375","118.778074","117.210813","114.3162","112.979353"};
		lat = new String[]{"30.679943","39.929986","31.249162","23.120049","22.546054","29.544606","30.259244","32.057236","39.14393","30.581084","28.213478"};
	}

	private void initList() {
		listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < city.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("city", city[i]);
			map.put("lon", lon[i]);
			map.put("lat", lat[i]);
			listItem.add(map);
		}

	}

	/** 初始化布局 */
	private void initView() {
		cityList = (ListView) findViewById(R.id.cityList);
		SimpleAdapter adapter = new SimpleAdapter(CityActivity.this, listItem,
				R.layout.city_layout, new String[] { "city", "lon","lat" },
				new int[] { R.id.cityname });
		cityList.setAdapter(adapter);
	}
}
