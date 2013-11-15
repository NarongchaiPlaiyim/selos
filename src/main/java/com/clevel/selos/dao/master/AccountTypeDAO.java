package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AccountTypeDAO extends GenericDAO<AccountType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AccountTypeDAO() {
    }

    public AccountType getIndividualByCode(String code) {
        log.debug("getIndividualByCode. (code: {}", code);
        AccountType accountType = new AccountType();
        if (!Util.isEmpty(code)) {
            //set for individual
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("customerEntity.id", 1));
            criteria.add(Restrictions.eq("ncbCode", code.trim()));
            accountType = (AccountType) criteria.uniqueResult();

            log.debug("getIndividualByCode. (accountType: {})", accountType);
        }
        return accountType;
    }

    public AccountType getJuristicByCode(String code) {
        log.debug("getJuristicByCode. (code: {}", code);
        AccountType accountType = new AccountType();
        if (!Util.isEmpty(code)) {
            //set for juristic
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("customerEntity.id", 2));
            criteria.add(Restrictions.eq("ncbCode", code.trim()));
            accountType = (AccountType) criteria.uniqueResult();

            log.debug("getJuristicByCode. (accountType: {})", accountType);
        }
        return accountType;
    }

    public AccountType getJuristicByName(String name) {
        log.debug("getJuristicByName. (name: {}", name);
        AccountType accountType = new AccountType();
        if (!Util.isEmpty(name)) {
            //set for juristic
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("customerEntity.id", 2));
            criteria.add(Restrictions.eq("name", name));
            accountType = (AccountType) criteria.uniqueResult();

            log.debug("getJuristicByCode. (accountType: {})", accountType);
        }
        return accountType;
    }

    public List<AccountType> getListLoanTypeByCusEntity(int customerEntityId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity.id", customerEntityId));
        criteria.add(Restrictions.eq("active", 1));
        List<AccountType> accountTypes = criteria.list();
        log.debug("getListLoanTypeByCusEntity. (AccountType:{} )", accountTypes);
        return accountTypes;
    }
}
