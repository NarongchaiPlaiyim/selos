package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.PrescreenBusinessControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.PrescreenDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.*;
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
    private List<Relation> relationList;
    private List<Reference> referenceList;
    private List<Title> titleList;
    private List<Nationality> nationalityList;
    private List<MaritalStatus> maritalStatusList;
    private List<User> bdmCheckerList;

    private List<Province> provinceList;
    private List<District> districtList;
    private List<SubDistrict> subDistrictList;

    private List<Bank> refinanceList;

    private List<ReferredExperience> referredExperienceList;

    private List<BorrowingType> borrowingTypeList;

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
    private Date currentDate;
    private int previousProductGroupId;


    enum ModeForButton{ ADD, EDIT, DELETE }
    private ModeForButton modeForButton;
    private String messageHeader;
    private String message;
    private int rowIndex;
    private boolean disableAssignButton;

    // ***Boolean CustomerDialog*** //
    //Individual
    private boolean isDocumentType;
    private boolean isCitizenId;
    private boolean isCustomerId;
    private boolean isServiceSegment;
    private boolean isRelation;
    private boolean isCollateralOwner;
    private boolean isReference;
    private boolean isPercentShare;
    private boolean isTitleTh;
    private boolean isFirstNameTh;
    private boolean isLastNameTh;
    private boolean isDateOfBirth;
    private boolean isAge;
    private boolean isNationality;
    private boolean isAddress;
    private boolean isPostalCode;
    private boolean isApproxIncome;
    private boolean isMaritalStatus;
    //Juristic
    private boolean isDateOfRegister;


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
    private DistrictDAO districtDAO;
    @Inject
    private SubDistrictDAO subDistrictDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private BankDAO bankDAO;
    @Inject
    private ReferredExperienceDAO referredExperienceDAO;
    @Inject
    private BorrowingTypeDAO borrowingTypeDAO;

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
                    if(stepId == 1001 && page.equals("prescreen.jsf")){
                        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                        ec.redirect(ec.getRequestContextPath() + "/site/prescreenInitial.jsf");
                        return;
                    }else if(stepId == 1003 && page.equals("prescreen.jsf")){
                        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                        ec.redirect(ec.getRequestContextPath() + "/site/prescreenMaker.jsf");
                        return;
                    }else if(stepId == 1002 && page.equals("prescreen.jsf")){
                        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                        ec.redirect(ec.getRequestContextPath() + "/site/prescreenChecker.jsf");
                        return;
                    }else{
                        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                        ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                        return;
                    }
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
        log.info("onCreation :::");
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCasePreScreenId") != null){
            log.info("onCreation ::: getAttrubute workCasePreScreenId : {}", session.getAttribute("workCasePreScreenId"));
            log.info("onCreation ::: getAttrubute stepId : {}", session.getAttribute("stepId"));
            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            queueName = session.getAttribute("queueName").toString();

            log.debug("onCreation ::: workCasePreScreenId : {}", workCasePreScreenId);
            log.debug("onCreation ::: stepId : {}", stepId);
            log.debug("onCreation ::: queueName : {}", queueName);
//            UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            String userId = userDetail.getUserName();
//            user = userDAO.findById(userId);
            user = (User)session.getAttribute("user");

            modeForButton = ModeForButton.ADD;

            onClearObjectList();
            onLoadSelectList();
            onClearObject();
            onCheckButton();
        }
    }

    public void onCheckButton(){
        if(borrowerInfoViewList != null && borrowerInfoViewList.size() > 0){
            disableAssignButton = false;
        }else{
            disableAssignButton = true;
        }
    }

    public void onClearObjectList(){
        // *** Clear Variable for result list *** //
        prescreenView = prescreenBusinessControl.getPreScreen(workCasePreScreenId);
        if(prescreenView == null){
            prescreenView = new PrescreenView();
            prescreenView.reset();
        } else {
            if(prescreenView.getProductGroup() != null){
                previousProductGroupId = prescreenView.getProductGroup().getId();
            } else {
                prescreenView.setProductGroup(new ProductGroup());
            }

            if(prescreenView.getBusinessLocation() == null){
                prescreenView.setBusinessLocation(new Province());
            }

            if(prescreenView.getRefinanceBank() == null){
                prescreenView.setRefinanceBank(new Bank());
            }

            if(prescreenView.getReferredExperience() == null){

            }
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

        if(stepId == 1001){
            bdmCheckerList = userDAO.findBDMChecker(user);
            log.info("onLoadSelectList ::: bdmCheckerList size : {}", bdmCheckerList.size());
        }

        if(stepId == 1003){
            businessGroupList = businessGroupDAO.findAll();
            log.info("onLoadSelectList ::: businessGroupList size : {}", businessGroupList.size());

            collateralTypeList = collateralTypeDAO.findAll();
            log.info("onLoadSelectList ::: collateralTypeList size : {}", collateralTypeList.size());

            refinanceList = bankDAO.getListRefinance();
            log.info("onLoadSelectList ::: refinanceList size : {}", refinanceList.size());

            referredExperienceList = referredExperienceDAO.findAll();
            log.info("onLoadSelectList ::: referredExperienceList size : {}", referredExperienceList.size());

            borrowingTypeList = borrowingTypeDAO.findAll();
            log.info("onLoadSelectList ::: borrowingTypeList size : {}", borrowingTypeList.size());
        }

        //*** List for Customer ***//
        documentTypeList = documentTypeDAO.findAll();
        log.info("onLoadSelectList ::: documentTypeList size : {}", documentTypeList.size());

        customerEntityList = customerEntityDAO.findAll();
        log.info("onLoadSelectList ::: borrowerTypeList size : {}", customerEntityList.size());

        relationList = relationDAO.findAll();
        log.info("onLoadSelectList ::: relationList size : {}", relationList.size());

        referenceList = referenceDAO.findAll();
        log.info("onLoadSelectList ::: referenceList size : {}", referenceList.size());

        maritalStatusList = maritalStatusDAO.findAll();
        log.info("onLoadSelectList ::: maritalStatusList size : {}", maritalStatusList.size());

        titleList = titleDAO.findAll();
        log.info("onLoadSelectList ::: titleList size : {}", titleList.size());

        nationalityList = nationalityDAO.findAll();
        log.info("onLoadSelectList ::: nationalityList size : {}", nationalityList.size());

        provinceList = provinceDAO.findAll();
        log.info("onLoadSelectList ::: provinceList size : {}", provinceList.size());

        districtList = districtDAO.findAll();
        log.info("onLoadSelectList ::: districtList size : {}", districtList.size());

        subDistrictList = subDistrictDAO.findAll();
        log.info("onLoadSelectList ::: subDistrictList size : {}", subDistrictList.size());
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

        if(prescreenView.getProductGroup() != null){
            //*** Reset form ***//
            log.info("onAddFacility ::: Reset Form");
            prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
            facility = new FacilityView();
            facility.setProductProgram(new ProductProgram());
            facility.setCreditType(new CreditType());
            modeForButton = ModeForButton.ADD;
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('facilityDlg').show()");
        }

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

        isDocumentType = false;
        isCitizenId = true;
        isCustomerId = true;
        isServiceSegment = true;
        isRelation = true;
        isCollateralOwner = true;
        isReference = true;
        isPercentShare = true;
        isTitleTh = true;
        isFirstNameTh = true;
        isLastNameTh = true;
        isDateOfBirth = true;
        isAge = true;
        isNationality = true;
        isAddress = true;
        isPostalCode = true;
        isApproxIncome = true;
        isMaritalStatus= true;
        isDateOfRegister = true;
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
        onCheckButton();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteCustomerInfo() {

    }

    public void onChangeDate(String borrowerType){
        log.debug("onChangeDate :::");
        if(borrowerType.equalsIgnoreCase("borrower")){
            log.info("onChangeDate : borrowerInfo.dateOfBirth : {}", borrowerInfo.getDateOfBirth());
            int age = Util.calAge(borrowerInfo.getDateOfBirth());
            borrowerInfo.setAge(age);
        } else if(borrowerType.equalsIgnoreCase("spouse")){
            log.info("onChangeDate : spouseInfo.dateOfBirth : {}", spouseInfo.getDateOfBirth());
            int age = Util.calAge(spouseInfo.getDateOfBirth());
            spouseInfo.setAge(age);
        } else if(borrowerType.equalsIgnoreCase("juristic")){
            log.info("onChangeDate : juristicInfo.dateOfRegister : {}", borrowerInfo.getDateOfRegister());
            int age = Util.calAge(borrowerInfo.getDateOfRegister());
            borrowerInfo.setAge(age);
        }

    }

    private void enableCustomerForm(boolean enable){
        isCitizenId = enable;
        isCustomerId = enable;
        isServiceSegment = !enable;
        isRelation = !enable;
        isCollateralOwner = !enable;
        isReference = !enable;
        isPercentShare = !enable;
        isTitleTh = !enable;
        isFirstNameTh = !enable;
        isLastNameTh = !enable;
        isDateOfBirth = !enable;
        isAge = enable;
        isNationality = !enable;
        isAddress = !enable;
        isPostalCode = !enable;
        isApproxIncome = !enable;
        isMaritalStatus= !enable;
        isDateOfRegister = !enable;
    }

    public void onSearchCustomerInfo() {
        log.info("onSearchCustomerInfo :::");
        log.info("onSearchCustomerInfo ::: borrowerInfo : {}", borrowerInfo);
        CustomerInfoResultView customerInfoResultView = new CustomerInfoResultView();
        messageHeader = "Please wait...";
        message = "Waiting for search customer from RM";
        try{
            //customerInfoResultView = prescreenBusinessControl.getCustomerInfoFromRM(borrowerInfo, user);
            customerInfoResultView.setActionResult(ActionResult.SUCCEED);
            customerInfoResultView.setCustomerInfoView(new CustomerInfoView());
            log.info("onSearchCustomerInfo ::: customerInfoResultView : {}", customerInfoResultView);
            if(customerInfoResultView.getActionResult().equals(ActionResult.SUCCEED)){
                log.info("onSearchCustomerInfo ActionResult.SUCCEED");
                if(customerInfoResultView.getCustomerInfoView() != null && customerInfoResultView.getCustomerInfoView().getSpouse() != null){
                    borrowerInfo = customerInfoResultView.getCustomerInfoView();
                    if(borrowerInfo.getMaritalStatus().getId() == 2){
                        spouseInfo = borrowerInfo.getSpouse();
                    }
                    enableCustomerForm(true);
                    isDocumentType = false;
                }else{
                    log.info("else borrowerInfo = null");
                    if(borrowerInfo.getSearchBy() == 2){
                        isDocumentType = true;
                    }else{
                        isDocumentType = false;
                    }
                    enableCustomerForm(false);
                    CustomerEntity customerEntity = new CustomerEntity();
                    customerEntity.setId(0);
                    borrowerInfo.setCustomerEntity(customerEntity);
                    messageHeader = customerInfoResultView.getActionResult().toString();
                    message = "Search Customer not found.";
                    RequestContext.getCurrentInstance().execute("PF('msgBoxSystemMessageDlg').show()");
                }

            }else {
                messageHeader = customerInfoResultView.getActionResult().toString();
                message = customerInfoResultView.getReason();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        }catch (Exception ex){
            log.info("onSearchCustomerInfo Exception");
            messageHeader = "Failed to Search Customer.";
            message = ex.getMessage();
            //TODO Show message box
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

    }

    public void onChangeCustomerEntity(){

        log.info("onChangeCustomerEntity ::: Custoemr Entity : {}", borrowerInfo.getCustomerEntity().getId());
        titleList = titleDAO.getListByCustomerEntity(borrowerInfo.getCustomerEntity());
        log.info("onChangeCustomerEntity ::{}", borrowerInfo);
        if ( borrowerInfo.getCustomerEntity().getId() == 1){
            isDocumentType = false;
        }else{
            isDocumentType = true;
        }
        isCitizenId = false;
        isCustomerId = false;
        isServiceSegment = false;
        isRelation = false;
        isCollateralOwner = false;
        isReference = false;
        isPercentShare = false;
        isTitleTh = false;
        isFirstNameTh = false;
        isLastNameTh = false;
        isDateOfBirth = false;
        isAge = false;
        isNationality = false;
        isAddress = false;
        isPostalCode = false;
        isApproxIncome = false;
        isMaritalStatus= false;
        isDateOfRegister = false;
    }

    public void onChangeMaritalStatus(){
        log.info("onChangeMaritalStatus ::: Marriage Status : {}", borrowerInfo.getMaritalStatus().getId());
    }

    public void onDisableDocType(){
        log.info("onDisableDocType ::: {}", borrowerInfo.getSearchBy());
        if(borrowerInfo.getSearchBy() == 2){
            isDocumentType = true;
        }else{
            isDocumentType = false;
        }
    }

    /*// *** Calculate Age And Year In Business ***//*/
    public void onCalAge(String type){
        log.info("onCalAge ::: DateOfBirth:{} ", borrowerInfo.getDateOfBirth());
        int age = 0;
        if(type.trim().toUpperCase().equals("AGE")){
            if(borrowerInfo.getDateOfBirth() != null){
                age = Util.calAge(borrowerInfo.getDateOfBirth());
                borrowerInfo.setAge(age);
            }else if(type.trim().toUpperCase().equals("SPOUSEAGE")){
                age = Util.calAge(borrowerInfo.getSpouse().getDateOfBirth());
                spouseInfo.setAge(age);
                borrowerInfo.setSpouse(spouseInfo);
            }else if(type.trim().toUpperCase().equals("DATEOFREGISTER")){
                age = Util.calAge(borrowerInfo.getDateOfRegister());
                borrowerInfo.setAge(age);
            }
        }
        log.info("onCalAge ::: DateOfBirth:{}", age);
    }*/

        // *** Function For BusinessInfoView *** //
    public void onAddBusinessInfo() {
        log.info("onAddBusinessInfo ::: reset form");
        /*** Reset Form ***/
        modeForButton = ModeForButton.ADD;

        bizInfoView = new BizInfoDetailView();
        bizInfoView.reset();
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
        if(bizInfoView.getBizDesc().getId() != 0 && bizInfoView.getBizGroup().getId() != 0){
            if(modeForButton.equals(ModeForButton.ADD)) {
                BizInfoDetailView bizInfoDetailView = new BizInfoDetailView();
                log.info("onSaveBusinessInformation ::: selectBusinessDescriptionID : {}", bizInfoView.getBizDesc().getId());
                log.info("onSaveBusinessInformation ::: selectBusinessGroupID : {}", bizInfoView.getBizGroup().getId());

                BusinessGroup businessGroup = businessGroupDAO.findById(bizInfoView.getBizGroup().getId());
                BusinessDescription businessDesc = businessDescriptionDAO.findById(bizInfoView.getBizDesc().getId());

                bizInfoDetailView.setBizDesc(businessDesc);
                bizInfoDetailView.setBizGroup(businessGroup);

                bizInfoViewList.add(bizInfoDetailView);
                complete = true;
            } else if(modeForButton.equals(ModeForButton.EDIT)) {
                log.info("onSaveBusinessInfo ::: rowIndex : {}", rowIndex);
                BusinessDescription businessDescription = businessDescriptionDAO.findById(bizInfoView.getBizDesc().getId());
                BusinessGroup businessGroup = businessGroupDAO.findById(bizInfoView.getBizGroup().getId());

                businessDescription.setBusinessGroup(businessGroup);

                bizInfoViewList.get(rowIndex).setBizDesc(businessDescription);
                complete = true;
            }
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

        log.debug("onSavePrescreen ::: prescreenView : {}", prescreenView);
        try{
            //prescreenBusinessControl.savePreScreenInitial(prescreenView, facilityViewList, customerInfoViewList, workCasePreScreenId, user);
            prescreenBusinessControl.savePreScreen(prescreenView, facilityViewList, customerInfoViewList, bizInfoViewList, workCasePreScreenId, user);
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

        /*log.info("onSavePrescreen ::: prescreenView : {}", prescreenView);
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
        prescreenService.save(prescreen);*/

        /*Province province = provinceDAO.findById(11);
        province.setName("testtest");
        provinceDAO.persist(province);*/

    }


    // *** Event for DropDown *** //
    public void checkOnChangeProductGroup(){
        log.info("checkOnChangeProductGroup ::: productGroup : {}", prescreenView.getProductGroup().getId());
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
        if(Integer.toString(prescreenView.getProductGroup().getId()) != null){
            previousProductGroupId = prescreenView.getProductGroup().getId();
        }else{
            previousProductGroupId = 0;
        }
        if (facilityViewList.size() > 0) {
            facilityViewList.removeAll(facilityViewList);
        }
    }

    public void onCancelChangeProductGroup(){
        log.info("onCancelChangeProductGroup ::: previousValue : {}", previousProductGroupId);
        prescreenView.getProductGroup().setId(previousProductGroupId);
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
        log.info("onChangeBusinessGroup ::: businessGroup.getId() : {}", bizInfoView.getBizGroup().getId());
        if(String.valueOf(bizInfoView.getBizGroup().getId()) != null && bizInfoView.getBizGroup().getId() != 0){
            BusinessGroup businessGroup = businessGroupDAO.findById(bizInfoView.getBizGroup().getId());
            log.info("onChangeBusinessGroup :: businessGroup : {}", businessGroup);
            businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
            bizInfoView.setBizDesc(new BusinessDescription());
            bizInfoView.setBizGroup(businessGroup);
        } else {
            businessDescriptionList = new ArrayList<BusinessDescription>();
            bizInfoView.setBizDesc(new BusinessDescription());
            bizInfoView.setBizGroup(new BusinessGroup());
        }
        log.info("onChangeBusinessGroupName ::: size is : {}", businessDescriptionList.size());
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
    // ***Boolean CustomerDialog***
    public boolean isCitizenId() {
        return isCitizenId;
    }

    public void setCitizenId(boolean citizenId) {
        isCitizenId = citizenId;
    }

    public boolean isCustomerId() {
        return isCustomerId;
    }

    public void setCustomerId(boolean customerId) {
        isCustomerId = customerId;
    }

    public boolean isServiceSegment() {
        return isServiceSegment;
    }

    public void setServiceSegment(boolean serviceSegment) {
        isServiceSegment = serviceSegment;
    }

    public boolean isRelation() {
        return isRelation;
    }

    public void setRelation(boolean relation) {
        isRelation = relation;
    }

    public boolean isCollateralOwner() {
        return isCollateralOwner;
    }

    public void setCollateralOwner(boolean collateralOwner) {
        isCollateralOwner = collateralOwner;
    }

    public boolean isReference() {
        return isReference;
    }

    public void setReference(boolean reference) {
        isReference = reference;
    }

    public boolean isPercentShare() {
        return isPercentShare;
    }

    public void setPercentShare(boolean percentShare) {
        isPercentShare = percentShare;
    }

    public boolean isTitleTh() {
        return isTitleTh;
    }

    public void setTitleTh(boolean titleTh) {
        isTitleTh = titleTh;
    }

    public boolean isFirstNameTh() {
        return isFirstNameTh;
    }

    public void setFirstNameTh(boolean firstNameTh) {
        isFirstNameTh = firstNameTh;
    }

    public boolean isLastNameTh() {
        return isLastNameTh;
    }

    public void setLastNameTh(boolean lastNameTh) {
        isLastNameTh = lastNameTh;
    }

    public boolean isDateOfBirth() {
        return isDateOfBirth;
    }

    public void setDateOfBirth(boolean dateOfBirth) {
        isDateOfBirth = dateOfBirth;
    }

    public boolean isAge() {
        return isAge;
    }

    public void setAge(boolean age) {
        isAge = age;
    }

    public boolean isNationality() {
        return isNationality;
    }

    public void setNationality(boolean nationality) {
        isNationality = nationality;
    }

    public boolean isAddress() {
        return isAddress;
    }

    public void setAddress(boolean address) {
        isAddress = address;
    }

    public boolean isPostalCode() {
        return isPostalCode;
    }

    public void setPostalCode(boolean postalCode) {
        isPostalCode = postalCode;
    }

    public boolean isApproxIncome() {
        return isApproxIncome;
    }

    public void setApproxIncome(boolean approxIncome) {
        isApproxIncome = approxIncome;
    }

    public boolean isMaritalStatus() {
        return isMaritalStatus;
    }

    public void setMaritalStatus(boolean maritalStatus) {
        isMaritalStatus = maritalStatus;
    }

    public boolean isDateOfRegister() {
        return isDateOfRegister;
    }

    public void setDateOfRegister(boolean dateOfRegister) {
        isDateOfRegister = dateOfRegister;
    }

    public boolean isDocumentType() {
        return isDocumentType;
    }

    public void setDocumentType(boolean documentType) {
        isDocumentType = documentType;
    }

    public boolean isDisableAssignButton() {
        return disableAssignButton;
    }

    public void setDisableAssignButton(boolean disableAssignButton) {
        this.disableAssignButton = disableAssignButton;
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public int getPreviousProductGroupId() {
        return previousProductGroupId;
    }

    public void setPreviousProductGroupId(int previousProductGroupId) {
        this.previousProductGroupId = previousProductGroupId;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public List<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
    }

    public List<SubDistrict> getSubDistrictList() {
        return subDistrictList;
    }

    public void setSubDistrictList(List<SubDistrict> subDistrictList) {
        this.subDistrictList = subDistrictList;
    }

    public List<Bank> getRefinanceList() {
        return refinanceList;
    }

    public void setRefinanceList(List<Bank> refinanceList) {
        this.refinanceList = refinanceList;
    }

    public List<ReferredExperience> getReferredExperienceList() {
        return referredExperienceList;
    }

    public void setReferredExperienceList(List<ReferredExperience> referredExperienceList) {
        this.referredExperienceList = referredExperienceList;
    }

    public List<BorrowingType> getBorrowingTypeList() {
        return borrowingTypeList;
    }

    public void setBorrowingTypeList(List<BorrowingType> borrowingTypeList) {
        this.borrowingTypeList = borrowingTypeList;
    }
}
