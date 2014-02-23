package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.request.data.*;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Converter implements Serializable {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    private ApplicationTypeLevel applicationTypeLevel = null;
    private List<BorrowerTypeLevel> borrowerTypeLevelList;
    private List<AccountTypeLevelBorrower> accountTypeLevelBorrowerList = null;
    private NCBReportTypeLevel ncbReportTypeLevel = null;
    private List<AccountTypeLevelNCBReport> ncbAccountTypeLevelList = null;
    private List<NCBEnquiryTypeLevel> ncbEnquiryTypesTypeLevelList = null;
    private List<WarningCodeFullMatched> warningCodeFullMatchedLevelList = null;
    private List<WarningCodePartialMatched> warningCodePartialMatchedLevelList = null;
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
    public Converter() {
    }

    public com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceRequest convertInputModelToRequestModel(PreScreenRequest inputModel) throws Exception {
        log.debug("convertInputModelToRequestModel(PreScreenRequest : {})", inputModel.toString());
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceRequest request = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.ApplicationType applicationType = null;
        GregorianCalendar gregory = null;
        XMLGregorianCalendar calendar = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType> attributeTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType attributeType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.BorrowerType> borrowerTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.BorrowerType borrowerType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AccountType> accountTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AccountType accountType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.NCBAccountType> ncbAccountTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.NCBAccountType ncbAccountType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.NCBReportType> ncbReportTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.NCBReportType ncbReportType = null;

        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.NCBEnquiryType> ncbEnquiryTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.NCBEnquiryType ncbEnquiryType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.WarningCodeFullMatchedType> warningCodeFullMatchedTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.WarningCodeFullMatchedType warningCodeFullMatchedType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.WarningCodePartialMatchedType> warningCodePartialMatchedTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.WarningCodePartialMatchedType warningCodePartialMatchedType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.BusinessType> businessTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.BusinessType businessType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.ProductType> productTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.ProductType productType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.SELOSProductProgramType> selosProductProgramTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.SELOSProductProgramType selosProductProgramType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.CreditFacilityType> creditFacilityTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.CreditFacilityType creditFacilityType = null;
        List<com.clevel.selos.integration.brms.service.prescreenunderwritingrules.CollateralType> collateralTypeList = null;
        com.clevel.selos.integration.brms.service.prescreenunderwritingrules.CollateralType collateralType = null;

        try {
            applicationTypeLevel = inputModel.getApplicationType();

            request = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceRequest();
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
            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();

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

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Customer Entity");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().getCustomerEntity());              //5//Enum = CustomerEntityEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Existing SME Customer Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isExistingSMECustomer()+"");       //6//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Set Of Borrower Flag");
            attributeType.setStringValue(applicationTypeLevel.isSameSetOfBorrower()+"");                        //7//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Refinance In Flag");
            attributeType.setStringValue(applicationTypeLevel.isRefinanceInFlag()+"");                          //8//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Refinance Out Flag");
            attributeType.setStringValue(applicationTypeLevel.isRefinanceOutFlag()+"");                         //9//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Borrower Group Sale");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getBorrowerGroupSale());          //19
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Related Juristic Group Sale");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getTotalGroupSale());             //20
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Num Of Total Facility");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNumOfTotalFacility());             //29
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Num Of Contingent Facility");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNumOfContingentFacility());        //30
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Business Location");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().getBusinessLocation());                 //44//Enum = ProvinceEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Year In Business");
            attributeType.setNumericValue(new BigDecimal(applicationTypeLevel.getAttribute().getYearInBusiness()));  //45
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
            attributeType.setName("Country Of Business");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().getCountryOfRegistration()+"");         //46//Enum = CountryEnum   todo to check result
            attributeTypeList.add(attributeType);

            borrowerTypeList = applicationType.getBorrower();
            borrowerTypeLevelList = applicationTypeLevel.getBorrowerType();
            for (BorrowerTypeLevel borrowerTypeLevel : borrowerTypeLevelList) {
                borrowerType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.BorrowerType();
                attributeTypeList = borrowerType.getAttribute();

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Customer Entity");
                attributeType.setStringValue(borrowerTypeLevel.getCustomerEntity()+"");                         //5//Enum = CustomerEntityEnum  todo to check result
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Existing SME Customer Flag");
                attributeType.setStringValue(borrowerTypeLevel.isExistingSMECustomer() + "");                     //6//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Relationship Type");
                attributeType.setStringValue(borrowerTypeLevel.getRelationshipType()+"");                       //51//Enum = RelationshipTypeEnum todo to check result
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Reference");
                attributeType.setStringValue(borrowerTypeLevel.getReference()+"");                              //52//Enum = ReferenceEnum todo to check result
                attributeTypeList.add(attributeType);

                borrowerType.setNationality(borrowerTypeLevel.getNationality()+"");                             //53//Enum = NationalityEnum todo to check result

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Number Of Month From Last Set Up Date");
                attributeType.setNumericValue(new BigDecimal(borrowerTypeLevel.getNumberOfMonthFromLastSetUpDate()));    //54
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("New Qualitative");
                attributeType.setStringValue(borrowerTypeLevel.getNewQualitative());                   //55//Enum = NewQualitativeEnum
                attributeTypeList.add(attributeType);

                accountTypeList = borrowerType.getAccount();
                accountTypeLevelBorrowerList = borrowerTypeLevel.getAccountType();
                for (AccountTypeLevelBorrower accountTypeLevelBorrower : accountTypeLevelBorrowerList) {
                    attributeTypeList = accountType.getAttribute();

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("Adjust Class");
                    attributeType.setStringValue(accountTypeLevelBorrower.getAttributeType().getBotClass());     //56//Enum = BOTClassEnum
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("Account Active Flag");
                    attributeType.setStringValue(accountTypeLevelBorrower.getAttributeType().isActiveFlag() + "");           //72//Enum = todo Enum YesNOEnum
                    attributeTypeList.add(attributeType);

                    accountType.setAccountType(accountTypeLevelBorrower.getDataSource());   //73//Enum = AccountTypeEnum  e.g. 04,05,09,22 etc.

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("Account Reference");
                    attributeType.setStringValue(accountTypeLevelBorrower.getAttributeType().getAccountRef()); //74//Enum =  AccountReferenceEnum
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("Cust To Account Relationship");
                    attributeType.setStringValue(accountTypeLevelBorrower.getAttributeType().getCustToAccountRelationship()); //75//Enum =  CustToAccountRelationshipEnum
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("Number Of Day Principal Past Due");
                    attributeType.setNumericValue(accountTypeLevelBorrower.getAttributeType().getNumberOfDayPrincipalPastDue()); //76
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("Number Of Day Interest Past Due");
                    attributeType.setNumericValue(accountTypeLevelBorrower.getAttributeType().getNumberOfDayInterestPastDue()); //77
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("Card Block Code");
                    attributeType.setStringValue(accountTypeLevelBorrower.getAttributeType().getCardBlockCode()); //78//Enum =  CardBlockCodeEnum
                    attributeTypeList.add(attributeType);

                    accountTypeList.add(accountType);
                }

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                try {
                    gregory.setTime(borrowerTypeLevel.getNextReviewDate());                             //57
                    calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                    attributeType.setName("Next Review Date");
                    attributeType.setDateTimeValue(calendar);
                    attributeTypeList.add(attributeType);
                } catch (Exception e) {
                    gregory = new GregorianCalendar();
                    gregory.setTime(new Date());
                    calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                    attributeType.setName("Next Review Date");
                    attributeType.setDateTimeValue(calendar);
                    attributeTypeList.add(attributeType);
                }

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Next Review Date Flag");
                attributeType.setStringValue(borrowerTypeLevel.isNextReviewDateFlag() + "");           //58//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                try {
                    gregory.setTime(borrowerTypeLevel.getExtendedReviewDate());                             //59
                    calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                    attributeType.setName("Extended Review Date");
                    attributeType.setDateTimeValue(calendar);
                    attributeTypeList.add(attributeType);
                } catch (Exception e) {
                    gregory = new GregorianCalendar();
                    gregory.setTime(new Date());
                    calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                    attributeType.setName("Extended Review Date");
                    attributeType.setDateTimeValue(calendar);
                    attributeTypeList.add(attributeType);
                }

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Extended Review Date Flag");
                attributeType.setStringValue(borrowerTypeLevel.isExtendedReviewDateFlag() + "");           //60//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Rating Final");
                attributeType.setStringValue(borrowerTypeLevel.getRatingFinal());     //61//Enum = BOTClassEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Unpaid Fee Flag");
                attributeType.setStringValue(borrowerTypeLevel.isUnpaidFeePaid() + "");           //62//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Claimed LG Flag");
                attributeType.setStringValue(borrowerTypeLevel.isClaimedLGFlag() + "");           //63//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                borrowerType.getIndividual().setCitizenID(borrowerTypeLevel.getIndividualType().getPersonalId());//69
                borrowerType.getIndividual().setAge(borrowerTypeLevel.getIndividualType().getAge());  //70

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Day Overdue Annual Review");
                attributeType.setNumericValue(borrowerTypeLevel.getDayAnnualReviewOverdue());                   //#N/A
                attributeTypeList.add(attributeType);


                /*ncbReportTypeList = borrowerType.getNcbReport();
                ncbReportTypeLevel = borrowerTypeLevel.getNcbReportType();*/

                ncbAccountTypeList = ncbReportType.getNcbAccount();
                ncbReportTypeLevel = borrowerTypeLevel.getNcbReportType();
                ncbAccountTypeLevelList = ncbReportTypeLevel.getNcbAccountType();
                for (AccountTypeLevelNCBReport accountTypeLevelNCBReport : ncbAccountTypeLevelList) {
                    attributeTypeList = ncbAccountType.getAttribute();

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("NCB Flag");
                    attributeType.setStringValue(accountTypeLevelNCBReport.getAttributeType().isNcbFlag() + "");           //79//todo Enum YesNOEnum
                    attributeTypeList.add(attributeType);

                    ncbAccountType.setNcbAccountStatus(accountTypeLevelNCBReport.getNcbAccountStatus());  //80Enum = enum=NCBAccountStatusEnum

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("TMB Bank Flag");
                    attributeType.setStringValue(accountTypeLevelNCBReport.getAttributeType().isTmbBankFlag() + "");    //81//todo Enum YesNOEnum
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("NCB NPL Flag");
                    attributeType.setStringValue(accountTypeLevelNCBReport.getAttributeType().isNcbNPLFlag() + "");     //82//todo Enum YesNOEnum
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("Credit Amount At First NPL Date");
                    attributeType.setNumericValue(accountTypeLevelNCBReport.getCreditAmountFirstNPL()); //83
                    attributeTypeList.add(attributeType);

                    accountType.setTdrFlag(accountTypeLevelNCBReport.isNcbTDRFlag());                   //84

                    ///////////////////////////////////////////////////////////////////////     //86-88 did not found.
                    accountTypeLevelNCBReport.getCurrentPayment();                                      //86
                    accountTypeLevelNCBReport.getPaymentPattern6M();                                    //87
                    accountTypeLevelNCBReport.getPaymentPattern12M();                                   //88
                    ////////////////////////////////////////////////////////////////////////////////////////

                    accountType.setOverdue31DTo60DCount(accountTypeLevelNCBReport.getOverDue31to60DaysWithinLast12Months()); //89
                    ncbAccountTypeList.add(ncbAccountType);

                }
                ncbEnquiryTypeList = ncbReportType.getNcbEnquiry();
                ncbEnquiryTypesTypeLevelList = ncbReportTypeLevel.getNcbEnquiryTypes();
                for (NCBEnquiryTypeLevel ncbEnquiryTypeLevel : ncbEnquiryTypesTypeLevelList) {
                    attributeTypeList = ncbEnquiryType.getAttribute();

                    ncbEnquiryType.setNumSearchesLast6Mths(ncbEnquiryTypeLevel.getNumberOfSearchInLast6Months());  //91

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("Validity Period Of NCB");
                    attributeType.setNumericValue(ncbEnquiryTypeLevel.getAttributeType().getNumberOfDaysNCBCheck()); //92
                    attributeTypeList.add(attributeType);

                    ncbEnquiryTypeList.add(ncbEnquiryType);
                }

                warningCodeFullMatchedTypeList = borrowerType.getWarningCodeFullMatched();
                warningCodeFullMatchedLevelList = borrowerTypeLevel.getWarningCodeFullMatched();
                for (WarningCodeFullMatched warningCodeFullMatched : warningCodeFullMatchedLevelList) {
                    warningCodeFullMatchedType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.WarningCodeFullMatchedType();
                    warningCodeFullMatchedType.setCode(warningCodeFullMatched.getRiskCodeWithFullyIdentifyMatched());  //93
                    warningCodeFullMatchedTypeList.add(warningCodeFullMatchedType);
                }

                warningCodePartialMatchedTypeList = borrowerType.getWarningCodePartialMatched();
                warningCodePartialMatchedLevelList = borrowerTypeLevel.getWarningCodePartialMatched();
                for (WarningCodePartialMatched warningCodePartialMatched : warningCodePartialMatchedLevelList) {
                    warningCodePartialMatchedType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.WarningCodePartialMatchedType();
                    warningCodePartialMatchedType.setCode(warningCodePartialMatched.getRiskCodeWithSomeIdentifyMatched());  //94
                    warningCodePartialMatchedTypeList.add(warningCodePartialMatchedType);
                }

                borrowerTypeList.add(borrowerType);
            }

            ////////////////////////////////////////////////////////////////////////
            accountTypeLevelList = applicationTypeLevel.getAccountType();
            for (AccountTypeLevel accountTypeLevel : accountTypeLevelList) {
                accountTypeLevel.getUtilizationPercent();   //95
                accountTypeLevel.getSwingPercent();             //96
                accountTypeLevel.getAvgL6MInFlowLimit();            //97
                accountTypeLevel.getNoOfTransaction();                  //98
                accountTypeLevel.isExcludeIncomeFlag();                     //105
            }
            ////////////////////////////////////////////////////////////////////////


            businessTypeList = applicationType.getBusiness();
            businessTypeLevelList = applicationTypeLevel.getBusinessType();
            for (BusinessTypeLevel businessTypeLevel : businessTypeLevelList) {
                businessType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.BusinessType();

                attributeTypeList = businessType.getAttribute();

                businessType.setID(businessTypeLevel.getBusinessCodeRunningNumber());  //106
                businessType.setBusinessType(businessTypeLevel.getBusinessCode());     //107

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("Negative Flag");
                attributeType.setStringValue(businessTypeLevel.getAttributeType().isNegativeFlag() + "");     //108//todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                attributeType.setName("High Risk Flag");
                attributeType.setStringValue(businessTypeLevel.getAttributeType().isHighRiskFlag() + "");     //109//todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                businessTypeList.add(businessType);
            }



            productTypeList = applicationType.getProduct();
            productTypeLevelList = applicationTypeLevel.getProductType();
            for(ProductTypeLevel productTypeLevel : productTypeLevelList) {
                productType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.ProductType();
                productType.setProductType(productTypeLevel.getProductGroup());                                     //110//Enum = SELOSProductGroupEnum
                selosProductProgramTypeList = productType.getSelosProductProgram();
                selosProductProgramTypeLevelList = productTypeLevel.getSelosProductProgramTypes();
                for (SELOSProductProgramTypeLevel selosProductProgramTypeLevel : selosProductProgramTypeLevelList) {
                    selosProductProgramType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.SELOSProductProgramType();
                    selosProductProgramType.setName(selosProductProgramTypeLevel.getProductProgram());              //113//Enum = SELOSProductProgramEnum
                    creditFacilityTypeList = selosProductProgramType.getCreditFacility();
                    creditFacilityTypeLevelList = selosProductProgramTypeLevel.getCreditFacilityType();
                    for (CreditFacilityTypeLevel creditFacilityTypeLevel : creditFacilityTypeLevelList) {
                        creditFacilityType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.CreditFacilityType();
                        creditFacilityType.setType(creditFacilityTypeLevel.getCreditFacilityType());                //114//Enum = CreditFacilityTypeEnum

                        creditFacilityTypeList.add(creditFacilityType);
                    }
                    selosProductProgramTypeList.add(selosProductProgramType);
                }

                collateralTypeList = productType.getCollateral();
                collateralTypeLevelList = productTypeLevel.getCollateralTypes();
                for (CollateralTypeLevel collateralTypeLevel : collateralTypeLevelList) {
                    collateralType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.CollateralType();

                    attributeType = new com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType();
                    attributeType.setName("Collateral Potential");
                    attributeType.setStringValue(collateralTypeLevel.getAttributeType().getCollateralPotential());  //119//Enum = CollateralPotentialEnum
                    attributeTypeList.add(attributeType);

                    collateralType.setCollateralType(collateralTypeLevel.getCollateralType());          //120//Enum = CollateralTypeEnum
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

    public com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceRequest convertInputModelToRequestModel(FullApplicationRequest inputModel) throws Exception {
        log.debug("convertInputModelToRequestModel(FullApplication : {})", inputModel.toString());
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceRequest request = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.ApplicationType applicationType = null;
        GregorianCalendar gregory = null;
        XMLGregorianCalendar calendar = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType> attributeTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType attributeType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.BorrowerType> borrowerTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.BorrowerType borrowerType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AccountType> accountTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AccountType accountType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.NCBReportType> ncbReportTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.NCBReportType ncbReportType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.NCBAccountType> ncbAccountTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.NCBAccountType ncbAccountType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.NCBEnquiryType> ncbEnquiryTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.NCBEnquiryType ncbEnquiryType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.WarningCodeFullMatchedType> warningCodeFullMatchedTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.WarningCodeFullMatchedType warningCodeFullMatchedType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.WarningCodePartialMatchedType> warningCodePartialMatchedTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.WarningCodePartialMatchedType warningCodePartialMatchedType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.BusinessType> businessTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.BusinessType businessType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.ProductType> productTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.ProductType productType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.SELOSProductProgramType> selosProductProgramTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.SELOSProductProgramType selosProductProgramType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.CreditFacilityType> creditFacilityTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.CreditFacilityType creditFacilityType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.CollateralType> collateralTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.CollateralType collateralType = null;
        List<com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AppraisalType> appraisalTypeList = null;
        com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AppraisalType appraisalType = null;

        try {
            applicationTypeLevel = inputModel.getApplicationType();

            request = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.DecisionServiceRequest();
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
            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();

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

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Customer Entity");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().getCustomerEntity());              //5//Enum = CustomerEntityEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Existing SME Customer Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isExistingSMECustomer()+"");       //6//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Set Of Borrower Flag");
            attributeType.setStringValue(applicationTypeLevel.isSameSetOfBorrower()+"");                        //7//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Refinance In Flag");
            attributeType.setStringValue(applicationTypeLevel.isRefinanceInFlag()+"");                          //8//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Refinance Out Flag");
            attributeType.setStringValue(applicationTypeLevel.isRefinanceOutFlag()+"");                         //9//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("SBCG Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isSBCGFlag()+"");                  //13//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("AAD Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isAadFlag()+"");                   //14//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Number Of Year From Latest Financial Statement");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNumberOfYearFromLatestFinancialStatement());//15
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Net Worth");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNetWorth());                   //16
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Final DBR");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getFinalDBR());                   //17
            attributeTypeList.add(attributeType);

            applicationType.setTotalMonthlyIncome(applicationTypeLevel.getAttribute().getMonthlyIncome());      //18

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Borrower Group Sale");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getBorrowerGroupSale());          //19
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Related Juristic Group Sale");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getTotalGroupSale());             //20
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Existing Group Exposure");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getExistingGroupExposure());      //21
            attributeTypeList.add(attributeType);

            applicationType.setAggregatedCreditExposureLimit(applicationTypeLevel.getFinalGroupExposure());     //22

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("LG Capability Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isLgCapability()+"");                   //23//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Ever LG Claim Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isEverLgClaim()+"");                   //24//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Abandon Project Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isAbandonProject()+"");                //25//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Project Delay Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isProjectDelay()+"");                  //26//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Sufficient Source Of Fund Flag");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().isSufficientSourceOfFund()+"");        //27//Enum = todo Enum YesNOEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Prime Customer");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().getPrimeCustomer());                   //28//Enum = Prime Customer
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Num Of Total Facility");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNumOfTotalFacility());             //29
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Num Of Contingent Facility");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNumOfContingentFacility());        //30
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Number Of Existing OD");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNumberOfExistiongOD());            //31
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Number Of Requested OD");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNumberOfRequestedOD());            //32
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Num Of Core Asset");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNumberOfCoreAsset());              //33
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Num Of Non Core Asset");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNumOfNonCoreAsset());              //34
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Total Fix Asset Value");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getTotalFixAssetValue());             //35
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Existing OD limit");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getExistingODLimit());                //36
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Total Collateral Precent Of Exposure");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getTotalCollOfExposure());            //37
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Total W/C Requirement");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getTotalWCRequirement());             //38
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Net W/C Requirement (1.25x)");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNetWCRequirement125x());           //39
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Net W/C Requirement (1.5x)");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNetWCRequirement15x());            //40
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Net W/C Requirement (35% of Sales)");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getNetWCRequirement35());             //41
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Existing W/C credit limit with TMB");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getExistingWCRCreditLimitWithTMB());  //42
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Existing Core W/C Loan credit limit with TMB");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getExistingCoreWCLoanCreditLimitWithTMB());//43
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Business Location");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().getBusinessLocation());                 //44//Enum = ProvinceEnum
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Year In Business");
            attributeType.setNumericValue(new BigDecimal(applicationTypeLevel.getAttribute().getYearInBusiness()));  //45
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Country Of Business");
            attributeType.setStringValue(applicationTypeLevel.getAttribute().getCountryOfRegistration()+"");         //46//Enum = CountryEnum   todo to check result
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Trade Cheque Return Percent");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getTradeChequeReturn());               //47
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Collateral Potential");
            attributeType.setStringValue(applicationTypeLevel.getCollateralPotertialForPricing());                   //48//Enum = CollateralPotentialEnum
            attributeTypeList.add(attributeType);


            borrowerTypeList = applicationType.getBorrower();
            borrowerTypeLevelList = applicationTypeLevel.getBorrowerType();
            for (BorrowerTypeLevel borrowerTypeLevel : borrowerTypeLevelList) {
                borrowerType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.BorrowerType();
                attributeTypeList = borrowerType.getAttribute();

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Customer Entity");
                attributeType.setStringValue(borrowerTypeLevel.getCustomerEntity()+"");                         //5//Enum = CustomerEntityEnum  todo to check result
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Existing SME Customer Flag");
                attributeType.setStringValue(borrowerTypeLevel.isExistingSMECustomer() + "");                     //6//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Relationship Type");
                attributeType.setStringValue(borrowerTypeLevel.getRelationshipType()+"");                       //51//Enum = RelationshipTypeEnum todo to check result
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Reference");
                attributeType.setStringValue(borrowerTypeLevel.getReference()+"");                              //52//Enum = ReferenceEnum todo to check result
                attributeTypeList.add(attributeType);

                borrowerType.setNationality(borrowerTypeLevel.getNationality()+"");                             //53//Enum = NationalityEnum todo to check result

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Number Of Month From Last Set Up Date");
                attributeType.setNumericValue(new BigDecimal(borrowerTypeLevel.getNumberOfMonthFromLastSetUpDate()));    //54
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("New Qualitative");
                attributeType.setStringValue(borrowerTypeLevel.getNewQualitative());                   //55//Enum = NewQualitativeEnum
                attributeTypeList.add(attributeType);


                accountTypeList = borrowerType.getAccount();
                accountTypeLevelBorrowerList = borrowerTypeLevel.getAccountType();
                for (AccountTypeLevelBorrower accountTypeLevelBorrower : accountTypeLevelBorrowerList) {
                    attributeTypeList = accountType.getAttribute();

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("Adjust Class");
                    attributeType.setStringValue(accountTypeLevelBorrower.getAttributeType().getBotClass());     //56//Enum = BOTClassEnum
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("Account Active Flag");
                    attributeType.setStringValue(accountTypeLevelBorrower.getAttributeType().isActiveFlag() + "");           //72//Enum = todo Enum YesNOEnum
                    attributeTypeList.add(attributeType);

                    accountType.setAccountType(accountTypeLevelBorrower.getDataSource());   //73//Enum = AccountTypeEnum  e.g. 04,05,09,22 etc.

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("Account Reference");
                    attributeType.setStringValue(accountTypeLevelBorrower.getAttributeType().getAccountRef()); //74//Enum =  AccountReferenceEnum
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("Cust To Account Relationship");
                    attributeType.setStringValue(accountTypeLevelBorrower.getAttributeType().getCustToAccountRelationship()); //75//Enum =  CustToAccountRelationshipEnum
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("Number Of Day Principal Past Due");
                    attributeType.setNumericValue(accountTypeLevelBorrower.getAttributeType().getNumberOfDayPrincipalPastDue()); //76
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("Number Of Day Interest Past Due");
                    attributeType.setNumericValue(accountTypeLevelBorrower.getAttributeType().getNumberOfDayInterestPastDue()); //77
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("Card Block Code");
                    attributeType.setStringValue(accountTypeLevelBorrower.getAttributeType().getCardBlockCode()); //78//Enum =  CardBlockCodeEnum
                    attributeTypeList.add(attributeType);

                    accountTypeList.add(accountType);
                }

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                try {
                    gregory.setTime(borrowerTypeLevel.getNextReviewDate());                             //57
                    calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                    attributeType.setName("Next Review Date");
                    attributeType.setDateTimeValue(calendar);
                    attributeTypeList.add(attributeType);
                } catch (Exception e) {
                    gregory = new GregorianCalendar();
                    gregory.setTime(new Date());
                    calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                    attributeType.setName("Next Review Date");
                    attributeType.setDateTimeValue(calendar);
                    attributeTypeList.add(attributeType);
                }

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Next Review Date Flag");
                attributeType.setStringValue(borrowerTypeLevel.isNextReviewDateFlag() + "");           //58//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                try {
                    gregory.setTime(borrowerTypeLevel.getExtendedReviewDate());                             //59
                    calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                    attributeType.setName("Extended Review Date");
                    attributeType.setDateTimeValue(calendar);
                    attributeTypeList.add(attributeType);
                } catch (Exception e) {
                    gregory = new GregorianCalendar();
                    gregory.setTime(new Date());
                    calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                    attributeType.setName("Extended Review Date");
                    attributeType.setDateTimeValue(calendar);
                    attributeTypeList.add(attributeType);
                }

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Extended Review Date Flag");
                attributeType.setStringValue(borrowerTypeLevel.isExtendedReviewDateFlag() + "");           //60//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Rating Final");
                attributeType.setStringValue(borrowerTypeLevel.getRatingFinal());     //61//Enum = BOTClassEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Unpaid Fee Flag");
                attributeType.setStringValue(borrowerTypeLevel.isUnpaidFeePaid() + "");           //62//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Claimed LG Flag");
                attributeType.setStringValue(borrowerTypeLevel.isClaimedLGFlag() + "");           //63//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Credit Worthiness Flag");
                attributeType.setStringValue(borrowerTypeLevel.getCreditWorthiness() + "");           //64//Enum = todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                borrowerType.setKycRiskLevel(borrowerTypeLevel.getKyc());                          //65
                borrowerType.getIndividual().setCitizenID(borrowerTypeLevel.getIndividualType().getPersonalId());//69
                borrowerType.getIndividual().setAge(borrowerTypeLevel.getIndividualType().getAge());  //70

                /*ncbReportTypeList = borrowerType.getNcbReport();
                ncbReportTypeLevel = borrowerTypeLevel.getNcbReportType();*/

                ncbAccountTypeList = ncbReportType.getNcbAccount();
                ncbAccountTypeLevelList = ncbReportTypeLevel.getNcbAccountType();
                for (AccountTypeLevelNCBReport accountTypeLevelNCBReport : ncbAccountTypeLevelList) {
                    attributeTypeList = ncbAccountType.getAttribute();

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("NCB Flag");
                    attributeType.setStringValue(accountTypeLevelNCBReport.getAttributeType().isNcbFlag() + "");           //79//todo Enum YesNOEnum
                    attributeTypeList.add(attributeType);

                    ncbAccountType.setNcbAccountStatus(accountTypeLevelNCBReport.getNcbAccountStatus());  //80Enum = enum=NCBAccountStatusEnum

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("TMB Bank Flag");
                    attributeType.setStringValue(accountTypeLevelNCBReport.getAttributeType().isTmbBankFlag() + "");    //81//todo Enum YesNOEnum
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("NCB NPL Flag");
                    attributeType.setStringValue(accountTypeLevelNCBReport.getAttributeType().isNcbNPLFlag() + "");     //82//todo Enum YesNOEnum
                    attributeTypeList.add(attributeType);

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("Credit Amount At First NPL Date");
                    attributeType.setNumericValue(accountTypeLevelNCBReport.getCreditAmountFirstNPL()); //83
                    attributeTypeList.add(attributeType);

                    accountType.setTdrFlag(accountTypeLevelNCBReport.isNcbTDRFlag());                   //84

                    ////////////////////////////////////////////////////////////////////////////////////////     //86-88 did not found.
                    accountTypeLevelNCBReport.getCurrentPayment();                                      //86
                    accountTypeLevelNCBReport.getPaymentPattern6M();                                    //87
                    accountTypeLevelNCBReport.getPaymentPattern12M();                                   //88
                    ////////////////////////////////////////////////////////////////////////////////////////

                    accountType.setOverdue31DTo60DCount(accountTypeLevelNCBReport.getOverDue31to60DaysWithinLast12Months()); //89
                    accountType.setOverLimitLast6MthsCount(accountTypeLevelNCBReport.getOverLimitWithinLast6Months());    //90

                    ncbAccountTypeList.add(ncbAccountType);
                }

                ncbEnquiryTypeList = ncbReportType.getNcbEnquiry();
                ncbEnquiryTypesTypeLevelList = ncbReportTypeLevel.getNcbEnquiryTypes();
                for (NCBEnquiryTypeLevel ncbEnquiryTypeLevel : ncbEnquiryTypesTypeLevelList) {
                    attributeTypeList = ncbEnquiryType.getAttribute();

                    ncbEnquiryType.setNumSearchesLast6Mths(ncbEnquiryTypeLevel.getNumberOfSearchInLast6Months());  //91

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("Validity Period Of NCB");
                    attributeType.setNumericValue(ncbEnquiryTypeLevel.getAttributeType().getNumberOfDaysNCBCheck()); //92
                    attributeTypeList.add(attributeType);

                    ncbEnquiryTypeList.add(ncbEnquiryType);
                }

                warningCodeFullMatchedTypeList = borrowerType.getWarningCodeFullMatched();
                warningCodeFullMatchedLevelList = borrowerTypeLevel.getWarningCodeFullMatched();
                for (WarningCodeFullMatched warningCodeFullMatched : warningCodeFullMatchedLevelList) {
                    warningCodeFullMatchedType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.WarningCodeFullMatchedType();
                    warningCodeFullMatchedType.setCode(warningCodeFullMatched.getRiskCodeWithFullyIdentifyMatched());  //93
                    warningCodeFullMatchedTypeList.add(warningCodeFullMatchedType);
                }

                warningCodePartialMatchedTypeList = borrowerType.getWarningCodePartialMatched();
                warningCodePartialMatchedLevelList = borrowerTypeLevel.getWarningCodePartialMatched();
                for (WarningCodePartialMatched warningCodePartialMatched : warningCodePartialMatchedLevelList) {
                    warningCodePartialMatchedType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.WarningCodePartialMatchedType();
                    warningCodePartialMatchedType.setCode(warningCodePartialMatched.getRiskCodeWithSomeIdentifyMatched());  //94
                    warningCodePartialMatchedTypeList.add(warningCodePartialMatchedType);
                }

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Day Overdue Annual Review");
                attributeType.setNumericValue(borrowerTypeLevel.getDayAnnualReviewOverdue());                   //#N/A
                attributeTypeList.add(attributeType);

                borrowerTypeList.add(borrowerType);
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////
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
            ////////////////////////////////////////////////////////////////////////////////////////////////


            businessTypeList = applicationType.getBusiness();
            businessTypeLevelList = applicationTypeLevel.getBusinessType();
            for (BusinessTypeLevel businessTypeLevel : businessTypeLevelList) {
                businessType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.BusinessType();

                attributeTypeList = businessType.getAttribute();

                businessType.setID(businessTypeLevel.getBusinessCodeRunningNumber());  //106
                businessType.setBusinessType(businessTypeLevel.getBusinessCode());      //107

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Negative Flag");
                attributeType.setStringValue(businessTypeLevel.getAttributeType().isNegativeFlag() + "");     //108//todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("High Risk Flag");
                attributeType.setStringValue(businessTypeLevel.getAttributeType().isHighRiskFlag() + "");     //109//todo Enum YesNOEnum
                attributeTypeList.add(attributeType);

                businessTypeList.add(businessType);
            }




            productTypeList = applicationType.getProduct();
            productTypeLevelList = applicationTypeLevel.getProductType();
            for(ProductTypeLevel productTypeLevel : productTypeLevelList) {
                productType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.ProductType();
                productType.setProductType(productTypeLevel.getProductGroup());                                     //110//Enum = SELOSProductGroupEnum

                attributeTypeList = productType.getAttribute();
                attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                attributeType.setName("Validity Period Of NCB");
                attributeType.setNumericValue(new BigDecimal(productTypeLevel.getAttributeType().getMaxCreditLimitByCollateral())); //111
                attributeTypeList.add(attributeType);

                productType.setRequestedCreditLimit(productTypeLevel.getTotalProposedCreditLimit());  //112

                selosProductProgramTypeList = productType.getSelosProductProgram();
                selosProductProgramTypeLevelList = productTypeLevel.getSelosProductProgramTypes();
                for (SELOSProductProgramTypeLevel selosProductProgramTypeLevel : selosProductProgramTypeLevelList) {
                    selosProductProgramType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.SELOSProductProgramType();
                    selosProductProgramType.setName(selosProductProgramTypeLevel.getProductProgram());              //113//Enum = SELOSProductProgramEnum
                    creditFacilityTypeList = selosProductProgramType.getCreditFacility();
                    creditFacilityTypeLevelList = selosProductProgramTypeLevel.getCreditFacilityType();
                    for (CreditFacilityTypeLevel creditFacilityTypeLevel : creditFacilityTypeLevelList) {
                        creditFacilityType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.CreditFacilityType();
                        creditFacilityType.setType(creditFacilityTypeLevel.getCreditFacilityType());                //114//Enum = CreditFacilityTypeEnum
                        creditFacilityType.setCreditLimit(creditFacilityTypeLevel.getCreditLimit());    //115
                        creditFacilityType.setTenor(creditFacilityTypeLevel.getTenor());                //116

                        creditFacilityTypeLevel.getGuaranteeType();                                     //117//Enum = guaranteeTypeEnum  //todo : setGuaranteeType did not found

                        creditFacilityType.setLoanPurpose(creditFacilityTypeLevel.getLoanPurpose());    //118

                        creditFacilityTypeList.add(creditFacilityType);
                    }
                    selosProductProgramTypeList.add(selosProductProgramType);
                }

                collateralTypeList = productType.getCollateral();
                collateralTypeLevelList = productTypeLevel.getCollateralTypes();
                for (CollateralTypeLevel collateralTypeLevel : collateralTypeLevelList) {
                    collateralType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.CollateralType();

                    attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                    attributeType.setName("Collateral Potential");
                    attributeType.setStringValue(collateralTypeLevel.getAttributeType().getCollateralPotential());  //119//Enum = CollateralPotentialEnum
                    attributeTypeList.add(attributeType);

                    collateralType.setCollateralType(collateralTypeLevel.getCollateralType());          //120//Enum = CollateralTypeEnum

                    appraisalTypeList = collateralType.getAppraisal();
                    appraisalTypeLevelList = collateralTypeLevel.getAppraisalType();
                    for (AppraisalTypeLevel appraisalTypeLevel : appraisalTypeLevelList) {

                        appraisalType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AppraisalType();

                        attributeTypeList = appraisalType.getAttribute();

                        attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                        attributeType.setName("Appraisal Flag");
                        attributeType.setStringValue(appraisalTypeLevel.getAttributeType().getAppraisalFlag());  //121//Enum = AppraisalFlagEnum
                        attributeTypeList.add(attributeType);

                        attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                        attributeType.setName("AAD Comment");
                        attributeType.setStringValue(appraisalTypeLevel.getAttributeType().getAadComment());  //122//Enum = AADCommentEnum
                        attributeTypeList.add(attributeType);

                        attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                        attributeType.setName("Percent Appraisal Value Of Building With Ownership Doc");
                        attributeType.setNumericValue(appraisalTypeLevel.getAttributeType().getAppraisalValueOfBuildingWithOwnershipDoc()); //123
                        attributeTypeList.add(attributeType);

                        attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
                        attributeType.setName("Appraisal Length");
                        attributeType.setNumericValue(appraisalTypeLevel.getAttributeType().getLengthOfAppraisal()); //124
                        attributeTypeList.add(attributeType);

                        appraisalTypeList.add(appraisalType);
                    }
                    collateralTypeList.add(collateralType);
                }
                productTypeList.add(productType);
            }

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Max WC Credit Limit");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getMaxWCCreditLimit());               //#N/A
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Total Requested WC Credit Limit");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getTotalRequestedWCCreditLimit());               //#N/A
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Max Core WC Loan Limit");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getMaxCoreWCLoanLimit());               //#N/A
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Total Requested Core WC Loan credit limit");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getTotalRequestedCoreWCLoanCreditLimit());               //#N/A
            attributeTypeList.add(attributeType);

            attributeType = new com.clevel.selos.integration.brms.service.fullapplicationUnderwritingrules.AttributeType();
            attributeType.setName("Total Requested OD credit limit");
            attributeType.setNumericValue(applicationTypeLevel.getAttribute().getTotalRequestedODCreditLimit());               //#N/A
            attributeTypeList.add(attributeType);

            request.getUnderwritingRequest().getUnderwritingApprovalRequest().setApplication(applicationType);
            return request;
        } catch (Exception e) {
            log.error("convertInputModelToRequestModel() Exception : {}", e);
            throw e;
        }
    }

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

    protected String getDecisionID(String applicationNo, String statusCode){

        String decisionID = new StringBuilder("SELOS").append(statusCode == null ? "" : statusCode).append("_").append(simpleDateFormat.format(Calendar.getInstance().getTime())).toString();
        return decisionID;

    }


}
