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
        mandateFieldView.setActionView(actionTransform.transformToView(mandateField.getAction()));
        mandateFieldView.setClassName(mandateField.getClassName());
        mandateFieldView.setFieldName(mandateField.getFieldName());
        mandateFieldView.setPage(mandateField.getPage());
        mandateFieldView.setStepView(stepTransform.transformToView(mandateField.getStep()));
        logger.debug("-- end return mandateFieldView: {}", mandateFieldView);
        return mandateFieldView;
    }

}
