package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.PrescreenBusiness;
import com.clevel.selos.model.db.working.PrescreenFacility;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class PrescreenBusinessDAO extends GenericDAO<PrescreenBusiness, Long> {
    @Inject
    private Logger log;

    @Inject
    public PrescreenBusinessDAO(){

    }

    public List<PrescreenBusiness> findByPreScreen(Prescreen prescreen){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("prescreen", prescreen));
        criteria.addOrder(Order.asc("id"));
        List<PrescreenBusiness> prescreenBusinessList = criteria.list();
        log.info("findByPreScreenId. (result size: {})",prescreenBusinessList.size());

        return prescreenBusinessList;
    }
}