package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankStatementSummaryDAO extends GenericDAO<BankStatementSummary, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BankStatementSummaryDAO() {

    }

    public BankStatementSummary getByWorkcase(WorkCase workCase){
        BankStatementSummary bankStatementSummary = new BankStatementSummary();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase", workCase));
        bankStatementSummary = (BankStatementSummary) criteria.uniqueResult();
        return bankStatementSummary;
    }
}
