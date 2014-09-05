package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.view.master.BizDescriptionView;
import com.clevel.selos.transform.master.BizDescriptionTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BizDescriptionControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;

    @Inject
    private BizDescriptionTransform bizDescriptionTransform;

    @Inject
    public BizDescriptionControl(){}

    public List<SelectItem> getBizDescSelectItemOrderByTMBCode(){
        Map<Integer, BizDescriptionView> _tmpMap = getInternalCacheMap();
        List<BizDescriptionView> bizDescriptionViewList = new ArrayList<BizDescriptionView>(_tmpMap.values());
        Collections.sort(bizDescriptionViewList, new BizDescriptionTMBCodeComparator());

        List<SelectItem> bizSelectItemList = new ArrayList<SelectItem>();
        for(BizDescriptionView bizDescriptionView : bizDescriptionViewList){
            if(Util.isTrue(bizDescriptionView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(bizDescriptionView.getTmbCode() + " - " + bizDescriptionView.getDescription());
                selectItem.setValue(bizDescriptionView.getId());
                bizSelectItemList.add(selectItem);
            }
        }
        return bizSelectItemList;
    }

    private Map<Integer, BizDescriptionView> loadData(){
        logger.debug("-- begin loadData --");
        List<BusinessDescription> bizDescriptionList = businessDescriptionDAO.findAll();
        Map<Integer, BizDescriptionView> _tmpMap = bizDescriptionTransform.transformToCache(bizDescriptionList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Title View");
            return new ConcurrentHashMap<Integer, BizDescriptionView>();
        } else {
            cacheLoader.setCacheMap(BusinessDescription.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, BizDescriptionView> getInternalCacheMap(){
        Map<Integer, BizDescriptionView> _tmpMap = cacheLoader.getCacheMap(BusinessDescription.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class BizDescriptionTMBCodeComparator implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {
            BizDescriptionView bizDescriptionView1 = (BizDescriptionView) o1;
            BizDescriptionView bizDescriptionView2 = (BizDescriptionView) o2;

            int flag = bizDescriptionView1.getTmbCode().compareTo(bizDescriptionView2.getTmbCode());

            return flag;
        }
    }
}
