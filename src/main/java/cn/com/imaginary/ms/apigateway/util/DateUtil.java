package cn.com.imaginary.ms.apigateway.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2018/8/10 20:58
 */
public class DateUtil {

    public static FastDateFormat ymdhms = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    public static FastDateFormat ymd = FastDateFormat.getInstance("yyyy-MM-dd");
    public static FastDateFormat ymdhdsNoPadding = FastDateFormat.getInstance("yyyyMMddHHmmss");
    public static FastDateFormat ymdNoPadding = FastDateFormat.getInstance("yyyyMMdd");
    public static FastDateFormat ymdhm = FastDateFormat.getInstance("yyyyMMdd-HHmm");

    public static String format() {
        return ymd.format(System.currentTimeMillis());
    }

    public static String formatYmdhms() {
        return ymdhms.format(System.currentTimeMillis());
    }

    public static String format(Date date) {
        return ymd.format(date);
    }

    public static Date parse(String dateStr) {
        Date date = null;
        try {
            date = ymd.parse(dateStr);
        } catch (ParseException e) {
        }
        return date;
    }

    public static Date parseYmdhms(String dateStr) {
        Date date = null;
        try {
            date = ymdhms.parse(dateStr);
        } catch (ParseException e) {
        }
        return date;
    }


    public static Date parse(String dateStr, FastDateFormat fastDateFormat) {
        Date date = null;
        try {
            date = fastDateFormat.parse(dateStr);
        } catch (ParseException e) {
        }
        return date;
    }

    public static String format(Date date, FastDateFormat fastDateFormat) {
        return fastDateFormat.format(date);
    }


    public static Date getDateByPeriod(int type, int n, Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.add(type, n);
        return calendar.getTime();
    }

    public static Date getDateByMinite(int n) {
        return getDateByPeriod(Calendar.MINUTE, n, null);
    }

    public static Date getDateByMinite(Date inputDate, int n) {
        return getDateByPeriod(Calendar.MINUTE, n, inputDate);
    }

    public static Date getDateByHours(int n) {
        return getDateByPeriod(Calendar.HOUR_OF_DAY, n, null);
    }

    public static Date getDateByHours(Date inputDate, int n) {
        return getDateByPeriod(Calendar.HOUR_OF_DAY, n, inputDate);
    }

    public static Date getDateByDay(int n) {
        return getDateByPeriod(Calendar.DAY_OF_MONTH, n, null);
    }

    public static Date getDateByDay(Date inputDate, int n) {
        return getDateByPeriod(Calendar.DAY_OF_MONTH, n, inputDate);
    }

    public static Date getDateByMonth(int n) {
        return getDateByPeriod(Calendar.MONTH, n, null);
    }

    public static Date getDateByMonth(Date inputDate, int n) {
        return getDateByPeriod(Calendar.MONTH, n, inputDate);
    }

    public static Date getDateByYear(int n) {
        return getDateByPeriod(Calendar.YEAR, n, null);
    }

    public static Date getDateByYear(Date inputDate, int n) {
        return getDateByPeriod(Calendar.YEAR, n, inputDate);
    }


    public static String getTimeDiff(long start, long end) {
        long between = 0;
        try {
            between = start - end;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000;
        return day + "天" + hour + "小时" + min + "分" + s + "秒" + ms + "毫秒";
    }


    public static Date getStartTime(int n) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.add(Calendar.DAY_OF_MONTH, n);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getStartTime(Date date) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getEndTime(int n) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.add(Calendar.DAY_OF_MONTH, n);

        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    public static Date getEndTime(Date date) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(date);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    public static Date getTomorowDate(int hours) {
        String todayStr = format() + " 00:00:00";
        Date today = parse(todayStr, ymdhms);

        Calendar afterTime = Calendar.getInstance();
        afterTime.setTime(today);
        afterTime.add(Calendar.HOUR, hours);
        afterTime.add(Calendar.DAY_OF_MONTH, 1);
        return afterTime.getTime();
    }

    public static Date today() {
        try {
            String ymdStr = ymd.format(System.currentTimeMillis());
            String todayStr = ymdStr + " 00:00:00";
            return ymdhms.parse(todayStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getFistDayOfMonth(Date date) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.DAY_OF_MONTH, 1);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getFistDayOfYear(Date date) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.MONTH, 0);
        todayStart.set(Calendar.DAY_OF_MONTH, 1);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static long getTwoDateInterval(String preYmd, String afterYmd) {
        try {
            Date preDate = ymd.parse(preYmd);
            Date afterDate = ymd.parse(afterYmd);
            return (afterDate.getTime() - preDate.getTime()) / (1000 * 3600 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
