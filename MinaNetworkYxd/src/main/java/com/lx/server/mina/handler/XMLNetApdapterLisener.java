/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.handler;

import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.codec.ProxyXmlBean;
import com.lx.server.mina.session.IConnect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class XMLNetApdapterLisener<T>
  implements INetApdapterLisener<T>
{
  private Log log = LogFactory.getLog(super.getClass());

  public void doProxy(IConnect session, T msg, ProxyXmlBean bean)
  {
    if ("all-pass" != bean.getRights()) {
      IBeanInvoke beanInvoke = getInvokeBean(bean.getEntryBean());
      if (beanInvoke != null)
        beanInvoke.invoke(msg, session);
      else
        this.log.error("have not IBeanInvoke:" + bean.getClassName());
    }
    else {
      return;
    }
  }

  public final void doProxy(IConnect session, T po)
  {
  }

  public void haveNoProxyCodeError(IConnect net)
  {
  }

  public void haveNoProxyError(IConnect net)
  {
  }

  public void decodeProxyError(IConnect net)
  {
  }

  protected abstract IBeanInvoke<T> getInvokeBean(String paramString);
}