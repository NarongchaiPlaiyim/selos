package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.PrescreenCollateral;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class PrescreenCollateralDAO extends GenericDAO<PrescreenCollateral, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public PrescreenCollateralDAO() {

    }

    public List<PrescreenCollateral> findByPreScreen(Prescreen prescreen) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("prescreen", prescreen));
        criteria.addOrder(Order.asc("id"));
        List<PrescreenCollateral> prescreenBusinessList = criteria.list();
        log.info("findByPreScreenId. (result size: {})", prescreenBusinessList.size());

        return prescreenBusinessList;
    }

    public List<PrescreenCollateral> findByPreScreenId(long prescreenId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("prescreen.id", prescreenId));
        criteria.addOrder(Order.asc("id"));
        List<PrescreenCollateral> prescreenBusinessList = criteria.list();
        log.info("findByPreScreenId. (result size: {})", prescreenBusinessList.size());

        return prescreenBusinessList;
    }
}
