package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.RelationCustomerDAO;
import com.clevel.selos.dao.working.IndividualDAO;
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
import com.clevel.selos.util.Util;
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
import java.util.*;

@ViewScoped
@ManagedBean(name = "custInfoSumIndi")
public class CustomerInfoIndividual implements Serializable {
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
    private IndividualDAO individualDAO;

    @Inject
    private CustomerInfoControl customerInfoControl;

    //*** Drop down List ***//
    private List<DocumentType> documentTypeList;
    private List<Relation> relationIndividualList;
    private List<Relation> relationSpouseList;
    private List<Reference> referenceIndividualList;
    private List<Reference> referenceSpouseList;
    private List<Title> titleThList;
    private List<Title> titleEnList;
    private List<Race> raceList;
    private List<Nationality> nationalityList;
    private List<Nationality> sndNationalityList;
    private List<Education> educationList;
    private List<Occupation> occupationList;
    private List<BusinessType> businessTypeList;
    private List<MaritalStatus> maritalStatusList;

    private List<Province> provinceForm1List;
    private List<District> districtForm1List;
    private List<SubDistrict> subDistrictForm1List;
    private List<Province> provinceForm2List;
    private List<District> districtForm2List;
    private List<SubDistrict> subDistrictForm2List;
    private List<Province> provinceForm3List;
    private List<District> districtForm3List;
    private List<SubDistrict> subDistrictForm3List;
    private List<Province> provinceForm4List;
    private List<District> districtForm4List;
    private List<SubDistrict> subDistrictForm4List;
    private List<Province> provinceForm5List;
    private List<District> districtForm5List;
    private List<SubDistrict> subDistrictForm5List;
    private List<Province> provinceForm6List;
    private List<District> districtForm6List;
    private List<SubDistrict> subDistrictForm6List;

    private List<Country> countryList;
    private List<AddressType> addressTypeList;
    private List<KYCLevel> kycLevelList;

    //*** View ***//
    private CustomerInfoView customerInfoView;
    private CustomerInfoView customerInfoSearch;
    private CustomerInfoView customerInfoSearchSpouse;

    private String messageHeader;
    private String message;

    private int addressFlagForm2;
    private int addressFlagForm3;
    private int addressFlagForm5;
    private int addressFlagForm6;

    //session
    private long workCaseId;

    //
    private int caseBorrowerTypeId;

    // Boolean for search customer //
    private boolean enableDocumentType;
    private boolean enableCitizenId;
    private boolean enableSpouseDocumentType;
    private boolean enableSpouseCitizenId;

    // maritalStatus
    private boolean maritalStatusFlag;

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

    private boolean reqSpoRelation;
    private boolean reqSpoReference;
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

    //onEdit , param for map
    private long customerId;
    private CustomerInfoView cusInfoJuristic;
    private boolean isFromJuristicParam;
    private boolean isFromSummaryParam;
    private boolean isEditFromJuristic;
    private int rowIndex;

    private boolean isEditBorrower;
    private boolean isEditSpouseBorrower;

    //mode
    private boolean isFromJuristic;

    private boolean isEditForm;
    private boolean isEditFormSpouse;

    //for date
    private String currentDateDDMMYY;

    public CustomerInfoIndividual(){
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

        //default value
        isFromJuristic = false;

        onAddNewIndividual();

        Flash flash = FacesUtil.getFlash();
        Map<String, Object> cusInfoParams = (Map<String, Object>) flash.get("cusInfoParams");
//        Map<String, Object> cusInfoParams = (Map<String, Object>) session.getAttribute("cusInfoParams");
        if (cusInfoParams != null) {
            isFromSummaryParam = (Boolean) cusInfoParams.get("isFromSummaryParam");
            isFromJuristicParam = (Boolean) cusInfoParams.get("isFromJuristicParam");
            isEditFromJuristic = (Boolean) cusInfoParams.get("isEditFromJuristic");
            customerId = (Long) cusInfoParams.get("customerId");
            cusInfoJuristic = (CustomerInfoView) cusInfoParams.get("customerInfoView");
            if(isEditFromJuristic){
                rowIndex = (Integer) cusInfoParams.get("rowIndex");
            }
        }

        //flag for show button
        if(isFromJuristicParam){                        // add new individual from juristic
            isFromJuristic = true;                      // for pass param return to juristic
        }else{
            isFromJuristic = false;                     // for save individual to DB
        }

        if(isFromSummaryParam){                         // go to edit from summary
            if(customerId != 0 && customerId != -1){
                onEditIndividual();
            }
        }

        if(isEditFromJuristic){                          // select edit individual from juristic
            if(cusInfoParams != null){
                CustomerInfoView cusView = (CustomerInfoView) cusInfoParams.get("individualView");
                customerInfoView = cusView;
                onEditIndividual();
            }
        }
    }

    public void onAddNewIndividual(){
        isEditForm = false;
        customerInfoView = new CustomerInfoView();
        customerInfoView.reset();
        customerInfoView.getSpouse().reset();

        customerInfoSearch = new CustomerInfoView();
        customerInfoSearch.reset();

        customerInfoSearchSpouse = new CustomerInfoView();
        customerInfoSearchSpouse.reset();

        documentTypeList = documentTypeDAO.findByCustomerEntityId(1);

        titleEnList = titleDAO.getListByCustomerEntityId(BorrowerType.INDIVIDUAL.value());
        titleThList = titleDAO.getListByCustomerEntityId(BorrowerType.INDIVIDUAL.value());
        raceList = raceDAO.findAll();
        nationalityList = nationalityDAO.findAll();
        sndNationalityList = nationalityDAO.findAll();
        educationList = educationDAO.findAll();
        occupationList = occupationDAO.findAll();
        businessTypeList = businessTypeDAO.findAll();
        maritalStatusList = maritalStatusDAO.findAll();

        provinceForm1List = provinceDAO.getListOrderByParameter("name");
        provinceForm2List = provinceDAO.getListOrderByParameter("name");
        provinceForm3List = provinceDAO.getListOrderByParameter("name");
        provinceForm4List = provinceDAO.getListOrderByParameter("name");
        provinceForm5List = provinceDAO.getListOrderByParameter("name");
        provinceForm6List = provinceDAO.getListOrderByParameter("name");

        countryList = countryDAO.findAll();

        caseBorrowerTypeId = customerInfoControl.getCaseBorrowerTypeIdByWorkCase(workCaseId);

        relationIndividualList = relationCustomerDAO.getListRelationWithOutBorrower(BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, 0);
        relationSpouseList = relationCustomerDAO.getListRelationWithOutBorrower(BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, 1);

        referenceIndividualList = new ArrayList<Reference>();
        referenceSpouseList = new ArrayList<Reference>();

        addressFlagForm2 = 1;
        addressFlagForm3 = 1;
        addressFlagForm5 = 1;
        addressFlagForm6 = 1;

        addressTypeList = addressTypeDAO.findByCustomerEntityId(BorrowerType.INDIVIDUAL.value());
        kycLevelList = kycLevelDAO.findAll();

        enableDocumentType = true;
        enableCitizenId = true;
        enableSpouseDocumentType = true;
        enableSpouseCitizenId = true;

        isEditBorrower = false;
        isEditSpouseBorrower = false;

        customerInfoView.setCollateralOwner(1);
        customerInfoView.getSpouse().setCollateralOwner(1);

        customerInfoView.getNationality().setId(1);
        customerInfoView.getCitizenCountry().setId(211);
        customerInfoView.getCurrentAddress().getCountry().setId(211);
        customerInfoView.getRegisterAddress().getCountry().setId(211);
        customerInfoView.getWorkAddress().getCountry().setId(211);
        customerInfoView.getSourceIncome().setId(211);

        customerInfoView.getSpouse().getNationality().setId(1);
        customerInfoView.getSpouse().getCitizenCountry().setId(211);
        customerInfoView.getSpouse().getCurrentAddress().getCountry().setId(211);
        customerInfoView.getSpouse().getRegisterAddress().getCountry().setId(211);
        customerInfoView.getSpouse().getWorkAddress().getCountry().setId(211);

        onChangeReference();
        onChangeReferenceSpouse();
        onChangeMaritalStatus();
    }

    public void onEditIndividual(){
        if(customerId != 0 && customerId != -1){
            customerInfoView = customerInfoControl.getCustomerIndividualById(customerId);
        }

        if(customerInfoView.getId() != 0){
            isEditForm = true;
        } else {
            isEditForm = false;
        }

        onChangeMaritalStatus();
        onChangeRelation();
        onChangeReference();
        onChangeProvinceEditForm1();
        onChangeDistrictEditForm1();
        onChangeProvinceEditForm2();
        onChangeDistrictEditForm2();
        onChangeProvinceEditForm3();
        onChangeDistrictEditForm3();

        if(customerInfoView.getMaritalStatus().getSpouseFlag() == 1){ // have spouse
            onChangeRelationSpouse();
            onChangeReferenceSpouse();
            onChangeProvinceEditForm4();
            onChangeDistrictEditForm4();
            onChangeProvinceEditForm5();
            onChangeDistrictEditForm5();
            onChangeProvinceEditForm6();
            onChangeDistrictEditForm6();
            isEditFormSpouse = true;
        } else {
            isEditFormSpouse = false;
        }

        if(customerInfoView.getSearchFromRM() == 1){
            enableDocumentType = false;
            enableCitizenId = false;
        }

        if(customerInfoView.getSpouse() != null && customerInfoView.getSpouse().getSearchFromRM() == 1){
            enableSpouseDocumentType = false;
            enableSpouseCitizenId = false;
        }

        if(customerInfoView.getRelation().getId() == 1){
            isEditBorrower = true;
            relationIndividualList = relationCustomerDAO.getListRelation(BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, 0);
        }else{
            relationIndividualList = relationCustomerDAO.getListRelationWithOutBorrower(BorrowerType.INDIVIDUAL.value(),caseBorrowerTypeId,0);
        }

        if(customerInfoView.getSpouse() != null && customerInfoView.getSpouse().getRelation().getId() == RelationValue.BORROWER.value()){
            isEditSpouseBorrower = true;
            relationSpouseList = relationCustomerDAO.getListRelation(BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, 1);
        }else{
            relationSpouseList = relationCustomerDAO.getListRelationWithOutBorrower(BorrowerType.INDIVIDUAL.value(),caseBorrowerTypeId,1);
        }
    }

    public void onChangeRelation(){
        referenceIndividualList = referenceDAO.findReferenceByFlag(BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, customerInfoView.getRelation().getId(), 1, 0);

        if(customerInfoView.getMaritalStatus().getSpouseFlag() != 0){
            onChangeRelationSpouse();
        }

//      - การแสดง Relationship ของ Spouse ไม่สามารถเลือกได้สูงกว่า Customer เช่น A = Guarantor, B ไม่สามารถเลือกเป็น Borrower ได้ แต่เลือก Guarantor ได้
        int relationId = customerInfoView.getRelation().getId();
        Relation tmp1 = new Relation();
        Relation tmp2 = new Relation();
        if(relationId == 3 || relationId == 4) {
            for(Relation relationSpouse : relationSpouseList){
                if(relationSpouse.getId() == 2){ // if main cus = 3 , 4 remove 2 only
                    tmp1 = relationSpouse;
                }
                if(relationId == 4){ // if main cus = 4 remove 3
                    if(relationSpouse.getId() == 3){
                        tmp2 = relationSpouse;
                    }
                }
            }
            relationSpouseList.remove(tmp1);
            if(relationId == 4){
                relationSpouseList.remove(tmp2);
            }
        } else {
            relationSpouseList = relationCustomerDAO.getListRelationWithOutBorrower(BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, 1);
        }
    }

    public void onChangeRelationSpouse(){
        referenceSpouseList = referenceDAO.findReferenceByFlag(BorrowerType.INDIVIDUAL.value(), caseBorrowerTypeId, customerInfoView.getSpouse().getRelation().getId(),0,1);

        //this condition for spouse
        Reference referenceMain = referenceDAO.findById(customerInfoView.getReference().getId());
        if (caseBorrowerTypeId == 2) { // Juristic as Borrower
            if(customerInfoView.getSpouse().getRelation().getId() == 4){ // Bypass related
                int flagRelateType = 0;
                if (referenceMain.getRelationType() == 1) { // Committee
                    flagRelateType = 4; // remove 4 ( relation_type in db ) ( remove shareholder )
                } else if (referenceMain.getRelationType() == 2){ // Shareholder
                    flagRelateType = 3; // remove 3 ( relation_type in db ) ( remove committee )
                }

                if(flagRelateType == 0){
                    Reference tmp1 = new Reference();
                    Reference tmp2 = new Reference();
                    for(Reference r : referenceSpouseList){
                        if(r.getRelationType() == 3){
                            tmp1 = r;
                        }
                        if(r.getRelationType() == 4){
                            tmp2 = r;
                        }
                    }
                    referenceSpouseList.remove(tmp1);
                    referenceSpouseList.remove(tmp2);
                } else {
                    for(Reference r : referenceSpouseList){
                        if(r.getRelationType() == flagRelateType){
                            referenceSpouseList.remove(r);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void onChangeProvinceForm1() {
        if(customerInfoView.getCurrentAddress() != null && customerInfoView.getCurrentAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getCurrentAddress().getProvince().getCode());
            districtForm1List = districtDAO.getListByProvince(province);
            customerInfoView.getCurrentAddress().setDistrict(new District());
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }else{
            provinceForm1List = provinceDAO.getListOrderByParameter("name");
            districtForm1List = new ArrayList<District>();
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm1() {
        if(customerInfoView.getCurrentAddress() != null && customerInfoView.getCurrentAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getCurrentAddress().getDistrict().getId());
            subDistrictForm1List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm1();
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm2() {
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getRegisterAddress().getProvince().getCode());
            districtForm2List = districtDAO.getListByProvince(province);
            customerInfoView.getRegisterAddress().setDistrict(new District());
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }else{
            provinceForm2List = provinceDAO.getListOrderByParameter("name");
            districtForm2List = new ArrayList<District>();
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm2() {
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getRegisterAddress().getDistrict().getId());
            subDistrictForm2List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm2();
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm3() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getWorkAddress().getProvince().getCode());
            districtForm3List = districtDAO.getListByProvince(province);
            customerInfoView.getWorkAddress().setDistrict(new District());
            subDistrictForm3List = new ArrayList<SubDistrict>();
        }else{
            provinceForm3List = provinceDAO.getListOrderByParameter("name");
            districtForm3List = new ArrayList<District>();
            subDistrictForm3List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm3() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getWorkAddress().getDistrict().getId());
            subDistrictForm3List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm3();
            subDistrictForm3List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm4() {
        if(customerInfoView.getSpouse().getCurrentAddress() != null && customerInfoView.getSpouse().getCurrentAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getSpouse().getCurrentAddress().getProvince().getCode());
            districtForm4List = districtDAO.getListByProvince(province);
            customerInfoView.getSpouse().getCurrentAddress().setDistrict(new District());
            subDistrictForm4List = new ArrayList<SubDistrict>();
        }else{
            provinceForm4List = provinceDAO.getListOrderByParameter("name");
            districtForm4List = new ArrayList<District>();
            subDistrictForm4List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm4() {
        if(customerInfoView.getSpouse().getCurrentAddress() != null && customerInfoView.getSpouse().getCurrentAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getSpouse().getCurrentAddress().getDistrict().getId());
            subDistrictForm4List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm4();
            subDistrictForm4List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm5() {
        if(customerInfoView.getSpouse().getRegisterAddress() != null && customerInfoView.getSpouse().getRegisterAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getSpouse().getRegisterAddress().getProvince().getCode());
            districtForm5List = districtDAO.getListByProvince(province);
            customerInfoView.getSpouse().getRegisterAddress().setDistrict(new District());
            subDistrictForm5List = new ArrayList<SubDistrict>();
        }else{
            provinceForm5List = provinceDAO.getListOrderByParameter("name");
            districtForm5List = new ArrayList<District>();
            subDistrictForm5List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm5() {
        if(customerInfoView.getSpouse().getRegisterAddress() != null && customerInfoView.getSpouse().getRegisterAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getSpouse().getRegisterAddress().getDistrict().getId());
            subDistrictForm5List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm5();
            subDistrictForm5List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm6() {
        if(customerInfoView.getSpouse().getWorkAddress() != null && customerInfoView.getSpouse().getWorkAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getSpouse().getWorkAddress().getProvince().getCode());
            districtForm6List = districtDAO.getListByProvince(province);
            customerInfoView.getSpouse().getWorkAddress().setDistrict(new District());
            subDistrictForm6List = new ArrayList<SubDistrict>();
        }else{
            provinceForm6List = provinceDAO.getListOrderByParameter("name");
            districtForm6List = new ArrayList<District>();
            subDistrictForm6List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm6() {
        if(customerInfoView.getSpouse().getWorkAddress() != null && customerInfoView.getSpouse().getWorkAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getSpouse().getWorkAddress().getDistrict().getId());
            subDistrictForm6List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm6();
            subDistrictForm6List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceEditForm1(){
        if(customerInfoView.getCurrentAddress() != null && customerInfoView.getCurrentAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getCurrentAddress().getProvince().getCode());
            districtForm1List = districtDAO.getListByProvince(province);
        }else{
            provinceForm1List = provinceDAO.getListOrderByParameter("name");
            districtForm1List = new ArrayList<District>();
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictEditForm1(){
        if(customerInfoView.getCurrentAddress() != null && customerInfoView.getCurrentAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getCurrentAddress().getDistrict().getId());
            subDistrictForm1List = subDistrictDAO.getListByDistrict(district);
        }else{
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceEditForm2() {
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getRegisterAddress().getProvince().getCode());
            districtForm2List = districtDAO.getListByProvince(province);
        }else{
            provinceForm2List = provinceDAO.getListOrderByParameter("name");
            districtForm2List = new ArrayList<District>();
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictEditForm2() {
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getRegisterAddress().getDistrict().getId());
            subDistrictForm2List = subDistrictDAO.getListByDistrict(district);
        }else{
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceEditForm3() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getWorkAddress().getProvince().getCode());
            districtForm3List = districtDAO.getListByProvince(province);
        }else{
            provinceForm3List = provinceDAO.getListOrderByParameter("name");
            districtForm3List = new ArrayList<District>();
            subDistrictForm3List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictEditForm3() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getWorkAddress().getDistrict().getId());
            subDistrictForm3List = subDistrictDAO.getListByDistrict(district);
        }else{
            subDistrictForm3List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceEditForm4() {
        if(customerInfoView.getSpouse().getCurrentAddress() != null && customerInfoView.getSpouse().getCurrentAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getSpouse().getCurrentAddress().getProvince().getCode());
            districtForm4List = districtDAO.getListByProvince(province);
        }else{
            provinceForm4List = provinceDAO.getListOrderByParameter("name");
            districtForm4List = new ArrayList<District>();
            subDistrictForm4List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictEditForm4() {
        if(customerInfoView.getSpouse().getCurrentAddress() != null && customerInfoView.getSpouse().getCurrentAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getSpouse().getCurrentAddress().getDistrict().getId());
            subDistrictForm4List = subDistrictDAO.getListByDistrict(district);
        }else{
            subDistrictForm4List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceEditForm5() {
        if(customerInfoView.getSpouse().getRegisterAddress() != null && customerInfoView.getSpouse().getRegisterAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getSpouse().getRegisterAddress().getProvince().getCode());
            districtForm5List = districtDAO.getListByProvince(province);
        }else{
            provinceForm5List = provinceDAO.getListOrderByParameter("name");
            districtForm5List = new ArrayList<District>();
            subDistrictForm5List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictEditForm5() {
        if(customerInfoView.getSpouse().getRegisterAddress() != null && customerInfoView.getSpouse().getRegisterAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getSpouse().getRegisterAddress().getDistrict().getId());
            subDistrictForm5List = subDistrictDAO.getListByDistrict(district);
        }else{
            subDistrictForm5List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceEditForm6() {
        if(customerInfoView.getSpouse().getWorkAddress() != null && customerInfoView.getSpouse().getWorkAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getSpouse().getWorkAddress().getProvince().getCode());
            districtForm6List = districtDAO.getListByProvince(province);
        }else{
            provinceForm6List = provinceDAO.getListOrderByParameter("name");
            districtForm6List = new ArrayList<District>();
            subDistrictForm6List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictEditForm6() {
        if(customerInfoView.getSpouse().getWorkAddress() != null && customerInfoView.getSpouse().getWorkAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getSpouse().getWorkAddress().getDistrict().getId());
            subDistrictForm6List = subDistrictDAO.getListByDistrict(district);
        }else{
            subDistrictForm6List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeMaritalStatus(){
        if(customerInfoView != null && customerInfoView.getMaritalStatus().getId() == 0){
            return;
        }

        MaritalStatus maritalStatus = maritalStatusDAO.findById(customerInfoView.getMaritalStatus().getId());
        if(maritalStatus != null && maritalStatus.getSpouseFlag() == 1){
            maritalStatusFlag = true;
        } else {
            maritalStatusFlag = false;
        }

        if(maritalStatusFlag){
            customerInfoView.getMaritalStatus().setSpouseFlag(1);
            if(customerInfoView.getSpouse() == null){
                CustomerInfoView cusView = new CustomerInfoView();
                cusView.reset();
                customerInfoView.setSpouse(cusView);
                onChangeRelation();
                isEditFormSpouse = false;
            }
        }
    }

    public void onChangeReference(){
        if(customerInfoView.getMaritalStatus().getSpouseFlag() != 0){
            onChangeRelationSpouse();
        }

        //Mandate only
        reqIndRelation = true;
        reqIndReference = true;
        reqIndDocType = true;
        reqIndCitId = true;
        reqIndTitTh = true;
        reqIndStNameTh = true;
        reqIndLastNameTh = true;
        reqIndGender = true;
        reqIndMarriage = true;

        //Test Mandate
//        reqIndDOB = true;
//        reqIndCOB = true;
//        reqIndDID = true;
//        reqIndDED = true;
//        reqIndTitEn = true;
//        reqIndStNameEn = true;
//        reqIndLastNameEn = true;
//        reqIndRace = true;
//        reqIndNation = true;
//        reqIndEdu = true;
//        reqIndOcc = true;
//        reqIndBizType = true;
//        reqIndAppInc = true;
//        reqIndSouInc = true;
//        reqIndCouSouInc = true;
//        reqIndCurNo = true;
//        reqIndCurPro = true;
//        reqIndCurDis = true;
//        reqIndCurSub = true;
//        reqIndCurPos = true;
//        reqIndCurCou = true;
//        reqIndCurPhone = true;
//        reqIndRegNo = true;
//        reqIndRegPro = true;
//        reqIndRegDis = true;
//        reqIndRegSub = true;
//        reqIndRegPos = true;
//        reqIndRegCou = true;
//        reqIndRegPhone = true;
//        reqIndWorNo = true;
//        reqIndWorPro = true;
//        reqIndWorDis = true;
//        reqIndWorSub = true;
//        reqIndWorPos = true;
//        reqIndWorCou = true;
//        reqIndWorPhone = true;
//        reqIndAddMail = true;
//        reqIndMobNo = true;
//        reqIndKYCLev = true;
    }

    public void onChangeReferenceSpouse(){
        reqSpoRelation = true;
        reqSpoReference = true;
        reqSpoDocType = true;
        reqSpoCitId = true;
        reqSpoTitTh = true;
        reqSpoStNameTh = true;
        reqSpoLastNameTh = true;

//        reqSpoDOB =
//        reqSpoCOB =
//        reqSpoDID =
//        reqSpoTitEn =
//        reqSpoStNameEn =
//        reqSpoLastNameEn =
//        reqSpoNation =
//        reqSpoEdu =
//        reqSpoOcc =
//        reqSpoBizType =
//        reqSpoSouInc =
//        reqSpoCouSouInc =
//        reqSpoCurNo =
//        reqSpoCurPro =
//        reqSpoCurDis =
//        reqSpoCurSub =
//        reqSpoCurPos =
//        reqSpoCurCou =
//        reqSpoCurPhone =
//        reqSpoRegNo =
//        reqSpoRegPro =
//        reqSpoRegDis =
//        reqSpoRegSub =
//        reqSpoRegPos =
//        reqSpoRegCou =
//        reqSpoRegPhone =
//        reqSpoWorNo =
//        reqSpoWorPro =
//        reqSpoWorDis =
//        reqSpoWorSub =
//        reqSpoWorPos =
//        reqSpoWorCou =
//        reqSpoWorPhone =
//        reqSpoMobNo =
//        reqSpoKYCLev =
    }

    public void onChangeDOB(){
        customerInfoView.setAge(Util.calAge(customerInfoView.getDateOfBirth()));
    }

    public void onChangeDOBSpouse(){
        customerInfoView.getSpouse().setAge(Util.calAge(customerInfoView.getSpouse().getDateOfBirth()));
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
                    customerInfoView.setCollateralOwner(1);
                    if(customerInfoView.getCurrentAddress() != null && customerInfoView.getRegisterAddress() != null){
                        if(customerInfoControl.checkAddress(customerInfoView.getCurrentAddress(),customerInfoView.getRegisterAddress()) == 1){
                            addressFlagForm2 = 1;
                        } else {
                            addressFlagForm2 = 3;
                        }
                    }
                    if(customerInfoView.getCurrentAddress() != null && customerInfoView.getWorkAddress() != null){
                        if(customerInfoControl.checkAddress(customerInfoView.getCurrentAddress(),customerInfoView.getWorkAddress()) == 1){
                            addressFlagForm3 = 1;
                        } else if(customerInfoView.getRegisterAddress() != null){
                            if(customerInfoControl.checkAddress(customerInfoView.getRegisterAddress(),customerInfoView.getWorkAddress()) == 1){
                                addressFlagForm3 = 2;
                            } else {
                                addressFlagForm3 = 3;
                            }
                        } else {
                            addressFlagForm3 = 3;
                        }
                    }

                    //for spouse
                    if(customerInfoView.getSpouse() != null && !customerInfoView.getSpouse().getCitizenId().equalsIgnoreCase("")){
                        try {
                            customerInfoView.getSpouse().setDocumentType(customerInfoSearch.getDocumentType());
                            customerInfoView.getSpouse().setSearchBy(customerInfoSearch.getSearchBy());
                            customerInfoView.getSpouse().setSearchId(customerInfoView.getSpouse().getCitizenId());
                            CustomerInfoResultView cusSpouseResultView = customerInfoControl.getCustomerInfoFromRM(customerInfoView.getSpouse());
                            if(cusSpouseResultView.getActionResult().equals(ActionResult.SUCCESS)){
                                log.debug("onSearchCustomerInfo ( spouse ) ActionResult.SUCCESS");
                                if(cusSpouseResultView.getCustomerInfoView() != null){
                                    log.debug("onSearchCustomerInfo ::: customer ( spouse ) found : {}", cusSpouseResultView.getCustomerInfoView());
                                    customerInfoView.setSpouse(cusSpouseResultView.getCustomerInfoView());
                                    customerInfoView.getSpouse().setSearchBy(customerInfoSearch.getSearchBy());
                                    customerInfoView.getSpouse().setSearchId(customerInfoResultView.getCustomerInfoView().getCitizenId());
                                    customerInfoView.getSpouse().getDocumentType().setId(customerInfoSearch.getDocumentType().getId());
                                    customerInfoView.getSpouse().setSearchFromRM(1);
                                    customerInfoView.getSpouse().setCollateralOwner(1);
                                    if(customerInfoView.getSpouse().getCurrentAddress() != null && customerInfoView.getSpouse().getRegisterAddress() != null){
                                        if(customerInfoControl.checkAddress(customerInfoView.getSpouse().getCurrentAddress(),customerInfoView.getSpouse().getRegisterAddress()) == 1){
                                            addressFlagForm5 = 1;
                                        } else {
                                            addressFlagForm5 = 3;
                                        }
                                    }
                                    if(customerInfoView.getSpouse().getCurrentAddress() != null && customerInfoView.getSpouse().getWorkAddress() != null){
                                        if(customerInfoControl.checkAddress(customerInfoView.getSpouse().getCurrentAddress(),customerInfoView.getSpouse().getWorkAddress()) == 1){
                                            addressFlagForm6 = 1;
                                        } else if(customerInfoView.getSpouse().getRegisterAddress() != null){
                                            if(customerInfoControl.checkAddress(customerInfoView.getSpouse().getRegisterAddress(),customerInfoView.getSpouse().getWorkAddress()) == 1){
                                                addressFlagForm6 = 2;
                                            } else {
                                                addressFlagForm6 = 3;
                                            }
                                        } else {
                                            addressFlagForm6 = 3;
                                        }
                                    }
                                    enableSpouseDocumentType = false;
                                    enableSpouseCitizenId = false;
                                } else {
                                    log.debug("onSearchCustomerInfo ( spouse ) ::: customer not found.");
                                    enableSpouseDocumentType = true;
                                    enableSpouseCitizenId = true;
                                }
                            }
                        } catch (Exception ex) {
                            enableSpouseDocumentType = true;
                            enableSpouseCitizenId = true;
                            log.debug("onSearchCustomerInfo ( spouse ) Exception : {}", ex);
                        }
                    }

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
            onChangeDOB();
            onChangeProvinceEditForm1();
            onChangeDistrictEditForm1();
            onChangeMaritalStatus();
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
        if(customerInfoView.getSearchFromRM() == 1){ // for individual && check spouse
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
                        if(customerInfoView.getCurrentAddress() != null && customerInfoView.getRegisterAddress() != null){
                            if(customerInfoControl.checkAddress(customerInfoView.getCurrentAddress(),customerInfoView.getRegisterAddress()) == 1){
                                addressFlagForm2 = 1;
                            } else {
                                addressFlagForm2 = 3;
                            }
                        }
                        if(customerInfoView.getCurrentAddress() != null && customerInfoView.getWorkAddress() != null){
                            if(customerInfoControl.checkAddress(customerInfoView.getCurrentAddress(),customerInfoView.getWorkAddress()) == 1){
                                addressFlagForm3 = 1;
                            } else if(customerInfoView.getRegisterAddress() != null){
                                if(customerInfoControl.checkAddress(customerInfoView.getRegisterAddress(),customerInfoView.getWorkAddress()) == 1){
                                    addressFlagForm3 = 2;
                                } else {
                                    addressFlagForm3 = 3;
                                }
                            } else {
                                addressFlagForm3 = 3;
                            }
                        }

                        if(customerInfoView.getSpouse() != null && customerInfoView.getSpouse().getSearchFromRM() == 1){
                            CustomerInfoResultView cusSpouseResultView = customerInfoControl.getCustomerInfoFromRM(customerInfoView.getSpouse());
                            if(cusSpouseResultView.getActionResult().equals(ActionResult.SUCCESS)){
                                log.debug("refreshInterfaceInfo ActionResult.SUCCESS");
                                if(cusSpouseResultView.getCustomerInfoView() != null){
                                    log.debug("refreshInterfaceInfo ::: customer found : {}", cusSpouseResultView.getCustomerInfoView());
                                    customerInfoView.setSpouse(cusSpouseResultView.getCustomerInfoView());
                                    if(customerInfoView.getSpouse().getCurrentAddress() != null && customerInfoView.getSpouse().getRegisterAddress() != null){
                                        if(customerInfoControl.checkAddress(customerInfoView.getSpouse().getCurrentAddress(),customerInfoView.getSpouse().getRegisterAddress()) == 1){
                                            addressFlagForm5 = 1;
                                        } else {
                                            addressFlagForm5 = 3;
                                        }
                                    }
                                    if(customerInfoView.getSpouse().getCurrentAddress() != null && customerInfoView.getSpouse().getWorkAddress() != null){
                                        if(customerInfoControl.checkAddress(customerInfoView.getSpouse().getCurrentAddress(),customerInfoView.getSpouse().getWorkAddress()) == 1){
                                            addressFlagForm6 = 1;
                                        } else if(customerInfoView.getSpouse().getRegisterAddress() != null){
                                            if(customerInfoControl.checkAddress(customerInfoView.getSpouse().getRegisterAddress(),customerInfoView.getSpouse().getWorkAddress()) == 1){
                                                addressFlagForm6 = 2;
                                            } else {
                                                addressFlagForm6 = 3;
                                            }
                                        } else {
                                            addressFlagForm6 = 3;
                                        }
                                    }
                                }
                            }
                        }
                        messageHeader = "Refresh Interface Info complete.";
                        message = "Customer found.";
                    } else {
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
        } else if(customerInfoView.getSpouse() != null && customerInfoView.getSpouse().getSearchFromRM() == 1) { // for only spouse
            try {
                CustomerInfoResultView cusSpouseResultView = customerInfoControl.getCustomerInfoFromRM(customerInfoView.getSpouse());
                if(cusSpouseResultView.getActionResult().equals(ActionResult.SUCCESS)){
                    log.debug("refreshInterfaceInfo ActionResult.SUCCESS");
                    if(cusSpouseResultView.getCustomerInfoView() != null){
                        log.debug("refreshInterfaceInfo ::: customer found : {}", cusSpouseResultView.getCustomerInfoView());
                        customerInfoView.setSpouse(cusSpouseResultView.getCustomerInfoView());

                        messageHeader = "Refresh Interface Info complete.";
                        message = "Customer found.";
                    } else {
                        log.debug("refreshInterfaceInfo ::: customer not found.");
                        messageHeader = cusSpouseResultView.getActionResult().toString();
                        message = "Refresh Interface Info Customer not found.";
                    }
                } else {
                    messageHeader = "Refresh Interface Info Failed.";
                    message = cusSpouseResultView.getReason();
                }
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }catch (Exception ex){
                log.debug("refreshInterfaceInfo Exception : {}", ex);
                messageHeader = "Refresh Interface Info Failed.";
                message = ex.getMessage();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        } else {
            messageHeader = "Refresh Interface Info Failed.";
            message = "Cause this customer do not search from RM";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onSearchSpouseCustomerInfo() {
        log.debug("onSearchSpouseCustomerInfo :::");
        log.debug("onSearchSpouseCustomerInfo ::: customerInfoSearchSpouse : {}", customerInfoSearchSpouse);
        CustomerInfoResultView customerInfoResultView;
        try{
            customerInfoResultView = customerInfoControl.getCustomerInfoFromRM(customerInfoSearchSpouse);
            log.debug("onSearchSpouseCustomerInfo ::: customerInfoResultView : {}", customerInfoResultView);
            if(customerInfoResultView.getActionResult().equals(ActionResult.SUCCESS)){
                log.debug("onSearchSpouseCustomerInfo ActionResult.SUCCESS");
                if(customerInfoResultView.getCustomerInfoView() != null){
                    log.debug("onSearchSpouseCustomerInfo ::: customer found : {}", customerInfoResultView.getCustomerInfoView());
                    customerInfoView.setSpouse(customerInfoResultView.getCustomerInfoView());

                    if(customerInfoView.getSpouse() != null){
                        customerInfoView.getSpouse().getDocumentType().setId(customerInfoSearchSpouse.getDocumentType().getId());
                        customerInfoView.getSpouse().setSearchFromRM(1);
                        customerInfoView.getSpouse().setSearchBy(customerInfoSearchSpouse.getSearchBy());
                        customerInfoView.getSpouse().setSearchId(customerInfoSearchSpouse.getSearchId());
                    }else{
                        CustomerInfoView cusView = new CustomerInfoView();
                        cusView.reset();
                        customerInfoView.setSpouse(cusView);
                        customerInfoView.getSpouse().getDocumentType().setId(customerInfoSearchSpouse.getDocumentType().getId());
                        customerInfoView.getSpouse().setSearchFromRM(1);
                        customerInfoView.getSpouse().setSearchBy(customerInfoSearchSpouse.getSearchBy());
                        customerInfoView.getSpouse().setSearchId(customerInfoSearchSpouse.getSearchId());
                    }

                    enableDocumentType = false;
                    enableCitizenId = false;

                    messageHeader = "Customer search complete.";
                    message = "Customer found.";
                }else{
                    log.debug("onSearchSpouseCustomerInfo ::: customer not found.");
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
            onChangeDOBSpouse();
            onChangeProvinceEditForm4();
            onChangeDistrictEditForm4();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }catch (Exception ex){
            enableDocumentType = true;
            enableCitizenId = true;
            log.debug("onSearchSpouseCustomerInfo Exception : {}", ex);
            messageHeader = "Customer search failed.";
            message = ex.getMessage();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onSave(){
        log.debug("onSave");
        //check citizen id
        if(customerInfoView.getSpouse() != null){
            if(customerInfoView.getCitizenId().equalsIgnoreCase(customerInfoView.getSpouse().getCitizenId())){
                messageHeader = "Save Individual Failed.";
                message = "Citizen Id is already exist";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                return;
            }
            Customer customer = individualDAO.findCustomerByCitizenIdAndWorkCase(customerInfoView.getSpouse().getCitizenId(),workCaseId);
            if(customer != null && customer.getId() != 0){
                if(customer.getId() != customerInfoView.getSpouse().getId()){
                    messageHeader = "Save Individual Failed.";
                    message = "Citizen Id is already exist";
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    return;
                }
            }
        }

        Customer customer = individualDAO.findCustomerByCitizenIdAndWorkCase(customerInfoView.getCitizenId(),workCaseId);
        if(customer != null && customer.getId() != 0){
            if(customer.getId() != customerInfoView.getId()){
                messageHeader = "Save Individual Failed.";
                message = "Citizen Id is already exist";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                return;
            }
        }

        if(addressFlagForm2 == 1){ //dup address 1 to address 2
            AddressView addressView = new AddressView(customerInfoView.getCurrentAddress(),customerInfoView.getRegisterAddress().getId());
            customerInfoView.setRegisterAddress(addressView);
        }

        if(addressFlagForm3 == 1){
            AddressView addressView = new AddressView(customerInfoView.getCurrentAddress(),customerInfoView.getWorkAddress().getId());
            customerInfoView.setWorkAddress(addressView);
        }else if(addressFlagForm3 == 2){
            AddressView addressView = new AddressView(customerInfoView.getRegisterAddress(),customerInfoView.getWorkAddress().getId());
            customerInfoView.setWorkAddress(addressView);
        }

        if(customerInfoView.getMaritalStatus().getSpouseFlag() == 1){
            if(addressFlagForm5 == 1){ //dup address 1 to address 2
                AddressView addressView = new AddressView(customerInfoView.getSpouse().getCurrentAddress(),customerInfoView.getSpouse().getRegisterAddress().getId());
                customerInfoView.getSpouse().setRegisterAddress(addressView);
            }

            if(addressFlagForm6 == 1){
                AddressView addressView = new AddressView(customerInfoView.getSpouse().getCurrentAddress(),customerInfoView.getSpouse().getWorkAddress().getId());
                customerInfoView.getSpouse().setWorkAddress(addressView);
            }else if(addressFlagForm6 == 2){
                AddressView addressView = new AddressView(customerInfoView.getSpouse().getRegisterAddress(),customerInfoView.getSpouse().getWorkAddress().getId());
                customerInfoView.getSpouse().setWorkAddress(addressView);
            }
        }

        //calculate age
        customerInfoView.setAge(Util.calAge(customerInfoView.getDateOfBirth()));
        if(customerInfoView.getSpouse() != null && customerInfoView.getSpouse().getDateOfBirth() != null){
            customerInfoView.getSpouse().setAge(Util.calAge(customerInfoView.getSpouse().getDateOfBirth()));
        }

        try{
            customerId = customerInfoControl.saveCustomerInfoIndividual(customerInfoView, workCaseId);
            isFromSummaryParam = true;
            onAddNewIndividual();
            onEditIndividual();
            messageHeader = "Save Individual Success.";
            message = "Save Individual data success.";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            messageHeader = "Save Individual Failed.";
            if(ex.getCause() != null){
                message = "Save Individual failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save Individual failed. Cause : " + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public String onSaveFromJuristic(){
        log.debug("onSaveFromJuristic");
        //check citizen id
        Customer customer = individualDAO.findCustomerByCitizenIdAndWorkCase(customerInfoView.getCitizenId(),workCaseId);
        if(customer != null && customer.getId() != 0){
            if(customer.getId() != customerInfoView.getId()){
                messageHeader = "Save Individual Failed.";
                message = "Citizen Id is already exist";
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                return "";
            }
        }

        //for check citizen id form list
        if(cusInfoJuristic.getIndividualViewList() != null && cusInfoJuristic.getIndividualViewList().size() > 0){
            int indexList = 0;
            for(CustomerInfoView cus : cusInfoJuristic.getIndividualViewList()){
                if(isEditFromJuristic) {
                    if(cus.getCitizenId().equalsIgnoreCase(customerInfoView.getCitizenId()) && rowIndex != indexList){
                        messageHeader = "Save Individual Failed.";
                        message = "Citizen Id is already exist";
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return "";
                    }
                } else {
                    if(cus.getCitizenId().equalsIgnoreCase(customerInfoView.getCitizenId())){
                        messageHeader = "Save Individual Failed.";
                        message = "Citizen Id is already exist";
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        return "";
                    }
                }
                indexList++;
            }
        }
        //customerInfoView = individual
        customerInfoView.getTitleTh().setTitleTh(titleDAO.findById(customerInfoView.getTitleTh().getId()).getTitleTh());
        customerInfoView.getRelation().setDescription(relationDAO.findById(customerInfoView.getRelation().getId()).getDescription());

        //calculate age
        customerInfoView.setAge(Util.calAge(customerInfoView.getDateOfBirth()));
        if(customerInfoView.getSpouse() != null && customerInfoView.getSpouse().getDateOfBirth() != null){
            customerInfoView.getSpouse().setAge(Util.calAge(customerInfoView.getSpouse().getDateOfBirth()));
        }

        if(isEditFromJuristic){
            cusInfoJuristic.getIndividualViewList().set(rowIndex,customerInfoView);
        } else {
            cusInfoJuristic.getIndividualViewList().add(customerInfoView);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFromIndividualParam",true);
        map.put("isFromSummaryParam",false);
        map.put("customerId",new Long(0));
        map.put("customerInfoView", cusInfoJuristic);
//        HttpSession session = FacesUtil.getSession(false);
//        session.setAttribute("cusInfoParams", map);
        FacesUtil.getFlash().put("cusInfoParams", map);
        return "customerInfoJuristic?faces-redirect=true";
    }

    public String onCancelFromJuristic(){
        log.debug("onCancelFromJuristic");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFromIndividualParam",true);
        map.put("isFromSummaryParam",false);
        map.put("customerId",new Long(0));
        map.put("customerInfoView", cusInfoJuristic);
        FacesUtil.getFlash().put("cusInfoParams", map);
        return "customerInfoJuristic?faces-redirect=true";
    }

    public void onChangeTitleTh(){
        customerInfoView.getTitleEn().setId(customerInfoView.getTitleTh().getId());
    }

    public void onChangeTitleEn(){
        customerInfoView.getTitleTh().setId(customerInfoView.getTitleEn().getId());
    }

    public void onChangeTitleThSpouse(){
        customerInfoView.getSpouse().getTitleEn().setId(customerInfoView.getSpouse().getTitleTh().getId());
    }

    public void onChangeTitleEnSpouse(){
        customerInfoView.getSpouse().getTitleTh().setId(customerInfoView.getSpouse().getTitleEn().getId());
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void onChangeSearch(){
        customerInfoView.setSearchId("");
    }

    public void onChangeSpouseSearch(){
        customerInfoView.getSpouse().setSearchId("");
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

    public List<Relation> getRelationIndividualList() {
        return relationIndividualList;
    }

    public void setRelationIndividualList(List<Relation> relationIndividualList) {
        this.relationIndividualList = relationIndividualList;
    }

    public List<Relation> getRelationSpouseList() {
        return relationSpouseList;
    }

    public void setRelationSpouseList(List<Relation> relationSpouseList) {
        this.relationSpouseList = relationSpouseList;
    }

    public List<Reference> getReferenceIndividualList() {
        return referenceIndividualList;
    }

    public void setReferenceIndividualList(List<Reference> referenceIndividualList) {
        this.referenceIndividualList = referenceIndividualList;
    }

    public List<Reference> getReferenceSpouseList() {
        return referenceSpouseList;
    }

    public void setReferenceSpouseList(List<Reference> referenceSpouseList) {
        this.referenceSpouseList = referenceSpouseList;
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

    public List<Race> getRaceList() {
        return raceList;
    }

    public void setRaceList(List<Race> raceList) {
        this.raceList = raceList;
    }

    public List<Nationality> getNationalityList() {
        return nationalityList;
    }

    public void setNationalityList(List<Nationality> nationalityList) {
        this.nationalityList = nationalityList;
    }

    public List<Nationality> getSndNationalityList() {
        return sndNationalityList;
    }

    public void setSndNationalityList(List<Nationality> sndNationalityList) {
        this.sndNationalityList = sndNationalityList;
    }

    public List<Education> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<Education> educationList) {
        this.educationList = educationList;
    }

    public List<Occupation> getOccupationList() {
        return occupationList;
    }

    public void setOccupationList(List<Occupation> occupationList) {
        this.occupationList = occupationList;
    }

    public List<BusinessType> getBusinessTypeList() {
        return businessTypeList;
    }

    public void setBusinessTypeList(List<BusinessType> businessTypeList) {
        this.businessTypeList = businessTypeList;
    }

    public List<MaritalStatus> getMaritalStatusList() {
        return maritalStatusList;
    }

    public void setMaritalStatusList(List<MaritalStatus> maritalStatusList) {
        this.maritalStatusList = maritalStatusList;
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

    public int getAddressFlagForm5() {
        return addressFlagForm5;
    }

    public void setAddressFlagForm5(int addressFlagForm5) {
        this.addressFlagForm5 = addressFlagForm5;
    }

    public int getAddressFlagForm6() {
        return addressFlagForm6;
    }

    public void setAddressFlagForm6(int addressFlagForm6) {
        this.addressFlagForm6 = addressFlagForm6;
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

    public List<Province> getProvinceForm4List() {
        return provinceForm4List;
    }

    public void setProvinceForm4List(List<Province> provinceForm4List) {
        this.provinceForm4List = provinceForm4List;
    }

    public List<District> getDistrictForm4List() {
        return districtForm4List;
    }

    public void setDistrictForm4List(List<District> districtForm4List) {
        this.districtForm4List = districtForm4List;
    }

    public List<SubDistrict> getSubDistrictForm4List() {
        return subDistrictForm4List;
    }

    public void setSubDistrictForm4List(List<SubDistrict> subDistrictForm4List) {
        this.subDistrictForm4List = subDistrictForm4List;
    }

    public List<Province> getProvinceForm5List() {
        return provinceForm5List;
    }

    public void setProvinceForm5List(List<Province> provinceForm5List) {
        this.provinceForm5List = provinceForm5List;
    }

    public List<District> getDistrictForm5List() {
        return districtForm5List;
    }

    public void setDistrictForm5List(List<District> districtForm5List) {
        this.districtForm5List = districtForm5List;
    }

    public List<SubDistrict> getSubDistrictForm5List() {
        return subDistrictForm5List;
    }

    public void setSubDistrictForm5List(List<SubDistrict> subDistrictForm5List) {
        this.subDistrictForm5List = subDistrictForm5List;
    }

    public List<Province> getProvinceForm6List() {
        return provinceForm6List;
    }

    public void setProvinceForm6List(List<Province> provinceForm6List) {
        this.provinceForm6List = provinceForm6List;
    }

    public List<District> getDistrictForm6List() {
        return districtForm6List;
    }

    public void setDistrictForm6List(List<District> districtForm6List) {
        this.districtForm6List = districtForm6List;
    }

    public List<SubDistrict> getSubDistrictForm6List() {
        return subDistrictForm6List;
    }

    public void setSubDistrictForm6List(List<SubDistrict> subDistrictForm6List) {
        this.subDistrictForm6List = subDistrictForm6List;
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

    public CustomerInfoView getCustomerInfoSearchSpouse() {
        return customerInfoSearchSpouse;
    }

    public void setCustomerInfoSearchSpouse(CustomerInfoView customerInfoSearchSpouse) {
        this.customerInfoSearchSpouse = customerInfoSearchSpouse;
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

    public boolean isEditBorrower() {
        return isEditBorrower;
    }

    public void setEditBorrower(boolean editBorrower) {
        isEditBorrower = editBorrower;
    }

    public boolean isEditSpouseBorrower() {
        return isEditSpouseBorrower;
    }

    public void setEditSpouseBorrower(boolean editSpouseBorrower) {
        isEditSpouseBorrower = editSpouseBorrower;
    }

    public boolean isMaritalStatusFlag() {
        return maritalStatusFlag;
    }

    public void setMaritalStatusFlag(boolean maritalStatusFlag) {
        this.maritalStatusFlag = maritalStatusFlag;
    }

    public boolean isFromJuristic() {
        return isFromJuristic;
    }

    public void setFromJuristic(boolean fromJuristic) {
        isFromJuristic = fromJuristic;
    }

    public boolean isReqSpoReference() {
        return reqSpoReference;
    }

    public void setReqSpoReference(boolean reqSpoReference) {
        this.reqSpoReference = reqSpoReference;
    }

    public boolean isReqSpoRelation() {
        return reqSpoRelation;
    }

    public void setReqSpoRelation(boolean reqSpoRelation) {
        this.reqSpoRelation = reqSpoRelation;
    }

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }

    public boolean isEditForm() {
        return isEditForm;
    }

    public void setEditForm(boolean editForm) {
        isEditForm = editForm;
    }

    public boolean isEditFormSpouse() {
        return isEditFormSpouse;
    }

    public void setEditFormSpouse(boolean editFormSpouse) {
        isEditFormSpouse = editFormSpouse;
    }
}
