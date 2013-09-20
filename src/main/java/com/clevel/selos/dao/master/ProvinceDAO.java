package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Province;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

public class ProvinceDAO extends GenericDAO<Province,Integer> {
    @Inject
    private Logger log;

    @Inject
    public ProvinceDAO() {
    }

    public List<Province> getListOrderByParameter(String orderField) {
        log.info("getListOrderByParameter. (orderField: {})",orderField);
        Criteria criteria = createCriteria();
        criteria.addOrder(Order.asc(orderField));
        List<Province> provinces = criteria.list();
        log.info("getListByCustomerType. (result size: {})",provinces.size());
        return provinces;
    }
}
