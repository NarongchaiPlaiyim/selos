package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.MandateDoc;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MandateDocDAO extends GenericDAO<MandateDoc, Long>{
    @Inject
    @SELOS
    Logger log;
    @Inject
    public MandateDocDAO() {

    }

    public MandateDoc findByWorkCaseId(final long workCaseId) {
    log.info("--findByWorkCaseId : {}", workCaseId);
    Criteria criteria = createCriteria();
    criteria.add(Restrictions.eq("workCase.id", workCaseId));
    MandateDoc mandateDoc = (MandateDoc) criteria.uniqueResult();
    return mandateDoc;

    }
}
