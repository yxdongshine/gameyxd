
package com.lx.nserver.model;  

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.TeamConfigPojo;
import com.read.txt.ResourceModelImpl;

/** 
 * ClassName:TeamConfigModel <br/> 
 * Function: TODO (). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-1 上午9:59:03 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
public class TeamConfigModel extends ResourceModelImpl<TeamConfigPojo>{

	private static Log log = LogFactory.getLog(TeamConfigModel.class);
	@Override
    public void printLog() {
	    // TODO Auto-generated method stub
		log.error("组队配置加载完成");
    }
	
	public TeamConfigModel() {
	    // TODO Auto-generated constructor stub
		super(TxtRes.TEAM_CONFIG, TeamConfigPojo.class);
    }
}
  