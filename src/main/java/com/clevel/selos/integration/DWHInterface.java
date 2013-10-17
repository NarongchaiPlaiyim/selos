package com.clevel.selos.integration;

import com.clevel.selos.integration.dwh.obligation.model.Obligation;

import java.util.List;

public interface DWHInterface {
    public List<Obligation> getObligationData(String userId, List<String> tmbCusIdList);
}
