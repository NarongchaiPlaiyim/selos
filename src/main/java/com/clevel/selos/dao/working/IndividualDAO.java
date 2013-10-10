package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Individual;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class IndividualDAO extends GenericDAO<Individual,Long> {
    @Inject
    private Logger log;

    @Inject
    public IndividualDAO() {
    }

    public Individual findByCitizenId(String citizenId){
        log.info("findByCitizenId : {}", citizenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("citizenId", citizenId));
        Individual individual = (Individual)criteria.uniqueResult();

        return individual;
    }
}
