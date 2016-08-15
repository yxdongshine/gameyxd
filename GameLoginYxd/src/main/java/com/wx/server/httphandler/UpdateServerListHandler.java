/** 
 * Project Name:DragonBallWorldServerHappy 
 * File Name:FightReport.java 
 * Package Name:com.sj.world.httphandler 
 * Date:2014-2-18下午4:15:47 
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.wx.server.httphandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.wx.server.beanfactory.SpringBeanFactory;
import com.wx.server.dbdao.EntityDAO;
import com.wx.server.dbdao.EntityDAOInterface;
import com.wx.server.domain.AreaList;
import com.wx.server.domain.ServerList;
import com.wx.server.httphandler.decoder.DecoderRequest;
import com.wx.server.logical.manage.LoginManage;
import com.wx.server.obj.LoginServerList;
import com.wx.server.utils.LogUtils;
import com.wx.server.utils.ToolUtils;

/**
 * FightReportHandler <br/>
 * Function: TODO (更新服务器列表). <br/>
 * Reason: TODO (). <br/>
 * Date: 2014-2-18 下午4:15:47 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */

public class UpdateServerListHandler implements HttpHandler {
	
	private Log log = LogFactory.getLog(UpdateServerListHandler.class);
	
	/**** http://xxx.xxx.xxx.xxx?key=add&area=1&server=1,9 *****/
	/** 命令键值 **/
	private String cmdKey[] = { "key", "area", "server" };
	/** 命令 **/
	private String cmdV[] = { "add", "update", "del" };
	
	@Autowired(required=true)
	private EntityDAOInterface dao;
	
	@Override
	public void handle(HttpExchange he) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("进来这儿了");
		DecoderRequest dr = newMyRequest(he.getRequestURI().toString());
		String cmd = dr.getAttribute(cmdKey[0]);
		String area = dr.getAttribute(cmdKey[1]);
		String servers = dr.getAttribute(cmdKey[2]);
		byte result = 0;
		String value = null;
		// 判断命令是否相等
		if (!ToolUtils.isStringNull(area) && area.matches("^[0-9]*$")) {
			if (cmd.equals(cmdV[0])) {// 增加
				AreaList pAreaList = dao.findById(AreaList.class, Integer.parseInt(area));
				LoginServerList lsl = LoginManage.addArea(pAreaList);
				if (lsl != null) {
					ArrayList<ServerList> lists = splitServerLists(servers);
					for (ServerList s : lists) {
						boolean badd = LoginManage.addServer(lsl, s);
						result = badd ? (byte) 1 : 0;
					}
				}
				
			} else if (cmd.equals(cmdV[1])) {// 更新
				AreaList pAreaList = dao.findById(AreaList.class, Integer.parseInt(area));
				if (pAreaList != null) {
					LoginServerList tmpLogin = LoginManage.contains(Integer.parseInt(area));
					AreaList tmpAreaList = tmpLogin.getAreaList();
					if (tmpAreaList != null) {
						tmpAreaList.setAreaName(pAreaList.getAreaName());
						tmpAreaList.setId(pAreaList.getId());
						tmpAreaList.setStatus(pAreaList.getStatus());
						// 服务器列表
						ArrayList<ServerList> lists = splitServerLists(servers);
						for (ServerList s : lists) {// 小心有可能引起并发
							ServerList pServerList = LoginManage.serverListMap.get("" + s.getId());
							if (pServerList != null) {
								pServerList.setAreaId(s.getAreaId());
								pServerList.setCurrentAppId(s.getCurrentAppId());
								pServerList.setId(s.getId());
								pServerList.setIsNewServer(s.getIsNewServer());
								pServerList.setIp(s.getIp());
								pServerList.setMaxPlayerLimit(s.getMaxPlayerLimit());
								pServerList.setPort(s.getPort());
								pServerList.setServerName(s.getServerName());
								pServerList.setSortIndex(s.getStatus());
								result = 1;
							}
						}
					}
				} else if (cmd.equals(cmdV[2])) {// 删除
					// AreaList pAreaList = dao.findById(AreaList.class, Integer.parseInt(area));
				}
			}
		}
		String resp = "sucess";
		if (result != 1) {
			resp = "fail";
		}
		byte bData[] = resp.getBytes("utf-8");
		he.sendResponseHeaders(200, bData.length);
		OutputStream os = he.getResponseBody();
		os.write(bData);
		os.flush();
		os.close();
	}
	
	/**
	 * splitServerLists:(). <br/>
	 * TODO().<br/>
	 * 拆分列表字符串
	 * 
	 * @author lyh
	 * @param str
	 * @return
	 */
	private ArrayList<ServerList> splitServerLists(String str) {
		ArrayList<ServerList> alist = new ArrayList<ServerList>();
		String s[] = ToolUtils.split(str, ",");
		for (String id : s) {
			boolean b = id.matches("^[0-9]*$");
			if (b) {
				ServerList pServerList = dao.findById(ServerList.class, Integer.parseInt(id));
				alist.add(pServerList);
			}
		}
		return alist;
		
	}
	
	/**
	 * Function name:newMyRequest Description: 生成新的一个数据
	 * 
	 * @param RequestURI get方式的http文本数据
	 * @return Request
	 */
	public static DecoderRequest newMyRequest(String RequestURI) {
		int beginIndex = RequestURI.indexOf("?");
		String body = RequestURI.substring(beginIndex + 1);
		DecoderRequest r = new DecoderRequest(body);
		return r;
	}
	
	// private void setJsonString(Proxy p, String json) {
	// String strField[] = p.getFieldTypes();
	// for (String str : strField) {
	// String[] fields = str.split(":");
	// String fieldName = fields[0];
	// String type = fields[1];
	// if (type.contains("string")) {
	// try {
	// Field f = p.getClass().getDeclaredField(fieldName);
	// try {
	// f.set(p, json);
	// } catch (IllegalArgumentException
	// | IllegalAccessException e) {
	// e.printStackTrace();
	// }
	// } catch (NoSuchFieldException e) {
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }
	// }
}
