package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.*;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewCollateralCreditDAO extends GenericDAO<NewCollateralCredit, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    public NewCollateralCreditDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<NewCollateralCredit> getListCollRelationByNewCollateral(NewCollateral newCollateral) {
        log.info("getListCollRelationByNewCollateral. (newCollateral: {})", newCollateral);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateral", newCollateral));
        criteria.setFetchMode("newCollateral", FetchMode.SELECT);
        List<NewCollateralCredit> newCollateralRelCreditList = (List<NewCollateralCredit>)criteria.list();
        log.info("getList. (result size: {})", newCollateralRelCreditList.size());

        return newCollateralRelCreditList;

    }

    public List<NewCollateralCredit> getListCollRelationByNewCreditDetail(NewCreditDetail newCreditDetail,ProposeType proposeType) {
        log.info("getListCollRelationByNewCreditDetail. (newCreditDetail: {})", newCreditDetail);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditDetail.id", newCreditDetail.getId()));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("newCollateral", FetchMode.SELECT);
        List<NewCollateralCredit> newCollateralRelCreditList = (List<NewCollateralCredit>)criteria.list();
        log.info("getList. (result size: {})", newCollateralRelCreditList.size());

        return newCollateralRelCreditList;

    }

    public List<NewCollateralCredit> getListByWorkCase(WorkCase workCase){
        Criteria criteria = createCriteria();
        List<NewCollateralCredit> newCollateralCreditList = new ArrayList<NewCollateralCredit>();
        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
        if(newCreditFacility != null && newCreditFacility.getNewCollateralDetailList() != null && newCreditFacility.getNewCollateralDetailList().size() > 0){
            for(NewCollateral newCollateral : newCreditFacility.getNewCollateralDetailList()){
                criteria.add(Restrictions.eq("newCollateral", newCollateral));
            }
            criteria.setFetchMode("newCollateral", FetchMode.SELECT);
            newCollateralCreditList = criteria.list();
        }

        return newCollateralCreditList;
    }

    public List<NewCollateralCredit> getListByNewCreditFacility(NewCreditFacility newCreditFacility , ProposeType proposeType) {
        log.info("getListByNewCreditFacility. (newCreditFacility: {})", newCreditFacility);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("newCollateral", FetchMode.SELECT);
        return  criteria.list();
    }

    public List<NewCollateralCredit> getListByNewCreditFacilityId(long newCreditFacilityId , ProposeType proposeType) {
        log.info("getListByNewCreditFacilityId. (newCreditFacilityId: {})", newCreditFacilityId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility.id", newCreditFacilityId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("newCollateral", FetchMode.SELECT);
        return  criteria.list();
    }

    public List<NewCollateralCredit> findByNewCollateralId(final long newCollateralId) {
        log.info("-- findByNewCollateral NewCollateral.id[{}]", newCollateralId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateral.id", newCollateralId));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralCredit> newCollateralCreditList = (List<NewCollateralCredit>) criteria.list();
        log.debug("-- NewCollateralCreditList[{}]", newCollateralCreditList);
        return newCollateralCreditList;
    }
}
