package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Customer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class CustomerDAO extends GenericDAO<Customer, Long> {
    @Inject
    private Logger log;

    @Inject
    public CustomerDAO() {
    }

    public List<Customer> findBorrowerByWorkCasePreScreenId(long workCasePreScreenId) {
        log.info("findByWorkCasePreScreenId ::: workCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.add(Restrictions.eq("relation.id", 1));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("findByWorkCasePreScreenId ::: size : {}", workCasePreScreenId);
        return customerList;
    }

    public List<Customer> findByWorkCasePreScreenId(long workCasePreScreenId) {
        log.info("findByWorkCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();

        return customerList;
    }

    public List<Customer> findCustomerByWorkCasePreScreenId(long workCasePreScreenId) {
        log.info("findByWorkCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.add(Restrictions.eq("isSpouse", 0));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();

        return customerList;
    }

    public Customer findSpouseById(long spouseId) {
        log.debug("findSpouseById ::: spouseId : {}", spouseId);
        Customer customer = new Customer();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("id", spouseId));
        customer = (Customer) criteria.uniqueResult();

        return customer;
    }


    public Customer findByCitizenlId(String citizenId, long workCasePreScreenId) {
        log.info("findByCitizenlId : {}", citizenId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.add(Restrictions.eq("individual.citizenId", citizenId));
        Customer customer = (Customer) criteria.uniqueResult();

        return customer;
    }

    public Customer findByRegistrationId(String registrationId) {
        log.info("findByRegistrationId : {}", registrationId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("juristic.registrationId", registrationId));
        Customer customer = (Customer) criteria.uniqueResult();
        return customer;
    }

    public List<Customer> findByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        List<Customer> customerList = (List<Customer>) criteria.list();

        return customerList;
    }

    public Customer findCustomerBySpouseId(long spouseId) {
        log.debug("findSpouseById ::: spouseId : {}", spouseId);
        Customer customer = new Customer();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("spouseId", spouseId));
        customer = (Customer) criteria.uniqueResult();

        return customer;
    }
}
