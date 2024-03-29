package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.Individual;
import org.slf4j.Logger;

import javax.inject.Inject;

public class IndividualDAO extends GenericDAO<Individual, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public IndividualDAO() {
    }

    public Customer findByCitizenId(String citizenId, long workCasePreScreenId) {
        log.info("findByCitizenId ::: citizenId : {}, workCasePreScreenId : {}", citizenId, workCasePreScreenId);
        String query = "SELECT customer FROM Individual individual WHERE individual.customer.workCasePrescreen.id = " + workCasePreScreenId + " AND individual.citizenId = '" + citizenId + "'";
        Customer customer = (Customer) getSession().createQuery(query).uniqueResult();

        return customer;
    }

    public Customer findCustomerByCitizenIdAndWorkCase(String citizenId, long workCaseId) {
        log.info("findCustomerByCitizenIdAndWorkCase ::: citizenId : {}, workCaseId : {}", citizenId, workCaseId);
        String query = "SELECT customer FROM Individual individual WHERE individual.customer.workCase.id = " + workCaseId + " AND individual.citizenId = '" + citizenId + "'";
        Customer customer = (Customer) getSession().createQuery(query).uniqueResult();

        return customer;
    }
}
