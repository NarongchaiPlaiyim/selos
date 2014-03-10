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
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
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
        criteria.setFetchMode("guarantorName", FetchMode.LAZY);
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
        criteria.setFetchMode("guarantorName", FetchMode.LAZY);
        NewGuarantorDetail newGuarantorDetail = (NewGuarantorDetail) criteria.uniqueResult();
        return newGuarantorDetail;
    }


}
