package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.CreditFacProposeControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.BasicInfoDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.coms.model.AppraisalData;
import com.clevel.selos.integration.coms.model.HeadCollateralData;
import com.clevel.selos.integration.coms.model.SubCollateralData;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.ProposeCollateralInfoTransform;
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
    private ModeForButton modeForTier;
    private ModeForButton modeForButton;
    private ModeForButton modeForSubColl;

    enum ModeForDB {ADD_DB, EDIT_DB, CANCEL_DB}
    private ModeForDB modeForDB;

    int rowIndex;
    int rowTierIndex;
    int rowIndexGuarantor;
    int rowSubIndex;
    int rowCollHeadIndex;

    private String messageHeader;
    private String message;
    private boolean messageErr;

    private List<CreditRequestType> creditRequestTypeList;
    private List<Country> countryList;
    private List<ProductProgram> productProgramList;
    private List<CreditType> creditTypeList;
    private ProductGroup productGroup;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;
    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<Disbursement> disbursementList;
    private List<RefRate> refRateList;
    private List<RefRate> refRateDlgList;
    private CreditFacProposeView creditFacProposeView;

    private BasicInfo basicInfo;

    //for control Propose Credit
    private ProposeCreditDetailView proposeCreditDetailView;
    private ProposeCreditDetailView proposeCreditDetailSelected;
    private CreditTierDetailView creditTierDetailView;
    private List<CreditTierDetailView> creditTierDetailViewList;
    private CreditTierDetailView tierSelected;
    private int rowSpanNumber;
    private boolean modeEdit;

    // for control Propose Collateral
    private ProposeCollateralInfoView proposeCollateralInfoView;
    private ProposeCollateralInfoView selectCollateralDetailView;
    private CollateralHeaderDetailView collateralHeaderDetailView;
    private CollateralHeaderDetailView collateralHeaderDetailItem;
    private SubCollateralDetailView subCollateralDetailView;
    private SubCollateralDetailView subCollateralDetailItem;

    private List<SubCollateralType> subCollateralTypeList;
    private List<CollateralType> collateralTypeList;
    private List<PotentialCollateral> potentialCollateralList;

    // for  control Guarantor Information Dialog
    private ProposeGuarantorDetailView proposeGuarantorDetailView;
    private ProposeGuarantorDetailView proposeGuarantorDetailViewItem;
    private List<Customer> guarantorList;
    private BigDecimal sumTotalGuarantor;

    private List<CreditTypeDetailView> creditTypeDetailList;
    private CreditTypeDetailView creditTypeDetailView;

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
    PrdGroupToPrdProgramDAO prdGroupToPrdProgramDAO;
    @Inject
    ProductProgramDAO productProgramDAO;
    @Inject
    CreditTypeDAO creditTypeDAO;
    @Inject
    PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    ProductFormulaDAO productFormulaDAO;
    @Inject
    DisbursementDAO disbursementDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    CollateralTypeDAO collateralTypeDAO;
    @Inject
    PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    CreditFacProposeControl creditFacProposeControl;
    @Inject
    ProposeCollateralInfoTransform collateralInfoTransform;
    @Inject
    RefRateDAO  refRateDAO;

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
                guarantorList = creditFacProposeControl.getListOfGuarantor(workCaseId);
            }

            if (productGroup == null) {
                basicInfo = creditFacProposeControl.getBasicByWorkCaseId(workCaseId);

                if (basicInfo != null) {
                    productGroup = basicInfo.getProductGroup();
                }
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

        if (proposeGuarantorDetailView == null) {
            proposeGuarantorDetailView = new ProposeGuarantorDetailView();
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

        if (prdGroupToPrdProgramList == null) {
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        }

        if(refRateList == null){
            refRateList = new ArrayList<RefRate>();
        }

        if(refRateDlgList == null){
            refRateDlgList = new ArrayList<RefRate>();
        }
        creditRequestTypeList = creditRequestTypeDAO.findAll();
        countryList = countryDAO.findAll();

        if (productGroup != null) {
            prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramPropose(productGroup);
        }

        refRateList = refRateDAO.findAll();
        refRateDlgList = refRateDAO.findAll();
        disbursementList = disbursementDAO.findAll();
        subCollateralTypeList = subCollateralTypeDAO.findAll();
        collateralTypeList = collateralTypeDAO.findAll();
        potentialCollateralList = potentialCollateralDAO.findAll();

        modeEdit = true;
        sumTotalGuarantor = new BigDecimal(0);
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
        subCollateralData.setAddress("ถนน ข้าวเหนียว จ ขอนแก่น");
        subCollateralData.setTitleDeed("กค 126");
        subCollateralData.setCollateralOwner("AAA");
        subCollateralData.setAppraisalValue(new BigDecimal(3810000));
        subCollateralDataList.add(subCollateralData);

        appraisalData.setSubCollateralDataList(subCollateralDataList);

        proposeCollateralInfoView = collateralInfoTransform.transformsCOMSToModelView(appraisalData);
        proposeCollateralInfoView.setCreditTypeDetailViewList(findCreditFacility());

        log.info("onCallRetrieveAppraisalReportInfo End");

    }

    //  Start Propose Credit Information  //

    public void onChangeProductProgram() {
        ProductProgram productProgram = productProgramDAO.findById(proposeCreditDetailView.getProductProgram().getId());
        log.debug("onChangeProductProgram :::: productProgram : {}", productProgram);

        prdProgramToCreditTypeList = null;
        proposeCreditDetailView.setProductCode("");
        proposeCreditDetailView.setProjectCode("");

        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(productProgram);
        if (prdProgramToCreditTypeList == null) {
            prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        }
        log.debug("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " + prdProgramToCreditTypeList.size());
    }

    public void onChangeCreditType() {
        log.debug("onChangeCreditType :::: creditType : {}",proposeCreditDetailView.getCreditType().getId());
        if ((proposeCreditDetailView.getProductProgram().getId() != 0) && (proposeCreditDetailView.getCreditType().getId() != 0)) {
            ProductProgram productProgram = productProgramDAO.findById(proposeCreditDetailView.getProductProgram().getId());
            CreditType creditType = creditTypeDAO.findById(proposeCreditDetailView.getCreditType().getId());

            if(productProgram != null && creditType != null){
                PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditType,productProgram);
                ProductFormula productFormula = productFormulaDAO.findProductFormula(prdProgramToCreditType);
                log.debug("onChangeCreditType :::: productFormula : {}", productFormula.getId());

                if(productFormula != null){
                    proposeCreditDetailView.setProductCode(productFormula.getProductCode());
                    proposeCreditDetailView.setProjectCode(productFormula.getProjectCode());
                }
            }
        }

    }

    public void onAddCreditInfo() {
        log.info("onAddCreditInfo ::: ");
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        proposeCreditDetailView = new ProposeCreditDetailView();
        modeForButton = ModeForButton.ADD;

        //****** tier test create ********//
        creditTierDetailViewList = new ArrayList<CreditTierDetailView>();
        creditTierDetailView = new CreditTierDetailView();
//        RefRate refRate = refRateDAO.findById(proposeCreditDetailView.getStandardBase().getId());

           /* if(proposeCreditDetailView.getStandardBase().getValue().doubleValue() > proposeCreditDetailView.getSuggestBase().getValue().doubleValue()){
                creditTierDetailAdd.setFinalBase(proposeCreditDetailView.getStandardBase());
            }else{
                creditTierDetailAdd.setFinalBase(proposeCreditDetailView.getSuggestBase());
            }

            if(proposeCreditDetailView.getStandardPrice().doubleValue() > proposeCreditDetailView.getSuggestPrice().doubleValue())
            {
                creditTierDetailAdd.setFinalPriceRate(proposeCreditDetailView.getStandardPrice());
            }else{
                creditTierDetailAdd.setFinalPriceRate(proposeCreditDetailView.getSuggestPrice());
            }*/

        creditTierDetailView.setFinalBase(new RefRate());
        creditTierDetailView.setFinalPriceRate(BigDecimal.valueOf(2000));
        creditTierDetailView.setTenor(6);
        creditTierDetailView.setInstallment(BigDecimal.valueOf(200000));
        creditTierDetailView.setCanEdit(false);
        creditTierDetailViewList.add(creditTierDetailView);
        //****** tier test create ********//
//        proposeCreditDetailView.setCreditTierDetailViewList(creditTierDetailViewList);

    }

    public void onEditCreditInfo() {
        modeEdit = false;
        modeForButton = ModeForButton.EDIT;
        log.info("rowIndex :: {}", rowIndex);
        log.info("creditFacProposeView.creditInfoDetailViewList :: {}", creditFacProposeView.getProposeCreditDetailViewList());
        ProductProgram productProgram = proposeCreditDetailSelected.getProductProgram();
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(productProgram);
        CreditType creditType = creditTypeDAO.findById(proposeCreditDetailView.getCreditType().getId());

        if (rowIndex < creditFacProposeView.getProposeCreditDetailViewList().size()) {
            proposeCreditDetailView = new ProposeCreditDetailView();
            proposeCreditDetailView.setProductProgram(productProgram);
            proposeCreditDetailView.setCreditType(creditType);
            proposeCreditDetailView.setRequestType(proposeCreditDetailSelected.getRequestType());
            proposeCreditDetailView.setRefinance(proposeCreditDetailSelected.getRefinance());
            proposeCreditDetailView.setProductCode(proposeCreditDetailSelected.getProductCode());
            proposeCreditDetailView.setProjectCode(proposeCreditDetailSelected.getProjectCode());
            proposeCreditDetailView.setLimit(proposeCreditDetailSelected.getLimit());
            proposeCreditDetailView.setPCEPercent(proposeCreditDetailSelected.getPCEPercent());
            proposeCreditDetailView.setPCEAmount(proposeCreditDetailSelected.getPCEAmount());

            proposeCreditDetailView.setCreditTierDetailViewList(proposeCreditDetailSelected.getCreditTierDetailViewList());
        }
    }

    public void onSaveCreditInfo() {
        log.info("onSaveCreditInfo ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if ((proposeCreditDetailView.getProductProgram().getId() != 0) && (proposeCreditDetailView.getCreditType().getId() != 0)) {
            if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

                ProductProgram productProgram = productProgramDAO.findById(proposeCreditDetailView.getProductProgram().getId());
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

//                creditDetailAdd.setCreditTierDetailViewList(proposeCreditDetailView.getCreditTierDetailViewList());

                creditFacProposeView.getProposeCreditDetailViewList().add(creditDetailAdd);

            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.info("onEditRecord ::: mode : {}", modeForButton);
                ProductProgram productProgram = productProgramDAO.findById(proposeCreditDetailView.getProductProgram().getId());
                CreditType creditType = creditTypeDAO.findById(proposeCreditDetailView.getCreditType().getId());

                creditFacProposeView.getProposeCreditDetailViewList().get(rowIndex).setProductProgram(productProgram);
                creditFacProposeView.getProposeCreditDetailViewList().get(rowIndex).setCreditType(creditType);
                creditFacProposeView.getProposeCreditDetailViewList().get(rowIndex).setRequestType(proposeCreditDetailView.getRequestType());
                creditFacProposeView.getProposeCreditDetailViewList().get(rowIndex).setRefinance(proposeCreditDetailView.getRefinance());
                creditFacProposeView.getProposeCreditDetailViewList().get(rowIndex).setProductCode(proposeCreditDetailView.getProductCode());
                creditFacProposeView.getProposeCreditDetailViewList().get(rowIndex).setProjectCode(proposeCreditDetailView.getProjectCode());
                creditFacProposeView.getProposeCreditDetailViewList().get(rowIndex).setLimit(proposeCreditDetailView.getLimit());
                creditFacProposeView.getProposeCreditDetailViewList().get(rowIndex).setPCEPercent(proposeCreditDetailView.getPCEPercent());
                creditFacProposeView.getProposeCreditDetailViewList().get(rowIndex).setPCEAmount(proposeCreditDetailView.getPCEAmount());
                creditFacProposeView.getProposeCreditDetailViewList().get(rowIndex).setCreditTierDetailViewList(proposeCreditDetailView.getCreditTierDetailViewList());
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
        creditFacProposeView.getProposeCreditDetailViewList().remove(proposeCreditDetailSelected);
    }

    //  END Propose Credit Information  //

    //  Start Tier Dialog //
    public void onAddTierInfo() {
        log.info("onAddTierInfo ::: ");
        creditTierDetailView = new CreditTierDetailView();
        creditTierDetailView.setCanEdit(true);
        proposeCreditDetailView.getCreditTierDetailViewList().add(0,creditTierDetailView);
        modeForTier = ModeForButton.ADD;

    }

    public void onEditTierInfo() {
       log.info("onEditTierInfo ::: {}",rowTierIndex);
       modeForTier = ModeForButton.EDIT;
       if(rowTierIndex > proposeCreditDetailView.getCreditTierDetailViewList().size()){
           creditTierDetailView = new CreditTierDetailView();

           if(proposeCreditDetailView.getStandardBase().getValue().doubleValue() > proposeCreditDetailView.getSuggestBase().getValue().doubleValue()){
               creditTierDetailView.setFinalBase(proposeCreditDetailView.getStandardBase());
           }else{
               creditTierDetailView.setFinalBase(proposeCreditDetailView.getSuggestBase());
           }

           if(proposeCreditDetailView.getStandardPrice().doubleValue() > proposeCreditDetailView.getSuggestPrice().doubleValue())
           {
                creditTierDetailView.setFinalPriceRate(proposeCreditDetailView.getStandardPrice());
           }else{
               creditTierDetailView.setFinalPriceRate(proposeCreditDetailView.getSuggestPrice());
           }

           creditTierDetailView.setInstallment(tierSelected.getInstallment());
           creditTierDetailView.setTenor(tierSelected.getTenor());
       }
    }

    public void onSaveTierInfo() {
        log.info("onSaveTierInfo ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if (modeForTier != null && modeForTier.equals(ModeForButton.ADD)) {
            log.info("modeForTier ::: {}",modeForTier);
            CreditTierDetailView creditTierDetailAdd = new CreditTierDetailView();
            creditTierDetailAdd.setTenor(creditTierDetailView.getTenor());
            creditTierDetailAdd.setInstallment(creditTierDetailView.getInstallment());
            proposeCreditDetailView.getCreditTierDetailViewList().add(creditTierDetailAdd);

        } else if(modeForTier != null && modeForTier.equals(ModeForButton.EDIT)) {
            log.info("modeForTier ::: {}",modeForTier);
            proposeCreditDetailView.getCreditTierDetailViewList().get(rowTierIndex).setFinalBase(creditTierDetailView.getFinalBase());
            proposeCreditDetailView.getCreditTierDetailViewList().get(rowTierIndex).setFinalPriceRate(creditTierDetailView.getFinalPriceRate());
            proposeCreditDetailView.getCreditTierDetailViewList().get(rowTierIndex).setInstallment(creditTierDetailView.getInstallment());
            proposeCreditDetailView.getCreditTierDetailViewList().get(rowTierIndex).setTenor(creditTierDetailView.getTenor());

        } else {
            log.info("onSaveNcbRecord ::: Undefined modeForButton !!");
            complete = false;
        }

        complete = true;
        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    public void onDeleteProposeTierInfo() {
        log.info("onDeleteProposeTierInfo::");
        proposeCreditDetailView.getCreditTierDetailViewList().remove(tierSelected);
    }

    //  END Tier Dialog //

    public List<CreditTypeDetailView> findCreditFacility(){

        creditTypeDetailList = new ArrayList<CreditTypeDetailView>();  // find credit existing and propose in this workCase

        if(creditFacProposeView.getProposeCreditDetailViewList().size() > 0)
        {
            for(ProposeCreditDetailView proposeCreditDetailView : creditFacProposeView.getProposeCreditDetailViewList()) {
                creditTypeDetailView = new CreditTypeDetailView();
                creditTypeDetailView.setAccount("-");
                creditTypeDetailView.setRequestType(proposeCreditDetailView.getRequestType());
                creditTypeDetailView.setProductProgram(proposeCreditDetailView.getProductProgram().getName());
                creditTypeDetailView.setCreditFacility(proposeCreditDetailView.getCreditType().getName());
                creditTypeDetailView.setLimit(proposeCreditDetailView.getLimit());
                creditTypeDetailList.add(creditTypeDetailView);
            }

        }

        return creditTypeDetailList;
    }

    //  Start Propose Collateral Information  //
    public void onAddProposeCollInfo() {
        log.info("onAddProposeCollInfo ::: {}",creditFacProposeView.getProposeCreditDetailViewList().size());
        proposeCollateralInfoView = new ProposeCollateralInfoView();

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

    public void onDeleteCollateralOwnerUW() {

    }

    public void onAddMortgageType() {

    }

    public void onDeleteMortgageType() {

    }

    public void onAddRelatedWith() {

    }

    public void onDeleteRelatedWith() {

    }
    // end for sub collateral dialog

    //  END Propose Collateral Information  //

    // Start Add SUB Collateral
    public void onAddSubCollateral() {
        log.info("onAddSubCollateral and rowCollHeadIndex :: {}",rowCollHeadIndex);
        subCollateralDetailView = new SubCollateralDetailView();
        modeForSubColl = ModeForButton.ADD;

    }

    public void onEditSubCollateral() {
        log.info("rowSubIndex :: {}",rowSubIndex);
        subCollateralDetailView.setSubCollateralType(subCollateralDetailItem.getSubCollateralType());
        subCollateralDetailView.setTitleDeed(subCollateralDetailItem.getTitleDeed());
        subCollateralDetailView.setAddress(subCollateralDetailItem.getAddress());
        subCollateralDetailView.setLandOffice(subCollateralDetailItem.getLandOffice());
        subCollateralDetailView.setCollateralOwnerAAD(subCollateralDetailItem.getCollateralOwnerAAD());
        subCollateralDetailView.setAppraisalValue(subCollateralDetailItem.getAppraisalValue());
        subCollateralDetailView.setMortgageValue(subCollateralDetailItem.getMortgageValue());
    }

    public void onSaveSubCollateral() {
        log.info("onSaveSubCollateral ::: mode : {}", modeForSubColl);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if (modeForSubColl != null && modeForSubColl.equals(ModeForButton.ADD)) {
            log.info("modeForSubColl ::: {}",modeForSubColl);
            SubCollateralDetailView subCollAdd = new SubCollateralDetailView();
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(subCollateralDetailView.getSubCollateralType().getId());
            subCollAdd.setSubCollateralType(subCollateralType);
            subCollAdd.setTitleDeed(subCollateralDetailView.getTitleDeed());
            subCollAdd.setAddress(subCollateralDetailView.getAddress());
            subCollAdd.setLandOffice(subCollateralDetailView.getLandOffice());
            subCollAdd.setCollateralOwnerAAD(subCollateralDetailView.getCollateralOwnerAAD());
            subCollAdd.setAppraisalValue(subCollateralDetailView.getAppraisalValue());
            subCollAdd.setMortgageValue(subCollateralDetailView.getMortgageValue());
            proposeCollateralInfoView.getCollateralHeaderDetailViewList().get(rowCollHeadIndex).getSubCollateralDetailViewList().add(subCollAdd);

        } else if(modeForSubColl != null && modeForSubColl.equals(ModeForButton.EDIT)) {
            log.info("modeForSubColl ::: {}",modeForSubColl);
            SubCollateralType subCollateralType = subCollateralTypeDAO.findById(subCollateralDetailView.getSubCollateralType().getId());

            collateralHeaderDetailView.getSubCollateralDetailViewList().get(rowSubIndex).setSubCollateralType(subCollateralType);
            collateralHeaderDetailView.getSubCollateralDetailViewList().get(rowSubIndex).setTitleDeed(subCollateralDetailView.getTitleDeed());
            collateralHeaderDetailView.getSubCollateralDetailViewList().get(rowSubIndex).setAddress(subCollateralDetailView.getAddress());
            collateralHeaderDetailView.getSubCollateralDetailViewList().get(rowSubIndex).setLandOffice(subCollateralDetailView.getLandOffice());
            collateralHeaderDetailView.getSubCollateralDetailViewList().get(rowSubIndex).setCollateralOwnerAAD(subCollateralDetailView.getCollateralOwnerAAD());
            collateralHeaderDetailView.getSubCollateralDetailViewList().get(rowSubIndex).setAppraisalValue(subCollateralDetailView.getAppraisalValue());
            collateralHeaderDetailView.getSubCollateralDetailViewList().get(rowSubIndex).setMortgageValue(subCollateralDetailView.getMortgageValue());

        } else {
            log.info("onSaveSubCollateral ::: Undefined modeForButton !!");
            complete = false;
        }

        complete = true;
        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);
    }


    public void onDeleteSubCollateral() {
       log.info("onDeleteSubCollateral :: ");
        collateralHeaderDetailView.getSubCollateralDetailViewList().remove(subCollateralDetailItem);
    }
    // END Add SUB Collateral

    //  Start Guarantor //
    public void onAddGuarantorInfo() {
        proposeGuarantorDetailView = new ProposeGuarantorDetailView();
        modeForButton = ModeForButton.ADD;
        proposeGuarantorDetailView.setCreditTypeDetailViewList(findCreditFacility());

    }

    public void onEditGuarantorInfo() {
        log.info("onEditGuarantorInfo ::: {}", rowIndexGuarantor);
        modeForButton = ModeForButton.EDIT;

        if(proposeGuarantorDetailViewItem != null){
            proposeGuarantorDetailView.setGuarantorName(proposeGuarantorDetailViewItem.getGuarantorName());
            proposeGuarantorDetailView.setTcgLgNo(proposeGuarantorDetailViewItem.getTcgLgNo());
            proposeGuarantorDetailView.setCreditTypeDetailViewList(proposeGuarantorDetailViewItem.getCreditTypeDetailViewList());
        }
    }

    public void onSaveGuarantorInfoDlg() {
        log.info("onSaveGuarantorInfoDlg ::: mode : {}", modeForButton);
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();
        BigDecimal summary = BigDecimal.ZERO;

        if (proposeGuarantorDetailView.getGuarantorName() != null) {
            if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                log.info("modeForButton ::: {}",modeForButton);
                Customer guarantor = customerDAO.findById(proposeGuarantorDetailView.getGuarantorName().getId());
                ProposeGuarantorDetailView guarantorDetailAdd = new ProposeGuarantorDetailView();
                guarantorDetailAdd.setGuarantorName(guarantor);
                guarantorDetailAdd.setTcgLgNo(proposeGuarantorDetailView.getTcgLgNo());

                for(CreditTypeDetailView creditTypeDetailView : proposeGuarantorDetailView.getCreditTypeDetailViewList()) {
                    CreditTypeDetailView creditTypeDetailAdd = new CreditTypeDetailView();
                  log.info("creditTypeDetailView.isNoFlag()  :: {}",creditTypeDetailView.isNoFlag());
                    if(creditTypeDetailView.isNoFlag()){
                        creditTypeDetailAdd.setNoFlag(creditTypeDetailView.isNoFlag());
                        creditTypeDetailAdd.setNo(creditTypeDetailView.getNo());
                        creditTypeDetailAdd.setAccount("-");
                        creditTypeDetailAdd.setRequestType(creditTypeDetailView.getRequestType());
                        creditTypeDetailAdd.setProductProgram(creditTypeDetailView.getProductProgram());
                        creditTypeDetailAdd.setCreditFacility(creditTypeDetailView.getCreditFacility());
                        creditTypeDetailAdd.setLimit(creditTypeDetailView.getLimit());
                        creditTypeDetailAdd.setGuaranteeAmount(creditTypeDetailView.getGuaranteeAmount());
                        guarantorDetailAdd.getCreditTypeDetailViewList().add(creditTypeDetailAdd);
                        summary = summary.add(creditTypeDetailView.getGuaranteeAmount());
                    }
                    guarantorDetailAdd.setGuaranteeAmount(summary);
                }

                if(guarantorDetailAdd.getCreditTypeDetailViewList().size() > 0){
                    creditFacProposeView.getProposeGuarantorDetailViewList().add(guarantorDetailAdd);
                }else{
                   //dialog error
                }

            } else if (modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                log.info("modeForButton ::: {}",modeForButton);
                Customer guarantor = customerDAO.findById(proposeGuarantorDetailView.getGuarantorName().getId());
                creditFacProposeView.getProposeGuarantorDetailViewList().get(rowIndexGuarantor).setGuarantorName(guarantor);
                creditFacProposeView.getProposeGuarantorDetailViewList().get(rowIndexGuarantor).setTcgLgNo(proposeGuarantorDetailView.getTcgLgNo());

                if(proposeGuarantorDetailView.getCreditTypeDetailViewList() != null){
                    creditFacProposeView.getProposeGuarantorDetailViewList().get(rowIndexGuarantor).setCreditTypeDetailViewList(proposeGuarantorDetailView.getCreditTypeDetailViewList());
                }

                for(CreditTypeDetailView creditTypeDetailView : creditFacProposeView.getProposeGuarantorDetailViewList().get(rowIndexGuarantor).getCreditTypeDetailViewList()){

                    if(creditTypeDetailView.isNoFlag()){
                        summary = summary.add(creditTypeDetailView.getGuaranteeAmount());

                    }
                }
                creditFacProposeView.getProposeGuarantorDetailViewList().get(rowIndexGuarantor).setGuaranteeAmount(summary);


            } else {
                log.info("onSaveGuarantorInfoDlg ::: Undefined modeForButton !!");
                complete = false;
            }
        }

        complete = true;

        calculationSummaryGuarantor();
        log.info("  complete >>>>  :  {}", complete);
        context.addCallbackParam("functionComplete", complete);

    }

    public void onDeleteGuarantorInfo() {
        log.info("onDeleteGuarantorInfo ::: {}", proposeGuarantorDetailViewItem.getTcgLgNo());
        creditFacProposeView.getProposeGuarantorDetailViewList().remove(proposeGuarantorDetailViewItem);
        log.info("delete success");
        calculationSummaryGuarantor();
    }

    public void calculationSummaryGuarantor(){
        sumTotalGuarantor = BigDecimal.ZERO;
        for(int i = 0 ; i < creditFacProposeView.getProposeGuarantorDetailViewList().size() ; i++){
            sumTotalGuarantor = sumTotalGuarantor.add(creditFacProposeView.getProposeGuarantorDetailViewList().get(i).getGuaranteeAmount());
        }
        creditFacProposeView.setTotalGuaranteeAmount(sumTotalGuarantor);
    }
    //  END Guarantor //

    //Start Condition Information //
    public void onAddConditionInfo() {
        log.info("onAddConditionInfo ::: ");
        proposeConditionDetailView = new ProposeConditionDetailView();
        modeForButton = ModeForButton.ADD;
    }

    public void onSaveConditionInfoDlg() {
        log.info("onSaveConditionInfoDlg ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        if (modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {

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

    public List<PrdProgramToCreditType> getPrdProgramToCreditTypeList() {
        return prdProgramToCreditTypeList;
    }

    public void setPrdProgramToCreditTypeList(List<PrdProgramToCreditType> prdProgramToCreditTypeList) {
        this.prdProgramToCreditTypeList = prdProgramToCreditTypeList;
    }

    public List<PrdGroupToPrdProgram> getPrdGroupToPrdProgramList() {
        return prdGroupToPrdProgramList;
    }

    public void setPrdGroupToPrdProgramList(List<PrdGroupToPrdProgram> prdGroupToPrdProgramList) {
        this.prdGroupToPrdProgramList = prdGroupToPrdProgramList;
    }

    public CreditTierDetailView getTierSelected() {
        return tierSelected;
    }

    public void setTierSelected(CreditTierDetailView tierSelected) {
        this.tierSelected = tierSelected;
    }

    public List<RefRate> getRefRateList() {
        return refRateList;
    }

    public void setRefRateList(List<RefRate> refRateList) {
        this.refRateList = refRateList;
    }

    public List<RefRate> getRefRateDlgList() {
        return refRateDlgList;
    }

    public void setRefRateDlgList(List<RefRate> refRateDlgList) {
        this.refRateDlgList = refRateDlgList;
    }

    public CreditTypeDetailView getCreditTypeDetailView() {
        return creditTypeDetailView;
    }

    public void setCreditTypeDetailView(CreditTypeDetailView creditTypeDetailView) {
        this.creditTypeDetailView = creditTypeDetailView;
    }

    public int getRowIndexGuarantor() {
        return rowIndexGuarantor;
    }

    public void setRowIndexGuarantor(int rowIndexGuarantor) {
        this.rowIndexGuarantor = rowIndexGuarantor;
    }

    public int getRowTierIndex() {
        return rowTierIndex;
    }

    public void setRowTierIndex(int rowTierIndex) {
        this.rowTierIndex = rowTierIndex;
    }

    public CollateralHeaderDetailView getCollateralHeaderDetailView() {
        return collateralHeaderDetailView;
    }

    public void setCollateralHeaderDetailView(CollateralHeaderDetailView collateralHeaderDetailView) {
        this.collateralHeaderDetailView = collateralHeaderDetailView;
    }

    public int getRowSubIndex() {
        return rowSubIndex;
    }

    public void setRowSubIndex(int rowSubIndex) {
        this.rowSubIndex = rowSubIndex;
    }

    public SubCollateralDetailView getSubCollateralDetailItem() {
        return subCollateralDetailItem;
    }

    public void setSubCollateralDetailItem(SubCollateralDetailView subCollateralDetailItem) {
        this.subCollateralDetailItem = subCollateralDetailItem;
    }

    public CollateralHeaderDetailView getCollateralHeaderDetailItem() {
        return collateralHeaderDetailItem;
    }

    public void setCollateralHeaderDetailItem(CollateralHeaderDetailView collateralHeaderDetailItem) {
        this.collateralHeaderDetailItem = collateralHeaderDetailItem;
    }

    public int getRowCollHeadIndex() {
        return rowCollHeadIndex;
    }

    public void setRowCollHeadIndex(int rowCollHeadIndex) {
        this.rowCollHeadIndex = rowCollHeadIndex;
    }
}

