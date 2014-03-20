package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.db.working.ExistingGuarantorDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ExistingGuarantorDetailDAO extends GenericDAO<ExistingGuarantorDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExistingGuarantorDetailDAO() {

    }

    public List<ExistingGuarantorDetail> findByExistingCreditFacility(ExistingCreditFacility existingCreditFacility) {
        log.info("findByExistingCreditFacility : {}", existingCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("existingCreditFacility", existingCreditFacility));
        criteria.addOrder(Order.asc("no"));
        List<ExistingGuarantorDetail> existingGuarantorDetailList = criteria.list();
        return existingGuarantorDetailList;
    }

    public List<ExistingGuarantorDetail> findByCustomerId(long customerId) {
        log.info("findByCustomerId : {}", customerId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("guarantorName.id", customerId));
        criteria.addOrder(Order.asc("id"));
        List<ExistingGuarantorDetail> existingGuarantorDetails = criteria.list();

        return existingGuarantorDetails;
    }
}
