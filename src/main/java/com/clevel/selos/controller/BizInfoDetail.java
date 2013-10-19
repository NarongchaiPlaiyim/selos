package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BizInfoDetailControl;
import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.model.view.BizProductDetailView;
import com.clevel.selos.model.view.BizStakeHolderDetailView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "bizInfoDetail")
public class BizInfoDetail implements Serializable {

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

    private String messageHeader;
    private String message;

    private int rowIndex;
    private String modeForButton;
    private String dlgStakeName;
    private String dlgStakeSaleType;
    private int bizGroupId;
    long bizInfoSummaryId;
    long bizInfoDetailViewId;
    private String descType;

    private BizStakeHolderDetailView bizStakeHolderDetailView;
    private List<BizStakeHolderDetailView> supplierDetailList;
    private List<BizStakeHolderDetailView> buyerDetailList;
    private BizProductDetailView bizProductDetailView;

    private BizProductDetailView selectBizProductDetail;
    private List<BizProductDetailView> bizProductDetailViewList;
    private List<BusinessGroup> businessGroupList;
    private List<BusinessDescription> businessDescriptionList;
    private List<BusinessType> businessTypeList;
    private List<BusinessActivity> businessActivityList;

    private BizStakeHolderDetailView bizStakeHolderTemp;
    private BizStakeHolderDetailView selectStakeHolder;

    private BizInfoDetailView bizInfoDetailView;

    private BusinessGroup bizGroup;
    private BusinessDescription bizDesc;

    private BusinessActivity bizActivity;
    private BusinessType bizType;

    private BizInfoSummaryView bizInfoSummaryView;

    @Inject
    Logger log;
    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;
    @Inject
    private BusinessTypeDAO businessTypeDAO;
    @Inject
    private BusinessActivityDAO businessActivityDAO;
    @Inject
    BizInfoDetailControl bizInfoDetailControl;
    @Inject
    private BizInfoSummaryControl bizInfoSummaryControl;

    public BizInfoDetail(){

    }

    @PostConstruct
    public void onCreation(){
        try{
            log.info("onCreation ");

            HttpSession session = FacesUtil.getSession(true);

            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info( " get FROM session workCaseId is " + workCaseId);
            bizInfoDetailViewId = Long.parseLong(session.getAttribute("bizInfoDetailViewId").toString());
            log.info( " get FROM session bizInfoDetailViewId is " + bizInfoDetailViewId);

            bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
            bizInfoSummaryId = bizInfoSummaryView.getId();
            log.info( " get FROM session bizInfoSummaryId is " + bizInfoSummaryId);

            descType = "";

            businessActivityList = businessActivityDAO.findAll();
            businessTypeList = businessTypeDAO.findAll();
            businessGroupList = businessGroupDAO.findAll();

            if(bizInfoDetailViewId == -1 ){

                log.info( "bizInfoDetailView NEW RECORD");

                bizInfoDetailView = new BizInfoDetailView();

                bizProductDetailViewList = new ArrayList<BizProductDetailView>();
                supplierDetailList = new ArrayList<BizStakeHolderDetailView>();
                buyerDetailList = new ArrayList<BizStakeHolderDetailView>();

                bizStakeHolderDetailView = new BizStakeHolderDetailView();
                bizProductDetailView = new BizProductDetailView();

                bizGroup = new BusinessGroup();
                bizDesc = new BusinessDescription();

                bizType = new BusinessType();
                bizActivity = new BusinessActivity();


                bizInfoDetailView.setBizDesc(bizDesc);
                bizInfoDetailView.setBizGroup(bizGroup);
                bizInfoDetailView.setBizType(bizType);
                bizInfoDetailView.setBizActivity(bizActivity);

                log.info( "bizInfoDetailView NEW RECORD");

            }else{
                //
                log.info( "bizInfoDetailView FIND BY ID ");
                bizInfoDetailView = bizInfoDetailControl.onFindByID(bizInfoDetailViewId);

                log.info( "bizInfoDetailView getBizProductDetailViewList Size " +  bizInfoDetailView.getBizProductDetailViewList().size());
                if(bizInfoDetailView.getBizProductDetailViewList().size()>0){
                    bizProductDetailViewList =   bizInfoDetailView.getBizProductDetailViewList();
                }else {
                    bizProductDetailViewList =   new ArrayList<BizProductDetailView>();
                }
                log.info( "bizInfoDetailView getSupplierDetailList Size " +  bizInfoDetailView.getSupplierDetailList().size());
                if(bizInfoDetailView.getSupplierDetailList().size()>0){
                    supplierDetailList =   bizInfoDetailView.getSupplierDetailList();
                }else {
                    supplierDetailList =   new ArrayList<BizStakeHolderDetailView>();
                }
                log.info( "bizInfoDetailView getBuyerDetailList Size " +  bizInfoDetailView.getBuyerDetailList().size());
                if(bizInfoDetailView.getBuyerDetailList().size()>0){
                    buyerDetailList =   bizInfoDetailView.getBuyerDetailList();
                }else {
                    buyerDetailList =   new ArrayList<BizStakeHolderDetailView>();
                }
                bizGroup =  bizInfoDetailView.getBizGroup();
                bizDesc =  bizInfoDetailView.getBizDesc();

                descType = "1";
                onChangeBusinessGroup();
                onChangeBusinessDesc();
                descType = "";
            }
            bizInfoDetailView.setBizProductDetailViewList(bizProductDetailViewList);
            bizInfoDetailView.setSupplierDetailList(supplierDetailList);
            bizInfoDetailView.setBuyerDetailList(buyerDetailList);
        }catch (Exception ex){
            log.info("onCreation Exception ");
            if(ex.getCause() != null){
                message = "Save Basic Info data failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save Basic Info data failed. Cause : " + ex.getMessage();
            }
        }finally {
            log.info("onCreation end ");
        }
    }

    public void onChangeBusinessGroup(){
        businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(bizGroup);

        if(descType.equals("")){
            bizInfoDetailView.setBizCode("");
            bizInfoDetailView.setIncomeFactor(null);
            bizInfoDetailView.setAdjustedIncomeFactor(null);
            bizInfoDetailView.setBizComment("");
            bizInfoDetailView.setBizPermission("");
            bizInfoDetailView.setBizDocPermission("");
            bizInfoDetailView.setBizDocExpiryDate(null);
        }
    }

    public void onChangeBusinessDesc(){
        BusinessDescription viewBizDesc;
        BusinessDescription businessDesc;

        viewBizDesc = bizInfoDetailView.getBizDesc();
        businessDesc = bizInfoDetailControl.onFindBizDescByID(viewBizDesc);

        if(descType.equals("")){
            bizInfoDetailView.setBizCode(businessDesc.getTmbCode());
            bizInfoDetailView.setIncomeFactor(businessDesc.getIncomeFactor());
            bizInfoDetailView.setBizPermission(businessDesc.getBusinessPermission());
            bizInfoDetailView.setBizComment(businessDesc.getComment());
            bizInfoDetailView.setBizDocPermission(businessDesc.getBusinessPermissionDesc());
        }

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
        onSetRowNoBizProductDetail();
    }

    public void onSetRowNoBizProductDetail(){
        BizProductDetailView bizProductDetailTemp;
        for(int i=0;i<bizProductDetailViewList.size();i++){
            bizProductDetailTemp = bizProductDetailViewList.get(i);
            bizProductDetailTemp.setNo(i+1);
        }
    }

    public void onSetRowNoBizBizStakeHolderDetail(){
        BizStakeHolderDetailView bizStakeHolderDetailViewTemp;
        bizStakeHolderDetailViewTemp = new BizStakeHolderDetailView();
        if(stakeType.equals("1")){
            for(int i=0;i<supplierDetailList.size();i++){
                bizStakeHolderDetailViewTemp = supplierDetailList.get(i);
                bizStakeHolderDetailViewTemp.setNo(i+1);
            }
        }else if(stakeType.equals("2")){
            for(int i=0;i<buyerDetailList.size();i++){
                bizStakeHolderDetailViewTemp = buyerDetailList.get(i);
                bizStakeHolderDetailViewTemp.setNo(i+1);
            }
        }
        bizStakeHolderTemp = new BizStakeHolderDetailView();

        bizStakeHolderTemp = onSetStakeHolder(bizStakeHolderTemp,bizStakeHolderDetailViewTemp);

        log.info( " bizStakeHolderTemp at onRow is " + bizStakeHolderTemp);


    }

    public void onSaveBizProductDetailView(){
        boolean complete = false;
        log.info(" modeForButton is " + modeForButton);

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
        bizStakeHolderDetailView = new BizStakeHolderDetailView();

    }



    public void onEditBizStakeHolderDetailView() {
        modeForButton = "edit";
        onSetLabelStakeHolder();
        bizStakeHolderDetailView = new BizStakeHolderDetailView();
        bizStakeHolderDetailView.setStakeHolderType(stakeType);
        bizStakeHolderDetailView = onSetStakeHolder(bizStakeHolderDetailView,selectStakeHolder);

        bizStakeHolderTemp = new BizStakeHolderDetailView();
        bizStakeHolderTemp = onSetStakeHolder(bizStakeHolderTemp,selectStakeHolder);

        log.info( " bizStakeHolderTemp at onRow is " + bizStakeHolderTemp);
    }

    private void onSetLabelStakeHolder(){
        if(stakeType.equalsIgnoreCase("1")){
            dlgStakeName = msg.get("app.bizInfoDetail.bizStakeHolder.label.supplierName");
            dlgStakeSaleType = msg.get("app.bizInfoDetail.bizStakeHolder.label.percentBuysVolume");
        }else if(stakeType.equalsIgnoreCase("2")){
            dlgStakeName =msg.get("app.bizInfoDetail.bizStakeHolder.label.buyerName");
            dlgStakeSaleType = msg.get("app.bizInfoDetail.bizStakeHolder.label.percentSalesVolume");
        }
    }

    public void onDeleteBizStakeHolderDetailView() {
        log.info("onDeleteBizStakeHolderDetailView is " + selectStakeHolder);

        if(stakeType.equalsIgnoreCase("1")){
            supplierDetailList.remove(selectStakeHolder);
            calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
        }else if(stakeType.equalsIgnoreCase("2")){
            buyerDetailList.remove(selectStakeHolder);
            calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
        }

        onSetRowNoBizBizStakeHolderDetail();
    }

    public void onSaveBizStakeHolderDetailView(){
        boolean supplier;
        boolean buyer;
        BizStakeHolderDetailView  stakeHolderRow;

        boolean complete = onValidateStakeHolder();
        log.info("Validate StakeHolder complete is " + complete);

        RequestContext context = RequestContext.getCurrentInstance();
        if(complete){
            if(modeForButton.equalsIgnoreCase("add")){
                if(stakeType.equals("1")){
                    bizStakeHolderDetailView.setNo(supplierDetailList.size()+1);
                    supplierDetailList.add(bizStakeHolderDetailView);
                    supplier =calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                     if(!supplier){
                         context.addCallbackParam("functionCalSum", true);
                         supplierDetailList.remove(bizStakeHolderDetailView);
                         calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                     }
                }else if(stakeType.equals("2")){
                    bizStakeHolderDetailView.setNo(buyerDetailList.size()+1);
                     buyerDetailList.add(bizStakeHolderDetailView);
                     buyer = calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
                     if(!buyer){
                         context.addCallbackParam("functionCalSum", true);
                         buyerDetailList.remove(bizStakeHolderDetailView);
                         calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
                     }
                }
            }else if(modeForButton.equalsIgnoreCase("edit")){
                log.info( " bizStakeHolderTemp at mode edit is " + bizStakeHolderTemp);
                if(stakeType.equals("1")){
                     stakeHolderRow = supplierDetailList.get(rowIndex);
                     stakeHolderRow = onSetStakeHolder(stakeHolderRow,bizStakeHolderDetailView);
                     supplierDetailList.set(rowIndex, stakeHolderRow);
                     supplier = calSumBizStakeHolderDetailView(supplierDetailList, stakeType);

                     if(!supplier){
                         stakeHolderRow = onSetStakeHolder(stakeHolderRow,bizStakeHolderTemp);
                         supplierDetailList.set(rowIndex,stakeHolderRow);
                         calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                         context.addCallbackParam("functionCalSum", true);
                     }

                }else if(stakeType.equals("2")){
                    stakeHolderRow = buyerDetailList.get(rowIndex);
                    stakeHolderRow = onSetStakeHolder(stakeHolderRow,bizStakeHolderDetailView);
                    buyerDetailList.set(rowIndex, stakeHolderRow);
                    buyer = calSumBizStakeHolderDetailView(buyerDetailList, stakeType);

                    if(!buyer){
                        stakeHolderRow = onSetStakeHolder(stakeHolderRow,bizStakeHolderTemp);
                        buyerDetailList.set(rowIndex,stakeHolderRow);
                        calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
                        context.addCallbackParam("functionCalSum", true);
                    }
                }
            }
        }

        context.addCallbackParam("functionComplete", complete);
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

    private boolean calSumBizStakeHolderDetailView(List<BizStakeHolderDetailView> stakeHoldersCalList,String stakeHolder){
        float summ1 = 0.0f;
        float summ2 = 0.0f;
        int   summ3 = 0;

        BizStakeHolderDetailView stakeHoldersCal;

        for(int i=0 ; i<stakeHoldersCalList.size(); i++){
            stakeHoldersCal = stakeHoldersCalList.get(i);
            summ1 += stakeHoldersCal.getPercentSalesVolume().doubleValue();
            summ2 += stakeHoldersCal.getPercentCredit().doubleValue() + stakeHoldersCal.getPercentCash().doubleValue();
            summ3 += stakeHoldersCal.getCreditTerm().intValue();
        }

        if(summ1>100.001 ||summ2>100.001 ){
            return false;
        }

        if(stakeType.equals("1")){
            supplierVolumeSum1 = new BigDecimal(summ1).setScale(2, RoundingMode.HALF_EVEN);;
            supplierVolumeSum2 = new BigDecimal(summ2).setScale(2, RoundingMode.HALF_EVEN);
            supplierTermSum = new BigDecimal(summ3).setScale(2, RoundingMode.HALF_EVEN);

            bizInfoDetailView.setSupplierTotalPercentBuyVolume(supplierVolumeSum1);
            bizInfoDetailView.setSupplierTotalPercentCredit(supplierVolumeSum2);
            bizInfoDetailView.setSupplierTotalCreditTerm(supplierTermSum);
            bizInfoDetailView.setSupplierUWAdjustPercentCredit(supplierVolumeSum2);
            bizInfoDetailView.setSupplierUWAdjustCreditTerm(supplierTermSum);
        }else if(stakeType.equals("2")){
            buyerVolumeSum1 = new BigDecimal(summ1).setScale(2, RoundingMode.HALF_EVEN);;
            buyerVolumeSum2 = new BigDecimal(summ2).setScale(2, RoundingMode.HALF_EVEN);;
            buyerTermSum = new BigDecimal(summ3).setScale(2, RoundingMode.HALF_EVEN);;

            bizInfoDetailView.setBuyerTotalPercentBuyVolume(buyerVolumeSum1);
            bizInfoDetailView.setBuyerTotalPercentCredit(buyerVolumeSum2);
            bizInfoDetailView.setBuyerTotalCreditTerm(buyerTermSum);
            bizInfoDetailView.setBuyerUWAdjustPercentCredit(buyerVolumeSum2);
            bizInfoDetailView.setBuyerUWAdjustCreditTerm(buyerTermSum);

        }

        return true;
    }
    private boolean onValidateStakeHolder(){
        boolean validate  = false;

        if(!bizStakeHolderDetailView.getName().equals("" )
                &&!bizStakeHolderDetailView.getContactName().equals("")
                &&!bizStakeHolderDetailView.getPhoneNo().equals("")
                &&!bizStakeHolderDetailView.getContactYear().equals("")
                &&!bizStakeHolderDetailView.getPercentSalesVolume().equals("")
                &&!bizStakeHolderDetailView.getPercentCash().equals("")
                &&!bizStakeHolderDetailView.getPercentCredit().equals("")
                &&!bizStakeHolderDetailView.getCreditTerm().equals("")
                ){
            validate = true;
        }

        return validate;
    }
    public void onSaveBizInfoView(){

        try{
            log.info("onSaveBizInfoView begin");
            bizInfoDetailControl.onSaveBizInfoToDB(bizInfoDetailView, bizInfoSummaryId);
            messageHeader = "Save Basic Info Success.";
            message = "Save Basic Info data success.";
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            messageHeader = "Save Basic Info Failed.";
            if(ex.getCause() != null){
                message = "Save Basic Info data failed. Cause : " + ex.getCause().toString();
            } else {
                message = "Save Basic Info data failed. Cause : " + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }finally {
            log.info("onSaveBizInfoView end");
        }
    }

    public void onDeleteBizInfoView(){
        bizInfoDetailControl.onDeleteBizInfoToDB(bizInfoDetailView);
    }

    public BizStakeHolderDetailView getBizStakeHolderDetailView() {
        return bizStakeHolderDetailView;
    }

    public void setBizStakeHolderDetailView(BizStakeHolderDetailView bizStakeHolderDetailView) {
        this.bizStakeHolderDetailView = bizStakeHolderDetailView;
    }

    public List<BizStakeHolderDetailView> getSupplierDetailList() {
        return supplierDetailList;
    }

    public void setSupplierDetailList(List<BizStakeHolderDetailView> supplierDetailList) {
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

    public BizInfoDetailView getBizInfoDetailView() {
        return bizInfoDetailView;
    }

    public void setBizInfoDetailView(BizInfoDetailView bizInfoDetailView) {
        this.bizInfoDetailView = bizInfoDetailView;
    }

    public List<BusinessType> getBusinessTypeList() {
        return businessTypeList;
    }

    public void setBusinessTypeList(List<BusinessType> businessTypeList) {
        this.businessTypeList = businessTypeList;
    }

    public List<BusinessActivity> getBusinessActivityList() {
        return businessActivityList;
    }

    public void setBusinessActivityList(List<BusinessActivity> businessActivityList) {
        this.businessActivityList = businessActivityList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getDlgStakeSaleType() {
        return dlgStakeSaleType;
    }

    public void setDlgStakeSaleType(String dlgStakeSaleType) {
        this.dlgStakeSaleType = dlgStakeSaleType;
    }
}
