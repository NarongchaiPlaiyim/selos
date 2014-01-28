package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.AppraisalPurpose;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AppraisalDetailDAO extends GenericDAO<AppraisalPurpose, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AppraisalDetailDAO(){

    }

    public List<AppraisalPurpose> findByAppraisal(Appraisal appraisal ){

        log.info("findByAppraisal begin ");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("appraisal", appraisal));
        criteria.addOrder(Order.asc("no"));
        List<AppraisalPurpose> appraisalDetailList = criteria.list();
        log.info("findByAppraisal. (result size: {})", appraisalDetailList.size());

        return appraisalDetailList;
    }
}