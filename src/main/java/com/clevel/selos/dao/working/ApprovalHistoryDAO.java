package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ApprovalHistory;
import com.clevel.selos.model.db.working.WorkCase;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ApprovalHistoryDAO extends GenericDAO<ApprovalHistory, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ApprovalHistoryDAO(){
    }

    public List<ApprovalHistory> findByWorkCase(long workCaseId, boolean isSubmit) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("isSubmit", isSubmit ? 1 : 0));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public ApprovalHistory findByWorkCaseAndUserForSubmit(long workCaseId, String userId, int approveType) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("isSubmit", 0));
        criteria.add(Restrictions.eq("user.id", userId));
        criteria.add(Restrictions.eq("approveType", approveType));
        ApprovalHistory approvalHistory = (ApprovalHistory)criteria.uniqueResult();
        return approvalHistory;
    }

    public ApprovalHistory findByWorkCaseAndUserAndApproveTypeForZMSubmit(long workCaseId, User user, int approveType) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("isSubmit", 0));
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("approveType", approveType));
        ApprovalHistory approvalHistory = (ApprovalHistory)criteria.uniqueResult();
        return approvalHistory;
    }

}
