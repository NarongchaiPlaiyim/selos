package com.clevel.selos.integration;

import com.clevel.selos.integration.rlos.appin.model.AppInProcessResult;
import com.clevel.selos.integration.rlos.csi.model.CSIInputData;
import com.clevel.selos.integration.rlos.csi.model.CSIResult;

import java.util.HashMap;
import java.util.List;

public interface RLOSInterface {
    enum DocumentType {CITIZEN_ID,PASSPORT,CORPORATE_ID}

    public CSIResult getCSIData(String userId, CSIInputData csiInputData); //todo: change return type to CSI View
    public AppInProcessResult getAppInProcessData(String userId, List<String> citizenIdList);
}
