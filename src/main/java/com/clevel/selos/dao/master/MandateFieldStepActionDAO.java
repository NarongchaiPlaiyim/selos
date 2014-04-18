package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateField;
import com.clevel.selos.model.db.master.MandateFieldStepAction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MandateFieldStepActionDAO extends GenericDAO<MandateFieldStepAction, Long> {

    @Inject
    @SELOS
    Logger logger;

    @Inject
    public MandateFieldStepActionDAO(){}

    public List<MandateFieldStepAction> findByAction(long stepId, long actionId){
        logger.debug("findByCriteria Step:{}, Action:{}", stepId, actionId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("action.id", actionId));
        List<MandateFieldStepAction> mandateFieldStepActionList = criteria.list();
        logger.debug("retrun List<MandateFieldStepAction> {}", mandateFieldStepActionList);
        return mandateFieldStepActionList;
    }
}
