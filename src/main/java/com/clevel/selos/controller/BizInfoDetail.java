package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BizInfoDetailControl;
import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.dao.master.BusinessActivityDAO;
import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.master.BusinessGroupDAO;
import com.clevel.selos.dao.master.BusinessTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.model.view.BizProductDetailView;
import com.clevel.selos.model.view.BizStakeHolderDetailView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "bizInfoDetail")
public class BizInfoDetail implements Serializable {

    @NormalMessage
    @Inject
    Message msg;

    private String stakeType;

    double sumBizPercent = 0;
    private BigDecimal sumSalePercentB;
    private BigDecimal sumCreditPercentB;
    private BigDecimal sumCreditTermB;
    double circulationAmount =0;
    double productionCostsAmount =0;
    private String messageHeader;
    private String message;

    private int rowIndex;
    private String modeForButton;
    private String dlgStakeName;
    private String dlgStakeSaleType;
    private long bizInfoSummaryId;
    private long bizInfoDetailViewId;
    private String descType;
    private Date currentDate;


    private BizStakeHolderDetailView bizStakeHolderDetailView;
    private List<BizStakeHolderDetailView> supplierDetailList;
    private List<BizStakeHolderDetailView> buyerDetailList;
    private BizProductDetailView bizProductDetailView;
    private BizProductDetailView bizProductDetailViewTemp;

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
    private User user;

    private boolean isDisable = false;

    @Inject
    @SELOS
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
            log.info("BizInfoDetail onCreation ");

            HttpSession session = FacesUtil.getSession(true);
            log.info("session.getAttribute('workCaseId') " + session.getAttribute("workCaseId"));

            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());

            log.info("session.getAttribute('bizInfoDetailViewId') " + session.getAttribute("bizInfoDetailViewId"));

            if(!session.getAttribute("bizInfoDetailViewId").toString().equals("")){
                bizInfoDetailViewId = Long.parseLong(session.getAttribute("bizInfoDetailViewId").toString());
            }else{
                bizInfoDetailViewId = -1;
            }

            user = (User)session.getAttribute("user");

            bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
            if(bizInfoSummaryView.getCirculationAmount()!=null){
                circulationAmount =bizInfoSummaryView.getCirculationAmount().doubleValue();
            }

            if(bizInfoSummaryView.getProductionCostsAmount()!=null){
                productionCostsAmount =bizInfoSummaryView.getProductionCostsAmount().doubleValue();
            }
            double x = (circulationAmount/365)*30;
            double y = (productionCostsAmount/365)*30;

            if(bizInfoSummaryView.getId() != 0 ){
                bizInfoSummaryId = bizInfoSummaryView.getId();
            }else{
                String url = "bizInfoSummary.jsf";
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                log.info("redirect to new page");
                ec.redirect(url);
            }

            descType = "";
            businessActivityList = businessActivityDAO.findAll();
            businessTypeList = businessTypeDAO.findAll();
            businessGroupList = businessGroupDAO.findAll();

            bizProductDetailViewList = new ArrayList<BizProductDetailView>();
            supplierDetailList = new ArrayList<BizStakeHolderDetailView>();
            buyerDetailList = new ArrayList<BizStakeHolderDetailView>();

            getBusinessInfoListDB();
            if(bizInfoDetailViewId == -1 ){

                log.info( "bizInfoDetailView NEW RECORD");
                bizInfoDetailView = new BizInfoDetailView();
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



            }else{
                //
                log.info( "bizInfoDetailView FIND BY ID ");
                bizInfoDetailView = bizInfoDetailControl.onFindByID(bizInfoDetailViewId);

                if(bizInfoDetailView.getBizProductDetailViewList().size()>0){
                    bizProductDetailViewList =   bizInfoDetailView.getBizProductDetailViewList();
                }

                if(bizInfoDetailView.getSupplierDetailList().size()>0){
                    supplierDetailList =   bizInfoDetailView.getSupplierDetailList();
                }

                if(bizInfoDetailView.getBuyerDetailList().size()>0){
                    buyerDetailList =   bizInfoDetailView.getBuyerDetailList();
                }

                bizGroup =  bizInfoDetailView.getBizGroup();
                bizDesc =  bizInfoDetailView.getBizDesc();

                descType = "1";
                onChangeBusinessGroup();
                onChangeBusinessDesc();
                onChangeBizPermission();
                descType = "";

                sumBizPercent = sumBizPercent -  bizInfoDetailView.getPercentBiz().doubleValue();
            }

            bizInfoDetailView.setAveragePurchaseAmount( new BigDecimal(y));
            bizInfoDetailView.setAveragePayableAmount( new BigDecimal(x));
            bizInfoDetailView.setBizProductDetailViewList(bizProductDetailViewList);

            bizInfoDetailView.setSupplierDetailList(supplierDetailList);
            if(supplierDetailList.size()>0){
                calSumBizStakeHolderDetailView(supplierDetailList,"1");
            }

            bizInfoDetailView.setBuyerDetailList(buyerDetailList);
            if(buyerDetailList.size()>0){
                calSumBizStakeHolderDetailView(buyerDetailList,"2");
            }


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

    public void getBusinessInfoListDB(){
        List<BizInfoDetailView> bizInfoDetailViewList;
        bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailByBizInfoSummary(bizInfoSummaryId);
        sumBizPercent = 0;
        BizInfoDetailView bizInfoDetailViewTemp;
        if(bizInfoDetailViewList.size()!=0){
            for(int i=0;i<bizInfoDetailViewList.size();i++){
                bizInfoDetailViewTemp =    bizInfoDetailViewList.get(i);
                sumBizPercent += bizInfoDetailViewTemp.getPercentBiz().doubleValue();
            }
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
        bizInfoDetailView.setStandardAccountPayable(new BigDecimal(businessDesc.getAr()));
        bizInfoDetailView.setStandardAccountReceivable(new BigDecimal(businessDesc.getAp()));
        bizInfoDetailView.setStandardStock(new BigDecimal (businessDesc.getInv()));
        onChangeBizPermission();

    }

    public void onChangeBizPermission(){
        log.info("onChangeBizPermission ");
        isDisable = true;
        if(bizInfoDetailView.getBizPermission() != null ){
            if( bizInfoDetailView.getBizPermission().equals("Y")){
                isDisable = false;
            }else{
                bizInfoDetailView.setBizDocExpiryDate(null);
                bizInfoDetailView.setBizDocPermission("");
            }
        }else{
            bizInfoDetailView.setBizDocExpiryDate(null);
            bizInfoDetailView.setBizDocPermission("");
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
        bizProductDetailViewTemp = new BizProductDetailView();
        bizProductDetailViewTemp.setProductType(selectBizProductDetail.getProductType());
        bizProductDetailViewTemp.setPercentSalesVolume(selectBizProductDetail.getPercentSalesVolume());
        bizProductDetailViewTemp.setPercentEBIT(selectBizProductDetail.getPercentEBIT());
        bizProductDetailViewTemp.setProductDetail(selectBizProductDetail.getProductDetail());
}

    public void onDeleteBizProductDetailView() {
        log.info( " onDeleteBizProductDetailView is " + selectBizProductDetail);
        bizProductDetailViewList.remove(selectBizProductDetail);
        calSumBizProductDetailView();
        onSetRowNoBizProductDetail();
    }

    public void onSetRowNoBizProductDetail(){
        BizProductDetailView bizProductDetailTemp;
        for(int i=0;i<bizProductDetailViewList.size();i++){
            bizProductDetailTemp = bizProductDetailViewList.get(i);
            bizProductDetailTemp.setNo(i+1);
        }
    }

    public void onSaveBizProductDetailView(){
        boolean complete = false;
        RequestContext context = RequestContext.getCurrentInstance();

        if(!bizProductDetailView.getProductType().equals("")&&!bizProductDetailView.getProductDetail().equals("")&&!bizProductDetailView.getPercentSalesVolume().equals("")&&!bizProductDetailView.getPercentEBIT().equals("")){
            complete = true;
            if(modeForButton.equalsIgnoreCase("add")){
                log.info("onSaveBizProductDetailView add >>> begin ");
                bizProductDetailView.setNo(bizProductDetailViewList.size()+1);
                bizProductDetailViewList.add(bizProductDetailView);

                if(!calSumBizProductDetailView()){
                    bizProductDetailViewList.remove(bizProductDetailView);
                    calSumBizProductDetailView();
                    messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                    message = msg.get("app.bizInfoDetail.message.validate.bizProductOver.fail");
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    complete = false;
                }
                bizProductDetailView = new BizProductDetailView();
            }else if(modeForButton.equalsIgnoreCase("edit")){
                BizProductDetailView bizTemp;
                bizTemp = bizProductDetailViewList.get(rowIndex);
                bizTemp.setProductType(bizProductDetailView.getProductType());
                bizTemp.setPercentSalesVolume(bizProductDetailView.getPercentSalesVolume());
                bizTemp.setPercentEBIT(bizProductDetailView.getPercentEBIT());
                bizTemp.setProductDetail(bizProductDetailView.getProductDetail());
                if(!calSumBizProductDetailView()){
                    bizTemp.setProductType(bizProductDetailViewTemp.getProductType());
                    bizTemp.setPercentSalesVolume(bizProductDetailViewTemp.getPercentSalesVolume());
                    bizTemp.setPercentEBIT(bizProductDetailViewTemp.getPercentEBIT());
                    bizTemp.setProductDetail(bizProductDetailViewTemp.getProductDetail());
                    calSumBizProductDetailView();
                    messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                    message = msg.get("app.bizInfoDetail.message.validate.bizProductOver.fail");
                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    complete = false;
                }
                bizProductDetailView = new BizProductDetailView();
            }
        }
        context.addCallbackParam("functionComplete", complete);
    }

    private boolean calSumBizProductDetailView(){
        BizProductDetailView bizProductDetailViewTemp;
        double salePercentBizPro=0;
        double sumSalePercentBizPro = 0;
        sumSalePercentBizPro = 0;
        for(int i=0 ; i<bizProductDetailViewList.size(); i++){
            bizProductDetailViewTemp = bizProductDetailViewList.get(i);
            salePercentBizPro = bizProductDetailViewTemp.getPercentSalesVolume().doubleValue();
            sumSalePercentBizPro += salePercentBizPro;
        }
        if(sumSalePercentBizPro>100.001 ){
            return false;
        }
        return true;
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
        if(stakeType.equalsIgnoreCase("1")){
            supplierDetailList.remove(selectStakeHolder);
            calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
        }else if(stakeType.equalsIgnoreCase("2")){
            buyerDetailList.remove(selectStakeHolder);
            calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
        }
        onSetRowNoBizStakeHolderDetail();
    }

    public void onSaveBizStakeHolderDetailView(){
        boolean supplier;
        boolean buyer;
        BizStakeHolderDetailView  stakeHolderRow;
        boolean complete = onValidateStakeHolder();
        RequestContext context = RequestContext.getCurrentInstance();
        if(complete){
            if(modeForButton.equalsIgnoreCase("add")){
                if(stakeType.equals("1")){
                    bizStakeHolderDetailView.setNo(supplierDetailList.size()+1);
                    supplierDetailList.add(bizStakeHolderDetailView);
                    supplier =calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                     if(!supplier){
                         supplierDetailList.remove(bizStakeHolderDetailView);
                         calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                         messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                         message = msg.get("app.bizInfoDetail.message.validate.supplierOver.fail");
                         RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                         complete = false;
                     }
                }else if(stakeType.equals("2")){
                    log.info( " add buyer 1 ");
                     bizStakeHolderDetailView.setNo(buyerDetailList.size()+1);
                     buyerDetailList.add(bizStakeHolderDetailView);
                    log.info( " add buyer 2 ");
                     buyer = calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
                    log.info( " add buyer 3 ");
                     if(!buyer){
                         log.info( " add buyer * ");
                         buyerDetailList.remove(bizStakeHolderDetailView);
                         calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
                         messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                         message = msg.get("app.bizInfoDetail.message.validate.buyerOver.fail");
                         RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                         complete = false;
                     }
                    log.info( " buyerlist size " + buyerDetailList.size());
                }
            }else if(modeForButton.equalsIgnoreCase("edit")){
                if(stakeType.equals("1")){
                     stakeHolderRow = supplierDetailList.get(rowIndex);
                     stakeHolderRow = onSetStakeHolder(stakeHolderRow,bizStakeHolderDetailView);
                     supplierDetailList.set(rowIndex, stakeHolderRow);
                     supplier = calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                     if(!supplier){
                         stakeHolderRow = onSetStakeHolder(stakeHolderRow,bizStakeHolderTemp);
                         supplierDetailList.set(rowIndex,stakeHolderRow);
                         calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                         messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                         message = msg.get("app.bizInfoDetail.message.validate.supplierOver.fail");
                         RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                         complete = false;
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
                        messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                        message = msg.get("app.bizInfoDetail.message.validate.buyerOver.fail");
                        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                        complete = false;
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

    private boolean calSumBizStakeHolderDetailView(List<BizStakeHolderDetailView> stakeHoldersCalList,String stakeType){
        double sumSalePercent = 0;
        double sumCreditPercent = 0;
        double sumCreditTerm = 0;
        double salePercent;
        double creditPercent;
        double creditPercentCal;
        double creditTerm;
        double creditTermCal;
        BizStakeHolderDetailView stakeHoldersCal;
        for(int i=0 ; i<stakeHoldersCalList.size(); i++){
            stakeHoldersCal = stakeHoldersCalList.get(i);
            salePercent = stakeHoldersCal.getPercentSalesVolume().doubleValue();
            sumSalePercent += salePercent;
            creditPercent = stakeHoldersCal.getPercentCredit().doubleValue();
            creditPercentCal = (creditPercent*salePercent)/100;
            sumCreditPercent += creditPercentCal ;
            creditTerm = stakeHoldersCal.getCreditTerm().doubleValue();
            creditTermCal = (creditTerm*salePercent)/100;
            sumCreditTerm += creditTermCal;
        }
        log.info(" sumSalePercent   is " + sumSalePercent);
        log.info(" sumCreditPercent is " + sumCreditPercent);
        log.info(" sumCreditTerm    is " + sumCreditTerm);

        if(sumSalePercent>100.001 ){
            return false;
        }

        sumSalePercentB = new BigDecimal(sumSalePercent).setScale(2,RoundingMode.HALF_UP);
        sumCreditPercentB = new BigDecimal(sumCreditPercent).setScale(2,RoundingMode.HALF_UP);
        sumCreditTermB = new BigDecimal(sumCreditTerm).setScale(2,RoundingMode.HALF_UP);
        log.info(" check stakeType " +stakeType );
        if(stakeType.equals("1")){
            log.info(" stakeType ===== 1" );
            bizInfoDetailView.setSupplierTotalPercentBuyVolume(sumSalePercentB);
            bizInfoDetailView.setSupplierTotalPercentCredit(sumCreditPercentB);
            bizInfoDetailView.setSupplierTotalCreditTerm(sumCreditTermB);
            log.info(" stakeType ===== 1.1" );
            bizInfoDetailView.setSupplierUWAdjustPercentCredit(sumCreditPercentB);
            bizInfoDetailView.setSupplierUWAdjustCreditTerm(sumCreditTermB);
            bizInfoDetailView.setPurchasePercentCash(new BigDecimal(100-sumCreditPercentB.doubleValue()));
            bizInfoDetailView.setPurchasePercentCredit(sumCreditPercentB);
            log.info(" stakeType ===== 1.5" );

        }else if(stakeType.equals("2")){
            log.info(" stakeType ===== 2" );
            bizInfoDetailView.setBuyerTotalPercentBuyVolume(sumSalePercentB);
            bizInfoDetailView.setBuyerTotalPercentCredit(sumCreditPercentB);
            bizInfoDetailView.setBuyerTotalCreditTerm(sumCreditTermB);
            log.info(" stakeType ===== 2.1" );
            bizInfoDetailView.setBuyerUWAdjustPercentCredit(sumCreditPercentB);
            bizInfoDetailView.setBuyerUWAdjustCreditTerm(sumCreditTermB);
            bizInfoDetailView.setPayablePercentCash(new BigDecimal(100 - sumCreditPercentB.doubleValue()));
            bizInfoDetailView.setPayablePercentCredit(sumCreditPercentB);
            log.info(" stakeType ===== 2.5" );
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

    public void onSetRowNoBizStakeHolderDetail(){
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
    }

    public void onSaveBizInfoView(){
        try{
            sumBizPercent = sumBizPercent +  bizInfoDetailView.getPercentBiz().doubleValue();
            if(sumBizPercent>100.001){
                sumBizPercent = sumBizPercent -  bizInfoDetailView.getPercentBiz().doubleValue();
                messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                message = msg.get("app.bizInfoDetail.message.validate.percentBizOver.fail");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                return;
            }
            if(bizInfoDetailView.getId() == 0){
                bizInfoDetailView.setCreateBy(user);
                bizInfoDetailView.setCreateDate(DateTime.now().toDate());
            }
            bizInfoDetailView.setModifyBy(user);
            bizInfoDetailView.setSupplierDetailList(supplierDetailList);
            bizInfoDetailView.setBuyerDetailList(buyerDetailList);
            bizInfoDetailView = bizInfoDetailControl.onSaveBizInfoToDB(bizInfoDetailView, bizInfoSummaryId);
            messageHeader = msg.get("app.bizInfoDetail.message.header.save.success");
            message = msg.get("app.bizInfoDetail.message.body.save.success");

            log.info(" after save to DB BizInfoDetail is "+bizInfoDetailView.getId());
            bizInfoDetailViewId =  bizInfoDetailView.getId();
            HttpSession session = FacesUtil.getSession(true);
            session.setAttribute("bizInfoDetailViewId", bizInfoDetailViewId );
            log.info(" after save to DB BizInfoDetail bizInfoDetailViewId at session is "+session.getAttribute("bizInfoDetailViewId"));
            onCreation();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            log.info("ERROR");
            messageHeader = msg.get("app.bizInfoDetail.message.header.save.fail");
            if(ex.getCause() != null){
                message = msg.get("app.bizInfoDetail.message.body.save.fail") + ex.getCause().toString();
            } else {
                message = msg.get("app.bizInfoDetail.message.body.save.fail") + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }finally {
            log.info("onSaveBizInfoView end");
        }
    }

    public void onDeleteBizInfoView(){
        try{
            log.info("onDeleteBizInfoView begin");
            bizInfoDetailControl.onDeleteBizInfoToDB(bizInfoDetailView);
            messageHeader = msg.get("app.bizInfoDetail.message.header.delete.success");
            message = msg.get("app.bizInfoDetail.message.body.delete.success");
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            messageHeader = msg.get("app.bizInfoDetail.message.header.delete.fail");
            if(ex.getCause() != null){
                message = msg.get("app.bizInfoDetail.message.body.delete.fail") + ex.getCause().toString();
            } else {
                message = msg.get("app.bizInfoDetail.message.body.delete.fail") + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }finally {
            log.info("onDeleteBizInfoView end");
        }
    }

    public void onCancel(){
        try{
            String url = "bizInfoSummary.jsf";
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            log.info("redirect to new page");
            ec.redirect(url);
        }catch (Exception ex){
            ex.getCause().toString();
        }finally {

        }
    }


    public void onCalCashCredit(String point ){
        double result = 0;
        BigDecimal resultB;

        if(point.equals("stakeHolderDlg")){
            result = 100 - bizStakeHolderDetailView.getPercentCash().doubleValue();
            resultB = new BigDecimal(result);
            bizStakeHolderDetailView.setPercentCredit(resultB);
        }else if(point.equals("purchasePercentCash")){
            result = 100 - bizInfoDetailView.getPurchasePercentCash().doubleValue();
            resultB = new BigDecimal(result);
            bizInfoDetailView.setPurchasePercentCredit(resultB);
        }else if(point.equals("payablePercentCash")){
            result = 100 - bizInfoDetailView.getPayablePercentCash().doubleValue();
            resultB = new BigDecimal(result);
            bizInfoDetailView.setPayablePercentCredit(resultB);
        }else if(point.equals("purchasePercentLocal")){
            result = 100 - bizInfoDetailView.getPurchasePercentLocal().doubleValue();
            resultB = new BigDecimal(result);
            bizInfoDetailView.setPurchasePercentForeign(resultB);
        }else if(point.equals("payablePercentLocal")){
            result = 100 - bizInfoDetailView.getPayablePercentLocal().doubleValue();
            resultB = new BigDecimal(result);
            bizInfoDetailView.setPayablePercentForeign(resultB);
        }

    }

    public void onCalStockValue(){

    double stockDuBDM =    bizInfoDetailView.getStockDurationBDM().doubleValue();
    double stockValueBDM = (productionCostsAmount/365)*stockDuBDM;
    bizInfoDetailView.setStockValueBDM( new BigDecimal(stockValueBDM));

    double stockDuUW =    bizInfoDetailView.getStockDurationUW().doubleValue();
    double stockValueUW= (productionCostsAmount/365)*stockDuUW;
    bizInfoDetailView.setStockValueUW( new BigDecimal(stockValueUW));

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

    public double getSumBizPercent() {
        return sumBizPercent;
    }

    public void setSumBizPercent(double sumBizPercent) {
        this.sumBizPercent = sumBizPercent;
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }
}
