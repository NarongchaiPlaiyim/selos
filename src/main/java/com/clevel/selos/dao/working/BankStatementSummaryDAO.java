package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.BankStatementSummary;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BankStatementSummaryDAO extends GenericDAO<BankStatementSummary, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public BankStatementSummaryDAO() {
    }

    public BankStatementSummary getByWorkCase(WorkCase workCase) {
        log.info("getByWorkCase : {}", workCase);
        BankStatementSummary bankStatementSummary = new BankStatementSummary();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCase.getId()));
        criteria.addOrder(Order.asc("id"));
//        bankStatementSummary = (BankStatementSummary) criteria.uniqueResult();
        List list = criteria.list();
        if (list != null && list.size() > 0) {
            bankStatementSummary = (BankStatementSummary) list.get(0);
        }
        return bankStatementSummary;
    }

    public BankStatementSummary findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);
        BankStatementSummary bankStatementSummary = new BankStatementSummary();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("id"));
//        bankStatementSummary = (BankStatementSummary) criteria.uniqueResult();
        List list = criteria.list();
        if (list != null && list.size() > 0) {
            bankStatementSummary = (BankStatementSummary) list.get(0);
        }
        return bankStatementSummary;
    }

    public BankStatementSummary findByWorkcasePrescreenId(long workCasePrescreenId){
        log.info("findByWorkCaseId : {}", workCasePrescreenId);
        BankStatementSummary bankStatementSummary = new BankStatementSummary();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.addOrder(Order.asc("id"));
        bankStatementSummary = (BankStatementSummary) criteria.uniqueResult();
        /*List list = criteria.list();
        if (list != null && list.size() > 0) {
            bankStatementSummary = (BankStatementSummary) list.get(0);
        }*/
        return bankStatementSummary;
    }
}
