/**
 * @Title: ConditionFilterActivity.java 
 * @Packhonor com.shangwupanlv.app.ui 
 * @Description: TODO 筛选页面
 * @author lcy   
 * @date 2013-5-17 上午10:57:55 
 * @version V1.0
 */
package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.peiban.R;
import com.peiban.app.FilterConfig;
import com.peiban.command.FileTool;
import com.peiban.command.TextdescTool;

public class ConditionFilterVipActivity extends BaseActivity {
	/**
	 * 性别选择
	 */
	@ViewInject(id = R.id.filter_btn_boy)
	private Button btnBoy;// 男
	@ViewInject(id = R.id.filter_btn_gril)
	private Button btnGril;// 女
	@ViewInject(id = R.id.filter_btn_sexnot)
	private Button btnSexnot;// 不限
	/**
	 * vip
	 * */
	@ViewInject(id = R.id.filter_btn_yes)
	private Button btnYes;// 是
	@ViewInject(id = R.id.filter_btn_no)
	private Button btnNo;// 否
	@ViewInject(id = R.id.filter_btn_not)
	private Button btnNot;// 不限
	/**
	 * 登录时间
	 **/
	@ViewInject(id = R.id.filter_btn_online)
	private Button btnOnline;// 在线  改为 30分钟内
	@ViewInject(id = R.id.filter_btn_threedday)
	private Button btnThreedday;// 3天内  改为 1天内
	@ViewInject(id = R.id.filter_btn_sevenday)
	private Button btnSevenday;// 7天内  改为 3天内
	@ViewInject(id = R.id.filter_btn_linenot)
	private Button btnLinenot;// 不限

	/**
	 * 荣誉
	 * */
	@ViewInject(id = R.id.filter_edit_honorone)
	private EditText honorOne;// 荣誉1
	@ViewInject(id = R.id.filter_edit_honortwo)
	private EditText honorTwo;// 荣誉2
	@ViewInject(id = R.id.filter_btn_honorrevoca)
	private Button honorRevoca;// 清空荣誉
	/**
	 * 距离
	 * */
	@ViewInject(id = R.id.filter_edit_distanceone)
	private EditText distanceOne;
	@ViewInject(id = R.id.filter_edit_distancetwo)
	private EditText distanceTwo;
	@ViewInject(id = R.id.filter_btn_distancerevoca)
	private Button distanceRevoca;
	/**
	 * 配置文件
	 * */
	private FilterConfig filterConfig;
	/**
	 * Activity相关
	 * */
	private btnOnlick l;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conditionfilter_vip);
		filterConfig = new FilterConfig(this);
		l = new btnOnlick();
		initFilter();
		OnClik();
		baseInit();
		hideSoftKeyboard();
	}

	/** 初始化条件筛选 */
	private void initFilter() {
		// TODO Auto-generated method stub
		// 性别选择
		if (filterConfig.getSex().equals("1")) {
			visibleBoy();
		} else if (filterConfig.getSex().equals("0")) {
			visibleGirl();
		} else if (filterConfig.getSex().equals("")) {
			visibleSexnot();
		}
		// 登录时间
		if (filterConfig.getLogintime().equals("1")) {
			visibleOnline();
		} else if (filterConfig.getLogintime().equals("2")) {
			visibleThreedday();
		} else if (filterConfig.getLogintime().equals("3")) {
			visibleSevenday();
		} else if (filterConfig.getLogintime().equals("4")) {
			visibleLinenot();
		}

		// 是否Vip
		if (filterConfig.getVip().equals("1")) {
			visibleYes();
		} else if (filterConfig.getVip().equals("0")) {
			visibleNo();
		} else if (filterConfig.getVip().equals("")) {
			visibleNot();
		}
		distanceOne.setText(filterConfig.getDistanceone());
		distanceTwo.setText(filterConfig.getDistancetwo());
		honorOne.setText(filterConfig.getHonorone());
		honorTwo.setText(filterConfig.getHonortwo());
		
	}

	/**
	 * 绑定点击事件
	 * */
	private void OnClik() {
		btnBoy.setOnClickListener(l);
		btnGril.setOnClickListener(l);
		btnLinenot.setOnClickListener(l);
		btnYes.setOnClickListener(l);
		btnNo.setOnClickListener(l);
		btnNot.setOnClickListener(l);
		btnOnline.setOnClickListener(l);
		btnSevenday.setOnClickListener(l);
		btnSexnot.setOnClickListener(l);
		btnThreedday.setOnClickListener(l);
		distanceRevoca.setOnClickListener(l);
		honorRevoca.setOnClickListener(l);
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleContent(R.string.conditionfilter_title);
		setBtnBack();
		setTitleRight(R.string.btn_enter);
	}

	@Override
	protected void titleBtnBack() {
		super.titleBtnBack();
	}

	@Override
	protected void titleBtnRight() {
		// TODO Auto-generated method stub
		super.titleBtnRight();
		getTextValue();
		if (getTextValue() == true) {
			filterConfig.FilterCommit();
			setResult(Activity.RESULT_OK);
			finish();
		}
	}

	/**
	 * 获取输入框的值
	 * */
	private boolean getTextValue() {
		String distanceOnestr = distanceOne.getText().toString();
		String distanceTwostr = distanceTwo.getText().toString();
		String honorOnestr = honorOne.getText().toString();
		String honorTwostr = honorTwo.getText().toString();
		if (!TextUtils.isEmpty(distanceOnestr)
				&& !TextUtils.isEmpty(distanceTwostr)) {
			boolean isSort = FileTool.isSort(distanceOnestr, distanceTwostr);
			if (isSort) {
				filterConfig.setDistanceone(distanceOnestr);
				filterConfig.setDistancetwo(distanceTwostr);
			} else {
				showToast(getResources().getString(R.string.issort));
				return false;
			}
		} else {
			showToast("年龄为空！");
			return false;
		}
		if (!TextUtils.isEmpty(honorOnestr) && !TextUtils.isEmpty(honorTwostr)) {
			boolean isSort = FileTool.isSort(honorOnestr, honorTwostr);
			if (isSort) {
				filterConfig.setHonorone(honorOnestr);
				filterConfig.setHonortwo(honorTwostr);
			} else {
				showToast(getResources().getString(R.string.issort));
				return false;
			}
		} else {
			showToast("荣誉为空！");
			return false;
		}
		return true;
	}

	/**
	 * 点击事件，将对应的属性值添加到配置文件中 eg:男--1，女--2，性别不限--3 以此类推
	 * */
	class btnOnlick implements OnClickListener {
		String day = null;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.filter_btn_boy:
				visibleBoy();
				filterConfig.setSex("1");
				break;
			case R.id.filter_btn_gril:
				visibleGirl();
				filterConfig.setSex("0");
				break;
			case R.id.filter_btn_sexnot:
				visibleSexnot();
				filterConfig.setSex("");
				break;
			case R.id.filter_btn_online:
				visibleOnline();
				day = TextdescTool.dayBefore(0.5f);
				filterConfig.setLogintime("1");
				break;
			case R.id.filter_btn_threedday:
				visibleThreedday();
				day = TextdescTool.dayBefore(1f);
				filterConfig.setLogintime("2");
				break;
			case R.id.filter_btn_sevenday:
				visibleSevenday();
				day = TextdescTool.dayBefore(3f);
				filterConfig.setLogintime("3");
				break;
			case R.id.filter_btn_linenot:
				visibleLinenot();
				day = "";
				filterConfig.setLogintime("4");
				break;
			case R.id.filter_btn_yes:
				visibleYes();
				filterConfig.setVip("1");
				break;
			case R.id.filter_btn_no:
				visibleNo();
				filterConfig.setVip("0");
				break;
			case R.id.filter_btn_not:
				visibleNot();
				filterConfig.setVip("");
				break;
			case R.id.filter_btn_distancerevoca:
				distanceOne.setText("0");
				distanceTwo.setText("0");
				break;
			case R.id.filter_btn_honorrevoca:
				honorOne.setText("0");
				honorTwo.setText("0");
				break;

			}
		}
	}
	/**
	 * 显示“男”
	 * */
	private void visibleBoy() {
		btnBoy.setBackgroundResource(R.drawable.btn_fiter_left_d);
		btnGril.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnSexnot.setBackgroundResource(R.drawable.btn_fiter_right_n);
		btnBoy.setTextColor(getResources().getColor(R.color.white));
		btnGril.setTextColor(getResources().getColor(R.color.black));
		btnSexnot.setTextColor(getResources().getColor(R.color.black));
	}
	/**
	 * 显示“女”
	 * */
	private void visibleGirl() {
		btnBoy.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnGril.setBackgroundResource(R.drawable.btn_fiter_middle_d);
		btnSexnot.setBackgroundResource(R.drawable.btn_fiter_right_n);
		btnBoy.setTextColor(getResources().getColor(R.color.black));
		btnGril.setTextColor(getResources().getColor(R.color.white));
		btnSexnot.setTextColor(getResources().getColor(R.color.black));
	}
	/**
	 * 显示“性别不限”
	 * */

	private void visibleSexnot() {
		btnBoy.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnGril.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnSexnot.setBackgroundResource(R.drawable.btn_fiter_right_d);

		btnBoy.setTextColor(getResources().getColor(R.color.black));
		btnGril.setTextColor(getResources().getColor(R.color.black));
		btnSexnot.setTextColor(getResources().getColor(R.color.white));
	}

	/**
	 * 显示“在线”
	 * */
	private void visibleOnline() {
		btnOnline.setBackgroundResource(R.drawable.btn_fiter_left_d);
		btnThreedday.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnSevenday.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnLinenot.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnOnline.setTextColor(getResources().getColor(R.color.white));
		btnThreedday.setTextColor(getResources().getColor(R.color.black));
		btnSevenday.setTextColor(getResources().getColor(R.color.black));
		btnLinenot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“3天内”
	 * */
	private void visibleThreedday() {
		btnOnline.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnThreedday.setBackgroundResource(R.drawable.btn_fiter_middle_d);
		btnSevenday.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnLinenot.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnOnline.setTextColor(getResources().getColor(R.color.black));
		btnThreedday.setTextColor(getResources().getColor(R.color.white));
		btnSevenday.setTextColor(getResources().getColor(R.color.black));
		btnLinenot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“7天内”
	 * */
	private void visibleSevenday() {
		btnOnline.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnThreedday.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnSevenday.setBackgroundResource(R.drawable.btn_fiter_middle_d);
		btnLinenot.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnOnline.setTextColor(getResources().getColor(R.color.black));
		btnThreedday.setTextColor(getResources().getColor(R.color.black));
		btnSevenday.setTextColor(getResources().getColor(R.color.white));
		btnLinenot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“登录时间不限”
	 * */
	private void visibleLinenot() {
		btnOnline.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnThreedday.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnSevenday.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnLinenot.setBackgroundResource(R.drawable.btn_fiter_right_d);
		btnOnline.setTextColor(getResources().getColor(R.color.black));
		btnThreedday.setTextColor(getResources().getColor(R.color.black));
		btnSevenday.setTextColor(getResources().getColor(R.color.black));
		btnLinenot.setTextColor(getResources().getColor(R.color.white));
	}

	/**
	 * 显示“是”
	 * */
	private void visibleYes() {
		btnYes.setBackgroundResource(R.drawable.btn_fiter_left_d);
		btnNo.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnNot.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnYes.setTextColor(getResources().getColor(R.color.white));
		btnNo.setTextColor(getResources().getColor(R.color.black));
		btnNot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“否”
	 * */
	private void visibleNo() {
		btnYes.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnNo.setBackgroundResource(R.drawable.btn_fiter_middle_d);
		btnNot.setBackgroundResource(R.drawable.btn_fiter_right_n);
		btnYes.setTextColor(getResources().getColor(R.color.black));
		btnNo.setTextColor(getResources().getColor(R.color.white));
		btnNot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“不限”
	 * */
	private void visibleNot() {
		btnYes.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnNo.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnNot.setBackgroundResource(R.drawable.btn_fiter_right_d);

		btnYes.setTextColor(getResources().getColor(R.color.black));
		btnNo.setTextColor(getResources().getColor(R.color.black));
		btnNot.setTextColor(getResources().getColor(R.color.white));
	}
}
