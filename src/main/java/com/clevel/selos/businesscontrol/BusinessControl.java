package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BaseRateConfig;
import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.security.UserDetail;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;

public abstract class BusinessControl implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    UserDAO userDAO;
    @Inject
    BaseRateDAO baseRateDAO;


    protected String getCurrentUserID() {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetail != null)
            return userDetail.getUserName();
        return null;
    }

    protected User getCurrentUser() {
        try {
            return userDAO.findById(getCurrentUserID());
        } catch (Exception ex) {
            log.error("User Not found", ex);
            return null;
        }
    }

    protected BigDecimal getMRRValue(){
        try{
            BaseRate baseRate = baseRateDAO.findById(BaseRateConfig.MRR.value());
            if(baseRate == null) return BigDecimal.ZERO;
            return baseRate.getValue() == null ? BigDecimal.ZERO :  baseRate.getValue();
        }catch (Exception e){
            log.error("getMRR Not found", e);
            return BigDecimal.ZERO;
        }
    }

    protected BigDecimal getMLRValue(){
        try{
            BaseRate baseRate = baseRateDAO.findById(BaseRateConfig.MLR.value());
            if(baseRate == null) return BigDecimal.ZERO;
            return baseRate.getValue() == null ? BigDecimal.ZERO :  baseRate.getValue();
        }catch (Exception e){
            log.error("getMRR Not found", e);
            return BigDecimal.ZERO;
        }
    }

    protected BigDecimal getMORValue(){
        try{
            BaseRate baseRate = baseRateDAO.findById(BaseRateConfig.MOR.value());
            if(baseRate == null) return BigDecimal.ZERO;
            return baseRate.getValue() == null ? BigDecimal.ZERO :  baseRate.getValue();
        }catch (Exception e){
            log.error("getMRR Not found", e);
            return BigDecimal.ZERO;
        }
    }

    protected BigDecimal getDBRInterest(){
        // plus 6% for MRR
        return getMRRValue().add(BigDecimal.valueOf(6));
    }

}
