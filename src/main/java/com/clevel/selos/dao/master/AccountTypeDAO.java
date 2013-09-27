package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.db.master.CustomerEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AccountTypeDAO extends GenericDAO<AccountType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public AccountTypeDAO() {
    }

    public AccountType getIndividualByCode(String code){
        log.debug("getIndividualByCode. (code: {}",code);

        //set for individual
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity", customerEntity));
        criteria.add(Restrictions.eq("code", code));
        AccountType accountType = (AccountType) criteria.uniqueResult();

        log.debug("getIndividualByCode. (code: {})",code);
        return accountType;
    }

    public AccountType getJuristicByCode(String code){
        log.debug("getJuristicByCode. (code: {}",code);

        //set for juristic
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity", customerEntity));
        criteria.add(Restrictions.eq("code", code));
        AccountType accountType = (AccountType) criteria.uniqueResult();

        log.debug("getJuristicByCode. (code: {})",code);
        return accountType;
    }
}
