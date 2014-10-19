package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.MandateDocAccessDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateDocAccess;
import com.clevel.selos.model.view.master.MandateDocAccessView;
import com.clevel.selos.transform.master.MandateDocAccessTransform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MandateDocAccessControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private MandateDocAccessDAO mandateDocAccessDAO;
    @Inject
    private MandateDocAccessTransform mandateDocAccessTransform;

    @Inject
    private ApplicationCacheLoader cacheLoader;


    @Inject
    public MandateDocAccessControl(){}

    public MandateDocAccessView getMandateDocAccessView(long stepId, long roleId){
        Map<Long, MandateDocAccessView> mandateDocAccessViewMap = getInternalCacheMap();
        for(MandateDocAccessView mandateDocAccessView : mandateDocAccessViewMap.values()){
            if(mandateDocAccessView.getRoleId() == roleId && mandateDocAccessView.getStepId() == stepId){
                return mandateDocAccessView;
            }
        }
        return null;
    }

    private Map<Long, MandateDocAccessView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap ");
        Map<Long, MandateDocAccessView> _tmpMap = cacheLoader.getCacheMap(MandateDocAccess.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("CustomerEntityView is null or empty, reload from DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private Map<Long, MandateDocAccessView> loadData(){
        logger.debug("-- begin loadData --");
        List<MandateDocAccess> mandateDocAccessList = mandateDocAccessDAO.findAll();
        Map<Long, MandateDocAccessView> _tmpMap = mandateDocAccessTransform.transformToCache(mandateDocAccessList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty MandateDocAccessView");
            return new ConcurrentHashMap<Long, MandateDocAccessView>();
        } else {
            cacheLoader.setCacheMap(MandateDocAccess.class.getName(), _tmpMap);
            logger.debug("loadData return MandateDocAccessView size: {} ", _tmpMap.size());
            return _tmpMap;
        }
    }



}
