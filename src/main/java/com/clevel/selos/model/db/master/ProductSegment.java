package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_product_segment")
public class ProductSegment {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "credit_category")
    private int creditCategory;

    @Column(name = "active")
    private int active;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreditCategory() {
        return creditCategory;
    }

    public void setCreditCategory(int creditCategory) {
        this.creditCategory = creditCategory;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("creditCategory", creditCategory)
                .append("active", active)
                .append("description", description)
                .append("name", name)
                .toString();
    }
}
