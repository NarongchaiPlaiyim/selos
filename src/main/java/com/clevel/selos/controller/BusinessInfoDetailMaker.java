package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BizProductView;
import com.clevel.selos.model.view.StakeholderView;
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
    private BigDecimal supplierVolumnSum1;
    private BigDecimal supplierVolumnSum2;
    private BigDecimal supplierTermSum;

    private BigDecimal buyerVolumnSum1;
    private BigDecimal buyerVolumnSum2;
    private BigDecimal buyerTermSum;

    private int rowIndex;
    private String modeForButton;
    private String dlgStakeName;
    private int bizGroupId;

    private StakeholderView stakeholderView;
    private List<StakeholderView> supplierList;
    private List<StakeholderView> buyerList;
    private BizProductView bizProductView;
    private List<BizProductView> bizProductViewList;
    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;
    private StakeholderView stakeholderTemp;

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
        stakeholderView = new StakeholderView();
        bizProductView = new BizProductView();
        bizProductViewList = new ArrayList<BizProductView>();
        supplierList = new ArrayList<StakeholderView>();
        buyerList = new ArrayList<StakeholderView>();
        businessGroupList = businessGroupDAO.findAll();
    }

    public void onChangeBusinessGroup(){
        log.info("onChangeBusinessGroup >>> begin ");


        BusinessGroup businessGroup = businessGroupDAO.findById(bizGroupId);
        log.info("onChangeBusinessGroup :::: businessGroup ::: ", businessGroup);

        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
    }

    public void onAddBizProductView(){
        log.info("onAddBizProductView >>> begin ");
        bizProductView = new BizProductView();
        bizProductView.reset();
        modeForButton = "add";
    }

    public void onEditBizProductView(int row) {
        modeForButton = "edit";
        rowIndex = row;
        //*** Check list size ***//
        if( rowIndex < bizProductViewList.size() ) {
            bizProductView = bizProductViewList.get(row);
        }
    }

    public void onDeleteBizProductView(int row) {
        bizProductViewList.remove(row);
    }

    public void onSaveBizProductView(){
        log.info( " modeForButton is " + modeForButton);
        if(modeForButton.equalsIgnoreCase("add")){
            log.info("onSaveBizProductView add >>> begin ");
            bizProductViewList.add(bizProductView);
            bizProductView = new BizProductView();
        }else if(modeForButton.equalsIgnoreCase("edit")){
            log.info("onSaveBizProductView edit >>> begin ");
            BizProductView bizTemp;
            bizTemp = new BizProductView();
            bizTemp = bizProductViewList.get(rowIndex);

            log.info("onSaveBizProductView edit >>> bizProductView form UI" + bizProductView);
            bizTemp =  bizProductView;
            log.info("onSaveBizProductView edit >>> bizTemp after Save" + bizTemp);

            bizProductView = new BizProductView();
        }
    }

    public void onAddStakeholderView(String uiStakeholderView){
        log.info("onAddStakeholderView >>> begin dlgStakeName ");
        stakeholderView = new StakeholderView();
        stakeType = uiStakeholderView;
        modeForButton = "add";
        log.info("stakeType >>> {}",stakeType);
        if(stakeType.equalsIgnoreCase("supplier")){
            dlgStakeName = msg.get("app.supplierName");
        }else if(stakeType.equalsIgnoreCase("buyer")){
            dlgStakeName =msg.get("app.buyerName");
        }

        log.info("dlgStakeName is {}",dlgStakeName);
    }

    public void onEditStakeholderView(int row,String uiStakeholderView) {
        modeForButton = "edit";
        rowIndex = row;
        stakeType = uiStakeholderView;
        //*** Check list size ***//
        if(stakeType.equalsIgnoreCase("supplier")){
            if( rowIndex < supplierList.size() ) {
                stakeholderView = supplierList.get(row);
            }
        }else if(stakeType.equalsIgnoreCase("buyer")){
            if( rowIndex < buyerList.size() ) {
                stakeholderView = buyerList.get(row);
            }
        }
        if(supplierList.size()>0){
            //stakeholderTemp = supplierList.get(rowIndex);
            stakeholderTemp = new StakeholderView();
            stakeholderTemp.setPercentCredit(supplierList.get(row).getPercentCredit());
            stakeholderTemp.setPercentCash(supplierList.get(row).getPercentCash());
            log.info("stakeholderTemp Temp file {}",stakeholderTemp);

        }
    }

    public void onDeleteStakeholderView(int row,String uiStakeholderView) {
        stakeType =     uiStakeholderView;
        if(stakeType.equalsIgnoreCase("supplier")){
            supplierList.remove(row);
            calSumStakeholderView(supplierList, stakeType);
        }else if(stakeType.equalsIgnoreCase("buyer")){
            buyerList.remove(row);
            calSumStakeholderView(buyerList, stakeType);
        }
    }

    public void onSaveStakeholderView(){
        boolean supplier;
        boolean buyer;

        if(stakeholderTemp !=null){
            log.info("stakeholderTemp Temp file {} ",stakeholderTemp);
        }

        log.info("stakeholderView dialog {} ",stakeholderView);

        if(modeForButton.equalsIgnoreCase("add")){
             if(stakeType.equals("supplier")){
                supplierList.add(stakeholderView);
                supplier =calSumStakeholderView(supplierList, stakeType);
                 if(!supplier){
                     supplierList.remove(stakeholderView);
                     supplier =calSumStakeholderView(supplierList, stakeType);
                 }
             }else if(stakeType.equals("buyer")){
                buyerList.add(stakeholderView);
                 buyer = calSumStakeholderView(buyerList, stakeType);
                 if(!buyer){
                     buyerList.remove(stakeholderView);
                     buyer =calSumStakeholderView(supplierList, stakeType);
                 }
             }
         }else if(modeForButton.equalsIgnoreCase("edit")){

             if(stakeType.equals("supplier")){
                 //stakeholderTemp = supplierList.get(rowIndex);

                 log.info( "getPercentCash before  is " +  supplierList.get(rowIndex).getPercentCash());
                 log.info( "getPercentCash before  is " +  stakeholderTemp.getPercentCash());

                 supplier = calSumStakeholderView(supplierList, stakeType);
                 log.info( "summary < 100  " + supplier);

                 if(supplier){
                     log.info( "do change value " );
                     stakeholderTemp = stakeholderView;
                     log.info( "getPercentCash progress change is " +  stakeholderTemp.getPercentCash());
                 }else{
                     log.info( "do not change value " );
                     log.info( "getPercentCash progress not change is " +  stakeholderTemp.getPercentCash());
                 }

                 log.info( "getPercentCash final is " +  stakeholderTemp.getPercentCash());

                 supplier = calSumStakeholderView(supplierList, stakeType);
             }else if(stakeType.equals("buyer")){
                 //stakeholderTemp = buyerList.get(rowIndex);
                 buyer = calSumStakeholderView(buyerList, stakeType);
                 if(buyer){
                     stakeholderTemp = stakeholderView;
                 }
                 buyer = calSumStakeholderView(buyerList, stakeType);
             }
         }
        stakeholderView = new StakeholderView();
    }

    private boolean calSumStakeholderView(List<StakeholderView> stakeholdersCalList,String stakeholder){
        log.info("calSumStakeholderView {}",stakeholder);
        float summ1 = 0.0f;
        float summ2 = 0.0f;
        float summ3 = 0.0f;

        StakeholderView stakeholdersCal;

        for(int i=0 ; i<stakeholdersCalList.size(); i++){
            stakeholdersCal = stakeholdersCalList.get(i);
            summ1 += stakeholdersCal.getPercentSalesVolumn().doubleValue();
            summ2 += stakeholdersCal.getPercentCredit().doubleValue() + stakeholdersCal.getPercentCash().doubleValue();
            summ3 += stakeholdersCal.getCreditTerm().doubleValue();
        }
        if(summ1>99.999 ||summ2>99.999 || summ3>99.999 ){
            return false;
        }

        if(stakeType.equals("supplier")){
            supplierVolumnSum1 = new BigDecimal(summ1);
            supplierVolumnSum2 = new BigDecimal(summ2);
            supplierTermSum = new BigDecimal(summ3);
            log.info("supplierSum1 {}",supplierVolumnSum1 +"  supplierSum2{} "+ supplierVolumnSum2 + " supplierTermSum " + supplierTermSum);
        }else if(stakeType.equals("buyer")){
            buyerVolumnSum1 = new BigDecimal(summ1);
            buyerVolumnSum2 = new BigDecimal(summ2);
            buyerTermSum = new BigDecimal(summ3);
            log.info("buyerSum1    {}",buyerVolumnSum1    +"  buyerSum2{} "   + buyerVolumnSum2 +" buyerTermSum "+ buyerTermSum);
        }

        return true;
    }

    public StakeholderView getStakeholderView() {
        return stakeholderView;
    }

    public void setStakeholderView(StakeholderView stakeholderView) {
        this.stakeholderView = stakeholderView;
    }

    public List<StakeholderView> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<StakeholderView> supplierList) {
        this.supplierList = supplierList;
    }

    public List<StakeholderView> getBuyerList() {
        return buyerList;
    }

    public void setBuyerList(List<StakeholderView> buyerList) {
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

    public List<BizProductView> getBizProductViewList() {
        return bizProductViewList;
    }

    public void setBizProductViewList(List<BizProductView> bizProductViewList) {
        this.bizProductViewList = bizProductViewList;
    }

    public BizProductView getBizProductView() {
        return bizProductView;
    }

    public void setBizProductView(BizProductView bizProductView) {
        this.bizProductView = bizProductView;
    }



    public String getDlgStakeName() {
        return dlgStakeName;
    }

    public void setDlgStakeName(String dlgStakeName) {
        this.dlgStakeName = dlgStakeName;
    }

    public int getBizGroupId() {
        return bizGroupId;
    }

    public void setBizGroupId(int bizGroupId) {
        this.bizGroupId = bizGroupId;
    }



    public BigDecimal getBuyerTermSum() {
        return buyerTermSum;
    }

    public void setBuyerTermSum(BigDecimal buyerTermSum) {
        this.buyerTermSum = buyerTermSum;
    }

    public BigDecimal getBuyerVolumnSum2() {
        return buyerVolumnSum2;
    }

    public void setBuyerVolumnSum2(BigDecimal buyerVolumnSum2) {
        this.buyerVolumnSum2 = buyerVolumnSum2;
    }

    public BigDecimal getBuyerVolumnSum1() {
        return buyerVolumnSum1;
    }

    public void setBuyerVolumnSum1(BigDecimal buyerVolumnSum1) {
        this.buyerVolumnSum1 = buyerVolumnSum1;
    }

    public BigDecimal getSupplierTermSum() {
        return supplierTermSum;
    }

    public void setSupplierTermSum(BigDecimal supplierTermSum) {
        this.supplierTermSum = supplierTermSum;
    }

    public BigDecimal getSupplierVolumnSum2() {
        return supplierVolumnSum2;
    }

    public void setSupplierVolumnSum2(BigDecimal supplierVolumnSum2) {
        this.supplierVolumnSum2 = supplierVolumnSum2;
    }

    public BigDecimal getSupplierVolumnSum1() {
        return supplierVolumnSum1;
    }

    public void setSupplierVolumnSum1(BigDecimal supplierVolumnSum1) {
        this.supplierVolumnSum1 = supplierVolumnSum1;
    }
}
