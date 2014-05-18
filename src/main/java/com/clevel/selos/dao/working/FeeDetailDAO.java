package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.FeeDetail;
import com.clevel.selos.model.db.working.NewCreditDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class FeeDetailDAO extends GenericDAO<FeeDetail, Long>{
	private static final long serialVersionUID = -8634969158200352474L;
	@Inject @SELOS
    private Logger log;
	
	@SuppressWarnings("unchecked")
    public List<FeeDetail> findAllByWorkCaseId(long workCaseId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.addOrder(Order.asc("paymentMethod.id"))
                .addOrder(Order.asc("feeType.id"))
                .addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<FeeDetail> findByCreditDetail(NewCreditDetail newCreditDetail){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("newCreditDetail", newCreditDetail));
        List<FeeDetail> feeDetailList = criteria.list();
        return feeDetailList;
    }
}
