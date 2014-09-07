package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BusinessTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BusinessType;
import com.clevel.selos.model.view.master.BusinessTypeView;
import com.clevel.selos.transform.master.BusinessTypeTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BusinessTypeControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private BusinessTypeTransform businessTypeTransform;
    @Inject
    private BusinessTypeDAO businessTypeDAO;

    public List<SelectItem> getBusinessTypeSelectItemList(){
        logger.debug("-- getBusinessTypeSelectItemList");
        Map<Integer, BusinessTypeView> _tmpMap = getInternalCacheMap();
        List<BusinessTypeView> businessTypeViewList = new ArrayList<BusinessTypeView>(_tmpMap.values());
        Collections.sort(businessTypeViewList, new BusinessTypeComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(BusinessTypeView businessTypeView : businessTypeViewList){
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(businessTypeView.getDescription());
            selectItem.setValue(businessTypeView.getId());
            logger.debug("add selectItem: {}", selectItem);
            selectItemList.add(selectItem);
        }
        logger.debug("getBusinessTypeSelectItemList return ");
        return selectItemList;
    }

    private Map<Integer, BusinessTypeView> loadData(){
        logger.debug("-- begin loadData --");
        List<BusinessType> bizTypeList = businessTypeDAO.findAll();
        Map<Integer, BusinessTypeView> _tmpMap = businessTypeTransform.transformToCache(bizTypeList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Business Type View");
            return new ConcurrentHashMap<Integer, BusinessTypeView>();
        } else {
            cacheLoader.setCacheMap(BusinessType.class.getName(), _tmpMap);
            logger.debug("loadData return BusinessTypeView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, BusinessTypeView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap --");
        Map<Integer, BusinessTypeView> _tmpMap = cacheLoader.getCacheMap(BusinessType.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("BusinessTypeView is null or empty, reload from DB");
            _tmpMap = loadData();
        }
        logger.debug("getInternalCacheMap return BusinessTypeView size: {}", _tmpMap.size());
        return _tmpMap;
    }

    private class BusinessTypeComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            BusinessTypeView businessType1 = (BusinessTypeView) o1;
            BusinessTypeView businessType2 = (BusinessTypeView) o2;

            int flag = ((Integer)businessType1.getId()).compareTo(businessType2.getId());

            return flag;
        }
    }

}
