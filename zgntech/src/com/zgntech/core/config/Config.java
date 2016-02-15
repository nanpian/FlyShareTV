package com.zgntech.core.config;

import android.content.Context;
import android.util.Log;

/**
 * 
 * @Description: TODO(项目幻境配置)
 * 
 * @author gufei 562401002@qq.com
 * 
 * @date 2015年4月3日 上午9:41:15
 */
public abstract class Config {

	public abstract ConfigSeting createSetting();

	private ConfigSeting setings;

	public synchronized void init(Context applicationContext) {
		Log.d(getClass().getName(), "Config install");
		if ((setings = createSetting()) == null) {
			setings = new DefaultSettings(applicationContext);
		}
		setings.SetingLog(true);
		setings.SettingToast();
		setings.SettingSharepreference();
		setings.SettingNetUitl();
		setings.SettingViewAnnotations(true);
		setings.SettingDb(true);
		Log.d(getClass().getName(), "Config install ok");
	}

	public ConfigSeting getSetting() {
		return setings;
	}

}
