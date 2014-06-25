package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.IsaBusinessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "isaUpload")
public class IsaUpload implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    private IsaBusinessControl isaBusinessControl;

    //dialogMessage
    private String messageHeader;
    private String message;
    private boolean complete;
    private enum Result{Success};
    public IsaUpload() {

    }

    @PostConstruct
    private void init(){
        log.debug("-- init()");
    }

    public void onSubmitExportCSV(){
        log.debug("-- onSubmitExportCSV()");
        RequestContext context = RequestContext.getCurrentInstance();
        complete = true;
        messageHeader = "Export to CSV";
        try {
            isaBusinessControl.exportProcess();
            message = Result.Success.toString();
        } catch (Exception e){
            message = e.getMessage();
        }
        context.execute("msgBoxSystemMessageDlg.show()");
    }

    public void onSubmitImportCSV(FileUploadEvent event){
        log.debug("-- onSubmitImportCSV()");
        RequestContext context = RequestContext.getCurrentInstance();
        UploadedFile file = null;
        String fileName = null;
        complete = true;
        messageHeader = "Import to model";
        try {
            file = event.getFile();
            fileName = file.getFileName();
            if(!Util.isNull(fileName) && !Util.isZero(fileName.length())){
                isaBusinessControl.importProcess(file.getInputstream());
                message = Result.Success.toString();
            }
        } catch (Exception e){
            message = e.getMessage();
        }
        context.execute("msgBoxSystemMessageDlg.show()");
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

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
