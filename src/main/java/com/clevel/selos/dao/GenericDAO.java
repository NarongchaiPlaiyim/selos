package com.clevel.selos.dao;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAO<T, ID extends Serializable> implements BaseDAO<T, ID>, Serializable {
    private Class<T> entityClass;
    //    @Inject
//    private Logger log;
    private Session session;
    @PersistenceContext
    protected EntityManager em;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void onCreation() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Session getSession() {
        return (em == null) ? session : (Session) em.getDelegate();
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setEntityClass(Class<T> T) {
        this.entityClass = T;
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

    public T persistWithCommit(T entity) {
        getSession().getTransaction().begin();
        getSession().saveOrUpdate(entity);
        getSession().getTransaction().commit();
        return entity;
    }

    public T save(T entity) {
        getSession().save(entity);
        return entity;
    }

    public T saveWithCommit(T entity) {
        getSession().getTransaction().begin();
        getSession().save(entity);
        getSession().getTransaction().commit();
        return entity;
    }

    public void persist(List<T> entities) {
        for (T entity : entities) {
            getSession().saveOrUpdate(entity);
        }
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    public void delete(List<T> entities) {
        for (T entity : entities) {
            getSession().delete(entity);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> refresh() {
        Session session = getSession();
        session.setCacheMode(CacheMode.REFRESH);
        Criteria criteria = session.createCriteria(getEntityClass());
        List<T> list = criteria.list();
        return list;
    }

    public boolean isRecordExist(Criterion... criterion) {
        List<T> list = findByCriteria(criterion);
        return list.size() > 0;
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
