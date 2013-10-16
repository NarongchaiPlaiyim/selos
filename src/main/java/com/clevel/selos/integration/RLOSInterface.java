package com.clevel.selos.integration;

import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.csi.model.CSIData;
import com.clevel.selos.integration.rlos.csi.model.CSIInputData;
import com.clevel.selos.integration.rlos.csi.model.CSIResult;
import com.clevel.selos.model.db.history.CaseCreationHistory;

import java.util.HashMap;
import java.util.List;

public interface RLOSInterface {
    enum DocumentType {CITIZEN_ID,PASSPORT,CORPORATE_ID}

    public CSIResult getCSIData(String userId, CSIInputData csiInputData); //todo: change return type to CSI View
    public List<AppInProcess> getAppInProcess(String userId, List<String> citizenIdList);
}
