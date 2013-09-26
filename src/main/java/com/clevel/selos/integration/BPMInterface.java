package com.clevel.selos.integration;

import com.clevel.selos.filenet.bpm.services.exception.SELOSBPMException;

public interface BPMInterface  {
    public void createCase(String bdmUsername,String caNumber) throws SELOSBPMException;
}
