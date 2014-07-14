package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeCollateralInfoSub;
import com.clevel.selos.model.db.working.ProposeCollateralSubRelated;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProposeCollateralSubRelatedDAO extends GenericDAO<ProposeCollateralSubRelated, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ProposeCollateralSubRelatedDAO() {}

    public List<ProposeCollateralSubRelated> findCollSubRelatedByCollSub(ProposeCollateralInfoSub proposeCollateralSub) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateralSub", proposeCollateralSub));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList = (List<ProposeCollateralSubRelated>)criteria.list();

        return proposeCollateralSubRelatedList;
    }

    public List<ProposeCollateralSubRelated> findByWorkCaseId(long workCaseId, ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList = criteria.list();
        return proposeCollateralSubRelatedList;
    }

    public List<ProposeCollateralSubRelated> findByMainCollSubId(long mainCollSubId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSub.id", mainCollSubId));
        List<ProposeCollateralSubRelated> newCollateralSubRelatedList = criteria.list();
        return newCollateralSubRelatedList;
    }

    public List<ProposeCollateralSubRelated> findByNewCollateralSubId(final long newCollateralSubId) {
        log.info("-- findByNewCollateralSubId NewCollateralSubId.id[{}]", newCollateralSubId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateralSub.id", newCollateralSubId));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralSubRelated> newCollateralSubRelatedList = (List<ProposeCollateralSubRelated>) criteria.list();
        log.debug("--NewCollateralSubRelatedList[{}]", newCollateralSubRelatedList);
        return newCollateralSubRelatedList;
    }

   /* public List<NewCollateralSubRelated> getListByWorkCase(WorkCase workCase){
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
        List<NewCollateralSubRelated> newCollateralSubList = criteria.list();

        return newCollateralSubList;
    }
*//*

    public List<NewCollateralSubRelated> getListByWorkCase(WorkCase workCase,ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        List<NewCollateralSubRelated> newCollateralSubRelatedList = criteria.list();
        return newCollateralSubRelatedList;
    }

    public List<NewCollateralSubRelated> findByMainCollSubId(long mainCollSubId,ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSub.id", mainCollSubId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        List<NewCollateralSubRelated> newCollateralSubRelatedList = criteria.list();
        return newCollateralSubRelatedList;
    }

    public List<NewCollateralSubRelated> findByMainCollSubId(long mainCollSubId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSub.id", mainCollSubId));
        List<NewCollateralSubRelated> newCollateralSubRelatedList = criteria.list();
        return newCollateralSubRelatedList;
    }*/


}