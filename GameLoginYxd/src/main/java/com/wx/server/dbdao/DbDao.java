package com.wx.server.dbdao;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.wx.server.domain.DbEntity;
import com.wx.server.utils.LogUtils;

/**
 * ClassName:DbDao <br/>
 * Function: TODO (数据库保存抽象类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2014-4-23 上午9:01:45 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public abstract class DbDao extends HibernateDaoSupport {
	protected LogUtils log = LogUtils.getLog(DbDao.class);
	
	protected boolean running = true;
	
	/** 更新数据库线程池 **ScheduledThreadPoolExecutor */
	
	protected ScheduledThreadPoolExecutor threadpool = new ScheduledThreadPoolExecutor(1);
	
	// @Autowired
	// private HibernateTemplate hibernateTemplate;
	
	protected abstract Long save(DbEntity transientInstance);
	
	protected abstract void delete(DbEntity persistentInstance);
	
	protected abstract void saveOrUpdate(DbEntity instance);
	
	protected abstract void updateFinal(DbEntity instance);
	
	// protected abstract void updateSynEntity(Object instance);
	
	protected abstract <T> T findById(Class<T> entity, long id);
	
	protected abstract <T> List<T> findAll(Class<T> c);
	
	protected abstract <T> List<T> findByProperty(Class<T> c, String propertyName, Object value);
	
	protected abstract <T> List<T> findByProperties(Class<T> c, String[] propertyName, Object[] value);
	
	protected abstract <T> List<T> findByHql(String hql);
	
	protected void stopEntityThread() {
		threadpool.shutdown();
	}
	
	/**
	 * dbThreadPoolStart:(). <br/>
	 * TODO().<br/>
	 * 加载数保存数据库的线程池
	 * 
	 * @author lyh
	 */
	public void dbThreadPoolStart() {
		
	}
	
	/**
	 * commintLogDb:(). <br/>
	 * TODO().<br/>
	 * 更新新的数据到数据库
	 * 
	 * @author lyh
	 * @param size
	 */
	protected void updateEntityDb(List<Object> list, int size) {
		long startTime = System.currentTimeMillis();
		Session se = getSession();
		Transaction tt = se.beginTransaction();
		while (size > 0) {
			size--;
			Object obj = null;
			try {
				obj = list.remove(0);
				if (obj != null) {
					se.update(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("updateentity::" + obj, e);
			}
		}
		
		try {
			tt.commit();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("update::", e);
		}
		se.close();
		long endTime = System.currentTimeMillis() - startTime;
		if (endTime >= 200) {
			log.error("UpdateEntity::" + (endTime));
		}
	}
	
	/**
	 * commintLogDb:(). <br/>
	 * TODO().<br/>
	 * 保存新的数据 到数据库
	 * 
	 * @author lyh
	 * @param size
	 */
	protected void saveEntityDb(List<Object> list, int size) {
		long startTime = System.currentTimeMillis();
		Session se = getSession();
		Transaction tt = se.beginTransaction();
		while (size > 0) {
			size--;
			Object obj = null;
			try {
				obj = list.remove(0);
				if (obj != null) {
					se.save(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("logentity::" + obj, e);
			}
		}
		
		try {
			tt.commit();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("save::", e);
		}
		se.close();
		long endTime = System.currentTimeMillis() - startTime;
		if (endTime >= 300) {
			log.error("SaveEntity::" + (endTime));
		}
	}
	
	/**
	 * commintLogDb:(). <br/>
	 * TODO().<br/>
	 * 删除数据库的数据
	 * 
	 * @author lyh
	 * @param size
	 */
	protected void deleteEntityDb(List<Object> list, int size) {
		long startTime = System.currentTimeMillis();
		// Session se = getSession();
		// Transaction tt = se.beginTransaction();
		while (size > 0) {
			size--;
			Object obj = null;
			try {
				obj = list.remove(0);
				if (obj != null) {
					
					getHibernateTemplate().delete(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("logentity::" + obj, e);
			}
		}
		long endTime = System.currentTimeMillis() - startTime;
		if (endTime >= 300) {
			log.error("DeleteEntity::" + (endTime));
		}
	}
	
}
