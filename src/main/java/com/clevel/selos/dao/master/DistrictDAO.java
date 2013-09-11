package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.master.District;
import com.clevel.selos.model.db.master.Province;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DistrictDAO extends GenericDAO<District,Integer> {
    @Inject
    private Logger log;

    @Inject
    public DistrictDAO() {
    }

    public List<District> getListByProvince(Province province) {
        log.info("getListByBusinessGroup. (province: {})",province);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("province", province));
        criteria.addOrder(Order.asc("name"));
        List<District> districts = criteria.list();
        log.info("getListByProvince. (result size: {})",districts.size());
        return districts;
    }
}
