/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.codec;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ProxySet
{
  private static Element rootElt;
  private static Map<String, ProxyXmlBean> proxyList = null;

  private static final Map<String, Integer> protocolMap = new HashMap();

  private static String proxy = "proxy";
  private static String XML = ".xml";

  public static Map<String, ProxyXmlBean> getProxyList() {
    return proxyList;
  }

  public static void proxy_initialize(String fileName) {
    SAXReader sb = new SAXReader();
    try {
      if ((fileName == null) || (fileName.equals(""))) {
        fileName = proxy;
      }
      File file = new File(fileName);

      Document doc = sb.read(file);
      rootElt = doc.getRootElement();
    } catch (DocumentException e) {
      e.printStackTrace();
    }
    proxyList = translationProxy();
  }

  private static Map<String, ProxyXmlBean> translationProxy()
  {
    Map pList = null;
    try {
      pList = new HashMap();

      List typeList = rootElt.selectNodes("//proxys/proxy");
      for (Iterator iter = typeList.iterator(); iter.hasNext(); ) {
        ProxyXmlBean xmlBean = new ProxyXmlBean();
        Element typeElt = (Element)iter.next();
        String code = typeElt.attributeValue("code");
        int head = Integer.parseInt(code.replace("0x", ""), 16);
        xmlBean.setCode(Integer.valueOf(head));
        xmlBean.setClassName(typeElt.attributeValue("class-name"));
        xmlBean.setEntryBean(typeElt.attributeValue("entry-bean"));
        xmlBean.setMethodName(typeElt.attributeValue("methoed"));
        xmlBean.setRights(typeElt.attributeValue("rights"));
        pList.put(Integer.valueOf(head), xmlBean);
        protocolMap.put(xmlBean.getClassName(), Integer.valueOf(head));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return pList;
  }

  public static Map<String, Integer> getProtocolmap() {
    return protocolMap;
  }
}