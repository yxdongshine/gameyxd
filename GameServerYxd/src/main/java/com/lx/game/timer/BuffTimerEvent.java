
package com.lx.game.timer;  

import com.engine.timer.TimerCallback;

/** 
 * ClassName:BuffTimerEvent <br/> 
 * Function: (buff定时器). <br/> 
 * Date:     2015-9-2 下午4:54:10 <br/> 
 * @author   jack 
 * @version   
 * @see       
 */
public class BuffTimerEvent extends TimerCallback{

	/** 
     * Creates a new instance of BuffTimerEvent. 
     * 
     * @param eventId
     * @param timer
     * @param count 
     */  
    public BuffTimerEvent(Object obj, String methodName, Object... args) {
    	super(obj, methodName, args);
    }
	
}
  