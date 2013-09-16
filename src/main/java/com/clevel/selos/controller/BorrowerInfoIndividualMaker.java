package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.model.RMmodel.IndividualModel;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 8/30/13
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
@ViewScoped
@ManagedBean(name = "borrowerInfoIndividual")
public class BorrowerInfoIndividualMaker implements Serializable {
    public int titleIDTh1;
    public int titleIDEn1;
    public int borrowerTypeID;
    public int provinceID;
    public int districtID;
    public int subDistrictID;
    public int nationalityID;
    public int raceID;
    public int maritalStatusID;
    @Inject
    Logger log;
    @Inject
    @NormalMessage
    Message msg;
    private List<Title> titleList;
    private List<BorrowerType> borrowerTypeList;
    private List<Province> provinceList;
    private List<District> districtList;
    private List<SubDistrict> subDistrictList;
    private List<Relation> relationList;
    private List<Nationality> nationalityList;
    private List<Nationality> raceList;
    private List<MaritalStatus> maritalStatusList;
    private List<AddressType> addressTypeList;
    @Inject
    private TitleDAO titleDAO;
    @Inject
    private BorrowerTypeDAO borrowerTypeDAO;
    @Inject
    private ProvinceDAO provinceDAO;    // find credit type
    @Inject
    private DistrictDAO districtDAO;
    @Inject
    private SubDistrictDAO subDistrictDAO;
    @Inject
    private RelationDAO relationDAO;
    @Inject
    private NationalityDAO nationalityDAO;
    @Inject
    private MaritalStatusDAO maritalStatusDAO;
    @Inject
    private AddressTypeDAO addressTypeDAO;
    @Inject
    private RMInterface rmInterfaceImpl;

    public BorrowerInfoIndividualMaker() {

    }

    @PostConstruct
    public void onCreation() {
        raceList = nationalityDAO.findAll();
        CustomerEntity customerEntity =new CustomerEntity();
        customerEntity.setId(1);
        titleList = titleDAO.getListByCustomerEntity(customerEntity);
        provinceList = provinceDAO.getListOrderByParameter("name");
        relationList = relationDAO.findAll();
        addressTypeList = addressTypeDAO.findAll();
        nationalityList = nationalityDAO.findAll();
        borrowerTypeList = borrowerTypeDAO.findAll();
        maritalStatusList = maritalStatusDAO.findAll();

        raceID = 178;
        nationalityID = 178;
    }

    public void onChangeProvince() {

        log.info("onChangeProvince :::: provinceID :::  ", provinceID);
        Province province = provinceDAO.findById(provinceID);
        log.info("onChangeProvince :::: province ::: ", province);

        districtList = districtDAO.getListByProvince(province);
        log.info("onChangeProvince :::: districtList.size ::: ", districtList.size());
    }

    public void onChangeDistrict() {

        log.info("onChangeDistrict :::: districtID  :::  ", districtID);
        District district = districtDAO.findById(districtID);
        log.info("onChangeDistrict :::: district ::: ", district);

        subDistrictList = subDistrictDAO.getListByDistrict(district);
        log.info("onChangeDistrict :::: subDistrictList.size ::: ", subDistrictList.size());
    }

    public void onChangeTitle( String titleLang) {
        if( titleLang.equals("thai")){
            titleIDEn1 = titleIDTh1;
        }else if(titleLang.equals("eng")){
            titleIDTh1 = titleIDEn1;
        }
    }

    public void onSearchRMIndividual() {

        IndividualModel individualModel;
        try{
            individualModel = rmInterfaceImpl.getIndividualInfo("","","",RMInterface.DocumentType.CITIZEN_ID);

            log.info("after get individualModel from RM is {}",individualModel);

        }catch (Exception ex){
            log.error("error");
        }finally {

        }


    }

    public int getTitleIDTh1() {
        return titleIDTh1;
    }

    public void setTitleIDTh1(int titleIDTh1) {
        this.titleIDTh1 = titleIDTh1;
    }

    public int getTitleIDEn1() {
        return titleIDEn1;
    }

    public void setTitleIDEn1(int titleIDEn1) {
        this.titleIDEn1 = titleIDEn1;
    }

    public int getBorrowerTypeID() {
        return borrowerTypeID;
    }

    public void setBorrowerTypeID(int borrowerTypeID) {
        this.borrowerTypeID = borrowerTypeID;
    }

    public List<Title> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<Title> titleList) {
        this.titleList = titleList;
    }

    public List<BorrowerType> getBorrowerTypeList() {
        return borrowerTypeList;
    }

    public void setBorrowerTypeList(List<BorrowerType> borrowerTypeList) {
        this.borrowerTypeList = borrowerTypeList;
    }

    public List<SubDistrict> getSubDistrictList() {
        return subDistrictList;
    }

    public void setSubDistrictList(List<SubDistrict> subDistrictList) {
        this.subDistrictList = subDistrictList;
    }

    public List<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public List<Relation> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<Relation> relationList) {
        this.relationList = relationList;
    }

    public int getSubDistrictID() {
        return subDistrictID;
    }

    public void setSubDistrictID(int subDistrictID) {
        this.subDistrictID = subDistrictID;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public List<Nationality> getNationalityList() {
        return nationalityList;
    }

    public void setNationalityList(List<Nationality> nationalityList) {
        this.nationalityList = nationalityList;
    }

    public List<Nationality> getRaceList() {
        return raceList;
    }

    public void setRaceList(List<Nationality> raceList) {
        this.raceList = raceList;
    }

    public int getNationalityID() {
        return nationalityID;
    }

    public void setNationalityID(int nationalityID) {
        this.nationalityID = nationalityID;
    }

    public int getRaceID() {
        return raceID;
    }

    public void setRaceID(int raceID) {
        this.raceID = raceID;
    }

    public int getMaritalStatusID() {
        return maritalStatusID;
    }

    public void setMaritalStatusID(int maritalStatusID) {
        this.maritalStatusID = maritalStatusID;
    }

    public List<MaritalStatus> getMaritalStatusList() {
        return maritalStatusList;
    }

    public void setMaritalStatusList(List<MaritalStatus> maritalStatusList) {
        this.maritalStatusList = maritalStatusList;
    }

    public List<AddressType> getAddressTypeList() {
        return addressTypeList;
    }

    public void setAddressTypeList(List<AddressType> addressTypeList) {
        this.addressTypeList = addressTypeList;
    }
}
