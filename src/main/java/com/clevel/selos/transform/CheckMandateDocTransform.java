package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.view.CheckMandateDocView;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CheckMandateDocTransform extends Transform {
    @Inject
    @SELOS
    private Logger log;
    private CheckMandateDocView checkMandateDocView;
    private MandateDoc mandateDoc;
    @Inject
    public CheckMandateDocTransform() {

    }

    public CheckMandateDocView transformToView(final MandateDoc mandateDoc){
        checkMandateDocView = new CheckMandateDocView();
        return checkMandateDocView;
    }

    public MandateDoc transformToModel(final CheckMandateDocView checkMandateDocView){
        mandateDoc = new MandateDoc();
        return mandateDoc;
    }
}
