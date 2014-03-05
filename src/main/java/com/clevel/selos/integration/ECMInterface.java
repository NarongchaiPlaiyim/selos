package com.clevel.selos.integration;

import com.clevel.selos.integration.ecm.model.ECMDataResult;

public interface ECMInterface {
    public ECMDataResult getECMDataResult(final String caNumber);
}
