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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Listener;
import com.android.volley.Request.Method;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyTools;
import com.zgntech.base.R;
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
public class BaseFragment extends Fragment {

	/** 网络请求时的对话框 */
	protected ProgressDialog dialog;
	private Context context = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		createProgressDialog();
		// CrashHandler.instance(mContext);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// L.state(this.getClass(), "onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public void showActivity(Class<?> cls) {
		context.startActivity(new Intent(context, cls));
	}

	public void showActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(getActivity(), cls);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(intent);
	}

	protected void showToast(String msg) {
		T.toast(msg);
	}

	protected void showToast(int id) {
		T.toast(id);
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

	public Context getContext() {

		if (context == null) {
			context = getActivity().getApplicationContext();
		}
		return context;
	}

	private void createProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			destroyProgressDialog();
		}
		dialog = UiTools.getDialog(context);
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {// 返回
					VolleyTools.getInstance(context).cancelPendingRequests();
				}
				return false;
			}
		});
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

	/**
	 * 关闭Dialog对象
	 */
	public void destroyProgressDialog() {
		if (dialog != null)
			dialog.dismiss();
		dialog = null;
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
		VolleyTools.getInstance(context).addToRequestQueue(request);
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
		VolleyTools.getInstance(context).addToRequestQueue(request);
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
		param.put("udid", "");
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

	/** 检测网络 /无网络则取消dialog */
	public boolean checkNet() {
		if (SystemTool.checkNet(context)) {
			return true;
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		T.toast(R.string.network_unavailable);
		return false;
	}
}
