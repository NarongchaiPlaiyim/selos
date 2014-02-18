package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductFormulaView implements Serializable {

    private int id;
    private PrdProgramToCreditTypeView programToCreditTypeView;
    private int creditCusType;
    private int applyTCG;
    private SpecialProgramView specialProgramView;
    private String productCode;
    private String projectCode;
    private int exposureMethod;
    private int wcCalculate;
    private int reducePricing;
    private int reduceFrontEndFee;
    private int dbrCalculate;
    private int dbrMethod;
    private BigDecimal dbrSpread;
    private int active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PrdProgramToCreditTypeView getProgramToCreditTypeView() {
        return programToCreditTypeView;
    }

    public void setProgramToCreditTypeView(PrdProgramToCreditTypeView programToCreditTypeView) {
        this.programToCreditTypeView = programToCreditTypeView;
    }

    public int getCreditCusType() {
        return creditCusType;
    }

    public void setCreditCusType(int creditCusType) {
        this.creditCusType = creditCusType;
    }

    public int getApplyTCG() {
        return applyTCG;
    }

    public void setApplyTCG(int applyTCG) {
        this.applyTCG = applyTCG;
    }

    public SpecialProgramView getSpecialProgramView() {
        return specialProgramView;
    }

    public void setSpecialProgramView(SpecialProgramView specialProgramView) {
        this.specialProgramView = specialProgramView;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public int getExposureMethod() {
        return exposureMethod;
    }

    public void setExposureMethod(int exposureMethod) {
        this.exposureMethod = exposureMethod;
    }

    public int getWcCalculate() {
        return wcCalculate;
    }

    public void setWcCalculate(int wcCalculate) {
        this.wcCalculate = wcCalculate;
    }

    public int getReducePricing() {
        return reducePricing;
    }

    public void setReducePricing(int reducePricing) {
        this.reducePricing = reducePricing;
    }

    public int getReduceFrontEndFee() {
        return reduceFrontEndFee;
    }

    public void setReduceFrontEndFee(int reduceFrontEndFee) {
        this.reduceFrontEndFee = reduceFrontEndFee;
    }

    public int getDbrCalculate() {
        return dbrCalculate;
    }

    public void setDbrCalculate(int dbrCalculate) {
        this.dbrCalculate = dbrCalculate;
    }

    public int getDbrMethod() {
        return dbrMethod;
    }

    public void setDbrMethod(int dbrMethod) {
        this.dbrMethod = dbrMethod;
    }

    public BigDecimal getDbrSpread() {
        return dbrSpread;
    }

    public void setDbrSpread(BigDecimal dbrSpread) {
        this.dbrSpread = dbrSpread;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("programToCreditTypeView", programToCreditTypeView)
                .append("creditCusType", creditCusType)
                .append("applyTCG", applyTCG)
                .append("specialProgramView", specialProgramView)
                .append("productCode", productCode)
                .append("projectCode", projectCode)
                .append("exposureMethod", exposureMethod)
                .append("wcCalculate", wcCalculate)
                .append("reducePricing", reducePricing)
                .append("reduceFrontEndFee", reduceFrontEndFee)
                .append("dbrCalculate", dbrCalculate)
                .append("dbrMethod", dbrMethod)
                .append("dbrSpread", dbrSpread)
                .append("active", active)
                .toString();
    }
}
