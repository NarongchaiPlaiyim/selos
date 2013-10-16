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

    //BPM exception
    public static final String BPM_NEW_CASE_EXCEPTION = "010";
    public static final String BPM_GET_INBOX_EXCEPTION = "011";
    public static final String BPM_DISPATCH_EXCEPTION = "012";
    public static final String BPM_LOCK_CASE_EXCEPTION = "013";
    public static final String BPM_UNLOCK_CASE_EXCEPTION = "014";
    public static final String BPM_AUTHENTICATION_FAILED = "015";

    //Email exception
    public static final String EMAIL_EMPTY_ADDRESS = "020";
    public static final String EMAIL_AUTHENTICATION_FAILED = "021";
    public static final String EMAIL_INVALID_ADDRESS = "022";
    public static final String EMAIL_COULD_NOT_BE_SENT = "023";
    public static final String EMAIL_EXCEPTION = "024";
    public static final String EMAIL_TEMPLATE_NOT_FOUND = "025";

    //RLOS exception
    public static final String RLOS_CSI_EXCEPTION = "030";

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
}
