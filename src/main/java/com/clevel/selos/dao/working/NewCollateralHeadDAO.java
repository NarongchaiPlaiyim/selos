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

    public List<NewCollateralHead> findByNewCollateralDetail(NewCollateral newCollateralDetail) {
        log.info("findByNewCollateralDetail ::: {}", newCollateralDetail);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralDetail", newCollateralDetail));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralHead> newCollateralHeadDetails = (List<NewCollateralHead>) criteria.list();
        log.info("newCollateralHeadDetails ::: size : {}", newCollateralHeadDetails.size());
        return newCollateralHeadDetails;
    }
}
