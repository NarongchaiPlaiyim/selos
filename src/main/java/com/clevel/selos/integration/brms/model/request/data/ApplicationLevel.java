package com.clevel.selos.integration.brms.model.request.data;

import java.math.BigDecimal;

public class ApplicationLevel {
    public String caNo;
    public int numberOfMonthsLastContractDate;
    public String clisFlag;
    public int productGroup; //enum
    public int productProgram; //enum
    public boolean existingSMECustomerIndv;
    public boolean isRefinance;
    public int cusType; //enum
    public int bizLocation; //enum
    public int ageOfBusinessMonths;
    public BigDecimal finalDBR;
    public BigDecimal totalApprovedCredit;
    public int creditCusType; //enum
    public boolean isRequestTCG;
    public BigDecimal existingODLimit;
    public BigDecimal case1WCLimit;
    public BigDecimal case2WCLimit;
    public BigDecimal case3WCLimit;
    //todo add more field

    public ApplicationLevel() {
    }

    public ApplicationLevel(String caNo, int numberOfMonthsLastContractDate, String clisFlag, int productGroup, int productProgram, boolean existingSMECustomerIndv, boolean refinance, int cusType, int bizLocation, int ageOfBusinessMonths, BigDecimal finalDBR, BigDecimal totalApprovedCredit, int creditCusType, boolean requestTCG, BigDecimal existingODLimit, BigDecimal case1WCLimit, BigDecimal case2WCLimit, BigDecimal case3WCLimit) {
        this.caNo = caNo;
        this.numberOfMonthsLastContractDate = numberOfMonthsLastContractDate;
        this.clisFlag = clisFlag;
        this.productGroup = productGroup;
        this.productProgram = productProgram;
        this.existingSMECustomerIndv = existingSMECustomerIndv;
        isRefinance = refinance;
        this.cusType = cusType;
        this.bizLocation = bizLocation;
        this.ageOfBusinessMonths = ageOfBusinessMonths;
        this.finalDBR = finalDBR;
        this.totalApprovedCredit = totalApprovedCredit;
        this.creditCusType = creditCusType;
        isRequestTCG = requestTCG;
        this.existingODLimit = existingODLimit;
        this.case1WCLimit = case1WCLimit;
        this.case2WCLimit = case2WCLimit;
        this.case3WCLimit = case3WCLimit;
    }

    public String getCaNo() {
        return caNo;
    }

    public void setCaNo(String caNo) {
        this.caNo = caNo;
    }

    public int getNumberOfMonthsLastContractDate() {
        return numberOfMonthsLastContractDate;
    }

    public void setNumberOfMonthsLastContractDate(int numberOfMonthsLastContractDate) {
        this.numberOfMonthsLastContractDate = numberOfMonthsLastContractDate;
    }

    public String getClisFlag() {
        return clisFlag;
    }

    public void setClisFlag(String clisFlag) {
        this.clisFlag = clisFlag;
    }

    public int getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(int productGroup) {
        this.productGroup = productGroup;
    }

    public int getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(int productProgram) {
        this.productProgram = productProgram;
    }

    public boolean isExistingSMECustomerIndv() {
        return existingSMECustomerIndv;
    }

    public void setExistingSMECustomerIndv(boolean existingSMECustomerIndv) {
        this.existingSMECustomerIndv = existingSMECustomerIndv;
    }

    public boolean isRefinance() {
        return isRefinance;
    }

    public void setRefinance(boolean refinance) {
        isRefinance = refinance;
    }

    public int getCusType() {
        return cusType;
    }

    public void setCusType(int cusType) {
        this.cusType = cusType;
    }

    public int getBizLocation() {
        return bizLocation;
    }

    public void setBizLocation(int bizLocation) {
        this.bizLocation = bizLocation;
    }

    public int getAgeOfBusinessMonths() {
        return ageOfBusinessMonths;
    }

    public void setAgeOfBusinessMonths(int ageOfBusinessMonths) {
        this.ageOfBusinessMonths = ageOfBusinessMonths;
    }

    public BigDecimal getFinalDBR() {
        return finalDBR;
    }

    public void setFinalDBR(BigDecimal finalDBR) {
        this.finalDBR = finalDBR;
    }

    public BigDecimal getTotalApprovedCredit() {
        return totalApprovedCredit;
    }

    public void setTotalApprovedCredit(BigDecimal totalApprovedCredit) {
        this.totalApprovedCredit = totalApprovedCredit;
    }

    public int getCreditCusType() {
        return creditCusType;
    }

    public void setCreditCusType(int creditCusType) {
        this.creditCusType = creditCusType;
    }

    public boolean isRequestTCG() {
        return isRequestTCG;
    }

    public void setRequestTCG(boolean requestTCG) {
        isRequestTCG = requestTCG;
    }

    public BigDecimal getExistingODLimit() {
        return existingODLimit;
    }

    public void setExistingODLimit(BigDecimal existingODLimit) {
        this.existingODLimit = existingODLimit;
    }

    public BigDecimal getCase1WCLimit() {
        return case1WCLimit;
    }

    public void setCase1WCLimit(BigDecimal case1WCLimit) {
        this.case1WCLimit = case1WCLimit;
    }

    public BigDecimal getCase2WCLimit() {
        return case2WCLimit;
    }

    public void setCase2WCLimit(BigDecimal case2WCLimit) {
        this.case2WCLimit = case2WCLimit;
    }

    public BigDecimal getCase3WCLimit() {
        return case3WCLimit;
    }

    public void setCase3WCLimit(BigDecimal case3WCLimit) {
        this.case3WCLimit = case3WCLimit;
    }
}
