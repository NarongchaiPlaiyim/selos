package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_document_type")
public class DocumentType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "description", length = 100)
    private String description;
    @OneToOne
    @JoinColumn(name = "customerentity_id")
    private CustomerEntity customerEntity;
    @Column(name = "active")
    private int active;
    @Column(name = "document_type_code", length = 5)
    private String documentTypeCode;
    @Column(name = "coms_name", length = 20)
    private String comsName;

    public DocumentType() {
    }

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

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
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
                .append("customerEntity", customerEntity)
                .append("active", active)
                .append("documentTypeCode", documentTypeCode)
                .append("comsName", comsName)
                .toString();
    }
}
