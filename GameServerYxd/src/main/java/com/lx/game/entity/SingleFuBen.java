package com.lx.game.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lx.logical.manage.SpaceManage;
import com.lx.nserver.txt.FuBenTemplatePojo;

/**
 * ClassName:FuBen <br/>
 * Function: (单人副本，剧情，装备类). <br/>
 * Date: 2015-8-22 上午10:54:00 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Scope("prototype")
public class SingleFuBen extends FuBen {
	@Autowired
	private SpaceManage spaceMange;
	
	public SingleFuBen() {
		
	}
	
	@Override
	public void init(FuBenTemplatePojo pojo) {
		// 创建场景
		GameSpace space = spaceMange.createSingleSpace(pojo.getMapUid());
		this.setFubenPojo(pojo);
		this.setSpace(space);
		this.setFubenId(space.getServerMapUid());
	}
	
}
