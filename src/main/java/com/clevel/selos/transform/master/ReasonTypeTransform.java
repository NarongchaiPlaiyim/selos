package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ReasonType;
import com.clevel.selos.model.view.master.ReasonTypeView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReasonTypeTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public ReasonTypeTransform(){}

    public ReasonTypeView transformToView(ReasonType reasonType){
        ReasonTypeView reasonTypeView = new ReasonTypeView();
        if(reasonType == null)
            return reasonTypeView;

        reasonTypeView.setId(reasonType.getId());
        reasonTypeView.setActive(reasonType.getActive());
        reasonTypeView.setDescription(reasonType.getDescription());
        reasonTypeView.setName(reasonType.getName());
        return reasonTypeView;
    }

    public Map<Integer, ReasonTypeView> transformToCache(List<ReasonType> reasonTypeList){
        if(reasonTypeList == null)
            return null;
        Map<Integer, ReasonTypeView> _tmpMap = new ConcurrentHashMap<Integer, ReasonTypeView>();
        for(ReasonType reasonType : reasonTypeList){
            ReasonTypeView reasonTypeView = transformToView(reasonType);
            _tmpMap.put(reasonTypeView.getId(), reasonTypeView);
        }
        return _tmpMap;
    }

}
