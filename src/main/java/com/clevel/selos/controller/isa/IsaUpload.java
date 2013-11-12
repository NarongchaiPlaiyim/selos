package com.clevel.selos.controller.isa;

import com.clevel.selos.integration.SELOS;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
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



    public void uploadUserFile(FileUploadEvent event){
        log.debug("uploadUserFile()");
        Date date= Calendar.getInstance().getTime();

        System.out.println(event.getFile().getFileName() + "  "+file.getSize());


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
