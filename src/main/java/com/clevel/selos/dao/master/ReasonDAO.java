package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.ReasonType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ReasonDAO extends GenericDAO<Reason, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ReasonDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<Reason> getList(ReasonType reasonType) {
        log.debug("getList. (reasonType: {})", reasonType);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("reasonType", reasonType));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        log.debug("getList. (result size: {})", list.size());
        return list;
    }

    public List<Reason> getReasonByStepAction(long stepId, long actionId){
        log.debug("getList. (stepId: {}, actionId: {})", stepId, actionId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("action.id", actionId));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        log.debug("getList. (result size: {})", list!=null?null:list.size());

        return list;
    }

    public List<Reason> getCancelList() {
        log.debug("getCancelList");

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("reasonType.id", 3));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        log.debug("getCancelList. (result size: {})", list.size());
        return list;
    }

    public List<Reason> getRejectList() {
        log.debug("getRejectList");

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("reasonType.id", 1));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        log.debug("getRejectList. (result size: {})", list.size());
        return list;
    }

    public List<Reason> getDeviateList() {
        log.debug("getRejectList");

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("reasonType.id", 6));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        log.debug("getRejectList. (result size: {})", list.size());
        return list;
    }

    public List<Reason> getContactReasonList() {
    	Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("reasonType.id", 8));
        criteria.addOrder(Order.asc("id"));
        List<Reason> list = criteria.list();
        return list;
    }

    public List<Reason> getReasonList() {
        log.debug("getReasonList");

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("reasonType.id", 2));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        log.debug("getReasonList. (result size: {})", list.size());
        return list;
    }

    public Reason getByCode(String code) {
        log.debug("getByCode. (code: {})", code);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("code", code));
        criteria.addOrder(Order.asc("id"));

        Reason reason = (Reason) criteria.uniqueResult();
        return reason;
    }

    public List<Reason> getAppealReasonList()
    {
        log.debug("getAppealReasonList");

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("reasonType.id", 5));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        log.debug("getAppealReasonList. (result size: {})", list.size());
        return list;
    }

    public int getBRMSReasonId(){
        int reasonId = 0;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("code", "B001"));
        criteria.addOrder(Order.desc("id"));

        List<Reason> list = criteria.list();

        if(list != null && list.size() > 0) {
            reasonId = list.get(0).getId();
        }

        return reasonId;
    }

    public List<Reason> getResubmitReasonList()
    {
        log.debug("getResubmitReasonList");

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("reasonType.id", 5));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        log.debug("getResubmitReasonList. (result size: {})", list.size());
        return list;
    }

    public List<Reason> getListByCodeAndReasonType(String code, int reasonType) {
        log.debug("getListByCodeAndReasonType. (code: {}, reasonType{})", code, reasonType);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("code", code));
        criteria.add(Restrictions.eq("reasonType.id", reasonType));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        if(list!=null) {
            log.debug("getResubmitReasonList. (result size: {})", list.size());
        } else {
            log.debug("getResubmitReasonList. (result : null)");
        }
        return list;
    }

}
