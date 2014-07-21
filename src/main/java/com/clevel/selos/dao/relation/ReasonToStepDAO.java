package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ReasonTypeValue;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.relation.ReasonToStep;
import com.clevel.selos.model.db.relation.StepToAction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ReasonToStepDAO extends GenericDAO<ReasonToStep, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ReasonToStepDAO() {
    }

    public List<Reason> getReturnReason(long stepId, long actionId){
        List<Reason> reasonList = new ArrayList<Reason>();
        log.info("getCancelReason ::: stepId : {}, actionId : {}", stepId, actionId);
        String query = "SELECT reason FROM ReasonToStep reasonToStep WHERE reasonToStep.reason.reasonType.id = " + ReasonTypeValue.RETURN_REASON.value() + " AND reasonToStep.step.id = " + stepId + " AND reasonToStep.action.id = " + actionId + " AND reasonToStep.active = 1 order by reasonToStep.reason.code asc";
        reasonList = (List<Reason>) getSession().createQuery(query).list();

        return reasonList;
    }

    public List<Reason> getCancelReason(long stepId, long actionId){
        List<Reason> reasonList = new ArrayList<Reason>();
        log.info("getCancelReason ::: stepId : {}, actionId : {}", stepId, actionId);
        String query = "SELECT reason FROM ReasonToStep reasonToStep WHERE reasonToStep.reason.reasonType.id = " + ReasonTypeValue.CANCEL_REASON.value() + " AND reasonToStep.step.id = " + stepId + " AND reasonToStep.action.id = " + actionId + " AND reasonToStep.active = 1 order by reasonToStep.reason.code asc";
        reasonList = (List<Reason>) getSession().createQuery(query).list();

        return reasonList;
    }

    public List<Reason> getRejectReason(long stepId){
        List<Reason> reasonList = new ArrayList<Reason>();
        log.info("getCancelReason ::: stepId : {}", stepId);
        String query = "SELECT reason FROM ReasonToStep reasonToStep WHERE reasonToStep.reason.reasonType.id = " + ReasonTypeValue.REJECT_REASON.value() + " AND reasonToStep.step.id = " + stepId + " AND reasonToStep.active = 1 order by reasonToStep.reason.code asc";
        reasonList = (List<Reason>) getSession().createQuery(query).list();

        return reasonList;
    }
}
