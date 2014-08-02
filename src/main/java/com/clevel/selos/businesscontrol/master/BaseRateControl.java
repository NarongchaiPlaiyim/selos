package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BaseRateConfig;
import com.clevel.selos.model.db.master.BaseRate;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;

public class BaseRateControl extends BusinessControl{

    @Inject
    public BaseRateDAO baseRateDAO;

    @Inject
    @SELOS
    Logger logger;

    public BigDecimal getBaseRateValue(BaseRateConfig _baseRate){

        try{
            BaseRate baseRate = getBaseRate(_baseRate);
            if(baseRate == null) return BigDecimal.ZERO;
            return baseRate.getValue() == null ? BigDecimal.ZERO :  baseRate.getValue();
        }catch (Exception e){
            return BigDecimal.ZERO;
        }

    }

    public BaseRate getBaseRate(BaseRateConfig _baseRate){
        try{
            BaseRate baseRate = baseRateDAO.findById(_baseRate.value());
            return baseRate;
        } catch (Exception e){
            return null;
        }
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
}
