package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingCreditTierDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ExistingCreditTierDetailDAO extends GenericDAO<ExistingCreditTierDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    public ExistingCreditTierDetailDAO() {

    }

    public List<ExistingCreditTierDetail> findByExistingCreditDetail(ExistingCreditDetail existingCreditDetail) {
        log.info("findByExistingCreditFacility : {}", existingCreditDetail.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("existingCreditDetail", existingCreditDetail));
        criteria.addOrder(Order.asc("no"));
        List<ExistingCreditTierDetail> existingCreditDetailList = criteria.list();
        return existingCreditDetailList;
    }

}
