package com.clevel.selos.integration;

import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.integration.rlos.csi.model.CSIData;
import com.clevel.selos.model.db.history.CaseCreationHistory;

import java.util.HashMap;
import java.util.List;

public interface RLOSInterface {
    enum DocumentType {CITIZEN_ID,PASSPORT,CORPORATE_ID}

    public List<CSIData> getCSIData(String userId, DocumentType documentType, String idNumber); //todo: change return type to CSI View
}
