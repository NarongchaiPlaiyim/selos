package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExistingGuarantorCredit;
import com.clevel.selos.model.db.working.ExistingGuarantorDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ExistingGuarantorCreditDAO extends GenericDAO<ExistingGuarantorCredit, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExistingGuarantorCreditDAO() {}

    public List<ExistingGuarantorCredit> findByExistingGuarantorDetail(ExistingGuarantorDetail existingGuarantorDetail) {
        log.info("existingGuarantorDetail : {}", existingGuarantorDetail.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("existingGuarantorDetail", existingGuarantorDetail));
        List<ExistingGuarantorCredit> existingGuarantorCreditList = criteria.list();
        return existingGuarantorCreditList;
    }

}
