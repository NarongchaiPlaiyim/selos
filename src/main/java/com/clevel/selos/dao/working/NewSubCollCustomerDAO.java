package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateralSubCustomer;
import com.clevel.selos.model.db.working.NewCollateralSubDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewSubCollCustomerDAO extends GenericDAO<NewCollateralSubCustomer, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewSubCollCustomerDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<NewCollateralSubCustomer> getListNewCollateralSubCustomer(NewCollateralSubDetail newCollateralSubDetail) {
        log.info("getListNewCollateralSubCustomer. (newCollateralSubDetail: {})", newCollateralSubDetail.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSubDetail", newCollateralSubDetail));
        criteria.addOrder(Order.asc("newCollateralSubDetail.id"));
        List<NewCollateralSubCustomer> newCollateralSubCustomerList = (List<NewCollateralSubCustomer>)criteria.list();
        log.info("getList. (result size: {})", newCollateralSubCustomerList.size());

        return newCollateralSubCustomerList;

    }

}
