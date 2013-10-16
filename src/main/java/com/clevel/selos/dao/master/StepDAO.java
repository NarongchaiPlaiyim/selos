package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Step;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class StepDAO extends GenericDAO<Step,Long> {
    @Inject
    private Logger log;

    @Inject
    public StepDAO() {
    }

    public long findIdByStepCode(String stepCode){
        log.info("findIdByStepCode : {}", stepCode);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("stepCode", stepCode));
        long stepId = Long.parseLong((String)criteria.uniqueResult());

        return stepId;
    }
}