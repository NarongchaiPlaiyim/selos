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

    //Initial Map to cache the data of BaseRateContol
    //Initial mutex object to guaranteed thread safe
    private static Map<Integer, BaseRateView> baseRateViewMap;
    private static Object _mutexLock = new Object();

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private BaseRateDAO baseRateDAO;

    @Inject
    private BaseRateTransform baseRateTransform;

    @Inject
    public BaseRateControl(){
    }

    public BigDecimal getBaseRateValue(BaseRateConfig _baseRate){
        BaseRateView baseRateView = getBaseRate(_baseRate);
        if(baseRateView == null) return BigDecimal.ZERO;
        return baseRateView.getValue() == null ? BigDecimal.ZERO :  baseRateView.getValue();
    }

    public BaseRateView getBaseRate(BaseRateConfig _baseRate){
        BaseRateView baseRateView = baseRateViewMap.get(_baseRate.value());
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
    public void loadData(){
        if(baseRateViewMap == null){
            synchronized (_mutexLock){
                if(baseRateViewMap == null)
                    baseRateViewMap = new HashMap<Integer, BaseRateView>();
            }
        }

        try{
            Map<Integer, BaseRateView> _tmpMap = new HashMap<Integer, BaseRateView>();

            List<BaseRate> _tmpList = baseRateDAO.findAll();
            for(BaseRate baseRate : _tmpList){
                BaseRateView baseRateView = baseRateTransform.transformToView(baseRate);
                _tmpMap.put(baseRateView.getId(), baseRateView);
                System.out.println("baseRateView: " + baseRateView);
                logger.info("baseRateView {}", baseRateView);
            }

            synchronized (_mutexLock){
                baseRateViewMap.clear();
                baseRateViewMap.putAll(_tmpMap);
            }

        } catch (Exception ex){
            logger.error("Cannot Load BaseRate into Cache {}", ex);
        }
    }
}
