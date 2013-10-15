package com.clevel.selos.dao.ext.ncb;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.NCBResult;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class NCBResultDAO extends GenericDAO<NCBResult,Long> {
    @Inject
    private Logger log;

    @Inject
    public NCBResultDAO() {
    }

    public boolean isExist(String appRefNumber) {
        log.debug("NCRS Call isExist({})", appRefNumber);
        return isRecordExist(Restrictions.eq("appRefNumber", appRefNumber));
    }
}
