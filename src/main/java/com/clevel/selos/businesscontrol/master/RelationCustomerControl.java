package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.relation.RelationCustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.relation.RelationCustomer;
import com.clevel.selos.model.view.master.RelationCustomerView;
import com.clevel.selos.model.view.master.RelationView;
import com.clevel.selos.transform.master.RelationCustomerTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RelationCustomerControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private RelationControl relationControl;

    @Inject
    private RelationCustomerTransform relationCustomerTransform;

    @Inject
    private RelationCustomerDAO relationCustomerDAO;

    @Inject
    public RelationCustomerControl(){}

    public List<SelectItem> getRelationSelectItem(int customerEntityId, int borrowerTypeId, int spouse){
        logger.debug("-- getRelationSelectItem, customerEntityId:{}, borrowerTypeId:{}, spouse:{}", customerEntityId, borrowerTypeId, spouse);
        Map<Integer, RelationCustomerView> _tmpRelationMap = getInternalCacheMap();
        List<RelationView> relationViewList = new ArrayList<RelationView>();

        for(RelationCustomerView relationCustomerView : _tmpRelationMap.values()){
            if(relationCustomerView.getCustomerEntityId() == customerEntityId &&
                    relationCustomerView.getBorrowerTypeCusEntityId() == borrowerTypeId &&
                    relationCustomerView.getSpouse() == spouse){
                RelationView relationView = relationControl.getRelationViewById(relationCustomerView.getRelationId());
                relationViewList.add(relationView);
            }
        }

        Collections.sort(relationViewList, new RelationComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(RelationView relationView : relationViewList){
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(relationView.getDescription());
            selectItem.setValue(relationView.getId());
            selectItemList.add(selectItem);
            logger.debug("add SelectItem: {}", selectItem);
        }
        logger.debug("getRelationSelectItem return relationView size: {}", selectItemList.size());
        return selectItemList;
    }

    public List<SelectItem> getRelationSelectItemWithOutBorrower(int customerEntityId, int borrowerTypeId, int spouse){
        logger.debug("-- getRelationSelectItemWithOutBorrower, customerEntityId: {}, borrowerTypeId:{}, spouse: {}", customerEntityId, borrowerTypeId, spouse);
        Map<Integer, RelationCustomerView> _tmpRelationMap = getInternalCacheMap();
        List<RelationView> relationViewList = new ArrayList<RelationView>();

        for(RelationCustomerView relationCustomerView : _tmpRelationMap.values()){
            if(relationCustomerView.getCustomerEntityId() == customerEntityId &&
                    relationCustomerView.getBorrowerTypeCusEntityId() == borrowerTypeId &&
                    relationCustomerView.getSpouse() == spouse){
                RelationView relationView = relationControl.getRelationViewById(relationCustomerView.getRelationId());
                if(relationView.getId() != RelationValue.BORROWER.value()) {
                    relationViewList.add(relationView);
                    logger.debug("add RelationView: {}", relationView);
                }
            }
        }

        Collections.sort(relationViewList, new RelationComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(RelationView relationView : relationViewList){
            SelectItem selectItem = new SelectItem();
            selectItem.setLabel(relationView.getDescription());
            selectItem.setValue(relationView.getId());
            selectItemList.add(selectItem);
            logger.debug("add SelectItem: {}", selectItem);
        }
        logger.debug("getRelationSelectItemWithOutBorrower return RelationView size: {}", selectItemList);
        return selectItemList;
    }

    private Map<Integer, RelationCustomerView> loadData(){
        logger.debug("-- begin loadData --");
        List<RelationCustomer> relationCustomerList = relationCustomerDAO.findAll();
        Map<Integer, RelationCustomerView> _tmpMap = relationCustomerTransform.transformToCache(relationCustomerList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty RelationCustomerView");
            return new ConcurrentHashMap<Integer, RelationCustomerView>();
        } else {
            cacheLoader.setCacheMap(RelationCustomer.class.getName(), _tmpMap);
            logger.debug("loadData return RelationCustomerView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, RelationCustomerView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, RelationCustomerView> _tmpMap = cacheLoader.getCacheMap(RelationCustomer.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("RelationCustomerView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    public class RelationComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            RelationView relationView1 = (RelationView)o1;
            RelationView relationView2 = (RelationView)o2;

            int flag = ((Integer)relationView1.getId()).compareTo(relationView2.getId());
            return flag;
        }
    }
}
