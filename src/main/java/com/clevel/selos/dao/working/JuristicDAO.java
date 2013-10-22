package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.Juristic;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class JuristicDAO extends GenericDAO<Juristic,Long> {
    @Inject
    private Logger log;

    @Inject
    public JuristicDAO() {
    }

    public Juristic findByRegisterId(String registerId){
        log.info("findByCitizenId : {}", registerId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("registrationId", registerId));
        Juristic juristic = (Juristic)criteria.uniqueResult();

        return juristic;
    }

    public Customer findByRegistrationId(String registrationId, long workCasePreScreenId){
        log.info("findByCitizenId ::: registrationId : {}, workCasePreScreenId : {}", registrationId, workCasePreScreenId);
        String query = "SELECT customer FROM Juristic juristic WHERE juristic.customer.workCasePrescreen.id = " + workCasePreScreenId + " AND juristic.registrationId = '" + registrationId + "'";
        Customer customer = (Customer)getSession().createQuery(query).uniqueResult();

        return customer;

    }
}
