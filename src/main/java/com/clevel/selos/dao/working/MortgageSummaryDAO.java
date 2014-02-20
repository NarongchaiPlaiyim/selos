package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.MortgageSummary;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MortgageSummaryDAO extends GenericDAO<MortgageSummary, Long> {
    private static final long serialVersionUID = -9047195995937366233L;
	
    @Inject
    @SELOS
    Logger log;
    
    public MortgageSummaryDAO() {
    }

    public MortgageSummary findByWorkCaseId(long workCaseId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        MortgageSummary mortgageSummary = (MortgageSummary) criteria.uniqueResult();
        return mortgageSummary;
    }
}
