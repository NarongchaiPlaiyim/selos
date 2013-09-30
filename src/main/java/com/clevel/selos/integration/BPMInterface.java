package com.clevel.selos.integration;

import com.clevel.selos.filenet.bpm.services.dto.CaseDTO;
import com.clevel.selos.filenet.bpm.services.exception.SELOSBPMException;
import com.clevel.selos.model.db.history.CaseCreationHistory;

import java.util.List;

public interface BPMInterface  {
    public boolean createCase(CaseCreationHistory caseCreationHistory);
    public List<CaseDTO> getInboxList();
}
