package com.clevel.selos.model.view.master;

import com.clevel.selos.model.db.master.CustomerEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

public class AddressTypeView implements Serializable{

    private int id;
    private String name;
    private int active;
    private int customerEntityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCustomerEntityId() {
        return customerEntityId;
    }

    public void setCustomerEntityId(int customerEntityId) {
        this.customerEntityId = customerEntityId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("active", active)
                .append("customerEntityId", customerEntityId)
                .toString();
    }
}
