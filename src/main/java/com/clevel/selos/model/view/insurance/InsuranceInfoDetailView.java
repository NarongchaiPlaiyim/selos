package com.clevel.selos.model.view.insurance;

import com.clevel.selos.model.view.insurance.model.InsuranceCompanyTypeModel;
import com.clevel.selos.model.view.insurance.model.InsurerNameModel;
import com.clevel.selos.model.view.insurance.model.ToDoRenameThisClass;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class InsuranceInfoDetailView implements Serializable {
    private InsuranceCompanyTypeModel insuranceCompanyType;
    private ToDoRenameThisClass todo;
    private InsurerNameModel insurerName;
    private BigDecimal insuredAmount;
    private BigDecimal premium;
    private String waitForName;
    private String insurancePolicyNumber;
    private String insuranceType;
    private String riskCode;
    private Date effectiveDateOfInsurance;
    private Date expiryDateOfInsurance;

    private String effectiveDateOfInsuranceShow;
    private String expiryDateOfInsuranceShow;

    public InsuranceInfoDetailView() {
        init();
    }

    private void init(){
        insuranceCompanyType = new InsuranceCompanyTypeModel();
        todo = new ToDoRenameThisClass();
        insurerName = new InsurerNameModel();
        insuredAmount = BigDecimal.ZERO;
        premium = BigDecimal.ZERO;
        effectiveDateOfInsurance = new Date();
        expiryDateOfInsurance = new Date();
    }

    public InsuranceCompanyTypeModel getInsuranceCompanyType() {
        return insuranceCompanyType;
    }

    public void setInsuranceCompanyType(InsuranceCompanyTypeModel insuranceCompanyType) {
        this.insuranceCompanyType = insuranceCompanyType;
    }

    public ToDoRenameThisClass getTodo() {
        return todo;
    }

    public void setTodo(ToDoRenameThisClass todo) {
        this.todo = todo;
    }

    public InsurerNameModel getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(InsurerNameModel insurerName) {
        this.insurerName = insurerName;
    }

    public BigDecimal getInsuredAmount() {
        return insuredAmount;
    }

    public void setInsuredAmount(BigDecimal insuredAmount) {
        this.insuredAmount = insuredAmount;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public String getWaitForName() {
        return waitForName;
    }

    public void setWaitForName(String waitForName) {
        this.waitForName = waitForName;
    }

    public String getInsurancePolicyNumber() {
        return insurancePolicyNumber;
    }

    public void setInsurancePolicyNumber(String insurancePolicyNumber) {
        this.insurancePolicyNumber = insurancePolicyNumber;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    public Date getEffectiveDateOfInsurance() {
        return effectiveDateOfInsurance;
    }

    public void setEffectiveDateOfInsurance(Date effectiveDateOfInsurance) {
        this.effectiveDateOfInsurance = effectiveDateOfInsurance;
    }

    public Date getExpiryDateOfInsurance() {
        return expiryDateOfInsurance;
    }

    public void setExpiryDateOfInsurance(Date expiryDateOfInsurance) {
        this.expiryDateOfInsurance = expiryDateOfInsurance;
    }

    public String getEffectiveDateOfInsuranceShow() {
        return effectiveDateOfInsuranceShow;
    }

    public void setEffectiveDateOfInsuranceShow(String effectiveDateOfInsuranceShow) {
        this.effectiveDateOfInsuranceShow = effectiveDateOfInsuranceShow;
    }

    public String getExpiryDateOfInsuranceShow() {
        return expiryDateOfInsuranceShow;
    }

    public void setExpiryDateOfInsuranceShow(String expiryDateOfInsuranceShow) {
        this.expiryDateOfInsuranceShow = expiryDateOfInsuranceShow;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("insuranceCompanyType", insuranceCompanyType)
                .append("todo", todo)
                .append("insurerName", insurerName)
                .append("insuredAmount", insuredAmount)
                .append("premium", premium)
                .append("waitForName", waitForName)
                .append("insurancePolicyNumber", insurancePolicyNumber)
                .append("insuranceType", insuranceType)
                .append("riskCode", riskCode)
                .append("effectiveDateOfInsurance", effectiveDateOfInsurance)
                .append("expiryDateOfInsurance", expiryDateOfInsurance)
                .toString();
    }
}
