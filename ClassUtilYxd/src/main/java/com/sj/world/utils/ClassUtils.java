/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.sj.world.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClassUtils
{
  static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

  private static Log log = LogFactory.getLog(ClassUtils.class);

  public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, boolean recursive, Set<Class<?>> classes)
  {
    File dir = new File(packagePath);

    if ((!(dir.exists())) || (!(dir.isDirectory())))
    {
      return;
    }

    File[] dirfiles = dir.listFiles(new FileFilter()
    {
      public boolean accept(File file) {
        return (file.isDirectory() || (file.getName().endsWith(".class")));
      }
    });
    for (File file : dirfiles)
    {
      if (file.isDirectory())
      {
        findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, 
          classes);
      }
      else {
        String className = file.getName().substring(0, file.getName().length() - 6);
        try
        {
          classes.add(Class.forName(packageName + '.' + className));
        }
        catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static Set<Class<?>> getClasses(Package pack)
  {
    Set classes = new LinkedHashSet();

    boolean recursive = true;

    String packageName = pack.getName();
    String packageDirName = packageName.replace('.', '/');
    try
    {
      Enumeration dirs = classLoader.getResources(packageDirName);

      while (dirs.hasMoreElements())
      {
        URL url = (URL)dirs.nextElement();

        String protocol = url.getProtocol();

        if ("file".equals(protocol))
        {
          String filePath = URLDecoder.decode(url.getFile(), "UTF-8");

          findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes); } else {
          if (!("jar".equals(protocol))) {
            continue;
          }

          try
          {
            JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();

            Enumeration entries = jar.entries();

            while (entries.hasMoreElements())
            {
              JarEntry entry = (JarEntry)entries.nextElement();
              String name = entry.getName();

              if (name.charAt(0) == '/')
              {
                name = name.substring(1);
              }

              if (!(name.startsWith(packageDirName)))
                continue;
              String temp = name.substring(packageDirName.length(), packageDirName.length() + 1);

              if (!(temp.equals("/"))) {
                continue;
              }

              int idx = name.lastIndexOf(47);

              if (idx != -1)
              {
                packageName = name.substring(0, idx).replace('/', '.');
              }

              if (((idx == -1) && (!(recursive))) || 
                (!(name.endsWith(".class"))) || (entry.isDirectory()))
                continue;
              String className = name.substring(packageName.length() + 1, name.length() - 6);
              try
              {
                classes.add(Class.forName(packageName + '.' + className));
              }
              catch (ClassNotFoundException e) {
                e.printStackTrace();
              }

            }

          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return classes;
  }

  public static String[] getPackageAllClassName(String classLocation, String packageName)
  {
    String[] packagePathSplit = packageName.split("[.]");
    String realClassLocation = classLocation;
    int packageLength = packagePathSplit.length;
    for (int i = 0; i < packageLength; ++i) {
      realClassLocation = realClassLocation + File.separator + packagePathSplit[i];
    }
    File packeageDir = new File(realClassLocation);
    if (packeageDir.isDirectory()) {
      String[] allClassName = packeageDir.list();
      return allClassName;
    }
    return null;
  }
}