package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.view.CountryView;
import com.clevel.selos.transform.CountryTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CountryControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private CountryTransform countryTransform;

    @Inject
    private CountryDAO countryDAO;

    @Inject
    public CountryControl(){}

    public CountryView getCountryViewById(int id){
        logger.debug("-- getCountryViewById, id: {}", id);
        Map<Integer, CountryView> _tmpMap = getInternalCacheMap();
        CountryView countryView = _tmpMap.get(id);
        logger.debug("getCountryViewById return countryView: {}", countryView);
        return countryView;
    }

    public List<SelectItem> getCountrySelectItemActiveList(){
        logger.debug("-- getCountrySelectItemActiveList --");
        Map<Integer, CountryView> _tmpMap = getInternalCacheMap();
        List<CountryView> countryViewList = new ArrayList<CountryView>(_tmpMap.values());
        Collections.sort(countryViewList, new CountryComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(CountryView countryView : countryViewList){
            if(Util.isTrue(countryView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(countryView.getName());
                selectItem.setValue(countryView.getId());
                selectItemList.add(selectItem);
            }
        }
        logger.debug("getCountrySelectItemActiveList return countryView size: {}", selectItemList.size());
        return selectItemList;
    }

    private Map<Integer, CountryView> loadData(){
        logger.debug("-- begin loadData --");
        List<Country> bizDescriptionList = countryDAO.findAll();
        Map<Integer, CountryView> _tmpMap = countryTransform.transformToCache(bizDescriptionList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty CountryView View");
            return new ConcurrentHashMap<Integer, CountryView>();
        } else {
            cacheLoader.setCacheMap(Country.class.getName(), _tmpMap);
            logger.debug("loadData return CountryView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, CountryView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, CountryView> _tmpMap = cacheLoader.getCacheMap(Country.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("CountryView is null or empty, reload from DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class CountryComparator implements Comparator{
        @Override
        public int compare(Object o1, Object o2) {

            CountryView countryView1 = (CountryView) o1;
            CountryView countryView2 = (CountryView) o2;

            int flag = countryView1.getName().compareTo(countryView2.getName());
            return flag;
        }
    }
}
