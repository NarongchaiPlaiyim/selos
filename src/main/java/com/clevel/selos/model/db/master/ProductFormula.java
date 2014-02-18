package com.clevel.selos.model.db.master;

import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "mst_product_formula")
public class ProductFormula implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "prdprogram_facility_id")
    private PrdProgramToCreditType programToCreditType;

    @Column(name = "credit_cus_type")
    private int creditCusType;
    @Column(name = "apply_tcg")
    private int applyTCG;

    @ManyToOne
    @JoinColumn(name = "special_program_id")
    private SpecialProgram specialProgram;
    @Column(name = "product_code")
    private String productCode;
    @Column(name = "project_code")
    private String projectCode;

    @Column(name = "exposure_method")
    private int exposureMethod;
    @Column(name = "wc_calculate")
    private int wcCalculate;
    @Column(name = "reduce_pricing")
    private int reducePricing;
    @Column(name = "reduce_frontend_fee")
    private int reduceFrontEndFee;
    @Column(name = "dbr_calculate")
    private int dbrCalculate;
    @Column(name = "dbr_method")
    private int dbrMethod;
    @Column(name = "dbr_spread", length = 10)
    private BigDecimal dbrSpread;
    @Column(name = "active")
    private int active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PrdProgramToCreditType getProgramToCreditType() {
        return programToCreditType;
    }

    public void setProgramToCreditType(PrdProgramToCreditType programToCreditType) {
        this.programToCreditType = programToCreditType;
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

    public SpecialProgram getSpecialProgram() {
        return specialProgram;
    }

    public void setSpecialProgram(SpecialProgram specialProgram) {
        this.specialProgram = specialProgram;
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
                .append("programToCreditType", programToCreditType)
                .append("creditCusType", creditCusType)
                .append("applyTCG", applyTCG)
                .append("specialProgram", specialProgram)
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
