package com.clevel.selos.integration;

import com.clevel.selos.integration.coms.model.AppraisalDataResult;

public interface COMSInterface {
    public AppraisalDataResult getAppraisalData(String userId, String jobNo);
    public void generateAgreement(String userId, long workCaseId);
}
