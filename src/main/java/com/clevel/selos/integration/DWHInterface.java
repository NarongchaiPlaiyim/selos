package com.clevel.selos.integration;

import com.clevel.selos.integration.dwh.bankstatement.model.BankStatement;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;

import java.util.Date;
import java.util.List;

public interface DWHInterface {
    public List<Obligation> getObligationData(String userId, List<String> tmbCusIdList);
    public List<BankStatement> getBankStatementData(String userId, String accountNumber, Date fromDate, int numberOfMonth);
}
