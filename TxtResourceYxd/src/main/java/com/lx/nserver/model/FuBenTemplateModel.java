package com.lx.nserver.model;

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.FuBenTemplatePojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:FuBenTemplateModel <br/>
 * Function: (副本配置表模型). <br/>
 * Date: 2015-8-19 下午2:25:13 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
public class FuBenTemplateModel extends ResourceModelImpl<FuBenTemplatePojo> {
	
	public FuBenTemplateModel() {
		super(TxtRes.FUBEN_TEMPLATE_TXT, FuBenTemplatePojo.class);
	}
	
	@Override
	public void printLog() {
		log.info("加载副本配置表完成！" + this.getClass().getSimpleName());
	}
	
}
