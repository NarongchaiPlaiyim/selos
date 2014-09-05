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
        Map<Integer, RelationView> _tmpMap = getInternalCacheMap();
        RelationView relationView = _tmpMap.get(relationId);
        if(relationView != null)
            return relationView;
        return new RelationView();
    }

    public SelectItem getRelationSelectItemById(int relationId){
        RelationView relationView = getRelationViewById(relationId);
        return relationTransform.transformToSelectItem(relationView);
    }

    private Map<Integer, RelationView> loadData(){
        logger.debug("-- begin loadData --");
        List<Relation> relationList = relationDAO.findAll();
        Map<Integer, RelationView> _tmpMap = relationTransform.transformToCache(relationList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty SBFScoreView");
            return new ConcurrentHashMap<Integer, RelationView>();
        } else {
            cacheLoader.setCacheMap(Relation.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, RelationView> getInternalCacheMap(){
        Map<Integer, RelationView> _tmpMap = cacheLoader.getCacheMap(Relation.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class RelationComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            RelationView relationView1 = (RelationView)o1;
            RelationView relationView2 = (RelationView)o2;

            int flag = ((Integer)relationView1.getId()).compareTo(relationView2.getId());
            return flag;
        }
    }

}
