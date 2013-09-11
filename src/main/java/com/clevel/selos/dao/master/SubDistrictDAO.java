package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.District;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.db.master.SubDistrict;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class SubDistrictDAO extends GenericDAO<SubDistrict,Integer> {
    @Inject
    private Logger log;

    @Inject
    public SubDistrictDAO() {
    }
    public List<SubDistrict> getListByDistrict(District district) {
        log.info("getListByBusinessGroup. (district: {})",district);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("district", district));
        criteria.addOrder(Order.asc("name"));
        List<SubDistrict> subDistricts = criteria.list();
        log.info("getListByDistrict. (result size: {})",subDistricts.size());
        return subDistricts;
    }
}
