package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.RequestTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.RequestType;
import com.clevel.selos.model.view.master.RequestTypeView;
import com.clevel.selos.transform.master.RequestTypeTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestTypeControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private RequestTypeDAO requestTypeDAO;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private RequestTypeTransform requestTypeTransform;

    @Inject
    public RequestTypeControl(){}

    public List<SelectItem> getRequestTypeViewActive(){
        logger.debug("-- getRequestTypeViewActive");
        Map<Integer, RequestTypeView> _tmpMap = getInternalCacheMap();
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(RequestTypeView requestTypeView : _tmpMap.values()){
            if(Util.isTrue(requestTypeView.getActive())) {
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(requestTypeView.getName());
                selectItem.setValue(requestTypeView.getId());
                selectItemList.add(selectItem);
                logger.debug("add SelectItem: {}", selectItem);
            }
        }
        logger.debug("getRequestTypeViewActive return RequestTypeView size:{}", selectItemList.size());
        return selectItemList;
    }

    private Map<Integer, RequestTypeView> loadData(){
        logger.debug("-- begin loadData --");
        List<RequestType> requestTypeList = requestTypeDAO.findAll();
        Map<Integer, RequestTypeView> _tmpMap = requestTypeTransform.transformToCache(requestTypeList);
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("return empty RequestTypeView");
            return new ConcurrentHashMap<Integer, RequestTypeView>();
        }
        else {
            cacheLoader.setCacheMap(RequestType.class.getName(), _tmpMap);
            logger.debug("loadData return RequestTypeView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, RequestTypeView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, RequestTypeView> _tmpMap = cacheLoader.getCacheMap(RequestType.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("RequestTypeView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }
}
