package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateralSubDetail;
import com.clevel.selos.model.db.working.NewCollateralSubMortgage;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewSubCollMortgageDAO extends GenericDAO<NewCollateralSubMortgage, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewSubCollMortgageDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<NewCollateralSubMortgage> getListNewCollateralSubMortgage(NewCollateralSubDetail newCollateralSubDetail) {
        log.info("getListNewCollateralSubMortgage. (newCollateralSubDetail: {})", newCollateralSubDetail.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSubDetail", newCollateralSubDetail));
        criteria.addOrder(Order.asc("newCollateralSubDetail.id"));
        List<NewCollateralSubMortgage> newCollateralSubMortgages = (List<NewCollateralSubMortgage>)criteria.list();
        log.info("getList. (result size: {})", newCollateralSubMortgages.size());

        return newCollateralSubMortgages;

    }

}
