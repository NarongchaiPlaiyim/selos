package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.view.FieldsControlView;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class BaseController implements Serializable {
    @Inject
    MandatoryFieldsControl mandatoryFieldsControl;

    private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
    private final HashMap<String, FieldsControlView> dialogFieldMap = new HashMap<String, FieldsControlView>();

    private void loadFieldControl(long workCaseId) {
        List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.ContactRecord);
        List<FieldsControlView> dialogFields = mandatoryFieldsControl.getFieldsControlView(workCaseId, Screen.CallingRecordDialog);
        fieldMap.clear();
        dialogFieldMap.clear();
        for (FieldsControlView field : fields) {
            fieldMap.put(field.getFieldName(), field);
        }
        for (FieldsControlView field : dialogFields) {
            dialogFieldMap.put(field.getFieldName(), field);
        }

    }
    public String mandate(String name) {
        FieldsControlView field = fieldMap.get(name);
        if (field == null)
            return "";
        return field.isMandate() ? " *" : "";
    }

    public boolean isDisabled(String name) {
        FieldsControlView field = fieldMap.get(name);
        if (field == null)
            return false;
        return field.isReadOnly();
    }

    public boolean isDialogMandate(String name) {
        FieldsControlView field = dialogFieldMap.get(name);
        if (field == null)
            return false;
        return field.isMandate();
    }

    public boolean isDialogDisable(String name) {
        FieldsControlView field = dialogFieldMap.get(name);
        if (field == null)
            return false;
        return field.isReadOnly();
    }
}
