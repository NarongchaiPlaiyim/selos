package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.IncomeSourceDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.IncomeSource;
import com.clevel.selos.model.view.master.IncomeSourceView;
import com.clevel.selos.transform.master.IncomeSourceTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IncomeSourceControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private IncomeSourceTransform incomeSourceTransform;

    @Inject
    private IncomeSourceDAO incomeSourceDAO;

    @Inject
    public IncomeSourceControl(){}

    public List<SelectItem> getIncomeSourceSelectItemActiveList(){
        logger.debug("-- getIncomeSourceSelectItemActiveList");
        Map<Integer, IncomeSourceView> _tmpMap = getInternalCacheMap();
        List<IncomeSourceView> incomeSourceViewList = new ArrayList<IncomeSourceView>(_tmpMap.values());
        Collections.sort(incomeSourceViewList, new IncomeSourceComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(IncomeSourceView incomeSourceView : incomeSourceViewList){
            if(Util.isTrue(incomeSourceView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(incomeSourceView.getName());
                selectItem.setValue(incomeSourceView.getId());
                logger.debug("add SelectItem: {}", selectItem);
                selectItemList.add(selectItem);
            }
        }
        logger.debug("getIncomeSourceSelectItemActiveList return incomeSourceView size: {}", selectItemList.size());
        return selectItemList;
    }

    private Map<Integer, IncomeSourceView> loadData(){
        logger.debug("-- begin loadData --");
        List<IncomeSource> bizDescriptionList = incomeSourceDAO.findAll();
        Map<Integer, IncomeSourceView> _tmpMap = incomeSourceTransform.transformToCache(bizDescriptionList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty IncomeSourceView View");
            return new ConcurrentHashMap<Integer, IncomeSourceView>();
        } else {
            cacheLoader.setCacheMap(IncomeSource.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, IncomeSourceView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, IncomeSourceView> _tmpMap = cacheLoader.getCacheMap(IncomeSource.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("IncomeSourceView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class IncomeSourceComparator implements Comparator{
        @Override
        public int compare(Object o1, Object o2) {
            IncomeSourceView incomeSourceView1 = (IncomeSourceView)o1;
            IncomeSourceView incomeSourceView2 = (IncomeSourceView)o2;

            int flag = ((Integer)incomeSourceView1.getId()).compareTo(incomeSourceView2.getId());
            return flag;
        }
    }
}
