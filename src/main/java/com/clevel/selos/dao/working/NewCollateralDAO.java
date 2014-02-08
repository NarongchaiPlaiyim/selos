package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralDAO extends GenericDAO<NewCollateral, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public NewCollateralDAO() {

    }

    public void persistProposeTypeA(final List<NewCollateral> newCollateralList){
        log.info("-- persistProposeTypeA ::: {}", newCollateralList.size());
        for (NewCollateral newCollateral : newCollateralList) {
            newCollateral.setAppraisalRequest(2);
            newCollateral.setProposeType("A");
        }
        persist(newCollateralList);
    }

    public List<NewCollateral> findNewCollateralByNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("-- findNewCollateralByNewCreditFacility ::: {}", newCreditFacility.toString());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateral> newCollateralList = (List<NewCollateral>) criteria.list();
        log.info("-- List<NewCollateral> ::: size : {}", newCollateralList.size());
        return newCollateralList;
    }

    public List<NewCollateral> findNewCollateralByTypeP(NewCreditFacility newCreditFacility) {
        log.info("-- findNewCollateralByTypeP ::: {}", newCreditFacility.toString());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.add(Restrictions.eq("proposeType", "P"));
        criteria.add(Restrictions.ne("appraisalRequest", 2));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateral> newCollateralDetailList = (List<NewCollateral>) criteria.list();
        log.info("newCollateralDetailList ::: size : {}", newCollateralDetailList.size());
        return newCollateralDetailList;
    }

    public void updateAppraisalRequest(final List<NewCollateral> newCollateralList){
        log.info("-- updateAppraisalRequest ::: size : {}", newCollateralList.size());
        long id;
        for(NewCollateral newCollateral : newCollateralList){
            id = newCollateral.getId();
            if(id != 0){
                newCollateral.setAppraisalRequest(2);
                persist(newCollateral);
                log.debug("-- NewCollateral(id : {}) updated", id);
            }
        }
    }

    public List<NewCollateral> findNewCollateralByTypeA(NewCreditFacility newCreditFacility) {
        log.info("-- findNewCollateralByTypeA ::: {}", newCreditFacility.toString());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.add(Restrictions.eq("proposeType", "A"));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateral> newCollateralDetailList = (List<NewCollateral>) criteria.list();
        log.info("-- List<NewCollateral> ::: size : {}", newCollateralDetailList.size());
        return newCollateralDetailList;
    }

    public void updateAppraisalFlag(final NewCollateral newCollateral) {
        log.debug("-- updateAppraisalFlag()");
        long id = newCollateral.getId();
        if(isRecordExist(Restrictions.eq("id", id))){
            log.debug("id : {} is exist", id);
            newCollateral.setAppraisalRequest(2);
            save(newCollateral);
            log.debug("-- NewCollateral(id : {}) has Updated", id);
        }
    }


}
