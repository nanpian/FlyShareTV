package com.zgntech.core.config;

import android.content.Context;

import com.android.volley.VolleyTools;
import com.lidroid.xutils.db.DbUtils;
import com.lidroid.xutils.db.DbUtils.DaoConfig;
import com.zgntech.core.annotation.ViewUtils;
import com.zgntech.core.util.DebugLog;
import com.zgntech.core.util.Sharepf;
import com.zgntech.core.util.T;

public class DefaultSettings extends ConfigSeting {
	private Context context;
	private String AppName;
	private static DbUtils dbUtils;
	private static VolleyTools volleyTools;

	public DefaultSettings(Context applicationContext) {
		this.context = applicationContext;
		AppName = context.getPackageName();
	}

	@Override
	public void SetingLog(boolean open) {
		DebugLog.i("Log install..." + open);
	}

	@Override
	public void SettingToast() {
		DebugLog.i("toast install...");
		T.create(context);
	}

	@Override
	public void SettingSharepreference() {
		DebugLog.i("Sharepf install...");
		Sharepf.getInstance(context);
	}

	@Override
	public void SettingViewAnnotations(boolean open) {
		DebugLog.i("ViewUtils install..." + open);
		ViewUtils.DEBUG = open;
	}

	/** 默认使用volley的网络请求框架 ***/
	@Override
	public VolleyTools SettingNetUitl() {
		if (volleyTools == null) {
			volleyTools = VolleyTools.getInstance(context);
			DebugLog.i("Volley install...");
		}
		return volleyTools;
	}

	@Override
	public DbUtils SettingDb(boolean debug) {
		DaoConfig daoConfig = new DaoConfig(context);
		String dbName = AppName + ".db";
		daoConfig.setDbName(dbName);
		dbUtils = DbUtils.create(daoConfig).configDebug(debug);
		DebugLog.i("DbUtils install...dbName:" + dbName);
		return dbUtils;
	}

	@Override
	public Object getDb() {
		if (dbUtils == null) {
			DebugLog.d("not setting db……");
			return null;
		}
		return dbUtils;
	}

	@Override
	public Object getNet() {
		return VolleyTools.getInstance(context);
	}

}
