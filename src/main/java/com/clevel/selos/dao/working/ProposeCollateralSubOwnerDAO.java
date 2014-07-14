package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeCollateralInfoSub;
import com.clevel.selos.model.db.working.ProposeCollateralSubOwner;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProposeCollateralSubOwnerDAO extends GenericDAO<ProposeCollateralSubOwner, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ProposeCollateralSubOwnerDAO() {}

    public List<ProposeCollateralSubOwner> findCollSubOwnerByCollSub(ProposeCollateralInfoSub proposeCollateralSub) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateralSub", proposeCollateralSub));
        criteria.addOrder(Order.asc("id"));
        criteria.setFetchMode("customer", FetchMode.LAZY);
        List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList = (List<ProposeCollateralSubOwner>)criteria.list();

        return proposeCollateralSubOwnerList;
    }

    public List<ProposeCollateralSubOwner> findByWorkCaseId(long workCaseId, ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList = criteria.list();

        return proposeCollateralSubOwnerList;
    }

    public List<ProposeCollateralSubOwner> findByCustomerId(long customerId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customer.id", customerId));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList = criteria.list();

        return proposeCollateralSubOwnerList;
    }

    public List<ProposeCollateralSubOwner> findByNewCollateralSubId(final long newCollateralSubId) {
        log.info("-- findByNewCollateralSubId NewCollateralSubId.id[{}]", newCollateralSubId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateralSub.id", newCollateralSubId));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralSubOwner> newCollateralSubOwnerList = (List<ProposeCollateralSubOwner>) criteria.list();
        log.debug("--NewCollateralSubOwnerList[{}]", newCollateralSubOwnerList);
        return newCollateralSubOwnerList;
    }

    /*public List<NewCollateralSubOwner> getListByWorkCase(WorkCase workCase){
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
        Criteria criteria = createCriteria();
        if(newCreditFacility != null && newCreditFacility.getNewCollateralDetailList() != null && newCreditFacility.getNewCollateralDetailList().size() > 0){
            for(NewCollateral newCollateral : newCreditFacility.getNewCollateralDetailList()){
                for(NewCollateralHead newCollateralHead : newCollateral.getNewCollateralHeadList()){
                    for(NewCollateralSub newCollateralSub : newCollateralHead.getNewCollateralSubList()){
                        criteria.add(Restrictions.eq("newCollateralSub", newCollateralSub));
                    }

                }
            }
        }
        criteria.setFetchMode("customer", FetchMode.LAZY);
        List<NewCollateralSubOwner> newCollateralSubOwnerList = criteria.list();

        return newCollateralSubOwnerList;
    }
*//*

    public List<NewCollateralSubOwner> getListByWorkCase(WorkCase workCase,ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("customer", FetchMode.LAZY);
        List<NewCollateralSubOwner> newCollateralSubOwnerList = criteria.list();
        return newCollateralSubOwnerList;
    }*/
}
