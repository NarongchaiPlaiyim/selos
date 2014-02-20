package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class PrdGroupToPrdProgramView implements Serializable {

    private int id;
    private ProductGroupView productGroupView;
    private ProductProgramView productProgramView;
    private int addExistingCredit;
    private int addProposeCredit;
    private int active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductGroupView getProductGroupView() {
        return productGroupView;
    }

    public void setProductGroupView(ProductGroupView productGroupView) {
        this.productGroupView = productGroupView;
    }

    public ProductProgramView getProductProgramView() {
        return productProgramView;
    }

    public void setProductProgramView(ProductProgramView productProgramView) {
        this.productProgramView = productProgramView;
    }

    public int getAddExistingCredit() {
        return addExistingCredit;
    }

    public void setAddExistingCredit(int addExistingCredit) {
        this.addExistingCredit = addExistingCredit;
    }

    public int getAddProposeCredit() {
        return addProposeCredit;
    }

    public void setAddProposeCredit(int addProposeCredit) {
        this.addProposeCredit = addProposeCredit;
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
                .append("productGroupView", productGroupView)
                .append("productProgramView", productProgramView)
                .append("addExistingCredit", addExistingCredit)
                .append("addProposeCredit", addProposeCredit)
                .append("active", active)
                .toString();
    }
}
