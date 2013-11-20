package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExSumDeviate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ExSumDeviateDAO extends GenericDAO<ExSumDeviate, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ExSumDeviateDAO() {
    }

    public List<ExSumDeviate> findByExSumId(long exSumId) {
        log.info("findByExSumId : {}", exSumId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("exSummary.id", exSumId));
        List<ExSumDeviate> exSumDeviateList = criteria.list();

        return exSumDeviateList;
    }
}
