package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class WorkCasePrescreenDAO extends GenericDAO<WorkCasePrescreen,Long> {
    @Inject
    private Logger log;

    @Inject
    public WorkCasePrescreenDAO() {
    }

    public long findIdByWobNumber(String wobNumber){
        log.info("findIdByWobNum : {}", wobNumber);
        long workCasePreScreenId = 0;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("wobNumber", wobNumber));
        WorkCasePrescreen workCasePrescreen = (WorkCasePrescreen)criteria.uniqueResult();
        if(workCasePrescreen != null){
            workCasePreScreenId = workCasePrescreen.getId();
        }

        return workCasePreScreenId;
    }
}
