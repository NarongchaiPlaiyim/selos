package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ProductGroup;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProductGroupDAO extends GenericDAO<ProductGroup, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ProductGroupDAO() {
    }

    @Override
    public List<ProductGroup> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1));
        List<ProductGroup> list = criteria.list();
        return list;
    }

    public List<ProductGroup> findProposeProductGroup(){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("addProposeCredit", 1));
        criteria.add(Restrictions.eq("active", 1));

        List<ProductGroup> list = criteria.list();

        return list;
    }

    public String productGroupNameById(Integer id)
    {
        Criteria criteria = getSession().createCriteria(getEntityClass()).add(Restrictions.eq("id",id));
        ProductGroup productGroup = (ProductGroup)criteria.uniqueResult();
        log.info("Product Group id :{} , name :{}",id,productGroup.getName());
        return productGroup.getName();
    }
}
