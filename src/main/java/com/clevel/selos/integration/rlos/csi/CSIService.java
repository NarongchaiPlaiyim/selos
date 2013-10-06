package com.clevel.selos.integration.rlos.csi;

import com.clevel.selos.integration.RLOS;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.rlos.csi.model.CSIData;
import com.clevel.selos.integration.rlos.csi.module.DBExecute;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSIService implements Serializable{
    @Inject
    @RLOS
    Logger log;

    @Inject
    @RLOS
    SystemAuditor rlosAuditor;

    @Inject
    DBExecute dbExecute;

    public List<CSIData> getCSIData(String userId,RLOSInterface.DocumentType documentType, String idNumber) throws Exception{
        log.debug("getCSIData service documentType: {}, idNumber: {}", documentType.toString(),idNumber);
        List<CSIData> csiDataList = new ArrayList<CSIData>();
        //actionDesc
        String actionDesc = "User="+userId+", Document Type="+documentType.toString()+", idNumber="+idNumber;
        Date requestTime = null;
        Date responseTime = null;
        String resultDesc = null;
        String linkKey = Util.getLinkKey(userId);
        try{
            log.debug("getCSIDataByDocumentType linkKey:{}",linkKey);
            requestTime = new Date();
            csiDataList = dbExecute.getCSIDataByDocumentType(documentType,idNumber);
            responseTime = new Date();
            resultDesc = "Got CSI data : "+csiDataList.size()+" record";
            //save audit
            rlosAuditor.add(userId, "getCSIData", actionDesc, requestTime, ActionResult.SUCCEED, resultDesc, responseTime, linkKey);
        }catch (Exception e){
            //save audit
            e.printStackTrace();
            log.error("Exception :{}", e.getMessage());
            rlosAuditor.add(userId, "getCSIData", actionDesc, requestTime, ActionResult.FAILED, resultDesc, responseTime, linkKey);
            throw new Exception(e.getMessage());
        }
        return csiDataList;
    }
}
