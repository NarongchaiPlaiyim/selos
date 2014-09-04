package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.TitleDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Title;
import com.clevel.selos.model.view.master.TitleView;
import com.clevel.selos.transform.master.TitleTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TitleControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private TitleTransform titleTransform;

    @Inject
    private TitleDAO titleDAO;

    @Inject
    public TitleControl(){}

    public List<SelectItem> getTitleEnSelectItemByCustomerEntity(int customerEntityId){
        if(customerEntityId == 0)
            return new ArrayList<SelectItem>();
        Map<Integer, TitleView> _tmpMap = getInternalCacheMap();
        List<TitleView> titleViewList = new ArrayList<TitleView>(_tmpMap.values());
        Collections.sort(titleViewList, new TitleEnComparator());

        List<SelectItem> titleSelectItemList = new ArrayList<SelectItem>();
        for(TitleView titleView : titleViewList){
            if(titleView.getCustomerEntityId() == customerEntityId) {
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(titleView.getTitleEn());
                selectItem.setValue(titleView.getId());
                titleSelectItemList.add(selectItem);
            }
        }
        return titleSelectItemList;
    }

    public List<SelectItem> getTitleThSelectItemByCustomerEntity(int customerEntityId){
        if(customerEntityId == 0)
            return new ArrayList<SelectItem>();

        Map<Integer, TitleView> _tmpMap = getInternalCacheMap();
        List<TitleView> titleViewList = new ArrayList<TitleView>(_tmpMap.values());
        Collections.sort(titleViewList, new TitleThComparator());

        List<SelectItem> titleList = new ArrayList<SelectItem>();
        for(TitleView titleView : titleViewList){
            if(titleView.getCustomerEntityId() == customerEntityId) {
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(titleView.getTitleTh());
                selectItem.setValue(titleView.getId());
                titleList.add(selectItem);
            }
        }
        return titleList;
    }

    public TitleView getTitleById(int id){
        Map<Integer, TitleView> titleViewMap = getInternalCacheMap();
        return titleViewMap.get(id);
    }

    private Map<Integer, TitleView> loadData(){
        logger.debug("-- begin loadData --");
        List<Title> titleList = titleDAO.findAll();
        Map<Integer, TitleView> _tmpMap = titleTransform.transformToCache(titleList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Title View");
            return new ConcurrentHashMap<Integer, TitleView>();
        } else {
            cacheLoader.setCacheMap(Title.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, TitleView> getInternalCacheMap(){
        Map<Integer, TitleView> _tmpMap = cacheLoader.getCacheMap(Title.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class TitleEnComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {

            TitleView titleView1 = (TitleView) o1;
            TitleView titleView2 = (TitleView) o2;
            Long titleView1Code = Long.parseLong(titleView1.getCode());
            Long titleView2Code = Long.parseLong(titleView2.getCode());

            int flag = titleView1Code.compareTo(titleView2Code);
            if (flag == 0) {
                return titleView1.getTitleEn().compareTo(titleView2.getTitleEn());
            } else {
                return flag;
            }
        }
    }

    private class TitleThComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {

            TitleView titleView1 = (TitleView) o1;
            TitleView titleView2 = (TitleView) o2;
            Long titleView1Code = Long.parseLong(titleView1.getCode());
            Long titleView2Code = Long.parseLong(titleView2.getCode());

            int flag = titleView1Code.compareTo(titleView2Code);
            if (flag == 0) {
                return titleView1.getTitleTh().compareTo(titleView2.getTitleTh());
            } else {
                return flag;
            }
        }
    }
}
