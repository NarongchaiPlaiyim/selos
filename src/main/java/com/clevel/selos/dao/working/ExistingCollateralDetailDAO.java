package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingCollateralDetail;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ExistingCollateralDetailDAO extends GenericDAO<ExistingCollateralDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExistingCollateralDetailDAO() {

    }

    public List<ExistingCollateralDetail> findByExistingCreditFacility(ExistingCreditFacility existingCreditFacility,int borrowerType){
        //log.info("findByExistingCreditFacility : {}", existingCreditFacility.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("borrowerType", borrowerType));
        criteria.add(Restrictions.eq("existingCreditFacility", existingCreditFacility));
        criteria.addOrder(Order.asc("no"));
        List<ExistingCollateralDetail> existingCollateralDetailList = criteria.list();
        return existingCollateralDetailList;
    }
}
