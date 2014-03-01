package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.BRMSField;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.response.UWRulesResponse;
import com.clevel.selos.integration.brms.model.response.UWRulesResult;
import com.clevel.selos.integration.brms.service.prescreenunderwritingrules.*;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.util.*;

public class PrescreenConverter extends Converter{
    @Inject
    @BRMS
    Logger logger;

    @Inject
    public PrescreenConverter(){}

    public DecisionServiceRequest getDecisionServiceRequest(BRMSApplicationInfo applicationInfo){

        ApplicationType applicationType = new ApplicationType();

        applicationType.setApplicationNumber(applicationInfo.getApplicationNo());
        try{
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(applicationInfo.getProcessDate());
            applicationType.setDateOfApplication(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        }catch (Exception ex){
            logger.error("Could not transform Date");
        }

        List<AttributeType> attributeTypeList = applicationType.getAttribute();
        attributeTypeList.add(getAttributeType(BRMSField.APP_IN_DATE, applicationInfo.getBdmSubmitDate()));
        attributeTypeList.add(getAttributeType(BRMSField.EXPECTED_SUBMIT_DATE, applicationInfo.getExpectedSubmitDate()));
        attributeTypeList.add(getAttributeType(BRMSField.CUSTOMER_ENTITY, applicationInfo.getBorrowerType()));
        attributeTypeList.add(getAttributeType(BRMSField.EXISTING_SME_CUSTOMER, applicationInfo.isExistingSMECustomer()));
        attributeTypeList.add(getAttributeType(BRMSField.SAME_SET_OF_BORROWER, applicationInfo.isRequestLoanWithSameName()));
        attributeTypeList.add(getAttributeType(BRMSField.REFINANCE_IN_FLAG, applicationInfo.isRefinanceIN()));
        attributeTypeList.add(getAttributeType(BRMSField.REFINANCE_OUT_FLAG, applicationInfo.isRefinanceOUT()));
        attributeTypeList.add(getAttributeType(BRMSField.STEP, applicationInfo.getStepCode()));
        attributeTypeList.add(getAttributeType(BRMSField.BORROWER_GROUP_SALE, applicationInfo.getBorrowerGroupIncome()));
        attributeTypeList.add(getAttributeType(BRMSField.TOTAL_GROUP_SALE, applicationInfo.getTotalGroupIncome()));
        attributeTypeList.add(getAttributeType(BRMSField.NUM_OF_TOTAL_FACILITY, applicationInfo.getTotalNumberProposeCredit()));
        attributeTypeList.add(getAttributeType(BRMSField.NUM_OF_CONTIGENT_FACILITY, applicationInfo.getTotalNumberContingenPropose()));
        attributeTypeList.add(getAttributeType(BRMSField.BUSINESS_LOCATION, applicationInfo.getBizLocation()));
        attributeTypeList.add(getAttributeType(BRMSField.YEAR_IN_BUSINESS, applicationInfo.getYearInBusinessMonth()));
        attributeTypeList.add(getAttributeType(BRMSField.COUNTRY_OF_REGISTRATION, applicationInfo.getCountryOfRegistration()));
        attributeTypeList.add(getAttributeType(BRMSField.REFERENCE_DOCUMENT_TYPE, applicationInfo.getReferredDocType()));

        List<ProductType> productTypeList =  applicationType.getProduct();
        ProductType productType = new ProductType();
        productType.setProductType(applicationInfo.getProductGroup());
        productTypeList.add(productType);

        List<SELOSProductProgramType> selosProductProgramTypeList = productType.getSelosProductProgram();
        List<BRMSAccountRequested> accountRequestedList = applicationInfo.getAccountRequestedList();

        SELOSProductProgramType selosProductProgramType = new SELOSProductProgramType();
        List<CreditFacilityType> creditFacilityTypeList = null;
        for(BRMSAccountRequested accountRequested: accountRequestedList){
            if(!accountRequested.getProductProgram().equals(selosProductProgramType.getName())){
                if(selosProductProgramType.getName() != null && !"".equals(selosProductProgramType.getName())){
                    selosProductProgramTypeList.add(selosProductProgramType);
                    selosProductProgramType = new SELOSProductProgramType();
                }
                selosProductProgramType.setID(accountRequested.getCreditDetailId());
                selosProductProgramType.setName(accountRequested.getProductProgram());
                creditFacilityTypeList = selosProductProgramType.getCreditFacility();
            }

            CreditFacilityType creditFacilityType = new CreditFacilityType();
            creditFacilityType.setID(accountRequested.getCreditDetailId());
            creditFacilityType.setType(accountRequested.getCreditType());
            creditFacilityType.setCreditLimit(accountRequested.getLimit());

            creditFacilityTypeList.add(creditFacilityType);
        }
        if(selosProductProgramType != null){
            selosProductProgramTypeList.add(selosProductProgramType);
        }

        List<BRMSBizInfo> bizInfoList = applicationInfo.getBizInfoList();
        List<BusinessType> businessTypeList = applicationType.getBusiness();
        for(BRMSBizInfo bizInfo : bizInfoList){
            BusinessType businessType = new BusinessType();
            businessType.setID(bizInfo.getId());
            businessType.setBusinessType(bizInfo.getBizCode());
            List<AttributeType> bizAttributeTypeList = businessType.getAttribute();
            bizAttributeTypeList.add(getAttributeType(BRMSField.NEGATIVE_FLAG, bizInfo.isNegativeFlag()));
            bizAttributeTypeList.add(getAttributeType(BRMSField.HIGH_RISK_FLAG, bizInfo.isHighRiskFlag()));
            bizAttributeTypeList.add(getAttributeType(BRMSField.ESR_FLAG, bizInfo.isEsrFlag()));
            bizAttributeTypeList.add(getAttributeType(BRMSField.SUSPEND_FLAG, bizInfo.isSuspendFlag()));
            bizAttributeTypeList.add(getAttributeType(BRMSField.BUSINESS_DEVIATION_FLAG, bizInfo.isDeviationFlag()));
            businessTypeList.add(businessType);
        }

        List<BRMSCustomerInfo> customerInfoList = applicationInfo.getCustomerInfoList();
        List<BorrowerType> borrowerTypeList = applicationType.getBorrower();
        for(BRMSCustomerInfo customerInfo : customerInfoList){
            BorrowerType borrowerType = new BorrowerType();
            borrowerType.setNationality(customerInfo.getNationality());

            List<AttributeType> cusAttributeTypeList = borrowerType.getAttribute();
            cusAttributeTypeList.add(getAttributeType(BRMSField.CUSTOMER_ENTITY, customerInfo.getCustomerEntity()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.EXISTING_SME_CUSTOMER, customerInfo.isExistingSMECustomer()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.RELATIONSHIP_TYPE, customerInfo.getRelation()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.REFERENCE, customerInfo.getReference()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.NUMBER_OF_MONTH_FROM_LAST_SET_UP_DATE, customerInfo.getNumberOfMonthLastContractDate()));
            //Set P by default at prescreen //
            cusAttributeTypeList.add(getAttributeType(BRMSField.NEW_QUALITATIVE, customerInfo.getQualitativeClass()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.NEXT_REVIEW_DATE, customerInfo.getNextReviewDate()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.NEXT_REVIEW_DATE_FLAG, customerInfo.isNextReviewDateFlag()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.EXTENDED_REVIEW_DATE, customerInfo.getExtendedReviewDate()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.EXTENDED_REVIEW_DATE_FLAG, customerInfo.isExtendedReviewDateFlag()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.RATING_FINAL, customerInfo.getRatingFinal()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.UNPAID_FEE_INSURANCE_PREMIUM, customerInfo.isUnpaidFeeInsurance()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.CLAIMED_LG, customerInfo.isPendingClaimLG()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.SPOUSE_ID, customerInfo.getSpousePersonalID()));
            cusAttributeTypeList.add(getAttributeType(BRMSField.SPOUSE_RELATIONSHIP_TYPE, customerInfo.getSpouseRelationType()));

            if(customerInfo.isIndividual()){
                IndividualType individualType = new IndividualType();
                individualType.setCitizenID(customerInfo.getPersonalID());
                individualType.setAge(customerInfo.getAgeMonths());
                individualType.setMaritalStatus(customerInfo.getMarriageStatus());
                borrowerType.setIndividual(individualType);
            }

            //Set NCB and NCB Equity in Customer Level
            List<NCBReportType> ncbReportTypeList = borrowerType.getNcbReport();
            NCBReportType ncbReportType = new NCBReportType();
            List<AttributeType> ncbAttributeTypeList = ncbReportType.getAttribute();
            ncbAttributeTypeList.add(getAttributeType(BRMSField.NCB_FLAG, customerInfo.isNcbFlag()));

            List<NCBEnquiryType> ncbEnquiryTypeList = ncbReportType.getNcbEnquiry();
            NCBEnquiryType ncbEnquiryType = new NCBEnquiryType();
            ncbEnquiryType.setNumSearchesLast6Mths(customerInfo.getNumberOfNCBCheckIn6Months());
            List<AttributeType> ncbEnAttributeTypeList = ncbEnquiryType.getAttribute();
            ncbEnAttributeTypeList.add(getAttributeType(BRMSField.NUMBER_OF_DAYS_NCB_CHECK, customerInfo.getNumberOfDayLastNCBCheck()));

            List<NCBAccountType> ncbAccountTypeList = ncbReportType.getNcbAccount();
            List<BRMSNCBAccountInfo> ncbAccountInfoList = customerInfo.getNcbAccountInfoList();
            for(BRMSNCBAccountInfo ncbAccountInfo : ncbAccountInfoList){
                NCBAccountType ncbAccountType = new NCBAccountType();
                ncbAccountType.setNcbAccountStatus(ncbAccountInfo.getLoanAccountStatus());
                ncbAccountType.setAccountType(ncbAccountInfo.getLoanAccountType());

                List<AttributeType> ncbAccAttributeList = ncbAccountType.getAttribute();
                ncbAccAttributeList.add(getAttributeType(BRMSField.TMB_BANK_FLAG, ncbAccountInfo.isTmbFlag()));
                ncbAccAttributeList.add(getAttributeType(BRMSField.NCB_NPL_FLAG, ncbAccountInfo.isNplFlag()));
                ncbAccAttributeList.add(getAttributeType(BRMSField.CREDIT_AMOUNT_AT_FIRST_NPL_DATE, ncbAccountInfo.getCreditAmtAtNPLDate()));
                if(customerInfo.isIndividual()){
                    ncbAccAttributeList.add(getAttributeType(BRMSField.CURRENT_PAYMENT_PATTERN_INDV, ncbAccountInfo.getCurrentPaymentType()));
                    ncbAccAttributeList.add(getAttributeType(BRMSField.SIX_MONTHS_PAYMENT_PATTERN_INDV, ncbAccountInfo.getSixMonthPaymentType()));
                    ncbAccAttributeList.add(getAttributeType(BRMSField.TWELVE_MONTHS_PAYMENT_PATTERN_INDV, ncbAccountInfo.getTwelveMonthPaymentType()));
                } else {
                    ncbAccAttributeList.add(getAttributeType(BRMSField.CURRENT_PAYMENT_PATTERN_JURIS, ncbAccountInfo.getCurrentPaymentType()));
                    ncbAccAttributeList.add(getAttributeType(BRMSField.SIX_MONTHS_PAYMENT_PATTERN_JURIS, ncbAccountInfo.getSixMonthPaymentType()));
                    ncbAccAttributeList.add(getAttributeType(BRMSField.TWELVE_MONTHS_PAYMENT_PATTERN_JURIS, ncbAccountInfo.getTwelveMonthPaymentType()));
                }
                ncbAccAttributeList.add(getAttributeType(BRMSField.NUMBER_OF_MONTH_ACCOUNT_CLOSE_DATE, ncbAccountInfo.getAccountCloseDateMonths()));

                ncbAccountType.setTdrFlag(ncbAccountInfo.isTdrFlag());
                ncbAccountType.setOverdue31DTo60DCount(ncbAccountInfo.getNumberOfOverDue());
                ncbAccountType.setOverLimitLast6MthsCount(ncbAccountInfo.getNumberOfOverLimit());

                ncbAccountTypeList.add(ncbAccountType);
            }

            ncbEnquiryTypeList.add(ncbEnquiryType);
            ncbReportTypeList.add(ncbReportType);

            List<WarningCodeFullMatchedType> warningCodeFullMatchedTypeList = borrowerType.getWarningCodeFullMatched();
            List<String> csiFullyMatchList = customerInfo.getCsiFullyMatchCode();
            for(String csiFullyMatchCode : csiFullyMatchList){
                WarningCodeFullMatchedType warningCodeFullMatchedType = new WarningCodeFullMatchedType();
                warningCodeFullMatchedType.setCode(csiFullyMatchCode);
                warningCodeFullMatchedTypeList.add(warningCodeFullMatchedType);
            }

            List<WarningCodePartialMatchedType> warningCodePartialMatchedTypeList = borrowerType.getWarningCodePartialMatched();
            List<String> csiSomeMatchList = customerInfo.getCsiSomeMatchCode();
            for(String csiSomeMatchCode : csiSomeMatchList){
                WarningCodePartialMatchedType warningCodePartialMatchedType = new WarningCodePartialMatchedType();
                warningCodePartialMatchedType.setCode(csiSomeMatchCode);
                warningCodePartialMatchedTypeList.add(warningCodePartialMatchedType);
            }

            borrowerType.setBotClass(customerInfo.getAdjustClass());

            List<AccountType> accountTypeList = borrowerType.getAccount();
            List<BRMSTMBAccountInfo> tmbAccountInfoList = customerInfo.getTmbAccountInfoList();
            for(BRMSTMBAccountInfo tmbAccountInfo : tmbAccountInfoList){
                AccountType accountType = new AccountType();
                List<AttributeType> tmbAccAttributeList = accountType.getAttribute();
                tmbAccAttributeList.add(getAttributeType(BRMSField.ACCOUNT_ACTIVE_FLAG, tmbAccountInfo.isActiveFlag()));
                tmbAccAttributeList.add(getAttributeType(BRMSField.DATA_SOURCE, tmbAccountInfo.getDataSource()));
                tmbAccAttributeList.add(getAttributeType(BRMSField.ACCOUNT_REF, tmbAccountInfo.getAccountRef()));
                tmbAccAttributeList.add(getAttributeType(BRMSField.CUST_TO_ACCOUNT_RELATIONSHIP, tmbAccountInfo.getCustToAccountRelationCD()));
                tmbAccAttributeList.add(getAttributeType(BRMSField.TMB_TDR_FLAG, tmbAccountInfo.isTmbTDRFlag()));
                tmbAccAttributeList.add(getAttributeType(BRMSField.NUMBER_OF_MONTH_PRINCIPAL_AND_INTEREST_PAST_DUE, tmbAccountInfo.getDelPrinIntMonth()));
                tmbAccAttributeList.add(getAttributeType(BRMSField.NUMBER_OF_MONTH_PRINCIPAL_AND_INTEREST_PAST_DUE_OF_TDR_ACCOUNT, tmbAccountInfo.getTmbMonthClass()));
                tmbAccAttributeList.add(getAttributeType(BRMSField.NUMBER_OF_DAY_PRINCIPAL_PAST_DUE, tmbAccountInfo.getTmbDelPriDay()));
                tmbAccAttributeList.add(getAttributeType(BRMSField.NUMBER_OF_DAY_INTEREST_PAST_DUE, tmbAccountInfo.getTmbDelIntDay()));
                tmbAccAttributeList.add(getAttributeType(BRMSField.CARD_BLOCK_CODE, tmbAccountInfo.getTmbBlockCode()));

                accountTypeList.add(accountType);
            }

            borrowerTypeList.add(borrowerType);
        }

        List<AccountType> accountTypeList = applicationType.getAccount();
        List<BRMSAccountStmtInfo> accountStmtInfoList = applicationInfo.getAccountStmtInfoList();
        for(BRMSAccountStmtInfo accountStmtInfo : accountStmtInfoList){
            AccountType accountType = new AccountType();
            List<AttributeType> accAttributeList = accountType.getAttribute();
            accAttributeList.add(getAttributeType(BRMSField.UTILIZATION_PERCENT, accountStmtInfo.getAvgUtilizationPercent()));
            accAttributeList.add(getAttributeType(BRMSField.SWING_PERCENT, accountStmtInfo.getAvgSwingPercent()));
            accAttributeList.add(getAttributeType(BRMSField.AVG_LAST_6_MONTHS_INFLOW_LIMIT, accountStmtInfo.getAvgGrossInflowPerLimit()));
            accAttributeList.add(getAttributeType(BRMSField.NUMBER_OF_TRANSACTION, accountStmtInfo.getTotalTransaction()));
            accAttributeList.add(getAttributeType(BRMSField.MAIN_ACCOUNT_FLAG, accountStmtInfo.isMainAccount()));
            accAttributeList.add(getAttributeType(BRMSField.HIGHEST_INFLOW_FLAG, accountStmtInfo.isHighestInflow()));
            accAttributeList.add(getAttributeType(BRMSField.TMB_ACCOUNT_FLAG, accountStmtInfo.isTmb()));
            accAttributeList.add(getAttributeType(BRMSField.EXCLUDE_INCOME_FLAG, accountStmtInfo.isNotCountIncome()));
            accountTypeList.add(accountType);
        }

        UnderwritingApprovalRequestType underwritingApprovalRequestType = new UnderwritingApprovalRequestType();
        underwritingApprovalRequestType.setApplication(applicationType);

        UnderwritingRequest underwritingRequest = new UnderwritingRequest();
        underwritingRequest.setUnderwritingApprovalRequest(underwritingApprovalRequestType);

        DecisionServiceRequest decisionServiceRequest = new DecisionServiceRequest();
        decisionServiceRequest.setDecisionID(getDecisionID(applicationInfo.getApplicationNo(), applicationInfo.getStatusCode()));
        decisionServiceRequest.setUnderwritingRequest(underwritingRequest);

        return decisionServiceRequest;
    }

    public UWRulesResponse getUWRulesResponse(DecisionServiceResponse decisionServiceResponse){

        UWRulesResponse uwRulesResponse = new UWRulesResponse();

        if(decisionServiceResponse != null){
            uwRulesResponse.setDecisionID(decisionServiceResponse.getDecisionID());

            UnderwritingRequest underwritingRequest = decisionServiceResponse.getUnderwritingRequest();
            UnderwritingApprovalRequestType underwritingApprovalRequestType = underwritingRequest.getUnderwritingApprovalRequest();

            ApplicationType applicationType = underwritingApprovalRequestType.getApplication();
            uwRulesResponse.setApplicationNo(applicationType.getApplicationNumber());

            UnderwritingResult underwritingResult = decisionServiceResponse.getUnderwritingResult();
            UnderwritingApprovalResultType underwritingApprovalResultType = underwritingResult.getUnderwritingApprovalResult();

            Map<String, UWRulesResult> uwRulesResultMap = new TreeMap<String, UWRulesResult>();
            List<ResultType> resultTypeList = underwritingApprovalResultType.getResult();
            for(ResultType resultType : resultTypeList){
                UWRulesResult uwRulesResult = new UWRulesResult();
                uwRulesResult.setRuleName(resultType.getRuleName());
                uwRulesResult.setType(resultType.getType());
                uwRulesResult.setColor(resultType.getColor());
                uwRulesResult.setDeviationFlag(resultType.getDeviationFlag());
                uwRulesResult.setRejectGroupCode(resultType.getRejectGroupCode());

                List<AttributeType> uwAttributeTypeList = resultType.getAttribute();
                for(AttributeType attributeType : uwAttributeTypeList){
                    if(attributeType.getName() != null){
                        if(attributeType.getName().equals(BRMSField.UW_RULE_ORDER.value())){
                            uwRulesResult.setRuleOrder(attributeType.getStringValue());
                        }
                        if(attributeType.getName().equals(BRMSField.UW_PERSONAL_ID)){
                            uwRulesResult.setRuleName(attributeType.getStringValue());
                        }
                    }
                }
                uwRulesResultMap.put(uwRulesResult.getRuleOrder(), uwRulesResult);
            }
            uwRulesResponse.setUwRulesResultMap(uwRulesResultMap);
        }
        return uwRulesResponse;
    }

    private AttributeType getAttributeType(BRMSField field, Date value){
        AttributeType attributeType = new AttributeType();

        try{
            attributeType.setName(field.value());
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(value);
            attributeType.setDateTimeValue(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (Exception ex){
            logger.error("Cannot convert XML");
        }
        return attributeType;
    }

    private AttributeType getAttributeType(BRMSField field, String value){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setStringValue(value);
        return attributeType;
    }

    private AttributeType getAttributeType(BRMSField field, BigDecimal value){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setNumericValue(value);
        return attributeType;
    }

    private AttributeType getAttributeType(BRMSField field, boolean existingSMECustomer){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setBooleanValue(existingSMECustomer);
        return attributeType;
    }
}
