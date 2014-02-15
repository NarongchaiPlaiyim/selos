package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PrdProgramToCreditTypeView {

    private int id;
    private ProductProgramView productProgramView;
    private CreditTypeView creditTypeView;
    private int addExistingCredit;
    private int addProposeCredit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductProgramView getProductProgramView() {
        return productProgramView;
    }

    public void setProductProgramView(ProductProgramView productProgramView) {
        this.productProgramView = productProgramView;
    }

    public CreditTypeView getCreditTypeView() {
        return creditTypeView;
    }

    public void setCreditTypeView(CreditTypeView creditTypeView) {
        this.creditTypeView = creditTypeView;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("productProgramView", productProgramView)
                .append("creditTypeView", creditTypeView)
                .append("addExistingCredit", addExistingCredit)
                .append("addProposeCredit", addProposeCredit)
                .toString();
    }
}
