package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.MandateDocDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.view.CheckMandateDocView;
import com.clevel.selos.transform.CheckMandateDocTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class CheckMandateDocControl extends BusinessControl{
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private MandateDocDAO mandateDocDAO;
    private CheckMandateDocView checkMandateDocView;
    private MandateDoc mandateDoc;
    @Inject
    private CheckMandateDocTransform checkMandateDocTransform;
    @Inject
    public CheckMandateDocControl() {

    }

    private void init(){
        log.debug("-- init()");
        checkMandateDocView = null;
        mandateDoc = null;
    }

    private CheckMandateDocView  getMandateDoc(final long workCaseId){
        log.info("-- getMandateDoc WorkCaseId : {}", workCaseId);
        mandateDoc = mandateDocDAO.findByWorkCaseId(workCaseId);
        if(!Util.isNull(mandateDoc)){
            checkMandateDocView = checkMandateDocTransform.transformToView(mandateDoc);

        } else {
            log.debug("-- Find by work case id = {} MandateDoc is {}   ", workCaseId, checkMandateDocView);
        }
        return checkMandateDocView;
    }

    private void onSaveMandateDoc(final CheckMandateDocView checkMandateDocView, final long workCaseId){
        log.info("-- onSaveMandateDoc ::: workCaseId : {}", workCaseId);
        checkMandateDocTransform.transformToModel(checkMandateDocView);
    }

}
