package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewFeeDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewFeeCreditDAO extends GenericDAO<NewFeeDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewFeeCreditDAO() {}

    public List<NewFeeDetail> findNewFeeDetailById(long newFeeDetailId) {
        log.info("findNewFeeDetailById ::: {}", newFeeDetailId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newFeeDetail.id", newFeeDetailId));
        criteria.addOrder(Order.asc("id"));
        List<NewFeeDetail> newFeeDetailList = (List<NewFeeDetail>) criteria.list();
        log.info("newFeeDetailList ::: size : {}", newFeeDetailList.size());
        return newFeeDetailList;
    }
}