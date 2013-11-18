package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.CreditFacProposeControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
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
    int rowIndex;
    private String messageHeader;
    private String message;
    private boolean messageErr;

    private List<CreditRequestType> creditRequestTypeList;
    private CreditRequestType creditRequestTypeSelected;
    private List<Country> countryList;
    private Country countrySelected;
    private List<ProductProgram> productProgramList;
    private List<CreditType> creditTypeList;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;
    private List<Disbursement> disbursementList;

    private CreditFacProposeView creditFacProposeView;

    //for control Propose Credit
    private CreditInfoDetailView creditInfoDetailView;
    private CreditInfoDetailView proposeCreditDetailSelected;
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
    private GuarantorDetailView guarantorDetailView;
    private GuarantorDetailView guarantorDetailViewItem;
    private List<Customer> guarantorList;
    List<CreditTypeDetailView> creditTypeDetailList;
    CreditTypeDetailView creditTypeDetailView;
    // for  control Condition Information Dialog
    private ConditionDetailView conditionDetailView;
    private ConditionDetailView selectConditionItem;

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

    public CreditFacPropose() {
    }


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

        if (workCaseId != null) {
            if (guarantorList == null) {
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

        if (creditInfoDetailView == null) {
            creditInfoDetailView = new CreditInfoDetailView();
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

        if (conditionDetailView == null) {
            conditionDetailView = new ConditionDetailView();
        }

        if (guarantorDetailView == null) {
            guarantorDetailView = new GuarantorDetailView();
        }

        if (proposeCollateralInfoView == null) {
            proposeCollateralInfoView = new ProposeCollateralInfoView();
        }

        if (subCollateralDetailView == null) {
            subCollateralDetailView = new SubCollateralDetailView();
        }

        if (subCollateralTypeList == null) {
            subCollateralTypeList = new ArrayList<SubCollateralType>();
        }

        if (collateralTypeList == null) {
            collateralTypeList = new ArrayList<CollateralType>();
        }

        if (potentialCollateralList == null) {
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

        modeEdit = true;
    }

    //Call  BRMS to get data Propose Credit Info
    public void onCallRetrieveProposeCreditInfo() {

    }

    // Call  COMs to get Data Propose Collateral
    public void onCallRetrieveAppraisalReportInfo() {
        log.info("onCallRetrieveAppraisalReportInfo begin key is  :: {}", proposeCollateralInfoView.getJobID());

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

        proposeCollateralInfoView = creditFacProposeControl.transformsCOMSToModelView(appraisalData);


        log.info("onCallRetrieveAppraisalReportInfo End");

    }

    //  Start Propose Credit Information  //
    public void onAddCreditInfo() {
        log.info("onAddCreditInfo ::: ");
        creditInfoDetailView = new CreditInfoDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onEditCreditInfo() {
        modeEdit = false;
        modeForButton = ModeForButton.EDIT;
        log.info("rowIndex :: {}", rowIndex);
        log.info("creditFacProposeView.creditInfoDetailViewList :: {}", creditFacProposeView.getCreditInfoDetailViewList());
        ProductProgram productProgram = proposeCreditDetailSelected.getProductProgram();
        CreditType creditType = proposeCreditDetailSelected.getCreditType();

        if (rowIndex < creditFacProposeView.getCreditInfoDetailViewList().size()) {
            creditInfoDetailView = new CreditInfoDetailView();
            creditInfoDetailView.setProductProgram(productProgram);
            creditInfoDetailView.setCreditType(creditType);
            creditInfoDetailView.setRequestType(proposeCreditDetailSelected.getRequestType());
            creditInfoDetailView.setRefinance(proposeCreditDetailSelected.getRefinance());
            creditInfoDetailView.setProductCode(proposeCreditDetailSelected.getProductCode());
            creditInfoDetailView.setProjectCode(proposeCreditDetailSelected.getProjectCode());
            creditInfoDetailView.setLimit(proposeCreditDetailSelected.getLimit());
            creditInfoDetailView.setPCEPercent(proposeCreditDetailSelected.getPCEPercent());
            creditInfoDetailView.setPCEAmount(proposeCreditDetailSelected.getPCEAmount());

            creditTierDetailViewList = new ArrayList<CreditTierDetailView>();
            creditInfoDetailView.setCreditTierDetailViewList(proposeCreditDetailSelected.getCreditTierDetailViewList());
        }
    }

    public void onSaveCreditInfo() {
        log.info("onSaveCreditInfo ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if ((creditInfoDetailView.getProductProgram().getId() != 0) && (creditInfoDetailView.getCreditType().getId() != 0)) {
            if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
//                rowSpanNumber = 1;
                ProductProgram productProgram = productProgramDAO.findById(creditInfoDetailView.getProductProgram().getId());
                CreditType creditType = creditTypeDAO.findById(creditInfoDetailView.getCreditType().getId());

                CreditInfoDetailView creditDetailAdd = new CreditInfoDetailView();
                creditDetailAdd.setRequestType(creditInfoDetailView.getRequestType());
                creditDetailAdd.setRefinance(creditInfoDetailView.getRefinance());
                creditDetailAdd.setProductProgram(productProgram);
                creditDetailAdd.setCreditType(creditType);
                creditDetailAdd.setProductCode(creditInfoDetailView.getProductCode());
                creditDetailAdd.setProjectCode(creditInfoDetailView.getProjectCode());
                creditDetailAdd.setLimit(creditInfoDetailView.getLimit());
                creditDetailAdd.setPCEPercent(creditInfoDetailView.getPCEPercent());
                creditDetailAdd.setPCEAmount(creditInfoDetailView.getPCEAmount());

                creditTierDetailViewList = new ArrayList<CreditTierDetailView>();
                creditTierDetailView = new CreditTierDetailView();
                creditTierDetailView.setStandardPrice(BigDecimal.ONE);
                creditTierDetailViewList.add(creditTierDetailView);
                creditTierDetailView = new CreditTierDetailView();
                creditTierDetailViewList.add(creditTierDetailView);
                creditDetailAdd.setCreditTierDetailViewList(creditTierDetailViewList);

                creditFacProposeView.getCreditInfoDetailViewList().add(creditDetailAdd);

            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.info("onEditRecord ::: mode : {}", modeForButton);
                ProductProgram productProgram = productProgramDAO.findById(creditInfoDetailView.getProductProgram().getId());
                CreditType creditType = creditTypeDAO.findById(creditInfoDetailView.getCreditType().getId());

                creditFacProposeView.getCreditInfoDetailViewList().get(rowIndex).setProductProgram(productProgram);
                creditFacProposeView.getCreditInfoDetailViewList().get(rowIndex).setCreditType(creditType);
                creditFacProposeView.getCreditInfoDetailViewList().get(rowIndex).setRequestType(creditInfoDetailView.getRequestType());
                creditFacProposeView.getCreditInfoDetailViewList().get(rowIndex).setRefinance(creditInfoDetailView.getRefinance());
                creditFacProposeView.getCreditInfoDetailViewList().get(rowIndex).setProductCode(creditInfoDetailView.getProductCode());
                creditFacProposeView.getCreditInfoDetailViewList().get(rowIndex).setProjectCode(creditInfoDetailView.getProjectCode());
                creditFacProposeView.getCreditInfoDetailViewList().get(rowIndex).setLimit(creditInfoDetailView.getLimit());
                creditFacProposeView.getCreditInfoDetailViewList().get(rowIndex).setPCEPercent(creditInfoDetailView.getPCEPercent());
                creditFacProposeView.getCreditInfoDetailViewList().get(rowIndex).setPCEAmount(creditInfoDetailView.getPCEAmount());


            } else {
                log.info("onSaveNcbRecord ::: Undefined modeForButton !!");
            }

            complete = true;

        } else {

            log.info("onSaveNcbRecord ::: validation failed.");
            complete = false;
        }

        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }


    public void onDeleteCreditInfo() {
        log.info("delete :: rowIndex :: {}", rowIndex);
        log.info("proposeCreditDetailSelected.getCreditTierDetailViewList() :: {}", proposeCreditDetailSelected.getCreditTierDetailViewList());
        creditFacProposeView.getCreditInfoDetailViewList().remove(proposeCreditDetailSelected);
    }
    //  END Propose Credit Information  //

    //  Start Tier Dialog //
    public void onAddTierInfo() {
        log.info("onAddCreditInfo ::: ");
        creditTierDetailView = new CreditTierDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onSaveTierInfo() {
        log.info("onSaveTierInfo ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
            CreditTierDetailView creditTierDetailAdd = new CreditTierDetailView();
            creditTierDetailAdd.setSuggestPrice(creditTierDetailView.getSuggestPrice());
            creditTierDetailAdd.setStandardPrice(creditTierDetailView.getStandardPrice());
            creditTierDetailAdd.setFinalPriceRate(creditTierDetailView.getFinalPriceRate());
            creditTierDetailAdd.setTenor(creditTierDetailView.getTenor());
            creditTierDetailAdd.setInstallment(creditTierDetailView.getInstallment());
        } else {
            log.info("onSaveNcbRecord ::: Undefined modeForButton !!");
            complete = false;
        }

        complete = true;
        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    public void onDeleteProposeTierInfo() {

    }


    //  END Tier Dialog //

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
    public void onAddCollateralOwnerUW() {

    }

    public void onAddMortgageType() {

    }

    public void onAddRelatedWith() {

    }
    // end for sub collateral dialog

    //  END Propose Collateral Information  //

    // Start Add SUB Collateral
    public void onAddSubCollateral() {

    }

    public void onEditSubCollateral() {

    }

    public void onSaveSubCollateral() {

    }


    public void onDeleteSubCollateral() {

    }
    // END Add SUB Collateral

    //  Start Guarantor //
    public void onAddGuarantorInfo() {
        guarantorDetailView = new GuarantorDetailView();
        modeForButton = ModeForButton.ADD;

       // find list  creditTypeDetailList of guarantor  :: test
        creditTypeDetailList = new ArrayList<CreditTypeDetailView>();

        creditTypeDetailView = new CreditTypeDetailView();
        creditTypeDetailView.setAccount("Mr. A Example " +
                                        "Acc No. xx-xxx-xxxx-xxxx");
        creditTypeDetailView.setProductProgram("SmartBiz");
        creditTypeDetailView.setCreditFacility("OD");
        creditTypeDetailView.setType("New");
        creditTypeDetailView.setLimit(BigDecimal.valueOf(200000));
        creditTypeDetailView.setGuaranteeAmount(BigDecimal.valueOf(200000));
        creditTypeDetailList.add(creditTypeDetailView);

        creditTypeDetailView = new CreditTypeDetailView();
        creditTypeDetailView.setAccount("Mr. B Example " +
                "Acc No. xx-xxx-xxxx-xxxx");
        creditTypeDetailView.setProductProgram("SmartBiz");
        creditTypeDetailView.setCreditFacility("Loan");
        creditTypeDetailView.setType("Change");
        creditTypeDetailView.setLimit(BigDecimal.valueOf(990000));
        creditTypeDetailView.setGuaranteeAmount(BigDecimal.valueOf(700000));
        creditTypeDetailList.add(creditTypeDetailView);

        guarantorDetailView.setCreditTypeDetailViewList(creditTypeDetailList);
    }

    public void onEditGuarantorInfo() {

    }

    public void onSaveGuarantorInfoDlg() {
        log.info("onSaveGuarantorInfoDlg ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if(guarantorDetailView.getGuarantorName() != null){
            if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
                GuarantorDetailView guarantorDetailAdd = new GuarantorDetailView();
                guarantorDetailAdd.setGuarantorName(guarantorDetailView.getGuarantorName());
                guarantorDetailAdd.setTcgLgNo(guarantorDetailView.getTcgLgNo());
                guarantorDetailAdd.setGuaranteeAmount(guarantorDetailView.getGuaranteeAmount());

                guarantorDetailAdd.setCreditTypeDetailViewList(guarantorDetailView.getCreditTypeDetailViewList());
                creditFacProposeView.getGuarantorDetailViewList().add(guarantorDetailAdd);
            } else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)){

            }
        }else{

        }

    }

    public void onDeleteGuarantorInfo() {

    }
    //  END Guarantor //

    //Start Condition Information //
    public void onAddConditionInfo() {
        log.info("onAddConditionInfo ::: ");
        conditionDetailView = new ConditionDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onSaveConditionInfoDlg() {
        log.info("onSaveConditionInfoDlg ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

            ConditionDetailView conditionDetailViewAdd = new ConditionDetailView();
            conditionDetailViewAdd.setLoanType(conditionDetailView.getLoanType());
            conditionDetailViewAdd.setConditionDesc(conditionDetailView.getConditionDesc());
            creditFacProposeView.getConditionDetailViewList().add(conditionDetailViewAdd);
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
        creditFacProposeView.getConditionDetailViewList().remove(selectConditionItem);
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

    public CreditInfoDetailView getCreditInfoDetailView() {
        return creditInfoDetailView;
    }

    public void setCreditInfoDetailView(CreditInfoDetailView creditInfoDetailView) {
        this.creditInfoDetailView = creditInfoDetailView;
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

    public ConditionDetailView getConditionDetailView() {
        return conditionDetailView;
    }

    public void setConditionDetailView(ConditionDetailView conditionDetailView) {
        this.conditionDetailView = conditionDetailView;
    }

    public ConditionDetailView getSelectConditionItem() {
        return selectConditionItem;
    }

    public void setSelectConditionItem(ConditionDetailView selectConditionItem) {
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

    public CreditInfoDetailView getProposeCreditDetailSelected() {
        return proposeCreditDetailSelected;
    }

    public void setProposeCreditDetailSelected(CreditInfoDetailView proposeCreditDetailSelected) {
        this.proposeCreditDetailSelected = proposeCreditDetailSelected;
    }

    public CreditTierDetailView getCreditTierDetailView() {
        return creditTierDetailView;
    }

    public void setCreditTierDetailView(CreditTierDetailView creditTierDetailView) {
        this.creditTierDetailView = creditTierDetailView;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<CreditTypeDetailView> getCreditTypeDetailList() {
        return creditTypeDetailList;
    }

    public void setCreditTypeDetailList(List<CreditTypeDetailView> creditTypeDetailList) {
        this.creditTypeDetailList = creditTypeDetailList;
    }
}

