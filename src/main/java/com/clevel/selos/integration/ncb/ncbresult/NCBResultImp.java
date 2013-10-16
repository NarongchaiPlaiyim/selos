package com.clevel.selos.integration.ncb.ncbresult;


import com.clevel.selos.dao.ext.ncb.NCBResultDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.ext.ncb.NCBResult;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
@NCB
public class NCBResultImp implements Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    NCBResultDAO ncbResult;

    @Inject
    public NCBResultImp() {
    }

    public void add(String appRefNumber, String customerType, String customerId, Date inquiryDate, ActionResult actionResult, String reason, String requestNo){
        log.debug("Call add (appRefNumber : {} customerType : {} customerId : {} inquiryDate : {} actionResult : {} reason : {})",
                            appRefNumber,      customerType,     customerId,     inquiryDate,     actionResult.toString(), reason, requestNo);
        ncbResult.persist(new NCBResult(appRefNumber, customerType, customerId, inquiryDate, actionResult.toString(), reason, requestNo));
    }

    public List<NCBResult> getListByAppRefNumber(String appRefNumber){
        log.debug("Call getListByAppRefNumber({})", appRefNumber);
        return ncbResult.findByCriteria(Restrictions.eq("appRefNumber", appRefNumber));
    }

    public boolean isSUCCEED(String appRefNumber, String customerId){
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        boolean result = "SUCCEED".equals(model.getResult())?true:false;
        log.debug("Call isSUCCEED({}, {}) is {}", appRefNumber, customerId, result);
        return result;
    }

    public String getRequestNo(String appRefNumber, String customerId){
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        String result = model.getRequestNo();
        log.debug("Call getRequestNo({}, {}) is {}", appRefNumber, customerId, result);
        return result;
    }

    public boolean isFAILED(String appRefNumber, String customerId){
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        boolean result = "FAILED".equals(model.getResult())?true:false;
        log.debug("Call isFAILED({}, {}) is {}", appRefNumber, customerId, result);
        return result;
    }

    public boolean isEXCEPTION(String appRefNumber, String customerId){
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        boolean result = "EXCEPTION".equals(model.getResult())?true:false;
        log.debug("Call isEXCEPTION({}, {}) is {}", appRefNumber, customerId, result);
        return result;
    }

    public boolean isChecked(String appRefNumber){
        boolean result = ncbResult.isExist(appRefNumber);
        log.debug("Call isChecked({}) is {}", appRefNumber, result);
        return result;
    }

    public void updateSUCCEED(String appRefNumber, String customerId, String trackingId){
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        model.setResult("SUCCEED");
        model.setReason(trackingId);
        ncbResult.save(model);
        log.debug("Call updateSUCCEED({}, {}) has updated", appRefNumber, customerId);
    }

    public void checkStatus(String appRefNumber, String customerId, String trackingId){
        log.debug("Call checkStatus({}, {})", appRefNumber, customerId);
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        if (!"SUCCEED".equals(model.getResult())){
            updateSUCCEED(appRefNumber, customerId, trackingId);
            log.debug("Call checkStatus({}, {}) updated", appRefNumber, customerId);
        }
    }
}