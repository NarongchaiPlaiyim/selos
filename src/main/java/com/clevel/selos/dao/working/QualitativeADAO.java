package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.QualitativeA;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class QualitativeADAO extends GenericDAO<QualitativeA, Integer> {
    @Inject
    private Logger log;

    @Inject
    public QualitativeADAO(){

    }

    public QualitativeA findByWorkCase(WorkCase workCase){
        log.info("findByWorkCaseId ::: workCase : {}", workCase);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        QualitativeA qualitativeA = (QualitativeA)criteria.uniqueResult();

        return qualitativeA;
    }
}
