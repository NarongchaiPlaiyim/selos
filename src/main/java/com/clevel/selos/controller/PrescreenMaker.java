package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.BusinessInformation;
import com.clevel.selos.model.view.Collateral;
import com.clevel.selos.model.view.Facility;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private List<CollateralType> collateralTypeList;

    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;

    //*** Result table List ***//
    private List<Facility> facilityList;
    private List<BusinessInformation> businessInformationList;
    private List<Collateral> collateralList;

    //*** Variable for view ***//
    private ProductGroup selectProductGroup;
    private Facility facility;

    /*private Collateral collateral;
    private CollateralType collateralType;

    private BusinessGroup businessGroup;
    private BusinessDescription businessDescription;
    private BusinessInformation businessInformation;




    private BigDecimal existCollateralAmount;
    private String existCollateralTypeName;*/

    private String modeForButton;
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


    public PrescreenMaker() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation");

        modeForButton = "add";

        if (facilityList == null) {
            facilityList = new ArrayList<Facility>();
        }

        if (productGroupList == null) {
            productGroupList = new ArrayList<ProductGroup>();
        }

        if (collateralList == null) {
            collateralList = new ArrayList<Collateral>();
        }

        if (businessInformationList == null) {
            businessInformationList = new ArrayList<BusinessInformation>();
        }

        onLoadSelectList();
    }

    public void onLoadSelectList() {
        log.info("onLoadSelectList :::");

        productGroupList = productGroupDAO.findAll();
        log.info("onLoadSelectList ::: productGroupList size : {}", productGroupList.size());

        collateralTypeList = collateralTypeDAO.findAll();
        log.info("onLoadSelectList ::: collateralTypeList size : {}", collateralTypeList.size());

        businessGroupList = businessGroupDAO.findAll();
        log.info("onLoadSelectList ::: businessGroupList size : {}", businessGroupList.size());
    }

    // *** Function For Facility *** //
    public void onAddFacility() {
        log.info("onAddFacility ::: ");
        log.info("onAddFacility ::: selectProductGroup : {}", selectProductGroup);

        //*** Reset form ***//
        log.info("onAddFacility ::: Reset Form");
        prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        facility = new Facility();
        modeForButton = "add";
    }

    public void onEditFacility(int row) {
        modeForButton = "edit";
        rowIndex = row;
        log.info("onSelectedFacility ::: rowNumber  : {}", row);
        log.info("onSelectedFacility ::: facilityList : {}", facilityList);

        //*** Check list size ***//
        if( rowIndex < facilityList.size() ) {
            facility = new Facility();
            ProductProgram productProgram = facilityList.get(rowIndex).getProductProgram();
            CreditType creditType = facilityList.get(rowIndex).getCreditType();
            BigDecimal requestAmount = facilityList.get(rowIndex).getRequestAmount();
            prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(productProgram);

            facility.setProductProgram(productProgram);
            facility.setCreditType(creditType);
            facility.setRequestAmount(requestAmount);
        }
    }

    public void onSaveFacility() {
        log.info("onSaveFacility ::: mode : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean complete = false;

        log.info("selectProductGroup.getId() : {} ", selectProductGroup.getId());
        log.info("facility.getProductProgram().getId() : {} ", facility.getProductProgram().getId());
        log.info("facility.getCreditType().getId() : {} ", facility.getCreditType().getId());

        /*if(modeForButton.equalsIgnoreCase("add")){
            /*//*** Validate for mandate value ***//*/
            if( facility.getProductProgram().getId() != 0 && facility.getCreditType().getId() != 0 && facility.getRequestAmount() != null ){

            }
            if(!((selectProductGroup.getId() == 0) || (selectProductProgram.getId() == 0) || (selectCreditType.getId() == 0))) {

                log.info("onAddFacility::: selectProductProgram.getId() :: " +
                        productProgramDAO.findById(selectProductProgram.getId()).toString());
                log.info("onAddFacility::: selectCreditType.getId() :: " +
                        creditTypeDao.findById(selectCreditType.getId()).toString());

                ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
                CreditType creditType = creditTypeDao.findById(selectCreditType.getId());
                Facility facAdd = new Facility();
                facAdd.setProductProgram(productProgram);
                facAdd.setCreditType(creditType);
                facAdd.setRequestAmount(facility.getRequestAmount());
                facilityList.add(facAdd);
                complete = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Complete","Complete");
            }else{
                complete = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR","ERROR");
                RequestContext.getCurrentInstance().execute("messageBoxFacilityDialog.show()");
            }
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.addCallbackParam("functionComplete", complete);
        }else if(modeForButton.equalsIgnoreCase("edit")){
            ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
            CreditType creditType         = creditTypeDao.findById(selectCreditType.getId());
            log.info("onEditFacility :::::: selectProductProgram ::: "+selectProductProgram.getName());
            log.info("onEditFacility :::::: selectCreditType ::: "+selectCreditType.getName());
            facilityList.get(rowIndex).setProductProgram(productProgram);
            facilityList.get(rowIndex).setCreditType(creditType);
            facilityList.get(rowIndex).setRequestAmount(facility.getRequestAmount());
        }*/
    }

    public void onDeleteFacility(int row) {
        facilityList.remove(row);
    }

    // *** Function For BusinessInformation ***//
    /*public void onAddBusinessInformation() {
        log.info("onAddBusinessInformation ::: reset form");
        /*//*** Reset Form ***//*/
        modeForButton = "add";
        businessInformation = new BusinessInformation();
        businessGroup = new BusinessGroup();
        businessDescription = new BusinessDescription();

        businessDescription.setBusinessGroup(businessGroup);
        businessInformation.setBusinessDescription(businessDescription);

        businessGroupID = 0;
        businessDescriptionID = 0;
    }

    public void onEditBusinessInformation(int index) {
        log.info("onEditBusinessInformation ::: rowIndex : {}", index);
        modeForButton = "edit";
        rowIndex = index;

        businessInformation = new BusinessInformation();
        businessGroup = new BusinessGroup();
        businessDescription = new BusinessDescription();

        BusinessInformation businessInfo;
        businessInfo = businessInformationList.get(index);

        businessGroup = businessInfo.getBusinessDescription().getBusinessGroup();
        businessDescription = businessInfo.getBusinessDescription();
        businessInformation = businessInfo;

        businessDescription.setBusinessGroup(businessGroup);
        businessInformation.setBusinessDescription(businessDescription);

        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessInfo.getBusinessDescription().getBusinessGroup());
    }

    public void onSaveBusinessInformation() {
        log.info("onSaveBusinessInformation ::: modeForButton : {}", modeForButton);
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean complete = false;
        /*//*** validate input ***//*/
        if((businessGroup.getId() != 0) && (businessDescription.getId() != 0)) {
            if(modeForButton.equalsIgnoreCase("add")) {
                BusinessInformation businessInfo = new BusinessInformation();

                log.info("onSaveBusinessInformation ::: selectBusinessGroupID : {}", businessGroupID);
                log.info("onSaveBusinessInformation ::: selectBusinessDescriptionID : {}", businessDescriptionID);

                businessGroup = businessGroupDAO.findById(businessGroupID);
                businessDescription = businessDescriptionDAO.findById(businessDescriptionID);

                businessDescription.setBusinessGroup(businessGroup);

                businessInfo.setBusinessDescription(businessDescription);

                businessInformationList.add(businessInfo);
                log.info("onSaveBusinessInformation ::: Complete.");
            } else {
                log.info("onSaveBusinessInformation ::: rowIndex : {}", rowIndex);

                BusinessInformation businessInfo;
                businessInfo = businessInformationList.get(indexBusiness);

                businessDescriptionID = businessInfo.getBusinessDescription().getId();
                businessGroupID = businessInfo.getBusinessDescription().getBusinessGroup().getId();

                businessGroup = businessGroupDAO.findById(businessGroupID);
                businessDescription = businessDescriptionDAO.findById(businessDescriptionID);

                businessInfo.setBusinessDescription(businessDescription);
                log.info("onSaveBusinessInformation ::: rowIndex : {}, Complete.", rowIndex);
            }
            complete = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Complete","Complete");
        }else{
            complete = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR","ERROR");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("functionComplete", complete);
    }

    public void onDeleteBusinessInformation(int index) {
        log.info("onDeleteBusinessInformation :::");
        businessInformationList.remove(index);

    }

    // *** Event for DropDown *** //
    public void checkOnChangeProductGroup(){
        if(facilityList != null && facilityList.size() > 0){
            RequestContext.getCurrentInstance().execute("confirmChangeProductGroupDlg.show()");
        }else{
            onChangeProductGroup();
        }
    }
    public void onChangeProductGroup(){
        log.info("onChangeProductGroup ::: selectProductGroup : {}", selectProductGroup);
        ProductGroup productGroup = productGroupDAO.findById(selectProductGroup.getId());

        /*//*** Get Product Program List from Product Group ***//*/
        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdProByPrdGroup(productGroup);
        if(prdGroupToPrdProgramList == null){
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        }
        log.info("onChangeProductGroup ::: prdGroupToPrdProgramList size : {}", prdGroupToPrdProgramList.size());

        /*/



    /*** Check if Facility added system must be remove all ***//*/
        log.info("onChangeProductGroup :::: facilityList.size :::::::::::" + facilityList.size());

        if (facilityList.size() > 0) {
            facilityList.removeAll(facilityList);
        }
    }

    public void onChangeProductProgram(){
        ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
        log.info("onChangeProductProgram :::: productProgram : {}", productProgram);

        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(productProgram);
        if(prdProgramToCreditTypeList == null){
            prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        }
        log.info("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " +prdProgramToCreditTypeList.size());
    }

    public void onAddCollateral() {
        log.info("onAddCollateral {}");
        Collateral colla = null;

        log.info("dCollateralTypeName is ", dCollateralTypeName);
        log.info("dCollateralAmount is ", dCollateralAmount);
        colla = new Collateral();
        colla.setCollateralTypeName(dCollateralTypeName);
        colla.setCollateralAmount(dCollateralAmount);
        collateralList.add(colla);
        dCollateralAmount = null;
        dCollateralTypeName = "";
    }

    public void onRowCollateral(int index) {
        log.info("onRowCollateral {}", index);

        Collateral colla = null;
        colla = new Collateral();
        collateral = new Collateral();

        colla = collateralList.get(index);

        log.info("colla getCollateralTypeName at index {}", colla.getCollateralTypeName());
        log.info("colla getCollateralAmount at index {}", colla.getCollateralAmount());


        dCollateralAmount = colla.getCollateralAmount();
        dCollateralTypeName = colla.getCollateralTypeName();
        indexEdit = index;
        modeForCollateral = "edit";

    }

    public void onEditCollateral() {
        log.info("onEditCollateral {}");

        Collateral colla = null;
        colla = new Collateral();

        colla = collateralList.get(indexEdit);
        colla.setCollateralTypeName(dCollateralTypeName);

        colla.setCollateralAmount(dCollateralAmount);
        modeForCollateral = "add";
        dCollateralTypeName = "";
        dCollateralAmount = null;
    }



    public void onRemoveCollateral(int index) {
        log.info("onRemoveCollateral {}", index);
        collateralList.remove(index);

    }

    public void onClearDlg() {

        dCollateralAmount = null;
        dCollateralTypeName = "";
        modeForCollateral = "add";
    }

    public void onAddExistCollateral() {
        log.info("onAddExistCollateral {}");
        Collateral collExist = null;

        log.info("dCollateralTypeName is ", existCollateralTypeName);
        log.info("dCollateralAmount is ", existCollateralAmount);
        collExist = new Collateral();
        collExist.setCollateralTypeName(existCollateralTypeName);
        collExist.setCollateralAmount(existCollateralAmount);
        collateralList.add(collExist);

    }

    public void onSelectedExistCollateral(int row) {
        existCollateralAmount = collateralList.get(row).getCollateralAmount();
        existCollateralTypeName = collateralList.get(row).getCollateralTypeName();
        indexExistEdit = row;
        modeForExist = "edit";
    }

    public void onEditExistCollateral() {
        collateralList.get(indexExistEdit).setCollateralAmount(existCollateralAmount);
        collateralList.get(indexExistEdit).setCollateralTypeName(existCollateralTypeName);
        modeForExist = "add";
        existCollateralTypeName = "";
        existCollateralAmount = null;
    }

    public void onDeleteExistCollateral(int row) {
        collateralList.remove(row);
    }



    public void onChangeBusinessGroupName() {

        log.info("onChangeBusinessGroupName");
        log.info("group obj getBusinessGroup().getId() is {}", businessInformation.getBusinessDescription().getBusinessGroup());
        businessGroupID = businessInformation.getBusinessDescription().getBusinessGroup().getId();

        businessGroup = businessGroupDAO.findById(businessGroupID);

        businessDescriptionList = new ArrayList<BusinessDescription>();
        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
        log.info("onChangeBusinessGroupName size is {}", businessDescriptionList.size());

    }

    public void onChangeBusinessDescriptionName(){

        log.info("onChangeBusinessDescriptinName");
        log.info("BusinessInformation : {}", businessInformation);
        businessDescriptionID = businessInformation.getBusinessDescription().getId();
        businessInformation.setBusinessDescription(businessDescriptionDAO.findById(businessDescriptionID));
        businessComment = businessInformation.getBusinessDescription().getBusinessComment();
        log.info("businessComment : {}", businessComment);

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
        log.info("onSelectedFacility ::: facilityList.get(rowNumber).getFacilityName :: " + rowNumber + "  "
                + facilityList.get(rowNumber).getCreditType().getId());
        log.info("onSelectedFacility ::: facilityList.get(rowNumber).getProductProgramName :: " + rowNumber + "  "
                + facilityList.get(rowNumber).getProductProgram().getId());
        log.info("onSelectedFacility ::: facilityList.get(rowNumber).getRequestAmount :: " + rowNumber + "  "
                + facilityList.get(rowNumber).getRequestAmount());

        selectProductProgram = facilityList.get(rowNumber).getProductProgram();
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(selectProductProgram);
        selectCreditType = facilityList.get(rowNumber).getCreditType();
        facility.setProductProgram(selectProductProgram);
        facility.setCreditType(selectCreditType);
        facility.setRequestAmount(facilityList.get(rowNumber).getRequestAmount());


    }*/

    /*public void onEditFacility() {
        ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
        CreditType creditType         = creditTypeDao.findById(selectCreditType.getId());
        log.info("onEditFacility :::::: selectProductProgram ::: "+selectProductProgram.getName());
        log.info("onEditFacility :::::: selectCreditType ::: "+selectCreditType.getName());
        facilityList.get(rowIndex).setProductProgram(productProgram);
        facilityList.get(rowIndex).setCreditType(creditType);
        facilityList.get(rowIndex).setRequestAmount(facility.getRequestAmount());
    }*/

    public String getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(String modeForButton) {
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

    public List<Facility> getFacilityList() {
        return facilityList;
    }

    public void setFacilityList(List<Facility> facilityList) {
        this.facilityList = facilityList;
    }

    public List<BusinessInformation> getBusinessInformationList() {
        return businessInformationList;
    }

    public void setBusinessInformationList(List<BusinessInformation> businessInformationList) {
        this.businessInformationList = businessInformationList;
    }

    public ProductGroup getSelectProductGroup() {
        return selectProductGroup;
    }

    public void setSelectProductGroup(ProductGroup selectProductGroup) {
        this.selectProductGroup = selectProductGroup;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
