package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateFieldClass;
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

    public List<MandateFieldCondition> findByClass(MandateFieldClass mandateFieldClass){
        logger.debug("-- begin findByClass mandateFieldClass: {}", mandateFieldClass);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("mandateFieldClass", mandateFieldClass));
        List<MandateFieldCondition> mandateFieldConditionList = criteria.list();
        logger.debug("return List<MandateFieldCondition> {}", mandateFieldConditionList);
        return mandateFieldConditionList;
    }

    public List<MandateFieldCondition> findByClass(long mandateFieldClassId){
        logger.debug("-- begin findByClass mandateFieldClassId: {}", mandateFieldClassId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("mandateFieldClass.id", mandateFieldClassId));
        List<MandateFieldCondition> mandateFieldConditionList = criteria.list();
        logger.debug("return List<MandateFieldCondition> {}", mandateFieldConditionList);
        return mandateFieldConditionList;
    }

    public MandateFieldCondition findByConditionName(String conditionName){
        logger.debug("--begin findByConditionName mandateFieldClassId: {}", conditionName);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("name", conditionName));

        MandateFieldCondition mandateFieldCondition = (MandateFieldCondition)criteria.uniqueResult();
        logger.debug("return MandateFieldCondition {}", mandateFieldCondition);
        return mandateFieldCondition;

    }
}
