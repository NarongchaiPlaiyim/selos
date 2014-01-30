package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.working.Customer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class CustomerDAO extends GenericDAO<Customer, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public CustomerDAO() {
    }

    public List<Customer> findBorrowerByWorkCasePreScreenId(long workCasePreScreenId) {
        log.info("findByWorkCasePreScreenId ::: workCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.add(Restrictions.eq("relation.id", RelationValue.BORROWER.value()));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("findByWorkCasePreScreenId ::: size : {}", customerList.size());
        return customerList;
    }

    public List<Customer> findBorrowerByWorkCaseId(long workCaseId){
        log.info("findBorrowerByWorkCaseId ::: workCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("relation.id", RelationValue.BORROWER.value()));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();
        log.info("findBorrowerByWorkCaseId ::: size : {}", customerList.size());
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
        criteria.addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        List<Customer> customerList = (List<Customer>) criteria.list();

        return customerList;
    }

    public Customer findMainCustomerBySpouseId(long spouseId) {
        log.debug("findMainCustomerBySpouseId ::: spouseId : {}", spouseId);
        Customer customer = new Customer();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("spouseId", spouseId));
        customer = (Customer) criteria.uniqueResult();

        return customer;
    }

    public List<Customer> findCustomerByCommitteeId(long committeeId) {
        log.debug("findCustomerByCommitteeId ::: committeeId : {}", committeeId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("juristicId", committeeId));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>) criteria.list();

        return customerList;
    }

    public List<Customer> findGuarantorByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("relation.id", RelationValue.GUARANTOR.value()));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>)criteria.list();
        log.info("criteria.list() :: {}",criteria.list());
        return customerList;
    }

    public List<Customer>  findCollateralOwnerUWByWorkCaseId(long workCaseId) {
        log.info("findByWorkCaseId : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.or(Restrictions.eq("relation.id", RelationValue.BORROWER.value()),Restrictions.eq("relation.id", RelationValue.GUARANTOR.value())));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>)criteria.list();
        log.info("criteria.list() :: {}",criteria.list());
        return customerList;
    }

    public List<Customer>  findCustomerBorrowerAndGuarantor(long workCaseId) {
        log.info("findCustomerBorrowerAndGuarantor : {}", workCaseId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.or(Restrictions.eq("relation.id", RelationValue.BORROWER.value()),Restrictions.eq("relation.id", RelationValue.GUARANTOR.value())));
        criteria.addOrder(Order.asc("id"));
        List<Customer> customerList = (List<Customer>)criteria.list();
        log.info("criteria.list() :: {}",criteria.list());
        return customerList;
    }

}
