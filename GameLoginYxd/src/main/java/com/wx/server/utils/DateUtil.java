package com.wx.server.utils;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.WEEK_OF_YEAR;
import static java.util.Calendar.YEAR;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/** 日期工具类 */
public class DateUtil {
	
	private static LogUtils log = LogUtils.getLog(DateUtil.class);
	
	/**
	 * 检查当前时间和指定时间是否同一周
	 * 
	 * @param year 年
	 * @param week 周
	 * @param firstDayOfWeek 周的第一天设置值，{@link Calendar#DAY_OF_WEEK}
	 * @return {@link Boolean} true-同一周, false-不同周
	 */
	public static boolean isSameWeek(int year, int week, int firstDayOfWeek) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(firstDayOfWeek);
		return year == cal.get(YEAR) && week == cal.get(WEEK_OF_YEAR);
	}
	
	/**
	 * 检查当前时间和指定时间是否同一周
	 * 
	 * @param time 被检查的时间
	 * @param firstDayOfWeek 周的第一天设置值，{@link Calendar#DAY_OF_WEEK}
	 * @return {@link Boolean} true-同一周, false-不同周
	 */
	public static boolean isSameWeek(Date time, int firstDayOfWeek) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.setFirstDayOfWeek(firstDayOfWeek);
		return isSameWeek(cal.get(YEAR), cal.get(WEEK_OF_YEAR), firstDayOfWeek);
	}
	
	/**
	 * 获取周的第一天
	 * 
	 * @param firstDayOfWeek 周的第一天设置值，{@link Calendar#DAY_OF_WEEK}
	 * @param time 指定时间，为 null 代表当前时间
	 * @return {@link Date} 周的第一天
	 */
	public static Date firstTimeOfWeek(int firstDayOfWeek, Date time) {
		Calendar cal = Calendar.getInstance();
		if (time != null) {
			cal.setTime(time);
		}
		cal.setFirstDayOfWeek(firstDayOfWeek);
		int day = cal.get(DAY_OF_WEEK);
		
		if (day == firstDayOfWeek) {
			day = 0;
		} else if (day < firstDayOfWeek) {
			day = day + (7 - firstDayOfWeek);
		} else if (day > firstDayOfWeek) {
			day = day - firstDayOfWeek;
		}
		
		cal.set(HOUR_OF_DAY, 0);
		cal.set(MINUTE, 0);
		cal.set(SECOND, 0);
		cal.set(MILLISECOND, 0);
		
		cal.add(DATE, -day);
		return cal.getTime();
	}
	
	/**
	 * 检查指定日期是否今天(使用系统时间)
	 * 
	 * @param date 被检查的日期
	 * @return
	 */
	public static boolean isToday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.add(DATE, 1);
		cal.set(HOUR_OF_DAY, 0);
		cal.set(MINUTE, 0);
		cal.set(SECOND, 0);
		cal.set(MILLISECOND, 0);
		Date end = cal.getTime(); // 明天的开始
		cal.add(MILLISECOND, -1);
		cal.add(DATE, -1);
		Date start = cal.getTime(); // 昨天的结束
		return date.after(start) && date.before(end);
	}
	
	/**
	 * 日期转换成字符串格式
	 * 
	 * @param theDate 待转换的日期
	 * @param datePattern 日期格式
	 * @return 日期字符串
	 */
	public static String date2String(Date theDate, String datePattern) {
		if (theDate == null) {
			return "";
		}
		
		DateFormat format = new SimpleDateFormat(datePattern);
		try {
			return format.format(theDate);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 字符串转换成日期格式
	 * 
	 * @param dateString 待转换的日期字符串
	 * @param datePattern 日期格式
	 * @return {@link Date} 转换后的日期
	 */
	public static Date string2Date(String dateString, String datePattern) {
		if (dateString == null || dateString.trim().isEmpty()) {
			return null;
		}
		
		DateFormat format = new SimpleDateFormat(datePattern);
		try {
			return format.parse(dateString);
		} catch (ParseException e) {
			log.error("ParseException in Converting String to date: " + e.getMessage());
		}
		
		return null;
		
	}
	
	/**
	 * 把秒数转换成把毫秒数
	 * 
	 * @param seconds 秒数的数组
	 * @return {@link Long} 毫秒数
	 */
	public static long toMillisSecond(long... seconds) {
		long millis = 0L;
		if (seconds != null && seconds.length > 0) {
			for (long time : seconds) {
				millis += (time * 1000);
			}
		}
		return millis;
	}
	
	/**
	 * 把毫秒数转换成把秒数
	 * 
	 * @param seconds 毫秒数的数组
	 * @return {@link Long} 毫秒数
	 */
	public static long toSecond(long... millis) {
		long second = 0L;
		if (millis != null && millis.length > 0) {
			for (long time : millis) {
				second += (time / TimeConstant.ONE_SECOND_MILLISECOND);
			}
		}
		return second;
	}
	
	/**
	 * 修改日期
	 * 
	 * @param theDate 待修改的日期
	 * @param addDays 加减的天数
	 * @param hour 设置的小时
	 * @param minute 设置的分
	 * @param second 设置的秒
	 * @return 修改后的日期
	 */
	public static Date changeDateTime(Date theDate, int addDays, int hour, int minute, int second) {
		if (theDate == null) {
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		
		cal.add(DAY_OF_MONTH, addDays);
		
		if (hour >= 0 && hour <= 24) {
			cal.set(HOUR_OF_DAY, hour);
		}
		if (minute >= 0 && minute <= 60) {
			cal.set(MINUTE, minute);
		}
		if (second >= 0 && second <= 60) {
			cal.set(SECOND, second);
		}
		
		return cal.getTime();
	}
	
	public static Date add(Date theDate, int addHours, int addMinutes, int addSecond) {
		if (theDate == null) {
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		
		cal.add(HOUR_OF_DAY, addHours);
		cal.add(MINUTE, addMinutes);
		cal.add(SECOND, addSecond);
		
		return cal.getTime();
	}
	
	/**
	 * 取得星期几
	 * 
	 * @param theDate
	 * @return
	 */
	public static int dayOfWeek(Date theDate) {
		if (theDate == null) {
			return -1;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		
		return cal.get(DAY_OF_WEEK);
	}
	
	/**
	 * 获得某一时间的0点
	 * 
	 * @param theDate 需要计算的时间
	 */
	public static Date getDate0AM(Date theDate) {
		if (theDate == null) {
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		return new GregorianCalendar(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH)).getTime();
	}
	
	/**
	 * 获得某一时间的下一个0点
	 * 
	 * @param theDate 需要计算的时间
	 */
	public static Date getNextDay0AM(Date theDate) {
		if (theDate == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(theDate.getTime() + TimeConstant.ONE_DAY_MILLISECOND);
		return new GregorianCalendar(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH)).getTime();
	}
	
	/**
	 * 获得指定日期的23点59分59秒的时间
	 * 
	 * @param theDate 需要计算的时间
	 */
	public static Date getThisDay2359PM(Date theDate) {
		if (theDate == null) {
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		long millis = theDate.getTime() + TimeConstant.ONE_DAY_MILLISECOND - TimeConstant.ONE_SECOND_MILLISECOND;
		cal.setTimeInMillis(millis);
		Date date = new GregorianCalendar(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH)).getTime();
		return new Date(date.getTime() - TimeConstant.ONE_SECOND_MILLISECOND);
	}
	
	/**
	 * 获得指定时间的下个周一的00:00:00的时间
	 * 
	 * @param date 指定的时间
	 * @return {@link Date} 周一的00:00:00的时间
	 */
	public static Date getNextMonday(Date date) {
		if (date == null) {
			return null;
		}
		
		// 本周周一
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.getDate0AM(date));
		cal.set(DAY_OF_WEEK, MONDAY);
		
		Calendar nextMondayCal = Calendar.getInstance();
		nextMondayCal.setTimeInMillis(cal.getTimeInMillis() + TimeConstant.ONE_DAY_MILLISECOND * 7);
		return nextMondayCal.getTime();
	}
	
	/**
	 * 获得获得改变后的时间
	 * 
	 * @param addDay 增加的天数(减少天数, 则传负数)
	 * @param to0AM 是否取0点时间
	 * @return
	 */
	public static Date add(int addDay, boolean to0AM) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(DATE, addDay);
		Date time = calendar.getTime();
		return to0AM ? getDate0AM(time) : time;
	}
	
	/**
	 * 获得当前时间的秒
	 * 
	 * @return {@link Long} 当前时间的秒
	 */
	public static long getCurrentSecond() {
		return toSecond(System.currentTimeMillis());
	}
}
