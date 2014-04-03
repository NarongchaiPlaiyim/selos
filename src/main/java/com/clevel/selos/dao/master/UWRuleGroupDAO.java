package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.UWRuleGroup;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UWRuleGroupDAO extends GenericDAO<UWRuleGroup, Integer> {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public UWRuleGroupDAO(){}

    public UWRuleGroup findByBRMSCode(String brmsCode){
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("brmsCode", brmsCode));
        return (UWRuleGroup)criteria.uniqueResult();

    }
}
