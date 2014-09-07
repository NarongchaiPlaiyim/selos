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
import java.util.*;
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
        logger.debug("-- getRaceSelectItemActiveList");
        Map<Integer, RaceView> _tmpMap = getInternalCacheMap();
        List<RaceView> raceViewList = new ArrayList<RaceView>(_tmpMap.values());
        Collections.sort(raceViewList, new RaceComparator());
        List<SelectItem> raceSelectItem = new ArrayList<SelectItem>();
        for(RaceView raceView : raceViewList){
            if(Util.isTrue(raceView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(raceView.getName());
                selectItem.setValue(raceView.getId());
                raceSelectItem.add(selectItem);
                logger.debug("add SelectItem: {}", selectItem);
            }
        }
        logger.debug("getRaceSelectItemActiveList return raceViewList size: {}", raceSelectItem.size());
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
        logger.debug("-- getInternalCacheMap");
        Map<Integer, RaceView> _tmpMap = cacheLoader.getCacheMap(Race.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("RaceView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class RaceComparator implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {
            RaceView raceView1 = (RaceView) o1;
            RaceView raceView2 = (RaceView) o2;

            int flag = ((Integer)raceView1.getId()).compareTo(raceView2.getId());
            return flag;
        }
    }

}
