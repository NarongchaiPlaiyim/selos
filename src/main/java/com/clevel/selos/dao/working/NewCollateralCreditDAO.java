package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCollateralCredit;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
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
        log.info("getListCollRelationByNewGuarantor. (newCollateral: {})", newCollateral);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateral", newCollateral));
        criteria.addOrder(Order.asc("newCollateral.id"));
        List<NewCollateralCredit> newCollateralRelCreditList = (List<NewCollateralCredit>)criteria.list();
        log.info("getList. (result size: {})", newCollateralRelCreditList.size());

        return newCollateralRelCreditList;

    }

    public List<NewCollateralCredit> getListCollRelationByNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("getListCollRelationByNewCreditFacility. (newCreditFacility: {})", newCreditFacility);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
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
            newCollateralCreditList = criteria.list();
        }

        return newCollateralCreditList;
    }


}
