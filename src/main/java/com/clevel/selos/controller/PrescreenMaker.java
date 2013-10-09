package com.clevel.selos.controller;

import com.clevel.selos.busiensscontrol.PrescreenBusinessControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.PrescreenDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.*;
import com.clevel.selos.security.UserDetail;
import com.clevel.selos.service.PrescreenService;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.PrescreenTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "prescreenMaker")
public class PrescreenMaker implements Serializable {
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

    //*** Drop down List ***//
    private List<ProductGroup> productGroupList;
    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;

    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;

    private List<CollateralType> collateralTypeList;

    private List<DocumentType> documentTypeList;
    private List<CustomerEntity> customerEntityList;
    private List<BorrowerType> borrowerTypeList;
    private List<Relation> relationList;
    private List<Reference> referenceList;
    private List<Title> titleList;
    private List<Nationality> nationalityList;
    private List<MaritalStatus> maritalStatusList;
    private List<User> bdmCheckerList;

    //*** Result table List ***//
    private List<FacilityView> facilityViewList;
    private List<CustomerInfoView> customerInfoViewList;
    private List<CustomerInfoView> borrowerInfoViewList;
    private List<CustomerInfoView> guarantorInfoViewList;
    private List<CustomerInfoView> relatedInfoViewList;
    private List<BizInfoDetailView> bizInfoViewList;
    private List<CollateralView> proposeCollateralViewList;

    //*** Variable for view ***//
    //private ProductGroup selectProductGroup;
    private FacilityView facility;
    private FacilityView selectFacilityItem;

    private CustomerInfoView borrowerInfo;
    private CustomerInfoView spouseInfo;
    private CustomerInfoView selectCustomerInfoItem;

    private BizInfoDetailView selectBizInfoItem;
    private BizInfoDetailView bizInfoView;

    private CollateralView proposeCollateral;
    private CollateralView selectProposeCollateralItem;

    private List<PreScreenResponseView> preScreenResponseViewList;
    private List<PreScreenResponseView> preScreenResponseCustomerList;
    private List<PreScreenResponseView> preScreenResponseGroupList;

    private PrescreenView prescreenView;

    // *** Variable for Session *** //
    private User user;
    private long workCasePreScreenId;
    private long stepId;
    private String queueName;


    enum ModeForButton{ ADD, EDIT, DELETE }
    private ModeForButton modeForButton;
    private String messageHeader;
    private String message;
    private int rowIndex;

    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private CreditTypeDAO creditTypeDao;
    @Inject
    private ProductGroupDAO productGroupDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;
    @Inject
    private PrdGroupToPrdProgramDAO prdGroupToPrdProgramDAO;        // find product program
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;    // find credit type
    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;
    @Inject
    private DocumentTypeDAO documentTypeDAO;
    @Inject
    private BorrowerTypeDAO borrowerTypeDAO;
    @Inject
    private CustomerEntityDAO customerEntityDAO;
    @Inject
    private RelationDAO relationDAO;
    @Inject
    private ReferenceDAO referenceDAO;
    @Inject
    private TitleDAO titleDAO;
    @Inject
    private NationalityDAO nationalityDAO;
    @Inject
    private MaritalStatusDAO maritalStatusDAO;
    @Inject
    private PrescreenDAO prescreenDAO;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    private StepDAO stepDAO;
    @Inject
    private StatusDAO statusDAO;
    @Inject
    private ProvinceDAO provinceDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private PrescreenService prescreenService;
    @Inject
    private PrescreenTransform prescreenTransform;
    @Inject
    private PrescreenBusinessControl prescreenBusinessControl;

    public PrescreenMaker() {
    }

    public void preRender(){
        HttpSession session = FacesUtil.getSession(true);
        log.info("preRender ::: setSession ");

        if(session.getAttribute("workCasePreScreenId") != null){
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            String page = Util.getCurrentPage();
            boolean checkPage = false;

            if(stepId == 1001 && page.equals("prescreenInitial.jsf")){
                checkPage = true;
            } else if(stepId == 1003 && page.equals("prescreenMaker.jsf")){
                checkPage = true;
            }

            if(!checkPage){
                try{
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                    return;
                }catch (Exception ex){
                    log.info("Exception :: {}",ex);
                }
            }
        }else{
            //TODO return to inbox
            log.info("preRender ::: workCasePrescreenId is null.");
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
        log.info("onCreation");
        HttpSession session = FacesUtil.getSession(true);
        log.info("preRender ::: setSession ");

        if(session.getAttribute("workCasePreScreenId") != null){
            log.info("onCreation ::: getAttrubute workCasePreScreenId : {}", session.getAttribute("workCasePreScreenId"));
            log.info("onCreation ::: getAttrubute stepId : {}", session.getAttribute("stepId"));
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            queueName = session.getAttribute("queueName").toString();
            UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userId = userDetail.getUserName();
            user = userDAO.findById(userId);

            modeForButton = ModeForButton.ADD;

            onClearObjectList();
            onLoadSelectList();
            onClearObject();
        }


    }

    public void onClearObjectList(){
        // *** Clear Variable for result list *** //
        prescreenView = prescreenBusinessControl.getPreScreen(workCasePreScreenId);
        if(prescreenView == null){
            prescreenView = new PrescreenView();
            prescreenView.reset();
        }

        facilityViewList = prescreenBusinessControl.getPreScreenFacility(prescreenView);
        if (facilityViewList == null) {
            facilityViewList = new ArrayList<FacilityView>();
        }

        /*proposeCollateralViewList = prescreenBusinessControl.getProposeCollateral()*/
        if (proposeCollateralViewList == null) { proposeCollateralViewList = new ArrayList<CollateralView>(); }


        bizInfoViewList = prescreenBusinessControl.getBusinessInfo(workCasePreScreenId);
        if (bizInfoViewList == null) { bizInfoViewList = new ArrayList<BizInfoDetailView>(); }

        customerInfoViewList = prescreenBusinessControl.getCustomerListByWorkCasePreScreenId(workCasePreScreenId);
        if(customerInfoViewList != null){
            borrowerInfoViewList = prescreenBusinessControl.getBorrowerViewListByCustomerViewList(customerInfoViewList);
            guarantorInfoViewList = prescreenBusinessControl.getGuarantorViewListByCustomerViewList(customerInfoViewList);
            relatedInfoViewList = prescreenBusinessControl.getRelatedViewListByCustomerViewList(customerInfoViewList);
        } else {
            customerInfoViewList = new ArrayList<CustomerInfoView>();
            borrowerInfoViewList = new ArrayList<CustomerInfoView>();
            guarantorInfoViewList = new ArrayList<CustomerInfoView>();
            relatedInfoViewList = new ArrayList<CustomerInfoView>();
        }
    }

    public void onLoadSelectList() {
        log.info("onLoadSelectList :::");

        productGroupList = productGroupDAO.findAll();
        log.info("onLoadSelectList ::: productGroupList size : {}", productGroupList.size());

        if(prescreenView.getProductGroup() != null){
            getProductProgramList();
        }

        collateralTypeList = collateralTypeDAO.findAll();
        log.info("onLoadSelectList ::: collateralTypeList size : {}", collateralTypeList.size());

        businessGroupList = businessGroupDAO.findAll();
        log.info("onLoadSelectList ::: businessGroupList size : {}", businessGroupList.size());

        documentTypeList = documentTypeDAO.findAll();
        log.info("onLoadSelectList ::: documentTypeList size : {}", documentTypeList.size());

        borrowerTypeList = borrowerTypeDAO.findAll();
        log.info("onLoadSelectList ::: borrowerTypeList size : {}", borrowerTypeList.size());

        customerEntityList = customerEntityDAO.findAll();
        log.info("onLoadSelectList ::: borrowerTypeList size : {}", customerEntityList.size());

        relationList = relationDAO.findAll();
        log.info("onLoadSelectList ::: relationList size : {}", relationList.size());

        referenceList = referenceDAO.findAll();
        log.info("onLoadSelectList ::: referenceList size : {}", referenceList.size());

        titleList = titleDAO.findAll();
        log.info("onLoadSelectList ::: titleList size : {}", titleList.size());

        nationalityList = nationalityDAO.findAll();
        log.info("onLoadSelectList ::: nationalityList size : {}", nationalityList.size());

        maritalStatusList = maritalStatusDAO.findAll();
        log.info("onLoadSelectList ::: maritalStatusList size : {}", maritalStatusList.size());

        log.info("onLoadSelectList ::: user : {}", user);
        bdmCheckerList = userDAO.findBDMChecker(user);
        log.info("onLoadSelectList ::: bdmCheckerList size : {}", bdmCheckerList.size());
    }

    public void onClearObject(){
        // *** Clear Variable for Dialog *** //
        log.info("onClearObject :::");

        if(facility == null){ facility = new FacilityView(); }
        if(bizInfoView == null){ bizInfoView = new BizInfoDetailView(); }
        if(proposeCollateral == null){ proposeCollateral = new CollateralView(); }
        if(borrowerInfo == null){ borrowerInfo = new CustomerInfoView(); }
        if(spouseInfo == null) { spouseInfo = new CustomerInfoView(); }


        proposeCollateral.reset();
        facility.reset();
        bizInfoView.reset();
        borrowerInfo.reset();
        spouseInfo.reset();

    }

    // *** Function for PreScreen *** //
    public void onCheckPreScreen(){
        // *** Validate Data for Check PreScreen *** //
        boolean validate = validateCheckPrescreen(customerInfoViewList);
        if(validate){
            preScreenResponseViewList = prescreenBusinessControl.getPreScreenResultFromBRMS(customerInfoViewList);
            prescreenBusinessControl.savePreScreenResult(preScreenResponseViewList, workCasePreScreenId, 0, stepId, user);
            preScreenResponseCustomerList = prescreenBusinessControl.getPreScreenCustomerResult(preScreenResponseViewList);
            preScreenResponseGroupList = prescreenBusinessControl.getPreScreenGroupResult(preScreenResponseViewList);
        }else{
            // *** MessageBox show validation Failed. *** //
        }
    }

    public boolean validateCheckPrescreen(List<CustomerInfoView> vCustomerInfoViewList){
        boolean validate = false;

        return validate;
    }

    public void onCloseSale(){
        log.info("onCloseSale ::: queueName : {}", queueName);
        //TODO get nextStep
        String actionCode = "1008";
        prescreenBusinessControl.nextStepPreScreen(workCasePreScreenId, queueName, actionCode);

        messageHeader = "Information";
        message = "Close Sale Complete.";
        //RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
            return;
        } catch (Exception ex) {
            log.error("Error to redirect : {}", ex.getMessage());
        }

    }

    // *** Function For Facility *** //
    public void onAddFacility() {
        log.info("onAddFacility ::: ");
        log.info("onAddFacility ::: prescreenView.productGroup : {}", prescreenView.getProductGroup());

        //*** Reset form ***//
        log.info("onAddFacility ::: Reset Form");
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        facility = new FacilityView();
        facility.setProductProgram(new ProductProgram());
        facility.setCreditType(new CreditType());
        modeForButton = ModeForButton.ADD;
    }

    public void onEditFacility() {
        modeForButton = ModeForButton.EDIT;
        log.info("onEditFacility ::: selecteFacilityItem : {}", selectFacilityItem);

        facility = new FacilityView();
        ProductProgram productProgram = selectFacilityItem.getProductProgram();
        CreditType creditType = selectFacilityItem.getCreditType();
        BigDecimal requestAmount = selectFacilityItem.getRequestAmount();
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(productProgram);

        facility.setProductProgram(productProgram);
        facility.setCreditType(creditType);
        facility.setRequestAmount(requestAmount);
    }

    public void onSaveFacility() {
        log.info("onSaveFacility ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        log.info("onSaveFacility ::: prescreen.productgroup.getId() : {} ", prescreenView.getProductGroup().getId());
        log.info("onSaveFacility ::: facility.getProductProgram().getId() : {} ", facility.getProductProgram().getId());
        log.info("onSaveFacility ::: facility.getCreditType().getId() : {} ", facility.getCreditType().getId());
        log.info("onSaveFacility ::: facility.getRequestAmount : {}", facility.getRequestAmount());
        log.info("onSaveFacility ::: facilityViewList : {}", facilityViewList);

        if( facility.getProductProgram().getId() != 0 && facility.getCreditType().getId() != 0 && facility.getRequestAmount() != null ) {
            if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                ProductProgram productProgram = productProgramDAO.findById(facility.getProductProgram().getId());
                CreditType creditType = creditTypeDao.findById(facility.getCreditType().getId());

                FacilityView facilityItem = new FacilityView();
                facilityItem.setProductProgram(productProgram);
                facilityItem.setCreditType(creditType);
                facilityItem.setRequestAmount(facility.getRequestAmount());
                facilityViewList.add(facilityItem);
                log.info("onSaveFacility ::: modeForButton : {}, Completed.", modeForButton);
            } else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                ProductProgram productProgram = productProgramDAO.findById(facility.getProductProgram().getId());
                CreditType creditType = creditTypeDao.findById(facility.getCreditType().getId());
                facilityViewList.get(rowIndex).setProductProgram(productProgram);
                facilityViewList.get(rowIndex).setCreditType(creditType);
                facilityViewList.get(rowIndex).setRequestAmount(facility.getRequestAmount());
                log.info("onSaveFacility ::: modeForButton : {}, rowIndex : {}, Completed.", modeForButton, rowIndex);
            } else {
                log.info("onSaveFacility ::: Undefined modeForbutton !!");
            }
            complete = true;
        } else {
            log.info("onSaveFacility ::: validation failed.");
            complete = false;
        }
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteFacility() {
        log.info("onDeleteFacility ::: selectFacilityItem : {}");
        facilityViewList.remove(selectFacilityItem);
    }

    // *** Function For Customer *** //
    public void onAddCustomerInfo() {
        log.info("onAddCustomerInfo ::: reset form");
        // *** Reset Form *** //
        modeForButton = ModeForButton.ADD;

        borrowerInfo = new CustomerInfoView();
        spouseInfo = new CustomerInfoView();

        borrowerInfo.reset();
        spouseInfo.reset();
    }

    public void onEditCustomerInfo() {
        log.info("onEditCustomer ::: selectCustomerItem : {}", selectCustomerInfoItem);
        borrowerInfo = selectCustomerInfoItem;
        modeForButton = ModeForButton.EDIT;
    }

    public void onSaveCustomerInfo() {
        log.info("onSaveCustomerInfo ::: modeForButton : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;
        //** validate form **//
        if(customerInfoViewList == null){
            customerInfoViewList = new ArrayList<CustomerInfoView>();
        }
        if(borrowerInfoViewList == null){
            borrowerInfoViewList = new ArrayList<CustomerInfoView>();
        }
        if(guarantorInfoViewList == null){
            guarantorInfoViewList = new ArrayList<CustomerInfoView>();
        }
        if(relatedInfoViewList == null){
            relatedInfoViewList = new ArrayList<CustomerInfoView>();
        }

        //** TODO dynamic validation for ncb checking
        log.info("onSaveCustomerInfo ::: customerEntity : {}", borrowerInfo.getCustomerEntity());
        log.info("onSaveCustomerInfo ::: relation : {}", borrowerInfo.getRelation());

        if(borrowerInfo.getCustomerEntity().getId() != 0){
            borrowerInfo.setCustomerEntity(customerEntityDAO.findById(borrowerInfo.getCustomerEntity().getId()));
        }

        if(borrowerInfo.getTitleTh().getId() != 0){
            borrowerInfo.setTitleTh(titleDAO.findById(borrowerInfo.getTitleTh().getId()));
        }

        if(modeForButton.equals(ModeForButton.ADD)){
            if(borrowerInfo.getCustomerEntity().getId() != 0){
                if(borrowerInfo.getCustomerEntity().getId() == 1){ //Individual
                    log.info("onSaveCustomerInfo ::: Borrower - relation : {}", borrowerInfo.getRelation());
                    //--- Borrower ---
                    if(borrowerInfo.getRelation().getId() == 1){
                        //Borrower
                        borrowerInfoViewList.add(borrowerInfo);
                        customerInfoViewList.add(borrowerInfo);
                    }else if(borrowerInfo.getRelation().getId() == 2){
                        //Guarantor
                        guarantorInfoViewList.add(borrowerInfo);
                        customerInfoViewList.add(borrowerInfo);
                    }else if(borrowerInfo.getRelation().getId() == 3 || borrowerInfo.getRelation().getId() == 4){
                        //Relate Person
                        relatedInfoViewList.add(borrowerInfo);
                        customerInfoViewList.add(borrowerInfo);
                    }else{
                        customerInfoViewList.add(borrowerInfo);
                    }

                    //--- Spouse ---
                    log.info("onSaveCustomerInfo ::: SpouseInfo : {}", borrowerInfo.getSpouse());
                    if(borrowerInfo.getMaritalStatus().getId() != 1 && borrowerInfo.getMaritalStatus().getId() != 4 && borrowerInfo.getMaritalStatus().getId() != 5){
                        if(borrowerInfo.getSpouse().getRelation() != null && borrowerInfo.getSpouse().getRelation().getId() != 0){
                            CustomerInfoView spouseInfo = borrowerInfo.getSpouse();
                            log.info("onSaveCustomerInfo ::: Spouse - relation : {}", spouseInfo.getRelation());
                            if(spouseInfo.getRelation().getId() == 1) {
                                //Spouse - Borrower
                                borrowerInfoViewList.add(spouseInfo);
                            } else if(spouseInfo.getRelation().getId() == 2) {
                                //Spouse - Guarantor
                                guarantorInfoViewList.add(spouseInfo);
                            } else if(spouseInfo.getRelation().getId() == 3 || spouseInfo.getRelation().getId() == 4) {
                                //Spouse - Relate Person
                                relatedInfoViewList.add(spouseInfo);
                            }
                        }
                    }


                }else if(borrowerInfo.getCustomerEntity().getId() == 2){ //Juristic
                    //--- Borrower ---
                    if(borrowerInfo.getRelation().getId() == 1){
                        //Borrower
                        borrowerInfoViewList.add(borrowerInfo);
                        customerInfoViewList.add(borrowerInfo);
                    }else if(borrowerInfo.getRelation().getId() == 2){
                        //Guarantor
                        guarantorInfoViewList.add(borrowerInfo);
                        customerInfoViewList.add(borrowerInfo);
                    }else if(borrowerInfo.getRelation().getId() == 3 || borrowerInfo.getRelation().getId() == 4){
                        //Relate Person
                        relatedInfoViewList.add(borrowerInfo);
                        customerInfoViewList.add(borrowerInfo);
                    }else{
                        customerInfoViewList.add(borrowerInfo);
                    }
                }
                complete = true;
            }
        } else {

        }

        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteCustomerInfo() {

    }

    public void onSearchCustomerInfo() {
        log.info("onSearchCustomerInfo :::");
        log.info("onSearchCustomerInfo ::: borrowerInfo : {}", borrowerInfo);
        CustomerInfoResultView customerInfoResultView;
        messageHeader = "Please wait...";
        message = "Waiting for search customer from RM";
        try{
            customerInfoResultView = prescreenBusinessControl.getCustomerInfoFromRM(borrowerInfo, user);
            log.info("onSearchCustomerInfo ::: customerInfoResultView : {}", customerInfoResultView);
            if(customerInfoResultView.getActionResult().equals(ActionResult.SUCCEED)){
                borrowerInfo = customerInfoResultView.getCustomerInfoView();
                if(borrowerInfo != null && borrowerInfo.getSpouse() != null){
                    spouseInfo = borrowerInfo.getSpouse();
                }
            }else {
                messageHeader = customerInfoResultView.getActionResult().toString();
                message = customerInfoResultView.getReason();
                //TODO Show message box
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        }catch (Exception ex){
            messageHeader = "Failed to Search Customer.";
            message = ex.getMessage();
            //TODO Show message box
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

    }

    public void onChangeCustomerEntity(){
        log.info("onChangeCustomerEntity ::: Custoemr Entity : {}", borrowerInfo.getCustomerEntity().getId());
        titleList = titleDAO.getListByCustomerEntity(borrowerInfo.getCustomerEntity());
    }

    public void onChangeMaritalStatus(){
        log.info("onChangeMaritalStatus ::: Marriage Status : {}", borrowerInfo.getMaritalStatus().getId());
    }

    // *** Function For BusinessInfoView *** //
    public void onAddBusinessInfo() {
        log.info("onAddBusinessInfo ::: reset form");
        /*** Reset Form ***/
        modeForButton = ModeForButton.ADD;

        bizInfoView = new BizInfoDetailView();
        bizInfoView.setBizDesc(new BusinessDescription());
        bizInfoView.getBizDesc().setBusinessGroup(new BusinessGroup());
    }

    public void onEditBusinessInfo() {
        log.info("onEditBusinessInfo ::: selectBusinessInfo : {}", selectBizInfoItem);
        modeForButton = ModeForButton.EDIT;

        bizInfoView = new BizInfoDetailView();
        bizInfoView.setBizDesc(selectBizInfoItem.getBizDesc());
        bizInfoView.getBizDesc().setBusinessGroup(selectBizInfoItem.getBizDesc().getBusinessGroup());

        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(bizInfoView.getBizDesc().getBusinessGroup());
    }

    public void onSaveBusinessInfo() {
        log.info("onSaveBusinessInfo ::: modeForButton : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        /*** validate input ***/
        if(bizInfoView.getBizDesc().getId() != 0 && bizInfoView.getBizDesc().getBusinessGroup().getId() != 0){
            if(modeForButton.equals(ModeForButton.ADD)) {
                BizInfoDetailView businessInfo = new BizInfoDetailView();

                log.info("onSaveBusinessInformation ::: selectBusinessGroupID : {}", businessInfo.getBizDesc().getId());
                log.info("onSaveBusinessInformation ::: selectBusinessDescriptionID : {}", businessInfo.getBizDesc().getBusinessGroup().getId());

                BusinessGroup businessGroup = businessGroupDAO.findById(businessInfo.getBizDesc().getBusinessGroup().getId());
                BusinessDescription businessDesc = businessDescriptionDAO.findById(businessInfo.getBizDesc().getId());

                businessDesc.setBusinessGroup(businessGroup);

                businessInfo.setBizDesc(businessDesc);

                bizInfoViewList.add(businessInfo);
                complete = true;
            } else if(modeForButton.equals(ModeForButton.EDIT)) {
                log.info("onSaveBusinessInfo ::: rowIndex : {}", rowIndex);
                BusinessDescription businessDescription = businessDescriptionDAO.findById(bizInfoView.getBizDesc().getId());
                BusinessGroup businessGroup = businessGroupDAO.findById(bizInfoView.getBizDesc().getBusinessGroup().getId());

                businessDescription.setBusinessGroup(businessGroup);

                bizInfoViewList.get(rowIndex).setBizDesc(businessDescription);
                complete = true;
            } else {
                log.info("onSaveBusinessInfo ::: Undefined modeForbutton !!");
                complete = false;
            }
        } else {
            log.info("onSaveBusinessInfo ::: validation failed.");
            complete = false;
        }
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteBusinessInfo() {
        log.info("onDeleteBusinessInformation ::: selectBizInfoItem : {}", selectBizInfoItem);
        bizInfoViewList.remove(selectBizInfoItem);
    }

    // *** Function For ProposeCollateral ***//
    public void onAddProposeCollateral() {
        log.info("onAddProposeCollateral :::");
        //*** Reset form ***//
        log.info("onAddProposeCollateral ::: Reset Form");
        modeForButton = ModeForButton.ADD;
        proposeCollateral = new CollateralView();
        proposeCollateral.setCollateralType(new CollateralType());
    }

    public void onEditProposeCollateral(){
        modeForButton = ModeForButton.EDIT;
        log.info("onEditProposeCollateral ::: selectProposeCollateralItem : {}", selectProposeCollateralItem);

        proposeCollateral = new CollateralView();
        CollateralType collateralType = collateralTypeDAO.findById(selectProposeCollateralItem.getCollateralType().getId());

        proposeCollateral.setCollateralType(collateralType);
        proposeCollateral.setCollateralAmount(selectProposeCollateralItem.getCollateralAmount());
    }

    public void onSaveProposeCollateral(){
        log.info("onSaveProposeCollateral ::: modeForButton : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        log.info("onSaveProposeCollateral ::: proposeCollateral.getCollateralType.getId() : {} ", proposeCollateral.getCollateralType().getId());
        log.info("onSaveProposeCollateral ::: proposeCollateral.getCollateralAmount : {} ", proposeCollateral.getCollateralAmount());

        if(proposeCollateral.getCollateralType().getId() != 0 && proposeCollateral.getCollateralAmount() != null) {
            if(modeForButton.equals(ModeForButton.ADD)){
                CollateralView collateral = new CollateralView();
                CollateralType collateralType = collateralTypeDAO.findById(proposeCollateral.getCollateralType().getId());
                collateral.setCollateralType(collateralType);
                collateral.setCollateralAmount(proposeCollateral.getCollateralAmount());

                proposeCollateralViewList.add(collateral);
                log.info("onSaveProposeCollateral ::: modeForButton : {}, Completed.", modeForButton);
            } else if(modeForButton.equals(ModeForButton.EDIT)){
                CollateralType collateralType = collateralTypeDAO.findById(proposeCollateral.getCollateralType().getId());

                proposeCollateralViewList.get(rowIndex).setCollateralType(collateralType);
                proposeCollateralViewList.get(rowIndex).setCollateralAmount(proposeCollateral.getCollateralAmount());
                log.info("onSaveProposeCollateral ::: modeForButton : {}, Completed.", modeForButton);
            } else {
                log.info("onSaveProposeCollateral ::: Undefined modeForbutton !!");
            }
            complete = true;
        } else {
            complete = false;
            log.info("onSaveProposeCollateral ::: validation failed.");
        }
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteProposeCollateral() {
        log.info("onDeleteProposeCollateral ::: selectProposeCollateralItem : {}", selectProposeCollateralItem);
        proposeCollateralViewList.remove(selectProposeCollateralItem);

    }

    // *** Function for Prescreen Initial *** //
    public void onSavePrescreenInitial(){
        log.info("onSavePrescreenInitial ::: prescreenView : {}", prescreenView);
        log.info("onSavePrescreenInitial ::: facilityViewList : {}", facilityViewList);

        try{
            //TODO set Business Location
            prescreenView.setBusinessLocation(null);
            prescreenBusinessControl.savePreScreenInitial(prescreenView, facilityViewList, customerInfoViewList, workCasePreScreenId, user);
            //TODO show messageBox success
            messageHeader = "Save PreScreen Success.";
            message = "Save PreScreen data success.";
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            log.error("onSavePreScreenInitial ::: exception : {}", ex);
            //TODO show messageBox error
            messageHeader = "Save PreScreen Failed.";
            if(ex.getCause() != null){
                message = "Save PreScreen data failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save PreScreen data failed. Cause : " + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onAssignToChecker(){
        log.info("onAssignToChecker ::: bdmChecker : {}", prescreenView.getCheckerId());
        log.info("onAssignToChecker ::: queueName : {}", queueName);
        //TODO get nextStep
        String actionCode = "1001";
        String checkerId = prescreenView.getCheckerId();
        prescreenBusinessControl.assignChecker(workCasePreScreenId, queueName, checkerId, actionCode);
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
            return;
        } catch (Exception ex) {
            log.error("Error to redirect : {}", ex.getMessage());
        }

    }

    public void onCancelCA(){
        log.info("onCancelCA ::: queueName : {}", queueName);
        //TODO get nextStep
        String actionCode = "1003";
        prescreenBusinessControl.nextStepPreScreen(workCasePreScreenId, queueName, actionCode);

        messageHeader = "Information";
        message = "Cancel CA Complete.";
        //RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
            return;
        } catch (Exception ex) {
            log.error("Error to redirect : {}", ex.getMessage());
        }

    }

    // *** Function for Prescreen Maker ***//
    public void onSavePrescreen(){
        //*** validate forms ***//
        log.info("onSavePrescreen ::: prescreenView : {}", prescreenView);
        Prescreen prescreen = new Prescreen();
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(new Long(1));
        log.info("onSavePrescreen ::: sWorkcasePrescreen : {}", workCasePrescreen);
        Province businessLocation = provinceDAO.findById(10);
        log.info("onSavePrescreen ::: businessLocation : {}", businessLocation);
        User user = userDAO.findById("10001");
        log.info("onSavePrescreen ::: user : {}", user);
        prescreen.setWorkCasePrescreen(workCasePrescreen);
        prescreen.setBusinessLocation(businessLocation);
        prescreen.setCreateDate(new Date());
        prescreen.setExpectedSubmitDate(prescreenView.getExpectedSubmitDate());
        prescreen.setRefinance(prescreenView.isRefinance());
        prescreen.setTcg(prescreenView.isTcg());
        prescreen.setModifyBy(user);
        prescreen.setModifyDate(DateTime.now().toDate());
        prescreen.setCreateDate(DateTime.now().toDate());
        prescreenService.save(prescreen);

        /*Province province = provinceDAO.findById(11);
        province.setName("testtest");
        provinceDAO.persist(province);*/

    }


    // *** Event for DropDown *** //
    public void checkOnChangeProductGroup(){
        if(facilityViewList != null && facilityViewList.size() > 0){
            RequestContext.getCurrentInstance().execute("msgBoxFacilityDlg.show()");
        }else{
            onChangeProductGroup();
        }
    }

    public void onChangeProductGroup(){
        getProductProgramList();
        //*** Check if Facility added system must be remove all ***//
        log.info("onChangeProductGroup :::: facilityViewList.size :::::::::::" + facilityViewList.size());

        if (facilityViewList.size() > 0) {
            facilityViewList.removeAll(facilityViewList);
        }
    }

    public void getProductProgramList(){
        log.info("getProductProgramList ::: prescreenView.productgroup : {}", prescreenView.getProductGroup());
        ProductGroup productGroup = new ProductGroup();
        if(prescreenView.getProductGroup().getId() != 0){
            productGroup = productGroupDAO.findById(prescreenView.getProductGroup().getId());
        }

        //*** Get Product Program List from Product Group ***//
        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdProByPrdGroup(productGroup);
        if(prdGroupToPrdProgramList == null){
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        }
        log.info("getProductProgramList ::: prdGroupToPrdProgramList size : {}", prdGroupToPrdProgramList.size());
    }

    public void onChangeProductProgram(){
        ProductProgram productProgram = productProgramDAO.findById(facility.getProductProgram().getId());
        log.info("onChangeProductProgram :::: productProgram : {}", productProgram);

        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(productProgram);
        if(prdProgramToCreditTypeList == null){
            prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        }
        log.info("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " +prdProgramToCreditTypeList.size());
    }

    public void onChangeBusinessGroup() {
        log.info("onChangeBusinessGroup :::");
        log.info("onChangeBusinessGroup ::: businessGroup.getId() : {}", bizInfoView.getBizDesc().getBusinessGroup().getId());
        if(String.valueOf(bizInfoView.getBizDesc().getBusinessGroup().getId()) != null && bizInfoView.getBizDesc().getBusinessGroup().getId() != 0){
            BusinessGroup businessGroup = businessGroupDAO.findById(bizInfoView.getBizDesc().getBusinessGroup().getId());
            log.info("onChangeBusinessGroup :: businessGroup : {}", businessGroup);
            businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
            bizInfoView.setBizDesc(new BusinessDescription());
            bizInfoView.getBizDesc().setBusinessGroup(businessGroup);
        } else {
            businessDescriptionList = new ArrayList<BusinessDescription>();
            bizInfoView.setBizDesc(new BusinessDescription());
            bizInfoView.getBizDesc().setBusinessGroup(new BusinessGroup());
        }
        log.info("onChangeBusinessGroupName ::: size is : {}", businessDescriptionList.size());
        log.info("onChangeBusinessGroupName ::: businessGroup : {}", bizInfoView.getBizDesc().getBusinessGroup());
    }

    public void onChangeBusinessDesc(){
        log.info("onChangeBusinessDesc :::");
        log.info("onChangeBusinessDesc ::: businessDesc.getId() : {}", bizInfoView.getBizDesc().getId());
        if(String.valueOf(bizInfoView.getBizDesc().getId()) != null){
            BusinessDescription businessDescription = businessDescriptionDAO.findById(bizInfoView.getBizDesc().getId());
            bizInfoView.setBizDesc(businessDescription);
        }
        log.info("onChangeBusinessDesc ::: businessDesc : {}", bizInfoView.getBizDesc());

    }

    /*


    public void onClearDlg() {

        dCollateralAmount = null;
        dCollateralTypeName = "";
        modeForCollateral = "add";
    }

    public void onAddExistCollateral() {
        log.info("onAddExistCollateral {}");
        CollateralView collExist = null;

        log.info("dCollateralTypeName is ", existCollateralTypeName);
        log.info("dCollateralAmount is ", existCollateralAmount);
        collExist = new CollateralView();
        collExist.setCollateralTypeName(existCollateralTypeName);
        collExist.setCollateralAmount(existCollateralAmount);
        proposeCollateralViewList.add(collExist);

    }

    public void onSelectedExistCollateral(int row) {
        existCollateralAmount = proposeCollateralViewList.get(row).getCollateralAmount();
        existCollateralTypeName = proposeCollateralViewList.get(row).getCollateralTypeName();
        indexExistEdit = row;
        modeForExist = "edit";
    }

    public void onEditExistCollateral() {
        proposeCollateralViewList.get(indexExistEdit).setCollateralAmount(existCollateralAmount);
        proposeCollateralViewList.get(indexExistEdit).setCollateralTypeName(existCollateralTypeName);
        modeForExist = "add";
        existCollateralTypeName = "";
        existCollateralAmount = null;
    }

    public void onDeleteExistCollateral(int row) {
        proposeCollateralViewList.remove(row);
    }





    public void onChangeCreditType(){
        log.info("onChangeCreditType ::: selectCreditType.Id() >>> " + selectCreditType.getId());
//        CreditType creditType  = creditTypeDao.findById(selectCreditType.getId());
//        log.info("onChangeCreditType ::: selectCreditType.Name() >>> " + creditType.getName());
    }*/

    // open dialog
 /*   public void onSelectedFacility(int rowNumber) {
        modeForButton = "edit";
        rowIndex = rowNumber;
        log.info("onSelectedFacility :::  rowNumber  :: " + rowNumber);
        log.info("onSelectedFacility ::: facilityViewList.get(rowNumber).getFacilityName :: " + rowNumber + "  "
                + facilityViewList.get(rowNumber).getCreditType().getId());
        log.info("onSelectedFacility ::: facilityViewList.get(rowNumber).getProductProgramName :: " + rowNumber + "  "
                + facilityViewList.get(rowNumber).getProductProgram().getId());
        log.info("onSelectedFacility ::: facilityViewList.get(rowNumber).getRequestAmount :: " + rowNumber + "  "
                + facilityViewList.get(rowNumber).getRequestAmount());

        selectProductProgram = facilityViewList.get(rowNumber).getProductProgram();
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(selectProductProgram);
        selectCreditType = facilityViewList.get(rowNumber).getCreditType();
        facility.setProductProgram(selectProductProgram);
        facility.setCreditType(selectCreditType);
        facility.setRequestAmount(facilityViewList.get(rowNumber).getRequestAmount());


    }*/

    /*public void onEditFacility() {
        ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
        CreditType creditType         = creditTypeDao.findById(selectCreditType.getId());
        log.info("onEditFacility :::::: selectProductProgram ::: "+selectProductProgram.getName());
        log.info("onEditFacility :::::: selectCreditType ::: "+selectCreditType.getName());
        facilityViewList.get(rowIndex).setProductProgram(productProgram);
        facilityViewList.get(rowIndex).setCreditType(creditType);
        facilityViewList.get(rowIndex).setRequestAmount(facility.getRequestAmount());
    }*/

    public ModeForButton getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(ModeForButton modeForButton) {
        this.modeForButton = modeForButton;
    }

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<ProductGroup> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public List<PrdGroupToPrdProgram> getPrdGroupToPrdProgramList() {
        return prdGroupToPrdProgramList;
    }

    public void setPrdGroupToPrdProgramList(List<PrdGroupToPrdProgram> prdGroupToPrdProgramList) {
        this.prdGroupToPrdProgramList = prdGroupToPrdProgramList;
    }

    public List<PrdProgramToCreditType> getPrdProgramToCreditTypeList() {
        return prdProgramToCreditTypeList;
    }

    public void setPrdProgramToCreditTypeList(List<PrdProgramToCreditType> prdProgramToCreditTypeList) {
        this.prdProgramToCreditTypeList = prdProgramToCreditTypeList;
    }

    public List<BusinessGroup> getBusinessGroupList() {
        return businessGroupList;
    }

    public void setBusinessGroupList(List<BusinessGroup> businessGroupList) {
        this.businessGroupList = businessGroupList;
    }

    public List<BusinessDescription> getBusinessDescriptionList() {
        return businessDescriptionList;
    }

    public void setBusinessDescriptionList(List<BusinessDescription> businessDescriptionList) {
        this.businessDescriptionList = businessDescriptionList;
    }

    public List<DocumentType> getDocumentTypeList() {
        return documentTypeList;
    }

    public void setDocumentTypeList(List<DocumentType> documentTypeList) {
        this.documentTypeList = documentTypeList;
    }

    public List<CustomerEntity> getCustomerEntityList() {
        return customerEntityList;
    }

    public void setCustomerEntityList(List<CustomerEntity> customerEntityList) {
        this.customerEntityList = customerEntityList;
    }

    public List<BorrowerType> getBorrowerTypeList() {
        return borrowerTypeList;
    }

    public void setBorrowerTypeList(List<BorrowerType> borrowerTypeList) {
        this.borrowerTypeList = borrowerTypeList;
    }

    public List<Relation> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<Relation> relationList) {
        this.relationList = relationList;
    }

    public List<Reference> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<Reference> referenceList) {
        this.referenceList = referenceList;
    }

    public List<Title> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<Title> titleList) {
        this.titleList = titleList;
    }

    public List<Nationality> getNationalityList() {
        return nationalityList;
    }

    public void setNationalityList(List<Nationality> nationalityList) {
        this.nationalityList = nationalityList;
    }

    public List<MaritalStatus> getMaritalStatusList() {
        return maritalStatusList;
    }

    public void setMaritalStatusList(List<MaritalStatus> maritalStatusList) {
        this.maritalStatusList = maritalStatusList;
    }

    public List<FacilityView> getFacilityViewList() {
        return facilityViewList;
    }

    public void setFacilityViewList(List<FacilityView> facilityViewList) {
        this.facilityViewList = facilityViewList;
    }

    public List<BizInfoDetailView> getBizInfoDetailViewList() {
        return bizInfoViewList;
    }

    public void setBizInfoDetailViewList(List<BizInfoDetailView> bizInfoViewList) {
        this.bizInfoViewList = bizInfoViewList;
    }

    /*public ProductGroup getSelectProductGroup() {
        return selectProductGroup;
    }

    public void setSelectProductGroup(ProductGroup selectProductGroup) {
        this.selectProductGroup = selectProductGroup;
    }*/

    public FacilityView getFacility() {
        return facility;
    }

    public void setFacility(FacilityView facility) {
        this.facility = facility;
    }

    public FacilityView getSelectFacilityItem() {
        return selectFacilityItem;
    }

    public void setSelectFacilityItem(FacilityView selectFacilityItem) {
        this.selectFacilityItem = selectFacilityItem;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<CollateralView> getProposeCollateralViewList() {
        return proposeCollateralViewList;
    }

    public void setProposeCollateralViewList(List<CollateralView> proposeCollateralViewList) {
        this.proposeCollateralViewList = proposeCollateralViewList;
    }

    public CollateralView getProposeCollateral() {
        return proposeCollateral;
    }

    public void setProposeCollateral(CollateralView proposeCollateral) {
        this.proposeCollateral = proposeCollateral;
    }

    public List<CollateralType> getCollateralTypeList() {
        return collateralTypeList;
    }

    public void setCollateralTypeList(List<CollateralType> collateralTypeList) {
        this.collateralTypeList = collateralTypeList;
    }

    public CollateralView getSelectProposeCollateralItem() {
        return selectProposeCollateralItem;
    }

    public void setSelectProposeCollateralItem(CollateralView selectProposeCollateralItem) {
        this.selectProposeCollateralItem = selectProposeCollateralItem;
    }

    public List<CustomerInfoView> getCustomerInfoViewList() {
        return customerInfoViewList;
    }

    public void setCustomerInfoViewList(List<CustomerInfoView> customerInfoViewList) {
        this.customerInfoViewList = customerInfoViewList;
    }

    public CustomerInfoView getSpouseInfo() {
        return spouseInfo;
    }

    public void setSpouseInfo(CustomerInfoView spouseInfo) {
        this.spouseInfo = spouseInfo;
    }

    public CustomerInfoView getBorrowerInfo() {
        return borrowerInfo;
    }

    public void setBorrowerInfo(CustomerInfoView borrowerInfo) {
        this.borrowerInfo = borrowerInfo;
    }

    public CustomerInfoView getSelectCustomerInfoItem() {
        return selectCustomerInfoItem;
    }

    public void setSelectCustomerInfoItem(CustomerInfoView selectCustomerInfoItem) {
        this.selectCustomerInfoItem = selectCustomerInfoItem;
    }

    public PrescreenView getPrescreenView() {
        return prescreenView;
    }

    public void setPrescreenView(PrescreenView prescreenView) {
        this.prescreenView = prescreenView;
    }

    public List<CustomerInfoView> getBorrowerInfoViewList() {
        return borrowerInfoViewList;
    }

    public void setBorrowerInfoViewList(List<CustomerInfoView> borrowerInfoViewList) {
        this.borrowerInfoViewList = borrowerInfoViewList;
    }

    public List<CustomerInfoView> getGuarantorInfoViewList() {
        return guarantorInfoViewList;
    }

    public void setGuarantorInfoViewList(List<CustomerInfoView> guarantorInfoViewList) {
        this.guarantorInfoViewList = guarantorInfoViewList;
    }

    public List<CustomerInfoView> getRelatedInfoViewList() {
        return relatedInfoViewList;
    }

    public void setRelatedInfoViewList(List<CustomerInfoView> relatedInfoViewList) {
        this.relatedInfoViewList = relatedInfoViewList;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PreScreenResponseView> getPreScreenResponseViewList() {
        return preScreenResponseViewList;
    }

    public void setPreScreenResponseViewList(List<PreScreenResponseView> preScreenResponseViewList) {
        this.preScreenResponseViewList = preScreenResponseViewList;
    }

    public List<PreScreenResponseView> getPreScreenResponseCustomerList() {
        return preScreenResponseCustomerList;
    }

    public void setPreScreenResponseCustomerList(List<PreScreenResponseView> preScreenResponseCustomerList) {
        this.preScreenResponseCustomerList = preScreenResponseCustomerList;
    }

    public List<PreScreenResponseView> getPreScreenResponseGroupList() {
        return preScreenResponseGroupList;
    }

    public void setPreScreenResponseGroupList(List<PreScreenResponseView> preScreenResponseGroupList) {
        this.preScreenResponseGroupList = preScreenResponseGroupList;
    }

    public BizInfoDetailView getSelectBizInfoItem() {
        return selectBizInfoItem;
    }

    public void setSelectBizInfoItem(BizInfoDetailView selectBizInfoItem) {
        this.selectBizInfoItem = selectBizInfoItem;
    }

    public BizInfoDetailView getBizInfoDetailView() {
        return bizInfoView;
    }

    public void setBizInfoDetailView(BizInfoDetailView bizInfoView) {
        this.bizInfoView = bizInfoView;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getBdmCheckerList() {
        return bdmCheckerList;
    }

    public void setBdmCheckerList(List<User> bdmCheckerList) {
        this.bdmCheckerList = bdmCheckerList;
    }
}
