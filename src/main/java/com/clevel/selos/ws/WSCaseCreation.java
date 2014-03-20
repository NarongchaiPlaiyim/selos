package com.clevel.selos.ws;

public interface WSCaseCreation {
    public CaseCreationResponse newCase(String jobName, String caNumber, String oldCaNumber, String accountNo1, String customerId,
                                        String customerName, String citizenId, int requestType, int customerType, String bdmId,
                                        String hubCode, String regionCode, String uwId, String appInDateBDM, String finalApproved,
                                        String parallel, String pending, String caExist, String caEnd, String accountNo2, String accountNo3,
                                        String accountNo4, String accountNo5, String accountNo6, String accountNo7, String accountNo8,
                                        String accountNo9, String accountNo10, String appInDateUW, String refAppNumber, String reason,
                                        String checkNCB, String ssoId
    );
}
