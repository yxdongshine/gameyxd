
package com.lx.logical.manage;  

import org.springframework.stereotype.Component;

import com.engine.entityattribute.Attribute;
import com.engine.entityobj.ServerPlayer;
import com.loncent.protocol.game.player.Role.OpenRoleAttrScreenResponse;

/** 
 * ClassName:RoleManage <br/> 
 * Function: TODO (角色管理类). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-9 下午3:06:26 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
public class RoleManage {
	
	public OpenRoleAttrScreenResponse  buildOpenRoleAttrScreenResponse(ServerPlayer sp){
		
		OpenRoleAttrScreenResponse.Builder resp=OpenRoleAttrScreenResponse.newBuilder();
		Attribute attr = sp.getAttribute();
		resp.setAir(attr.getAttribute(Attribute.AIR));
		resp.setAgility(attr.getAttribute(Attribute.AGILITY));
		resp.setDamage(attr.damage());
		resp.setTenacity(attr.getAttribute(Attribute.TENACITY));
		resp.setDefence(attr.getAttribute(Attribute.DEFENCE));
		resp.setExternalForceAttack(attr.getAttribute(Attribute.EXTERNAL_FORCE_ATTACK));
		resp.setInnerForceAttack(attr.getAttribute(Attribute.INNER_FORCE_ATTACK));
		resp.setInnerForce(attr.getAttribute(Attribute.INNER_FORCE));
		resp.setWarlordWine(1000);
		resp.setEnlightenmentTea(10000);
		return resp.build();
		
	}
}
  