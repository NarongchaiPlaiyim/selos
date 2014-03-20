package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.*;

import javax.persistence.*;

@Entity
@Table(name = "rel_mandatory")
public class MandatoryRelation {
    @Id
    @Column(name = "id")
    long id;
    @OneToOne
    @JoinColumn(name = "role_id")
    Role role;
    @OneToOne
    @JoinColumn(name = "step_id")
    Step step;
    @OneToOne
    @JoinColumn(name = "status_id")
    Status status;
    @OneToOne
    @JoinColumn(name = "product_group_id")
    ProductGroup productGroup;
    @OneToOne
    @JoinColumn(name = "field_id")
    Field field;
    @Column(name = "display")
    int display;
    @Column(name = "editable")
    int editable;
    @Column(name = "mandatory")
    int mandatory;
    @Column(name = "active")
    int active;

    public MandatoryRelation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public int getEditable() {
        return editable;
    }

    public void setEditable(int editable) {
        this.editable = editable;
    }

    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
//                .append("id", id)
//                .append("role", role)
//                .append("step", step)
//                .append("status", status)
//                .append("productGroup", productGroup)
//                .append("field", field)
//                .append("display", display)
//                .append("editable", editable)
//                .append("mandatory", mandatory)
//                .append("active", active)
//                .toString();
//    }
}
