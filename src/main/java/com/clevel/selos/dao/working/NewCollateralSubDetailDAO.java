package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateralHeadDetail;
import com.clevel.selos.model.db.working.NewCollateralSubDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralSubDetailDAO extends GenericDAO<NewCollateralSubDetail, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public NewCollateralSubDetailDAO() {
    }

    public List<NewCollateralSubDetail> getAllNewCollateralSubDetail(NewCollateralHeadDetail newCollateralHeadDetail) {

        log.info("getAllNewCollateralSubDetailByNewCollHeadDetail. (newCollateralDetail: {})", newCollateralHeadDetail);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralHeadDetail", newCollateralHeadDetail));
        criteria.addOrder(Order.asc("newCollateralHeadDetail.id"));
        List<NewCollateralSubDetail> newCollateralSubDetails = (List<NewCollateralSubDetail>) criteria.list();
        log.info("getList. (result size: {})", newCollateralSubDetails.size());

        return newCollateralSubDetails;

    }
}
