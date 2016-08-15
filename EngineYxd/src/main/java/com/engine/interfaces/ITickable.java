package com.engine.interfaces;

/**
 * ClassName:ITickable <br/>
 * Function: (可更新对象公共接口). <br/>
 * Date: 2015-7-14 上午10:56:10 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
public interface ITickable {
	
	/**
	 * 更新 tick:(). <br/>
	 */
	public void tick(long time);
}
