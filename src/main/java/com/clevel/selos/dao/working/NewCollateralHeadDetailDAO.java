package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateralDetail;
import com.clevel.selos.model.db.working.NewCollateralHeadDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralHeadDetailDAO extends GenericDAO<NewCollateralHeadDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCollateralHeadDetailDAO() {}

    public List<NewCollateralHeadDetail> findByNewCollateralDetail(NewCollateralDetail newCollateralDetail) {
        log.info("findByNewCollateralDetail ::: {}", newCollateralDetail);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralDetail", newCollateralDetail));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralHeadDetail> newCollateralHeadDetails = (List<NewCollateralHeadDetail>) criteria.list();
        log.info("newCollateralHeadDetails ::: size : {}", newCollateralHeadDetails.size());
        return newCollateralHeadDetails;
    }
}
