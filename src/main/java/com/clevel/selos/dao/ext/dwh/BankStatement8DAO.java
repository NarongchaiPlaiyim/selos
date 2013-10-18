package com.clevel.selos.dao.ext.dwh;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.bankstatement.BankStatement8;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BankStatement8DAO extends GenericDAO<BankStatement8,Long> {
    @Inject
    private Logger log;

    @Inject
    public BankStatement8DAO() {
    }

    public BankStatement8 getByAccountNumber(String accountNumber){
        log.debug("getByAccountNumber. (accountNumber: {})",accountNumber);
        BankStatement8 bankStatement = null;
        List<BankStatement8> bankStatementList = new ArrayList<BankStatement8>();
        if(!Util.isEmpty(accountNumber)){
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("accountNumber", accountNumber));
            bankStatementList = criteria.list();

            if(bankStatementList!=null && bankStatementList.size()>0){
                bankStatement = new BankStatement8();
                bankStatement = bankStatementList.get(0);

                log.debug("getByAccountNumber. (result bankStatement: {})", bankStatement);
            }
        }

        return bankStatement;
    }
}
