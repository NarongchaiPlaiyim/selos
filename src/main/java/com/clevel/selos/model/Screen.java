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
    CUSTOMER_INFO_JURISTIC(2300),
    CUSTOMER_INFO_INDIVIDUAL(2400),
    NCB_SUMMARY(2500),                  //2030
    NCB_DETAIL(2600),                   //2040
    TCG_INFO(2700),                     //2050
    BUSINESS_INFO_SUMMARY(2800),        //2060
    BUSINESS_INFO_DETAIL(2900),         //2070
    QUALITATIVE(3000),                  //2080
    BANK_STATEMENT_SUMMARY(3100),       //2090
    BANK_STATEMENT_DETAIL(3200),        //2100
    DBR_INFO(3300),                     //2110
    CREDIT_FACILITY_EXISTING(3400),     //2120
    CREDIT_FACILITY_PROPOSE(3500),      //2130
    EXECUTIVE_SUMMARY(3600),            //2140

    //Decision
    DECISION(3700),                     //2150

    //Customer Accept Pre
    CUSTOMER_ACCEPTANCE_PRE(3800),      //2160
    
    //Step 301 Screen 
    InsuranceInfo(4100),                //3010
    ContactRecord(4200),                //3020
    CallingRecordDialog(4210),          //3021
    CollateralMortgageInfoSum(4300),    //3040
    FeeCalculation(9400),               //3030
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
    AppraisalRequest(8100),            //4010
    AppraisalAppointment(8200),        //4020
    AppraisalResult(8300),             //4030

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
