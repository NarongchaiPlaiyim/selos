package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCollateralHead;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralHeadDAO extends GenericDAO<NewCollateralHead, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCollateralHeadDAO() {}

    public List<NewCollateralHead> findByNewCollateral(NewCollateral newCollateral) {
        log.info("findByNewCollateral ::: {}", newCollateral);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateral", newCollateral));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralHead> newCollateralHeadDetails = (List<NewCollateralHead>) criteria.list();
        log.info("newCollateralHeadDetails ::: size : {}", newCollateralHeadDetails.size());
        return newCollateralHeadDetails;
    }

    public List<NewCollateralHead> findByNewCollateralId(final long newCollateralId) {
        log.info("-- findByNewCollateral ::: {}", newCollateralId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateral.id", newCollateralId));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralHead> newCollateralHeadDetails = (List<NewCollateralHead>) criteria.list();
        return newCollateralHeadDetails;
    }

}
