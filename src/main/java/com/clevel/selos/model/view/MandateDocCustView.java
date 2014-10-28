package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;


public class MandateDocCustView implements Serializable {
    private long id;
    private String custName;
    private MandateDocView mandateDocView;
    public MandateDocCustView() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public MandateDocView getMandateDocView() {
        return mandateDocView;
    }

    public void setMandateDocView(MandateDocView mandateDocView) {
        this.mandateDocView = mandateDocView;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("custName", custName)
                .toString();
    }
}
