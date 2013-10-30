package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class TCGDAO extends GenericDAO<TCG, Integer> {
    @Inject
    private Logger log;

    @Inject
    public TCGDAO() {
    }

    public TCG findTcgById(long id) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("id", id));
        TCG tcg = (TCG) criteria.uniqueResult();
        return tcg;
    }


    public TCG findByWorkCase(WorkCase workCase) {
        log.info("findByWorkCaseId ::: workCase : {}", workCase);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        TCG tcg = (TCG) criteria.uniqueResult();

        return tcg;
    }

    public TCG findByWorkCaseId(long workCase) {
        log.info("findByWorkCaseId ::: workCase : {}", workCase);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCase));
        TCG tcg = (TCG) criteria.uniqueResult();

        return tcg;
    }
}
