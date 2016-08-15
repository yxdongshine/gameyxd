package com.lx.nserver.model;

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.NPCConfigPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:NPCConfigPojoModel <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-20 下午8:06:58 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class NpcConfigPojoModel extends ResourceModelImpl<NPCConfigPojo> {
	
	public NpcConfigPojoModel() {
		// TODO Auto-generated constructor stub
		super(TxtRes.NPC_CONFIG, NPCConfigPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		System.out.println("NPC 配置加载完成");
	}
	
}
