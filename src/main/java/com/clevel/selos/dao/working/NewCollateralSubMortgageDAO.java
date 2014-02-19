package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralSubMortgageDAO extends GenericDAO<NewCollateralSubMortgage, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    public NewCollateralSubMortgageDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<NewCollateralSubMortgage> getListNewCollateralSubMortgage(NewCollateralSub newCollateralSub) {
        log.info("getListNewCollateralSubMortgage. (v: {})", newCollateralSub);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSub", newCollateralSub));
        criteria.addOrder(Order.asc("newCollateralSub.id"));
        List<NewCollateralSubMortgage> newCollateralSubMortgages = (List<NewCollateralSubMortgage>)criteria.list();
        log.info("getList. (result size: {})", newCollateralSubMortgages.size());

        return newCollateralSubMortgages;

    }


    public List<NewCollateralSubMortgage> getListByWorkCase(WorkCase workCase){
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
        List<NewCollateralSubMortgage> newCollateralSubMortgageList = criteria.list();

        return newCollateralSubMortgageList;
    }

}
