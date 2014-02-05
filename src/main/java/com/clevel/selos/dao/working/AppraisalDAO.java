package com.clevel.selos.dao.working;

import com.clevel.selos.controller.BizInfoSummary;
import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AppraisalDAO extends GenericDAO<Appraisal, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AppraisalDAO(){

    }

    public Appraisal onSearchByWorkCase( WorkCase workCase){
        Appraisal appraisal;
        log.info("onSearchByWorkCase "+workCase);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));

        appraisal = (Appraisal)criteria.uniqueResult();

        log.info("onSearchByWorkCase result null is " + (appraisal==null));
        return appraisal;
    }

    public Appraisal findByWorkCaseId(long workCaseId) {
        log.info("-- findAppraisalByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("id"));
        Appraisal appraisal = (Appraisal) criteria.uniqueResult();
        return appraisal;
    }

    public Appraisal findByWorkCase(WorkCase workCase){
        Appraisal appraisal;
        log.info("-- findByWorkCase {}", workCase);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));

        appraisal = (Appraisal)criteria.uniqueResult();

        return appraisal;
    }

}
