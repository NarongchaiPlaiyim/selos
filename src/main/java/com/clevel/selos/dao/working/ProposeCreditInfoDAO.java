package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeCreditInfo;
import com.clevel.selos.model.db.working.ProposeLine;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProposeCreditInfoDAO extends GenericDAO<ProposeCreditInfo, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ProposeCreditInfoDAO() {
    }

    public ProposeCreditInfo findBySeqAndPropose(int seq, ProposeLine proposeLine, ProposeType proposeType) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("seq", seq));
        criteria.add(Restrictions.eq("proposeLine", proposeLine));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        ProposeCreditInfo proposeCreditInfo = (ProposeCreditInfo) criteria.uniqueResult();

        return proposeCreditInfo;
    }

    public List<ProposeCreditInfo> findApprovedNewCreditDetail(long workCaseId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType", ProposeType.A));
        criteria.add(Restrictions.eq("uwDecision", DecisionType.APPROVED));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCreditInfo> newCreditDetailList = (List<ProposeCreditInfo>) criteria.list();
        return newCreditDetailList;
    }

    public List<ProposeCreditInfo> findNewCreditDetail(long workCaseId, ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        List<ProposeCreditInfo> newCreditDetailList = (List<ProposeCreditInfo>) criteria.list();
        log.debug("findNewCreditDetail workCaseId : {}, proposeType : {}", workCaseId, proposeType);
        return newCreditDetailList;
    }

    public List<ProposeCreditInfo> findNewCreditDetailByWorkCaseIdForBA(long workCaseId,boolean isTopUpBA) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType",ProposeType.A));
        criteria.add(Restrictions.eq("uwDecision",DecisionType.APPROVED));
        criteria.createAlias("productProgram", "product_program")
                .add(Restrictions.eq("product_program.ba",isTopUpBA));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCreditInfo> newCreditDetailList = (List<ProposeCreditInfo>) criteria.list();
        return newCreditDetailList;
    }
}
