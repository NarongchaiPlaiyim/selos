package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SubDistrict;
import com.clevel.selos.model.view.master.SubDistrictView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubDistrictTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public SubDistrictTransform(){}

    public SubDistrictView transformToView(SubDistrict subDistrict){
        SubDistrictView subDistrictView = new SubDistrictView();
        if(subDistrictView != null){
            subDistrictView.setCode(subDistrict.getCode());
            subDistrictView.setName(subDistrict.getName());
            subDistrictView.setDistrictId(subDistrict.getDistrict() == null? 0: subDistrict.getDistrict().getId());
            subDistrictView.setProvinceCode(subDistrict.getProvince() == null? 0: subDistrict.getProvince().getCode());
            subDistrictView.setActive(subDistrict.getActive());
        }
        return subDistrictView;
    }

    public Map<Integer, SubDistrictView> transformToCache(List<SubDistrict> subDistrictList){
        if(subDistrictList == null || subDistrictList.size() == 0)
            return null;

        Map<Integer, SubDistrictView> _tmpMap = new ConcurrentHashMap<Integer, SubDistrictView>();
        for(SubDistrict subDistrict : subDistrictList){
            SubDistrictView subDistrictView = transformToView(subDistrict);
            _tmpMap.put(subDistrictView.getCode(), subDistrictView);
        }
        return _tmpMap;
    }
}
