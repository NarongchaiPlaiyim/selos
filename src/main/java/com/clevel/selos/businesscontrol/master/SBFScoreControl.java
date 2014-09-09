package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.SBFScoreDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SBFScore;
import com.clevel.selos.model.view.master.SBFScoreView;
import com.clevel.selos.transform.master.SBFScoreTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SBFScoreControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private SBFScoreDAO sbfScoreDAO;
    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private SBFScoreTransform sbfScoreTransform;

    @Inject
    public SBFScoreControl(){}

    public List<SelectItem> getSBFScoreActive(){
        logger.debug("-- getSBFScoreActive");
        Map<Integer, SBFScoreView> _tmpMap = getInternalCacheMap();
        List<SelectItem> sbfScoreViewList = new ArrayList<SelectItem>();
        for(SBFScoreView sbfScoreView : _tmpMap.values()){
            if(Util.isTrue(sbfScoreView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(String.valueOf(sbfScoreView.getScore()));
                selectItem.setValue(sbfScoreView.getId());
                sbfScoreViewList.add(selectItem);
                logger.debug("add SelectItem: {}", selectItem);
            }
        }
        logger.debug("getSBFScoreActive return SBFScoreView size: {}", sbfScoreViewList.size());
        return sbfScoreViewList;
    }

    private Map<Integer, SBFScoreView> loadData(){
        logger.debug("-- begin loadData --");
        List<SBFScore> sbfScoreList = sbfScoreDAO.findAll();
        Map<Integer, SBFScoreView> _tmpMap = sbfScoreTransform.transformToCache(sbfScoreList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty SBFScoreView");
            return new ConcurrentHashMap<Integer, SBFScoreView>();
        } else {
            cacheLoader.setCacheMap(SBFScore.class.getName(), _tmpMap);
            logger.debug("loadData return SBFScoreView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, SBFScoreView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, SBFScoreView> _tmpMap = cacheLoader.getCacheMap(SBFScore.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("SBFScoreView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }
}
