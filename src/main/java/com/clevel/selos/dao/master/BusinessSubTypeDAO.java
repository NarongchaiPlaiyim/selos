package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BusinessSubType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BusinessSubTypeDAO extends GenericDAO<BusinessSubType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BusinessSubTypeDAO() {
    }

    public List<BusinessSubType> findAll(){
        log.info("findAll BusinessSubType");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.addOrder(Order.asc("mainCode"));
        criteria.addOrder(Order.asc("extendCode"));
        List<BusinessSubType> businessSubTypes = criteria.list();
        log.info("findAll BusinessSubType. (result size: {})", businessSubTypes.size());

        return businessSubTypes;
    }
}
