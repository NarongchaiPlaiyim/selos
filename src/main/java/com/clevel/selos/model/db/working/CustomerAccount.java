package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.DocumentType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_customer_account")
public class CustomerAccount implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CUST_ACC_ID", sequenceName = "SEQ_WRK_CUST_ACC_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CUST_ACC_ID")
    long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @OneToOne
    @JoinColumn(name = "document_type_id")
    DocumentType documentType;

    @Column(name = "id_number")
    String idNumber;

    public CustomerAccount(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customer", customer)
                .append("documentType", documentType)
                .append("idNumber", idNumber)
                .toString();
    }
}
