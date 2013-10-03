package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BizInfo;
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
public class BizInfoDAO extends GenericDAO<BizInfo,Long> {
    @Inject
    private Logger log;

    @Inject
    public BizInfoDAO() {
    }

    public List<BizInfo> findByWorkCasePreScreen(WorkCasePrescreen workCasePrescreen){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen", workCasePrescreen));
        criteria.addOrder(Order.asc("id"));
        List<BizInfo> bizInfoList = criteria.list();
        log.info("findByWorkCasePreScreen. (result size: {})",bizInfoList.size());

        return bizInfoList;
    }

    public List<BizInfo> findByWorkCasePreScreenId(long workCasePrescreenId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.addOrder(Order.asc("id"));
        List<BizInfo> bizInfoList = criteria.list();
        log.info("findByWorkCasePreScreen. (result size: {})",bizInfoList.size());

        return bizInfoList;
    }
}
