package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ServiceSegment;
import com.clevel.selos.model.db.master.UWRuleName;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UWRuleNameDAO extends GenericDAO<UWRuleName, Integer> {

    @Inject
    @SELOS
    Logger logger;
    @Inject
    public UWRuleNameDAO() {
    }

    public UWRuleName findByBRMSCode(String brmsCode){
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("brmsCode", brmsCode));
        return (UWRuleName)criteria.uniqueResult();

    }
}
