package com.clevel.selos.integration.rlos;

import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.rlos.csi.CSIService;
import com.clevel.selos.integration.rlos.csi.model.CSIData;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RLOSInterfaceImpl implements RLOSInterface,Serializable{
    @Inject
    CSIService csiService;
    public List<CSIData> getCSIData(String userId, DocumentType documentType, String idNumber){
        List<CSIData> csiDataList = new ArrayList<CSIData>();
        try{
            csiDataList = csiService.getCSIData(userId,documentType,idNumber);
        } catch (Exception e){

        }
        return csiDataList;
    }
}
