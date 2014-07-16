package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeCollateralInfo;
import com.clevel.selos.model.db.working.ProposeLine;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProposeCollateralInfoDAO extends GenericDAO<ProposeCollateralInfo, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ProposeCollateralInfoDAO() {
    }

    public List<ProposeCollateralInfo> findNewCollateralByNewCreditFacility(ProposeLine proposeLine) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeLine", proposeLine));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfo> proposeCollateralInfoList = (List<ProposeCollateralInfo>) criteria.list();

        return proposeCollateralInfoList;
    }

    public List<ProposeCollateralInfo> findNewCollateral(long workCaseId, ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfo> newCollateralDetailList = (List<ProposeCollateralInfo>) criteria.list();

        return newCollateralDetailList;
    }

    public List<ProposeCollateralInfo> findNewCollateralByTypeP(ProposeLine newCreditFacility) {
        log.info("-- findNewCollateralByTypeP ::: {}", newCreditFacility.toString());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeLine", newCreditFacility));
        criteria.add(Restrictions.eq("proposeType", ProposeType.P));
        criteria.add(Restrictions.ne("appraisalRequest", 2));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfo> newCollateralDetailList = (List<ProposeCollateralInfo>) criteria.list();
        log.info("newCollateralDetailList ::: size : {}", newCollateralDetailList.size());
        return newCollateralDetailList;
    }

    public List<ProposeCollateralInfo> findNewCollateralByTypeA2(ProposeLine newCreditFacility) {
        log.info("-- findNewCollateralByTypeA2 ::: {}", newCreditFacility.toString());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeLine", newCreditFacility));
        criteria.add(Restrictions.eq("proposeType", ProposeType.A));    //2
        criteria.add(Restrictions.eq("appraisalRequest", 2));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfo> newCollateralDetailList = (List<ProposeCollateralInfo>) criteria.list();
        log.info("newCollateralDetailList ::: size : {}", newCollateralDetailList.size());
        return newCollateralDetailList;
    }

    public boolean isExist(final long id) {
        boolean result;
        log.debug("-- isExist NewCollateral.id[{}]", id);
        result = isRecordExist(Restrictions.eq("id", id));
        log.debug("-- Result[{}]", result);
        return result;
    }

    public List<ProposeCollateralInfo> findNewCollateralByTypeA(ProposeLine newCreditFacility) {
        log.info("-- findNewCollateralByTypeA ::: {}", newCreditFacility.toString());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeLine", newCreditFacility));
        criteria.add(Restrictions.eq("proposeType", ProposeType.A));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfo> newCollateralDetailList = (List<ProposeCollateralInfo>) criteria.list();
        log.info("-- List<NewCollateral> ::: size : {}", newCollateralDetailList.size());
        return newCollateralDetailList;
    }

    public List<ProposeCollateralInfo> findNewCollateralByTypeA(long workCaseId) {
        log.info("-- findNewCollateralByWorkCaseId ::: {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType", ProposeType.A));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfo> newCollateralDetailList = (List<ProposeCollateralInfo>) criteria.list();
        log.info("-- List<NewCollateral> ::: size : {}", newCollateralDetailList.size());
        return newCollateralDetailList;
    }
 }