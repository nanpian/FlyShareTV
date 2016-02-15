
package com.zgntech.core.config;

/**
 * @author broil
 */
public class Constant {

    public static class ServerConfig {
        // public static final String SERVER_URL =
        // "http://rd.ruyue.mobi/api/server.php?";// 服务器
        public static final String SERVER_URL = "http://ruyue.72zhe.com/api/server.php?";
        // public static final String APP_ID = "20140508135723930CI92203";

        public final static String VERSION = "1.0.0";
    }


    public static class ResponseCode {
        /** 返回成功 */
        public static final String RESPONSE_SUCCESS = "0";
        /** 返回错误 */
        public static final String RESPONSE_ERROR = "20000";
        /** 查询错误 已经存在 */
        public static final String RESPONSE_EXIST = "20001";
        /** 错误：系统维护 error_code为10001 */
        public static final String RESPONSE_SYSTEM_NOTUSEFULL = "10001";
        /** 错误：不支持的客户端版本 error_code为10002 */
        public static final String RESPONSE_SYSTEM_NOTEXIST = "10002";
        /** 错误：不存在该接口 error_code为10003 */
        public static final String RESPONSE_SYSTEM_NOTSUPPORT = "10003";
    }

}
