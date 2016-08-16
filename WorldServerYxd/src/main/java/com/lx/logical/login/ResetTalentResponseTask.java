package com.lx.logical.login;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.lib.utils.ServerRandomUtils;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.login.LoginGameServer.ResetTalentRequest;
import com.loncent.protocol.game.login.LoginGameServer.ResetTalentResponse;
import com.loncent.protocol.game.login.LoginGameServer.TalentData;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.nserver.model.CareerModel;
import com.lx.nserver.model.CareerTalentModel;
import com.lx.nserver.txt.CareerTalentPojo;
import com.lx.server.mina.session.IConnect;
import com.lx.world.container.WorldGlogalContainer;
import com.lx.world.data.CareerTalentData;
import com.lx.world.send.MessageSend;

/**
 * ClassName:ResetTalentResponseTask <br/>
 * Function: TODO (重置资质). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 下午3:06:25 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_RESET_TALENT_REQUEST_VALUE)
public class ResetTalentResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private CareerTalentModel careerTalentModel;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ResetTalentRequest req = ResetTalentRequest.parseFrom(msg.getBody());
		List<CareerTalentPojo> list = careerTalentModel.findCareerTalentListByCareerId(req.getCareerConfigId());
		if (list != null) {
			CareerTalentData data = new CareerTalentData();
			data.setCareerConfigId(req.getCareerConfigId());
			CareerTalentPojo ctPojo = null;
			if (list.size() == 1) {
				ctPojo = list.get(0);
			} else {// 根据里面的随机值得到对象
				int rate = calculateTotalRate(list);
				// 随机得到一个资质对象
				ctPojo = randomCareerTalent(rate, list);
			}
			
			if (ctPojo != null) {// 计算增加的资质点
				int num = 0;
				List<Integer> randomReduceList = new ArrayList<Integer>();
				
				if (ctPojo.getAddAir() > 0) {
					num += ctPojo.getAddAir();
					data.setAir(ctPojo.getAddAir());
				}
				if (ctPojo.getAddInnerForce() > 0) {
					num += ctPojo.getAddInnerForce();
					data.setInnerForce(ctPojo.getAddInnerForce());
				}
				if (ctPojo.getAddAgility() > 0) {
					num += ctPojo.getAddAgility();
					data.setAgility(ctPojo.getAddAgility());
				}
				if (ctPojo.getAddControl() > 0) {
					num += ctPojo.getAddControl();
					data.setControl(ctPojo.getAddControl());
				}
				if (ctPojo.getAddTenacity() > 0) {
					num += ctPojo.getAddTenacity();
					data.setTenacity(ctPojo.getAddTenacity());
				}
				// if (ctPojo.getAddDefence() > 0) {
				// num += ctPojo.getAddDefence();
				// data.setDefence(ctPojo.getAddDefence());
				// }
				
				// 减
				if (ctPojo.getReduceAir() >= 0) {
					num -= ctPojo.getReduceAir();
					data.setAir(data.getAir() - ctPojo.getReduceAir());
				} else {
					randomReduceList.add(1);
				}
				if (ctPojo.getReduceInnerForce() >= 0) {
					num -= ctPojo.getReduceInnerForce();
					data.setInnerForce(data.getInnerForce() - ctPojo.getReduceInnerForce());
				} else {
					randomReduceList.add(2);
				}
				if (ctPojo.getReduceAgility() >= 0) {
					num -= ctPojo.getReduceAgility();
					data.setAgility(data.getAgility() - ctPojo.getReduceAgility());
				} else {
					randomReduceList.add(3);
				}
				if (ctPojo.getReduceControl() >= 0) {
					num -= ctPojo.getReduceControl();
					data.setControl(data.getControl() - ctPojo.getReduceControl());
				} else {
					randomReduceList.add(4);
				}
				if (ctPojo.getReduceTenacity() >= 0) {
					num -= ctPojo.getReduceTenacity();
					data.setTenacity(data.getTenacity() - ctPojo.getReduceTenacity());
				} else {
					randomReduceList.add(5);
				}
				// if (ctPojo.getReduceDefence() >= 0) {
				// num -= ctPojo.getReduceDefence();
				// data.setDefence(data.getDefence() - ctPojo.getReduceDefence());
				// } else {
				// randomReduceList.add(6);
				// }
				
				if (randomReduceList.size() == 1) {// 只有一个
					int type = randomReduceList.get(0);
					setReduceValue(data, type, num);
				}
				// 有资质随机点
				if (num > 0 && randomReduceList.size() > 0) {
					for (int i = 0; i < randomReduceList.size(); i++) {
						if (num > 0) {
							if (num > 1) {
								int t = ServerRandomUtils.randomNum(1, num);
								num -= t;
								int type = randomReduceList.get(i);
								setReduceValue(data, type, t);
							} else {
								int type = randomReduceList.get(i);
								setReduceValue(data, type, num);
								num -= num;
							}
							
							if (i == randomReduceList.size() - 1 && num > 0) {
								int type = randomReduceList.get(i);
								setReduceValue(data, type, num);
							}
							
						} else {
							break;
						}
					}
				}
				
				// 放入全局容器
				WorldGlogalContainer.getCareertalentmap().put(msg.getClientSessionId(), data);
				this.sendResetTalentResponse(data, msg, ctPojo, session);
			}
		} else {
			log.error("没有找到职业资质");
		}
	}
	
	/**
	 * calculateTotalRate:(). <br/>
	 * TODO().<br/>
	 * 计算同一职业总的机率
	 * 
	 * @author lyh
	 * @param list
	 * @return
	 */
	private int calculateTotalRate(List<CareerTalentPojo> list) {
		int rate = 0;
		for (CareerTalentPojo pojo : list) {
			rate += pojo.getTalentRate();
		}
		return rate;
	}
	
	/**
	 * randomCareerTalent:(). <br/>
	 * TODO().<br/>
	 * 随机得到一个资质对象
	 * 
	 * @author lyh
	 * @param totalRate
	 * @param list
	 * @return
	 */
	private CareerTalentPojo randomCareerTalent(int totalRate, List<CareerTalentPojo> list) {
		int r = ServerRandomUtils.nextInt(totalRate) + 1;
		int tmp = 0;
		for (CareerTalentPojo pojo : list) {
			if (r > tmp && r <= tmp + pojo.getTalentRate()) {
				return pojo;
			} else {
				tmp += pojo.getTalentRate();
			}
		}
		
		return null;
	}
	
	/**
	 * setReduceValue:(). <br/>
	 * TODO().<br/>
	 * 设置减少的资质
	 * 
	 * @author lyh
	 * @param data
	 * @param type
	 */
	private void setReduceValue(CareerTalentData data, int type, int v) {
		switch (type) {
			case 1: // 气力
				data.setAir(data.getAir() - v);
				break;
			case 2: // 内力
				data.setInnerForce(data.getInnerForce() - v);
				break;
			case 3: // 敏捷
				data.setAgility(data.getAgility() - v);
				break;
			case 4: // 掌控
				data.setControl(data.getControl() - v);
				break;
			case 5: // 坚韧
				data.setTenacity(data.getTenacity() - v);
				break;
		// case 6:// 防御
		// data.setDefence(data.getDefence() - v);
		// break;
		}
	}
	
	// str力量、dex敏捷、int智力、ctr控制、def防御
	public void sendResetTalentResponse(CareerTalentData data, InnerMessage inner, CareerTalentPojo curPojo, IConnect con) {
		ResetTalentResponse.Builder builder = ResetTalentResponse.newBuilder();
		TalentData.Builder tData = TalentData.newBuilder();
		tData.setTalentName(curPojo.getTalentName());
		tData.setTalentCtrl(data.getControl());
		tData.setTalentDef(data.getTenacity());
		tData.setTalentDex(data.getAgility());
		tData.setTalentInt(data.getInnerForce());
		tData.setTalentStr(data.getAir());
		builder.setData(tData.build());
		MessageSend.sendToGate(builder.build(), con, inner.getClientSessionId(), inner.getGateTypeId());
	}
}
