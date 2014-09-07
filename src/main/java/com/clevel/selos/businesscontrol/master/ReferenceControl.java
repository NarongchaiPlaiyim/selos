package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.ReferenceDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Reference;
import com.clevel.selos.model.view.master.ReferenceView;
import com.clevel.selos.transform.master.ReferenceTransform;
import com.clevel.selos.transform.master.RelationTransform;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReferenceControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private ReferenceTransform referenceTransform;

    @Inject
    private ReferenceDAO referenceDAO;

    @Inject
    public ReferenceControl(){}

    public ReferenceView getReferenceViewById(int referenceId){
        logger.debug("-- getReferenceViewById, referenceId: {}", referenceId);
        Map<Integer, ReferenceView> referenceViewMap = getInternalCacheMap();
        ReferenceView referenceView = referenceViewMap.get(referenceId);
        logger.debug("getReferenceViewById return referenceView: {}", referenceView);
        return referenceView;
    }

    public List<SelectItem> getReferenceSelectItemByFlag(int customerEntityId, int borrowerTypeId, int relationId, int mainCustomer, int spouse){
        logger.debug("-- getReferenceSelectItemByFlag, customerEntityId: {},borrowerTypeId: {}, relationId:{}, mainCustomer: {}, spouse: {}", customerEntityId, borrowerTypeId, relationId, mainCustomer, spouse);
        Map<Integer, ReferenceView> _tmpMap = getInternalCacheMap();
        List<ReferenceView> referenceViewList = new ArrayList<ReferenceView>(_tmpMap.values());
        Collections.sort(referenceViewList, new ReferenceDescComparator());

        List<SelectItem> referenceList = new ArrayList<SelectItem>();
        for(ReferenceView referenceView : referenceViewList){
            boolean isMatched = true;
            if (referenceView.getCustomerEntityId() == customerEntityId &&
                    referenceView.getBorrowerTypeCusEntityId() == borrowerTypeId &&
                    referenceView.getRelationId() == relationId){
                isMatched = true;

                if(mainCustomer != 0){
                    if(referenceView.getMainCustomer() == mainCustomer) {
                        isMatched = true;
                    } else {
                        isMatched = false;
                    }
                }

                if(spouse != 0) {
                    if(referenceView.getSpouse() == spouse){
                        isMatched = true;
                    } else {
                        isMatched = false;
                    }
                }
            } else {
                isMatched = false;
            }

            if(isMatched){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(referenceView.getDescription());
                selectItem.setValue(referenceView.getId());
                referenceList.add(selectItem);
                logger.debug("add SelectItem: {}", selectItem);
            }
        }
        logger.debug("getReferenceSelectItemByFlag return referenceView size: {}", referenceList.size());
        return referenceList;
    }

    private Map<Integer, ReferenceView> loadData(){
        logger.debug("-- begin loadData --");
        List<Reference> referenceList = referenceDAO.findAll();
        Map<Integer, ReferenceView> _tmpMap = referenceTransform.transformToCache(referenceList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty ReferenceView");
            return new ConcurrentHashMap<Integer, ReferenceView>();
        } else {
            cacheLoader.setCacheMap(Reference.class.getName(), _tmpMap);
            logger.debug("return ReferenceView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, ReferenceView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap");
        Map<Integer, ReferenceView> _tmpMap = cacheLoader.getCacheMap(Reference.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            logger.debug("ReferenceView is null or empty in Cache DB");
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class ReferenceDescComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            ReferenceView referenceView1 = (ReferenceView)o1;
            ReferenceView referenceView2 = (ReferenceView)o2;

            //int flag = ((Integer)referenceView1.getId()).compareTo(referenceView2.getId());
            int flag = referenceView1.getDescription().compareTo(referenceView2.getDescription());
            return flag;
        }
    }
}
