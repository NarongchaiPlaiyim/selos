package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement7;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BankStatement7DAO extends GenericDAO<BankStatement7, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BankStatement7DAO() {
    }

    public BankStatement7 getByAccountNumber(String accountNumber) {
        log.debug("getByAccountNumber. (accountNumber: {})", accountNumber);
        BankStatement7 bankStatement = null;
        List<BankStatement7> bankStatementList = new ArrayList<BankStatement7>();
        if (!Util.isEmpty(accountNumber)) {
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("accountNumber", accountNumber));
            bankStatementList = criteria.list();

            if (bankStatementList != null && bankStatementList.size() > 0) {
                bankStatement = new BankStatement7();
                bankStatement = bankStatementList.get(0);

                log.debug("getByAccountNumber. (result bankStatement: {})", bankStatement);
            }
        }

        return bankStatement;
    }
}
