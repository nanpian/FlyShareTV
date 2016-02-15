package com.zgntech.core.config;

public abstract class ConfigSeting implements I_Config{
	/** Log工具类 */
	public abstract void SetingLog(boolean open);

	/** 全局toast无重复工具类 */
	public abstract void SettingToast();

	/** 配置文件工具类 */
	public abstract void SettingSharepreference();

	/** 自定义文件工具类交给子类去初始化 */
	public abstract Object SettingNetUitl();

	/***** UI注入(注解影响效率需考虑) *********/
	public abstract void SettingViewAnnotations(boolean open);

	/** 初始化数据库 */
	public abstract Object SettingDb(boolean debug);

}
