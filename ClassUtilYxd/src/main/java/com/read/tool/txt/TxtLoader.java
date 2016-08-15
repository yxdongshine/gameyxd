/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.read.tool.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class TxtLoader<T> extends ConcurrentHashMap<Integer, T> {
	private static final long serialVersionUID = 7856584857008107681L;
	private Class<T> _class;
	private TxtParam key;
	private List<TxtParam> params = new ArrayList();
	private HSSFSheet sheet;
	private int startCol = 0;

	private int startRow = 3;
	private FileInputStream fis;
	private CopyOnWriteArrayList<T> list = new CopyOnWriteArrayList();

	public TxtLoader(String xlsFilePath, Class<T> CLASS) throws Exception {
		this._class = CLASS;

		File file = new File(xlsFilePath);
		if (!(file.exists())) {
			throw new NullPointerException("�ļ�[" + xlsFilePath + "]�����ڣ�");
		}

		this.fis = new FileInputStream(file);

		if (!(this._class.getSuperclass().getSimpleName()
				.equalsIgnoreCase("GameObject"))) {
			Class superClass = this._class.getSuperclass();
			loadFields(superClass);
		}

		loadFields(this._class);
	}

	public TxtParam getKey() {
		return this.key;
	}

	public HSSFSheet getSheet() {
		return this.sheet;
	}

	public int getStartCol() {
		return this.startCol;
	}

	public int getStartRow() {
		return this.startRow;
	}

	protected int getValue(T value, String property) {
		try {
			String methodName = property;
			methodName = methodName.substring(0, 1).toUpperCase()
					+ methodName.substring(1, methodName.length());
			methodName = "get" + methodName;
			Method method = this._class.getMethod(methodName, new Class[0]);
			Integer key = (Integer) method.invoke(value, new Object[0]);
			return key.intValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public Map<Integer, T> load(boolean keyFromZero) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				this.fis, "UTF-8"));
		int i = 0;
		while (reader.ready()) {
			String line = reader.readLine();

			if (this.startRow <= i) {
				if (line == null) {
					break;
				}

				Object instance = loadUnit(line.trim());
				if (this.key.getField().getModifiers() == 1) {
					Integer keyValue = Integer.valueOf(this.key.getField()
							.getInt(instance));
					if ((keyFromZero) && (keyValue.intValue() >= 0)) {
						put(keyValue, (T) instance);
						this.list.add((T) instance);
					} else if ((!(keyFromZero)) && (keyValue.intValue() > 0)) {
						put(keyValue, (T) instance);
						this.list.add((T) instance);
					}
				} else {
					String field = this.key.getField().getName();
					String tmp = field.substring(0, 1).toUpperCase();
					field = tmp + field.substring(1, field.length());
					String methodName = "get" + field;
					Method method = instance.getClass().getMethod(methodName,
							new Class[0]);
					if (method == null) {
						throw new NullPointerException("����[" + methodName
								+ "]�����ڣ�");
					}
					Integer keyValue = (Integer) method.invoke(instance,
							new Object[0]);
					if ((keyFromZero) && (keyValue.intValue() >= 0)) {
						put(keyValue, (T) instance);
						this.list.add((T) instance);
					} else if ((!(keyFromZero)) && (keyValue.intValue() > 0)) {
						put(keyValue, (T) instance);
						this.list.add((T) instance);
					}
				}
			}
			++i;
		}

		return this;
	}

	private void loadFields(Class<?> _class) {
		Field[] arrayOfField;
		int j = (arrayOfField = _class.getDeclaredFields()).length;
		for (int i = 0; i < j; ++i) {
			Field field = arrayOfField[i];

			Annotation[] anos = field.getDeclaredAnnotations();
			Annotation[] arrayOfAnnotation1;
			int l = (arrayOfAnnotation1 = anos).length;
			for (int k = 0; k < l; ++k) {
				Annotation annotation = arrayOfAnnotation1[k];

				TxtParam param = new TxtParam();
				param.setField(field);

				if (annotation instanceof TxtKey) {
					if (this.key != null) {
						continue;
					}
					this.key = param;
				} else {
					if (annotation instanceof TxtBoolean) {
						param.setDataType(DataType.BOOLEAN);
					} else if (annotation instanceof TxtByte) {
						param.setDataType(DataType.BYTE);
					} else if (annotation instanceof TxtShort) {
						param.setDataType(DataType.SHORT);
					} else if (annotation instanceof TxtInt) {
						param.setDataType(DataType.INT);
					} else if (annotation instanceof TxtDouble) {
						param.setDataType(DataType.DOUBLE);
					} else if (annotation instanceof TxtFloat) {
						param.setDataType(DataType.FLOAT);
					} else {
						if (!(annotation instanceof TxtString))
							continue;
						param.setDataType(DataType.STRING);
					}

					this.params.add(param);
				}
			}
		}
	}

	private T loadUnit(String row)
    throws Exception
  {
    Object instance = this._class.newInstance();
    String[] cells = new String[this.params.size()];
    String[] tcells = row.split("\\t");
    for (int i = 0; i < cells.length; ++i) {
      if (i < tcells.length)
        cells[i] = tcells[i];
      else {
        cells[i] = "";
      }
    }

    for (int x = 0; x < this.params.size(); ++x) {
      TxtParam param = (TxtParam)this.params.get(x);
      String cell = cells[x];

      if (cell == null)
      {
        continue;
      }

      if (cell.toString().equalsIgnoreCase("end")) {
        return null;
      }

      if (param.getField().getModifiers() == 1) {
        try {
          if (param.getDataType() == DataType.BOOLEAN) {
            param.getField().setBoolean(instance, CellUtils.getBoolean(cell));
          }
          if (param.getDataType() == DataType.BYTE) {
            param.getField().setByte(instance, CellUtils.getByte(cell));
          }
          if (param.getDataType() == DataType.INT) {
            param.getField().setInt(instance, CellUtils.getInt(cell));
          }
          if (param.getDataType() == DataType.SHORT) {
            param.getField().setShort(instance, CellUtils.getShort(cell));
          }
          if (param.getDataType() == DataType.DOUBLE) {
            param.getField().setDouble(instance, CellUtils.getDouble(cell));
          }
          if (param.getDataType() == DataType.FLOAT) {
            param.getField().setDouble(instance, CellUtils.getFloat(cell));
          }
          if (param.getDataType() == DataType.STRING) {
            String value = CellUtils.getString(cell);
            if (value == null) {
              value = "";
            }
            param.getField().set(instance, value);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
          System.err.println("��----" + x + "�����ֶΣ�" + param.getField());
        }
      } else {
        Method method = null;
        try {
          String field = param.getField().getName();
          String tmp = field.substring(0, 1).toUpperCase();
          field = tmp + field.substring(1, field.length());
          String methodName = "set" + field;

          Object arg = null;
          if (param.getDataType() == DataType.STRING) {
            method = instance.getClass().getMethod(methodName, new Class[] { String.class });
            try {
              arg = CellUtils.getString(cell);
            } catch (Exception ex) {
              arg = Integer.valueOf(CellUtils.getInt(cell));
              System.err.println("��----" + x + "�����ֶΣ�" + param.getField());
            }
          }

          if (param.getDataType() == DataType.BYTE) {
            method = instance.getClass().getMethod(methodName, new Class[] { Byte.TYPE });
            arg = Byte.valueOf(CellUtils.getByte(cell));
          } else if (param.getDataType() == DataType.SHORT) {
            method = instance.getClass().getMethod(methodName, new Class[] { Short.TYPE });
            arg = Short.valueOf(CellUtils.getShort(cell));
          } else if (param.getDataType() == DataType.INT) {
            method = instance.getClass().getMethod(methodName, new Class[] { Integer.TYPE });
            arg = Integer.valueOf(CellUtils.getInt(cell));
          } else if (param.getDataType() == DataType.BOOLEAN) {
            method = instance.getClass().getMethod(methodName, new Class[] { Boolean.TYPE });
            arg = Boolean.valueOf(CellUtils.getBoolean(cell));
          } else if (param.getDataType() == DataType.DOUBLE) {
            method = instance.getClass().getMethod(methodName, new Class[] { Double.TYPE });
            arg = Double.valueOf(CellUtils.getDouble(cell));
          } else if (param.getDataType() == DataType.FLOAT) {
            method = instance.getClass().getMethod(methodName, new Class[] { Float.TYPE });
            arg = Float.valueOf(CellUtils.getFloat(cell));
          }

          if (method == null) {
            throw new NullPointerException("����[" + methodName + "]�����ڣ�");
          }

          if (arg.equals("-1")) {
            throw new NullPointerException("����[" + methodName + "]����Ϊ-1��");
          }

          method.invoke(instance, new Object[] { arg });
        } catch (Exception e) {
          e.printStackTrace();
          System.err.println("��----" + x + "�����ֶΣ�" + param.getField());
        }
      }
    }
    return (T) instance;
  }

	public void printParams() {
		for (TxtParam param : this.params)
			System.out.println(param);
	}

	public void setKey(TxtParam key) {
		this.key = key;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public CopyOnWriteArrayList<T> getList() {
		return this.list;
	}

	public void setList(CopyOnWriteArrayList<T> list) {
		this.list = list;
	}
}