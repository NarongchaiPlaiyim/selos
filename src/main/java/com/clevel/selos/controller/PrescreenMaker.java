package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.InboxControl;
import com.clevel.selos.businesscontrol.PrescreenBusinessControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.IndividualDAO;
import com.clevel.selos.dao.working.PrescreenDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.PrescreenTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import com.rits.cloning.Cloner;
import com.sun.istack.internal.Nullable;
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

    //*** Drop down List ***//
    private List<ProductGroup> productGroupList;
    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;

    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;

    private List<PotentialCollateral> potentialCollateralList;

    private List<DocumentType> documentTypeList;
    private List<DocumentType> spouseDocumentTypeList;
    private List<CustomerEntity> customerEntityList;
    private List<Relation> relationList;
    private List<Reference> referenceList;
    private List<Relation> spouseRelationList;
    private List<Reference> spouseReferenceList;
    private List<Title> titleList;
    private List<Nationality> nationalityList;
    private List<MaritalStatus> maritalStatusList;
    private List<User> bdmCheckerList;

    private List<Province> provinceList;
    private List<District> districtList;
    private List<SubDistrict> subDistrictList;

    private List<Province> provinceSpouseList;
    private List<District> districtSpouseList;
    private List<SubDistrict> subDistrictSpouseList;

    private List<Province> businessLocationList;

    private List<Country> countryOfRegisterList;

    private List<Bank> refinanceList;

    private List<ReferredExperience> referredExperienceList;

    private List<BorrowingType> borrowingTypeList;

    //*** Result table List ***//
    private List<FacilityView> facilityViewList;
    private List<CustomerInfoView> customerInfoViewList;
    private List<CustomerInfoView> borrowerInfoViewList;
    private List<CustomerInfoView> guarantorInfoViewList;
    private List<CustomerInfoView> deleteCustomerInfoViewList;
    private List<CustomerInfoView> relatedInfoViewList;
    private List<BizInfoDetailView> bizInfoViewList;
    private List<PrescreenCollateralView> proposePrescreenCollateralViewList;

    //*** Variable for view ***//
    //private ProductGroup selectProductGroup;
    private FacilityView facility;
    private FacilityView selectFacilityItem;

    private CustomerInfoView borrowerInfo;
    //private CustomerInfoView spouseInfo;
    private CustomerInfoView selectCustomerInfoItem;

    private BizInfoDetailView selectBizInfoItem;
    private BizInfoDetailView bizInfoView;

    private PrescreenCollateralView proposeCollateral;
    private PrescreenCollateralView selectProposeCollateralItem;

    private List<PreScreenResponseView> preScreenResponseViewList;
    private List<PreScreenResponseView> preScreenResponseCustomerList;
    private List<PreScreenResponseView> preScreenResponseGroupList;

    private PrescreenView prescreenView;

    private Relation borrowerRelation;
    private Relation spouseRelation;

    private Reference borrowerReference;
    private Reference spouseReference;

    // *** Variable for Session *** //
    private User user;
    private long workCasePreScreenId;
    private long stepId;
    private String queueName;
    private Date currentDate;
    private String currentDateDDMMYY;
    private int previousProductGroupId;
    private int caseBorrowerTypeId;
    private CustomerEntity caseBorrowerType;
    private CustomerEntity customerEntity;


    enum ModeForButton{ ADD, EDIT, DELETE }
    enum ListCustomerName {BORROWER, GUARANTOR, RELATED, CUSTOMER}
    private ModeForButton modeForButton;
    private String messageHeader;
    private String message;
    private int rowIndex;
    private boolean disableAssignButton;
    private RadioValue radioValue;
    private int customerModifyFlag;

    // ***Boolean CustomerDialog*** //
    private boolean enableCustomerForm;
    private boolean enableDocumentType;
    private boolean enableTMBCustomerId;
    private boolean enableCitizenId;
    private boolean enableCustomerEntity;
    private boolean enableSearchForm;

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
    private PotentialCollateralDAO potentialCollateralDAO;
    @Inject
    private IndividualDAO individualDAO;
    @Inject
    private CountryDAO countryDAO;

    @Inject
    private PrescreenTransform prescreenTransform;
    @Inject
    private PrescreenBusinessControl prescreenBusinessControl;
    @Inject
    private InboxControl inboxControl;
    @Inject
    private CustomerInfoControl customerInfoControl;

    public PrescreenMaker() {
    }

    public void preRender(){
        HttpSession session = FacesUtil.getSession(true);
        log.debug("preRender ::: setSession ");

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
                    if(stepId == StepValue.PRESCREEN_INITIAL.value() && page.equals("prescreen.jsf")){
                        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                        ec.redirect(ec.getRequestContextPath() + "/site/prescreenInitial.jsf");
                        return;
                    }else if(stepId == StepValue.PRESCREEN_MAKER.value() && page.equals("prescreen.jsf")){
                        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                        ec.redirect(ec.getRequestContextPath() + "/site/prescreenMaker.jsf");
                        return;
                    }else if(stepId == StepValue.PRESCREEN_CHECKER.value() && page.equals("prescreen.jsf")){
                        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                        ec.redirect(ec.getRequestContextPath() + "/site/prescreenChecker.jsf");
                        return;
                    }else{
                        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                        ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                        return;
                    }
                }catch (Exception ex){
                    log.debug("Exception :: {}",ex);
                }
            }
        }else{
            //TODO return to inbox
            log.debug("preRender ::: workCasePrescreenId is null.");
            try{
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/inbox.jsf");
                return;
            }catch (Exception ex){
                log.debug("Exception :: {}",ex);
            }
        }
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation :::");
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCasePreScreenId") != null){
            log.debug("onCreation ::: getAttrubute workCasePreScreenId : {}", session.getAttribute("workCasePreScreenId"));
            log.debug("onCreation ::: getAttrubute stepId : {}", session.getAttribute("stepId"));

            workCasePreScreenId = Long.parseLong(session.getAttribute("workCasePreScreenId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            caseBorrowerTypeId = prescreenBusinessControl.getCaseBorrowerTypeId(workCasePreScreenId);
            log.debug("onCreation ::: caseBorrowerTYpeId : {}", caseBorrowerTypeId);
            queueName = session.getAttribute("queueName").toString();

            log.debug("onCreation ::: workCasePreScreenId : {}", workCasePreScreenId);
            log.debug("onCreation ::: stepId : {}", stepId);
            log.debug("onCreation ::: queueName : {}", queueName);


            user = (User)session.getAttribute("user");

            modeForButton = ModeForButton.ADD;
            customerModifyFlag = 0;

            onClearObjectList();
            onLoadSelectList();
            onClearObject();
            onCheckButton();
        }
    }

    public void onReset(){
        onCreation();
    }

    public void onCheckButton(){
        if(borrowerInfoViewList != null && borrowerInfoViewList.size() > 0){
            disableAssignButton = false;
        } else {
            disableAssignButton = true;
        }
    }

    public void onCheckMaxDate(String type){
        if(type.equalsIgnoreCase("dateOfexpected")){
            prescreenView.setExpectedSubmitDate(DateTimeUtil.checkMaxDate(prescreenView.getExpectedSubmitDate()));
        }else if(type.equalsIgnoreCase("dateOfregister")){
            prescreenView.setRegisterDate(DateTimeUtil.checkMaxDate(prescreenView.getRegisterDate()));
        }else if(type.equalsIgnoreCase("dateOfrefer")){
            prescreenView.setReferDate(DateTimeUtil.checkMaxDate(prescreenView.getReferDate()));
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

            if(prescreenView.getRefinanceInBank() == null){
                prescreenView.setRefinanceInBank(new Bank());
            }

            if(prescreenView.getRefinanceOutBank() == null){
                prescreenView.setRefinanceOutBank(new Bank());
            }

            if(prescreenView.getReferredExperience() == null){
                prescreenView.setReferredExperience(new ReferredExperience());
            }
        }

        facilityViewList = prescreenBusinessControl.getPreScreenFacility(prescreenView);
        if (facilityViewList == null) {
            facilityViewList = new ArrayList<FacilityView>();
        }

        /*proposePrescreenCollateralViewList = prescreenBusinessControl.getProposeCollateral()*/
        if (proposePrescreenCollateralViewList == null) { proposePrescreenCollateralViewList = new ArrayList<PrescreenCollateralView>(); }

        if(stepId == 1003){
            if(prescreenView.getId() != 0){
                bizInfoViewList = prescreenBusinessControl.getPreScreenBusinessInfo(prescreenView.getId());
                if ( bizInfoViewList == null ) {
                    bizInfoViewList = new ArrayList<BizInfoDetailView>();
                }

                proposePrescreenCollateralViewList = prescreenBusinessControl.getPreScreenCollateral(prescreenView.getId());
                if(proposePrescreenCollateralViewList == null){
                    proposePrescreenCollateralViewList = new ArrayList<PrescreenCollateralView>();
                }

            } else {
                bizInfoViewList = new ArrayList<BizInfoDetailView>();
                proposePrescreenCollateralViewList = new ArrayList<PrescreenCollateralView>();
            }


        }

        deleteCustomerInfoViewList = new ArrayList<CustomerInfoView>();

        customerInfoViewList = prescreenBusinessControl.getCustomerListByWorkCasePreScreenId(workCasePreScreenId);
        generateCustomerInfoList(customerInfoViewList);


        /*customerInfoViewList = prescreenBusinessControl.getCustomerListByWorkCasePreScreenId(workCasePreScreenId); asdf
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
        deleteCustomerInfoViewList = new ArrayList<CustomerInfoView>();*/
    }

    public void generateCustomerInfoList(List<CustomerInfoView> customerInfoViews){
        borrowerInfoViewList = new ArrayList<CustomerInfoView>();
        guarantorInfoViewList = new ArrayList<CustomerInfoView>();
        relatedInfoViewList = new ArrayList<CustomerInfoView>();

        for(CustomerInfoView item : customerInfoViews){
            if(item.getRelation().getId() == RelationValue.BORROWER.value()){
                item.setSubIndex(borrowerInfoViewList.size());
                item.setIsSpouse(0);
                borrowerInfoViewList.add(item);
                if(item.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                    //if(item.getMaritalStatus() != null && item.getMaritalStatus().getId() == 2){
                    if(item.getMaritalStatus() != null && item.getMaritalStatus().getSpouseFlag() == 1){
                        CustomerInfoView spouse = new CustomerInfoView();
                        spouse = item.getSpouse();
                        if(spouse != null){
                            spouse.setIsSpouse(1);
                            if(spouse.getRelation() != null && spouse.getRelation().getId() == RelationValue.BORROWER.value()){
                                spouse.setSubIndex(borrowerInfoViewList.size());
                                borrowerInfoViewList.add(spouse);
                            } else if(spouse.getRelation() != null && spouse.getRelation().getId() == RelationValue.GUARANTOR.value()){
                                spouse.setSubIndex(guarantorInfoViewList.size());
                                guarantorInfoViewList.add(spouse);
                            } else if(spouse.getRelation() != null && ( spouse.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value()
                                    || spouse.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value())){
                                spouse.setSubIndex(relatedInfoViewList.size());
                                relatedInfoViewList.add(spouse);
                            }
                        }
                    }
                }
            } else if(item.getRelation().getId() == RelationValue.GUARANTOR.value()){
                item.setSubIndex(guarantorInfoViewList.size());
                item.setIsSpouse(0);
                guarantorInfoViewList.add(item);
                if(item.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                    if(item.getMaritalStatus() != null && item.getMaritalStatus().getSpouseFlag() == 1){
                        CustomerInfoView spouse = new CustomerInfoView();
                        spouse.setIsSpouse(1);
                        spouse = item.getSpouse();
                        if(spouse != null){
                            if(spouse.getRelation() != null && spouse.getRelation().getId() == RelationValue.BORROWER.value()){
                                spouse.setSubIndex(borrowerInfoViewList.size());
                                borrowerInfoViewList.add(spouse);
                            } else if(spouse.getRelation() != null && spouse.getRelation().getId() == RelationValue.GUARANTOR.value()){
                                spouse.setSubIndex(guarantorInfoViewList.size());
                                guarantorInfoViewList.add(spouse);
                            } else if(spouse.getRelation() != null && ( spouse.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value()
                                    || spouse.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value() )){
                                spouse.setSubIndex(relatedInfoViewList.size());
                                relatedInfoViewList.add(spouse);
                            }
                        }
                    }
                }
            } else if(item.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value()
                    || item.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value()){
                item.setSubIndex(relatedInfoViewList.size());
                item.setIsSpouse(0);
                relatedInfoViewList.add(item);
                if(item.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                    if(item.getMaritalStatus() != null && item.getMaritalStatus().getSpouseFlag() == 1){
                        CustomerInfoView spouse = new CustomerInfoView();
                        spouse.setIsSpouse(1);
                        spouse = item.getSpouse();
                        if(spouse != null){
                            if(spouse.getRelation() != null && spouse.getRelation().getId() == RelationValue.BORROWER.value()){
                                spouse.setSubIndex(borrowerInfoViewList.size());
                                borrowerInfoViewList.add(spouse);
                            } else if(spouse.getRelation() != null && spouse.getRelation().getId() == RelationValue.GUARANTOR.value()){
                                spouse.setSubIndex(guarantorInfoViewList.size());
                                guarantorInfoViewList.add(spouse);
                            } else if(spouse.getRelation() != null && ( spouse.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value()
                                    || spouse.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value() )){
                                spouse.setSubIndex(relatedInfoViewList.size());
                                relatedInfoViewList.add(spouse);
                            }
                        }
                    }
                }
            }
        }
    }

    public void onLoadSelectList() {
        log.debug("onLoadSelectList :::");

        productGroupList = productGroupDAO.findAll();
        log.debug("onLoadSelectList ::: productGroupList size : {}", productGroupList.size());

        if(prescreenView.getProductGroup() != null){
            getProductProgramList();
        }

        /*if(stepId == 1001){
            bdmCheckerList = userDAO.findBDMChecker(user);
            log.debug("onLoadSelectList ::: bdmCheckerList size : {}", bdmCheckerList.size());
        }*/

        if(stepId == StepValue.PRESCREEN_MAKER.value()){
            businessGroupList = businessGroupDAO.findAll();
            log.debug("onLoadSelectList ::: businessGroupList size : {}", businessGroupList.size());

            potentialCollateralList = potentialCollateralDAO.findAll();
            log.debug("onLoadSelectList ::: potentialCollateralList size : {}", potentialCollateralList.size());

            refinanceList = bankDAO.getListRefinance();
            log.debug("onLoadSelectList ::: refinanceList size : {}", refinanceList.size());

            referredExperienceList = referredExperienceDAO.findAll();
            log.debug("onLoadSelectList ::: referredExperienceList size : {}", referredExperienceList.size());

            //borrowingTypeList = borrowingTypeDAO.findAll();
            borrowingTypeList = borrowingTypeDAO.findByCustomerEntityId(caseBorrowerTypeId);
            log.debug("onLoadSelectList ::: borrowingTypeList size : {}", borrowingTypeList.size());

            countryOfRegisterList = countryDAO.findAll();
            log.debug("onLoadSelectList ::: countryOfRegisterList size : {}", countryOfRegisterList.size());
        }

        //*** List for Customer ***//
        documentTypeList = documentTypeDAO.findAll();
        log.debug("onLoadSelectList ::: documentTypeList size : {}", documentTypeList.size());

        spouseDocumentTypeList = documentTypeDAO.findByCustomerEntityId(BorrowerType.INDIVIDUAL.value());
        log.debug("onLoadSelectList ::: spouseDocumentTypeList size : {}", documentTypeList.size());

        customerEntityList = customerEntityDAO.findAll();
        log.debug("onLoadSelectList ::: borrowerTypeList size : {}", customerEntityList.size());

        /*relationList = prescreenBusinessControl.getRelationByStepId(stepId);
        log.debug("onLoadSelectList ::: relationList size : {}", relationList.size());*/
        relationList = new ArrayList<Relation>();

        referenceList = new ArrayList<Reference>();

        log.debug("onLoadSelectList ::: referenceList size : {}", referenceList.size());

        maritalStatusList = maritalStatusDAO.findAll();
        log.debug("onLoadSelectList ::: maritalStatusList size : {}", maritalStatusList.size());

        titleList = titleDAO.findAll();
        log.debug("onLoadSelectList ::: titleList size : {}", titleList.size());

        nationalityList = nationalityDAO.findAll();
        log.debug("onLoadSelectList ::: nationalityList size : {}", nationalityList.size());

        businessLocationList = provinceDAO.getListOrderByParameter("name");

        provinceList = provinceDAO.getListOrderByParameter("name");
        log.debug("onLoadSelectList ::: provinceList size : {}", provinceList.size());

        provinceSpouseList = provinceDAO.getListOrderByParameter("name");
        log.debug("onLoadSelectList ::: provinceSpouseList size : {}", provinceSpouseList.size());

        /*districtList = districtDAO.findAll();
        log.debug("onLoadSelectList ::: districtList size : {}", districtList.size());*/

        /*subDistrictList = subDistrictDAO.findAll();
        log.debug("onLoadSelectList ::: subDistrictList size : {}", subDistrictList.size());*/
    }

    public void onClearObject(){
        // *** Clear Variable for Dialog *** //
        log.debug("onClearObject :::");

        if(facility == null){ facility = new FacilityView(); }
        if(bizInfoView == null){ bizInfoView = new BizInfoDetailView(); }
        if(proposeCollateral == null){ proposeCollateral = new PrescreenCollateralView(); }
        if(borrowerInfo == null){
            borrowerInfo = new CustomerInfoView();
            CustomerInfoView spouse = new CustomerInfoView();
            spouse.reset();
            spouse.setSpouse(null);
            borrowerInfo.setSpouse(spouse);
        }
        //if(spouseInfo == null) { spouseInfo = new CustomerInfoView(); }
        if(customerEntity == null){ customerEntity = new CustomerEntity(); }


        proposeCollateral.reset();
        facility.reset();
        bizInfoView.reset();
        borrowerInfo.reset();
        CustomerInfoView spouse = new CustomerInfoView();
        spouse.reset();
        spouse.setSpouse(null);
        borrowerInfo.setSpouse(spouse);
        borrowerRelation = new Relation();
        spouseRelation = new Relation();
        borrowerReference = new Reference();
        spouseReference = new Reference();
        //spouseInfo.reset();

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
        log.debug("onCloseSale ::: queueName : {}", queueName);
        try{
            //TODO Check Modified flag
            int modifyFlag = prescreenBusinessControl.getModifyValue(workCasePreScreenId);
            if(modifyFlag == 1){
                messageHeader = "Exception";
                message = "Some of data has been changed. Please Retrive Interface before Closesale.";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            } else if(modifyFlag == 2){
                messageHeader = "Exception";
                message = "Could not get data for PreScreen.";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            } else {
                prescreenBusinessControl.duplicateData(workCasePreScreenId, queueName, ActionCode.CLOSE_SALES.getVal());
                //prescreenBusinessControl.closeSale(workCasePreScreenId, queueName, ActionCode.CLOSE_SALES.getVal());

                messageHeader = "Information";
                message = "Close Sales Complete.";

                RequestContext.getCurrentInstance().execute("msgBoxRedirectDlg.show()");
            }
        } catch (Exception ex){
            messageHeader = "Exception";
            message = "Close Sales Failed, " + ex.getMessage();

            log.error("onCloseSale failed : ", ex);
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    // *** Function For Facility *** //
    public void onAddFacility() {
        log.debug("onAddFacility ::: ");
        log.debug("onAddFacility ::: prescreenView.productGroup : {}", prescreenView.getProductGroup());

        if(prescreenView.getProductGroup() != null){
            //*** Reset form ***//
            log.debug("onAddFacility ::: Reset Form");
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
        log.debug("onEditFacility ::: selectFacilityItem : {}", selectFacilityItem);

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
        log.debug("onSaveFacility ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        log.debug("onSaveFacility ::: prescreen.productgroup.getId() : {} ", prescreenView.getProductGroup().getId());
        log.debug("onSaveFacility ::: facility.getProductProgram().getId() : {} ", facility.getProductProgram().getId());
        log.debug("onSaveFacility ::: facility.getCreditType().getId() : {} ", facility.getCreditType().getId());
        log.debug("onSaveFacility ::: facility.getRequestAmount : {}", facility.getRequestAmount());
        log.debug("onSaveFacility ::: facilityViewList : {}", facilityViewList);

        if( facility.getProductProgram().getId() != 0 && facility.getCreditType().getId() != 0 && facility.getRequestAmount() != null ) {
            if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)) {
                ProductProgram productProgram = productProgramDAO.findById(facility.getProductProgram().getId());
                CreditType creditType = creditTypeDao.findById(facility.getCreditType().getId());

                FacilityView facilityItem = new FacilityView();
                facilityItem.setProductProgram(productProgram);
                facilityItem.setCreditType(creditType);
                facilityItem.setRequestAmount(facility.getRequestAmount());
                facilityViewList.add(facilityItem);
                log.debug("onSaveFacility ::: modeForButton : {}, Completed.", modeForButton);
            } else if(modeForButton != null && modeForButton.equals(ModeForButton.EDIT)) {
                ProductProgram productProgram = productProgramDAO.findById(facility.getProductProgram().getId());
                CreditType creditType = creditTypeDao.findById(facility.getCreditType().getId());
                facilityViewList.get(rowIndex).setProductProgram(productProgram);
                facilityViewList.get(rowIndex).setCreditType(creditType);
                facilityViewList.get(rowIndex).setRequestAmount(facility.getRequestAmount());
                log.debug("onSaveFacility ::: modeForButton : {}, rowIndex : {}, Completed.", modeForButton, rowIndex);
            } else {
                log.debug("onSaveFacility ::: Undefined modeForbutton !!");
            }
            complete = true;
        } else {
            log.debug("onSaveFacility ::: validation failed.");
            complete = false;
        }
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteFacility() {
        log.debug("onDeleteFacility ::: selectFacilityItem : {}");
        facilityViewList.remove(selectFacilityItem);
    }

    // *** Function For Customer *** //
    public void onAddCustomerInfo() {
        log.debug("onAddCustomerInfo ::: reset form");
        // *** Reset Form *** //
        modeForButton = ModeForButton.ADD;

        borrowerRelation = new Relation();
        spouseRelation = new Relation();
        borrowerReference = new Reference();
        spouseReference = new Reference();

        borrowerInfo = new CustomerInfoView();
        borrowerInfo.reset();

        CustomerInfoView spouse = new CustomerInfoView();
        spouse.reset();
        spouse.setSpouse(null);
        borrowerInfo.setSpouse(spouse);

        customerEntity = new CustomerEntity();

        if(stepId == StepValue.PRESCREEN_INITIAL.value()){
            borrowerInfo.getRelation().setId(RelationValue.BORROWER.value());    //Set default relation to borrower
            //TODO Check caseBorrowerType;
            if(caseBorrowerTypeId == BorrowerType.INDIVIDUAL.value()){    //case borrower type = individual
                borrowerInfo.getCustomerEntity().setId(BorrowerType.INDIVIDUAL.value());
                documentTypeList = documentTypeDAO.getDocumentTypeListPreScreen(BorrowerType.INDIVIDUAL.value());
                spouseDocumentTypeList = documentTypeDAO.getDocumentTypeListPreScreen(BorrowerType.INDIVIDUAL.value());
            } else if (caseBorrowerTypeId == BorrowerType.JURISTIC.value()){
                borrowerInfo.getCustomerEntity().setId(BorrowerType.JURISTIC.value());
                documentTypeList = documentTypeDAO.getDocumentTypeListPreScreen(BorrowerType.JURISTIC.value());
            } else { // if case borrower type = 0 check borrowerList
                if(borrowerInfoViewList != null && borrowerInfoViewList.size() > 0){
                    int borrowerType = 0;
                    borrowerType = borrowerInfoViewList.get(0).getRelation().getId();
                    if(borrowerType == BorrowerType.INDIVIDUAL.value()){
                        borrowerInfo.getCustomerEntity().setId(BorrowerType.INDIVIDUAL.value());
                        documentTypeList = documentTypeDAO.getDocumentTypeListPreScreen(BorrowerType.INDIVIDUAL.value());
                    }else if(borrowerType == BorrowerType.JURISTIC.value()){
                        borrowerInfo.getCustomerEntity().setId(BorrowerType.JURISTIC.value());
                        documentTypeList = documentTypeDAO.getDocumentTypeListPreScreen(BorrowerType.JURISTIC.value());
                    } else {
                        documentTypeList = documentTypeDAO.getDocumentTypeListPreScreen(0);
                    }
                } else {
                    documentTypeList = documentTypeDAO.getDocumentTypeListPreScreen(0);
                }
            }
        } else {
            documentTypeList = documentTypeDAO.findAll();
        }

        if(caseBorrowerTypeId == 2){
            provinceList = provinceDAO.getListOrderByParameter("name");
            districtList = new ArrayList<District>();
            subDistrictList = new ArrayList<SubDistrict>();
        } else {
            provinceList = provinceDAO.getListOrderByParameter("name");
            districtList = new ArrayList<District>();
            subDistrictList = new ArrayList<SubDistrict>();

            provinceSpouseList = provinceDAO.getListOrderByParameter("name");
            districtSpouseList = new ArrayList<District>();
            subDistrictSpouseList = new ArrayList<SubDistrict>();
        }

        log.debug("onAddCustomerInfo : borrower : {}", borrowerInfo);

        relationList = prescreenBusinessControl.getRelationByStepId(stepId, borrowerInfo.getCustomerEntity().getId(), caseBorrowerTypeId, 0);
        spouseRelationList = prescreenBusinessControl.getRelationByStepId(stepId, borrowerInfo.getCustomerEntity().getId(), caseBorrowerTypeId, 1);

        enableCustomerForm = false;
        enableDocumentType = true;
        /*enableCustomerEntity = true;*/
        enableTMBCustomerId = false;
        enableCitizenId = false;
        enableSearchForm = true;
    }

    public void onEditCustomerInfo() {
        log.debug("onEditCustomer ::: selectCustomerItem : {}", selectCustomerInfoItem);
        log.debug("onEditCustomer ::: customerInfoViewList : {}", customerInfoViewList);
        //Clone object
        Cloner cloner = new Cloner();
        if(selectCustomerInfoItem.getIsSpouse() == 1){
            //if select spouse to edit...
            log.debug("onEditCustomer ::: select spouse to edit ...");
            borrowerInfo = cloner.deepClone(customerInfoViewList.get(selectCustomerInfoItem.getListIndex()));
            borrowerRelation = cloner.deepClone(borrowerInfo.getRelation());
            onChangeRelation();
            borrowerReference = cloner.deepClone(borrowerInfo.getReference());
            log.debug("onEditCustomer ::: get borrower from list to edit : {}", borrowerInfo);
            if(borrowerInfo.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                if(borrowerInfo.getSpouse() == null){
                    CustomerInfoView spouse = new CustomerInfoView();
                    spouse.reset();
                    borrowerInfo.setSpouse(spouse);
                } else {
                    spouseRelation = cloner.deepClone(borrowerInfo.getSpouse().getRelation());
                    onChangeSpouseRelation();
                    spouseReference = cloner.deepClone(borrowerInfo.getSpouse().getReference());
                }
            }
        } else {
            log.debug("onEditCustomer ::: customerInfoViewList : {}", customerInfoViewList);
            borrowerInfo = cloner.deepClone(selectCustomerInfoItem);
            borrowerRelation = cloner.deepClone(borrowerInfo.getRelation());
            onChangeRelation();
            log.debug("onEditCustomer ::: customerInfoViewList : {}", customerInfoViewList);
            borrowerReference = cloner.deepClone(borrowerInfo.getReference());
            if(borrowerInfo.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                if(borrowerInfo.getSpouse() == null){
                    CustomerInfoView spouse = new CustomerInfoView();
                    spouse.reset();
                    borrowerInfo.setSpouse(spouse);
                } else {
                    spouseRelation = cloner.deepClone(borrowerInfo.getSpouse().getRelation());
                    onChangeSpouseRelation();
                    spouseReference = cloner.deepClone(borrowerInfo.getSpouse().getReference());
                }
            }
        }
        log.debug("onEditCustomer ::: customerInfoViewList : {}", customerInfoViewList);
        if(stepId == StepValue.PRESCREEN_INITIAL.value()){
            spouseDocumentTypeList = documentTypeDAO.getDocumentTypeListPreScreen(BorrowerType.INDIVIDUAL.value());
        }

        modeForButton = ModeForButton.EDIT;

        if(borrowerInfo.getRelation().getId() == RelationValue.BORROWER.value()){
            relationList = prescreenBusinessControl.getRelationByStepId(StepValue.PRESCREEN_INITIAL.value(), borrowerInfo.getCustomerEntity().getId(), caseBorrowerTypeId, 0);
        } else {
            relationList = prescreenBusinessControl.getRelationByStepId(StepValue.PRESCREEN_MAKER.value(), borrowerInfo.getCustomerEntity().getId(), caseBorrowerTypeId, 0);
        }
        log.debug("onEditCustomer ::: customerInfoViewList : {}", customerInfoViewList);
        if(stepId == StepValue.PRESCREEN_INITIAL.value()){
            spouseRelationList = prescreenBusinessControl.getRelationByStepAndBorrowerRelationId(StepValue.PRESCREEN_INITIAL.value(), BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, 1);
        } else {
            int borrowerRelationId = borrowerInfo.getRelation().getId();
            Relation tmpBorrowerRelation = relationDAO.findById(borrowerRelationId);
            if(borrowerInfo.getSpouse() != null){
                if(borrowerInfo.getSpouse().getId() != 0){
                    if(borrowerInfo.getSpouse().getRelation() != null){
                        if(borrowerInfo.getSpouse().getRelation().getId() == RelationValue.BORROWER.value()){
                            spouseRelationList = prescreenBusinessControl.getRelationByStepAndBorrowerRelationId(StepValue.PRESCREEN_INITIAL.value(), BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, tmpBorrowerRelation.getPriority());
                        } else {
                            spouseRelationList = prescreenBusinessControl.getRelationByStepAndBorrowerRelationId(StepValue.PRESCREEN_MAKER.value(), BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, tmpBorrowerRelation.getPriority());
                        }
                    }

                }else{
                    spouseRelationList = prescreenBusinessControl.getRelationByStepAndBorrowerRelationId(StepValue.PRESCREEN_MAKER.value(), BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, tmpBorrowerRelation.getPriority());
                }
            } else {
                spouseRelationList = prescreenBusinessControl.getRelationByStepAndBorrowerRelationId(StepValue.PRESCREEN_MAKER.value(), BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, tmpBorrowerRelation.getPriority());
            }
        }
        log.debug("onEditCustomer ::: customerInfoViewList : {}", customerInfoViewList);
        documentTypeList = documentTypeDAO.findByCustomerEntityId(borrowerInfo.getCustomerEntity().getId());
        titleList = titleDAO.getListByCustomerEntityId(borrowerInfo.getCustomerEntity().getId());

        this.customerEntity = borrowerInfo.getCustomerEntity();

        //-- To Get district list, subDistrict
        onChangeProvinceBorrower();
        onChangeDistrictBorrower();

        //-- To Get spouse district, subDistrict List
        if(borrowerInfo.getSpouse() != null){
            if(borrowerInfo.getMaritalStatus() != null && borrowerInfo.getMaritalStatus().getId() == 2){
                MaritalStatus maritalStatus = maritalStatusDAO.findById(borrowerInfo.getMaritalStatus().getId());
                if(maritalStatus != null && maritalStatus.getSpouseFlag() == 1){
                    onChangeProvinceSpouse();
                    onChangeDistrictSpouse();
                }
            }
        }
        log.debug("onEditCustomer ::: customerInfoViewList : {}", customerInfoViewList);
        enableCustomerForm = true;
        if(Util.isTrue(borrowerInfo.getSearchFromRM())){
            enableDocumentType = false;
            enableCitizenId = false;
            enableTMBCustomerId = false;
        } else {
            if(stepId == StepValue.PRESCREEN_MAKER.value() && borrowerInfo.getRelation().getId() == 1){
                enableDocumentType = false;
                enableCitizenId = false;
                enableTMBCustomerId = false;
            } else {
                enableDocumentType = false;
                if(borrowerInfo.getNcbFlag() == 1){
                    enableCitizenId = false;
                }else{
                    enableCitizenId = true;
                }
                enableTMBCustomerId = true;
            }
        }
        log.debug("customerInfoViewList before save edit : {}", customerInfoViewList);
        enableSearchForm = false;
    }

    public void onChangeProvinceBorrower(){
        if(borrowerInfo.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
            if(borrowerInfo.getCurrentAddress().getProvince() != null && borrowerInfo.getCurrentAddress().getProvince().getCode() != 0){
                districtList = districtDAO.getListByProvince(borrowerInfo.getCurrentAddress().getProvince());
                subDistrictList = new ArrayList<SubDistrict>();
            } else {
                districtList = new ArrayList<District>();
                subDistrictList = new ArrayList<SubDistrict>();
            }
        } else {
            if(borrowerInfo.getRegisterAddress().getProvince() != null && borrowerInfo.getRegisterAddress().getProvince().getCode() != 0){
                districtList = districtDAO.getListByProvince(borrowerInfo.getRegisterAddress().getProvince());
                subDistrictList = new ArrayList<SubDistrict>();
            } else {
                districtList = new ArrayList<District>();
                subDistrictList = new ArrayList<SubDistrict>();
            }
        }
    }

    public void onChangeDistrictBorrower(){
        if(borrowerInfo.getCurrentAddress().getDistrict() != null && borrowerInfo.getCurrentAddress().getDistrict().getId() != 0){
            subDistrictList = subDistrictDAO.getListByDistrict(borrowerInfo.getCurrentAddress().getDistrict());
        } else {
            subDistrictList = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceSpouse(){
        if(borrowerInfo.getSpouse() != null){
            CustomerInfoView spouse = borrowerInfo.getSpouse();
            if(spouse.getCurrentAddress().getProvince() != null && spouse.getCurrentAddress().getProvince().getCode() != 0){
                districtSpouseList = districtDAO.getListByProvince(spouse.getCurrentAddress().getProvince());
                subDistrictSpouseList = new ArrayList<SubDistrict>();
            } else {
                districtSpouseList = new ArrayList<District>();
                subDistrictSpouseList = new ArrayList<SubDistrict>();
            }
        }
    }

    public void onChangeDistrictSpouse(){
        if(borrowerInfo.getSpouse() != null){
            CustomerInfoView spouse = borrowerInfo.getSpouse();
            if(spouse.getCurrentAddress().getDistrict() != null && spouse.getCurrentAddress().getDistrict().getId() != 0){
                subDistrictSpouseList = subDistrictDAO.getListByDistrict(spouse.getCurrentAddress().getDistrict());
            } else {
                subDistrictSpouseList = new ArrayList<SubDistrict>();
            }
        }
    }

    public void onSaveCustomerInfo() {
        log.debug("onSaveCustomerInfo ::: modeForButton : {}", modeForButton);
        log.debug("onSaveCustomerInfo ::: customerInfoViewList : {}", customerInfoViewList);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = true;        //Change only failed to save
        Cloner cloner = new Cloner();

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
        log.debug("onSaveCustomerInfo ::: customerEntity : {}", borrowerInfo.getCustomerEntity());
        log.debug("onSaveCustomerInfo ::: relation : {}", borrowerInfo.getRelation());

        if(borrowerInfo.getCustomerEntity() != null && borrowerInfo.getCustomerEntity().getId() != 0){
            borrowerInfo.setCustomerEntity(customerEntityDAO.findById(borrowerInfo.getCustomerEntity().getId()));
        }

        if(borrowerInfo.getTitleTh() != null && borrowerInfo.getTitleTh().getId() != 0){
            borrowerInfo.setTitleTh(titleDAO.findById(borrowerInfo.getTitleTh().getId()));
        }

        if(borrowerInfo.getMaritalStatus() != null && borrowerInfo.getMaritalStatus().getId() != 0){
            borrowerInfo.setMaritalStatus(maritalStatusDAO.findById(borrowerInfo.getMaritalStatus().getId()));
        }

        if(borrowerRelation != null && borrowerRelation.getId() != 0){
            borrowerInfo.setRelation(cloner.deepClone(borrowerRelation));
        }

        if(borrowerReference != null && borrowerReference.getId() != 0){
            borrowerInfo.setReference(cloner.deepClone(borrowerReference));
        }

        if(spouseRelation != null && spouseRelation.getId() != 0){
            borrowerInfo.getSpouse().setRelation(cloner.deepClone(spouseRelation));
        }

        if(spouseReference != null && spouseReference.getId() != 0){
            borrowerInfo.getSpouse().setReference(cloner.deepClone(spouseReference));
        }

        if(modeForButton.equals(ModeForButton.ADD)){
            log.debug("onSaveCustomerInfo ::: borrowerInfo : {}", borrowerInfo);
            if(borrowerInfo.getCustomerEntity().getId() != 0){
                int customerListIndex = 0;
                customerListIndex = customerInfoViewList.size();        //Index is already +1

                if(borrowerInfo.getCustomerEntity().getId() == 1){      //Individual
                    //---- Validate CitizenId ----//
                    boolean validateCitizen = true;

                    if(borrowerInfo.getMaritalStatus() != null && borrowerInfo.getMaritalStatus().getSpouseFlag() == 1){
                        if(borrowerInfo.getSpouse() != null){
                            log.debug("Borrower Citizen : {} , Spouse Citizen : {}", borrowerInfo.getCitizenId(), borrowerInfo.getSpouse().getCitizenId());
                            if(borrowerInfo.getCitizenId().equals(borrowerInfo.getSpouse().getCitizenId())){
                                validateCitizen = false;
                                messageHeader = "Save customer failed.";
                                message = "Duplicate citizen id.";
                            }
                        }
                    }

                    if(validateCitizen){
                        for(CustomerInfoView customerInfoView : customerInfoViewList ){
                            //Case when update customer and change citizen id to same another.
                            if(borrowerInfo.getCitizenId().equalsIgnoreCase(customerInfoView.getCitizenId())){
                                validateCitizen = false;
                                messageHeader = "Save customer failed.";
                                message = "Duplicate citizen id.";
                                break;
                            }

                            //Case when Borrower add citizen duplicate with spouse
                            if(customerInfoView.getMaritalStatus() != null && customerInfoView.getMaritalStatus().getId() == 2){
                                if(customerInfoView.getSpouse() != null){
                                    if(customerInfoView.getSpouse().getCitizenId().equalsIgnoreCase(borrowerInfo.getCitizenId())){
                                        validateCitizen = false;
                                        messageHeader = "Save customer failed.";
                                        message = "Duplicate citizen id.";
                                        break;
                                    }
                                }
                            }

                            //Case when update customer add change citizen id (spouse) same another.
                            if(borrowerInfo.getMaritalStatus() != null && borrowerInfo.getMaritalStatus().getId() != 0 && borrowerInfo.getMaritalStatus().getId() == 2){
                                if(borrowerInfo.getSpouse() != null && customerInfoView.getSpouse() != null){
                                    if(borrowerInfo.getSpouse().getListIndex() != 0){
                                        //Update old spouse check with out old index
                                        if(borrowerInfo.getSpouse().getCitizenId().equalsIgnoreCase(customerInfoView.getCitizenId())){
                                            //Check with other borrower
                                            log.debug("spouse fail 01");
                                            validateCitizen = false;
                                            messageHeader = "Save customer (Spouse) failed.";
                                            message = "Duplicate citizen id (Spouse).";
                                            break;
                                        }
                                        if(borrowerInfo.getSpouse().getCitizenId().equalsIgnoreCase(customerInfoView.getSpouse().getCitizenId())){
                                            //Check with other spouse
                                            validateCitizen = false;
                                            messageHeader = "Save customer (Spouse) failed.";
                                            message = "Duplicate citizen id (Spouse).";
                                            break;
                                        }
                                    } else if (borrowerInfo.getSpouse().getListIndex() == 0){
                                        //Insert new spouse check all in customerInfoViewList
                                        if(borrowerInfo.getSpouse().getCitizenId().equalsIgnoreCase(customerInfoView.getCitizenId())){
                                            //Check with other borrower
                                            validateCitizen = false;
                                            messageHeader = "Save customer (Spouse) failed.";
                                            message = "Duplicate citizen id (Spouse).";
                                            break;
                                        }
                                        if(borrowerInfo.getSpouse().getCitizenId().equalsIgnoreCase(customerInfoView.getSpouse().getCitizenId())){
                                            //Check with other spouse
                                            if(borrowerInfo.getSpouse().getListIndex() != customerInfoView.getSpouse().getListIndex()){
                                                validateCitizen = false;
                                                messageHeader = "Save customer (Spouse) failed.";
                                                message = "Duplicate citizen id (Spouse).";
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    if(validateCitizen){
                        log.debug("onSaveCustomerInfo ::: Borrower - relation : {}", borrowerInfo.getRelation());
                        //--- Borrower ---
                        borrowerInfo.setListIndex(customerListIndex);
                        if(borrowerInfo.getRelation().getId() == RelationValue.BORROWER.value()){
                            //Borrower
                            borrowerInfo.setListName("BORROWER");
                            borrowerInfo.setSubIndex(borrowerInfoViewList.size());
                            borrowerInfo.setIsSpouse(0);
                            //TODO assign caseBorrowerType
                            borrowerInfoViewList.add(borrowerInfo);
                            customerInfoViewList.add(borrowerInfo);
                            //Set case borrower type id
                            if(caseBorrowerTypeId == 0){
                                caseBorrowerTypeId = BorrowerType.INDIVIDUAL.value();
                            }
                            //Add flag for popup when save
                            customerModifyFlag = customerModifyFlag + 1;
                        }else if(borrowerInfo.getRelation().getId() == RelationValue.GUARANTOR.value()){
                            //Guarantor
                            borrowerInfo.setListName("GUARANTOR");
                            borrowerInfo.setSubIndex(guarantorInfoViewList.size());
                            borrowerInfo.setIsSpouse(0);
                            guarantorInfoViewList.add(borrowerInfo);
                            customerInfoViewList.add(borrowerInfo);
                            //Add flag for popup when save
                            customerModifyFlag = customerModifyFlag + 1;
                        }else if(borrowerInfo.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value() || borrowerInfo.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value()){
                            //Relate Person
                            borrowerInfo.setListName("RELATED");
                            borrowerInfo.setSubIndex(relatedInfoViewList.size());
                            borrowerInfo.setIsSpouse(0);
                            relatedInfoViewList.add(borrowerInfo);
                            customerInfoViewList.add(borrowerInfo);
                            //Add flag for popup when save
                            customerModifyFlag = customerModifyFlag + 1;
                        }else{
                            //customerInfoViewList.add(borrowerInfo);
                            complete = false;
                            messageHeader = "Save customer failed.";
                            message = "Invalid relation type.";
                        }

                        if(complete){
                            //--- Spouse ---
                            log.debug("onSaveCustomerInfo ::: SpouseInfo : {}", borrowerInfo.getSpouse());
                            //if(borrowerInfo.getMaritalStatus().getId() != 1 && borrowerInfo.getMaritalStatus().getId() != 4 && borrowerInfo.getMaritalStatus().getId() != 5){
                            if(borrowerInfo.getMaritalStatus().getSpouseFlag() == 1){
                                if(borrowerInfo.getSpouse().getRelation() != null && borrowerInfo.getSpouse().getRelation().getId() != 0){
                                    CustomerInfoView spouseInfo = borrowerInfo.getSpouse();
                                    spouseInfo.setIsSpouse(1);
                                    spouseInfo.setListIndex(customerListIndex);
                                    CustomerEntity spouseCustomerEntity = new CustomerEntity();
                                    spouseCustomerEntity = customerEntityDAO.findById(1);
                                    spouseInfo.setCustomerEntity(spouseCustomerEntity);
                                    if(spouseInfo.getTitleTh().getId() != 0){
                                        spouseInfo.setTitleTh(titleDAO.findById(spouseInfo.getTitleTh().getId()));
                                    }
                                    spouseInfo.setMaritalStatus(borrowerInfo.getMaritalStatus());
                                    log.debug("onSaveCustomerInfo ::: Spouse - relation : {}", spouseInfo.getRelation());
                                    if(spouseInfo.getRelation().getId() == RelationValue.BORROWER.value()) {
                                        //Spouse - Borrower
                                        spouseInfo.setListName("BORROWER");
                                        spouseInfo.setSubIndex(borrowerInfoViewList.size());
                                        borrowerInfoViewList.add(spouseInfo);
                                    } else if(spouseInfo.getRelation().getId() == RelationValue.GUARANTOR.value()) {
                                        //Spouse - Guarantor
                                        spouseInfo.setListName("GUARANTOR");
                                        spouseInfo.setSubIndex(guarantorInfoViewList.size());
                                        guarantorInfoViewList.add(spouseInfo);
                                    } else if(spouseInfo.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value() || spouseInfo.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value()) {
                                        //Spouse - Relate Person
                                        spouseInfo.setListName("RELATED");
                                        spouseInfo.setSubIndex(relatedInfoViewList.size());
                                        relatedInfoViewList.add(spouseInfo);
                                    } else {
                                        complete = false;
                                        messageHeader = "Save customer (Spouse) failed.";
                                        message = "Invalid relation type.";
                                    }
                                }
                            }
                        }
                    } else {
                        // Validate citizen failed..
                        complete = false;
                    }

                }else if(borrowerInfo.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){ //Juristic
                    DocumentType documentType = new DocumentType();
                    documentType.setId(3);
                    borrowerInfo.setDocumentType(documentType);
                    borrowerInfo.setIsSpouse(0);
                    borrowerInfo.setListIndex(customerListIndex);

                    boolean validateRegistration = true;
                    for(CustomerInfoView customerInfoView : customerInfoViewList ){
                        if(borrowerInfo.getRegistrationId().equalsIgnoreCase(customerInfoView.getRegistrationId())){
                            if(borrowerInfo.getListIndex() != customerInfoView.getListIndex()){
                                validateRegistration = false;
                                messageHeader = "Save customer failed.";
                                message = "Duplicate registration id.";
                                break;
                            }
                        }
                    }

                    if(validateRegistration){
                        //--- Borrower ---
                        if(borrowerInfo.getRelation().getId() == RelationValue.BORROWER.value()){
                            borrowerInfo.setListName("BORROWER");
                            borrowerInfo.setSubIndex(borrowerInfoViewList.size());
                            //Borrower
                            borrowerInfoViewList.add(borrowerInfo);
                            customerInfoViewList.add(borrowerInfo);

                            //Set case borrower type id
                            if(caseBorrowerTypeId == 0){
                                caseBorrowerTypeId = BorrowerType.JURISTIC.value();
                            }
                        }else if(borrowerInfo.getRelation().getId() == RelationValue.GUARANTOR.value()){
                            borrowerInfo.setListName("GUARANTOR");
                            borrowerInfo.setSubIndex(guarantorInfoViewList.size());
                            //Guarantor
                            guarantorInfoViewList.add(borrowerInfo);
                            customerInfoViewList.add(borrowerInfo);
                        }else if(borrowerInfo.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value() || borrowerInfo.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value()){
                            borrowerInfo.setListName("RELATED");
                            borrowerInfo.setSubIndex(relatedInfoViewList.size());
                            //Relate Person
                            relatedInfoViewList.add(borrowerInfo);
                            customerInfoViewList.add(borrowerInfo);
                        }else{
                            complete = false;
                            messageHeader = "Save customer failed.";
                            message = "Invalid relation type.";
                        }
                    } else {
                        complete = false;
                    }
                } else {
                    complete = false;
                    messageHeader = "Save customer failed.";
                    message = "Invalid customer entity.";
                }
            }
//---------------------------------------------------------------- EDIT CUSTOMER ------------------------------------------------------------------//
        } else { // Edit
            log.debug("onSaveCustomerInfo ::: borrowerInfo : {}", borrowerInfo);
            log.debug("onSaveCustomerInfo ::: customerInfoList : {}", customerInfoViewList);
            if(borrowerInfo.getCustomerEntity().getId() != 0){
                int customerListIndex = borrowerInfo.getListIndex();
                int oldRelationId = 0;
                String oldCitizenId = "";
                String oldRegistrationId = "";
                if(borrowerInfo.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){          //Individual
                    //---- Validate CitizenId ----//
                    boolean validateCitizen = true;
                    for(CustomerInfoView customerInfoView : customerInfoViewList ){
                        //Case when update customer and change citizen id to same another.
                        if(borrowerInfo.getCitizenId().equalsIgnoreCase(customerInfoView.getCitizenId()) && (borrowerInfo.getListIndex() != customerInfoView.getListIndex())){
                            validateCitizen = false;
                            messageHeader = "Save customer failed.";
                            message = "Duplicate citizen id.";
                            break;
                        }

                        //Case when Borrower add citizen duplicate with spouse
                        if(customerInfoView.getMaritalStatus() != null && customerInfoView.getMaritalStatus().getId() == 2){
                            if(customerInfoView.getSpouse() != null){
                                if(customerInfoView.getSpouse().getCitizenId().equalsIgnoreCase(borrowerInfo.getCitizenId())){
                                    validateCitizen = false;
                                    messageHeader = "Save customer failed.";
                                    message = "Duplicate citizen id.";
                                    break;
                                }
                            }
                        }

                        //Case when update customer add change citizen id (spouse) same another.
                        if(borrowerInfo.getMaritalStatus() != null && borrowerInfo.getMaritalStatus().getSpouseFlag() == 1){
                            if(borrowerInfo.getSpouse() != null && customerInfoView.getSpouse() != null){
                                if(borrowerInfo.getSpouse().getListIndex() != 0){
                                    //Update old spouse check with out old index
                                    if(borrowerInfo.getSpouse().getCitizenId().equalsIgnoreCase(customerInfoView.getCitizenId())){
                                        //Check with other borrower
                                        if(borrowerInfo.getSpouse().getListIndex() != customerInfoView.getListIndex()){
                                            log.debug("spouse fail 01");
                                            validateCitizen = false;
                                            messageHeader = "Save customer (Spouse) failed.";
                                            message = "Duplicate citizen id (Spouse).";
                                            break;
                                        }
                                    }
                                    if(borrowerInfo.getSpouse().getCitizenId().equalsIgnoreCase(customerInfoView.getSpouse().getCitizenId())){
                                        //Check with other spouse
                                        if(borrowerInfo.getSpouse().getListIndex() != customerInfoView.getSpouse().getListIndex()){
                                            validateCitizen = false;
                                            messageHeader = "Save customer (Spouse) failed.";
                                            message = "Duplicate citizen id (Spouse).";
                                            break;
                                        }
                                    }
                                } else if (borrowerInfo.getSpouse().getListIndex() == 0){
                                    //Insert new spouse check all in customerInfoViewList
                                    if(borrowerInfo.getSpouse().getCitizenId().equalsIgnoreCase(customerInfoView.getCitizenId())){
                                        //Check with other borrower
                                        validateCitizen = false;
                                        messageHeader = "Save customer (Spouse) failed.";
                                        message = "Duplicate citizen id (Spouse).";
                                        break;
                                    }
                                    if(borrowerInfo.getSpouse().getCitizenId().equalsIgnoreCase(customerInfoView.getSpouse().getCitizenId())){
                                        //Check with other spouse
                                        if(borrowerInfo.getSpouse().getListIndex() != customerInfoView.getSpouse().getListIndex()){
                                            validateCitizen = false;
                                            messageHeader = "Save customer (Spouse) failed.";
                                            message = "Duplicate citizen id (Spouse).";
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if(customerInfoView.getListIndex() == borrowerInfo.getListIndex()){
                            oldRelationId = customerInfoView.getRelation().getId();
                            oldCitizenId = customerInfoView.getCitizenId();
                        }
                    }

                    if(validateCitizen){
                        boolean borrowerChangedRelation = false;
                        log.debug("onSaveCustomerInfo ::: Borrower - relation : {}", borrowerInfo.getRelation());
                        //--- Get old borrower by list index //
                        //CustomerInfoView oldCustomerInfoView = prescreenBusinessControl.cloneCustomer(customerInfoViewList.get(borrowerInfo.getListIndex()));
                        CustomerInfoView oldCustomerInfoView = null;
                        try {
                            oldCustomerInfoView = cloner.deepClone(customerInfoViewList.get(borrowerInfo.getListIndex()));
                        } catch (Exception e) {
                            log.error("============= cloning exception begin ================");
                            log.error("workcasePrescreenId: {}",workCasePreScreenId);
                            log.error("customerInfoViewList: {}",customerInfoViewList);
                            log.error("borrowerInfo index: {}",borrowerInfo.getListIndex());
                            log.error("cloning exception!",e);
                            log.error("============= cloning exception end ================");
                        }
                        CustomerInfoView oldSpouse = null;
                        if(oldCustomerInfoView.getSpouse() != null){
                            oldSpouse = oldCustomerInfoView.getSpouse();
                        }
                        int oldSubIndex = borrowerInfo.getSubIndex();
                        //--- Borrower ---
                        if(borrowerInfo.getRelation().getId() == RelationValue.BORROWER.value()){
                            //Borrower
                            borrowerInfo.setListName("BORROWER");
                            borrowerInfo.setIsSpouse(0);
                            if(oldRelationId == RelationValue.BORROWER.value()){
                                //Update old borrowerList;
                                int subIndex = borrowerInfo.getSubIndex();
                                borrowerInfoViewList.set(subIndex, borrowerInfo);       //replace new value for old object list;
                            } else {
                                //Remove from old list
                                if(oldRelationId == RelationValue.GUARANTOR.value()){
                                    //Remove from guarantor list
                                    guarantorInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for guarantor list
                                    reIndexCustomerList(ListCustomerName.GUARANTOR);
                                } else {
                                    //Remove from related list
                                    relatedInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for related list
                                    reIndexCustomerList(ListCustomerName.RELATED);
                                }

                                //Insert into new borrowerList;
                                borrowerInfo.setSubIndex(borrowerInfoViewList.size());
                                borrowerInfoViewList.add(borrowerInfo);
                                borrowerChangedRelation = true;
                            }

                            //customerInfoViewList.set(borrowerInfo.getListIndex(), borrowerInfo);
                        } else if(borrowerInfo.getRelation().getId() == RelationValue.GUARANTOR.value()){
                            borrowerInfo.setListName("GUARANTOR");
                            borrowerInfo.setIsSpouse(0);
                            if(oldRelationId == RelationValue.GUARANTOR.value()){
                                //Update old guarantorList;
                                int subIndex = borrowerInfo.getSubIndex();
                                guarantorInfoViewList.set(subIndex, borrowerInfo);       //replace new value for old object list;
                            } else {
                                //Remove from old list
                                if(oldRelationId == RelationValue.BORROWER.value()){
                                    //Remove from guarantor list
                                    borrowerInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for guarantor list
                                    reIndexCustomerList(ListCustomerName.BORROWER);
                                } else {
                                    //Remove from related list
                                    relatedInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for related list
                                    reIndexCustomerList(ListCustomerName.RELATED);
                                }

                                //Insert into new guarantorList;
                                borrowerInfo.setSubIndex(guarantorInfoViewList.size());
                                guarantorInfoViewList.add(borrowerInfo);
                                borrowerChangedRelation = true;
                            }
                            //customerInfoViewList.set(borrowerInfo.getListIndex(), borrowerInfo);
                        } else if(borrowerInfo.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value() || borrowerInfo.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value()){
                            borrowerInfo.setListName("RELATED");
                            borrowerInfo.setIsSpouse(0);
                            if(oldRelationId == RelationValue.DIRECTLY_RELATED.value() || oldRelationId == RelationValue.INDIRECTLY_RELATED.value()){
                                //Update old relatedList;
                                int subIndex = borrowerInfo.getSubIndex();
                                relatedInfoViewList.set(subIndex, borrowerInfo);       //replace new value for old object list;
                            } else {
                                //Remove from old list
                                if(oldRelationId == RelationValue.BORROWER.value()){
                                    //Remove from guarantor list
                                    borrowerInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for guarantor list
                                    reIndexCustomerList(ListCustomerName.BORROWER);
                                } else {
                                    //Remove from related list
                                    guarantorInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for related list
                                    reIndexCustomerList(ListCustomerName.GUARANTOR);
                                }

                                //Insert into new relatedList;
                                borrowerInfo.setSubIndex(relatedInfoViewList.size());
                                relatedInfoViewList.add(borrowerInfo);
                                borrowerChangedRelation = true;
                            }
                            //customerInfoViewList.set(borrowerInfo.getListIndex(), borrowerInfo);
                        }
                        log.debug("onSaveCustomer ::: checkSpouse ------");
                        //--- TODO add spouse to list
                        if(borrowerInfo.getMaritalStatus() != null && borrowerInfo.getMaritalStatus().getSpouseFlag() == 1){
                            boolean sameRelationWithBorrower = false;
                            log.debug("onSaveCustomer ::: borrowerInfo.getMaritalStatus() : {}", borrowerInfo.getMaritalStatus());
                            //TODO check old spouse and new spouse
                            log.debug("onSaveCustomer ::: oldSpouse : {}", oldSpouse);
                            CustomerInfoView newSpouse = borrowerInfo.getSpouse();
                            CustomerEntity spouseCustomerEntity = new CustomerEntity();
                            spouseCustomerEntity = customerEntityDAO.findById(1);
                            newSpouse.setCustomerEntity(spouseCustomerEntity);
                            newSpouse.setMaritalStatus(borrowerInfo.getMaritalStatus());
                            if(newSpouse.getTitleTh().getId() != 0){
                                newSpouse.setTitleTh(titleDAO.findById(newSpouse.getTitleTh().getId()));
                            }
                            log.debug("onSaveCustomer ::: newSpouse : {}", newSpouse);
                            int oldSpouseRelation = 0;
                            if(oldSpouse != null){
                                //Update old spouse
                                if(oldSpouse.getRelation() != null){
                                    int spouseSubIndex = newSpouse.getSubIndex();
                                    oldSpouseRelation = oldSpouse.getRelation().getId();
                                    if(oldRelationId == oldSpouseRelation){
                                        sameRelationWithBorrower = true;
                                    }
                                    if(borrowerChangedRelation && sameRelationWithBorrower){
                                        spouseSubIndex = spouseSubIndex - 1;
                                    }
                                    if(newSpouse.getRelation() != null && newSpouse.getRelation().getId() == oldSpouseRelation){
                                        //update old list
                                        if(oldSpouseRelation == RelationValue.BORROWER.value()){
                                            borrowerInfoViewList.set(spouseSubIndex, newSpouse);
                                        } else if(oldSpouseRelation == RelationValue.GUARANTOR.value()){
                                            guarantorInfoViewList.set(spouseSubIndex, newSpouse);
                                        } else if(oldSpouseRelation == RelationValue.DIRECTLY_RELATED.value() || oldSpouseRelation == RelationValue.INDIRECTLY_RELATED.value()){
                                            relatedInfoViewList.set(spouseSubIndex, newSpouse);
                                        }
                                    } else if(newSpouse.getRelation() != null && newSpouse.getRelation().getId() != oldSpouseRelation){
                                        //remove old list
                                        if(oldSpouseRelation == RelationValue.BORROWER.value()){
                                            borrowerInfoViewList.remove(spouseSubIndex);
                                            reIndexCustomerList(ListCustomerName.BORROWER);
                                        } else if(oldSpouseRelation == RelationValue.GUARANTOR.value()){
                                            guarantorInfoViewList.remove(spouseSubIndex);
                                            reIndexCustomerList(ListCustomerName.GUARANTOR);
                                        } else if(oldSpouseRelation == RelationValue.DIRECTLY_RELATED.value() || oldSpouseRelation == RelationValue.INDIRECTLY_RELATED.value()){
                                            relatedInfoViewList.remove(spouseSubIndex);
                                            reIndexCustomerList(ListCustomerName.RELATED);
                                        }
                                        //add to new list
                                        newSpouse.setListIndex(customerListIndex);
                                        if(newSpouse.getRelation() != null && newSpouse.getRelation().getId() == RelationValue.BORROWER.value()){
                                            newSpouse.setListName("BORROWER");
                                            newSpouse.setSubIndex(borrowerInfoViewList.size());
                                            borrowerInfoViewList.add(newSpouse);
                                        } else if(newSpouse.getRelation() != null && newSpouse.getRelation().getId() == RelationValue.GUARANTOR.value()){
                                            newSpouse.setListName("GUARANTOR");
                                            newSpouse.setSubIndex(guarantorInfoViewList.size());
                                            guarantorInfoViewList.add(newSpouse);
                                        } else if(newSpouse.getRelation() != null && (newSpouse.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value() || newSpouse.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value())){
                                            newSpouse.setListName("RELATED");
                                            newSpouse.setSubIndex(relatedInfoViewList.size());
                                            relatedInfoViewList.add(newSpouse);
                                        }
                                    }
                                }
                            } else {
                                //Add new spouse to list
                                newSpouse.setListIndex(customerListIndex);
                                newSpouse.setIsSpouse(1);
                                newSpouse.setMaritalStatus(borrowerInfo.getMaritalStatus());
                                if(newSpouse.getRelation() != null && newSpouse.getRelation().getId() == RelationValue.BORROWER.value()){
                                    newSpouse.setListName("BORROWER");
                                    newSpouse.setSubIndex(borrowerInfoViewList.size());
                                    borrowerInfoViewList.add(newSpouse);
                                } else if (newSpouse.getRelation() != null && newSpouse.getRelation().getId() == RelationValue.GUARANTOR.value()){
                                    newSpouse.setListName("GUARANTOR");
                                    newSpouse.setSubIndex(guarantorInfoViewList.size());
                                    guarantorInfoViewList.add(newSpouse);
                                } else if (newSpouse.getRelation() != null && ( newSpouse.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value() || newSpouse.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value())){
                                    newSpouse.setListName("RELATED");
                                    newSpouse.setSubIndex(relatedInfoViewList.size());
                                    relatedInfoViewList.add(newSpouse);
                                }
                            }
                        } else if(borrowerInfo.getMaritalStatus() != null && borrowerInfo.getMaritalStatus().getSpouseFlag() == 0) {
                            //TODO check old spouse and remove from list
                            log.debug("onSaveCustomer ::: remove spouse from list");
                            log.debug("onSaveCustomer ::: oldSpouse : {}", oldSpouse);
                            if(oldSpouse != null){
                                if(oldSpouse.getRelation() != null && oldSpouse.getRelation().getId() == RelationValue.BORROWER.value()){
                                    borrowerInfoViewList.remove(oldSpouse.getSubIndex());
                                    reIndexCustomerList(ListCustomerName.BORROWER);
                                } else if(oldSpouse.getRelation() != null && oldSpouse.getRelation().getId() == RelationValue.GUARANTOR.value()){
                                    guarantorInfoViewList.remove(oldSpouse.getSubIndex());
                                    reIndexCustomerList(ListCustomerName.GUARANTOR);
                                } else if(oldSpouse.getRelation() != null && (oldSpouse.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value() || oldSpouse.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value())){
                                    relatedInfoViewList.remove(oldSpouse.getSubIndex());
                                    reIndexCustomerList(ListCustomerName.RELATED);
                                }
                            }
                        }
                        customerInfoViewList.set(borrowerInfo.getListIndex(), borrowerInfo);
                    } else {
                        complete = false;
                    }

                }else if(borrowerInfo.getCustomerEntity().getId() == 2){    //Juristic
                    //--- Validate Registration Id ---//
                    DocumentType documentType = new DocumentType();
                    documentType.setId(3);
                    borrowerInfo.setDocumentType(documentType);
                    borrowerInfo.setIsSpouse(0);
                    borrowerInfo.setListIndex(customerListIndex);

                    boolean validateRegistration = true;
                    for(CustomerInfoView customerInfoView : customerInfoViewList ){
                        if(borrowerInfo.getRegistrationId().equalsIgnoreCase(customerInfoView.getRegistrationId())){
                            if(borrowerInfo.getListIndex() != customerInfoView.getListIndex()){
                                validateRegistration = false;
                                messageHeader = "Save customer failed.";
                                message = "Duplicate registration id.";
                                break;
                            }
                        }

                        if(customerInfoView.getListIndex() == borrowerInfo.getListIndex()){
                            oldRelationId = customerInfoView.getRelation().getId();
                        }
                    }

                    if(validateRegistration){
                        log.debug("onSaveCustomerInfo ::: Borrower - relation : {}", borrowerInfo.getRelation());
                        //--- Borrower ---
                        if(borrowerInfo.getRelation().getId() == RelationValue.BORROWER.value()){
                            //Borrower
                            borrowerInfo.setListName("BORROWER");
                            borrowerInfo.setIsSpouse(0);
                            if(oldRelationId == RelationValue.BORROWER.value()){
                                //Update old borrowerList;
                                int subIndex = borrowerInfo.getSubIndex();
                                borrowerInfoViewList.set(subIndex, borrowerInfo);       //replace new value for old object list;
                            } else {
                                //Insert into new borrowerList;
                                borrowerInfoViewList.add(borrowerInfo);

                                //Remove from old list
                                if(oldRelationId == RelationValue.GUARANTOR.value()){
                                    //Remove from guarantor list
                                    guarantorInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for guarantor list
                                    reIndexCustomerList(ListCustomerName.GUARANTOR);
                                } else {
                                    //Remove from related list
                                    relatedInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for related list
                                    reIndexCustomerList(ListCustomerName.RELATED);
                                }
                            }
                            customerInfoViewList.set(borrowerInfo.getListIndex(), borrowerInfo);
                        } else if(borrowerInfo.getRelation().getId() == RelationValue.GUARANTOR.value()){
                            borrowerInfo.setListName("GUARANTOR");
                            borrowerInfo.setIsSpouse(0);
                            if(oldRelationId == RelationValue.GUARANTOR.value()){
                                //Update old guarantorList;
                                int subIndex = borrowerInfo.getSubIndex();
                                guarantorInfoViewList.set(subIndex, borrowerInfo);       //replace new value for old object list;
                            } else {
                                //Insert into new guarantorList;
                                borrowerInfo.setSubIndex(guarantorInfoViewList.size());
                                guarantorInfoViewList.add(borrowerInfo);

                                //Remove from old list
                                if(oldRelationId == RelationValue.BORROWER.value()){
                                    //Remove from guarantor list
                                    borrowerInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for guarantor list
                                    reIndexCustomerList(ListCustomerName.BORROWER);
                                } else {
                                    //Remove from related list
                                    relatedInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for related list
                                    reIndexCustomerList(ListCustomerName.RELATED);
                                }
                            }
                            customerInfoViewList.set(borrowerInfo.getListIndex(), borrowerInfo);
                        } else if(borrowerInfo.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value() || borrowerInfo.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value()){
                            borrowerInfo.setListName("RELATED");
                            borrowerInfo.setIsSpouse(0);
                            if(oldRelationId == RelationValue.DIRECTLY_RELATED.value() || oldRelationId == RelationValue.INDIRECTLY_RELATED.value()){
                                //Update old relatedList;
                                int subIndex = borrowerInfo.getSubIndex();
                                relatedInfoViewList.set(subIndex, borrowerInfo);       //replace new value for old object list;
                            } else {
                                //Insert into new relatedList;
                                borrowerInfo.setSubIndex(relatedInfoViewList.size());
                                relatedInfoViewList.add(borrowerInfo);

                                //Remove from old list
                                if(oldRelationId == RelationValue.BORROWER.value()){
                                    //Remove from guarantor list
                                    borrowerInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for guarantor list
                                    reIndexCustomerList(ListCustomerName.BORROWER);
                                } else {
                                    //Remove from related list
                                    guarantorInfoViewList.remove(borrowerInfo.getSubIndex());
                                    //Re index for related list
                                    reIndexCustomerList(ListCustomerName.GUARANTOR);
                                }
                            }
                            customerInfoViewList.set(borrowerInfo.getListIndex(), borrowerInfo);
                        }
                    } else {
                        complete = false;
                    }
                } else {
                    complete = false;
                    messageHeader = "Save customer failed.";
                    message = "Invalid customer entity.";
                }
            }
            //complete = true;
        }

        context.addCallbackParam("functionComplete", complete);
        log.debug("customerInfoViewList after save : {}", customerInfoViewList);

        if(!complete){
            log.debug("onSaveCustomerInfo ::: duplicate personal id : {}", complete);
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void reIndexCustomerList(ListCustomerName listCustomerName){
        if(listCustomerName == ListCustomerName.BORROWER){
            List<CustomerInfoView> tmpBorrowerViewList = new ArrayList<CustomerInfoView>();
            for(CustomerInfoView customerInfoView : borrowerInfoViewList){
                tmpBorrowerViewList.add(customerInfoView);
            }
            int index = 0;
            borrowerInfoViewList.removeAll(borrowerInfoViewList);
            for(CustomerInfoView customerInfoView : tmpBorrowerViewList){
                customerInfoView.setSubIndex(index);
                borrowerInfoViewList.add(customerInfoView);
                index = index + 1;
            }
        } else if(listCustomerName == ListCustomerName.GUARANTOR){
            List<CustomerInfoView> tmpGuarantorViewList = new ArrayList<CustomerInfoView>();
            for(CustomerInfoView customerInfoView : guarantorInfoViewList){
                tmpGuarantorViewList.add(customerInfoView);
            }
            int index = 0;
            guarantorInfoViewList.removeAll(guarantorInfoViewList);
            for(CustomerInfoView customerInfoView : tmpGuarantorViewList){
                customerInfoView.setSubIndex(index);
                guarantorInfoViewList.add(customerInfoView);
                index = index + 1;
            }
        } else if(listCustomerName == ListCustomerName.RELATED){
            List<CustomerInfoView> tmpRelatedViewList = new ArrayList<CustomerInfoView>();
            for(CustomerInfoView customerInfoView : relatedInfoViewList){
                tmpRelatedViewList.add(customerInfoView);
            }
            int index = 0;
            relatedInfoViewList.removeAll(relatedInfoViewList);
            for(CustomerInfoView customerInfoView : tmpRelatedViewList){
                customerInfoView.setSubIndex(index);
                relatedInfoViewList.add(customerInfoView);
                index = index + 1;
            }
        } else if(listCustomerName == ListCustomerName.CUSTOMER){
            List<CustomerInfoView> tmpCustomerInfoViewList = new ArrayList<CustomerInfoView>();
            for(CustomerInfoView customerInfoView : customerInfoViewList){
                tmpCustomerInfoViewList.add(customerInfoView);
            }
            int index = 0;
            customerInfoViewList.removeAll(customerInfoViewList);
            for(CustomerInfoView customerInfoView : tmpCustomerInfoViewList){
                customerInfoView.setListIndex(index);
                customerInfoViewList.add(customerInfoView);
                index = index + 1;
            }
        }
    }

    public void onDeleteCustomerInfo() {
        log.debug("onDeleteCustomerInfo ::: selectCustomerInfoItem : {}", selectCustomerInfoItem);
        if(deleteCustomerInfoViewList == null){
            deleteCustomerInfoViewList = new ArrayList<CustomerInfoView>();
        }
        if(selectCustomerInfoItem.getIsSpouse() == 1){
            //remove spouse from old list
            CustomerInfoView customerInfoView = customerInfoViewList.get(selectCustomerInfoItem.getListIndex());
            CustomerInfoView blankCustomerInfo = new CustomerInfoView();
            blankCustomerInfo.reset();
            customerInfoView.setSpouse(blankCustomerInfo);
            customerInfoViewList.set(customerInfoView.getListIndex(), customerInfoView);

            if(selectCustomerInfoItem.getRelation().getId() == 1){
                borrowerInfoViewList.remove(selectCustomerInfoItem);
                reIndexCustomerList(ListCustomerName.BORROWER);
            } else if(selectCustomerInfoItem.getRelation().getId() == 2){
                guarantorInfoViewList.remove(selectCustomerInfoItem);
                reIndexCustomerList(ListCustomerName.GUARANTOR);
            } else if(selectCustomerInfoItem.getRelation().getId() == 3 || selectCustomerInfoItem.getRelation().getId() == 4){
                relatedInfoViewList.remove(selectCustomerInfoItem);
                reIndexCustomerList(ListCustomerName.RELATED);
            }
            //Add to delete for delete from database
            if(selectCustomerInfoItem.getId() != 0){
                deleteCustomerInfoViewList.add(selectCustomerInfoItem);
            }
        } else {
            CustomerInfoView customerInfoView = customerInfoViewList.get(selectCustomerInfoItem.getListIndex());
            //Add to delete for delete from database
            if(customerInfoView.getId() != 0){
                deleteCustomerInfoViewList.add(customerInfoView);
            }

            if(selectCustomerInfoItem.getRelation().getId() == RelationValue.BORROWER.value()){
                //Remove Borrower List
                borrowerInfoViewList.remove(selectCustomerInfoItem);
                reIndexCustomerList(ListCustomerName.BORROWER);
            } else if(selectCustomerInfoItem.getRelation().getId() == RelationValue.GUARANTOR.value()){
                //Remove Guarantor List
                guarantorInfoViewList.remove(selectCustomerInfoItem);
                reIndexCustomerList(ListCustomerName.GUARANTOR);
            } else if(selectCustomerInfoItem.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value() || selectCustomerInfoItem.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value()){
                //Remove Related List
                relatedInfoViewList.remove(selectCustomerInfoItem);
                reIndexCustomerList(ListCustomerName.RELATED);
            }

            customerInfoViewList.remove(customerInfoView);
            reIndexCustomerList(ListCustomerName.CUSTOMER);

            if(selectCustomerInfoItem.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){

                if(selectCustomerInfoItem.getMaritalStatus().getSpouseFlag() == 1){
                    if(selectCustomerInfoItem.getSpouse() != null){
                        CustomerInfoView spouse = selectCustomerInfoItem.getSpouse();
                        //Add to delete for delete from database
                        if(spouse.getId() != 0){
                            deleteCustomerInfoViewList.add(spouse);
                        }
                        //Remove Spouse List
                        if(spouse.getRelation().getId() == RelationValue.BORROWER.value()){
                            //Remove Borrower List
                            borrowerInfoViewList.remove(spouse);
                            reIndexCustomerList(ListCustomerName.BORROWER);
                        } else if(spouse.getRelation().getId() == RelationValue.GUARANTOR.value()){
                            //Remove Guarantor List
                            guarantorInfoViewList.remove(spouse);
                            reIndexCustomerList(ListCustomerName.GUARANTOR);
                        } else if(spouse.getRelation().getId() == RelationValue.DIRECTLY_RELATED.value() || spouse.getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value()){
                            //Remove Related List
                            relatedInfoViewList.remove(spouse);
                            reIndexCustomerList(ListCustomerName.RELATED);
                        }

                        customerInfoViewList.remove(spouse);
                    }
                }
            }
        }

        if(customerInfoViewList.size() == 0){
            caseBorrowerTypeId = 0;
        }
    }

    public void onDeleteBorrower(){
        log.debug("onDeleteBorrower ::: selectCustomerInfoItem : {}", selectCustomerInfoItem);
        borrowerInfoViewList.remove(selectCustomerInfoItem);
    }

    public void onDeleteGuarantor(){
        log.debug("onDeleteGuarantor ::: selectCustomerInfoItem : {}", selectCustomerInfoItem);
        guarantorInfoViewList.remove(selectCustomerInfoItem);
    }

    public void onDeleteRelatedPerson(){
        log.debug("onDeleteRelatedPerson ::: selectCustomerInfoItem : {}", selectCustomerInfoItem);
        relatedInfoViewList.remove(selectCustomerInfoItem);
    }

    public void onChangeDate(String borrowerType){
        log.debug("onChangeDate :::");
        if(borrowerType.equalsIgnoreCase("borrower")){
            log.debug("onChangeDate ::: compare date : {}", DateTimeUtil.compareDate(borrowerInfo.getDateOfBirth(), getCurrentDate()));
            if(DateTimeUtil.compareDate(borrowerInfo.getDateOfBirth(), getCurrentDate()) >= 1){
                borrowerInfo.setDateOfBirth(getCurrentDate());
            }
            log.debug("onChangeDate : borrowerInfo.dateOfBirth : {}", borrowerInfo.getDateOfBirth());
            if(borrowerInfo.getDateOfBirth() != null){
                int age = Util.calAge(borrowerInfo.getDateOfBirth());
                borrowerInfo.setAge(age);
            }
        } else if(borrowerType.equalsIgnoreCase("spouse")){
            log.debug("onChangeDate ::: compare date : {}", DateTimeUtil.compareDate(borrowerInfo.getSpouse().getDateOfBirth(), getCurrentDate()));
            if(DateTimeUtil.compareDate(borrowerInfo.getSpouse().getDateOfBirth(), getCurrentDate()) >= 1){
                borrowerInfo.setDateOfBirth(getCurrentDate());
            }
            log.debug("onChangeDate : spouseInfo.dateOfBirth : {}", borrowerInfo.getSpouse().getDateOfBirth());
            if(borrowerInfo.getSpouse().getDateOfBirth() != null){
                int age = Util.calAge(borrowerInfo.getSpouse().getDateOfBirth());
                borrowerInfo.getSpouse().setAge(age);
            }
        } else if(borrowerType.equalsIgnoreCase("juristic")){
            log.debug("onChangeDate ::: compare date : {}", DateTimeUtil.compareDate(borrowerInfo.getDateOfRegister(), getCurrentDate()));
            if(DateTimeUtil.compareDate(borrowerInfo.getDateOfRegister(), getCurrentDate()) >= 1){
                borrowerInfo.setDateOfBirth(getCurrentDate());
            }
            log.debug("onChangeDate : juristicInfo.dateOfRegister : {}", borrowerInfo.getDateOfRegister());
            if(borrowerInfo.getDateOfRegister() != null){
                int age = Util.calAge(borrowerInfo.getDateOfRegister());
                borrowerInfo.setAge(age);
            }
        }

    }

    /*private void enableCustomerForm(boolean enable){
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
    }*/

    public void onSearchCustomerInfo() {
        log.debug("onSearchCustomerInfo :::");
        log.debug("onSearchCustomerInfo ::: borrowerInfo : {}", borrowerInfo);
        CustomerInfoResultView customerInfoResultView = new CustomerInfoResultView();
        messageHeader = "Please wait...";
        message = "Waiting for search customer from RM";
        try{
            customerInfoResultView = prescreenBusinessControl.getCustomerInfoFromRM(borrowerInfo, user);
            log.debug("onSearchCustomerInfo ::: customerInfoResultView : {}", customerInfoResultView);
            if(customerInfoResultView.getActionResult().equals(ActionResult.SUCCESS)){
                log.debug("onSearchCustomerInfo ActionResult.SUCCESS");
                if(customerInfoResultView.getCustomerInfoView() != null){
                    int searchBy = borrowerInfo.getSearchBy();
                    String searchId = borrowerInfo.getSearchId();
                    log.debug("onSearchCustomerInfo ::: customer found : {}", customerInfoResultView.getCustomerInfoView());
                    //TODO get Customer Segment
                    log.debug("onSearchCustomerInfo ::: getServiceSegment from CustomerInfo");
                    CustomerInfoView tmpCustomerInfoView = customerInfoResultView.getCustomerInfoView();
                    tmpCustomerInfoView = customerInfoControl.getCustomerCreditInfo(tmpCustomerInfoView);
                    log.debug("onSearchCustomerInfo ::: getServiceSegment : {}", tmpCustomerInfoView.getServiceSegmentView());
                    //borrowerInfo = customerInfoResultView.getCustomerInfoView();
                    borrowerInfo = tmpCustomerInfoView;
                    borrowerInfo.setSearchBy(searchBy);
                    borrowerInfo.setSearchFromRM(1);
                    borrowerInfo.setSearchId(searchId);
                    //borrowerInfo = customerInfoSummaryControl.getInfoForExistingCustomer(borrowerInfo);

                    enableCustomerForm = true;
                    enableDocumentType = false;

                    enableCustomerEntity = false;
                    enableTMBCustomerId = false;
                    enableCitizenId = false;

                    enableSearchForm = false;

                    if(borrowerInfo.getCustomerEntity() != null && borrowerInfo.getCustomerEntity().getId() != 0){
                        onChangeProvinceBorrower();
                        onChangeDistrictBorrower();
                        if(borrowerInfo.getCustomerEntity().getId() == 1){
                            onChangeDate("borrower");
                            if(Util.isEmpty(borrowerInfo.getCitizenId())){
                                enableCitizenId = true;
                            }
                        } else if (borrowerInfo.getCustomerEntity().getId() == 2){
                            onChangeDate("juristic");
                            if(Util.isEmpty(borrowerInfo.getRegistrationId())){
                                enableCitizenId = true;
                            }
                        }
                        if(borrowerInfo.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                            if(borrowerInfo.getSpouse() != null){
                                /*if(Util.isEmpty(borrowerInfo.getSpouse().getCitizenId())){
                                    enableSpous
                                }*/
                                onChangeProvinceSpouse();
                                onChangeDistrictSpouse();
                                onChangeDate("spouse");
                            }
                        }
                    }

                    messageHeader = "Customer search complete.";
                    message = "Customer found.";
                }else{
                    log.debug("onSearchCustomerInfo ::: customer not found.");
                    if(borrowerInfo.getSearchBy() == 2){
                        //enableDocumentType = true;
                        //borrowerInfo.setTmbCustomerId(borrowerInfo.getSearchId());
                        log.debug("search by TMB Cus id not found");
                    }else{
                        //enableDocumentType = false;
                        borrowerInfo.setCitizenId(borrowerInfo.getSearchId());
                    }
                    enableCustomerForm = true;
                    enableDocumentType = false;
                    enableCustomerEntity = true;
                    enableTMBCustomerId = true;
                    enableCitizenId = true;
                    enableSearchForm = false;

                    CustomerEntity customerEntity = new CustomerEntity();
                    customerEntity.setId(0);
                    borrowerInfo.setCustomerEntity(customerEntity);

                    //Assign value after search not found


                    messageHeader = "Customer search complete.";
                    message = "Search customer not found.";
                }
            } else {
                if(borrowerInfo.getSearchBy() == 2){
                    //enableDocumentType = true;
                    //borrowerInfo.setTmbCustomerId(borrowerInfo.getSearchId());
                    log.debug("Search customer failed : search by 2");
                }else{
                    //enableDocumentType = false;
                    if(borrowerInfo.getSearchId() != null){
                        if(borrowerInfo.getDocumentType().getId() == 1 || borrowerInfo.getDocumentType().getId() == 2){
                            if(borrowerInfo.getSearchId().length() > 12){
                                borrowerInfo.setCitizenId(borrowerInfo.getSearchId().substring(0,13));
                            }
                        } else if(borrowerInfo.getDocumentType().getId() == 3){
                            if(borrowerInfo.getSearchId().length() > 12){
                                borrowerInfo.setRegistrationId(borrowerInfo.getSearchId().substring(0,13));
                            }
                        }

                    }
                }

                enableDocumentType = false;
                enableCustomerForm = true;
                enableCustomerEntity = true;
                enableTMBCustomerId = true;
                enableCitizenId = true;
                enableSearchForm = false;

                messageHeader = "Customer search complete.";
                message = customerInfoResultView.getReason();

            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }catch (Exception ex){
            if(borrowerInfo.getSearchBy() == 2){
                //enableDocumentType = true;
                borrowerInfo.setTmbCustomerId(borrowerInfo.getSearchId());
            }else{
                //enableDocumentType = false;
                if(borrowerInfo.getDocumentType() != null){
                    if(borrowerInfo.getDocumentType().getId() == 1 || borrowerInfo.getDocumentType().getId() == 2){
                        if(borrowerInfo.getSearchId().length() > 12){
                            borrowerInfo.setCitizenId(borrowerInfo.getSearchId().substring(0,13));
                        }
                    } else if(borrowerInfo.getDocumentType().getId() == 3){
                        if(borrowerInfo.getSearchId().length() > 12){
                            borrowerInfo.setRegistrationId(borrowerInfo.getSearchId().substring(0,13));
                        }
                    }
                }
            }

            enableDocumentType = false;
            enableCustomerForm = true;
            enableCustomerEntity = true;
            enableTMBCustomerId = true;
            enableCitizenId = true;
            enableSearchForm = false;
            log.debug("onSearchCustomerInfo Exception : {}", ex);
            messageHeader = "Customer search failed.";
            message = ex.getMessage();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

        log.debug("customerInfo : borrowerInfo : {}", borrowerInfo);

    }

    public void onChangeSearchBy(){
        log.debug("onChangeSearchBy ::: relationId");
        borrowerInfo.setSearchId("");
    }

    public void onChangeRelation(){
        //int relationId = borrowerInfo.getRelation().getId()
        int relationId = borrowerRelation.getId();
        log.debug("onChangeRelation ::: relationId : {}", relationId);

        if(caseBorrowerTypeId == 0){
            referenceList = referenceDAO.findReferenceByFlag(borrowerInfo.getCustomerEntity().getId(), borrowerInfo.getCustomerEntity().getId(), relationId, 1, 0);
        } else {
            referenceList = referenceDAO.findReferenceByFlag(borrowerInfo.getCustomerEntity().getId(), caseBorrowerTypeId, relationId, 1, 0);
        }

        if(relationId != 0 && borrowerInfo.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
            Relation tmpRelation = relationDAO.findById(relationId);
            spouseRelationList = prescreenBusinessControl.getRelationByStepAndBorrowerRelationId(stepId, BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, tmpRelation.getPriority());
            spouseRelation = new Relation();
            spouseReferenceList = new ArrayList<Reference>();
            spouseReference = new Reference();
            onChangeSpouseRelation();
        } else {
            spouseRelationList = new ArrayList<Relation>();
            spouseRelation = new Relation();
            spouseReferenceList = new ArrayList<Reference>();
            spouseReference = new Reference();
        }

        Relation tmp1 = new Relation();
        Relation tmp2 = new Relation();
        if(relationId == RelationValue.DIRECTLY_RELATED.value() || relationId == RelationValue.INDIRECTLY_RELATED.value()) {
            for(Relation relationSpouse : spouseRelationList){
                if(relationSpouse.getId() == 2){ // if main cus = 3 , 4 remove 2 only
                    tmp1 = relationSpouse;
                }
                if(relationId == RelationValue.INDIRECTLY_RELATED.value()){ // if main cus = 4 remove 3
                    if(relationSpouse.getId() == 3){
                        tmp2 = relationSpouse;
                    }
                }
            }
            spouseRelationList.remove(tmp1);

            if(relationId == RelationValue.INDIRECTLY_RELATED.value()){
                spouseRelationList.remove(tmp2);
            }
        }

        /*if(caseBorrowerTypeId == 0){
            referenceList = referenceDAO.findByCustomerEntityId(borrowerInfo.getCustomerEntity().getId(), borrowerInfo.getCustomerEntity().getId(), relationId);
         } else{
            referenceList = referenceDAO.findByCustomerEntityId(borrowerInfo.getCustomerEntity().getId(), caseBorrowerTypeId, relationId);
        }
        borrowerReference.setId(0);*/


    }

    public void onChangeSpouseRelation(){
        int relationId = spouseRelation.getId();
        if(caseBorrowerTypeId == 0){
            spouseReferenceList = referenceDAO.findReferenceByFlag(BorrowerType.INDIVIDUAL.value(), borrowerInfo.getCustomerEntity().getId(), relationId, 0, 1);
        } else {
            spouseReferenceList = referenceDAO.findReferenceByFlag(BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, relationId, 0, 1);
        }

        int referenceMainCusId = borrowerReference.getId();

        Reference referenceMain = referenceDAO.findById(referenceMainCusId);
        if (caseBorrowerTypeId == 2) { // Juristic as Borrower
//            if(customerInfoView.getSpouse().getRelation().getId() == RelationValue.INDIRECTLY_RELATED.value()){ // Bypass related
            if(relationId == RelationValue.INDIRECTLY_RELATED.value()){ // Bypass related
                int flagRelateType = 0;
                if (referenceMain.getRelationType() == 1) { // Committee
                    flagRelateType = 4; // remove 4 ( relation_type in db ) ( remove shareholder )
                } else if (referenceMain.getRelationType() == 2){ // Shareholder
                    flagRelateType = 3; // remove 3 ( relation_type in db ) ( remove committee )
                }

                if(flagRelateType == 0){
                    Reference tmp1 = new Reference();
                    Reference tmp2 = new Reference();
                    for(Reference r : spouseReferenceList){
                        if(r.getRelationType() == 3){
                            tmp1 = r;
                        }
                        if(r.getRelationType() == 4){
                            tmp2 = r;
                        }
                    }
                    spouseReferenceList.remove(tmp1);
                    spouseReferenceList.remove(tmp2);
                } else {
                    for(Reference r : spouseReferenceList){
                        if(r.getRelationType() == flagRelateType){
                            spouseReferenceList.remove(r);
                            return;
                        }
                    }
                }
            }
        }

        /*log.debug("onChangeSpouseRelation ::: relationId : {}", relationId);
        if(caseBorrowerTypeId == 0){
            spouseReferenceList = referenceDAO.findByCustomerEntityId(borrowerInfo.getCustomerEntity().getId(), borrowerInfo.getCustomerEntity().getId(), relationId);
        } else{
            spouseReferenceList = referenceDAO.findByCustomerEntityId(borrowerInfo.getCustomerEntity().getId(), caseBorrowerTypeId, relationId);
        }*/
    }

    public void onChangeMaritalStatus(){
        log.debug("onChangeMaritalStatus ::: Marriage Status : {}", borrowerInfo.getMaritalStatus().getId());
    }

    public void onChangeDocType(){
        log.debug("onDisableDocType ::: searchBy : {}", borrowerInfo.getSearchBy());
        log.debug("onDisableDocType ::: customerEntity : {}", borrowerInfo.getCustomerEntity());

        //*** Fixed Customer Entity BY DocumentType ***//
        CustomerEntity customerEntity = new CustomerEntity();
        for(DocumentType documentType : documentTypeList){
            if(documentType.getId() == borrowerInfo.getDocumentType().getId()){
                customerEntity.setId(documentType.getCustomerEntity().getId());
                relationList = prescreenBusinessControl.getRelationByStepId(stepId, documentType.getCustomerEntity().getId(), caseBorrowerTypeId, 0);
                if(caseBorrowerTypeId == 0){
                    referenceList = referenceDAO.findByCustomerEntityId(documentType.getCustomerEntity().getId(), documentType.getCustomerEntity().getId(), borrowerInfo.getRelation().getId());
                }else{
                    referenceList = referenceDAO.findByCustomerEntityId(documentType.getCustomerEntity().getId(), caseBorrowerTypeId, borrowerInfo.getRelation().getId());
                }
            }
        }

        borrowerInfo.setCustomerEntity(customerEntity);
        this.customerEntity = customerEntity;

        titleList = titleDAO.getListByCustomerEntity(borrowerInfo.getCustomerEntity());

        enableCustomerEntity = false;

        /*if(borrowerInfo.getSearchBy() == 1){
            if(borrowerInfo.getCustomerEntity() != null){
                if(borrowerInfo.getCustomerEntity().getId() == 1){
                    enableDocumentType = true;
                } else if (borrowerInfo.getCustomerEntity().getId() == 2){
                    enableDocumentType = false;
                } else {
                    enableDocumentType = true;
                }
            } else {
                enableDocumentType = true;
            }
        } else {
            enableDocumentType = false;
        }*/
    }

    /*// *** Calculate Age And Year In Business ***//*/
    public void onCalAge(String type){
        log.debug("onCalAge ::: DateOfBirth:{} ", borrowerInfo.getDateOfBirth());
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
        log.debug("onCalAge ::: DateOfBirth:{}", age);
    }*/

    // *** Function For BusinessInfoView *** //
    public void onAddBusinessInfo() {
        log.debug("onAddBusinessInfo ::: reset form");
        /*** Reset Form ***/
        modeForButton = ModeForButton.ADD;

        bizInfoView = new BizInfoDetailView();
        bizInfoView.reset();
    }

    public void onEditBusinessInfo() {
        log.debug("onEditBusinessInfo ::: selectBusinessInfo : {}", selectBizInfoItem);
        modeForButton = ModeForButton.EDIT;

        bizInfoView = new BizInfoDetailView();
        bizInfoView.setBizDesc(selectBizInfoItem.getBizDesc());
        bizInfoView.setBizGroup(selectBizInfoItem.getBizGroup());

        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(bizInfoView.getBizGroup());
    }

    public void onSaveBusinessInfo() {
        log.debug("onSaveBusinessInfo ::: modeForButton : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        /*** validate input ***/
        if(bizInfoView.getBizDesc().getId() != 0 && bizInfoView.getBizGroup().getId() != 0){
            if(modeForButton.equals(ModeForButton.ADD)) {
                BizInfoDetailView bizInfoDetailView = new BizInfoDetailView();
                log.debug("onSaveBusinessInformation ::: selectBusinessDescriptionID : {}", bizInfoView.getBizDesc().getId());
                log.debug("onSaveBusinessInformation ::: selectBusinessGroupID : {}", bizInfoView.getBizGroup().getId());

                BusinessGroup businessGroup = businessGroupDAO.findById(bizInfoView.getBizGroup().getId());
                BusinessDescription businessDesc = businessDescriptionDAO.findById(bizInfoView.getBizDesc().getId());

                bizInfoDetailView.setBizDesc(businessDesc);
                bizInfoDetailView.setBizGroup(businessGroup);

                bizInfoViewList.add(bizInfoDetailView);
                complete = true;
            } else if(modeForButton.equals(ModeForButton.EDIT)) {
                log.debug("onSaveBusinessInfo ::: rowIndex : {}", rowIndex);
                BizInfoDetailView bizInfoDetailView = new BizInfoDetailView();
                log.debug("onSaveBusinessInformation ::: selectBusinessDescriptionID : {}", bizInfoView.getBizDesc().getId());
                log.debug("onSaveBusinessInformation ::: selectBusinessGroupID : {}", bizInfoView.getBizGroup().getId());

                BusinessGroup businessGroup = businessGroupDAO.findById(bizInfoView.getBizGroup().getId());
                BusinessDescription businessDesc = businessDescriptionDAO.findById(bizInfoView.getBizDesc().getId());

                bizInfoDetailView.setBizDesc(businessDesc);
                bizInfoDetailView.setBizGroup(businessGroup);

                bizInfoViewList.set(rowIndex, bizInfoDetailView);

                complete = true;
            }
        }
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteBusinessInfo() {
        log.debug("onDeleteBusinessInformation ::: selectBizInfoItem : {}", selectBizInfoItem);
        bizInfoViewList.remove(selectBizInfoItem);
    }

    // *** Function For ProposeCollateral ***//
    public void onAddProposeCollateral() {
        log.debug("onAddProposeCollateral :::");
        //*** Reset form ***//
        log.debug("onAddProposeCollateral ::: Reset Form");
        modeForButton = ModeForButton.ADD;
        proposeCollateral = new PrescreenCollateralView();
        proposeCollateral.setPotentialCollateral(new PotentialCollateral());
    }

    public void onEditProposeCollateral(){
        modeForButton = ModeForButton.EDIT;
        log.debug("onEditProposeCollateral ::: selectProposeCollateralItem : {}", selectProposeCollateralItem);

        Cloner cloner = new Cloner();
        proposeCollateral = cloner.deepClone(selectProposeCollateralItem);
    }

    public void onSaveProposeCollateral(){
        log.debug("onSaveProposeCollateral ::: modeForButton : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        log.debug("onSaveProposeCollateral ::: proposeCollateral.getPotentialCollateral.getId() : {} ", proposeCollateral.getPotentialCollateral().getId());

        if(proposeCollateral.getPotentialCollateral().getId() != 0) {
            if(modeForButton.equals(ModeForButton.ADD)){
                PrescreenCollateralView collateral = new PrescreenCollateralView();
                PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(proposeCollateral.getPotentialCollateral().getId());
                collateral.setPotentialCollateral(potentialCollateral);

                proposePrescreenCollateralViewList.add(collateral);
                log.debug("onSaveProposeCollateral ::: modeForButton : {}, Completed.", modeForButton);
            } else if(modeForButton.equals(ModeForButton.EDIT)){
                PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(proposeCollateral.getPotentialCollateral().getId());
                proposeCollateral.setPotentialCollateral(potentialCollateral);
                proposePrescreenCollateralViewList.set(rowIndex, proposeCollateral);
                log.debug("onSaveProposeCollateral ::: modeForButton : {}, Completed.", modeForButton);
            } else {
                log.debug("onSaveProposeCollateral ::: Undefined modeForbutton !!");
            }
            complete = true;
        } else {
            complete = false;
            log.debug("onSaveProposeCollateral ::: validation failed.");
        }
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteProposeCollateral() {
        log.debug("onDeleteProposeCollateral ::: selectProposeCollateralItem : {}", selectProposeCollateralItem);
        proposePrescreenCollateralViewList.remove(selectProposeCollateralItem);

    }

    // *** Function for Prescreen Initial *** //
    public void onSavePrescreenInitial(){
        log.debug("onSavePrescreenInitial ::: prescreenView : {}", prescreenView);
        log.debug("onSavePrescreenInitial ::: facilityViewList : {}", facilityViewList);

        try{
            //TODO set Business Location
            prescreenView.setBusinessLocation(null);
            prescreenView.setBorrowingType(null);
            prescreenView.setReferredExperience(null);
            prescreenView.setRefinanceInBank(null);
            prescreenView.setRefinanceOutBank(null);
            prescreenBusinessControl.savePreScreenInitial(prescreenView, facilityViewList, customerInfoViewList, deleteCustomerInfoViewList, workCasePreScreenId, caseBorrowerTypeId, user);

            //TODO show messageBox success
            messageHeader = "Save PreScreen Success.";
            message = "Save PreScreen data success.";

            /*BaseController baseController = new BaseController();
            baseController.setAppHeaderView(inboxControl.getHeaderInformation(workCasePreScreenId, new Long(0)));*/
            //setAppHeaderView(inboxControl.getHeaderInformation(workCasePreScreenId, new Long(0)));

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

    public void onOpenAssignCheckerDialog(){
        bdmCheckerList = userDAO.findBDMChecker(user);
        prescreenView.setCheckerId("");
        prescreenView.setRemark("");
        log.debug("onOpenAssignDialog ::: bdmCheckerList size : {}", bdmCheckerList.size());
    }

    public void onAssignToChecker(){
        log.debug("onAssignToChecker ::: starting...");
        boolean complete = false;
        try {
            if(prescreenView.getCheckerId() != null && !prescreenView.getCheckerId().equals("")) {
                prescreenBusinessControl.assignChecker(workCasePreScreenId, queueName, prescreenView.getCheckerId(), ActionCode.ASSIGN_TO_CHECKER.getVal());
                complete = true;
                messageHeader = "Information.";
                message = "Assign to checker complete.";
                RequestContext.getCurrentInstance().execute("msgBoxRedirectDlg.show()");
            } else {
                complete = false;
            }
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);
            log.debug("onAssignToChecker ::: complete");
        } catch (Exception ex) {
            messageHeader = "Assign to checker failed.";
            message = "Assign to checker failed. Cause : " + Util.getMessageException(ex);
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", complete);

            log.error("onAssignToChecker ::: exception : {}", ex);
        }
    }

    public void onCancelCA(){
        try{
            //TODO : set reason and remark from screen.
            prescreenBusinessControl.cancelCase(workCasePreScreenId, queueName, ActionCode.CANCEL_CA.getVal(), "", "");
            messageHeader = "Information.";
            message = "Cancel CA Complete.";

            RequestContext.getCurrentInstance().execute("msgBoxRedirectDlg.show()");
        } catch (Exception ex){
            messageHeader = "Exception.";
            message = "Cancel CA Failed, cause : " + Util.getMessageException(ex);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    // *** Function for Prescreen Maker ***//
    public void onSavePrescreen(){
        //*** validate forms ***//
        log.debug("onSavePrescreen ::: prescreenView : {}", prescreenView);
        try{
            customerModifyFlag = customerModifyFlag + prescreenBusinessControl.checkModifyValue(prescreenView, workCasePreScreenId);
            boolean modifyFlag = prescreenBusinessControl.savePreScreen(prescreenView, facilityViewList, customerInfoViewList, deleteCustomerInfoViewList, bizInfoViewList, proposePrescreenCollateralViewList, workCasePreScreenId, customerModifyFlag, user);

            messageHeader = "Save PreScreen Success.";
            message = "Save PreScreen data success.";
            if(modifyFlag){
//                PrescreenResult prescreenResult = new PrescreenResult();
//                prescreenResult.onCreation();
//                prescreenResult.onRetrieveInterfaceInfo();
//                prescreenResult.onSave();
                message = message + "<br/><br/>Prescreen data has been modified.<br/>Please refresh interface info.";
            }

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


    // *** Event for DropDown *** //
    public void checkOnChangeProductGroup(){
        log.debug("checkOnChangeProductGroup ::: productGroup : {}", prescreenView.getProductGroup().getId());
        if(facilityViewList != null && facilityViewList.size() > 0){
            RequestContext.getCurrentInstance().execute("msgBoxFacilityDlg.show()");
        }else{
            onChangeProductGroup();
        }
    }

    public void onChangeProductGroup(){
        getProductProgramList();
        //*** Check if Facility added system must be remove all ***//
        log.debug("onChangeProductGroup :::: facilityViewList.size :::::::::::" + facilityViewList.size());
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
        log.debug("onCancelChangeProductGroup ::: previousValue : {}", previousProductGroupId);
        prescreenView.getProductGroup().setId(previousProductGroupId);
    }

    public void getProductProgramList(){
        log.debug("getProductProgramList ::: prescreenView.productgroup : {}", prescreenView.getProductGroup());
        ProductGroup productGroup = new ProductGroup();
        if(prescreenView.getProductGroup().getId() != 0){
            productGroup = productGroupDAO.findById(prescreenView.getProductGroup().getId());
        }

        //*** Get Product Program List from Product Group ***//
        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdProByPrdGroup(productGroup);
        if(prdGroupToPrdProgramList == null){
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        }
        log.debug("getProductProgramList ::: prdGroupToPrdProgramList size : {}", prdGroupToPrdProgramList.size());
    }

    public void onChangeProductProgram(){
        ProductProgram productProgram = productProgramDAO.findById(facility.getProductProgram().getId());
        log.debug("onChangeProductProgram :::: productProgram : {}", productProgram);

        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(productProgram);
        if(prdProgramToCreditTypeList == null){
            prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        }
        log.debug("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " +prdProgramToCreditTypeList.size());
    }

    public void onChangeBusinessGroup() {
        log.debug("onChangeBusinessGroup :::");
        log.debug("onChangeBusinessGroup ::: businessGroup.getId() : {}", bizInfoView.getBizGroup().getId());
        if(String.valueOf(bizInfoView.getBizGroup().getId()) != null && bizInfoView.getBizGroup().getId() != 0){
            BusinessGroup businessGroup = businessGroupDAO.findById(bizInfoView.getBizGroup().getId());
            log.debug("onChangeBusinessGroup :: businessGroup : {}", businessGroup);
            businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
            bizInfoView.setBizDesc(new BusinessDescription());
            bizInfoView.setBizGroup(businessGroup);
        } else {
            businessDescriptionList = new ArrayList<BusinessDescription>();
            bizInfoView.setBizDesc(new BusinessDescription());
            bizInfoView.setBizGroup(new BusinessGroup());
        }
        log.debug("onChangeBusinessGroupName ::: size is : {}", businessDescriptionList.size());
    }

    public void onChangeBusinessDesc(){
        log.debug("onChangeBusinessDesc :::");
        log.debug("onChangeBusinessDesc ::: businessDesc.getId() : {}", bizInfoView.getBizDesc().getId());
        if(String.valueOf(bizInfoView.getBizDesc().getId()) != null){
            BusinessDescription businessDescription = businessDescriptionDAO.findById(bizInfoView.getBizDesc().getId());
            bizInfoView.setBizDesc(businessDescription);
        }
        log.debug("onChangeBusinessDesc ::: businessDesc : {}", bizInfoView.getBizDesc());

    }

    /*


    public void onClearDlg() {

        dCollateralAmount = null;
        dCollateralTypeName = "";
        modeForCollateral = "add";
    }

    public void onAddExistCollateral() {
        log.debug("onAddExistCollateral {}");
        PrescreenCollateralView collExist = null;

        log.debug("dCollateralTypeName is ", existCollateralTypeName);
        log.debug("dCollateralAmount is ", existCollateralAmount);
        collExist = new PrescreenCollateralView();
        collExist.setCollateralTypeName(existCollateralTypeName);
        collExist.setCollateralAmount(existCollateralAmount);
        proposePrescreenCollateralViewList.add(collExist);

    }

    public void onSelectedExistCollateral(int row) {
        existCollateralAmount = proposePrescreenCollateralViewList.get(row).getCollateralAmount();
        existCollateralTypeName = proposePrescreenCollateralViewList.get(row).getCollateralTypeName();
        indexExistEdit = row;
        modeForExist = "edit";
    }

    public void onEditExistCollateral() {
        proposePrescreenCollateralViewList.get(indexExistEdit).setCollateralAmount(existCollateralAmount);
        proposePrescreenCollateralViewList.get(indexExistEdit).setCollateralTypeName(existCollateralTypeName);
        modeForExist = "add";
        existCollateralTypeName = "";
        existCollateralAmount = null;
    }

    public void onDeleteExistCollateral(int row) {
        proposePrescreenCollateralViewList.remove(row);
    }





    public void onChangeCreditType(){
        log.debug("onChangeCreditType ::: selectCreditType.Id() >>> " + selectCreditType.getId());
//        CreditType creditType  = creditTypeDao.findById(selectCreditType.getId());
//        log.debug("onChangeCreditType ::: selectCreditType.Name() >>> " + creditType.getName());
    }*/

    // open dialog
 /*   public void onSelectedFacility(int rowNumber) {
        modeForButton = "edit";
        rowIndex = rowNumber;
        log.debug("onSelectedFacility :::  rowNumber  :: " + rowNumber);
        log.debug("onSelectedFacility ::: facilityViewList.get(rowNumber).getFacilityName :: " + rowNumber + "  "
                + facilityViewList.get(rowNumber).getCreditType().getId());
        log.debug("onSelectedFacility ::: facilityViewList.get(rowNumber).getProductProgramName :: " + rowNumber + "  "
                + facilityViewList.get(rowNumber).getProductProgram().getId());
        log.debug("onSelectedFacility ::: facilityViewList.get(rowNumber).getRequestAmount :: " + rowNumber + "  "
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
        log.debug("onEditFacility :::::: selectProductProgram ::: "+selectProductProgram.getName());
        log.debug("onEditFacility :::::: selectCreditType ::: "+selectCreditType.getName());
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

    public List<Reference> getSpouseReferenceList() {
        return spouseReferenceList;
    }

    public void setSpouseReferenceList(List<Reference> spouseReferenceList) {
        this.spouseReferenceList = spouseReferenceList;
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

    public List<PrescreenCollateralView> getProposePrescreenCollateralViewList() {
        return proposePrescreenCollateralViewList;
    }

    public void setProposePrescreenCollateralViewList(List<PrescreenCollateralView> proposePrescreenCollateralViewList) {
        this.proposePrescreenCollateralViewList = proposePrescreenCollateralViewList;
    }

    public PrescreenCollateralView getProposeCollateral() {
        return proposeCollateral;
    }

    public void setProposeCollateral(PrescreenCollateralView proposeCollateral) {
        this.proposeCollateral = proposeCollateral;
    }

    public PrescreenCollateralView getSelectProposeCollateralItem() {
        return selectProposeCollateralItem;
    }

    public void setSelectProposeCollateralItem(PrescreenCollateralView selectProposeCollateralItem) {
        this.selectProposeCollateralItem = selectProposeCollateralItem;
    }

    public List<CustomerInfoView> getCustomerInfoViewList() {
        return customerInfoViewList;
    }

    public void setCustomerInfoViewList(List<CustomerInfoView> customerInfoViewList) {
        this.customerInfoViewList = customerInfoViewList;
    }

    /*public CustomerInfoView getSpouseInfo() {
        return spouseInfo;
    }

    public void setSpouseInfo(CustomerInfoView spouseInfo) {
        this.spouseInfo = spouseInfo;
    }*/

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

    public boolean isDisableAssignButton() {
        return disableAssignButton;
    }

    public void setDisableAssignButton(boolean disableAssignButton) {
        this.disableAssignButton = disableAssignButton;
    }

    public Date getCurrentDate() {
        //log.debug("++++++++++++++++++++++++++++++++++=== Current Date : {}", DateTimeUtil.getCurrentDateTH());
        return DateTime.now().toDate();
        //return DateTimeUtil.getCurrentDate();
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

    public boolean isEnableCustomerForm() {
        return enableCustomerForm;
    }

    public void setEnableCustomerForm(boolean enableCustomerForm) {
        this.enableCustomerForm = enableCustomerForm;
    }

    public boolean isEnableDocumentType() {
        return enableDocumentType;
    }

    public void setEnableDocumentType(boolean enableDocumentType) {
        this.enableDocumentType = enableDocumentType;
    }

    public boolean isEnableTMBCustomerId() {
        return enableTMBCustomerId;
    }

    public void setEnableTMBCustomerId(boolean enableTMBCustomerId) {
        this.enableTMBCustomerId = enableTMBCustomerId;
    }

    public boolean isEnableCitizenId() {
        return enableCitizenId;
    }

    public void setEnableCitizenId(boolean enableCitizenId) {
        this.enableCitizenId = enableCitizenId;
    }

    public boolean isEnableCustomerEntity() {
        return enableCustomerEntity;
    }

    public void setEnableCustomerEntity(boolean enableCustomerEntity) {
        this.enableCustomerEntity = enableCustomerEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public List<Province> getBusinessLocationList() {
        return businessLocationList;
    }

    public void setBusinessLocationList(List<Province> businessLocationList) {
        this.businessLocationList = businessLocationList;
    }

    public List<PotentialCollateral> getPotentialCollateralList() {
        return potentialCollateralList;
    }

    public void setPotentialCollateralList(List<PotentialCollateral> potentialCollateralList) {
        this.potentialCollateralList = potentialCollateralList;
    }

    public List<Province> getProvinceSpouseList() {
        return provinceSpouseList;
    }

    public void setProvinceSpouseList(List<Province> provinceSpouseList) {
        this.provinceSpouseList = provinceSpouseList;
    }

    public List<District> getDistrictSpouseList() {
        return districtSpouseList;
    }

    public void setDistrictSpouseList(List<District> districtSpouseList) {
        this.districtSpouseList = districtSpouseList;
    }

    public List<SubDistrict> getSubDistrictSpouseList() {
        return subDistrictSpouseList;
    }

    public void setSubDistrictSpouseList(List<SubDistrict> subDistrictSpouseList) {
        this.subDistrictSpouseList = subDistrictSpouseList;
    }

    public List<Relation> getSpouseRelationList() {
        return spouseRelationList;
    }

    public void setSpouseRelationList(List<Relation> spouseRelationList) {
        this.spouseRelationList = spouseRelationList;
    }

    public List<DocumentType> getSpouseDocumentTypeList() {
        return spouseDocumentTypeList;
    }

    public void setSpouseDocumentTypeList(List<DocumentType> spouseDocumentTypeList) {
        this.spouseDocumentTypeList = spouseDocumentTypeList;
    }

    public RadioValue getRadioValue() {
        return radioValue;
    }

    public void setRadioValue(RadioValue radioValue) {
        this.radioValue = radioValue;
    }

    public boolean isEnableSearchForm() {
        return enableSearchForm;
    }

    public void setEnableSearchForm(boolean enableSearchForm) {
        this.enableSearchForm = enableSearchForm;
    }

    public Relation getBorrowerRelation() {
        return borrowerRelation;
    }

    public void setBorrowerRelation(Relation borrowerRelation) {
        this.borrowerRelation = borrowerRelation;
    }

    public Relation getSpouseRelation() {
        return spouseRelation;
    }

    public void setSpouseRelation(Relation spouseRelation) {
        this.spouseRelation = spouseRelation;
    }

    public Reference getBorrowerReference() {
        return borrowerReference;
    }

    public void setBorrowerReference(Reference borrowerReference) {
        this.borrowerReference = borrowerReference;
    }

    public Reference getSpouseReference() {
        return spouseReference;
    }

    public void setSpouseReference(Reference spouseReference) {
        this.spouseReference = spouseReference;
    }

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }

    public List<Country> getCountryOfRegisterList() {
        return countryOfRegisterList;
    }

    public void setCountryOfRegisterList(List<Country> countryOfRegisterList) {
        this.countryOfRegisterList = countryOfRegisterList;
    }
}
