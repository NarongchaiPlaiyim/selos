package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.AddressType;
import com.clevel.selos.model.db.master.CustomerEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AddressTypeDAO extends GenericDAO<AddressType, Integer> {
    @Inject
    private Logger log;

    @Inject
    public AddressTypeDAO() {
    }

    public List<AddressType> findByCustomerEntityId(int customerEntityId) {
        log.info("findByCustomerEntityId : {}", customerEntityId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity.id", customerEntityId));
        criteria.addOrder(Order.asc("id"));
        List<AddressType> addressTypeList = criteria.list();

        return addressTypeList;
    }
}
