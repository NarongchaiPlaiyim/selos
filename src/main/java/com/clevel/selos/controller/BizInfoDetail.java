package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BizInfoDetailControl;
import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.businesscontrol.CalculationControl;
import com.clevel.selos.businesscontrol.DBRControl;
import com.clevel.selos.dao.master.BusinessActivityDAO;
import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.master.BusinessGroupDAO;
import com.clevel.selos.dao.master.BusinessTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.model.view.BizProductDetailView;
import com.clevel.selos.model.view.BizStakeHolderDetailView;
import com.clevel.selos.system.audit.SLOSAuditor;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "bizInfoDetail")
public class BizInfoDetail extends BaseController {

    @NormalMessage
    @Inject
    Message msg;

    @Inject
    @SELOS
    Logger log;

    @Inject
    private SLOSAuditor slosAuditor;

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
    @Inject
    private DBRControl dbrControl;
    @Inject
    private CalculationControl calculationControl;

    private long workCaseId;
    private long stepId;
    private long statusId;

    private String stakeType;

    double sumBizPercent = 0;
    private BigDecimal sumSalePercentB;
    private BigDecimal sumCreditPercentB;
    private BigDecimal sumCreditTermB;
    private double circulationAmount =0;
    private double productionCostsAmount =0;

    private String messageHeader;
    private String message;
    private String severity;

    private int rowIndex;
    private String modeForButton;
    private String dlgStakeHeader;
    private String dlgStakeName;
    private String dlgStakeSaleType;
    private long bizInfoSummaryId;
    private long bizInfoDetailViewId;
    private Date currentDate;
    private String currentDateDDMMYY;
    private boolean readonlyIsUW;
    private boolean readonlyIsBDM;

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

    private BizInfoSummaryView bizInfoSummaryView;
    private User user;

    private boolean isDisable;

    private String userId;

    public BizInfoDetail(){

    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            //TODO Check valid step
            log.debug("preRender ::: Check valid stepId");

        }else{
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation(){
        log.debug("onCreation");
        Date date = new Date();

        HttpSession session = FacesUtil.getSession(false);

        user = getCurrentUser();
        if(!Util.isNull(user)) {
            userId = user.getId();
        } else {
            userId = "Null";
        }

        if(checkSession(session)){
            try{
                workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
                stepId = Util.parseLong(session.getAttribute("stepId"), 0);
                statusId = Util.parseLong(session.getAttribute("statusId"), 0);

                bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
                String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");

                loadFieldControl(workCaseId, Screen.BUSINESS_INFO_DETAIL, ownerCaseUserId);

                if(!Util.isNull(bizInfoSummaryView) && !Util.isZero(bizInfoSummaryView.getId())){
                    bizInfoSummaryId = bizInfoSummaryView.getId();
                }

                if (!Util.isNull(bizInfoSummaryView)){
                    if(bizInfoSummaryView.getCirculationAmount() != null){
                        circulationAmount = bizInfoSummaryView.getCirculationAmount().doubleValue();
                    }
                    if(bizInfoSummaryView.getProductionCostsAmount() != null){
                        productionCostsAmount = bizInfoSummaryView.getProductionCostsAmount().doubleValue();
                    }
                } else {
                    circulationAmount = 0;
                    productionCostsAmount = 0;
                }

                double x = (circulationAmount/365)*30;
                double y = (productionCostsAmount/365)*30;

                businessActivityList = businessActivityDAO.findAll();
                businessTypeList = businessTypeDAO.findAll();
                businessGroupList = businessGroupDAO.findAll();

                bizProductDetailViewList = new ArrayList<BizProductDetailView>();
                supplierDetailList = new ArrayList<BizStakeHolderDetailView>();
                buyerDetailList = new ArrayList<BizStakeHolderDetailView>();

                getBusinessInfoListDB();

                bizInfoDetailViewId = Util.parseLong(session.getAttribute("bizInfoDetailViewId"), -1);

                if(bizInfoDetailViewId == -1 ){
                    bizInfoDetailView = new BizInfoDetailView();
                    bizStakeHolderDetailView = new BizStakeHolderDetailView();
                    bizProductDetailView = new BizProductDetailView();
                } else {
                    bizInfoDetailView = bizInfoDetailControl.onFindByID(bizInfoDetailViewId, statusId);

                    if(!Util.isNull(bizInfoDetailView.getBizProductDetailViewList()) && bizInfoDetailView.getBizProductDetailViewList().size() > 0) {
                        bizProductDetailViewList =   bizInfoDetailView.getBizProductDetailViewList();
                    }

                    if(!Util.isNull(bizInfoDetailView.getSupplierDetailList()) && bizInfoDetailView.getSupplierDetailList().size() > 0) {
                        supplierDetailList =   bizInfoDetailView.getSupplierDetailList();
                    }

                    if(!Util.isNull(bizInfoDetailView.getBuyerDetailList()) && bizInfoDetailView.getBuyerDetailList().size() > 0) {
                        buyerDetailList =   bizInfoDetailView.getBuyerDetailList();
                    }

                    onChangeBusinessGroupInitial();

                    if (Util.isNull(bizInfoDetailView.getBizDocExpiryDate())){
                        onChangeBusinessDesc();
                    }

                    if(Util.subtract(BigDecimal.valueOf(sumBizPercent),bizInfoDetailView.getPercentBiz()) != null){
                        sumBizPercent = Util.subtract(BigDecimal.valueOf(sumBizPercent),bizInfoDetailView.getPercentBiz()).doubleValue();
                    }
                }

                bizInfoDetailView.setAveragePurchaseAmount(new BigDecimal(y));
                bizInfoDetailView.setAveragePayableAmount(new BigDecimal(x));
                bizInfoDetailView.setBizProductDetailViewList(bizProductDetailViewList);

                bizInfoDetailView.setSupplierDetailList(supplierDetailList);
                if(supplierDetailList.size() > 0){
                    calSumBizStakeHolderDetailView(supplierDetailList,"1");
                }

                bizInfoDetailView.setBuyerDetailList(buyerDetailList);
                if(buyerDetailList.size() > 0){
                    calSumBizStakeHolderDetailView(buyerDetailList,"2");
                }
                onCheckRole();
                onChangeBizPermission();

                slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.SUCCESS, "");
            } catch (Exception ex){
                log.error("onCreation Exception : ", Util.getMessageException(ex));
                slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.FAILED, Util.getMessageException(ex));
            }
        } else {
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.FAILED, "Invalid Session");

            log.debug("No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    private void onCheckRole(){
        readonlyIsUW = user.getRole().getId() != RoleValue.UW.id();
        readonlyIsBDM = user.getRole().getId() != RoleValue.BDM.id();
    }

    public void getBusinessInfoListDB(){
        List<BizInfoDetailView> bizInfoDetailViewList;
        bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailViewByBizInfoSummary(bizInfoSummaryId);
        sumBizPercent = 0;
        BizInfoDetailView bizInfoDetailViewTemp;
        if(bizInfoDetailViewList.size()!=0){
            for (BizInfoDetailView aBizInfoDetailViewList : bizInfoDetailViewList) {
                bizInfoDetailViewTemp = aBizInfoDetailViewList;
                sumBizPercent += bizInfoDetailViewTemp.getPercentBiz().doubleValue();
            }
        }
    }

    public void onChangeBusinessGroup(){
        if(bizInfoDetailView.getBizGroup() != null){
            businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(bizInfoDetailView.getBizGroup());

            bizInfoDetailView.setBizCode("");
            bizInfoDetailView.setIncomeFactor(null);
            bizInfoDetailView.setAdjustedIncomeFactor(BigDecimal.ZERO);
            bizInfoDetailView.setBizComment("");
            bizInfoDetailView.setBizPermission("");
            bizInfoDetailView.setBizDocPermission("");
            bizInfoDetailView.setBizDocExpiryDate(null);
            bizInfoDetailView.setPercentBiz(BigDecimal.ZERO);
        }
    }

    public void onChangeBusinessGroupInitial(){
        if(bizInfoDetailView.getBizGroup() != null){
            businessDescriptionList = businessDescriptionDAO.getListByBusinessGroup(bizInfoDetailView.getBizGroup());
        }
    }

    public void onChangeBusinessDesc(){
        BusinessDescription viewBizDesc;
        BusinessDescription businessDesc;
        viewBizDesc = bizInfoDetailView.getBizDesc();
        businessDesc = bizInfoDetailControl.onFindBizDescByID(viewBizDesc);

        bizInfoDetailView.setBizCode(businessDesc.getTmbCode());
        bizInfoDetailView.setIncomeFactor(businessDesc.getIncomeFactor());
        bizInfoDetailView.setBizComment(businessDesc.getComment());
        bizInfoDetailView.setBizDocPermission(businessDesc.getBusinessPermissionDesc());

//        if(Util.equals("Y",businessDesc.getBusinessPermission())) {
//            bizInfoDetailView.setBizPermission(businessDesc.getBusinessPermission());
//        } else {
//            bizInfoDetailView.setBizPermission("N");
//        }
        bizInfoDetailView.setStandardAccountPayable(businessDesc.getAp());
        bizInfoDetailView.setStandardAccountReceivable(businessDesc.getAr());
        bizInfoDetailView.setStandardStock(businessDesc.getInv());
    }

    public void onChangeBizPermission(){
        log.debug("onChangeBizPermission ");
        if(bizInfoDetailView.getBizPermission() != null ){
            if(bizInfoDetailView.getBizPermission().equals("Y")){
                bizInfoDetailView.setBizDocPermission(bizInfoDetailView.getBizDocPermission());
                bizInfoDetailView.setBizDocExpiryDate(bizInfoDetailView.getBizDocExpiryDate());
                setMandateValue("bizDocPermission",true);
                setDisabledValue("bizDocPermission",false);
                setMandateValue("bizDocExpiryDate",true);
                setDisabledValue("bizDocExpiryDate",false);
            } else {
                bizInfoDetailView.setBizPermission("N");
                bizInfoDetailView.setBizDocPermission("");
                bizInfoDetailView.setBizDocExpiryDate(null);
                setMandateValue("bizDocPermission",false);
                setDisabledValue("bizDocPermission",true);
                setMandateValue("bizDocExpiryDate",false);
                setDisabledValue("bizDocExpiryDate",true);
            }
        }
    }

    public void onAddBizProductDetailView(){
        bizProductDetailView = new BizProductDetailView();
        modeForButton = "add";

        slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_ADD, "On Add Product Information", new Date(), ActionResult.SUCCESS, "");
    }

    public void onSaveBizProductDetailView(){
        Date date = new Date();
        
        boolean complete = false;
        if(!bizProductDetailView.getProductType().equals("")&&!bizProductDetailView.getProductDetail().equals("")&&!bizProductDetailView.getPercentSalesVolume().equals("")&&!bizProductDetailView.getPercentEBIT().equals("")){
            complete = true;
            if(modeForButton.equalsIgnoreCase("add")){
                log.debug("onSaveBizProductDetailView add >>> begin ");
                bizProductDetailView.setNo(bizProductDetailViewList.size()+1);
                bizProductDetailViewList.add(bizProductDetailView);

                if(!calSumBizProductDetailView()){
                    bizProductDetailViewList.remove(bizProductDetailView);
                    calSumBizProductDetailView();
                    messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                    message = msg.get("app.bizInfoDetail.message.validate.bizProductOver.fail");
                    severity = MessageDialogSeverity.ALERT.severity();
                    complete = false;
                }
            } else if(modeForButton.equalsIgnoreCase("edit")) {
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
                    severity = MessageDialogSeverity.ALERT.severity();  
                    complete = false;
                }
            }
        }
        slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_SAVE, "On Save Product Information", date, ActionResult.SUCCESS, "");

        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onCancelBizProductDetailView(){
        slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_CANCEL, "On Cancel Product Information", new Date(), ActionResult.SUCCESS, "");

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", true);
    }

    public void onEditBizProductDetailView() {
        log.debug( " onEditBizProductDetailView is {}",selectBizProductDetail);
        Date date = new Date();
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

        slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_EDIT, "On Edit Product Information - Product Type :: " + selectBizProductDetail.getProductType(), date, ActionResult.SUCCESS, "");
}

    public void onDeleteBizProductDetailView() {
        Date date = new Date();
        log.debug( " onDeleteBizProductDetailView is {}",selectBizProductDetail);
        bizProductDetailViewList.remove(selectBizProductDetail);
        calSumBizProductDetailView();
        onSetRowNoBizProductDetail();

        slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_DELETE, "On Delete Product Information - Product Type :: " + selectBizProductDetail.getProductType(), date, ActionResult.SUCCESS, "");
    }

    public void onSetRowNoBizProductDetail(){
        BizProductDetailView bizProductDetailTemp;
        for(int i=0;i<bizProductDetailViewList.size();i++){
            bizProductDetailTemp = bizProductDetailViewList.get(i);
            bizProductDetailTemp.setNo(i+1);
        }
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
        Date date = new Date();
        modeForButton = "add";
        onSetLabelStakeHolder();
        bizStakeHolderDetailView = new BizStakeHolderDetailView();
        if(stakeType.equalsIgnoreCase("1")){
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_ADD, "On Add Stake Holder Supplier", date, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_ADD, "On Add Stake Holder Buyer", date, ActionResult.SUCCESS, "");
        }
    }

    public void onEditBizStakeHolderDetailView() {
        Date date = new Date();
        modeForButton = "edit";
        onSetLabelStakeHolder();
        bizStakeHolderDetailView = new BizStakeHolderDetailView();
        bizStakeHolderDetailView.setStakeHolderType(stakeType);
        bizStakeHolderDetailView = onSetStakeHolder(bizStakeHolderDetailView,selectStakeHolder);
        bizStakeHolderTemp = new BizStakeHolderDetailView();
        bizStakeHolderTemp = onSetStakeHolder(bizStakeHolderTemp,selectStakeHolder);
        if(stakeType.equalsIgnoreCase("1")){
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_EDIT, "On Edit Stake Holder Supplier - Name :: " + selectStakeHolder.getName(), date, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_EDIT, "On Edit Stake Holder Buyer - Name :: " + selectStakeHolder.getName(), date, ActionResult.SUCCESS, "");
        }
    }

    private void onSetLabelStakeHolder(){
        if(stakeType.equalsIgnoreCase("1")){
            dlgStakeHeader = msg.get("app.bizInfoDetail.label.supplierDetail");
            dlgStakeName = msg.get("app.bizInfoDetail.bizStakeHolder.label.supplierName");
            dlgStakeSaleType = msg.get("app.bizInfoDetail.bizStakeHolder.label.percentBuysVolume");
        } else if(stakeType.equalsIgnoreCase("2")) {
            dlgStakeHeader = msg.get("app.bizInfoDetail.label.buyerDetail");
            dlgStakeName =msg.get("app.bizInfoDetail.bizStakeHolder.label.buyerName");
            dlgStakeSaleType = msg.get("app.bizInfoDetail.bizStakeHolder.label.percentSalesVolume");
        }
    }

    public void onDeleteBizStakeHolderDetailView() {
        Date date = new Date();
        if(stakeType.equalsIgnoreCase("1")){
            supplierDetailList.remove(selectStakeHolder);
            calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
        }else if(stakeType.equalsIgnoreCase("2")){
            buyerDetailList.remove(selectStakeHolder);
            calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
        }
        onSetRowNoBizStakeHolderDetail();

        if(stakeType.equalsIgnoreCase("1")){
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_DELETE, "On Delete Stake Holder Supplier :: Name :: " + selectStakeHolder.getName(),date, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_DELETE, "On Delete Stake Holder Buyer :: Name :: " + selectStakeHolder.getName(),date, ActionResult.SUCCESS, "");
        }
    }

    public void onSaveBizStakeHolderDetailView(){
        Date date = new Date();
        boolean supplier;
        boolean buyer;
        BizStakeHolderDetailView stakeHolderRow;
        boolean complete = true;
        if(modeForButton.equalsIgnoreCase("add")) {
            if(stakeType.equals("1")){
                bizStakeHolderDetailView.setNo(supplierDetailList.size()+1);
                supplierDetailList.add(bizStakeHolderDetailView);
                supplier =calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                 if(!supplier){
                     supplierDetailList.remove(bizStakeHolderDetailView);
                     calSumBizStakeHolderDetailView(supplierDetailList, stakeType);
                     messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                     message = msg.get("app.bizInfoDetail.message.validate.supplierOver.fail");
                     severity = MessageDialogSeverity.ALERT.severity();

                     complete = false;
                 }
            } else if(stakeType.equals("2")) {
                log.debug( " add buyer 1 ");
                 bizStakeHolderDetailView.setNo(buyerDetailList.size()+1);
                 buyerDetailList.add(bizStakeHolderDetailView);
                log.debug( " add buyer 2 ");
                 buyer = calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
                log.debug( " add buyer 3 ");
                 if(!buyer){
                     log.debug( " add buyer * ");
                     buyerDetailList.remove(bizStakeHolderDetailView);
                     calSumBizStakeHolderDetailView(buyerDetailList, stakeType);
                     messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                     message = msg.get("app.bizInfoDetail.message.validate.buyerOver.fail");
                     severity = MessageDialogSeverity.ALERT.severity();

                     complete = false;
                 }
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
                     severity = MessageDialogSeverity.ALERT.severity();

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
                    severity = MessageDialogSeverity.ALERT.severity();

                    complete = false;
                }
            }
        }

        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");

        if(stakeType.equalsIgnoreCase("1")){
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_SAVE, "On Save Stake Holder Supplier", date, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_SAVE, "On Save Stake Holder Buyer", date, ActionResult.SUCCESS, "");
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", complete);
    }

    public void onCancelBizStakeHolderDetailView(){
        if(stakeType.equalsIgnoreCase("1")){
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_CANCEL, "On Cancel Stake Holder Supplier", new Date(), ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_CANCEL, "On Cancel Stake Holder Buyer", new Date(), ActionResult.SUCCESS, "");
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", true);
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

        if(stakeHolderChild.getPercentCredit() != null){
            if(stakeHolderChild.getPercentCredit().compareTo(BigDecimal.ZERO) > 0){
                setMandateValue("bizSupplier.creditTerm",true);
            } else {
                setMandateValue("bizSupplier.creditTerm",false);
            }
        }

        return stakeHolderMaster;
    }

    private boolean calSumBizStakeHolderDetailView(List<BizStakeHolderDetailView> stakeHoldersCalList,String stakeType){
        onCheckRole();
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
        log.debug(" sumSalePercent   is {}",sumSalePercent);
        log.debug(" sumCreditPercent is {}",sumCreditPercent);
        log.debug(" sumCreditTerm    is {}",sumCreditTerm);

        if(sumSalePercent>100.001 ){
            return false;
        }

        sumSalePercentB = new BigDecimal(sumSalePercent).setScale(2,RoundingMode.HALF_UP);
        sumCreditPercentB = new BigDecimal(sumCreditPercent).setScale(2,RoundingMode.HALF_UP);
        sumCreditTermB = new BigDecimal(sumCreditTerm).setScale(2,RoundingMode.HALF_UP);
        log.debug(" check stakeType {}",stakeType );
        if("1".equalsIgnoreCase(stakeType)){
            log.debug(" stakeType ===== 1" );
            bizInfoDetailView.setSupplierTotalPercentBuyVolume(sumSalePercentB);
            bizInfoDetailView.setSupplierTotalPercentCredit(sumCreditPercentB);
            bizInfoDetailView.setSupplierTotalCreditTerm(sumCreditTermB);
            log.debug(" stakeType ===== 1.1" );
            if (readonlyIsUW){
                bizInfoDetailView.setSupplierUWAdjustPercentCredit(null);
                bizInfoDetailView.setSupplierUWAdjustCreditTerm(null);
            } else {
                bizInfoDetailView.setSupplierUWAdjustPercentCredit(sumCreditPercentB);
                bizInfoDetailView.setSupplierUWAdjustCreditTerm(sumCreditTermB);
            }
            bizInfoDetailView.setPurchasePercentCash(new BigDecimal(100 - sumCreditPercentB.doubleValue()));
            bizInfoDetailView.setPurchasePercentCredit(sumCreditPercentB);
            log.debug(" stakeType ===== 1.5" );

        }else if("2".equalsIgnoreCase(stakeType)){
            log.debug(" stakeType ===== 2" );
            bizInfoDetailView.setBuyerTotalPercentBuyVolume(sumSalePercentB);
            bizInfoDetailView.setBuyerTotalPercentCredit(sumCreditPercentB);
            bizInfoDetailView.setBuyerTotalCreditTerm(sumCreditTermB);
            log.debug(" stakeType ===== 2.1" );
            if (readonlyIsUW){
                bizInfoDetailView.setBuyerUWAdjustPercentCredit(null);
                bizInfoDetailView.setBuyerUWAdjustCreditTerm(null);
            } else {
                bizInfoDetailView.setBuyerUWAdjustPercentCredit(sumCreditPercentB);
                bizInfoDetailView.setBuyerUWAdjustCreditTerm(sumCreditTermB);
            }
            bizInfoDetailView.setPayablePercentCash(new BigDecimal(100 - sumCreditPercentB.doubleValue()));
            bizInfoDetailView.setPayablePercentCredit(sumCreditPercentB);
            log.debug(" stakeType ===== 2.5" );
        }
        return true;
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
        Date date = new Date();
        try{
            if(Util.add(BigDecimal.valueOf(sumBizPercent), bizInfoDetailView.getPercentBiz()) != null){
                sumBizPercent = Util.add(BigDecimal.valueOf(sumBizPercent), bizInfoDetailView.getPercentBiz()).doubleValue();
            }

            if(sumBizPercent>100.001){
                sumBizPercent = sumBizPercent -  bizInfoDetailView.getPercentBiz().doubleValue();
                messageHeader = msg.get("app.bizInfoDetail.message.validate.header.fail");
                message = msg.get("app.bizInfoDetail.message.validate.percentBizOver.fail");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                return;
            }

            if (Util.isNull(bizInfoSummaryView)){
                bizInfoSummaryId = bizInfoDetailControl.onSaveSummaryByDetail(workCaseId);
            }

            if(bizInfoDetailView.getId() == 0){
                bizInfoDetailView.setCreateBy(user);
                bizInfoDetailView.setCreateDate(DateTime.now().toDate());
            }

            if(onCheckPermission()){
                bizInfoDetailView.setModifyBy(user);
                bizInfoDetailView.setSupplierDetailList(supplierDetailList);
                bizInfoDetailView.setBuyerDetailList(buyerDetailList);
                bizInfoDetailView = bizInfoDetailControl.onSaveBizInfoToDB(bizInfoDetailView, bizInfoSummaryId, workCaseId, stepId);
                dbrControl.updateValueOfDBR(workCaseId);
                calculationControl.calForBizInfoSummary(workCaseId);

                bizInfoDetailViewId =  bizInfoDetailView.getId();

                HttpSession session = FacesUtil.getSession(true);
                session.setAttribute("bizInfoDetailViewId", bizInfoDetailViewId);

                calculationControl.calWC(workCaseId);

                slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.SUCCESS, "");

                onCreation();

                messageHeader = msg.get("app.messageHeader.info");
                message = msg.get("app.bizInfoDetail.message.body.save.success");
                severity = MessageDialogSeverity.INFO.severity();
            }
        } catch(Exception ex) {
            log.error("Save Business Info Detail :: Exception : {}", Util.getMessageException(ex));
            slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.FAILED, Util.getMessageException(ex));

            messageHeader = msg.get("app.messageHeader.error");
            message = "Save business info detail data failed. Cause : " + Util.getMessageException(ex);
            severity = MessageDialogSeverity.ALERT.severity();
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onCancel(){
        slosAuditor.add(Screen.BUSINESS_INFO_DETAIL.value(), userId, ActionAudit.ON_CANCEL, "", new Date(), ActionResult.SUCCESS, "");

        FacesUtil.redirect("/site/bizInfoSummary.jsf");
    }

    public boolean onCheckPermission(){
        boolean result = true;
        if("Y".equals(bizInfoDetailView.getBizPermission())){
            if(Util.isNull(bizInfoDetailView.getBizDocPermission()) || Util.isZero(bizInfoDetailView.getBizDocPermission().length())){
                result = false;
            }
            else if(Util.isNull(bizInfoDetailView.getBizDocExpiryDate())) {
                result = false;
            }
            return result;
        } else {
            return result;
        }
    }

    public void onCalCashCredit(String point){
        if(point.equals("stakeHolderDlg")){
            bizStakeHolderDetailView.setPercentCredit(Util.subtract(BigDecimal.valueOf(100),bizStakeHolderDetailView.getPercentCash()));
        }else if(point.equals("purchasePercentCash")){
            bizInfoDetailView.setPurchasePercentCredit(Util.subtract(BigDecimal.valueOf(100),bizInfoDetailView.getPurchasePercentCash()));
        }else if(point.equals("payablePercentCash")){
            bizInfoDetailView.setPayablePercentCredit(Util.subtract(BigDecimal.valueOf(100),bizInfoDetailView.getPayablePercentCash()));
        }else if(point.equals("purchasePercentLocal")){
            bizInfoDetailView.setPurchasePercentForeign(Util.subtract(BigDecimal.valueOf(100),bizInfoDetailView.getPurchasePercentLocal()));
        }else if(point.equals("payablePercentLocal")){
            bizInfoDetailView.setPayablePercentForeign(Util.subtract(BigDecimal.valueOf(100),bizInfoDetailView.getPayablePercentLocal()));
        }
    }

    //GETTER SETTER
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

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public boolean isReadonlyIsUW() {
        return readonlyIsUW;
    }

    public void setReadonlyIsUW(boolean readonlyIsUW) {
        this.readonlyIsUW = readonlyIsUW;
    }

    public boolean isReadonlyIsBDM() {
        return readonlyIsBDM;
    }

    public void setReadonlyIsBDM(boolean readonlyIsBDM) {
        this.readonlyIsBDM = readonlyIsBDM;
    }

    public String getDlgStakeHeader() {
        return dlgStakeHeader;
    }

    public void setDlgStakeHeader(String dlgStakeHeader) {
        this.dlgStakeHeader = dlgStakeHeader;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
