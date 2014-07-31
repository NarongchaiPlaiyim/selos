package com.clevel.selos.dao.history;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.history.ReturnInfoHistory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ReturnInfoHistoryDAO extends GenericDAO<ReturnInfoHistory, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ReturnInfoHistoryDAO() {
    }

    public List<ReturnInfoHistory> findReturnHistoryList(long workCaseId) {
        log.info("findReturnHistoryList (workCaseId:{})", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.desc("dateOfReturn")).addOrder(Order.asc("id"));
        List<ReturnInfoHistory> returnInfoList = criteria.list();

        log.info("findReturnHistoryList result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfoHistory> findReturnHistoryListPrescreen(long workCasePrescreenId) {
        log.info("findReturnHistoryListPrescreen (workCasePrescreenId:{})", workCasePrescreenId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.addOrder(Order.desc("dateOfReturn")).addOrder(Order.asc("id"));
        List<ReturnInfoHistory> returnInfoList = criteria.list();

        log.info("findReturnHistoryListPrescreen result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfoHistory> findReturnHistoryLimitList(long workCaseId, int maxResult) {
        log.info("findReturnHistoryLimitList  (workCaseId:{}, maxResult:{})", workCaseId,maxResult);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.desc("dateOfReturn")).addOrder(Order.asc("id"));
        criteria.setMaxResults(maxResult);
        List<ReturnInfoHistory> returnInfoList = criteria.list();

        log.info("findReturnHistoryLimitList result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfoHistory> findReturnHistoryLimitListPrescreen(long workCasePrescreenId, int maxResult) {
        log.info("findReturnHistoryLimitListPrescreen  (workCasePrescreenId:{}, maxResult:{})", workCasePrescreenId,maxResult);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.addOrder(Order.desc("dateOfReturn")).addOrder(Order.asc("id"));
        criteria.setMaxResults(maxResult);
        List<ReturnInfoHistory> returnInfoList = criteria.list();

        log.info("findReturnHistoryLimitListPrescreen result size : {}", returnInfoList.size());

        return returnInfoList;
    }

}
