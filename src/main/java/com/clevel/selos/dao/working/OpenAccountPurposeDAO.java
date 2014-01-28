package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.OpenAccountPurpose;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class OpenAccountPurposeDAO extends GenericDAO<OpenAccountPurpose, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public OpenAccountPurposeDAO() {
    }

    public List<OpenAccountPurpose> findByOpenAccountId(long openAccountId) {
        log.info("findByOpenAccountId : {}", openAccountId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("openAccount.id", openAccountId));
        List<OpenAccountPurpose> openAccPurposeList = criteria.list();

        return openAccPurposeList;
    }
}
