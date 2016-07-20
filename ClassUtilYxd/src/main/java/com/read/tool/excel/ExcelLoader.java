/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.read.tool.excel;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelLoader<T> extends ConcurrentHashMap<Integer, T>
{
  private static final long serialVersionUID = 7856584857008107681L;
  private Class<T> _class;
  private ExcelParam key;
  private List<ExcelParam> params = new ArrayList();
  private HSSFSheet sheet;
  private int startCol = 0;

  private int startRow = 1;
  private HSSFWorkbook workbook;
  private CopyOnWriteArrayList<T> list = new CopyOnWriteArrayList();

  public ExcelLoader(String xlsFilePath, Class<T> CLASS)
    throws Exception
  {
    this._class = CLASS;

    POIFSFileSystem fs = new POIFSFileSystem(
      new FileInputStream(xlsFilePath));
    this.workbook = new HSSFWorkbook(fs);

    Sheet _sheet = (Sheet)this._class.getAnnotation(Sheet.class);

    this.sheet = this.workbook.getSheet(_sheet.value());

    if (!(this._class.getSuperclass().getSimpleName()
      .equalsIgnoreCase("GameObject"))) {
      Class superClass = this._class.getSuperclass();
      loadFields(superClass);
    }

    loadFields(this._class);
  }

  public ExcelParam getKey()
  {
    return this.key;
  }

  public HSSFSheet getSheet()
  {
    return this.sheet;
  }

  public int getStartCol()
  {
    return this.startCol;
  }

  public int getStartRow()
  {
    return this.startRow;
  }

  protected int getValue(T value, String property)
  {
    try
    {
      String methodName = property;
      methodName = methodName.substring(0, 1).toUpperCase() + 
        methodName.substring(1, methodName.length());
      methodName = "get" + methodName;
      Method method = this._class.getMethod(methodName, new Class[0]);
      Integer key = (Integer)method.invoke(value, new Object[0]);
      return key.intValue();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return 0;
  }

  public Map<Integer, T> load()
    throws Exception
  {
    return load(this.sheet, false);
  }

  public Map<Integer, T> load(HSSFSheet sheet, boolean keyFromZero)
    throws Exception
  {
    for (int i = this.startRow; i < 32767; ++i)
    {
      HSSFRow row = sheet.getRow(i);

      if (row == null) {
        break;
      }

      Object instance = loadUnit(row);
      if (this.key.getField().getModifiers() == 1) {
        Integer keyValue = Integer.valueOf(this.key.getField().getInt(
          instance));
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
        Method method = instance.getClass()
          .getMethod(methodName, new Class[0]);
        if (method == null) {
          throw new NullPointerException("方法[" + methodName + "]不存在！");
        }
        Integer keyValue = (Integer)method.invoke(instance, 
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

    return this;
  }

  public Map<Integer, T> load(String[] sheets, boolean keyFromZero)
    throws Exception
  {
    if (sheets != null) {
      for (String name : sheets) {
        this.sheet = this.workbook.getSheet(name);
        if (this.sheet != null) {
          load(this.sheet, keyFromZero);
        }
      }
    }
    return this;
  }

  public Map<Integer, T> load(String[] sheets)
    throws Exception
  {
    if (sheets != null) {
      for (String name : sheets) {
        this.sheet = this.workbook.getSheet(name);
        if (this.sheet != null) {
          load(this.sheet, false);
        }
      }
    }
    return this;
  }

  public List<T> loadAsList() throws Exception {
    return loadAsList(this.sheet);
  }

  public List<T> loadAsList(HSSFSheet sheet)
    throws Exception
  {
    List result = new ArrayList();

    for (int i = this.startRow; i < 32767; ++i) {
      HSSFRow row = sheet.getRow(i);
      if (row == null) {
        break;
      }
      Object instance = loadUnit(row);
      if (instance != null) {
        result.add(instance);
      }
    }

    return result;
  }

  public List<T> loadAsList(String[] sheets)
  {
    List result = new ArrayList();

    if (sheets != null) {
      for (String name : sheets) {
        this.sheet = this.workbook.getSheet(name);
        if (this.sheet == null) continue;
        try {
          List tmp = loadAsList(this.sheet);
          for (Object curr : tmp)
            result.add(curr);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }

    }

    return result;
  }

  private void loadFields(Class<?> _class)
  {
    Field[] arrayOfField;
    int j = (arrayOfField = _class.getDeclaredFields()).length;
    for (int i = 0; i < j; ++i)
    {
      Field field = arrayOfField[i];

      Annotation[] anos = field.getDeclaredAnnotations();
      Annotation[] arrayOfAnnotation1;
      int l = (arrayOfAnnotation1 = anos).length;
      for (int k = 0; k < l; ++k) {
        Annotation annotation = arrayOfAnnotation1[k];

        ExcelParam param = new ExcelParam();
        param.setField(field);

        if (annotation instanceof XlsKey) {
          if (this.key != null) {
            continue;
          }
          this.key = param;
        } else {
          if (annotation instanceof XlsBoolean) {
            param.setDataType(DataType.BOOLEAN);
          } else if (annotation instanceof XlsByte) {
            param.setDataType(DataType.BYTE);
          } else if (annotation instanceof XlsShort) {
            param.setDataType(DataType.SHORT);
          } else if (annotation instanceof XlsInt) {
            param.setDataType(DataType.INT);
          } else if (annotation instanceof XlsDouble) {
            param.setDataType(DataType.DOUBLE); } else {
            if (!(annotation instanceof XlsString)) continue;
            param.setDataType(DataType.STRING);
          }

          this.params.add(param);
        }
      }
    }
  }

  private T loadUnit(HSSFRow row)
    throws Exception
  {
    Object instance = this._class.newInstance();
    for (int x = 0; x < this.params.size(); ++x) {
      ExcelParam param = (ExcelParam)this.params.get(x);
      HSSFCell cell = row.getCell(x);

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
            param.getField().setBoolean(instance, 
              CellUtils.getBoolean(cell));
            break ;
          }
          if (param.getDataType() == DataType.BYTE) {
            param.getField().setByte(instance, 
              CellUtils.getByte(cell));
            break ;
          }
          if (param.getDataType() == DataType.INT) {
            param.getField().setInt(instance, 
              CellUtils.getInt(cell));
            break ;
          }
          if (param.getDataType() == DataType.SHORT) {
            param.getField().setShort(instance, 
              CellUtils.getShort(cell));
            break ;
          }
          if (param.getDataType() == DataType.DOUBLE) {
            param.getField().setDouble(instance, 
              CellUtils.getDouble(cell));
            break ;
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
          System.err.println("列----" + x + "出错字段：" + param.getField());
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
            method = instance.getClass().getMethod(methodName, 
              new Class[] { String.class });
            try {
              arg = CellUtils.getString(cell);
            } catch (Exception ex) {
              arg = Integer.valueOf(CellUtils.getInt(cell));
              System.err.println("列----" + x + "出错字段：" + param.getField());
            }
          }

          if (param.getDataType() == DataType.BYTE) {
            method = instance.getClass().getMethod(methodName, 
              new Class[] { Byte.TYPE });
            arg = Byte.valueOf(CellUtils.getByte(cell));
          } else if (param.getDataType() == DataType.SHORT) {
            method = instance.getClass().getMethod(methodName, 
              new Class[] { Short.TYPE });
            arg = Short.valueOf(CellUtils.getShort(cell));
          } else if (param.getDataType() == DataType.INT) {
            method = instance.getClass().getMethod(methodName, 
              new Class[] { Integer.TYPE });
            arg = Integer.valueOf(CellUtils.getInt(cell));
          } else if (param.getDataType() == DataType.BOOLEAN) {
            method = instance.getClass().getMethod(methodName, 
              new Class[] { Boolean.TYPE });
            arg = Boolean.valueOf(CellUtils.getBoolean(cell));
          } else if (param.getDataType() == DataType.DOUBLE) {
            method = instance.getClass().getMethod(methodName, 
              new Class[] { Double.TYPE });
            arg = Double.valueOf(CellUtils.getDouble(cell));
          }

          if (method == null) {
            throw new NullPointerException("方法[" + methodName + 
              "]不存在！");
          }

          if (arg.equals("-1")) {
            throw new NullPointerException("方法[" + methodName + 
              "]参数为-1！");
          }

          method.invoke(instance, new Object[] { arg });
        } catch (Exception e) {
          e.printStackTrace();
          System.err.println("列----" + x + "出错字段：" + param.getField());
        }
      }
    }
    return (T) instance;
  }

  public void printParams()
  {
    for (ExcelParam param : this.params)
      System.out.println(param);
  }

  public void setKey(ExcelParam key)
  {
    this.key = key;
  }

  public void setSheet(HSSFSheet sheet)
  {
    this.sheet = sheet;
  }

  public void setStartCol(int startCol)
  {
    this.startCol = startCol;
  }

  public void setStartRow(int startRow)
  {
    this.startRow = startRow;
  }

  public CopyOnWriteArrayList<T> getList() {
    return this.list;
  }

  public void setList(CopyOnWriteArrayList<T> list) {
    this.list = list;
  }
}