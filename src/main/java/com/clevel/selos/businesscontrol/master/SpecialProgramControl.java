package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.SpecialProgramDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SpecialProgram;
import com.clevel.selos.model.view.master.SpecialProgramView;
import com.clevel.selos.transform.master.SpecialProgramTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpecialProgramControl extends BusinessControl{
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private SpecialProgramDAO specialProgramDAO;

    @Inject
    private SpecialProgramTransform specialProgramTransform;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    public SpecialProgramControl(){}

    public List<SelectItem> getSpecialProgramSelectItem(){
        logger.debug("-- getSpecialProgramSelectItem");
        Map<Integer, SpecialProgramView> _tmpMap = getInternalCacheMap();
        List<SelectItem> _tmpList = new ArrayList<SelectItem>();
        for(SpecialProgramView specialProgramView : _tmpMap.values()){
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(specialProgramView.getCode());
            selectItem.setValue(specialProgramView.getId());
            _tmpList.add(selectItem);
            logger.debug("add SelectItem: {}", selectItem);
        }
        logger.debug("getSpecialProgramSelectItem return SpecialProgramView size: {}", _tmpList.size());
        return _tmpList;
    }

    private Map<Integer, SpecialProgramView> loadData(){
        logger.debug("-- begin loadData --");
        List<SpecialProgram> specialProgramList = specialProgramDAO.findAll();
        Map<Integer, SpecialProgramView> _tmpMap = specialProgramTransform.transformToCache(specialProgramList);
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("return empty SpecialProgramView");
            return new ConcurrentHashMap<Integer, SpecialProgramView>();
        }
        else {
            cacheLoader.setCacheMap(SpecialProgram.class.getName(), _tmpMap);
            logger.debug("loadData return SBFScoreView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, SpecialProgramView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, SpecialProgramView> _tmpMap = cacheLoader.getCacheMap(SpecialProgram.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("SpecialProgramView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

}
