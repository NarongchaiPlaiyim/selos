package com.clevel.selos.model.db.master;

import com.clevel.selos.model.DocLevel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_mandate_documents")
public class MandateDocument implements Serializable {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "ecm_doc_id")
    private String ecmDocId;

    @Column(name = "description")
    private String description;


    @Column(name = "doc_level", columnDefinition = "int default 1")
    @Enumerated(EnumType.ORDINAL)
    private DocLevel docLevel;

    @ManyToOne
    @JoinColumn(name = "customer_entity", columnDefinition = "int default 0")
    private CustomerEntity customerEntity;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private Step step;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEcmDocId() {
        return ecmDocId;
    }

    public void setEcmDocId(String ecmDocId) {
        this.ecmDocId = ecmDocId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocLevel getDocLevel() {
        return docLevel;
    }

    public void setDocLevel(DocLevel docLevel) {
        this.docLevel = docLevel;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("ecmDocId", ecmDocId)
                .append("description", description)
                .append("docLevel", docLevel)
                .append("step", step)
                .toString();
    }
}
