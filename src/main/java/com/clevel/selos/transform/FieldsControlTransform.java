package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.FieldsControl;
import com.clevel.selos.model.view.FieldsControlView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FieldsControlTransform extends Transform {

    @Inject
    public FieldsControlTransform() {
    }

    public FieldsControlView transformToView(FieldsControl fieldsControl) {
        FieldsControlView fieldsControlView = new FieldsControlView();
        if (fieldsControl == null) {
            return fieldsControlView;
        }

        fieldsControlView.setId(fieldsControl.getId());
        if (fieldsControl.getFieldName()  != null)
        	fieldsControlView.setFieldName(fieldsControl.getFieldName().trim());

        if(fieldsControl.getMandate() == 1){
            fieldsControlView.setMandate(true);
        } else {
            fieldsControlView.setMandate(false);
        }
        if(fieldsControl.getReadonly() == 1){
            fieldsControlView.setReadOnly(true);
        } else {
            fieldsControlView.setReadOnly(false);
        }

        return fieldsControlView;
    }

    public List<FieldsControlView> transformToViewList(List<FieldsControl> fieldsControlList) {
        List<FieldsControlView> fieldsControlViewList = new ArrayList<FieldsControlView>();

        if (fieldsControlList == null) {
            return fieldsControlViewList;
        }

        for(FieldsControl fieldsControl : fieldsControlList){
            FieldsControlView fieldsControlView = transformToView(fieldsControl);
            fieldsControlViewList.add(fieldsControlView);
        }

        return fieldsControlViewList;
    }
}
