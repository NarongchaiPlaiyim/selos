package com.clevel.selos.model.db.master;

import com.clevel.selos.model.db.relation.RelationCustomer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "mst_relation")
public class Relation implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "priority", length = 1)
    private int priority;
    @Column(name = "active")
    private int active;
    @OneToMany(mappedBy = "relation")
    private List<RelationCustomer> relationCustomerList;


    public Relation() {
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int piority) {
        this.priority = piority;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public List<RelationCustomer> getRelationCustomerList() {
        return relationCustomerList;
    }

    public void setRelationCustomerList(List<RelationCustomer> relationCustomerList) {
        this.relationCustomerList = relationCustomerList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("description", description).
                append("active", active).
                toString();
    }
}
