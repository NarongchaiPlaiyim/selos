package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.NewCreditTierDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class NewCreditTierDetailDAO extends GenericDAO<NewCreditTierDetail, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NewCreditTierDetailDAO() {}


    public List<NewCreditTierDetail> findByNewCreditDetail(NewCreditDetail newCreditDetail) {
        log.info("findByNewCreditDetail :::{}", newCreditDetail);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditDetail", newCreditDetail));
//      criteria.addOrder(Order.asc("id"));
        List<NewCreditTierDetail> newCreditTierDetails = (List<NewCreditTierDetail>)criteria.list();
        log.info("newCreditTierDetails ::: size : {}", newCreditTierDetails.size());
        return newCreditTierDetails;
    }
}
