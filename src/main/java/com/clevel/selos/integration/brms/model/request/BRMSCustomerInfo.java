package com.clevel.selos.integration.brms.model.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BRMSCustomerInfo implements Serializable{

    private String customerEntity;
    private boolean existingSMECustomer;
    private String relation;
    private String reference;
    private String nationality;
    private BigDecimal numberOfMonthLastContractDate;
    private String qualitativeClass;
    private Date nextReviewDate;
    private boolean nextReviewDateFlag;
    private Date extendedReviewDate;
    private boolean extendedReviewDateFlag;
    private String ratingFinal;
    private boolean unpaidFeeInsurance;
    private boolean noPendingClaimLG;
    private String creditWorthiness;
    private int kycLevel;
    private String spousePersonalID;
    private String spouseRelationType;
    private String personalID;
    private BigDecimal ageMonths;
    private String marriageStatus;
    private boolean ncbFlag;
    private int numberOfNCBCheckIn6Months;
    private BigDecimal numberOfDayLastNCBCheck;
    private String csiFullyMatchCode;
    private String csiSomeMatchCode;
    private String numberOfDaysOverAnnualReview;

    private List<BRMSTMBAccountInfo> tmbAccountInfoList;
    private List<BRMSNCBAccountInfo> ncbAccountInfoList;


}
