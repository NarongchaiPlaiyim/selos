package com.clevel.selos.dao.ext.ncb;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.ext.ncb.NCBResult;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class NCBResultDAO extends GenericDAO<NCBResult, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public NCBResultDAO() {
    }

    public boolean isExist(String appRefNumber) {
        log.debug("NCRS Call isExist({})", appRefNumber);
        return isRecordExist(Restrictions.eq("appNumber", appRefNumber));
    }

    public boolean isCheckLlst(String appRefNumber, String customerId) {
        log.info("Call isOldCustomer(appRefNumber : {}, customerId : {}) is {}", appRefNumber, customerId);
        return isRecordExist(Restrictions.and(Restrictions.eq("appNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
    }
}
