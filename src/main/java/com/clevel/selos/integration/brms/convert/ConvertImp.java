package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.request.data.*;

import java.io.Serializable;
import java.util.List;

public class ConvertImp implements ConvertInf, Serializable {
    private ApplicationLevel applicationLevel;
    List<CustomerLevel> customerLevelList;
    List<BusinessLevel> businessLevelList;
    List<BankAccountLevel> bankAccountLevelList;
    private TmbAccountLevel tmbAccountLevel;
    private NcbAccountLevel ncbAccountLevel;
    private Collateralevel collateralevel;

    public ConvertImp() {
    }

    @Override
    public void convertInputModelToRequestModel(PreScreenRequest inputModel)throws Exception{
        applicationLevel = inputModel.getApplicationLevel();
        customerLevelList = inputModel.getCustomerLevelList();
        businessLevelList = inputModel.getApplicationLevel().getBusinessLevelList();
        bankAccountLevelList = inputModel.getApplicationLevel().getBankAccountLevelList();


        applicationLevel.getApplicationNumber();                //1
        applicationLevel.getProcessDate();                      //2
        applicationLevel.getAppInDate();                        //3
        applicationLevel.getExpectedSubmitDate();               //4
        applicationLevel.getCustomerEntity();                   //5

        for (CustomerLevel model : customerLevelList) {
            model.getCustomerEntity();                          //5
        }

        applicationLevel.isExistingSMECustomer();               //6

        for (CustomerLevel model : customerLevelList) {
            model.isExistingSMECustomer();                      //6
        }

        applicationLevel.isSameSetOfBorrower();                 //7
        applicationLevel.isRefinanceInFlag();                   //8
        applicationLevel.isRefinanceOutFlag();                  //9
        applicationLevel.getBorrowerGroupSale();                //19
        applicationLevel.getTotalFacility();                    //20
        applicationLevel.getNumOfTotalFacility();               //29
        applicationLevel.getNumOfContingentFacility();          //30
        applicationLevel.getBusinessLocation();                 //44
        applicationLevel.getYearInBusiness();                   //45
        applicationLevel.getCountryOfRegistration();            //46

        for (CustomerLevel model : customerLevelList) {
            model.getRelationshipType();                        //51
            model.getReference();                               //52
            model.getNationality();                             //53
            model.getNumberOfMonthFromLastSetUpDate();          //54
            model.getNewQualitative();                          //55
        }

        tmbAccountLevel.getBotClass();//56??????????

        for (CustomerLevel model : customerLevelList) {
            model.getNextReviewDate();          //57
            model.isNextReviewDateFlag();//58
            model.getExtendedReviewDate();//59
            model.isExtendedReviewDateFlag();//60
            model.getRatingFinal();//61
            model.isUnpaidFeePaid();   //62
            model.isClaimedLGFlag();//63
            model.getPersonalId();//69
            model.getAge();//70
        }

        tmbAccountLevel.isActiveFlag(); //72??????????
        tmbAccountLevel.getDataSource();//73??????????
        tmbAccountLevel.getAccountRef();//74??????????
        tmbAccountLevel.getCustToAccountRelationship();//75????????
        tmbAccountLevel.getNumberOfDayPrincipalPastDue();//76???????
        tmbAccountLevel.getNumberOfDayInterestPastDue();//77???????
        tmbAccountLevel.getCardBlockCode();//78???????

        for (CustomerLevel model : customerLevelList) {
            model.isNcbFlag();          //79
        }

        ncbAccountLevel.getNcbAccountStatus();//80???????
        ncbAccountLevel.isTmbBankFlag();//81???????
        ncbAccountLevel.isNcbNPLFlag();//82???????
        ncbAccountLevel.getCreditAmountFirstNPL();//83???????
        ncbAccountLevel.isNcbTDRFlag();//84???????
        ncbAccountLevel.getCurrentPayment();//86???????
        ncbAccountLevel.getPaymentPattern6M();//87???????
        ncbAccountLevel.getPaymentPattern12M();//88???????
        ncbAccountLevel.getOverdue31dTo60dCount();//89???????
        ncbAccountLevel.getOverLimitLast6MthsCount();//90???????

        for (CustomerLevel model : customerLevelList) {
            model.getNumSearchesLast6Mths();//91
            model.getNumberOfDaysNCBcheck();//92
            model.getWarningCodeFullyMatched();//93
            model.getWarningCodeSomeMatched();//94
        }
        //95-105

        for (BusinessLevel model : businessLevelList) {
            model.getBusinessSeqId();//106
            model.getBusinesscode();//107
            model.isNegativeFlag();//108
            model.isHighRiskFlag();//109
        }

        applicationLevel.getProductGroup();//110

        //113-114

        collateralevel.getCollateralPotential();//119
        collateralevel.getCollateralType();//120

    }

    @Override
    public void convertInputModelToRequestModel(FullApplicationRequest inputModel) throws Exception{
        applicationLevel = inputModel.getApplicationLevel();
        customerLevelList = inputModel.getCustomerLevelList();
        businessLevelList = inputModel.getApplicationLevel().getBusinessLevelList();

        applicationLevel.getApplicationNumber();                //1
        applicationLevel.getProcessDate();                      //2
        applicationLevel.getAppInDate();                        //3
        applicationLevel.getExpectedSubmitDate();               //4
        applicationLevel.getCustomerEntity();                   //5

        for (CustomerLevel model : customerLevelList) {
            model.getCustomerEntity();                          //5
        }

        applicationLevel.isExistingSMECustomer();               //6

        for (CustomerLevel model : customerLevelList) {
            model.isExistingSMECustomer();                      //6
        }

        applicationLevel.isSameSetOfBorrower();                 //7
        applicationLevel.isRefinanceInFlag();                   //8
        applicationLevel.isRefinanceOutFlag();                  //9
        applicationLevel.isSbcgFlag();                          //13
        applicationLevel.isAadFlag();                           //14
        applicationLevel.getNumberOfYearFromLatestFinancialStatement();//15
        applicationLevel.getNetWorth();                         //16
        applicationLevel.getFinalDBR();                         //17
        applicationLevel.getMonthlyIncome();                    //18
        applicationLevel.getBorrowerGroupSale();                //19
        applicationLevel.getTotalFacility();                    //20
        applicationLevel.getExistingGroupExposure();            //21
        applicationLevel.getFinalGroupExposure();               //22
        applicationLevel.getLgCapability();                     //23
        applicationLevel.getEverLgClaim();                      //24
        applicationLevel.getAbandonProject();                   //25
        applicationLevel.getProjectDelay();                     //26
        applicationLevel.getSufficientSourceOfFund();           //27
        applicationLevel.getPrimeCustomer();                    //28
        applicationLevel.getNumOfTotalFacility();               //29
        applicationLevel.getNumOfContingentFacility();          //30
        applicationLevel.getNumberOfExistiongOD();              //31
        applicationLevel.getNumberOfRequestedOD();              //32
        applicationLevel.getNumberOfCoreAsset();                //33
        applicationLevel.getNumOfNonCoreAsset();                //34
        applicationLevel.getTotalFixAssetValue();               //35
        applicationLevel.getExistingODLimit();                  //36
        applicationLevel.getTotalCollOfExposure();              //37
        applicationLevel.getTotalWCRequirement();               //38
        applicationLevel.getNetWCRequirement125x();             //39
        applicationLevel.getNetWCRequirement15x();              //40
        applicationLevel.getNetWCRequirement35();               //41
        applicationLevel.getExistingWCRCreditLimitWithTMB();    //42
        applicationLevel.getExistingCoreWCLoanCreditLimitWithTMB();//43
        applicationLevel.getBusinessLocation();                 //44
        applicationLevel.getYearInBusiness();                   //45
        applicationLevel.getCountryOfRegistration();            //46
        applicationLevel.getTradeChequeReturn();                //47
        applicationLevel.getCollateralPotertialForPricing();    //48

        for (CustomerLevel model : customerLevelList) {
            model.getRelationshipType();                        //51
            model.getReference();                               //52
            model.getNationality();                             //53
            model.getNumberOfMonthFromLastSetUpDate();          //54
            model.getNewQualitative();                          //55
        }

        tmbAccountLevel.getBotClass();                          //56??????????

        for (CustomerLevel model : customerLevelList) {
            model.getNextReviewDate();                          //57
            model.isNextReviewDateFlag();                       //58
            model.getExtendedReviewDate();                      //59
            model.isExtendedReviewDateFlag();                   //60
            model.getRatingFinal();                             //61
            model.isUnpaidFeePaid();                            //62
            model.isClaimedLGFlag();                            //63
            model.getCreditWorthiness();                        //64
            model.getKyc();                                     //65
            model.getPersonalId();                              //69
            model.getAge();                                     //70
        }

        tmbAccountLevel.isActiveFlag();                         //72??????????
        tmbAccountLevel.getDataSource();                        //73??????????
        tmbAccountLevel.getAccountRef();                        //74??????????
        tmbAccountLevel.getCustToAccountRelationship();         //75????????
        tmbAccountLevel.getNumberOfDayPrincipalPastDue();       //76???????
        tmbAccountLevel.getNumberOfDayInterestPastDue();        //77???????
        tmbAccountLevel.getCardBlockCode();                     //78???????

        for (CustomerLevel model : customerLevelList) {
            model.isNcbFlag();                                  //79
        }

        ncbAccountLevel.getNcbAccountStatus();                  //80???????
        ncbAccountLevel.isTmbBankFlag();                        //81???????
        ncbAccountLevel.isNcbNPLFlag();                         //82???????
        ncbAccountLevel.getCreditAmountFirstNPL();              //83???????
        ncbAccountLevel.isNcbTDRFlag();                         //84???????
        ncbAccountLevel.getCurrentPayment();                    //86???????
        ncbAccountLevel.getPaymentPattern6M();                  //87???????
        ncbAccountLevel.getPaymentPattern12M();                 //88???????
        ncbAccountLevel.getOverdue31dTo60dCount();              //89???????
        ncbAccountLevel.getOverLimitLast6MthsCount();           //90???????

        for (CustomerLevel model : customerLevelList) {
            model.getNumSearchesLast6Mths();                    //91
            model.getNumberOfDaysNCBcheck();                    //92
            model.getWarningCodeFullyMatched();                 //93
            model.getWarningCodeSomeMatched();                  //94
        }

        //95-105

        for (BusinessLevel model : businessLevelList) {
            model.getBusinessSeqId();                           //106
            model.getBusinesscode();                            //107
            model.isNegativeFlag();                             //108
            model.isHighRiskFlag();                             //109
        }

        applicationLevel.getProductGroup();                     //110
        applicationLevel.getMaxCreditLimitByCollateral();       //111
        applicationLevel.getTotalProposedCreditLimit();         //112

        //113-116

        applicationLevel.getGuaranteeType();                    //117

        //118

        collateralevel.getCollateralPotential();                //119
        collateralevel.getCollateralType();                     //120
        collateralevel.getAppraisalFlag();                      //121
        collateralevel.getAadComment();                         //122
        collateralevel.getAppraisalValueOfBuildin();            //123
        collateralevel.getLengthOfAppraisal();                  //124

        for (CustomerLevel model : customerLevelList) {
            model.getDayAnnualReviewOverdue();                  //#N/A
        }

        applicationLevel.getMaxWCCreditLimit();                 //#N/A
        applicationLevel.getTotalRequestedWCCreditLimit();      //#N/A
        applicationLevel.getMaxCoreWCLoanLimit();               //#N/A
        applicationLevel.getTotalRequestedCoreWCLoanCreditLimit(); //#N/A
        applicationLevel.getTotalRequestedODCreditLimit();      //#N/A

    }

    @Override
    public void convertInputModelToRequestModel(DocCustomerRequest inputModel) throws Exception{
        applicationLevel = inputModel.getApplicationLevel();
        customerLevelList = inputModel.getCustomerLevelList();

        applicationLevel.getApplicationNumber();                //1
        applicationLevel.getProcessDate();                      //2
        applicationLevel.getAppInDate();                        //3
        applicationLevel.getCustomerEntity();                   //5

        for (CustomerLevel model : customerLevelList) {
            model.getCustomerEntity();                          //5
        }

        applicationLevel.isExistingSMECustomer();               //6

        for (CustomerLevel model : customerLevelList) {
            model.isExistingSMECustomer();                      //6
        }

        applicationLevel.isSameSetOfBorrower();                 //7
        applicationLevel.isRefinanceInFlag();                   //8
        applicationLevel.isRefinanceOutFlag();                  //9
        applicationLevel.getLendingReferType();                 //10
        applicationLevel.getBAFlag();                           //11
        applicationLevel.getTopUpBAFlag();                      //12
        applicationLevel.getSBCGFlag();                         //13

        for (CustomerLevel model : customerLevelList) {
            model.getRelationshipType();                        //51
            model.getReference();                               //52
            model.getNationality();                             //53
            model.getsPouseId();                                //66
            model.getsPouseRelationshipType();                  //67
            model.getPersonalId();                              //69
            model.getMarriageStatus();                          //71
        }

        applicationLevel.getProductGroup();                     //110

        //113-114

        collateralevel.getCollateralPotential();                //119
        collateralevel.getCollateralType();                     //120
    }

    @Override
    public void convertInputModelToRequestModel(DocAppraisalRequest inputModel) throws Exception{
        applicationLevel = inputModel.getApplicationLevel();
        customerLevelList = inputModel.getCustomerLevelList();

        applicationLevel.getApplicationNumber();                //1
        applicationLevel.getProcessDate();                      //2
        applicationLevel.getAppInDate();                        //3
        applicationLevel.isSameSetOfBorrower();                 //7
        for (CustomerLevel model : customerLevelList) {
            model.getPersonalId();                              //69
        }
        applicationLevel.getProductGroup();                     //110

        //113-114

        collateralevel.getCollateralPotential();                //119
        collateralevel.getCollateralType();                     //120
    }

    @Override
    public void convertInputModelToRequestModel(StandardPricingIntRequest inputModel) throws Exception{
        applicationLevel = inputModel.getApplicationLevel();
        customerLevelList = inputModel.getCustomerLevelList();

        applicationLevel.getApplicationNumber();                //1
        applicationLevel.getProcessDate();                      //2
        applicationLevel.getAppInDate();                        //3
        applicationLevel.getCollateralPotertialForPricing();    //48

        for (CustomerLevel model : customerLevelList) {
            model.getPersonalId();                              //69
        }

        applicationLevel.getProductGroup();                     //110
        applicationLevel.getTotalProposedCreditLimit();         //112

        //113-115

        applicationLevel.getGuaranteeType();                    //117
        collateralevel.getCollateralPotential();                //119
        collateralevel.getCollateralType();                     //120
    }

    @Override
    public void convertInputModelToRequestModel(StandardPricingFeeRequest inputModel) throws Exception{
        applicationLevel = inputModel.getApplicationLevel();
        customerLevelList = inputModel.getCustomerLevelList();

        applicationLevel.getApplicationNumber();                //1
        applicationLevel.getProcessDate();                      //2
        applicationLevel.getAppInDate();                        //3
        applicationLevel.getCollateralPotertialForPricing();    //48

        for (CustomerLevel model : customerLevelList) {
            model.getPersonalId();                              //69
        }

        applicationLevel.getProductGroup();                     //110
        applicationLevel.getTotalProposedCreditLimit();         //112

        //113-115

        applicationLevel.getGuaranteeType();                    //117
        collateralevel.getCollateralPotential();                //119
        collateralevel.getCollateralType();                     //120
    }


}
