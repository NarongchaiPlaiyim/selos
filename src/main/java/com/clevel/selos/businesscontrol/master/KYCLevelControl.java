package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.KYCLevelDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.KYCLevel;
import com.clevel.selos.model.view.master.KYCLevelView;
import com.clevel.selos.transform.master.KYCLevelTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class KYCLevelControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private KYCLevelTransform kycLevelTransform;

    @Inject
    private KYCLevelDAO kycLevelDAO;

    public KYCLevelView getKYCLevelViewById(int kycLevelId){
        logger.debug("-- getRelationViewById");
        Map<Integer, KYCLevelView> _tmpMap = getInternalCacheMap();
        KYCLevelView kycLevelView = _tmpMap.get(kycLevelId);
        if(kycLevelView != null){
            logger.debug("getKYCLevelViewById return kycLevelView: {}", kycLevelView);
            return kycLevelView;
        }
        logger.debug("return empty RelationView");
        return new KYCLevelView();
    }

    public List<SelectItem> getKYCLevelSelectItem(){
        logger.debug("-- getKYCLevelSelectItem");
        Map<Integer, KYCLevelView> _tmpMap = getInternalCacheMap();
        List<KYCLevelView> kycLevelViewList = new ArrayList<KYCLevelView>(_tmpMap.values());
        Collections.sort(kycLevelViewList, new KYCLevelComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(KYCLevelView kycLevelView : kycLevelViewList){
            if(Util.isTrue(kycLevelView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(kycLevelView.getName());
                selectItem.setValue(kycLevelView.getId());
                logger.debug("add SelectItem: {}", selectItem);
                selectItemList.add(selectItem);
            }
        }
        logger.debug("getKYCLevelSelectItem return kycLevelView size: {}", kycLevelViewList.size());
        return selectItemList;
    }

    private Map<Integer, KYCLevelView> loadData(){
        logger.debug("-- begin loadData --");
        List<KYCLevel> kycLevelList = kycLevelDAO.findAll();
        Map<Integer, KYCLevelView> _tmpMap = kycLevelTransform.transformToCache(kycLevelList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty KYC Level View");
            return new ConcurrentHashMap<Integer, KYCLevelView>();
        } else {
            cacheLoader.setCacheMap(KYCLevel.class.getName(), _tmpMap);
            logger.debug("loadData return KYCLevel size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, KYCLevelView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, KYCLevelView> _tmpMap = cacheLoader.getCacheMap(KYCLevel.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("KYCLevelView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class KYCLevelComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            KYCLevelView kycLevelView1 = (KYCLevelView)o1;
            KYCLevelView kycLevelView2 = (KYCLevelView)o2;

            int flag = kycLevelView1.getName().compareTo(kycLevelView2.getName());
            return flag;
        }
    }
}
