package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.DistrictDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.District;
import com.clevel.selos.model.view.master.DistrictView;
import com.clevel.selos.transform.master.DistrictTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DistrictControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private DistrictTransform districtTransform;

    @Inject
    private DistrictDAO districtDAO;

    @Inject
    public DistrictControl(){}

    public DistrictView getDistrictById(int districtId){
        logger.debug("-- getDistrictById, districtId: {}", districtId);
        Map<Integer, DistrictView> _tmpMap = getInternalCacheMap();
        DistrictView districtView = _tmpMap.get(districtId);
        logger.debug("getDistrictById return districtView: {}", districtView);
        return districtView;
    }

    public List<SelectItem> getDistrictSelectItemByProvince(int provinceCode){
        logger.debug("-- getDistrictSelectItemByProvince, provinceCode: {}", provinceCode);
        Map<Integer, DistrictView> _tmpMap = getInternalCacheMap();
        List<DistrictView> districtViewList = new ArrayList<DistrictView>(_tmpMap.values());
        Collections.sort(districtViewList, new DistrictComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(DistrictView districtView : districtViewList){
            if(districtView.getProvinceCode() == provinceCode){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(districtView.getName());
                selectItem.setValue(districtView.getId());
                logger.debug("add SelectItem: {}", selectItem);
                selectItemList.add(selectItem);
            }
        }
        logger.debug("getDistrictSelectItemByProvince return districtView size: {}", selectItemList.size());
        return selectItemList;
    }

    private Map<Integer, DistrictView> loadData(){
        logger.debug("-- begin loadData --");
        List<District> districtList = districtDAO.findAll();
        Map<Integer, DistrictView> _tmpMap = districtTransform.transformToCache(districtList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty DistrictView");
            return new ConcurrentHashMap<Integer, DistrictView>();
        } else {
            cacheLoader.setCacheMap(District.class.getName(), _tmpMap);
            logger.debug("loadData return ");
            return _tmpMap;
        }
    }

    private Map<Integer, DistrictView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap --");
        Map<Integer, DistrictView> _tmpMap = cacheLoader.getCacheMap(District.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("DistrictView is null or empty, reload from DB");
            _tmpMap = loadData();
        }
        logger.debug("getInternalCacheMap return DistrictView size: {}", _tmpMap.size());
        return _tmpMap;
    }

    private class DistrictComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            DistrictView districtView1 = (DistrictView)o1;
            DistrictView districtView2 = (DistrictView)o2;

            int flag = districtView1.getName().compareTo(districtView2.getName());
            return flag;
        }
    }
}
