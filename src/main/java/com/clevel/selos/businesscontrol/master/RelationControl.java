package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.RelationDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Relation;
import com.clevel.selos.model.view.master.RelationView;
import com.clevel.selos.transform.master.RelationTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RelationControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private RelationDAO relationDAO;

    @Inject
    private RelationTransform relationTransform;

    @Inject
    public RelationControl(){}

    public RelationView getRelationViewById(int relationId){
        logger.debug("-- getRelationViewById");
        Map<Integer, RelationView> _tmpMap = getInternalCacheMap();
        RelationView relationView = _tmpMap.get(relationId);
        if(relationView != null){
            logger.debug("getRelationViewById return RelationView: {}", relationView);
            return relationView;
        }
        logger.debug("return empty RelationView");
        return new RelationView();
    }

    private Map<Integer, RelationView> loadData(){
        logger.debug("-- begin loadData --");
        List<Relation> relationList = relationDAO.findAll();
        Map<Integer, RelationView> _tmpMap = relationTransform.transformToCache(relationList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty RelationView");
            return new ConcurrentHashMap<Integer, RelationView>();
        } else {
            cacheLoader.setCacheMap(Relation.class.getName(), _tmpMap);
            logger.debug("loadData return RelationView size:{}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, RelationView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, RelationView> _tmpMap = cacheLoader.getCacheMap(Relation.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("RelationView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

}
