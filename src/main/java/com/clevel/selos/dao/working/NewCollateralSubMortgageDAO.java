package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateralSub;
import com.clevel.selos.model.db.working.NewCollateralSubMortgage;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralSubMortgageDAO extends GenericDAO<NewCollateralSubMortgage, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCollateralSubMortgageDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<NewCollateralSubMortgage> getListNewCollateralSubMortgage(NewCollateralSub newCollateralSub) {
        log.info("getListNewCollateralSubMortgage. (v: {})", newCollateralSub);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSub", newCollateralSub));
        criteria.addOrder(Order.asc("newCollateralSub.id"));
        List<NewCollateralSubMortgage> newCollateralSubMortgages = (List<NewCollateralSubMortgage>)criteria.list();
        log.info("getList. (result size: {})", newCollateralSubMortgages.size());

        return newCollateralSubMortgages;

    }

}
