package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BaseRateConfig;
import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.view.BaseRateView;
import com.clevel.selos.transform.BaseRateTransform;
import org.slf4j.Logger;

import javax.ejb.Lock;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ejb.LockType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class BaseRateControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private BaseRateDAO baseRateDAO;

    @Inject
    private BaseRateTransform baseRateTransform;

    @Inject
    public BaseRateControl(){}

    public BigDecimal getBaseRateValue(BaseRateConfig _baseRate){
        logger.debug("-- getBaseRateValue --");
        BaseRateView baseRateView = getBaseRate(_baseRate);
        if(baseRateView == null || baseRateView.getValue() == null) {
            logger.debug("-- getBaseRateValue return ZERO");
            return BigDecimal.ZERO;
        }
        logger.debug("-- getBaseRateValue return baseRateView: {}", baseRateView);
        return baseRateView.getValue();
    }

    public BaseRateView getBaseRate(BaseRateConfig _baseRate){
        logger.debug("-- getBaseRate --");
        Map<Integer, BaseRateView> baseRateViewMap = (Map<Integer, BaseRateView>) cacheLoader.getCacheMap(BaseRate.class.getName());
        if(baseRateViewMap == null) {
            baseRateViewMap = loadData();
        }
        BaseRateView baseRateView = baseRateViewMap.get(_baseRate.value());
        logger.debug("getBaseRate return baseRateView: {}", baseRateView);
        return baseRateView;
    }

    public BigDecimal getDBRInterest(){
        // plus 6% MRR
        return getMRRValue().add(BigDecimal.valueOf(6));
    }

    public BigDecimal getMRRValue(){
        return getBaseRateValue(BaseRateConfig.MRR);
    }

    public BigDecimal getMLRValue(){
        return getBaseRateValue(BaseRateConfig.MLR);
    }

    public BigDecimal getMORValue(){
        return getBaseRateValue(BaseRateConfig.MOR);
    }

    /**
     * loadData is for get the data from Database and cache in the MAP.
     */
    public Map<Integer, BaseRateView> loadData(){
        logger.debug("-- loadData --");
        Map<Integer, BaseRateView> _tmpMap = new HashMap<Integer, BaseRateView>();
        try{
            List<BaseRate> _tmpList = baseRateDAO.findAll();
            for(BaseRate baseRate : _tmpList){
                BaseRateView baseRateView = baseRateTransform.transformToView(baseRate);
                _tmpMap.put(baseRateView.getId(), baseRateView);
                logger.debug("baseRateView {}", baseRateView);
            }
            cacheLoader.setCacheMap(BaseRate.class.getName(), _tmpMap);
        } catch (Exception ex){
            logger.error("Cannot Load BaseRate into Cache {}", ex);
        }
        logger.debug("loadData return baseRateView size: {}", _tmpMap.size());
        return _tmpMap;
    }
}
