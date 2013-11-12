package com.clevel.selos.controller;


import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.*;
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
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "creditFacPropose")
public class CreditFacPropose implements Serializable {

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

    private Long workCaseId;
    private User user;

    enum ModeForButton {ADD, EDIT}

    private ModeForButton modeForButton;

    enum ModeForDB {ADD_DB, EDIT_DB, CANCEL_DB}

    private ModeForDB modeForDB;
    private String messageHeader;
    private String message;
    private boolean messageErr;

    private List<CreditRequestType> creditRequestTypeList;
    private CreditRequestType creditRequestTypeSelected;
    private List<Country> countryList;
    private Country countrySelected;
    private List<ProductProgram> productProgramList;
    private List<CreditType> creditTypeList;
    private List<Disbursement> disbursementList;

    private CreditFacProposeView creditFacProposeView;
    private ProposeCreditDetailView proposeCreditDetailView;

    // for control Propose Collateral
    private ProposeCollateralInfoView proposeCollateralInfoView;
    private ProposeCollateralInfoView selectCollateralDetailView;

    private SubCollateralDetailView subCollateralDetailView;
    private List<SubCollateralType> subCollateralTypeList;
    private List<CollateralType> collateralTypeList;

    // for  control Guarantor Information Dialog
    private GuarantorDetailView guarantorDetailView;
    private GuarantorDetailView guarantorDetailViewItem;
    private List<Customer> guarantorList;

    // for  control Condition Information Dialog
    private ConditionInfoDetailView conditionInfoDetailView;
    private ConditionInfoDetailView selectConditionItem;


    @Inject
    UserDAO userDAO;
    @Inject
    CreditRequestTypeDAO creditRequestTypeDAO;
    @Inject
    CountryDAO countryDAO;
    @Inject
    ProductProgramDAO productProgramDAO;
    @Inject
    CreditTypeDAO creditTypeDAO;
    @Inject
    DisbursementDAO disbursementDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;

    public CreditFacPropose() {}


    @PostConstruct
    public void onCreation() {
        log.info("onCreation.");

        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("workCaseId", new Long(2));    // ไว้เทส set workCaseId ที่เปิดมาจาก Inbox
        user = (User) session.getAttribute("user");

        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ", workCaseId);
        }

        if(workCaseId != null){
            if(guarantorList ==null){
                guarantorList = new ArrayList<Customer>();
//                guarantorList = customerDAO.findGuarantorByWorkCaseId(workCaseId);
            }
        }

        if (creditFacProposeView == null) {
            creditFacProposeView = new CreditFacProposeView();
        }

        if (creditRequestTypeList == null) {
            creditRequestTypeList = new ArrayList<CreditRequestType>();
        }

        if (countryList == null) {
            countryList = new ArrayList<Country>();
        }

        if (proposeCreditDetailView == null) {
            proposeCreditDetailView = new ProposeCreditDetailView();
        }

        if (productProgramList == null) {
            productProgramList = new ArrayList<ProductProgram>();
        }

        if (creditTypeList == null) {
            creditTypeList = new ArrayList<CreditType>();
        }

        if (disbursementList == null) {
            disbursementList = new ArrayList<Disbursement>();
        }

        if (conditionInfoDetailView == null) {
            conditionInfoDetailView = new ConditionInfoDetailView();
        }

        if(guarantorDetailView == null){
            guarantorDetailView = new GuarantorDetailView();
        }

        if(proposeCollateralInfoView == null){
            proposeCollateralInfoView = new ProposeCollateralInfoView();
        }

        if(subCollateralDetailView == null){
            subCollateralDetailView = new SubCollateralDetailView();
        }

        if(subCollateralTypeList == null){
            subCollateralTypeList = new ArrayList<SubCollateralType>();
        }

        if(collateralTypeList == null){
            collateralTypeList = new ArrayList<CollateralType>();
        }

        creditRequestTypeList = creditRequestTypeDAO.findAll();
        countryList = countryDAO.findAll();
        productProgramList = productProgramDAO.findAll();
        creditTypeList = creditTypeDAO.findAll();
        disbursementList = disbursementDAO.findAll();
        subCollateralTypeList = subCollateralTypeDAO.findAll();
        collateralTypeList = collateralTypeDAO.findAll();
    }

    //Call  COMs to get data Propose Credit Info
    public void onCallRetrieveProposeCreditInfo() {

    }

    // Call  COMs to get Data Propose Collateral
    public void onCallRetrieveAppraisalReportInfo(){

    }

    //  Start Propose Credit Information  //
    public void onAddCreditInfo() {
    }

    public void onEditCreditInfo() {

    }

    public void onDeleteCreditInfo() {

    }
    //  END Propose Credit Information  //

    //  Start Propose Collateral Information  //
    public void onAddProposeCollInfo() {

    }

    public void onEditProposeCollInfo() {

    }

    public void onSaveProposeCollInfo() {

    }

    public void onDeleteProposeCollInfo() {

    }

    // for sub collateral dialog
    public void onAddCollateralOwnerUW(){

    }

    public void onAddMortgageType(){

    }

    public void onAddRelatedWith(){

    }
    // end for sub collateral dialog

    //  END Propose Collateral Information  //

    // Start Add SUB Collateral
    public void onAddSubCollateral(){

    }

    public void onEditSubCollateral(){

    }

    public void onSaveSubCollateral(){

    }


    public void onDeleteSubCollateral(){

    }
    // END Add SUB Collateral

    //  Start Guarantor //
    public void onAddGuarantorInfo() {

    }

    public void onEditGuarantorInfo() {

    }

    public void onSaveGuarantorInfoDlg(){

    }

    public void onDeleteGuarantorInfo() {

    }
    //  END Guarantor //

    //Start Condition Information //
    public void onAddConditionInfo() {
        log.info("onAddConditionInfo ::: ");
        conditionInfoDetailView = new ConditionInfoDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onSaveConditionInfoDlg(){
        log.info("onSaveConditionInfoDlg ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){

            ConditionInfoDetailView conditionInfoDetailViewAdd = new ConditionInfoDetailView();
            conditionInfoDetailViewAdd.setLoanType(conditionInfoDetailView.getLoanType());
            conditionInfoDetailViewAdd.setConditionDesc(conditionInfoDetailView.getConditionDesc());
            creditFacProposeView.getConditionInfoDetailViewList().add(conditionInfoDetailViewAdd);
            complete = true;

        } else {

            log.info("onSaveConditionInfoDlg ::: validation failed.");
            complete = false;
        }

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);
    }


    public void onDeleteConditionInfo() {
       log.info("onDeleteConditionInfo :: ");
       creditFacProposeView.getConditionInfoDetailViewList().remove(selectConditionItem);
    }

    // END Condition Information //

    // Database Action
    public void onSaveCreditFacPropose() {
        log.info("onSaveCreditFacPropose ::: ModeForDB  {}", modeForDB);

        try {
            messageHeader = msg.get("app.header.save.success");
            message = msg.get("");
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");


        } catch (Exception ex) {
            log.error("Exception : {}", ex);
            messageHeader = msg.get("app.header.save.failed");

            if (ex.getCause() != null) {
                message = msg.get("") + " cause : " + ex.getCause().toString();
            } else {
                message = msg.get("") + ex.getMessage();
            }

            messageErr = true;
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        }

    }


    public void onCancelCreditFacPropose() {
        modeForDB = ModeForDB.CANCEL_DB;
        log.info("onCancelCreditFacPropose ::: ");

        onCreation();
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

    public List<CreditRequestType> getCreditRequestTypeList() {
        return creditRequestTypeList;
    }

    public void setCreditRequestTypeList(List<CreditRequestType> creditRequestTypeList) {
        this.creditRequestTypeList = creditRequestTypeList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public CreditRequestType getCreditRequestTypeSelected() {
        return creditRequestTypeSelected;
    }

    public void setCreditRequestTypeSelected(CreditRequestType creditRequestTypeSelected) {
        this.creditRequestTypeSelected = creditRequestTypeSelected;
    }

    public Country getCountrySelected() {
        return countrySelected;
    }

    public void setCountrySelected(Country countrySelected) {
        this.countrySelected = countrySelected;
    }

    public CreditFacProposeView getCreditFacProposeView() {
        return creditFacProposeView;
    }

    public void setCreditFacProposeView(CreditFacProposeView creditFacProposeView) {
        this.creditFacProposeView = creditFacProposeView;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProposeCreditDetailView getProposeCreditDetailView() {
        return proposeCreditDetailView;
    }

    public void setProposeCreditDetailView(ProposeCreditDetailView proposeCreditDetailView) {
        this.proposeCreditDetailView = proposeCreditDetailView;
    }

    public List<ProductProgram> getProductProgramList() {
        return productProgramList;
    }

    public void setProductProgramList(List<ProductProgram> productProgramList) {
        this.productProgramList = productProgramList;
    }

    public List<CreditType> getCreditTypeList() {
        return creditTypeList;
    }

    public void setCreditTypeList(List<CreditType> creditTypeList) {
        this.creditTypeList = creditTypeList;
    }

    public List<Disbursement> getDisbursementList() {
        return disbursementList;
    }

    public void setDisbursementList(List<Disbursement> disbursementList) {
        this.disbursementList = disbursementList;
    }

    public ConditionInfoDetailView getConditionInfoDetailView() {
        return conditionInfoDetailView;
    }

    public void setConditionInfoDetailView(ConditionInfoDetailView conditionInfoDetailView) {
        this.conditionInfoDetailView = conditionInfoDetailView;
    }

    public ConditionInfoDetailView getSelectConditionItem() {
        return selectConditionItem;
    }

    public void setSelectConditionItem(ConditionInfoDetailView selectConditionItem) {
        this.selectConditionItem = selectConditionItem;
    }

    public GuarantorDetailView getGuarantorDetailViewItem() {
        return guarantorDetailViewItem;
    }

    public void setGuarantorDetailViewItem(GuarantorDetailView guarantorDetailViewItem) {
        this.guarantorDetailViewItem = guarantorDetailViewItem;
    }

    public GuarantorDetailView getGuarantorDetailView() {
        return guarantorDetailView;
    }

    public void setGuarantorDetailView(GuarantorDetailView guarantorDetailView) {
        this.guarantorDetailView = guarantorDetailView;
    }

    public List<Customer> getGuarantorList() {
        return guarantorList;
    }

    public void setGuarantorList(List<Customer> guarantorList) {
        this.guarantorList = guarantorList;
    }

    public ProposeCollateralInfoView getProposeCollateralInfoView() {
        return proposeCollateralInfoView;
    }

    public void setProposeCollateralInfoView(ProposeCollateralInfoView proposeCollateralInfoView) {
        this.proposeCollateralInfoView = proposeCollateralInfoView;
    }

    public ProposeCollateralInfoView getSelectCollateralDetailView() {
        return selectCollateralDetailView;
    }

    public void setSelectCollateralDetailView(ProposeCollateralInfoView selectCollateralDetailView) {
        this.selectCollateralDetailView = selectCollateralDetailView;
    }

    public SubCollateralDetailView getSubCollateralDetailView() {
        return subCollateralDetailView;
    }

    public void setSubCollateralDetailView(SubCollateralDetailView subCollateralDetailView) {
        this.subCollateralDetailView = subCollateralDetailView;
    }

    public List<SubCollateralType> getSubCollateralTypeList() {
        return subCollateralTypeList;
    }

    public void setSubCollateralTypeList(List<SubCollateralType> subCollateralTypeList) {
        this.subCollateralTypeList = subCollateralTypeList;
    }

    public List<CollateralType> getCollateralTypeList() {
        return collateralTypeList;
    }

    public void setCollateralTypeList(List<CollateralType> collateralTypeList) {
        this.collateralTypeList = collateralTypeList;
    }
}

