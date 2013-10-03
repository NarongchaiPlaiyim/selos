package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.Customer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

public class CustomerDAO extends GenericDAO<Customer,Long> {
    @Inject
    private Logger log;

    @Inject
    public CustomerDAO() {
    }

    public List<Customer> findByWorkCasePreScreenId(long workCasePreScreenId){
        log.info("findByWorkCasePreScreenId : {}", workCasePreScreenId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        List<Customer> customerList = (List<Customer>)criteria.list();

        return customerList;
    }
}
