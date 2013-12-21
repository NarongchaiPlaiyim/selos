package com.clevel.selos.controller;


import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.Util;
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
@ManagedBean(name = "disbursementInfo")
public class DisbursementInfo implements Serializable {

    @Inject
    @SELOS
    Logger log;

    @Inject
    BankDAO bankDAO;

    enum ModeForButton {ADD, EDIT}

    private ModeForButton modeForButton;
    private int selectRowNumber;

    DisbursementInfoView disbursementInfoView;

    DisbursementMcDetailView disbursementMcDetailView;
    DisbursementMcDetailView newDisbursementMcDetailView;
    List<DisbursementMcDetailView> disbursementMcList;

    DisbursementDepositBaDetailView disbursementDepositDetailView;
    DisbursementDepositBaDetailView newDisbursementDepositDetailView;
    List<DisbursementDepositBaDetailView> disbursementDepositList;

    DisbursementBahtnetDetailView disbursementBahtnetDetailView;
    DisbursementBahtnetDetailView newDisbursementBahtnetDetailView;
    List<DisbursementBahtnetDetailView> disbursementBahtnetList;


    private List<Bank> bankList;


    @PostConstruct
    public void onCreate() {

        disbursementInfoView = new DisbursementInfoView();
        disbursementMcDetailView = new DisbursementMcDetailView();
        disbursementMcList = new ArrayList<DisbursementMcDetailView>();

        disbursementDepositDetailView = new DisbursementDepositBaDetailView();
        disbursementDepositList = new ArrayList<DisbursementDepositBaDetailView>();

        disbursementBahtnetDetailView = new DisbursementBahtnetDetailView();
        disbursementBahtnetList = new ArrayList<DisbursementBahtnetDetailView>();

        //init Disbursement Summary
        List<DisbursementSummaryView> disburseSumList = new ArrayList<DisbursementSummaryView>();
        DisbursementSummaryView disbursementSummaryView = null;
        disbursementSummaryView = new DisbursementSummaryView();
        disbursementSummaryView.setProductProgram("SmartBiz");
        disbursementSummaryView.setCreditFacility("Loan");
        disbursementSummaryView.setProductCode("-");
        disbursementSummaryView.setProjectCode("-");
        disbursementSummaryView.setRefinance("No");
        disbursementSummaryView.setApprovedLimit(BigDecimal.valueOf(300000));
        disbursementSummaryView.setHoldAmount(BigDecimal.valueOf(100000));
        disbursementSummaryView.setDisburseAmount(BigDecimal.valueOf(1900000));
        disbursementSummaryView.setDiffAmount(BigDecimal.valueOf(100000));
        disburseSumList.add(disbursementSummaryView);
        disbursementSummaryView = new DisbursementSummaryView();
        disbursementSummaryView.setProductProgram("SmartBiz");
        disbursementSummaryView.setCreditFacility("OD");
        disbursementSummaryView.setProductCode("-");
        disbursementSummaryView.setProjectCode("-");
        disbursementSummaryView.setRefinance("Yes");
        disbursementSummaryView.setApprovedLimit(BigDecimal.valueOf(5000000));
        disbursementSummaryView.setHoldAmount(BigDecimal.valueOf(2500000));
        disbursementSummaryView.setDisburseAmount(BigDecimal.valueOf(250000));
        disbursementSummaryView.setDiffAmount(BigDecimal.ZERO);
        disburseSumList.add(disbursementSummaryView);
        disbursementInfoView.setDisburse(disburseSumList);

        //init Ba Summary

        List<DisbursementDepositBaDetailView> disbursementBaList = new ArrayList<DisbursementDepositBaDetailView>();
        DisbursementDepositBaDetailView depositDetailView1 = new DisbursementDepositBaDetailView();
        depositDetailView1.setAccountName("บริษัท FWD");
        depositDetailView1.setAccountNumber("111-2-33333-4");
        depositDetailView1.setTotalAmount(BigDecimal.valueOf(1000000));
        disbursementBaList.add(depositDetailView1);
        DisbursementDepositBaDetailView depositDetailView2 = new DisbursementDepositBaDetailView();
        depositDetailView2.setAccountName("บริษัท FWD");
        depositDetailView2.setAccountNumber("111-2-33333-4");
        depositDetailView2.setTotalAmount(BigDecimal.valueOf(1000000));
        disbursementBaList.add(depositDetailView2);
        disbursementInfoView.setDisbursementBaList(disbursementBaList);
        calculationSummaryTotalBa();

        disbursementInfoView.setNumberOfCheque(0);
        disbursementInfoView.setNumberOfDeposit(0);
        disbursementInfoView.setNumberOfBahtnet(0);
        disbursementInfoView.setTotalBahtnetDisburse(BigDecimal.ZERO);
        disbursementInfoView.setTotalMCDisburse(BigDecimal.ZERO);
        disbursementInfoView.setTotalDepositDisburse(BigDecimal.ZERO);

    }

    private List<CreditTypeDetailView> initCreditTypeList() {

        List<CreditTypeDetailView> creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
        CreditTypeDetailView creditTypeDetailView = null;
        creditTypeDetailView = new CreditTypeDetailView();
        creditTypeDetailView.setProductProgram("SmartBiz");
        creditTypeDetailView.setCreditFacility("Loan");
        creditTypeDetailView.setLimit(BigDecimal.valueOf(100000));
        creditTypeDetailView.setComponentFlag(true);
        creditTypeDetailView.setPurpose("คุ้มครองวงเงิน Loan");
        creditTypeDetailViewList.add(creditTypeDetailView);
        creditTypeDetailView = new CreditTypeDetailView();
        creditTypeDetailView.setProductProgram("SmartBiz");
        creditTypeDetailView.setCreditFacility("OD");
        creditTypeDetailView.setLimit(BigDecimal.valueOf(100000));
        creditTypeDetailView.setComponentFlag(true);
        creditTypeDetailView.setPurpose("คุ้มครองวงเงิน OD");
        creditTypeDetailViewList.add(creditTypeDetailView);
        return creditTypeDetailViewList;
    }

    public void onOpenAddMcDialog() {
        log.debug("onOpenAddMcDialog()");

        modeForButton = ModeForButton.ADD;
        disbursementMcDetailView = new DisbursementMcDetailView();
        disbursementMcDetailView.setCreditType(initCreditTypeList());

    }

    public void onOpenEditMcDialog() {
        log.debug("onOpenEditMcDialog()");
        modeForButton = ModeForButton.EDIT;


        disbursementMcDetailView = new DisbursementMcDetailView();
        disbursementMcDetailView.setIssuedBy(newDisbursementMcDetailView.getIssuedBy());
        disbursementMcDetailView.setIssuedDate(newDisbursementMcDetailView.getIssuedDate());
        disbursementMcDetailView.setPayeeName(newDisbursementMcDetailView.getPayeeName());
        disbursementMcDetailView.setPayeeSubname(newDisbursementMcDetailView.getPayeeSubname());
        disbursementMcDetailView.setCrossType(newDisbursementMcDetailView.getCrossType());
        disbursementMcDetailView.setCreditType(cloneCreditTypeList(newDisbursementMcDetailView.getCreditType()));


    }

    public void onOpenAddDepositDialog() {
        log.debug("onOpenAddDepositDialog()");

        modeForButton = ModeForButton.ADD;
        disbursementDepositDetailView = new DisbursementDepositBaDetailView();
        disbursementDepositDetailView.setCreditType(initCreditTypeList());
    }

    public void onOpenEditDepositDialog() {
        log.debug("onOpenEditDepositDialog()");

        modeForButton = ModeForButton.EDIT;
        disbursementDepositDetailView = new DisbursementDepositBaDetailView();

        disbursementDepositDetailView.setAccountNumber(newDisbursementDepositDetailView.getAccountNumber());
        disbursementDepositDetailView.setAccountName(newDisbursementDepositDetailView.getAccountName());
        disbursementDepositDetailView.setCreditType(cloneCreditTypeList(newDisbursementDepositDetailView.getCreditType()));

    }

    public void onOpenAddBahtnetDialog() {
        log.debug("onOpenAddBahtnetDialog()");
        bankList = bankDAO.findAll();
        modeForButton = ModeForButton.ADD;
        disbursementBahtnetDetailView = new DisbursementBahtnetDetailView();
        disbursementBahtnetDetailView.setCreditType(initCreditTypeList());

    }

    public void onOpenEditBahtnetDialog() {
        log.debug("onOpenEditBahtnetDialog()");
        bankList = bankDAO.findAll();
        modeForButton = ModeForButton.EDIT;
        disbursementBahtnetDetailView = new DisbursementBahtnetDetailView();
        disbursementBahtnetDetailView.setCreditType(cloneCreditTypeList(newDisbursementBahtnetDetailView.getCreditType()));
        disbursementBahtnetDetailView.setBankCode(newDisbursementBahtnetDetailView.getBankCode());
        disbursementBahtnetDetailView.setAccountNumber(newDisbursementBahtnetDetailView.getAccountNumber());
        disbursementBahtnetDetailView.setBenefitName(newDisbursementBahtnetDetailView.getBenefitName());


    }

    private List<CreditTypeDetailView> cloneCreditTypeList(List<CreditTypeDetailView> list){
        List<CreditTypeDetailView> creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
        for(CreditTypeDetailView creditTypes : list){
            CreditTypeDetailView creditTypeDetailView = new CreditTypeDetailView();
            creditTypeDetailView.setProductProgram(creditTypes.getProductProgram());
            creditTypeDetailView.setCreditFacility(creditTypes.getCreditFacility());
            creditTypeDetailView.setLimit(creditTypes.getLimit());
            creditTypeDetailView.setDisburseAmount(creditTypes.getDisburseAmount());
            creditTypeDetailView.setComponentFlag(true);
            creditTypeDetailViewList.add(creditTypeDetailView);
        }
        return creditTypeDetailViewList;
    }


    public void onSubmitDisbursementMc() {
        log.debug("onSubmitDisbursementMc()");
        log.info("modeForButton ::: {}", modeForButton);

        boolean complete = false;
        BigDecimal totalAmount = BigDecimal.ZERO;
        RequestContext context = RequestContext.getCurrentInstance();
        int falseCount = 0;
        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

            if (disbursementMcDetailView != null || disbursementMcDetailView.getCreditType() != null) {

                for (int i = 0; i < disbursementMcDetailView.getCreditType().size(); i++) {
                    if (disbursementMcDetailView.getCreditType().get(i).getDisburseAmount().compareTo(disbursementMcDetailView.getCreditType().get(i).getLimit()) > 0) {
                        disbursementMcDetailView.getCreditType().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementMcDetailView.getCreditType().get(i).setComponentFlag(true);
                    }
                    totalAmount = totalAmount.add(disbursementMcDetailView.getCreditType().get(i).getDisburseAmount());
                }

                if (falseCount == 0) {
                    disbursementMcDetailView.setTotalAmount(totalAmount);
                    disbursementMcDetailView.setPayeeName(disbursementMcDetailView.getPayeeName() + " " + disbursementMcDetailView.getPayeeSubname());
                    disbursementMcList.add(disbursementMcDetailView);
                    disbursementInfoView.setDisburseMcList(disbursementMcList);
                    complete = true;
                }
            }
        }
        if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {

            if (disbursementMcDetailView != null || disbursementMcDetailView.getCreditType() != null) {

                for (int i = 0; i < disbursementMcDetailView.getCreditType().size(); i++) {
                    if (disbursementMcDetailView.getCreditType().get(i).getDisburseAmount().compareTo(disbursementMcDetailView.getCreditType().get(i).getLimit()) > 0) {
                        disbursementMcDetailView.getCreditType().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementMcDetailView.getCreditType().get(i).setComponentFlag(true);
                    }
                    totalAmount = totalAmount.add(disbursementMcDetailView.getCreditType().get(i).getDisburseAmount());
                }

                if (falseCount == 0) {
                    disbursementMcList.get(selectRowNumber).setIssuedBy(disbursementMcDetailView.getIssuedBy());
                    disbursementMcList.get(selectRowNumber).setIssuedDate(disbursementMcDetailView.getIssuedDate());
                    disbursementMcList.get(selectRowNumber).setPayeeName(disbursementMcDetailView.getPayeeName());
                    disbursementMcList.get(selectRowNumber).setPayeeSubname(disbursementMcDetailView.getPayeeSubname());
                    disbursementMcList.get(selectRowNumber).setCrossType(disbursementMcDetailView.getCrossType());
                    disbursementMcList.get(selectRowNumber).setCreditType(disbursementMcDetailView.getCreditType());
                    disbursementMcList.get(selectRowNumber).setTotalAmount(totalAmount);
                    complete = true;
                }
            }
        }

        calculationSummaryTotalMc();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onSubmitDisbursementDeposit() {
        log.debug("onSubmitDisbursementDeposit()");
        log.info("modeForButton ::: {}", modeForButton);

        boolean complete = false;
        BigDecimal totalAmount = BigDecimal.ZERO;
        RequestContext context = RequestContext.getCurrentInstance();
        int falseCount = 0;

        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

            if (disbursementDepositDetailView != null || disbursementDepositDetailView.getCreditType() != null) {

                for (int i = 0; i < disbursementDepositDetailView.getCreditType().size(); i++) {
                    if (disbursementDepositDetailView.getCreditType().get(i).getDisburseAmount().compareTo(disbursementDepositDetailView.getCreditType().get(i).getLimit()) > 0) {

                        disbursementDepositDetailView.getCreditType().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementDepositDetailView.getCreditType().get(i).setComponentFlag(true);
                    }
                    totalAmount = totalAmount.add(disbursementDepositDetailView.getCreditType().get(i).getDisburseAmount());
                }
                if (falseCount == 0) {
                    disbursementDepositDetailView.setTotalAmount(totalAmount);
                    disbursementDepositList.add(disbursementDepositDetailView);
                    disbursementInfoView.setDisburseDepositList(disbursementDepositList);
                    complete = true;
                }
            }
        }
        if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {

            if (disbursementDepositDetailView != null || disbursementDepositDetailView.getCreditType() != null) {

                for (int i = 0; i < disbursementDepositDetailView.getCreditType().size(); i++) {
                    if (disbursementDepositDetailView.getCreditType().get(i).getDisburseAmount().compareTo(disbursementDepositDetailView.getCreditType().get(i).getLimit()) > 0) {

                        disbursementDepositDetailView.getCreditType().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementDepositDetailView.getCreditType().get(i).setComponentFlag(true);
                    }
                    totalAmount = totalAmount.add(disbursementDepositDetailView.getCreditType().get(i).getDisburseAmount());
                }

                if (falseCount == 0) {
                    disbursementDepositList.get(selectRowNumber).setAccountNumber(disbursementDepositDetailView.getAccountNumber());
                    disbursementDepositList.get(selectRowNumber).setAccountName(disbursementDepositDetailView.getAccountName());
                    disbursementDepositList.get(selectRowNumber).setCreditType(disbursementDepositDetailView.getCreditType());
                    disbursementDepositList.get(selectRowNumber).setTotalAmount(totalAmount);

                    complete = true;
                }
            }
        }

        calculationSummaryTotalDeposit();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onSubmitDisbursementBahtnet() {
        log.debug("onSubmitDisbursementBahtnet()");
        log.info("modeForButton ::: {}", modeForButton);


        boolean complete = false;
        BigDecimal totalAmount = BigDecimal.ZERO;
        RequestContext context = RequestContext.getCurrentInstance();
        int falseCount = 0;
        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

            if (disbursementBahtnetDetailView != null || disbursementBahtnetDetailView.getCreditType() != null) {

                for (int i = 0; i < disbursementBahtnetDetailView.getCreditType().size(); i++) {
                    if (disbursementBahtnetDetailView.getCreditType().get(i).getDisburseAmount().compareTo(disbursementBahtnetDetailView.getCreditType().get(i).getLimit()) > 0) {
                        disbursementBahtnetDetailView.getCreditType().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementBahtnetDetailView.getCreditType().get(i).setComponentFlag(true);
                    }
                    totalAmount = totalAmount.add(disbursementBahtnetDetailView.getCreditType().get(i).getDisburseAmount());
                }
                if (falseCount == 0) {
                    disbursementBahtnetDetailView.setBankName(bankDAO.findById(disbursementBahtnetDetailView.getBankCode()));
                    disbursementBahtnetDetailView.setBankCode(disbursementBahtnetDetailView.getBankCode());
                    disbursementBahtnetDetailView.setTotalAmount(totalAmount);
                    disbursementBahtnetList.add(disbursementBahtnetDetailView);
                    disbursementInfoView.setDisbursementBahtnetList(disbursementBahtnetList);
                    complete=true;
                }
            }
        }
        if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {

            if (disbursementBahtnetDetailView != null || disbursementBahtnetDetailView.getCreditType() != null) {

                for (int i = 0; i < disbursementBahtnetDetailView.getCreditType().size(); i++) {
                    if (disbursementBahtnetDetailView.getCreditType().get(i).getDisburseAmount().compareTo(disbursementBahtnetDetailView.getCreditType().get(i).getLimit()) > 0) {

                        disbursementBahtnetDetailView.getCreditType().get(i).setComponentFlag(false);
                        falseCount++;
                    } else {
                        disbursementBahtnetDetailView.getCreditType().get(i).setComponentFlag(true);

                    }
                    totalAmount = totalAmount.add(disbursementBahtnetDetailView.getCreditType().get(i).getDisburseAmount());
                }

                if (falseCount == 0) {
                    disbursementBahtnetList.get(selectRowNumber).setBankName(bankDAO.findById(disbursementBahtnetDetailView.getBankCode()));
                    disbursementBahtnetList.get(selectRowNumber).setBankCode(disbursementBahtnetDetailView.getBankCode());
                    disbursementBahtnetList.get(selectRowNumber).setBenefitName(disbursementBahtnetDetailView.getBenefitName());
                    disbursementBahtnetList.get(selectRowNumber).setAccountNumber(disbursementBahtnetDetailView.getAccountNumber());
                    disbursementBahtnetList.get(selectRowNumber).setBranchName(disbursementBahtnetDetailView.getBranchName());
                    disbursementBahtnetList.get(selectRowNumber).setCreditType(disbursementBahtnetDetailView.getCreditType());
                    disbursementBahtnetList.get(selectRowNumber).setTotalAmount(totalAmount);
                    complete = true;
                }
            }
        }
        calculationSummaryTotalBahtnet();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteDisbursementMcList() {
        log.debug("onDeleteDisbursementMcList()");
        disbursementMcList.remove(selectRowNumber);
        calculationSummaryTotalMc();
    }

    public void onDeleteDisbursementDepositList() {
        log.debug("onDeleteDisbursementDepositList()");
        disbursementDepositList.remove(selectRowNumber);
        calculationSummaryTotalDeposit();
    }

    public void onDeleteDisbursementBahtnetList() {
        log.debug("onDeleteDisbursementBahtnetList()");
        disbursementBahtnetList.remove(selectRowNumber);
        calculationSummaryTotalBahtnet();
    }

    private void calculationSummaryTotalMc() {
        log.debug("calculationSummaryTotalMc()");
        BigDecimal totalMcDisbursement = BigDecimal.ZERO;
        int listSize = disbursementInfoView.getDisburseMcList().size();
        if (disbursementInfoView.getDisburseMcList() != null) {
            for (int i = 0; i < listSize; i++) {
                totalMcDisbursement = totalMcDisbursement.add(disbursementInfoView.getDisburseMcList().get(i).getTotalAmount());
            }
        }
        disbursementInfoView.setTotalMCDisburse(totalMcDisbursement);
        disbursementInfoView.setNumberOfCheque(listSize);
    }

    private void calculationSummaryTotalDeposit() {
        log.debug("calculationSummaryTotalDeposit()");
        BigDecimal totalDepositDisbursement = BigDecimal.ZERO;
        int listSize = disbursementInfoView.getDisburseDepositList().size();
        if (disbursementInfoView.getDisburseDepositList() != null) {
            for (int i = 0; i < listSize; i++) {
                totalDepositDisbursement = totalDepositDisbursement.add(disbursementInfoView.getDisburseDepositList().get(i).getTotalAmount());
            }
        }
        disbursementInfoView.setTotalDepositDisburse(totalDepositDisbursement);
        disbursementInfoView.setNumberOfDeposit(listSize);
    }

    private void calculationSummaryTotalBahtnet() {
        log.debug("calculationSummaryTotalBahtnet()");
        BigDecimal totalBahtnetDisbursement = BigDecimal.ZERO;
        int listSize = Util.safetyList(disbursementInfoView.getDisbursementBahtnetList()).size();
        if (disbursementInfoView.getDisbursementBahtnetList() != null) {
            for (int i = 0; i < listSize; i++) {
                totalBahtnetDisbursement = totalBahtnetDisbursement.add(disbursementInfoView.getDisbursementBahtnetList().get(i).getTotalAmount());
            }
        }
        disbursementInfoView.setTotalBahtnetDisburse(totalBahtnetDisbursement);
        disbursementInfoView.setNumberOfBahtnet(listSize);
    }

    private void calculationSummaryTotalBa() {
        log.debug("calculationSummaryTotalBa()");
        BigDecimal totalBaDisbursement = BigDecimal.ZERO;
        int listSize = disbursementInfoView.getDisbursementBaList().size();
        if (disbursementInfoView.getDisbursementBaList() != null) {
            for (int i = 0; i < listSize; i++) {
                totalBaDisbursement = totalBaDisbursement.add(disbursementInfoView.getDisbursementBaList().get(i).getTotalAmount());
            }
        }
        disbursementInfoView.setTotalBADisburse(totalBaDisbursement);
        disbursementInfoView.setNumberOfBA(listSize);
    }


    public DisbursementMcDetailView getNewDisbursementMcDetailView() {
        return newDisbursementMcDetailView;
    }

    public void setNewDisbursementMcDetailView(DisbursementMcDetailView newDisbursementMcDetailView) {
        this.newDisbursementMcDetailView = newDisbursementMcDetailView;
    }

    public List<DisbursementMcDetailView> getDisbursementMcList() {
        return disbursementMcList;
    }

    public void setDisbursementMcList(List<DisbursementMcDetailView> disbursementMcList) {
        this.disbursementMcList = disbursementMcList;
    }

    public DisbursementInfoView getDisbursementInfoView() {
        return disbursementInfoView;
    }

    public void setDisbursementInfoView(DisbursementInfoView disbursementInfoView) {
        this.disbursementInfoView = disbursementInfoView;
    }

    public DisbursementMcDetailView getDisbursementMcDetailView() {
        return disbursementMcDetailView;
    }

    public void setDisbursementMcDetailView(DisbursementMcDetailView disbursementMcDetailView) {
        this.disbursementMcDetailView = disbursementMcDetailView;
    }

    public int getSelectRowNumber() {
        return selectRowNumber;
    }

    public void setSelectRowNumber(int selectRowNumber) {
        this.selectRowNumber = selectRowNumber;
    }

    public DisbursementDepositBaDetailView getDisbursementDepositDetailView() {
        return disbursementDepositDetailView;
    }

    public void setDisbursementDepositDetailView(DisbursementDepositBaDetailView disbursementDepositDetailView) {
        this.disbursementDepositDetailView = disbursementDepositDetailView;
    }

    public DisbursementDepositBaDetailView getNewDisbursementDepositDetailView() {
        return newDisbursementDepositDetailView;
    }

    public void setNewDisbursementDepositDetailView(DisbursementDepositBaDetailView newDisbursementDepositDetailView) {
        this.newDisbursementDepositDetailView = newDisbursementDepositDetailView;
    }

    public List<DisbursementDepositBaDetailView> getDisbursementDepositList() {
        return disbursementDepositList;
    }

    public void setDisbursementDepositList(List<DisbursementDepositBaDetailView> disbursementDepositList) {
        this.disbursementDepositList = disbursementDepositList;
    }

    public DisbursementBahtnetDetailView getDisbursementBahtnetDetailView() {
        return disbursementBahtnetDetailView;
    }

    public void setDisbursementBahtnetDetailView(DisbursementBahtnetDetailView disbursementBahtnetDetailView) {
        this.disbursementBahtnetDetailView = disbursementBahtnetDetailView;
    }

    public DisbursementBahtnetDetailView getNewDisbursementBahtnetDetailView() {
        return newDisbursementBahtnetDetailView;
    }

    public void setNewDisbursementBahtnetDetailView(DisbursementBahtnetDetailView newDisbursementBahtnetDetailView) {
        this.newDisbursementBahtnetDetailView = newDisbursementBahtnetDetailView;
    }

    public List<DisbursementBahtnetDetailView> getDisbursementBahtnetList() {
        return disbursementBahtnetList;
    }

    public void setDisbursementBahtnetList(List<DisbursementBahtnetDetailView> disbursementBahtnetList) {
        this.disbursementBahtnetList = disbursementBahtnetList;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }
}
