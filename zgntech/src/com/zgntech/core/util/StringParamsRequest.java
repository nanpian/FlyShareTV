package com.zgntech.core.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Listener;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.request.StringRequest;
import com.zgntech.core.util.DebugLog;

/**
 * @title : StringParamsRequest.java
 * @package : com.android.volley.toolbox
 * @description :TODO
 * @author: lyu
 * @date:2015-4-17
 */
public class StringParamsRequest extends StringRequest {

	private Priority priority = Priority.NORMAL;
	private Map<String, String> params = null;

	public StringParamsRequest(String url, Listener<String> listener) {
		super(url, listener);
		// TODO Auto-generated constructor stub
	}

	public StringParamsRequest(int method, String url, Listener<String> listener) {
		super(method, url, listener);
		// TODO Auto-generated constructor stub
	}

	public StringParamsRequest(int method, String url,
			Listener<String> listener, Map<String, String> param) {
		super(method, url + createLinkString(param), listener);
		Log.d("StringParamsRequest", "request:" + url + createLinkString(param));
		// TODO Auto-generated constructor stub
	}

	public StringParamsRequest(int method, String url,
			Listener<String> listener, Map<String, String> param,
			Map<String, String> paramsCN) {
		super(method, url + createLinkString(param), listener);
		Log.d("StringParamsRequest", "request:" + url + createLinkString(param));
		this.params = paramsCN;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key) + "";

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

	@Override
	public Map<String, String> getParams() throws AuthFailureError {
		return params;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data, response.charset);
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		DebugLog.i("response:" + parsed);
		return super.parseNetworkResponse(response);
	}

	@Override
	public Priority getPriority() {
		return priority;
		// TODO Auto-generated method stub
	}

	/**
	 * @param priority
	 *            LOW,NORMAL,HIGH,IMMEDIATE
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
}
