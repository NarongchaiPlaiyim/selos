package com.clevel.selos.integration.rlos;

import com.clevel.selos.integration.RLOS;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.rlos.csi.CSIService;
import com.clevel.selos.integration.rlos.csi.model.CSIData;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RLOSInterfaceImpl implements RLOSInterface,Serializable{
    @Inject
    @RLOS
    Logger log;

    @Inject
    CSIService csiService;
    public List<CSIData> getCSIData(String userId, DocumentType documentType, String idNumber){
        List<CSIData> csiDataList = new ArrayList<CSIData>();
        try{
            csiDataList = csiService.getCSIData(userId,documentType,idNumber);
        } catch (Exception e){
            //todo: return fail result
            log.error("get CSI data fail!",e);
        }
        return csiDataList;
    }
}
