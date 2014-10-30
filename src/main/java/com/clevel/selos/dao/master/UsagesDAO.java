package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Usages;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UsagesDAO extends GenericDAO<Usages, Integer> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public UsagesDAO() {
    }

    public Usages getByCode(String code) {
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.eq("code", code));
        return (Usages)criteria.uniqueResult();
    }
}
