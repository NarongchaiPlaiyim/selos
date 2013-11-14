package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.CreditFacProposeControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.*;
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

    //for control Propose Credit
    private ProposeCreditDetailView proposeCreditDetailView;
    private ProposeCreditDetailView proposeCreditDetailSelected;
    CreditTierDetailView creditTierDetailView;
    List<CreditTierDetailView> creditTierDetailViewList;
    private int rowSpanNumber;
    private boolean modeEdit;

    // for control Propose Collateral
    private ProposeCollateralInfoView proposeCollateralInfoView;
    private ProposeCollateralInfoView selectCollateralDetailView;

    private SubCollateralDetailView subCollateralDetailView;
    private List<SubCollateralType> subCollateralTypeList;
    private List<CollateralType> collateralTypeList;
    private List<PotentialCollateral> potentialCollateralList;
    // for  control Guarantor Information Dialog
    private ProposeGuarantorDetailView proposeGuarantorDetailView;
    private ProposeGuarantorDetailView proposeGuarantorDetailViewItem;
    private List<Customer> guarantorList;

    // for  control Condition Information Dialog
    private ProposeConditionDetailView proposeConditionDetailView;
    private ProposeConditionDetailView selectConditionItem;

    AppraisalData appraisalData;
    HeadCollateralData headCollateralData;
    List<SubCollateralData> subCollateralDataList;
    SubCollateralData subCollateralData;

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
    @Inject
    CreditFacProposeControl creditFacProposeControl;
    @Inject
    PotentialCollateralDAO potentialCollateralDAO;

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

        if (proposeConditionDetailView == null) {
            proposeConditionDetailView = new ProposeConditionDetailView();
        }

        if(proposeGuarantorDetailView == null){
            proposeGuarantorDetailView = new ProposeGuarantorDetailView();
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

        if(potentialCollateralList == null){
            potentialCollateralList = new ArrayList<PotentialCollateral>();
        }

        creditRequestTypeList = creditRequestTypeDAO.findAll();
        countryList = countryDAO.findAll();
        productProgramList = productProgramDAO.findAll();
        creditTypeList = creditTypeDAO.findAll();
        disbursementList = disbursementDAO.findAll();
        subCollateralTypeList = subCollateralTypeDAO.findAll();
        collateralTypeList = collateralTypeDAO.findAll();
        potentialCollateralList = potentialCollateralDAO.findAll();
        rowSpanNumber = 1;
        modeEdit = true;
    }

    //Call  BRMS to get data Propose Credit Info
    public void onCallRetrieveProposeCreditInfo() {

    }

    // Call  COMs to get Data Propose Collateral
    public void onCallRetrieveAppraisalReportInfo(){
        log.info("onCallRetrieveAppraisalReportInfo begin key is  :: {}" ,proposeCollateralInfoView.getJobID() );

        //COMSInterface

        log.info("getData From COMS begin");
        appraisalData = new AppraisalData();
        appraisalData.setJobId(proposeCollateralInfoView.getJobID());
        appraisalData.setAppraisalDate(DateTime.now().toDate());
        appraisalData.setAadDecision("ผ่าน");
        appraisalData.setAadDecisionReason("กู้");
        appraisalData.setAadDecisionReasonDetail("ok");

        headCollateralData = new HeadCollateralData();
        headCollateralData.setCollateralLocation("ประเทศไทย แลน ออฟ สไมล์");
        headCollateralData.setTitleDeed("กค 126,ญก 156");
        headCollateralData.setAppraisalValue("4810000");
        appraisalData.setHeadCollateralData(headCollateralData);

        subCollateralDataList = new ArrayList<SubCollateralData>();
        subCollateralData = new SubCollateralData();
        subCollateralData.setLandOffice("ขอนแก่น");
        subCollateralData.setAddress("ถนน ข้าวแนว จ ขอนแก่น");
        subCollateralData.setTitleDeed("กค 126");
        subCollateralData.setCollateralOwner("AAAA");
        subCollateralData.setAppraisalValue(new BigDecimal(3810000));
        subCollateralDataList.add(subCollateralData);

        subCollateralData = new SubCollateralData();

        subCollateralData.setTitleDeed("ญก 156");
        subCollateralData.setLandOffice("กทม");
        subCollateralData.setAddress("ถนน ข้าวหมาก จ กรุงเทพมหานคร");
        subCollateralData.setCollateralOwner("BBB");
        subCollateralData.setAppraisalValue(new BigDecimal(1000000));
        subCollateralDataList.add(subCollateralData);

        appraisalData.setSubCollateralDataList(subCollateralDataList);

        proposeCollateralInfoView =  creditFacProposeControl.transformsCOMSToModelView(appraisalData);


        log.info("onCallRetrieveAppraisalReportInfo End");

    }

    //  Start Propose Credit Information  //
    public void onAddCreditInfo() {
        log.info("onAddCreditInfo ::: ");
        proposeCreditDetailView  = new ProposeCreditDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onEditCreditInfo() {
        modeEdit = false;
    }

    public void onSaveCreditInfo() {
        log.info("onSaveCreditInfo ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if((proposeCreditDetailView.getProductProgram().getId() != 0 ) && (proposeCreditDetailView.getCreditType().getId() !=0))
        {
            if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){

                ProductProgram  productProgram = productProgramDAO.findById(proposeCreditDetailView.getProductProgram().getId());
                CreditType creditType = creditTypeDAO.findById(proposeCreditDetailView.getCreditType().getId());

                ProposeCreditDetailView creditDetailAdd = new ProposeCreditDetailView();
                creditDetailAdd.setRequestType(proposeCreditDetailView.getRequestType());
                creditDetailAdd.setRefinance(proposeCreditDetailView.getRefinance());
                creditDetailAdd.setProductProgram(productProgram);
                creditDetailAdd.setCreditType(creditType);
                creditDetailAdd.setProductCode(proposeCreditDetailView.getProductCode());
                creditDetailAdd.setProjectCode(proposeCreditDetailView.getProjectCode());
                creditDetailAdd.setLimit(proposeCreditDetailView.getLimit());
                creditDetailAdd.setPCEPercent(proposeCreditDetailView.getPCEPercent());
                creditDetailAdd.setPCEAmount(proposeCreditDetailView.getPCEAmount());

                creditTierDetailViewList = new ArrayList<CreditTierDetailView>();
                creditTierDetailView = new CreditTierDetailView();
                creditTierDetailViewList.add(creditTierDetailView);
                creditTierDetailView = new CreditTierDetailView();
                creditTierDetailViewList.add(creditTierDetailView);
                creditDetailAdd.setCreditTierDetailViewList(creditTierDetailViewList);
                creditFacProposeView.getProposeCreditDetailViewList().add(creditDetailAdd);

            }else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){


            }else {
                log.info("onSaveNcbRecord ::: Undefined modeForButton !!");
            }

            complete = true;

        } else {

            log.info("onSaveNcbRecord ::: validation failed.");
            complete = false;
        }
        rowSpanNumber = creditFacProposeView.getProposeCreditDetailViewList().size();
        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

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
        proposeConditionDetailView = new ProposeConditionDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onSaveConditionInfoDlg(){
        log.info("onSaveConditionInfoDlg ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){

            ProposeConditionDetailView proposeConditionDetailViewAdd = new ProposeConditionDetailView();
            proposeConditionDetailViewAdd.setLoanType(proposeConditionDetailView.getLoanType());
            proposeConditionDetailViewAdd.setConditionDesc(proposeConditionDetailView.getConditionDesc());
            creditFacProposeView.getProposeConditionDetailViewList().add(proposeConditionDetailViewAdd);
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
       creditFacProposeView.getProposeConditionDetailViewList().remove(selectConditionItem);
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

    public ProposeConditionDetailView getProposeConditionDetailView() {
        return proposeConditionDetailView;
    }

    public void setProposeConditionDetailView(ProposeConditionDetailView proposeConditionDetailView) {
        this.proposeConditionDetailView = proposeConditionDetailView;
    }

    public ProposeConditionDetailView getSelectConditionItem() {
        return selectConditionItem;
    }

    public void setSelectConditionItem(ProposeConditionDetailView selectConditionItem) {
        this.selectConditionItem = selectConditionItem;
    }

    public ProposeGuarantorDetailView getProposeGuarantorDetailViewItem() {
        return proposeGuarantorDetailViewItem;
    }

    public void setProposeGuarantorDetailViewItem(ProposeGuarantorDetailView proposeGuarantorDetailViewItem) {
        this.proposeGuarantorDetailViewItem = proposeGuarantorDetailViewItem;
    }

    public ProposeGuarantorDetailView getProposeGuarantorDetailView() {
        return proposeGuarantorDetailView;
    }

    public void setProposeGuarantorDetailView(ProposeGuarantorDetailView proposeGuarantorDetailView) {
        this.proposeGuarantorDetailView = proposeGuarantorDetailView;
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

    public List<PotentialCollateral> getPotentialCollateralList() {
        return potentialCollateralList;
    }

    public void setPotentialCollateralList(List<PotentialCollateral> potentialCollateralList) {
        this.potentialCollateralList = potentialCollateralList;
    }

    public int getRowSpanNumber() {
        return rowSpanNumber;
    }

    public void setRowSpanNumber(int rowSpanNumber) {
        this.rowSpanNumber = rowSpanNumber;
    }

    public boolean isModeEdit() {
        return modeEdit;
    }

    public void setModeEdit(boolean modeEdit) {
        this.modeEdit = modeEdit;
    }

    public ProposeCreditDetailView getProposeCreditDetailSelected() {
        return proposeCreditDetailSelected;
    }

    public void setProposeCreditDetailSelected(ProposeCreditDetailView proposeCreditDetailSelected) {
        this.proposeCreditDetailSelected = proposeCreditDetailSelected;
    }

    //

    public CreditTierDetailView getCreditTierDetailView() {
        return creditTierDetailView;
    }

    public void setCreditTierDetailView(CreditTierDetailView creditTierDetailView) {
        this.creditTierDetailView = creditTierDetailView;
    }
}

