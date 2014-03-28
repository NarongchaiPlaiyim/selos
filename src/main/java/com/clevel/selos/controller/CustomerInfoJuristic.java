package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.RelationCustomerDAO;
import com.clevel.selos.dao.working.JuristicDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.AddressView;
import com.clevel.selos.model.view.CustomerInfoResultView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@ViewScoped
@ManagedBean(name = "custInfoSumJuris")
public class CustomerInfoJuristic implements Serializable {
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

    @Inject
    private DocumentTypeDAO documentTypeDAO;
    @Inject
    private RelationDAO relationDAO;
    @Inject
    private RelationCustomerDAO relationCustomerDAO;
    @Inject
    private ReferenceDAO referenceDAO;
    @Inject
    private TitleDAO titleDAO;
    @Inject
    private RaceDAO raceDAO;
    @Inject
    private NationalityDAO nationalityDAO;
    @Inject
    private EducationDAO educationDAO;
    @Inject
    private OccupationDAO occupationDAO;
    @Inject
    private BusinessTypeDAO businessTypeDAO;
    @Inject
    private MaritalStatusDAO maritalStatusDAO;
    @Inject
    private ProvinceDAO provinceDAO;
    @Inject
    private DistrictDAO districtDAO;
    @Inject
    private SubDistrictDAO subDistrictDAO;
    @Inject
    private CountryDAO countryDAO;
    @Inject
    private AddressTypeDAO addressTypeDAO;
    @Inject
    private KYCLevelDAO kycLevelDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private JuristicDAO juristicDAO;
    @Inject
    private IncomeSourceDAO incomeSourceDAO;

    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    ExSummaryControl exSummaryControl;

    //*** Drop down List ***//
    private List<DocumentType> documentTypeList;
    private List<Relation> relationList;
    private List<Reference> referenceList;
    private List<Title> titleThList;
    private List<Title> titleEnList;
    private List<BusinessType> businessTypeList;

    private List<Province> provinceForm1List;
    private List<District> districtForm1List;
    private List<SubDistrict> subDistrictForm1List;
    private List<Province> provinceForm2List;
    private List<District> districtForm2List;
    private List<SubDistrict> subDistrictForm2List;
    private List<Province> provinceForm3List;
    private List<District> districtForm3List;
    private List<SubDistrict> subDistrictForm3List;

    private List<Country> countryList;
    private List<AddressType> addressTypeList;
    private List<KYCLevel> kycLevelList;

    private List<IncomeSource> incomeSourceList;

    private List<String> yearList;

    private List<IncomeSource> incomeSourceList;

    //*** View ***//
    private CustomerInfoView customerInfoView;
    private CustomerInfoView customerInfoSearch;

    private String messageHeader;
    private String message;
    private String severity;

    //session
    private long workCaseId;

    private int caseBorrowerTypeId;

    // Boolean for search customer //
    private boolean enableDocumentType;
    private boolean enableCitizenId;
    private boolean enableSpouseDocumentType;
    private boolean enableSpouseCitizenId;

    //onEdit
    private CustomerInfoView selectEditIndividual;
    private boolean isEditBorrower;
    private int rowIndex;

    // Mandate boolean for change Reference
    private boolean reqRelation;
    private boolean reqReference;
    private boolean reqDocType;
    private boolean reqRegId;
    private boolean reqTitTh;
    private boolean reqStNameTh;
    private boolean reqTitEn;
    private boolean reqStNameEn;
    private boolean reqBizType;
    private boolean reqRegNo;
    private boolean reqRegPro;
    private boolean reqRegDis;
    private boolean reqRegSub;
    private boolean reqRegPos;
    private boolean reqRegCou;
    private boolean reqRegPhone;
    private boolean reqWorNo;
    private boolean reqWorPro;
    private boolean reqWorDis;
    private boolean reqWorSub;
    private boolean reqWorPos;
    private boolean reqWorCou;
    private boolean reqWorPhone;
    private boolean reqMobNo;
    private boolean reqAddMail;
    private boolean reqKYCLev;

    //param for map
    private long customerId;
    private boolean isFromSummaryParam;
    private boolean isFromIndividualParam;

    private String currentDateDDMMYY;

    private boolean isEditForm;

    private boolean enableAllFieldCus;

    private int relationId;
    private int referenceId;

    public CustomerInfoJuristic(){
    }

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if( (Long)session.getAttribute("workCaseId") != 0 && (Long)session.getAttribute("stepId") != 0){
            checkSession = true;
        }

        return checkSession;
    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(true);

        if(checkSession(session)){
            //TODO Check valid stepId
            log.debug("preRender ::: Check valid stepId");
        }else{
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation");

        HttpSession session = FacesUtil.getSession(true);
        if(checkSession(session)){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());

            initial();

            enableAllFieldCus = false;

            Flash flash = FacesUtil.getFlash();
            Map<String, Object> cusInfoParams = (Map<String, Object>) flash.get("cusInfoParams");
            if (cusInfoParams != null) {
                isFromSummaryParam = (Boolean) cusInfoParams.get("isFromSummaryParam");
                isFromIndividualParam = (Boolean) cusInfoParams.get("isFromIndividualParam");
                customerId = (Long) cusInfoParams.get("customerId");
                if(isFromIndividualParam){
                    customerInfoView = (CustomerInfoView) cusInfoParams.get("customerInfoView");
                    onEditJuristic();
                }
            }

            if(isFromSummaryParam){                         // go to edit from summary
                if(customerId != 0 && customerId != -1){
                    onEditJuristic();
                }
            }
        } else {
            //TODO Show messageBox
        }
    }

    public void initial(){
        isEditForm = false;
        customerInfoView = new CustomerInfoView();
        customerInfoView.reset();
        customerInfoView.setIndividualViewList(new ArrayList<CustomerInfoView>());
        customerInfoView.setCurrentAddress(null);
        customerInfoView.getRegisterAddress().setAddressTypeFlag(3);
        customerInfoView.getWorkAddress().setAddressTypeFlag(3);

        customerInfoSearch = new CustomerInfoView();
        customerInfoSearch.reset();

        caseBorrowerTypeId = customerInfoControl.getCaseBorrowerTypeIdByWorkCase(workCaseId);

        documentTypeList = documentTypeDAO.findByCustomerEntityId(2);
        relationList = relationCustomerDAO.getListRelationWithOutBorrower(BorrowerType.JURISTIC.value(),caseBorrowerTypeId,0);

        titleEnList = titleDAO.getListByCustomerEntityId(BorrowerType.JURISTIC.value());
        titleThList = titleDAO.getListByCustomerEntityId(BorrowerType.JURISTIC.value());
        businessTypeList = businessTypeDAO.findAll();

        provinceForm1List = provinceDAO.getListOrderByParameter("name");
        provinceForm2List = provinceDAO.getListOrderByParameter("name");

        countryList = countryDAO.findAll();

        incomeSourceList = incomeSourceDAO.findAll();

        referenceList = new ArrayList<Reference>();

        addressTypeList = addressTypeDAO.findByCustomerEntityId(BorrowerType.JURISTIC.value());
        kycLevelList = kycLevelDAO.findAll();

        yearList = DateTimeUtil.getPreviousFiftyYearTH();

        customerInfoView.setCollateralOwner(1);

        enableDocumentType = true;
        enableCitizenId = true;
        enableSpouseDocumentType = true;
        enableSpouseCitizenId = true;

        customerInfoView.setCapital(BigDecimal.ZERO);
        customerInfoView.setPaidCapital(BigDecimal.ZERO);
        customerInfoView.setSalesFromFinancialStmt(BigDecimal.ZERO);
        customerInfoView.setShareHolderRatio(BigDecimal.ZERO);
        customerInfoView.setTotalShare(BigDecimal.ZERO);

        customerInfoView.getSourceIncome().setId(211);
        customerInfoView.getCountryIncome().setId(211);
        customerInfoView.getRegisterAddress().getCountry().setId(211);
        customerInfoView.getWorkAddress().getCountry().setId(211);

        onChangeReference();
    }

    public void onEditJuristic(){
        if(customerId != 0 && customerId != -1){
            customerInfoView = customerInfoControl.getCustomerJuristicById(customerId);
        }

        if(customerInfoView.getId() != 0 || isFromIndividualParam){
            isEditForm = true;
        } else {
            isEditForm = false;
        }

        enableAllFieldCus = true;

        if(customerInfoView.getRelation() != null){
            relationId = customerInfoView.getRelation().getId();
        } else {
            relationId = 0;
        }

        if(customerInfoView.getReference() != null){
            referenceId = customerInfoView.getReference().getId();
        } else {
            referenceId = 0;
        }

        onChangeRelation();
        onChangeReference();
        onChangeProvinceEditForm1();
        onChangeDistrictEditForm1();
        onChangeProvinceEditForm2();
        onChangeDistrictEditForm2();

        if(customerInfoView.getSearchFromRM() == 1){
            enableDocumentType = false;
            enableCitizenId = false;
        }

        if(relationId == RelationValue.BORROWER.value()){
            isEditBorrower = true;
            relationList = relationCustomerDAO.getListRelation(BorrowerType.JURISTIC.value(), caseBorrowerTypeId, 0);
        }else{
            relationList = relationCustomerDAO.getListRelationWithOutBorrower(BorrowerType.JURISTIC.value(), caseBorrowerTypeId, 0);
        }

        if(customerInfoView.getRemoveIndividualIdList() == null){
            customerInfoView.setRemoveIndividualIdList(new ArrayList<Long>());
        }
    }

    public String onAddIndividual(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFromSummaryParam",false);
        map.put("isFromJuristicParam",true);
        map.put("isEditFromJuristic", false);
        map.put("customerId", -1L);
        map.put("customerInfoView", customerInfoView);
        FacesUtil.getFlash().put("cusInfoParams", map);
        return "customerInfoIndividual?faces-redirect=true";
    }

    public String onEditIndividual(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFromSummaryParam",false);
        map.put("isFromJuristicParam",true);
        map.put("isEditFromJuristic", true);
        map.put("customerId", -1L);
        map.put("customerInfoView", customerInfoView);
        map.put("rowIndex",rowIndex);
        map.put("individualView", selectEditIndividual);
        FacesUtil.getFlash().put("cusInfoParams", map);
        return "customerInfoIndividual?faces-redirect=true";
    }

    public void onChangeRelation(){
        referenceList = referenceDAO.findReferenceByFlag(BorrowerType.JURISTIC.value(), caseBorrowerTypeId, relationId, 1, 0);
    }

    public void onChangeProvinceForm1() {
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getRegisterAddress().getProvince().getCode());
            districtForm1List = districtDAO.getListByProvince(province);
            customerInfoView.getRegisterAddress().setDistrict(new District());
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }else{
            provinceForm1List = provinceDAO.getListOrderByParameter("name");
            districtForm1List = new ArrayList<District>();
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm1() {
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getRegisterAddress().getDistrict().getId());
            subDistrictForm1List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm1();
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm2() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getWorkAddress().getProvince().getCode());
            districtForm2List = districtDAO.getListByProvince(province);
            customerInfoView.getWorkAddress().setDistrict(new District());
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }else{
            provinceForm2List = provinceDAO.getListOrderByParameter("name");
            districtForm2List = new ArrayList<District>();
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm2() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getWorkAddress().getDistrict().getId());
            subDistrictForm2List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm2();
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceEditForm1(){
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getRegisterAddress().getProvince().getCode());
            districtForm1List = districtDAO.getListByProvince(province);
        }else{
            provinceForm1List = provinceDAO.getListOrderByParameter("name");
            districtForm1List = new ArrayList<District>();
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictEditForm1(){
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getRegisterAddress().getDistrict().getId());
            subDistrictForm1List = subDistrictDAO.getListByDistrict(district);
        }else{
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceEditForm2() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getWorkAddress().getProvince().getCode());
            districtForm2List = districtDAO.getListByProvince(province);
        }else{
            provinceForm2List = provinceDAO.getListOrderByParameter("name");
            districtForm2List = new ArrayList<District>();
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictEditForm2() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getWorkAddress().getDistrict().getId());
            subDistrictForm2List = subDistrictDAO.getListByDistrict(district);
        }else{
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }
    }

    public void onSearchCustomerInfo() {
        log.debug("onSearchCustomerInfo :::");
        log.debug("onSearchCustomerInfo ::: customerInfoView : {}", customerInfoSearch);
        CustomerInfoResultView customerInfoResultView;
        try{
            customerInfoResultView = customerInfoControl.getCustomerInfoFromRM(customerInfoSearch);
            log.debug("onSearchCustomerInfo ::: customerInfoResultView : {}", customerInfoResultView);
            enableAllFieldCus = true;
            isEditForm = true;
            if(customerInfoResultView.getActionResult().equals(ActionResult.SUCCESS)){
                log.debug("onSearchCustomerInfo ActionResult.SUCCESS");
                if(customerInfoResultView.getCustomerInfoView() != null){
                    log.debug("onSearchCustomerInfo ::: customer found : {}", customerInfoResultView.getCustomerInfoView());
                    customerInfoView = customerInfoResultView.getCustomerInfoView();

                    customerInfoView.getDocumentType().setId(customerInfoSearch.getDocumentType().getId());
                    customerInfoView.setSearchFromRM(1);
                    customerInfoView.setSearchBy(customerInfoSearch.getSearchBy());
                    customerInfoView.setSearchId(customerInfoSearch.getSearchId());
                    customerInfoView.setCollateralOwner(1);

                    //set default country
                    if(customerInfoView.getCitizenCountry() != null){
                        customerInfoView.getCitizenCountry().setId(211);
                    } else {
                        Country country = new Country();
                        country.setId(211);
                        customerInfoView.setCitizenCountry(country);
                    }

                    if(customerInfoView.getRegisterAddress() != null && customerInfoView.getWorkAddress() != null){
                        if(customerInfoControl.checkAddress(customerInfoView.getRegisterAddress(),customerInfoView.getWorkAddress()) == 1){
                            customerInfoView.getWorkAddress().setAddressTypeFlag(1);
                        } else {
                            customerInfoView.getWorkAddress().setAddressTypeFlag(3);
                        }
                        customerInfoView.getWorkAddress().setAddressTypeFlag(1);
                    }

                    if(customerInfoView.getWorkAddress() == null){
                        customerInfoView.setWorkAddress(new AddressView());
                        customerInfoView.getWorkAddress().setAddressTypeFlag(3);
                    }

                    enableDocumentType = false;
                    enableCitizenId = false;

                    messageHeader = "Information.";
                    message = "Search customer found.";
                    severity = "info";
                }else{
                    log.debug("onSearchCustomerInfo ::: customer not found.");
                    enableDocumentType = true;
                    enableCitizenId = true;

                    messageHeader = "Information.";
                    message = "Search customer not found.";
                    severity = "info";
                }
            } else {
                enableDocumentType = true;
                enableCitizenId = true;
                messageHeader = "Information.";
                message = customerInfoResultView.getReason();
                severity = "info";
            }

            customerInfoView.getDocumentType().setId(customerInfoSearch.getDocumentType().getId());
            if(customerInfoSearch.getSearchBy() == 1){
                customerInfoView.setRegistrationId(customerInfoSearch.getSearchId());
            }

            onChangeProvinceEditForm1();
            onChangeDistrictEditForm1();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }catch (Exception ex){
            enableDocumentType = true;
            enableCitizenId = true;
            customerInfoView.getDocumentType().setId(customerInfoSearch.getDocumentType().getId());
            customerInfoView.setRegistrationId(customerInfoSearch.getSearchId());
            log.error("onSearchCustomerInfo Exception : {}", ex);
            messageHeader = "Error.";
            message = ex.getMessage();
            severity = "alert";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onRefreshInterfaceInfo(){
        if(customerInfoView.getSearchFromRM() == 1){
            long cusId = customerInfoView.getId();
            int searchBy = customerInfoView.getSearchBy();
            String searchId = customerInfoView.getSearchId();
            int relId = 0;
            int refId = 0;
            if(relationId == RelationValue.BORROWER.value()){
                relId = relationId;
                refId = referenceId;
            }

            log.debug("refreshInterfaceInfo ::: customerInfoView : {}", customerInfoView);
            CustomerInfoResultView customerInfoResultView;
            try{
                customerInfoResultView = customerInfoControl.getCustomerInfoFromRM(customerInfoView);
                log.debug("refreshInterfaceInfo ::: customerInfoResultView : {}", customerInfoResultView);
                if(customerInfoResultView.getActionResult().equals(ActionResult.SUCCESS)){
                    log.debug("refreshInterfaceInfo ActionResult.SUCCESS");
                    if(customerInfoResultView.getCustomerInfoView() != null){
                        log.debug("refreshInterfaceInfo ::: customer found : {}", customerInfoResultView.getCustomerInfoView());
                        customerInfoView = customerInfoResultView.getCustomerInfoView();
                        customerInfoView.setId(cusId);
                        Relation relation = new Relation();
                        relation.setId(relId);
                        customerInfoView.setRelation(relation);
                        Reference reference = new Reference();
                        reference.setId(refId);
                        customerInfoView.setReference(reference);

                        //set default country
                        if(customerInfoView.getCitizenCountry() != null){
                            customerInfoView.getCitizenCountry().setId(211);
                        } else {
                            Country country = new Country();
                            country.setId(211);
                            customerInfoView.setCitizenCountry(country);
                        }

                        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getWorkAddress() != null){
                            if(customerInfoControl.checkAddress(customerInfoView.getRegisterAddress(),customerInfoView.getWorkAddress()) == 1){
                                customerInfoView.getWorkAddress().setAddressTypeFlag(1);
                            } else {
                                customerInfoView.getWorkAddress().setAddressTypeFlag(3);
                            }
                        }

                        messageHeader = "Information.";
                        message = "Refresh interface info complete.";
                        severity = "info";
                    }else{
                        log.debug("refreshInterfaceInfo ::: customer not found.");
                        messageHeader = "Information.";
                        message = "Refresh interface info failed.";
                        severity = "info";
                    }
                } else {
                    messageHeader = "Information.";
                    message = "Refresh interface info failed.";
                    severity = "info";
                }
                customerInfoView.setSearchFromRM(1);
                customerInfoView.setSearchBy(searchBy);
                customerInfoView.setSearchId(searchId);
                onChangeProvinceEditForm1();
                onChangeDistrictEditForm1();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }catch (Exception ex){
                log.error("refreshInterfaceInfo Exception : {}", ex);
                messageHeader = "Error.";
                message = ex.getMessage();
                severity = "alert";
                customerInfoView.setSearchFromRM(1);
                customerInfoView.setSearchBy(searchBy);
                customerInfoView.setSearchId(searchId);
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        } else {
            messageHeader = "Information.";
            message = "Cause this customer do not search from RM";
            severity = "info";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onChangeReference(){
       reqRelation = true;
       reqReference = true;
       reqDocType = true;
       reqRegId = true;
       reqTitTh = true;
       reqStNameTh = true;
    }

    public void onSave(){
        log.debug("onSave");
        //check registration
        Customer customer = juristicDAO.findCustomerByRegistrationIdAndWorkCase(customerInfoView.getRegistrationId(),workCaseId);
        if(customer != null && customer.getId() != 0){
            if(customer.getId() != customerInfoView.getId()){
                messageHeader = "Information.";
                message = "Registration Id is already exist";
                severity = "info";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                return;
            }
        }

        if(customerInfoView.getWorkAddress().getAddressTypeFlag() == 1){ //dup address 1 to address 2 - Address 1 is Regis , Address 2 is Work
            AddressView addressView = new AddressView(customerInfoView.getRegisterAddress(),customerInfoView.getWorkAddress().getId());
            addressView.setAddressTypeFlag(1);
            customerInfoView.setWorkAddress(addressView);
        }

        //check using customer in basic info
        if(customerInfoView.getId() != 0){
            boolean isExist = customerInfoControl.checkExistingAll(customerInfoView.getId());
            if(isExist){
                if(relationId == RelationValue.DIRECTLY_RELATED.value()
                        || relationId == RelationValue.INDIRECTLY_RELATED.value()){
                    messageHeader = "Information.";
                    message = msg.get("app.message.customer.existing.error");
                    severity = "info";
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    return;
                }
            }
        }

        Relation relation = new Relation();
        relation.setId(relationId);
        Reference reference = new Reference();
        reference.setId(referenceId);
        customerInfoView.setRelation(relation);
        customerInfoView.setReference(reference);

        try{
            customerId = customerInfoControl.saveCustomerInfoJuristic(customerInfoView, workCaseId);
            exSummaryControl.calForCustomerInfoJuristic(workCaseId);
            isFromSummaryParam = true;
            initial();
            onEditJuristic();
            messageHeader = "Information.";
            message = "Save Customer Juristic Data Success.";
            severity = "info";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            log.error("Exception :: {}",ex);
            messageHeader = "Error.";
            if(ex.getCause() != null){
                message = "Save Juristic Failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save Juristic Failed. Cause : " + ex.getMessage();
            }
            severity = "alert";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onChangeTitleTh(){
        customerInfoView.getTitleEn().setId(customerInfoView.getTitleTh().getId());
    }

    public void onChangeTitleEn(){
        customerInfoView.getTitleTh().setId(customerInfoView.getTitleEn().getId());
    }

    public void onDeleteIndividual(){
        try{
            //check individual using on basic info
            if(selectEditIndividual.getId() != 0){
                boolean isExist = customerInfoControl.checkExistingAll(selectEditIndividual.getId());
                if(isExist){
                    messageHeader = "Information.";
                    message = msg.get("app.message.customer.existing.error");
                    severity = "info";
                } else {
                    customerInfoView.getIndividualViewList().remove(selectEditIndividual);
                    customerInfoView.getRemoveIndividualIdList().add(selectEditIndividual.getId());
                    messageHeader = "Information.";
                    message = "Delete Customer Info Individual Success.";
                    severity = "info";
                }
            } else {
                customerInfoView.getIndividualViewList().remove(selectEditIndividual);
                messageHeader = "Information.";
                message = "Delete Customer Info Individual Success.";
                severity = "info";
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }catch (Exception ex){
            log.error("Exception :: {}",ex);
            messageHeader = "Error.";
            if(ex.getCause() != null){
                message = "Delete Customer Info Individual Failed. <br/><br/> Cause : " + ex.getCause().toString();
            } else {
                message = "Delete Customer Info Guarantor Failed. <br/><br/> Cause : " + ex.getMessage();
            }
            severity = "alert";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// Get Set ////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public CustomerInfoView getCustomerInfoView() {
        return customerInfoView;
    }

    public void setCustomerInfoView(CustomerInfoView customerInfoView) {
        this.customerInfoView = customerInfoView;
    }

    public List<DocumentType> getDocumentTypeList() {
        return documentTypeList;
    }

    public void setDocumentTypeList(List<DocumentType> documentTypeList) {
        this.documentTypeList = documentTypeList;
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

    public CustomerInfoView getCustomerInfoSearch() {
        return customerInfoSearch;
    }

    public void setCustomerInfoSearch(CustomerInfoView customerInfoSearch) {
        this.customerInfoSearch = customerInfoSearch;
    }

    public List<Title> getTitleThList() {
        return titleThList;
    }

    public void setTitleThList(List<Title> titleThList) {
        this.titleThList = titleThList;
    }

    public List<Title> getTitleEnList() {
        return titleEnList;
    }

    public void setTitleEnList(List<Title> titleEnList) {
        this.titleEnList = titleEnList;
    }

    public List<BusinessType> getBusinessTypeList() {
        return businessTypeList;
    }

    public void setBusinessTypeList(List<BusinessType> businessTypeList) {
        this.businessTypeList = businessTypeList;
    }

    public List<Province> getProvinceForm1List() {
        return provinceForm1List;
    }

    public void setProvinceForm1List(List<Province> provinceForm1List) {
        this.provinceForm1List = provinceForm1List;
    }

    public List<District> getDistrictForm1List() {
        return districtForm1List;
    }

    public void setDistrictForm1List(List<District> districtForm1List) {
        this.districtForm1List = districtForm1List;
    }

    public List<SubDistrict> getSubDistrictForm1List() {
        return subDistrictForm1List;
    }

    public void setSubDistrictForm1List(List<SubDistrict> subDistrictForm1List) {
        this.subDistrictForm1List = subDistrictForm1List;
    }

    public List<Province> getProvinceForm2List() {
        return provinceForm2List;
    }

    public void setProvinceForm2List(List<Province> provinceForm2List) {
        this.provinceForm2List = provinceForm2List;
    }

    public List<District> getDistrictForm2List() {
        return districtForm2List;
    }

    public void setDistrictForm2List(List<District> districtForm2List) {
        this.districtForm2List = districtForm2List;
    }

    public List<SubDistrict> getSubDistrictForm2List() {
        return subDistrictForm2List;
    }

    public void setSubDistrictForm2List(List<SubDistrict> subDistrictForm2List) {
        this.subDistrictForm2List = subDistrictForm2List;
    }

    public List<Province> getProvinceForm3List() {
        return provinceForm3List;
    }

    public void setProvinceForm3List(List<Province> provinceForm3List) {
        this.provinceForm3List = provinceForm3List;
    }

    public List<District> getDistrictForm3List() {
        return districtForm3List;
    }

    public void setDistrictForm3List(List<District> districtForm3List) {
        this.districtForm3List = districtForm3List;
    }

    public List<SubDistrict> getSubDistrictForm3List() {
        return subDistrictForm3List;
    }

    public void setSubDistrictForm3List(List<SubDistrict> subDistrictForm3List) {
        this.subDistrictForm3List = subDistrictForm3List;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<AddressType> getAddressTypeList() {
        return addressTypeList;
    }

    public void setAddressTypeList(List<AddressType> addressTypeList) {
        this.addressTypeList = addressTypeList;
    }

    public List<KYCLevel> getKycLevelList() {
        return kycLevelList;
    }

    public void setKycLevelList(List<KYCLevel> kycLevelList) {
        this.kycLevelList = kycLevelList;
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

    public boolean isEnableCitizenId() {
        return enableCitizenId;
    }

    public void setEnableCitizenId(boolean enableCitizenId) {
        this.enableCitizenId = enableCitizenId;
    }

    public boolean isEnableDocumentType() {
        return enableDocumentType;
    }

    public void setEnableDocumentType(boolean enableDocumentType) {
        this.enableDocumentType = enableDocumentType;
    }

    public boolean isEnableSpouseCitizenId() {
        return enableSpouseCitizenId;
    }

    public void setEnableSpouseCitizenId(boolean enableSpouseCitizenId) {
        this.enableSpouseCitizenId = enableSpouseCitizenId;
    }

    public boolean isEnableSpouseDocumentType() {
        return enableSpouseDocumentType;
    }

    public void setEnableSpouseDocumentType(boolean enableSpouseDocumentType) {
        this.enableSpouseDocumentType = enableSpouseDocumentType;
    }

    public List<String> getYearList() {
        return yearList;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
    }

    public CustomerInfoView getSelectEditIndividual() {
        return selectEditIndividual;
    }

    public void setSelectEditIndividual(CustomerInfoView selectEditIndividual) {
        this.selectEditIndividual = selectEditIndividual;
    }

    public boolean isEditBorrower() {
        return isEditBorrower;
    }

    public void setEditBorrower(boolean editBorrower) {
        isEditBorrower = editBorrower;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }

    public boolean isReqRelation() {
        return reqRelation;
    }

    public void setReqRelation(boolean reqRelation) {
        this.reqRelation = reqRelation;
    }

    public boolean isReqReference() {
        return reqReference;
    }

    public void setReqReference(boolean reqReference) {
        this.reqReference = reqReference;
    }

    public boolean isReqDocType() {
        return reqDocType;
    }

    public void setReqDocType(boolean reqDocType) {
        this.reqDocType = reqDocType;
    }

    public boolean isReqRegId() {
        return reqRegId;
    }

    public void setReqRegId(boolean reqRegId) {
        this.reqRegId = reqRegId;
    }

    public boolean isReqTitTh() {
        return reqTitTh;
    }

    public void setReqTitTh(boolean reqTitTh) {
        this.reqTitTh = reqTitTh;
    }

    public boolean isReqStNameTh() {
        return reqStNameTh;
    }

    public void setReqStNameTh(boolean reqStNameTh) {
        this.reqStNameTh = reqStNameTh;
    }

    public boolean isReqTitEn() {
        return reqTitEn;
    }

    public void setReqTitEn(boolean reqTitEn) {
        this.reqTitEn = reqTitEn;
    }

    public boolean isReqStNameEn() {
        return reqStNameEn;
    }

    public void setReqStNameEn(boolean reqStNameEn) {
        this.reqStNameEn = reqStNameEn;
    }

    public boolean isReqBizType() {
        return reqBizType;
    }

    public void setReqBizType(boolean reqBizType) {
        this.reqBizType = reqBizType;
    }

    public boolean isReqRegNo() {
        return reqRegNo;
    }

    public void setReqRegNo(boolean reqRegNo) {
        this.reqRegNo = reqRegNo;
    }

    public boolean isReqRegPro() {
        return reqRegPro;
    }

    public void setReqRegPro(boolean reqRegPro) {
        this.reqRegPro = reqRegPro;
    }

    public boolean isReqRegDis() {
        return reqRegDis;
    }

    public void setReqRegDis(boolean reqRegDis) {
        this.reqRegDis = reqRegDis;
    }

    public boolean isReqRegSub() {
        return reqRegSub;
    }

    public void setReqRegSub(boolean reqRegSub) {
        this.reqRegSub = reqRegSub;
    }

    public boolean isReqRegPos() {
        return reqRegPos;
    }

    public void setReqRegPos(boolean reqRegPos) {
        this.reqRegPos = reqRegPos;
    }

    public boolean isReqRegCou() {
        return reqRegCou;
    }

    public void setReqRegCou(boolean reqRegCou) {
        this.reqRegCou = reqRegCou;
    }

    public boolean isReqRegPhone() {
        return reqRegPhone;
    }

    public void setReqRegPhone(boolean reqRegPhone) {
        this.reqRegPhone = reqRegPhone;
    }

    public boolean isReqWorNo() {
        return reqWorNo;
    }

    public void setReqWorNo(boolean reqWorNo) {
        this.reqWorNo = reqWorNo;
    }

    public boolean isReqWorPro() {
        return reqWorPro;
    }

    public void setReqWorPro(boolean reqWorPro) {
        this.reqWorPro = reqWorPro;
    }

    public boolean isReqWorDis() {
        return reqWorDis;
    }

    public void setReqWorDis(boolean reqWorDis) {
        this.reqWorDis = reqWorDis;
    }

    public boolean isReqWorSub() {
        return reqWorSub;
    }

    public void setReqWorSub(boolean reqWorSub) {
        this.reqWorSub = reqWorSub;
    }

    public boolean isReqWorPos() {
        return reqWorPos;
    }

    public void setReqWorPos(boolean reqWorPos) {
        this.reqWorPos = reqWorPos;
    }

    public boolean isReqWorCou() {
        return reqWorCou;
    }

    public void setReqWorCou(boolean reqWorCou) {
        this.reqWorCou = reqWorCou;
    }

    public boolean isReqWorPhone() {
        return reqWorPhone;
    }

    public void setReqWorPhone(boolean reqWorPhone) {
        this.reqWorPhone = reqWorPhone;
    }

    public boolean isReqMobNo() {
        return reqMobNo;
    }

    public void setReqMobNo(boolean reqMobNo) {
        this.reqMobNo = reqMobNo;
    }

    public boolean isReqAddMail() {
        return reqAddMail;
    }

    public void setReqAddMail(boolean reqAddMail) {
        this.reqAddMail = reqAddMail;
    }

    public boolean isReqKYCLev() {
        return reqKYCLev;
    }

    public void setReqKYCLev(boolean reqKYCLev) {
        this.reqKYCLev = reqKYCLev;
    }

    public boolean isEditForm() {
        return isEditForm;
    }

    public void setEditForm(boolean editForm) {
        isEditForm = editForm;
    }

    public boolean isEnableAllFieldCus() {
        return enableAllFieldCus;
    }

    public void setEnableAllFieldCus(boolean enableAllFieldCus) {
        this.enableAllFieldCus = enableAllFieldCus;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public List<IncomeSource> getIncomeSourceList() {
        return incomeSourceList;
    }

    public void setIncomeSourceList(List<IncomeSource> incomeSourceList) {
        this.incomeSourceList = incomeSourceList;
    }
}
