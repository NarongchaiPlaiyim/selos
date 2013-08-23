package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BusinessInformation;
import com.clevel.selos.model.view.Collateral;
import com.clevel.selos.model.view.Facility;
import com.clevel.selos.system.MessageProvider;
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
    MessageProvider msg;


    private Collateral     collateral;
    private CollateralType collateralType;

    private BusinessGroup       businessGroup;
    private BusinessDescription businessDescription;
    private BusinessInformation businessInformation;

    private List<BusinessGroup>       businessGroupList;
    private List<BusinessDescription> businessDescriptionList;
    private List<BusinessInformation> businessInformationList;

    private List<Collateral>          collateralList;
    private List<CollateralType>      collateralTypeList;

    private BigDecimal existCollateralAmount;
    private String     existCollateralTypeName;

    private String dlgCreditTypeName;
    private String productProgramName;
    private String productGroupName;

    private BigDecimal dCollateralAmount;
    private String     dCollateralTypeName;

    private int businessGroupID;
    private int businessDescriptionID;

    private String modeForButton;
    private String modeForExist;
    private String modeForCollateral;
    private String modeForBusiness;

    private int rowIndex;
    private int indexEdit;
    private int indexBusiness;
    private int indexExistEdit;

    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private CreditTypeDAO creditTypeDao;
    @Inject
    private ProductGroupDAO productGroupDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;

    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;

    public PrescreenMaker() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.z");

        modeForButton     = "add";
        modeForCollateral = "add";
        modeForExist      = "add";
        modeForBusiness   = "add";

        businessInformation = new BusinessInformation();
        businessGroup       = new BusinessGroup();
        businessDescription = new BusinessDescription();

        businessDescription.setBusinessGroup(businessGroup);
        businessInformation.setBusinessDescription(businessDescription);



        if (collateral == null) {
            collateral = new Collateral();
        }

        if (collateralList == null) {
            collateralList = new ArrayList<Collateral>();
        }

        collateralTypeList      = collateralTypeDAO.findAll();
        businessGroupList       = businessGroupDAO.findAll();
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

    public void onAddBusinessInformation() {
        log.info("onAddBusinessInformation");

        BusinessInformation businessInfo = null;
        businessInfo = new BusinessInformation();

        businessDescriptionID = businessInformation.getBusinessDescription().getId();
        businessGroupID       = businessInformation.getBusinessDescription().getBusinessGroup().getId();

        businessGroup = businessGroupDAO.findById(businessGroupID);
        businessDescription = businessDescriptionDAO.findById(businessDescriptionID);

        businessDescription.setBusinessGroup(businessGroup);

        businessInfo.setBusinessDescription(businessDescription);

        businessInformationList.add(businessInfo);

        businessInformation = new BusinessInformation();
        businessGroup       = new BusinessGroup();
        businessDescription = new BusinessDescription();

        businessDescription.setBusinessGroup(businessGroup);
        businessInformation.setBusinessDescription(businessDescription);
    }

    public void onRowBusinessInformation(int index) {

        log.info("onRowBusinessInformation {}", index);

        businessInformation = new BusinessInformation();
        businessGroup       = new BusinessGroup();
        businessDescription = new BusinessDescription();

        businessDescription.setBusinessGroup(businessGroup);
        businessInformation.setBusinessDescription(businessDescription);

        BusinessInformation businessInfo = null;

        businessInfo = businessInformationList.get(index);

        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessInfo.getBusinessDescription().getBusinessGroup());

        businessInformation = businessInfo;

        indexBusiness = index;
    }

    public void onEditBusinessInformation() {
        log.info("onEditBusinessInformation {}@ index", indexEdit);

        BusinessInformation businessInfo = null;
        businessInfo = businessInformationList.get(indexBusiness);

        businessDescriptionID = businessInfo.getBusinessDescription().getId();
        businessGroupID       = businessInfo.getBusinessDescription().getBusinessGroup().getId();

        businessGroup = businessGroupDAO.findById(businessGroupID);
        businessDescription = businessDescriptionDAO.findById(businessDescriptionID);

        modeForBusiness = "add";

        businessInfo.setBusinessDescription(businessDescription);

        businessInformation = new BusinessInformation();
        businessGroup       = new BusinessGroup();
        businessDescription = new BusinessDescription();

        businessDescription.setBusinessGroup(businessGroup);
        businessInformation.setBusinessDescription(businessDescription);

        businessGroupID         = 0;
        businessDescriptionID   = 0;

    }

    public void onRemoveBusinessInformation(int index) {

        businessInformationList.remove(index);

    }

    public void onChangeBusinessGroupName() {

        log.info("onChangeBusinessGroupName");
        log.info("group obj getBusinessGroup().getId() is {}", businessInformation.getBusinessDescription().getBusinessGroup());
        businessGroupID       = businessInformation.getBusinessDescription().getBusinessGroup().getId();

        businessGroup = businessGroupDAO.findById(businessGroupID);

        businessDescriptionList = new ArrayList<BusinessDescription>();
        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
        log.info("onChangeBusinessGroupName size is {}",businessDescriptionList.size());

    }

    public String getProductProgramName() {
        return productProgramName;
    }

    public void setProductProgramName(String productProgramName) {
        this.productProgramName = productProgramName;
    }

    public String getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(String modeForButton) {
        this.modeForButton = modeForButton;
    }


    public String getDlgCreditTypeName() {
        return dlgCreditTypeName;
    }

    public void setDlgCreditTypeName(String dlgCreditTypeName) {
        this.dlgCreditTypeName = dlgCreditTypeName;
    }

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
    }

    public List<CollateralType> getCollateralTypeList() {
        return collateralTypeList;
    }

    public void setCollateralTypeList(List<CollateralType> collateralTypeList) {
        this.collateralTypeList = collateralTypeList;
    }

    public Collateral getCollateral() {
        return collateral;
    }

    public void setCollateral(Collateral collateral) {
        this.collateral = collateral;
    }

    public List<Collateral> getCollateralList() {
        return collateralList;
    }

    public void setCollateralList(List<Collateral> collateralList) {
        this.collateralList = collateralList;
    }

    public BigDecimal getdCollateralAmount() {
        return dCollateralAmount;
    }

    public void setdCollateralAmount(BigDecimal dCollateralAmount) {
        this.dCollateralAmount = dCollateralAmount;
    }

    public String getdCollateralTypeName() {
        return dCollateralTypeName;
    }

    public void setdCollateralTypeName(String dCollateralTypeName) {
        this.dCollateralTypeName = dCollateralTypeName;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public void setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
    }

    public BusinessInformation getBusinessInformation() {
        return businessInformation;
    }

    public void setBusinessInformation(BusinessInformation businessInformation) {
        this.businessInformation = businessInformation;
    }

    public List<BusinessInformation> getBusinessInformationList() {
        return businessInformationList;
    }

    public void setBusinessInformationList(List<BusinessInformation> businessInformationList) {
        this.businessInformationList = businessInformationList;
    }

    public List<BusinessDescription> getBusinessDescriptionList() {
        return businessDescriptionList;
    }

    public void setBusinessDescriptionList(List<BusinessDescription> businessDescriptionList) {
        this.businessDescriptionList = businessDescriptionList;
    }

    public List<BusinessGroup> getBusinessGroupList() {
        return businessGroupList;
    }

    public void setBusinessGroupList(List<BusinessGroup> businessGroupList) {
        this.businessGroupList = businessGroupList;
    }

    public BusinessDescription getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(BusinessDescription businessDescription) {
        this.businessDescription = businessDescription;
    }

    public BusinessGroup getBusinessGroup() {
        return businessGroup;
    }

    public void setBusinessGroup(BusinessGroup businessGroup) {
        this.businessGroup = businessGroup;

    }

    public int getBusinessDescriptionID() {
        return businessDescriptionID;
    }

    public void setBusinessDescriptionID(int businessDescriptionID) {
        this.businessDescriptionID = businessDescriptionID;
    }

    public int getBusinessGroupID() {
        return businessGroupID;
    }

    public void setBusinessGroupID(int businessGroupID) {
        this.businessGroupID = businessGroupID;
    }

    public BigDecimal getExistCollateralAmount() {
        return existCollateralAmount;
    }

    public void setExistCollateralAmount(BigDecimal existCollateralAmount) {
        this.existCollateralAmount = existCollateralAmount;
    }

    public String getExistCollateralTypeName() {
        return existCollateralTypeName;
    }

    public void setExistCollateralTypeName(String existCollateralTypeName) {
        this.existCollateralTypeName = existCollateralTypeName;
    }

    public String getModeForExist() {
        return modeForExist;
    }

    public void setModeForExist(String modeForExist) {
        this.modeForExist = modeForExist;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getIndexEdit() {
        return indexEdit;
    }

    public void setIndexEdit(int indexEdit) {
        this.indexEdit = indexEdit;
    }

    public int getIndexBusiness() {
        return indexBusiness;
    }

    public void setIndexBusiness(int indexBusiness) {
        this.indexBusiness = indexBusiness;
    }

    public int getIndexExistEdit() {
        return indexExistEdit;
    }

    public void setIndexExistEdit(int indexExistEdit) {
        this.indexExistEdit = indexExistEdit;
    }

    public String getModeForCollateral() {
        return modeForCollateral;
    }

    public void setModeForCollateral(String modeForCollateral) {
        this.modeForCollateral = modeForCollateral;
    }

    public String getModeForBusiness() {
        return modeForBusiness;
    }

    public void setModeForBusiness(String modeForBusiness) {
        this.modeForBusiness = modeForBusiness;
    }
}
