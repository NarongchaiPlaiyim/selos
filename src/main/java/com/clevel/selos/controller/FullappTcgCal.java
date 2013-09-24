package com.clevel.selos.controller;


import com.clevel.selos.dao.master.PotentialColToTCGColDAO;
import com.clevel.selos.dao.master.PotentialCollateralDAO;
import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.db.relation.PotentialColToTCGCol;
import com.clevel.selos.model.view.TcgCalRecordView;
import com.clevel.selos.model.view.TcgCalculateView;
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
@ManagedBean(name = "fullappTcgCal")
public class FullappTcgCal implements Serializable {

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

    private List<TcgCalRecordView> tcgCalRecordViewList;
    private TcgCalRecordView tcgCalRecordView;
    private TcgCalRecordView selectCollateralItem;
    private TcgCalculateView tcgCalculateView;


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


    public FullappTcgCal() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");
        modeForButton = "add";

        if (tcgCalRecordView == null) {
            tcgCalRecordView = new TcgCalRecordView();
        }

        if (tcgCalRecordViewList == null) {
            tcgCalRecordViewList = new ArrayList<TcgCalRecordView>();
        }

        if(tcgCalculateView == null){
            tcgCalculateView = new TcgCalculateView();
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
        log.info("onChangePotentialCollateralType ::: tcgCalRecordView.getPotentialCollateral().getId() : {}", tcgCalRecordView.getPotentialCollateral().getId());
        PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(tcgCalRecordView.getPotentialCollateral().getId());

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
        tcgCalRecordView = new TcgCalRecordView();
        tcgCalRecordView.setPotentialCollateral(new PotentialCollateral());
        tcgCalRecordView.setTcgCollateralType(new TCGCollateralType());
        tcgCalRecordView.setProposeInThisRequest("Y");
    }

    // onclick edit button
    public void onEditCollateralDetail() {
        log.info("onEditCollateralDetail rowIndex {} ", rowIndex);
        modeForButton = "edit";
        log.info("onEditCollateralDetail ::: selectCollateralItem  : {}", selectCollateralItem.toString());

        if(rowIndex < tcgCalRecordViewList.size()){

           PotentialCollateral  potentialCollateralEdit = selectCollateralItem.getPotentialCollateral();
           potentialColToTCGColList = potentialColToTCGColDAO.getListPotentialColToTCGCol(potentialCollateralEdit);
           TCGCollateralType    tcgCollateralTypeEdit   = selectCollateralItem.getTcgCollateralType();

           tcgCalRecordView.setPotentialCollateral(potentialCollateralEdit);
           tcgCalRecordView.setTcgCollateralType(tcgCollateralTypeEdit);
           tcgCalRecordView.setProposeInThisRequest(selectCollateralItem.getProposeInThisRequest());
           tcgCalRecordView.setLtvValue(selectCollateralItem.getLtvValue());
           tcgCalRecordView.setAppraisalAmount(selectCollateralItem.getAppraisalAmount());

        }
    }


    public void onSaveCollateralDetail() {
        log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        log.info("tcgCalRecordView.getPotentialCollateral().getId() :: {}", tcgCalRecordView.getPotentialCollateral().getId());
        log.info("tcgCalRecordView.getTcgCollateralType().getId() :: {}", tcgCalRecordView.getTcgCollateralType().getId());

        if (tcgCalRecordView.getPotentialCollateral().getId() != 0 && tcgCalRecordView.getTcgCollateralType().getId() != 0) {

            if (modeForButton != null && modeForButton.equalsIgnoreCase("add")) {
                log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);
                PotentialCollateral potentialCollateralSave = potentialCollateralDAO.findById(tcgCalRecordView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralTypeSave = tcgCollateralTypeDAO.findById(tcgCalRecordView.getTcgCollateralType().getId());

                TcgCalRecordView tcgCalRecordViewSave = new TcgCalRecordView();
                tcgCalRecordViewSave.setPotentialCollateral(potentialCollateralSave);
                tcgCalRecordViewSave.setTcgCollateralType(tcgCollateralTypeSave);
                tcgCalRecordViewSave.setAppraisalAmount(tcgCalRecordView.getAppraisalAmount());
                tcgCalRecordViewSave.setLtvValue(tcgCalRecordView.getLtvValue());
                tcgCalRecordViewSave.setProposeInThisRequest(tcgCalRecordView.getProposeInThisRequest());

                tcgCalRecordViewList.add(tcgCalRecordViewSave);

            } else if (modeForButton != null && modeForButton.equalsIgnoreCase("edit")) {
                log.info("onSaveCollateralDetail ::: mode : {}", modeForButton);
                PotentialCollateral potentialCollateralSave = potentialCollateralDAO.findById(tcgCalRecordView.getPotentialCollateral().getId());
                TCGCollateralType tcgCollateralTypeSave = tcgCollateralTypeDAO.findById(tcgCalRecordView.getTcgCollateralType().getId());

                tcgCalRecordViewList.get(rowIndex).setPotentialCollateral(potentialCollateralSave);
                tcgCalRecordViewList.get(rowIndex).setTcgCollateralType(tcgCollateralTypeSave);
                tcgCalRecordViewList.get(rowIndex).setAppraisalAmount(tcgCalRecordView.getAppraisalAmount());
                tcgCalRecordViewList.get(rowIndex).setLtvValue(tcgCalRecordView.getLtvValue());
                tcgCalRecordViewList.get(rowIndex).setProposeInThisRequest(tcgCalRecordView.getProposeInThisRequest());

            } else {
                log.info("onSaveCollateralDetail ::: Undefined modeForbutton !!");
            }

            if (tcgCalRecordViewList.size() > 0) {
                log.info("complete ::: CalculateSumValue(tcgCalRecordViewList); :: {} ", CalculateSumValue(tcgCalRecordViewList,"Appraisal" ));
                this.sumAppraisalAmount = CalculateSumValue(tcgCalRecordViewList,"Appraisal");
                this.sumLtvValue = CalculateSumValue(tcgCalRecordViewList,"LTV");
                this.sumInThisAppraisalAmount = CalculateSumValueInThis(tcgCalRecordViewList,"Appraisal");
                this.sumInThisLtvValue = CalculateSumValueInThis(tcgCalRecordViewList,"LTV");
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

    public BigDecimal CalculateSumValue(List<TcgCalRecordView> tcgCalRecordViewList, String typeAmt) {
        BigDecimal sum = new BigDecimal(0);

        for (int i = 0; i < tcgCalRecordViewList.size(); i++) {

            if(typeAmt.equals("Appraisal")){
                sum = sum.add(tcgCalRecordViewList.get(i).getAppraisalAmount());
            }else if(typeAmt.equals("LTV")){
                sum = sum.add(tcgCalRecordViewList.get(i).getLtvValue());
            }else{
                sum = new BigDecimal(0);
            }
        }
        log.info("sum ::: {} ", sum);

        return  sum;
    }

     public BigDecimal CalculateSumValueInThis(List<TcgCalRecordView> tcgCalRecordViewList, String typeAmt) {
         BigDecimal sum = new BigDecimal(0);

         for (int i = 0; i < tcgCalRecordViewList.size(); i++)
         {

                 if(typeAmt.equals("Appraisal"))
                 {
                     if(tcgCalRecordViewList.get(i).getProposeInThisRequest().equals("Y"))
                     {
                        sum = sum.add(tcgCalRecordViewList.get(i).getAppraisalAmount());
                     }
                 }
                 else if(typeAmt.equals("LTV"))
                 {
                     if(tcgCalRecordViewList.get(i).getProposeInThisRequest().equals("Y"))
                     {
                        sum = sum.add(tcgCalRecordViewList.get(i).getLtvValue());
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
       tcgCalRecordViewList.remove(selectCollateralItem);
    }

    /*public void calculateAfterDelete(){
        log.info("calculateAfterDelete :: {} ");
        if (tcgCalRecordViewList.size() > 0) {
            log.info("onDeleteTcgDetail ::: CalculateSumValue(tcgCalRecordViewList); :: ");
            this.sumAppraisalAmount = CalculateSumValue(tcgCalRecordViewList ,"Appraisal");
            this.sumLtvValue = CalculateSumValue(tcgCalRecordViewList ,"LTV");
            this.sumInThisAppraisalAmount = CalculateSumValueInThis(tcgCalRecordViewList ,"Appraisal");
            this.sumInThisLtvValue = CalculateSumValueInThis(tcgCalRecordViewList ,"LTV");
        }else{
            this.sumAppraisalAmount = new BigDecimal(0);
            this.sumLtvValue = new BigDecimal(0);
            this.sumInThisAppraisalAmount = new BigDecimal(0);
            this.sumInThisLtvValue = new BigDecimal(0);
        }
    }*/


    public List<TcgCalRecordView> getTcgCalRecordViewList() {
        return tcgCalRecordViewList;
    }

    public void setTcgCalRecordViewList(List<TcgCalRecordView> tcgCalRecordViewList) {
        this.tcgCalRecordViewList = tcgCalRecordViewList;
    }

    public TcgCalRecordView getTcgCalRecordView() {
        return tcgCalRecordView;
    }

    public void setTcgCalRecordView(TcgCalRecordView tcgCalRecordView) {
        this.tcgCalRecordView = tcgCalRecordView;
    }

    public TcgCalRecordView getSelectCollateralItem() {
        return selectCollateralItem;
    }

    public void setSelectCollateralItem(TcgCalRecordView selectCollateralItem) {
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

    public TcgCalculateView getTcgCalculateView() {
        return tcgCalculateView;
    }

    public void setTcgCalculateView(TcgCalculateView tcgCalculateView) {
        this.tcgCalculateView = tcgCalculateView;
    }
}

