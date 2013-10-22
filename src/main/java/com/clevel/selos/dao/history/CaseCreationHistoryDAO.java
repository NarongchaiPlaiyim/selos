package com.clevel.selos.dao.history;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.history.CaseCreationHistory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CaseCreationHistoryDAO extends GenericDAO<CaseCreationHistory,Long> {
    @Inject
    private Logger log;

    @Inject
    public CaseCreationHistoryDAO() {
    }

    public boolean isExist(String caNumber) {
        log.debug("isExist. (caNumber: {})",caNumber);

        boolean exist = isRecordExist(Restrictions.and(
                Restrictions.eq("caNumber", caNumber),
                Restrictions.or(
                        Restrictions.eq("status", ActionResult.SUCCESS),
                        Restrictions.eq("status", ActionResult.SUCCEED),
                        Restrictions.eq("status",ActionResult.WAITING)))
                );

        log.debug("isExist. (result: {})", exist);
        return exist;
    }

}
