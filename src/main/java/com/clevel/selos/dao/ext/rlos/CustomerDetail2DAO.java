package com.clevel.selos.dao.ext.rlos;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.ext.rlos.CustomerDetail2;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CustomerDetail2DAO extends GenericDAO<CustomerDetail2,Long> {
    @Inject
    private Logger log;

    @Inject
    public CustomerDetail2DAO() {
    }

    public List<CustomerDetail2> getListCustomerByCitizenId(List<String> citizenIdList){
        log.debug("getListCustomerByCitizenId (citizenId : {})",citizenIdList);
        List<CustomerDetail2> customerDetail2List = new ArrayList<CustomerDetail2>();
        if(citizenIdList!=null && citizenIdList.size()>0){
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.in("citizenId", citizenIdList));
            customerDetail2List = criteria.list();

            log.debug("getListCustomerByCitizenId , result (customerDetail1List : {})",customerDetail2List);
        }

        return customerDetail2List;
    }
}
