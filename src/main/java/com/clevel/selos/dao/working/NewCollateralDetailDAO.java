package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateralDetail;
import com.clevel.selos.model.db.working.NewCreditFacility;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralDetailDAO extends GenericDAO<NewCollateralDetail, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public NewCollateralDetailDAO() {
    }

    public List<NewCollateralDetail> findNewCollateralByNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("findNewCollateralByNewCreditFacility ::: {}", newCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.addOrder(Order.asc("id"));
        List<NewCollateralDetail> newCollateralDetailList = (List<NewCollateralDetail>) criteria.list();
        log.info("newCollateralDetailList ::: size : {}", newCollateralDetailList.size());
        return newCollateralDetailList;
    }


}
