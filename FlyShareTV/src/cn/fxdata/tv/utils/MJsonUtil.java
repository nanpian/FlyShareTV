package cn.fxdata.tv.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @FileNmae MJsonUtil.java
 * @TODO
 */
public class MJsonUtil {

    public static GsonBuilder builder;

    public static Gson gson;

    static {
        builder = new GsonBuilder();
        // 不转换没有 @Expose 注解的字段
        builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();
    }

    /**
     * 对象转换成json字符串
     * 
     * @param obj
     * @return
     */
    public static String ObjectToJsonString(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            return "";
        }
    }

    public static Object JsonStringToObject(String str, Class<?> cls) {

        return gson.fromJson(str, cls);
    }

}