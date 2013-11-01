package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.BankAccountStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BankAccountStatusDAO extends GenericDAO<BankAccountStatus, Integer> {
    @Inject
    private Logger log;

    @Inject
    BankAccountStatusDAO(){
    }

    public BankAccountStatus findByCodeAndType(String code, int type){
        log.info("findByCodeAndType. (code: {}, type: {})", code, type);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("code", code));
        criteria.add(Restrictions.eq("bankAccountType", type));
        BankAccountStatus bankAccountStatus = (BankAccountStatus)criteria.uniqueResult();

        return bankAccountStatus;
    }
}
