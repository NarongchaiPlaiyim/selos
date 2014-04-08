package com.clevel.selos.model.db.master;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_mandate_fields")
public class MandateField implements Serializable{
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @ManyToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @Column(name = "class_name", length = 200)
    private String className;

    @Column(name = "field_name", length = 50)
    private String fieldName;

    @Column(name = "field_description", length = 100)
    private String fieldDescription;

    @Column(name = "page_name", length = 100)
    private String page;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("action", action)
                .append("step", step)
                .append("className", className)
                .append("fieldName", fieldName)
                .append("fieldDescription", fieldDescription)
                .append("page", page)
                .toString();
    }
}
