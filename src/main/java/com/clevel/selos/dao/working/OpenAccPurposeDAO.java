package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.OpenAccPurpose;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class OpenAccPurposeDAO extends GenericDAO<OpenAccPurpose, Long> {
    @Inject
    private Logger log;

    @Inject
    public OpenAccPurposeDAO() {
    }

    public List<OpenAccPurpose> findByOpenAccountId(long openAccountId) {
        log.info("findByOpenAccountId : {}", openAccountId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("openAccount.id", openAccountId));
        List<OpenAccPurpose> openAccPurposeList = criteria.list();

        return openAccPurposeList;
    }
}
