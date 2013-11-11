package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.CustomerCSI;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class CustomerCSIDAO extends GenericDAO<CustomerCSI, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public CustomerCSIDAO() {
    }

    public List<CustomerCSI> findCustomerCSIByCustomerId(long customerId) {
        log.debug("findCustomerCSIByCustomerId ::: customerId : {}", customerId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customer.id", customerId));
        List<CustomerCSI> customerCSIList = (List<CustomerCSI>) criteria.list();

        return customerCSIList;
    }
}
