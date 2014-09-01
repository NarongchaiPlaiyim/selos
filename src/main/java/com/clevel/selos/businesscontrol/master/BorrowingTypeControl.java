package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BorrowingTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BorrowingType;
import com.clevel.selos.model.view.master.BorrowingTypeView;
import com.clevel.selos.transform.master.BorrowingTypeTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BorrowingTypeControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private BorrowingTypeTransform borrowingTypeTransform;

    @Inject
    private BorrowingTypeDAO borrowingTypeDAO;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    public BorrowingTypeControl(){}

    public List<SelectItem> getBorrowingTypeByCustomerEntity(int customerEntityId){
        Map<Integer, BorrowingTypeView> _tmpMap = getInternalCacheMap();
        List<SelectItem> borrowingTypeList = new ArrayList<SelectItem>();
        for(BorrowingTypeView borrowingTypeView : _tmpMap.values()){
            if(Util.isTrue(borrowingTypeView.getActive()) && (customerEntityId == borrowingTypeView.getCustomerEntityID()))
                borrowingTypeList.add(borrowingTypeTransform.transformToSelectItem(borrowingTypeView));
        }
        return borrowingTypeList;
    }

    private Map<Integer, BorrowingTypeView> loadData(){
        logger.debug("-- begin loadData --");
        List<BorrowingType> borrowingTypeList = borrowingTypeDAO.findAll();
        Map<Integer, BorrowingTypeView> _tmpMap = borrowingTypeTransform.transformToCache(borrowingTypeList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty SBFScoreView");
            return new ConcurrentHashMap<Integer, BorrowingTypeView>();
        } else {
            cacheLoader.setCacheMap(BorrowingType.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, BorrowingTypeView> getInternalCacheMap(){
        Map<Integer, BorrowingTypeView> _tmpMap = cacheLoader.getCacheMap(BorrowingType.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }
}
