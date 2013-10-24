package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CustomerInfoSummaryControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
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
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "custInfoSumIndi")
public class CustomerInfoIndividual implements Serializable {
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

    @Inject
    private DocumentTypeDAO documentTypeDAO;
    @Inject
    private RelationDAO relationDAO;
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
    private CustomerInfoSummaryControl customerInfoSummaryControl;

    //*** Drop down List ***//
    private List<DocumentType> documentTypeList;
    private List<Relation> relationList;
    private List<Reference> referenceList;
    private List<Title> titleThList;
    private List<Title> titleEnList;
    private List<Race> raceList;
    private List<Nationality> nationalityList;
    private List<Nationality> sndNationalityList;
    private List<Education> educationList;
    private List<Occupation> occupationList;
    private List<BusinessType> businessTypeList;
    //income
    //
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

    private String messageHeader;
    private String message;

    private int addressFlagForm2;
    private int addressFlagForm3;
    private int addressFlagForm5;
    private int addressFlagForm6;

    //session
    private long workCaseId;
    private long stepId;
    private String userId;

    private int caseBorrowerTypeId;

    public CustomerInfoIndividual(){
    }

    public void preRender(){
        HttpSession session = FacesUtil.getSession(false);
        session.setAttribute("workCaseId", 101);
        session.setAttribute("stepId", 1006);
        session.setAttribute("userId", 10001);

        log.info("preRender ::: setSession ");

        session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
            userId = session.getAttribute("userId").toString();
        }else{
            //TODO return to inbox
            log.info("preRender ::: workCaseId is null.");
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
        preRender();
        customerInfoView = new CustomerInfoView();
        customerInfoView.reset();
        customerInfoView.getSpouse().reset();

        customerInfoSearch = new CustomerInfoView();
        customerInfoSearch.reset();

        documentTypeList = documentTypeDAO.findAll();
        relationList = relationDAO.findAll();
        titleEnList = titleDAO.findAll();
        titleThList = titleDAO.findAll();
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

        caseBorrowerTypeId = customerInfoSummaryControl.getCaseBorrowerTypeIdByWorkCase(workCaseId);

        referenceList = new ArrayList<Reference>();

        addressFlagForm2 = 1;
        addressFlagForm3 = 1;
        addressFlagForm5 = 1;
        addressFlagForm6 = 1;

        addressTypeList = addressTypeDAO.findAll();
        kycLevelList = kycLevelDAO.findAll();

//        customerInfoView = customerInfoSummaryControl.getCustomerInfoSummary(workCaseId);
    }

    public void onChangeRelation(){
        referenceList = referenceDAO.findByCustomerEntityId(1, caseBorrowerTypeId, customerInfoView.getRelation().getId());
    }

    public void onChangeProvinceForm1() {
        if(customerInfoView.getCurrentAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getCurrentAddress().getProvince().getCode());
            districtForm1List = districtDAO.getListByProvince(province);
            customerInfoView.getCurrentAddress().setDistrict(new District());
        }else{
            provinceForm1List = provinceDAO.getListOrderByParameter("name");
            districtForm1List = new ArrayList<District>();
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm1() {
        if(customerInfoView.getCurrentAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getCurrentAddress().getDistrict().getId());
            subDistrictForm1List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm1();
            subDistrictForm1List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm2() {
        if(customerInfoView.getRegisterAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getRegisterAddress().getProvince().getCode());
            districtForm2List = districtDAO.getListByProvince(province);
            customerInfoView.getRegisterAddress().setDistrict(new District());
        }else{
            provinceForm2List = provinceDAO.getListOrderByParameter("name");
            districtForm2List = new ArrayList<District>();
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm2() {
        if(customerInfoView.getRegisterAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getRegisterAddress().getDistrict().getId());
            subDistrictForm2List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm2();
            subDistrictForm2List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm3() {
        if(customerInfoView.getWorkAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getWorkAddress().getProvince().getCode());
            districtForm3List = districtDAO.getListByProvince(province);
            customerInfoView.getWorkAddress().setDistrict(new District());
        }else{
            provinceForm3List = provinceDAO.getListOrderByParameter("name");
            districtForm3List = new ArrayList<District>();
            subDistrictForm3List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm3() {
        if(customerInfoView.getWorkAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getWorkAddress().getDistrict().getId());
            subDistrictForm3List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm3();
            subDistrictForm3List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm4() {
        if(customerInfoView.getSpouse().getCurrentAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getSpouse().getCurrentAddress().getProvince().getCode());
            districtForm4List = districtDAO.getListByProvince(province);
            customerInfoView.getSpouse().getCurrentAddress().setDistrict(new District());
        }else{
            provinceForm4List = provinceDAO.getListOrderByParameter("name");
            districtForm4List = new ArrayList<District>();
            subDistrictForm4List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm4() {
        if(customerInfoView.getSpouse().getCurrentAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getSpouse().getCurrentAddress().getDistrict().getId());
            subDistrictForm4List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm4();
            subDistrictForm4List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm5() {
        if(customerInfoView.getSpouse().getRegisterAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getSpouse().getRegisterAddress().getProvince().getCode());
            districtForm5List = districtDAO.getListByProvince(province);
            customerInfoView.getSpouse().getRegisterAddress().setDistrict(new District());
        }else{
            provinceForm5List = provinceDAO.getListOrderByParameter("name");
            districtForm5List = new ArrayList<District>();
            subDistrictForm5List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm5() {
        if(customerInfoView.getSpouse().getRegisterAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getSpouse().getRegisterAddress().getDistrict().getId());
            subDistrictForm5List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm5();
            subDistrictForm5List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceForm6() {
        if(customerInfoView.getSpouse().getWorkAddress().getProvince().getCode() != 0){
            Province province = provinceDAO.findById(customerInfoView.getSpouse().getWorkAddress().getProvince().getCode());
            districtForm6List = districtDAO.getListByProvince(province);
            customerInfoView.getSpouse().getWorkAddress().setDistrict(new District());
        }else{
            provinceForm6List = provinceDAO.getListOrderByParameter("name");
            districtForm6List = new ArrayList<District>();
            subDistrictForm6List = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictForm6() {
        if(customerInfoView.getSpouse().getWorkAddress().getDistrict().getId() != 0){
            District district = districtDAO.findById(customerInfoView.getSpouse().getWorkAddress().getDistrict().getId());
            subDistrictForm6List = subDistrictDAO.getListByDistrict(district);
        }else{
            onChangeProvinceForm6();
            subDistrictForm6List = new ArrayList<SubDistrict>();
        }
    }

    public void onSave(){

        if(addressFlagForm2 == 1){ //dup address 1 to address 2
            customerInfoView.setRegisterAddress(customerInfoView.getCurrentAddress());
        }

        if(addressFlagForm3 == 1){
            customerInfoView.setWorkAddress(customerInfoView.getCurrentAddress());
        }else if(addressFlagForm3 == 2){
            customerInfoView.setWorkAddress(customerInfoView.getRegisterAddress());
        }

        if(addressFlagForm5 == 1){ //dup address 1 to address 2
            customerInfoView.getSpouse().setRegisterAddress(customerInfoView.getSpouse().getCurrentAddress());
        }

        if(addressFlagForm6 == 1){
            customerInfoView.getSpouse().setWorkAddress(customerInfoView.getSpouse().getCurrentAddress());
        }else if(addressFlagForm6 == 2){
            customerInfoView.getSpouse().setWorkAddress(customerInfoView.getSpouse().getRegisterAddress());
        }

        customerInfoView.getCustomerEntity().setId(1); // for individual

        try{
            customerInfoSummaryControl.saveCustomerInfoIndividual(customerInfoView, workCaseId);
            messageHeader = "Save Customer Info Individual Success.";
            message = "Save Customer Info Individual data success.";
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            messageHeader = "Save Customer Info Individual Failed.";
            if(ex.getCause() != null){
                message = "Save Customer Info Individual failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save Customer Info Individual failed. Cause : " + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            onCreation();
        }
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
}
