package com.clevel.selos.transform;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.view.CheckMandateDocView;
import com.clevel.selos.model.view.MandateDocView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class CheckMandateDocTransform {
    @Inject
    @NCB
    private Logger log;
    private CheckMandateDocView checkMandateDocView;
    private MandateDoc mandateDoc;
    @Inject
    public CheckMandateDocTransform() {
        init();
    }

    private void init(){
        log.debug("-- init()");
        checkMandateDocView = null;
    }

    public CheckMandateDocView transformToView(final MandateDoc mandateDoc, final Map<String,List<ECMDetail>> listECMDetailMap, final Map<String, MandateDocView> mandateDocViewMap){
        log.debug("-- transformToView");
        init();
//        mandateDocViewMap.values()

        checkMandateDocView = new CheckMandateDocView();
        return checkMandateDocView;
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
