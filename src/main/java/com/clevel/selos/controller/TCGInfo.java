package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.TCGInfoControl;
import com.clevel.selos.dao.master.PotentialCollateralDAO;
import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import com.clevel.selos.model.view.TCGDetailView;
import com.clevel.selos.model.view.TCGView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "tcgInfo")
public class TCGInfo implements Serializable {

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


    private List<TCGDetailView> TCGDetailViewList;
    private TCGDetailView tcgDetailView;
    private TCGDetailView selectCollateralItem;
    private TCGView TCGView;
    private int rowIndex;
    private Long workCaseId;
    //private User user;

    enum ModeForButton {ADD, EDIT}

    private ModeForButton modeForButton;

    enum ModeForDB {ADD_DB, EDIT_DB, CANCEL_DB}

    private ModeForDB modeForDB;
    private String messageHeader;
    private String message;
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
//    @Inject
//    UserDAO userDAO;

    public TCGInfo() {
    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");
        HttpSession session = FacesUtil.getSession(true);
//        session.setAttribute("workCaseId", new Long(2));    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }else{
            log.info("preRender ::: workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }

        if(workCaseId != null){
            TCGView = tcgInfoControl.getTcgView(workCaseId);

            if (TCGView != null) {
                TCGDetailViewList = tcgInfoControl.getTcgDetailListView(TCGView);
                modeForDB = ModeForDB.EDIT_DB;
            } else if (TCGView == null) {
                TCGView = new TCGView();
                modeForDB = ModeForDB.ADD_DB;
            }
        }

        if (tcgDetailView == null) {
            tcgDetailView = new TCGDetailView();
        }

        if (TCGDetailViewList == null) {
            TCGDetailViewList = new ArrayList<TCGDetailView>();
        }

        if (potentialCollateralList == null) {
            potentialCollateralList = new ArrayList<PotentialCollateral>();
        }

        potentialCollateralList = potentialCollateralDAO.findAll();

    }


    public void onChangePotentialCollateralType() {
        log.info("onChangePotentialCollateralType ::: TCGDetailView.getPotentialCollateral().getId() : {}", tcgDetailView.getPotentialCollateral().getId());

        try {
            PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(tcgDetailView.getPotentialCollateral().getId());

            log.info("potentialCollateralDAO.findById ::::: {}", potentialCollateral);

            //*** Get TCG Collateral  List from Potential Collateral    ***//
            potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(potentialCollateral);

            if (potentialColToTCGColList == null) {
                potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
            }

            log.info("onChangePotentialCollateralType ::: potentialColToTCGColList size : {}", potentialColToTCGColList.size());

        } catch (Exception e) {
            log.error("onChangePotentialCollateralType  error ::: {}", e.getMessage());
        }

    }

    // onclick ADD button
    public void onAddCollateralDetail() {
        log.info("onAddCollateralDetail :: reset form");
        modeForButton = ModeForButton.ADD;
        tcgDetailView = new TCGDetailView();
        tcgDetailView.reset();
    }

    // onclick edit button
    public void onEditCollateralDetail() {
        log.info("onEditCollateralDetail rowIndex {} ", rowIndex);
        modeForButton = ModeForButton.EDIT;
        log.info("onEditCollateralDetail ::: selectCollateralItem  : {}", selectCollateralItem.toString());

        if (rowIndex < TCGDetailViewList.size()) {

            PotentialCollateral potentialCollateralEdit = selectCollateralItem.getPotentialCollateral();
            potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(potentialCollateralEdit);
            TCGCollateralType tcgCollateralTypeEdit = selectCollateralItem.getTcgCollateralType();

            tcgDetailView.setPotentialCollateral(potentialCollateralEdit);
            tcgDetailView.setTcgCollateralType(tcgCollateralTypeEdit);
            tcgDetailView.setProposeInThisRequest(selectCollateralItem.getProposeInThisRequest());
            tcgDetailView.setLtvValue(selectCollateralItem.getLtvValue());
            tcgDetailView.setAppraisalAmount(selectCollateralItem.getAppraisalAmount());

        }
    }

//    public void calculateLtvValue(){
//        if (tcgDetailView.getPotentialCollateral().getId() != 0 && tcgDetailView.getTcgCollateralType().getId() != 0) {
//            BigDecimal ltvValueBig ;
//            log.info("TCGDetailView AppraisalAmount :: {}" , tcgDetailView.getAppraisalAmount());
//
//            ltvValueBig = tcgInfoControl.toCalculateLtvValue(tcgDetailView, this.workCaseId);
//            tcgDetailView.setLtvValue(ltvValueBig);
//        }
//    }

    public void onSaveCollateralDetail() {
        log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        log.info("TCGDetailView.getPotentialCollateral().getId() :: {}", tcgDetailView.getPotentialCollateral().getId());
        log.info("TCGDetailView.getTcgCollateralType().getId() :: {}", tcgDetailView.getTcgCollateralType().getId());

        if (tcgDetailView.getPotentialCollateral().getId() != 0 && tcgDetailView.getTcgCollateralType().getId() != 0) {

            if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);
                PotentialCollateral potentialCollateralSave = potentialCollateralDAO.findById(tcgDetailView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralTypeSave = tcgCollateralTypeDAO.findById(tcgDetailView.getTcgCollateralType().getId());
                TCGDetailView tcgDetailViewSave = new TCGDetailView();
                tcgDetailViewSave.setPotentialCollateral(potentialCollateralSave);
                tcgDetailViewSave.setTcgCollateralType(tcgCollateralTypeSave);
                tcgDetailViewSave.setAppraisalAmount(tcgDetailView.getAppraisalAmount());
                tcgDetailViewSave.setLtvValue(tcgDetailView.getLtvValue());
                tcgDetailViewSave.setProposeInThisRequest(tcgDetailView.getProposeInThisRequest());
                TCGDetailViewList.add(tcgDetailViewSave);

            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);
                PotentialCollateral potentialCollateralSave = potentialCollateralDAO.findById(tcgDetailView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralTypeSave = tcgCollateralTypeDAO.findById(tcgDetailView.getTcgCollateralType().getId());
                TCGDetailViewList.get(rowIndex).setPotentialCollateral(potentialCollateralSave);
                TCGDetailViewList.get(rowIndex).setTcgCollateralType(tcgCollateralTypeSave);
                TCGDetailViewList.get(rowIndex).setAppraisalAmount(tcgDetailView.getAppraisalAmount());
                TCGDetailViewList.get(rowIndex).setLtvValue(tcgDetailView.getLtvValue());
                TCGDetailViewList.get(rowIndex).setProposeInThisRequest(tcgDetailView.getProposeInThisRequest());

            } else {
                log.info("onSaveCollateralDetail ::: Undefined modeForbutton !!");
            }

            complete = true;
            calculate();
        } else {

            log.info("onSaveCollateralDetail ::: validation failed.");
            complete = false;
        }

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteTcgDetail() {
        TCGDetailViewList.remove(selectCollateralItem);
        calculate();
    }

    public void calculate() {
        log.info("calculateAfterDelete :: {} ");
        if (TCGDetailViewList.size() > 0) {
            log.info("onDeleteTcgDetail ::: CalculateSumValue(TCGDetailViewList); :: ");
            TCGView.setSumAppraisalAmount(tcgInfoControl.toCalculateSumAppraisalValue(TCGDetailViewList));
            TCGView.setSumLtvValue(tcgInfoControl.toCalculateSumLtvValue(TCGDetailViewList));
            TCGView.setSumInThisAppraisalAmount(tcgInfoControl.toCalculateSumAppraisalInThis(TCGDetailViewList));
            TCGView.setSumInThisLtvValue(tcgInfoControl.toCalculateSumLtvInThis(TCGDetailViewList));
        } else {
            TCGView.setSumAppraisalAmount(new BigDecimal(0));
            TCGView.setSumLtvValue(new BigDecimal(0));
            TCGView.setSumInThisAppraisalAmount(new BigDecimal(0));
            TCGView.setSumInThisLtvValue(new BigDecimal(0));
        }

    }

    public void onSaveTcgInfo() {
        log.info("onSaveTcgInfo ::: ModeForDB  {}", modeForDB);
        try {
            if (TCGDetailViewList.size() > 0) {
                if (modeForDB != null && modeForDB.equals(ModeForDB.ADD_DB)) {
                    tcgInfoControl.onSaveTCGToDB(TCGView, TCGDetailViewList, workCaseId);
                } else if (modeForDB != null && modeForDB.equals(ModeForDB.EDIT_DB)) {
                    tcgInfoControl.onSaveTCGToDB(TCGView, TCGDetailViewList, workCaseId);
                }

                messageHeader = msg.get("app.header.save.success");
                message = msg.get("app.tcg.response.save.success");
                onCreation();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            } else {
                messageHeader = msg.get("app.tcg.response.cannot.save");
                message = msg.get("app.tcg.response.desc.cannot.save");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }

        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.header.save.failed");

            if (ex.getCause() != null) {
                message = msg.get("app.tcg.response.save.failed") + " cause : " + ex.getCause().toString();
            } else {
                message = msg.get("app.tcg.response.save.failed") + ex.getMessage();
            }

            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        }

    }


    public void onCancelTcgInfo() {
        modeForDB = ModeForDB.CANCEL_DB;
        log.info("onCancelTcgInfo ::: ");

        for (int i = 0; i < TCGDetailViewList.size(); i++) {
            TCGDetailViewList.remove(TCGDetailViewList.get(i));
        }

        onCreation();
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

}

