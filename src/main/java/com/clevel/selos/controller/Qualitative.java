package com.clevel.selos.controller;

import com.clevel.selos.busiensscontrol.QualitativeControl;
import com.clevel.selos.dao.master.QualityLevelDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.master.QualityLevel;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.QualitativeView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "qualitative")
public class Qualitative {
    @Inject
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

    @Inject
    QualityLevelDAO qualityLevelDAO;

    @Inject
    QualitativeControl qualitativeControl;

    @Inject
    UserDAO userDAO;

    @Inject
    WorkCaseDAO workCaseDAO ;

    private QualitativeView qualitativeView;
    private String modeForButton ;
    private List<QualityLevel> qualityLevelList;


    public Qualitative(){

    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        modeForButton = "add";

        if(qualitativeView == null){
            qualitativeView = new QualitativeView();
        }

        if(qualityLevelList == null){
            qualityLevelList = new ArrayList<QualityLevel>();
        }

//        qualityLevelList = qualityLevelDAO.findAll();
    }


    public void onSaveQualitativeA(){
        log.info(" onSaveQualitativeA :::");
        User user = userDAO.findById("1");
        log.info("onSaveQualitativeA ::: user : {}", user);
//        WorkCase workCase  = workCaseDAO.findById(new Long(1));
//        log.info("onSaveQualitativeA ::: workCase : {}", workCase);

        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("workCaseId", 1);
        QualitativeView qualitativeView = new  QualitativeView();

//        QualityLevel qualityLevel = qualityLevelDAO.findById(qualitativeView.getQualityLevel().getId());

        if(qualitativeView.getId() == 0){
            qualitativeView.setCreateDate(DateTime.now().toDate());
        }

        qualitativeView.setCreateBy(user);

        long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
//        qualitativeView.setQualityLevel(qualityLevel);

        log.info("qualitativeView :: {} ",qualitativeView.toString());
        qualitativeControl.saveQualitativeA(qualitativeView,workCaseId);
    }

    public void onSaveQualitativeB(){
        log.info(" onSaveQualitativeB :::");
        User user = userDAO.findById("1");
        log.info("onSaveQualitativeB ::: user : {}", user);

        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("workCaseId", 1);
        QualitativeView qualitativeView = new  QualitativeView();

        QualityLevel qualityLevel = qualityLevelDAO.findById(qualitativeView.getQualityLevel().getId());

        if(qualitativeView.getId() == 0){
            qualitativeView.setCreateDate(DateTime.now().toDate());
        }

        qualitativeView.setCreateBy(user);

        long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        qualitativeView.setQualityLevel(qualityLevel);
        qualitativeControl.saveQualitativeB(qualitativeView,workCaseId);
    }

    public QualitativeView getQualitativeView() {
        return qualitativeView;
    }

    public void setQualitativeView(QualitativeView qualitativeView) {
        this.qualitativeView = qualitativeView;
    }

    public List<QualityLevel> getQualityLevelList() {
        return qualityLevelList;
    }

    public void setQualityLevelList(List<QualityLevel> qualityLevelList) {
        this.qualityLevelList = qualityLevelList;
    }
}
