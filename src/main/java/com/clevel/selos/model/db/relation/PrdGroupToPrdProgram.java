package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.ProductProgram;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_prdgroup_prdprogram")
public class PrdGroupToPrdProgram implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @OneToOne
    @JoinColumn(name = "productgroup_id")
    private ProductGroup productGroup;
    @OneToOne
    @JoinColumn(name = "productprogram_id")
    private ProductProgram productProgram;

    @Column(name = "add_existing_credit")
    private int addExistingCredit;

    @Column(name = "add_propose_credit")
    private int addProposeCredit;

    @Column(name = "active")
    private int active;

    public PrdGroupToPrdProgram() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public int getAddExistingCredit() {
        return addExistingCredit;
    }

    public void setAddExistingCredit(int addExistingCredit) {
        this.addExistingCredit = addExistingCredit;
    }

    public int getAddProposeCredit() {
        return addProposeCredit;
    }

    public void setAddProposeCredit(int addProposeCredit) {
        this.addProposeCredit = addProposeCredit;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("productGroup", productGroup)
                .append("productProgram", productProgram)
                .append("addExistingCredit", addExistingCredit)
                .append("addProposeCredit", addProposeCredit)
                .append("active", active)
                .toString();
    }
}
