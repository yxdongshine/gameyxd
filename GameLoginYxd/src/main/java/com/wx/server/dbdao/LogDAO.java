package com.wx.server.dbdao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.wx.server.domain.DbEntity;
import com.wx.server.utils.LogUtils;

/**
 * 数据保存
 * 
 * @author lyh
 */
public class LogDAO extends DbDao implements Runnable {
	private LogUtils log = LogUtils.getLog(LogDAO.class);
	
	/** 更新数据库线程池 **/
	private ArrayList<Object> logList = new ArrayList<Object>();
	
	// private ArrayList<Object> removeLogList = new ArrayList<Object>();
	
	/**
	 * logDAOThread:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh
	 */
	@Override
	public void dbThreadPoolStart() {
		threadpool.scheduleAtFixedRate(this, 2000, 1000, TimeUnit.MILLISECONDS);
		super.dbThreadPoolStart();
	}
	
	@Override
	public Long save(DbEntity transientInstance) {
		log.debug("saving tbActor instance");
		long id = 0;
		try {
			// synchronized (logList) {
			if (transientInstance != null) {
				// for (int i = 0;i < 1000; i++){
				logList.add(transientInstance);
				// }
			}
			// }
			
			// final Object obj = transientInstance;
			// this.threadpool.execute(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// getHibernateTemplate().save(obj);
			// }
			// });
			
			log.debug("save successful");
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("save failed" + transientInstance, re);
		}
		
		return (long) 0;
	}
	
	@Override
	public void delete(DbEntity persistentInstance) {
		
		// final Object obj = persistentInstance;
		// if (!threadpool.isShutdown()) {
		// threadpool.execute(new Runnable() {
		// @Override
		// public void run() {
		// TODO Auto-generated method stub
		log.debug("deleting tbActor instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("delete failed" + persistentInstance, re);
			
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
	public void updateFinal(DbEntity instance) {
		
		// final Object obj = instance;
		// if (!threadpool.isShutdown()) {
		// threadpool.execute(new Runnable() {
		// @Override
		// public void run() {
		// log.debug("attaching dirty tbActor instance " + obj);
		try {
			getHibernateTemplate().update(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed" + instance, re);
			re.printStackTrace();
		}
		// }
		// });
		// }
	}
	
	/**
	 * updateSynEntity:(). <br/>
	 * TODO().<br/>
	 * 绑定到当前线程
	 * 
	 * @author lyh
	 * @param instance
	 */
	public void updateSynEntity(DbEntity instance) {
		
		log.debug("attaching dirty  instance");
		try {
			getHibernateTemplate().update(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			re.printStackTrace();
			// throw re;
		}
	}
	
	@Override
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
	public <T> List<T> findByProperty(Class<T> c, String propertyName, Object value) {
		log.debug("finding instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from " + c.getName() + " as model where model." + propertyName + "= ?";
			
			return (List<T>) getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			re.printStackTrace();
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findByProperties(Class<T> c, String[] propertyName, Object[] value) {
		// log.debug("finding instance with properties: " +
		// propertyName.toString() + ", value: " + value.toString());
		try {
			String queryString = "from " + c.getName() + " as model ";
			for (int i = 0; i < propertyName.length; i++) {
				if (i == 0)
					queryString = queryString + " where model." + propertyName[i] + "=? ";
				else {
					queryString = queryString + " and model." + propertyName[i] + "=? ";
				}
			}
			return (List<T>) getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			re.printStackTrace();
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
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
		running = false;
		log.error("logdbsize::" + logList.size());
		// 等列表打完
		if (logList.size() > 0) {
			saveEntityDb(logList, logList.size());
		}
		super.stopEntityThread();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// removeLogList.clear();
		int size = 0;
		size = logList.size();
		if (size > 0 && size >= 50) {
			log.error("size ::" + size);
		}
		
		if (size > 0 && running) {
			saveEntityDb(logList, size);
			
			// updateEntityDb(logList, size);
			// deleteEntityDb(logList, size);
		}
		
	}
	
}
