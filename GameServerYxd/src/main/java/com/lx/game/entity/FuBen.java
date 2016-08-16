package com.lx.game.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.engine.domain.Role;
import com.engine.entityobj.PlayerStatus;
import com.engine.entityobj.ServerPlayer;
import com.lib.utils.IConst;
import com.lib.utils.ServerRandomUtils;
import com.loncent.protocol.PublicData.PbAoiEntity;
import com.loncent.protocol.game.fuben.FuBen.PassFuBenRewardUpdateResponse;
import com.loncent.protocol.game.fuben.FuBen.PassFuBenRewardUpdateResponse.RewardItem;
import com.lx.game.send.MessageSend;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.nserver.txt.FuBenTemplatePojo;

/**
 * ClassName:FuBen <br/>
 * Function: (副本). <br/>
 * Date: 2015-8-22 上午10:29:50 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
public abstract class FuBen {
	
	class OpenBoxInfo {
		int openCount;
		ArrayList<Integer> ItemIdList = new ArrayList<Integer>();
	}
	
	@Autowired
	private ItemLogicManage itemManage;
	
	Log log = LogFactory.getLog(FuBen.class);
	
	/** 准备状态 **/
	public static final int FUBEN_STATUS_READY = 1;
	/** 运行状态 **/
	public static final int FUBEN_STATUS_RUNNING = 2;
	/** 结束状态 **/
	public static final int FUBEN_STATUS_END = 3;
	
	/** 副本ID **/
	private long fubenId;
	
	/** 副本状态 **/
	private int status;
	
	/** 副本场景 **/
	private GameSpace space;
	
	/** 副本配置 **/
	private FuBenTemplatePojo fubenPojo;
	
	private Map<Long, OpenBoxInfo> openBoxInfos = new ConcurrentHashMap<Long, OpenBoxInfo>();
	
	public FuBen() {
		this.status = FUBEN_STATUS_READY;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public long getFubenId() {
		return fubenId;
	}
	
	public void setFubenId(long fubenId) {
		this.fubenId = fubenId;
	}
	
	public GameSpace getSpace() {
		return space;
	}
	
	public void setSpace(GameSpace space) {
		this.space = space;
	}
	
	public FuBenTemplatePojo getFubenPojo() {
		return fubenPojo;
	}
	
	public void setFubenPojo(FuBenTemplatePojo fubenPojo) {
		this.fubenPojo = fubenPojo;
	}
	
	public abstract void init(FuBenTemplatePojo pojo);
	
	/**
	 * 进入副本 roleEnterFuBen:(). <br/>
	 */
	public void roleEnterFuBen(ServerPlayer sp) {
		if (this.status != FUBEN_STATUS_RUNNING) {
			log.error("fuBen is not Running. name = " + this.fubenPojo.getName());
			return;
		}
		
		sp.setSpace(space);
		Role role = sp.getRole();
		
		role.setFuBenId(this.fubenId);
		role.setMapUid(this.fubenPojo.getMapUid());
		sp.setX(role.getX());
		sp.setY(role.getY());
		sp.setZ(role.getZ());
		space.playerIntoMap(sp);
		sp.setStatus(PlayerStatus.STATUS_ENTER_MAP);
	}
	
	/**
	 * 退出副本 roleExitFuBen:(). <br/>
	 */
	public void roleExitFuBen(ServerPlayer sp) {
		
		this.space.exitMap(sp);
	}
	
	/**
	 * 开始副本 startFuBen:(). <br/>
	 */
	public void startFuBen() {
		status = FUBEN_STATUS_RUNNING;
		
		// TODO开始计时等
	}
	
	/**
	 * 结束副本 endFuBen:(). <br/>
	 */
	public void endFuBen() {
		
		if (this.fubenPojo.getType() == IConst.FUBEN_TYPE_WORLD) {
			log.error("world FuBen cant End!!!! fuBenName = " + this.fubenPojo.getName());
			return;
		}
		
		status = FUBEN_STATUS_END;
		
		// TODO 判断是否通关
		boolean passed = true;
		if (passed == true) {
			this.giveFuBenReward();
		}
		
	}
	
	/**
	 * 发放通关奖励 giveFuBenReward:(). <br/>
	 */
	private void giveFuBenReward() {
		for (Map.Entry<Long, ServerPlayer> spObj : this.space.getPlayerMaps().entrySet()) {
			ServerPlayer sp = spObj.getValue();
			// 计算得分
			int starNum = 5;
			
			int gold = this.fubenPojo.getGoldReward();
			int copper = this.fubenPojo.getCopperReward();
			int sliver = this.fubenPojo.getSliverReward();
			int exp = this.fubenPojo.getExpReward();
			// 发放奖励
			PassFuBenRewardUpdateResponse.Builder updateMsg = PassFuBenRewardUpdateResponse.newBuilder();
			updateMsg.setGold(this.fubenPojo.getGoldReward());
			updateMsg.setCopper(this.fubenPojo.getCopperReward());
			updateMsg.setSliver(this.fubenPojo.getSliverReward());
			updateMsg.setExp(this.fubenPojo.getExpReward());
			
			sp.addExp(exp);
			sp.addMoney(copper);
			sp.addBindGold(sliver);
			sp.addRechargeGold(gold);
			
			int[][] items = this.fubenPojo.getItemRewards();
			for (int i = 0; i < items.length; i++) {
				RewardItem.Builder itemBuilder = RewardItem.newBuilder();
				itemBuilder.setItemId(items[i][0]);
				itemBuilder.setNum(items[i][1]);
				updateMsg.addItemRewards(itemBuilder);
				itemManage.putItemInBagByPropertyId(items[i][0], items[i][1], sp);
			}
			updateMsg.setStarNum(starNum);
			updateMsg.setMapUid(this.getFubenPojo().getMapUid());
			MessageSend.sendToGate(updateMsg.build(), spObj.getValue());
		}
	}
	
	/**
	 * 增加开箱次数 addOpenBoxCount:(). <br/>
	 */
	public void addOpenBoxCount(long roleId, int count) {
		if (openBoxInfos.containsKey(roleId)) {
			OpenBoxInfo boxInfo = openBoxInfos.get(roleId);
			boxInfo.openCount += count;
		} else {
			OpenBoxInfo boxInfo = new OpenBoxInfo();
			boxInfo.openCount = count;
			openBoxInfos.put(roleId, boxInfo);
		}
	}
	
	public int getRoleOpenCount(long roleId) {
		if (openBoxInfos.containsKey(roleId)) {
			OpenBoxInfo boxInfo = openBoxInfos.get(roleId);
			return boxInfo.openCount;
		}
		return 0;
	}
	
	/**
	 * 扣除开箱子的花费 costOpenBox:(). <br/>
	 */
	public boolean costOpenBox(ServerPlayer sp, int count) {
		int curCount = this.getRoleOpenCount(sp.getRole().getId());
		ArrayList<Integer> openCountList = new ArrayList<Integer>();
		for (int i = curCount + 1; i <= curCount + count; i++) {
			openCountList.add(i);
		}
		
		for (Iterator<Integer> it = openCountList.iterator(); it.hasNext();) {
			int openCount = it.next();
			switch (openCount) {
				case 1:
					break;
				case 2: {
					// 扣除铜币
					sp.addMoney(-this.fubenPojo.getOpenBoxCosts()[0][0]);
					break;
				}
				case 3: {
					// 扣除金币
					sp.addRechargeGold(-this.fubenPojo.getOpenBoxCosts()[0][1]);
					break;
				}
				case 4: {
					// 扣除金币
					sp.addRechargeGold(-this.fubenPojo.getOpenBoxCosts()[0][2]);
					break;
				}
				default: {
					break;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 获取奖励 getBoxReward:(). <br/>
	 */
	public Map<Integer, Integer> getBoxReward(long roleId, int count) {
		OpenBoxInfo boxInfo = this.openBoxInfos.get(roleId);
		List<Integer> itemIdList = new ArrayList<Integer>();
		Map<Integer, Integer> canGetList = new HashMap<Integer, Integer>();
		int[][] items = this.fubenPojo.getItemRewards();
		for (int i = 0; i < items.length; i++) {
			int itemId = items[i][0];
			int num = items[i][1];
			if (!boxInfo.ItemIdList.contains(itemId)) {
				canGetList.put(itemId, num);
				itemIdList.add(itemId);
			}
		}

		Map<Integer, Integer> rewardList = new HashMap<Integer, Integer>();
		if(itemIdList.size() <= 0) return rewardList;
		for (int i = 0; i < count; i++) {
			// 随机抽取
			int seed = ServerRandomUtils.randomNum(0, itemIdList.size() - 1);
			int getItemId = itemIdList.get(seed);
			itemIdList.remove(seed);
			rewardList.put(getItemId, canGetList.get(getItemId));
			boxInfo.ItemIdList.add(getItemId);
		}
		
		return rewardList;
	}
}
