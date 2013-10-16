package com.clevel.selos.integration.rlos;

import com.clevel.selos.exception.RLOSInterfaceException;
import com.clevel.selos.integration.RLOS;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.rlos.appin.AppInProcessService;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.csi.CSIService;
import com.clevel.selos.integration.rlos.csi.model.CSIInputData;
import com.clevel.selos.integration.rlos.csi.model.CSIResult;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RLOSInterfaceImpl implements RLOSInterface,Serializable{
    @Inject
    @RLOS
    Logger log;

    @Inject
    @ExceptionMessage
    Message msg;

    @Inject
    @RLOS
    SystemAuditor rlosAuditor;

    @Inject
    CSIService csiService;
    @Inject
    AppInProcessService appInProcessService;

    @Inject
    public RLOSInterfaceImpl(){

    }

    public CSIResult getCSIData(String userId, CSIInputData csiInputData){
        CSIResult csiResult = null;
        Date requestTime = new Date();
        String linkKey = Util.getLinkKey(userId);
        String actionDesc = "User="+userId+", csiInputData="+csiInputData.toString();
        log.debug("getCSIDataByDocumentType linkKey:{}",linkKey);
        try{
            csiResult = csiService.getCSIData(userId,csiInputData);
            Date responseTime = new Date();
            log.debug("CSI Result linkKey {}, data {}",linkKey,csiResult.toString());
            rlosAuditor.add(userId, "getCSIData", actionDesc, requestTime, ActionResult.SUCCEED, "", responseTime, linkKey);
        } catch (Exception e){
            log.error("Exception while get CSI data!", e);
            rlosAuditor.add(userId, "getCSIData", actionDesc, requestTime, ActionResult.FAILED, e.getMessage(), new Date(), linkKey);
            throw new RLOSInterfaceException(e, ExceptionMapping.RLOS_CSI_EXCEPTION,msg.get(ExceptionMapping.RLOS_CSI_EXCEPTION,userId));
        }
        return csiResult;
    }

    @Override
    public List<AppInProcess> getAppInProcess(String userId, List<String> citizenIdList) {
        log.debug("getAppInProcess (userId : {}, citizenIdList : {})",userId,citizenIdList);
        List<AppInProcess> appInProcessList = Collections.EMPTY_LIST;
        if(citizenIdList!=null && citizenIdList.size()>0){
            appInProcessList = appInProcessService.getAppInProcessData(citizenIdList);
            log.debug("getAppInProcess result (appInProcessList size : {})",appInProcessList.size());
        }
        return appInProcessList;
    }
}
