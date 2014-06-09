package com.clevel.selos.dao.history;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.history.CaseHistory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CaseHistoryDAO extends GenericDAO<CaseHistory, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public CaseHistoryDAO() {
    }

    public boolean checkCaseReason(String appNumber, String reason)
    {

        Criteria criteria = createCriteria();

        criteria.add(Restrictions.eq("appNumber",appNumber));

        criteria.add(Restrictions.eq("reason",reason));

        if(criteria.list().size()>0)
        {
            log.info("Case terminated with Auto expiry reason, disable resubmit button");
            return true;
        }

        else
        {
            log.info("Case in not terminated with Auto expiry reason, enable resubmit button");
            return false;
        }


    }

}
