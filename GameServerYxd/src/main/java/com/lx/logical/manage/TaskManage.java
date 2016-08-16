package com.lx.logical.manage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.dbdao.EntityDAO;
import com.engine.domain.Role;
import com.engine.domain.TaskData;
import com.engine.entityobj.ServerPlayer;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.game.task.Task.CanAcceptTaskData;
import com.loncent.protocol.game.task.Task.OpenRoleTaskResponse;
import com.loncent.protocol.game.task.Task.RoleAcceptTaskResponse;
import com.loncent.protocol.game.task.Task.RoleCanAcceptTaskResponse;
import com.loncent.protocol.game.task.Task.RoleHandTaskResponse;
import com.loncent.protocol.stauscode.StatusCode.StatusCodeResponse;
import com.lx.game.item.ItemManage;
import com.lx.game.item.PackItem;
import com.lx.game.item.util.MathUtil;
import com.lx.game.res.item.Item;
import com.lx.game.send.MessageSend;
import com.lx.game.task.TaskModular;
import com.lx.game.task.TaskStaticDataConfig;
import com.lx.nserver.model.TaskConfigModel;
import com.lx.nserver.model.TaskModel;
import com.lx.nserver.txt.TaskPojo;

/**
 * ClassName:TaskManage <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-4 下午3:14:08 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class TaskManage {
	private static final Log log = LogFactory.getLog(TaskManage.class);
	@Autowired
	private TaskConfigModel taskConfigModel;
	@Autowired
	private TaskModel taskModel;
	@Autowired
	private EntityDAO dao;
	@Autowired
	private ItemManage itemManage;
	@Autowired
	private ItemLogicManage itemLogicManage;
	// 玩家最大可接的任务数量
	private static byte taskMaxNumberConfig;
	
	public void loadTaskData(ServerPlayer sp) {
		taskMaxNumberConfig = taskConfigModel.getNumber();
		// 取出没有完成的任务
		List<TaskData> TaskDataList = dao.findByProperties(TaskData.class, new String[] { "roleId" }, new Object[] { sp.getRole().getId() });
		if (sp.getTaskModular() == null) {
			sp.setTaskModular(new TaskModular());
		}
		/* if(sp.getRole().getMainTaskIndex()<=0){ sp.getTaskModular().setMainTaskIndex(taskConfigModel.getInitTaskId()); TaskPojo
		 * taskPojo=taskModel.getTaskPojoByTaskId(taskConfigModel.getInitTaskId()); //初始化主任务 TaskData taskData= this.buildTaskData(taskPojo, sp.getRole());
		 * sp.getTaskModular().getTaskHashmap().put(taskPojo.getId(), taskData);
		 * 
		 * }else{ */
		sp.getTaskModular().setMainTaskIndex(sp.getRole().getMainTaskIndex());
		// }
		
		if (TaskDataList != null && TaskDataList.size() <= taskMaxNumberConfig) {
			for (int i = 0; i < TaskDataList.size(); i++) {
				TaskData taskData = TaskDataList.get(i);
				sp.getTaskModular().getTaskHashmap().put((int) taskData.getId(), taskData);
				
			}
		}
		
	}
	
	/**
	 * 
	 * saveTaskData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param sp
	 */
	public void saveTaskData(ServerPlayer sp) {
		for (Iterator iterator = sp.getTaskModular().getTaskHashmap().values().iterator(); iterator.hasNext();) {
			TaskData taskData = (TaskData) iterator.next();
			dao.save(taskData);
		}
		
	}
	
	/**
	 * 添加新任务 addNewTask:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param sp
	 * @return
	 */
	public List<TaskPojo> addNewTask(ServerPlayer sp) {
		List<TaskPojo> returnTaskPojoList = new ArrayList<TaskPojo>();
		if (sp.getTaskModular().getTaskHashmap().size() <= taskMaxNumberConfig) {// 遍历可以做的其他的任务
			List<TaskPojo> taskPojoList = lookForCanTask(sp);
			if (taskPojoList != null && taskPojoList.size() > 0) {
				for (int i = 0; i < taskPojoList.size(); i++) {
					TaskPojo taskPojo = taskPojoList.get(i);
					if (sp.getTaskModular().getTaskHashmap().containsKey(taskPojo.getId())) {
						continue;
					} else {
						// sp.getTaskModular().getTaskHashmap().put(taskPojo.getId(), this.buildTaskData(taskPojo, sp.getRole()));
						returnTaskPojoList.add(taskPojo);
					}
				}
			}
		}
		return returnTaskPojoList;
	}
	
	/**
	 * 删除任务 delTask:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskId
	 */
	public void delTask(ServerPlayer sp, int taskId) {
		// 删除内存列表任务
		TaskData taskData = sp.getTaskModular().getTaskHashmap().remove(taskId);
		// 删除数据库
		dao.delete(taskData);
	}
	
	/**
	 * 遍历能看见或者做的任务 lookForCanTask:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param mainTaskIndex
	 * @return
	 */
	public List<TaskPojo> lookForCanTask(ServerPlayer sp) {
		List<TaskPojo> taskPojoList = new ArrayList<TaskPojo>();
		for (Iterator iterator = taskModel.getTaskPojoHM().values().iterator(); iterator.hasNext();) {
			TaskPojo taskPojo = (TaskPojo) iterator.next();
			if (taskPojo != null) {
				if (isMeet(taskPojo, sp)) {
					taskPojoList.add(taskPojo);
				}
			}
		}
		return taskPojoList;
	}
	
	/**
	 * 
	 * isMeet:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskPojo
	 * @return
	 */
	public boolean isMeet(TaskPojo taskPojo, ServerPlayer sp) {
		boolean isMeet = false;
		if (taskPojo.getCanGiveUp() == 0) {// 不能申请放弃 为主线任务
			int mainTaskIndex = 0;
			if (sp.getTaskModular().getTaskHashmap().containsKey(sp.getRole().getMainTaskIndex())) {// 如果没有做完
				mainTaskIndex = sp.getRole().getMainTaskIndex();
			} else {
				mainTaskIndex = sp.getRole().getMainTaskIndex();
				mainTaskIndex++;
			}
			if (taskPojo.getLimitCareer() == sp.getRole().getCareerConfigId() && mainTaskIndex == taskPojo.getId()) {
				if (taskPojo.getTaskDispalyLevle() <= sp.getRole().getRoleLevel() || taskPojo.getTaskAcpteetLevle() >= sp.getRole().getRoleLevel()) {
					isMeet = true;
				}
			}
		} else if (taskPojo.getCanGiveUp() == 1 && taskPojo.getCanRepeat() == 0) {// 支线任务
			if (taskPojo.getTaskDispalyLevle() <= sp.getRole().getRoleLevel() || taskPojo.getTaskAcpteetLevle() >= sp.getRole().getRoleLevel()) {
				isMeet = true;
			}
		}
		
		return isMeet;
	}
	
	/**
	 * 构建一个TaskData buildTaskData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskPojo
	 * @param role
	 * @return
	 */
	public TaskData buildTaskData(TaskPojo taskPojo, Role role) {
		TaskData td = null;
		byte taskState = -1;
		if (taskPojo.getCanGiveUp() == 1) {// 主线任务
			if (role.getRoleLevel() >= taskPojo.getTaskDispalyLevle() && role.getRoleLevel() >= taskPojo.getTaskAcpteetLevle()) {
				taskState = TaskStaticDataConfig.TASK_STATE_ACCEPTED_NO_FINISH;
			} else if (role.getRoleLevel() >= taskPojo.getTaskDispalyLevle() && role.getRoleLevel() < taskPojo.getTaskAcpteetLevle()) {
				taskState = TaskStaticDataConfig.TASK_STATE_DIAPALY_NO_ACCEPT;
			}
			td = new TaskData(role.getId(), taskState, taskPojo.getId());
		}
		
		return td;
	}
	
	/**
	 * 接收一个任务 acceptTask:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskId
	 */
	public TaskData acceptTask(ServerPlayer sp, int taskId) {
		TaskData taskData = null;
		TaskPojo taskPojo = taskModel.getTaskPojoByTaskId(taskId);
		if (taskPojo != null) {
			String taskType = taskPojo.getTaskType();
			if (taskType != null && taskType.trim().length() > 0) {
				String[] taskTypeArr = taskType.split("\\*");
				if (taskTypeArr != null) {
					taskData = buildTaskData(taskPojo, sp.getRole());
					for (int i = 0; i < taskTypeArr.length; i++) {
						// 接收获取的道具
						if (this.getTaskTool(sp, taskPojo, TaskStaticDataConfig.FINISH_TASK_BALANCE_BEFROCE) == -1) {// 背包控件不足
							return null;
						}
						
						if (Integer.parseInt(taskTypeArr[i]) == TaskStaticDataConfig.TASK_TYPE_CONVERSATION) {// 如果是对话
							
							taskData.setNPCId(taskPojo.getFinishNPC());
							taskData.setTaskState(TaskStaticDataConfig.TASK_STATE_FINISHED_NO_HAND);
						} else {// 杀怪等其他任务
							taskData.setTaskState(TaskStaticDataConfig.TASK_STATE_ACCEPTED_NO_FINISH);
							//设置跟新数据值
							taskData=this.initFinishData(taskPojo, taskData);
						}
					}
					
					// 设置当前主任务编号
					sp.getTaskModular().setMainTaskIndex(taskId);
					// 添加到已经接的任务列表中
					sp.getTaskModular().getTaskHashmap().put(taskPojo.getId(), taskData);
				}
			}
		}
		return taskData;
	}
	
	/**
	 * 在第一个npc接收的道具 放入背包 getTaskTool:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param sp
	 * @param taskPojo
	 * 
	 * 
	 */
	public int getTaskTool(ServerPlayer sp, TaskPojo taskPojo, byte isAfter) {
		int result = 0;
		String propsIds = "";
		String propsNums = "";
		if (isAfter == TaskStaticDataConfig.FINISH_TASK_BALANCE_BEFROCE) {// 完成任务前
			propsIds = taskPojo.getGetProps();
			propsNums = taskPojo.getGetPropsNum();
		} else if (isAfter == TaskStaticDataConfig.FINISH_TASK_BALANCE_AFTER) {// 完成任务后
			propsIds = taskPojo.getRewardProps();
			propsNums = taskPojo.getRewardPropsCount();
		}
		if (propsIds != null && propsIds.trim().length() > 0 && propsNums != null && propsNums.trim().length() > 0) {
			String[] propsIdsArr = propsIds.split("\\*");
			String[] propsNumsArr = propsNums.split("\\*");
			if (propsIdsArr.length == propsNumsArr.length) {// 在正常情况下开始创建物品 并放入背包
				List<Item> itemAL = new ArrayList<Item>();
				Item item = null;
				for (int i = 0; i < propsIdsArr.length; i++) {
					item = itemManage.createItem(Integer.parseInt(propsIdsArr[i]));
					itemAL.add(item);
					// 判断背包能否装下
					byte isGridEnough = sp.getBag().getSubBags()[item.getProperty().getBagClass()].onlyCanAddBuyItem(Integer.parseInt(propsIdsArr[i]), Integer.parseInt(propsNumsArr[i]));
					if (isGridEnough == -1) {
						result = isGridEnough;
						return result;
					}
				}
				List<PackItem> resultLongAL = null;
				// 开始放入 放入背包
				for (int i = 0; i < propsIdsArr.length; i++) {
					resultLongAL = sp.getBag().putItemInBag(itemAL.get(i), Integer.parseInt(propsNumsArr[i]), sp.getRole().getId(), dao);
				}
				itemLogicManage.sendAddItemMessage(sp, resultLongAL);
			}
		}
		return result;
	}
	
	/**
	 * 收集材料 taskOfCollect:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param propertyId
	 * @param sp
	 */
	public int taskOfCollect(int propertyId, ServerPlayer sp) {
		int isTaskOfCollect=0;
		if (propertyId > 0 && sp != null) {
			if (sp.getTaskModular() != null) {
				List<TaskPojo> taskPojoList = getTaskPojoListById(propertyId, TaskStaticDataConfig.TASK_TYPE_COLLECT);
				if (taskPojoList != null && taskPojoList.size() > 0) {//
					for (int i = 0; i < taskPojoList.size(); i++) {
						TaskPojo taskPojo = taskPojoList.get(i);
						if (sp.getTaskModular().getTaskHashmap().containsKey(taskPojo.getId())) {// 有该任务
							isTaskOfCollect=1;//表示是任务道具
							// 根据propertyId创建道具 看是否能放入背包中
							Item item = itemManage.createItem(propertyId);
							// 判断背包能否装下
							byte isGridEnough = sp.getBag().getSubBags()[item.getProperty().getBagClass()].onlyCanAddBuyItem(propertyId, 1);
							if (isGridEnough >= 0) {// 能装下来
								// 装东西
								List<PackItem> resultLongAL = null;
								resultLongAL = sp.getBag().putItemInBag(item, 1, sp.getRole().getId(), dao);
								itemLogicManage.sendAddItemMessage(sp, resultLongAL);
								// 做一次该任务
								byte result = sp.getTaskModular().makeTask(taskPojo.getId(), taskPojo, 1, TaskStaticDataConfig.TASK_TYPE_COLLECT);
								if (result >= TaskStaticDataConfig.TASK_STATE_ACCEPTED_NO_FINISH && result <= TaskStaticDataConfig.TASK_STATE_FINISHED_NO_HAND) {
									// 发送该任务的具体的数据内容
									TaskData taskData = sp.getTaskModular().getTaskHashmap().get(taskPojo.getId());
									SendRoleAcceptTaskResponse(this.buildTaskData(taskData), sp);
									// 更新数据库
									dao.updateFinal(taskData);
								}
								
							} else {// 发送提示背包已满
								MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_E), sp);
							}
						}
					}
				}
			}
		} else {
			log.error("传入参数异常");
		}
		return isTaskOfCollect;
	}
	
	/**
	 * 
	 * 
	 * taskOfKillMoster:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param monsterId
	 * @param sp
	 */
	public void taskOfKillMonster(long monsterId, ServerPlayer sp) {
		if (monsterId > 0 && sp != null) {
			if (sp.getTaskModular() != null) {
				List<TaskPojo> taskPojoList = getTaskPojoListById(monsterId, TaskStaticDataConfig.TASK_TYPE_KILL_MOSTER);
				if (taskPojoList != null && taskPojoList.size() > 0) {//
					for (int i = 0; i < taskPojoList.size(); i++) {
						TaskPojo taskPojo = taskPojoList.get(i);
						if (sp.getTaskModular().getTaskHashmap().containsKey(taskPojo.getId())) {// 有该任务
							// 做一次该任务
							byte result = sp.getTaskModular().makeTask(taskPojo.getId(), taskPojo, 1, TaskStaticDataConfig.TASK_TYPE_KILL_MOSTER);
							if (result >= TaskStaticDataConfig.TASK_STATE_ACCEPTED_NO_FINISH && result <= TaskStaticDataConfig.TASK_STATE_FINISHED_NO_HAND) {
								// 发送该任务的具体的数据内容
								TaskData taskData = sp.getTaskModular().getTaskHashmap().get(taskPojo.getId());
								SendRoleAcceptTaskResponse(this.buildTaskData(taskData), sp);
								// 跟新数据库
								dao.updateFinal(taskData);
							}
						}
					}
				}
			}
		} else {
			log.error("传入参数异常");
		}
	}
	
	/**
	 * 提交任务 结算奖励 handTaskBalance:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskPojo
	 * @param sp
	 */
	public int handTaskBalance(int taskId, ServerPlayer sp) {
		TaskPojo taskPojo = taskModel.getTaskPojoByTaskId(taskId);
		int result = 0;
		// 先计算物品是否能放下
		result = this.getTaskTool(sp, taskPojo, TaskStaticDataConfig.FINISH_TASK_BALANCE_AFTER);
		if (result == -1) {// 表示装不下提交任务失败
			return result;
		}
		// 结算经验
		sp.addExp(taskPojo.getRewardEXPrewardCurr());
		// 结算金钱
		sp.addCurrency(taskPojo.getCurrencyType(), 1, taskPojo.getRewardCurrency());
		return result;
	}
	
	/**
	 * 
	 * getTaskPojoListById:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param id
	 * @param type
	 * @return
	 */
	public List<TaskPojo> getTaskPojoListById(long id, byte type) {
		List<TaskPojo> taskPojoList = new ArrayList<TaskPojo>();
		for (int i = 0; i < taskModel.getReslList().size(); i++) {
			TaskPojo taskPojo = taskModel.getReslList().get(i);
			if (type == TaskStaticDataConfig.TASK_TYPE_COLLECT) {// 采集
				// 该id为道具编号
				if (taskPojo != null) {
					if (taskPojo.getTargetProps() == id) {
						taskPojoList.add(taskPojo);
					}
				}
			} else if (type == TaskStaticDataConfig.TASK_TYPE_KILL_MOSTER) {// 杀怪
				// 该id为怪物编号
				if (taskPojo != null) {
					if (taskPojo.getTargetMonster() == id) {
						taskPojoList.add(taskPojo);
					}
				}
			}
		}
		return taskPojoList;
	}
	
	
	/**
	 * 
	 * initFinishData:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param taskPojo
	 * @param taskData
	 * @return
	 */
	public TaskData initFinishData(TaskPojo taskPojo,TaskData taskData){
		int[] taskTypeNumber = MathUtil.splitNumber(taskPojo.getTaskType().trim());
		//初始阿华
		String initFinishData="";
		for (int i = 0; i < taskTypeNumber.length; i++) {
			if (i == taskTypeNumber.length - 1) {// 最后一个
				initFinishData+="0";
            }else{
            	initFinishData+="0"+ "\\*";;
            }
        }
		
		taskData.setFinishNumber(initFinishData);
		
		return taskData;
	}
	
	
	/**
	 * 
	 * 
	 * 以下为封装消息协议
	 * 
	 * 
	 */
	
	/**
	 * 上线发送任务列表消息 buildOpenRoleTaskResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	public OpenRoleTaskResponse buildOpenRoleTaskResponse(ServerPlayer sp) {
		OpenRoleTaskResponse.Builder builder = OpenRoleTaskResponse.newBuilder();
		if (sp.getTaskModular() != null) {
			for (Iterator iterator = sp.getTaskModular().getTaskHashmap().values().iterator(); iterator.hasNext();) {
				TaskData taskData = (TaskData) iterator.next();
				builder.addTaskDataList(this.buildTaskData(taskData));
			}
		}
		return builder.build();
	}
	
	/**
	 * 构建可接的任务数据 buildCanAcceptTaskData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskPojo
	 * @return
	 */
	public CanAcceptTaskData buildCanAcceptTaskData(TaskPojo taskPojo) {
		CanAcceptTaskData.Builder builder = CanAcceptTaskData.newBuilder();
		builder.setTaskId(taskPojo.getId());
		return builder.build();
	}
	
	/**
	 * 构建可见任务列表 buildRoleCanAcceptTaskResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param sp
	 * @return
	 */
	public RoleCanAcceptTaskResponse buildRoleCanAcceptTaskResponse(ServerPlayer sp) {
		RoleCanAcceptTaskResponse.Builder builder = RoleCanAcceptTaskResponse.newBuilder();
		List<TaskPojo> taskPojoList = this.addNewTask(sp);
		if (taskPojoList != null && taskPojoList.size() > 0) {
			for (int i = 0; i < taskPojoList.size(); i++) {
				builder.addCanAcceptTaskData(this.buildCanAcceptTaskData(taskPojoList.get(i)));
			}
		}
		return builder.build();
	}
	
	/**
	 * 
	 * buildTaskData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskData
	 * @return
	 */
	public com.loncent.protocol.game.task.Task.TaskData buildTaskData(TaskData taskData) {
		com.loncent.protocol.game.task.Task.TaskData.Builder taskDataBuilder = com.loncent.protocol.game.task.Task.TaskData.newBuilder();
		taskDataBuilder.setTaskId((int) taskData.getId());
		taskDataBuilder.setTaskState(taskData.getTaskState());
		taskDataBuilder.setTaskNextNpc(taskData.getNPCId());
		if (taskData.getFinishNumber() != null && taskData.getFinishNumber().trim().length() > 0) {
			String[] finishstrs = taskData.getFinishNumber().trim().split("\\*");
			for (int i = 0; i < finishstrs.length; i++) {
				taskDataBuilder.addTaskFinishNumber(Integer.parseInt(finishstrs[i]));
			}
		}else{
			
		}
		return taskDataBuilder.build();
	}
	
	/**
	 * 
	 * buildRoleAcceptTaskResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskData
	 * @return
	 */
	public RoleAcceptTaskResponse buildRoleAcceptTaskResponse(com.loncent.protocol.game.task.Task.TaskData taskData) {
		RoleAcceptTaskResponse.Builder builder = RoleAcceptTaskResponse.newBuilder();
		builder.setTaskData(taskData);
		return builder.build();
	}
	
	/**
	 * 发送任务数据 SendRoleAcceptTaskResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskData
	 * @param sp
	 */
	public void SendRoleAcceptTaskResponse(com.loncent.protocol.game.task.Task.TaskData taskData, ServerPlayer sp) {
		MessageSend.sendToGate(buildRoleAcceptTaskResponse(taskData), sp);
	}
	
	/**
	 * 返回提交完成任务的返回值 buildRoleHandTaskResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskId
	 * @param taskState
	 * @return
	 */
	public RoleHandTaskResponse buildRoleHandTaskResponse(int taskId, byte taskState) {
		RoleHandTaskResponse.Builder builder = RoleHandTaskResponse.newBuilder();
		builder.setTaskId(taskId);
		builder.setTaskState(taskState);
		return builder.build();
	}
	
	/**
	 * sendPopUpTip:(). <br/>
	 * TODO().<br/>
	 * 发送弹出框信息
	 * 
	 * @author lyh
	 * @param net
	 * @param statusCodeId
	 */
	public static StatusCodeResponse createPopUpTip(int statusCodeId) {
		StatusCodeResponse scr = StatusCodeResponse.newBuilder().setPopstr(statusCodeId).build();
		return scr;
	}
}
