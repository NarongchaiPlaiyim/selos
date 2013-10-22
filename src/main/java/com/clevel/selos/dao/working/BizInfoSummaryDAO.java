package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BizInfoSummaryDAO extends GenericDAO<BizInfoSummary,Long> {
    @Inject
    private Logger log;

    @Inject
    public BizInfoSummaryDAO() {
    }

    public BizInfoSummary onSearchByWorkCase( WorkCase workCase){
        BizInfoSummary bizInfoSummary;
        log.info("getListByBusinessGroup. (district: {})",workCase);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));

        bizInfoSummary = (BizInfoSummary)criteria.uniqueResult();

        log.info("getListByDistrict. (result size: {})",bizInfoSummary);
        return bizInfoSummary;
    }

}
