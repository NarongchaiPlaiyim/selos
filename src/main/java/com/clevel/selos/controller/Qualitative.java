package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.QualitativeControl;
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
import com.clevel.selos.util.Util;
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
    private List<QualityLevel> qualityLevelList;
    private long workCaseId;
    private User user;
    enum ModeForButton{ ADD, EDIT, CANCEL }
    private ModeForButton modeForButton;

    public Qualitative(){

    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");

        HttpSession session = FacesUtil.getSession(true);
        user = userDAO.findById("10001");
        log.info("onSaveQualitativeA ::: user : {}", user);

        String page = Util.getCurrentPage();
        log.info("this page :: {} ",page);
        if(page.equals("qualitativeA.jsf")){
            session.setAttribute("workCaseId", new Long(1)) ;    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox

            if(session.getAttribute("workCaseId") != null){
                workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                log.info("workCaseId :: {} ",workCaseId);
            }

            qualitativeView = qualitativeControl.getQualitativeA(workCaseId);

        } else if(page.equals("qualitativeB.jsf")){
            session.setAttribute("workCaseId", new Long(2)) ;    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox

            if(session.getAttribute("workCaseId") != null){
                workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                log.info("workCaseId :: {} ",workCaseId);
            }

            qualitativeView = qualitativeControl.getQualitativeB(workCaseId);
        }

        if(qualitativeView == null){
            qualitativeView = new QualitativeView();
            modeForButton = ModeForButton.ADD;
        } else{
            modeForButton = ModeForButton.EDIT;
        }

        onLoadSelectList();
    }

    public void onLoadSelectList(){

        if(qualityLevelList == null){
            qualityLevelList = new ArrayList<QualityLevel>();
        }

        qualityLevelList = qualityLevelDAO.findAll();
    }

    public void onSaveQualitativeA(){
        log.info(" onSaveQualitativeA :::");
        log.info("modeForButton :: {} ",modeForButton);
        if(qualitativeView.getId() == 0){
            qualitativeView.setCreateDate(DateTime.now().toDate());
        }

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
            HttpSession session = FacesUtil.getSession(true);
            session.setAttribute("workCaseId", new Long(1)) ;    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox

            qualitativeView.setCreateBy(user);
            qualitativeView.setCreateDate(DateTime.now().toDate());
            qualitativeControl.saveQualitativeA(qualitativeView,workCaseId);
            modeForButton = ModeForButton.EDIT;

        }else  if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
            qualitativeView.setModifyBy(user);
            qualitativeView.setModifyDate(DateTime.now().toDate());
            qualitativeControl.saveQualitativeA(qualitativeView,workCaseId);
        }
    }

    public void onCancelQualitativeA(){
        modeForButton = ModeForButton.CANCEL;
        log.info("modeForButton :: {} ",modeForButton);
        onCreation();
    }



    public void onSaveQualitativeB(){
        log.info(" onSaveQualitativeB :::");
        log.info("modeForButton :: {} ",modeForButton);
        if(qualitativeView.getId() == 0){
            qualitativeView.setCreateDate(DateTime.now().toDate());
        }

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
            HttpSession session = FacesUtil.getSession(true);
            session.setAttribute("workCaseId", new Long(2)) ;    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox

            qualitativeView.setCreateBy(user);
            qualitativeView.setCreateDate(DateTime.now().toDate());
            qualitativeControl.saveQualitativeB(qualitativeView, workCaseId);
            modeForButton = ModeForButton.EDIT;

        }else  if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
            qualitativeView.setModifyBy(user);
            qualitativeView.setModifyDate(DateTime.now().toDate());
            qualitativeControl.saveQualitativeB(qualitativeView, workCaseId);
        }

    }


    public void onCancelQualitativeB(){
        modeForButton = ModeForButton.CANCEL;
        log.info("modeForButton :: {} ",modeForButton);
        onCreation();
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
