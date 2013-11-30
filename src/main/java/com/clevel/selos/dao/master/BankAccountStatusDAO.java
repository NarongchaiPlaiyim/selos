package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BankAccountStatusDAO extends GenericDAO<BankAccountStatus, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    BankAccountStatusDAO(){
    }

    public BankAccountStatus findByCodeAndType(String code, int type){
        log.info("findByCodeAndType. (code: {}, type: {})", code.trim(), type);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("code", code.trim()));
        criteria.add(Restrictions.eq("bankAccountType.id", type));
        BankAccountStatus bankAccountStatus = (BankAccountStatus)criteria.uniqueResult();

        log.info("findByCodeAndType. bankAccountStatus : {}", bankAccountStatus);

        return bankAccountStatus;
    }
}
