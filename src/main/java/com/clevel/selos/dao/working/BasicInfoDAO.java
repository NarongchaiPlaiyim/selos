package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.BasicInfo;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BasicInfoDAO extends GenericDAO<BasicInfo, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BasicInfoDAO() {
    }

    public BasicInfo findByWorkCaseId(long workCaseId) {
        log.info("-- findByWorkCaseId : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        BasicInfo basicInfo = (BasicInfo) criteria.uniqueResult();

        return basicInfo;
    }

    //Function for AppHeaderView
    public BasicInfo getBasicInfoByWorkCaseId(long workCaseId){
        log.debug("getBasicInfoByWorkCaseId ::: workCaseId : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.setFetchMode("workCase", FetchMode.SELECT);
        criteria.setFetchMode("productGroup", FetchMode.SELECT);
        criteria.setFetchMode("requestReason", FetchMode.SELECT);
        criteria.setFetchMode("borrowerType", FetchMode.SELECT);
        criteria.setFetchMode("specialProgram", FetchMode.SELECT);
        criteria.setFetchMode("refinanceInValue", FetchMode.SELECT);
        criteria.setFetchMode("refinanceOutValue", FetchMode.SELECT);
        criteria.setFetchMode("riskCustomerType", FetchMode.SELECT);
        criteria.setFetchMode("sbfScore", FetchMode.SELECT);
        criteria.setFetchMode("loanRequestPattern", FetchMode.SELECT);
        criteria.setFetchMode("createBy", FetchMode.SELECT);
        criteria.setFetchMode("modifyBy", FetchMode.SELECT);

        BasicInfo basicInfo = (BasicInfo) criteria.uniqueResult();

        return basicInfo;
    }
}
