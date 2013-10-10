package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class WorkCaseDAO extends GenericDAO<WorkCase,Long> {
    @Inject
    private Logger log;

    @Inject
    public WorkCaseDAO() {
    }

    public long findIdByWobNumber(String wobNumber){
        log.info("findIdByWobNum : {}", wobNumber);
        long workCaseId = 0;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));
        WorkCase workCase = (WorkCase)criteria.uniqueResult();
        if(workCase != null){
            workCaseId = workCase.getId();
        }

        return workCaseId;
    }

    public WorkCase findByWobNumber(String wobNumber){
        log.info("findIdByWobNum : {}", wobNumber);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));
        WorkCase workCase = (WorkCase)criteria.uniqueResult();

        return workCase;
    }
}
