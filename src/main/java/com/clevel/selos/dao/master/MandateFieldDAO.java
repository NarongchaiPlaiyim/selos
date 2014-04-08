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

    public List<MandateField> findByAction(long stepId, long actionId){
        logger.debug("findByCriteria Step:{}, Action:{}", stepId, actionId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("action.id", actionId));
        List<MandateField> mandateFieldConfigureList = criteria.list();
        logger.debug("retrun List<MandateFieldConfigure> {}", mandateFieldConfigureList);
        return mandateFieldConfigureList;
    }

    public List<MandateField> findByAction(Step step, Status status, Action action){
        logger.debug("findByCriteria Step:{}, Status:{}, Action:{}", step, status, action);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step", step));
        criteria.add(Restrictions.eq("status", status));
        criteria.add(Restrictions.eq("action", action));
        List<MandateField> mandateFieldConfigureList = criteria.list();
        logger.debug("retrun List<MandateFieldConfigure> {}", mandateFieldConfigureList);
        return mandateFieldConfigureList;
    }
}
