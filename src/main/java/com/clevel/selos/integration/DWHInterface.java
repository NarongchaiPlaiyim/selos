package com.clevel.selos.integration;

import com.clevel.selos.integration.dwh.bankstatement.model.DWHBankStatementResult;
import com.clevel.selos.integration.dwh.obligation.model.ObligationResult;

import java.util.Date;
import java.util.List;

public interface DWHInterface {
    public ObligationResult getObligationData(String userId, List<String> tmbCusIdList);

    public ObligationResult getObligationData(String userId, String tmbCusId);

    public DWHBankStatementResult getBankStatementData(String userId, String accountNumber, Date fromDate, int numberOfMonth);
}
