package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.BusinessInfoView;
import com.clevel.selos.model.view.CollateralView;
import com.clevel.selos.model.view.FacilityView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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

    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;

    private List<CollateralType> collateralTypeList;

    //*** Result table List ***//
    private List<FacilityView> facilityViewList;
    private List<BusinessInfoView> businessInfoViewList;
    private List<CollateralView> proposeCollateralViewList;

    //*** Variable for view ***//
    private ProductGroup selectProductGroup;
    private FacilityView facility;
    private FacilityView selectFacilityItem;

    private BusinessInfoView selectBusinessInfoItem;
    private BusinessInfoView businessInfo;

    private CollateralView proposeCollateral;
    private CollateralView selectProposeCollateralItem;

    /*private CollateralView collateral;
    private CollateralType collateralType;


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

        if (facilityViewList == null) {
            facilityViewList = new ArrayList<FacilityView>();
        }

        if (productGroupList == null) {
            productGroupList = new ArrayList<ProductGroup>();
        }

        if (proposeCollateralViewList == null) {
            proposeCollateralViewList = new ArrayList<CollateralView>();
        }

        if (businessInfoViewList == null) {
            businessInfoViewList = new ArrayList<BusinessInfoView>();
        }

        onLoadSelectList();

        if(selectProductGroup == null){
            selectProductGroup = new ProductGroup();
        }

        if(facility == null){
            facility = new FacilityView();
        }

        if(businessInfo == null){
            businessInfo = new BusinessInfoView();
            businessInfo.setBusinessDescription(new BusinessDescription());
            businessInfo.getBusinessDescription().setBusinessGroup(new BusinessGroup());
        }

        if(proposeCollateral == null){
            proposeCollateral = new CollateralView();
        }
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
        facility = new FacilityView();
        facility.setProductProgram(new ProductProgram());
        facility.setCreditType(new CreditType());
        modeForButton = "add";
    }

    public void onEditFacility() {
        modeForButton = "edit";
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

        log.info("onSaveFacility ::: selectProductGroup.getId() : {} ", selectProductGroup.getId());
        log.info("onSaveFacility ::: facility.getProductProgram().getId() : {} ", facility.getProductProgram().getId());
        log.info("onSaveFacility ::: facility.getCreditType().getId() : {} ", facility.getCreditType().getId());
        log.info("onSaveFacility ::: facility.getRequestAmount : {}", facility.getRequestAmount());

        if( facility.getProductProgram().getId() != 0 && facility.getCreditType().getId() != 0 && facility.getRequestAmount() != null ) {
            if(modeForButton != null && modeForButton.equalsIgnoreCase("add")) {
                ProductProgram productProgram = productProgramDAO.findById(facility.getProductProgram().getId());
                CreditType creditType = creditTypeDao.findById(facility.getCreditType().getId());

                FacilityView facilityItem = new FacilityView();
                facilityItem.setProductProgram(productProgram);
                facilityItem.setCreditType(creditType);
                facilityItem.setRequestAmount(facility.getRequestAmount());
                facilityViewList.add(facilityItem);
                log.info("onSaveFacility ::: modeForButton : {}, Completed.", modeForButton);
            } else if(modeForButton != null && modeForButton.equalsIgnoreCase("edit")) {
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

    // *** Function For BusinessInfoView ***//
    public void onAddBusinessInfo() {
        log.info("onAddBusinessInfo ::: reset form");
        /*** Reset Form ***/
        modeForButton = "add";

        businessInfo = new BusinessInfoView();
        businessInfo.setBusinessDescription(new BusinessDescription());
        businessInfo.getBusinessDescription().setBusinessGroup(new BusinessGroup());
    }

    public void onEditBusinessInfo() {
        log.info("onEditBusinessInfo ::: selectBusinessInfo : {}", selectBusinessInfoItem);
        modeForButton = "edit";

        businessInfo = new BusinessInfoView();
        businessInfo.setBusinessDescription(selectBusinessInfoItem.getBusinessDescription());
        businessInfo.getBusinessDescription().setBusinessGroup(selectBusinessInfoItem.getBusinessDescription().getBusinessGroup());

        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessInfo.getBusinessDescription().getBusinessGroup());
    }

    public void onSaveBusinessInfo() {
        log.info("onSaveBusinessInfo ::: modeForButton : {}", modeForButton);

        RequestContext context = RequestContext.getCurrentInstance();
        boolean complete = false;

        /*** validate input ***/
        if(businessInfo.getBusinessDescription().getId() != 0 && businessInfo.getBusinessDescription().getBusinessGroup().getId() != 0){
            if(modeForButton.equalsIgnoreCase("add")) {
                BusinessInfoView businessInfo = new BusinessInfoView();

                log.info("onSaveBusinessInformation ::: selectBusinessGroupID : {}", businessInfo.getBusinessDescription().getId());
                log.info("onSaveBusinessInformation ::: selectBusinessDescriptionID : {}", businessInfo.getBusinessDescription().getBusinessGroup().getId());

                BusinessGroup businessGroup = businessGroupDAO.findById(businessInfo.getBusinessDescription().getBusinessGroup().getId());
                BusinessDescription businessDesc = businessDescriptionDAO.findById(businessInfo.getBusinessDescription().getId());

                businessDesc.setBusinessGroup(businessGroup);

                businessInfo.setBusinessDescription(businessDesc);

                businessInfoViewList.add(businessInfo);
                complete = true;
            } else if(modeForButton.equalsIgnoreCase("edit")) {
                log.info("onSaveBusinessInfo ::: rowIndex : {}", rowIndex);
                BusinessDescription businessDescription = businessDescriptionDAO.findById(businessInfo.getBusinessDescription().getId());
                BusinessGroup businessGroup = businessGroupDAO.findById(businessInfo.getBusinessDescription().getBusinessGroup().getId());

                businessDescription.setBusinessGroup(businessGroup);

                businessInfoViewList.get(rowIndex).setBusinessDescription(businessDescription);
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
        log.info("onDeleteBusinessInformation ::: selectBusinessInfoItem : {}", selectBusinessInfoItem);
        businessInfoViewList.remove(selectBusinessInfoItem);
    }

    // *** Function For ProposeCollateral ***//
    public void onAddProposeCollateral() {
        log.info("onAddProposeCollateral :::");
        //*** Reset form ***//
        log.info("onAddProposeCollateral ::: Reset Form");
        proposeCollateral = new CollateralView();
        proposeCollateral.setCollateralType(new CollateralType());
    }

    public void onEditProposeCollateral(){
        modeForButton = "edit";
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
            if(modeForButton.equalsIgnoreCase("add")){
                CollateralView collateral = new CollateralView();
                CollateralType collateralType = collateralTypeDAO.findById(proposeCollateral.getCollateralType().getId());
                collateral.setCollateralType(collateralType);
                collateral.setCollateralAmount(proposeCollateral.getCollateralAmount());

                proposeCollateralViewList.add(collateral);
                log.info("onSaveProposeCollateral ::: modeForButton : {}, Completed.", modeForButton);
            } else if(modeForButton.equalsIgnoreCase("edit")){
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
    }

    public void onDeleteProposeCollateral() {
        log.info("onDeleteProposeCollateral ::: selectProposeCollateralItem : {}", selectProposeCollateralItem);
        proposeCollateralViewList.remove(selectProposeCollateralItem);

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
        log.info("onChangeProductGroup ::: selectProductGroup : {}", selectProductGroup);
        ProductGroup productGroup = productGroupDAO.findById(selectProductGroup.getId());

        //*** Get Product Program List from Product Group ***//
        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdProByPrdGroup(productGroup);
        if(prdGroupToPrdProgramList == null){
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        }
        log.info("onChangeProductGroup ::: prdGroupToPrdProgramList size : {}", prdGroupToPrdProgramList.size());

        //*** Check if Facility added system must be remove all ***//
        log.info("onChangeProductGroup :::: facilityViewList.size :::::::::::" + facilityViewList.size());

        if (facilityViewList.size() > 0) {
            facilityViewList.removeAll(facilityViewList);
        }
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
        log.info("onChangeBusinessGroup ::: businessGroup.getId() : {}", businessInfo.getBusinessDescription().getBusinessGroup().getId());
        if(String.valueOf(businessInfo.getBusinessDescription().getBusinessGroup().getId()) != null && businessInfo.getBusinessDescription().getBusinessGroup().getId() != 0){
            BusinessGroup businessGroup = businessGroupDAO.findById(businessInfo.getBusinessDescription().getBusinessGroup().getId());
            log.info("onChangeBusinessGroup :: businessGroup : {}", businessGroup);
            businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
            businessInfo.setBusinessDescription(new BusinessDescription());
            businessInfo.getBusinessDescription().setBusinessGroup(businessGroup);
        } else {
            businessDescriptionList = new ArrayList<BusinessDescription>();
            businessInfo.setBusinessDescription(new BusinessDescription());
            businessInfo.getBusinessDescription().setBusinessGroup(new BusinessGroup());
        }
        log.info("onChangeBusinessGroupName ::: size is : {}", businessDescriptionList.size());
        log.info("onChangeBusinessGroupName ::: businessGroup : {}", businessInfo.getBusinessDescription().getBusinessGroup());
    }

    public void onChangeBusinessDesc(){
        log.info("onChangeBusinessDesc :::");
        log.info("onChangeBusinessDesc ::: businessDesc.getId() : {}", businessInfo.getBusinessDescription().getId());
        if(String.valueOf(businessInfo.getBusinessDescription().getId()) != null){
            BusinessDescription businessDescription = businessDescriptionDAO.findById(businessInfo.getBusinessDescription().getId());
            businessInfo.setBusinessDescription(businessDescription);
        }
        log.info("onChangeBusinessDesc ::: businessDesc : {}", businessInfo.getBusinessDescription());

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

    public List<FacilityView> getFacilityViewList() {
        return facilityViewList;
    }

    public void setFacilityViewList(List<FacilityView> facilityViewList) {
        this.facilityViewList = facilityViewList;
    }

    public List<BusinessInfoView> getBusinessInfoViewList() {
        return businessInfoViewList;
    }

    public void setBusinessInfoViewList(List<BusinessInfoView> businessInfoViewList) {
        this.businessInfoViewList = businessInfoViewList;
    }

    public ProductGroup getSelectProductGroup() {
        return selectProductGroup;
    }

    public void setSelectProductGroup(ProductGroup selectProductGroup) {
        this.selectProductGroup = selectProductGroup;
    }

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

    public BusinessInfoView getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(BusinessInfoView businessInfo) {
        this.businessInfo = businessInfo;
    }

    public BusinessInfoView getSelectBusinessInfoItem() {
        return selectBusinessInfoItem;
    }

    public void setSelectBusinessInfoItem(BusinessInfoView selectBusinessInfoItem) {
        this.selectBusinessInfoItem = selectBusinessInfoItem;
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
}
