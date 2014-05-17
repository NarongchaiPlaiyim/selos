package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateFieldConStepAction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MandateFieldConStepActionDAO extends GenericDAO<MandateFieldConStepAction, Long>{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    public MandateFieldConStepActionDAO(){}

    public List<MandateFieldConStepAction> findByAction(long stepId, long actionId){
        logger.debug("findByCriteria Step:{}, Action:{}", stepId, actionId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("action.id", actionId));
        List<MandateFieldConStepAction> mandateFieldStepActionList = criteria.list();
        logger.debug("retrun List<MandateFieldStepAction> {}", mandateFieldStepActionList);
        return mandateFieldStepActionList;
    }

    public MandateFieldConStepAction findByActionAndCon(long stepId, long actionId, long conditionId){
        logger.debug("findByCriteria Step:{}, Action:{}", stepId, actionId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("action.id", actionId));
        criteria.add(Restrictions.eq("mandateFieldCondition.id", conditionId));
        MandateFieldConStepAction conStepAction = (MandateFieldConStepAction)criteria.uniqueResult();
        logger.debug("retrun MandateFieldStepAction {}", conStepAction);
        return conStepAction;
    }
}
