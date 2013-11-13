package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class ClosedAccountsAccountModel implements Serializable {
    private ClosedAccountsAccountDisputeModel dispute;
    private ClosedAccountsAccountCreditInfoModel creditinfo;
    private ClosedAccountsAccountPaymentPatternModel paymentpattern;
    private ClosedAccountsAccountCreditHistoriesModel credithistories;

    public ClosedAccountsAccountDisputeModel getDispute() {
        return dispute;
    }

    public ClosedAccountsAccountCreditInfoModel getCreditinfo() {
        return creditinfo;
    }

    public ClosedAccountsAccountPaymentPatternModel getPaymentpattern() {
        return paymentpattern;
    }

    public ClosedAccountsAccountCreditHistoriesModel getCredithistories() {
        return credithistories;
    }
}
