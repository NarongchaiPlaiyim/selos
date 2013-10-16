package com.clevel.selos.integration;

import com.clevel.selos.integration.dwh.model.Obligation;
import com.clevel.selos.integration.rlos.csi.model.CSIInputData;
import com.clevel.selos.integration.rlos.csi.model.CSIResult;

import java.util.List;

public interface DWHInterface {
    public List<Obligation> getObligation(String userId, List<String> tmbCusIdList);
}
