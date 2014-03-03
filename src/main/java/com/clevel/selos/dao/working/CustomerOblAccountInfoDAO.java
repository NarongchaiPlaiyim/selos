package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.working.CustomerOblAccountInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class CustomerOblAccountInfoDAO extends GenericDAO<CustomerOblAccountInfo, Long> {

    @Inject
    @SELOS
    Logger log;
    @Inject
    public CustomerOblAccountInfoDAO() {
    }

    public List<CustomerOblAccountInfo> findByCustomerId(long customerId){
        log.info("findByCustomerId ::: customerId : {}", customerId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customer.id", customerId));
        criteria.addOrder(Order.asc("id"));
        List<CustomerOblAccountInfo> customerOblAccountInfoList = (List<CustomerOblAccountInfo>) criteria.list();
        log.info("findByCustomerId ::: size : {}", customerOblAccountInfoList.size());
        return customerOblAccountInfoList;
    }
}
