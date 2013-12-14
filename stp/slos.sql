DROP SEQUENCE SEQ_APP_NUMBER;

CREATE SEQUENCE SEQ_APP_NUMBER
START WITH 0
MAXVALUE 999999999999999999999999999
MINVALUE 0
NOCYCLE
NOCACHE
NOORDER;

COMMIT;

CREATE OR REPLACE PACKAGE SLOS AS

  FUNCTION getApplicationNumber(segmentCode IN VARCHAR2) RETURN VARCHAR2;
  procedure resetSeq(p_seq_name in varchar2);
  PROCEDURE createWorkCase(vWorkCasePreScreenId IN WRK_CASE_PRESCREEN.ID%TYPE);

END slos;
COMMIT;

CREATE OR REPLACE PACKAGE BODY SLOS AS

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
  BEGIN
    
    SELECT SEQ_WRK_CASE_ID.NEXTVAL INTO vWorkCaseId FROM DUAL;
    
    -- INSERT TO WORKCASE
    INSERT INTO WRK_CASE (
        ID, 
        APP_NUMBER, 
        CA_NUMBER, 
        CREATE_DATE, 
        CASE_LOCK, 
        LOCK_USER, 
        MODIFY_DATE, 
        REF_APP_NUMBER, 
        WOB_NUMBER, 
        CREATE_BY, 
        MODIFY_BY, 
        STATUS_ID, 
        STEP_ID, 
        STEP_OWNER, 
        WORKCASEPRESCREEN_ID, 
        BORROWER_TYPE_ID)
    SELECT
        vWorkCaseId,
        APP_NUMBER,
        CA_NUMBER,
        CREATE_DATE,
        CASE_LOCK,
        LOCK_USER,
        MODIFY_DATE, 
        REF_APP_NUMBER, 
        WOB_NUMBER, 
        CREATE_BY, 
        MODIFY_BY, 
        STATUS_ID, 
        STEP_ID, 
        STEP_OWNER, 
        ID, 
        BORROWER_TYPE_ID 
    FROM
        WRK_CASE_PRESCREEN
    WHERE 
        ID = vWorkCasePreScreenId;
    
    -- LOOP FOR INSERT CUSTOMER, INDIVIDUAL, JURISTIC
    FOR CUSTOMER_CURSOR IN ( SELECT * FROM WRK_CUSTOMER WHERE WORKCASE_PRESCREEN_ID = vWorkCasePreScreenId ORDER BY ID ASC )
    LOOP
        vCustomerId_Old := CUSTOMER_CURSOR.ID;
        vCustomerEntity := CUSTOMER_CURSOR.CUSTOMERENTITY_ID;
        
        SELECT SEQ_WRK_CUSTOMER_ID.NEXTVAL INTO vCustomerId_New FROM DUAL;
        
        --INSERT TO CUSTOMER 
        INSERT INTO WRK_CUSTOMER (
            ID, 
            AGE, 
            EXPIRE_DATE, 
            ID_NUMBER, 
            LASTNAME_EN, 
            LASTNAME_TH, 
            NAME_EN, 
            NAME_TH, 
            NCB_CHECKED, 
            CUSTOMERENTITY_ID, 
            DOCUMENTTYPE_ID, 
            RELATION_ID, 
            TITLE_ID, 
            WORKCASE_ID, 
            WORKCASE_PRESCREEN_ID, 
            APPROX_INCOME, 
            DOCUMENT_AUTHORIZE_BY, 
            COLLATERAL_OWNER, 
            PERCENT_SHARE, 
            SERVICE_SEGMENT, 
            BUSINESSTYPE_ID, 
            WARNINGCODE_ID, 
            REFERENCE_ID, 
            IS_SPOUSE, 
            SPOUSE_ID, 
            IS_SEARCH_RM, 
            CSI_CHECKED
        ) VALUES (
            vCustomerId_New,
            CUSTOMER_CURSOR.AGE, 
            CUSTOMER_CURSOR.EXPIRE_DATE, 
            CUSTOMER_CURSOR.ID_NUMBER, 
            CUSTOMER_CURSOR.LASTNAME_EN, 
            CUSTOMER_CURSOR.LASTNAME_TH, 
            CUSTOMER_CURSOR.NAME_EN, 
            CUSTOMER_CURSOR.NAME_TH, 
            CUSTOMER_CURSOR.NCB_CHECKED, 
            CUSTOMER_CURSOR.CUSTOMERENTITY_ID, 
            CUSTOMER_CURSOR.DOCUMENTTYPE_ID, 
            CUSTOMER_CURSOR.RELATION_ID, 
            CUSTOMER_CURSOR.TITLE_ID, 
            vWorkCaseId,
            NULL,
            CUSTOMER_CURSOR.APPROX_INCOME, 
            CUSTOMER_CURSOR.DOCUMENT_AUTHORIZE_BY, 
            CUSTOMER_CURSOR.COLLATERAL_OWNER, 
            CUSTOMER_CURSOR.PERCENT_SHARE, 
            CUSTOMER_CURSOR.SERVICE_SEGMENT, 
            CUSTOMER_CURSOR.BUSINESSTYPE_ID, 
            CUSTOMER_CURSOR.WARNINGCODE_ID, 
            CUSTOMER_CURSOR.REFERENCE_ID, 
            CUSTOMER_CURSOR.IS_SPOUSE, 
            CUSTOMER_CURSOR.SPOUSE_ID, 
            CUSTOMER_CURSOR.IS_SEARCH_RM, 
            CUSTOMER_CURSOR.CSI_CHECKED    
        );
        
        IF vCustomerEntity = '1' THEN
            --INSERT TO INDIVIDUAL
            INSERT INTO WRK_INDIVIDUAL (
                ID, 
                BIRTH_DATE, 
                CITIZEN_ID, 
                GENDER, 
                NUM_OF_CHILDREN, 
                CUSTOMER_ID, 
                EDUCATION_ID, 
                MARITALSTATUS_ID, 
                NATIONALITY_ID, 
                OCCUPATION_ID, 
                RACE_ID, 
                SND_NATIONALITY_ID, 
                CITIZEN_COUNTRY_ID    
            ) SELECT
                SEQ_WRK_INDIVIDUAL_ID.NEXTVAL,
                BIRTH_DATE, 
                CITIZEN_ID, 
                GENDER, 
                NUM_OF_CHILDREN, 
                vCustomerId_New, 
                EDUCATION_ID, 
                MARITALSTATUS_ID, 
                NATIONALITY_ID, 
                OCCUPATION_ID, 
                RACE_ID, 
                SND_NATIONALITY_ID, 
                CITIZEN_COUNTRY_ID
              FROM WRK_INDIVIDUAL 
              WHERE CUSTOMER_ID = vCustomerId_Old;
        ELSIF vCustomerEntity = '2' THEN
            --INSERT TO JURISTIC
            INSERT INTO WRK_JURISTIC (  
                ID, 
                CAPITAL, 
                FINANCIAL_YEAR, 
                FOUNDED, 
                PAIDCAPITAL, 
                REGISTRATION_ID, 
                SIGNCONDITION, 
                TOTALSHARE, 
                BUSINESSACTIVITY_ID, 
                BUSINESSDESCRIPTION_ID, 
                BUSINESSGROUP_ID, 
                BUSINESSTYPE_ID, 
                CUSTOMER_ID, 
                OWNER_ID, 
                REGISTER_DATE, 
                REGISTER_COUNTRY, 
                DOCUMENT_ISSUE_DATE, 
                NUMBER_AUTHORIZED_USERS, 
                PAID_CAPITAL, 
                SALES_FINANCIAL_STMT, 
                SHARE_HOLDER_RATIO, 
                SIGN_SONDITION, TOTAL_SHARE
            ) SELECT
                SEQ_WRK_JURISTIC_ID.NEXTVAL, 
                CAPITAL, 
                FINANCIAL_YEAR, 
                FOUNDED, 
                PAIDCAPITAL, 
                REGISTRATION_ID, 
                SIGNCONDITION, 
                TOTALSHARE, 
                BUSINESSACTIVITY_ID, 
                BUSINESSDESCRIPTION_ID, 
                BUSINESSGROUP_ID, 
                BUSINESSTYPE_ID, 
                vCustomerId_New, 
                OWNER_ID, 
                REGISTER_DATE, 
                REGISTER_COUNTRY, 
                DOCUMENT_ISSUE_DATE, 
                NUMBER_AUTHORIZED_USERS, 
                PAID_CAPITAL, 
                SALES_FINANCIAL_STMT, 
                SHARE_HOLDER_RATIO, 
                SIGN_SONDITION, TOTAL_SHARE
              FROM WRK_JURISTIC
              WHERE CUSTOMER_ID = vCustomerId_Old;                        
        END IF;
        
        --INSERT TO ADDRESS
        INSERT INTO WRK_ADDRESS (
            ID, 
            ADDRESS_NO, 
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
            SUBDISTRICT_ID, 
            ADDRESS
        ) SELECT
            SEQ_WRK_ADDRESS_ID.NEXTVAL, 
            ADDRESS_NO, 
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
            SUBDISTRICT_ID, 
            ADDRESS
          FROM WRK_ADDRESS
          WHERE CUSTOMER_ID = vCustomerId_Old;
          
        --INSERT TO CUSTOMER_ACCOUNT
        INSERT INTO WRK_CUSTOMER_ACCOUNT (
            ID, 
            ID_NUMBER, 
            CUSTOMER_ID, 
            DOCUMENT_TYPE_ID
        ) SELECT 
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
            CUSTOMER_ID
        ) SELECT 
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
            WARNING_ID
        ) SELECT
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
                CHECK_IN_6_MONTH, 
                CHECKING_DATE, 
                CURRENT_PAYMENT_TYPE, 
                ENQUIRY, 
                HISTORY_PAYMENT_TYPE, 
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
                CUSTOMER_ID, 
                TDR_CONDITION_ID, 
                ACTIVE, 
                CREATE_DATE, 
                MODIFY_DATE, 
                CREATE_USER_ID, 
                MODIFY_USER_ID
            ) VALUES ( 
                vNCBId_New, 
                NCB_CURSOR.CHECK_IN_6_MONTH, 
                NCB_CURSOR.CHECKING_DATE, 
                NCB_CURSOR.CURRENT_PAYMENT_TYPE, 
                NCB_CURSOR.ENQUIRY, 
                NCB_CURSOR.HISTORY_PAYMENT_TYPE, 
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
                vCustomerId_New, 
                NCB_CURSOR.TDR_CONDITION_ID, 
                NCB_CURSOR.ACTIVE, 
                NCB_CURSOR.CREATE_DATE, 
                NCB_CURSOR.MODIFY_DATE, 
                NCB_CURSOR.CREATE_USER_ID, 
                NCB_CURSOR.MODIFY_USER_ID
             );
             
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
    
            
       
    --INSERT TO BANKSTMT_SUMMARY
    INSERT INTO WRK_BANKSTATEMENT_SUMMARY (
        ID, 
        TMB_TOTAL_INCOME_GROSS, 
        TMB_TOTAL_INCOME_BDM, 
        TMB_TOTAL_INCOME_UW, 
        CREATE_DATE, 
        EXPECTED_SUBMIT_DATE, 
        GRAND_AVG_OS_BALANCE_AMT, 
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
        WORKCASE_PRESCREEN_ID
    ) SELECT 
        SEQ_WRK_BANKSTMT_SUM_ID.NEXTVAL, 
        TMB_TOTAL_INCOME_GROSS, 
        TMB_TOTAL_INCOME_BDM, 
        TMB_TOTAL_INCOME_UW, 
        CREATE_DATE, 
        EXPECTED_SUBMIT_DATE, 
        GRAND_AVG_OS_BALANCE_AMT, 
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
        vWorkCaseId, 
        NULL
      FROM WRK_BANKSTATEMENT_SUMMARY BANK_SUM
      WHERE WORKCASE_PRESCREEN_ID = vWorkCasePreScreenId;
      
    --INSERT TO BANKSTMT
    INSERT INTO WRK_BANKSTATEMENT (
        ID, 
        ACCOUNT_NAME, 
        ACCOUNT_NO, 
        BRANCH_NAME, 
        CREATE_DATE, 
        MODIFY_DATE, 
        BANK_ID, 
        BANK_ACCOUNT_TYPE_ID, 
        CREATE_USER_ID, 
        MODIFY_USER_ID, 
        WORKCASE_ID, 
        ACCOUNT_CHARACTER, 
        ACCOUNT_NUMBER, 
        AVG_WITHDRAW_AMOUNT, 
        AVG_GROSS_INFLOW_PER_LIMIT, 
        AVG_INCOME_GROSS, 
        AVG_INCOME_BDM, 
        AVG_INCOME_UW, 
        TD_CHQ_RETURN_TIMES, 
        LIMIT, 
        MAIN_ACCOUNT, 
        OTHER_ACCOUNT_TYPE, 
        OVER_LIMIT_DAYS, 
        OVER_LIMIT_TIMES, 
        REMARK, 
        AVG_SWING_PERCENT, 
        TD_CHQ_RETURN_AMOUNT, 
        TD_CHQ_RETURN_PERCENT, 
        AVG_UTILIZATION_PERCENT, 
        ACCOUNT_STATUS_ID, 
        BANK_STMT_SUMMARY_ID, 
        AVG_OS_BALANCE_AMOUNT
    ) SELECT
        SEQ_WRK_BANKSTMT_ID.NEXTVAL, 
        ACCOUNT_NAME, 
        ACCOUNT_NO, 
        BRANCH_NAME, 
        CREATE_DATE, 
        MODIFY_DATE, 
        BANK_ID, 
        BANK_ACCOUNT_TYPE_ID, 
        CREATE_USER_ID, 
        MODIFY_USER_ID, 
        WORKCASE_ID, 
        ACCOUNT_CHARACTER, 
        ACCOUNT_NUMBER, 
        AVG_WITHDRAW_AMOUNT, 
        AVG_GROSS_INFLOW_PER_LIMIT, 
        AVG_INCOME_GROSS, 
        AVG_INCOME_BDM, 
        AVG_INCOME_UW, 
        TD_CHQ_RETURN_TIMES, 
        LIMIT, 
        MAIN_ACCOUNT, 
        OTHER_ACCOUNT_TYPE, 
        OVER_LIMIT_DAYS, 
        OVER_LIMIT_TIMES, 
        REMARK, 
        AVG_SWING_PERCENT, 
        TD_CHQ_RETURN_AMOUNT, 
        TD_CHQ_RETURN_PERCENT, 
        AVG_UTILIZATION_PERCENT, 
        ACCOUNT_STATUS_ID, 
        BANK_STMT_SUMMARY_ID_NEW, 
        AVG_OS_BALANCE_AMOUNT
      FROM WRK_BANKSTATEMENT BANKSTMT 
      INNER JOIN (
        SELECT BANK_STMT_SUMMARY_ID, BANK_STMT_SUMMARY_ID_NEW 
        FROM (
            SELECT ID AS BANK_STMT_SUMMARY_ID, vWorkCaseId AS JOINER
            FROM WRK_BANKSTATEMENT_SUMMARY
            WHERE WORKCASE_PRESCREEN_ID = vWorkCasePreScreenId
        ) BANKSUM_OLD 
        INNER JOIN (
            SELECT ID AS BANK_STMT_SUMMARY_ID_NEW, WORKCASE_ID AS JOINER
            FROM WRK_BANKSTATEMENT_SUMMARY
            WHERE WORKCASE_ID = vWorkCaseId
        ) BANKSUM_NEW ON BANKSUM_OLD.JOINER = BANKSUM_NEW.JOINER 
      ) BANKSTMT_SUM 
      ON BANKSTMT.BANK_STMT_SUMMARY_ID = BANKSTMT_SUM.BANK_STMT_SUMMARY_ID;
              
 
    --INSERT TO BANKSTMT_DETAIL
    INSERT INTO WRK_BANKSTATEMENT_DETAIL (
        ID, 
        AS_OF_DATE, 
        CHEQUE_RETURN_AMOUNT, 
        CREDIT_AMOUNT_BDM, 
        CREDIT_AMOUNT_UW, 
        DEBIT_AMOUNT, 
        EXCLUDE_LIST_BDM, 
        EXCLUDE_LIST_UW, 
        GROSS_CREDIT_BALANCE, 
        GROSS_INFLOW_PER_LIMIT, 
        HIGHEST_BALANCE, 
        HIGHEST_BALANCE_DATE, 
        LOWEST_BALANCE, 
        LOWEST_BALANCE_DATE, 
        MONTH_END_BALANCE, 
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
        BANK_STMT_ID, 
        DATE_OF_MAX_BALANCE, 
        DATE_OF_MIN_BALANCE, 
        MAX_BALANCE, 
        MIN_BALANCE, 
        MONTH_BALANCE
    ) SELECT 
        SEQ_WRK_BANKSTMT_DETAIL_ID.NEXTVAL, 
        AS_OF_DATE, 
        CHEQUE_RETURN_AMOUNT, 
        CREDIT_AMOUNT_BDM, 
        CREDIT_AMOUNT_UW, 
        DEBIT_AMOUNT, 
        EXCLUDE_LIST_BDM, 
        EXCLUDE_LIST_UW, 
        GROSS_CREDIT_BALANCE, 
        GROSS_INFLOW_PER_LIMIT, 
        HIGHEST_BALANCE, 
        HIGHEST_BALANCE_DATE, 
        LOWEST_BALANCE, 
        LOWEST_BALANCE_DATE, 
        MONTH_END_BALANCE, 
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
        BANKSTMT_ID_NEW, 
        DATE_OF_MAX_BALANCE, 
        DATE_OF_MIN_BALANCE, 
        MAX_BALANCE, 
        MIN_BALANCE, 
        MONTH_BALANCE
      FROM WRK_BANKSTATEMENT_DETAIL BANKSTMT_DETAIL
      INNER JOIN (
        SELECT BANKSTMT_ID, BANKSTMT_ID_NEW
        FROM (
            SELECT ID AS BANKSTMT_ID, ROWNUM AS ROWNUM_OLD
            FROM WRK_BANKSTATEMENT BANKSTMT_OLD
            INNER JOIN (
                SELECT ID AS BANKSTMT_SUM_ID
                FROM WRK_BANKSTATEMENT_SUMMARY 
                WHERE WORKCASE_PRESCREEN_ID = vWorkCasePreScreenId
            ) BANKSTMT_SUM_OLD ON BANKSTMT_OLD.BANK_STMT_SUMMARY_ID = BANKSTMT_SUM_OLD.BANKSTMT_SUM_ID
            ORDER BY BANKSTMT_OLD.ID ASC  
            
        ) BANKSTMTS_OLD INNER JOIN (
            SELECT ID AS BANKSTMT_ID_NEW, ROWNUM AS ROWNUM_NEW
            FROM WRK_BANKSTATEMENT BANKSTMT_NEW
            INNER JOIN (
                SELECT ID AS BANKSTMT_SUM_ID
                FROM WRK_BANKSTATEMENT_SUMMARY
                WHERE WORKCASE_ID = vWorkCaseId
            ) BANKSTMT_SUM_NEW ON BANKSTMT_NEW.BANK_STMT_SUMMARY_ID = BANKSTMT_SUM_NEW.BANKSTMT_SUM_ID
            ORDER BY BANKSTMT_NEW.ID ASC
        ) BANKSTMTS_NEW ON BANKSTMTS_OLD.ROWNUM_OLD = BANKSTMTS_NEW.ROWNUM_NEW
      ) BANKSTMTS ON BANKSTMT_DETAIL.BANK_STMT_ID = BANKSTMTS.BANKSTMT_ID;
    
  END;

END slos;
/
