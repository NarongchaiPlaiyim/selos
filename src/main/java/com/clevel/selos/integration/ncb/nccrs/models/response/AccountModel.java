package com.clevel.selos.integration.ncb.nccrs.models.response;

public class AccountModel {
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
