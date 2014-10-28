package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.ReasonDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.view.master.ReasonView;
import com.clevel.selos.transform.master.ReasonTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReasonControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private ReasonDAO reasonDAO;
    @Inject
    private ReasonTransform reasonTransform;

    @Inject
    public ReasonControl(){}

    public List<SelectItem> getReasonSelectItem(int reasonTypeId){
        logger.debug("-- begin getReasonSelectItem: {}", reasonTypeId);
        List<SelectItem> _tmpList = new ArrayList<SelectItem>();
        Map<Integer, ReasonView> _tmpMap = getInternalCacheMap();
        for(ReasonView reasonView : _tmpMap.values()){
            if(reasonView.getReasonTypeId() == reasonTypeId){
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(reasonView.getId());
                selectItem.setLabel(reasonView.getDescription());
                _tmpList.add(selectItem);
            }
        }
        logger.debug("-- end getReasonSelectItem return: {}", _tmpList.size());
        return _tmpList;
    }

    private Map<Integer, ReasonView> loadData(){
        logger.debug("-- begin loadData --");
        List<Reason> reasonList = reasonDAO.findAll();
        Map<Integer, ReasonView> _tmpMap = reasonTransform.transformToCache(reasonList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty ReasonView");
            return new ConcurrentHashMap<Integer, ReasonView>();
        } else {
            cacheLoader.setCacheMap(Reason.class.getName(), _tmpMap);
            logger.debug("loadData return ReasonView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, ReasonView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, ReasonView> _tmpMap = cacheLoader.getCacheMap(Reason.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("ReasonView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }
}
