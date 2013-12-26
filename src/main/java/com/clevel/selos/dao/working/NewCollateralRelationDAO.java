package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateralDetail;
import com.clevel.selos.model.db.working.NewCollateralRelCredit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralRelationDAO extends GenericDAO<NewCollateralRelCredit, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCollateralRelationDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<NewCollateralRelCredit> getListCollRelationByNewCollateral(NewCollateralDetail newCollateralDetail) {
        log.info("getListCollRelationByNewGuarantor. (newCollateralDetail: {})", newCollateralDetail);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralDetail", newCollateralDetail));
        criteria.addOrder(Order.asc("newCollateralDetail.id"));
        List<NewCollateralRelCredit> newCollateralRelCreditList = (List<NewCollateralRelCredit>)criteria.list();
        log.info("getList. (result size: {})", newCollateralRelCreditList.size());

        return newCollateralRelCreditList;

    }

}
