package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.AppraisalContactDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AppraisalContactDetailDAO extends GenericDAO<AppraisalContactDetail, Long> {
    @Inject
    private Logger log;

    @Inject
    public AppraisalContactDetailDAO(){

    }

    public List<AppraisalContactDetail> findByAppraisal(Appraisal appraisal ){

        log.info("findByAppraisal begin ");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("appraisal", appraisal));
        criteria.addOrder(Order.asc("no"));
        List<AppraisalContactDetail> appraisalContactDetailList = criteria.list();
        log.info("findByAppraisal. (result size: {})", appraisalContactDetailList.size());

        return appraisalContactDetailList;
    }
}
