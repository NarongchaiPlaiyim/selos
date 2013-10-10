package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 20/9/2556
 * Time: 16:02 น.
 * To change this template use File | Settings | File Templates.
 */
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
}
