package com.zgntech.core.config;

import com.android.volley.VolleyTools;
import com.lidroid.xutils.db.DbUtils;

public class ConfigHelper extends Config implements I_Config {

	/** 此处可以创建自己的配置信息 /默认使用DefaultSettings **/
	@Override
	public ConfigSeting createSetting() {
		return null;
	}

	@Override
	public DbUtils getDb() {
		return (DbUtils) getSetting().getDb();
	}

	@Override
	public VolleyTools getNet() {
		return (VolleyTools) getSetting().getNet();
	}
}
