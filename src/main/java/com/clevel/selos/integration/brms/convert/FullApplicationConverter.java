package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.NCB;
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

public class FullApplicationConverter extends Converter{

    @Inject
    @BRMS
    private Logger logger;

    @Inject
    public FullApplicationConverter(){

    }

    public DecisionServiceRequest getDecisionServiceRequest(BRMSApplicationInfo applicationInfo){
        ApplicationType applicationType = new ApplicationType();

        applicationType.setApplicationNumber(getValueForInterface(applicationInfo.getApplicationNo()));
        try{
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(getValueForInterface(applicationInfo.getProcessDate()));
            applicationType.setDateOfApplication(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        }catch (Exception ex){
            logger.error("Could not transform Date");
        }
        applicationType.setTotalMonthlyIncome(getValueForInterface(applicationInfo.getNetMonthlyIncome()));
        applicationType.setAggregatedCreditExposureLimit(applicationInfo.getFinalGroupExposure());

        //1. Convert Value for Application Level//
        List<AttributeType> attributeTypeList = applicationType.getAttribute();
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.APP_IN_DATE, applicationInfo.getBdmSubmitDate()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXPECTED_SUBMIT_DATE, applicationInfo.getExpectedSubmitDate()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.CUSTOMER_ENTITY, applicationInfo.getBorrowerType()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXISTING_SME_CUSTOMER, applicationInfo.isExistingSMECustomer()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.SAME_SET_OF_BORROWER, applicationInfo.isRequestLoanWithSameName()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFINANCE_IN_FLAG, applicationInfo.isRefinanceIN()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFINANCE_OUT_FLAG, applicationInfo.isRefinanceOUT()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TCG_FLAG, applicationInfo.isRequestTCG()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.AAD_FLAG, applicationInfo.isPassAppraisalProcess()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.STEP, applicationInfo.getStepCode()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_YEAR_FROM_LATEST_FINANCIAL_STATEMENT, applicationInfo.getNumberOfYearFinancialStmt()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NET_WORTH, applicationInfo.getShareHolderRatio()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.FINAL_DBR, applicationInfo.getFinalDBR()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.BORROWER_GROUP_SALE, applicationInfo.getBorrowerGroupIncome()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TOTAL_GROUP_SALE, applicationInfo.getTotalGroupIncome()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.LG_CAPABILITY, applicationInfo.isAbleToGettingGuarantorJob()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NEVER_LG_CLAIM, applicationInfo.isNoClaimLGHistory()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NEVER_ABANDON_PROJECT, applicationInfo.isNoRevokedLicense()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NEVER_PROJECT_DELAY, applicationInfo.isNoLateWorkDelivery()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.SUFFICIENT_SOURCE_OF_FUND, applicationInfo.isAdequateOfCapital()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.PRIME_CUSTOMER, applicationInfo.getCreditCusType()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_TOTAL_FACILITY, applicationInfo.getTotalNumberProposeCredit()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_CONTINGENT_FACILITY, applicationInfo.getTotalNumberContingenPropose()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_EXISTING_OD, applicationInfo.getTotalNumberOfExistingOD()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_REQUEST_OD, applicationInfo.getTotalNumberOfRequestedOD()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_CORE_ASSET, applicationInfo.getTotalNumberOfCoreAsset()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_NON_CORE_ASSET, applicationInfo.getTotalNumberOfNonCoreAsset()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TOTAL_FIX_ASSET_VALUE, applicationInfo.getNetFixAsset()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXISTING_OD_LIMIT, applicationInfo.getTotalExistingODLimit()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TOTAL_COLL_PERCENT_OF_EXPOSURE, applicationInfo.getCollateralPercent()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TOTAL_WC_REQUIREMENT, applicationInfo.getWcNeed()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NET_WC_REQUIREMENT_1_25X, applicationInfo.getCase1WCminLimit()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NET_WC_REQUIREMENT_1_5X, applicationInfo.getCase2WCminLimit()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NET_WC_REQUIREMENT_35_PERCENT_OF_SALES, applicationInfo.getCase3WCminLimit()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXISTING_WC_CREDIT_LIMIT_WITH_TMB, applicationInfo.getTotalWCTMB()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXISTING_CORE_WC_LOAN_CREDIT_LIMIT_WITH_TMB, applicationInfo.getTotalLoanWCTMB()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.BUSINESS_LOCATION, applicationInfo.getBizLocation()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.YEAR_IN_BUSINESS, applicationInfo.getYearInBusinessMonth()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.COUNTRY_OF_BUSINESS, applicationInfo.getCountryOfRegistration()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TRADE_CHEQUE_RETURN_PERCENT, applicationInfo.getTradeChequeReturnPercent()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFERENCE_DOCUMENT_TYPE, applicationInfo.getReferredDocType()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXISTING_GROUP_EXPOSURE, applicationInfo.getExistingGroupExposure()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.BOT_CLASS, applicationInfo.getBotClass()));


        List<ProductType> productTypeList = applicationType.getProduct();
        ProductType productType = new ProductType();
        productType.setProductType(getValueForInterface(applicationInfo.getProductGroup()));
        productType.setRequestedCreditLimit(getValueForInterface(applicationInfo.getTotalApprovedCredit()));
        List<AttributeType> prodAttributeList = productType.getAttribute();
        prodAttributeList.add(getAttributeType(BRMSFieldAttributes.GUARANTEE_TYPE, applicationInfo.getLoanRequestType()));
        prodAttributeList.add(getAttributeType(BRMSFieldAttributes.MAX_CREDIT_LIMIT_BY_COLLATERAL, applicationInfo.getMaximumSMELimit()));

        //2. Convert Value for Collateral Level//
        List<CollateralType> collateralTypeList = productType.getCollateral();
        List<BRMSCollateralInfo> collateralInfoList = applicationInfo.getCollateralInfoList();
        for(BRMSCollateralInfo brmsCollInfo : collateralInfoList){
            CollateralType collateralType = new CollateralType();
            collateralType.setID(getValueForInterface(brmsCollInfo.getCollId()));
            collateralType.setCollateralType(getValueForInterface(brmsCollInfo.getCollateralType()));

            List<AttributeType> collAttributeList = collateralType.getAttribute();
            //Add sub collateral list
            if(brmsCollInfo.getSubCollateralTypeList() != null && brmsCollInfo.getSubCollateralTypeList().size() > 0){
                for(String subColCode : brmsCollInfo.getSubCollateralTypeList()) {
                    collAttributeList.add(getAttributeType(BRMSFieldAttributes.SUB_COLLATERAL_TYPE, subColCode));
                }
            }else{
                collAttributeList.add(getAttributeType(BRMSFieldAttributes.SUB_COLLATERAL_TYPE, ""));
            }
            //collAttributeList.add(getAttributeType(BRMSFieldAttributes.SUB_COLLATERAL_TYPE, brmsCollInfo.getSubCollateralType()));
            collAttributeList.add(getAttributeType(BRMSFieldAttributes.APPRAISAL_FLAG, brmsCollInfo.isAppraisalFlag()));
            collAttributeList.add(getAttributeType(BRMSFieldAttributes.AAD_COMMENT, brmsCollInfo.getAadDecision()));
            collAttributeList.add(getAttributeType(BRMSFieldAttributes.LENGTH_OF_APPRAISAL_MONTHS, brmsCollInfo.getNumberOfMonthsApprDate()));
            collateralTypeList.add(collateralType);
        }

        //3. Convert Value for Product Program - Acc/Requested//
        //Convert the each row of Credit Detail to be:
        // - Production Program - A
        //    - Credit Type A
        //    - Credit Type B
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
            creditFacilityType.setTenor(getValueForInterface(accountRequested.getTenors()));
            creditFacilityType.setLoanPurpose(getValueForInterface(accountRequested.getLoanPurpose()));

            creditFacilityTypeList.add(creditFacilityType);
        }
        if(selosProductProgramType != null){
            selosProductProgramTypeList.add(selosProductProgramType);
        }
        productTypeList.add(productType);

        //4. Convert Value for Business Level//
        List<BusinessType> businessList = applicationType.getBusiness();
        List<BRMSBizInfo> bizInfoList = applicationInfo.getBizInfoList();
        for(BRMSBizInfo brmsBizInfo : bizInfoList){
            BusinessType businessType = new BusinessType();
            businessType.setID(getValueForInterface(brmsBizInfo.getId()));
            businessType.setBusinessType(getValueForInterface(brmsBizInfo.getBizCode()));

            List<AttributeType> bizAttributeLists = businessType.getAttribute();
            bizAttributeLists.add(getAttributeType(BRMSFieldAttributes.NEGATIVE_FLAG, brmsBizInfo.getNegativeValue()));
            bizAttributeLists.add(getAttributeType(BRMSFieldAttributes.HIGH_RISK_FLAG, brmsBizInfo.getHighRiskValue()));
            bizAttributeLists.add(getAttributeType(BRMSFieldAttributes.ESR_FLAG, brmsBizInfo.getEsrValue()));
            bizAttributeLists.add(getAttributeType(BRMSFieldAttributes.SUSPEND_FLAG, brmsBizInfo.getSuspendValue()));
            bizAttributeLists.add(getAttributeType(BRMSFieldAttributes.BUSINESS_DEVIATION_FLAG, brmsBizInfo.getDeviationValue()));
            businessList.add(businessType);
        }

        //5. Convert Bank Account Statement to account statement//
        List<AccountType> accountTypeList = applicationType.getAccount();
        List<BRMSAccountStmtInfo> accountStmtInfoList = applicationInfo.getAccountStmtInfoList();
        for(BRMSAccountStmtInfo accountStmtInfo : accountStmtInfoList){
            AccountType accountType = new AccountType();
            List<AttributeType> accAttributeList = accountType.getAttribute();
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.UTILIZATION_PERCENT, accountStmtInfo.getAvgUtilizationPercent()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.SWING_PERCENT, accountStmtInfo.getAvgSwingPercent()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.AVG_LAST_6_MONTHS_INFLOW_LIMIT, accountStmtInfo.getAvgGrossInflowPerLimit()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_TRANSACTION, accountStmtInfo.getTotalTransaction()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_CHEQUE_RETURN, accountStmtInfo.getCheckReturn()));
            //accAttributeList.add(getAttributeType(BRMSFieldAttributes.TRADE_CHEQUE_RETURN_PERCENT, accountStmtInfo.getCheckReturnPercent()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.OD_OVER_LIMIT_DAYS, accountStmtInfo.getOverLimitDays()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.CASH_INFLOW, accountStmtInfo.getAvgIncomeGross()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.MAIN_ACCOUNT_FLAG, accountStmtInfo.isMainAccount()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.HIGHEST_INFLOW_FLAG, accountStmtInfo.isHighestInflow()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.TMB_ACCOUNT_FLAG, accountStmtInfo.isTmb()));
            accAttributeList.add(getAttributeType(BRMSFieldAttributes.EXCLUDE_INCOME_FLAG, accountStmtInfo.isNotCountIncome()));
            accountTypeList.add(accountType);
        }

        //6. Convert Customer, NCB Account, Obligation Account//
        List<BRMSCustomerInfo> customerInfoList = applicationInfo.getCustomerInfoList();
        List<BorrowerType> borrowerTypeList = applicationType.getBorrower();
        for(BRMSCustomerInfo customerInfo : customerInfoList){
            BorrowerType borrowerType = new BorrowerType();
            borrowerType.setNationality(getValueForInterface(customerInfo.getNationality()));
            borrowerType.setBotClass(getValueForInterface(customerInfo.getAdjustClass()));
            borrowerType.setKycRiskLevel(getValueForInterface(customerInfo.getKycLevel()));

            List<AttributeType> borrowerAttributeList = borrowerType.getAttribute();
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.CUSTOMER_ENTITY, customerInfo.getCustomerEntity()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.EXISTING_SME_CUSTOMER, customerInfo.isExistingSMECustomer()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.RELATIONSHIP_TYPE, customerInfo.getRelation()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.REFERENCE, customerInfo.getReference()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_MONTH_FROM_LAST_SET_UP_DATE, customerInfo.getNumberOfMonthLastContractDate()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.NEW_QUALITATIVE, customerInfo.getQualitativeClass()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.NEXT_REVIEW_DATE, customerInfo.getNextReviewDate()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.NEXT_REVIEW_DATE_FLAG, customerInfo.isNextReviewDateFlag()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.EXTENDED_REVIEW_DATE, customerInfo.getExtendedReviewDate()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.EXTENDED_REVIEW_DATE_FLAG, customerInfo.isExtendedReviewDateFlag()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.RATING_FINAL, customerInfo.getRatingFinal()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.UNPAID_FEE_INSURANCE_PREMIUM, customerInfo.isUnpaidFeeInsurance()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.PENDING_CLAIMED_LG, customerInfo.isPendingClaimLG()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.CREDIT_WORTHINESS, customerInfo.getCreditWorthiness()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.SPOUSE_ID, customerInfo.getSpousePersonalID()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.SPOUSE_RELATIONSHIP_TYPE, customerInfo.getSpouseRelationType()));


            IndividualType individualType = new IndividualType();
            individualType.setCitizenID(getValueForInterface(customerInfo.getPersonalID()));
            individualType.setAge(getValueForInterface(customerInfo.getAgeMonths()));
            individualType.setMaritalStatus(getValueForInterface(customerInfo.getMarriageStatus()));
            borrowerType.setIndividual(individualType);


            //7. Convert Acc/TMB Acc information//
            List<BRMSTMBAccountInfo> brmsTMBAccountInfoList = customerInfo.getTmbAccountInfoList();
            List<AccountType> cusAccountList = borrowerType.getAccount();
            convertTMBAccountInfo(cusAccountList, brmsTMBAccountInfoList);

            //8. Convert NCB Account//
            List<NCBReportType> ncbReportList = borrowerType.getNcbReport();

            NCBReportType ncbReportType = getNCBReportType(customerInfo);
            ncbReportList.add(ncbReportType);

            //Convert Warning Code into Customer.
            List<WarningCodeFullMatchedType> warningCodeFullMatchedTypeList = borrowerType.getWarningCodeFullMatched();
            List<String> csiFullyMatchList = customerInfo.getCsiFullyMatchCode();
            if(csiFullyMatchList != null && csiFullyMatchList.size() > 0) {
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
            if(csiSomeMatchList != null && csiSomeMatchList.size() > 0) {
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

            borrowerTypeList.add(borrowerType);
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
}
