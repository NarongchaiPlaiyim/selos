package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BusinessGroup;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BusinessGroupDAO extends GenericDAO<BusinessGroup, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BusinessGroupDAO() {
    }

    @Override
    public List<BusinessGroup> findAll(){
        log.info("findAll businessGroup");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.addOrder(Order.asc("name"));
        List<BusinessGroup> businessGroupList = criteria.list();
        log.info("findAll businessGroup. (result size: {})", businessGroupList.size());

        return businessGroupList;
    }
}
