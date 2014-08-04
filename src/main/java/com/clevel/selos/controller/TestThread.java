package com.clevel.selos.controller;

import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "testThread")
public class TestThread implements Serializable {
    @Inject
    @SELOS
    private Logger log;
    private long workCaseId;
    private long workCasePreScreenId;
    private String resultOfWorkCase;
    private String resultOfWorkCasePreScreen;

    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    public TestThread() {

    }

    @PostConstruct
    public void init(){
        log.debug("-- init()");
    }


    public static void main(String[] args) {
        List<String> stringList = null;
//        System.out.println(Util.isSafetyList(stringList));

        stringList = new ArrayList<String>();
//        System.out.println(Util.isSafetyList(stringList));

        stringList = Util.safetyList(null);
//        System.out.println(Util.isSafetyList(stringList));

    }

    public void onSubmitGetWorkCase(){
        log.debug("-- onSubmitGetWorkCase()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                resultOfWorkCase = workCaseDAO.findById(workCaseId).toString();
            }
        });
        thread.start();

        if(thread.getState() == Thread.State.TERMINATED){
            log.debug("-- DONE");
        }
    }

    public void onSubmitGetWorkCasePreScreen(){
        log.debug("-- onSubmitGetWorkCasePreScreen()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                resultOfWorkCasePreScreen = workCasePrescreenDAO.findById(workCasePreScreenId).toString();
            }
        });
        thread.start();

        if(thread.getState() == Thread.State.TERMINATED){
            log.debug("-- DONE");
        }
    }

    public Logger getLog() {
        return log;
    }

    public void setLog(Logger log) {
        this.log = log;
    }

    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }

    public long getWorkCasePreScreenId() {
        return workCasePreScreenId;
    }

    public void setWorkCasePreScreenId(long workCasePreScreenId) {
        this.workCasePreScreenId = workCasePreScreenId;
    }

    public String getResultOfWorkCase() {
        return resultOfWorkCase;
    }

    public void setResultOfWorkCase(String resultOfWorkCase) {
        this.resultOfWorkCase = resultOfWorkCase;
    }

    public String getResultOfWorkCasePreScreen() {
        return resultOfWorkCasePreScreen;
    }

    public void setResultOfWorkCasePreScreen(String resultOfWorkCasePreScreen) {
        this.resultOfWorkCasePreScreen = resultOfWorkCasePreScreen;
    }
}
