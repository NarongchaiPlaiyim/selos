package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BasicInfoAccountView;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@ManagedBean(name = "basicInfo")
public class BasicInfo implements Serializable {
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
    private ProductGroupDAO productGroupDAO;
    @Inject
    private SpecialProgramDAO specialProgramDAO;
    @Inject
    private RequestTypeDAO requestTypeDAO;
    @Inject
    private RiskTypeDAO riskTypeDAO;
    @Inject
    private SBFScoreDAO sbfScoreDAO;
    @Inject
    private OpenAccountTypeDAO openAccountTypeDAO;
    @Inject
    private OpenAccountProductDAO openAccountProductDAO;
    @Inject
    private OpenAccountPurposeDAO openAccountPurposeDAO;
    @Inject
    private BankDAO bankDAO;

    //*** Drop down List ***//
    private List<ProductGroup> productGroupList;
    private List<SpecialProgram> specialProgramList;
    private List<RequestType> requestTypeList;
    private List<RiskType> riskTypeList;
    private List<SBFScore> sbfScoreList;
    private List<Bank> bankList;

    private List<OpenAccountType> openAccountTypeList;
    private List<OpenAccountProduct> openAccountProductList;
    private List<OpenAccountPurpose> openAccountPurposeList;

    //*** View ***//
    private BasicInfoView basicInfoView;

    //Dialog
    private BasicInfoAccountView basicInfoAccountView;
    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private BasicInfoAccountView selectAccount;
    private int rowIndex;

    public BasicInfo(){
    }

    @PostConstruct
    public void onCreation() {
        basicInfoView = new BasicInfoView();

        basicInfoView.setQualitative("A");
        basicInfoView.setBaPayment("TOPUP");

        basicInfoAccountView = new BasicInfoAccountView();

        productGroupList = productGroupDAO.findAll();
        specialProgramList = specialProgramDAO.findAll();
        requestTypeList = requestTypeDAO.findAll();
        riskTypeList = riskTypeDAO.findAll();
        sbfScoreList = sbfScoreDAO.findAll();
        bankList = bankDAO.getListRefinance();

        openAccountTypeList = openAccountTypeDAO.findAll();
        openAccountProductList = openAccountProductDAO.findAll();
        openAccountPurposeList = openAccountPurposeDAO.findAll();
    }

    public void onAddAccount(){
        basicInfoAccountView = new BasicInfoAccountView();
        modeForButton = ModeForButton.ADD;
    }

    public void onEditAccount(){
        basicInfoAccountView = new BasicInfoAccountView();
        basicInfoAccountView = selectAccount;
        modeForButton = ModeForButton.EDIT;
    }

    public void onSave(){
        log.debug("basicInfoView : {}", basicInfoView);
    }

    public void addAccount(){
        if(basicInfoAccountView.getAccountType().getId() != 0){
            basicInfoAccountView.setAccountType(openAccountTypeDAO.findById(basicInfoAccountView.getAccountType().getId()));
        }else{
            basicInfoAccountView.getAccountType().setName("-");
        }

        if(basicInfoAccountView.getProduct().getId() != 0){
            basicInfoAccountView.setProduct(openAccountProductDAO.findById(basicInfoAccountView.getProduct().getId()));
        }else{
            basicInfoAccountView.getProduct().setName("-");
        }

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
            basicInfoView.getBasicInfoAccountViews().add(basicInfoAccountView);
        }else{
            basicInfoView.getBasicInfoAccountViews().set(rowIndex,basicInfoAccountView);
        }
    }

    // Get Set
    public BasicInfoView getBasicInfoView() {
        return basicInfoView;
    }

    public void setBasicInfoView(BasicInfoView basicInfoView) {
        this.basicInfoView = basicInfoView;
    }

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<ProductGroup> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public List<RequestType> getRequestTypeList() {
        return requestTypeList;
    }

    public void setRequestTypeList(List<RequestType> requestTypeList) {
        this.requestTypeList = requestTypeList;
    }

    public List<SpecialProgram> getSpecialProgramList() {
        return specialProgramList;
    }

    public void setSpecialProgramList(List<SpecialProgram> specialProgramList) {
        this.specialProgramList = specialProgramList;
    }

    public List<RiskType> getRiskTypeList() {
        return riskTypeList;
    }

    public void setRiskTypeList(List<RiskType> riskTypeList) {
        this.riskTypeList = riskTypeList;
    }

    public BasicInfoAccountView getBasicInfoAccountView() {
        return basicInfoAccountView;
    }

    public void setBasicInfoAccountView(BasicInfoAccountView basicInfoAccountView) {
        this.basicInfoAccountView = basicInfoAccountView;
    }

    public List<OpenAccountType> getOpenAccountTypeList() {
        return openAccountTypeList;
    }

    public void setOpenAccountTypeList(List<OpenAccountType> openAccountTypeList) {
        this.openAccountTypeList = openAccountTypeList;
    }

    public List<OpenAccountProduct> getOpenAccountProductList() {
        return openAccountProductList;
    }

    public void setOpenAccountProductList(List<OpenAccountProduct> openAccountProductList) {
        this.openAccountProductList = openAccountProductList;
    }

    public List<OpenAccountPurpose> getOpenAccountPurposeList() {
        return openAccountPurposeList;
    }

    public void setOpenAccountPurposeList(List<OpenAccountPurpose> openAccountPurposeList) {
        this.openAccountPurposeList = openAccountPurposeList;
    }

    public List<SBFScore> getSbfScoreList() {
        return sbfScoreList;
    }

    public void setSbfScoreList(List<SBFScore> sbfScoreList) {
        this.sbfScoreList = sbfScoreList;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }

    public BasicInfoAccountView getSelectAccount() {
        return selectAccount;
    }

    public void setSelectAccount(BasicInfoAccountView selectAccount) {
        this.selectAccount = selectAccount;
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
}
