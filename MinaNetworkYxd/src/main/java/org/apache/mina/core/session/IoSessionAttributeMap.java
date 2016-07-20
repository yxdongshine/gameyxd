/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.session;

import java.util.Set;

public abstract interface IoSessionAttributeMap
{
  public abstract Object getAttribute(IoSession paramIoSession, Object paramObject1, Object paramObject2);

  public abstract Object setAttribute(IoSession paramIoSession, Object paramObject1, Object paramObject2);

  public abstract Object setAttributeIfAbsent(IoSession paramIoSession, Object paramObject1, Object paramObject2);

  public abstract Object removeAttribute(IoSession paramIoSession, Object paramObject);

  public abstract boolean removeAttribute(IoSession paramIoSession, Object paramObject1, Object paramObject2);

  public abstract boolean replaceAttribute(IoSession paramIoSession, Object paramObject1, Object paramObject2, Object paramObject3);

  public abstract boolean containsAttribute(IoSession paramIoSession, Object paramObject);

  public abstract Set<Object> getAttributeKeys(IoSession paramIoSession);

  public abstract void dispose(IoSession paramIoSession)
    throws Exception;
}