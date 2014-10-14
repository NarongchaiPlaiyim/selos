package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.view.StepView;
import com.clevel.selos.transform.StepTransform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StepControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private StepDAO stepDAO;
    @Inject
    private StepTransform stepTransform;

    @Inject
    public StepControl(){
    }

    public StepView getStepView(long stepId){
        logger.debug("-- getStepView: stepId: {}", stepId);
        Map<Long, StepView> _tmpMap = getInternalCacheMap();
        StepView stepView = _tmpMap.get(stepId);
        return stepView;
    }

    private Map<Long, StepView> loadData(){
        logger.debug("-- begin loadData --");
        List<Step> stepList = stepDAO.findAll();
        Map<Long, StepView> _tmpMap = stepTransform.transformToCache(stepList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty StepView");
            return new ConcurrentHashMap<Long, StepView>();
        } else {
            cacheLoader.setCacheMap(Step.class.getName(), _tmpMap);
            logger.debug("loadData return StepView size: {} ", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Long, StepView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap ");
        Map<Long, StepView> _tmpMap = cacheLoader.getCacheMap(Step.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("StepView is null or empty, reload from DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

}
