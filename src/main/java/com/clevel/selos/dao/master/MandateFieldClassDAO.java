package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateFieldClass;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MandateFieldClassDAO extends GenericDAO<MandateFieldClass, Long>{

    @Inject
    @SELOS
    Logger log;
    @Inject
    public MandateFieldClassDAO() {
    }

    public MandateFieldClass findByClassName(String className){
        log.debug("-- begin findByClassName className: {}", className);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("className", className));
        MandateFieldClass mandateFieldClass = (MandateFieldClass) criteria.uniqueResult();
        log.debug("-- return findByClassName {}", mandateFieldClass);
        return mandateFieldClass;
    }
}
