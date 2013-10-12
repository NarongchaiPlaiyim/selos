package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Bank;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BankDAO extends GenericDAO<Bank,Integer> {
    @Inject
    private Logger log;

    @Inject
    public BankDAO() {
    }

    public List<Bank> getListExcludeTMB() {
        //todo: change later
        return findAll();
    }

    public List<Bank> getListRefinance() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.eq("refinance",1));
        List<Bank> list = criteria.list();
        return list;
    }
}
