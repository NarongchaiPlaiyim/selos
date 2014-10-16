package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AADDecision;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AADDecisionDAO extends GenericDAO<AADDecision, Integer> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public AADDecisionDAO() {
    }

    public AADDecision getByCode(String code) {
        Criteria criteria = createCriteria()
                .add(Restrictions.eq("active", 1))
                .add(Restrictions.eq("code", code));
        return (AADDecision)criteria.uniqueResult();
    }
}
