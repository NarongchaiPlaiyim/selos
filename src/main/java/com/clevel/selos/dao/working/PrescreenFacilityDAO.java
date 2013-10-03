package com.clevel.selos.dao.working;

import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.PrescreenFacility;
import com.clevel.selos.dao.GenericDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class PrescreenFacilityDAO extends GenericDAO<PrescreenFacility, Integer> {
    @Inject
    private Logger log;

    @Inject
    public PrescreenFacilityDAO(){

    }

    public List<PrescreenFacility> findByPreScreen(Prescreen prescreen){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("prescreen", prescreen));
        criteria.addOrder(Order.asc("id"));
        List<PrescreenFacility> prescreenFacilityList = criteria.list();
        log.info("findByPreScreenId. (result size: {})",prescreenFacilityList.size());

        return prescreenFacilityList;
    }

    public List<PrescreenFacility> findByPreScreenId(long preScreenId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("prescreen.id", preScreenId));
        criteria.addOrder(Order.asc("id"));
        List<PrescreenFacility> prescreenFacilityList = criteria.list();
        log.info("findByPreScreenId. (result size: {})",prescreenFacilityList.size());

        return prescreenFacilityList;
    }
}
