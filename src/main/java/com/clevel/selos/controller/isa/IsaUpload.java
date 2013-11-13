package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.IsaUploadService;
import com.clevel.selos.integration.SELOS;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "isaUpload")
public class IsaUpload implements Serializable{

    @Inject
    @SELOS
    Logger log;

    @Inject
    IsaUploadService isaUploadService;

    public IsaUpload(){

    }

    @PostConstruct
    public void onCreate(){

        testlist=new ArrayList<String>();
        testlist.add("sd"); testlist.add("sd");testlist.add("sd");testlist.add("sd");testlist.add("sd");testlist.add("sd");

        System.out.println(testlist.size());
    }

    private UploadedFile file;
    private List<String> testlist;

    private final static String UPLOAD_FOLDER = "_userUpload";
    private final static SimpleDateFormat dateFormatFile = new SimpleDateFormat("yyyyMMddHHmmssSSS");



    public void uploadUserFile(){
        log.debug("uploadUserFile()");
        System.out.println("upload");

        if(file!=null){
        FacesMessage facesMessage=new FacesMessage("Succesful",file.getFileName()+" is upload");
        FacesContext.getCurrentInstance().addMessage(null,facesMessage);
        Date now = Calendar.getInstance().getTime();
        String userUploadId = dateFormatFile.format(now) + UPLOAD_FOLDER;
        System.out.println(userUploadId+file.getSize());
        }else {
            System.out.println("file is null");
        }
//        String result = importService.uploadDocFiles(request, userUploadId);
//
//        String tmpDir = importService.getTmpFolder(userUploadId);
//
        File file = new File("C:\\Users\\sahawat\\Desktop\\test");
        if(file.isDirectory()){
//            isaUploadService.processUserUploadFiles(tmpDir, file.listFiles()[0].getName());
        }

    }



    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<String> getTestlist() {
        return testlist;
    }

    public void setTestlist(List<String> testlist) {
        this.testlist = testlist;
    }
}
