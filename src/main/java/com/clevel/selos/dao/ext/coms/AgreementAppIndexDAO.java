package com.clevel.selos.dao.ext.coms;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.ext.coms.AgreementAppIndex;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AgreementAppIndexDAO extends GenericDAO<AgreementAppIndex, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AgreementAppIndexDAO() {
    }

    public AgreementAppIndex findByWorkCaseId(long workCaseId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        AgreementAppIndex agreementAppIndex = (AgreementAppIndex) criteria.uniqueResult();
        return agreementAppIndex;
    }
}
