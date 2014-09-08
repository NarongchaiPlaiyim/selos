package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BusinessType;
import com.clevel.selos.model.view.master.BusinessTypeView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BusinessTypeTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public BusinessTypeTransform(){}

    public BusinessTypeView transformToView(BusinessType businessType){
        BusinessTypeView businessTypeView = new BusinessTypeView();
        if(businessTypeView != null) {
            businessTypeView.setId(businessType.getId());
            businessTypeView.setName(businessType.getName());
            businessTypeView.setDescription(businessType.getDescription());
            businessTypeView.setActive(businessType.getActive());
        }
        return businessTypeView;
    }

    public Map<Integer, BusinessTypeView> transformToCache(List<BusinessType> businessTypeList){
        if(businessTypeList == null || businessTypeList.size() == 0)
            return null;

        Map<Integer, BusinessTypeView> _tmpMap = new ConcurrentHashMap<Integer, BusinessTypeView>();
        for(BusinessType businessType : businessTypeList){
            BusinessTypeView businessTypeView = transformToView(businessType);
            _tmpMap.put(businessTypeView.getId(), businessTypeView);
        }
        return _tmpMap;
    }

}
