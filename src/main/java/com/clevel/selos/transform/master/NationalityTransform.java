package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Nationality;
import com.clevel.selos.model.view.master.NationalityView;
import com.clevel.selos.transform.Transform;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NationalityTransform extends Transform {

    @Inject
    @SELOS
    private org.slf4j.Logger logger;

    public NationalityView transformToView(Nationality nationality){
        NationalityView nationalityView = new NationalityView();
        if(nationality != null){
            nationalityView.setId(nationality.getId());
            nationalityView.setName(nationality.getName());
            nationalityView.setCode(nationality.getCode());
            nationalityView.setActive(nationality.getActive());
        }
        return nationalityView;
    }

    public Map<Integer, NationalityView> transformToCache(List<Nationality> nationalityList){
        if(nationalityList == null || nationalityList.size() == 0)
            return null;

        Map<Integer, NationalityView> _tmpMap = new ConcurrentHashMap<Integer, NationalityView>();
        for(Nationality nationality : nationalityList){
            NationalityView nationalityView = transformToView(nationality);
            _tmpMap.put(nationalityView.getId(), nationalityView);
        }
        return _tmpMap;
    }
}
