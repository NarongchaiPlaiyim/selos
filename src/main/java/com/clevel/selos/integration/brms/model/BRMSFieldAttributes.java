package com.clevel.selos.integration.brms.model;

public enum BRMSFieldAttributes {
    APP_IN_DATE("App in Date"),
    EXPECTED_SUBMIT_DATE("Expected Submit Date"),
    CUSTOMER_ENTITY("Customer Entity"),
    EXISTING_SME_CUSTOMER("Existing SME Customer Flag"),
    SAME_SET_OF_BORROWER("Set Of Borrower Flag"),
    REFINANCE_IN_FLAG("Refinance In Flag"),
    REFINANCE_OUT_FLAG("Refinance Out Flag"),
    LENDING_REFER_TYPE("Lending Refer Type"),
    BA_FLAG("BA Flag"),
    TOP_UP_BA_FLAG("Top Up BA Flag"),
    TCG_FLAG("TCG Flag"),
    AAD_FLAG("AAD Flag"),
    STEP("Step"),
    NUM_OF_YEAR_FROM_LATEST_FINANCIAL_STATEMENT("Number Of Year From Latest Financial Statement"),
    NET_WORTH("Net Worth"),
    FINAL_DBR("Final DBR"),
    BORROWER_GROUP_SALE("Borrower Group Sale"),
    TOTAL_GROUP_SALE("Related Juristic Group Sale"),
    EXISTING_GROUP_EXPOSURE("Existing Group Exposure"),
    LG_CAPABILITY("LG Capability Flag"),
    NEVER_LG_CLAIM("Never LG Claim Flag"),
    NEVER_ABANDON_PROJECT("Never Abandon Project Flag"),
    NEVER_PROJECT_DELAY("Never Project Delay Flag"),
    SUFFICIENT_SOURCE_OF_FUND("Sufficient Source Of Fund Flag"),
    PRIME_CUSTOMER("Prime Customer"),
    NUM_OF_TOTAL_FACILITY("Num Of Total Facility"),
    NUM_OF_CONTINGENT_FACILITY("Num Of Contingent Facility"),
    NUM_OF_EXISTING_OD("Number Of Existing OD"),
    NUM_OF_REQUEST_OD("Number Of Requested OD"),
    NUM_OF_CORE_ASSET("Num Of Core Asset"),
    NUM_OF_NON_CORE_ASSET("Num Of Non Core Asset"),
    TOTAL_FIX_ASSET_VALUE("Total Fix Asset Value"),
    EXISTING_OD_LIMIT("Existing OD limit"),
    TOTAL_COLL_PERCENT_OF_EXPOSURE("Total Collateral Precent Of Exposure"),
    TOTAL_WC_REQUIREMENT("Total W/C Requirement"),
    NET_WC_REQUIREMENT_1_25X("Net W/C Requirement (1.25x)"),
    NET_WC_REQUIREMENT_1_5X("Net W/C Requirement (1.5x)"),
    NET_WC_REQUIREMENT_35_PERCENT_OF_SALES("Net W/C Requirement (35% of Sales)"),
    EXISTING_WC_CREDIT_LIMIT_WITH_TMB("Existing W/C Credit Limit With TMB"),
    EXISTING_CORE_WC_LOAN_CREDIT_LIMIT_WITH_TMB("Existing Core W/C Loan Credit Limit With TMB"),
    BUSINESS_LOCATION("Business Location"),
    YEAR_IN_BUSINESS("Year In Business"),
    COUNTRY_OF_BUSINESS("Country Of Business"),
    TRADE_CHEQUE_RETURN_PERCENT("Trade Cheque Return Percent"),
    RELATIONSHIP_TYPE("Relationship Type"),
    REFERENCE("Reference"),
    NUM_OF_MONTH_FROM_LAST_SET_UP_DATE("Number Of Month From Last Set Up Date"),
    NEW_QUALITATIVE("New Qualitative"),
    NEXT_REVIEW_DATE("Next Review Date"),
    NEXT_REVIEW_DATE_FLAG("Next Review Date Flag"),
    EXTENDED_REVIEW_DATE("Extended Review Date"),
    EXTENDED_REVIEW_DATE_FLAG("Extended Review Date Flag"),
    RATING_FINAL("Rating Final"),
    UNPAID_FEE_INSURANCE_PREMIUM("Unpaid Fee Flag"),
    PENDING_CLAIMED_LG("Claimed LG Flag"),
    CREDIT_WORTHINESS("Credit Worthiness Flag"),
    SPOUSE_ID("Spouse ID"),
    SPOUSE_RELATIONSHIP_TYPE("Spouse Borrower Type"),
    ACCOUNT_ACTIVE_FLAG("Account Active Flag"),
    DATA_SOURCE("Data Source"),
    ACCOUNT_REFERENCE("Account Reference"),
    CUST_TO_ACCOUNT_RELATIONSHIP("Cust To Account Relationship"),
    TMB_TDR_FLAG("TMB TDR Flag"),
    NUM_OF_MONTH_PRINCIPAL_AND_INTEREST_PAST_DUE("Number Of Month Principal And Interest Past Due"),
    NUM_OF_MONTH_PRINCIPAL_AND_INTEREST_PAST_DUE_OF_TDR_ACCOUNT("Number Of Month Principal And Interest Past Due Of TDR Account"),
    NUM_OF_DAY_PRINCIPAL_PAST_DUE("Number Of Day Principal Past Due"),
    NUM_OF_DAY_INTEREST_PAST_DUE("Number Of Day Interest Past Due"),
    CARD_BLOCK_CODE("Card Block Code"),
    NCB_FLAG("NCB Flag"),
    TMB_BANK_FLAG("TMB Bank Flag"),
    NCB_NPL_FLAG("NCB NPL Flag"),
    NCB_TDR_FLAG("NCB TDR Flag"),
    CREDIT_AMOUNT_AT_FIRST_NPL_DATE("Credit Amount At First NPL Date"),
    CURRENT_PAYMENT_PATTERN_INDV("Current Payment Pattern (Individual)"),
    CURRENT_PAYMENT_PATTERN_JURIS("Current Payment Pattern (Juristic)"),
    SIX_MONTHS_PAYMENT_PATTERN_INDV("6M Payment Pattern (Individual)"),
    SIX_MONTHS_PAYMENT_PATTERN_JURIS("6M Payment Pattern (Juristic)"),
    TWELVE_MONTHS_PAYMENT_PATTERN_INDV("12M Payment Pattern (Individual)"),
    TWELVE_MONTHS_PAYMENT_PATTERN_JURIS("12M Payment Pattern (Juristic)"),
    NUM_OF_MONTH_ACCOUNT_CLOSE_DATE("Number Of Month From Account Close Date"),
    NUM_OF_DAYS_NCB_CHECK("Validity Period Of NCB"),
    UTILIZATION_PERCENT("Utilization Percent"),
    SWING_PERCENT("Swing Percent"),
    AVG_LAST_6_MONTHS_INFLOW_LIMIT("Avg Balance L6 Mths"),
    NUM_OF_TRANSACTION("Number Of Transaction"),
    NUM_OF_CHEQUE_RETURN("Number Of Cheque Return"),
    OD_OVER_LIMIT_DAYS("OD Over Limit (Days)"),
    CASH_INFLOW("Cash Inflow"),
    MAIN_ACCOUNT_FLAG("Main Account Flag"),
    HIGHEST_INFLOW_FLAG("Highest Inflow Flag"),
    TMB_ACCOUNT_FLAG("TMB Account Flag"),
    EXCLUDE_INCOME_FLAG("Exclude Income Flag"),
    NEGATIVE_FLAG("Negative Flag"),
    HIGH_RISK_FLAG("High Risk Flag"),
    ESR_FLAG("ESR Flag"),
    SUSPEND_FLAG("Suspend Flag"),
    BUSINESS_DEVIATION_FLAG("Business Deviation Flag"),
    MAX_CREDIT_LIMIT_BY_COLLATERAL("Max Credit Limit By Collateral"),
    GUARANTEE_TYPE("Guarantee Type"),
    SUB_COLLATERAL_TYPE("Sub Collateral Type"),
    APPRAISAL_FLAG("Appraisal Flag"),
    AAD_COMMENT("AAD Comment"),
    PERCENT_APPRAISAL_VALUE_OF_BUILDING_WITH_OWNERSHIP_DOC("Percent Appraisal Value Of Building With Ownership Doc"),
    LENGTH_OF_APPRAISAL_MONTHS("Appraisal Length"),
    REFERENCE_DOCUMENT_TYPE("Reference Document Type"),
    TOTAL_TCG_GUARANTEE_AMOUNT("Total TCG Guarantee Amount"),
    NUM_OF_INDV_GUARANTOR("Number Of Individual Guarantor"),
    NUM_OF_JURIS_GUARANTOR("Number Of Juristic Guarantor"),
    TOTAL_MORTGAGE_VALUE("Total Mortgage Value"),
    NUM_OF_REDEEM_TRANSACTION("Number Of Redeem Transaction"),
    DAY_ANNUAL_REVIEW_OVERDUE("Day Overdue Annual Review"),
    //Only for Output, not sure where to use it.
    MAX_WC_CREDIT_LIMIT("Max WC Credit Limit"),
    TOTAL_REQUESTED_WC_CREDIT_LIMIT("Total Requested WC Credit Limit"),
    MAX_CORE_WC_LOAN_LIMIT("Max Core WC Loan Limit"),
    TOTAL_REQUESTED_CORE_WC_LOAN_CREDIT_LIMIT("Total Requested Core WC Loan Credit Limit"),
    TOTAL_REQUESTED_OD_CREDIT_LIMIT("Total Requested OD Credit Limit"),
    NET_WC_REQUIREMENT_TMB("Net W/C Requirement TMB"),

    //Additional Response Attributes for Pricing
    PRICE_MAXIMUM_RATE("Maximum Rate"),
    PRICE_FEE_PERCENT("Fee Percent"),
    PRICE_FEE_PERCENT_AFT_DISCOUNT("Fee Percent After Discount"),
    PRICE_PAYMENT_METHOD("Payment Method"),
    PRICE_YEAR("Year"),

    //Additional Response Attributes for UW Rules set
    UW_RULE_ORDER("Order"),
    UW_PERSONAL_ID("Borrower_Id"),
    //Additional Response Attributes for Mandate Doc Services.
    DOCUMENT_GROUP("Document Group"),
    CONDITION("Condition"),
    SHOW_FLAG("Show Flag"),
    OPER_MANDATORY_Flag("Oper Mandatory Flag"),
    OPER_STEP("Oper Step"),
    OPER_SHOW_FLAG("Oper Show Flag"),

    BOT_CLASS("botClass");

    String value;

    BRMSFieldAttributes(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
