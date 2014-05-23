package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.BRMSFieldAttributes;
import com.clevel.selos.integration.brms.model.request.*;
import com.ilog.rules.decisionservice.DecisionServiceRequest;
import com.ilog.rules.param.UnderwritingRequest;
import com.tmbbank.enterprise.model.*;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;
import java.util.List;

public class PrescreenConverter extends Converter{
    @Inject
    @BRMS
    Logger logger;

    @Inject
    public PrescreenConverter(){}

    public DecisionServiceRequest getDecisionServiceRequest(BRMSApplicationInfo applicationInfo){
        logger.debug("-- start convert getDecisionServiceRequest from BRMSApplicationInfo");
        ApplicationType applicationType = new ApplicationType();

        applicationType.setApplicationNumber(getValueForInterface(applicationInfo.getApplicationNo()));
        try{
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(getValueForInterface(applicationInfo.getProcessDate()));
            applicationType.setDateOfApplication(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        }catch (Exception ex){
            logger.error("Could not transform Date");
        }

        List<AttributeType> attributeTypeList = applicationType.getAttribute();
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.APP_IN_DATE, applicationInfo.getBdmSubmitDate()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXPECTED_SUBMIT_DATE, applicationInfo.getExpectedSubmitDate()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.CUSTOMER_ENTITY, applicationInfo.getBorrowerType()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXISTING_SME_CUSTOMER, applicationInfo.isExistingSMECustomer()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.SAME_SET_OF_BORROWER, applicationInfo.isRequestLoanWithSameName()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFINANCE_IN_FLAG, applicationInfo.isRefinanceIN()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFINANCE_OUT_FLAG, applicationInfo.isRefinanceOUT()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.STEP, applicationInfo.getStepCode()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.BORROWER_GROUP_SALE, applicationInfo.getBorrowerGroupIncome()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TOTAL_GROUP_SALE, applicationInfo.getTotalGroupIncome()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_TOTAL_FACILITY, applicationInfo.getTotalNumberProposeCredit()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_CONTINGENT_FACILITY, applicationInfo.getTotalNumberContingenPropose()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.BUSINESS_LOCATION, applicationInfo.getBizLocation()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.YEAR_IN_BUSINESS, applicationInfo.getYearInBusinessMonth()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.COUNTRY_OF_BUSINESS, applicationInfo.getCountryOfRegistration()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFERENCE_DOCUMENT_TYPE, applicationInfo.getReferredDocType()));

        List<ProductType> productTypeList =  applicationType.getProduct();
        ProductType productType = new ProductType();
        productType.setProductType(getValueForInterface(applicationInfo.getProductGroup()));
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
                selosProductProgramType.setID(getValueForInterface(accountRequested.getCreditDetailId()));
                selosProductProgramType.setName(getValueForInterface(accountRequested.getProductProgram()));
                creditFacilityTypeList = selosProductProgramType.getCreditFacility();
            }

            CreditFacilityType creditFacilityType = new CreditFacilityType();
            creditFacilityType.setID(getValueForInterface(accountRequested.getCreditDetailId()));
            creditFacilityType.setType(getValueForInterface(accountRequested.getCreditType()));
            creditFacilityType.setCreditLimit(getValueForInterface(accountRequested.getLimit()));

            creditFacilityTypeList.add(creditFacilityType);
        }
        if(selosProductProgramType != null){
            selosProductProgramTypeList.add(selosProductProgramType);
        }

        List<BRMSBizInfo> bizInfoList = applicationInfo.getBizInfoList();
        List<BusinessType> businessTypeList = applicationType.getBusiness();
        for(BRMSBizInfo bizInfo : bizInfoList){
            BusinessType businessType = new BusinessType();
            businessType.setID(getValueForInterface(bizInfo.getId()));
            businessType.setBusinessType(getValueForInterface(bizInfo.getBizCode()));
            List<AttributeType> bizAttributeTypeList = businessType.getAttribute();
            bizAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.NEGATIVE_FLAG, bizInfo.getNegativeValue()));
            bizAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.HIGH_RISK_FLAG, bizInfo.getHighRiskValue()));
            bizAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.ESR_FLAG, bizInfo.getEsrValue()));
            bizAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.SUSPEND_FLAG, bizInfo.getSuspendValue()));
            bizAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.BUSINESS_DEVIATION_FLAG, bizInfo.getDeviationValue()));
            businessTypeList.add(businessType);
        }

        List<BRMSCustomerInfo> customerInfoList = applicationInfo.getCustomerInfoList();
        List<BorrowerType> borrowerTypeList = applicationType.getBorrower();
        for(BRMSCustomerInfo customerInfo : customerInfoList){
            BorrowerType borrowerType = new BorrowerType();
            borrowerType.setNationality(getValueForInterface(customerInfo.getNationality()));

            List<AttributeType> cusAttributeTypeList = borrowerType.getAttribute();
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.CUSTOMER_ENTITY, customerInfo.getCustomerEntity()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXISTING_SME_CUSTOMER, customerInfo.isExistingSMECustomer()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.RELATIONSHIP_TYPE, customerInfo.getRelation()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFERENCE, customerInfo.getReference()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_MONTH_FROM_LAST_SET_UP_DATE, customerInfo.getNumberOfMonthLastContractDate()));
            //Set P by default at prescreen //
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.NEW_QUALITATIVE, customerInfo.getQualitativeClass()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.NEXT_REVIEW_DATE, customerInfo.getNextReviewDate()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.NEXT_REVIEW_DATE_FLAG, customerInfo.isNextReviewDateFlag()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXTENDED_REVIEW_DATE, customerInfo.getExtendedReviewDate()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXTENDED_REVIEW_DATE_FLAG, customerInfo.isExtendedReviewDateFlag()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.RATING_FINAL, customerInfo.getRatingFinal()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.UNPAID_FEE_INSURANCE_PREMIUM, customerInfo.isUnpaidFeeInsurance()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.PENDING_CLAIMED_LG, customerInfo.isPendingClaimLG()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.SPOUSE_ID, customerInfo.getSpousePersonalID()));
            cusAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.SPOUSE_RELATIONSHIP_TYPE, customerInfo.getSpouseRelationType()));

            IndividualType individualType = new IndividualType();
            individualType.setCitizenID(getValueForInterface(customerInfo.getPersonalID()));
            individualType.setAge(getValueForInterface(customerInfo.getAgeMonths()));
            individualType.setMaritalStatus(getValueForInterface(customerInfo.getMarriageStatus()));
            borrowerType.setIndividual(individualType);

            //Set NCB and NCB Equity in Customer Level
            List<NCBReportType> ncbReportTypeList = borrowerType.getNcbReport();
            NCBReportType ncbReportType = new NCBReportType();
            List<AttributeType> ncbAttributeTypeList = ncbReportType.getAttribute();
            ncbAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.NCB_FLAG, customerInfo.isNcbFlag()));

            List<NCBAccountType> ncbAccountTypeList = ncbReportType.getNcbAccount();
            List<BRMSNCBAccountInfo> ncbAccountInfoList = customerInfo.getNcbAccountInfoList();
            //TODO CHECK WITH BRMS
            if(customerInfo.isNcbFlag()){
                for(BRMSNCBAccountInfo ncbAccountInfo : ncbAccountInfoList){
                    NCBAccountType ncbAccountType = new NCBAccountType();
                    ncbAccountType.setNcbAccountStatus(getValueForInterface(ncbAccountInfo.getLoanAccountStatus()));
                    ncbAccountType.setAccountType(getValueForInterface(ncbAccountInfo.getLoanAccountType()));
                    ncbAccountType.setTdrFlag(getValueForInterface(ncbAccountInfo.isTdrFlag()));
                    ncbAccountType.setOverdue31DTo60DCount(getValueForInterface(ncbAccountInfo.getNumberOfOverDue()));
                    ncbAccountType.setOverLimitLast6MthsCount(getValueForInterface(ncbAccountInfo.getNumberOfOverLimit()));

                    List<AttributeType> ncbAccAttributeList = ncbAccountType.getAttribute();
                    ncbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.TMB_BANK_FLAG, ncbAccountInfo.isTmbFlag()));
                    ncbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NCB_NPL_FLAG, ncbAccountInfo.isNplFlag()));
                    ncbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.CREDIT_AMOUNT_AT_FIRST_NPL_DATE, ncbAccountInfo.getCreditAmtAtNPLDate()));
                    if(customerInfo.isIndividual()){
                        ncbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.CURRENT_PAYMENT_PATTERN_INDV, ncbAccountInfo.getCurrentPaymentType()));
                        ncbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.SIX_MONTHS_PAYMENT_PATTERN_INDV, ncbAccountInfo.getSixMonthPaymentType()));
                        ncbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.TWELVE_MONTHS_PAYMENT_PATTERN_INDV, ncbAccountInfo.getTwelveMonthPaymentType()));

                    } else {
                        ncbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.CURRENT_PAYMENT_PATTERN_JURIS, ncbAccountInfo.getCurrentPaymentType()));
                        ncbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.SIX_MONTHS_PAYMENT_PATTERN_JURIS, ncbAccountInfo.getSixMonthPaymentType()));
                        ncbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.TWELVE_MONTHS_PAYMENT_PATTERN_JURIS, ncbAccountInfo.getTwelveMonthPaymentType()));
                    }
                    ncbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_MONTH_ACCOUNT_CLOSE_DATE, ncbAccountInfo.getAccountCloseDateMonths()));

                    ncbAccountTypeList.add(ncbAccountType);
                }
            }

            List<NCBEnquiryType> ncbEnquiryTypeList = ncbReportType.getNcbEnquiry();
            NCBEnquiryType ncbEnquiryType = new NCBEnquiryType();
            ncbEnquiryType.setNumSearchesLast6Mths(getValueForInterface(customerInfo.getNumberOfNCBCheckIn6Months()));
            List<AttributeType> ncbEnAttributeTypeList = ncbEnquiryType.getAttribute();
            ncbEnAttributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_DAYS_NCB_CHECK, customerInfo.getNumberOfDayLastNCBCheck()));

            ncbEnquiryTypeList.add(ncbEnquiryType);
            ncbReportTypeList.add(ncbReportType);

            //Convert Warning Code into Customer.
            List<WarningCodeFullMatchedType> warningCodeFullMatchedTypeList = borrowerType.getWarningCodeFullMatched();
            List<String> csiFullyMatchList = customerInfo.getCsiFullyMatchCode();
            int csiFullyMatchSize = 0;
            if(csiFullyMatchList != null && csiFullyMatchList.size() > 0) {
                csiFullyMatchSize = csiFullyMatchList.size();
                for (String csiFullyMatchCode : csiFullyMatchList) {
                    WarningCodeFullMatchedType warningCodeFullMatchedType = new WarningCodeFullMatchedType();
                    warningCodeFullMatchedType.setCode(getValueForInterface(csiFullyMatchCode));
                    warningCodeFullMatchedTypeList.add(warningCodeFullMatchedType);
                }
            } else {
                WarningCodeFullMatchedType warningCodeFullMatchedType = new WarningCodeFullMatchedType();
                warningCodeFullMatchedType.setCode("");
                warningCodeFullMatchedTypeList.add(warningCodeFullMatchedType);
            }

            List<WarningCodePartialMatchedType> warningCodePartialMatchedTypeList = borrowerType.getWarningCodePartialMatched();
            List<String> csiSomeMatchList = customerInfo.getCsiSomeMatchCode();
            if(csiSomeMatchList != null && csiSomeMatchList.size() > 0 && csiFullyMatchSize == 0) {
                for (String csiSomeMatchCode : csiSomeMatchList) {
                    WarningCodePartialMatchedType warningCodePartialMatchedType = new WarningCodePartialMatchedType();
                    warningCodePartialMatchedType.setCode(getValueForInterface(csiSomeMatchCode));
                    warningCodePartialMatchedTypeList.add(warningCodePartialMatchedType);
                }
            } else {
                WarningCodePartialMatchedType warningCodePartialMatchedType = new WarningCodePartialMatchedType();
                warningCodePartialMatchedType.setCode("");
                warningCodePartialMatchedTypeList.add(warningCodePartialMatchedType);
            }

            borrowerType.setBotClass(getValueForInterface(customerInfo.getAdjustClass()));

            List<AccountType> accountTypeList = borrowerType.getAccount();
            List<BRMSTMBAccountInfo> tmbAccountInfoList = customerInfo.getTmbAccountInfoList();
            //TODO Check condition with BRMS Sheet...
            if(customerInfo.isNcbFlag()){
                for(BRMSTMBAccountInfo tmbAccountInfo : tmbAccountInfoList){
                    AccountType accountType = new AccountType();
                    List<AttributeType> tmbAccAttributeList = accountType.getAttribute();
                    tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.ACCOUNT_ACTIVE_FLAG, tmbAccountInfo.isActiveFlag()));
                    tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.DATA_SOURCE, tmbAccountInfo.getDataSource()));
                    tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.ACCOUNT_REFERENCE, tmbAccountInfo.getAccountRef()));
                    tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.CUST_TO_ACCOUNT_RELATIONSHIP, tmbAccountInfo.getCustToAccountRelationCD()));
                    tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.TMB_TDR_FLAG, tmbAccountInfo.isTmbTDRFlag()));
                    tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_MONTH_PRINCIPAL_AND_INTEREST_PAST_DUE, tmbAccountInfo.getNumMonthIntPastDue()));
                    tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_MONTH_PRINCIPAL_AND_INTEREST_PAST_DUE_OF_TDR_ACCOUNT, tmbAccountInfo.getNumMonthIntPastDueTDRAcc()));
                    tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_DAY_PRINCIPAL_PAST_DUE, tmbAccountInfo.getTmbDelPriDay()));
                    tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_DAY_INTEREST_PAST_DUE, tmbAccountInfo.getTmbDelIntDay()));
                    tmbAccAttributeList.add(getAttributeType(BRMSFieldAttributes.CARD_BLOCK_CODE, tmbAccountInfo.getTmbBlockCode()));

                    accountTypeList.add(accountType);
                }
            }

            borrowerTypeList.add(borrowerType);
        }

        List<AccountType> accountTypeList = applicationType.getAccount();
        //TODO Check condition with BRMS sheet...
        List<BRMSAccountStmtInfo> accountStmtInfoList = applicationInfo.getAccountStmtInfoList();
        for(BRMSAccountStmtInfo accountStmtInfo : accountStmtInfoList){
            AccountType accountType = new AccountType();
            List<AttributeType> accAttributeList = accountType.getAttribute();
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.UTILIZATION_PERCENT, accountStmtInfo.getAvgUtilizationPercent()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.SWING_PERCENT, accountStmtInfo.getAvgSwingPercent()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.AVG_LAST_6_MONTHS_INFLOW_LIMIT, accountStmtInfo.getAvgGrossInflowPerLimit()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_TRANSACTION, accountStmtInfo.getTotalTransaction()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.MAIN_ACCOUNT_FLAG, accountStmtInfo.isMainAccount()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.HIGHEST_INFLOW_FLAG, accountStmtInfo.isHighestInflow()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.TMB_ACCOUNT_FLAG, accountStmtInfo.isTmb()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.EXCLUDE_INCOME_FLAG, accountStmtInfo.isNotCountIncome()));
            accountTypeList.add(accountType);
        }

        UnderwritingApprovalRequestType underwritingApprovalRequestType = new UnderwritingApprovalRequestType();
        underwritingApprovalRequestType.setApplication(applicationType);

        UnderwritingRequest underwritingRequest = new UnderwritingRequest();
        underwritingRequest.setUnderwritingApprovalRequest(underwritingApprovalRequestType);

        DecisionServiceRequest decisionServiceRequest = new DecisionServiceRequest();
        decisionServiceRequest.setDecisionID(getDecisionID(applicationInfo.getApplicationNo(), applicationInfo.getStatusCode()));
        decisionServiceRequest.setUnderwritingRequest(underwritingRequest);

        logger.debug("-- end convert getDecisionServiceRequest from BRMSApplicationInfo return {}", decisionServiceRequest);
        return decisionServiceRequest;
    }
}
