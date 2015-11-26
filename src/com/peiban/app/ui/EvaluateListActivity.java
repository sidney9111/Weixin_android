/**
 * @Title: GetBackActivity.java 
 * @Package com.shangwupanlv.app.ui 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-17 上午10:57:55 
 * @version V1.0
 */
package com.peiban.app.ui;

import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.peiban.R;
import com.peiban.adapter.ObjectBaseAdapter;
import com.peiban.app.Constants;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.UserInfoApi;
import com.peiban.app.ui.common.FinalOnloadBitmap;
import com.peiban.command.TextdescTool;
import com.peiban.vo.CustomerVo;
import com.shangwupanlv.widget.XListView;
import com.shangwupanlv.widget.XListView.IXListViewListener;

public class EvaluateListActivity extends BaseActivity implements
		IXListViewListener {
	@ViewInject(id = R.id.evaluate_list)
	private XListView xListView;

	private int currPage = 0;
	private int pageSize = 10;
	private EvaluateListAdapter adapter;

	private UserInfoApi userInfoApi;
	private CustomerVo customerVo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evaluate_list);
		baseInit();
	}

	protected void baseInit() {
		super.baseInit();

		customerVo = (CustomerVo) getIntent().getSerializableExtra("data");
		if (customerVo == null) {
			finish();
			return;
		}

		adapter = new EvaluateListAdapter();
		xListView.setXListViewListener(this);
		xListView.setAdapter(adapter);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.startLoadMore();
	}

	@Override
	protected void initTitle() {
		setTitleContent("评价列表");
		setBtnBack();
	}

	@Override
	protected void titleBtnBack() {
		super.titleBtnBack();
	}

	private void pullScoreList() {
		if (!checkNetWork()) {
			stopLoad();
			return;
		}

		if (userInfoApi == null) {
			userInfoApi = new UserInfoApi();
		}

		userInfoApi.getScoreList(getUserInfoVo().getUid(), customerVo.getUid(),
				(currPage) + "", pageSize + "", new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						Log.v("积分列表", t);
						stopLoad();
						try {
							String data = ErrorCode
									.getData(getBaseContext(), t);
							if (!TextUtils.isEmpty(data)) {
								try {
									List<CustomerVo> customerVos = JSON
											.parseArray(data, CustomerVo.class);
									if (currPage <= 1) {
										adapter.removeAll();
									}
									adapter.addList(customerVos);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								xListView.setPullLoadEnable(false);
							}
						} catch (Exception e) {
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						stopLoad();
						showToast("服务器响应错误!");
					}

				});
	}

	class EvaluateListAdapter extends ObjectBaseAdapter<CustomerVo> {

		@Override
		public void addList(List<CustomerVo> lists) {
			if (lists == null) {
				return;
			}

			super.lists.addAll(lists);
			notifyDataSetInvalidated();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holderView;
			if (convertView == null) {
				holderView = new HolderView();
				convertView = getLayoutInflater().inflate(
						R.layout.evaluate_list_item, null);
				holderView.head = (ImageView) convertView
						.findViewById(R.id.evaluate_img_head);
				holderView.time = (TextView) convertView
						.findViewById(R.id.evaulate_txt_time);
				holderView.appearancetag = (RatingBar) convertView
						.findViewById(R.id.appearance_tag);
				holderView.service_tag = (RatingBar) convertView
						.findViewById(R.id.service_tag);

				holderView.txtAppearance = (TextView) convertView
						.findViewById(R.id.appearance_num);
				holderView.txtService = (TextView) convertView
						.findViewById(R.id.service_num);
				convertView.setTag(holderView);
			} else {
				holderView = (HolderView) convertView.getTag();
			}
			CustomerVo customerVo = getItem(position);
			if (customerVo != null) {
				FinalOnloadBitmap.finalDisplay(getBaseContext(), customerVo,
						holderView.head, getHeadBitmap());
				holderView.head.setTag(customerVo);
				holderView.head.setOnClickListener(evaluateListener);
				holderView.time.setText(TextdescTool.getTime(customerVo
						.getTime() + "000"));
				float ascore = 0.0f;
				float sscore = 0.0f;
				try {
					ascore = Float.valueOf(TextdescTool.floatMac(customerVo
							.getAscore()));
				} catch (Exception e) {
				}

				try {
					sscore = Float.valueOf(TextdescTool.floatMac(customerVo
							.getSscore()));
				} catch (Exception e) {
				}

				holderView.txtAppearance.setText(ascore + "");
				holderView.txtService.setText(sscore + "");

				holderView.appearancetag.setRating(ascore);
				holderView.service_tag.setRating(sscore);
			}

			return convertView;
		}

	}

	class HolderView {
		ImageView head;
		TextView time;
		RatingBar appearancetag;
		RatingBar service_tag;
		TextView txtAppearance, txtService;
	}

	private void stopLoad() {
		xListView.stopLoadMore();
		xListView.stopRefresh();
	}

	@Override
	public void onRefresh() {
		currPage = 1;
		pullScoreList();
	}

	@Override
	public void onLoadMore() {
		currPage ++;
		pullScoreList();

	}

	private View.OnClickListener evaluateListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			CustomerVo customerVo = (CustomerVo) v.getTag();
			if (customerVo != null) {
				showEvaluateUser(customerVo);
			}

		}
	};

	private void showEvaluateUser(CustomerVo customerVo) {
		Intent intent = null;
		if (Constants.CustomerType.CHATTING
				.equals(customerVo.getCustomertype())
				&& "1".equals(customerVo.getFriend())) {
			intent = new Intent(EvaluateListActivity.this,
					FriendChattingActivity.class);
		} else if (Constants.CustomerType.WITHCHAT.equals(customerVo
				.getCustomertype()) && "1".equals(customerVo.getFriend())) {
			intent = new Intent(EvaluateListActivity.this,
					FriendFindPartnerActivity.class);
		} else if (Constants.CustomerType.WITHCHAT.equals(customerVo
				.getCustomertype())) {
			intent = new Intent(EvaluateListActivity.this,
					StrangerFindPartnerActivity.class);
		} else {
			intent = new Intent(EvaluateListActivity.this,
					StrangerChattingActivity.class);
		}
		intent.putExtra("data", customerVo);

		startActivity(intent);
	}
}
