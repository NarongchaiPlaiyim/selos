package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.RiskTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.RiskType;
import com.clevel.selos.model.view.master.RequestTypeView;
import com.clevel.selos.model.view.master.RiskTypeView;
import com.clevel.selos.transform.master.RiskTypeTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RiskTypeControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private RiskTypeDAO riskTypeDAO;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private RiskTypeTransform riskTypeTransform;

    public List<SelectItem> getRiskTypeActive(){
        logger.debug("-- begin getRiskTypeActive --");
        Map<Integer, RiskTypeView> _tmpMap = getInternalCacheMap();
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(RiskTypeView riskTypeView : _tmpMap.values()){
            if(Util.isTrue(riskTypeView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(riskTypeView.getDescription());
                selectItem.setValue(riskTypeView.getId());
                selectItemList.add(selectItem);
            }
        }
        logger.debug("-- return SelectItem {} --", selectItemList);
        return selectItemList;
    }

    private Map<Integer, RiskTypeView> loadData(){
        logger.debug("-- begin loadData --");
        List<RiskType> riskTypeList = riskTypeDAO.findAll();
        Map<Integer, RiskTypeView> _tmpMap = riskTypeTransform.transformToCache(riskTypeList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty RiskTypeView");
            return new ConcurrentHashMap<Integer, RiskTypeView>();
        } else {
            cacheLoader.setCacheMap(RiskType.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, RiskTypeView> getInternalCacheMap(){
        Map<Integer, RiskTypeView> _tmpMap = cacheLoader.getCacheMap(RiskType.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }
}
