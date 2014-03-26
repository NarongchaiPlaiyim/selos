package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CheckMandateDocControl;
import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.view.CheckMandateDocView;
import com.clevel.selos.model.view.CheckMandatoryDocView;
import com.clevel.selos.model.view.CheckOptionalDocView;
import com.clevel.selos.model.view.CheckOtherDocView;
import com.clevel.selos.transform.CheckMandateDocCustTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "checkMandateDoc")
public class CheckMandateDoc implements Serializable {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private CheckMandateDocControl checkMandateDocControl;
    private CheckMandateDocView checkMandateDocView;

    private int rowIndex;
    private String messageHeader;
    private String message;
    private long workCaseId;
    private int roleId;

    @Inject
    CheckMandateDocCustTransform checkMandateDocCustTransform;

    @Inject
    public CheckMandateDoc() {
//        init();
    }

    private void init(){
        log.debug("-- init()");
        checkMandateDocView = new CheckMandateDocView();
    }

    @PostConstruct
    public void onCreation() {
        log.info("-- onCreation.");
        init();
        String result = null;
        checkMandateDocView = null;
        try{
            workCaseId = 161L;
            roleId = 1;

//            checkMandateDocView = checkMandateDocControl.MakefileMandateDoc();

            checkMandateDocView = checkMandateDocControl.getDetail(161);
//
//            checkMandateDocView = new CheckMandateDocView();
//            checkMandateDocView.readOnly();
//            checkMandateDocView.setMandatoryDocumentsList(mandatoryDocumentsList);

//            checkMandateDocView = checkMandateDocControl.getMandateDocView(workCaseId, roleId);
//            if(!Util.isNull(checkMandateDocView)){
//                log.debug("-- MandateDoc.id[{}]", checkMandateDocView.getId());
//            } else {
//                log.debug("-- Find by work case id = {} CheckMandateDocView is {}   ", workCaseId, checkMandateDocView);
//                checkMandateDocView = new CheckMandateDocView();
//                checkMandateDocView.readOnly();
//                log.debug("-- CheckMandateDocView[New] created");
//            }
        } catch (ECMInterfaceException e) {
            log.error("-- ECMInterfaceException : {}", e.getMessage());
            result = e.getMessage();
        } catch (Exception e) {
            log.error("-- Exception : {}", e.getMessage());
            result = e.getMessage();
        }
    }

    public void onSaveCheckMandateDoc(){
        log.debug("--onSaveCheckMandateDoc.");
        checkMandateDocControl.onSaveMandateDoc(checkMandateDocView,161);
    }

    public void onCancelCheckMandateDoc(){

    }

    public CheckMandateDocView getCheckMandateDocView() {
        return checkMandateDocView;
    }

    public void setCheckMandateDocView(CheckMandateDocView checkMandateDocView) {
        this.checkMandateDocView = checkMandateDocView;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
