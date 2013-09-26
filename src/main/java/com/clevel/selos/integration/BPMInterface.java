package com.clevel.selos.integration;

import com.clevel.selos.filenet.bpm.services.exception.SELOSBPMException;
import com.clevel.selos.model.db.history.CaseCreationHistory;

public interface BPMInterface  {
    public boolean createCase(CaseCreationHistory caseCreationHistory);
}
