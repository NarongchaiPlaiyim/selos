package com.clevel.selos.model.view;

import java.io.Serializable;

public class FieldsControlView implements Serializable {
    private long id;
    private String fieldName;
    private boolean isMandate;
    private boolean isReadOnly;

    public FieldsControlView() {
    }

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

    public boolean isMandate() {
        return isMandate;
    }

    public void setMandate(boolean mandate) {
        isMandate = mandate;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }
}
