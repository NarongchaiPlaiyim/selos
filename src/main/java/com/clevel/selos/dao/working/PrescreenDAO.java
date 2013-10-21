package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PrescreenDAO extends GenericDAO<Prescreen, Long> {
    @Inject
    private Logger log;

    @Inject
    public PrescreenDAO(){

    }

    public Prescreen findByWorkCasePrescreen(WorkCasePrescreen workCasePrescreen){
        log.info("findByWorkCasePrescreen : {}", workCasePrescreen);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen", workCasePrescreen));
        Prescreen prescreen = (Prescreen)criteria.uniqueResult();

        return prescreen;
    }

    public Prescreen findByWorkCasePrescreenId(long workCasePrescreenId){
        log.info("findByWorkCasePrescreen ::: workCasePreScreenId : {}", workCasePrescreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        Prescreen prescreen = (Prescreen)criteria.uniqueResult();

        return prescreen;
    }
}
