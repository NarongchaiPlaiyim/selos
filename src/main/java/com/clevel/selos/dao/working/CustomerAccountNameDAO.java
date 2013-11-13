package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.CustomerAccountName;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class CustomerAccountNameDAO extends GenericDAO<CustomerAccountName, Long> {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    public CustomerAccountNameDAO(){

    }

    public List<CustomerAccountName> getCustomerAccountNameByCustomerId(long customerId){
        log.info("getCustomerAccountNameByCustomerId ::: customerId : {}", customerId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customer.id", customerId));
        criteria.addOrder(Order.asc("id"));
        List<CustomerAccountName> customerAccountNameList = (List<CustomerAccountName>) criteria.list();
        log.info("getCustomerAccountNameByCustomerId ::: size : {}", customerAccountNameList.size());
        return customerAccountNameList;
    }

    public List<CustomerAccountName> getCustomerAccountNameByCustomer(Customer customer){
        log.info("getCustomerAccountNameByCustomer ::: customer.id : {}", customer);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customer", customer));
        criteria.addOrder(Order.asc("id"));
        List<CustomerAccountName> customerAccountNameList = (List<CustomerAccountName>) criteria.list();
        log.info("getCustomerAccountNameByCustomer ::: size : {}", customerAccountNameList.size());
        return customerAccountNameList;
    }
}
