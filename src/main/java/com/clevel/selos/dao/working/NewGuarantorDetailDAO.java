package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.NewGuarantorDetail;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;

import javax.inject.Inject;

import java.util.List;

public class NewGuarantorDetailDAO extends GenericDAO<NewGuarantorDetail, Long> {
    @Inject
    @SELOS
    Logger log;

    public NewGuarantorDetailDAO() {}

    public List<NewGuarantorDetail> findNewGuarantorByNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("findNewCreditDetailByNewCreditFacility ::: {}", newCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.addOrder(Order.asc("id"));
        List<NewGuarantorDetail> newGuarantorDetails = (List<NewGuarantorDetail>) criteria.list();
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
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
}
