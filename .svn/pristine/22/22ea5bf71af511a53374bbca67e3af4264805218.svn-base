package com.android.volley.download;

import java.io.File;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Listener;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.android.volley.request.StringRequest;
import com.zgntech.core.util.DebugLog;

public class DownloadManager extends DownloadApk {
	private static final String mSaveDirPath = "/sdcard/add/";

	public DownloadManager(Context context) {
		super(context);
	}

	@Override
	public void checkUpdate(String url) {
		show("正在检查更新");
		tools.addToRequestQueue(new StringRequest(Method.GET, url,
				new Listener<String>() {

					@Override
					public void onSuccess(String response) {
						dismiss();
						DebugLog.v(response);
						File downloadDir = new File(mSaveDirPath);
						if (!downloadDir.exists())
							downloadDir.mkdir();
						JSONObject jsonObject = JSON.parseObject(response);
						String url = jsonObject.getString("path");
						showDownloadDialog("下载", "有更新内容了", url, mSaveDirPath);
					}

					@Override
					public void onError(VolleyError error) {
						super.onError(error);
						dismiss();
					}

					@Override
					public void onCancel() {
						super.onCancel();
						dismiss();
						DebugLog.d("onCancel");

					}
				}), tools.VolleyTAG);

	}

}
