package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.view.master.ReasonView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReasonTransform extends Transform {

    @SELOS
    @Inject
    private Logger log;
    @Inject
    private ReasonDAO reasonDAO;

    @Inject
    public ReasonTransform(){

    }

    public ReasonView transformToView(Reason reason){
        ReasonView reasonView = new ReasonView();
        if(reason == null)
            return reasonView;
        reasonView.setId(reason.getId());
        reasonView.setActive(reason.getActive());
        reasonView.setCode(reason.getCode());
        reasonView.setDescription(reason.getDescription());
        reasonView.setReasonTypeId(reason.getReasonType() == null ? 0 : reason.getReasonType().getId());
        reasonView.setUwRejectGroupId(reason.getUwRejectGroup() == null? 0: reason.getUwRejectGroup().getId());
        return reasonView;
    }

    public Map<Integer, ReasonView> transformToCache(List<Reason> reasonList){
        if(reasonList == null || reasonList.size() == 0)
            return null;

        Map<Integer, ReasonView> reasonViewMap = new ConcurrentHashMap<Integer, ReasonView>();

        for(Reason reason : reasonList){
            ReasonView reasonView = transformToView(reason);
            reasonViewMap.put(reason.getId(), reasonView);
        }
        return reasonViewMap;
    }

}
