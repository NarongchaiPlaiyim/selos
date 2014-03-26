package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CheckMandateDocControl;
import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.ECMInterface;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.view.CheckMandateDocView;
import com.clevel.selos.model.view.CheckMandatoryDocView;
import com.clevel.selos.model.view.CheckOptionalDocView;
import com.clevel.selos.model.view.CheckOtherDocView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.CheckMandateDocCustTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "checkMandateDoc")
public class CheckMandateDoc implements Serializable {
    @Inject
    @NCB
    private Logger log;
    @Inject
    @NormalMessage
    private Message msg;
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

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if(( (Long)session.getAttribute("workCaseId") != 0 || (Long)session.getAttribute("workCasePreScreenId") != 0 ) &&
                (Long)session.getAttribute("stepId") != 0){
            checkSession = true;
        }

        return checkSession;
    }

    @PostConstruct
    public void onCreation() {
        log.info("-- onCreation.");
//        init();
        HttpSession session = FacesUtil.getSession(true);
        if(true){//checkSession(session)){

            if(false){//(Long)session.getAttribute("workCaseId") != 0){
                workCaseId = Long.valueOf(""+session.getAttribute("workCaseId"));
            }

            String result = null;
            checkMandateDocView = null;
            try{
                workCaseId = 481L;
                roleId = 1;
                checkMandateDocView = checkMandateDocControl.getMandateDocView(workCaseId);
                if(!Util.isNull(checkMandateDocView)){
                    log.debug("-- MandateDoc.id[{}]", checkMandateDocView.getId());
                    checkMandateDocView.readOnly();
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
        } else {
            //TODO show message box
        }

    }

    public void onSaveCheckMandateDoc(){
        log.debug("-- onSaveCheckMandateDoc.");
        try {
            checkMandateDocControl.onSaveMandateDoc(checkMandateDocView, workCaseId);
            messageHeader = "success";
            message = "success";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex){
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.appraisal.request.message.header.save.fail");
            if(ex.getCause() != null){
                message = msg.get("app.appraisal.request.message.body.save.fail") + " cause : "+ ex.getCause().toString();
            } else {
                message = msg.get("app.appraisal.request.message.body.save.fail") + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

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

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
