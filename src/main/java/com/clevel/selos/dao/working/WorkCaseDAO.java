package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class WorkCaseDAO extends GenericDAO<WorkCase, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public WorkCaseDAO() {
    }

    public long findIdByWobNumber(String wobNumber) {
        log.info("findIdByWobNum : {}", wobNumber);
        long workCaseId = 0;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));
        WorkCase workCase = (WorkCase) criteria.uniqueResult();
        if (workCase != null) {
            workCaseId = workCase.getId();
        }

        return workCaseId;
    }

    public WorkCase findByWobNumber(String wobNumber) {
        log.info("findIdByWobNum : {}", wobNumber);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));
        criteria.setFetchMode("workCasePrescreen", FetchMode.SELECT);
        WorkCase workCase = (WorkCase) criteria.uniqueResult();

        return workCase;
    }

    //Function for AppHeader
    public WorkCase getWorkCaseById(long id){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("id", id));
        criteria.setFetchMode("stepOwner", FetchMode.SELECT);
        criteria.setFetchMode("workCasePrescreen", FetchMode.SELECT);
        criteria.setFetchMode("atUserTeam", FetchMode.SELECT);
        criteria.setFetchMode("productGroup", FetchMode.SELECT);
        criteria.setFetchMode("requestType", FetchMode.SELECT);
        criteria.setFetchMode("fromUser", FetchMode.SELECT);
        criteria.setFetchMode("atUser", FetchMode.SELECT);
        criteria.setFetchMode("authorizationDOA", FetchMode.SELECT);
        criteria.setFetchMode("step", FetchMode.SELECT);
        criteria.setFetchMode("modifyBy", FetchMode.SELECT);
        WorkCase workCase = (WorkCase) criteria.uniqueResult();

        return workCase;
    }
}
