package com.zgntech.core.base;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Listener;
import com.android.volley.Request.Method;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyTools;
import com.zgntech.base.R;
import com.zgntech.core.ActivityStack;
import com.zgntech.core.config.Constant;
import com.zgntech.core.util.StringParamsRequest;
import com.zgntech.core.util.SystemTool;
import com.zgntech.core.util.T;
import com.zgntech.core.util.UiTools;

/**
 * @description：TODO<请描述这个类是干什么的>
 * @author：lyu
 * @date:2015-5-11
 * @version：V1.0
 */
public class BaseActivity extends FragmentActivity {

	/** 网络请求时的对话框 */
	protected ProgressDialog dialog;
	private Context context;
	public boolean isBack = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT); // 设置竖屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		super.onCreate(savedInstanceState);
		ActivityStack.create().addActivity(this);
		getContext();
		createProgressDialog();
	}

	private void createProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			destroyProgressDialog();
		}
		dialog = UiTools.getDialog(this);
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {// 返回
					VolleyTools.getInstance(getContext())
							.cancelPendingRequests();
				}
				return false;
			}
		});
	}

	/**
	 * 关闭Dialog对象
	 */
	public void destroyProgressDialog() {
		if (dialog != null)
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		dialog = null;
	}

	protected void showToast(String msg) {
		T.toast(msg);
	}

	protected void showToast(int id) {
		T.toast(id);
	}

	protected void showDialog(String msg) {
		dialog.setMessage(msg);
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	protected void dismissDialog() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public void showActivity(Class<?> cls) {
		startActivity(new Intent(this, cls));
	}

	/**
	 * @param cls
	 * @param requestCode
	 */
	public void showActivityForResult(Class<?> cls, int requestCode) {
		startActivityForResult(new Intent(this, cls), requestCode);
	}

	/**
	 * @param cls
	 * @param requestCode
	 * @param bundle
	 */
	public void showActivityForResult(Class<?> cls, int requestCode,
			Bundle bundle) {
		Intent intent = new Intent(this, cls);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * @param cls
	 * @param bundle
	 */
	public void showActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(this, cls);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	protected void showHttpError(String errorMsg) {
		showToast(errorMsg);
	}

	public Context getContext() {
		if (context == null)
			context = getApplicationContext();
		return context;
	}

	/**
	 * 获取 用户Id
	 * 
	 * @return
	 */
	public String getUserId() {
		String userId = "";
		return userId;
	}

	/** volley相关 */
	public void sendVolleyRequest(Map<String, String> params,
			Listener<String> requestListener) {
		if (checkNet()) {
			sendRequest(params, requestListener);
		}
	}

	/** volley相关 需要汉字编码 */
	public void sendVolleyRequestWithEncode(Map<String, String> params,
			Listener<String> requestListener) {
		if (checkNet()) {
			sendRequestWithEncode(params, requestListener);
		}
	}

	/**
	 * 发送请求
	 * 
	 * @param params
	 * @param requestListener
	 * @param errorListener
	 */
	public void sendRequest(Map<String, String> params,
			Listener<String> requestListener) {

		Map<String, String> reqParams = getReqParam(params);
		StringParamsRequest request = new StringParamsRequest(Method.POST,
				Constant.ServerConfig.SERVER_URL, requestListener, reqParams);
		request.setRetryPolicy(getRetryPolicy());
		VolleyTools.getInstance(this).addToRequestQueue(request);
	}

	/**
	 * 发送需要编码的请求，带有用户自己输入文字的参数
	 * 
	 * @param params
	 * @param requestListener
	 * @param errorListener
	 */
	public void sendRequestWithEncode(Map<String, String> params,
			Listener<String> requestListener) {

		Map<String, String> reqParams = getReqParam(null);
		StringParamsRequest request = new StringParamsRequest(Method.POST,
				Constant.ServerConfig.SERVER_URL, requestListener, reqParams,
				params);
		request.setRetryPolicy(getRetryPolicy());
		VolleyTools.getInstance(this).addToRequestQueue(request);
	}

	/**
	 * volley请求 默认设置
	 */
	private RetryPolicy getRetryPolicy() {
		return new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	}

	/**
	 * 获取公共参数
	 * 
	 * @param pageParam
	 * @return
	 */
	private Map<String, String> getReqParam(Map<String, String> pageParam) {

		Map<String, String> param = new HashMap<String, String>();
		String timestamp = String.valueOf(new Date().getTime());
		param.put("platform", "Android");
		param.put("version", SystemTool.getAppVersionCode(context) + "");
		param.put("source", "APP");
		param.put("timestamp", timestamp);
		param.put("token", "");
		param.put("udid", SystemTool.getPhoneIMEI(context));
		// String privateCode = new
		// SharePreferencePersonUtil(getContext()).getPersonUserPrivateCode();
		// if (false == TextUtils.privateCode)) {
		param.put("private_code", URLEncoder.encode(""));
		// }
		if (pageParam != null) {
			for (String iterable_element : pageParam.keySet()) {
				param.put(iterable_element, pageParam.get(iterable_element));
			}
		}
		return param;
	}

	/**
	 * 检测网络 /无网络则取消dialog
	 * 
	 * @return
	 */
	public boolean checkNet() {
		if (SystemTool.checkNet(this)) {
			return true;
		}
		if (dialog.isShowing()) {
			destroyProgressDialog();
		}
		T.toast(R.string.network_unavailable);
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityStack.create().finishActivity(this);
	}
}
