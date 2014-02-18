package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.CreditCategory;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ExistingCreditDetailDAO extends GenericDAO<ExistingCreditDetail, Long> {

    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExistingCreditDetailDAO() {
    }
    public List<ExistingCreditDetail> findByExistingCreditFacility(ExistingCreditFacility existingCreditFacility,int borrowerType,int existingCreditFrom) {
        log.info("findByExistingCreditFacility : {}", existingCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("borrowerType", borrowerType));
        criteria.add(Restrictions.eq("existingCreditFrom", existingCreditFrom));
        criteria.add(Restrictions.eq("existingCreditFacility", existingCreditFacility));
        criteria.addOrder(Order.asc("no"));
        List<ExistingCreditDetail> existingCreditDetailList = criteria.list();
        return existingCreditDetailList;
    }

    public List<ExistingCreditDetail> findByExistingCreditFacilityByTypeAndCategory(ExistingCreditFacility existingCreditFacility,int borrowerType,CreditCategory category) {
        log.info("findByExistingCreditFacility : {}", existingCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("borrowerType", borrowerType));
        criteria.add(Restrictions.eq("existingCreditFacility", existingCreditFacility));
        criteria.add(Restrictions.eq("creditCategory", category.value()));
        criteria.addOrder(Order.asc("no"));
        List<ExistingCreditDetail> existingCreditDetailList = criteria.list();
        return existingCreditDetailList;
    }
}
