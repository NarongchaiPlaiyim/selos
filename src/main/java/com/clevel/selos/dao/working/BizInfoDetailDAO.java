package com.clevel.selos.dao.working;

import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BizInfoDetailDAO extends GenericDAO<BizInfoDetail,Long> {
    @Inject
    private Logger log;

    @Inject
    public BizInfoDetailDAO() {
    }

    public List<BizInfoDetail> findByWorkCasePreScreen(WorkCasePrescreen workCasePrescreen){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen", workCasePrescreen));
        criteria.addOrder(Order.asc("id"));
        List<BizInfoDetail> bizInfoDetailList = criteria.list();
        log.info("findByWorkCasePreScreen. (result size: {})", bizInfoDetailList.size());

        return bizInfoDetailList;
    }

    public List<BizInfoDetail> findByWorkCasePreScreenId(long workCasePrescreenId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.addOrder(Order.asc("id"));
        List<BizInfoDetail> bizInfoDetailList = criteria.list();
        log.info("findByWorkCasePreScreen. (result size: {})", bizInfoDetailList.size());

        return bizInfoDetailList;
    }

    public List<BizInfoDetail> findByBizInfoSummaryId(BizInfoSummary bizInfoSummary){
        log.info("findByBizInfoSummaryId begin ");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("bizInfoSummary", bizInfoSummary));
        criteria.addOrder(Order.asc("id"));
        List<BizInfoDetail> bizInfoDetailList = criteria.list();
        log.info("findByBizInfoSummaryId. (result size: {})", bizInfoDetailList.size());

        return bizInfoDetailList;
    }

}
