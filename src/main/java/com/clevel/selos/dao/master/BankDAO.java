package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Bank;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BankDAO extends GenericDAO<Bank, Integer> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public BankDAO() {
    }

    public Bank getTMBBank() {
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.eq("shortName", "TMB"));
        List list = criteria.list();
        return (list != null && list.size() > 0) ?
                (Bank) list.get(0) : null;
    }

    public List<Bank> getListExcludeTMB() {
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.ne("shortName", "TMB"));
        return criteria.list();
    }

    public List<Bank> getListRefinance() {
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.eq("refinance", 1));
        List<Bank> list = criteria.list();
        return list;
    }
}
