package com.peiban.app.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.widget.Toast;

import com.peiban.app.ui.RegisterActivity;
import com.peiban.app.ui.common.RegisterUtil;
import com.peiban.app.vo.RegisterVo;

public class RegisterControl{
	private RegisterActivity registerActivity;

	public RegisterControl(RegisterActivity registerActivity) {
		super();
		this.registerActivity = registerActivity;
	}

	public void submit() {
		RegisterVo registerVo = null;
		String username = registerActivity.getEditUsername().getText()
				.toString();
		String password = registerActivity.getEditPassword().getText()
				.toString();
		String repassword = registerActivity.getEditRepassword().getText()
				.toString();
		String nominate = registerActivity.getEditNominate().getText()
				.toString();

		if (TextUtils.isEmpty(username)) {
			Toast.makeText(registerActivity, "请填写手机号", Toast.LENGTH_SHORT)
					.show();
			return;
		} else if (TextUtils.isEmpty(password)) {
			Toast.makeText(registerActivity, "请填写密码", Toast.LENGTH_SHORT)
					.show();
			return;
		} else if (TextUtils.isEmpty(repassword)) {
			Toast.makeText(registerActivity, "请确认密码", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		//		else if(TextUtils.isEmpty(nominate)){
//			Toast.makeText(registerActivity, "填写推荐人号码!", Toast.LENGTH_SHORT)
//			.show();
//		}
		else if (!validatePhone(username)) {
			Toast.makeText(registerActivity, "请输入正确的手机号", Toast.LENGTH_SHORT)
					.show();
			return;
		} else if (!validatePassword(password)) {
			Toast.makeText(registerActivity, "密码长度范围为 6 ~ 16 位",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (!validate(password, repassword)) {
			Toast.makeText(registerActivity, "两次密码输入不正确", Toast.LENGTH_SHORT)
					.show();
			return;
		} 
//		else if (!validatePhone(nominate)) {
//			Toast.makeText(registerActivity, "推荐人手机号有误", Toast.LENGTH_SHORT)
//					.show();
//			return;
//		}
		else{
			registerVo = new RegisterVo(username, password, repassword, nominate);
			registerActivity.nextActivity(registerVo);
		}
	}
	public static boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	private boolean validatePassword(String str) {
		return RegisterUtil.validatePassword(str);
	}

	private boolean validatePhone(String str) {
		return RegisterUtil.validatePhone(str);
	}

	private boolean validate(String str1, String str2) {
		return RegisterUtil.validate(str1, str2);
	}

}
