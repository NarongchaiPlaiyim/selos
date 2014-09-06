package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.CustomerEntityDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.view.master.CustomerEntityView;
import com.clevel.selos.transform.master.CustomerEntityTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CustomerEntityControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private CustomerEntityDAO customerEntityDAO;
    @Inject
    private CustomerEntityTransform customerEntityTransform;

    @Inject
    public CustomerEntityControl(){}

    public List<SelectItem> getCustomerEntitySelectItemList(){
        Map<Integer, CustomerEntityView> _tmpMap = getInternalCacheMap();
        List<CustomerEntityView> customerEntityViewList = new ArrayList<CustomerEntityView>(_tmpMap.values());
        Collections.sort(customerEntityViewList, new CustomerEntityComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(CustomerEntityView customerEntityView : customerEntityViewList){
            if(Util.isTrue(customerEntityView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(customerEntityView.getDescription());
                selectItem.setValue(customerEntityView.getId());
                selectItemList.add(selectItem);
            }
        }
        return selectItemList;
    }

    private Map<Integer, CustomerEntityView> loadData(){
        logger.debug("-- begin loadData --");
        List<CustomerEntity> customerEntityList = customerEntityDAO.findAll();
        Map<Integer, CustomerEntityView> _tmpMap = customerEntityTransform.transformToCache(customerEntityList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Business Type View");
            return new ConcurrentHashMap<Integer, CustomerEntityView>();
        } else {
            cacheLoader.setCacheMap(CustomerEntity.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, CustomerEntityView> getInternalCacheMap(){
        Map<Integer, CustomerEntityView> _tmpMap = cacheLoader.getCacheMap(CustomerEntity.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class CustomerEntityComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            CustomerEntityView customerEntityView1 = (CustomerEntityView) o1;
            CustomerEntityView customerEntityView2 = (CustomerEntityView) o2;

            int flag = ((Integer)customerEntityView1.getId()).compareTo(customerEntityView2.getId());

            return flag;
        }
    }
}
