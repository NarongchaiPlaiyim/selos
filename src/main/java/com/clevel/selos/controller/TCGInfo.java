package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.TCGInfoControl;
import com.clevel.selos.dao.master.PotentialCollateralDAO;
import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import com.clevel.selos.model.view.TCGDetailView;
import com.clevel.selos.model.view.TCGView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
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
    private TCGDetailView TCGDetailView;
    private TCGDetailView selectCollateralItem;
    private TCGView TCGView;

    private int rowIndex;
    private Long workCaseId;
    private User user;
    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;

    enum ModeForDB{ ADD_DB, EDIT_DB,CANCEL_DB }
    private ModeForDB  modeForDB;

    private List<PotentialCollateral> potentialCollateralList;
    private List<PotentialColToTCGCol> potentialColToTCGColList;

    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;

    @Inject
    private PotentialColToTCGColDAO potentialColToTCGColDAO;

    @Inject
    private TCGCollateralTypeDAO tcgCollateralTypeDAO;

    @Inject
    TCGInfoControl tcgInfoControl ;

    @Inject
    UserDAO userDAO;

    public TCGInfo() {}


    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");

        HttpSession session = FacesUtil.getSession(true);
        user = userDAO.findById("10001");

        session.setAttribute("workCaseId", new Long(2)) ;    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ",workCaseId);
        }

        TCGView = tcgInfoControl.getTcgView(workCaseId);

        if(TCGView != null){
            TCGDetailViewList = tcgInfoControl.getTcgDetailListView(TCGView);
            modeForDB = ModeForDB.EDIT_DB;
        }else if(TCGView == null){
            TCGView = new TCGView();
            modeForDB = ModeForDB.ADD_DB;
        }

        if (TCGDetailView == null) {
            TCGDetailView = new TCGDetailView();
        }

        if (TCGDetailViewList == null) {
            TCGDetailViewList = new ArrayList<TCGDetailView>();
        }

        if (potentialCollateralList == null) {
            potentialCollateralList = new ArrayList<PotentialCollateral>();
        }

        try{
            potentialCollateralList = potentialCollateralDAO.findAll();
        }catch (Exception e){
            log.error( "potentialCollateralDAO findAll error ::: {}" , e.getMessage());
        }

    }


    public void onChangePotentialCollateralType() {
        log.info("onChangePotentialCollateralType ::: TCGDetailView.getPotentialCollateral().getId() : {}", TCGDetailView.getPotentialCollateral().getId());

        try{
            PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(TCGDetailView.getPotentialCollateral().getId());

            log.info("potentialCollateralDAO.findById ::::: {}", potentialCollateral);

            //*** Get TCG Collateral  List from Potential Collateral    ***//
            potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(potentialCollateral);

            if (potentialColToTCGColList == null) {
                potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
            }

            log.info("onChangePotentialCollateralType ::: potentialColToTCGColList size : {}", potentialColToTCGColList.size());

        }catch (Exception e){
            log.error( "onChangePotentialCollateralType  error ::: {}" , e.getMessage());
        }

    }

    // onclick ADD button
    public void onAddCollateralDetail() {
        log.info("onAddCollateralDetail :: reset form");
        modeForButton = ModeForButton.ADD;
        TCGDetailView = new TCGDetailView();
        TCGDetailView.setPotentialCollateral(new PotentialCollateral());
        TCGDetailView.setTcgCollateralType(new TCGCollateralType());
        TCGDetailView.setProposeInThisRequest(0);

    }

    // onclick edit button
    public void onEditCollateralDetail() {
        log.info("onEditCollateralDetail rowIndex {} ", rowIndex);
        modeForButton = ModeForButton.EDIT;
        log.info("onEditCollateralDetail ::: selectCollateralItem  : {}", selectCollateralItem.toString());

        if(rowIndex < TCGDetailViewList.size()){

           PotentialCollateral  potentialCollateralEdit = selectCollateralItem.getPotentialCollateral();
           potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(potentialCollateralEdit);
           TCGCollateralType    tcgCollateralTypeEdit   = selectCollateralItem.getTcgCollateralType();

           TCGDetailView.setPotentialCollateral(potentialCollateralEdit);
           TCGDetailView.setTcgCollateralType(tcgCollateralTypeEdit);
           TCGDetailView.setProposeInThisRequest(selectCollateralItem.getProposeInThisRequest());
           TCGDetailView.setLtvValue(selectCollateralItem.getLtvValue());
           TCGDetailView.setAppraisalAmount(selectCollateralItem.getAppraisalAmount());

        }
    }


    public void onSaveCollateralDetail() {
        log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        log.info("TCGDetailView.getPotentialCollateral().getId() :: {}", TCGDetailView.getPotentialCollateral().getId());
        log.info("TCGDetailView.getTcgCollateralType().getId() :: {}", TCGDetailView.getTcgCollateralType().getId());

        if (TCGDetailView.getPotentialCollateral().getId() != 0 && TCGDetailView.getTcgCollateralType().getId() != 0) {

            if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);
                PotentialCollateral potentialCollateralSave = potentialCollateralDAO.findById(TCGDetailView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralTypeSave = tcgCollateralTypeDAO.findById(TCGDetailView.getTcgCollateralType().getId());

                TCGDetailView TCGDetailViewSave = new TCGDetailView();
                TCGDetailViewSave.setPotentialCollateral(potentialCollateralSave);
                TCGDetailViewSave.setTcgCollateralType(tcgCollateralTypeSave);
                TCGDetailViewSave.setAppraisalAmount(TCGDetailView.getAppraisalAmount());
                TCGDetailViewSave.setLtvValue(TCGDetailView.getLtvValue());
                TCGDetailViewSave.setProposeInThisRequest(TCGDetailView.getProposeInThisRequest());
                TCGDetailViewList.add(TCGDetailViewSave);

            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);
                PotentialCollateral potentialCollateralSave = potentialCollateralDAO.findById(TCGDetailView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralTypeSave = tcgCollateralTypeDAO.findById(TCGDetailView.getTcgCollateralType().getId());

                TCGDetailViewList.get(rowIndex).setPotentialCollateral(potentialCollateralSave);
                TCGDetailViewList.get(rowIndex).setTcgCollateralType(tcgCollateralTypeSave);
                TCGDetailViewList.get(rowIndex).setAppraisalAmount(TCGDetailView.getAppraisalAmount());
                TCGDetailViewList.get(rowIndex).setLtvValue(TCGDetailView.getLtvValue());
                TCGDetailViewList.get(rowIndex).setProposeInThisRequest(TCGDetailView.getProposeInThisRequest());

            } else {
                log.info("onSaveCollateralDetail ::: Undefined modeForbutton !!");
            }

            if (TCGDetailViewList.size() > 0) {
                log.info("complete ::: CalculateSumValue(TCGDetailViewList); :: {} ", CalculateSumValue(TCGDetailViewList,"Appraisal" ));
                TCGView.setSumAppraisalAmount(CalculateSumValue(TCGDetailViewList, "Appraisal"));
                TCGView.setSumLtvValue(CalculateSumValue(TCGDetailViewList,"LTV"));
                TCGView.setSumInThisAppraisalAmount(CalculateSumValueInThis(TCGDetailViewList,"Appraisal"));
                TCGView.setSumInThisLtvValue(CalculateSumValueInThis(TCGDetailViewList,"LTV"));
            }else{
                TCGView.setSumAppraisalAmount(new BigDecimal(0));
                TCGView.setSumLtvValue(new BigDecimal(0));
                TCGView.setSumInThisAppraisalAmount(new BigDecimal(0));
                TCGView.setSumInThisLtvValue(new BigDecimal(0));
            }

            complete = true;

        } else {

            log.info("onSaveCollateralDetail ::: validation failed.");
            complete = false;
        }

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);
    }

    public BigDecimal CalculateSumValue(List<TCGDetailView> TCGDetailViewList, String typeAmt) {
        BigDecimal sum = new BigDecimal(0);

        for (int i = 0; i < TCGDetailViewList.size(); i++) {

            if(typeAmt.equals("Appraisal")){
                sum = sum.add(TCGDetailViewList.get(i).getAppraisalAmount());
            }else if(typeAmt.equals("LTV")){
                sum = sum.add(TCGDetailViewList.get(i).getLtvValue());
            }else{
                sum = new BigDecimal(0);
            }
        }

        log.info("sum ::: {} ",  sum);

        return  sum;
    }

     public BigDecimal CalculateSumValueInThis(List<TCGDetailView> TCGDetailViewList, String typeAmt) {
         BigDecimal sum = new BigDecimal(0);

         for (int i = 0; i < TCGDetailViewList.size(); i++){

             if(typeAmt.equals("Appraisal")){
                 if(TCGDetailViewList.get(i).getProposeInThisRequest() == 1){
                    sum = sum.add(TCGDetailViewList.get(i).getAppraisalAmount());
                 }
             }else if(typeAmt.equals("LTV")){
                 if(TCGDetailViewList.get(i).getProposeInThisRequest() == 1){
                    sum = sum.add(TCGDetailViewList.get(i).getLtvValue());
                 }
             }else{
                 sum = new BigDecimal(0);
             }

         }
         log.info("sum ::: {} ", sum);

         return  sum;
     }

    public void onDeleteTcgDetail() {
       TCGDetailViewList.remove(selectCollateralItem);
       calculateAfterDelete();
    }

    public void calculateAfterDelete(){
        log.info("calculateAfterDelete :: {} ");
        if (TCGDetailViewList.size() > 0) {
            log.info("onDeleteTcgDetail ::: CalculateSumValue(TCGDetailViewList); :: ");
            TCGView.setSumAppraisalAmount(CalculateSumValue(TCGDetailViewList,"Appraisal"));
            TCGView.setSumLtvValue(CalculateSumValue(TCGDetailViewList,"LTV"));
            TCGView.setSumInThisAppraisalAmount(CalculateSumValueInThis(TCGDetailViewList,"Appraisal"));
            TCGView.setSumInThisLtvValue(CalculateSumValueInThis(TCGDetailViewList,"LTV"));
        }else{
            TCGView.setSumAppraisalAmount(new BigDecimal(0));
            TCGView.setSumLtvValue(new BigDecimal(0));
            TCGView.setSumInThisAppraisalAmount(new BigDecimal(0));
            TCGView.setSumInThisLtvValue(new BigDecimal(0));
        }

    }

    public void onSaveTcgInfo(){
        log.info("onSaveTcgInfo ::: ModeForDB  {}", modeForDB);

        if(modeForDB != null && modeForDB.equals(ModeForDB.ADD_DB)) {
            if(TCGView.getId() == 0){
                TCGView.setCreateBy(user);
                TCGView.setCreateDate(DateTime.now().toDate());
            }
            if (TCGDetailViewList.size() > 0) {
                tcgInfoControl.onSaveTCGToDB(TCGView,TCGDetailViewList,workCaseId);
            }

        } else if(modeForDB != null && modeForDB.equals(ModeForDB.EDIT_DB)) {
            TCGView.setModifyBy(user);
            TCGView.setModifyDate(DateTime.now().toDate());
            tcgInfoControl.onEditTCGToDB(TCGView,TCGDetailViewList,workCaseId);
        }

        onCreation();

    }


    public void onCancelTcgInfo(){
        modeForDB = ModeForDB.CANCEL_DB;
        log.info("onCancelTcgInfo ::: ");

        for(int i = 0 ; i < TCGDetailViewList.size() ; i ++){
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

    public TCGDetailView getTCGDetailView() {
        return TCGDetailView;
    }

    public void setTCGDetailView(TCGDetailView TCGDetailView) {
        this.TCGDetailView = TCGDetailView;
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


}

