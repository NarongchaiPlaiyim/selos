package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.MaritalStatusDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MaritalStatus;
import com.clevel.selos.model.view.master.MaritalStatusView;
import com.clevel.selos.transform.master.MaritalStatusTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MaritalStatusControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private MaritalStatusDAO maritalStatusDAO;
    @Inject
    private MaritalStatusTransform maritalStatusTransform;

    @Inject
    public MaritalStatusControl(){}

    public MaritalStatusView getMaritalStatusById(int maritalStatusId){
        logger.debug("-- getMaritalStatusById, maritalStatusId: {}", maritalStatusId);
        Map<Integer, MaritalStatusView> _tmpMap = getInternalCacheMap();
        MaritalStatusView maritalStatusView = _tmpMap.get(maritalStatusId);
        logger.debug("getMaritalStatusById return maritalStatusView: {}", maritalStatusView);
        return maritalStatusView;
    }

    public List<SelectItem> getMaritalStatusSelectItemList(){
        logger.debug("-- getMaritalStatusSelectItemList");
        Map<Integer, MaritalStatusView> _tmpMap = getInternalCacheMap();
        List<MaritalStatusView> maritalStatusViewList = new ArrayList(_tmpMap.values());
        Collections.sort(maritalStatusViewList, new MaritalStatusComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(MaritalStatusView maritalStatusView : maritalStatusViewList){
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(maritalStatusView.getName());
            selectItem.setValue(maritalStatusView.getId());
            logger.debug("add SelectItem: {}", selectItem);
            selectItemList.add(selectItem);
        }
        logger.debug("getMaritalStatusSelectItemList return maritalStatusView size: {}", selectItemList.size());
        return selectItemList;
    }

    private Map<Integer, MaritalStatusView> loadData(){
        logger.debug("-- begin loadData --");
        List<MaritalStatus> bizDescriptionList = maritalStatusDAO.findAll();
        Map<Integer, MaritalStatusView> _tmpMap = maritalStatusTransform.transformToCache(bizDescriptionList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty MaritalStatusView View");
            return new ConcurrentHashMap<Integer, MaritalStatusView>();
        } else {
            cacheLoader.setCacheMap(MaritalStatus.class.getName(), _tmpMap);
            logger.debug("loadData return MaritalStatusView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, MaritalStatusView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, MaritalStatusView> _tmpMap = cacheLoader.getCacheMap(MaritalStatus.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("MaritalStatusView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class MaritalStatusComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {

            MaritalStatusView maritalStatusView1 = (MaritalStatusView)o1;
            MaritalStatusView maritalStatusView2 = (MaritalStatusView)o2;

            int flag = ((Integer)maritalStatusView1.getId()).compareTo(maritalStatusView2.getId());
            return flag;
        }
    }


}
