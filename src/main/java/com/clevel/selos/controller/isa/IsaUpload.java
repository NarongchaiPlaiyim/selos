package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.IsaBusinessControl;
import com.clevel.selos.controller.isa.download.model.DownloadModel;
import com.clevel.selos.controller.isa.download.service.DownloadService;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "isaUpload")
public class IsaUpload implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    private IsaBusinessControl isaBusinessControl;
    @Inject
    private DownloadService downloadService;
    private StreamedContent streamedContent;
    //dialogMessage
    private String messageHeader;
    private String message;
    private boolean complete;
    private enum Result{Success};
    private List<DownloadModel> downloadModelList;
    @Inject
    public IsaUpload() {

    }

    @PostConstruct
    private void init(){
        log.debug("-- init()");
        onLoadDownloadModel();
    }

    private void onLoadDownloadModel(){
        log.debug("-- onLoadDownloadModel()");
        downloadModelList = new ArrayList<DownloadModel>();
    }

    public void onSubmitExportCSV(){
        log.debug("-- onSubmitExportCSV()");
        RequestContext context = RequestContext.getCurrentInstance();
        complete = true;
        messageHeader = "Export to CSV";
        try {
            final String fullPath = isaBusinessControl.exportProcess();
            if(!Util.isNull(fullPath)){
                streamedContent = downloadService.process(fullPath);
            }
            message = Result.Success.toString();
        } catch (Exception e){
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
            } else {
                message = e.getMessage();
            }
        }
        context.execute("msgBoxSystemMessageDlg.show()");
    }

    public void onSubmitImportCSV(FileUploadEvent event){
        log.debug("-- onSubmitImportCSV()");
        RequestContext context = RequestContext.getCurrentInstance();
        UploadedFile file = null;
        String fileName = null;
        DownloadModel downloadModel = null;
        complete = true;
        messageHeader = "Import to model";
        try {
            file = event.getFile();
            fileName = file.getFileName();
            if(!Util.isNull(fileName) && !Util.isZero(fileName.length())){
                downloadModel = isaBusinessControl.importProcess(file.getInputstream());
                if(!Util.isNull(downloadModel)){
                    downloadModelList.add(downloadModel);
                }
                message = Result.Success.toString();
            }
        } catch (Exception e){
            message = e.getMessage();
        }
        context.execute("msgBoxSystemMessageDlg.show()");
    }

    public void onSubmitDownloadResultFile(final String fullPath){
        log.debug("-- onSubmitDownloadResultFile(fullPath : {})", fullPath);
        RequestContext context = RequestContext.getCurrentInstance();
        messageHeader = "Download .csv";
        try {
            streamedContent = downloadService.process(fullPath);
        } catch (Exception e){
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
            } else {
                message = e.getMessage();
            }
            log.debug("-- ",e);
            context.execute("msgBoxSystemMessageDlg.show()");
        }
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

    public List<DownloadModel> getDownloadModelList() {
        return downloadModelList;
    }

    public void setDownloadModelList(List<DownloadModel> downloadModelList) {
        this.downloadModelList = downloadModelList;
    }

    public StreamedContent getStreamedContent() {
        return streamedContent;
    }

    public void setStreamedContent(StreamedContent streamedContent) {
        this.streamedContent = streamedContent;
    }
}
