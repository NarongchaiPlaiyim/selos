package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.StepLandingPage;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class StepLandingPageDAO extends GenericDAO<StepLandingPage, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public StepLandingPageDAO() {
    }

    public StepLandingPage findByStepId(long stepId) {
        StepLandingPage stepLandingPage;
        log.info("findByStepId : {}", stepId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        stepLandingPage = (StepLandingPage)criteria.uniqueResult();

        return stepLandingPage;
    }
}
