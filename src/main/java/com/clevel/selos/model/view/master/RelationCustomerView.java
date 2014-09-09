package com.clevel.selos.model.view.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class RelationCustomerView implements Serializable{

    private int id;
    private int customerEntityId;
    private int borrowerTypeCusEntityId;
    private int spouse;
    private int relationId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerEntityId() {
        return customerEntityId;
    }

    public void setCustomerEntityId(int customerEntityId) {
        this.customerEntityId = customerEntityId;
    }

    public int getBorrowerTypeCusEntityId() {
        return borrowerTypeCusEntityId;
    }

    public void setBorrowerTypeCusEntityId(int borrowerTypeCusEntityId) {
        this.borrowerTypeCusEntityId = borrowerTypeCusEntityId;
    }

    public int getSpouse() {
        return spouse;
    }

    public void setSpouse(int spouse) {
        this.spouse = spouse;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customerEntityId", customerEntityId)
                .append("borrowerTypeCusEntityId", borrowerTypeCusEntityId)
                .append("spouse", spouse)
                .append("relationId", relationId)
                .toString();
    }
}
