package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.WorkCaseAppraisal;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class WorkCaseAppraisalDAO extends GenericDAO<WorkCaseAppraisal, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public WorkCaseAppraisalDAO() {
    }

    public long findIdByWobNumber(String wobNumber) {
        log.info("findIdByWobNum : {}", wobNumber);
        long workCaseAppraisalId = 0;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));
        WorkCaseAppraisal workCaseAppraisal = (WorkCaseAppraisal) criteria.uniqueResult();
        if (workCaseAppraisal != null) {
            workCaseAppraisalId = workCaseAppraisal.getId();
        }

        return workCaseAppraisalId;
    }

    public WorkCaseAppraisal findByWobNumber(String wobNumber) {
        log.info("findIdByWobNum : {}", wobNumber);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));
        WorkCaseAppraisal workCaseAppraisal = (WorkCaseAppraisal) criteria.uniqueResult();

        return workCaseAppraisal;
    }

    public WorkCaseAppraisal findByAppNumber(String appNumber){
        log.debug("findByAppNumber : {}", appNumber);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("appNumber", appNumber));
        WorkCaseAppraisal workCaseAppraisal = (WorkCaseAppraisal) criteria.uniqueResult();

        return workCaseAppraisal;
    }
}
