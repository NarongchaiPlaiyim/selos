package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MandateFieldDAO extends GenericDAO<MandateField, Long> {

    @Inject
    @SELOS
    Logger logger;

    @Inject
    public MandateFieldDAO() {
    }

    public List<MandateField> findByAction(Step step, Status status, Action action){
        logger.debug("findByCriteria Step:{}, Status:{}, Action:{}", step, status, action);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step", step));
        criteria.add(Restrictions.eq("status", status));
        criteria.add(Restrictions.eq("action", action));
        List<MandateField> mandateFieldList = criteria.list();
        logger.debug("retrun List<mandateFieldList> {}", mandateFieldList);
        return mandateFieldList;
    }

    public List<MandateField> findByClass(int mandateFieldClassId){
        logger.debug("-- begin findByClass mandateFieldClassId: {}", mandateFieldClassId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("mandateFieldClass.id", mandateFieldClassId));
        List<MandateField> mandateFieldList = criteria.list();
        logger.debug("-- end findByClass return: {}", mandateFieldList);
        return mandateFieldList;
    }
}
