package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.Facility;
import com.clevel.selos.system.MessageProvider;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "prescreenFacilityMaker")
public class PrescreenFacilityMaker implements Serializable {
    @Inject
    Logger log;
    @Inject
    MessageProvider msg;

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

    private String modeForButton;
    private int rowIndex;

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

    public PrescreenFacilityMaker() {

    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");
        modeForButton = "add";

        if (facilityList == null) {
            facilityList = new ArrayList<Facility>();
        }

        if (facility == null) {
            facility = new Facility();
        }

        if (selectProductGroup == null) {
            selectProductGroup = new ProductGroup();
        }

        if (selectProductProgram == null) {
            selectProductProgram = new ProductProgram();
        }

        if (selectCreditType == null) {
            selectCreditType = new CreditType();
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

        if (prdGroupToPrdProgramList == null) {
            prdGroupToPrdProgramList = new ArrayList<PrdGroupToPrdProgram>();
        }

        if (prdProgramToCreditTypeList == null) {
            prdProgramToCreditTypeList = new ArrayList<PrdProgramToCreditType>();
        }

        onLoadSelectOneListbox();
    }


    public void onLoadSelectOneListbox() {
        log.info("onLoadFirst ::::::: ");
        productGroupList = null;
        productGroupList = productGroupDAO.findAll();
        log.info("onLoadFirst :::::::  productGroupList size ::::::::::::", productGroupList.size());

//        selectProductGroup   = productGroupList.get(0);
        log.info("onLoadFirst :::::::  selectProductGroup  ::::::::::::", selectProductGroup.getId());

        prdGroupToPrdProgramList = null;
        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdProByPrdGroup(selectProductGroup);
        log.info("onLoadFirst ::::::: prdGroupToPrdProgramList size ::::::::::::", prdGroupToPrdProgramList.size());

//        selectProductProgram = prdGroupToPrdProgramList.get(0).getProductProgram();
        prdProgramToCreditTypeList = null;
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(selectProductProgram);
        log.info("onLoadFirst ::::::: selectProductProgram.name :::::::::::: " + selectProductProgram.getName());

//        selectCreditType = prdProgramToCreditTypeList.get(0).getCreditType();
        log.info("onLoadFirst ::::::: selectCreditType.name :::::::::::: " + selectCreditType.getName());

    }

    public void onChangeProductGroup() {
        log.info("onChangeProductGroup :::::::: selectProductGroup.id ::::: " + selectProductGroup.getId());
        ProductGroup productGroup = null;
        productGroup = productGroupDAO.findById(selectProductGroup.getId());
        prdGroupToPrdProgramList = null;
        prdGroupToPrdProgramList = prdGroupToPrdProgramDAO.getListPrdProByPrdGroup(productGroup);
        log.info("onChangeProductGroup :::::::: prdGroupToPrdProgramList size :::: ", prdGroupToPrdProgramList.size());

//        selectProductProgram = prdGroupToPrdProgramList.get(0).getProductProgram();
        log.info("onChangeProductGroup ::::::: selectProductProgram.id :::: " + selectProductProgram.getId());
        prdProgramToCreditTypeList = null;
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(selectProductProgram);

        log.info("onChangeProductGroup :::::::: facilityList.size :::::::::::" + facilityList.size());

        if (facilityList.size() > 0) {
            facilityList.removeAll(facilityList);
        }

    }

    public void onChangeProductProgram() {
        ProductProgram productProgram = null;
        productProgram = productProgramDAO.findById(selectProductProgram.getId());
        log.info("onChangeProductProgram :::: productProgram.Id :::  " + productProgram.getId());
        log.info("onChangeProductProgram :::: productProgram.name ::: " + productProgram.getName());
        prdProgramToCreditTypeList = null;
        prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListPrdProByPrdprogram(productProgram);
        log.info("onChangeProductProgram :::: prdProgramToCreditTypeList.size ::: " + prdProgramToCreditTypeList.size());
    }

    public void onChangeCreditType() {
        log.info("onChangeCreditType ::: selectCreditType.Id() >>> " + selectCreditType.getId());
//        CreditType creditType  = creditTypeDao.findById(selectCreditType.getId());
//        log.info("onChangeCreditType ::: selectCreditType.Name() >>> " + creditType.getName());
    }


    public void onAddFacility() {
        log.info("onAddFacility:::");
        log.info("selectProductGroup.getId() >> " + selectProductGroup.getId());
        log.info("selectProductProgram.getId() >> " + selectProductProgram.getId());
        log.info("selectCreditType.getId() >> " + selectCreditType.getId());

        if (!((selectProductGroup.getId() == 0) || (selectProductProgram.getId() == 0) || (selectCreditType.getId() == 0))) {

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
        }

    }

    // open dialog
    public void onSelectedFacility(int rowNumber) {
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

    }

    public void onEditFacility() {
        ProductProgram productProgram = productProgramDAO.findById(selectProductProgram.getId());
        CreditType creditType = creditTypeDao.findById(selectCreditType.getId());
        log.info("onEditFacility :::::: selectProductProgram ::: " + selectProductProgram.getName());
        log.info("onEditFacility :::::: selectCreditType ::: " + selectCreditType.getName());
        facilityList.get(rowIndex).setProductProgram(productProgram);
        facilityList.get(rowIndex).setCreditType(creditType);
        facilityList.get(rowIndex).setRequestAmount(facility.getRequestAmount());
    }

    public void onDeleteFacility(int row) {
        facilityList.remove(row);
    }

    public void onClickDialog() {
        selectProductProgram = new ProductProgram();
        selectCreditType = new CreditType();
        facility.setRequestAmount(null);
        modeForButton = "add";
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

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }

    public void setProductGroupList(List<ProductGroup> productGroupList) {
        this.productGroupList = productGroupList;
    }

    public String getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(String modeForButton) {
        this.modeForButton = modeForButton;
    }

    public ProductProgram getSelectProductProgram() {
        return selectProductProgram;
    }

    public void setSelectProductProgram(ProductProgram selectProductProgram) {
        this.selectProductProgram = selectProductProgram;
    }

    public CreditType getSelectCreditType() {
        return selectCreditType;
    }

    public void setSelectCreditType(CreditType selectCreditType) {
        this.selectCreditType = selectCreditType;
    }

    public ProductGroup getSelectProductGroup() {
        return selectProductGroup;
    }

    public void setSelectProductGroup(ProductGroup selectProductGroup) {
        this.selectProductGroup = selectProductGroup;
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
}
