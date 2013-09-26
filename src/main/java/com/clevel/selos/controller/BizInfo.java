package com.clevel.selos.controller;

import com.clevel.selos.busiensscontrol.BizInfoControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BizInfoView;
import com.clevel.selos.model.view.BizProductDetailView;
import com.clevel.selos.model.view.BizStakeHolderDetailView;
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
@ManagedBean(name = "bizInfo")
public class BizInfo implements Serializable {

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

    private BizStakeHolderDetailView BizStakeHolderDetailView;
    private List<BizStakeHolderDetailView> supplierDetailList;
    private List<BizStakeHolderDetailView> buyerDetailList;
    private BizProductDetailView bizProductDetailView;

    private BizProductDetailView selectBizProductDetail;
    private List<BizProductDetailView> bizProductDetailViewList;
    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;
    private BizStakeHolderDetailView bizStakeHolderTemp;
    private BizStakeHolderDetailView selectStakeHolder;

    private BizInfoView bizInfoView;

    private BusinessGroup bizGroup;
    private BusinessDescription bizDesc;

    @Inject
    Logger log;
    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;
    @Inject
    BizInfoControl bizInfoControl;
    public BizInfo(){

    }

    @PostConstruct
    public void onCreation(){
        bizInfoView = new BizInfoView();

        bizProductDetailViewList = new ArrayList<BizProductDetailView>();
        supplierDetailList = new ArrayList<BizStakeHolderDetailView>();
        buyerDetailList = new ArrayList<BizStakeHolderDetailView>();

        bizInfoView.setBizProductDetailViewList(bizProductDetailViewList);
        bizInfoView.setSupplierDetailList(supplierDetailList);
        bizInfoView.setBuyerDetailList(buyerDetailList);

        BizStakeHolderDetailView = new BizStakeHolderDetailView();
        bizProductDetailView = new BizProductDetailView();

        businessGroupList = businessGroupDAO.findAll();
        bizGroup = new BusinessGroup();
        bizDesc = new BusinessDescription();

        bizInfoView.setBizDesc(bizDesc);
        bizInfoView.setBizGroup(bizGroup);

    }

    public void onChangeBusinessGroup(){
        log.info("onChangeBusinessGroup >>> begin ");
        bizGroupId = bizInfoView.getBizGroup().getId();
        BusinessGroup businessGroup = businessGroupDAO.findById(bizGroupId);
        log.info("onChangeBusinessGroup :::: businessGroup ::: ", businessGroup);
        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
        log.info("onChangeBusinessGroup :::: businessDescriptionList Size ::: ", businessDescriptionList.size());
    }

    public void onChangeBusinessDesc(){
        log.info("onChangeBusinessDesc >>> begin ");
        bizGroupId = bizInfoView.getBizDesc().getId();
        BusinessDescription businessDesc = businessDescriptionDAO.findById(bizGroupId);
        log.info("onChangeBusinessGroup :::: businessDesc ::: ", businessDesc);
        bizInfoView.setBizCode(businessDesc.getTmbCode());
        log.info("onChangeBusinessGroup :::: bizInfoView ::: ", bizInfoView.getBizCode());
    }

    public void onAddBizProductDetailView(){
        log.info("onAddBizProductDetailView >>> begin ");
        bizProductDetailView = new BizProductDetailView();
        modeForButton = "add";
    }

    public void onEditBizProductDetailView() {
        log.info( " onEditBizProductDetailView is " + selectBizProductDetail);
        modeForButton = "edit";
        bizProductDetailView = new BizProductDetailView();
        //*** Check list size ***//
        if( rowIndex < bizProductDetailViewList.size() ) {
            bizProductDetailView.setProductType(selectBizProductDetail.getProductType());
            bizProductDetailView.setPercentSalesVolume(selectBizProductDetail.getPercentSalesVolume());
            bizProductDetailView.setPercentEBIT(selectBizProductDetail.getPercentEBIT());
            bizProductDetailView.setProductDetail(selectBizProductDetail.getProductDetail());
        }
    }

    public void onDeleteBizProductDetailView() {
        log.info( " onDeleteBizProductDetailView is " + selectBizProductDetail);
        bizProductDetailViewList.remove(selectBizProductDetail);
    }

    public void onSaveBizProductDetailView(){
        boolean complete = false;
        log.info( " modeForButton is " + modeForButton);

        log.info( "context.addCallbackParam " );
        RequestContext context = RequestContext.getCurrentInstance();

        if(!bizProductDetailView.getProductType().equals("")&&!bizProductDetailView.getProductDetail().equals("")&&!bizProductDetailView.getPercentSalesVolume().equals("")&&!bizProductDetailView.getPercentEBIT().equals("")){
            if(modeForButton.equalsIgnoreCase("add")){
                log.info("onSaveBizProductDetailView add >>> begin ");

                bizProductDetailView.setNo(bizProductDetailViewList.size()+1);
                bizProductDetailViewList.add(bizProductDetailView);
                bizProductDetailView = new BizProductDetailView();
            }else if(modeForButton.equalsIgnoreCase("edit")){
                log.info("onSaveBizProductDetailView edit >>> begin ");
                BizProductDetailView bizTemp;
                bizTemp = bizProductDetailViewList.get(rowIndex);
                bizTemp.setProductType(bizProductDetailView.getProductType());
                bizTemp.setPercentSalesVolume(bizProductDetailView.getPercentSalesVolume());
                bizTemp.setPercentEBIT(bizProductDetailView.getPercentEBIT());
                bizTemp.setProductDetail(bizProductDetailView.getProductDetail());

                bizProductDetailView = new BizProductDetailView();
            }
            complete = true;
        }

        context.addCallbackParam("functionComplete", complete);
    }

    public void onAddBizStakeHolderDetailView(){
        log.info("onAddBizStakeHolderDetailView >>> label is  " + stakeType );
        modeForButton = "add";
        onSetLabelStakeHolder();
        BizStakeHolderDetailView = new BizStakeHolderDetailView();

    }

    public void onEditBizStakeHolderDetailView() {
        modeForButton = "edit";
        onSetLabelStakeHolder();
        BizStakeHolderDetailView = new BizStakeHolderDetailView();
        BizStakeHolderDetailView.setStakeHolderType(stakeType);
        BizStakeHolderDetailView = onSetStakeHolder(BizStakeHolderDetailView,selectStakeHolder);
    }

    private void onSetLabelStakeHolder(){


        if(stakeType.equalsIgnoreCase("1")){
            dlgStakeName = msg.get("app.supplierName");
        }else if(stakeType.equalsIgnoreCase("2")){
            dlgStakeName =msg.get("app.buyerName");
        }

        log.info("onSetLabelStakeHolder >>> label is  " + dlgStakeName );
    }

    private BizStakeHolderDetailView onSetStakeHolder(BizStakeHolderDetailView stakeHolderMaster ,BizStakeHolderDetailView stakeHolderChild){

        stakeHolderMaster.setName(stakeHolderChild.getName());
        stakeHolderMaster.setContactName(stakeHolderChild.getContactName());
        stakeHolderMaster.setPhoneNo(stakeHolderChild.getPhoneNo());
        stakeHolderMaster.setContactYear(stakeHolderChild.getContactYear());
        stakeHolderMaster.setPercentSalesVolume(stakeHolderChild.getPercentSalesVolume());
        stakeHolderMaster.setPercentCash(stakeHolderChild.getPercentCash());
        stakeHolderMaster.setPercentCredit(stakeHolderChild.getPercentCredit());
        stakeHolderMaster.setCreditTerm(stakeHolderChild.getCreditTerm());

        return stakeHolderMaster;
    }

    public void onDeleteBizStakeHolderDetailView() {
        log.info("onDeleteBizStakeHolderDetailView is " + selectStakeHolder);

        log.info("onDeleteBizStakeHolderDetailView stakeType is " + stakeType);

        if(stakeType.equalsIgnoreCase("1")){
            supplierDetailList.remove(selectStakeHolder);
            calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
        }else if(stakeType.equalsIgnoreCase("2")){
            buyerDetailList.remove(selectStakeHolder);
            calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
        }
    }

    public void onSaveBizStakeHolderDetailView(){

        log.info( "onSaveBizStakeHolderDetailView modeForButton is " +  modeForButton + "   stakeType is " + stakeType);

        boolean supplier;
        boolean buyer;
        BizStakeHolderDetailView  stakeHolderRow;

        if(modeForButton.equalsIgnoreCase("add")){
             if(stakeType.equals("1")){
                BizStakeHolderDetailView.setNo(supplierDetailList.size()+1);
                supplierDetailList.add(BizStakeHolderDetailView);
                supplier =calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                 if(!supplier){
                     supplierDetailList.remove(BizStakeHolderDetailView);
                     calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                 }
             }else if(stakeType.equals("2")){
                 BizStakeHolderDetailView.setNo(buyerDetailList.size()+1);
                 buyerDetailList.add(BizStakeHolderDetailView);
                 buyer = calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
                 if(!buyer){
                     buyerDetailList.remove(BizStakeHolderDetailView);
                     calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
                 }
             }
        }else if(modeForButton.equalsIgnoreCase("edit")){
            if(stakeType.equals("1")){
                 stakeHolderRow = supplierDetailList.get(rowIndex);
                 stakeHolderRow = onSetStakeHolder(stakeHolderRow,BizStakeHolderDetailView);
                 supplierDetailList.set(rowIndex, stakeHolderRow);
                 supplier = calSumBizStakeHolderDetailView(supplierDetailList, stakeType);

                 if(!supplier){
                     stakeHolderRow = onSetStakeHolder(stakeHolderRow,bizStakeHolderTemp);
                     supplierDetailList.set(rowIndex,stakeHolderRow);
                     calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                 }

            }else if(stakeType.equals("2")){
                stakeHolderRow = buyerDetailList.get(rowIndex);
                stakeHolderRow = onSetStakeHolder(stakeHolderRow,BizStakeHolderDetailView);
                buyerDetailList.set(rowIndex, stakeHolderRow);
                buyer = calSumBizStakeHolderDetailView(buyerDetailList, stakeType);

                if(!buyer){
                    stakeHolderRow = onSetStakeHolder(stakeHolderRow,bizStakeHolderTemp);
                    buyerDetailList.set(rowIndex,stakeHolderRow);
                    calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
                }
            }
        }
        BizStakeHolderDetailView = new BizStakeHolderDetailView();
    }

    private boolean calSumBizStakeHolderDetailView(List<BizStakeHolderDetailView> stakeHoldersCalList,String stakeHolder){
        float summ1 = 0.0f;
        float summ2 = 0.0f;
        int summ3 = 0;

        BizStakeHolderDetailView stakeHoldersCal;

        for(int i=0 ; i<stakeHoldersCalList.size(); i++){
            stakeHoldersCal = stakeHoldersCalList.get(i);

            //log.info("stakeHoldersCal getPercentCredit{} " + i + " is " + stakeHoldersCal);

            summ1 += stakeHoldersCal.getPercentSalesVolume().doubleValue();
            summ2 += stakeHoldersCal.getPercentCredit().doubleValue() + stakeHoldersCal.getPercentCash().doubleValue();
            summ3 += stakeHoldersCal.getCreditTerm().intValue();

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
        //log.info( " bizInfoView is " + bizInfoView);


        //save to DB
        bizInfoControl.onSaveBizInfoToDB(bizInfoView);

    }

    public BizStakeHolderDetailView getBizStakeHolderDetailView() {
        return BizStakeHolderDetailView;
    }

    public void setBizStakeHolderDetailView(BizStakeHolderDetailView BizStakeHolderDetailView) {
        this.BizStakeHolderDetailView = BizStakeHolderDetailView;
    }

    public List<BizStakeHolderDetailView> getSupplierDetailList() {
        return supplierDetailList;
    }

    public void setSupplierList(List<BizStakeHolderDetailView> supplierDetailList) {
        this.supplierDetailList = supplierDetailList;
    }

    public List<BizStakeHolderDetailView> getBuyerDetailList() {
        return buyerDetailList;
    }

    public void setBuyerDetailList(List<BizStakeHolderDetailView> buyerDetailList) {
        this.buyerDetailList = buyerDetailList;
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

    public List<BizProductDetailView> getBizProductDetailViewList() {
        return bizProductDetailViewList;
    }

    public void setBizProductDetailViewList(List<BizProductDetailView> bizProductDetailViewList) {
        this.bizProductDetailViewList = bizProductDetailViewList;
    }

    public BizProductDetailView getBizProductDetailView() {
        return bizProductDetailView;
    }

    public void setBizProductDetailView(BizProductDetailView bizProductDetailView) {
        this.bizProductDetailView = bizProductDetailView;
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

    public BizProductDetailView getSelectBizProductDetail() {
        return selectBizProductDetail;
    }

    public void setSelectBizProductDetail(BizProductDetailView selectBizProductDetail) {
        this.selectBizProductDetail = selectBizProductDetail;
    }

    public BizStakeHolderDetailView getSelectStakeHolder() {
        return selectStakeHolder;
    }

    public void setSelectStakeHolder(BizStakeHolderDetailView selectStakeHolder) {
        this.selectStakeHolder = selectStakeHolder;
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

    public BizInfoView getBizInfoFullApp() {
        return bizInfoView;
    }

    public void setBizInfoFullApp(BizInfoView bizInfoView) {
        this.bizInfoView = bizInfoView;
    }
}
