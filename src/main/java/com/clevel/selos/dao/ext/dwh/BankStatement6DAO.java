package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement6;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BankStatement6DAO extends GenericDAO<BankStatement6, Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement6DAO() {
    }

    public BankStatement6 getByAccountNumber(String accountNumber) {
        log.debug("getByAccountNumber. (accountNumber: {})", accountNumber);
        BankStatement6 bankStatement = null;
        List<BankStatement6> bankStatementList = new ArrayList<BankStatement6>();
        if (!Util.isEmpty(accountNumber)) {
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("accountNumber", accountNumber));
            bankStatementList = criteria.list();

            if (bankStatementList != null && bankStatementList.size() > 0) {
                bankStatement = new BankStatement6();
                bankStatement = bankStatementList.get(0);

                log.debug("getByAccountNumber. (result bankStatement: {})", bankStatement);
            }
        }

        return bankStatement;
    }
}
