package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingCollateralCredit;
import com.clevel.selos.model.db.working.ExistingCollateralDetail;
import com.clevel.selos.model.db.working.ExistingCreditTypeDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ExistingCollateralCreditDAO extends GenericDAO<ExistingCollateralCredit, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExistingCollateralCreditDAO() {}

    public List<ExistingCollateralCredit> findByExistingCollateralDetail(ExistingCollateralDetail existingCollateralDetail) {
        log.info("existingCollateralDetail : {}", existingCollateralDetail.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("existingCollateralDetail", existingCollateralDetail));
        List<ExistingCollateralCredit> existingCollateralCreditList = criteria.list();
        return existingCollateralCreditList;
    }
}
