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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Map<Integer, ReferenceView> referenceViewMap = getInternalCacheMap();
        return referenceViewMap.get(referenceId);
    }

    public List<SelectItem> getReferenceSelectItemByFlag(int customerEntityId, int borrowerTypeId, int relationId, int mainCustomer, int spouse){

        Map<Integer, ReferenceView> _tmpMap = getInternalCacheMap();
        List<SelectItem> referenceList = new ArrayList<SelectItem>();
        for(ReferenceView referenceView : _tmpMap.values()){
            boolean isMatched = true;
            if(referenceView.getCustomerEntityId() != customerEntityId || referenceView.getBorrowerTypeCusEntityId() != borrowerTypeId
                && referenceView.getRelationId() != relationId){
                isMatched = false;
            }
            if(mainCustomer != 0){
                if(referenceView.getMainCustomer() != mainCustomer)
                    isMatched = false;
            }
            if(spouse != 0){
                if(referenceView.getSpouse() != spouse)
                    isMatched = false;
            }
            if(isMatched){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(referenceView.getDescription());
                selectItem.setValue(referenceView.getId());
                referenceList.add(selectItem);
            }
        }
        return referenceList;
    }

    private Map<Integer, ReferenceView> loadData(){
        logger.debug("-- begin loadData --");
        List<Reference> referenceList = referenceDAO.findAll();
        Map<Integer, ReferenceView> _tmpMap = referenceTransform.transformToCache(referenceList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty SBFScoreView");
            return new ConcurrentHashMap<Integer, ReferenceView>();
        } else {
            cacheLoader.setCacheMap(Reference.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, ReferenceView> getInternalCacheMap(){
        Map<Integer, ReferenceView> _tmpMap = cacheLoader.getCacheMap(Reference.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }
}
