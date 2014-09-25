package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ReturnInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ReturnInfoDAO extends GenericDAO<ReturnInfo, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ReturnInfoDAO() {
    }

    public List<ReturnInfo> findReturnList(long workCaseId) {
        log.info("findReturnList : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findReturnList result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findReturnListPrescreen(long workCasePrescreenId) {
        log.info("findReturnListPrescreen : {}", workCasePrescreenId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findReturnListPrescreen result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findByNotAcceptList(long workCaseId) {
        log.info("findByNotAcceptList : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("challenge", 2));
        criteria.add(Restrictions.eq("acceptChallenge", 1));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findByNotAcceptList result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findByNotAcceptListPreScreen(long workCasePrescreenId) {
        log.info("findByNotAcceptListPreScreen : {}", workCasePrescreenId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.add(Restrictions.eq("challenge", 2));
        criteria.add(Restrictions.eq("acceptChallenge", 1));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findByNotAcceptListPreScreen result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findByNotReplyList(long workCaseId) {
        log.info("findByNotAcceptList : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("challenge", 0));
        criteria.add(Restrictions.eq("replyDetail", ""));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findByNotAcceptList result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findByNotReplyDetailList(long workCaseId) {
        log.info("findByNotAcceptList : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("replyDetail", ""));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findByNotAcceptList result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findByNotReplyListPrescreen(long workCasePrescreenId) {
        log.info("findByNotReplyListPrescreen : {}", workCasePrescreenId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.add(Restrictions.eq("challenge", 0));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findByNotReplyListPrescreen result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findByNotReplyDetailListPrescreen(long workCasePrescreenId) {
        log.info("findByNotReplyListPrescreen : {}", workCasePrescreenId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.add(Restrictions.eq("challenge", 0));
        criteria.add(Restrictions.eq("replyDetail", ""));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findByNotReplyListPrescreen result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findByNotReviewList(long workCaseId) {
        log.info("findByNotAcceptList : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.ne("challenge", 2));
        criteria.add(Restrictions.ne("acceptChallenge", 0));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findByNotAcceptList result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findByNotReviewListPrescreen(long workCasePrescreenId) {
        log.info("findByNotReviewListPrescreen : {}", workCasePrescreenId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.add(Restrictions.ne("challenge", 2));
        criteria.add(Restrictions.ne("acceptChallenge", 0));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findByNotAcceptList result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findReturnReplyList(long workCaseId) {
        log.info("findByNotAcceptList : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.ne("challenge", 0));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findByNotAcceptList result size : {}", returnInfoList.size());

        return returnInfoList;
    }

    public List<ReturnInfo> findReturnReplyListPrescreen(long workCasePrescreenId) {
        log.info("findReturnReplyListPrescreen : {}", workCasePrescreenId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));
        criteria.add(Restrictions.ne("challenge", 0));
        criteria.addOrder(Order.asc("id"));
        List<ReturnInfo> returnInfoList = criteria.list();

        log.info("findReturnReplyListPrescreen result size : {}", returnInfoList.size());

        return returnInfoList;
    }
}
