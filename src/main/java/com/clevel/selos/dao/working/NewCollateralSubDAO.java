package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralSubDAO extends GenericDAO<NewCollateralSub, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public NewCollateralSubDAO() {}

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;

    public List<NewCollateralSub> getAllNewSubCollateral(long workCaseId) {
        log.info("findAllSubCollThisWorkCase :: start :: {}", workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        List<NewCollateralSub> newCollateralSubDetailList = null;
        if (workCase != null) {
            NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
            if (newCreditFacility != null) {
                if (newCreditFacility.getNewCollateralDetailList() != null) {
                    log.info("newCreditFacility.getNewCollateralDetailList() :: {}", newCreditFacility.getNewCollateralDetailList().size());
                    for (NewCollateral newCollateralDetail : newCreditFacility.getNewCollateralDetailList()) {
                        log.info("newCollateralDetail :: id :: {}", newCollateralDetail.getId());
                        if (newCollateralDetail.getNewCollateralHeadList() != null) {
                            log.info("newCollateralDetail.getNewCollateralHeadList() :: {}", newCollateralDetail.getNewCollateralHeadList().size());
                            for (NewCollateralHead newCollateralHead : newCollateralDetail.getNewCollateralHeadList()) {
                                log.info("newCollateralHeadDetail .id:: {}", newCollateralHead.getId());
                                newCollateralSubDetailList = getAllNewSubCollateral(newCollateralHead);
                            }
                        }
                    }
                }
            }
        }

        log.info("newCollateralSubList end :::");
        return newCollateralSubDetailList;
    }


    public List<NewCollateralSub> getAllNewSubCollateral(NewCollateralHead newCollateralHead) {
        log.info("getAllNewSubCollateral. (newCollateralHead: {})", newCollateralHead);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralHead", newCollateralHead));
        criteria.addOrder(Order.asc("newCollateralHead.id"));
        List<NewCollateralSub> newCollateralSubDetails = (List<NewCollateralSub>) criteria.list();
        log.info("getList. (result size: {})", newCollateralSubDetails.size());

        return newCollateralSubDetails;

    }

    public List<NewCollateralSub> findByNewCollateralHead(NewCollateralHead newCollateralHead) {
        log.info("getAllNewSubCollateral. (newCollateralHead: {})", newCollateralHead);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralHead", newCollateralHead));
        criteria.addOrder(Order.asc("newCollateralHead.id"));
        List<NewCollateralSub> newCollateralSubDetails = (List<NewCollateralSub>) criteria.list();
        log.info("getList. (result size: {})", newCollateralSubDetails.size());

        return newCollateralSubDetails;

    }

    public List<NewCollateralSub> findByNewCollateralHeadId(final long newCollateralHeadId) {
        log.info("-- findByNewCollateralHeadId : {}", newCollateralHeadId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralHead.id", newCollateralHeadId));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralSub> newCollateralSubDetails = (List<NewCollateralSub>) criteria.list();
        log.info("getList. (result size: {})", newCollateralSubDetails.size());

        return newCollateralSubDetails;
    }
    
    @SuppressWarnings("unchecked")
    public List<NewCollateralSub> findForMortgageSummary(long workCaseId) {
    	Criteria criteria = createCriteria();
    	criteria.createAlias("newCollateralHead", "head");
    	criteria.createAlias("head.newCollateral", "main");
    	
    	criteria.add(Restrictions.eq("main.workCase.id", workCaseId));
    	criteria.add(Restrictions.eq("main.proposeType", ProposeType.A));
    	criteria.add(Restrictions.eq("main.uwDecision", DecisionType.APPROVED));
    	criteria.addOrder(Order.asc("id"));
    	return criteria.list();
    }

    public NewCollateralSub findBySubId(String subId){
        log.debug("findBySubId ::: subId : {}", subId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("subId", subId));
        NewCollateralSub newCollateralSub = (NewCollateralSub) criteria.uniqueResult();

        return newCollateralSub;
    }
}
