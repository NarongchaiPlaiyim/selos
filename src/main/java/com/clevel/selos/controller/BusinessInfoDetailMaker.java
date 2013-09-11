package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.view.BizProduct;
import com.clevel.selos.model.view.Stakeholder;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 5/9/2556
 * Time: 16:26 น.
 * To change this template use File | Settings | File Templates.
 */
@ViewScoped
@ManagedBean(name = "businessInfoDetail")
public class BusinessInfoDetailMaker implements Serializable {

    @NormalMessage
    @Inject
    Message msg;



    private String stakeType;
    private BigDecimal supplierSum1;
    private BigDecimal supplierSum2;

    private BigDecimal buyerSum1;
    private BigDecimal buyerSum2;
    private int rowIndex;
    private String modeForButton;
    private String dlgStakeName;

    private Stakeholder stakeholder;
    private List<Stakeholder> supplierList;
    private List<Stakeholder> buyerList;
    private BizProduct bizProduct;
    private List<BizProduct> bizProductList;
    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;


    @Inject
    Logger log;
    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;

    public BusinessInfoDetailMaker(){

    }

    @PostConstruct
    public void onCreation(){
        stakeholder = new Stakeholder();
        bizProduct = new BizProduct();
        bizProductList = new ArrayList<BizProduct>();
        supplierList = new ArrayList<Stakeholder>();
        buyerList = new ArrayList<Stakeholder>();
        businessGroupList = businessGroupDAO.findAll();
    }

    public void onChangeBusinessGroup(){
        log.info("onChangeBusinessGroup >>> begin ");
        businessDescriptionList = businessDescriptionDAO.findAll();
    }

    public void onAddBizProduct(){
        log.info("onAddBizProduct >>> begin ");
        bizProduct = new BizProduct();
        bizProduct.reset();
        modeForButton = "add";
    }

    public void onEditBizProduct(int row) {
        modeForButton = "edit";
        rowIndex = row;
        //*** Check list size ***//
        if( rowIndex < bizProductList.size() ) {
            bizProduct = bizProductList.get(row);
        }
    }

    public void onDeleteBizProduct(int row) {
        bizProductList.remove(row);
    }

    public void onSaveBizProduct(){
        log.info( " modeForButton is " + modeForButton);
        if(modeForButton.equalsIgnoreCase("add")){
            log.info("onSaveBizProduct add >>> begin ");
            bizProductList.add(bizProduct);
            bizProduct = new BizProduct();
        }else if(modeForButton.equalsIgnoreCase("edit")){
            log.info("onSaveBizProduct edit >>> begin ");
            BizProduct bizTemp;
            bizTemp = new BizProduct();
            bizTemp = bizProductList.get(rowIndex);

            log.info("onSaveBizProduct edit >>> bizProduct form UI" + bizProduct);
            bizTemp =  bizProduct;
            log.info("onSaveBizProduct edit >>> bizTemp after Save" + bizTemp);

            bizProduct = new BizProduct();
        }
    }

    public void onAddStakeholder(String uiStakeholder){
        log.info("onAddStakeholder >>> begin dlgStakeName ");
        stakeholder = new Stakeholder();
        stakeType = uiStakeholder;
        modeForButton = "add";
        log.info("stakeType >>> {}",stakeType);
        if(stakeType.equalsIgnoreCase("supplier")){
            dlgStakeName = msg.get("app.supplierName");
        }else if(stakeType.equalsIgnoreCase("buyer")){
            dlgStakeName =msg.get("app.buyerName");
        }

        log.info("dlgStakeName is {}",dlgStakeName);
    }

    public void onEditStakeholder(int row,String uiStakeholder) {
        modeForButton = "edit";
        rowIndex = row;
        stakeType = uiStakeholder;
        //*** Check list size ***//
        if(stakeType.equalsIgnoreCase("supplier")){
            if( rowIndex < supplierList.size() ) {
                stakeholder = supplierList.get(row);
            }
        }else if(stakeType.equalsIgnoreCase("buyer")){
            if( rowIndex < buyerList.size() ) {
                stakeholder = buyerList.get(row);
            }
        }
    }

    public void onDeleteStakeholder(int row,String uiStakeholder) {
        stakeType =     uiStakeholder;
        if(stakeType.equalsIgnoreCase("supplier")){
            supplierList.remove(row);
            calSumStakeholder(supplierList,stakeType);
        }else if(stakeType.equalsIgnoreCase("buyer")){
            buyerList.remove(row);
            calSumStakeholder(buyerList,stakeType);
        }
    }

    public void onSaveStakeholder(){
         log.info("stakeholder {}",stakeholder);
         if(modeForButton.equalsIgnoreCase("add")){
             if(stakeType.equals("supplier")){
                supplierList.add(stakeholder);
                 calSumStakeholder(supplierList,stakeType);
             }else if(stakeType.equals("buyer")){
                buyerList.add(stakeholder);
                calSumStakeholder(buyerList,stakeType);
             }
         }else if(modeForButton.equalsIgnoreCase("edit")){
             Stakeholder stakeholderTemp;
             stakeholderTemp = new Stakeholder();
             if(stakeType.equals("supplier")){
                 stakeholderTemp = supplierList.get(rowIndex);
                 calSumStakeholder(supplierList,stakeType);
             }else if(stakeType.equals("buyer")){
                 stakeholderTemp = buyerList.get(rowIndex);
                 calSumStakeholder(buyerList,stakeType);
             }
             stakeholderTemp = stakeholder;
         }
        stakeholder = new Stakeholder();
    }

    private void calSumStakeholder(List<Stakeholder> stakeholdersCalList,String stakeholder){
        log.info("calSumStakeholder {}",stakeholder);
        float summ1 = 0.0f;
        float summ2 = 0.0f;
        Stakeholder stakeholdersCal;

        for(int i=0 ; i<stakeholdersCalList.size(); i++){
            stakeholdersCal = stakeholdersCalList.get(i);
            summ1 += stakeholdersCal.getPercentSalesVolumn().doubleValue();
            summ2 += stakeholdersCal.getPercentCredit().doubleValue();
        }
        if(stakeType.equals("supplier")){
            supplierSum1 = new BigDecimal(summ1);
            supplierSum2 = new BigDecimal(summ2);
            log.info("supplierSum1 {}",supplierSum1 +"  supplierSum2{}"+ supplierSum2);
        }else if(stakeType.equals("buyer")){
            buyerSum1 = new BigDecimal(summ1);
            buyerSum2 = new BigDecimal(summ2);
            log.info("buyerSum1    {}",buyerSum1    +"  buyerSum2{}"   + buyerSum2);

        }

    }

    public Stakeholder getStakeholder() {
        return stakeholder;
    }

    public void setStakeholder(Stakeholder stakeholder) {
        this.stakeholder = stakeholder;
    }

    public List<Stakeholder> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<Stakeholder> supplierList) {
        this.supplierList = supplierList;
    }

    public List<Stakeholder> getBuyerList() {
        return buyerList;
    }

    public void setBuyerList(List<Stakeholder> buyerList) {
        this.buyerList = buyerList;
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

    public List<BizProduct> getBizProductList() {
        return bizProductList;
    }

    public void setBizProductList(List<BizProduct> bizProductList) {
        this.bizProductList = bizProductList;
    }

    public BizProduct getBizProduct() {
        return bizProduct;
    }

    public void setBizProduct(BizProduct bizProduct) {
        this.bizProduct = bizProduct;
    }

    public BigDecimal getBuyerSum2() {
        return buyerSum2;
    }

    public void setBuyerSum2(BigDecimal buyerSum2) {
        this.buyerSum2 = buyerSum2;
    }

    public BigDecimal getBuyerSum1() {
        return buyerSum1;
    }

    public void setBuyerSum1(BigDecimal buyerSum1) {
        this.buyerSum1 = buyerSum1;
    }

    public BigDecimal getSupplierSum2() {
        return supplierSum2;
    }

    public void setSupplierSum2(BigDecimal supplierSum2) {
        this.supplierSum2 = supplierSum2;
    }

    public BigDecimal getSupplierSum1() {
        return supplierSum1;
    }

    public void setSupplierSum1(BigDecimal supplierSum1) {
        this.supplierSum1 = supplierSum1;
    }

    public String getDlgStakeName() {
        log.info("getDlgStakeName {}",dlgStakeName);
        return dlgStakeName;
    }

    public void setDlgStakeName(String dlgStakeName) {
        log.info("setDlgStakeName {}",dlgStakeName);
        this.dlgStakeName = dlgStakeName;
    }
}
