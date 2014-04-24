package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.NewGuarantorDetail;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewGuarantorDetailDAO extends GenericDAO<NewGuarantorDetail, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public NewGuarantorDetailDAO() {}

    public List<NewGuarantorDetail> findNewGuarantorByNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("findNewGuarantorByNewCreditFacility ::: {}", newCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.addOrder(Order.asc("id"));
        criteria.setFetchMode("guarantorName", FetchMode.SELECT);
        List<NewGuarantorDetail> newGuarantorDetails = (List<NewGuarantorDetail>)criteria.list();
        log.info("newGuarantorDetails ::: size : {}", newGuarantorDetails.size());
        return newGuarantorDetails;
    }

    @SuppressWarnings("unchecked")
    public List<NewGuarantorDetail> findGuarantorByProposeType(long workCaseId,ProposeType proposeType) {
    	Criteria criteria = createCriteria();
    	criteria.createAlias("guarantorName", "cus");
    	criteria.createAlias("cus.workCase", "wrk");
        criteria.add(Restrictions.eq("wrk.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType",proposeType));
        criteria.add(Restrictions.eq("uwDecision",DecisionType.APPROVED));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
    @SuppressWarnings("unchecked")
    public List<Long> findGuarantorIdByProposeType(long workCaseId,ProposeType proposeType) {
    	Criteria criteria = createCriteria();
    	criteria.createAlias("guarantorName", "cus");
    	criteria.createAlias("cus.workCase", "wrk");
        criteria.add(Restrictions.eq("wrk.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType",proposeType));
        criteria.add(Restrictions.eq("uwDecision",DecisionType.APPROVED));
        criteria.addOrder(Order.asc("id"));
        criteria.setProjection(Projections.distinct(Projections.property("id")));
        List<Object> result = criteria.list();
        if (result == null || result.isEmpty())
        	return Collections.emptyList();
        List<Long> rtnData = new ArrayList<Long>();
        for (Object data : result) {
        	if (data == null)
        		continue;
        	rtnData.add((Long)data);
        }
        return rtnData;
    }
    public List<NewGuarantorDetail> findNewGuarantorByNewCreditFacId(long newCreditFacId, ProposeType proposeType) {
        Criteria criteria = createCriteria();
        criteria.createAlias("guarantorName", "cus");
        criteria.createAlias("cus.workCase", "wrk");
        criteria.add(Restrictions.eq("newCreditFacility.id", newCreditFacId));
        criteria.add(Restrictions.eq("proposeType",proposeType));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }



    public NewGuarantorDetail findGuarantorById(long newGuarantorId,ProposeType proposeType) {
        log.info("findById ::: {}", newGuarantorId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("id",newGuarantorId));
        criteria.add(Restrictions.eq("proposeType",proposeType));
        criteria.setFetchMode("guarantorName", FetchMode.SELECT);
        NewGuarantorDetail newGuarantorDetail = (NewGuarantorDetail) criteria.uniqueResult();
        return newGuarantorDetail;
    }

    public List<NewGuarantorDetail> findByCustomerId(long customerId) {
        log.info("findByCustomerId : {}", customerId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("guarantorName.id", customerId));
        criteria.addOrder(Order.asc("id"));
        List<NewGuarantorDetail> newGuarantorDetails = criteria.list();

        return newGuarantorDetails;
    }
}
