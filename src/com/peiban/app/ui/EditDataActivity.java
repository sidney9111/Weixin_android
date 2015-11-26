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
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.FinalFactory;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.UserInfoApi;
import com.peiban.app.control.AlbumControl;
import com.peiban.app.control.DatePickerLisenerImpl;
import com.peiban.app.control.DialogSalaryLisenerImpl;
import com.peiban.app.control.DialogServiceLoaction;
import com.peiban.app.ui.common.AlbumDb;
import com.peiban.application.PeibanApplication;
import com.peiban.command.FileTool;
import com.peiban.command.NetworkUtils;
import com.peiban.command.TextdescTool;
import com.peiban.vo.AlbumVo;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.UserInfoVo;

public class EditDataActivity extends BaseActivity {
	@ViewInject(id = R.id.editdata_info_salary)
	private TextView textSalary;
	@ViewInject(id = R.id.editdata_info_birthday)
	private TextView textBirthday;
	@ViewInject(id = R.id.editdata_info_service)
	private TextView textService;
	@ViewInject(id = R.id.editdata_info_name)
	private TextView textName;
	@ViewInject(id = R.id.editdata_info_signature)
	private TextView textSignature;
	@ViewInject(id = R.id.editdata_info_qq)
	private TextView textQQ;
	@ViewInject(id = R.id.editdata_info_tall)
	private TextView textTall;
	@ViewInject(id = R.id.editdata_info_weight)
	private TextView textWeight;
	@ViewInject(id = R.id.editdata_info_mobilephone)
	private TextView textMobilephone;
	@ViewInject(id = R.id.editdata_info_savor)
	private TextView textSavor;
	@ViewInject(id = R.id.editdata_info_job)
	private TextView textJob;

	@ViewInject(id = R.id.editdata_layout_salary)
	private RelativeLayout btnSalary;
	@ViewInject(id = R.id.editdata_layout_birthday)
	private RelativeLayout btnBirthday;
	@ViewInject(id = R.id.editdata_layout_service)
	private RelativeLayout btnService;
	@ViewInject(id = R.id.editdata_layout_albums)
	private LinearLayout btnAlbum;
	private CustomerVo customerVo;
	private CustomerVo customerVoClone;
	private UserInfoVo userInfoVo;
	
	private PeibanApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.editdata_layout);
		super.onCreate(savedInstanceState);
		OnClickListener();
		baseInit();
		initCustomerinfo();
	}

	@Override
	protected void baseInit() {
		super.baseInit();
		application = (PeibanApplication) getApplication();
		userInfoVo = application.getUserInfoVo();
		customerVo = application.getCustomerVo();
		customerVoClone = customerVo.clone();
	}

	private void initCustomerinfo() {
		textName.setText(customerVo.getName());
		textBirthday.setText(customerVo.getBirthday());
		String service = Constants.CustomerType.getServiceType(customerVo
				.getOccasions());
		textService.setText(service);
		textService.setTag(customerVo.getOccasions());
		textSignature.setText(customerVo.getSign());
		textQQ.setText(customerVo.getQq());
		textTall.setText(customerVo.getHeight());
		textWeight.setText(customerVo.getWeight());
		textJob.setText(customerVo.getProfession());
		textMobilephone.setText(customerVo.getPhone());
		textSalary.setText(customerVo.getSalary());
		textSavor.setText(customerVo.getInterest());
		getLocalAlbum();
	}

	/**
	 * 获取本地数据库中的相册信息
	 * */
	private void getLocalAlbum() {
		// TODO 获取本地缓存相册
		if (!FileTool.isMounted()) {
			showToast(getResources().getString(R.string.toast_sdcard_mounted));
		} else if (!FileTool.isSDCardAvailable()) {
			showToast(getResources().getString(R.string.toast_sdcard_available));
		} else {
			AlbumTask getlocalalbumTask = new AlbumTask();
			getlocalalbumTask.execute("getlocalalbum");

		}
	}

	/**
	 * 从服务器获取相册信息
	 * */
	private void getNetworkAlbum() {
		// TODO 从服务器获取相册信息
		AlbumApi albumApi = new AlbumApi();
		String uid = userInfoVo.getUid();
		albumApi.getAlbum(uid, uid, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				// TODO 服务器获取成功
				super.onSuccess(t);
				try {
					String data = ErrorCode.getData(t);
					if (!"".equals(data)) {
						AlbumTask saveAlbumTask = new AlbumTask();
						saveAlbumTask.execute("saveAlbum", data);
					} else {
						AlbumTask delallAlbumTask = new AlbumTask();
						delallAlbumTask.execute("delallAlbum");
					}
				} catch (Exception e) {
					showToast("错误(0,1)");
				}
				
			}
			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO 服务器获取失败
				super.onFailure(t, strMsg);
			}
		});
	}

	/**
	 * 显示相册信息
	 * 
	 * */
	private void showAlbum() {	
		AlbumDb albumDb=new AlbumDb(EditDataActivity.this, getFinalDb());
		List<AlbumVo> albumList=albumDb.findAlbumList();
		AlbumControl albumControl=new AlbumControl(EditDataActivity.this,btnAlbum, albumList);
		albumControl.showAlbum(userInfoVo.getUid(),customerVo.getUid());
	}
	private void OnClickListener() {
		View.OnClickListener l = new OnClicklistener();
		btnSalary.setOnClickListener(l);
		btnBirthday.setOnClickListener(l);
		btnService.setOnClickListener(l);
//		btnAlbum.setOnClickListener(l);
	}

	@Override
	protected void initTitle() {
		setTitleContent(R.string.editdata_title);
		setBtnBack();
		setTitleRight(R.string.btn_save);
	}

	@Override
	protected void titleBtnBack() {
		setResult(Activity.RESULT_OK);
		super.titleBtnBack();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(KeyEvent.KEYCODE_BACK == keyCode){
			setResult(Activity.RESULT_OK);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		super.hideSoftKeyboard();
		updateCustomer();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					FinalFactory.createFinalDb(getBaseContext(), userInfoVo)
							.update(application.getCustomerVo());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 提交编辑资料
	 * */
	private void updateCustomer() {
		if (!NetworkUtils.isnetWorkAvilable(getBaseContext())) {
			showToast(getResources().getString(R.string.toast_network));
			return;
		}

		if (!getCustomerinfo()) {
			return;
		}

		Map<String, String> customermap = TextdescTool
				.objectToMap(customerVoClone);
		UserInfoApi uia = new UserInfoApi();
		uia.editInfo(customerVoClone.getUid(), customermap,
				new AjaxCallBack<String>() {
					@Override
					public void onStart() {
						super.onStart();
						getWaitDialog().setMessage("正在提交......");
						getWaitDialog().show();
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						try {
							String data = ErrorCode.getData(getBaseContext(), t);
							if (data != null || "0".equals(data)
									|| "1".equals(data)) {
								application.setCustomerVo(customerVoClone);
								customerVo = application.getCustomerVo();
								customerVoClone = customerVo.clone();
								getWaitDialog().dismiss();

								showToast("提交成功！");
								titleBtnBack();
							}
						} catch (Exception e) {
							showToast("错误(0,1)");
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						getWaitDialog().setMessage("提交失败！");
						getWaitDialog().dismiss();
					}

				});

	}

	private boolean getCustomerinfo() {
		String name = textName.getText().toString();
		if (!TextUtils.isEmpty(name)) {
			customerVoClone.setName(name);
		} else {
			showToast("昵称不能为空！");
			return false;
		}
		String birthday = textBirthday.getText().toString();
		if (!TextUtils.isEmpty(birthday)) {
			customerVoClone.setBirthday(birthday);
		} else {
			showToast("生日不能为空！");
			return false;
		}
		String occasions = (String) textService.getTag();
		if (!TextUtils.isEmpty(occasions)) {
			customerVoClone.setOccasions(occasions);
		} else {
			showToast("服务场合不能为空！");
			return false;
		}
		String sign = textSignature.getText().toString();
		if (!TextUtils.isEmpty(sign)) {
			customerVoClone.setSign(sign);
		} else {
			customerVoClone.setSign("");
		}
		String height = textTall.getText().toString();
		if (!TextUtils.isEmpty(height)) {
			customerVoClone.setHeight(height);
		} else {
			customerVoClone.setHeight("");
		}
		String weight = textWeight.getText().toString();
		if (!TextUtils.isEmpty(weight)) {
			customerVoClone.setWeight(weight);
		} else {
			customerVoClone.setWeight("");
		}
		String profession = textJob.getText().toString();
		if (!TextUtils.isEmpty(profession)) {
			customerVoClone.setProfession(profession);
		} else {
			customerVoClone.setProfession("");
		}
		String phone = textMobilephone.getText().toString();
		if (!TextUtils.isEmpty(phone)) {
			if(phone.length() == 11){
				customerVoClone.setPhone(phone);
			}else{
				showToast("手机号应该是11位!");
				return false;
			}
		} else {
			customerVoClone.setPhone("");
		}
		String salary = textSalary.getText().toString();
		if (!TextUtils.isEmpty(salary)) {
			customerVoClone.setSalary(salary);
		} else {
			customerVoClone.setSalary("");
		}
		String interest = textSavor.getText().toString();
		if (!TextUtils.isEmpty(interest)) {
			customerVoClone.setInterest(interest);
		} else {
			customerVoClone.setInterest("");
		}
		String qq = textQQ.getText().toString();
//		if(TextUtils.isEmpty(qq)){
//			customerVoClone.setQq("");
//		}else if(qq.length() > 4){
//			customerVoClone.setQq(qq);
//		}else{
//			showToast("qq号应该最小位数是5位");
//			return false;
//		}
		customerVoClone.setQq(qq);

		return true;
	}

	class OnClicklistener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.editdata_layout_salary:
				new DialogSalaryLisenerImpl(textSalary, EditDataActivity.this)
						.show();
				break;
			case R.id.editdata_layout_birthday:
				new DatePickerLisenerImpl(EditDataActivity.this, textBirthday)
						.show();
				break;
			case R.id.editdata_layout_service:
				new DialogServiceLoaction(EditDataActivity.this, textService)
						.show();
				break;
			case R.id.editdata_layout_albums:
				Intent intent = new Intent(EditDataActivity.this,
						AlbumActivity.class);
				startActivity(intent);
				break;
			}
		}

	}

	/**
	 * 线程处理类
	 * */
	class AlbumTask extends AsyncTask<String, Integer, String> {
		private AlbumDb albumDb = new AlbumDb(EditDataActivity.this, getFinalDb());

		@Override
		protected void onPreExecute() {
			// TODO 线程准备
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO 后台线程操作
			if ("getlocalalbum".equals(params[0])) {
				List<AlbumVo> albumList = albumDb.findAlbumList();
				if (albumList == null || albumList.isEmpty()) {
					return "本地没有缓存";
				} else {
					return "显示相册";
				}
			}else if ("saveAlbum".equals(params[0])) {
				try {
					albumDb.albumListSave(params[1]);
					return "保存服务器数据成功";
				} catch (Exception e) {

				}
			} else if ("delallAlbum".equals(params[0])) {
				albumDb.albumDeleteAll();
				return "删除本地所有缓存成功";
			}
			return "未知错误";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO 线程结束
			super.onPostExecute(result);
			if ("本地没有缓存".equals(result)) {
				getNetworkAlbum(); // 如果本地没有相册，从网络获取
			} else if ("显示相册".equals(result) || "保存服务器数据成功".equals(result)
					|| "删除本地所有缓存成功".equals(result)) {
				showAlbum();
			}
			
		}
	}
}
