package com.clevel.selos.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import java.io.Serializable;
import java.util.List;

public interface BaseDAO <T,ID extends Serializable> {
    public T findById(ID id);
    public List<T> findAll();
    public T persist(T entity);
    public void persist(List<T> entities);
    public void delete(T entity);
    public void delete(List<T> entities);
    public Criteria createCriteria();
    public List<T> findByCriteria(Criterion... criterion);
    public T findOneByCriteria(Criterion... criterion);
}
