package com.clevel.selos.controller;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "decision")
public class Decision implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    //Business logic

    //DAO

    //Transform

    //Session
    private long workCaseId;
    private long workCasePrescreenId;
    private long stepId;
    private String userId;

    //View


    public Decision() {
    }

    @PostConstruct
    public void onCreation() {

    }

}
