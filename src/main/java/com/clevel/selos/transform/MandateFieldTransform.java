package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateField;
import com.clevel.selos.model.view.MandateFieldView;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MandateFieldTransform extends Transform{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    private ActionTransform actionTransform;
    @Inject
    private StepTransform stepTransform;

    @Inject
    public MandateFieldTransform(){

    }

    public MandateFieldView transformToView(MandateField mandateField){
        logger.debug("-- begin transformToView mandateField{}", mandateField);
        if(mandateField == null)
            return null;

        MandateFieldView mandateFieldView = new MandateFieldView();
        mandateFieldView.setId(mandateField.getId());
        mandateFieldView.setClassName(mandateField.getClassName());
        mandateFieldView.setFieldName(mandateField.getFieldName());
        mandateFieldView.setFieldDesc(mandateField.getFieldDescription());
        mandateFieldView.setPage(mandateField.getPage());
        mandateFieldView.setMinValue(mandateField.getMinValue());
        mandateFieldView.setMaxValue(mandateField.getMaxValue());
        mandateFieldView.setMatchedValue(mandateField.getMatchedValue());
        mandateFieldView.setNotMatchedValue(mandateField.getNotMatchedValue());
        logger.debug("-- end transformToView return mandateFieldView: {}", mandateFieldView);
        return mandateFieldView;
    }

}
