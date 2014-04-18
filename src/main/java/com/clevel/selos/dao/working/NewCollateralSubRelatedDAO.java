package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.NewCollateralSub;
import com.clevel.selos.model.db.working.NewCollateralSubRelated;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralSubRelatedDAO extends GenericDAO<NewCollateralSubRelated, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    public NewCollateralSubRelatedDAO() {}

    @SuppressWarnings("unchecked")
    public List<NewCollateralSubRelated> getListNewCollateralSubRelate(NewCollateralSub newCollateralSub) {
        log.info("getListNewCollateralSubRelate. (newCollateralSub: {})", newCollateralSub);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSub", newCollateralSub));
        criteria.addOrder(Order.asc("newCollateralSub.id"));
        List<NewCollateralSubRelated> newCollateralSubRelates = (List<NewCollateralSubRelated>)criteria.list();
        log.info("getList. (result size: {})", newCollateralSubRelates.size());

        return newCollateralSubRelates;

    }
/*

    public List<NewCollateralSubRelated> getListByWorkCase(WorkCase workCase){
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
*/

    public List<NewCollateralSubRelated> getListByWorkCase(WorkCase workCase,ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        //criteria.setFetchMode("newCollateralHead", FetchMode.LAZY);
        //criteria.setFetchMode("newCollateralSubMortgageList", FetchMode.LAZY);
        //criteria.setFetchMode("newCollateralSubOwnerList", FetchMode.LAZY);
        List<NewCollateralSubRelated> newCollateralSubRelatedList = criteria.list();
        return newCollateralSubRelatedList;
    }


}
