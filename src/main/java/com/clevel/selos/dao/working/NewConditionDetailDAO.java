package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewConditionDetail;
import com.clevel.selos.model.db.working.NewCreditFacility;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewConditionDetailDAO extends GenericDAO<NewConditionDetail, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewConditionDetailDAO() {}

    public List<NewConditionDetail> findByNewCreditFacility(NewCreditFacility newCreditFacility) {
        log.info("findByNewCreditFacility ::: {}", newCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditFacility", newCreditFacility));
        criteria.addOrder(Order.asc("id"));
        List<NewConditionDetail> newConditionDetailList = (List<NewConditionDetail>) criteria.list();
        log.info("newConditionDetailList ::: size : {}", newConditionDetailList.size());
        return newConditionDetailList;
    }
}
