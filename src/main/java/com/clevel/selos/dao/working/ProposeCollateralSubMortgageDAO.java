package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeCollateralInfoSub;
import com.clevel.selos.model.db.working.ProposeCollateralSubMortgage;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProposeCollateralSubMortgageDAO extends GenericDAO<ProposeCollateralSubMortgage, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ProposeCollateralSubMortgageDAO() {
    }

    public List<ProposeCollateralSubMortgage> findCollSubMortgageByCollSub(ProposeCollateralInfoSub proposeCollateralSub) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateralSub", proposeCollateralSub));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralSubMortgage> proposeCollateralSubMortgageList = (List<ProposeCollateralSubMortgage>)criteria.list();

        return proposeCollateralSubMortgageList;
    }

   public List<ProposeCollateralSubMortgage> findByNewCollateralSubId(final long newCollateralSubId) {
        log.info("-- findByNewCollateralSubId NewCollateralSubId.id[{}]", newCollateralSubId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateralSub.id", newCollateralSubId));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralSubMortgage> newCollateralSubMortgageList = (List<ProposeCollateralSubMortgage>) criteria.list();
        log.debug("--NewCollateralSubMortgageList[{}]", newCollateralSubMortgageList);
        return newCollateralSubMortgageList;
    }

     /*public List<NewCollateralSubMortgage> getListByWorkCase(WorkCase workCase , ProposeType proposeType){
//        NewCreditFacility newCreditFacility = newCreditFacilityDAO.findByWorkCase(workCase);
        Criteria criteria = createCriteria();
//        if(newCreditFacility != null && newCreditFacility.getNewCollateralDetailList() != null && newCreditFacility.getNewCollateralDetailList().size() > 0){
//            for(NewCollateral newCollateral : newCreditFacility.getNewCollateralDetailList()){
//                for(NewCollateralHead newCollateralHead : newCollateral.getNewCollateralHeadList()){
//                    for(NewCollateralSub newCollateralSub : newCollateralHead.getNewCollateralSubList()){
                        criteria.add(Restrictions.eq("workCase", workCase));
                        criteria.add(Restrictions.eq("proposeType", proposeType));
//                    }
//                }
//            }
//        }
        List<NewCollateralSubMortgage> newCollateralSubMortgageList = criteria.list();

        return newCollateralSubMortgageList;
    }*/

    public List<ProposeCollateralSubMortgage> findByWorkCaseId(long workCaseId, ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        List<ProposeCollateralSubMortgage> proposeCollateralSubMortgageList = criteria.list();
        return proposeCollateralSubMortgageList;
    }
}
