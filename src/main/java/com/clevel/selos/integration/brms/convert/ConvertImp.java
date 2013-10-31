package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.request.data2.*;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ConvertImp implements ConvertInf, Serializable {

    private ApplicationTypeLevel applicationTypeLevel = null;
    private List<BorrowerTypeLevel> borrowerTypeLevelList;
    private List<AccountTypeLevelBorrower> accountTypeLevelBorrowerList = null;
    private NCBReportTypeLevel ncbReportTypeLevel = null;
    private List<AccountTypeLevelNCBReport> ncbAccountType = null;
    private List<NCBEnquiryTypeLevel> ncbEnquiryTypesTypeLevelList = null;
    private List<AccountTypeLevel> accountTypeLevelList = null;
    private List<BusinessTypeLevel> businessTypeLevelList = null;
    private List<ProductTypeLevel> productTypeLevelList = null;
    private List<SELOSProductProgramTypeLevel> selosProductProgramTypeLevelList = null;
    private List<CreditFacilityTypeLevel> creditFacilityTypeLevelList = null;
    private List<CollateralTypeLevel> collateralTypeLevelList = null;
    private List<AppraisalTypeLevel> appraisalTypeLevelList;

    @Inject
    @BRMS
    Logger log;

    @Inject
    public ConvertImp() {
    }

    @Override
    public void convertInputModelToRequestModel(PreScreenRequest inputModel) throws Exception {
        log.debug("convertInputModelToRequestModel(PreScreenRequest : {})", inputModel.toString());
        String applicationNumber = null;            //1
        Date processDate = null;                    //2
        Date appInDate = null;                      //3
        Date expectedSubmitDate = null;             //4
        String customerEntity = null;               //5
        boolean existingSMECustomer;                //6

        try {
            applicationTypeLevel = inputModel.getApplicationType();

            applicationNumber = applicationTypeLevel.getApplicationNumber();                            //1
            log.debug("Application Number is {}", applicationNumber);
            processDate = applicationTypeLevel.getProcessDate();                                        //2
            log.debug("Process Date is {}", processDate);
            appInDate = applicationTypeLevel.getAttribute().getAppInDate();                             //3
            log.debug("App in Date is {}", appInDate);

            expectedSubmitDate = applicationTypeLevel.getAttribute().getExpectedSubmitDate();           //4
            log.debug("Expected Submit Date is {}", expectedSubmitDate);
            customerEntity = applicationTypeLevel.getAttribute().getCustomerEntity();                   //5
            log.debug("Customer Entity is {}", customerEntity);
            existingSMECustomer = applicationTypeLevel.getAttribute().isExistingSMECustomer();          //6
            log.debug("Existing SME Customer is {}", existingSMECustomer);

            borrowerTypeLevelList = applicationTypeLevel.getBorrowerType();
            for (BorrowerTypeLevel borrowerTypeLevel : borrowerTypeLevelList) {
                borrowerTypeLevel.getCustomerEntity();                                                  //5
                borrowerTypeLevel.isExistingSMECustomer();                                              //6
                borrowerTypeLevel.getRelationshipType();//51
                borrowerTypeLevel.getReference();           //52
                borrowerTypeLevel.getNationality();//53
                borrowerTypeLevel.getNumberOfMonthFromLastSetUpDate();//54
                borrowerTypeLevel.getNewQualitative();//55
                accountTypeLevelBorrowerList = borrowerTypeLevel.getAccountType();
                for (AccountTypeLevelBorrower accountTypeLevelBorrower : accountTypeLevelBorrowerList) {
                    accountTypeLevelBorrower.getAttributeType().getBotClass(); //56
                    accountTypeLevelBorrower.getAttributeType().isActiveFlag();//72
                    accountTypeLevelBorrower.getDataSource();        //73
                    accountTypeLevelBorrower.getAttributeType().getAccountRef();//74
                    accountTypeLevelBorrower.getAttributeType().getCustToAccountRelationship();//75
                    accountTypeLevelBorrower.getAttributeType().getNumberOfDayPrincipalPastDue();//76
                    accountTypeLevelBorrower.getAttributeType().getNumberOfDayInterestPastDue();//77
                    accountTypeLevelBorrower.getAttributeType().getCardBlockCode();//78
                }
                borrowerTypeLevel.getNextReviewDate();        //57
                borrowerTypeLevel.isNextReviewDateFlag(); //58
                borrowerTypeLevel.getExtendedReviewDate();//59
                borrowerTypeLevel.isExtendedReviewDateFlag();//60
                borrowerTypeLevel.getRatingFinal();//61
                borrowerTypeLevel.isUnpaidFeePaid();//62
                borrowerTypeLevel.isClaimedLGFlag();//63
                borrowerTypeLevel.getIndividualType().getPersonalId();//69
                borrowerTypeLevel.getIndividualType().getAge();       //70
                borrowerTypeLevel.getDayAnnualReviewOverdue();//#N/A

                ncbReportTypeLevel = borrowerTypeLevel.getNcbReportType();
                ncbAccountType = ncbReportTypeLevel.getNcbAccountType();
                for (AccountTypeLevelNCBReport accountTypeLevelNCBReport : ncbAccountType) {
                    accountTypeLevelNCBReport.getAttributeType().isNcbNPLFlag(); //79
                    accountTypeLevelNCBReport.getNcbAccountStatus();//80
                    accountTypeLevelNCBReport.getAttributeType().isTmbBankFlag();//81
                    accountTypeLevelNCBReport.getAttributeType().isNcbNPLFlag();//82
                    accountTypeLevelNCBReport.getCreditAmountFirstNPL();//83
                    accountTypeLevelNCBReport.isNcbTDRFlag(); //84
                    accountTypeLevelNCBReport.getCurrentPayment(); //86
                    accountTypeLevelNCBReport.getPaymentPattern6M();   //87
                    accountTypeLevelNCBReport.getPaymentPattern12M();     //88
                    accountTypeLevelNCBReport.getOverDue31to60DaysWithinLast12Months();//89

                }
                ncbEnquiryTypesTypeLevelList = ncbReportTypeLevel.getNcbEnquiryTypes();
                for (NCBEnquiryTypeLevel ncbEnquiryTypeLevel : ncbEnquiryTypesTypeLevelList) {
                    ncbEnquiryTypeLevel.getNumberOfSearchInLast6Months();//91
                    ncbEnquiryTypeLevel.getAttributeType().getNumberOfDaysNCBCheck();//92
                }
                borrowerTypeLevel.getWarningCodeFullMatched().getRiskCodeWithFullyIdentifyMatched();  //93
                borrowerTypeLevel.getWarningCodePartialMatched().getRiskCodeWithSomeIdentifyMatched(); //94

            }

            applicationTypeLevel.isSameSetOfBorrower();                                                 //7
            applicationTypeLevel.isRefinanceInFlag();                                                   //8
            applicationTypeLevel.isRefinanceOutFlag();                                                  //9
            applicationTypeLevel.getAttribute().getBorrowerGroupSale();                                 //19
            applicationTypeLevel.getAttribute().getTotalGroupSale();                                    //20
            applicationTypeLevel.getAttribute().getNumOfTotalFacility();     //29
            applicationTypeLevel.getAttribute().getNumOfContingentFacility(); //30
            applicationTypeLevel.getAttribute().getBusinessLocation();//44
            applicationTypeLevel.getAttribute().getYearInBusiness();//45
            applicationTypeLevel.getAttribute().getCountryOfRegistration();//46

            accountTypeLevelList = applicationTypeLevel.getAccountType();
            for (AccountTypeLevel accountTypeLevel : accountTypeLevelList) {
                accountTypeLevel.getUtilizationPercent();   //95
                accountTypeLevel.getSwingPercent();             //96
                accountTypeLevel.getAvgL6MInFlowLimit();            //97
                accountTypeLevel.getNoOfTransaction();                  //98
                accountTypeLevel.isExcludeIncomeFlag();                     //105
            }

            businessTypeLevelList = applicationTypeLevel.getBusinessType();
            for (BusinessTypeLevel businessTypeLevel : businessTypeLevelList) {
                businessTypeLevel.getBusinessCodeRunningNumber(); //106
                businessTypeLevel.getBusinessCode();                   //107
                businessTypeLevel.getAttributeType().isNegativeFlag();      //108
                businessTypeLevel.getAttributeType().isHighRiskFlag(); //109
            }

            productTypeLevelList = applicationTypeLevel.getProductType();
            for(ProductTypeLevel productTypeLevel : productTypeLevelList) {
                productTypeLevel.getProductGroup();//110
                selosProductProgramTypeLevelList = productTypeLevel.getSelosProductProgramTypes();
                for (SELOSProductProgramTypeLevel selosProductProgramTypeLevel : selosProductProgramTypeLevelList) {
                    selosProductProgramTypeLevel.getProductProgram();//113
                    creditFacilityTypeLevelList = selosProductProgramTypeLevel.getCreditFacilityType();
                    for (CreditFacilityTypeLevel creditFacilityTypeLevel : creditFacilityTypeLevelList) {
                        creditFacilityTypeLevel.getCreditFacilityType();//114
                    }
                }
                collateralTypeLevelList = productTypeLevel.getCollateralTypes();
                for (CollateralTypeLevel collateralTypeLevel : collateralTypeLevelList) {
                    collateralTypeLevel.getAttributeType().getCollateralPotential();///119
                    collateralTypeLevel.getCollateralType();//120
                }
            }


        } catch (Exception e) {

        }
    }

    @Override
    public void convertInputModelToRequestModel(FullApplicationRequest inputModel) throws Exception {
        log.debug("convertInputModelToRequestModel(FullApplication : {})", inputModel.toString());
        //todo
        String applicationNumber = null;            //1
        Date processDate = null;                    //2
        Date appInDate = null;                      //3
        Date expectedSubmitDate = null;             //4
        String customerEntity = null;               //5
        boolean existingSMECustomer;                //6

        try {
            applicationTypeLevel = inputModel.getApplicationType();

            applicationNumber = applicationTypeLevel.getApplicationNumber();                            //1
            log.debug("Application Number is {}", applicationNumber);
            processDate = applicationTypeLevel.getProcessDate();                                        //2
            log.debug("Process Date is {}", processDate);
            appInDate = applicationTypeLevel.getAttribute().getAppInDate();                             //3
            log.debug("App in Date is {}", appInDate);

            expectedSubmitDate = applicationTypeLevel.getAttribute().getExpectedSubmitDate();           //4
            log.debug("Expected Submit Date is {}", expectedSubmitDate);
            customerEntity = applicationTypeLevel.getAttribute().getCustomerEntity();                   //5
            log.debug("Customer Entity is {}", customerEntity);
            existingSMECustomer = applicationTypeLevel.getAttribute().isExistingSMECustomer();          //6
            log.debug("Existing SME Customer is {}", existingSMECustomer);

            applicationTypeLevel.isSameSetOfBorrower();                                                 //7
            applicationTypeLevel.isRefinanceInFlag();                                                   //8
            applicationTypeLevel.isRefinanceOutFlag();                                                  //9
            applicationTypeLevel.getAttribute().isSBCGFlag();                                           //13
            applicationTypeLevel.getAttribute().isAadFlag();                                            //14
            applicationTypeLevel.getAttribute().getNumberOfYearFromLatestFinancialStatement();          //15
            applicationTypeLevel.getAttribute().getNetWorth();                                          //16
            applicationTypeLevel.getAttribute().getFinalDBR();                                          //17
            applicationTypeLevel.getAttribute().getMonthlyIncome();                                     //18
            applicationTypeLevel.getAttribute().getBorrowerGroupSale();                                 //19
            applicationTypeLevel.getAttribute().getTotalGroupSale();                                    //20
            applicationTypeLevel.getAttribute().getExistingGroupExposure();                             //21
            applicationTypeLevel.getFinalGroupExposure();                                               //22
            applicationTypeLevel.getAttribute().isLgCapability();                                       //23
            applicationTypeLevel.getAttribute().isEverLgClaim();                                        //24
            applicationTypeLevel.getAttribute().isAbandonProject();                                     //25
            applicationTypeLevel.getAttribute().isProjectDelay();                                       //26
            applicationTypeLevel.getAttribute().isSufficientSourceOfFund();                             //27
            applicationTypeLevel.getAttribute().getPrimeCustomer();                                     //28
            applicationTypeLevel.getAttribute().getNumOfTotalFacility();                                //29
            applicationTypeLevel.getAttribute().getNumOfContingentFacility();                           //30
            applicationTypeLevel.getAttribute().getNumberOfExistiongOD();                               //31
            applicationTypeLevel.getAttribute().getNumberOfRequestedOD();                               //32
            applicationTypeLevel.getAttribute().getNumberOfCoreAsset();                                 //33
            applicationTypeLevel.getAttribute().getNumOfNonCoreAsset();                                 //34
            applicationTypeLevel.getAttribute().getTotalFixAssetValue();                                //35
            applicationTypeLevel.getAttribute().getExistingODLimit();                                   //36
            applicationTypeLevel.getAttribute().getTotalCollOfExposure();                               //37
            applicationTypeLevel.getAttribute().getTotalWCRequirement();                                //38
            applicationTypeLevel.getAttribute().getNetWCRequirement125x();                              //39
            applicationTypeLevel.getAttribute().getNetWCRequirement15x();                               //40
            applicationTypeLevel.getAttribute().getNetWCRequirement35();                                //41
            applicationTypeLevel.getAttribute().getExistingWCRCreditLimitWithTMB();                     //42
            applicationTypeLevel.getAttribute().getExistingCoreWCLoanCreditLimitWithTMB();              //43
            applicationTypeLevel.getAttribute().getBusinessLocation();                                  //44
            applicationTypeLevel.getAttribute().getYearInBusiness();                                    //45
            applicationTypeLevel.getAttribute().getCountryOfRegistration();                             //46
            applicationTypeLevel.getAttribute().getTradeChequeReturn();                                 //47
            applicationTypeLevel.getCollateralPotertialForPricing();                                    //48//Enum = CollateralPotentialEnum

            borrowerTypeLevelList = applicationTypeLevel.getBorrowerType();
            for (BorrowerTypeLevel borrowerTypeLevel : borrowerTypeLevelList) {
                borrowerTypeLevel.getCustomerEntity();                                                  //5
                borrowerTypeLevel.isExistingSMECustomer();                                              //6
                borrowerTypeLevel.getRelationshipType();                                                //51
                borrowerTypeLevel.getReference();                                                       //52
                borrowerTypeLevel.getNationality();                                                     //53
                borrowerTypeLevel.getNumberOfMonthFromLastSetUpDate();                                  //54
                borrowerTypeLevel.getNewQualitative();                                                  //55
                accountTypeLevelBorrowerList = borrowerTypeLevel.getAccountType();
                for (AccountTypeLevelBorrower accountTypeLevelBorrower : accountTypeLevelBorrowerList) {
                    accountTypeLevelBorrower.getAttributeType().getBotClass();                          //56
                    accountTypeLevelBorrower.getAttributeType().isActiveFlag();                         //72
                    accountTypeLevelBorrower.getDataSource();                                           //73
                    accountTypeLevelBorrower.getAttributeType().getAccountRef();                        //74
                    accountTypeLevelBorrower.getAttributeType().getCustToAccountRelationship();         //75
                    accountTypeLevelBorrower.getAttributeType().getNumberOfDayPrincipalPastDue();       //76
                    accountTypeLevelBorrower.getAttributeType().getNumberOfDayInterestPastDue();        //77
                    accountTypeLevelBorrower.getAttributeType().getCardBlockCode();                     //78
                }
                borrowerTypeLevel.getNextReviewDate();                                                  //57
                borrowerTypeLevel.isNextReviewDateFlag();                                               //58
                borrowerTypeLevel.getExtendedReviewDate();                                              //59
                borrowerTypeLevel.isExtendedReviewDateFlag();                                           //60
                borrowerTypeLevel.getRatingFinal();                                                     //61
                borrowerTypeLevel.isUnpaidFeePaid();                                                    //62
                borrowerTypeLevel.isClaimedLGFlag();                                                    //63
                borrowerTypeLevel.getCreditWorthiness();                                                //64
                borrowerTypeLevel.getKyc();                                                             //65
                borrowerTypeLevel.getIndividualType().getPersonalId();                                  //69
                borrowerTypeLevel.getIndividualType().getAge();                                         //70
                borrowerTypeLevel.getDayAnnualReviewOverdue();                                          //#N/A

                ncbReportTypeLevel = borrowerTypeLevel.getNcbReportType();
                ncbAccountType = ncbReportTypeLevel.getNcbAccountType();
                for (AccountTypeLevelNCBReport accountTypeLevelNCBReport : ncbAccountType) {
                    accountTypeLevelNCBReport.getAttributeType().isNcbNPLFlag();                        //79
                    accountTypeLevelNCBReport.getNcbAccountStatus();                                    //80
                    accountTypeLevelNCBReport.getAttributeType().isTmbBankFlag();                       //81
                    accountTypeLevelNCBReport.getAttributeType().isNcbNPLFlag();                        //82
                    accountTypeLevelNCBReport.getCreditAmountFirstNPL();                                //83
                    accountTypeLevelNCBReport.isNcbTDRFlag();                                           //84
                    accountTypeLevelNCBReport.getCurrentPayment();                                      //86
                    accountTypeLevelNCBReport.getPaymentPattern6M();                                    //87
                    accountTypeLevelNCBReport.getPaymentPattern12M();                                   //88
                    accountTypeLevelNCBReport.getOverDue31to60DaysWithinLast12Months();                 //89
                    accountTypeLevelNCBReport.getOverLimitWithinLast6Months();                          //90
                }
                ncbEnquiryTypesTypeLevelList = ncbReportTypeLevel.getNcbEnquiryTypes();
                for (NCBEnquiryTypeLevel ncbEnquiryTypeLevel : ncbEnquiryTypesTypeLevelList) {
                    ncbEnquiryTypeLevel.getNumberOfSearchInLast6Months();                               //91
                    ncbEnquiryTypeLevel.getAttributeType().getNumberOfDaysNCBCheck();                   //92
                }
                borrowerTypeLevel.getWarningCodeFullMatched().getRiskCodeWithFullyIdentifyMatched();    //93
                borrowerTypeLevel.getWarningCodePartialMatched().getRiskCodeWithSomeIdentifyMatched();  //94
                borrowerTypeLevel.getDayAnnualReviewOverdue();//#N/A

            }

            accountTypeLevelList = applicationTypeLevel.getAccountType();
            for (AccountTypeLevel accountTypeLevel : accountTypeLevelList) {
                accountTypeLevel.getUtilizationPercent();                                               //95
                accountTypeLevel.getSwingPercent();                                                     //96
                accountTypeLevel.getAvgL6MInFlowLimit();                                                //97
                accountTypeLevel.getNoOfTransaction();                                                  //98
                accountTypeLevel.getChequeReturnTime();                                                 //99
                accountTypeLevel.getOdOverLimitDay();                                                   //100
                accountTypeLevel.getCashInflow();                                                       //101
                accountTypeLevel.isMainAccountFlag();                                                   //102
                accountTypeLevel.isHighestInflowFlag();                                                 //103
                accountTypeLevel.isTmbAccountFlag();                                                    //104
                accountTypeLevel.isExcludeIncomeFlag();                                                 //105
            }

            businessTypeLevelList = applicationTypeLevel.getBusinessType();
            for (BusinessTypeLevel businessTypeLevel : businessTypeLevelList) {
                businessTypeLevel.getBusinessCodeRunningNumber();                                       //106
                businessTypeLevel.getBusinessCode();                                                    //107
                businessTypeLevel.getAttributeType().isNegativeFlag();                                  //108
                businessTypeLevel.getAttributeType().isHighRiskFlag();                                  //109
            }

            productTypeLevelList = applicationTypeLevel.getProductType();
            for(ProductTypeLevel productTypeLevel : productTypeLevelList) {
                productTypeLevel.getProductGroup();                                                     //110
                productTypeLevel.getAttributeType().getMaxCreditLimitByCollateral();                    //111
                productTypeLevel.getTotalProposedCreditLimit();                                         //112
                selosProductProgramTypeLevelList = productTypeLevel.getSelosProductProgramTypes();
                for (SELOSProductProgramTypeLevel selosProductProgramTypeLevel : selosProductProgramTypeLevelList) {
                    selosProductProgramTypeLevel.getProductProgram();                                   //113
                    creditFacilityTypeLevelList = selosProductProgramTypeLevel.getCreditFacilityType();
                    for (CreditFacilityTypeLevel creditFacilityTypeLevel : creditFacilityTypeLevelList) {
                        creditFacilityTypeLevel.getCreditFacilityType();                                //114
                        creditFacilityTypeLevel.getCreditLimit();                                       //115
                        creditFacilityTypeLevel.getTenor();                                             //116
                        creditFacilityTypeLevel.getGuaranteeType();                                     //117
                        creditFacilityTypeLevel.getLoanPurpose();                                       //118
                    }
                }
                collateralTypeLevelList = productTypeLevel.getCollateralTypes();
                for (CollateralTypeLevel collateralTypeLevel : collateralTypeLevelList) {
                    collateralTypeLevel.getAttributeType().getCollateralPotential();                    //119
                    collateralTypeLevel.getCollateralType();                                            //120
                    appraisalTypeLevelList = collateralTypeLevel.getAppraisalType();
                    for (AppraisalTypeLevel appraisalTypeLevel : appraisalTypeLevelList) {
                        appraisalTypeLevel.getAttributeType().getAppraisalFlag();                       //121
                        appraisalTypeLevel.getAttributeType().getAadComment();                          //122
                        appraisalTypeLevel.getAttributeType().getAppraisalValueOfBuildingWithOwnershipDoc(); //123
                        appraisalTypeLevel.getAttributeType().getLengthOfAppraisal();                   //124

                    }

                }
            }

            applicationTypeLevel.getAttribute().getMaxWCCreditLimit();                                  //#N/A
            applicationTypeLevel.getAttribute().getTotalRequestedWCCreditLimit();                       //#N/A
            applicationTypeLevel.getAttribute().getMaxCoreWCLoanLimit();                                //#N/A
            applicationTypeLevel.getAttribute().getTotalRequestedCoreWCLoanCreditLimit();               //#N/A
            applicationTypeLevel.getAttribute().getTotalRequestedODCreditLimit();                       //#N/A

        } catch (Exception e) {

        }
    }

    @Override
    public com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceRequest convertInputModelToRequestModel(DocCustomerRequest inputModel) throws Exception {
        log.debug("convertInputModelToRequestModel(DocCustomerRequest : {})", inputModel.toString());
        com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceRequest request = null;
        com.clevel.selos.integration.brms.service.document.customerrules.ApplicationType applicationType = null;
        GregorianCalendar gregory = null;
        XMLGregorianCalendar calendar = null;
        List<com.clevel.selos.integration.brms.service.document.customerrules.AttributeType> attributeTypeList = null;
        com.clevel.selos.integration.brms.service.document.customerrules.AttributeType attributeType = null;
        List<com.clevel.selos.integration.brms.service.document.customerrules.BorrowerType> borrowerTypeList = null;
        com.clevel.selos.integration.brms.service.document.customerrules.BorrowerType borrowerType = null;
        List<com.clevel.selos.integration.brms.service.document.customerrules.ProductType> productTypeList = null;
        com.clevel.selos.integration.brms.service.document.customerrules.ProductType productType = null;
        List<com.clevel.selos.integration.brms.service.document.customerrules.SELOSProductProgramType> selosProductProgramTypeList = null;
        com.clevel.selos.integration.brms.service.document.customerrules.SELOSProductProgramType selosProductProgramType = null;
        List<com.clevel.selos.integration.brms.service.document.customerrules.CreditFacilityType> creditFacilityTypeList = null;
        com.clevel.selos.integration.brms.service.document.customerrules.CreditFacilityType creditFacilityType = null;
        List<com.clevel.selos.integration.brms.service.document.customerrules.CollateralType> collateralTypeList = null;
        com.clevel.selos.integration.brms.service.document.customerrules.CollateralType collateralType = null;
        try {
            request = new com.clevel.selos.integration.brms.service.document.customerrules.DecisionServiceRequest();
            applicationType = request.getUnderwritingRequest().getUnderwritingApprovalRequest().getApplication();
            applicationTypeLevel = inputModel.getApplicationType();

            applicationType.setApplicationNumber(applicationTypeLevel.getApplicationNumber());  //1

            try {
                gregory = new GregorianCalendar();
                gregory.setTime(applicationTypeLevel.getProcessDate());                         //2
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            } catch (Exception e) {
                gregory = new GregorianCalendar();
                gregory.setTime(new Date());
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            }

            attributeTypeList = applicationType.getAttribute();
            attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();

            try {
                gregory.setTime(applicationTypeLevel.getAttribute().getAppInDate());            //3
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            } catch (Exception e) {
                gregory = new GregorianCalendar();
                gregory.setTime(new Date());
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            }

            attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
            attributeType.setName("Customer Entity");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().getCustomerEntity());              //5//Enum = CustomerEntityEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
            attributeType.setName("Existing SME Customer Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isExistingSMECustomer()+"");       //6//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
            attributeType.setName("Set Of Borrower Flag");
            attributeType.setStringValue(applicationTypeLevel.isSameSetOfBorrower()+"");                        //7//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
            attributeType.setName("Refinance In Flag");
            attributeType.setStringValue(applicationTypeLevel.isRefinanceInFlag()+"");                          //8//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
            attributeType.setName("Refinance Out Flag");
            attributeType.setStringValue(applicationTypeLevel.isRefinanceOutFlag()+"");                         //9//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
            attributeType.setName("Lending Refer Type");
            attributeType.setStringValue(applicationTypeLevel.getLendingReferType());                           //10//Enum = LendingReferTypeEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
            attributeType.setName("BA Flag");
            attributeType.setStringValue(applicationTypeLevel.isBAFlag()+"");                                   //11//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
            attributeType.setName("Top Up BA Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isTopUpBAFlag()+"");               //12//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
            attributeType.setName("SBCG Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isSBCGFlag()+"");                  //13//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            borrowerTypeList = applicationType.getBorrower();
            borrowerTypeLevelList = applicationTypeLevel.getBorrowerType();
            for (BorrowerTypeLevel borrowerTypeLevel : borrowerTypeLevelList) {

                borrowerType = new com.clevel.selos.integration.brms.service.document.customerrules.BorrowerType();
                attributeTypeList = borrowerType.getAttribute();

                attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
                attributeType.setName("Customer Entity");
                attributeType.setStringValue(borrowerTypeLevel.getCustomerEntity()+"");                         //5//Enum = CustomerEntityEnum  todo to check result
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
                attributeType.setName("Existing SME Customer Flag");
                attributeType.setStringValue(borrowerTypeLevel.isExistingSMECustomer()+"");                     //6//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
                attributeType.setName("Relationship Type");
                attributeType.setStringValue(borrowerTypeLevel.getRelationshipType()+"");                       //51//Enum = RelationshipTypeEnum todo to check result
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
                attributeType.setName("Reference");
                attributeType.setStringValue(borrowerTypeLevel.getReference()+"");                              //52//Enum = ReferenceEnum todo to check result
                attributeTypeList.add(attributeType);

                borrowerType.setNationality(borrowerTypeLevel.getNationality()+"");                             //53//Enum = NationalityEnum todo to check result

                attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
                attributeType.setName("Spouse ID");
                attributeType.setStringValue(borrowerTypeLevel.getsPouseId());                                  //66
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
                attributeType.setName("Spouse Borrower Type");
                attributeType.setStringValue(borrowerTypeLevel.getsPouseRelationshipType());                    //67//Enum = BorrowerTypeEnum todo to check result
                attributeTypeList.add(attributeType);

                borrowerType.getIndividual().setCitizenID(borrowerTypeLevel.getIndividualType().getPersonalId());//69

                borrowerType.getIndividual().setMaritalStatus(borrowerTypeLevel.getIndividualType().getMarriageStatus());//71 Enum = MaritalStatusEnum

                borrowerTypeList.add(borrowerType);
            }


            productTypeList = applicationType.getProduct();
            productTypeLevelList = applicationTypeLevel.getProductType();
            for(ProductTypeLevel productTypeLevel : productTypeLevelList) {
                productType = new com.clevel.selos.integration.brms.service.document.customerrules.ProductType();
                productType.setProductType(productTypeLevel.getProductGroup());                                     //110//Enum = SELOSProductGroupEnum

                selosProductProgramTypeList = productType.getSelosProductProgram();
                selosProductProgramTypeLevelList = productTypeLevel.getSelosProductProgramTypes();
                for (SELOSProductProgramTypeLevel selosProductProgramTypeLevel : selosProductProgramTypeLevelList) {
                    selosProductProgramType = new com.clevel.selos.integration.brms.service.document.customerrules.SELOSProductProgramType();
                    selosProductProgramType.setName(selosProductProgramTypeLevel.getProductProgram());              //113//Enum = SELOSProductProgramEnum
                    creditFacilityTypeList = selosProductProgramType.getCreditFacility();
                    creditFacilityTypeLevelList = selosProductProgramTypeLevel.getCreditFacilityType();
                    for (CreditFacilityTypeLevel creditFacilityTypeLevel : creditFacilityTypeLevelList) {
                        creditFacilityType = new com.clevel.selos.integration.brms.service.document.customerrules.CreditFacilityType();
                        creditFacilityType.setType(creditFacilityTypeLevel.getCreditFacilityType());                //114//Enum = CreditFacilityTypeEnum
                        creditFacilityTypeList.add(creditFacilityType);
                    }
                    selosProductProgramTypeList.add(selosProductProgramType);
                }

                collateralTypeList = productType.getCollateral();
                collateralTypeLevelList = productTypeLevel.getCollateralTypes();
                for (CollateralTypeLevel collateralTypeLevel : collateralTypeLevelList) {
                    collateralType = new com.clevel.selos.integration.brms.service.document.customerrules.CollateralType();
                    attributeType.setStringValue(collateralTypeLevel.getAttributeType().getCollateralPotential());  //119//Enum = CollateralPotentialEnum
                    attributeType = new com.clevel.selos.integration.brms.service.document.customerrules.AttributeType();
                    attributeType.setName("Collateral Potential");
                    collateralType.getAttribute().add(attributeType);
                    collateralType.setCollateralType(collateralTypeLevel.getCollateralType());                      //120//Enum = CollateralTypeEnum
                    collateralTypeList.add(collateralType);
                }
                productTypeList.add(productType);
            }
            request.getUnderwritingRequest().getUnderwritingApprovalRequest().setApplication(applicationType);
            return request;
        } catch (Exception e) {
            log.error("convertInputModelToRequestModel() Exception : {}", e);
            throw e;
        }

    }

    @Override
    public com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceRequest convertInputModelToRequestModel(DocAppraisalRequest inputModel) throws Exception {
        log.debug("convertInputModelToRequestModel(DocAppraisalRequest : {})", inputModel.toString());
        com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceRequest request = null;
        com.clevel.selos.integration.brms.service.document.apprisalrules.ApplicationType applicationType = null;
        GregorianCalendar gregory = null;
        XMLGregorianCalendar calendar = null;
        List<com.clevel.selos.integration.brms.service.document.apprisalrules.AttributeType> attributeTypeList = null;
        com.clevel.selos.integration.brms.service.document.apprisalrules.AttributeType attributeType = null;
        List<com.clevel.selos.integration.brms.service.document.apprisalrules.BorrowerType> borrowerTypeList = null;
        com.clevel.selos.integration.brms.service.document.apprisalrules.BorrowerType borrowerType = null;
        List<com.clevel.selos.integration.brms.service.document.apprisalrules.ProductType> productTypeList = null;
        com.clevel.selos.integration.brms.service.document.apprisalrules.ProductType productType = null;
        List<com.clevel.selos.integration.brms.service.document.apprisalrules.SELOSProductProgramType> selosProductProgramTypeList = null;
        com.clevel.selos.integration.brms.service.document.apprisalrules.SELOSProductProgramType selosProductProgramType = null;
        List<com.clevel.selos.integration.brms.service.document.apprisalrules.CreditFacilityType> creditFacilityTypeList = null;
        com.clevel.selos.integration.brms.service.document.apprisalrules.CreditFacilityType creditFacilityType = null;
        List<com.clevel.selos.integration.brms.service.document.apprisalrules.CollateralType> collateralTypeList = null;
        com.clevel.selos.integration.brms.service.document.apprisalrules.CollateralType collateralType = null;

        //todo
        try {
            request = new com.clevel.selos.integration.brms.service.document.apprisalrules.DecisionServiceRequest();
            applicationType = request.getUnderwritingRequest().getUnderwritingApprovalRequest().getApplication();
            applicationTypeLevel = inputModel.getApplicationType();

            applicationType.setApplicationNumber(applicationTypeLevel.getApplicationNumber());  //1

            try {
                gregory = new GregorianCalendar();
                gregory.setTime(applicationTypeLevel.getProcessDate());                         //2
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            } catch (Exception e) {
                gregory = new GregorianCalendar();
                gregory.setTime(new Date());
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            }

            attributeTypeList = applicationType.getAttribute();
            attributeType = new com.clevel.selos.integration.brms.service.document.apprisalrules.AttributeType();

            try {
                gregory.setTime(applicationTypeLevel.getAttribute().getAppInDate());            //3
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            } catch (Exception e) {
                gregory = new GregorianCalendar();
                gregory.setTime(new Date());
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            }
            attributeType = new com.clevel.selos.integration.brms.service.document.apprisalrules.AttributeType();
            attributeType.setName("Set Of Borrower Flag");
            if(applicationTypeLevel.isSameSetOfBorrower()){                                     //7
                attributeType.setStringValue("YES");
            } else {
                attributeType.setStringValue("No");                                             //todo : YesNoEnum
            }
            attributeTypeList.add(attributeType);

            borrowerTypeList = applicationType.getBorrower();
            borrowerTypeLevelList = applicationTypeLevel.getBorrowerType();
            for (BorrowerTypeLevel borrowerTypeLevel : borrowerTypeLevelList) {
                borrowerType = new com.clevel.selos.integration.brms.service.document.apprisalrules.BorrowerType();
                borrowerType.getIndividual().setCitizenID(borrowerTypeLevel.getIndividualType().getPersonalId());   //69
                borrowerTypeList.add(borrowerType);
            }

            productTypeList = applicationType.getProduct();
            productTypeLevelList = applicationTypeLevel.getProductType();
            for(ProductTypeLevel productTypeLevel : productTypeLevelList) {
                productType = new com.clevel.selos.integration.brms.service.document.apprisalrules.ProductType();
                productType.setProductType(productTypeLevel.getProductGroup());                                     //110//Enum = SELOSProductGroupEnum

                selosProductProgramTypeList = productType.getSelosProductProgram();
                selosProductProgramTypeLevelList = productTypeLevel.getSelosProductProgramTypes();
                for (SELOSProductProgramTypeLevel selosProductProgramTypeLevel : selosProductProgramTypeLevelList) {
                    selosProductProgramType = new com.clevel.selos.integration.brms.service.document.apprisalrules.SELOSProductProgramType();
                    selosProductProgramType.setName(selosProductProgramTypeLevel.getProductProgram());              //113//Enum = SELOSProductProgramEnum
                    creditFacilityTypeList = selosProductProgramType.getCreditFacility();
                    creditFacilityTypeLevelList = selosProductProgramTypeLevel.getCreditFacilityType();
                    for (CreditFacilityTypeLevel creditFacilityTypeLevel : creditFacilityTypeLevelList) {
                        creditFacilityType = new com.clevel.selos.integration.brms.service.document.apprisalrules.CreditFacilityType();
                        creditFacilityType.setType(creditFacilityTypeLevel.getCreditFacilityType());                //114//Enum = CreditFacilityTypeEnum
                        creditFacilityTypeList.add(creditFacilityType);
                    }
                    selosProductProgramTypeList.add(selosProductProgramType);
                }

                collateralTypeList = productType.getCollateral();
                collateralTypeLevelList = productTypeLevel.getCollateralTypes();
                for (CollateralTypeLevel collateralTypeLevel : collateralTypeLevelList) {
                    collateralType = new com.clevel.selos.integration.brms.service.document.apprisalrules.CollateralType();
                    attributeType.setStringValue(collateralTypeLevel.getAttributeType().getCollateralPotential());  //119//Enum = CollateralPotentialEnum
                    attributeType = new com.clevel.selos.integration.brms.service.document.apprisalrules.AttributeType();
                    attributeType.setName("Collateral Potential");
                    collateralType.getAttribute().add(attributeType);
                    collateralType.setCollateralType(collateralTypeLevel.getCollateralType());                      //120//Enum = CollateralTypeEnum
                    collateralTypeList.add(collateralType);
                }
                productTypeList.add(productType);
            }
            request.getUnderwritingRequest().getUnderwritingApprovalRequest().setApplication(applicationType);
            return request;
        } catch (Exception e) {
            log.error("convertInputModelToRequestModel() Exception : {}", e);
            throw e;
        }
    }

    @Override
    public com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceRequest convertInputModelToRequestModel(StandardPricingIntRequest inputModel) throws Exception {
        log.debug("convertInputModelToRequestModel(StandardPricingIntRequest : {})", inputModel.toString());
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceRequest request = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.ApplicationType applicationType = null;
        GregorianCalendar gregory = null;
        XMLGregorianCalendar calendar = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.AttributeType> attributeTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.AttributeType attributeType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.BorrowerType> borrowerTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.BorrowerType borrowerType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.ProductType> productTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.ProductType productType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.SELOSProductProgramType> selosProductProgramTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.SELOSProductProgramType selosProductProgramType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.CreditFacilityType> creditFacilityTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.CreditFacilityType creditFacilityType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.CollateralType> collateralTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.CollateralType collateralType = null;

        String applicationNumber = null;
        Date processDate = null;
        Date appInDate = null;
        String collateralPotertialForPricing = null;
        String personalId = null;
        String productGroup = null;
        BigDecimal totalProposedCreditLimit = null;
        String productProgram = null;
        String strCreditFacilityType = null;
        BigDecimal creditLimit = null;
        String collateralPotential = null;
        String strCollateralType = null;
        try {
            request = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceRequest();
            applicationType = request.getUnderwritingRequest().getUnderwritingApprovalRequest().getApplication();
            applicationTypeLevel = inputModel.getApplicationType();

            applicationNumber = applicationTypeLevel.getApplicationNumber();                            //1
            log.debug("Application Number is {}", applicationNumber);
            applicationType.setApplicationNumber(applicationNumber);

            try {
                processDate = applicationTypeLevel.getProcessDate();                                    //2
                log.debug("Process Date is {}", processDate);
                gregory = new GregorianCalendar();
                gregory.setTime(processDate);
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            } catch (Exception e) {
                processDate = new Date();
                log.debug("Exception Process Date is {}", processDate);
                gregory = new GregorianCalendar();
                gregory.setTime(processDate);
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            }

            attributeTypeList = applicationType.getAttribute();
            attributeType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.AttributeType();

            try {
                appInDate = applicationTypeLevel.getAttribute().getAppInDate();                         //3
                log.debug("App in Date is {}", appInDate);
                gregory = new GregorianCalendar();
                gregory.setTime(appInDate);
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            } catch (Exception e) {
                appInDate = new Date();
                log.debug("Exception App in Date is {}", appInDate);
                gregory = new GregorianCalendar();
                gregory.setTime(new Date());
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            }

            attributeType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.AttributeType();
            attributeType.setName("Collateral Potential");
            collateralPotertialForPricing = applicationTypeLevel.getCollateralPotertialForPricing();      //48//Enum = CollateralPotentialEnum
            attributeType.setStringValue(collateralPotertialForPricing);
            log.debug("Enum is {}", "CollateralPotentialEnum");
            log.debug("Attribute Name : {}, String value : {} ", "Collateral Potential", collateralPotertialForPricing);
            attributeTypeList.add(attributeType);

            borrowerTypeList = applicationType.getBorrower();

            borrowerTypeLevelList = applicationTypeLevel.getBorrowerType();
            for (BorrowerTypeLevel model : borrowerTypeLevelList) {
                borrowerType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.BorrowerType();
                personalId = model.getIndividualType().getPersonalId();                                    //69
                log.debug("Personal Id is {}", personalId);
                borrowerType.getIndividual().setCitizenID(personalId);
                borrowerTypeList.add(borrowerType);
            }

            productTypeList = applicationType.getProduct();

            productTypeLevelList = applicationTypeLevel.getProductType();
            for (ProductTypeLevel model : productTypeLevelList) {
                productType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.ProductType();//110//Enum = SELOSProductGroupEnum
                productGroup = model.getProductGroup();
                log.debug("Enum is {}", "SELOSProductGroupEnum");
                productType.setProductType(productGroup);
                log.debug("Product Group is {}", productGroup);

                totalProposedCreditLimit =  model.getTotalProposedCreditLimit();                           //112
                productType.setRequestedCreditLimit(totalProposedCreditLimit);
                log.debug("Total Proposed Credit Limit is {}", totalProposedCreditLimit);

                selosProductProgramTypeList = productType.getSelosProductProgram();
                selosProductProgramTypeLevelList = model.getSelosProductProgramTypes();
                for (SELOSProductProgramTypeLevel selosProduct : selosProductProgramTypeLevelList) {
                    selosProductProgramType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.SELOSProductProgramType();
                    productProgram = selosProduct.getProductProgram();                                      //113//Enum = SELOSProductProgramEnum
                    log.debug("Enum is {}", "SELOSProductProgramEnum");
                    selosProductProgramType.setName(productProgram);
                    log.debug("Product Program is {}", productProgram);
                    creditFacilityTypeList = selosProductProgramType.getCreditFacility();
                    creditFacilityTypeLevelList = selosProduct.getCreditFacilityType();
                    for (CreditFacilityTypeLevel creditFacility : creditFacilityTypeLevelList) {
                        creditFacilityType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.CreditFacilityType();
                        strCreditFacilityType = creditFacility.getCreditFacilityType();                     //114//Enum = CreditFacilityTypeEnum
                        log.debug("Enum is {}", "CreditFacilityTypeEnum");
                        creditFacilityType.setType(strCreditFacilityType);
                        log.debug("Credit Facility Type is {}", strCreditFacilityType);
                        creditLimit = creditFacility.getCreditLimit();                                      //115
                        creditFacilityType.setCreditLimit(creditLimit);
                        log.debug("Credit Limit is {}", creditLimit);

                        creditFacility.getGuaranteeType();                                                  //117//Enum = guaranteeTypeEnum  //todo : setGuaranteeType did not found

                        creditFacilityTypeList.add(creditFacilityType);
                    }
                    selosProductProgramTypeList.add(selosProductProgramType);
                }
                collateralTypeList = productType.getCollateral();
                collateralTypeLevelList = model.getCollateralTypes();
                for (CollateralTypeLevel collateralTypeLevel : collateralTypeLevelList) {
                    collateralType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.CollateralType();

                    attributeType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.AttributeType();
                    collateralPotential = collateralTypeLevel.getAttributeType().getCollateralPotential();  //119//Enum = CollateralPotentialEnum
                    attributeType.setStringValue(collateralPotential);
                    attributeType.setName("Collateral Potential");
                    log.debug("Enum is {}", "CollateralPotentialEnum");
                    log.debug("Attribute Name : {}, String value : {} ", "Collateral Potential", collateralPotential);
                    collateralType.getAttribute().add(attributeType);

                    log.debug("Enum is {}", "CollateralTypeEnum");
                    strCollateralType = collateralTypeLevel.getCollateralType();                           //120//Enum = CollateralTypeEnum
                    collateralType.setCollateralType(strCollateralType);
                    log.debug("Collateral Type is {}", strCollateralType);

                    collateralTypeList.add(collateralType);
                }
                productTypeList.add(productType);
            }
            request.getUnderwritingRequest().getUnderwritingApprovalRequest().setApplication(applicationType);
            return request;
        } catch (Exception e) {
            log.error("convertInputModelToRequestModel() Exception : {}", e);
            throw e;
        }
    }

    @Override
    public com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceRequest convertInputModelToRequestModel(StandardPricingFeeRequest inputModel) throws Exception {
        log.debug("convertInputModelToRequestModel(StandardPricingFeeRequest : {})", inputModel.toString());
        com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceRequest request = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.ApplicationType applicationType = null;
        GregorianCalendar gregory = null;
        XMLGregorianCalendar calendar = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType> attributeTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType attributeType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.BorrowerType> borrowerTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.BorrowerType borrowerType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.ProductType> productTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.ProductType productType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.SELOSProductProgramType> selosProductProgramTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.SELOSProductProgramType selosProductProgramType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.CreditFacilityType> creditFacilityTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.CreditFacilityType creditFacilityType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.CollateralType> collateralTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.CollateralType collateralType = null;

        String applicationNumber = null;
        Date processDate = null;
        Date appInDate = null;
        String collateralPotertialForPricing = null;
        String personalId = null;
        String productGroup = null;
        BigDecimal totalProposedCreditLimit = null;
        String productProgram = null;
        String strCreditFacilityType = null;
        BigDecimal creditLimit = null;
        String collateralPotential = null;
        String strCollateralType = null;
        try {
            request = new com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceRequest();
            applicationType = request.getUnderwritingRequest().getUnderwritingApprovalRequest().getApplication();
            applicationTypeLevel = inputModel.getApplicationType();

            applicationNumber = applicationTypeLevel.getApplicationNumber();                            //1
            log.debug("Application Number is {}", applicationNumber);
            applicationType.setApplicationNumber(applicationNumber);

            try {
                processDate = applicationTypeLevel.getProcessDate();                                    //2
                log.debug("Process Date is {}", processDate);
                gregory = new GregorianCalendar();
                gregory.setTime(processDate);
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            } catch (Exception e) {
                processDate = new Date();
                log.debug("Exception Process Date is {}", processDate);
                gregory = new GregorianCalendar();
                gregory.setTime(processDate);
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            }

            attributeTypeList = applicationType.getAttribute();
            attributeType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType();

            try {
                appInDate = applicationTypeLevel.getAttribute().getAppInDate();                         //3
                log.debug("App in Date is {}", appInDate);
                gregory = new GregorianCalendar();
                gregory.setTime(appInDate);
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            } catch (Exception e) {
                appInDate = new Date();
                log.debug("Exception App in Date is {}", appInDate);
                gregory = new GregorianCalendar();
                gregory.setTime(new Date());
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            }

            attributeType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType();
            attributeType.setName("Collateral Potential");
            collateralPotertialForPricing = applicationTypeLevel.getCollateralPotertialForPricing();      //48//Enum = CollateralPotentialEnum
            attributeType.setStringValue(collateralPotertialForPricing);
            log.debug("Enum is {}", "CollateralPotentialEnum");
            log.debug("Attribute Name : {}, String value : {} ", "Collateral Potential", collateralPotertialForPricing);
            attributeTypeList.add(attributeType);

            borrowerTypeList = applicationType.getBorrower();

            borrowerTypeLevelList = applicationTypeLevel.getBorrowerType();
            for (BorrowerTypeLevel model : borrowerTypeLevelList) {
                borrowerType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.BorrowerType();
                personalId = model.getIndividualType().getPersonalId();                                    //69
                log.debug("Personal Id is {}", personalId);
                borrowerType.getIndividual().setCitizenID(personalId);
                borrowerTypeList.add(borrowerType);
            }

            productTypeList = applicationType.getProduct();

            productTypeLevelList = applicationTypeLevel.getProductType();
            for (ProductTypeLevel model : productTypeLevelList) {
                productType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.ProductType();//110//Enum = SELOSProductGroupEnum
                productGroup = model.getProductGroup();
                log.debug("Enum is {}", "SELOSProductGroupEnum");
                productType.setProductType(productGroup);
                log.debug("Product Group is {}", productGroup);

                totalProposedCreditLimit =  model.getTotalProposedCreditLimit();                           //112
                productType.setRequestedCreditLimit(totalProposedCreditLimit);
                log.debug("Total Proposed Credit Limit is {}", totalProposedCreditLimit);

                selosProductProgramTypeList = productType.getSelosProductProgram();
                selosProductProgramTypeLevelList = model.getSelosProductProgramTypes();
                for (SELOSProductProgramTypeLevel selosProduct : selosProductProgramTypeLevelList) {
                    selosProductProgramType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.SELOSProductProgramType();
                    productProgram = selosProduct.getProductProgram();                                      //113//Enum = SELOSProductProgramEnum
                    log.debug("Enum is {}", "SELOSProductProgramEnum");
                    selosProductProgramType.setName(productProgram);
                    log.debug("Product Program is {}", productProgram);

                    creditFacilityTypeList = selosProductProgramType.getCreditFacility();
                    creditFacilityTypeLevelList = selosProduct.getCreditFacilityType();
                    for (CreditFacilityTypeLevel creditFacility : creditFacilityTypeLevelList) {
                        creditFacilityType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.CreditFacilityType();
                        strCreditFacilityType = creditFacility.getCreditFacilityType();                     //114//Enum = CreditFacilityTypeEnum
                        log.debug("Enum is {}", "CreditFacilityTypeEnum");
                        creditFacilityType.setType(strCreditFacilityType);
                        log.debug("Credit Facility Type is {}", strCreditFacilityType);
                        creditLimit = creditFacility.getCreditLimit();                                      //115
                        creditFacilityType.setCreditLimit(creditLimit);
                        log.debug("Credit Limit is {}", creditLimit);

                        creditFacility.getGuaranteeType();                                                  //117//Enum = guaranteeTypeEnum  //todo : setGuaranteeType did not found

                        creditFacilityTypeList.add(creditFacilityType);
                    }
                    selosProductProgramTypeList.add(selosProductProgramType);
                }
                collateralTypeList = productType.getCollateral();
                collateralTypeLevelList = model.getCollateralTypes();
                for (CollateralTypeLevel collateralTypeLevel : collateralTypeLevelList) {
                    collateralType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.CollateralType();

                    attributeType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType();
                    collateralPotential = collateralTypeLevel.getAttributeType().getCollateralPotential();  //119//Enum = CollateralPotentialEnum
                    attributeType.setStringValue(collateralPotential);
                    attributeType.setName("Collateral Potential");
                    log.debug("Enum is {}", "CollateralPotentialEnum");
                    log.debug("Attribute Name : {}, String value : {} ", "Collateral Potential", collateralPotential);
                    collateralType.getAttribute().add(attributeType);

                    log.debug("Enum is {}", "CollateralTypeEnum");
                    strCollateralType = collateralTypeLevel.getCollateralType();                           //120//Enum = CollateralTypeEnum
                    collateralType.setCollateralType(strCollateralType);
                    log.debug("Collateral Type is {}", strCollateralType);

                    collateralTypeList.add(collateralType);
                }
                productTypeList.add(productType);
            }
            request.getUnderwritingRequest().getUnderwritingApprovalRequest().setApplication(applicationType);
            return request;
        } catch (Exception e) {
            log.error("convertInputModelToRequestModel() Exception : {}", e);
            throw e;
        }
    }


}
