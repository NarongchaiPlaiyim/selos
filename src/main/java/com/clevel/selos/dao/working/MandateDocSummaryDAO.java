package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StageValue;
import com.clevel.selos.model.db.working.MandateDocSummary;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MandateDocSummaryDAO extends GenericDAO<MandateDocSummary, Long> {

    @Inject
    @SELOS
    Logger log;

    @Inject
    public MandateDocSummaryDAO() {

    }

    public MandateDocSummary findByWorkCaseIdForStepRole(long workCaseId, long stepId, int stageId, int roleId) {
        log.info("--findByWorkCaseIdForStepRole : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("stage.id", stageId));
        criteria.add(Restrictions.eq("role.id", roleId));
        MandateDocSummary mandateDocSummary = (MandateDocSummary)criteria.uniqueResult();
        return mandateDocSummary;
    }

    public MandateDocSummary findByWorkCasePrescreenIdForStepRole(long workCasePrescreenId, long stepId, int stageId, int roleId){
        log.info("-- begin findByWorkCasePrescreenId: {}", workCasePrescreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("stage.id", stageId));
        criteria.add(Restrictions.eq("role.id", roleId));
        MandateDocSummary mandateDocSummary = (MandateDocSummary) criteria.uniqueResult();

        return mandateDocSummary;
    }

    public MandateDocSummary findLatestInfoByWorkCasePrescreenId(long workCasePrescreenId, StageValue stageValue){
        log.info("-- begin findLatestInfoByWorkCasePrescreenId: {}", workCasePrescreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.add(Restrictions.eq("stage.id", stageValue.value()));
        criteria.addOrder(Order.desc("step")).addOrder(Order.desc("modifyDate"));

        MandateDocSummary mandateDocSummary = (MandateDocSummary) criteria.list().get(0);
        return mandateDocSummary;
    }

    public MandateDocSummary findLatestInfoByWorkCaseId(long workCaseId, StageValue stageValue){
        log.info("-- begin findLatestInfoByWorkCaseId: {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("stage.id", stageValue.value()));
        criteria.addOrder(Order.desc("step")).addOrder(Order.desc("modifyDate"));
        MandateDocSummary mandateDocSummary = (MandateDocSummary) criteria.list().get(0);
        return mandateDocSummary;
    }



}
