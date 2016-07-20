/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.read.tool.excel;

import java.lang.reflect.Field;

public class ExcelParam {
	private static final long serialVersionUID = -2629458415317463493L;
	private int column;
	private DataType dataType;
	private Field field;

	public int getColumn() {
		return this.column;
	}

	public DataType getDataType() {
		return this.dataType;
	}

	public Field getField() {
		return this.field;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String toString() {
		String msg = "[Parameter] DataType=" + this.dataType;
		msg = msg + ", Field=" + this.field.getName();
		return msg;
	}
}