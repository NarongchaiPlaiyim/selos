package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.QualitativeB;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class QualitativeBDAO extends GenericDAO<QualitativeB, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public QualitativeBDAO() {

    }

    public QualitativeB findByWorkCase(WorkCase workCase) {
        log.info("findByWorkCaseId ::: workCase : {}", workCase);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        QualitativeB qualitativeB = (QualitativeB) criteria.uniqueResult();

        return qualitativeB;
    }

    public QualitativeB findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId ::: workCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        QualitativeB qualitativeB = (QualitativeB) criteria.uniqueResult();

        return qualitativeB;
    }
}
