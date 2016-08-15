
package com.lx.nserver.model;  

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.BuffTypePojo;
import com.read.txt.ResourceModelImpl;

/** 
 * ClassName:BuffTypeModel <br/> 
 * Function: (buff类别模型). <br/> 
 * Date:     2015-9-1 上午11:00:48 <br/> 
 * @author   jack 
 * @version   
 * @see       
 */
@Component
public class BuffTypeModel extends ResourceModelImpl<BuffTypePojo> {

	@Override
    public void printLog() {
		log.info("Buff类别加载完成！！！" + this.getClass().getSimpleName());
    }
	
	public BuffTypeModel()
	{
		super(TxtRes.BUFF_TYPE_TXT, BuffTypePojo.class);
	}
	
}
  