package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.master.BorrowingTypeDAO;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BasicInfoAccountPurposeView;
import com.clevel.selos.model.view.BasicInfoAccountView;
import com.clevel.selos.model.view.BasicInfoView;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
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
    @Inject
    private BasicInfoControl basicInfoControl;
    @Inject
    private BorrowingTypeDAO borrowingTypeDAO;
    @Inject
    private BAPaymentMethodDAO baPaymentMethodDAO;

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

    private List<BasicInfoAccountPurposeView> basicInfoAccountPurposeViewList;

    private List<BorrowingType> borrowingTypeList;
    private List<BAPaymentMethod> baPaymentMethodList;

    //*** View ***//
    private BasicInfoView basicInfoView;

    //Dialog
    private BasicInfoAccountView basicInfoAccountView;
    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private BasicInfoAccountView selectAccount;
    private int rowIndex;

    private String messageHeader;
    private String message;

    //session
    private long workCaseId;
    private long stepId;
    private String userId;

    public BasicInfo(){
    }

    public void preRender(){
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 101);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);

        log.info("preRender ::: setSession ");

        session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();
        }else{
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
            try{
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                return;
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }
    }


    @PostConstruct
    public void onCreation() {

        preRender();

        basicInfoView = new BasicInfoView();

        productGroupList = productGroupDAO.findAll();
        specialProgramList = specialProgramDAO.findAll();
        requestTypeList = requestTypeDAO.findAll();
        riskTypeList = riskTypeDAO.findAll();
        sbfScoreList = sbfScoreDAO.findAll();
        bankList = bankDAO.getListRefinance();

        openAccountTypeList = openAccountTypeDAO.findAll();
        openAccountProductList = openAccountProductDAO.findAll();

        openAccountPurposeList = openAccountPurposeDAO.findAll();
        basicInfoAccountPurposeViewList = new ArrayList<BasicInfoAccountPurposeView>();
        for(OpenAccountPurpose oap : openAccountPurposeList){
            BasicInfoAccountPurposeView purposeView = new BasicInfoAccountPurposeView();
            purposeView.setPurpose(oap);
            basicInfoAccountPurposeViewList.add(purposeView);
        }

        CustomerEntity customerEntity = basicInfoControl.getCustomerEntityByWorkCaseId(workCaseId);

        borrowingTypeList = borrowingTypeDAO.findByCustomerEntity(customerEntity);

        baPaymentMethodList = baPaymentMethodDAO.findAll();

        if(baPaymentMethodList != null){
            basicInfoView.setBaPaymentMethod(baPaymentMethodList.get(0));
        }

        basicInfoView = basicInfoControl.getBasicInfo(workCaseId);

        if(basicInfoView.getId() == 0){
            basicInfoView.setQualitative(customerEntity.getDefaultQualitative());
        }

        if(customerEntity.isChangeQualtiEnable()){
            basicInfoView.setIndividual(1);
        }else{
            basicInfoView.setIndividual(0);
        }

        basicInfoAccountView = new BasicInfoAccountView();
    }

    public void onAddAccount(){
        basicInfoAccountView = new BasicInfoAccountView();

        openAccountPurposeList = openAccountPurposeDAO.findAll();
        basicInfoAccountPurposeViewList = new ArrayList<BasicInfoAccountPurposeView>();
        for(OpenAccountPurpose oap : openAccountPurposeList){
            BasicInfoAccountPurposeView purposeView = new BasicInfoAccountPurposeView();
            purposeView.setPurpose(oap);
            basicInfoAccountPurposeViewList.add(purposeView);
        }

        modeForButton = ModeForButton.ADD;
    }

    public void onEditAccount(){
        basicInfoAccountView = new BasicInfoAccountView();
        basicInfoAccountView = selectAccount;
        for(BasicInfoAccountPurposeView biapv : basicInfoAccountView.getBasicInfoAccountPurposeView()){
            if(biapv.isSelected()){
                for(BasicInfoAccountPurposeView purposeView : basicInfoAccountPurposeViewList){
                    if(biapv.getPurpose().getName().equals(purposeView.getPurpose().getName())){
                        purposeView.setSelected(true);
                    }
                }
            }
        }
        modeForButton = ModeForButton.EDIT;
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

        StringBuilder stringBuilder = new StringBuilder();

        basicInfoAccountView.setBasicInfoAccountPurposeView(new ArrayList<BasicInfoAccountPurposeView>());
        for(BasicInfoAccountPurposeView bia : basicInfoAccountPurposeViewList){
            if(bia.isSelected()){
                if(basicInfoAccountView.getBasicInfoAccountPurposeView().size() == 0){
                    basicInfoAccountView.getBasicInfoAccountPurposeView().add(bia);
                    stringBuilder.append(bia.getPurpose().getName());
                }else{
                    basicInfoAccountView.getBasicInfoAccountPurposeView().add(bia);
                    stringBuilder.append(", "+bia.getPurpose().getName());
                }
            }
        }

        if(!stringBuilder.toString().isEmpty()){
            basicInfoAccountView.setPurposeForShow(stringBuilder.toString());
        }else{
            basicInfoAccountView.setPurposeForShow("-");
        }

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
            basicInfoView.getBasicInfoAccountViews().add(basicInfoAccountView);
        }else{
            basicInfoView.getBasicInfoAccountViews().set(rowIndex,basicInfoAccountView);
        }
    }

    public void onDeleteAccount() {
        basicInfoView.getBasicInfoAccountViews().remove(selectAccount);
    }

    public void onSave(){
        try{
            basicInfoControl.saveBasicInfo(basicInfoView, workCaseId, userId);
            messageHeader = "Save Basic Info Success.";
            message = "Save Basic Info data success.";
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            messageHeader = "Save Basic Info Failed.";
            if(ex.getCause() != null){
                message = "Save Basic Info data failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save Basic Info data failed. Cause : " + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onChangeSpecialProgram(){
        basicInfoView.getSpecialProgram().setId(0);
    }

    public void onChangeRefIn(){
        basicInfoView.getRefinanceIn().setCode(0);
    }

    public void onChangeRefOut(){
        basicInfoView.getRefinanceOut().setCode(0);
    }

    public void onChangeBA(){
        if(basicInfoView.getApplyBA() == 0){
            basicInfoView.getBaPaymentMethod().setId(0);
        }else{
            if(baPaymentMethodList != null && baPaymentMethodList.size() > 0){
                basicInfoView.getBaPaymentMethod().setId(baPaymentMethodList.get(0).getId());
            }else{
                basicInfoView.getBaPaymentMethod().setId(0);
            }
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

    public List<BasicInfoAccountPurposeView> getBasicInfoAccountPurposeViewList() {
        return basicInfoAccountPurposeViewList;
    }

    public void setBasicInfoAccountPurposeViewList(List<BasicInfoAccountPurposeView> basicInfoAccountPurposeViewList) {
        this.basicInfoAccountPurposeViewList = basicInfoAccountPurposeViewList;
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

    public List<BorrowingType> getBorrowingTypeList() {
        return borrowingTypeList;
    }

    public void setBorrowingTypeList(List<BorrowingType> borrowingTypeList) {
        this.borrowingTypeList = borrowingTypeList;
    }

    public List<BAPaymentMethod> getBaPaymentMethodList() {
        return baPaymentMethodList;
    }

    public void setBaPaymentMethodList(List<BAPaymentMethod> baPaymentMethodList) {
        this.baPaymentMethodList = baPaymentMethodList;
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
