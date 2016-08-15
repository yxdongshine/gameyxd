/** 
 * Project Name:DragonBallWorldServerHappy 
 * File Name:Head.java 
 * Package Name:com.sj.game.msgloader 
 * Date:2013-8-6下午2:20:48 
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.engine.msgloader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:Head <br/>
 * Function: TODO (消息头使用自动注解). <br/>
 * Reason: TODO (). <br/>
 * Date: 2013-8-6 下午2:20:48 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Head {
	abstract int value();
}
