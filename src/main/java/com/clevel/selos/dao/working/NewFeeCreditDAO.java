package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.NewFeeDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewFeeCreditDAO extends GenericDAO<NewFeeDetail, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewFeeCreditDAO() {}


    public List<NewFeeDetail> findByNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("findByNewCreditFacility ::: {}", newCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.addOrder(Order.asc("id"));
        List<NewFeeDetail> newFeeDetailList = (List<NewFeeDetail>) criteria.list();
        log.info("newFeeDetailList ::: size : {}", newFeeDetailList.size());
        return newFeeDetailList;
    }
}
