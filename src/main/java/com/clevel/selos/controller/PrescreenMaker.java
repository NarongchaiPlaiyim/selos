package com.clevel.selos.controller;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.CreditTypeDAO;
import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.dao.master.ProductProgramDAO;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.view.Collateral;
import com.clevel.selos.model.view.Facility;
import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.system.MessageProvider;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "prescreenMaker")
public class PrescreenMaker implements Serializable {
    @Inject
    Logger log;
    @Inject
    MessageProvider msg;

    private List<Facility> facilityList;
    private List<CreditType> creditTypeList;
    private List<ProductGroup> productGroupList;
    private List<ProductProgram> productProgramList;
    private CollateralType collateralType;
    private List<CollateralType> collateralTypeList;
    private Collateral collateral;
    private List<Collateral> collateralList;
    private Facility facility;
    private String dlgCreditTypeName;
    private String productProgramName;
    private String modeForButton;
    private int rowIndex;
    private BigDecimal dCollateralAmount;
    private String dCollateralTypeName;
    private int indexEdit = -1;
    private String mode;

    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private CreditTypeDAO creditTypeDao;
    @Inject
    private ProductGroupDAO productGroupDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;

    public PrescreenMaker() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");

        modeForButton = "add";
        mode = "add";

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

        if (collateral == null) {
            collateral = new Collateral();
        }

        if (collateralList == null) {
            collateralList = new ArrayList<Collateral>();
        }

        collateralTypeList = collateralTypeDAO.findAll();
        productProgramList = productProgramDAO.findAll();
        productGroupList = productGroupDAO.findAll();
        creditTypeList = creditTypeDao.findAll();

    }


    // table facility
    public void onAddFacility() {

        Facility facAdd = new Facility();
        facAdd.setProductProgramName(productProgramName);
        facAdd.setFacilityName(dlgCreditTypeName);
        facAdd.setRequestAmount(facility.getRequestAmount());
        facilityList.add(facAdd);

    }

    public void onSelectedFacility(int rowNumber) {
        log.info("facilityList.get(row).getFacilityName()>> ", facilityList.get(rowNumber).getFacilityName());
        modeForButton = "edit";
        productProgramName = facilityList.get(rowNumber).getProductProgramName();
        dlgCreditTypeName = facilityList.get(rowNumber).getFacilityName();
        facility.setRequestAmount(facilityList.get(rowNumber).getRequestAmount());
        rowIndex = rowNumber;
    }

    public void onEditFacility() {
        facilityList.get(rowIndex).setProductProgramName(productProgramName);
        facilityList.get(rowIndex).setFacilityName(dlgCreditTypeName);
        facilityList.get(rowIndex).setRequestAmount(facility.getRequestAmount());
        modeForButton = "add";
    }

    public void onDeleteFacility(int row) {
        facilityList.remove(facilityList.get(row));
    }

    public void onClickDialog() {
        productProgramName = null;
        dlgCreditTypeName = null;
        facility.setRequestAmount(null);
    }

    public void onChangeProductGroup() {

        if (facilityList.size() > 0) {
            log.info("facilityList.size()" + facilityList.size());
        }
//        for(int i = 0 ; i < facilityList.size() ; i++){
//            facilityList.remove(facilityList.get(i));
//        }
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
        mode = "edit";

    }

    public void onEditCollateral() {
        log.info("onEditCollateral {}");

        Collateral colla = null;
        colla = new Collateral();

        colla = collateralList.get(indexEdit);
        colla.setCollateralTypeName(dCollateralTypeName);

        colla.setCollateralAmount(dCollateralAmount);
        mode = "add";
        dCollateralTypeName = "";
        dCollateralAmount = null;
        //collateralList.set(index,colla) ;
    }


    public void onRemoveCollateral(int index) {
        log.info("onRemoveCollateral {}", index);
        collateralList.remove(index);

    }

    public void onClearDlg() {

        dCollateralAmount = null;
        dCollateralTypeName = "";
        mode = "add";
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

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<ProductGroup> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public List<ProductProgram> getProductProgramList() {
        return productProgramList;
    }

    public void setProductProgramList(List<ProductProgram> productProgramList) {
        this.productProgramList = productProgramList;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }


}
