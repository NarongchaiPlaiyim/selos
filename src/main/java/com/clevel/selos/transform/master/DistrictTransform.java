package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.District;
import com.clevel.selos.model.view.master.DistrictView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DistrictTransform extends Transform {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public DistrictTransform(){}

    public DistrictView transformToView(District district){
        DistrictView districtView = new DistrictView();
        if(district != null){
            districtView.setId(district.getId());
            districtView.setName(district.getName());
            districtView.setProvinceCode(district.getProvince() == null? 0: district.getProvince().getCode());
            districtView.setActive(district.getActive());
        }
        return districtView;
    }

    public Map<Integer, DistrictView> transformToCache(List<District> districtList){
        if(districtList == null || districtList.size() == 0)
            return null;
        Map<Integer, DistrictView> _tmpMap = new ConcurrentHashMap<Integer, DistrictView>();
        for(District district : districtList){
            DistrictView districtView = transformToView(district);
            _tmpMap.put(districtView.getId(), districtView);
        }
        return _tmpMap;
    }
}
