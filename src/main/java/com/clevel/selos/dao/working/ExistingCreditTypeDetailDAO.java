package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingCollateralDetail;
import com.clevel.selos.model.db.working.ExistingCreditTypeDetail;
import com.clevel.selos.model.db.working.ExistingGuarantorDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ExistingCreditTypeDetailDAO extends GenericDAO<ExistingCreditTypeDetail, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExistingCreditTypeDetailDAO() {

    }

    public List<ExistingCreditTypeDetail> findByExistingCollateralDetail(ExistingCollateralDetail existingCollateralDetail) {
        log.info("existingCollateralDetail : {}", existingCollateralDetail.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("existingCollateralDetail", existingCollateralDetail));
        criteria.addOrder(Order.asc("no"));
        List<ExistingCreditTypeDetail> existingCreditTypeDetailList = criteria.list();
        return existingCreditTypeDetailList;
    }

    public List<ExistingCreditTypeDetail> findByExistingGuarantorDetail(ExistingGuarantorDetail existingGuarantorDetail) {
        log.info("existingGuarantorDetail : {}", existingGuarantorDetail.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("existingGuarantorDetail", existingGuarantorDetail));
        criteria.addOrder(Order.asc("no"));
        List<ExistingCreditTypeDetail> existingCreditTypeDetailList = criteria.list();
        return existingCreditTypeDetailList;
    }
    
}
