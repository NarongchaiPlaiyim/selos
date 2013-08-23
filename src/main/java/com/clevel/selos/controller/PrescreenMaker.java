package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.view.Collateral;
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

    private CollateralType collateralType;
    private List<CollateralType> collateralTypeList;
    private Collateral collateral;
    private List<Collateral> collateralList;

    private int indexExistEdit;
    private BigDecimal dCollateralAmount;
    private String dCollateralTypeName;
    private BigDecimal existCollateralAmount;
    private String existCollateralTypeName;
    private int indexEdit = -1;
    private String mode;
    private String modeForExist;

    @Inject
    private CollateralTypeDAO collateralTypeDAO;

    public PrescreenMaker() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.y");

        mode = "add";
        modeForExist = "add";


        if (collateral == null) {
            collateral = new Collateral();
        }

        if (collateralList == null) {
            collateralList = new ArrayList<Collateral>();
        }

        collateralTypeList = collateralTypeDAO.findAll();


    }


/*    public void onLoadDescription() {
        productProgramList = productProgramDAO.findAll();
        selectProductProgram = productProgramList.get(0);
        ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
        log.info("selectProductProgram.getName() >> " + productProgram.getName());

        productProgramName = productProgram.getName();
        CreditType creditType = creditTypeDao.findById(selectCreditType.getId());
        log.info("selectCreditType.getName() >>> " + creditType.getName());
        dlgCreditTypeName  = creditType.getName();
    }


    public void onChangeProductProgram(){
        ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
        log.info("selectProductProgram.getName() >> }" + productProgram.getName());
        productProgramName = productProgram.getName();


    }

    public void onChangeCreditType(){
        CreditType creditType = creditTypeDao.findById(selectCreditType.getId());
        log.info("selectCreditType.getName() >>> }" + creditType.getName());
        dlgCreditTypeName  = creditType.getName();
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
    }

    public void onDeleteFacility(int row) {
        facilityList.remove(row);
    }

    public void onClickDialog() {
        productProgramName = "";
        dlgCreditTypeName  = "";
        facility.setRequestAmount(null);
        onLoadDescription();
        modeForButton = "add";
    }

    public void onChangeProductGroup(){
        log.info("facilityList.size()" + facilityList.size());

        if (facilityList.size() > 0) {
            facilityList.removeAll(facilityList);
        }

        productProgramName = "";
        dlgCreditTypeName  = "";
    }*/


    //********************************************** Collateral ******************************************************//
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

    // ******************************* Existing Collateral  *********************************//

    public void onClearExistDlg() {
        existCollateralAmount = null;
        existCollateralTypeName = "";
        modeForExist = "add";
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


}
