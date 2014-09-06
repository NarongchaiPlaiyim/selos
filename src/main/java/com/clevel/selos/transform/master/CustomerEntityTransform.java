package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.view.master.CustomerEntityView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomerEntityTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    public CustomerEntityView transformToView(CustomerEntity customerEntity){
        CustomerEntityView customerEntityView = new CustomerEntityView();
        if(customerEntity != null){
            customerEntityView.setId(customerEntity.getId());
            customerEntityView.setDescription(customerEntity.getDescription());
            customerEntityView.setBrmsCode(customerEntity.getBrmsCode());
            customerEntityView.setChangeQualtiEnable(customerEntity.isChangeQualtiEnable());
            customerEntityView.setDefaultQualitative(customerEntity.getDefaultQualitative());
            customerEntityView.setActive(customerEntity.getActive());
        }
        return customerEntityView;
    }

    public Map<Integer, CustomerEntityView> transformToCache(List<CustomerEntity> customerEntityList) {
        if(customerEntityList == null || customerEntityList.size() == 0)
            return null;

        Map<Integer, CustomerEntityView> _tmpMap = new ConcurrentHashMap<Integer, CustomerEntityView>();
        for(CustomerEntity customerEntity : customerEntityList){
            CustomerEntityView customerEntityView = transformToView(customerEntity);
            _tmpMap.put(customerEntityView.getId(), customerEntityView);
        }
        return _tmpMap;
    }
}
