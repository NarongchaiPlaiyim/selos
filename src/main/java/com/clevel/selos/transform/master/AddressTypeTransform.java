package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.AddressTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AddressType;
import com.clevel.selos.model.view.master.AddressTypeView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddressTypeTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private AddressTypeDAO addressTypeDAO;

    public AddressTypeView transformToView(AddressType addressType){
        logger.debug("-- begin transformToView Address Type: {}", addressType);
        AddressTypeView addressTypeView = new AddressTypeView();
        if(addressType != null){
            addressTypeView.setId(addressType.getId());
            addressTypeView.setName(addressType.getName());
            addressTypeView.setCustomerEntityId(addressType.getCustomerEntity() == null? 0: addressType.getCustomerEntity().getId());
            addressTypeView.setActive(addressType.getActive());
        }
        logger.debug("-- return AddressTypeView: {}", addressTypeView);
        return addressTypeView;
    }

    public Map<Integer, AddressTypeView> transformToCache(List<AddressType> addressTypeList){
        logger.debug("-- transformToCache AddressType: {}", addressTypeList);

        if(addressTypeList == null || addressTypeList.size() == 0) {
            logger.debug("AddressType list is null or empty");
            return null;
        }

        Map<Integer, AddressTypeView> _tmpMap = new ConcurrentHashMap<Integer, AddressTypeView>();
        for(AddressType addressType : addressTypeList){
            AddressTypeView addressTypeView = transformToView(addressType);
            _tmpMap.put(addressTypeView.getId(), addressTypeView);
        }
        logger.debug("-- transformToCache return {} ", _tmpMap.size());
        return _tmpMap;
    }

}
