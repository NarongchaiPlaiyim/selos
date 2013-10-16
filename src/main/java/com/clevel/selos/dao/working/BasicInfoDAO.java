package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BasicInfoDAO extends GenericDAO<BasicInfo, Long> {
    @Inject
    private Logger log;

    @Inject
    public BasicInfoDAO(){
    }

    public BasicInfo findByWorkCaseId(long workCaseId){
        log.info("findByWorkCaseId : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        BasicInfo basicInfo = (BasicInfo)criteria.uniqueResult();

        return basicInfo;
    }
}