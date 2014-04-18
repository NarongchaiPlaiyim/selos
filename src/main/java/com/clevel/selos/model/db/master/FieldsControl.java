package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_fields_control")
public class FieldsControl implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_MST_FIELDS_CONTROL_ID", sequenceName = "SEQ_MST_FIELDS_CONTROL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MST_FIELDS_CONTROL_ID")
    private long id;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "screen_id")
    private int screenId;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @Column(name = "mandate")
    private int mandate;

    @Column(name = "readonly")
    private int readonly;

    @OneToOne
    @JoinColumn(name = "product_group_id")
    ProductGroup productGroup;

    @OneToOne
    @JoinColumn(name = "product_program_id")
    ProductProgram productProgram;

    @Column(name = "special_type_id")
    private int specialTypeId;

    @OneToOne
    @JoinColumn(name = "status_id")
    Status status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getMandate() {
        return mandate;
    }

    public void setMandate(int mandate) {
        this.mandate = mandate;
    }

    public int getReadonly() {
        return readonly;
    }

    public void setReadonly(int readonly) {
        this.readonly = readonly;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
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

    public int getSpecialTypeId() {
        return specialTypeId;
    }

    public void setSpecialTypeId(int specialTypeId) {
        this.specialTypeId = specialTypeId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("fieldName", fieldName).
                append("screenId", screenId).
                append("role", role).
                append("step", step).
                append("mandate", mandate).
                append("readonly", readonly).
                append("productGroup", productGroup).
                append("productProgram", productProgram).
                append("specialTypeId", specialTypeId).
                append("status", status).
                toString();
    }
}
