package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeFeeDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ProposeFeeDetailDAO extends GenericDAO<ProposeFeeDetail, Long>{

    public List<ProposeFeeDetail> findByWorkCaseId(long workCaseId, ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        List<ProposeFeeDetail> proposeFeeDetailList = criteria.list();
        return proposeFeeDetailList;
    }

    public List<ProposeFeeDetail> findAllByWorkCaseId(long workCaseId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("paymentMethod.id"))
                .addOrder(Order.asc("feeType.id"))
                .addOrder(Order.asc("id"));
        return criteria.list();
    }
    public ProposeFeeDetail findByType(long workCaseId,long feeTypeId) {
    	Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("feeType.id", feeTypeId));
        List<ProposeFeeDetail> list = criteria.list();
        if (list == null || list.isEmpty())
        	return null;
        else
        	return list.get(0);
    }
    
}
