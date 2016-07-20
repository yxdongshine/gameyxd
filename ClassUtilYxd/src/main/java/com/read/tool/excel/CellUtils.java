/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.read.tool.excel;

import java.io.PrintStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class CellUtils {
	static String file = "";

	public static void debug(HSSFCell cell) {
		int start = cell.getRow().getFirstCellNum();
		int end = cell.getRow().getPhysicalNumberOfCells();
		String msg = "(";
		for (int i = start; i <= end; ++i) {
			HSSFCell tmp = cell.getRow().getCell(i);
			if (tmp == null)
				continue;
			msg = msg + tmp.toString();
			if (i < end)
				msg = msg + ",";
		}
		msg = msg + ")";

		System.err.println("[" + cell.getClass().getSimpleName() + "]³ö´í---->"
				+ cell.getSheet().getSheetName() + "->" + msg);
	}

	public static boolean getBoolean(HSSFCell cell) {
		if (cell != null) {
			try {
				int value = (int) cell.getNumericCellValue();
				switch (value) {
				case 0:
					return false;
				case 1:
					return true;
				}
				return cell.getBooleanCellValue();
			} catch (Exception ex) {
				try {
					return cell.getBooleanCellValue();
				} catch (Exception e) {
					debug(cell);
					return false;
				}
			}
		}
		return false;
	}

	public static byte getByte(HSSFCell cell) {
		if (cell != null) {
			try {
				return (byte) getInt(cell);
			} catch (Exception ex) {
				try {
					return Byte.parseByte(cell.getRichStringCellValue()
							.getString().trim());
				} catch (Exception e) {
					debug(cell);
					return 0;
				}
			}
		}

		return -1;
	}

	public static double getDouble(HSSFCell cell) {
		if (cell != null) {
			try {
				return cell.getNumericCellValue();
			} catch (Exception ex) {
				try {
					return Double.valueOf(
							cell.getRichStringCellValue().getString().trim())
							.doubleValue();
				} catch (Exception e) {
					debug(cell);
					return 0.0D;
				}
			}
		}
		return -1.0D;
	}

	public static int getInt(HSSFCell cell) {
		if (cell != null) {
			try {
				return (int) cell.getNumericCellValue();
			} catch (Exception e) {
				try {
					Integer.valueOf(cell.getRichStringCellValue().getString()
							.trim());
				} catch (Exception ex) {
					debug(cell);
					return 0;
				}
			}
		}

		return -1;
	}

	public static short getShort(HSSFCell cell) {
		if (cell != null) {
			try {
				return (short) (int) cell.getNumericCellValue();
			} catch (Exception ex) {
				try {
					return Short.valueOf(
							cell.getRichStringCellValue().getString().trim())
							.shortValue();
				} catch (Exception e) {
					debug(cell);
				}
			}
		}
		return -1;
	}

	public static String getString(HSSFCell cell) {
		try {
			return cell.getRichStringCellValue().getString().trim();
		} catch (Exception localException) {
		}
		return String.valueOf(cell.getNumericCellValue()).trim();
	}
}