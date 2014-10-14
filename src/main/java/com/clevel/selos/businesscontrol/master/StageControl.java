package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.StageDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Stage;
import com.clevel.selos.model.view.StageView;
import com.clevel.selos.transform.StageTransform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StageControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private StageDAO stageDAO;
    @Inject
    private StageTransform stageTransform;

    @Inject
    public StageControl(){}

    private Map<Integer, StageView> loadData(){
        logger.debug("-- begin loadData --");
        List<Stage> stageList = stageDAO.findAll();
        Map<Integer, StageView> _tmpMap = stageTransform.transformToCache(stageList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty StageView");
            return new ConcurrentHashMap<Integer, StageView>();
        } else {
            cacheLoader.setCacheMap(Stage.class.getName(), _tmpMap);
            logger.debug("loadData return StageView size: {} ", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, StageView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap ");
        Map<Integer, StageView> _tmpMap = cacheLoader.getCacheMap(Stage.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("StageView is null or empty, reload from DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

}
