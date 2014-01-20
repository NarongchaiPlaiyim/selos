package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateralSubDetail;
import com.clevel.selos.model.db.working.NewCollateralSubRelate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewSubCollRelateDAO extends GenericDAO<NewCollateralSubRelate, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewSubCollRelateDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<NewCollateralSubRelate> getListNewCollateralSubRelate(NewCollateralSubDetail newCollateralSubDetail) {
        log.info("getListNewCollateralSubRelate. (newCollateralSubDetail: {})", newCollateralSubDetail.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSubDetail", newCollateralSubDetail));
        criteria.addOrder(Order.asc("newCollateralSubDetail.id"));
        List<NewCollateralSubRelate> newCollateralSubRelates = (List<NewCollateralSubRelate>)criteria.list();
        log.info("getList. (result size: {})", newCollateralSubRelates.size());

        return newCollateralSubRelates;

    }

}
