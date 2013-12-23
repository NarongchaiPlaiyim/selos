package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.RelationCustomerDAO;
import com.clevel.selos.dao.working.JuristicDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.BorrowerType;
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
    private CustomerInfoControl customerInfoControl;

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

    private List<String> yearList;

    //*** View ***//
    private CustomerInfoView customerInfoView;
    private CustomerInfoView customerInfoSearch;

    private String messageHeader;
    private String message;

    private int addressFlagForm2;
    private int addressFlagForm3;

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
    private boolean reqIndRelation;
    private boolean reqIndReference;
    private boolean reqIndDocType;
    private boolean reqIndCitId;
    private boolean reqIndDOB;
    private boolean reqIndCOB;
    private boolean reqIndDID;
    private boolean reqIndDED;
    private boolean reqIndTitTh;
    private boolean reqIndStNameTh;
    private boolean reqIndLastNameTh;
    private boolean reqIndTitEn;
    private boolean reqIndStNameEn;
    private boolean reqIndLastNameEn;
    private boolean reqIndGender;
    private boolean reqIndRace;
    private boolean reqIndNation;
    private boolean reqIndEdu;
    private boolean reqIndOcc;
    private boolean reqIndBizType;
    private boolean reqIndAppInc;
    private boolean reqIndSouInc;
    private boolean reqIndCouSouInc;
    private boolean reqIndMarriage;
    private boolean reqIndCurNo;
    private boolean reqIndCurPro;
    private boolean reqIndCurDis;
    private boolean reqIndCurSub;
    private boolean reqIndCurPos;
    private boolean reqIndCurCou;
    private boolean reqIndCurPhone;
    private boolean reqIndRegNo;
    private boolean reqIndRegPro;
    private boolean reqIndRegDis;
    private boolean reqIndRegSub;
    private boolean reqIndRegPos;
    private boolean reqIndRegCou;
    private boolean reqIndRegPhone;
    private boolean reqIndWorNo;
    private boolean reqIndWorPro;
    private boolean reqIndWorDis;
    private boolean reqIndWorSub;
    private boolean reqIndWorPos;
    private boolean reqIndWorCou;
    private boolean reqIndWorPhone;
    private boolean reqIndAddMail;
    private boolean reqIndMobNo;
    private boolean reqIndKYCLev;

    private boolean reqSpoDocType;
    private boolean reqSpoCitId;
    private boolean reqSpoDOB;
    private boolean reqSpoCOB;
    private boolean reqSpoDID;
    private boolean reqSpoTitTh;
    private boolean reqSpoStNameTh;
    private boolean reqSpoLastNameTh;
    private boolean reqSpoTitEn;
    private boolean reqSpoStNameEn;
    private boolean reqSpoLastNameEn;
    private boolean reqSpoNation;
    private boolean reqSpoEdu;
    private boolean reqSpoOcc;
    private boolean reqSpoBizType;
    private boolean reqSpoSouInc;
    private boolean reqSpoCouSouInc;
    private boolean reqSpoCurNo;
    private boolean reqSpoCurPro;
    private boolean reqSpoCurDis;
    private boolean reqSpoCurSub;
    private boolean reqSpoCurPos;
    private boolean reqSpoCurCou;
    private boolean reqSpoCurPhone;
    private boolean reqSpoRegNo;
    private boolean reqSpoRegPro;
    private boolean reqSpoRegDis;
    private boolean reqSpoRegSub;
    private boolean reqSpoRegPos;
    private boolean reqSpoRegCou;
    private boolean reqSpoRegPhone;
    private boolean reqSpoWorNo;
    private boolean reqSpoWorPro;
    private boolean reqSpoWorDis;
    private boolean reqSpoWorSub;
    private boolean reqSpoWorPos;
    private boolean reqSpoWorCou;
    private boolean reqSpoWorPhone;
    private boolean reqSpoMobNo;
    private boolean reqSpoKYCLev;

    //param for map
    private long customerId;
    private boolean isFromSummaryParam;
    private boolean isFromIndividualParam;

    private String currentDateDDMMYY;

    public CustomerInfoJuristic(){
    }

    @PostConstruct
    public void onCreation() {
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }else{
            log.info("preRender ::: workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }

        onAddNewJuristic();

        Flash flash = FacesUtil.getFlash();
        Map<String, Object> cusInfoParams = (Map<String, Object>) flash.get("cusInfoParams");
//        Map<String, Object> cusInfoParams = (Map<String, Object>) session.getAttribute("cusInfoParams");
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
    }

    public void onAddNewJuristic(){
        customerInfoView = new CustomerInfoView();
        customerInfoView.reset();
        customerInfoView.setIndividualViewList(new ArrayList<CustomerInfoView>());

        customerInfoSearch = new CustomerInfoView();
        customerInfoSearch.reset();

        caseBorrowerTypeId = customerInfoControl.getCaseBorrowerTypeIdByWorkCase(workCaseId);

        documentTypeList = documentTypeDAO.findAll();
//        relationList = relationDAO.getOtherRelationList();
        relationList = relationCustomerDAO.getListRelationWithOutBorrower(BorrowerType.JURISTIC.value(),caseBorrowerTypeId,0);

        titleEnList = titleDAO.getListByCustomerEntityId(BorrowerType.JURISTIC.value());
        titleThList = titleDAO.getListByCustomerEntityId(BorrowerType.JURISTIC.value());
        businessTypeList = businessTypeDAO.findAll();

        provinceForm1List = provinceDAO.getListOrderByParameter("name");
        provinceForm2List = provinceDAO.getListOrderByParameter("name");

        countryList = countryDAO.findAll();

        referenceList = new ArrayList<Reference>();

        addressFlagForm2 = 1;

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
    }

    public void onEditJuristic(){
        if(customerId != 0 && customerId != -1){
            customerInfoView = customerInfoControl.getCustomerJuristicById(customerId);
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

        if(customerInfoView.getRelation().getId() == 1){
            isEditBorrower = true;
//            relationList = relationDAO.findAll();
            relationList = relationCustomerDAO.getListRelation(BorrowerType.JURISTIC.value(), caseBorrowerTypeId, 0);
        }else{
//            relationList = relationDAO.getOtherRelationList();
            relationList = relationCustomerDAO.getListRelationWithOutBorrower(BorrowerType.JURISTIC.value(),caseBorrowerTypeId,0);
        }
    }

    public String onAddIndividual(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFromSummaryParam",false);
        map.put("isFromJuristicParam",true);
        map.put("isEditFromJuristic", false);
        map.put("customerId", new Long(-1));
        map.put("customerInfoView", customerInfoView);
        HttpSession session = FacesUtil.getSession(false);
//        session.setAttribute("cusInfoParams", map);
        FacesUtil.getFlash().put("cusInfoParams", map);
        return "customerInfoIndividual?faces-redirect=true";
    }

    public String onEditIndividual(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFromSummaryParam",false);
        map.put("isFromJuristicParam",true);
        map.put("isEditFromJuristic", true);
        map.put("customerId", new Long(-1));
        map.put("customerInfoView", customerInfoView);
        map.put("rowIndex",rowIndex);
        map.put("individualView", selectEditIndividual);
        HttpSession session = FacesUtil.getSession(false);
//        session.setAttribute("cusInfoParams", map);
        FacesUtil.getFlash().put("cusInfoParams", map);
        return "customerInfoIndividual?faces-redirect=true";
    }

    public void onChangeRelation(){
//        referenceList = referenceDAO.findByCustomerEntityId(1, caseBorrowerTypeId, customerInfoView.getRelation().getId());
        referenceList = referenceDAO.findReferenceByFlag(BorrowerType.JURISTIC.value(), caseBorrowerTypeId, customerInfoView.getRelation().getId(), 1, 0);
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
            if(customerInfoResultView.getActionResult().equals(ActionResult.SUCCESS)){
                log.debug("onSearchCustomerInfo ActionResult.SUCCESS");
                if(customerInfoResultView.getCustomerInfoView() != null){
                    log.debug("onSearchCustomerInfo ::: customer found : {}", customerInfoResultView.getCustomerInfoView());
                    customerInfoView = customerInfoResultView.getCustomerInfoView();

                    customerInfoView.getDocumentType().setId(customerInfoSearch.getDocumentType().getId());
                    customerInfoView.setSearchFromRM(1);
                    customerInfoView.setSearchBy(customerInfoSearch.getSearchBy());
                    customerInfoView.setSearchId(customerInfoSearch.getSearchId());

                    //for spouse
//                    if(customerInfoView.getSpouse() != null && !customerInfoView.getSpouse().getCitizenId().equalsIgnoreCase("")){
//                        customerInfoView.getSpouse().setSearchBy(1);
//                        customerInfoView.getSpouse().setSearchId(customerInfoView.getSpouse().getCitizenId());
//                        try {
//                            CustomerInfoResultView cusSpouseResultView = customerInfoControl.getCustomerInfoFromRM(customerInfoSearch);
//                            if(cusSpouseResultView.getActionResult().equals(ActionResult.SUCCESS)){
//                                if(cusSpouseResultView.getCustomerInfoView() != null){
//                                    customerInfoView.setSpouse(customerInfoResultView.getCustomerInfoView());
//                                    enableSpouseDocumentType = false;
//                                    enableSpouseCitizenId = false;
//                                } else {
//                                    log.debug("onSearchCustomerInfo ( spouse ) ::: customer not found.");
//                                    enableSpouseDocumentType = true;
//                                    enableSpouseCitizenId = true;
//                                }
//                            }
//                        } catch (Exception ex) {
//                            enableSpouseDocumentType = true;
//                            enableSpouseCitizenId = true;
//                            log.debug("onSearchCustomerInfo ( spouse ) Exception : {}", ex);
//                        }
//                    }

                    enableDocumentType = false;
                    enableCitizenId = false;

                    messageHeader = "Customer search complete.";
                    message = "Customer found.";
                }else{
                    log.debug("onSearchCustomerInfo ::: customer not found.");
                    enableDocumentType = true;
                    enableCitizenId = true;

                    messageHeader = customerInfoResultView.getActionResult().toString();
                    message = "Search customer not found.";
                }
            } else {
                enableDocumentType = true;
                enableCitizenId = true;
                messageHeader = "Customer search failed.";
                message = customerInfoResultView.getReason();

            }
            onChangeProvinceEditForm1();
            onChangeDistrictEditForm1();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }catch (Exception ex){
            enableDocumentType = true;
            enableCitizenId = true;
            log.debug("onSearchCustomerInfo Exception : {}", ex);
            messageHeader = "Customer search failed.";
            message = ex.getMessage();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onRefreshInterfaceInfo(){
        if(customerInfoView.getSearchFromRM() == 1){
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

//                        if(customerInfoView.getSpouse() != null && customerInfoView.getSpouse().getSearchFromRM() == 1){
//                            CustomerInfoResultView cusSpouseResultView = customerInfoControl.getCustomerInfoFromRM(customerInfoView.getSpouse());
//                            if(cusSpouseResultView.getActionResult().equals(ActionResult.SUCCESS)){
//                                log.debug("refreshInterfaceInfo ActionResult.SUCCESS");
//                                if(cusSpouseResultView.getCustomerInfoView() != null){
//                                    log.debug("refreshInterfaceInfo ::: customer found : {}", customerInfoResultView.getCustomerInfoView());
//                                    customerInfoView.setSpouse(cusSpouseResultView.getCustomerInfoView());
//                                }
//                            }
//                        }

                        messageHeader = "Refresh Interface Info complete.";
                        message = "Customer found.";
                    }else{
                        log.debug("refreshInterfaceInfo ::: customer not found.");
                        messageHeader = customerInfoResultView.getActionResult().toString();
                        message = "Refresh Interface Info Customer not found.";
                    }
                } else {
                    messageHeader = "Refresh Interface Info Failed.";
                    message = customerInfoResultView.getReason();
                }
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }catch (Exception ex){
                log.debug("refreshInterfaceInfo Exception : {}", ex);
                messageHeader = "Refresh Interface Info Failed.";
                message = ex.getMessage();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        }
    }

    public void onChangeReference(){
        //todo:mandatory field
    }

    public void onSave(){
        //check registration
        Customer customer = juristicDAO.findCustomerByRegistrationIdAndWorkCase(customerInfoView.getRegistrationId(),workCaseId);
        if(customer != null && customer.getId() != 0){
            if(customer.getId() != customerInfoView.getId()){
                messageHeader = "Save Juristic Failed.";
                message = "Registration Id is already exist";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                return;
            }
        }

        if(addressFlagForm2 == 1){ //dup address 1 to address 2 - Address 1 is Regis , Address 2 is Work
            AddressView addressView = new AddressView(customerInfoView.getRegisterAddress(),customerInfoView.getWorkAddress().getId());
            customerInfoView.setWorkAddress(addressView);
        }

        try{
            customerInfoControl.saveCustomerInfoJuristic(customerInfoView, workCaseId);
            messageHeader = "Save Juristic Success.";
            message = "Save Juristic data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            messageHeader = "Save Juristic Failed.";
            if(ex.getCause() != null){
                message = "Save Juristic failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save Juristic failed. Cause : " + ex.getMessage();
            }
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
        customerInfoView.getIndividualViewList().remove(selectEditIndividual);
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    //Get Set
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

    public int getAddressFlagForm2() {
        return addressFlagForm2;
    }

    public void setAddressFlagForm2(int addressFlagForm2) {
        this.addressFlagForm2 = addressFlagForm2;
    }

    public int getAddressFlagForm3() {
        return addressFlagForm3;
    }

    public void setAddressFlagForm3(int addressFlagForm3) {
        this.addressFlagForm3 = addressFlagForm3;
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

    public boolean isReqIndRelation() {
        return reqIndRelation;
    }

    public void setReqIndRelation(boolean reqIndRelation) {
        this.reqIndRelation = reqIndRelation;
    }

    public boolean isReqIndReference() {
        return reqIndReference;
    }

    public void setReqIndReference(boolean reqIndReference) {
        this.reqIndReference = reqIndReference;
    }

    public boolean isReqIndDocType() {
        return reqIndDocType;
    }

    public void setReqIndDocType(boolean reqIndDocType) {
        this.reqIndDocType = reqIndDocType;
    }

    public boolean isReqIndCitId() {
        return reqIndCitId;
    }

    public void setReqIndCitId(boolean reqIndCitId) {
        this.reqIndCitId = reqIndCitId;
    }

    public boolean isReqIndDOB() {
        return reqIndDOB;
    }

    public void setReqIndDOB(boolean reqIndDOB) {
        this.reqIndDOB = reqIndDOB;
    }

    public boolean isReqIndCOB() {
        return reqIndCOB;
    }

    public void setReqIndCOB(boolean reqIndCOB) {
        this.reqIndCOB = reqIndCOB;
    }

    public boolean isReqIndDID() {
        return reqIndDID;
    }

    public void setReqIndDID(boolean reqIndDID) {
        this.reqIndDID = reqIndDID;
    }

    public boolean isReqIndDED() {
        return reqIndDED;
    }

    public void setReqIndDED(boolean reqIndDED) {
        this.reqIndDED = reqIndDED;
    }

    public boolean isReqIndTitTh() {
        return reqIndTitTh;
    }

    public void setReqIndTitTh(boolean reqIndTitTh) {
        this.reqIndTitTh = reqIndTitTh;
    }

    public boolean isReqIndStNameTh() {
        return reqIndStNameTh;
    }

    public void setReqIndStNameTh(boolean reqIndStNameTh) {
        this.reqIndStNameTh = reqIndStNameTh;
    }

    public boolean isReqIndLastNameTh() {
        return reqIndLastNameTh;
    }

    public void setReqIndLastNameTh(boolean reqIndLastNameTh) {
        this.reqIndLastNameTh = reqIndLastNameTh;
    }

    public boolean isReqIndTitEn() {
        return reqIndTitEn;
    }

    public void setReqIndTitEn(boolean reqIndTitEn) {
        this.reqIndTitEn = reqIndTitEn;
    }

    public boolean isReqIndStNameEn() {
        return reqIndStNameEn;
    }

    public void setReqIndStNameEn(boolean reqIndStNameEn) {
        this.reqIndStNameEn = reqIndStNameEn;
    }

    public boolean isReqIndLastNameEn() {
        return reqIndLastNameEn;
    }

    public void setReqIndLastNameEn(boolean reqIndLastNameEn) {
        this.reqIndLastNameEn = reqIndLastNameEn;
    }

    public boolean isReqIndGender() {
        return reqIndGender;
    }

    public void setReqIndGender(boolean reqIndGender) {
        this.reqIndGender = reqIndGender;
    }

    public boolean isReqIndRace() {
        return reqIndRace;
    }

    public void setReqIndRace(boolean reqIndRace) {
        this.reqIndRace = reqIndRace;
    }

    public boolean isReqIndNation() {
        return reqIndNation;
    }

    public void setReqIndNation(boolean reqIndNation) {
        this.reqIndNation = reqIndNation;
    }

    public boolean isReqIndEdu() {
        return reqIndEdu;
    }

    public void setReqIndEdu(boolean reqIndEdu) {
        this.reqIndEdu = reqIndEdu;
    }

    public boolean isReqIndOcc() {
        return reqIndOcc;
    }

    public void setReqIndOcc(boolean reqIndOcc) {
        this.reqIndOcc = reqIndOcc;
    }

    public boolean isReqIndBizType() {
        return reqIndBizType;
    }

    public void setReqIndBizType(boolean reqIndBizType) {
        this.reqIndBizType = reqIndBizType;
    }

    public boolean isReqIndAppInc() {
        return reqIndAppInc;
    }

    public void setReqIndAppInc(boolean reqIndAppInc) {
        this.reqIndAppInc = reqIndAppInc;
    }

    public boolean isReqIndSouInc() {
        return reqIndSouInc;
    }

    public void setReqIndSouInc(boolean reqIndSouInc) {
        this.reqIndSouInc = reqIndSouInc;
    }

    public boolean isReqIndCouSouInc() {
        return reqIndCouSouInc;
    }

    public void setReqIndCouSouInc(boolean reqIndCouSouInc) {
        this.reqIndCouSouInc = reqIndCouSouInc;
    }

    public boolean isReqIndMarriage() {
        return reqIndMarriage;
    }

    public void setReqIndMarriage(boolean reqIndMarriage) {
        this.reqIndMarriage = reqIndMarriage;
    }

    public boolean isReqIndCurNo() {
        return reqIndCurNo;
    }

    public void setReqIndCurNo(boolean reqIndCurNo) {
        this.reqIndCurNo = reqIndCurNo;
    }

    public boolean isReqIndCurPro() {
        return reqIndCurPro;
    }

    public void setReqIndCurPro(boolean reqIndCurPro) {
        this.reqIndCurPro = reqIndCurPro;
    }

    public boolean isReqIndCurDis() {
        return reqIndCurDis;
    }

    public void setReqIndCurDis(boolean reqIndCurDis) {
        this.reqIndCurDis = reqIndCurDis;
    }

    public boolean isReqIndCurSub() {
        return reqIndCurSub;
    }

    public void setReqIndCurSub(boolean reqIndCurSub) {
        this.reqIndCurSub = reqIndCurSub;
    }

    public boolean isReqIndCurPos() {
        return reqIndCurPos;
    }

    public void setReqIndCurPos(boolean reqIndCurPos) {
        this.reqIndCurPos = reqIndCurPos;
    }

    public boolean isReqIndCurCou() {
        return reqIndCurCou;
    }

    public void setReqIndCurCou(boolean reqIndCurCou) {
        this.reqIndCurCou = reqIndCurCou;
    }

    public boolean isReqIndCurPhone() {
        return reqIndCurPhone;
    }

    public void setReqIndCurPhone(boolean reqIndCurPhone) {
        this.reqIndCurPhone = reqIndCurPhone;
    }

    public boolean isReqIndRegNo() {
        return reqIndRegNo;
    }

    public void setReqIndRegNo(boolean reqIndRegNo) {
        this.reqIndRegNo = reqIndRegNo;
    }

    public boolean isReqIndRegPro() {
        return reqIndRegPro;
    }

    public void setReqIndRegPro(boolean reqIndRegPro) {
        this.reqIndRegPro = reqIndRegPro;
    }

    public boolean isReqIndRegDis() {
        return reqIndRegDis;
    }

    public void setReqIndRegDis(boolean reqIndRegDis) {
        this.reqIndRegDis = reqIndRegDis;
    }

    public boolean isReqIndRegSub() {
        return reqIndRegSub;
    }

    public void setReqIndRegSub(boolean reqIndRegSub) {
        this.reqIndRegSub = reqIndRegSub;
    }

    public boolean isReqIndRegPos() {
        return reqIndRegPos;
    }

    public void setReqIndRegPos(boolean reqIndRegPos) {
        this.reqIndRegPos = reqIndRegPos;
    }

    public boolean isReqIndRegCou() {
        return reqIndRegCou;
    }

    public void setReqIndRegCou(boolean reqIndRegCou) {
        this.reqIndRegCou = reqIndRegCou;
    }

    public boolean isReqIndRegPhone() {
        return reqIndRegPhone;
    }

    public void setReqIndRegPhone(boolean reqIndRegPhone) {
        this.reqIndRegPhone = reqIndRegPhone;
    }

    public boolean isReqIndWorNo() {
        return reqIndWorNo;
    }

    public void setReqIndWorNo(boolean reqIndWorNo) {
        this.reqIndWorNo = reqIndWorNo;
    }

    public boolean isReqIndWorPro() {
        return reqIndWorPro;
    }

    public void setReqIndWorPro(boolean reqIndWorPro) {
        this.reqIndWorPro = reqIndWorPro;
    }

    public boolean isReqIndWorDis() {
        return reqIndWorDis;
    }

    public void setReqIndWorDis(boolean reqIndWorDis) {
        this.reqIndWorDis = reqIndWorDis;
    }

    public boolean isReqIndWorSub() {
        return reqIndWorSub;
    }

    public void setReqIndWorSub(boolean reqIndWorSub) {
        this.reqIndWorSub = reqIndWorSub;
    }

    public boolean isReqIndWorPos() {
        return reqIndWorPos;
    }

    public void setReqIndWorPos(boolean reqIndWorPos) {
        this.reqIndWorPos = reqIndWorPos;
    }

    public boolean isReqIndWorCou() {
        return reqIndWorCou;
    }

    public void setReqIndWorCou(boolean reqIndWorCou) {
        this.reqIndWorCou = reqIndWorCou;
    }

    public boolean isReqIndWorPhone() {
        return reqIndWorPhone;
    }

    public void setReqIndWorPhone(boolean reqIndWorPhone) {
        this.reqIndWorPhone = reqIndWorPhone;
    }

    public boolean isReqIndAddMail() {
        return reqIndAddMail;
    }

    public void setReqIndAddMail(boolean reqIndAddMail) {
        this.reqIndAddMail = reqIndAddMail;
    }

    public boolean isReqIndMobNo() {
        return reqIndMobNo;
    }

    public void setReqIndMobNo(boolean reqIndMobNo) {
        this.reqIndMobNo = reqIndMobNo;
    }

    public boolean isReqIndKYCLev() {
        return reqIndKYCLev;
    }

    public void setReqIndKYCLev(boolean reqIndKYCLev) {
        this.reqIndKYCLev = reqIndKYCLev;
    }

    public boolean isReqSpoDocType() {
        return reqSpoDocType;
    }

    public void setReqSpoDocType(boolean reqSpoDocType) {
        this.reqSpoDocType = reqSpoDocType;
    }

    public boolean isReqSpoCitId() {
        return reqSpoCitId;
    }

    public void setReqSpoCitId(boolean reqSpoCitId) {
        this.reqSpoCitId = reqSpoCitId;
    }

    public boolean isReqSpoDOB() {
        return reqSpoDOB;
    }

    public void setReqSpoDOB(boolean reqSpoDOB) {
        this.reqSpoDOB = reqSpoDOB;
    }

    public boolean isReqSpoCOB() {
        return reqSpoCOB;
    }

    public void setReqSpoCOB(boolean reqSpoCOB) {
        this.reqSpoCOB = reqSpoCOB;
    }

    public boolean isReqSpoDID() {
        return reqSpoDID;
    }

    public void setReqSpoDID(boolean reqSpoDID) {
        this.reqSpoDID = reqSpoDID;
    }

    public boolean isReqSpoTitTh() {
        return reqSpoTitTh;
    }

    public void setReqSpoTitTh(boolean reqSpoTitTh) {
        this.reqSpoTitTh = reqSpoTitTh;
    }

    public boolean isReqSpoStNameTh() {
        return reqSpoStNameTh;
    }

    public void setReqSpoStNameTh(boolean reqSpoStNameTh) {
        this.reqSpoStNameTh = reqSpoStNameTh;
    }

    public boolean isReqSpoLastNameTh() {
        return reqSpoLastNameTh;
    }

    public void setReqSpoLastNameTh(boolean reqSpoLastNameTh) {
        this.reqSpoLastNameTh = reqSpoLastNameTh;
    }

    public boolean isReqSpoTitEn() {
        return reqSpoTitEn;
    }

    public void setReqSpoTitEn(boolean reqSpoTitEn) {
        this.reqSpoTitEn = reqSpoTitEn;
    }

    public boolean isReqSpoStNameEn() {
        return reqSpoStNameEn;
    }

    public void setReqSpoStNameEn(boolean reqSpoStNameEn) {
        this.reqSpoStNameEn = reqSpoStNameEn;
    }

    public boolean isReqSpoLastNameEn() {
        return reqSpoLastNameEn;
    }

    public void setReqSpoLastNameEn(boolean reqSpoLastNameEn) {
        this.reqSpoLastNameEn = reqSpoLastNameEn;
    }

    public boolean isReqSpoNation() {
        return reqSpoNation;
    }

    public void setReqSpoNation(boolean reqSpoNation) {
        this.reqSpoNation = reqSpoNation;
    }

    public boolean isReqSpoEdu() {
        return reqSpoEdu;
    }

    public void setReqSpoEdu(boolean reqSpoEdu) {
        this.reqSpoEdu = reqSpoEdu;
    }

    public boolean isReqSpoOcc() {
        return reqSpoOcc;
    }

    public void setReqSpoOcc(boolean reqSpoOcc) {
        this.reqSpoOcc = reqSpoOcc;
    }

    public boolean isReqSpoBizType() {
        return reqSpoBizType;
    }

    public void setReqSpoBizType(boolean reqSpoBizType) {
        this.reqSpoBizType = reqSpoBizType;
    }

    public boolean isReqSpoSouInc() {
        return reqSpoSouInc;
    }

    public void setReqSpoSouInc(boolean reqSpoSouInc) {
        this.reqSpoSouInc = reqSpoSouInc;
    }

    public boolean isReqSpoCouSouInc() {
        return reqSpoCouSouInc;
    }

    public void setReqSpoCouSouInc(boolean reqSpoCouSouInc) {
        this.reqSpoCouSouInc = reqSpoCouSouInc;
    }

    public boolean isReqSpoCurNo() {
        return reqSpoCurNo;
    }

    public void setReqSpoCurNo(boolean reqSpoCurNo) {
        this.reqSpoCurNo = reqSpoCurNo;
    }

    public boolean isReqSpoCurPro() {
        return reqSpoCurPro;
    }

    public void setReqSpoCurPro(boolean reqSpoCurPro) {
        this.reqSpoCurPro = reqSpoCurPro;
    }

    public boolean isReqSpoCurDis() {
        return reqSpoCurDis;
    }

    public void setReqSpoCurDis(boolean reqSpoCurDis) {
        this.reqSpoCurDis = reqSpoCurDis;
    }

    public boolean isReqSpoCurSub() {
        return reqSpoCurSub;
    }

    public void setReqSpoCurSub(boolean reqSpoCurSub) {
        this.reqSpoCurSub = reqSpoCurSub;
    }

    public boolean isReqSpoCurPos() {
        return reqSpoCurPos;
    }

    public void setReqSpoCurPos(boolean reqSpoCurPos) {
        this.reqSpoCurPos = reqSpoCurPos;
    }

    public boolean isReqSpoCurCou() {
        return reqSpoCurCou;
    }

    public void setReqSpoCurCou(boolean reqSpoCurCou) {
        this.reqSpoCurCou = reqSpoCurCou;
    }

    public boolean isReqSpoCurPhone() {
        return reqSpoCurPhone;
    }

    public void setReqSpoCurPhone(boolean reqSpoCurPhone) {
        this.reqSpoCurPhone = reqSpoCurPhone;
    }

    public boolean isReqSpoRegNo() {
        return reqSpoRegNo;
    }

    public void setReqSpoRegNo(boolean reqSpoRegNo) {
        this.reqSpoRegNo = reqSpoRegNo;
    }

    public boolean isReqSpoRegPro() {
        return reqSpoRegPro;
    }

    public void setReqSpoRegPro(boolean reqSpoRegPro) {
        this.reqSpoRegPro = reqSpoRegPro;
    }

    public boolean isReqSpoRegDis() {
        return reqSpoRegDis;
    }

    public void setReqSpoRegDis(boolean reqSpoRegDis) {
        this.reqSpoRegDis = reqSpoRegDis;
    }

    public boolean isReqSpoRegSub() {
        return reqSpoRegSub;
    }

    public void setReqSpoRegSub(boolean reqSpoRegSub) {
        this.reqSpoRegSub = reqSpoRegSub;
    }

    public boolean isReqSpoRegPos() {
        return reqSpoRegPos;
    }

    public void setReqSpoRegPos(boolean reqSpoRegPos) {
        this.reqSpoRegPos = reqSpoRegPos;
    }

    public boolean isReqSpoRegCou() {
        return reqSpoRegCou;
    }

    public void setReqSpoRegCou(boolean reqSpoRegCou) {
        this.reqSpoRegCou = reqSpoRegCou;
    }

    public boolean isReqSpoRegPhone() {
        return reqSpoRegPhone;
    }

    public void setReqSpoRegPhone(boolean reqSpoRegPhone) {
        this.reqSpoRegPhone = reqSpoRegPhone;
    }

    public boolean isReqSpoWorNo() {
        return reqSpoWorNo;
    }

    public void setReqSpoWorNo(boolean reqSpoWorNo) {
        this.reqSpoWorNo = reqSpoWorNo;
    }

    public boolean isReqSpoWorPro() {
        return reqSpoWorPro;
    }

    public void setReqSpoWorPro(boolean reqSpoWorPro) {
        this.reqSpoWorPro = reqSpoWorPro;
    }

    public boolean isReqSpoWorDis() {
        return reqSpoWorDis;
    }

    public void setReqSpoWorDis(boolean reqSpoWorDis) {
        this.reqSpoWorDis = reqSpoWorDis;
    }

    public boolean isReqSpoWorSub() {
        return reqSpoWorSub;
    }

    public void setReqSpoWorSub(boolean reqSpoWorSub) {
        this.reqSpoWorSub = reqSpoWorSub;
    }

    public boolean isReqSpoWorPos() {
        return reqSpoWorPos;
    }

    public void setReqSpoWorPos(boolean reqSpoWorPos) {
        this.reqSpoWorPos = reqSpoWorPos;
    }

    public boolean isReqSpoWorCou() {
        return reqSpoWorCou;
    }

    public void setReqSpoWorCou(boolean reqSpoWorCou) {
        this.reqSpoWorCou = reqSpoWorCou;
    }

    public boolean isReqSpoWorPhone() {
        return reqSpoWorPhone;
    }

    public void setReqSpoWorPhone(boolean reqSpoWorPhone) {
        this.reqSpoWorPhone = reqSpoWorPhone;
    }

    public boolean isReqSpoMobNo() {
        return reqSpoMobNo;
    }

    public void setReqSpoMobNo(boolean reqSpoMobNo) {
        this.reqSpoMobNo = reqSpoMobNo;
    }

    public boolean isReqSpoKYCLev() {
        return reqSpoKYCLev;
    }

    public void setReqSpoKYCLev(boolean reqSpoKYCLev) {
        this.reqSpoKYCLev = reqSpoKYCLev;
    }

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }
}
