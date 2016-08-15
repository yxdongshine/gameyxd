
package com.lx.nserver.model;  

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.BuffPojo;
import com.read.txt.ResourceModelImpl;

/** 
 * ClassName:BuffModel <br/> 
 * Function: (Buff配置表). <br/> 
 * Date:     2015-9-1 上午11:07:07 <br/> 
 * @author   jack 
 * @version   
 * @see       
 */
@Component
public class BuffModel extends ResourceModelImpl<BuffPojo> {

	@Override
    public void printLog() {
	   log.info("Buff总表加载完成!!!" + this.getClass().getSimpleName());	    
    }
	
	public BuffModel()
	{
		super(TxtRes.BUFF_TXT, BuffPojo.class);
	}
}
  