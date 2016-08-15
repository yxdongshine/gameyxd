package com.wx.server.dbdao;

import java.util.List;

import com.wx.server.domain.DbEntity;
import com.wx.server.utils.LogUtils;

public interface EntityDAOInterface {
LogUtils log = LogUtils.getLog(DbDao.class);
	
	boolean running = true;

	
	// @Autowired
	// private HibernateTemplate hibernateTemplate;
	
	abstract Long save(DbEntity transientInstance);
	
	abstract void delete(DbEntity persistentInstance);
	
	abstract void saveOrUpdate(DbEntity instance);
	
	abstract void updateFinal(DbEntity instance);
	
	// protected abstract void updateSynEntity(Object instance);
	
	abstract <T> T findById(Class<T> entity, long id);
	
	abstract <T> List<T> findAll(Class<T> c);
	
	abstract <T> List<T> findByProperty(Class<T> c, String propertyName, Object value);
	
	abstract <T> List<T> findByProperties(Class<T> c, String[] propertyName, Object[] value);
	
	abstract <T> List<T> findByHql(String hql);
}
