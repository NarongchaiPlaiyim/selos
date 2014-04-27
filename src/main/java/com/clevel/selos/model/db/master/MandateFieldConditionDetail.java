package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_mandate_fields_con_detail")
public class MandateFieldConditionDetail implements Serializable{

    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "mandate_fields_con_id")
    private MandateFieldCondition mandateFieldCondition;

    @ManyToOne
    @JoinColumn(name = "mandate_fields_id")
    private MandateField mandateField;

    @Column(name = "base_value")
    private String baseValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MandateFieldCondition getMandateFieldCondition() {
        return mandateFieldCondition;
    }

    public void setMandateFieldCondition(MandateFieldCondition mandateFieldCondition) {
        this.mandateFieldCondition = mandateFieldCondition;
    }

    public MandateField getMandateField() {
        return mandateField;
    }

    public void setMandateField(MandateField mandateField) {
        this.mandateField = mandateField;
    }

    public String getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(String baseValue) {
        this.baseValue = baseValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mandateFieldCondition", mandateFieldCondition)
                .append("mandateField", mandateField)
                .toString();
    }
}
