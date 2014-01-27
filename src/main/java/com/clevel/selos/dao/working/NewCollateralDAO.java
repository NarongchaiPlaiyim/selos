package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCreditFacility;
import org.hibernate.Criteria;
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

    public List<NewCollateral> findNewCollateralByNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("findNewCollateralByNewCreditFacility ::: {}", newCreditFacility);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateral> newCollateralDetailList = (List<NewCollateral>) criteria.list();
        log.info("newCollateralDetailList ::: size : {}", newCollateralDetailList.size());
        return newCollateralDetailList;
    }

    public void updateAppraisalFlag(final NewCollateral newCollateral) {
        log.debug("-- updateAppraisalFlag()");
        long id = newCollateral.getId();
        if(isRecordExist(Restrictions.eq("id", id))){
            save(newCollateral);
            log.debug("-- NewCollateral(id : {}) has Updated", id);
        }
    }


}
