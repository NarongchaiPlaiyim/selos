package com.clevel.selos.integration.ncrs.service;


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
    Logger log;

    @Inject
    NCBResultDAO ncbResult;

    @Inject
    public NCBResultImp() {
    }

    public void add(String appRefNumber, String customerType, String customerId, Date inquiryDate, ActionResult actionResult, String reason){
        ncbResult.persist(new NCBResult(appRefNumber, customerType, customerId, inquiryDate, actionResult.toString(), reason));
    }

    public List<NCBResult> getListByAppRefNumber(String appRefNumber){
        log.debug("NCRS Call getListByAppRefNumber({})", appRefNumber);
        return ncbResult.findByCriteria(Restrictions.eq("appRefNumber", appRefNumber));
    }

    public boolean isFAILED(String appRefNumber, String customerId){
        log.debug("NCRS Call isFAILED({}, {})", appRefNumber, customerId);
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        return "FAILED".equals(model.getResult())?true:false;
    }

    public boolean isEXCEPTION(String appRefNumber, String customerId){
        log.debug("NCRS Call isEXCEPTION({}, {})", appRefNumber, customerId);
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        return "EXCEPTION".equals(model.getResult())?true:false;
    }

    public boolean isChecked(String appRefNumber){
        log.debug("NCRS Call isChecked({})", appRefNumber);
        return  ncbResult.isExist(appRefNumber);
    }

    public void updateSUCCEED(String appRefNumber, String customerId, String trackingId){
        log.debug("NCRS Call updateSUCCEED({}, {})", appRefNumber, customerId);
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        model.setResult("SUCCEED");
        model.setReason(trackingId);
        ncbResult.save(model);
    }

    public void checkStatus(String appRefNumber, String customerId, String trackingId){
        log.debug("NCRS Call checkStatus({}, {})", appRefNumber, customerId);
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        if (!"SUCCEED".equals(model.getResult())){
            updateSUCCEED(appRefNumber, customerId, trackingId);
            log.debug("NCRS Call checkStatus({}, {}) updated", appRefNumber, customerId);
        }
    }
}
