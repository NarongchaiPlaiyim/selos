package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeCollateralInfoSub;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProposeCollateralInfoSubDAO extends GenericDAO<ProposeCollateralInfoSub, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ProposeCollateralInfoSubDAO() {}

    public List<ProposeCollateralInfoSub> findForMortgageSummary(long workCaseId) {
        Criteria criteria = createCriteria();
        criteria.createAlias("proposeCollateralHead", "head");
        criteria.createAlias("head.proposeCollateral", "main");

        criteria.add(Restrictions.eq("main.workCase.id", workCaseId));
        criteria.add(Restrictions.eq("main.proposeType", ProposeType.A));
        criteria.add(Restrictions.eq("main.uwDecision", DecisionType.APPROVED));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<ProposeCollateralInfoSub> findByNewCollateralHeadId(final long newCollateralHeadId) {
        log.info("-- findByNewCollateralHeadId : {}", newCollateralHeadId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateralHead.id", newCollateralHeadId));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfoSub> newCollateralSubDetails = (List<ProposeCollateralInfoSub>) criteria.list();
        log.info("getList. (result size: {})", newCollateralSubDetails.size());

        return newCollateralSubDetails;
    }

    public ProposeCollateralInfoSub findBySubId(String subId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("subId", subId));
        List<ProposeCollateralInfoSub> proposeCollateralInfoSubList = (List<ProposeCollateralInfoSub>) criteria.list();

        if(proposeCollateralInfoSubList != null && proposeCollateralInfoSubList.size() > 0){
            log.debug("proposeCollateralInfoSubList.size() :: {}", proposeCollateralInfoSubList.size());
            log.debug("Return ProposeCollateralInfoSub :: {}", proposeCollateralInfoSubList.get(0).getId());
            return proposeCollateralInfoSubList.get(0);
        }
        log.debug("Return Null");
        return null;
    }
}
