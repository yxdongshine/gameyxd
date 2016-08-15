package com.lx.game.task;

import java.util.HashMap;
import com.engine.domain.TaskData;
import com.lx.nserver.txt.TaskPojo;

/**
 * ClassName:TaskModular <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-4 下午2:42:30 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class TaskModular {
	
	/**
	 * 存已经接但是没有完成的任务
	 */
	HashMap<Integer, TaskData> taskHashmap = new HashMap<Integer, TaskData>();
	// 当前主线任务编号
	int mainTaskIndex = 0;
	
	/**
	 * 
	 * makeTask:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param id
	 * @param taskPojo
	 * @param number
	 * @return
	 */
	public byte makeTask(int id, TaskPojo taskPojo, int number, byte type) {
		byte resultType = 1;
		int[] taskTypeNumber = splitNumber(taskPojo.getTaskType().trim());
		//如果第一次构建任务
		if(this.taskHashmap.get(id).getFinishNumber()==null||this.taskHashmap.get(id).getFinishNumber().equals("")){
			//初始阿华
			String initFinishData="";
			for (int i = 0; i < taskTypeNumber.length; i++) {
				if (i == taskTypeNumber.length - 1) {// 最后一个
					initFinishData+="0";
	            }else{
	            	initFinishData+="0"+ "\\*";;
	            }
            }
			this.taskHashmap.get(id).setFinishNumber(initFinishData);
		}
		int[] finishNumberOfData = splitNumber(this.taskHashmap.get(id).getFinishNumber());
		int index = -1;
		for (int i = 0; i < taskTypeNumber.length; i++) {
			if (type == taskTypeNumber[i]) {
				index = i;
				break;
			}
		}
		switch (type) {
			case TaskStaticDataConfig.TASK_TYPE_KILL_MOSTER:
				finishNumberOfData[index] += number;
				String finishStr = packArr(finishNumberOfData);
				this.taskHashmap.get(id).setFinishNumber(finishStr);
				// 设置状态
				this.taskHashmap.get(id).setTaskState(this.isFinishTask(taskPojo, type, finishNumberOfData[index]));
				break;
			case TaskStaticDataConfig.TASK_TYPE_COLLECT:
				finishNumberOfData[index] += number;
				String finishStr2 = packArr(finishNumberOfData);
				this.taskHashmap.get(id).setFinishNumber(finishStr2);
				// 设置状态
				this.taskHashmap.get(id).setTaskState(this.isFinishTask(taskPojo, type, finishNumberOfData[index]));
				break;
			default:
				break;
		}
		resultType = this.taskHashmap.get(id).getTaskState();
		return resultType;
	}
	
	/**
	 * 完成状态 isFinishTask:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param taskPojo
	 * @param type
	 * @param finishNumber
	 * @return
	 */
	public byte isFinishTask(TaskPojo taskPojo, byte type, int finishNumber) {
		byte resultType = 1;
		switch (type) {
			case TaskStaticDataConfig.TASK_TYPE_KILL_MOSTER:
				if (taskPojo.getTargetMonsterCount() > finishNumber) {// 没有完成 只是更新进度
					resultType = TaskStaticDataConfig.TASK_STATE_ACCEPTED_NO_FINISH;
				} else {// 完成任务 没有提交
					resultType = TaskStaticDataConfig.TASK_STATE_FINISHED_NO_HAND;
				}
				break;
			case TaskStaticDataConfig.TASK_TYPE_COLLECT:
				if (taskPojo.getTargetPropsCount() > finishNumber) {// 没有完成 只是更新进度
					resultType = TaskStaticDataConfig.TASK_STATE_ACCEPTED_NO_FINISH;
				} else {// 完成任务 没有提交
					resultType = TaskStaticDataConfig.TASK_STATE_FINISHED_NO_HAND;
				}
				break;
			default:
				break;
		}
		return resultType;
	}
	
	/**
	 * 拆分字符串 splitNumber:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param str
	 * @return
	 */
	public int[] splitNumber(String str) {
		String[] subStrArr = str.trim().split("\\*");
		int[] strArr = new int[subStrArr.length];
		for (int i = 0; i < subStrArr.length; i++) {
			strArr[i] = Integer.parseInt(subStrArr[i].trim());
		}
		return strArr;
	}
	
	/**
	 * 
	 * packArr:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param finishNumberOfData
	 * @return
	 */
	public String packArr(int[] finishNumberOfData) {
		String str = "";
		for (int i = 0; i < finishNumberOfData.length; i++) {
			if (i == finishNumberOfData.length - 1) {// 最后一个
				str += finishNumberOfData[i];
			} else {
				str += finishNumberOfData[i] + "\\*";
			}
			
		}
		return str;
	}
	
	public HashMap<Integer, TaskData> getTaskHashmap() {
		return taskHashmap;
	}
	
	public void setTaskHashmap(HashMap<Integer, TaskData> taskHashmap) {
		this.taskHashmap = taskHashmap;
	}
	
	public int getMainTaskIndex() {
		return mainTaskIndex;
	}
	
	public void setMainTaskIndex(int mainTaskIndex) {
		this.mainTaskIndex = mainTaskIndex;
	}
	
}
