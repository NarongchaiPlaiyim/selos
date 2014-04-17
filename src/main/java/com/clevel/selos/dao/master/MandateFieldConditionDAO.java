package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateFieldCondition;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MandateFieldConditionDAO extends GenericDAO<MandateFieldCondition, Long>{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    public MandateFieldConditionDAO() {
    }

    public List<MandateFieldCondition> findByClassName(String className){
        logger.debug("findByClassName className:{}", className);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("className", className));
        List<MandateFieldCondition> mandateFieldConditionList = criteria.list();
        logger.debug("retrun List<MandateFieldCondition> {}", mandateFieldConditionList);
        return mandateFieldConditionList;
    }
}
