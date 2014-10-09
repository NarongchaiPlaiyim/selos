package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateFieldClassStepAction;
import com.clevel.selos.model.db.master.MandateFieldConStepAction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MandateFieldClassStepActionDAO extends GenericDAO<MandateFieldClassStepAction, Long>{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    public MandateFieldClassStepActionDAO() {
    }

    public List<MandateFieldClassStepAction> findByStepAction(long stepId, long actionId){
        logger.debug("findByCriteria Step:{}, Action:{}", stepId, actionId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("action.id", actionId));
        List<MandateFieldClassStepAction> classStepActionList = criteria.list();
        logger.debug("retrun MandateFieldClassStepAction {}", classStepActionList);
        return classStepActionList;
    }

    public MandateFieldClassStepAction findByActionAndClass(long stepId, long actionId, long classId){
        logger.debug("findByStepAction Step:{}, Action:{}", stepId, actionId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("action.id", actionId));
        criteria.add(Restrictions.eq("mandateFieldClass.id", classId));
        MandateFieldClassStepAction classStepAction = (MandateFieldClassStepAction)criteria.uniqueResult();
        logger.debug("retrun MandateFieldClassStepAction {}", classStepAction);
        return classStepAction;
    }
}
