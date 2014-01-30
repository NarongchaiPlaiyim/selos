package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AccountPurpose;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AccountPurposeDAO extends GenericDAO<AccountPurpose, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AccountPurposeDAO() {
    }

    @Override
    public List<AccountPurpose> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1));
        List<AccountPurpose> list = criteria.list();
        return list;
    }
}
