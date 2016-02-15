package com.android.volley.download;

import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

import com.android.volley.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyTools;
import com.android.volley.toolbox.FileDownloader;
import com.zgntech.core.util.DebugLog;
import com.zgntech.core.util.SystemTool;
import com.zgntech.core.util.T;
import com.zgntech.core.util.UiTools;

public abstract class DownloadApk implements I_Apk {

	private Context mContext;
	public VolleyTools tools;
	private ProgressDialog pd;
	private FileDownloader mDownloder;

	public DownloadApk(Context context) {
		this.mContext = context;
		tools = VolleyTools.getInstance(context);
		pd = UiTools.getDialog(context);
		pd.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {// 返回
					tools.cancelPendingRequests();
					dismiss();
				}
				return false;
			}
		});
		mDownloder = new FileDownloader(tools.requestQueue, 1);
	}

	public void show(int resId) {
		show(mContext.getResources().getString(resId));
	}

	public void show(String msg) {
		pd.setMessage(msg);
		pd.show();
	}

	public void dismiss() {
		if (pd.isShowing()) {
			pd.dismiss();
		}
	}

	@Override
	public void showDownloadDialog(String title, String msg, final String url,
			final String saveFile) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setCancelable(false);
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				T.toast("确认");
				downApk(url, saveFile);
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				T.toast("取消");
			}
		});
		builder.create().show();
	}

	FileDownloader.DownloadController controller;
	ProgressDialog xh_pDialog;

	@Override
	public void downApk(String url, String path) {
		xh_pDialog = new ProgressDialog(mContext);
		xh_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		xh_pDialog.setTitle("提示");
		xh_pDialog.setMessage("这是一个长形进度条对话框");
		xh_pDialog.setIndeterminate(false);
		xh_pDialog.setProgress(100);
		xh_pDialog.setCancelable(true);
		final String filePath = path + "jiawo.apk";
		controller = mDownloder.add(filePath, url, new Listener<Void>() {
			@Override
			public void onPreExecute() {
				super.onPreExecute();
				xh_pDialog.show();
			}

			@Override
			public void onSuccess(Void response) {
			}

			@Override
			public void onFinish() {
				super.onFinish();
				xh_pDialog.dismiss();
				T.toast("下载成功");
				install(new File(filePath));
			}

			@Override
			public void onProgressChange(long fileSize, long downloadedSize) {
				super.onProgressChange(fileSize, downloadedSize);
				xh_pDialog
						.setProgress((int) (downloadedSize * 100 / fileSize) );
			}

			@Override
			public void onError(VolleyError error) {
				super.onError(error);
				xh_pDialog.dismiss();
				T.toast("下载失败");
			}
		});
	}

	@Override
	public void install(File file) {
		SystemTool.installApk(mContext, file);
	}

}

interface I_Apk {
	/** 检测是否需要更新 */
	void checkUpdate(String url);

	void downApk(String url, String path);

	void install(File file);

	void showDownloadDialog(String title, String msg, String url,
			String saveFile);
}