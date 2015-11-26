package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.peiban.R;

/**
 * 功能：显示支付网页<br />
 * 日期：2013-5-9<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author lcy
 * @since
 */
public class PanlvWebActivity extends BaseActivity {
	@ViewInject(id=R.id.panlv_webView)
	private WebView panLvWeb;
	private String webUrl,titleName,
	/** 是否需要重新定向 */
	redirection;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panlv_webview);
		Bundle bundle = getIntent().getExtras();
		// 如果数据为空，退出页面
		if(bundle == null){
			finish();
			return;
		}
		
		webUrl = bundle.getString("url");
		titleName = bundle.getString("titleName");
		redirection = bundle.getString("redirection");
		
		baseInit();
		showWeb();
	}
	private void showWeb() {
		panLvWeb.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

		panLvWeb.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				panLvWeb.loadUrl(webUrl);
			}
		});
		
		panLvWeb.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
			
		});
	}

	@Override
	protected void titleBtnBack() {
		checkRedirection();
		super.titleBtnBack();
	}
	
	/** 检查是否需要重定向 */
	private void checkRedirection(){
		if(!TextUtils.isEmpty(redirection))
		{
			try {
				Intent intent = new Intent();
				intent.setClassName(PanlvWebActivity.this, redirection);
				startActivity(intent);
			} catch (Exception e) {
			}
			
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果按下返回键, 判断是否需要应用重定向
		if(KeyEvent.KEYCODE_BACK == keyCode){
			checkRedirection();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void initTitle() {
		setTitleContent(titleName);
		setBtnBack();
	}
}