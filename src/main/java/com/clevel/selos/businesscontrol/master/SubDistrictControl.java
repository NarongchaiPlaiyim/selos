package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.SubDistrictDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SubDistrict;
import com.clevel.selos.model.view.master.SubDistrictView;
import com.clevel.selos.transform.master.SubDistrictTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SubDistrictControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private SubDistrictDAO subDistrictDAO;
    @Inject
    private SubDistrictTransform subDistrictTransform;

    public List<SelectItem> getSubDistrictSelectItemByDistrict(int districtId){
        Map<Integer, SubDistrictView> _tmpMap = getInternalCacheMap();
        List<SubDistrictView> subDistrictViewList = new ArrayList<SubDistrictView>(_tmpMap.values());
        Collections.sort(subDistrictViewList, new SubDistrictComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(SubDistrictView subDistrictView : subDistrictViewList){
            if(subDistrictView.getDistrictId() == districtId) {
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(subDistrictView.getName());
                selectItem.setValue(subDistrictView.getCode());
                selectItemList.add(selectItem);
            }
        }
        return selectItemList;
    }

    private Map<Integer, SubDistrictView> loadData(){
        logger.debug("-- begin loadData --");
        List<SubDistrict> subDistrictList = subDistrictDAO.findAll();
        Map<Integer, SubDistrictView> _tmpMap = subDistrictTransform.transformToCache(subDistrictList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Province View");
            return new ConcurrentHashMap<Integer, SubDistrictView>();
        } else {
            cacheLoader.setCacheMap(SubDistrict.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, SubDistrictView> getInternalCacheMap(){
        Map<Integer, SubDistrictView> _tmpMap = cacheLoader.getCacheMap(SubDistrict.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class SubDistrictComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            SubDistrictView subDistrictView1 = (SubDistrictView)o1;
            SubDistrictView subDistrictView2 = (SubDistrictView)o2;

            int flag = subDistrictView1.getName().compareTo(subDistrictView2.getName());
            return flag;
        }
    }
}
