package com.lx.logical.manage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.beanfactory.SpringBeanFactory;
import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.lib.utils.IConst;
import com.lx.game.entity.FuBen;
import com.lx.game.entity.GameSpace;
import com.lx.game.entity.Monster;
import com.lx.nserver.model.FuBenTemplateModel;
import com.lx.nserver.txt.FuBenTemplatePojo;

/**
 * ClassName:FuBenManage <br/>
 * Function: (副本管理类). <br/>
 * Date: 2015-8-22 上午11:12:12 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
public class FuBenManage {
	
	@Autowired
	private FuBenTemplateModel fuBenConfs;
	
	Log log = LogFactory.getLog(FuBenManage.class);
	
	/** 副本集合 **/
	private Map<Long, FuBen> fubenMaps = new ConcurrentHashMap<Long, FuBen>();
	
	/** 进入副本前信息(单人副本和组队副本需要保持) **/
	private Map<Long, Role> preRoleMaps = new ConcurrentHashMap<Long, Role>();
	
	public Map<Long, Long> willEnterFuBenMaps = new ConcurrentHashMap<Long, Long>();
	
	/**
	 * 初始化世界副本 initWorldFuBen:(). <br/>
	 */
	public void initWorldFuBen() {
		log.info("=======初始化世界副本=======");
		
		for (FuBenTemplatePojo pojo : fuBenConfs.getReslList()) {
			if (pojo.getType() == IConst.FUBEN_TYPE_WORLD) {
				FuBen fuBen = (FuBen) SpringBeanFactory.getSpringBean("worldFuBen");
				fuBen.init(pojo);
				log.info("=======创建世界副本：" + pojo.getName());
				fubenMaps.put(fuBen.getFubenId(), fuBen);
			}
		}
		
		log.info("=======初始化世界副本完成=======");
	}
	
	/**
	 * 创建组队副本 createTeamFuBen:(). <br/>
	 */
	public FuBen createTeamFuBen(int mapUid) {
		FuBenTemplatePojo pojo = fuBenConfs.get(mapUid);
		if (pojo == null)
			return null;
		if (pojo.getType() != IConst.FUBEN_TYPE_TEAM)
			return null;
		
		FuBen fuBen = (FuBen) SpringBeanFactory.getSpringBean("teamFuBen");
		fuBen.init(pojo);
		log.info("-------创建组队副本：" + pojo.getName());
		fubenMaps.put(fuBen.getFubenId(), fuBen);
		
		return fuBen;
	}
	
	/**
	 * 创建单人副本 createSingleFuBen:(). <br/>
	 */
	public FuBen createSingleFuBen(int mapUid) {
		FuBenTemplatePojo pojo = fuBenConfs.get(mapUid);
		if (pojo == null)
			return null;
		if (pojo.getType() != IConst.FUBEN_TYPE_SINGLE)
			return null;
		
		FuBen fuBen = (FuBen) SpringBeanFactory.getSpringBean("singleFuBen");
		fuBen.init(pojo);
		log.info("-------创建单人副本：" + pojo.getName());
		fubenMaps.put(fuBen.getFubenId(), fuBen);
		
		return fuBen;
	}
	
	/**
	 * 传送角色到副本 transRoleToFuBen:(). <br/>
	 */
	public boolean transRoleToFuBen(ServerPlayer sp, long fuBenId) {
		FuBen fuBen = this.fubenMaps.get(fuBenId);
		if (fuBen == null)
			return false;
		
		fuBen.roleEnterFuBen(sp);
		return false;
	}
	
	/**
	 * 角色退出副本 roleExitFuBen:(). <br/>
	 */
	public boolean roleExitFuBen(ServerPlayer sp) {
		Role role = sp.getRole();
		long fuBenId = role.getFuBenId();
		FuBen fuBen = this.fubenMaps.get(fuBenId);
		if (fuBen == null)
			return false;
		
		fuBen.roleExitFuBen(sp);
		return true;
	}
	
	/**
	 * 角色下线 roleOffline:(). <br/>
	 */
	public void roleOffline(ServerPlayer sp) {
		this.roleExitFuBen(sp);
		this.revertRoleInfo(sp.getRole(), sp.getFuBenId());
	}
	
	/**
	 * 记录角色进入副本前信息 recordRoleInfo:(). <br/>
	 */
	public void recordRoleInfo(Role role, long newFuBenId) {
		FuBen fuBen = this.getFuBen(newFuBenId);
		if (fuBen == null)
			return;
		if (fuBen.getFubenPojo().getType() != IConst.FUBEN_TYPE_WORLD) {
			this.preRoleMaps.put(role.getId(), (Role) role.clone());
		}
	}
	
	/**
	 * 获取传入副本前角色信息 getPreRoleInfo:(). <br/>
	 */
	public Role getPreRoleInfo(long roleId) {
		Role preRole = this.preRoleMaps.get(roleId);
		return preRole;
	}
	
	/**
	 * 恢复角色进入副本前信息 revertRoleInfo:(). <br/>
	 */
	public void revertRoleInfo(Role role, long exitFuBenId) {
		FuBen fuBen = this.getFuBen(exitFuBenId);
		if (fuBen == null)
			return;
		if (fuBen.getFubenPojo().getType() != IConst.FUBEN_TYPE_WORLD) {
			// 恢复进入时的位置信息
			Role preRole = this.preRoleMaps.get(role.getId());
			role.setFuBenId(preRole.getFuBenId());
			role.setMapUid(preRole.getMapUid());
			role.setX(preRole.getX() - 1.0f);
			role.setY(preRole.getY());
			role.setZ(preRole.getZ() - 1.0f);
			this.preRoleMaps.remove(role.getId());
		}
	}
	
	/**
	 * 获取副本配置 getFuBenPojo:(). <br/>
	 */
	public FuBenTemplatePojo getFuBenPojo(int mapUid) {
		return fuBenConfs.get(mapUid);
	}
	
	/**
	 * 获取副本 getFuBen:(). <br/>
	 */
	public FuBen getFuBen(long fuBenId) {
		return this.fubenMaps.get(fuBenId);
	}
	
	/**
	 * 销毁副本 destoryFuBen:(). <br/>
	 */
	public void destoryFuBen(long fuBenId) {
		FuBen fuBen = this.getFuBen(fuBenId);
		if (fuBen.getFubenPojo().getType() == IConst.FUBEN_TYPE_WORLD) {
			log.error("world fuBen cant destory. fuBenName = " + fuBen.getFubenPojo().getName());
			return;
		}
		GameSpace space = fuBen.getSpace();
		space.clearSpace();
		this.fubenMaps.remove(fuBenId);
	}
	
	/** 
	 * 通知怪物死亡
	 * onMonsterDead:(). <br/> 
	 */  
	public void onMonsterDead(Monster monster)
	{
		GameSpace space = monster.getSpace();
		boolean isFinished = space.OnMonsterDead(monster);
	}
}
