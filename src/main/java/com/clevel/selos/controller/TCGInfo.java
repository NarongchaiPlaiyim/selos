package com.clevel.selos.controller;


import com.clevel.selos.busiensscontrol.TCGInfoControl;
import com.clevel.selos.dao.relation.PotentialColToTCGColDAO;
import com.clevel.selos.dao.master.PotentialCollateralDAO;
import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import com.clevel.selos.model.view.TCGDetailView;
import com.clevel.selos.model.view.TCGView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
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


    private String modeForButton;
    private int rowIndex;
    private BigDecimal sumAppraisalAmount;
    private BigDecimal sumLtvValue;
    private BigDecimal sumInThisAppraisalAmount;
    private BigDecimal sumInThisLtvValue;

    private List<PotentialCollateral> potentialCollateralList;
    private List<PotentialColToTCGCol> potentialColToTCGColList;

    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;

    @Inject
    private PotentialColToTCGColDAO potentialColToTCGColDAO;

    @Inject
    private TCGCollateralTypeDAO tcgCollateralTypeDAO;

    @Inject
    TCGInfoControl tcgBusinessControl ;

    public TCGInfo() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");
        modeForButton = "add";

        if (TCGDetailView == null) {
            TCGDetailView = new TCGDetailView();
        }

        if (TCGDetailViewList == null) {
            TCGDetailViewList = new ArrayList<TCGDetailView>();
        }

        if(TCGView == null){
            TCGView = new TCGView();
        }

        if (potentialCollateralList == null) {
            potentialCollateralList = new ArrayList<PotentialCollateral>();
        }

        this.sumAppraisalAmount = new BigDecimal(0);
        this.sumLtvValue = new BigDecimal(0);
        this.sumInThisAppraisalAmount = new BigDecimal(0);
        this.sumInThisLtvValue = new BigDecimal(0);

        potentialCollateralList = potentialCollateralDAO.findAll();
    }

    public void onChangePotentialCollateralType() {
        log.info("onChangePotentialCollateralType ::: TCGDetailView.getPotentialCollateral().getId() : {}", TCGDetailView.getPotentialCollateral().getId());
        PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(TCGDetailView.getPotentialCollateral().getId());

        log.info("potentialCollateralDAO.findById ::::: {}", potentialCollateral);

        //*** Get TCG Collateral  List from Potential Collateral    ***//
        potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(potentialCollateral);

        if (potentialColToTCGColList == null) {
            potentialColToTCGColList = new ArrayList<PotentialColToTCGCol>();
        }
        log.info("onChangePotentialCollateralType ::: potentialColToTCGColList size : {}", potentialColToTCGColList.size());

    }

    // onclick ADD button
    public void onAddCollateralDetail() {
        log.info("onAddCollateralDetail :: reset form");
        modeForButton = "add";
        TCGDetailView = new TCGDetailView();
//        TCGDetailView.setPotentialCollateral(new PotentialCollateral());
//        TCGDetailView.setTcgCollateralType(new TCGCollateralType());
        TCGDetailView.setProposeInThisRequest(false);
    }

    // onclick edit button
    public void onEditCollateralDetail() {
        log.info("onEditCollateralDetail rowIndex {} ", rowIndex);
        modeForButton = "edit";
        log.info("onEditCollateralDetail ::: selectCollateralItem  : {}", selectCollateralItem.toString());

        if(rowIndex < TCGDetailViewList.size()){

           PotentialCollateral  potentialCollateralEdit = selectCollateralItem.getPotentialCollateral();
           potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(potentialCollateralEdit);
           TCGCollateralType    tcgCollateralTypeEdit   = selectCollateralItem.getTcgCollateralType();

           TCGDetailView.setPotentialCollateral(potentialCollateralEdit);
           TCGDetailView.setTcgCollateralType(tcgCollateralTypeEdit);
           TCGDetailView.setProposeInThisRequest(selectCollateralItem.isProposeInThisRequest());
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

            if (modeForButton != null && modeForButton.equalsIgnoreCase("add")) {
                log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);
                PotentialCollateral potentialCollateralSave = potentialCollateralDAO.findById(TCGDetailView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralTypeSave = tcgCollateralTypeDAO.findById(TCGDetailView.getTcgCollateralType().getId());

                TCGDetailView TCGDetailViewSave = new TCGDetailView();
                TCGDetailViewSave.setPotentialCollateral(potentialCollateralSave);
                TCGDetailViewSave.setTcgCollateralType(tcgCollateralTypeSave);
                TCGDetailViewSave.setAppraisalAmount(TCGDetailView.getAppraisalAmount());
                TCGDetailViewSave.setLtvValue(TCGDetailView.getLtvValue());
                TCGDetailViewSave.setProposeInThisRequest(TCGDetailView.isProposeInThisRequest());

                TCGDetailViewList.add(TCGDetailViewSave);

            } else if (modeForButton != null && modeForButton.equalsIgnoreCase("edit")) {
                log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);
                PotentialCollateral potentialCollateralSave = potentialCollateralDAO.findById(TCGDetailView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralTypeSave = tcgCollateralTypeDAO.findById(TCGDetailView.getTcgCollateralType().getId());

                TCGDetailViewList.get(rowIndex).setPotentialCollateral(potentialCollateralSave);
                TCGDetailViewList.get(rowIndex).setTcgCollateralType(tcgCollateralTypeSave);
                TCGDetailViewList.get(rowIndex).setAppraisalAmount(TCGDetailView.getAppraisalAmount());
                TCGDetailViewList.get(rowIndex).setLtvValue(TCGDetailView.getLtvValue());
                TCGDetailViewList.get(rowIndex).setProposeInThisRequest(TCGDetailView.isProposeInThisRequest());

            } else {
                log.info("onSaveCollateralDetail ::: Undefined modeForbutton !!");
            }

            if (TCGDetailViewList.size() > 0) {
                log.info("complete ::: CalculateSumValue(TCGDetailViewList); :: {} ", CalculateSumValue(TCGDetailViewList,"Appraisal" ));
                this.sumAppraisalAmount = CalculateSumValue(TCGDetailViewList,"Appraisal");
                this.sumLtvValue = CalculateSumValue(TCGDetailViewList,"LTV");
                this.sumInThisAppraisalAmount = CalculateSumValueInThis(TCGDetailViewList,"Appraisal");
                this.sumInThisLtvValue = CalculateSumValueInThis(TCGDetailViewList,"LTV");
            }else{
                this.sumAppraisalAmount = new BigDecimal(0);
                this.sumLtvValue = new BigDecimal(0);
                this.sumInThisAppraisalAmount = new BigDecimal(0);
                this.sumInThisLtvValue = new BigDecimal(0);
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
        log.info("sum ::: {} ", sum);

        return  sum;
    }

     public BigDecimal CalculateSumValueInThis(List<TCGDetailView> TCGDetailViewList, String typeAmt) {
         BigDecimal sum = new BigDecimal(0);

         for (int i = 0; i < TCGDetailViewList.size(); i++)
         {

                 if(typeAmt.equals("Appraisal"))
                 {
                     if(TCGDetailViewList.get(i).isProposeInThisRequest() == true)
                     {
                        sum = sum.add(TCGDetailViewList.get(i).getAppraisalAmount());
                     }
                 }
                 else if(typeAmt.equals("LTV"))
                 {
                     if(TCGDetailViewList.get(i).isProposeInThisRequest() == true)
                     {
                        sum = sum.add(TCGDetailViewList.get(i).getLtvValue());
                     }

                 }
                 else
                 {
                     sum = new BigDecimal(0);
                 }

         }
         log.info("sum ::: {} ", sum);

         return  sum;
     }

    public void onDeleteTcgDetail() {
       log.info("onDeleteTcgDetail rowIndex {} ", rowIndex);
       TCGDetailViewList.remove(selectCollateralItem);
    }

    /*public void calculateAfterDelete(){
        log.info("calculateAfterDelete :: {} ");
        if (TCGDetailViewList.size() > 0) {
            log.info("onDeleteTcgDetail ::: CalculateSumValue(TCGDetailViewList); :: ");
            this.sumAppraisalAmount = CalculateSumValue(TCGDetailViewList ,"Appraisal");
            this.sumLtvValue = CalculateSumValue(TCGDetailViewList ,"LTV");
            this.sumInThisAppraisalAmount = CalculateSumValueInThis(TCGDetailViewList ,"Appraisal");
            this.sumInThisLtvValue = CalculateSumValueInThis(TCGDetailViewList ,"LTV");
        }else{
            this.sumAppraisalAmount = new BigDecimal(0);
            this.sumLtvValue = new BigDecimal(0);
            this.sumInThisAppraisalAmount = new BigDecimal(0);
            this.sumInThisLtvValue = new BigDecimal(0);
        }
    }*/

    public void onSaveTcgInfo(){
        log.info("onSaveTcgInfo ::: ");
        tcgBusinessControl.onSaveTCGToDB(TCGView,TCGDetailViewList);
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

    public String getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(String modeForButton) {
        this.modeForButton = modeForButton;
    }

    public BigDecimal getSumAppraisalAmount() {
        return sumAppraisalAmount;
    }

    public void setSumAppraisalAmount(BigDecimal sumAppraisalAmount) {
        this.sumAppraisalAmount = sumAppraisalAmount;
    }

    public BigDecimal getSumLtvValue() {
        return sumLtvValue;
    }

    public void setSumLtvValue(BigDecimal sumLtvValue) {
        this.sumLtvValue = sumLtvValue;
    }

    public BigDecimal getSumInThisAppraisalAmount() {
        return sumInThisAppraisalAmount;
    }

    public void setSumInThisAppraisalAmount(BigDecimal sumInThisAppraisalAmount) {
        this.sumInThisAppraisalAmount = sumInThisAppraisalAmount;
    }

    public BigDecimal getSumInThisLtvValue() {
        return sumInThisLtvValue;
    }

    public void setSumInThisLtvValue(BigDecimal sumInThisLtvValue) {
        this.sumInThisLtvValue = sumInThisLtvValue;
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

