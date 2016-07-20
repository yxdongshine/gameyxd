/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.read.excel;

import com.read.tool.excel.ExcelLoader;
import com.read.tool.excel.XlsKey;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ResourceModelImpl<T> extends Dictionary<T>
  implements ResourceModel<T>
{
  private static final long serialVersionUID = 5716704764378187037L;
  protected Log log = LogFactory.getLog(super.getClass().getName());
  protected String resFile;
  protected Class<T> _class;
  protected ExcelParam xlsKey;
  private CopyOnWriteArrayList<T> reslList;

  public ResourceModelImpl()
  {
  }

  public ResourceModelImpl(String resFile, Class<T> _class)
  {
    this.resFile = resFile;
    this._class = _class;
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
        param.setName(field.getName());
        if (annotation instanceof XlsKey)
          this.xlsKey = param;
      }
    }
  }

  public T[] asArray()
  {
    return asArray(this.xlsKey.getName());
  }

  protected T[] asArray(String field)
  {
    List list = sort();
    Object[] valueArrays = (Object[])Array.newInstance(this._class, list.size());
    for (int i = 0; i < list.size(); ++i) {
      valueArrays[i] = list.get(i);
    }
    return (T[]) valueArrays;
  }

  public T get(int resID)
  {
    return lookupEntry(resID);
  }

  public T get(String resName)
  {
    return lookupEntry(resName);
  }

  protected int getKeyValue(T value)
  {
    if (this.xlsKey == null)
      return 0;
    return getValue(value, this.xlsKey.getField().getName());
  }

  protected String getName(T value)
  {
    try
    {
      Method method = this._class.getMethod("getName", new Class[0]);
      String name = (String)method.invoke(value, new Object[0]);
      return name;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
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

  public void load()
    throws Exception
  {
    reset();
    ExcelLoader loader = new ExcelLoader(this.resFile, this._class);
    Map stages = loader.load();
    this.reslList = loader.getList();
    for (Iterator iterator = stages.entrySet().iterator(); iterator.hasNext();) {
  	  Map.Entry entry = (Map.Entry) iterator.next();
      Object value = entry.getValue();

      Integer key = Integer.valueOf(getKeyValue((T) value));
      String name = null;
      if (name == null) {
        name = key+"";
      }
      addEntry(key.intValue(), name, (T) value);
    }
  }

  protected void load(String[] sheets)
    throws Exception
  {
    reset();
    ExcelLoader loader = new ExcelLoader(this.resFile, this._class);
    Map stages = loader.load(sheets);
    this.reslList = loader.getList();
    for (Iterator iterator = stages.entrySet().iterator(); iterator.hasNext();) {
	  Map.Entry entry = (Map.Entry) iterator.next();
      Object value = entry.getValue();
      Integer key = Integer.valueOf(getKeyValue((T) value));
      String name = null;
      if (name == null) {
        name = key+"";
      }
      addEntry(key.intValue(), name, (T) value);
    }
  }

  protected void load(String[] sheets, boolean keyFromZero)
    throws Exception
  {
    reset();
    ExcelLoader loader = new ExcelLoader(this.resFile, this._class);
    Map stages = loader.load(sheets, keyFromZero);
    this.reslList = loader.getList();
    for (Iterator iterator = stages.entrySet().iterator(); iterator.hasNext();) {
  	  Map.Entry entry = (Map.Entry) iterator.next();
      Object value = entry.getValue();
      Integer key = Integer.valueOf(getKeyValue((T) value));
      String name = null;
      if (name == null) {
        name = key+"";
      }
      addEntry(key.intValue(), name, (T) value);
    }
  }

  protected void loadList(String[] sheets)
    throws Exception
  {
    reset();
    ExcelLoader loader = new ExcelLoader(this.resFile, this._class);

    Integer cursor = Integer.valueOf(0);
    for (Object value : loader.loadAsList(sheets))
    {
      String name = getName((T) value);
      if (name == null) {
        name = cursor+"";
      }
      addEntry(cursor.intValue(), name, (T) value);
      cursor = Integer.valueOf(cursor.intValue() + 1);
    }
  }

  public void print()
  {
    for (Object value : sort())
    {
      System.out.println("加载[" + this._class.getSimpleName() + "] " + value);
    }
  }

  @SuppressWarnings("unchecked")
protected List<T> sort()
  {
    List result = iterate();
    Collections.sort(result, new Comparator() {
      public int compare(Object o1, Object o2) {
        return ((ResourceModelImpl.this.getKeyValue((T) o1) < ResourceModelImpl.this.getKeyValue((T) o2)) ? 0 : 1);
      }
    });
    return result;
  }
  

  public int length() {
    return countEntries();
  }

  public CopyOnWriteArrayList<T> getReslList() {
    return this.reslList;
  }

  public void setReslList(CopyOnWriteArrayList<T> reslList) {
    this.reslList = reslList;
  }
}