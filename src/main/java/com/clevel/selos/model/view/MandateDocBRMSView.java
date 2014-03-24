package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class MandateDocBRMSView implements Serializable {
    private long id;
    private String BRMSDocType;

    public MandateDocBRMSView() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBRMSDocType() {
        return BRMSDocType;
    }

    public void setBRMSDocType(String BRMSDocType) {
        this.BRMSDocType = BRMSDocType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("BRMSDocType", BRMSDocType)
                .toString();
    }
}
