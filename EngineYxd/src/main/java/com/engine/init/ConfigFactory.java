package com.engine.init;

import org.springframework.stereotype.Component;

import com.engine.properties.ServerGameConfig;

/**
 * ClassName:ConfigFactory <br/>
 * Function: TODO (文件配置管理 类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-6 下午2:24:39 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class ConfigFactory implements Initialize {
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		ServerGameConfig.loadGameConfig();// 加载游戏.propterties配置
	}
	
}
