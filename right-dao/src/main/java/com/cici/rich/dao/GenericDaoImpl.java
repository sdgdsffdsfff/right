/*
 * Copyright(c) 2014 GVTV All rights reserved.
 * distributed with this file and available online at
 * http://www.gvtv.com.cn/
 */
/**
 * @FileName GenericDaoImpl.java
 * @Description: 
 * @Date 2014年11月26日 下午2:52:50
 * @author qj
 * @version V1.0
 * 
 */
package com.cici.rich.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.NullArgumentException;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * @FileName GenericDaoImpl.java
 * @Description: myBatis DAO层泛型基类，实现了基本的DAO功能 利用了Spring的DaoSupport功能
 * @Date 2014年11月26日 下午2:52:50
 * @author qj
 * @version V1.0
 * 
 */
@SuppressWarnings("unchecked")
public class GenericDaoImpl<T, PK extends Serializable> extends SqlSessionDaoSupport implements GenericDao<T, PK> {
	public static final Integer BATCH_DEAL_NUM = 50;
	// sqlmap.xml定义文件中对应的sqlid
	public static final String SQLID_INSERT = "insert";
	public static final String SQLID_UPDATE = "update";
	public static final String SQLID_UPDATE_PARAM = "updateParam";
	public static final String SQLID_DELETE = "delete";
	public static final String SQLID_DELETE_PARAM = "deleteParam";
	public static final String SQLID_TRUNCATE = "truncate";
	public static final String SQLID_SELECT = "select";
	public static final String SQLID_SELECT_PK = "selectPk";
	public static final String SQLID_SELECT_PARAM = "selectParam";
	public static final String SQLID_SELECT_FK = "selectFk";
	public static final String SQLID_COUNT = "count";
	public static final String SQLID_COUNT_PARAM = "countParam";

	private SqlSession batchSession;

	@Resource(name = "sqlSessionFactory")
	private SqlSessionFactory sqlSessionFactory;

	@PostConstruct
	public void SqlSessionFactory() {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	private Class<T> clazz;

	private String sqlmapNamespace = "";

	public GenericDaoImpl() {
		// 通过范型反射，取得在子类中定义的class.
		clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		sqlmapNamespace = clazz.getName();
	}

	/**
	 * sqlmapNamespace，对应sqlmap.xml中的命名空间
	 * 
	 * @return
	 */
	public String getSqlmapNamespace() {
		return sqlmapNamespace;
	}

	/**
	 * sqlmapNamespace的设置方法，可以用于spring注入
	 * 
	 * @param sqlmapNamespace
	 */
	public void setSqlmapNamespace(String sqlmapNamespace) {
		this.sqlmapNamespace = sqlmapNamespace;
	}

	public int count() {
		Integer count = (Integer) this.getSqlSession().selectOne(sqlmapNamespace + "." + SQLID_COUNT);
		return count.intValue();
	}

	public int count(Map<String, Object> paramMap) {
		Integer count = (Integer) this.getSqlSession().selectOne(sqlmapNamespace + "." + SQLID_COUNT_PARAM, paramMap);
		return count.intValue();
	}

	public int delete(PK primaryKey) {
		int rows = this.getSqlSession().delete(sqlmapNamespace + "." + SQLID_DELETE, primaryKey);
		return rows;
	}

	public int delete(Map<String, String> paramMap) {
		int rows = this.getSqlSession().delete(sqlmapNamespace + "." + SQLID_DELETE_PARAM, paramMap);
		return rows;
	}

	public int truncate() {
		int rows = this.getSqlSession().delete(sqlmapNamespace + "." + SQLID_TRUNCATE);
		return rows;
	}

	public T get(PK primaryKey) {
		return (T) this.getSqlSession().selectOne(sqlmapNamespace + "." + SQLID_SELECT_PK, primaryKey);
	}

	public void insert(T entity) {
		this.getSqlSession().insert(sqlmapNamespace + "." + SQLID_INSERT, entity);
	}

	public T load(PK primaryKey) {
		Object o = this.getSqlSession().selectOne(sqlmapNamespace + "." + SQLID_SELECT_PK, primaryKey);
		if (o == null)
			throw new NullPointerException("数据查询异常：无法查询出主键数据");
		return (T) o;
	}

	public List<T> select() {
		return this.getSqlSession().selectList(sqlmapNamespace + "." + SQLID_SELECT);
	}

	public List<T> select(Map<String, String> paramMap) {
		return this.getSqlSession().selectList(sqlmapNamespace + "." + SQLID_SELECT_PARAM, paramMap);
	}

	public PaginationResult<T> selectPagination(Map<String, Object> paramMap) {

		PaginationResult<T> result = new PaginationResult<T>();
		Page page = new Page();
		result.setPage(page);
		int count = count(paramMap);
		result.getPage().setCounts(count);
		if (count > 0) {
			List<T> data = this.getSqlSession().selectList(sqlmapNamespace + "." + SQLID_SELECT_PARAM, paramMap);
			result.setObjectList(data);
		}

		return result;
	}

	public List<T> selectFk(Map<String, String> paramMap) {
		return this.getSqlSession().selectList(sqlmapNamespace + "." + SQLID_SELECT_FK, paramMap);
	}

	public PaginationResult<T> selectFkPagination(Map<String, Object> paramMap) {

		PaginationResult<T> result = new PaginationResult<T>();
		int count = count(paramMap);
		result.getPage().setCounts(count);
		if (count > 0) {
			List<T> data = this.getSqlSession().selectList(sqlmapNamespace + "." + SQLID_SELECT_FK, paramMap);
			result.setObjectList(data);
		}

		return result;
	}

	public int update(T entity) {
		return this.getSqlSession().update(sqlmapNamespace + "." + SQLID_UPDATE, entity);
	}

	public int update(Map<String, String> paramMap) {
		if (paramMap == null || paramMap.size() > 0)
			throw new NullArgumentException("参数设置错误:使用带参数的update必须设定update的column！");
		return this.getSqlSession().update(sqlmapNamespace + "." + SQLID_UPDATE_PARAM, paramMap);
	}

	public int batchInsert(final List<T> list) {
		batchSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		int i = 0;
		for (int cnt = list.size(); i < cnt; i++) {
			batchSession.insert(sqlmapNamespace + "." + SQLID_INSERT, list.get(i));
			if ((i + 1) % BATCH_DEAL_NUM == 0) {
				// Constants.BATCH_DEAL_NUM为批量提交的条数
				batchSession.flushStatements();
			}
		}
		batchSession.flushStatements();
		batchSession.close();
		return i;
	}

	public int batchUpdate(final List<T> list) {
		batchSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		int i = 0;
		for (int cnt = list.size(); i < cnt; i++) {
			batchSession.update(sqlmapNamespace + "." + SQLID_UPDATE, list.get(i));
			if ((i + 1) % BATCH_DEAL_NUM == 0) {
				batchSession.flushStatements();
			}
		}
		batchSession.flushStatements();
		batchSession.close();
		return i;
	}

	public int batchDelete(final List<PK> list) {
		batchSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		int i = 0;
		for (int cnt = list.size(); i < cnt; i++) {
			batchSession.delete(sqlmapNamespace + "." + SQLID_DELETE, list.get(i));
			if ((i + 1) % BATCH_DEAL_NUM == 0) {
				batchSession.flushStatements();
			}
		}
		batchSession.flushStatements();
		batchSession.close();
		return i;
	}
}
