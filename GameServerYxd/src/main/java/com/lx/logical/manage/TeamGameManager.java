
package com.lx.logical.manage;  


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.lib.utils.ServerUUID;
import com.loncent.protocol.game.team.Team.RoleApplyInTeamListResponse;
import com.loncent.protocol.game.team.Team.RoleApplyInTeamResponse;
import com.loncent.protocol.game.team.Team.RoleDismissTeamResponse;
import com.loncent.protocol.game.team.Team.RoleDriverOutTeamResponse;
import com.loncent.protocol.game.team.Team.RoleInviteOtherResponse;
import com.loncent.protocol.game.team.Team.RoleListTeamResponse;
import com.loncent.protocol.game.team.Team.RoleOtherInviteMeResponse;
import com.loncent.protocol.game.team.Team.RoleTeamerDataListResponse;
import com.loncent.protocol.game.team.Team.RoleTransferInTeamResponse;
import com.loncent.protocol.game.team.Team.TeamerListData;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.game.team.Team;
import com.lx.game.team.TeamManager;
import com.lx.game.team.TeamModular;
import com.lx.game.team.TeamStaticConifg;
import com.lx.game.team.Teamer;
import com.lx.nserver.model.TeamConfigModel;
import com.lx.nserver.txt.TeamConfigPojo;

/** 
 * ClassName:TeamGameManager <br/> 
 * Function: TODO (). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-8-31 下午4:48:22 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
public class TeamGameManager {
	
	@Autowired
	private  TeamConfigModel teamConfigModel;
	
	
	public static ConcurrentHashMap<Long, HashMap<Long, Integer>> driverOutMap= new ConcurrentHashMap<Long, HashMap<Long,Integer>>(); 
	
	
	/**
	 * 加载组队配置文件
	 * initTeamData:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd
	 */
	public  void initTeamData(){
		if(teamConfigModel!=null&&teamConfigModel.getReslList()!=null&&teamConfigModel.getReslList().size()>0){//大于零
			TeamConfigPojo teamConfigPojo=teamConfigModel.getReslList().get(0);
			TeamStaticConifg.num++;
			TeamStaticConifg.TEAM_INVITE_TIME=teamConfigPojo.getTeamInviteTime();//邀间隔时间
			TeamStaticConifg.TEAM_INVITE_MIN_NUMBER=teamConfigPojo.getMinNumber();//最小人数
			TeamStaticConifg.TEAM_INVITE_MAX_NUMBER=teamConfigPojo.getMaxNumber();//最大人数
			TeamStaticConifg.TEAM_LIMIT_MAX_NUMBER=teamConfigPojo.getMaxInviteNumber();//最大申请数
			TeamStaticConifg.TEAM_LEADER_PIC=teamConfigPojo.getLaederPic();//队长图标
			TeamStaticConifg.TEAM_VOTE_TIME=teamConfigPojo.getVoteTime();//投票有效时间数
		}
		
	}
	
	
	/**
	 * 解散队伍
	 * dismissTeam:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 * @return
	 */
	public List<Teamer> dismissTeam(ServerPlayer sp){

		List<Teamer> teamerList=new ArrayList<Teamer>();
		 if(sp.getTeamModular()!=null&&sp.getTeamModular().getTeam()!=null){
			 long teamId=sp.getTeamModular().getTeam().getTeamId();
			 //依次删除小队中所有的队员
			 teamerList=sp.getTeamModular().getTeam().getObjects();
			 for (int i = 0; i < teamerList.size(); i++) {
				 ServerPlayer sp2=GameGlogalContainer.getRolesMap().get(teamerList.get(i).getRoleId());
				 if(sp2!=null){//删除小队
					 sp2.getTeamModular().setTeamId(0);
					 //发送解散小队消息 
					 MessageSend.sendToGate(buildRoleDismissTeamResponse(sp2),sp2);
				 }
            }
			 //删除数据库
			 TeamManager.removeTeam(teamId);
		 }
		 return teamerList;
	}
	
	
   /**
    * 驱逐某个玩家
    * diverOutTeam:(). <br/> 
    * TODO().<br/> 
    * 
    * @author yxd 
    * @param serverPlayer
    * @param beDiverOutRoleId
    */
	public void diverOutTeam(ServerPlayer serverPlayer,long beDiverOutRoleId){
		if(hasTeam(serverPlayer)){//存在团队
			boolean isLeader=false;
			long oldTeamerId=serverPlayer.getTeamModular().getTeam().getTeamLeader().getRoleId();
			if(beDiverOutRoleId==oldTeamerId){
				isLeader=true;
			}
			List<Teamer> beDiverOutAfterTeamerList=serverPlayer.getTeamModular().getTeam().getObjects();
			//先让某个玩家离开
			ServerPlayer beDiverOutRole=GameGlogalContainer.getRolesMap().get(beDiverOutRoleId);
			serverPlayer.getTeamModular().getTeam().removeTeamer(beDiverOutRoleId);//先修改离开的团队数据
			if(beDiverOutRole!=null){//表示不是下线
				delServerPlayerTeam(beDiverOutRole);
				MessageSend.sendToGate(buildRoleDriverOutTeamResponse(beDiverOutRoleId, 1), beDiverOutRole);
			}
			if(beDiverOutAfterTeamerList.size()<=1){//表示一个人解散团队
				if(beDiverOutAfterTeamerList!=null&&beDiverOutAfterTeamerList.size()==1){
					long leaderRoleId=beDiverOutAfterTeamerList.get(0).getRoleId();
					ServerPlayer spLeader=GameGlogalContainer.getRolesMap().get(leaderRoleId);
					//给spleader发送两个玩家离开组队
					MessageSend.sendToGate(buildRoleDriverOutTeamResponse(leaderRoleId, 1), spLeader);
					MessageSend.sendToGate(buildRoleDriverOutTeamResponse(beDiverOutRoleId, 1), spLeader);
					//删除内存
					long teamId=spLeader.getRole().getId();
					TeamManager.removeTeam(teamId);
					//最后一个人的team变为0
					delServerPlayerTeam(spLeader);
				}
			}else{//发送某人离开团队
				
				for (int i = 0; i < beDiverOutAfterTeamerList.size(); i++) {
					ServerPlayer sp=GameGlogalContainer.getRolesMap().get(beDiverOutAfterTeamerList.get(i).getRoleId());
					//发送某人离开团队
					MessageSend.sendToGate(buildRoleDriverOutTeamResponse(beDiverOutRoleId, 1), sp);
					//发送小队列表
	            	MessageSend.sendToGate(buildRoleTeamerDataListResponse(sp.getTeamModular()),sp);
                }
			}
			//如果是队长呢 
			if(isLeader){
				List<ServerPlayer> spList =new ArrayList<ServerPlayer>();
				long newTeamerId=serverPlayer.getTeamModular().getTeam().getTeamLeader().getRoleId();
				ServerPlayer oldLeader=GameGlogalContainer.getRolesMap().get(oldTeamerId);
				ServerPlayer newTeamer=GameGlogalContainer.getRolesMap().get(newTeamerId);
				spList.add(oldLeader);
				spList.add(newTeamer);
				sendAllTeamerTransferLeader(spList);
			}
		}
		
	}
	
	/**
	 * 删除玩家小队数据结构
	 * delServerPlayerTeam:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 */
	public void delServerPlayerTeam(ServerPlayer sp){
		if(hasTeam(sp)){
			sp.getTeamModular().setTeamId(0);
		}
	}
	
	/**
	 * 是否满员
	 * isFull:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 * @return
	 */
	public boolean isFull(ServerPlayer sp){
		boolean isFull=false;
		if(sp.getTeamModular()!=null&&sp.getTeamModular().getTeam()!=null){
			if(sp.getTeamModular().getTeam().getObjects()!=null&&sp.getTeamModular().getTeam().getObjects().size()>=TeamStaticConifg.TEAM_INVITE_MAX_NUMBER){
				isFull=true;//不满足
			}
		}
		
		return isFull;
	}
	
	/**
	 * 是否超过加入队伍人数
	 * canPassInviteLimit:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 * @return
	 */
	public boolean canPassInviteLimit(ServerPlayer sp){
		boolean isFull=false;
		if(sp.getTeamModular()!=null&&sp.getTeamModular().getTeam()!=null){
			if(sp.getTeamModular().getTeam().getRequestList()!=null&&sp.getTeamModular().getTeam().getRequestList().size()<TeamStaticConifg.TEAM_LIMIT_MAX_NUMBER){
				isFull=true;//没有超过限制人数
			}
		}
		
		return isFull;
	}
	
	/**
	 * 加入申请小队的申请列表中
	 * joinPassInviteLimit:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 */
	public boolean joinPassInviteLimit(ServerPlayer leader,ServerPlayer sp){
		boolean hasRequester=false;
		if(leader.getTeamModular()!=null&&leader.getTeamModular().getTeam()!=null){
			if(leader.getTeamModular().getTeam().hasRequestRole(sp.getRole().getId())){//已经存在
				hasRequester=true;
			}else{
				leader.getTeamModular().getTeam().getRequestList().add(sp.getRole().getId());
			}
			
		}
		return hasRequester;
	}
	
	/**
	 * 判断玩家是否已经有小队
	 * hasTeam:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 * @return
	 */
	public boolean hasTeam(ServerPlayer sp){
		boolean hasTeam=false;
		if(sp.getTeamModular()!=null&&sp.getTeamModular().getTeam()!=null){
			hasTeam=true;
		}
		return hasTeam;
	}
	
	/**
	 * 判断小队中是否已经有该成员
	 * hasTeamer:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 * @param roleId
	 * @return
	 */
	public boolean hasTeamer(ServerPlayer sp,long roleId){
		boolean hasTeamer=false;
		if(hasTeam(sp)){
			hasTeamer=sp.getTeamModular().hasRoleInTeam(roleId);
		}
		return hasTeamer;
	}
	
	/**
	 * 获取小队编号
	 * getLeaderId:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param serverPlayer
	 * @return
	 */
	public long getLeaderId(ServerPlayer serverPlayer){
		long launchInviteRoleId=0;
		if(serverPlayer.getTeamModular()!=null&&serverPlayer.getTeamModular().getTeam()!=null){
			launchInviteRoleId=serverPlayer.getTeamModular().getTeam().getTeamLeader().getRoleId();
		}else{
			launchInviteRoleId=serverPlayer.getRole().getId();
		}
		return launchInviteRoleId;
	}
	
	/**
	 * 
	 * initSpTeam:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param serverPlayer
	 */
	public void initSpTeam(ServerPlayer serverPlayer){
			//获取团队
			Team team=TeamManager.getTeam(0);
			
			/**
			 * 构建小队模块
			 */
			TeamModular tm=new TeamModular();
			tm.setTeamId(team.getTeamId());
			
			/**
			 *设置 队伍
			 */
			serverPlayer.setTeamModular(tm);
			
			/**
			 * 发送信息
			 */
			this.sendTeamerAllInfo(serverPlayer);
	}
	
	
	/**
	 * 
	 * buildTeam:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param launchSp
	 * @param beInvitedSp
	 */
	public void buildTeam(ServerPlayer launchSp,ServerPlayer beInvitedSp){
		if(launchSp!=null&&beInvitedSp!=null){
			//初始化团队配置文件
			if(TeamStaticConifg.num==0){
				initTeamData();
			}
			/**
			 * 构建队员
			 */
			Teamer teamer=new Teamer();
			teamer.setRoleId(launchSp.getRole().getId());
			teamer.setOrder((byte)TeamStaticConifg.TEAM_LEADER_ORDER);
			Teamer teamer2=new Teamer();
			teamer2.setOrder((byte)TeamStaticConifg.TEAM_LEADER_ORDER_2);
			teamer2.setRoleId(beInvitedSp.getRole().getId());
			
			/**
			 * 构建团队
			 */
			Team team=new Team(ServerUUID.getUUID());
			team.getObjects().add(teamer);
			team.getObjects().add(teamer2);
			
			/**
			 * 构建邀请者 小队模块
			 */
			TeamModular launchtm=new TeamModular();
			launchtm.setTeamId(team.getTeamId());
			launchtm.setInviteTime(System.currentTimeMillis());//设置该次邀请的时间
			
			/**
			 * 邀请者 添加队伍
			 */
			launchSp.setTeamModular(launchtm);
			
			/**
			 * 构建被邀请者 小队模块
			 */
			TeamModular beInvitedtm=new TeamModular();
			beInvitedtm.setTeamId(team.getTeamId());
			
			/**
			 * 被邀请者添加队伍
			 */
			beInvitedSp.setTeamModular(beInvitedtm);
			
			/**
			 * 存入缓存
			 */
			TeamManager.addTeam(team.getTeamId(), team);
		}
	}
	
	/**
	 * 设置角色小队编号
	 * setRoleTeamId:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 * @param teamId
	 */
	public void setRoleTeamId(ServerPlayer sp ,long teamId){

	}
	
	/**
	 * 移除角色小队编号
	 * removeRoleTeamId:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 */
	public void removeRoleTeamId(ServerPlayer sp ){
		
	}
	
	/**
	 * 
	 * addTeamerInTeam:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param leader
	 * @param iningsp
	 */
	public void addTeamerInTeam(ServerPlayer leader,ServerPlayer iningsp){
		Teamer teamer=new Teamer(iningsp.getRole().getId(),(byte)leader.getTeamModular().nextTeamerOrder());
		//设置iningsp的team编号
		TeamModular agreeRoleInvitetm=new TeamModular();
		agreeRoleInvitetm.setTeamId(leader.getTeamModular().getTeamId());
		iningsp.setTeamModular(agreeRoleInvitetm);
		//添加成员
		leader.getTeamModular().getTeam().getObjects().add(teamer);
	}
	
	/**
	 * 角色下线
	 * roleOffline:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 */
	public void roleOffline(ServerPlayer sp,long offlineRoleId){
		diverOutTeam(sp, offlineRoleId);
	}
	
	/**
	 * 发送给每个小队成员的所有小队组成信息
	 * sendAllTeamerAllInfo:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param serverPlayer
	 */
	public void sendAllTeamerAllInfoAllMessage(ServerPlayer serverPlayer,com.google.protobuf.GeneratedMessage generatedMessage){
		for (int i = 0; i < serverPlayer.getTeamModular().getTeam().getObjects().size(); i++) {
			Teamer teamer=serverPlayer.getTeamModular().getTeam().getObjects().get(i);
			if(teamer!=null){
				ServerPlayer sp = GameGlogalContainer.getRolesMap().get(teamer.getRoleId());
				if(sp!=null){//在线发送
					MessageSend.sendToGate(generatedMessage, sp);
				}
			}
        }
	}
	
	/**
	 *  发送给每个小队成员的所有小队组成信息
	 * sendAllTeamerAllInfo:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param serverPlayer
	 */
	public void sendAllTeamerAllInfo(ServerPlayer serverPlayer){
		RoleTeamerDataListResponse roleTeamerDataListResponse =this.buildRoleTeamerDataListResponse(serverPlayer.getTeamModular());
		this.sendAllTeamerAllInfoAllMessage(serverPlayer, roleTeamerDataListResponse);
	}
	
	
	/**
	 * 构建发送给全队人员组队信息成功
	 * sendAllTeamerBulidTeam:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param serverPlayer
	 * @param result
	 */
	public void sendAllTeamerBulidTeam(ServerPlayer serverPlayer,int result){
		RoleInviteOtherResponse  roleInviteOtherResponse =buildRoleInviteOtherResponse(result, serverPlayer);
		this.sendAllTeamerAllInfoAllMessage(serverPlayer, roleInviteOtherResponse);
	}
	
	/**
	 * 全队某人被驱逐消息
	 * sendAllTeamerBuildRoleListTeamResponse:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 * @param beDriversp
	 */
	public void sendAllTeamerBuildRoleListTeamResponse(ServerPlayer sp,ServerPlayer beDriversp){
		RoleListTeamResponse roleListTeamResponse=this.buildRoleListTeamResponse(beDriversp.getRole().getId(), beDriversp.getLevel(), beDriversp.getRole().getCareerConfigId(), beDriversp.getName());
		this.sendAllTeamerAllInfoAllMessage(sp, roleListTeamResponse);
	}
	/**
	 * 发送给小队所有成员有新人进入
	 * sendAllTeamerNewTeamerJoin:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param serverPlayer
	 */
	public void sendAllTeamerNewTeamerJoin(ServerPlayer serverPlayer,long roleId,int result){
		RoleApplyInTeamResponse roleApplyInTeamResponse= buildRoleApplyInTeamResponse(roleId, result);
		this.sendAllTeamerAllInfoAllMessage(serverPlayer, roleApplyInTeamResponse);
	}
	/**
	 * 移交队长 全队发送
	 * sendAllTeamerTransferLeader:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param spList
	 */
	public void sendAllTeamerTransferLeader(List<ServerPlayer> spList){
		RoleTransferInTeamResponse  roleTransferInTeamResponse =buildRoleTransferInTeamResponse(spList);
		this.sendAllTeamerAllInfoAllMessage(spList.get(0), roleTransferInTeamResponse);
	}
	
	/**
	 * 个人加载是发送 个人
	 * sendTeamerAllInfo:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param serverPlayer
	 */
	public void sendTeamerAllInfo(ServerPlayer serverPlayer){
		RoleTeamerDataListResponse roleTeamerDataListResponse =this.buildRoleTeamerDataListResponse(serverPlayer.getTeamModular());
		MessageSend.sendToGate(roleTeamerDataListResponse, serverPlayer);
	}
	
	/**
	 * 构建下发受邀请的人员
	 * BuildRoleOtherInviteMeRequest:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param otherRoleId
	 * @return
	 */
	public RoleOtherInviteMeResponse BuildRoleOtherInviteMeResponse(ServerPlayer serverPlayer){
		RoleOtherInviteMeResponse.Builder builder = RoleOtherInviteMeResponse.newBuilder();
		builder.setRoleInviteName(serverPlayer.getRole().getRoleName());
		builder.setRoleInviteId(serverPlayer.getRole().getId());
		builder.setRoleLevel(serverPlayer.getLevel());
		builder.setRoleCareerId(serverPlayer.getRole().getCareerConfigId());
		return builder.build();
	}
	
	
	/**
	 * 构建邀请组队结果
	 * buildRoleInviteOtherResponse:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param result
	 * @return
	 */
	public RoleInviteOtherResponse buildRoleInviteOtherResponse(int result,ServerPlayer serverPlayer){
		RoleInviteOtherResponse.Builder builder = RoleInviteOtherResponse.newBuilder();
		builder.setResult(result);
		builder.setRoleInviteName(serverPlayer.getName());
		return builder.build();
	}
	
	
	/**
	 * 构建小队成员列表信息
	 * buildRoleTeamerDataListResponse:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param teamModular
	 * @return
	 */
	public RoleTeamerDataListResponse buildRoleTeamerDataListResponse(TeamModular teamModular){
		RoleTeamerDataListResponse.Builder builder = RoleTeamerDataListResponse.newBuilder();
		if(teamModular!=null&&teamModular.getTeam()!=null){
			for (int i = 0; i < teamModular.getTeam().getObjects().size(); i++) {
				Teamer teamer=teamModular.getTeam().getObjects().get(i);
				builder.addTeamerListData(buildTeamerListData(teamer,teamModular.getTeam().getTeamId()));
            }
		}
		return builder.build();
	}
	
	/**
	 * 队员信息
	 * buildTeamerListData:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param teamer
	 * @return
	 */
	public TeamerListData buildTeamerListData(Teamer teamer,long teamId){
		TeamerListData.Builder builder=TeamerListData.newBuilder();
		builder.setTeamId(teamId);
		builder.setRoleId(teamer.getRoleId());
		builder.setRoleOrder(teamer.getOrder());
		//取出人员
		ServerPlayer sp=GameGlogalContainer.getRolesMap().get(teamer.getRoleId());
		if(sp!=null){
			builder.setRoleName(sp.getName());
			builder.setRoleLevel(sp.getLevel());
			builder.setRoleCareerId(sp.getRole().getCareerConfigId());
			builder.setRoleMapId(sp.getMp());
			builder.setRoleVIPLevelId(sp.getRole().getVipLevel());
			//还要加上战斗力
			
			//加上精 血
			builder.setHp(sp.getHp());
			builder.setMp(sp.getMp());
			builder.setMaxHp(sp.getMaxHp());
			builder.setMaxMp(sp.getMaxMp());
		}
		return builder.build();
	}
	
	
	
	/**
	 * 队员信息
	 * buildTeamerListData:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param teamer
	 * @return
	 */
	public TeamerListData buildTeamerListData(long roleId){
		TeamerListData.Builder builder=TeamerListData.newBuilder();
		builder.setRoleId(roleId);
		//取出人员
		ServerPlayer sp=GameGlogalContainer.getRolesMap().get(roleId);
		if(sp!=null){
			builder.setRoleName(sp.getName());
			builder.setRoleLevel(sp.getLevel());
			builder.setRoleCareerId(sp.getRole().getCareerConfigId());
			builder.setRoleMapId(sp.getMp());
			builder.setRoleVIPLevelId(sp.getRole().getVipLevel());
			//还要加上战斗力
		}
		return builder.build();
	}
	
	
	/**
	 * 构建申请加入小队列表
	 * buildRoleApplyInTeamListResponse:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param requestRoleList
	 * @return
	 */
	public RoleApplyInTeamListResponse buildRoleApplyInTeamListResponse(List<Long> requestRoleList){
		RoleApplyInTeamListResponse.Builder builder =RoleApplyInTeamListResponse.newBuilder();
		if(requestRoleList!=null){
			for (int i = 0; i < requestRoleList.size(); i++) {
				builder.addTeamerListData(buildTeamerListData(requestRoleList.get(i)));
            }
		}
		return builder.build();
	}
	
	/**
	 * 构建发送小队中有新人员进去
	 * buildRoleApplyInTeamResponse:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param roleId
	 * @param result
	 * @return
	 */
	public RoleApplyInTeamResponse buildRoleApplyInTeamResponse(long roleId,int result){
		RoleApplyInTeamResponse.Builder builder =RoleApplyInTeamResponse.newBuilder();
		builder.setTeamerListData(buildTeamerListData(roleId));
		builder.setResult(result);
		return builder.build();
	}
	
	/**
	 * 构建发送给全队人员的队长移交的消息
	 * buildRoleTransferInTeamResponse:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param spList
	 * @return
	 */
	public RoleTransferInTeamResponse buildRoleTransferInTeamResponse(List<ServerPlayer> spList){
		RoleTransferInTeamResponse.Builder buider=RoleTransferInTeamResponse.newBuilder();
		buider.setTransferRoleInviteId(spList.get(1).getRole().getId());
		return buider.build();
	}
	
	/**
	 * 解散小队消息
	 * buildRoleDismissTeamResponse:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param sp
	 * @return
	 */
	public RoleDismissTeamResponse buildRoleDismissTeamResponse(ServerPlayer sp){
		RoleDismissTeamResponse.Builder  buider = RoleDismissTeamResponse.newBuilder();
		buider.setResult(1);
		return buider.build();
	}
	
	/**
	 * 驱逐某个玩家结果消息
	 * buildRoleDriverOutTeamResponse:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param roleId
	 * @param result
	 * @return
	 */
	public RoleDriverOutTeamResponse buildRoleDriverOutTeamResponse(long roleId,int result){
		RoleDriverOutTeamResponse.Builder builder =RoleDriverOutTeamResponse.newBuilder();
		builder.setLauchDriverOutRoleId(roleId);
		builder.setResult(result);
		return builder.build();
	}


	/**
	 * 
	 *  optional int64 driverOutRoleId=1;//驱逐团队中某个角色id
    optional int32 roleLevel=2;//驱逐团队中某个角色等级
    optional int32 roleCareerId=3;//驱逐团队中某个角色职业编号
    optional string roleInviteName=4;//驱逐团队中某个角色名称
	 * 
	 * 
	 * buildRoleListTeamResponse:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @return
	 */
	public RoleListTeamResponse buildRoleListTeamResponse(long driverOutRoleId,int roleLevel,int roleCareerId,String roleInviteName){
		RoleListTeamResponse.Builder builder =RoleListTeamResponse.newBuilder();
		builder.setDriverOutRoleId(driverOutRoleId);
		builder.setRoleLevel(roleLevel);
		builder.setRoleCareerId(roleCareerId);
		builder.setRoleInviteName(roleInviteName);
		return builder.build();
	}
	
	public TeamConfigModel getTeamConfigModel() {
    	return teamConfigModel;
    }


	public void setTeamConfigModel(TeamConfigModel teamConfigModel) {
    	this.teamConfigModel = teamConfigModel;
    }
	
	
}
  