/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.read.tool.txt;

import java.io.PrintStream;

public class CellUtils {
	static String file = "";

	public static void debug(String fileName, String cell, int row, int col) {
		System.err.println("[" + fileName + "]³ö´í---->" + "->" + cell + "::ÐÐ"
				+ row + "::ÁÐ:" + col);
	}

	public static boolean getBoolean(String cell) {
		if (cell != null) {
			try {
				if (cell.equals("")) {
					return false;
				}
				int value = Byte.parseByte(cell);
				switch (value) {
				case 0:
					return false;
				case 1:
					return true;
				}
				return (value > 0);
			} catch (Exception ex) {
				try {
					return (Byte.parseByte(cell) > 0);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	public static byte getByte(String cell) {
		if (cell != null) {
			try {
				if (cell.equals("")) {
					return 0;
				}
				return Byte.parseByte(cell);
			} catch (Exception ex) {
				ex.printStackTrace();
				return 0;
			}
		}

		return -1;
	}

	public static double getDouble(String cell) {
		if (cell != null) {
			try {
				if (cell.equals("")) {
					return 0.0D;
				}
				return Double.parseDouble(cell);
			} catch (Exception ex) {
				ex.printStackTrace();
				return 0.0D;
			}
		}
		return -1.0D;
	}

	public static float getFloat(String cell) {
		if (cell != null) {
			try {
				if (cell.equals("")) {
					return 0.0F;
				}
				return Float.parseFloat(cell);
			} catch (Exception ex) {
				ex.printStackTrace();
				return 0.0F;
			}
		}
		return -1.0F;
	}

	public static int getInt(String cell) {
		if (cell != null) {
			try {
				if (cell.equals("")) {
					return 0;
				}
				return Integer.parseInt(cell);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}

		return -1;
	}

	public static short getShort(String cell) {
		if (cell != null) {
			try {
				if (cell.equals("")) {
					return 0;
				}
				return Short.parseShort(cell);
			} catch (Exception ex) {
				ex.printStackTrace();
				return 0;
			}
		}
		return -1;
	}

	public static String getString(String cell) {
		return cell;
	}
}