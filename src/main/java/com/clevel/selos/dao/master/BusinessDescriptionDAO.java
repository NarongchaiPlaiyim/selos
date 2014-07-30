package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BusinessDescriptionDAO extends GenericDAO<BusinessDescription, Integer> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public BusinessDescriptionDAO() {
    }

    public List<BusinessDescription> getListByBusinessGroup(BusinessGroup businessGroup) {
        log.info("getListByBusinessGroup. (businessGroup: {})", businessGroup);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("businessGroup", businessGroup));
        criteria.addOrder(Order.asc("id"));
        List<BusinessDescription> businessDescriptions = criteria.list();
        log.info("getListByBusinessGroup. (result size: {})", businessDescriptions.size());
        return businessDescriptions;
    }

    public List<BusinessDescription> getListOrderByTMBCode() {
        log.info("getListOrderByTMBCode");
        Criteria criteria = createCriteria();
        criteria.addOrder(Order.asc("tmbCode"));
        List<BusinessDescription> businessDescriptions = criteria.list();
        log.info("getListOrderByTMBCode. (result size: {})", businessDescriptions.size());
        return businessDescriptions;
    }
}
