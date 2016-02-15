package cn.fxdata.tv.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
    private static DateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static DateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");

    public synchronized static Date parseDate(String dateStr)
            throws ParseException {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            if (Log.E) {
                Log.e(FormatUtil.class.getName(), "parseDate() dateStr -->> "
                        + dateStr);
            }
            throw e;
        }
    }

    public synchronized static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    /**
     * 只返回 年月日 的工具类
     * 
     * @param date
     * @return
     */
    public synchronized static String formatDateWithYMH(Date date) {
        return ymdFormat.format(date);
    }
}
