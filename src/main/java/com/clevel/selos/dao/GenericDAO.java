package com.clevel.selos.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAO<T, ID extends Serializable> implements BaseDAO<T,ID>,Serializable {
    private Class<T> entityClass;
    @Inject
    private Logger log;
    private Session session;
    @PersistenceContext
    protected EntityManager em;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void onCreation() {
        this.entityClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Session getSession() {
        return (em==null)?session:(Session) em.getDelegate();
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public void setEntityClass(Class<T> T) {
        this.entityClass = T;
    }

    public void setup(Session session,Class<T> T,Logger log) {
        setSession(session);
        setEntityClass(T);
        setLog(log);
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id) {
        return (T) getSession().load(getEntityClass(), id);
    }

    public List<T> findAll() {
        return findByCriteria();
    }

    public T persist(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void persist(List<T> entities) {
        for (T entity: entities) {
            getSession().saveOrUpdate(entity);
        }
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    public void delete(List<T> entities) {
        for (T entity: entities) {
            getSession().delete(entity);
        }
    }

    public Criteria createCriteria() {
        return getSession().createCriteria(getEntityClass());
    }

    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(Criterion... criterion) {
        Criteria criteria = getSession().createCriteria(getEntityClass());
        for (Criterion c : criterion) {
            criteria.add(c);
        }
        List<T> list = criteria.list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public T findOneByCriteria(Criterion... criterion) {
        Criteria criteria = getSession().createCriteria(getEntityClass());
        for (Criterion c : criterion) {
            criteria.add(c);
        }
        return (T) criteria.uniqueResult();
    }
}
