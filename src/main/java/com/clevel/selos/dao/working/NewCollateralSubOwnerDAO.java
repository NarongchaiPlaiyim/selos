package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.NewCollateralSub;
import com.clevel.selos.model.db.working.NewCollateralSubOwner;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralSubOwnerDAO extends GenericDAO<NewCollateralSubOwner, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;

    @Inject
    public NewCollateralSubOwnerDAO() {}

    @SuppressWarnings("unchecked")
    public List<NewCollateralSubOwner> getListNewCollateralSubCustomer(NewCollateralSub newCollateralSub) {
        log.info("getListNewCollateralSubCustomer. (newCollateralSub: {})", newCollateralSub);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSub", newCollateralSub));
        criteria.setFetchMode("customer", FetchMode.LAZY);
        List<NewCollateralSubOwner> newCollateralSubCustomerList = (List<NewCollateralSubOwner>)criteria.list();
        log.info("getList. (result size: {})", newCollateralSubCustomerList.size());

        return newCollateralSubCustomerList;

    }

    public List<NewCollateralSubOwner> findByNewCollateralSubId(final long newCollateralSubId) {
        log.info("-- findByNewCollateralSubId NewCollateralSubId.id[{}]", newCollateralSubId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSub.id", newCollateralSubId));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralSubOwner> newCollateralSubOwnerList = (List<NewCollateralSubOwner>) criteria.list();
        log.debug("--NewCollateralSubOwnerList[{}]", newCollateralSubOwnerList);
        return newCollateralSubOwnerList;
    }
/*

    public List<NewCollateralSubOwner> getListByWorkCase(WorkCase workCase){
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
*/

    public List<NewCollateralSubOwner> getListByWorkCase(WorkCase workCase,ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        criteria.setFetchMode("customer", FetchMode.LAZY);
        List<NewCollateralSubOwner> newCollateralSubOwnerList = criteria.list();
        return newCollateralSubOwnerList;
    }

    public List<NewCollateralSubOwner> findByCustomerId(long customerId) {
        log.info("findByCustomerId : {}", customerId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customer.id", customerId));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralSubOwner> newCollateralSubOwners = criteria.list();

        return newCollateralSubOwners;
    }
}
