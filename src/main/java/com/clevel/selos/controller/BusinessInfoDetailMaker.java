package com.clevel.selos.controller;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BizInfoFullView;
import com.clevel.selos.model.view.BizProductView;
import com.clevel.selos.model.view.StakeholderView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
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

    private BizProductView selectBizProduct;
    private List<BizProductView> bizProductViewList;
    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;
    private StakeholderView stakeholderTemp;
    private StakeholderView selectStakeholer;

    private BizInfoFullView bizInfoFullApp;

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

    public void onEditBizProductView() {
        log.info( " onEditBizProductView is " + selectBizProduct);
        modeForButton = "edit";
        bizProductView = new BizProductView();
        //*** Check list size ***//
        if( rowIndex < bizProductViewList.size() ) {
            bizProductView.setProductType(selectBizProduct.getProductType());
            bizProductView.setPercentSalesVolumn(selectBizProduct.getPercentSalesVolumn());
            bizProductView.setPercentEBIT(selectBizProduct.getPercentEBIT());
            bizProductView.setProductDetail(selectBizProduct.getProductDetail());
        }
    }

    public void onDeleteBizProductView() {
        log.info( " onDeleteBizProductView is " + selectBizProduct);
        bizProductViewList.remove(selectBizProduct);
    }

    public void onSaveBizProductView(){
        boolean complete = false;
        log.info( " modeForButton is " + modeForButton);

        log.info( "context.addCallbackParam " );
        RequestContext context = RequestContext.getCurrentInstance();

        if(!bizProductView.getProductType().equals("")&&!bizProductView.getProductDetail().equals("")&&!bizProductView.getPercentSalesVolumn().equals("")&&!bizProductView.getPercentEBIT().equals("")){
            if(modeForButton.equalsIgnoreCase("add")){
                log.info("onSaveBizProductView add >>> begin ");
                bizProductViewList.add(bizProductView);
                bizProductView = new BizProductView();
            }else if(modeForButton.equalsIgnoreCase("edit")){
                log.info("onSaveBizProductView edit >>> begin ");
                BizProductView bizTemp;
                bizTemp = bizProductViewList.get(rowIndex);
                bizTemp.setProductType(bizProductView.getProductType());
                bizTemp.setPercentSalesVolumn(bizProductView.getPercentSalesVolumn());
                bizTemp.setPercentEBIT(bizProductView.getPercentEBIT());
                bizTemp.setProductDetail(bizProductView.getProductDetail());

                bizProductView = new BizProductView();
            }
            complete = true;
        }

        context.addCallbackParam("functionComplete", complete);
    }

    public void onAddStakeholderView(){
        log.info("onAddStakeholderView >>> label is  " + stakeType );
        modeForButton = "add";
        onSetLabelStakeholder();
        stakeholderView = new StakeholderView();

    }

    public void onEditStakeholderView() {
        modeForButton = "edit";
        onSetLabelStakeholder();
        stakeholderView = new StakeholderView();
        stakeholderView = onSetStakeholder(stakeholderView,selectStakeholer);
    }

    private void onSetLabelStakeholder(){
        log.info("onSetLabelStakeholder >>> label is  " + stakeType );

        if(stakeType.equalsIgnoreCase("supplier")){
            dlgStakeName = msg.get("app.supplierName");
        }else if(stakeType.equalsIgnoreCase("buyer")){
            dlgStakeName =msg.get("app.buyerName");
        }

        log.info("onSetLabelStakeholder >>> label is  " + dlgStakeName );
    }

    private StakeholderView onSetStakeholder(StakeholderView stakeholderMaster ,StakeholderView stakeholderChild){

        stakeholderMaster.setName(stakeholderChild.getName());
        stakeholderMaster.setContactName(stakeholderChild.getContactName());
        stakeholderMaster.setPhoneNo(stakeholderChild.getPhoneNo());
        stakeholderMaster.setContactYear(stakeholderChild.getContactYear());
        stakeholderMaster.setPercentSalesVolumn(stakeholderChild.getPercentSalesVolumn());
        stakeholderMaster.setPercentCash(stakeholderChild.getPercentCash());
        stakeholderMaster.setPercentCredit(stakeholderChild.getPercentCredit());
        stakeholderMaster.setCreditTerm(stakeholderChild.getCreditTerm());

        return stakeholderMaster;
    }

    public void onDeleteStakeholderView() {
        log.info("onDeleteStakeholderView is " + selectStakeholer);

        log.info("onDeleteStakeholderView stakeType is " + stakeType);

        if(stakeType.equalsIgnoreCase("supplier")){
            supplierList.remove(selectStakeholer);
            calSumStakeholderView(supplierList, stakeType);
        }else if(stakeType.equalsIgnoreCase("buyer")){
            buyerList.remove(selectStakeholer);
            calSumStakeholderView(buyerList, stakeType);
        }
    }

    public void onSaveStakeholderView(){

        log.info( "onSaveStakeholderView modeForButton is " +  modeForButton + "   stakeType is " + stakeType);

        boolean supplier;
        boolean buyer;
        StakeholderView  stakeholderRow;

        if(modeForButton.equalsIgnoreCase("add")){
             if(stakeType.equals("supplier")){
                supplierList.add(stakeholderView);
                supplier =calSumStakeholderView(supplierList, stakeType);
                 if(!supplier){
                     supplierList.remove(stakeholderView);
                     calSumStakeholderView(supplierList, stakeType);
                 }
             }else if(stakeType.equals("buyer")){
                buyerList.add(stakeholderView);
                 buyer = calSumStakeholderView(buyerList, stakeType);
                 if(!buyer){
                     buyerList.remove(stakeholderView);
                     calSumStakeholderView(buyerList, stakeType);
                 }
             }
        }else if(modeForButton.equalsIgnoreCase("edit")){
            if(stakeType.equals("supplier")){
                 stakeholderRow = supplierList.get(rowIndex);
                 stakeholderRow = onSetStakeholder(stakeholderRow,stakeholderView);
                 supplierList.set(rowIndex, stakeholderRow);
                 supplier = calSumStakeholderView(supplierList, stakeType);

                 if(!supplier){
                     stakeholderRow = onSetStakeholder(stakeholderRow,stakeholderTemp);
                     supplierList.set(rowIndex,stakeholderRow);
                     calSumStakeholderView(supplierList, stakeType);
                 }

            }else if(stakeType.equals("buyer")){
                stakeholderRow = buyerList.get(rowIndex);
                stakeholderRow = onSetStakeholder(stakeholderRow,stakeholderView);
                buyerList.set(rowIndex, stakeholderRow);
                buyer = calSumStakeholderView(buyerList, stakeType);

                if(!buyer){
                    stakeholderRow = onSetStakeholder(stakeholderRow,stakeholderTemp);
                    buyerList.set(rowIndex,stakeholderRow);
                    calSumStakeholderView(buyerList, stakeType);
                }
            }
        }
        stakeholderView = new StakeholderView();
    }

    private boolean calSumStakeholderView(List<StakeholderView> stakeholdersCalList,String stakeholder){
        float summ1 = 0.0f;
        float summ2 = 0.0f;
        int summ3 = 0;

        StakeholderView stakeholdersCal;

        for(int i=0 ; i<stakeholdersCalList.size(); i++){
            stakeholdersCal = stakeholdersCalList.get(i);

            //log.info("stakeholdersCal getPercentCredit{} " + i + " is " + stakeholdersCal);

            summ1 += stakeholdersCal.getPercentSalesVolumn().doubleValue();
            summ2 += stakeholdersCal.getPercentCredit().doubleValue() + stakeholdersCal.getPercentCash().doubleValue();
            summ3 += stakeholdersCal.getCreditTerm().intValue();

            //log.info("Summation at " + i + " is supplierSum1 {}"+ summ1 + "  supplierSum2{} "+ summ2 + " supplierTermSum " + summ3);

        }
        if(summ1>99.999 ||summ2>99.999 ){
            return false;
        }

        if(stakeType.equals("supplier")){
            supplierVolumnSum1 = new BigDecimal(summ1);
            supplierVolumnSum2 = new BigDecimal(summ2);
            supplierTermSum = new BigDecimal(summ3);
        }else if(stakeType.equals("buyer")){
            buyerVolumnSum1 = new BigDecimal(summ1);
            buyerVolumnSum2 = new BigDecimal(summ2);
            buyerTermSum = new BigDecimal(summ3);
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

    public BizProductView getSelectBizProduct() {
        return selectBizProduct;
    }

    public void setSelectBizProduct(BizProductView selectBizProduct) {
        this.selectBizProduct = selectBizProduct;
    }

    public StakeholderView getSelectStakeholer() {
        return selectStakeholer;
    }

    public void setSelectStakeholer(StakeholderView selectStakeholer) {
        this.selectStakeholer = selectStakeholer;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getStakeType() {
        return stakeType;
    }

    public void setStakeType(String stakeType) {
        this.stakeType = stakeType;
    }

    public BizInfoFullView getBizInfoFullApp() {
        return bizInfoFullApp;
    }

    public void setBizInfoFullApp(BizInfoFullView bizInfoFullApp) {
        this.bizInfoFullApp = bizInfoFullApp;
    }
}
