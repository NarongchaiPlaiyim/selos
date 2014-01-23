package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCollateralSub;
import com.clevel.selos.model.db.working.NewCollateralSubOwner;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCollateralSubOwnerDAO extends GenericDAO<NewCollateralSubOwner, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCollateralSubOwnerDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<NewCollateralSubOwner> getListNewCollateralSubCustomer(NewCollateralSub newCollateralSubDetail) {
        log.info("getListNewCollateralSubCustomer. (newCollateralSubDetail: {})", newCollateralSubDetail);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCollateralSubDetail", newCollateralSubDetail));
        criteria.addOrder(Order.asc("newCollateralSubDetail.id"));
        List<NewCollateralSubOwner> newCollateralSubCustomerList = (List<NewCollateralSubOwner>)criteria.list();
        log.info("getList. (result size: {})", newCollateralSubCustomerList.size());

        return newCollateralSubCustomerList;

    }

}
