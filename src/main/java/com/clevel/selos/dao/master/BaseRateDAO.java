package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BaseRateDAO extends GenericDAO<BaseRate, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BaseRateDAO() {
    }

    public BaseRate findByName(String name){
        log.debug("findByName (name : {})",name);
        BaseRate baseRate = null;
        if (!Util.isEmpty(name)) {
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("name", name));
            baseRate = (BaseRate) criteria.uniqueResult();

            log.debug("findByName. (baseRate: {})", baseRate);
        }
        return baseRate;
    }
}
