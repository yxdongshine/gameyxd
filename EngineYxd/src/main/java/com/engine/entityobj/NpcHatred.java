package com.engine.entityobj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.engine.entityobj.space.AbsSpace;
import com.engine.entityobj.space.IMapObject;

/**
 * ClassName:NpcHatred <br/>
 * Function: TODO (npc仇恨对象). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-6 下午7:23:10 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class NpcHatred {
	
	protected AbsMonster monster;
	
	/** 第一个打的目标 */
	protected long firstHit;
	
	/** 仇恨列表 key: 目标对象编号 Hatred:仇恨对象 */
	protected Map<Long, Hatred> hatredMap;
	
	/** 仇恨排序列表 */
	protected List<Hatred> hatredSortList;
	
	/** 仇恨排序列表长度 */
	protected int SORT_LENGTH = 10;
	
	/** 刷新仇恨列表等待时间 */
	protected long waitTime;
	
	/** 仇恨默认刷新延时 */
	protected static long DEFUALT_TIME = 150;
	
	public NpcHatred(AbsMonster npc) {
		this.monster = npc;
		this.hatredMap = new HashMap<Long, Hatred>();
		this.hatredSortList = new ArrayList<Hatred>();
		for (int i = 0; i < SORT_LENGTH; i++)
			this.hatredSortList.add(null);
	}
	
	/**
	 * 将仇恨复制给某个NPC
	 * 
	 * @param footman
	 */
	public void copy(AbsMonster footman, long time) {
		if (this.hatredMap.size() == 0)
			return;
		AbsSpace scene = monster.getSpace();
		if (scene == null)
			return;
		List<Hatred> tmpHdSort = new ArrayList<Hatred>();
		Map<Long, Hatred> tmpMap = new HashMap<Long, Hatred>();
		for (Iterator<Entry<Long, Hatred>> iterator = hatredMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<Long, Hatred> entry = iterator.next();
			if (entry.getValue() == null)
				continue;
			Hatred h = entry.getValue().clone();
			tmpMap.put(entry.getKey(), h);
			tmpHdSort.add(h);
		}
		if (tmpHdSort.size() < SORT_LENGTH) {
			int size = tmpHdSort.size();
			for (int i = size; i < SORT_LENGTH; i++)
				tmpHdSort.add(null);
		}
		// footman.getNpcHatred().copy(this.firstHit, tmpMap, tmpHdSort, time);
	}
	
	/**
	 * 添加或增加仇恨值
	 * 
	 * @param fighter
	 * @param hatred
	 * @param isAtk
	 */
	public void addHatred(IMapObject fighter, int hatred, boolean isAtk) {
		if (hatred < 0)
			return;
		Hatred hd = this.hatredMap.get(fighter.getId());
		if (hd == null) {
			hd = new Hatred();
			hd.fighterId = fighter.getId();
			hd.value = hatred;
			hd.name = fighter.getName();
			this.hatredMap.put(fighter.getId(), hd);
			this.join(hd);
		} else {
			hd.value += hatred;
			this.update();
		}
		if (isAtk && this.firstHit == 0)
			this.firstHit = fighter.getId();
	}
	
	/**
	 * 减少仇恨值
	 * 
	 * @param fighter
	 * @param hatred
	 */
	public void deductHatred(IMapObject fighter, int hatred) {
		if (hatred < 0)
			return;
		Hatred hd = hatredMap.get(fighter.getId());
		if (hd == null)
			return;
		if (hd.value > hatred)
			hd.value -= hatred;
		else
			hd.value = 0;
		this.update();
	}
	
	/**
	 * 将一个对象的仇恨值清零
	 * 
	 * @param fighter
	 */
	public void clearZeroHatred(IMapObject fighter, boolean isWarlike) {
		Hatred hd = hatredMap.get(fighter.getId());
		if (hd == null)
			return;
		if (this.firstHit != 0 && this.firstHit == hd.fighterId)
			this.firstHit = 0;
		hd.value = 0;
		for (int i = 0; i < this.hatredSortList.size(); i++) {
			Hatred hatred = this.hatredSortList.get(i);
			if (hatred == null)
				continue;
			if (hatred.fighterId != fighter.getId())
				continue;
			if (!isWarlike) {
				this.hatredMap.remove(fighter.getId());
				this.hatredSortList.set(i, null);
			}
			break;
		}
		this.update();
	}
	
	/**
	 * 清理整个仇恨
	 * 
	 */
	public void clearHatred() {
		this.hatredMap.clear();
		this.firstHit = 0;
		for (int i = 0; i < SORT_LENGTH; i++)
			this.hatredSortList.set(i, null);
	}
	
	/**
	 * 移除某个对象的仇恨
	 * 
	 * @param fighter
	 */
	public void removeHatred(IMapObject fighter) {
		if (this.firstHit == fighter.getId())
			this.firstHit = 0;
		Hatred hatred = this.hatredMap.remove(fighter.getId());
		if (hatred == null)
			return;
		for (int i = 0; i < this.hatredSortList.size(); i++) {
			Hatred h = this.hatredSortList.get(i);
			if (h == null)
				continue;
			if (h.fighterId != fighter.getId())
				continue;
			this.hatredSortList.set(i, null);
			break;
		}
		this.update();
	}
	
	/**
	 * 获取首次攻击者
	 * 
	 * @return
	 */
	public IMapObject getFirstHit() {
		return monster.getSpace().getMapObjects().get(this.firstHit);
	}
	
	/**
	 * 获取最大仇恨值的对象
	 * 
	 * @param isWarlike
	 * @return
	 */
	public IMapObject getMaxHatred(boolean isWarlike) {
		Hatred hatred = this.hatredSortList.get(0);
		if (hatred == null)
			return null;
		if (hatred.value == 0 && !isWarlike)
			return null;
		IMapObject targert = monster.getSpace().getMapObjects().get(hatred.fighterId);
		if (targert == null)
			return null;
		if (((IFighter) targert).isDie())
			return null;
		return targert;
	}
	
	/**
	 * 获取仇恨列表中排行前十的某一个对象
	 * 
	 * @param index
	 * @return
	 */
	public IMapObject getIndexHatred(int index) {
		if (index >= SORT_LENGTH)
			return null;
		Hatred hatred = this.hatredSortList.get(index);
		if (hatred == null)
			return null;
		IMapObject targert = monster.getSpace().getMapObjects().get(hatred.fighterId);
		if (targert == null)
			return null;
		if (((IFighter) targert).isDie())
			return null;
		return targert;
	}
	
	/**
	 * 刷新NPC的仇恨值
	 * 
	 * @param time
	 */
	public void rushHatred(long time, boolean isWarlike) {
		if (!this.isHatred(isWarlike))
			return;
		if (this.waitTime == 0)
			this.waitTime = time + DEFUALT_TIME;
		if (this.waitTime > time)
			return;
		if (this.hatredMap.size() > 0) {
			Iterator<Entry<Long, Hatred>> iterator = this.hatredMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Long, Hatred> entry = iterator.next();
				if (entry == null)
					continue;
				if (entry.getValue() == null)
					continue;
				Hatred hatred = entry.getValue();
				IMapObject fighter = monster.getSpace().getMapObjects().get(hatred.fighterId);
				if (fighter == null /* || fighter.isOver()* */) {
					iterator.remove();
					if (this.firstHit != 0 && this.firstHit == hatred.fighterId)
						this.firstHit = 0;
					for (int i = 0; i < this.hatredSortList.size(); i++) {
						Hatred h = this.hatredSortList.get(i);
						if (h == null)
							continue;
						if (fighter == null)
							continue;
						if (h.fighterId != fighter.getId())
							continue;
						this.hatredSortList.set(i, null);
						break;
					}
					this.update();
				} else {
					if (((IFighter) fighter).isDie() && hatred.value != 0) {
						hatred.value = 0;
						this.update();
					}
				}
			}
		}
		this.waitTime = time + DEFUALT_TIME;
	}
	
	/**
	 * 获取仇恨列表
	 * 
	 * @return
	 */
	public List<IMapObject> getHatredList() {
		List<IMapObject> list = new ArrayList<IMapObject>();
		Iterator<Entry<Long, Hatred>> iterator = this.hatredMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Long, Hatred> entry = iterator.next();
			if (entry == null)
				continue;
			if (entry.getValue() == null)
				continue;
			IMapObject fighter = monster.getSpace().getMapObjects().get(entry.getValue().fighterId);
			if (fighter == null)
				continue;
			if (((IFighter) fighter).isDie())
				continue;
			list.add(fighter);
		}
		return list;
	}
	
	/**
	 * 返回是否有仇恨
	 * 
	 * @param fighter
	 * @return
	 */
	public boolean isHatred(boolean isWarlike) {
		if (isWarlike && this.hatredMap.size() > 0)
			return true;
		int hatred = 0;
		for (Iterator<Entry<Long, Hatred>> iterator = this.hatredMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<Long, Hatred> entry = iterator.next();
			if (entry == null)
				continue;
			if (entry.getValue() == null)
				continue;
			hatred += entry.getValue().value;
		}
		if (hatred > 0)
			return true;
		return false;
	}
	
	/**
	 * 返回Npc与战斗对象之间是否有仇恨
	 * 
	 * @param fighter
	 * @return
	 */
	public boolean isHatred(IMapObject fighter) {
		Hatred value = this.hatredMap.get(fighter.getId());
		if (value == null)
			return false;
		return true;
	}
	
	/**
	 * 复制仇恨
	 * 
	 * @param firstHit
	 * @param hatredMap
	 * @param hatredSortList
	 */
	private void copy(int firstHit, Map<Long, Hatred> hatredMap, List<Hatred> hatredSortList, long time) {
		this.firstHit = firstHit;
		this.hatredMap = hatredMap;
		this.hatredSortList = hatredSortList;
		this.update();
		// this.npc.getNpcAtkAction().attack(time);
	}
	
	/**
	 * 更新排序
	 * 
	 * @param key
	 */
	public void update() {
		int size = this.hatredSortList.size();
		for (int i = 0; i < size; i++) {
			for (int j = size - 1; j > i; j--) {
				if (this.hatredSortList.get(i) != null && this.hatredSortList.get(j) != null) {
					if (this.hatredSortList.get(i).value < this.hatredSortList.get(j).value) {
						Hatred hatredA = this.hatredSortList.get(i);
						Hatred hatredB = this.hatredSortList.get(j);
						this.hatredSortList.set(i, hatredB);
						this.hatredSortList.set(j, hatredA);
					}
				}
				if (this.hatredSortList.get(i) == null && this.hatredSortList.get(j) != null) {
					Hatred hatred = this.hatredSortList.remove(j);
					this.hatredSortList.set(i, hatred);
					this.hatredSortList.add(null);
				}
			}
		}
	}
	
	/**
	 * 加入排序
	 * 
	 * @param key
	 * @param hatred
	 */
	public void join(Hatred hatred) {
		for (int i = 0; i < this.hatredSortList.size(); i++) {
			if (this.hatredSortList.get(i) == null) {
				this.hatredSortList.set(i, hatred);
				break;
			} else {
				Hatred tmp = this.hatredSortList.get(i);
				if (tmp.value < hatred.value) {
					this.hatredSortList.set(i, hatred);
					hatred = tmp;
				}
			}
		}
	}
	
	/**
	 * 仇恨对象
	 * 
	 * @author *
	 */
	class Hatred implements Cloneable {
		/** 仇恨值 */
		public int value;
		
		/** 施加者 */
		public long fighterId;
		
		/** 名字 */
		public String name;
		
		public Hatred clone() {
			Hatred hatred = null;
			try {
				hatred = (Hatred) super.clone();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return hatred;
		}
	}
	
	/**
	 * addVal:(). <br/>
	 * TODO().<br/>
	 * 第一个加2点，其他的加一点
	 * 
	 * @author lyh
	 * @param isFirst
	 * @return
	 */
	public static int addVal(boolean isFirst) {
		return isFirst ? 2 : 1;
	}
}
