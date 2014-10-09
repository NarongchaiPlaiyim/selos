package com.clevel.selos.model;


public enum Screen {
    //PreScreen
    PRESCREEN_INITIAL(1100),            //1010
    PRESCREEN_CHECKER(1200),            //1020
    PRESCREEN_MAKER(1300),              //1030
    PRESCREEN_RESULT(1400),             //1040

    //Full Application
    BASIC_INFO(2100),                   //2010
    CUSTOMER_INFO_SUMMARY(2200),        //2020
//    CUSTOMER_INFO_INDIVIDUAL(3003),
//    CUSTOMER_INFO_JURISTIC(3004),
    NCB_SUMMARY(2500),                  //2030
    NCB_DETAIL(2510),                   //2040
    TCG_INFO(2600),                     //2050
    BUSINESS_INFO_SUMMARY(2700),        //2060
    BUSINESS_INFO_DETAIL(2710),         //2070
    QUALITATIVE(2800),                  //2080
    BANK_STATEMENT_SUMMARY(2810),       //2090
    BANK_STATEMENT_DETAIL(2820),        //2100
    DBR_INFO(2830),                     //2110
    CREDIT_FACILITY_EXISTING(2850),     //2120
    CREDIT_FACILITY_PROPOSE(2880),      //2130
    EXECUTIVE_SUMMARY(2910),            //2140

    //Decision
    DECISION(2920),                     //2150

    //Customer Accept Pre
    CUSTOMER_ACCEPTANCE_PRE(2930),      //2160
    
    //Step 301 Screen 
    InsuranceInfo(3100),                //3010
    ContactRecord(3300),                //3020
    CallingRecordDialog(3500),          //3021
    FeeCalculation(9400),               //3030
    CollateralMortgageInfoSum(3700),    //3040
    MortgageInfoDetail(4000),           //3050
    PledgeDetail(8000),                 //3060
    AddDepInfoDialog(8100),             //3061
    GuarantorDetail(8200),              //3070
    BAInfo(8300),                       //3080
    applyBAinfoDialog(8400),            //3081
    addBAPAInfoDialog(10300),           //3082
    AccountInfo(8500),                  //3090
    AddAccountInfoDialog(8600),         //3091
    PreDisbursement(9200),              //3100
    TCGInfo(9700),                      //3110
    PostCustomerInfoSum(5300),          //3120
    PostCustomerInfoIndv(5400),         //3130
    PostCustomerInfoJuris(9100),        //3140
    AgreementAndMortgageConfirm(9800),  //3150
    AgreementSign(9900),                //3160
    PledgeConfirm(10000),               //3170

    ApproveDetailInfo(8700),            //3180
    AddMCDisburseInfoDialog(8800) ,     //3181
    AddDepositDisburseInfoDialog(8900) ,//3182
    AddBahtNetDisburseInfoDialog(9000) ,//3183
    Disbursement(9300) ,                //3190
    
    UpdateCollateralInfo(9600) ,        //3200
    PerfectionReview(10100) ,           //3210
    ConfirmLimitSetUp(10200) ,          //3220

    //Appraisal
    AppraisalRequest(21000),            //4010
    AppraisalAppointment(22000),        //4020
    AppraisalResult(23000),             //4030

    //Return Screen
    ReturnInfoReply(20100) ,            //9010
    ReturnInfoReview(20200)             //9020
    
    ;
    int value;

    Screen(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
