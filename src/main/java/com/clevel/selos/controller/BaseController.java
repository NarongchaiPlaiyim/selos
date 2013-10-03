package com.clevel.selos.controller;

import com.clevel.selos.model.ManageButton;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "baseController")
public class BaseController implements Serializable {
    @Inject
    Logger log;

    private ManageButton manageButton;

    public BaseController(){

    }

    @PostConstruct
    public void onCreation(){
        log.info("BaseController ::: Creation ");
        manageButton = new ManageButton();
        HttpSession session = FacesUtil.getSession(true);
        long workCasePreScreenId = 0;
        long stepId = 0;

        if(session.getAttribute("workCasePreScreenId") != null){
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
        }
        if(session.getAttribute("stepId") != null){
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
        }
        log.info("BaseController ::: getSession : workcase = {}, stepid = {}",workCasePreScreenId, stepId);

        if(stepId == 2){
            manageButton.setCheckNCBButton(true);
        }
    }

    public ManageButton getManageButton() {
        return manageButton;
    }

    public void setManageButton(ManageButton manageButton) {
        this.manageButton = manageButton;
    }
}
