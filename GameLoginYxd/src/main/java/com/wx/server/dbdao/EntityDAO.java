package com.wx.server.dbdao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wx.server.domain.DbEntity;
import com.wx.server.utils.LogUtils;

/**
 * 数据保存
 * 
 * @author lyh
 */
@Component
public class EntityDAO extends DbDao implements Runnable {
	
	private LogUtils log = LogUtils.getLog(EntityDAO.class);
	
	/** 更新数据库线程池 **/
	private Map<Long, DbEntity> updateDbList = new ConcurrentHashMap<Long, DbEntity>();
	
	/** 删除数据容器 **/
	private Map<Long, DbEntity> deleteDbList = new ConcurrentHashMap<Long, DbEntity>();
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Long save(DbEntity transientInstance) {
		log.debug("saving tbActor instance");
		long id = 0;
		try {
			id = (Long) getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("save failed" + transientInstance, re);
		}
		
		return id;
	}
	
	/**
	 * isInDeleteDbList:(). <br/>
	 * TODO().<br/>
	 * 是否在删除容器里了
	 * 
	 * @author lyh
	 * @param db
	 * @return
	 */
	public boolean isInDeleteDbList(DbEntity db) {
		return deleteDbList.containsKey(db.getId());
	}
	
	/**
	 * isInUpdateDbList:(). <br/>
	 * TODO().<br/>
	 * 是否在更新容器里
	 * 
	 * @author lyh
	 * @param db
	 * @return
	 */
	public boolean isInUpdateDbList(DbEntity db) {
		return updateDbList.containsKey(db.getId());
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(DbEntity db) {
		
		// final Object obj = persistentInstance;
		// if (!threadpool.isShutdown()) {
		// threadpool.execute(new Runnable() {
		// @Override
		// public void run() {
		// TODO Auto-generated method stub
		// log.debug("deleting tbActor instance");
		try {
			
			if (db != null) {
				// if (!isInDeleteDbList(db)){
				// deleteDbList.put(db.getId(), db);
				// }
				getHibernateTemplate().delete(db);
			}
			
			// getHibernateTemplate().delete(persistentInstance);
			// log.debug("delete successful");
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("delete failed" + db, re);
		}
		
		// }
		// });
		// }
	}
	
	@Override
	public void saveOrUpdate(DbEntity instance) {
		log.debug("attaching dirty tbActor instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed" + instance, re);
			re.printStackTrace();
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateFinal(DbEntity db) {
		
		// final Object obj = instance;
		// if (!threadpool.isShutdown()) {
		// threadpool.execute(new Runnable() {
		// @Override
		// public void run() {
		// log.debug("attaching dirty tbActor instance " + obj);
		try {
			
			if (db != null) {
				// entityDbList.add(instance);
				getHibernateTemplate().update(db);
				// if (!isInUpdateDbList(db)){
				// updateDbList.put(db.getId(), db);
				// }
			}
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed" + db, re);
			re.printStackTrace();
		}
		// }
		// });
		// }
	}
	
	// /**
	// * updateSynEntity:(). <br/>
	// * TODO().<br/>
	// * 绑定到当前线程
	// *
	// * @author lyh
	// * @param instance
	// */
	// public void updateSynEntity(Object instance) {
	//
	// log.debug("attaching dirty  instance");
	// try {
	// getHibernateTemplate().update(instance);
	// log.debug("attach successful");
	// } catch (RuntimeException re) {
	// log.error("attach failed", re);
	// re.printStackTrace();
	// // throw re;
	// }
	// }
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public <T> T findById(Class<T> entity, long id) {
		log.debug("finding instance with id: " + id);
		try {
			return getHibernateTemplate().get(entity, Long.valueOf(id));
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public <T> List<T> findAll(Class<T> c) {
		log.debug("finding all from " + c.getName());
		try {
			String queryString = "from " + c.getName();
			return (List<T>) getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			re.printStackTrace();
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public <T> List<T> findByProperty(Class<T> c, String propertyName, Object value) {
		log.debug("finding instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from " + c.getName() + " as model where model." + propertyName + "= :"+propertyName;
			Query queryObject = this.getSession().createQuery(queryString);

			if (value != null) {
				//for (int i = 0; i < values.length; i++) {
					queryObject.setParameter(propertyName, value);
				//}
			}
			return queryObject.list();
		//	return (List<T>) getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			re.printStackTrace();
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public <T> List<T> findByProperties(Class<T> c, String[] propertyName, Object[] value) {
		// log.debug("finding instance with properties: " +
		// propertyName.toString() + ", value: " + value.toString());
		try {
			String queryString = "from " + c.getName() + " as model ";
			for (int i = 0; i < propertyName.length; i++) {
				if (i == 0)
					queryString = queryString + " where model." + propertyName[i] + "= :"+propertyName[i];
				else {
					queryString = queryString + " and model." + propertyName[i] + "= :"+propertyName[i];
				}
			}
			
			Query queryObject = this.getSession().createQuery(queryString);
			if (value != null) {
				for (int i = 0; i < value.length; i++) {
					queryObject.setParameter(propertyName[i], value[i]);
				}
			}
			return queryObject.list();
			//return (List<T>) getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			re.printStackTrace();
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public <T> List<T> findByHql(String hql) {
		log.debug("finding instance with hql: " + hql);
		try {
			return (List<T>) getHibernateTemplate().find(hql);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			re.printStackTrace();
			throw re;
		}
	}
	
	@Override
	public void stopEntityThread() {
		// TODO Auto-generated method stub
		// running = false;
		// log.error("entityDbList::" + entityDbList.size());
		//
		// if (entityDbList.size() > 0) {
		// this.updateEntityDb(entityDbList, entityDbList.size());
		// }
		//
		// log.error("deleteDbList::" + entityDbList.size());
		// if (deleteDbList.size() > 0) {
		// this.deleteEntityDb(deleteDbList, deleteDbList.size());
		// }
		// super.stopEntityThread();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// int size = 0;
		// Collection<DbEntity> list = updateDbList.values();
		//
		// size = entityDbList.size();
		// if (size > 0 && size >= 20) {
		// log.error("entityDbListsize ::" + size);
		// }
		// if (size > 0 && running) {
		// updateEntityDb(entityDbList, size);
		// }
		//
		// size = deleteDbList.size();
		// if (size > 0 && size >= 50) {
		// log.error("deleteDbListsize ::" + size);
		// }
		// if (size > 0 && running) {
		// deleteEntityDb(deleteDbList, size);
		// }
	}
	
	/**
	 * deleteAll:(). <br/>
	 * TODO().<br/>
	 * 删除全部的对象
	 * 
	 * @author lyh
	 * @param list
	 */
	public <E> void deleteAll(List<E> list) {
		if (!list.isEmpty() && list != null) {
			getHibernateTemplate().deleteAll(list);
		}
		// Session ss = getSession();
		// Transaction tt = ss.beginTransaction();
		// for (E obj : list){
		// try{
		// ss.delete(obj);
		// }catch(Exception e){
		// e.printStackTrace();
		// log.error("deleteAll::" + obj, e);
		// }
		// }
		//
		// try {
		// tt.commit();
		// } catch (Exception e) {
		// e.printStackTrace();
		// log.error("deleteAllCommit::", e);
		// }
		// ss.close();
	}
	
	@Override
	public void dbThreadPoolStart() {
		// TODO Auto-generated method stub
		// 每5分钟调一次
		// threadpool.scheduleAtFixedRate(this, 2000, 300000,
		// TimeUnit.MILLISECONDS);
		// super.dbThreadPoolStart();
	}
	
}
