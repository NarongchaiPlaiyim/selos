package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.District;
import com.clevel.selos.model.db.master.Province;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class DistrictDAO extends GenericDAO<District, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public DistrictDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<District> getListByProvince(Province province) {
        log.debug("getListByProvince. (province: {})", province);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("province", province));
        criteria.addOrder(Order.asc("name"));
        List<District> districts = criteria.list();
        log.debug("getListByProvince. (result size: {})", districts.size());
        return districts;
    }
    @SuppressWarnings("unchecked")
    public List<District> getListByProvince(int provinceId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("province.code", provinceId));
        criteria.add(Restrictions.eq("active",1));
        criteria.addOrder(Order.asc("name"));
        List<District> districts = criteria.list();
        return districts;
    }
    

    public District getByNameAndProvince(String districtName, Province province) {
        log.debug("getByNameAndProvince. (districtName: {}, province: {})", districtName, province);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("province", province));
        criteria.add(Restrictions.eq("name", districtName));
        District district = (District) criteria.uniqueResult();
        log.debug("getByNameAndProvince. (result : {})", district);
        return district;
    }
}
