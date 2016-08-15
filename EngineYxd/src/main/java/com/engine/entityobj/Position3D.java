package com.engine.entityobj;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.engine.httpserver.GameHttpServer;

/**
 * ClassName:Position3D <br/>
 * Function: (空间位置坐标3D). <br/>
 * Reason: (). <br/>
 * Date: 2015-7-13 上午10:55:38 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
public class Position3D implements Cloneable {
	
	private Log log = LogFactory.getLog(GameHttpServer.class);
	
	/* x坐标 */
	private float x;
	
	/* y坐标 */
	private float y;
	
	/* z坐标 */
	private float z;
	
	public Position3D() {
	}
	
	public Position3D(Position3D p) {
		x = p.x;
		y = p.y;
		z = p.z;
	}
	
	public Position3D(float x, float y, float z) {
		setXYZ(x, y, z);
	}
	
	/**
	 * 设置坐标 setPoint:(). <br/>
	 * ().<br/>
	 */
	public void setPoint(Position3D point) {
		if (point != null) {
			setXYZ(point.x, point.y, point.z);
		} else {
			setXYZ(0, 0, 0);
		}
	}
	
	/**
	 * 
	 * setXYZ:(设置坐标xyz值). <br/>
	 * ().<br/>
	 */
	public void setXYZ(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * 
	 * isInRange:(判断p是否在当前点为圆心radius为半径的园内). <br/>
	 * ().<br/>
	 */
	public boolean isInRange(Position3D p, int radius) {
		return (int) p.x <= (int) x + radius && (int) p.x >= (int) x - radius && (int) p.y <= (int) y + radius && (int) p.y >= (int) y - radius && (int) p.z <= (int) z + radius && (int) p.z >= (int) z - radius;
	}
	
	/**
	 * 
	 * getAbsDistance:(获取2点间的绝对距离). <br/>
	 * ().<br/>
	 */
	public double getAbsDistance(Position3D p) {
		return Math.abs(Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2) + Math.pow(this.z - p.z, 2)));
	}
	 
	/** 
	 * getXZDistance:(). <br/> 
	 * TODO().<br/> 
	 * 得到XZ距离
	 * @author lyh 
	 * @param p
	 * @return 
	 */  
	public double getXZDistance(Position3D p){
		return Math.abs(Math.sqrt(Math.pow(this.x - p.x, 2)+ Math.pow(this.z - p.z, 2)));
	}
	
	/**
	 * 
	 * randomIn2Point:(在两点之间随机一点). <br/>
	 * ().<br/>
	 */
	public static Position3D randomIn2Point(Position3D min, Position3D max) {
		Random random = new Random();
		float nx = max.x - min.x;
		if (nx > 0) {
			nx = random.nextFloat() * nx;
		}
		nx += min.x;
		
		float ny = max.y - min.y;
		if (ny > 0) {
			ny = random.nextFloat() * ny;
		}
		ny += min.y;
		
		float nz = max.z - min.z;
		if (nz > 0) {
			nz = random.nextFloat() * nz;
		}
		nz += min.z;
		
		Position3D np = new Position3D();
		np.setXYZ(nx, ny, nz);
		return np;
	}
	
	/**
	 * 
	 * getMidPoint:(获取中间点). <br/>
	 * ().<br/>
	 */
	public static Position3D getMidPoint(Position3D minpoint, Position3D maxpoint, Position3D midpoint) {
		midpoint.x = (minpoint.x + (maxpoint.x - minpoint.x) / 2);
		midpoint.y = (minpoint.y + (maxpoint.y - minpoint.y) / 2);
		midpoint.z = (minpoint.z + (maxpoint.z - minpoint.z) / 2);
		return midpoint;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append((int) x).append(",").append((int) y).append(",").append((int) z).toString();
	}
	
	/**
	 * 
	 * clonePoint:(克隆坐标). <br/>
	 * ().<br/>
	 */
	public Position3D clonePoint() {
		
		try {
			return (Position3D) super.clone();
		} catch (CloneNotSupportedException ex) {
			log.error("point clone error:", ex);
		}
		return null;
	}
	
	/**
	 * equal:(). <br/>
	 * TODO().<br/>
	 * 坐标是否相等
	 * 
	 * @author lyh
	 * @param pos3d
	 * @return
	 */
	public boolean equal(Position3D pos3d) {
		return (x == pos3d.x && y == pos3d.y && z == pos3d.z);
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
}
