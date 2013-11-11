package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.CustomerAccount;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class CustomerAccountDAO extends GenericDAO<CustomerAccount, Long> {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    public CustomerAccountDAO(){

    }

    public List<CustomerAccount> getCustomerAccountByCustomerId(long customerId){
        log.info("getCustomerAccountByCustomerId ::: customerId : {}", customerId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customer.id", customerId));
        criteria.addOrder(Order.asc("id"));
        List<CustomerAccount> customerAccountList = (List<CustomerAccount>) criteria.list();
        log.info("getCustomerAccountByCustomerId ::: size : {}", customerAccountList.size());
        return customerAccountList;
    }
}
