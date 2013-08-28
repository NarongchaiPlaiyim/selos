package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
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


    private Collateral collateral;
    private CollateralType collateralType;

    private BusinessGroup businessGroup;
    private BusinessDescription businessDescription;
    private BusinessInformation businessInformation;

    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;
    private List<BusinessInformation> businessInformationList;

    private List<Collateral> collateralList;
    private List<CollateralType> collateralTypeList;

    private List<Facility> facilityList;
    private Facility facility;

    private List<CreditType> creditTypeList;
    private CreditType selectCreditType;

    private List<ProductProgram> productProgramList;
    private ProductProgram selectProductProgram;

    private List<PrdGroupToPrdProgram> prdGroupToPrdProgramList;
    private List<PrdProgramToCreditType> prdProgramToCreditTypeList;

    private List<ProductGroup> productGroupList;
    private ProductGroup selectProductGroup;

    private BigDecimal existCollateralAmount;
    private String existCollateralTypeName;

    private String dlgCreditTypeName;
    private String productProgramName;
    private String productGroupName;

    private BigDecimal dCollateralAmount;
    private String dCollateralTypeName;

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

        log.info("onCreation.z");

        modeForButton = "add";
        modeForCollateral = "add";
        modeForExist = "add";
        modeForBusiness = "add";

        businessInformation = new BusinessInformation();
        businessGroup = new BusinessGroup();
        businessDescription = new BusinessDescription();

        businessDescription.setBusinessGroup(businessGroup);
        businessInformation.setBusinessDescription(businessDescription);

        if (facilityList == null) {
            facilityList = new ArrayList<Facility>();
        }

        if (facility == null) {
            facility = new Facility();
        }

        if (creditTypeList == null) {
            creditTypeList = new ArrayList<CreditType>();
        }

        if (productGroupList == null) {
            productGroupList = new ArrayList<ProductGroup>();
        }

        if (productProgramList == null) {
            productProgramList = new ArrayList<ProductProgram>();
        }

        if(prdGroupToPrdProgramList == null){
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        }

        if(prdProgramToCreditTypeList == null){
            prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        }

        if (collateral == null) {
            collateral = new Collateral();
        }

        if (collateralList == null) {
            collateralList = new ArrayList<Collateral>();
        }

        if (businessInformationList == null) {
            businessInformationList = new ArrayList<BusinessInformation>();
        }

        collateralTypeList = collateralTypeDAO.findAll();
        businessGroupList = businessGroupDAO.findAll();

        onLoadSelectOneListbox();
    }

    public void onLoadSelectOneListbox() {
        log.info("onLoadFirst ::::::: ");
        productGroupList     = productGroupDAO.findAll();
        log.info("onLoadFirst :::::::  productGroupList size :::::::::::: {}", productGroupList.size());

        selectProductGroup   = productGroupList.get(0);
        prdGroupToPrdProgramList   = null;
        prdGroupToPrdProgramList   = prdGroupToPrdProgramDAO.getListPrdProByPrdGroup(productGroupList.get(0));
        log.info("onLoadFirst ::::::: prdGroupToPrdProgramList size ::::::::::::", prdGroupToPrdProgramList.size());

        selectProductProgram = prdGroupToPrdProgramList.get(0).getProductProgram();
        prdProgramToCreditTypeList = null;
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(selectProductProgram);
        log.info("onLoadFirst ::::::: selectProductProgram.name :::::::::::: "+selectProductProgram.getName());

        selectCreditType = prdProgramToCreditTypeList.get(0).getCreditType();
        log.info("onLoadFirst ::::::: selectCreditType.name :::::::::::: "+selectCreditType.getName());
    }

    public void onChangeProductGroup(){
        log.info("onChangeProductGroup :::::::: selectProductGroup.id ::::: " + selectProductGroup.getId());
        ProductGroup productGroup  = productGroupDAO.findById(selectProductGroup.getId());
        prdGroupToPrdProgramList   = null;
        prdGroupToPrdProgramList   = prdGroupToPrdProgramDAO.getListPrdProByPrdGroup(productGroup);
        log.info("onChangeProductGroup :::::::: prdGroupToPrdProgramList size :::: ", prdGroupToPrdProgramList.size());

        selectProductProgram = prdGroupToPrdProgramList.get(0).getProductProgram();
        log.info("onChangeProductGroup ::::::: selectProductProgram.id :::: "+selectProductProgram.getId());
        prdProgramToCreditTypeList = null;
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(selectProductProgram);

        log.info("onChangeProductGroup :::::::: facilityList.size :::::::::::" + facilityList.size());

        if (facilityList.size() > 0) {
            facilityList.removeAll(facilityList);
        }

        onLoadSelectOneListbox();

    }

    public void onChangeProductProgram(){
        ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
        log.info("onChangeProductProgram :::: productProgram.Id :::  " + productProgram.getId());
        log.info("onChangeProductProgram :::: productProgram.name ::: " + productProgram.getName());
        prdProgramToCreditTypeList = null;
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(productProgram);
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

    public void onClickDialog() {
        onLoadSelectOneListbox();
        /*facility.setRequestAmount(null) ;*/
        modeForButton = "add";
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
        businessGroupID = businessInformation.getBusinessDescription().getBusinessGroup().getId();

        businessGroup = businessGroupDAO.findById(businessGroupID);
        businessDescription = businessDescriptionDAO.findById(businessDescriptionID);

        businessDescription.setBusinessGroup(businessGroup);

        businessInfo.setBusinessDescription(businessDescription);

        businessInformationList.add(businessInfo);

        businessInformation = new BusinessInformation();
        businessGroup = new BusinessGroup();
        businessDescription = new BusinessDescription();

        businessDescription.setBusinessGroup(businessGroup);
        businessInformation.setBusinessDescription(businessDescription);
    }

    public void onRowBusinessInformation(int index) {

        log.info("onRowBusinessInformation {}", index);

        businessInformation = new BusinessInformation();
        businessGroup = new BusinessGroup();
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
        businessGroupID = businessInfo.getBusinessDescription().getBusinessGroup().getId();

        businessGroup = businessGroupDAO.findById(businessGroupID);
        businessDescription = businessDescriptionDAO.findById(businessDescriptionID);

        modeForBusiness = "add";

        businessInfo.setBusinessDescription(businessDescription);

        businessInformation = new BusinessInformation();
        businessGroup = new BusinessGroup();
        businessDescription = new BusinessDescription();

        businessDescription.setBusinessGroup(businessGroup);
        businessInformation.setBusinessDescription(businessDescription);

        businessGroupID = 0;
        businessDescriptionID = 0;

    }

    public void onRemoveBusinessInformation(int index) {

        businessInformationList.remove(index);

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

    public void onChangeCreditType(){
        log.info("onChangeCreditType ::: selectCreditType.Id() >>> " + selectCreditType.getId());
//        CreditType creditType  = creditTypeDao.findById(selectCreditType.getId());
//        log.info("onChangeCreditType ::: selectCreditType.Name() >>> " + creditType.getName());
    }

    public void onAddFacility() {
        log.info("onAddFacility:::");
        log.info("onAddFacility::: selectProductProgram.getId() :: "+
                productProgramDAO.findById(selectProductProgram.getId()).toString());
        log.info("onAddFacility::: selectCreditType.getId() :: "+
                creditTypeDao.findById(selectCreditType.getId()).toString());

        Facility facAdd   = new Facility();
        ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
        CreditType creditType         = creditTypeDao.findById(selectCreditType.getId());
        facAdd.setProductProgram(productProgram);
        facAdd.setCreditType(creditType);
        facAdd.setRequestAmount(facility.getRequestAmount());
        facilityList.add(facAdd);
    }

    // open dialog
    public void onSelectedFacility(int rowNumber) {
        modeForButton   = "edit";
        rowIndex        =  rowNumber;
        log.info("onSelectedFacility :::  rowNumber  :: "+ rowNumber );
        log.info("onSelectedFacility ::: facilityList.get(rowNumber).getFacilityName :: "+ rowNumber +"  "
                + facilityList.get(rowNumber).getCreditType().getId());
        log.info("onSelectedFacility ::: facilityList.get(rowNumber).getProductProgramName :: "+ rowNumber +"  "
                + facilityList.get(rowNumber).getProductProgram().getId());
        log.info("onSelectedFacility ::: facilityList.get(rowNumber).getRequestAmount :: "+ rowNumber +"  "
                + facilityList.get(rowNumber).getRequestAmount());

        selectProductProgram = facilityList.get(rowNumber).getProductProgram();
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(selectProductProgram);
        selectCreditType     = facilityList.get(rowNumber).getCreditType();
        facility.setProductProgram(selectProductProgram);
        facility.setCreditType(selectCreditType);
        facility.setRequestAmount(facilityList.get(rowNumber).getRequestAmount());

    }

    public void onEditFacility() {
        ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
        CreditType creditType         = creditTypeDao.findById(selectCreditType.getId());
        log.info("onEditFacility :::::: selectProductProgram ::: "+selectProductProgram.getName());
        log.info("onEditFacility :::::: selectCreditType ::: "+selectCreditType.getName());
        facilityList.get(rowIndex).setProductProgram(productProgram);
        facilityList.get(rowIndex).setCreditType(creditType);
        facilityList.get(rowIndex).setRequestAmount(facility.getRequestAmount());
    }

    public void onDeleteFacility(int row) {
        facilityList.remove(row);
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

    public List<Facility> getFacilityList() {
        return facilityList;
    }

    public void setFacilityList(List<Facility> facilityList) {
        this.facilityList = facilityList;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public List<CreditType> getCreditTypeList() {
        return creditTypeList;
    }

    public void setCreditTypeList(List<CreditType> creditTypeList) {
        this.creditTypeList = creditTypeList;
    }

    public CreditType getSelectCreditType() {
        return selectCreditType;
    }

    public void setSelectCreditType(CreditType selectCreditType) {
        this.selectCreditType = selectCreditType;
    }

    public List<ProductProgram> getProductProgramList() {
        return productProgramList;
    }

    public void setProductProgramList(List<ProductProgram> productProgramList) {
        this.productProgramList = productProgramList;
    }

    public ProductProgram getSelectProductProgram() {
        return selectProductProgram;
    }

    public void setSelectProductProgram(ProductProgram selectProductProgram) {
        this.selectProductProgram = selectProductProgram;
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

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<ProductGroup> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public ProductGroup getSelectProductGroup() {
        return selectProductGroup;
    }

    public void setSelectProductGroup(ProductGroup selectProductGroup) {
        this.selectProductGroup = selectProductGroup;
    }

}
