package com.clevel.selos.controller;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.CreditTypeDAO;
import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.dao.master.ProductProgramDAO;
import com.clevel.selos.model.db.Facility;
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
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name="prescreenMaker")
public class PrescreenMaker implements Serializable {
    @Inject
    Logger log;
    @Inject
    MessageProvider msg;

    private List<Facility> facilityList;
    private List<CreditType> creditTypeList;
    private List<ProductGroup> productGroupList;
    private List<ProductProgram> productProgramList;
    private Facility facility;
    private String dlgCreditTypeName;
    private String productProgramName;
    private String productProgramNameDlg;
    private String modeForButton ;
    private int rowIndex;

    @Inject
    private CreditTypeDAO    creditTypeDao;
    @Inject
    private ProductGroupDAO   productGroupDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;

    public PrescreenMaker() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");

        modeForButton = "add";

        if(facilityList == null){
            facilityList = new ArrayList<Facility>();
        }

        if(facility == null){
            facility = new Facility();
        }

        if(creditTypeList == null){
            creditTypeList = new ArrayList<CreditType>();
        }

        if(productGroupList == null){
            productGroupList = new ArrayList<ProductGroup>();
        }

        if(productProgramList == null){
            productProgramList = new ArrayList<ProductProgram>();
        }

        productProgramList  = productProgramDAO.findAll();
        productGroupList    = productGroupDAO.findAll();
        creditTypeList      = creditTypeDao.findAll();

    }


    // table facility
    public void onAddFacility(){
        log.info("dlgCreditTypeName >> ", dlgCreditTypeName);
        Facility facAdd = new Facility();
        facAdd.setFacilityName(dlgCreditTypeName);
        facAdd.setRequestAmount(facility.getRequestAmount());
        facilityList.add(facAdd);

    }

    public void onSelectedFacility(int rowNumber){
        log.info("facilityList.get(row).getFacilityName()>> ",facilityList.get(rowNumber).getFacilityName() );
        modeForButton = "edit";
        dlgCreditTypeName = facilityList.get(rowNumber).getFacilityName();
        facility.setRequestAmount(facilityList.get(rowNumber).getRequestAmount());
        rowIndex = rowNumber;
    }

    public void onEditFacility(){
        facilityList.get(rowIndex).setFacilityName(dlgCreditTypeName);
        facilityList.get(rowIndex).setRequestAmount(facility.getRequestAmount());
    }

    public void onDeleteFacility(int row){
        facilityList.remove(facilityList.get(row));

    }

    public void onClickDialog(){
        log.info("new facility and productProgramNameDlg >> ", productProgramName);
        dlgCreditTypeName     = null;
        productProgramNameDlg = productProgramName;
        facility.setFacilityName(productProgramNameDlg);
        facility.setRequestAmount(null);
    }

    public void onClickDialogTest(){
        log.info("new facility and productProgramNameDlg >> ", productProgramName);
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


    public String getProductProgramNameDlg() {
        return productProgramNameDlg;
    }

    public void setProductProgramNameDlg(String productProgramNameDlg) {
        this.productProgramNameDlg = productProgramNameDlg;
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


}
