package com.clevel.selos.model;


public enum Screen {
    //PreScreen
    PRESCREEN_INITIAL(1100),
    PRESCREEN_CHECKER(1200),
    PRESCREEN_MAKER(1300),
    PRESCREEN_RESULT(1400),

    //Full Application
    BASIC_INFO(2100),
    CUSTOMER_INFO_SUMMARY(2200),
    DBR_INFO(2830),
//    CUSTOMER_INFO_INDIVIDUAL(3003),
//    CUSTOMER_INFO_JURISTIC(3004),

    //Decision
    DECISION(2920),
    
    //Step 301 Screen //TODO
    InsuranceInfo(3100),
    ContactRecord(3300),
    CallingRecordDialog(3500),
    FeeCalculation(9400),
    CollateralMortgageInfoSum(3700),
    MortgageInfoDetail(4000),
    PledgeDetail(8000),
    AddDepInfoDialog(8100),
    GuarantorDetail(8200),
    BAInfo(8300),
    applyBAinfoDialog(8400),
    addBAPAInfoDialog(8500),
    AccountInfo(8500),
    AddAccountInfoDialog(8600),
    
    PostCustomerInfoSum(5300),
    PostCustomerInfoIndv(0),
    PostCustomerInfoJuris(9100),
    AgreementAndMortgageConfirm(9800),
    AgreementSign(9900),
    PledgeConfirm(10000),
    
    ;
    int value;

    Screen(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
