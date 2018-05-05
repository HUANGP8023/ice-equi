package com.ice.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月19日
 * @Desc 日期工具包
 */
public class DateUtils {
	
	public static final String df_Str = "yyyy-MM-dd HH:mm:ss";

	public static final String YYYY_MM_DD_Str = "yyyy-MM-dd";

	public static final String df_apm_Str = "yyyy-MM-dd ahh:mm:ss";

	public static Date toDate(String dateStr) {
		try {
			return new SimpleDateFormat(df_Str).parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date toYMDDate(String dateStr) {
		try {
			return new SimpleDateFormat(YYYY_MM_DD_Str).parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String toStr(Date date) {
		try {
			return new SimpleDateFormat(df_Str).format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return date == null ? "" : date.toString();
		}
	}

	public static String toStr(Object date) {
		try {
			return new SimpleDateFormat(df_Str).format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return date == null ? "" : date.toString();
		}
	}

	/**
	 * 增加时间 （小时）
	 * 
	 * @param startDate
	 * @param hour
	 * @return
	 */
	public static Date addHour(Date startDate, int hour) {
		String format = new SimpleDateFormat(df_Str).format(startDate);
		Date date = null;
		try {
			date = new SimpleDateFormat(df_Str).parse(format);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.HOUR_OF_DAY, hour);
		return ca.getTime();
	}


	/**
	 * Get now Date.
	 * 
	 * @return
	 */
	public static Date now() {
		return new Date();
	}

	public static long getTime() {
		return now().getTime();
	}
	/**
	 * 转化成上午、下午
	 * 
	 * @param date
	 * @return
	 */
	public static String toAPM(Date date) {
		return new SimpleDateFormat(df_apm_Str).format(date);
	}

	public static String toAPM(String date) {
		try {
			return new SimpleDateFormat(df_apm_Str).format(new SimpleDateFormat(df_Str).parse(date));
		} catch (ParseException e) {
			return date;
		}
	}

	public static String format(Date date, String format) {
		if (date == null || "".equals(format) || format == null) {
			return null;
		}
		return new SimpleDateFormat(format).format(date);
	}

	public static Date parse(String date, String format) {
		if (date == null || "".equals(format) || format == null) {
			return null;
		}
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			// e.printStackTrace();
		}
		return null;
	}

	public static long dateToTimestamp(String dateStr) {
		Date d = toDate(dateStr);
		if (null != d) {
			return d.getTime();
		}
		return 0;
	}
    public static long getTimestamp(){
    	return new Date().getTime();
    }
	public static String unixTimestampToDateStr(long timestamp) {
		return timestampToDateStr(timestamp, true);
	}

	public static String timestampToDateStr(long timestamp, boolean isUnix) {
		if (timestamp == 0)
			return null;
		if (isUnix)
			return new SimpleDateFormat(df_Str).format(timestamp * 1000);
		return new SimpleDateFormat(df_Str).format(timestamp);
	}
	public static long getUnixNowTimestamp(){
		return (getNowTimestamp()/1000);
	}
	public static long getNowTimestamp(){
		return System.currentTimeMillis();
	}

	public static String getNowToStrYYYY_MM_DD() {
		return new SimpleDateFormat(YYYY_MM_DD_Str).format(new Date());
	}

	// day = -1 表示上一天
	public static long lastDays(int day) {
		Calendar c = setCalendar();
		c.add(Calendar.DATE, day);
		Date d = c.getTime();
		return d.getTime();
	}

	// month = -1 表示过去一个月
	public static long lastMonth(int month) {
		Calendar c = setCalendar();
		c.add(Calendar.MONTH, month);
		Date m = c.getTime();
		return m.getTime();
	}

	public static long lastYear(int year) {
		// 过去一年
		Calendar c = setCalendar();
		c.add(Calendar.YEAR, year);
		Date y = c.getTime();
		return y.getTime();
	}

	private static Calendar setCalendar() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c;
	}

	//一天开始  0点0时0分
	public static long getStartTime() {  
        Calendar c = Calendar.getInstance();  
        c.set(Calendar.HOUR_OF_DAY, 0);  
        c.set(Calendar.MINUTE, 0);  
        c.set(Calendar.SECOND, 0);  
        c.set(Calendar.MILLISECOND, 0);  
        return c.getTime().getTime();  
    } 
	//一天结束  23点59时59分
	public static long getnowEndTime() {  
        Calendar c = Calendar.getInstance();  
        c.set(Calendar.HOUR_OF_DAY, 23);  
        c.set(Calendar.MINUTE, 59);  
        c.set(Calendar.SECOND, 59);  
        c.set(Calendar.MILLISECOND, 999);  
        return c.getTime().getTime();  
    }  
	
	public static  String getstringtime(Date aDate, String separator) {
		try {
			SimpleDateFormat df = null;
			String returnValue = "";
			if (aDate != null) {
				df = new SimpleDateFormat(separator);
				returnValue = df.format(aDate);
			}
			return returnValue;
		} catch (RuntimeException e) {
			return null;
		}
	}
	
	/**
	 * 当前时间是否是今天的?到?之间,例:"19:20:00","20:43:00"(不能夸天)
	 * @param stime
	 * @param etime
	 * @return
	 */
	public  static boolean isRunTime(String stime,String etime) {
		String nowString = getstringtime(new Date(System.currentTimeMillis()), "HH:mm:ss");
		String startTime = stime;
		String overTime = etime;

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
		Date dtStart = null;
		Date dtOver = null;
		Date now = null;
		try {
			dtStart = sdf.parse(startTime);
			dtOver = sdf.parse(overTime);
			now = sdf.parse(nowString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean isNullIssueTime = (now.getTime() >= dtStart.getTime())
				&& (now.getTime() <= dtOver.getTime());
		return isNullIssueTime;
	}
	
	  
	
	
	
}
