package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.GuarantorCategory;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeGuarantorInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProposeGuarantorInfoDAO extends GenericDAO<ProposeGuarantorInfo, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ProposeGuarantorInfoDAO() {
    }

    public List<ProposeGuarantorInfo> findByCustomerId(long customerId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("guarantorName.id", customerId));
        criteria.addOrder(Order.asc("id"));
        List<ProposeGuarantorInfo> proposeGuarantorInfoList = criteria.list();

        return proposeGuarantorInfoList;
    }

    public List<ProposeGuarantorInfo> findGuarantorByProposeType(long workCaseId,ProposeType proposeType) {
        Criteria criteria = createCriteria();
        criteria.createAlias("guarantorName", "cus");
        criteria.createAlias("cus.workCase", "wrk");
        criteria.add(Restrictions.eq("wrk.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType",proposeType));
        criteria.add(Restrictions.eq("uwDecision", DecisionType.APPROVED));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<ProposeGuarantorInfo> findApprovedTCGGuarantor(long workCaseId){
        Criteria criteria = createCriteria();
        criteria.createAlias("guarantorName", "cus");
        criteria.createAlias("cus.workCase", "wrk");
        criteria.add(Restrictions.eq("wrk.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType", ProposeType.A));
        criteria.add(Restrictions.eq("uwDecision", DecisionType.APPROVED));
        criteria.add(Restrictions.eq("guarantorCategory", GuarantorCategory.TCG));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

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
}
