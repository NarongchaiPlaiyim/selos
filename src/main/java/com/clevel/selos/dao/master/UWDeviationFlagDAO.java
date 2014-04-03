package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UWDeviationFlag;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UWDeviationFlagDAO extends GenericDAO<UWDeviationFlag, Integer>{
    @Inject
    @SELOS
    Logger logger;
    @Inject
    public UWDeviationFlagDAO() {
    }

    public UWDeviationFlag findByBRMSCode(String brmsCode){
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("brmsCode", brmsCode));
        return (UWDeviationFlag)criteria.uniqueResult();

    }
}
