package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.InsuranceInfo;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCreditFacility;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

import java.util.List;

public class InsuranceInfoDAO extends GenericDAO<InsuranceInfo, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public InsuranceInfoDAO() {

    }
    
    public List<InsuranceInfo> findInsuranceInfoByWorkCaseId(long workCaseId) {
        log.info("-- findInsuranceInfoByWorkCaseId ::: {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        List<InsuranceInfo> insuranceInfoList = (List<InsuranceInfo>) criteria.list();
        if (insuranceInfoList != null)
        	log.info("-- newCollateralDetailList ::: size : {}", insuranceInfoList.size());
        return insuranceInfoList;
    }



}
