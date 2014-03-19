package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CheckMandateDocControl;
import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.ECM;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.bpm.BPMInterfaceImpl;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.model.ECMDataResult;
import com.clevel.selos.integration.filenet.ce.connection.CESessionToken;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.view.CheckMandateDocView;
import com.clevel.selos.model.view.CheckMandatoryDocView;
import com.clevel.selos.model.view.CheckOptionalDocView;
import com.clevel.selos.model.view.CheckOtherDocView;
import com.clevel.selos.security.encryption.EncryptionService;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.Util;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "checkMandateDoc")
public class CheckMandateDoc implements Serializable {
    @Inject
    @NCB
    private Logger log;
    @Inject
    private CheckMandateDocControl checkMandateDocControl;
    private CheckMandateDocView checkMandateDocView;
    private int rowIndex;
    private String messageHeader;
    private String message;
    private long workCaseId;

    @Inject
    private ECMInterface ecmInterface;
    @Inject
    private BPMInterfaceImpl bpmInterface;

    public CheckMandateDoc() {
        init();
    }

    private void init(){
//        checkMandateDocView = new CheckMandateDocView();
    }

    @PostConstruct
    public void onCreation() {
        log.info("-- onCreation.");
        String result = null;
        try{
            checkMandateDocView = checkMandateDocControl.getMandateDocView(workCaseId);
            if(!Util.isNull(checkMandateDocView)){
                log.debug("-- MandateDoc.id[{}]", checkMandateDocView.getId());
            } else {
                log.debug("-- Find by work case id = {} CheckMandateDocView is {}   ", workCaseId, checkMandateDocView);
                checkMandateDocView = new CheckMandateDocView();
                log.debug("-- CheckMandateDocView[New] created");
            }
        } catch (ECMInterfaceException e) {
            log.error("-- ECMInterfaceException : {}", e.getMessage());
            result = e.getMessage();
        } catch (Exception e) {
            log.error("-- Exception : {}", e.getMessage());
            result = e.getMessage();
        }

    }

    public void onSaveCheckMandateDoc(){

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
