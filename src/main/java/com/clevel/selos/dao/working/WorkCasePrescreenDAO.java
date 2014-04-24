package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class WorkCasePrescreenDAO extends GenericDAO<WorkCasePrescreen, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public WorkCasePrescreenDAO() {
    }

    public long findIdByWobNumber(String wobNumber) {
        log.info("findIdByWobNum : {}", wobNumber);
        long workCasePreScreenId = 0;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));
        WorkCasePrescreen workCasePrescreen = (WorkCasePrescreen) criteria.uniqueResult();
        log.info("workcaseprescreen object : {}",workCasePrescreen);
        if (workCasePrescreen != null) {
            workCasePreScreenId = workCasePrescreen.getId();
            log.info("findIdByWobNum : {}", workCasePreScreenId);
        }

        return workCasePreScreenId;
    }

    public WorkCasePrescreen findByWobNumber(String wobNumber) {
        log.info("findByWobNumber : {}", wobNumber);
        WorkCasePrescreen workCasePrescreen;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));


        workCasePrescreen = (WorkCasePrescreen) criteria.uniqueResult();
        return workCasePrescreen;
    }

    public WorkCasePrescreen findByAppNumber(String appNumber){
        log.info("findByAppNumber : {}", appNumber);
        WorkCasePrescreen workCasePrescreen;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("appNumber", appNumber));


        workCasePrescreen = (WorkCasePrescreen) criteria.uniqueResult();
        return workCasePrescreen;
    }

    //Function for AppHeader
    public WorkCasePrescreen getWorkCasePreScreenById(long id){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("id", id));
        criteria.setFetchMode("borrowerType", FetchMode.SELECT);
        criteria.setFetchMode("customerList", FetchMode.SELECT);
        criteria.setFetchMode("stepOwner", FetchMode.SELECT);
        criteria.setFetchMode("atUserTeam", FetchMode.SELECT);
        criteria.setFetchMode("productGroup", FetchMode.SELECT);
        criteria.setFetchMode("requestType", FetchMode.SELECT);
        criteria.setFetchMode("fromUser", FetchMode.SELECT);
        criteria.setFetchMode("atUser", FetchMode.SELECT);
        criteria.setFetchMode("authorizationDOA", FetchMode.SELECT);
        criteria.setFetchMode("step", FetchMode.SELECT);
        criteria.setFetchMode("modifyBy", FetchMode.SELECT);

        WorkCasePrescreen workCasePrescreen = (WorkCasePrescreen)criteria.uniqueResult();

        return workCasePrescreen;
    }

    //find number of appeals
    public Integer getAppealResubmitCount(String refAppNumber, Integer requestType)
    {

        Criteria criteria = createCriteria();

        criteria.add(Restrictions.eq("refAppNumber",refAppNumber)).add(Restrictions.eq("requestTypeId",requestType));

        Integer caseCount = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

        return caseCount;

    }
}
