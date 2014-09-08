package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.MaritalStatusDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MaritalStatus;
import com.clevel.selos.model.view.master.MaritalStatusView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MaritalStatusTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;


    @Inject
    private MaritalStatusDAO maritalStatusDAO;

    @Inject
    public MaritalStatusTransform(){}

    public MaritalStatusView transformToView(MaritalStatus maritalStatus){
        MaritalStatusView maritalStatusView = new MaritalStatusView();
        if(maritalStatus != null){
            maritalStatusView.setId(maritalStatus.getId());
            maritalStatusView.setName(maritalStatus.getName());
            maritalStatusView.setCode(maritalStatus.getCode());
            maritalStatusView.setSpouseFlag(maritalStatus.getSpouseFlag());
            maritalStatusView.setActive(maritalStatus.getActive());
        }
        return maritalStatusView;
    }

    public Map<Integer, MaritalStatusView> transformToCache(List<MaritalStatus> maritalStatusList){
        if(maritalStatusList == null || maritalStatusList.size() == 0)
            return null;
        Map<Integer, MaritalStatusView> _tmpMap = new ConcurrentHashMap<Integer, MaritalStatusView>();
        for(MaritalStatus maritalStatus : maritalStatusList){
            MaritalStatusView maritalStatusView = transformToView(maritalStatus);
            _tmpMap.put(maritalStatusView.getId(), maritalStatusView);
        }
        return _tmpMap;
    }

}
