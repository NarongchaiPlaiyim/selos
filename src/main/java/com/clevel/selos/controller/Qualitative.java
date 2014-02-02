package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.QualitativeControl;
import com.clevel.selos.dao.master.QualityLevelDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.QualitativeClass;
import com.clevel.selos.model.db.master.QualityLevel;
import com.clevel.selos.model.view.QualitativeView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.sun.istack.internal.Nullable;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "qualitative")
public class Qualitative implements Serializable {
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

    @Inject
    QualityLevelDAO qualityLevelDAO;

    @Inject
    QualitativeControl qualitativeControl;

    private QualitativeView qualitativeView;
    private List<QualityLevel> qualityLevelList;
    private long workCaseId;
    private Hashtable qualitativeValueMap;

    enum ModeForButton {ADD, EDIT, CANCEL}

    private ModeForButton modeForButton;
    private String messageHeader;
    private String message;
    private boolean messageErr;
    private int result;
    private boolean validate;
    private boolean requiredReason;

    public Qualitative() {

    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        HttpSession session = FacesUtil.getSession(true);

        String page = Util.getCurrentPage();
        log.info("this page :: {} ", page);

        qualitativeValueMap = new Hashtable<String, String>();

        if (page.equals("qualitativeA.jsf")) {
            if (session.getAttribute("workCaseId") != null) {
                workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            } else {
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }
            qualitativeView = qualitativeControl.getQualitativeA(workCaseId);
        } else if (page.equals("qualitativeB.jsf")) {
            if (session.getAttribute("workCaseId") != null) {
                workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            } else {
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }
            qualitativeView = qualitativeControl.getQualitativeB(workCaseId);
        }

        if (qualitativeView == null) {
            qualitativeView = new QualitativeView();
            modeForButton = ModeForButton.ADD;
            result = 0;
        } else {
            modeForButton = ModeForButton.EDIT;
            log.info("qualitativeView  EDIT result :: {}", qualitativeView.getQualityResult());
            if (qualitativeView.getQualityResult() != null) {
                onSetQualityToValue(qualitativeView.getQualityResult());
            }
        }

        requiredReason = false;
        if(qualitativeView.getQualityLevel() != null && qualitativeView.getQualityLevel().getId() != 0){
            requiredReason = true;
        }

        onLoadSelectList();
    }

    public void onLoadSelectList() {

        if (qualityLevelList == null) {
            qualityLevelList = new ArrayList<QualityLevel>();
        }

        try {
            qualityLevelList = qualityLevelDAO.findAll();
        } catch (Exception e) {
            log.error("qualityLevelDAO.findAll  error ::: {}", e.getMessage());
        }
    }

    public void onClickQuality() {
        log.debug("qualitativeView : {}", qualitativeView);
        if(qualitativeView != null){
            if(qualitativeView.isProperties_dl1() || qualitativeView.isProperties_dl2() || qualitativeView.isProperties_dl3() || qualitativeView.isProperties_dl4() || qualitativeView.isProperties_dl5() || qualitativeView.isProperties_dl6() || qualitativeView.isProperties_dl7() || qualitativeView.isProperties_dl8() || qualitativeView.isProperties_dl9() || qualitativeView.isProperties_dl10() ||
                    qualitativeView.isProperties_dl11() || qualitativeView.isProperties_dl12() || qualitativeView.isProperties_dl13()){
                result = 5;
            } else {
                if(qualitativeView.isProperties_d1() || qualitativeView.isProperties_d2() || qualitativeView.isProperties_d3() || qualitativeView.isProperties_d4() || qualitativeView.isProperties_d5() || qualitativeView.isProperties_d6() || qualitativeView.isProperties_d7() || qualitativeView.isProperties_d8() || qualitativeView.isProperties_d9() || qualitativeView.isProperties_d10() ||
                        qualitativeView.isProperties_d11() || qualitativeView.isProperties_d12() || qualitativeView.isProperties_d13() || qualitativeView.isProperties_d14() || qualitativeView.isProperties_d15() || qualitativeView.isProperties_d16() || qualitativeView.isProperties_d17() || qualitativeView.isProperties_d18() || qualitativeView.isProperties_d19() || qualitativeView.isProperties_d20()) {
                    result = 4;
                } else {
                    if(qualitativeView.isProperties_ss1() || qualitativeView.isProperties_ss2() || qualitativeView.isProperties_ss3() || qualitativeView.isProperties_ss4() || qualitativeView.isProperties_ss5() || qualitativeView.isProperties_ss6() || qualitativeView.isProperties_ss7() || qualitativeView.isProperties_ss8() || qualitativeView.isProperties_ss9() || qualitativeView.isProperties_ss10() ||
                            qualitativeView.isProperties_ss11() || qualitativeView.isProperties_ss12() || qualitativeView.isProperties_ss13() || qualitativeView.isProperties_ss14() || qualitativeView.isProperties_ss15() || qualitativeView.isProperties_ss16() || qualitativeView.isProperties_ss17() || qualitativeView.isProperties_ss18() || qualitativeView.isProperties_ss19() || qualitativeView.isProperties_ss20() ||
                            qualitativeView.isProperties_ss21() || qualitativeView.isProperties_ss22()){
                        result = 3;
                    } else {
                        if(qualitativeView.isProperties_sm1() || qualitativeView.isProperties_sm2() || qualitativeView.isProperties_sm3() || qualitativeView.isProperties_sm4() || qualitativeView.isProperties_sm5() || qualitativeView.isProperties_sm6() || qualitativeView.isProperties_sm7() || qualitativeView.isProperties_sm8() || qualitativeView.isProperties_sm9() || qualitativeView.isProperties_sm10() ||
                                qualitativeView.isProperties_sm11() || qualitativeView.isProperties_sm12() || qualitativeView.isProperties_sm13()){
                            result = 2;
                        } else {
                            if(qualitativeView.isProperties_p1() || qualitativeView.isProperties_p2() || qualitativeView.isProperties_p3()){
                                result = 1;
                                //requiredReason = true;
                            } else {
                                result = 0;
                            }
                        }
                    }
                }
            }
        }
        log.info("result :: {}", result);
        log.debug("requiredReason : {}", requiredReason);
    }

    public void onSelectQualityReason(){
        if(Long.toString(qualitativeView.getQualityLevel().getId()) != null && qualitativeView.getQualityLevel().getId() != 0){
            requiredReason = true;
        }
    }

    public void onSetQualityToSave() {
        log.debug("onSetQualityToSave : result : {}", result);
        if(result != 0){
            QualitativeClass[] qualitativeClasses = QualitativeClass.values();
            for (QualitativeClass qualitativeValue : qualitativeClasses) {
                if (result == qualitativeValue.value()) {
                    qualitativeView.setQualityResult(qualitativeValue.toString());
                    break;
                }
            }
        } else {
            qualitativeView.setQualityResult("");
        }

        log.info("qualitativeView.getQualityResult :: {}", qualitativeView.getQualityResult());
    }

    public void onSetQualityToValue(String quality) {
        log.info("onSetQualityToValue ::: quality  {}", quality);

        try {
            result = QualitativeClass.valueOf(quality).value();
        } catch (IllegalArgumentException iaEx) {
            log.info("Qualitative class not true :: IllegalArgumentException :: ");
            result = 0;
        }

        log.info("result in mode edit :: {}", result);
    }

    public void onSaveQualitativeA() {
        log.info("onSaveQualitativeA :::");
        log.info("modeForButton :: {} ", modeForButton);
        boolean validate = true;
        if (requiredReason) {
            if(qualitativeView.getQualityLevel().getId() != 0){
                validate = true;
            } else {
                validate = false;
            }
        }
        log.debug("onSaveQualitativeA ::: validation : {}", validate);
        if(validate){
            try {
                onSetQualityToSave();
                qualitativeControl.saveQualitativeA(qualitativeView, workCaseId);
                modeForButton = ModeForButton.EDIT;
                messageHeader = msg.get("app.header.save.success");
                message = msg.get("app.qualitativeA.response.save.success");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                onCreation();
            } catch (Exception ex) {
                log.error("Exception : {}", ex);
                messageHeader = msg.get("app.header.save.failed");

                if (ex.getCause() != null) {
                    message = msg.get("app.qualitativeA.response.save.failed ") + " cause : " + ex.getCause().toString();
                } else {
                    message = msg.get("app.qualitativeA.response.save.failed ") + ex.getMessage();
                }
                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

            }
        }
    }

    public void onCancelQualitativeA() {
        modeForButton = ModeForButton.CANCEL;
        log.info("modeForButton :: {} ", modeForButton);
        onCreation();
    }

    public void onSaveQualitativeB() {
        log.info("onSaveQualitativeB :::");
        log.info("modeForButton :: {} ", modeForButton);
        boolean validate = true;
        if (requiredReason) {
            if(qualitativeView.getQualityLevel().getId() != 0){
                validate = true;
            } else {
                validate = false;
            }
        }

        if(validate){
            try {
                onSetQualityToSave();
                qualitativeControl.saveQualitativeB(qualitativeView, workCaseId);
                messageHeader = msg.get("app.header.save.success");
                message = msg.get("app.qualitativeB.response.save.success");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                onCreation();
            } catch (Exception ex) {
                log.error("Exception : {}", ex);
                messageHeader = msg.get("app.header.save.failed");

                if (ex.getCause() != null) {
                    message = msg.get("app.qualitativeB.response.save.failed ") + " cause : " + ex.getCause().toString();
                } else {
                    message = msg.get("app.qualitativeB.response.save.failed ") + ex.getMessage();
                }

                messageErr = true;
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

            }
        }

    }

    public void onCancelQualitativeB() {
        modeForButton = ModeForButton.CANCEL;
        log.info("modeForButton :: {} ", modeForButton);
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

    public boolean isRequiredReason() {
        return requiredReason;
    }

    public void setRequiredReason(boolean requiredReason) {
        this.requiredReason = requiredReason;
    }
}
