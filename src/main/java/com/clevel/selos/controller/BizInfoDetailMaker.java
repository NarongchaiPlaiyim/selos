package com.clevel.selos.controller;

import com.clevel.selos.busiensscontrol.FullAppBusinessControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizProduct;
import com.clevel.selos.model.db.working.BizStakeholder;
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
@ManagedBean(name = "bizInfoDetail")
public class BizInfoDetailMaker implements Serializable {

    @NormalMessage
    @Inject
    Message msg;

    private String stakeType;
    private BigDecimal supplierVolumeSum1;
    private BigDecimal supplierVolumeSum2;
    private BigDecimal supplierTermSum;

    private BigDecimal buyerVolumeSum1;
    private BigDecimal buyerVolumeSum2;
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
    private StakeholderView selectStakeholder;

    private BizInfoFullView bizInfoFullApp;

    private BusinessGroup bizGroup;
    private BusinessDescription bizDesc;

    @Inject
    Logger log;
    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;
    @Inject
    FullAppBusinessControl fullAppBusinessControl;
    public BizInfoDetailMaker(){

    }

    @PostConstruct
    public void onCreation(){
        stakeholderView = new StakeholderView();
        bizProductView = new BizProductView();
        bizProductViewList = new ArrayList<BizProductView>();
        supplierList = new ArrayList<StakeholderView>();
        buyerList = new ArrayList<StakeholderView>();
        businessGroupList = businessGroupDAO.findAll();
        bizInfoFullApp = new BizInfoFullView();
        bizGroup = new BusinessGroup();
        bizDesc = new BusinessDescription();

        bizInfoFullApp.setBizDesc(bizDesc);
        bizInfoFullApp.setBizGroup(bizGroup);

    }

    public void onChangeBusinessGroup(){
        log.info("onChangeBusinessGroup >>> begin ");
        bizGroupId = bizInfoFullApp.getBizGroup().getId();
        BusinessGroup businessGroup = businessGroupDAO.findById(bizGroupId);
        log.info("onChangeBusinessGroup :::: businessGroup ::: ", businessGroup);
        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
        log.info("onChangeBusinessGroup :::: businessDescriptionList Size ::: ", businessDescriptionList.size());
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
            bizProductView.setPercentSalesVolume(selectBizProduct.getPercentSalesVolume());
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

        if(!bizProductView.getProductType().equals("")&&!bizProductView.getProductDetail().equals("")&&!bizProductView.getPercentSalesVolume().equals("")&&!bizProductView.getPercentEBIT().equals("")){
            if(modeForButton.equalsIgnoreCase("add")){
                log.info("onSaveBizProductView add >>> begin ");
                bizProductViewList.add(bizProductView);
                bizProductView = new BizProductView();
            }else if(modeForButton.equalsIgnoreCase("edit")){
                log.info("onSaveBizProductView edit >>> begin ");
                BizProductView bizTemp;
                bizTemp = bizProductViewList.get(rowIndex);
                bizTemp.setProductType(bizProductView.getProductType());
                bizTemp.setPercentSalesVolume(bizProductView.getPercentSalesVolume());
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
        stakeholderView.setStakeholderType(stakeType);
        stakeholderView = onSetStakeholder(stakeholderView,selectStakeholder);
    }

    private void onSetLabelStakeholder(){


        if(stakeType.equalsIgnoreCase("1")){
            dlgStakeName = msg.get("app.supplierName");
        }else if(stakeType.equalsIgnoreCase("2")){
            dlgStakeName =msg.get("app.buyerName");
        }

        log.info("onSetLabelStakeholder >>> label is  " + dlgStakeName );
    }

    private StakeholderView onSetStakeholder(StakeholderView stakeholderMaster ,StakeholderView stakeholderChild){

        stakeholderMaster.setName(stakeholderChild.getName());
        stakeholderMaster.setContactName(stakeholderChild.getContactName());
        stakeholderMaster.setPhoneNo(stakeholderChild.getPhoneNo());
        stakeholderMaster.setContactYear(stakeholderChild.getContactYear());
        stakeholderMaster.setPercentSalesVolume(stakeholderChild.getPercentSalesVolume());
        stakeholderMaster.setPercentCash(stakeholderChild.getPercentCash());
        stakeholderMaster.setPercentCredit(stakeholderChild.getPercentCredit());
        stakeholderMaster.setCreditTerm(stakeholderChild.getCreditTerm());

        return stakeholderMaster;
    }

    public void onDeleteStakeholderView() {
        log.info("onDeleteStakeholderView is " + selectStakeholder);

        log.info("onDeleteStakeholderView stakeType is " + stakeType);

        if(stakeType.equalsIgnoreCase("1")){
            supplierList.remove(selectStakeholder);
            calSumStakeholderView(supplierList, stakeType);
        }else if(stakeType.equalsIgnoreCase("2")){
            buyerList.remove(selectStakeholder);
            calSumStakeholderView(buyerList, stakeType);
        }
    }

    public void onSaveStakeholderView(){

        log.info( "onSaveStakeholderView modeForButton is " +  modeForButton + "   stakeType is " + stakeType);

        boolean supplier;
        boolean buyer;
        StakeholderView  stakeholderRow;

        if(modeForButton.equalsIgnoreCase("add")){
             if(stakeType.equals("1")){
                supplierList.add(stakeholderView);
                supplier =calSumStakeholderView(supplierList, stakeType);
                 if(!supplier){
                     supplierList.remove(stakeholderView);
                     calSumStakeholderView(supplierList, stakeType);
                 }
             }else if(stakeType.equals("2")){
                buyerList.add(stakeholderView);
                 buyer = calSumStakeholderView(buyerList, stakeType);
                 if(!buyer){
                     buyerList.remove(stakeholderView);
                     calSumStakeholderView(buyerList, stakeType);
                 }
             }
        }else if(modeForButton.equalsIgnoreCase("edit")){
            if(stakeType.equals("1")){
                 stakeholderRow = supplierList.get(rowIndex);
                 stakeholderRow = onSetStakeholder(stakeholderRow,stakeholderView);
                 supplierList.set(rowIndex, stakeholderRow);
                 supplier = calSumStakeholderView(supplierList, stakeType);

                 if(!supplier){
                     stakeholderRow = onSetStakeholder(stakeholderRow,stakeholderTemp);
                     supplierList.set(rowIndex,stakeholderRow);
                     calSumStakeholderView(supplierList, stakeType);
                 }

            }else if(stakeType.equals("2")){
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

            summ1 += stakeholdersCal.getPercentSalesVolume().doubleValue();
            summ2 += stakeholdersCal.getPercentCredit().doubleValue() + stakeholdersCal.getPercentCash().doubleValue();
            summ3 += stakeholdersCal.getCreditTerm().intValue();

            //log.info("Summation at " + i + " is supplierSum1 {}"+ summ1 + "  supplierSum2{} "+ summ2 + " supplierTermSum " + summ3);

        }
        if(summ1>99.999 ||summ2>99.999 ){
            return false;
        }

        if(stakeType.equals("1")){
            supplierVolumeSum1 = new BigDecimal(summ1);
            supplierVolumeSum2 = new BigDecimal(summ2);
            supplierTermSum = new BigDecimal(summ3);
        }else if(stakeType.equals("2")){
            buyerVolumeSum1 = new BigDecimal(summ1);
            buyerVolumeSum2 = new BigDecimal(summ2);
            buyerTermSum = new BigDecimal(summ3);
        }

        return true;
    }

    public void onSaveBizInfoView(){
        //log.info( " bizInfoFullApp is " + bizInfoFullApp);

        /*log.info( " bizProductViewList is " + bizProductViewList);
        bizInfoFullApp.setBizProductViewList(bizProductViewList);

        log.info( " supplierList is " + supplierList);
        bizInfoFullApp.setSupplierList(supplierList);

        log.info( " buyerList is " + buyerList);
        bizInfoFullApp.setBuyerList(buyerList);*/
        //convert View to DB
        BizInfoDetail   bizInfoDetail ;
        bizInfoDetail = new BizInfoDetail();
        bizInfoDetail.setBizInfoText(bizInfoFullApp.getBizInfoText());
        bizInfoDetail.setBusinessType(bizInfoFullApp.getBizType());
        bizInfoDetail.setBusinessGroup(bizInfoFullApp.getBizGroup());
        bizInfoDetail.setBusinessDescription(bizInfoFullApp.getBizDesc());
        bizInfoDetail.setBizCode(bizInfoFullApp.getBizCode());
        bizInfoDetail.setBizComment(bizInfoFullApp.getBizComment());
        bizInfoDetail.setIncomeFactor(bizInfoFullApp.getIncomeFactor());
        bizInfoDetail.setAdjustedIncomeFactor(bizInfoFullApp.getAdjustedIncomeFactor());
        bizInfoDetail.setPercentBiz(bizInfoFullApp.getPercentBiz());

        StakeholderView stakeholderTemp;
        BizStakeholder bizStakeholderTemp;

        BizProductView bizProductViewTemp;
        BizProduct bizProductTemp;

        List<BizStakeholder> bizSupplierList;

        List<BizStakeholder> bizBuyerList;

        List<BizProduct> bizProductList;
        bizProductList = new ArrayList<BizProduct>();

        log.info( " bizProductViewList Size is " + bizProductViewList.size());
        for (int i =0;i<bizProductViewList.size();i++){
            bizProductViewTemp = bizProductViewList.get(i);
            log.info( " stakeholderTemp is " + bizProductViewTemp);
            bizProductTemp = onBizProductTransform(bizProductViewTemp);
            log.info( " bizStakeholderTemp is " + bizProductTemp);
            bizProductList.add(bizProductTemp);
        }

        log.info( " bizProductViewList is " + supplierList.size());
        bizSupplierList = new ArrayList<BizStakeholder>();
        for (int i =0;i<supplierList.size();i++){
            stakeholderTemp = supplierList.get(i);
            log.info( " stakeholderTemp is " + stakeholderTemp);
            bizStakeholderTemp = onStakeholderTransform(stakeholderTemp);
            log.info( " bizStakeholderTemp is " + bizStakeholderTemp);
            bizSupplierList.add( bizStakeholderTemp);
        }

        log.info( " bizProductViewList is " + buyerList.size());
        bizBuyerList = new ArrayList<BizStakeholder>();
        for (int i =0;i<buyerList.size();i++){
            stakeholderTemp = buyerList.get(i);
            log.info( " stakeholderTemp is " + stakeholderTemp);
            bizStakeholderTemp = onStakeholderTransform(stakeholderTemp);
            log.info( " bizStakeholderTemp is " + bizStakeholderTemp);
            bizBuyerList.add(bizStakeholderTemp);
        }


        bizInfoDetail.setSupplierList(bizSupplierList);
        bizInfoDetail.setBuyerList(bizSupplierList);
        bizInfoDetail.setSupplierList(bizSupplierList);
        log.info( "bizInfoDetail before persist is " + bizInfoDetail );
        // xxxxx

        //save to DB
        fullAppBusinessControl.onSaveBizInfoDetailToDB(bizInfoDetail);

    }

    private BizStakeholder onStakeholderTransform(StakeholderView stakeholderView){
        BizStakeholder bizStakeholderTran;

        bizStakeholderTran = new BizStakeholder();
        bizStakeholderTran.setContactName(stakeholderView.getContactName());
        return  bizStakeholderTran;
    }

    private BizProduct onBizProductTransform(BizProductView bizProductView){
        BizProduct bizProductTran;

        bizProductTran = new BizProduct();
        bizProductTran.setProductDetail(bizProductView.getProductDetail());
        return  bizProductTran;
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

    public BigDecimal getBuyerVolumeSum2() {
        return buyerVolumeSum2;
    }

    public void setBuyerVolumeSum2(BigDecimal buyerVolumeSum2) {
        this.buyerVolumeSum2 = buyerVolumeSum2;
    }

    public BigDecimal getBuyerVolumeSum1() {
        return buyerVolumeSum1;
    }

    public void setBuyerVolumeSum1(BigDecimal buyerVolumeSum1) {
        this.buyerVolumeSum1 = buyerVolumeSum1;
    }

    public BigDecimal getSupplierTermSum() {
        return supplierTermSum;
    }

    public void setSupplierTermSum(BigDecimal supplierTermSum) {
        this.supplierTermSum = supplierTermSum;
    }

    public BigDecimal getSupplierVolumeSum2() {
        return supplierVolumeSum2;
    }

    public void setSupplierVolumeSum2(BigDecimal supplierVolumeSum2) {
        this.supplierVolumeSum2 = supplierVolumeSum2;
    }

    public BigDecimal getSupplierVolumeSum1() {
        return supplierVolumeSum1;
    }

    public void setSupplierVolumeSum1(BigDecimal supplierVolumeSum1) {
        this.supplierVolumeSum1 = supplierVolumeSum1;
    }

    public BizProductView getSelectBizProduct() {
        return selectBizProduct;
    }

    public void setSelectBizProduct(BizProductView selectBizProduct) {
        this.selectBizProduct = selectBizProduct;
    }

    public StakeholderView getSelectStakeholder() {
        return selectStakeholder;
    }

    public void setSelectStakeholder(StakeholderView selectStakeholder) {
        this.selectStakeholder = selectStakeholder;
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
