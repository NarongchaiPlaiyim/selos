package com.clevel.selos.integration;

import com.clevel.selos.integration.ecm.db.ECMCAPShare;
import com.clevel.selos.integration.ecm.model.ECMDataResult;

public interface ECMInterface {
    public ECMDataResult getECMDataResult(final String caNumber);
    public boolean update(final ECMCAPShare ecmcapShare);
    public boolean insert(final ECMCAPShare ecmcapShare);
}
