/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.logging;

public enum LogLevel
{
  TRACE, DEBUG, INFO, WARN, ERROR, NONE;

  private int level;

  public int getLevel()
  {
    return this.level;
  }
}