package com.wx.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串与日期间转换,字符转日期有可能返回null
 * 
 * @author huangyang
 */
public class DateFormat {
	
	/**
	 * 长时间保存用格式
	 * 
	 * @author huangyang
	 * @createDate 2009-4-7 下午10:46:16
	 */
	private static final SimpleDateFormat sdff = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	/**
	 * 长时间显示用格式
	 * 
	 * @author huangyang
	 * @createDate 2009-4-7 下午10:46:15
	 */
	@SuppressWarnings("unused")
	private static final SimpleDateFormat sdfsf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	
	/**
	 * 一般时间保存用格式
	 * 
	 * @author huangyang
	 * @createDate 2009-4-7 下午10:46:15
	 */
	@SuppressWarnings("unused")
	private static final SimpleDateFormat sdfl = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 一般时间显示用格式
	 * 
	 * @author huangyang
	 * @createDate 2009-4-7 下午10:46:14
	 */
	private static final SimpleDateFormat sdfsl = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 短时间保存用格式
	 * 
	 * @author huangyang
	 * @createDate 2009-4-7 下午10:46:12
	 */
	private static final SimpleDateFormat sdfs = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 短时间显示用格式
	 * 
	 * @author huangyang
	 * @createDate 2009-4-7 下午10:46:10
	 */
	private static final SimpleDateFormat sdfss = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 将日期转换为字符串:完整(yyyyMMddHHmmssSSS)
	 * 
	 * @author huangyang
	 * @createDate 2008-11-25 下午04:06:02
	 * @param date
	 * @return
	 */
	public static String getFullDate(Date date) {
		if (date == null) {
			return null;
		} else {
			return sdff.format(date);
		}
	}
	
	/**
	 * 将完整日期(yyyyMMddHHmmssSSS)字符转换为日期,如果有异常,返回null
	 * 
	 * @author huangyang
	 * @createDate 2008-11-25 下午04:40:22
	 * @param date
	 * @return
	 */
	public static Date getFullDate(String date) {
		if (date == null || date.length() == 0) {
			return null;
		} else {
			try {
				return sdff.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * 将日期转换为字符串:短型(yyyyMMdd)
	 * 
	 * @author huangyang
	 * @createDate 2008-11-25 下午04:06:02
	 * @param date
	 * @return
	 */
	public static String getShortDate(Date date) {
		if (date == null) {
			return null;
		} else {
			return sdfs.format(date);
		}
	}
	
	/**
	 * 将短型日期(yyyyMMdd)字符转换为日期,如果有异常,返回null
	 * 
	 * @author huangyang
	 * @createDate 2008-11-25 下午04:40:22
	 * @param date
	 * @return
	 */
	public static Date getShortDate(String date) {
		try {
			return sdfs.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将日期转换为字符串:短型(yyyy-MM-dd)
	 * 
	 * @author huangyang
	 * @createDate 2008-11-25 下午04:06:02
	 * @param date
	 * @return
	 */
	public static String getShortDateShow(Date date) {
		if (date == null) {
			return null;
		} else {
			return sdfss.format(date);
		}
	}
	
	/**
	 * 将短型日期(yyyy-MM-dd)字符转换为日期,如果有异常,返回null
	 * 
	 * @author huangyang
	 * @createDate 2008-11-25 下午04:40:22
	 * @param date
	 * @return
	 */
	public static Date getShortDateShow(String date) {
		try {
			return sdfss.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将日期转换为字符串:长型(yyyy-MM-dd hh:mm:ss)
	 * 
	 * @author huangyang
	 * @createDate 2008-11-25 下午04:06:41
	 * @param date
	 * @return
	 */
	public static String getLongDateShow(Date date) {
		if (date == null) {
			return null;
		} else {
			return sdfsl.format(date);
		}
	}
	
	/**
	 * 将长型日期(yyyy-MM-dd hh:mm:ss)字符转换为日期,如果有异常,返回null
	 * 
	 * @author huangyang
	 * @createDate 2008-11-25 下午04:41:03
	 * @param date
	 * @return
	 */
	public static Date getLongDateShow(String date) {
		try {
			return sdfsl.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 通过日期得到月份
	 * 
	 * @author huangyang
	 * @createDate 2009-9-4 下午02:26:40
	 * @param date
	 * @return
	 */
	public static int getMonthByDate(Date date) {
		String str = sdff.format(date);
		return Integer.valueOf(str.substring(4, 6));
	}
	
	/**
	 * 通过日期得到日
	 * 
	 * @author huangyang
	 * @createDate 2009-9-4 下午02:26:40
	 * @param date
	 * @return
	 */
	public static int getDayByDate(Date date) {
		String str = sdff.format(date);
		return Integer.valueOf(str.substring(6, 8));
	}
	
	/**
	 * @author huangyang
	 * @createDate 2009-11-16 上午09:41:55
	 * @param date
	 * @return
	 */
	public static long dateFormat(String date) {
		try {
			if (ToolUtils.isStringNull(date)) {
				return 0;
			}
			return sdfsl.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
