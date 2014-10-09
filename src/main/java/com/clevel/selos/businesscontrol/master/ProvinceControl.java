package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.ProvinceDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.view.master.ProvinceView;
import com.clevel.selos.transform.master.ProvinceTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProvinceControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private ProvinceDAO provinceDAO;
    @Inject
    private ProvinceTransform provinceTransform;

    @Inject
    public ProvinceControl(){}

    public ProvinceView getProvinceViewById(int code){
        logger.debug("-- getProvinceViewById, code: {}", code);
        Map<Integer, ProvinceView> _tmpMap = getInternalCacheMap();
        ProvinceView provinceView = _tmpMap.get(code);
        logger.debug("getProvinceViewById return provinceView: {}", provinceView);
        return provinceView;
    }

    public List<SelectItem> getProviceSelectItemActiveList(){
        logger.debug("-- getProviceSelectItemActiveList");
        Map<Integer, ProvinceView> _tmpMap = getInternalCacheMap();
        List<ProvinceView> provinceViewList = new ArrayList<ProvinceView>(_tmpMap.values());
        Collections.sort(provinceViewList, new ProvinceComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(ProvinceView provinceView : provinceViewList){
            if(Util.isTrue(provinceView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(provinceView.getName());
                selectItem.setValue(provinceView.getCode());
                selectItemList.add(selectItem);
            }
        }
        logger.debug("getProviceSelectItemActiveList return provinceView size: {}", selectItemList.size());
        return selectItemList;
    }

    private Map<Integer, ProvinceView> loadData(){
        logger.debug("-- begin loadData --");
        List<Province> provinceList = provinceDAO.findAll();
        Map<Integer, ProvinceView> _tmpMap = provinceTransform.transformToCache(provinceList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Province View");
            return new ConcurrentHashMap<Integer, ProvinceView>();
        } else {
            cacheLoader.setCacheMap(Province.class.getName(), _tmpMap);
            logger.debug("loadData return ProvinceView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, ProvinceView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, ProvinceView> _tmpMap = cacheLoader.getCacheMap(Province.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("ProvinceView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class ProvinceComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            ProvinceView provinceView1 = (ProvinceView)o1;
            ProvinceView provinceView2 = (ProvinceView)o2;

            int flag = provinceView1.getName().compareTo(provinceView2.getName());
            return flag;
        }
    }
}
