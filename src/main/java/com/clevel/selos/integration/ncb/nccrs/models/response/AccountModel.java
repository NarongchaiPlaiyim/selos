package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class AccountModel implements Serializable {
    private AccountDisputeModel dispute;
    private CreditInfoModel creditinfo;
    private PaymentPatternModel paymentpattern;
    private CreditHistoriesModel credithistories;

    public AccountDisputeModel getDispute() {
        return dispute;
    }

    public CreditInfoModel getCreditinfo() {
        return creditinfo;
    }

    public PaymentPatternModel getPaymentpattern() {
        return paymentpattern;
    }

    public CreditHistoriesModel getCredithistories() {
        return credithistories;
    }
}
