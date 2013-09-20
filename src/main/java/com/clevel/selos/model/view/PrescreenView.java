package com.clevel.selos.model.view;


import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.Province;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PrescreenView implements Serializable {
    private BigDecimal id;
    private ProductGroup productGroup;
    private Date expectedSubmitDate;
    private Province businessLocation;
    private Date registerDate;
    private Date referDate;
    private boolean tcg;
    private boolean refinance;

    public PrescreenView(){

    }

    public void reset(){
        this.productGroup = new ProductGroup();
        this.expectedSubmitDate = null;
        this.businessLocation = new Province();
        this.registerDate = null;
        this.tcg = false;
        this.refinance = false;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public Province getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(Province businessLocation) {
        this.businessLocation = businessLocation;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getReferDate() {
        return referDate;
    }

    public void setReferDate(Date referDate) {
        this.referDate = referDate;
    }

    public boolean isTcg() {
        return tcg;
    }

    public void setTcg(boolean tcg) {
        this.tcg = tcg;
    }

    public boolean isRefinance() {
        return refinance;
    }

    public void setRefinance(boolean refinance) {
        this.refinance = refinance;
    }
}
