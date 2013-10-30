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
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
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
    private String messageHeader;
    private String message;
    private boolean messageErr;
    private Date date;
    private String dateToShow;
    private  int result;
    public Qualitative(){
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");

        HttpSession session = FacesUtil.getSession(true);
        user = (User)session.getAttribute("user");
        result = 0;

        String page = Util.getCurrentPage();
        log.info("this page :: {} ",page);
        if(page.equals("qualitativeA.jsf")){
            session.setAttribute("workCaseId", new Long(3)) ;    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox

            if(session.getAttribute("workCaseId") != null){
                workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
                log.info("workCaseId :: {} ",workCaseId);
            }

            qualitativeView = qualitativeControl.getQualitativeA(workCaseId);

        } else if(page.equals("qualitativeB.jsf")){
            session.setAttribute("workCaseId", new Long(4)) ;    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox

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

        try{
            qualityLevelList = qualityLevelDAO.findAll();
        }catch (Exception e){
            log.error( "qualityLevelDAO.findAll  error ::: {}" , e.getMessage());
        }
    }

    public void onClickQuality(int clickResult){

        if(clickResult > result){
            result = clickResult;
            onSetQuality(result);
        }else{
            result = result;
            onSetQuality(result);
        }

      log.info("result :: {}" , result);
    }

    public void onSetQuality(int clickResult){
         if(clickResult == 1){
             qualitativeView.setQualityResult("P");
         }else if(clickResult == 2){
             qualitativeView.setQualityResult("SM");
         }else if(clickResult == 3){
             qualitativeView.setQualityResult("SS");
         }else if(clickResult == 4){
             qualitativeView.setQualityResult("D");
         }else if(clickResult == 5){
            qualitativeView.setQualityResult("DL");
         }


        log.info("qualitativeView.getQualityResult :: {}" , qualitativeView.getQualityResult());
    }

    public void onSaveQualitativeA(){
        log.info(" onSaveQualitativeA :::");
        log.info("modeForButton :: {} ",modeForButton);

        try{
            if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                if(qualitativeView.getId() == 0){
                    qualitativeView.setCreateBy(user);
                    qualitativeView.setCreateDate(DateTime.now().toDate());
//                    date = new Date();
//                    log.info("DateTime.now().toDate() ;; {}", DateTimeUtil.getDateTimeStr(date));
//                    dateToShow = DateTimeUtil.getDateTimeStr(date);
                }
                qualitativeControl.saveQualitativeA(qualitativeView,workCaseId,user);
                modeForButton = ModeForButton.EDIT;


            }else  if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                qualitativeView.setModifyBy(user);
                qualitativeView.setModifyDate(DateTime.now().toDate());
                qualitativeControl.saveQualitativeA(qualitativeView,workCaseId,user);

            }

            messageHeader = msg.get("app.header.save.success");
            message =  msg.get("app.qualitativeA.response.save.success");
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        } catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader =  msg.get("app.header.save.failed");

            if(ex.getCause() != null){
                message =  msg.get("app.qualitativeA.response.save.failed")+ " cause : " + ex.getCause().toString();
            } else {
                message =  msg.get("app.qualitativeA.response.save.failed") + ex.getMessage();
            }

            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

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

        try{
            if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                qualitativeControl.saveQualitativeB(qualitativeView, workCaseId,user);
                modeForButton = ModeForButton.EDIT;

            }else  if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                qualitativeControl.saveQualitativeB(qualitativeView, workCaseId,user);
            }

            messageHeader = msg.get("app.header.save.success");
            message =  msg.get("app.qualitativeB.response.save.success");
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        }catch(Exception ex){
            log.error("Exception : {}", ex);
            messageHeader =  msg.get("app.header.save.failed");

            if(ex.getCause() != null){
                message =  msg.get("app.qualitativeB.response.save.failed")+ " cause : " + ex.getCause().toString();
            } else {
                message =  msg.get("app.qualitativeB.response.save.failed") + ex.getMessage();
            }

            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

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

    public boolean isMessageErr() {
        return messageErr;
    }

    public void setMessageErr(boolean messageErr) {
        this.messageErr = messageErr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getDateToShow() {
        return dateToShow;
    }

    public void setDateToShow(String dateToShow) {
        this.dateToShow = dateToShow;
    }
}
