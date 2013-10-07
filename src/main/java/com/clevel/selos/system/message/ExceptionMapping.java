package com.clevel.selos.system.message;

public interface ExceptionMapping {
    public static final String SYSTEM_EXCEPTION = "001";
    //LDAP exception
    public static final String LDAP_EMPTY_PASSWORD = "005";
    public static final String LDAP_AUTHENTICATION_FAILED = "006";

    //BPM exception
    public static final String BPM_NEW_CASE_EXCEPTION = "010";
    public static final String BPM_GET_INBOX_EXCEPTION = "011";
    public static final String BPM_DISPATCH_EXCEPTION = "012";
    public static final String BPM_LOCK_CASE_EXCEPTION = "013";
    public static final String BPM_UNLOCK_CASE_EXCEPTION = "014";
    public static final String BPM_AUTHENTICATION_FAILED = "015";

    public static final String RM_SERVICE_FAILED = "500";
    public static final String HOST_PARAMETER_IS_NULL = "501";
    public static final String FAIL = "502";
    public static final String DATA_NOT_FOUND = "503";


}
