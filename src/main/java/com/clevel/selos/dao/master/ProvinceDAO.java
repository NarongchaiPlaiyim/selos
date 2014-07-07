package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

public class ProvinceDAO extends GenericDAO<Province, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ProvinceDAO() {
    }

    @Override
    public List<Province> findAll() {
        log.debug("findAll.");
        Criteria criteria = createCriteria();
        criteria.setCacheable(true);
        List<Province> provinces = criteria.list();
        log.debug("findAll. (result: {})",provinces.size());
        return provinces;
    }

    public List<Province> findAllASC(){
        log.debug("-- findAllASC()");
        List<Province> provinceList = null;
        Criteria criteria = createCriteria();
        criteria.addOrder(Order.asc("name"));
        provinceList = Util.safetyList((List<Province>) criteria.list());
        int round = 0;
        if(Util.isSafetyList(provinceList)){
            for(final Province province : provinceList){
                if(province.getCode() == 10){
                    Collections.swap(provinceList, 0, round);
                }
                round++;
            }
        }
        log.debug("-- ProvinceList.size()[{}]", provinceList.size());
        return provinceList;
    }

    public List<Province> getListOrderByParameter(String orderField) {
        log.info("getListOrderByParameter. (orderField: {})", orderField);
        Criteria criteria = createCriteria();
        criteria.addOrder(Order.asc(orderField));
        criteria.setCacheable(true);
        List<Province> provinces = criteria.list();
        log.info("getListOrderByParameter. (result size: {})", provinces.size());
        return provinces;
    }

    public Province getByName(String provinceName) {
        log.info("getByName. (provinceName: {})", provinceName);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("name", provinceName));
        Province province = (Province) criteria.uniqueResult();
        log.info("getByName . (result : {})", province);
        return province;
    }

    public void clearCache() {
        log.debug("clearCache.");
        clearCache("com.clevel.selos.model.db.master.Province");
    }
}
