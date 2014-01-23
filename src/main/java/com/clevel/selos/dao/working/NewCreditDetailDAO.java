package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditFacility;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCreditDetailDAO extends GenericDAO<NewCreditDetail, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCreditDetailDAO() {}

    public List<NewCreditDetail> findNewCreditDetailByNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("findNewCreditDetailByNewCreditFacility ::: {}", newCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.addOrder(Order.asc("id"));
        List<NewCreditDetail> newCreditDetailList = (List<NewCreditDetail>) criteria.list();
        log.info("newCreditDetailList ::: size : {}", newCreditDetailList.size());
        return newCreditDetailList;
    }
}
