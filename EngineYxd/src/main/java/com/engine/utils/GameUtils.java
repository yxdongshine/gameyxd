package com.engine.utils;

import java.util.ArrayList;
import java.util.List;

import com.engine.config.xml.map.SpaceInfo;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.space.Grid;
import com.engine.utils.astar.Point;

/**
 * ClassName:Utils <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-27 下午4:58:01 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class GameUtils {
	/**
	 * 以世界坐标系为准,求方向,0-360度
	 * 
	 * @param home
	 * @param target
	 * @return
	 */
	public static double getAngel(Position3D home, Position3D target) {
		float dx = target.getX() - home.getX();
		float dz = -(target.getZ() - home.getZ());// 因在地图上,z是上小下大的
		
		double dac = Math.atan2(dz, dx);
		double degree = (360 + dac * 180 / Math.PI) % 360;
		return degree;
	}
	
	/**
	 * 根据方向距离求出新坐标
	 * 
	 * @param home
	 * @param target
	 * @return
	 */
	public static Position3D getDirPoint(Position3D home, double degree, int distance) {
		double rdeg = degree * Math.PI / 180;
		float tox = (float) (home.getX() + distance * Math.cos(rdeg));
		float toz = (float) (home.getZ() - distance * Math.sin(rdeg));
		return new Position3D(tox, home.getY(), toz);
	}
	
	/**
	 * getLineGrids:(). <br/>
	 * TODO().<br/>
	 * 直线上的坐标
	 * 
	 * @author lyh
	 * @param from
	 * @param to
	 * @param info
	 * @return
	 */
	public static List<Position3D> getLineGrids(Position3D from, Position3D to, SpaceInfo info) {
		List<Position3D> grids = new ArrayList<Position3D>();
		
		int startX = info.getCol(from.getX());
		int startY = info.getRow(from.getZ());
		int endX = info.getCol(to.getX());
		int endY = info.getRow(to.getZ());
		
		grids.add(new Position3D(from));
		if (!(startX == endX && startY == endY)) {
			int y = endY - startY;
			int x = endX - startX;
			
			if (Math.abs(y) >= Math.abs(x)) {
				int step = 1;
				if (y < 0) {
					step = -1;
				}
				double per = (double) x / y;
				int posY = startY;
				for (int i = 0; i < Math.abs(y); i++) {
					posY = posY + step;
					int posX = startX + (int) Math.round(per * step * (i + 1));
					if (!info.isCanWalk(posX, 0, posY)) {
						grids.add(new Position3D(info.getGridX(posX), from.getY(), info.getGridZ(posY)));
					}
				}
			} else {
				int step = 1;
				if (x < 0) {
					step = -1;
				}
				double per = (double) y / x;
				int posX = startX;
				for (int i = 0; i < Math.abs(x); i++) {
					posX = posX + step;
					int posY = startY + (int) Math.round(per * step * (i + 1));
					if (!info.isCanWalk(posX, 0, posY)) {
						grids.add(new Position3D(info.getGridX(posX), from.getY(), info.getGridZ(posY)));
					}
				}
			}
		}
		return grids;
	}
	
	/**
	 * 得到射线上的点
	 * 
	 * @param sx
	 * @param sy
	 * @param ex
	 * @param ey
	 * @param size 作用的范围
	 * @return
	 */
	public static List<Point> getRaySyPoint(int sx, int sy, int ex, int ey, double size) {
		Point spot = new Point(sx, sy);
		Point tmpPoint;
		List<Point> pointList = new ArrayList<Point>();
		pointList.add(spot);
		int y, x, i = 1;
		int vy = ((ey - sy) < 0 ? -1 : 1);
		int vx = ((ex - sx) < 0 ? -1 : 1);
		double Δk = (ey - sy) / (1.0 * (ex - sx));
		if (Δk == Double.POSITIVE_INFINITY || Δk == Double.NEGATIVE_INFINITY) {
			for (int n = 1; n <= size; n++) {
				tmpPoint = new Point();
				tmpPoint.setLocation(sx, sy + vy * n);
				int dis = getDistance(spot, tmpPoint);
				if (dis >= size) {
					if (dis == size)
						pointList.add(tmpPoint);
					return pointList;
				}
				pointList.add(tmpPoint);
			}
			return pointList;
		}
		tmpPoint = new Point();
		while (true) {
			x = sx + vx * i;
			y = (int) (Δk * (x - sx)) + sy;
			tmpPoint.setLocation(pointList.get(pointList.size() - 1));
			Point tpot;
			int Δy = Math.abs(y - tmpPoint.y);
			if (Δy > 1) {
				for (int n = 1; n < Δy; n++) {
					tpot = new Point();
					tpot.setLocation(x, tmpPoint.y + vy * n);
					int dis = getDistance(spot, tpot);
					if (dis >= size) {
						if (dis == size)
							pointList.add(tmpPoint);
						return pointList;
					}
					pointList.add(tpot);
				}
			}
			tpot = new Point();
			tpot.setLocation(x, y);
			int dis = getDistance(spot, tpot);
			if (dis >= size) {
				if (dis == size)
					pointList.add(tmpPoint);
				break;
			}
			pointList.add(tpot);
			i++;
		}
		return pointList;
	}
	
	/**
	 * 得到射线上的点
	 * 
	 * @param sx
	 * @param sy
	 * @param ex
	 * @param ey
	 * @param size 作用的范围
	 * @return
	 */
	public static List<Point> getEyRayPoint(int sx, int sy, int ex, int ey, int size) {
		Point epot = new Point(ex, ey);
		Point tmpPoint;
		List<Point> pointList = new ArrayList<Point>();
		pointList.add(epot);
		int y, x, i = 1;
		int vy = ((ey - sy) < 0 ? -1 : 1);
		int vx = ((ex - sx) < 0 ? -1 : 1);
		double Δk = (ey - sy) / (1.0 * (ex - sx));
		if (Δk == Double.POSITIVE_INFINITY || Δk == Double.NEGATIVE_INFINITY) {
			for (int n = 1; n <= size; n++) {
				tmpPoint = new Point();
				tmpPoint.setLocation(ex, ey + vy * n);
				int dis = getDistance(tmpPoint, epot);
				if (dis > size)
					return pointList;
				pointList.add(tmpPoint);
				if (dis == size)
					return pointList;
			}
			return pointList;
		}
		tmpPoint = new Point();
		while (true) {
			x = ex + vx * i;
			y = (int) (Δk * (x - ex)) + ey;
			tmpPoint.setLocation(pointList.get(pointList.size() - 1));
			Point tpot;
			int Δy = Math.abs(y - tmpPoint.y);
			if (Δy > 1) {
				for (int n = 1; n <= Δy; n++) {
					tpot = new Point();
					tpot.setLocation(x, tmpPoint.y + vy * n);
					int dis = getDistance(tpot, epot);
					if (dis > size)
						return pointList;
					pointList.add(tpot);
					if (dis == size)
						return pointList;
				}
			}
			tpot = new Point();
			tpot.setLocation(x, y);
			int dis = getDistance(tpot, epot);
			if (dis > size)
				break;
			pointList.add(tpot);
			if (dis == size)
				break;
			i++;
		}
		return pointList;
	}
	
	/**
	 * 得到两点间的距离，取两点间x或y的最大值
	 * 
	 * @param sx
	 * @param sy
	 * @param ex
	 * @param ey
	 * @return
	 */
	public static int getDistance(int sx, int sy, int ex, int ey) {
		int Δx = Math.abs(sx - ex);
		int Δy = Math.abs(sy - ey);
		if (Δx >= Δy) {
			return Δx;
		} else {
			return Δy;
		}
	}
	
	/**
	 * 击退效果最终坐标点
	 * 
	 * @param fighter
	 * @param target
	 * @param dis 击退格子数目
	 * @return
	 */
	public static Position3D findPoint(Position3D fighter, Position3D target, int dis, SpaceInfo info) {
		List<Point> pointList = getEyRayPoint(info.getCol(fighter.getX()), info.getRow(fighter.getZ()), info.getCol(target.getX()), info.getRow(target.getZ()), dis);
		// IScene scene = target.getScene();
		int size = pointList.size();
		if (size < 1) {
			return new Position3D(target);
		}
		for (int i = 1; i <= pointList.size() - 1; i++) {
			Point point = pointList.get(i);
			if (!info.isCanWalk(point.x, 0, point.y)) {
				if (i == 1) {
					return new Position3D(target);
				}
				Point p = pointList.get(i - 1);
				return new Position3D(info.getGridX(p.getX()), target.getY(), info.getGridZ(p.getY()));
			}
		}
		Point p = pointList.get(size - 1);
		return new Position3D(info.getGridX(p.getX()), target.getY(), info.getGridZ(p.getY()));
	}
	
	/**
	 * 冲锋
	 * 
	 * @param fighter
	 * @param target
	 * @param dis 击退格子数目
	 * @return
	 */
	public static Position3D findPoint1(Position3D fighter, Position3D target, int dis, SpaceInfo info) {
		List<Point> pointList = getRaySyPoint(info.getCol(fighter.getX()), info.getRow(fighter.getZ()), info.getCol(target.getX()), info.getRow(target.getZ()), dis);
	
		int size = pointList.size();
		if (size < 1) {
			return new Position3D(fighter);
		}
		for (int i = 0; i < size; i++) {
			Point point = pointList.get(i);
			if (!info.isCanWalk(point.x, 0, point.y)) {
				if (i == 0)
					return  new Position3D(fighter);
				Point p = pointList.get(i - 1);
				return new Position3D(info.getGridX(p.getX()), target.getY(), info.getGridZ(p.getY()));
			}
		}
		Point p = pointList.get(size - 1);
		return new Position3D(info.getGridX(p.getX()), target.getY(), info.getGridZ(p.getY()));
	}
	
	// /**
	// * 击飞效果最终坐标点
	// *
	// * @param fighter
	// * @param target
	// * @param dis 击飞格子数目
	// * @return
	// */
	// public static Position3D findFlyPoint(Position3D fighter, Position3D target, int dis,SpaceInfo info) {
	// List<Point> pointList = getEyRayPoint(info.getCol(fighter.getX()), info.getRow(fighter.getZ()), info.getCol(target.getX()), info.getRow(target.getZ()), dis);
	// IScene scene = target.getScene();
	// if (pointList.size() <= 1) {
	// return target.getPoint();
	// }
	// for (int i = pointList.size() - 1; i >= 0; i--) {
	// Point point = pointList.get(i);
	// if (!scene.isStand(target, point)) {
	// continue;
	// }
	// return point;
	// }
	// return null;
	// }
	
	/**
	 * 得到两点间的距离，取两点间x或y的最大值
	 * 
	 * @param spoint 起始点
	 * @param epoint 终止点
	 * @return
	 */
	public static int getDistance(Point spoint, Point epoint) {
		return getDistance(spoint.x, spoint.y, epoint.x, epoint.y);
	}
}
