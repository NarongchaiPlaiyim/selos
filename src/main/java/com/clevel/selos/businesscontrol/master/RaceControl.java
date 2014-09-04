package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.dao.master.RaceDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Race;
import com.clevel.selos.model.view.master.RaceView;
import com.clevel.selos.transform.master.RaceTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RaceControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private RaceTransform raceTransform;

    @Inject
    private RaceDAO raceDAO;

    public List<SelectItem> getRaceSelectItemActiveList(){
        Map<Integer, RaceView> _tmpMap = getInternalCacheMap();
        List<SelectItem> raceSelectItem = new ArrayList<SelectItem>();
        for(RaceView raceView : _tmpMap.values()){
            if(Util.isTrue(raceView.getId())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(raceView.getName());
                selectItem.setValue(raceView.getId());
                raceSelectItem.add(selectItem);
            }
        }
        return raceSelectItem;
    }

    private Map<Integer, RaceView> loadData(){
        logger.debug("-- begin loadData --");
        List<Race> raceList = raceDAO.findAll();
        Map<Integer, RaceView> _tmpMap = raceTransform.transformToCache(raceList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Race View");
            return new ConcurrentHashMap<Integer, RaceView>();
        } else {
            cacheLoader.setCacheMap(Race.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, RaceView> getInternalCacheMap(){
        Map<Integer, RaceView> _tmpMap = cacheLoader.getCacheMap(Race.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }
}
