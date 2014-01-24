package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateralSub;
import com.clevel.selos.model.db.working.NewCollateralSubRelated;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralSubRelatedDAO extends GenericDAO<NewCollateralSubRelated, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCollateralSubRelatedDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<NewCollateralSubRelated> getListNewCollateralSubRelate(NewCollateralSub newCollateralSub) {
        log.info("getListNewCollateralSubRelate. (newCollateralSub: {})", newCollateralSub);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSub", newCollateralSub));
        criteria.addOrder(Order.asc("newCollateralSub.id"));
        List<NewCollateralSubRelated> newCollateralSubRelates = (List<NewCollateralSubRelated>)criteria.list();
        log.info("getList. (result size: {})", newCollateralSubRelates.size());

        return newCollateralSubRelates;

    }

}
