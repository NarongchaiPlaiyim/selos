package com.clevel.selos.system.message;

public interface ExceptionMapping {
    public static final String SYSTEM_EXCEPTION = "001";
    //LDAP exception
    public static final String LDAP_EMPTY_PASSWORD = "002";
    public static final String LDAP_AUTHENTICATION_FAILED = "003";
    public static final String USER_NOT_ACTIVE = "004";
    public static final String USER_NOT_FOUND = "005";
    public static final String USER_STATUS_DISABLED = "006";
    public static final String USER_STATUS_DELETED = "007";
    public static final String LDAP_EMPTY_USERNAME = "008";
    public static final String USER_OR_PASS_INCORRECT = "009";
    public static final String USER_LOCKED = "099";
    public static final String USER_OTHER_EXCEPTION = "999";


    //BPM exception
    public static final String BPM_NEW_CASE_EXCEPTION = "010";
    public static final String BPM_GET_INBOX_EXCEPTION = "011";
    public static final String BPM_DISPATCH_EXCEPTION = "012";
    public static final String BPM_LOCK_CASE_EXCEPTION = "013";
    public static final String BPM_UNLOCK_CASE_EXCEPTION = "014";
    public static final String BPM_AUTHENTICATION_FAILED = "015";
    public static final String BPM_GET_INBOX_CONNECT_ERROR = "016";
    public static final String BPM_GET_INBOX_GETDATA_ERROR = "017";
    public static final String BPM_GET_INBOX_DATA_NOT_FOUND = "018";

    //Email exception
    public static final String EMAIL_EMPTY_ADDRESS = "020";
    public static final String EMAIL_AUTHENTICATION_FAILED = "021";
    public static final String EMAIL_INVALID_ADDRESS = "022";
    public static final String EMAIL_COULD_NOT_BE_SENT = "023";
    public static final String EMAIL_EXCEPTION = "024";
    public static final String EMAIL_TEMPLATE_NOT_FOUND = "025";

    //RLOS exception
    public static final String RLOS_CSI_EXCEPTION = "030";
    public static final String RLOS_APPIN_EXCEPTION = "031";
    public static final String RLOS_DATA_NOT_FOUND = "032";
    public static final String RLOS_INVALID_INPUT = "033";
    public static final String RLOS_CSI_CONNECT_ERROR = "034";
    public static final String RLOS_CSI_GETDATA_ERROR = "035";

    //DWH exception
    public static final String DWH_OBLIGATION_EXCEPTION = "040";
    public static final String DWH_BANK_STATEMENT_EXCEPTION = "041";
    public static final String DWH_DATA_NOT_FOUND = "042";
    public static final String DWH_INVALID_INPUT = "043";
    public static final String DWH_DATA_NOT_ENOUGH = "044";

    //COMS exception
    public static final String COMS_EXCEPTION = "050";
    public static final String COMS_CONNECT_ERROR = "051";
    public static final String COMS_GETDATA_ERROR = "052";
    public static final String COMS_DATA_NOT_FOUND = "053";
    public static final String COMS_WORKCASE_NOT_FOUND = "054";

    //RM
    public static final String RM_HOST_PARAMETER_IS_NULL = "501";
    public static final String RM_FAIL = "502";
    public static final String RM_DATA_NOT_FOUND = "503";
    public static final String RM_CUSTOMER_RESULT_MULTIPLE = "504";
    public static final String RM_SERVICE_FAILED = "505";

    //NCB
    public static final String NCB_EXCEPTION = "601";
    public static final String NCB_FAILED = "602";
    public static final String NCB_HTTPHOSTCONNECTEXCEPTION = "603";
    public static final String NCB_CONNECTTIMEOUTEXCEPTION = "604";
    public static final String NCB_CONNECT_ERROR = "605";
    public static final String NCB_WORKCASE_NOT_FOUND = "606";

    //DB Inteface
    public static final String INVALID_SYSTEM_PARAM = "701";
    public static final String NOT_FOUND_SYSTEM_PARAM = "702";

    //ECM Exception
    public static final String ECM_EXCEPTION = "801";
    public static final String ECM_CONNECT_ERROR = "802";
    public static final String ECM_GETDATA_ERROR = "803";
    public static final String ECM_DATA_NOT_FOUND = "804";
    public static final String ECM_UPDATEDATA_ERROR = "805";
    public static final String ECM_INSERTDATA_ERROR = "806";


    //BRMS Exception
    public static final String BRMS_INVALID_RETURN_DATA = "901";
}
