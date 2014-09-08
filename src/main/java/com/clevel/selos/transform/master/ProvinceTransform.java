package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.ProvinceDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.view.master.ProvinceView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProvinceTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ProvinceDAO provinceDAO;

    @Inject
    public ProvinceTransform(){}

    public ProvinceView transformToView(Province province){
        ProvinceView provinceView = new ProvinceView();
        if(province != null){
            provinceView.setCode(province.getCode());
            provinceView.setName(province.getName());
            provinceView.setRegionCode(province.getRegion() == null? 0: province.getRegion().getCode());
            provinceView.setActive(province.getActive());
        }
        return provinceView;
    }

    public Map<Integer, ProvinceView> transformToCache(List<Province> provinceList){
        if(provinceList == null || provinceList.size() == 0)
            return null;

        Map<Integer, ProvinceView> _tmpMap = new ConcurrentHashMap<Integer, ProvinceView>();
        for(Province province : provinceList){
            ProvinceView provinceView = transformToView(province);
            _tmpMap.put(provinceView.getCode(), provinceView);
        }
        return _tmpMap;
    }

}
