package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Occupation;
import com.clevel.selos.model.view.master.OccupationView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OccupationTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    public OccupationView transformToView(Occupation occupation){
        OccupationView occupationView = new OccupationView();
        if(occupation != null) {
            occupationView.setId(occupation.getId());
            occupationView.setCode(occupation.getCode());
            occupationView.setDescription(occupation.getDescription());
            occupationView.setActive(occupation.getActive());
        }
        return occupationView;
    }

    public Map<Integer, OccupationView> transformToCache(List<Occupation> occupationList){
        if(occupationList == null || occupationList.size() == 0)
            return null;
        Map<Integer, OccupationView> _tmpMap = new ConcurrentHashMap<Integer, OccupationView>();
        for(Occupation occupation : occupationList){
            OccupationView occupationView = transformToView(occupation);
            _tmpMap.put(occupationView.getId(), occupationView);
        }
        return _tmpMap;
    }
}
