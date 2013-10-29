package com.clevel.selos.dao.system;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.system.SystemParameter;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SystemParameterDAO extends GenericDAO<SystemParameter, String> {
    @Inject
    private Logger log;

    @Inject
    public SystemParameterDAO() {
    }

    public SystemParameter findByParameterName(String parameterName) {
        log.debug("findByParameterName. (parameterName: {}", parameterName);
        if (!Util.isEmpty(parameterName)) {
            //set for individual
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("name", parameterName));
            SystemParameter systemParameter = (SystemParameter) criteria.uniqueResult();

            log.debug("findByParameterName. (systemParameter: {})", systemParameter);
            return systemParameter;
        }
        return null;
    }
}
