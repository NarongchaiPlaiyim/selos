package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Disbursement;
import com.clevel.selos.model.db.working.DisbursementCredit;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

public class DisbursementDAO extends GenericDAO<Disbursement, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public DisbursementDAO() {
    	
    }

    
    public Disbursement findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        Disbursement disbursement = (Disbursement) criteria.uniqueResult();

        return disbursement;
    }
    
    public BigDecimal getTotalDisbursementAmount(long workCaseId) {
    	Disbursement model = findByWorkCaseId(workCaseId);
    	if (model == null)
    		return BigDecimal.ZERO;
    	List<DisbursementCredit> summaryList = model.getDisbursementSummaryList();
    	if (summaryList == null || summaryList.isEmpty())
    		return BigDecimal.ZERO;
    	BigDecimal total = BigDecimal.ZERO;
    	for(DisbursementCredit summ : summaryList) {
    		if (summ.getDisburseAmount() == null)
    			continue;
    		total = total.add(summ.getDisburseAmount());
    	}
    	return total;
    }
}
