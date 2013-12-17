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
                .add(Restrictions.eq("openAccountFlag", 1))
                .add(Restrictions.eq("othBankStatementFlag", 0));

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

    public List<BankAccountType> getBankAccountTypeList() {
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.eq("bankStatementFlag", 1))
                .add(Restrictions.eq("othBankStatementFlag", 0));

        List<BankAccountType> list = criteria.list();
        return list;
    }

    public List<BankAccountType> getOtherAccountTypeList() {
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.eq("bankStatementFlag", 0))
                .add(Restrictions.eq("othBankStatementFlag", 1));

        List<BankAccountType> list = criteria.list();
        return list;
    }

    public BankAccountType getByShortName(String shortName) {
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.eq("shortName", shortName));
        List list = criteria.list();
        if (list != null && list.size() > 1)
            return (BankAccountType) list.get(0);
        else
            return (BankAccountType) criteria.uniqueResult();
    }

    public BankAccountType getByName(String name) {
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.eq("name", name));
        List list = criteria.list();
        if (list != null && list.size() > 1)
            return (BankAccountType) list.get(0);
        else
            return (BankAccountType) criteria.uniqueResult();
    }
}
