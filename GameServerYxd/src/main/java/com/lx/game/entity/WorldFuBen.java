package com.lx.game.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.AbsSpace;
import com.lx.logical.manage.SpaceManage;
import com.lx.nserver.txt.FuBenTemplatePojo;

/**
 * ClassName:FuBen <br/>
 * Function: (世界副本). <br/>
 * Date: 2015-8-22 上午10:54:00 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Scope("prototype")
public class WorldFuBen extends FuBen {
	
	@Autowired
	private SpaceManage spaceMange;
	
	public WorldFuBen() {
		
	}
	
	@Override
	public void init(FuBenTemplatePojo pojo) {
		// 创建场景
		GameSpace space = spaceMange.createWorldSpace(pojo.getMapUid());
		this.setFubenPojo(pojo);
		this.setSpace(space);
		this.setFubenId(space.getServerMapUid());
		this.startFuBen();
	}
	
}
