package com.vteam.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.type.AbstractStandardBasicType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateConn {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateConn.class);
	
	public static Configuration config;
	public static SessionFactory sessionFactory;
	private int firstResult;
	private int maxResults;

	public HibernateConn() {
		if (config == null) {
			config = new Configuration().configure();
			if (sessionFactory == null) {
				ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
						.applySettings(config.getProperties())
						.buildServiceRegistry();
				sessionFactory = config.buildSessionFactory(serviceRegistry);
			}
		}
	}

	public static Configuration getConfiguration() {
		if (config == null) {
			new HibernateConn();
		}
		return config;
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			new HibernateConn();
		}
		return sessionFactory;
	}

	public void add(Object obj) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.save(obj);
			tx.commit();
		} catch (Exception e) {
			if (tx != null){
				tx.rollback();
			}
			throw new Exception((new StringBuilder("hibernate save() error:")).append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public Object addReturn(Class classname, Object obj) throws Exception {
		Session session = null;
		Object robj = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			java.io.Serializable sid = session.save(obj);
			tx.commit();
			robj = session.get(classname, sid);
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw new Exception((new StringBuilder("hibernate save() error:")).append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					throw e;
				}
			}
		}
		return robj;
	}

	public void update(Object obj) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.update(obj);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new Exception(new StringBuilder("hibernate update() error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	public void save(Object obj) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(obj);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw new Exception(new StringBuilder("hibernate update() error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void batchSave(List objlist) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			for (int i = 0; i < objlist.size(); i++) {
				Object obj = objlist.get(i);
				session.saveOrUpdate(obj);
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null){
				tx.rollback();
			}
			throw new Exception(new StringBuilder("hibernate batchSave() error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	public void delete(Object obj) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			session.delete(obj);
			tx.commit();
		} catch (Exception e) {
			if (tx != null){
				tx.rollback();
			}
			throw new Exception(new StringBuilder("hibernate delete() error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public List commonHBNQuery(String querystr, List<ConditionObject> condition)
			throws Exception {
		List results = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery(querystr);
			if (maxResults > 0) {
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
			}
			if (condition != null && condition.size() > 0) {
				for (int i = 0; i < condition.size(); i++) {
					ConditionObject obj = (ConditionObject) condition.get(i);
					if (obj.getType() == null) {
						query.setParameter(obj.getParam(), obj.getObj());
					} else {
						query.setParameter(obj.getParam(), obj.getObj(), obj.getType());
					}
				}
			}
			results = query.list();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("commonQuery() error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	@SuppressWarnings("rawtypes")
	public List commonHBNQuery(String querystr, List<ConditionObject> condition,
			List<WhereinObject> whereincondition) throws Exception {
		List results = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery(querystr);
			if (maxResults > 0) {
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
			}
			if (condition != null && condition.size() > 0) {
				for (int i = 0; i < condition.size(); i++) {
					ConditionObject obj = (ConditionObject) condition.get(i);
					if (obj.getType() == null) {
						query.setParameter(obj.getParam(), obj.getObj());
					} else {
						query.setParameter(obj.getParam(), obj.getObj(), obj.getType());
					}
				}
			}
			if (whereincondition != null && whereincondition.size() > 0) {
				for (int i = 0; i < whereincondition.size(); i++) {
					WhereinObject obj = (WhereinObject) whereincondition.get(i);
					query.setParameterList(obj.getParam(), obj.getObj());
				}
			}
			results = query.list();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("commonQuery(String, List, List) error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	@SuppressWarnings("rawtypes")
	public List commonSQLQuery(String querystr, List<ConditionObject> condition)
			throws Exception {
		List results = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(querystr);
			if (maxResults > 0) {
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
			}
			if (condition != null && condition.size() > 0) {
				for (int i = 0; i < condition.size(); i++) {
					ConditionObject obj = (ConditionObject) condition.get(i);
					if (obj.getType() == null) {
						query.setParameter(obj.getParam(), obj.getObj());
					} else {
						query.setParameter(obj.getParam(), obj.getObj(), obj.getType());
					}
				}
			}
			results = query.list();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("commonSQLQuery() error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	@SuppressWarnings("rawtypes")
	public List commonSQLQuery(String querystr, List<ConditionObject> condition,
			List<WhereinObject> whereincondition) throws Exception {
		List results = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(querystr);
			if (maxResults > 0) {
				query.setFirstResult(firstResult);
				query.setMaxResults(maxResults);
			}
			if (condition != null && condition.size() > 0) {
				for (int i = 0; i < condition.size(); i++) {
					ConditionObject obj = (ConditionObject) condition.get(i);
					if (obj.getType() == null) {
						query.setParameter(obj.getParam(), obj.getObj());
					} else {
						query.setParameter(obj.getParam(), obj.getObj(), obj.getType());
					}
				}
			}
			if (whereincondition != null && whereincondition.size() > 0) {
				for (int i = 0; i < whereincondition.size(); i++) {
					WhereinObject obj = (WhereinObject) whereincondition.get(i);
					query.setParameterList(obj.getParam(), obj.getObj());
				}
			}
			results = query.list();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("commonSQLQuery(String, List, List) error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}

	public int commonSQLUpdate(String querystr, List<ConditionObject> condition,
			List<WhereinObject> whereincondition) throws Exception {
		int result = 0;
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			SQLQuery query = session.createSQLQuery(querystr);
			if (condition != null && condition.size() > 0) {
				for (int i = 0; i < condition.size(); i++) {
					ConditionObject obj = (ConditionObject) condition.get(i);
					if (obj.getType() == null) {
						query.setParameter(obj.getParam(), obj.getObj());
					} else {
						query.setParameter(obj.getParam(), obj.getObj(), obj.getType());
					}
				}
			}
			if (whereincondition != null && whereincondition.size() > 0) {
				for (int i = 0; i < whereincondition.size(); i++) {
					WhereinObject obj = (WhereinObject) whereincondition.get(i);
					query.setParameterList(obj.getParam(), obj.getObj());
				}
			}
			result = query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx != null){
				tx.rollback();
			}
			LOGGER.error(new StringBuilder("commonSQLUpdate() error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public int commonSQLUpdate(String querystr, List<ConditionObject> condition)
			throws Exception {
		int result = 0;
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			SQLQuery query = session.createSQLQuery(querystr);
			if (condition != null && condition.size() > 0) {
				for (int i = 0; i < condition.size(); i++) {
					ConditionObject obj = (ConditionObject) condition.get(i);
					if (obj.getType() == null) {
						query.setParameter(obj.getParam(), obj.getObj());
					} else {
						query.setParameter(obj.getParam(), obj.getObj(), obj.getType());
					}
				}
			}
			result = query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx != null){
				tx.rollback();
			}
			LOGGER.error(new StringBuilder("commonSQLUpdate() error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public Object getValueObject(String querystr) {
		Object result = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			SQLQuery sqlquery = session.createSQLQuery(querystr);
			result = sqlquery.uniqueResult();
		} catch (Exception e) {
			LOGGER.error(new StringBuilder("getValueObject() error:").append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public Object findByPK(String querystr, List<ConditionObject> condition) {
		Object result = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery(querystr);
			if (condition != null && condition.size() > 0) {
				for (int i = 0; i < condition.size(); i++) {
					ConditionObject obj = (ConditionObject) condition.get(i);
					query.setParameter(obj.getParam(), obj.getObj());
				}
			}
			result = query.uniqueResult();
		} catch (Exception e) {
			LOGGER.error((new StringBuilder("findByPK() error:")).append(e).toString());
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getVoLength(String entityName) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		PersistentClass pc = config.getClassMapping(entityName);
		Table table = pc.getIdentityTable();
		Iterator<Column> it = table.getColumnIterator();
		while (it.hasNext()) {
			Column c = (Column) it.next();
			String colname = c.getName();
			if (colname.indexOf("_") >= 0) {
				int idx = colname.indexOf("_");
				colname = colname.substring(0, idx)
						+ colname.substring(idx + 1, idx + 2).toUpperCase()
						+ colname.substring(idx + 2);
			}
			int length = c.getLength();
			map.put(colname, Integer.valueOf(length));
		}
		return map;
	}

	public int getFirstResult() {
		return this.firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResults() {
		return this.maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	
	public class ConditionObject {
		
		private String param;
		private Object obj;
		private AbstractStandardBasicType type;

		public ConditionObject() {
		}

		public ConditionObject(String param, Object obj,
				AbstractStandardBasicType type) {
			this.param = param;
			this.obj = obj;
			this.type = type;
		}

		public String getParam() {
			return this.param;
		}

		public void setParam(String param) {
			this.param = param;
		}

		public Object getObj() {
			return this.obj;
		}

		public void setObj(Object obj) {
			this.obj = obj;
		}

		public AbstractStandardBasicType getType() {
			return this.type;
		}

		public void setType(AbstractStandardBasicType type) {
			this.type = type;
		}
	}
	
	public class WhereinObject {
		
		private String param;
		private Object[] obj;

		public WhereinObject() {
		}

		public WhereinObject(String param, Object[] obj) {
			this.param = param;
			this.obj = obj;
		}

		public String getParam() {
			return this.param;
		}

		public void setParam(String param) {
			this.param = param;
		}

		public Object[] getObj() {
			return this.obj;
		}

		public void setObj(Object[] obj) {
			this.obj = obj;
		}
	}

}

