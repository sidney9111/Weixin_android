/**
 * @Title: ConditionFilterActivity.java 
 * @Package com.shangwupanlv.app.ui 
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

public class ConditionFilterActivity extends BaseActivity {
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
	 * 服务场合
	 */
	@ViewInject(id = R.id.filter_btn_social)
	private Button btnSocial;// 社交
	@ViewInject(id = R.id.filter_btn_alone)
	private Button btnAlone;// 独处
	@ViewInject(id = R.id.filter_btn_servcenot)
	private Button btnServcenot;// 不限
	/**
	 * 登录时间
	 **/
	@ViewInject(id = R.id.filter_btn_online)
	private Button btnOnline;// 在线
	@ViewInject(id = R.id.filter_btn_threedday)
	private Button btnThreedday;// 3天内
	@ViewInject(id = R.id.filter_btn_sevenday)
	private Button btnSevenday;// 7天内
	@ViewInject(id = R.id.filter_btn_linenot)
	private Button btnLinenot;// 不限
	/**
	 * 是否认证
	 * */
	@ViewInject(id = R.id.filter_btn_authed)
	private Button btnAuthed;// 已认证
	@ViewInject(id = R.id.filter_btn_notauth)
	private Button btnNotauth;
	/** 未认证 */
	@ViewInject(id = R.id.filter_btn_authnot)
	private Button btnAuthnot;// 不限
	/**
	 * 是否经纪人
	 * */
	@ViewInject(id = R.id.filter_btn_yes)
	private Button btnYes;// 是
	@ViewInject(id = R.id.filter_btn_no)
	private Button btnNo;// 否
	@ViewInject(id = R.id.filter_btn_brokernot)
	private Button btnBrokernot;// 不限
	/**
	 * 是否有综合评分
	 * */
	@ViewInject(id = R.id.filter_btn_have)
	private Button btnHave;// 有
	@ViewInject(id = R.id.filter_btn_havenot)
	private Button btnHavenot;// 没有
	@ViewInject(id = R.id.filter_btn_nothave)
	private Button btnNothave;// 不限
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
	 * 年龄
	 * */
	@ViewInject(id = R.id.filter_edit_ageone)
	private EditText ageOne;// 年龄1
	@ViewInject(id = R.id.filter_edit_agetwo)
	private EditText ageTwo;// 年龄2
	@ViewInject(id = R.id.filter_btn_agerevoca)
	private Button ageRevoca;// 清空年龄
	/**
	 * 身高
	 * */
	@ViewInject(id = R.id.filter_edit_tallone)
	private EditText tallOne;// 身高1
	@ViewInject(id = R.id.filter_edit_talltwo)
	private EditText tallTwo;// 身高2
	@ViewInject(id = R.id.filter_btn_tallrevoca)
	private Button tallRevoca;// 清空身高
	/**
	 * 体重
	 * */
	@ViewInject(id = R.id.filter_edit_weightone)
	private EditText weightOne;// 体重1
	@ViewInject(id = R.id.filter_edit_weighttwo)
	private EditText weightTwo;// 体重2
	@ViewInject(id = R.id.filter_btn_weightrevoca)
	private Button weightRevoca;// 清空体重
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
		setContentView(R.layout.conditionfilter);
		filterConfig = new FilterConfig(this);
		l = new btnOnlick();
		hideSoftKeyboard();
		initFilter();
		OnClik();
		baseInit();
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
		// 服务场合
		if (filterConfig.getService().equals("1")) {
			visibleSocial();
		} else if (filterConfig.getService().equals("2")) {
			visibleAlone();
		} else if (filterConfig.getService().equals("3")) {
			visibleServcenot();
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
		// 是否认证
		if (filterConfig.getAuth().equals("1")) {
			visibleAuthed();
		} else if (filterConfig.getAuth().equals("0")) {
			visibleNotauth();
		} else if (filterConfig.getAuth().equals("")) {
			visibleAuthnot();
		}
		// 是否经纪人
		if (filterConfig.getBroker().equals("1")) {
			visibleYes();
		} else if (filterConfig.getBroker().equals("0")) {
			visibleNo();
		} else if (filterConfig.getBroker().equals("")) {
			visibleBrokernot();
		}
		// 是否有综合评分
		if (filterConfig.getComposite().equals("1")) {
			visibleHave();
		} else if ("2".equals(filterConfig.getComposite())) {
			visibleHavenot();
		} else{
			visibleNothave();
		}
		distanceOne.setText(filterConfig.getDistanceone());
		distanceTwo.setText(filterConfig.getDistancetwo());
		ageOne.setText(filterConfig.getAgeone());
		ageTwo.setText(filterConfig.getAgetwo());
		weightOne.setText(filterConfig.getWeightone());
		weightTwo.setText(filterConfig.getWeighttwo());
		tallOne.setText(filterConfig.getTallone());
		tallTwo.setText(filterConfig.getTalltwo());
	}

	/**
	 * 绑定点击事件
	 * */
	private void OnClik() {
		// TODO Auto-generated method stub
		btnBoy.setOnClickListener(l);
		btnGril.setOnClickListener(l);
		btnAlone.setOnClickListener(l);
		btnAuthed.setOnClickListener(l);
		btnAuthnot.setOnClickListener(l);
		btnBrokernot.setOnClickListener(l);
		btnHave.setOnClickListener(l);
		btnHavenot.setOnClickListener(l);
		btnLinenot.setOnClickListener(l);
		btnNo.setOnClickListener(l);
		btnNotauth.setOnClickListener(l);
		btnNothave.setOnClickListener(l);
		btnOnline.setOnClickListener(l);
		btnServcenot.setOnClickListener(l);
		btnSevenday.setOnClickListener(l);
		btnSexnot.setOnClickListener(l);
		btnSocial.setOnClickListener(l);
		btnThreedday.setOnClickListener(l);
		btnYes.setOnClickListener(l);
		distanceRevoca.setOnClickListener(l);
		ageRevoca.setOnClickListener(l);
		tallRevoca.setOnClickListener(l);
		weightRevoca.setOnClickListener(l);
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
		// TODO Auto-generated method stub
		super.titleBtnBack();
	}

	@Override
	protected void titleBtnRight() {
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
		// TODO Auto-generated method stub
		String distanceOnestr = distanceOne.getText().toString();
		String distanceTwostr = distanceTwo.getText().toString();
		String ageOnestr = ageOne.getText().toString();
		String ageTwostr = ageTwo.getText().toString();
		String weightOnestr = weightOne.getText().toString();
		String weightTwostr = weightTwo.getText().toString();
		String tallOnestr = tallOne.getText().toString();
		String tallTwostr = tallTwo.getText().toString();
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
		if (!TextUtils.isEmpty(ageOnestr) && !TextUtils.isEmpty(ageTwostr)) {
			boolean isSort = FileTool.isSort(ageOnestr, ageTwostr);
			if (isSort) {
				filterConfig.setAgeone(ageOnestr);
				filterConfig.setAgetwo(ageTwostr);
			} else {
				showToast(getResources().getString(R.string.issort));
				return false;
			}
		} else {
			showToast("年龄为空！");
			return false;
		}
		if (!TextUtils.isEmpty(weightOnestr)
				&& !TextUtils.isEmpty(weightTwostr)) {
			boolean isSort = FileTool.isSort(weightOnestr, weightTwostr);
			if (isSort) {
				filterConfig.setWeightone(weightOnestr);
				filterConfig.setWeighttwo(weightTwostr);
			} else {
				showToast(getResources().getString(R.string.issort));
				return false;
			}
		} else {
			showToast("体重为空！");
			return false;
		}
		if (!TextUtils.isEmpty(tallOnestr) && !TextUtils.isEmpty(tallTwostr)) {
			boolean isSort = FileTool.isSort(tallOnestr, tallTwostr);
			if (isSort) {
				filterConfig.setTallone(tallOnestr);
				filterConfig.setTalltwo(tallTwostr);
			} else {
				showToast(getResources().getString(R.string.issort));
				return false;
			}
		} else {
			showToast("身高为空！");
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
			// TODO Auto-generated method stub
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
			case R.id.filter_btn_social:
				visibleSocial();
				filterConfig.setService("1");
				break;
			case R.id.filter_btn_alone:
				visibleAlone();
				filterConfig.setService("2");
				break;
			case R.id.filter_btn_servcenot:
				visibleServcenot();
				filterConfig.setService("3");
				break;
			case R.id.filter_btn_online:
				visibleOnline();
				filterConfig.setLogintime("1");
				break;
			case R.id.filter_btn_threedday:
				visibleThreedday();
				filterConfig.setLogintime("2");
				break;
			case R.id.filter_btn_sevenday:
				visibleSevenday();
				filterConfig.setLogintime("3");
				break;
			case R.id.filter_btn_linenot:
				visibleLinenot();
				filterConfig.setLogintime("4");
				break;
			case R.id.filter_btn_authed:
				visibleAuthed();
				filterConfig.setAuth("1");
				break;
			case R.id.filter_btn_notauth:
				visibleNotauth();
				filterConfig.setAuth("0");
				break;
			case R.id.filter_btn_authnot:
				visibleAuthnot();
				filterConfig.setAuth("");
				break;
			case R.id.filter_btn_yes:
				visibleYes();
				filterConfig.setBroker("1");
				break;
			case R.id.filter_btn_no:
				visibleNo();
				filterConfig.setBroker("0");
				break;
			case R.id.filter_btn_brokernot:
				visibleBrokernot();
				filterConfig.setBroker("");
				break;
			case R.id.filter_btn_have:
				visibleHave();
				filterConfig.setComposite("1");
				break;
			case R.id.filter_btn_havenot:
				visibleHavenot();
				filterConfig.setComposite("2");
				break;
			case R.id.filter_btn_nothave:
				visibleNothave();
				filterConfig.setComposite("");
				break;
			case R.id.filter_btn_distancerevoca:
				distanceOne.setText("0");
				distanceTwo.setText("0");
				break;
			case R.id.filter_btn_agerevoca:
				ageOne.setText("0");
				ageTwo.setText("0");
				break;
			case R.id.filter_btn_tallrevoca:
				tallOne.setText("0");
				tallTwo.setText("0");
				break;
			case R.id.filter_btn_weightrevoca:
				weightOne.setText("0");
				weightTwo.setText("0");
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
	 * 显示“社交”
	 * */
	private void visibleSocial() {
		btnSocial.setBackgroundResource(R.drawable.btn_fiter_left_d);
		btnAlone.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnServcenot.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnSocial.setTextColor(getResources().getColor(R.color.white));
		btnAlone.setTextColor(getResources().getColor(R.color.black));
		btnServcenot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“独处”
	 * */
	private void visibleAlone() {
		btnSocial.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnAlone.setBackgroundResource(R.drawable.btn_fiter_middle_d);
		btnServcenot.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnSocial.setTextColor(getResources().getColor(R.color.black));
		btnAlone.setTextColor(getResources().getColor(R.color.white));
		btnServcenot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“服务场合不限”
	 * */
	private void visibleServcenot() {
		btnSocial.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnAlone.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnServcenot.setBackgroundResource(R.drawable.btn_fiter_right_d);

		btnSocial.setTextColor(getResources().getColor(R.color.black));
		btnAlone.setTextColor(getResources().getColor(R.color.black));
		btnServcenot.setTextColor(getResources().getColor(R.color.white));
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
	 * 显示“已认证”
	 * */
	private void visibleAuthed() {
		btnAuthed.setBackgroundResource(R.drawable.btn_fiter_left_d);
		btnNotauth.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnAuthnot.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnAuthed.setTextColor(getResources().getColor(R.color.white));
		btnNotauth.setTextColor(getResources().getColor(R.color.black));
		btnAuthnot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“未认证”
	 * */
	private void visibleNotauth() {
		btnAuthed.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnNotauth.setBackgroundResource(R.drawable.btn_fiter_middle_d);
		btnAuthnot.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnAuthed.setTextColor(getResources().getColor(R.color.black));
		btnNotauth.setTextColor(getResources().getColor(R.color.white));
		btnAuthnot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“认证不限”
	 * */
	private void visibleAuthnot() {
		btnAuthed.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnNotauth.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnAuthnot.setBackgroundResource(R.drawable.btn_fiter_right_d);

		btnAuthed.setTextColor(getResources().getColor(R.color.black));
		btnNotauth.setTextColor(getResources().getColor(R.color.black));
		btnAuthnot.setTextColor(getResources().getColor(R.color.white));
	}

	/**
	 * 显示“是”
	 * */
	private void visibleYes() {
		btnYes.setBackgroundResource(R.drawable.btn_fiter_left_d);
		btnNo.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnBrokernot.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnYes.setTextColor(getResources().getColor(R.color.white));
		btnNo.setTextColor(getResources().getColor(R.color.black));
		btnBrokernot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“否”
	 * */
	private void visibleNo() {
		btnYes.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnNo.setBackgroundResource(R.drawable.btn_fiter_middle_d);
		btnBrokernot.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnYes.setTextColor(getResources().getColor(R.color.black));
		btnNo.setTextColor(getResources().getColor(R.color.white));
		btnBrokernot.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“经纪人不限”
	 * */
	private void visibleBrokernot() {
		btnYes.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnNo.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnBrokernot.setBackgroundResource(R.drawable.btn_fiter_right_d);

		btnYes.setTextColor(getResources().getColor(R.color.black));
		btnNo.setTextColor(getResources().getColor(R.color.black));
		btnBrokernot.setTextColor(getResources().getColor(R.color.white));
	}

	/**
	 * 显示“有”
	 * */
	private void visibleHave() {
		btnHave.setBackgroundResource(R.drawable.btn_fiter_left_d);
		btnHavenot.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnNothave.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnHave.setTextColor(getResources().getColor(R.color.white));
		btnHavenot.setTextColor(getResources().getColor(R.color.black));
		btnNothave.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“无”
	 * */
	private void visibleHavenot() {
		btnHave.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnHavenot.setBackgroundResource(R.drawable.btn_fiter_middle_d);
		btnNothave.setBackgroundResource(R.drawable.btn_fiter_right_n);

		btnHave.setTextColor(getResources().getColor(R.color.black));
		btnHavenot.setTextColor(getResources().getColor(R.color.white));
		btnNothave.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 显示“ 综合评分不限”
	 * */
	private void visibleNothave() {
		btnHave.setBackgroundResource(R.drawable.btn_fiter_left_n);
		btnHavenot.setBackgroundResource(R.drawable.btn_fiter_middle_n);
		btnNothave.setBackgroundResource(R.drawable.btn_fiter_right_d);

		btnHave.setTextColor(getResources().getColor(R.color.black));
		btnHavenot.setTextColor(getResources().getColor(R.color.black));
		btnNothave.setTextColor(getResources().getColor(R.color.white));
	}

}
