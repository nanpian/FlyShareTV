package com.zgntech.core.listener;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.android.volley.Listener;
import com.android.volley.VolleyError;
import com.zgntech.core.config.Constant;
import com.zgntech.core.entity.BaseReturn;

/**
 * @description：TODO<请描述这个类是干什么的>
 * @author：lyu
 * @date:2015-5-21
 * @version：V1.0
 */
public class VolleyListener extends Listener<String> {
	private static final int JSON_RESPONSE = 100;
	private static final int JSON_ERROR = 200;
	// private static final Feature[] features = {
	// Feature.
	// };

	private Class<?> cls;
	private VolleyJsonCallBack volleyJsonCallBack;

	public VolleyListener(Class<?> cls, VolleyJsonCallBack volleyJsonCallBack) {
		this.cls = cls;
		this.volleyJsonCallBack = volleyJsonCallBack;
	}

	private Object jsonStringToObject(String str, Class<?> cls) {
		return JSON.parseObject(str, cls);
	}

	@Override
	public void onSuccess(String response) {
		if (TextUtils.isEmpty(response + "")) {
			onJsonCallBack(JSON_RESPONSE, "");
			return;
		}
		// TODO Auto-generated method stub
		BaseReturn object = null;
		try {
			object = (BaseReturn) jsonStringToObject(response + "", cls);
			if (object != null) {
				if (object.getError_code().equals(
						Constant.ResponseCode.RESPONSE_SUCCESS)) {
					onJsonCallBack(JSON_RESPONSE, object);
				} else if (object.getError_code().equals(
						Constant.ResponseCode.RESPONSE_ERROR)) {
					onJsonCallBack(JSON_ERROR, object.getError_msg());
				} else if (object.getError_code().equals(
						Constant.ResponseCode.RESPONSE_SYSTEM_NOTUSEFULL)) {
					onJsonCallBack(JSON_ERROR, "系统维护");
				} else if (object.getError_code().equals(
						Constant.ResponseCode.RESPONSE_SYSTEM_NOTSUPPORT)) {
					onJsonCallBack(JSON_ERROR, "不存在该接口");
				} else {
					onJsonCallBack(JSON_ERROR, "返回码无法识别");
				}
			} else {
				onJsonCallBack(JSON_ERROR, "返回为空");
			}
		} catch (JSONException e) {
			// TODO: handle exception
			onJsonCallBack(JSON_ERROR, "数据解析出现错误");
		}
	}

	@Override
	public void onError(VolleyError error) {
		// TODO Auto-generated method stub
		super.onError(error);
	}

	private void onJsonCallBack(int httpCode, Object object) {
		if (volleyJsonCallBack != null) {
			if (httpCode == JSON_RESPONSE) {
				volleyJsonCallBack.onResonpse(object);
			} else if (httpCode == JSON_ERROR) {
				volleyJsonCallBack.onError(httpCode, (String) object);
			}
		}
	}

	/**
	 * @description：TODO<请描述这个类是干什么的>
	 * @author：lyu
	 * @date:2015-5-21
	 * @version：V1.0
	 */
	public static interface VolleyJsonCallBack {
		void onResonpse(Object object);

		void onError(int errorCode, String errorMsg);
	}
}
