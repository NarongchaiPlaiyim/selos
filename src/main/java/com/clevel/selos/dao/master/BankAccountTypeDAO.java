package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BankAccountTypeDAO extends GenericDAO<BankAccountType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BankAccountTypeDAO() {
    }

    public List<BankAccountType> findOpenAccountType() {
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.eq("openAccountFlag", 1));

        List<BankAccountType> list = criteria.list();
        return list;
    }

    public BankAccountType getAccountTypeRLOS(){
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("bankStatementFlag", 0))
                .add(Restrictions.eq("active", 1));

        BankAccountType bankAccountType = (BankAccountType)criteria.uniqueResult();

        return bankAccountType;
    }
}
