package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.WarningCode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class WarningCodeDAO extends GenericDAO<WarningCode, Integer> {
    @Inject
    private Logger log;

    @Inject
    public WarningCodeDAO() {

    }

    public WarningCode findByCode(String code) {
        log.info("findByCode : {}", code);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("code", code));
        WarningCode warningCode = (WarningCode) criteria.uniqueResult();

        return warningCode;
    }
}
