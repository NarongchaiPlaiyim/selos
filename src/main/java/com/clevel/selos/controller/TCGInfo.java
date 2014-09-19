package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.businesscontrol.TCGInfoControl;
import com.clevel.selos.dao.master.PotentialCollateralDAO;
import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.MessageDialogSeverity;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import com.clevel.selos.model.view.TCGDetailView;
import com.clevel.selos.model.view.TCGView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "tcgInfo")
public class TCGInfo extends BaseController {

    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    private List<TCGDetailView> TCGDetailViewList;
    private TCGDetailView tcgDetailView;
    private TCGDetailView selectCollateralItem;
    private TCGView TCGView;
    private int rowIndex;
    private Long workCaseId;

    enum ModeForButton {ADD, EDIT}

    private ModeForButton modeForButton;

    enum ModeForDB {ADD_DB, EDIT_DB, CANCEL_DB}

    private ModeForDB modeForDB;
    private String messageHeader;
    private String message;
    private String messageSeverity;
    private boolean messageErr;

    private List<PotentialCollateral> potentialCollateralList;
    private List<PotentialColToTCGCol> potentialColToTCGColList;

    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    private PotentialColToTCGColDAO potentialColToTCGColDAO;
    @Inject
    private TCGCollateralTypeDAO tcgCollateralTypeDAO;

    @Inject
    TCGInfoControl tcgInfoControl;
    @Inject
    private ExSummaryControl exSummaryControl;

    public TCGInfo() {
    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            //TODO Check valid step
            log.debug("preRender ::: Check valid stepId");
        }else{
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        HttpSession session = FacesUtil.getSession(false);
        if(checkSession(session)){
            workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
            TCGView = tcgInfoControl.getTCGView(workCaseId);
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");
            loadFieldControl(workCaseId, Screen.TCG_INFO, ownerCaseUserId);
            if(TCGView != null){
                TCGDetailViewList = tcgInfoControl.getTcgDetailViewList(TCGView);
                modeForDB = ModeForDB.EDIT_DB;
            }else{
                TCGView = new TCGView();
                modeForDB = ModeForDB.ADD_DB;
            }

            if(tcgDetailView == null)
                tcgDetailView = new TCGDetailView();


            if(TCGDetailViewList == null)
                TCGDetailViewList = new ArrayList<TCGDetailView>();

            potentialCollateralList = potentialCollateralDAO.findAll();
        }
    }

    public void onChangePotentialCollateralType() {
        if (tcgDetailView.getPotentialCollateral().getId() != 0) {
            potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(tcgDetailView.getPotentialCollateral().getId());
            if (potentialColToTCGColList == null) {
                potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
            }
        }
    }

    public void onAddCollateralDetail() {
        modeForButton = ModeForButton.ADD;
        tcgDetailView = new TCGDetailView();
        tcgDetailView.reset();
    }

    public void onEditCollateralDetail() {
        log.info("onEditCollateralDetail ::: rowIndex ::: {} ", rowIndex);
        modeForButton = ModeForButton.EDIT;
        Cloner cloner = new Cloner();
        tcgDetailView = new TCGDetailView();
        tcgDetailView = cloner.deepClone(selectCollateralItem);
        if(tcgDetailView.getPotentialCollateral() != null && tcgDetailView.getPotentialCollateral().getId() != 0){
            potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(tcgDetailView.getPotentialCollateral().getId());
            if (potentialColToTCGColList == null) {
                potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
            }
        } else {
            potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
        }
    }

    public void onSaveCollateralDetail() {
        log.info("onSaveCollateralDetail ::: modeForButton : {}", modeForButton);
        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete;
        if (tcgDetailView.getPotentialCollateral().getId() != 0 && tcgDetailView.getTcgCollateralType().getId() != 0) {
            if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                PotentialCollateral potentialCollateralSave = potentialCollateralDAO.findById(tcgDetailView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralTypeSave = tcgCollateralTypeDAO.findById(tcgDetailView.getTcgCollateralType().getId());
                TCGDetailView tcgDetailViewSave = new TCGDetailView();
                tcgDetailViewSave.setPotentialCollateral(potentialCollateralSave);
                tcgDetailViewSave.setTcgCollateralType(tcgCollateralTypeSave);
                tcgDetailViewSave.setAppraisalAmount(tcgDetailView.getAppraisalAmount());
                tcgDetailViewSave.setLtvValue(BigDecimal.ZERO);
                tcgDetailViewSave.setProposeInThisRequest(tcgDetailView.getProposeInThisRequest());
                TCGDetailViewList.add(tcgDetailViewSave);
            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                PotentialCollateral potentialCollateralSave = potentialCollateralDAO.findById(tcgDetailView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralTypeSave = tcgCollateralTypeDAO.findById(tcgDetailView.getTcgCollateralType().getId());
                TCGDetailViewList.get(rowIndex).setPotentialCollateral(potentialCollateralSave);
                TCGDetailViewList.get(rowIndex).setTcgCollateralType(tcgCollateralTypeSave);
                TCGDetailViewList.get(rowIndex).setAppraisalAmount(tcgDetailView.getAppraisalAmount());
                TCGDetailViewList.get(rowIndex).setLtvValue(BigDecimal.ZERO);
                TCGDetailViewList.get(rowIndex).setProposeInThisRequest(tcgDetailView.getProposeInThisRequest());
            } else {
                log.info("onSaveCollateralDetail ::: Undefined modeForButton !!");
            }
            complete = true;
            calculate();
        } else {
            log.info("onSaveCollateralDetail ::: Validation Failed.");
            complete = false;
        }
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteTcgDetail() {
        TCGDetailViewList.remove(selectCollateralItem);
        calculate();
    }

    public void calculate(){
        if (TCGDetailViewList != null && TCGDetailViewList.size() > 0) {
            TCGView.setSumAppraisalAmount(tcgInfoControl.toCalculateSumAppraisalValue(TCGDetailViewList));
            TCGView.setSumLtvValue(tcgInfoControl.toCalculateSumLtvValue(TCGDetailViewList));
            TCGView.setSumInThisAppraisalAmount(tcgInfoControl.toCalculateSumAppraisalInThis(TCGDetailViewList));
            TCGView.setSumInThisLtvValue(tcgInfoControl.toCalculateSumLtvInThis(TCGDetailViewList));
        } else {
            TCGView.setSumAppraisalAmount(BigDecimal.ZERO);
            TCGView.setSumLtvValue(BigDecimal.ZERO);
            TCGView.setSumInThisAppraisalAmount(BigDecimal.ZERO);
            TCGView.setSumInThisLtvValue(BigDecimal.ZERO);
        }
    }

    public void onSaveTcgInfo() {
        log.info("onSaveTcgInfo ::: modeForDB  {}", modeForDB);
        try {
            tcgInfoControl.saveTCGInfo(TCGView, TCGDetailViewList, workCaseId);
            exSummaryControl.calForTCG(workCaseId);

            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.tcg.response.save.success");
            messageSeverity = MessageDialogSeverity.INFO.severity();
            onCreation();
        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.messageHeader.exception");
            message = Util.getMessageException(ex);
            messageSeverity = MessageDialogSeverity.ALERT.severity();
            messageErr = true;
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onCancelTcgInfo() {
        modeForDB = ModeForDB.CANCEL_DB;
        for (int i = 0; i < TCGDetailViewList.size(); i++) {
            TCGDetailViewList.remove(TCGDetailViewList.get(i));
        }
        onCreation();
    }

    public void onChangeTCG() {
        if(TCGView.getTCG() == 2){ // yes
            setMandateValue("requestLimitRequiredTCG",true);
        } else {
            setMandateValue("requestLimitRequiredTCG",false);
        }
    }

    public List<TCGDetailView> getTCGDetailViewList() {
        return TCGDetailViewList;
    }

    public void setTCGDetailViewList(List<TCGDetailView> TCGDetailViewList) {
        this.TCGDetailViewList = TCGDetailViewList;
    }

    public TCGDetailView getTcgDetailView() {
        return tcgDetailView;
    }

    public void setTcgDetailView(TCGDetailView tcgDetailView) {
        this.tcgDetailView = tcgDetailView;
    }

    public TCGDetailView getSelectCollateralItem() {
        return selectCollateralItem;
    }

    public void setSelectCollateralItem(TCGDetailView selectCollateralItem) {
        this.selectCollateralItem = selectCollateralItem;
    }

    public List<PotentialCollateral> getPotentialCollateralList() {
        return potentialCollateralList;
    }

    public void setPotentialCollateralList(List<PotentialCollateral> potentialCollateralList) {
        this.potentialCollateralList = potentialCollateralList;
    }

    public List<PotentialColToTCGCol> getPotentialColToTCGColList() {
        return potentialColToTCGColList;
    }

    public void setPotentialColToTCGColList(List<PotentialColToTCGCol> potentialColToTCGColList) {
        this.potentialColToTCGColList = potentialColToTCGColList;
    }

    public ModeForButton getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(ModeForButton modeForButton) {
        this.modeForButton = modeForButton;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public TCGView getTCGView() {
        return TCGView;
    }

    public void setTCGView(TCGView TCGView) {
        this.TCGView = TCGView;
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

    public String getMessageSeverity() {
        return messageSeverity;
    }

    public void setMessageSeverity(String messageSeverity) {
        this.messageSeverity = messageSeverity;
    }
}

