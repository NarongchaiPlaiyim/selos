DROP SEQUENCE SEQ_APP_NUMBER;

CREATE SEQUENCE SEQ_APP_NUMBER
START WITH 0
MAXVALUE 999999999999999999999999999
MINVALUE 0
NOCYCLE
NOCACHE
NOORDER;

COMMIT;

CREATE OR REPLACE PACKAGE BODY SLOS.SLOS AS

  FUNCTION getApplicationNumber(segmentCode IN VARCHAR2) RETURN VARCHAR2 IS
    appNumber VARCHAR2(5);
  BEGIN
    select TO_CHAR(SEQ_APP_NUMBER.nextval,'0000') into appNumber from dual;
    RETURN segmentCode || TO_CHAR(sysdate, 'yyyymmdd') || trim(appNumber);
  END;

  procedure resetSeq(p_seq_name in varchar2)
    is
        l_val number;
    begin
        execute immediate 'select ' || p_seq_name || '.nextval from dual' INTO l_val;
        execute immediate 'alter sequence ' || p_seq_name || ' increment by -' || l_val || ' minvalue 0';
        execute immediate 'select ' || p_seq_name || '.nextval from dual' INTO l_val;
        execute immediate 'alter sequence ' || p_seq_name || ' increment by 1 minvalue 0';
    end;
    
  PROCEDURE createWorkCase(vWorkCasePreScreenId IN WRK_CASE_PRESCREEN.ID%TYPE) IS
    vWorkCaseId             NUMBER;
    vCustomerId_Old         WRK_CUSTOMER.ID%TYPE;
    vCustomerId_New         NUMBER;
    vCustomerEntity         WRK_CUSTOMER.CUSTOMERENTITY_ID%TYPE;
    vNCBId_Old              WRK_NCB.ID%TYPE;
    vNCBId_New              NUMBER;
    vBankSumId_Old          WRK_BANKSTATEMENT_SUMMARY.ID%TYPE;
    vBankSumId_New          NUMBER;
    vBankId_Old             WRK_BANKSTATEMENT.ID%TYPE;
    vBankId_New             NUMBER;
    vCa_Number              VARCHAR(35);
    vBorrower_Type_Id       NUMBER;
    vBorrowingType_Id       NUMBER;
    vRef_App_Number         VARCHAR(35);
    vBasicInfo_Id           NUMBER;
    vCreateBy               VARCHAR(10);
    vProductGroup_Id        NUMBER;
    vQualitative_Type       NUMBER;
    vExpectedSubmit         DATE;
    vTcgRequired            NUMBER;
    vCustomerOblId_New      NUMBER;
    vCustomerOblId_Old      WRK_CUSTOMER.CUSTOMER_OBL_INFO_ID%TYPE;

    BEGIN
    
        SELECT SEQ_WRK_CASE_ID.NEXTVAL INTO vWorkCaseId FROM DUAL;
        
        -- INSERT TO WORKCASE
        INSERT INTO WRK_CASE (
            ID, 
            APP_NUMBER, 
            CASE_LOCK, 
            LOCK_USER, 
            WOB_NUMBER,
            CREATE_DATE,  
            CREATE_BY, 
            MODIFY_DATE,
            MODIFY_BY, 
            STATUS_ID, 
            STEP_ID, 
            STEP_OWNER, 
            WORKCASEPRESCREEN_ID)
        SELECT 
            vWorkCaseId, 
            APP_NUMBER, 
            CASE_LOCK, 
            LOCK_USER,         
            WOB_NUMBER, 
            CREATE_DATE, 
            CREATE_BY,
            MODIFY_DATE, 
            MODIFY_BY, 
            STATUS_ID, 
            STEP_ID,  
            STEP_OWNER,
            ID
        FROM WRK_CASE_PRESCREEN
        WHERE ID = vWorkCasePreScreenId;
        
        SELECT SEQ_WRK_BASIC_INFO_ID.NEXTVAL INTO vBasicInfo_Id FROM DUAL;
        
        SELECT
            BORROWING_TYPE_ID, EXPECTED_SUBMIT_DATE, PRODUCT_GROUP_ID, CREATE_USER_ID, TCG
        INTO vBorrowingType_Id, vExpectedSubmit, vProductGroup_Id, vCreateBy, vTcgRequired
        FROM WRK_PRESCREEN
        WHERE WORKCASE_PRESCREEN_ID = vWorkCasePreScreenId; 
        
        SELECT
            BORROWER_TYPE_ID, REF_APP_NUMBER, CA_NUMBER, CASE WHEN BORROWER_TYPE_ID = 1 THEN 2 ELSE 1 END AS QUALITATIVE  
        INTO vBorrower_Type_Id, vRef_App_Number, vCa_Number, vQualitative_Type 
        FROM WRK_CASE_PRESCREEN 
        WHERE ID = vWorkCasePreScreenId;     
        
        -- INSERT TO BASICINFO
        INSERT INTO WRK_BASICINFO (
            ID,
            BORROWER_TYPE_ID,
            BORROWING_TYPE_ID,
            CA_NUMBER,
            CREATE_DATE,
            CREATE_USER_ID,
            PRODUCT_GROUP_ID,
            QUALITATIVE_TYPE,
            REF_APP_NUMBER,
            WORKCASE_ID)
        VALUES(
            vBasicInfo_Id,
            vBorrower_Type_Id,
            vBorrowingType_Id,
            vCa_Number,
            SYSDATE,
            vCreateBy,
            vProductGroup_Id,
            vQualitative_Type,
            vRef_App_Number,
            vWorkCaseId
        );
        
        INSERT INTO WRK_TCG (
            ID, 
            ACTIVE, 
            CREATE_DATE, 
            CREATE_USER_ID,
            COLLATERAL_RULE_RESULT,
            EXIST_LOAN_UNDER_SAME,
            EXIST_LOAN_NOT_UNDER_SAME,
            REQUEST_LIMIT_TCG,
            REQUEST_LIMIT_NOT_TCG,
            REQUEST_TCG_AMOUNT,
            TCB_FLOOD_AMOUNT,
            SUM_APPRAISAL_AMOUNT,
            SUM_LTV_VALUE,
            SUM_IN_THIS_APPRAISAL_AMOUNT,
            SUM_IN_THIS_LTV_VALUE,
            TCG_FLAG, 
            WORKCASE_ID) 
        VALUES ( 
            SEQ_WRK_TCG_ID.NEXTVAL,
            1,
            SYSDATE,
            vCreateBy,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            vTcgRequired,
            vWorkCaseId
        );
            
    
        -- LOOP FOR INSERT CUSTOMER, INDIVIDUAL, JURISTIC
        FOR CUSTOMER_CURSOR IN ( SELECT * FROM WRK_CUSTOMER WHERE WORKCASE_PRESCREEN_ID = vWorkCasePreScreenId ORDER BY ID ASC )
        LOOP
            vCustomerId_Old := CUSTOMER_CURSOR.ID;
            vCustomerEntity := CUSTOMER_CURSOR.CUSTOMERENTITY_ID;
            vCustomerOblId_Old := CUSTOMER_CURSOR.CUSTOMER_OBL_INFO_ID;
            
            INSERT INTO ADT_USER_ACTIVITY(ID, ACTION, ACTION_DATE, RESULT)
            VALUES ( SEQ_ADT_USER_ID.NEXTVAL, 'CLOSE SALE', SYSDATE, 'BEFORE CUST_OBLIGATION - CUST OBL ID : ' || vCustomerOblId_Old);
                SELECT SEQ_WRK_CUS_OBL_ID.NEXTVAL INTO vCustomerOblId_New FROM DUAL;
            
            IF vCustomerOblId_Old IS NOT NULL THEN
            
            INSERT INTO ADT_USER_ACTIVITY(ID, ACTION, ACTION_DATE, RESULT)
            VALUES ( SEQ_ADT_USER_ID.NEXTVAL, 'CLOSE SALE', SYSDATE, 'INSERT CUST_OBLIGATION');
                SELECT SEQ_WRK_CUS_OBL_ID.NEXTVAL INTO vCustomerOblId_New FROM DUAL;
                
                INSERT INTO WRK_CUSTOMER_OBL_INFO (
                   ID, 
                   ADJUST_CLASS, 
                   EXISTING_SME_CUSTOMER, 
                   EXTENDED_REVIEW_DATE, 
                   EXTENDED_REVIEW_DATE_FLAG, 
                   LAST_CONTRACT_DATE, 
                   LAST_REVIEW_DATE, 
                   NEXT_REVIEW_DATE, 
                   NEXT_REVIEW_DATE_FLAG, 
                   MONTHS_LAST_CONTRACT_DATE, 
                   PENDING_CLAIM_LG, 
                   UNPAID_FEE_INSURANCE, 
                   RATING_FINAL, 
                   SERVICE_SEGMENT_ID) 
                SELECT 
                   vCustomerOblId_New,
                   ADJUST_CLASS, 
                   EXISTING_SME_CUSTOMER, 
                   EXTENDED_REVIEW_DATE, 
                   EXTENDED_REVIEW_DATE_FLAG, 
                   LAST_CONTRACT_DATE, 
                   LAST_REVIEW_DATE, 
                   NEXT_REVIEW_DATE, 
                   NEXT_REVIEW_DATE_FLAG, 
                   MONTHS_LAST_CONTRACT_DATE, 
                   PENDING_CLAIM_LG, 
                   UNPAID_FEE_INSURANCE, 
                   RATING_FINAL, 
                   SERVICE_SEGMENT_ID
                FROM WRK_CUSTOMER_OBL_INFO
                WHERE ID = vCustomerOblId_Old;    
            ELSE
                vCustomerOblId_New := NULL; 
                INSERT INTO ADT_USER_ACTIVITY(ID, ACTION, ACTION_DATE, RESULT)
            VALUES ( SEQ_ADT_USER_ID.NEXTVAL, 'CLOSE SALE', SYSDATE, 'NOT INSERT INSERT CUST_OBLIGATION');           
            END IF;
            
            
            SELECT SEQ_WRK_CUSTOMER_ID.NEXTVAL INTO vCustomerId_New FROM DUAL;
            
            --INSERT TO CUSTOMER
            INSERT INTO WRK_CUSTOMER (
                AGE, 
                AGE_MONTHS, 
                APPROX_INCOME, 
                BUSINESSTYPE_ID, 
                COLLATERAL_OWNER, 
                COUNTRY_INCOME, 
                COVENANT_FLAG, 
                CSI_CHECKED, 
                CUSTOMERENTITY_ID, 
                CUSTOMER_OBL_INFO_ID, 
                DOCUMENTTYPE_ID, 
                DOCUMENT_AUTHORIZE_BY, 
                DOCUMENT_AUTHORIZE_DATE, 
                DOCUMENT_EXPIRE_DATE, 
                EMAIL, 
                FAX_NUMBER, 
                ID, 
                IS_COMMITTEE, 
                IS_SEARCH_RM, 
                IS_SPOUSE, 
                JURISTIC_ID, 
                KYCLEVEL_ID, 
                KYC_REASON, 
                LASTNAME_EN, 
                LASTNAME_TH, 
                MAILING_ADDRESS_ID, 
                MOBILE_NUMBER, 
                NAME_EN, 
                NAME_TH, 
                NCB_CHECKED, 
                PERCENT_SHARE, 
                REASON, 
                REFERENCE_ID, 
                RELATION_ID, 
                REVIEW_FLAG, 
                SEARCH_BY, 
                SEARCH_ID, 
                SHARES, 
                SOURCE_INCOME, 
                SPOUSE_ID, 
                TITLE_ID, 
                TMB_CUSTOMER_ID, 
                WORKCASE_ID, 
                WORKCASE_PRESCREEN_ID, 
                WORTHINESS) 
            VALUES ( 
                CUSTOMER_CURSOR.AGE, 
                CUSTOMER_CURSOR.AGE_MONTHS, 
                CUSTOMER_CURSOR.APPROX_INCOME, 
                CUSTOMER_CURSOR.BUSINESSTYPE_ID, 
                CUSTOMER_CURSOR.COLLATERAL_OWNER, 
                CUSTOMER_CURSOR.COUNTRY_INCOME, 
                CUSTOMER_CURSOR.COVENANT_FLAG, 
                CUSTOMER_CURSOR.CSI_CHECKED, 
                CUSTOMER_CURSOR.CUSTOMERENTITY_ID, 
                vCustomerOblId_New, 
                CUSTOMER_CURSOR.DOCUMENTTYPE_ID, 
                CUSTOMER_CURSOR.DOCUMENT_AUTHORIZE_BY, 
                CUSTOMER_CURSOR.DOCUMENT_AUTHORIZE_DATE, 
                CUSTOMER_CURSOR.DOCUMENT_EXPIRE_DATE, 
                CUSTOMER_CURSOR.EMAIL, 
                CUSTOMER_CURSOR.FAX_NUMBER, 
                vCustomerId_New, 
                CUSTOMER_CURSOR.IS_COMMITTEE, 
                CUSTOMER_CURSOR.IS_SEARCH_RM, 
                CUSTOMER_CURSOR.IS_SPOUSE, 
                CUSTOMER_CURSOR.JURISTIC_ID, 
                CUSTOMER_CURSOR.KYCLEVEL_ID, 
                CUSTOMER_CURSOR.KYC_REASON, 
                CUSTOMER_CURSOR.LASTNAME_EN, 
                CUSTOMER_CURSOR.LASTNAME_TH, 
                CUSTOMER_CURSOR.MAILING_ADDRESS_ID, 
                CUSTOMER_CURSOR.MOBILE_NUMBER, 
                CUSTOMER_CURSOR.NAME_EN, 
                CUSTOMER_CURSOR.NAME_TH, 
                CUSTOMER_CURSOR.NCB_CHECKED, 
                CUSTOMER_CURSOR.PERCENT_SHARE, 
                CUSTOMER_CURSOR.REASON, 
                CUSTOMER_CURSOR.REFERENCE_ID, 
                CUSTOMER_CURSOR.RELATION_ID, 
                CUSTOMER_CURSOR.REVIEW_FLAG, 
                CUSTOMER_CURSOR.SEARCH_BY, 
                CUSTOMER_CURSOR.SEARCH_ID, 
                CUSTOMER_CURSOR.SHARES, 
                CUSTOMER_CURSOR.SOURCE_INCOME, 
                CUSTOMER_CURSOR.SPOUSE_ID, 
                CUSTOMER_CURSOR.TITLE_ID, 
                CUSTOMER_CURSOR.TMB_CUSTOMER_ID, 
                vWorkCaseId, 
                NULL, 
                CUSTOMER_CURSOR.WORTHINESS );

                   
            IF vCustomerEntity = '1' THEN
                --INSERT TO INDIVIDUAL
                INSERT INTO WRK_INDIVIDUAL (
                    ID, 
                    BIRTH_DATE, 
                    CITIZEN_ID, 
                    GENDER, 
                    NUM_OF_CHILDREN, 
                    CITIZEN_COUNTRY_ID, 
                    CUSTOMER_ID, 
                    EDUCATION_ID, 
                    MARITALSTATUS_ID, 
                    NATIONALITY_ID, 
                    OCCUPATION_ID, 
                    RACE_ID, 
                    SND_NATIONALITY_ID) 
                SELECT 
                    SEQ_WRK_INDIVIDUAL_ID.NEXTVAL, 
                    BIRTH_DATE, 
                    CITIZEN_ID, 
                    GENDER, 
                    NUM_OF_CHILDREN, 
                    CITIZEN_COUNTRY_ID, 
                    vCustomerId_New, 
                    EDUCATION_ID, 
                    MARITALSTATUS_ID, 
                    NATIONALITY_ID, 
                    OCCUPATION_ID, RACE_ID, 
                    SND_NATIONALITY_ID
                FROM WRK_INDIVIDUAL
                WHERE CUSTOMER_ID = vCustomerId_Old;
            ELSIF vCustomerEntity = '2' THEN
                --INSERT TO JURISTIC
                INSERT INTO WRK_JURISTIC (
                    ID, 
                    CAPITAL, 
                    CONTACT_NAME, 
                    DOCUMENT_ISSUE_DATE, 
                    FINANCIAL_YEAR, 
                    NUMBER_AUTHORIZED_USERS, 
                    PAID_CAPITAL, 
                    REGISTER_DATE, 
                    REGISTRATION_ID, 
                    SALES_FINANCIAL_STMT, 
                    SHARE_HOLDER_RATIO, 
                    SIGN_SONDITION, 
                    TOTAL_SHARE, 
                    CUSTOMER_ID)
                SELECT 
                    SEQ_WRK_JURISTIC_ID.NEXTVAL, 
                    CAPITAL, 
                    CONTACT_NAME, 
                    DOCUMENT_ISSUE_DATE, 
                    FINANCIAL_YEAR, 
                    NUMBER_AUTHORIZED_USERS, 
                    PAID_CAPITAL, 
                    REGISTER_DATE, 
                    REGISTRATION_ID, 
                    SALES_FINANCIAL_STMT, 
                    SHARE_HOLDER_RATIO, 
                    SIGN_SONDITION, 
                    TOTAL_SHARE, 
                    vCustomerId_New
                FROM WRK_JURISTIC
                WHERE CUSTOMER_ID = vCustomerId_Old;                        
            END IF;
            
            --INSERT TO ADDRESS
            INSERT INTO WRK_ADDRESS (
                ID, 
                ADDRESS, 
                ADDRESS_NO, 
                ADDRESS_TYPE_FLAG, 
                BUILDING, 
                CONTACT_NAME, 
                CONTACT_PHONE, 
                PHONE_EXTENSION, 
                MOO, 
                PHONE_NUMBER, 
                POSTAL_CODE, 
                ROAD, 
                ADDRESS_TYPE_ID, 
                COUNTRY_ID, 
                CUSTOMER_ID, 
                DISTRICT_ID, 
                PROVINCE_ID, 
                SUBDISTRICT_ID) 
            SELECT 
                SEQ_WRK_ADDRESS_ID.NEXTVAL, 
                ADDRESS, 
                ADDRESS_NO, 
                ADDRESS_TYPE_FLAG, 
                BUILDING, 
                CONTACT_NAME, 
                CONTACT_PHONE, 
                PHONE_EXTENSION, 
                MOO, 
                PHONE_NUMBER, 
                POSTAL_CODE, 
                ROAD, 
                ADDRESS_TYPE_ID, 
                COUNTRY_ID, 
                vCustomerId_New, 
                DISTRICT_ID, 
                PROVINCE_ID, 
                SUBDISTRICT_ID
            FROM SLOS.WRK_ADDRESS
            WHERE CUSTOMER_ID = vCustomerId_Old;
                     
            --INSERT TO CUSTOMER_ACCOUNT
            INSERT INTO WRK_CUSTOMER_ACCOUNT (
                ID, 
                ID_NUMBER, 
                CUSTOMER_ID, 
                DOCUMENT_TYPE_ID ) 
            SELECT 
                SEQ_WRK_CUST_ACC_ID.NEXTVAL,
                ID_NUMBER,
                vCustomerId_New,
                DOCUMENT_TYPE_ID
            FROM WRK_CUSTOMER_ACCOUNT
            WHERE CUSTOMER_ID = vCustomerId_Old; 
              
            --INSERT TO CUSTOMER_ACCOUNT_NAME
            INSERT INTO WRK_CUSTOMER_ACCOUNT_NAME (
                ID, 
                NAME_EN, 
                NAME_TH, 
                SURNAME_EN, 
                SURNAME_TH, 
                CUSTOMER_ID ) 
            SELECT 
                SEQ_WRK_CUST_ACC_NAME_ID.NEXTVAL,
                NAME_EN,
                NAME_TH,
                SURNAME_EN,
                SURNAME_TH,
                vCustomerId_New
            FROM WRK_CUSTOMER_ACCOUNT_NAME
            WHERE CUSTOMER_ID = vCustomerId_Old;
              
            INSERT INTO WRK_CUSTOMER_CSI (
                ID, 
                MATCHED_TYPE, 
                WARNING_DATE, 
                CUSTOMER_ID, 
                WARNING_ID ) 
            SELECT
                SEQ_WRK_CUST_CSI_ID.NEXTVAL,
                MATCHED_TYPE,
                WARNING_DATE,
                vCustomerId_New,
                WARNING_ID
            FROM WRK_CUSTOMER_CSI 
            WHERE CUSTOMER_ID = vCustomerId_Old;             
            
            FOR NCB_CURSOR IN ( SELECT * FROM WRK_NCB WHERE CUSTOMER_ID = vCustomerId_Old )
            LOOP
                vNCBId_Old := NCB_CURSOR.ID;
                
                SELECT SEQ_WRK_NCB_ID.NEXTVAL INTO vNCBId_New FROM DUAL;
                
                --INSERT TO NCB
                INSERT INTO WRK_NCB (
                    ID, 
                    ACTIVE, 
                    CHECK_IN_6_MONTH, 
                    CHECKING_DATE, 
                    CREATE_DATE, 
                    CURRENT_PAYMENT_TYPE, 
                    ENQUIRY, 
                    HISTORY_PAYMENT_TYPE, 
                    MODIFY_DATE, 
                    NCB_CUS_ADDRESS, 
                    CUS_MARRIAGE_STATUS, 
                    NCB_CUS_NAME, 
                    LAST_INFO_AS_OF_DATE, 
                    NPL_FLAG, 
                    NPL_OTHER_FLAG, 
                    NPL_OTHER_MONTH, 
                    NPL_OTHER_YEAR, 
                    NPL_TMB_FLAG, 
                    NPL_TMB_MONTH, 
                    NPL_TMB_YEAR, 
                    PAYMENT_CLASS, 
                    PERSONAL_ID, 
                    REMARK, 
                    TDR_FLAG, 
                    TDR_OTHER_FLAG, 
                    TDR_OTHER_MONTH, 
                    TDR_OTHER_YEAR, 
                    TDR_TMB_FLAG, 
                    TDR_TMB_MONTH, 
                    TDR_TMB_YEAR, 
                    CREATE_USER_ID, 
                    CUSTOMER_ID, 
                    MODIFY_USER_ID, 
                    TDR_CONDITION_ID)
                SELECT 
                    vNCBId_New, 
                    NCB_CURSOR.ACTIVE, 
                    NCB_CURSOR.CHECK_IN_6_MONTH, 
                    NCB_CURSOR.CHECKING_DATE, 
                    NCB_CURSOR.CREATE_DATE, 
                    NCB_CURSOR.CURRENT_PAYMENT_TYPE, 
                    NCB_CURSOR.ENQUIRY, 
                    NCB_CURSOR.HISTORY_PAYMENT_TYPE, 
                    NCB_CURSOR.MODIFY_DATE, 
                    NCB_CURSOR.NCB_CUS_ADDRESS, 
                    NCB_CURSOR.CUS_MARRIAGE_STATUS, 
                    NCB_CURSOR.NCB_CUS_NAME, 
                    NCB_CURSOR.LAST_INFO_AS_OF_DATE, 
                    NCB_CURSOR.NPL_FLAG, 
                    NCB_CURSOR.NPL_OTHER_FLAG, 
                    NCB_CURSOR.NPL_OTHER_MONTH, 
                    NCB_CURSOR.NPL_OTHER_YEAR, 
                    NCB_CURSOR.NPL_TMB_FLAG, 
                    NCB_CURSOR.NPL_TMB_MONTH, 
                    NCB_CURSOR.NPL_TMB_YEAR, 
                    NCB_CURSOR.PAYMENT_CLASS, 
                    NCB_CURSOR.PERSONAL_ID, 
                    NCB_CURSOR.REMARK, 
                    NCB_CURSOR.TDR_FLAG, 
                    NCB_CURSOR.TDR_OTHER_FLAG, 
                    NCB_CURSOR.TDR_OTHER_MONTH, 
                    NCB_CURSOR.TDR_OTHER_YEAR, 
                    NCB_CURSOR.TDR_TMB_FLAG, 
                    NCB_CURSOR.TDR_TMB_MONTH, 
                    NCB_CURSOR.TDR_TMB_YEAR, 
                    NCB_CURSOR.CREATE_USER_ID, 
                    vCustomerId_New, 
                    NCB_CURSOR.MODIFY_USER_ID, 
                    TDR_CONDITION_ID
                FROM WRK_NCB
                WHERE ID = vNCBId_Old; 

                --INSERT TO NCB DETAIL
                INSERT INTO WRK_NCB_DETAIL (
                    ID, 
                    ACCOUNT_OPEN_DATE, 
                    ACCOUNT_TMB_FLAG, 
                    AS_OF_DATE, 
                    INSTALLMENT, 
                    LAST_RESTRUCTURE_DATE, 
                    LIMIT, 
                    MONTH1, 
                    MONTH2, 
                    MONTH3, 
                    MONTH4, 
                    MONTH5, 
                    MONTH6, 
                    NO_OF_MONTHS_PAYMENT, 
                    OUTSTANDING, 
                    OUTSTANDING_IN_12_MONTH, 
                    OVER_LIMIT, 
                    REFINANCE_FLAG, 
                    WCFLAG, 
                    ACCOUNT_STATUS_ID, 
                    ACCOUNT_TYPE_ID, 
                    CURRENT_PAYMENT_ID, 
                    HISTORY_PAYMENT_ID, 
                    NCB_ID, 
                    CAN_EDIT
                ) SELECT
                    SEQ_WRK_NCB_DETAIL_ID.NEXTVAL, 
                    ACCOUNT_OPEN_DATE, 
                    ACCOUNT_TMB_FLAG, 
                    AS_OF_DATE, 
                    INSTALLMENT, 
                    LAST_RESTRUCTURE_DATE, 
                    LIMIT, 
                    MONTH1, 
                    MONTH2, 
                    MONTH3, 
                    MONTH4, 
                    MONTH5, 
                    MONTH6, 
                    NO_OF_MONTHS_PAYMENT, 
                    OUTSTANDING, 
                    OUTSTANDING_IN_12_MONTH, 
                    OVER_LIMIT, 
                    REFINANCE_FLAG, 
                    WCFLAG, 
                    ACCOUNT_STATUS_ID, 
                    ACCOUNT_TYPE_ID, 
                    CURRENT_PAYMENT_ID, 
                    HISTORY_PAYMENT_ID, 
                    vNCBId_New, 
                    CAN_EDIT
                  FROM WRK_NCB_DETAIL 
                  WHERE NCB_ID = vNCBId_Old;
                     
            END LOOP;  
            
            
        END LOOP;
     
               
        --UPDATE SPOUSE ID
        UPDATE WRK_CUSTOMER CUS 
        SET SPOUSE_ID = 
        (
            SELECT ID_NEW
            FROM (
                    SELECT CUSTOMER.ID, IND.CITIZEN_ID 
                    FROM WRK_CUSTOMER CUSTOMER
                        LEFT OUTER JOIN WRK_INDIVIDUAL IND ON CUSTOMER.ID = IND.CUSTOMER_ID
                    WHERE CUSTOMER.ID IN (
                        SELECT DISTINCT SPOUSE_ID
                        FROM WRK_CUSTOMER 
                        WHERE SPOUSE_ID IN (SELECT ID FROM WRK_CUSTOMER WHERE WORKCASE_PRESCREEN_ID = vWorkCasePreScreenId)
                    )
                  ) T1 INNER JOIN
                  (
                    SELECT CUSTOMER.ID AS ID_NEW, IND.CITIZEN_ID AS CITIZEN_ID_NEW
                    FROM WRK_CUSTOMER CUSTOMER
                        LEFT OUTER JOIN WRK_INDIVIDUAL IND ON CUSTOMER.ID = IND.CUSTOMER_ID
                    WHERE IND.CITIZEN_ID IN (
                            SELECT INDV.CITIZEN_ID
                            FROM WRK_CUSTOMER CUST 
                                LEFT OUTER JOIN WRK_INDIVIDUAL INDV ON CUST.ID = INDV.CUSTOMER_ID
                            WHERE CUST.ID IN (
                                SELECT DISTINCT SPOUSE_ID
                                FROM WRK_CUSTOMER
                                WHERE SPOUSE_ID IN (
                                    SELECT ID 
                                    FROM WRK_CUSTOMER
                                    WHERE WORKCASE_PRESCREEN_ID = vWorkCasePreScreenId )
                                )
                          ) AND WORKCASE_ID = vWorkCaseId
                  ) T2 ON
                    T1.CITIZEN_ID = T2.CITIZEN_ID_NEW
            WHERE CUS.SPOUSE_ID = T1.ID
        ) WHERE CUS.WORKCASE_ID = vWorkCaseId AND SPOUSE_ID <> 0;
        
        --TODO UPDATE SUB CUSTOMER (COMMITTEE)
        
        --LOOP FOR INSERT BANKSTATEMENT
        FOR BANK_STMT_SUM_CURSOR IN ( SELECT * FROM WRK_BANKSTATEMENT_SUMMARY WHERE WORKCASE_PRESCREEN_ID = vWorkCasePreScreenId)
        LOOP
        
            vBankSumId_Old := BANK_STMT_SUM_CURSOR.ID;
            SELECT SEQ_WRK_BANKSTMT_SUM_ID.NEXTVAL INTO vBankSumId_New FROM DUAL;
        
            INSERT INTO WRK_BANKSTATEMENT_SUMMARY (
                ID, 
                TMB_TOTAL_INCOME_GROSS, 
                TMB_TOTAL_INCOME_BDM, 
                TMB_TOTAL_INCOME_UW, 
                COUNT_REFRESH, 
                CREATE_DATE, 
                EXPECTED_SUBMIT_DATE, 
                GRAND_AVG_OS_BALANCE_AMT, 
                GRAND_TOTAL_BRW_INCOME_GROSS, 
                GRAND_TOTAL_BRW_INCOME_BDM, 
                GRAND_TOTAL_BRW_INCOME_UW, 
                GRAND_TOTAL_INCOME_GROSS, 
                GRAND_TOTAL_INCOME_BDM, 
                GRAND_TOTAL_INCOME_UW, 
                GRAND_TD_CHQ_RETURN_AMT, 
                GRAND_TD_CHQ_RETURN_PERCENT, 
                MODIFY_DATE, 
                OTHER_TOTAL_INCOME_GROSS, 
                OTHER_TOTAL_INCOME_BDM, 
                OTHER_TOTAL_INCOME_UW, 
                SEASONAL_FLAG, 
                CREATE_USER_ID, 
                MODIFY_USER_ID, 
                WORKCASE_ID, 
                WORKCASE_PRESCREEN_ID)
            VALUES (
                vBankSumId_New, 
                BANK_STMT_SUM_CURSOR.TMB_TOTAL_INCOME_GROSS, 
                BANK_STMT_SUM_CURSOR.TMB_TOTAL_INCOME_BDM, 
                BANK_STMT_SUM_CURSOR.TMB_TOTAL_INCOME_UW, 
                BANK_STMT_SUM_CURSOR.COUNT_REFRESH, 
                BANK_STMT_SUM_CURSOR.CREATE_DATE, 
                vExpectedSubmit, 
                BANK_STMT_SUM_CURSOR.GRAND_AVG_OS_BALANCE_AMT, 
                BANK_STMT_SUM_CURSOR.GRAND_TOTAL_BRW_INCOME_GROSS, 
                BANK_STMT_SUM_CURSOR.GRAND_TOTAL_BRW_INCOME_BDM, 
                BANK_STMT_SUM_CURSOR.GRAND_TOTAL_BRW_INCOME_UW, 
                BANK_STMT_SUM_CURSOR.GRAND_TOTAL_INCOME_GROSS, 
                BANK_STMT_SUM_CURSOR.GRAND_TOTAL_INCOME_BDM, 
                BANK_STMT_SUM_CURSOR.GRAND_TOTAL_INCOME_UW, 
                BANK_STMT_SUM_CURSOR.GRAND_TD_CHQ_RETURN_AMT, 
                BANK_STMT_SUM_CURSOR.GRAND_TD_CHQ_RETURN_PERCENT, 
                BANK_STMT_SUM_CURSOR.MODIFY_DATE, 
                BANK_STMT_SUM_CURSOR.OTHER_TOTAL_INCOME_GROSS, 
                BANK_STMT_SUM_CURSOR.OTHER_TOTAL_INCOME_BDM, 
                BANK_STMT_SUM_CURSOR.OTHER_TOTAL_INCOME_UW, 
                BANK_STMT_SUM_CURSOR.SEASONAL_FLAG, 
                BANK_STMT_SUM_CURSOR.CREATE_USER_ID, 
                BANK_STMT_SUM_CURSOR.MODIFY_USER_ID, 
                vWorkCaseId, 
                NULL );
            
            FOR BANK_STMT_CURSOR IN ( SELECT * FROM WRK_BANKSTATEMENT WHERE BANK_STMT_SUMMARY_ID = vBankSumId_Old )
            LOOP
            
                vBankId_Old := BANK_STMT_CURSOR.ID;
                SELECT SEQ_WRK_BANKSTMT_ID.NEXTVAL INTO vBankId_New FROM DUAL;
                
                INSERT INTO WRK_BANKSTATEMENT (
                    ID, 
                    ACCOUNT_CHARACTER, 
                    ACCOUNT_NAME, 
                    ACCOUNT_NUMBER, 
                    AVG_WITHDRAW_AMOUNT, 
                    AVG_GROSS_INFLOW_PER_LIMIT, 
                    AVG_INCOME_GROSS, 
                    AVG_INCOME_BDM, 
                    AVG_INCOME_UW, 
                    LIMIT, 
                    AVG_OS_BALANCE_AMOUNT, 
                    AVG_SWING_PERCENT, 
                    AVG_UTILIZATION_PERCENT, 
                    BRANCH_NAME, 
                    TD_CHQ_RETURN_TIMES, 
                    CREATE_DATE, 
                    IS_HIGHEST_INFLOW, 
                    IS_TMB, 
                    MAIN_ACCOUNT, 
                    MODIFY_DATE, 
                    OTHER_ACCOUNT_TYPE, 
                    OVER_LIMIT_DAYS, 
                    OVER_LIMIT_TIMES, 
                    REMARK, 
                    TD_CHQ_RETURN_AMOUNT, 
                    TD_CHQ_RETURN_PERCENT, 
                    ACCOUNT_STATUS_ID, 
                    BANK_ID, 
                    BANK_ACCOUNT_TYPE_ID, 
                    BANK_STMT_SUMMARY_ID, 
                    CREATE_USER_ID, 
                    MODIFY_USER_ID)
                VALUES (
                    vBankId_New, 
                    BANK_STMT_CURSOR.ACCOUNT_CHARACTER, 
                    BANK_STMT_CURSOR.ACCOUNT_NAME, 
                    BANK_STMT_CURSOR.ACCOUNT_NUMBER, 
                    BANK_STMT_CURSOR.AVG_WITHDRAW_AMOUNT, 
                    BANK_STMT_CURSOR.AVG_GROSS_INFLOW_PER_LIMIT, 
                    BANK_STMT_CURSOR.AVG_INCOME_GROSS, 
                    BANK_STMT_CURSOR.AVG_INCOME_BDM, 
                    BANK_STMT_CURSOR.AVG_INCOME_UW, 
                    BANK_STMT_CURSOR.LIMIT, 
                    BANK_STMT_CURSOR.AVG_OS_BALANCE_AMOUNT, 
                    BANK_STMT_CURSOR.AVG_SWING_PERCENT, 
                    BANK_STMT_CURSOR.AVG_UTILIZATION_PERCENT, 
                    BANK_STMT_CURSOR.BRANCH_NAME, 
                    BANK_STMT_CURSOR.TD_CHQ_RETURN_TIMES, 
                    BANK_STMT_CURSOR.CREATE_DATE, 
                    BANK_STMT_CURSOR.IS_HIGHEST_INFLOW, 
                    BANK_STMT_CURSOR.IS_TMB, 
                    BANK_STMT_CURSOR.MAIN_ACCOUNT, 
                    BANK_STMT_CURSOR.MODIFY_DATE, 
                    BANK_STMT_CURSOR.OTHER_ACCOUNT_TYPE, 
                    BANK_STMT_CURSOR.OVER_LIMIT_DAYS, 
                    BANK_STMT_CURSOR.OVER_LIMIT_TIMES, 
                    BANK_STMT_CURSOR.REMARK, 
                    BANK_STMT_CURSOR.TD_CHQ_RETURN_AMOUNT, 
                    BANK_STMT_CURSOR.TD_CHQ_RETURN_PERCENT, 
                    BANK_STMT_CURSOR.ACCOUNT_STATUS_ID, 
                    BANK_STMT_CURSOR.BANK_ID, 
                    BANK_STMT_CURSOR.BANK_ACCOUNT_TYPE_ID, 
                    BANK_STMT_CURSOR.BANK_STMT_SUMMARY_ID, 
                    BANK_STMT_CURSOR.CREATE_USER_ID, 
                    BANK_STMT_CURSOR.MODIFY_USER_ID);
                    
                INSERT INTO WRK_BANKSTATEMENT_DETAIL (
                    ID, 
                    AS_OF_DATE, 
                    CHEQUE_RETURN_AMOUNT, 
                    CREDIT_AMOUNT_BDM, 
                    CREDIT_AMOUNT_UW, 
                    DATE_OF_MAX_BALANCE, 
                    DATE_OF_MIN_BALANCE, 
                    DEBIT_AMOUNT, 
                    EXCLUDE_LIST_BDM, 
                    EXCLUDE_LIST_UW, 
                    GROSS_CREDIT_BALANCE, 
                    GROSS_INFLOW_PER_LIMIT, 
                    MAX_BALANCE, 
                    MIN_BALANCE, 
                    MONTH_BALANCE, 
                    NUMBER_OF_CHQ_RETURN, 
                    NUMBER_OF_CREDIT_TXN, 
                    NUMBER_OF_DEBIT_TXN, 
                    OVER_LIMIT_AMOUNT, 
                    OVER_LIMIT_DAYS, 
                    OVER_LIMIT_TIMES, 
                    SWING_PERCENT, 
                    TIMES_OF_AVG_CREDIT_BDM, 
                    TIMES_OF_AVG_CREDIT_UW, 
                    TOTAL_TRANSACTION, 
                    UTILIZATION_PERCENT, 
                    BANK_STMT_ID)
                SELECT 
                    SEQ_WRK_BANKSTMT_DETAIL_ID.NEXTVAL, 
                    AS_OF_DATE, 
                    CHEQUE_RETURN_AMOUNT, 
                    CREDIT_AMOUNT_BDM, 
                    CREDIT_AMOUNT_UW, 
                    DATE_OF_MAX_BALANCE, 
                    DATE_OF_MIN_BALANCE, 
                    DEBIT_AMOUNT, 
                    EXCLUDE_LIST_BDM, 
                    EXCLUDE_LIST_UW, 
                    GROSS_CREDIT_BALANCE, 
                    GROSS_INFLOW_PER_LIMIT, 
                    MAX_BALANCE, 
                    MIN_BALANCE, 
                    MONTH_BALANCE, 
                    NUMBER_OF_CHQ_RETURN, 
                    NUMBER_OF_CREDIT_TXN, 
                    NUMBER_OF_DEBIT_TXN, 
                    OVER_LIMIT_AMOUNT, 
                    OVER_LIMIT_DAYS, 
                    OVER_LIMIT_TIMES, 
                    SWING_PERCENT, 
                    TIMES_OF_AVG_CREDIT_BDM, 
                    TIMES_OF_AVG_CREDIT_UW, 
                    TOTAL_TRANSACTION, 
                    UTILIZATION_PERCENT, 
                    vBankId_New
                FROM WRK_BANKSTATEMENT_DETAIL
                WHERE BANK_STMT_ID = vBankId_Old;                  
                    
            END LOOP;                 
                
        END LOOP;
    
    END;

END slos;
/
