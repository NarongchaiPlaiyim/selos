package com.clevel.selos.dao.working;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.UWRuleResultDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class UWRuleResultDetailDAO extends GenericDAO<UWRuleResultDetail, Long>{
    @Inject
    @SELOS
    Logger log;

    @Inject
    public UWRuleResultDetailDAO() {
    }

    public List<UWRuleResultDetail> findByUWRuleSummaryId(long uwRuleSummaryId) {
        log.info("findByUWRuleSummaryId ::: uwRuleSummaryId : {}", uwRuleSummaryId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("uwRuleResultSummary.id", uwRuleSummaryId));
        List<UWRuleResultDetail> uwRuleResultDetailList = (List<UWRuleResultDetail>) criteria.list();
        log.info("findByUWRuleSummaryId ::: size : {}", uwRuleResultDetailList.size());
        return uwRuleResultDetailList;
    }
}
