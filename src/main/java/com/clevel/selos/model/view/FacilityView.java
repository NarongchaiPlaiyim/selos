package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductProgram;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class FacilityView implements Serializable {

    private long id;
    private String facilityName;
    private BigDecimal requestAmount;
    private String productProgramName;
    private ProductProgram productProgram;
    private CreditType creditType ;

    public FacilityView(){

    }

    public void reset(){
        this.id = 0;
        this.facilityName = "";
        this.requestAmount = new BigDecimal(0);
        this.productProgram = new ProductProgram();
        this.creditType = new CreditType();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public BigDecimal getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(BigDecimal requestAmount) {
        this.requestAmount = requestAmount;
    }

    public String getProductProgramName() {
        return productProgramName;
    }

    public void setProductProgramName(String productProgramName) {
        this.productProgramName = productProgramName;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("facilityName", facilityName)
                .append("requestAmount", requestAmount)
                .append("productProgramName", productProgramName)
                .append("productProgram", productProgram)
                .append("creditType", creditType)
                .toString();
    }
}
