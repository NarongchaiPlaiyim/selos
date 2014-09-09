package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.NationalityDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Nationality;
import com.clevel.selos.model.view.master.NationalityView;
import com.clevel.selos.transform.master.NationalityTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NationalityControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private NationalityTransform nationalityTransform;

    @Inject
    private NationalityDAO nationalityDAO;

    @Inject
    public NationalityControl(){}

    public List<SelectItem> getNationalitySelectItemActiveList(){
        logger.debug("-- getNationalitySelectItemActiveList");
        Map<Integer, NationalityView> _tmpMap = getInternalCacheMap();
        List<NationalityView> nationalityViewList = new ArrayList<NationalityView>(_tmpMap.values());
        Collections.sort(nationalityViewList, new NationalityComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(NationalityView nationalityView : nationalityViewList){
            if(Util.isTrue(nationalityView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(nationalityView.getName());
                selectItem.setValue(nationalityView.getId());
                if(!nationalityView.getName().startsWith("TH"))
                    selectItemList.add(selectItem);
                else
                    selectItemList.add(0, selectItem);
                logger.debug("add SelectItem: {}", selectItem);
            }
        }
        logger.debug("getNationalitySelectItemActiveList return nationalityView size: {}", selectItemList.size());
        return selectItemList;
    }


    private Map<Integer, NationalityView> loadData(){
        logger.debug("-- begin loadData --");
        List<Nationality> nationalityList = nationalityDAO.findAll();
        Map<Integer, NationalityView> _tmpMap = nationalityTransform.transformToCache(nationalityList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Nationality View");
            return new ConcurrentHashMap<Integer, NationalityView>();
        } else {
            cacheLoader.setCacheMap(Nationality.class.getName(), _tmpMap);
            logger.debug("loadData return NationalityView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, NationalityView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, NationalityView> _tmpMap = cacheLoader.getCacheMap(Nationality.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("NationalityView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class NationalityComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            NationalityView nationalityView1 = (NationalityView)o1;
            NationalityView nationalityView2 = (NationalityView)o2;

            int flag = nationalityView1.getName().compareTo(nationalityView2.getName());
            return flag;
        }
    }

}
