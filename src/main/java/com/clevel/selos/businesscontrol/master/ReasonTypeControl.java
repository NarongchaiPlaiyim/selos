package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.ReasonTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ReasonType;
import com.clevel.selos.model.view.master.ReasonTypeView;
import com.clevel.selos.transform.master.ReasonTypeTransform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReasonTypeControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private ReasonTypeDAO reasonTypeDAO;
    @Inject
    private ReasonTypeTransform reasonTypeTransform;

    @Inject
    public ReasonTypeControl(){}

    private Map<Integer, ReasonTypeView> loadData(){
        logger.debug("-- begin loadData --");
        List<ReasonType> reasonTypeList = reasonTypeDAO.findAll();
        Map<Integer, ReasonTypeView> _tmpMap = reasonTypeTransform.transformToCache(reasonTypeList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty ReasonTypeView");
            return new ConcurrentHashMap<Integer, ReasonTypeView>();
        } else {
            cacheLoader.setCacheMap(ReasonType.class.getName(), _tmpMap);
            logger.debug("loadData return ReasonTypeView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, ReasonTypeView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, ReasonTypeView> _tmpMap = cacheLoader.getCacheMap(ReasonType.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("ReasonTypeView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }
}
