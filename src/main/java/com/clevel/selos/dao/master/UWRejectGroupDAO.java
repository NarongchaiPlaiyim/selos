package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UWRejectGroup;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UWRejectGroupDAO extends GenericDAO<UWRejectGroup, Integer> {

    @Inject
    @SELOS
    Logger logger;
    @Inject
    public UWRejectGroupDAO() {
    }

    public UWRejectGroup findByBRMSCode(String brmsCode){
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("brmsCode", brmsCode));
        return (UWRejectGroup)criteria.uniqueResult();

    }
}
