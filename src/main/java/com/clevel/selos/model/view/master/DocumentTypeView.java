package com.clevel.selos.model.view.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class DocumentTypeView implements Serializable{

    private int id;
    private String description;
    private int customerEntityId;
    private int active;
    private String documentTypeCode;
    private String comsName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCustomerEntityId() {
        return customerEntityId;
    }

    public void setCustomerEntityId(int customerEntityId) {
        this.customerEntityId = customerEntityId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getComsName() {
        return comsName;
    }

    public void setComsName(String comsName) {
        this.comsName = comsName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("description", description)
                .append("customerEntityId", customerEntityId)
                .append("active", active)
                .append("documentTypeCode", documentTypeCode)
                .append("comsName", comsName)
                .toString();
    }
}
