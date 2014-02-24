package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditFacility;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

import java.util.List;

public class NewCreditDetailDAO extends GenericDAO<NewCreditDetail, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCreditDetailDAO() {}

    public List<NewCreditDetail> findNewCreditDetailByNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("findNewCreditDetailByNewCreditFacility ::: {}", newCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.addOrder(Order.asc("id"));
        List<NewCreditDetail> newCreditDetailList = (List<NewCreditDetail>) criteria.list();
        log.info("newCreditDetailList ::: size : {}", newCreditDetailList.size());
        return newCreditDetailList;
    }

    public List<NewCreditDetail> findNewCreditDetailByWorkCaseId(long workCaseId) {
        log.info("-- findNewCreditDetailByWorkCaseId ::: {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("newCreditDetail.id"));
        List<NewCreditDetail> newCreditDetailList = (List<NewCreditDetail>) criteria.list();
        log.info("-- newCreditDetailList ::: size : {}", newCreditDetailList.size());
        return newCreditDetailList;
    }

    public List<NewCreditDetail> findNewCreditDetailByWorkCaseIdForBA(long workCaseId,boolean isTopUpBA) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType",ProposeType.A));
        criteria.add(Restrictions.eq("uwDecision",DecisionType.APPROVED));
        criteria.createAlias("productProgram", "product_program")
        	.add(Restrictions.eq("product_program.ba",isTopUpBA));
        
        //TODO Add restriction for listing in BA/PA
        
       
        List<NewCreditDetail> newCreditDetailList = (List<NewCreditDetail>) criteria.list();
        return newCreditDetailList;
    }

    public List<NewCreditDetail> findNewCreditDetail(long workCaseId, ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        List<NewCreditDetail> newCreditDetailList = (List<NewCreditDetail>) criteria.list();
        return newCreditDetailList;
    }
}
