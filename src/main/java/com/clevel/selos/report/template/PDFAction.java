package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.dao.master.TitleDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.ExSummaryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.ExSummary;
import com.clevel.selos.model.report.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PDFAction implements Serializable {
    @Inject
    private ExSummaryControl exSummaryControl;

    @Inject
    CustomerInfoControl customerInfoControl;

    @Inject
    private TitleDAO titleDAO;

    @Inject
    CustomerDAO customerDAO;

    @Inject
    @SELOS
    Logger log;

    @Inject
    ExSummaryView exSummaryView;

    @Inject
    BizInfoSummaryControl bizInfoSummaryControl;

    @Inject
    ExSummaryDAO exSummaryDAO;

    @Inject
    ExSummary exSummary;

//    long workCaseId = 147;

    long workCaseId;

    @Inject
    public PDFAction() {
    }

    public void init(){


        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }else{
            log.debug("onCreation ::: workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
            }catch (Exception ex){
                log.error("Exception :: {}",ex);
            }
        }
        exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
        exSummaryView  = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId);
    }

    public List<BorrowerExsumReport> fillBorrowerRelatedProfile(){
        init();
        List<BorrowerExsumReport> reports = new ArrayList<BorrowerExsumReport>();
        List<CustomerInfoView> customerInfoViewList = exSummaryView.getBorrowerListView();

        int count = 1 ;
        for (CustomerInfoView view:customerInfoViewList){
            BorrowerExsumReport borrowerExsumReport = new BorrowerExsumReport();
            borrowerExsumReport.setNo(count++);
            borrowerExsumReport.setTitleTh(view.getTitleTh().getTitleTh());
            borrowerExsumReport.setFirstNameTh(view.getFirstNameTh());
            borrowerExsumReport.setLastNameTh(view.getLastNameTh());
            borrowerExsumReport.setCitizenId(view.getCitizenId());
            borrowerExsumReport.setRegistrationId(view.getRegistrationId());
            borrowerExsumReport.setTmbCustomerId(view.getTmbCustomerId());
            borrowerExsumReport.setRelation(view.getRelation().getDescription());
            if(view.getCollateralOwner() == 2){
                borrowerExsumReport.setCollateralOwner("Y");
            } else {
                borrowerExsumReport.setCollateralOwner("N");
            }

            borrowerExsumReport.setIndLv(view.getIndLv());
            borrowerExsumReport.setJurLv(view.getJurLv());
            borrowerExsumReport.setPercentShare(view.getPercentShare());
            borrowerExsumReport.setAge(view.getAge());
            borrowerExsumReport.setKycLevel(view.getKycLevel().getKycLevel());
            List<CustomerCSIView> customerCSIList = view.getCustomerCSIList();

            for(CustomerCSIView csiView:customerCSIList){
                borrowerExsumReport.setCustomerCSIList(csiView.getWarningCode().getCode());
            }

            if(view.getWorthiness() == 3){
                borrowerExsumReport.setWorthiness("Pass");
            } else if (view.getWorthiness() == 4){
                borrowerExsumReport.setWorthiness("Fail");
            } else if (view.getWorthiness() == 5){
                borrowerExsumReport.setWorthiness("N/A");
            } else {
                borrowerExsumReport.setWorthiness("-");
            }

            reports.add(borrowerExsumReport);
        }

//        log.info("fillBorrowerRelatedProfile: {}",reports );

        return reports;
    }

    public List<TradeFinanceExsumReport> fillTradeFinance(){
        init();
        List<TradeFinanceExsumReport> financeExsumReports = new ArrayList<TradeFinanceExsumReport>();
        NewCreditFacilityView creditFacilityViews = exSummaryView.getTradeFinance();
        List<NewCreditFacilityView> creditFacilityViews1 = new ArrayList<NewCreditFacilityView>();
        creditFacilityViews1.add(creditFacilityViews);

        for (NewCreditFacilityView facilityView:creditFacilityViews1){
            TradeFinanceExsumReport tradeFinanceExsumReport = new TradeFinanceExsumReport();
            tradeFinanceExsumReport.setContactName(facilityView.getContactName());
            tradeFinanceExsumReport.setContactPhoneNo(facilityView.getContactPhoneNo());
            tradeFinanceExsumReport.setInterService(facilityView.getInterService());
            tradeFinanceExsumReport.setCurrentAddress(facilityView.getCurrentAddress());
            tradeFinanceExsumReport.setImportMail(facilityView.getImportMail());
            tradeFinanceExsumReport.setExportMail(facilityView.getExportMail());
            tradeFinanceExsumReport.setDepositBranchCode(facilityView.getDepositBranchCode());
            tradeFinanceExsumReport.setOwnerBranchCode(facilityView.getOwnerBranchCode());
            financeExsumReports.add(tradeFinanceExsumReport);
        }
//        log.info("fillTradeFinance: {}",financeExsumReports );
        return financeExsumReports;
    }

    public List<NCBRecordExsumReport> fillNCBRecord(){
        init();
        List<NCBRecordExsumReport> recordExsumReports = new ArrayList<NCBRecordExsumReport>();
        List<NCBInfoView> ncbInfoViewList = exSummaryView.getNcbInfoListView();

        for (NCBInfoView ncbInfoView:ncbInfoViewList){
            NCBRecordExsumReport ncbRecordExsumReport = new NCBRecordExsumReport();
            ncbRecordExsumReport.setNcbCusName(ncbInfoView.getNcbCusName());
            ncbRecordExsumReport.setCheckIn6Month(ncbInfoView.getCheckIn6Month());
            ncbRecordExsumReport.setCurrentPaymentType(ncbInfoView.getCurrentPaymentType());
            ncbRecordExsumReport.setHistoryPaymentType(ncbInfoView.getHistoryPaymentType());
            ncbRecordExsumReport.setNplFlagText(ncbInfoView.getNplFlagText());
            ncbRecordExsumReport.setTdrFlagText(ncbInfoView.getTdrFlagText());
            ncbRecordExsumReport.setDescription(ncbInfoView.getTdrCondition().getDescription());
            ncbRecordExsumReport.setPaymentClass(ncbInfoView.getPaymentClass());

            recordExsumReports.add(ncbRecordExsumReport);
        }
//        log.info("fillNCBRecord: {}",recordExsumReports );
        return recordExsumReports;
    }

    public BorrowerExsumReport fillBorrower(){
        init();
        BorrowerExsumReport borrowerExsumReport = new BorrowerExsumReport();

        if(!Util.isNull(exSummaryView)){
            borrowerExsumReport.setBusinessLocationName(exSummaryView.getBusinessLocationName());
            borrowerExsumReport.setBusinessLocationAddress(exSummaryView.getBusinessLocationAddress());
            borrowerExsumReport.setBusinessLocationAddressEN(exSummaryView.getBusinessLocationAddressEN());
            borrowerExsumReport.setOwner(exSummaryView.getOwner());
        }
//        log.info("fillBorrower: {}",borrowerExsumReport.toString());
        return borrowerExsumReport;
    }

    public BorrowerCharacteristicExSumReport fillBorrowerCharacteristic(){
        init();
        BorrowerCharacteristicExSumReport characteristicExSumReport = new BorrowerCharacteristicExSumReport();
        ExSumCharacteristicView exSumCharacteristicView = exSummaryView.getExSumCharacteristicView();
//        log.debug("exSumCharacteristicView: {}",exSumCharacteristicView);

        characteristicExSumReport.setCustomer(exSumCharacteristicView.getCustomer());
        characteristicExSumReport.setCurrentDBR(exSumCharacteristicView.getCurrentDBR());
        characteristicExSumReport.setFinalDBR(exSumCharacteristicView.getFinalDBR());
        characteristicExSumReport.setIncome(exSumCharacteristicView.getIncome());
        characteristicExSumReport.setRecommendedWCNeed(exSumCharacteristicView.getRecommendedWCNeed());
        characteristicExSumReport.setActualWC(exSumCharacteristicView.getActualWC());
        characteristicExSumReport.setStartBusinessDate(exSumCharacteristicView.getStartBusinessDate());
        characteristicExSumReport.setYearInBusiness(exSumCharacteristicView.getYearInBusiness());
        characteristicExSumReport.setSalePerYearBDM(exSumCharacteristicView.getSalePerYearBDM());
        characteristicExSumReport.setSalePerYearUW(exSumCharacteristicView.getSalePerYearUW());
        characteristicExSumReport.setGroupSaleBDM(exSumCharacteristicView.getGroupSaleBDM());
        characteristicExSumReport.setGroupSaleUW(exSumCharacteristicView.getGroupSaleUW());
        characteristicExSumReport.setGroupExposureBDM(exSumCharacteristicView.getGroupExposureBDM());
        characteristicExSumReport.setGroupExposureUW(exSumCharacteristicView.getGroupExposureUW());

        ExSumBusinessInfoView exSumBusinessInfoView = exSummaryView.getExSumBusinessInfoView();
//        log.debug("exSumBusinessInfoView: {}",exSumBusinessInfoView);

        characteristicExSumReport.setNetFixAsset(exSumBusinessInfoView.getNetFixAsset());
        characteristicExSumReport.setNoOfEmployee(exSumBusinessInfoView.getNoOfEmployee());
        characteristicExSumReport.setBizProvince(exSumBusinessInfoView.getBizProvince());
        characteristicExSumReport.setBizType(exSumBusinessInfoView.getBizType());
        characteristicExSumReport.setBizGroup(exSumBusinessInfoView.getBizGroup());
        characteristicExSumReport.setBizCode(exSumBusinessInfoView.getBizCode());
        characteristicExSumReport.setBizDesc(exSumBusinessInfoView.getBizDesc());
        characteristicExSumReport.setQualitativeClass(exSumBusinessInfoView.getQualitativeClass());
        characteristicExSumReport.setBizSize(exSumBusinessInfoView.getBizSize());
        characteristicExSumReport.setBDM(exSumBusinessInfoView.getBDM());
        characteristicExSumReport.setUW(exSumBusinessInfoView.getUW());
        characteristicExSumReport.setAR(exSumBusinessInfoView.getAR());
        characteristicExSumReport.setAP(exSumBusinessInfoView.getAP());
        characteristicExSumReport.setINV(exSumBusinessInfoView.getINV());

        characteristicExSumReport.setBusinessOperationActivity(exSummaryView.getBusinessOperationActivity());
        characteristicExSumReport.setBusinessPermission(exSummaryView.getBusinessPermission());
        characteristicExSumReport.setExpiryDate(exSummaryView.getExpiryDate());

//        log.info("fillBorrowerCharacteristic: {}",characteristicExSumReport.toString());
        return characteristicExSumReport;
    }

    public AccountMovementExSumReport fillAccountMovement(){
        init();
        AccountMovementExSumReport movementExSumReport = new AccountMovementExSumReport();
        List<ExSumAccountMovementView> movementViewList = exSummaryView.getExSumAccMovementViewList();

        for (ExSumAccountMovementView movementView:movementViewList){
            movementExSumReport.setOdLimit(movementView.getOdLimit());
            movementExSumReport.setUtilization(movementView.getUtilization());
            movementExSumReport.setSwing(movementView.getSwing());
            movementExSumReport.setOverLimitTimes(movementView.getOverLimitTimes());
            movementExSumReport.setOverLimitDays(movementExSumReport.getOverLimitDays());
            movementExSumReport.setChequeReturn(movementView.getChequeReturn());
            movementExSumReport.setCashFlow(movementView.getCashFlow());
            movementExSumReport.setCashFlowLimit(movementView.getCashFlowLimit());
            movementExSumReport.setTradeChequeReturnAmount(movementExSumReport.getTradeChequeReturnPercent());
            movementExSumReport.setTradeChequeReturnPercent(movementExSumReport.getTradeChequeReturnPercent());
        }
//        log.info("fillAccountMovement: {}",movementExSumReport.toString());
        return movementExSumReport;
    }

    public CollateralExSumReport fillCollateral(){
        init();
        CollateralExSumReport collateralExSumReport = new CollateralExSumReport();
        ExSumCollateralView exSumCollateralView = exSummaryView.getExSumCollateralView();

        collateralExSumReport.setCashCollateralValue(exSummary.getCashCollateralValue());
        collateralExSumReport.setCoreAssetValue(exSummary.getCoreAssetValue());
        collateralExSumReport.setNoneCoreAssetValue(exSummary.getNoneCoreAssetValue());
        collateralExSumReport.setLimitApprove(exSummary.getLimitApprove());
        collateralExSumReport.setPercentLTV(exSummary.getPercentLTV());

//        log.info("fillCollateral: {}",collateralExSumReport.toString());
        return collateralExSumReport;
    }

    public CreditRiskInfoExSumReport fillCreditRisk(){
        init();
        CreditRiskInfoExSumReport riskInfoExSumReport = new CreditRiskInfoExSumReport();
        ExSumCreditRiskInfoView exSumCreditRiskInfoView = exSummaryView.getExSumCreditRiskInfoView();

        riskInfoExSumReport.setRiskCusType(exSumCreditRiskInfoView.getRiskCusType());
        riskInfoExSumReport.setBotClass(exSumCreditRiskInfoView.getBotClass());
        riskInfoExSumReport.setReason(exSumCreditRiskInfoView.getReason());
        riskInfoExSumReport.setLastReviewDate(exSumCreditRiskInfoView.getLastReviewDate());
        riskInfoExSumReport.setNextReviewDate(exSumCreditRiskInfoView.getNextReviewDate());
        riskInfoExSumReport.setExtendedReviewDate(exSumCreditRiskInfoView.getExtendedReviewDate());
        riskInfoExSumReport.setIndirectCountryName(exSumCreditRiskInfoView.getIndirectCountryName());
        riskInfoExSumReport.setPercentExport(exSumCreditRiskInfoView.getPercentExport());

//        log.info("fillCreditRisk: {}",riskInfoExSumReport.toString());
        return riskInfoExSumReport;
    }

    public DecisionExSumReport fillDecision(){
        init();
        DecisionExSumReport decisionExSumReport = new DecisionExSumReport();
        List<ExSumDecisionView> exSumDecisionView = exSummaryView.getExSumDecisionListView();

        for (ExSumDecisionView decisionView : exSumDecisionView){
            decisionExSumReport.setId(decisionView.getId());
            decisionExSumReport.setFlag(decisionView.getFlag());
            decisionExSumReport.setGroup(decisionView.getGroup());
            decisionExSumReport.setRuleName(decisionView.getRuleName());
            decisionExSumReport.setCusName(decisionView.getCusName());
            decisionExSumReport.setDeviationReason(decisionView.getDeviationReason());
        }

//        log.info("fillDecision: {}",decisionExSumReport.toString());
        return decisionExSumReport;
    }

    public BizSupportExSumReport fillBizSupport(){
        init();
        BizSupportExSumReport bizSupportExSumReport = new BizSupportExSumReport();

        bizSupportExSumReport.setNatureOfBusiness(exSummaryView.getNatureOfBusiness());
        bizSupportExSumReport.setHistoricalAndReasonOfChange(exSummaryView.getHistoricalAndReasonOfChange());
        bizSupportExSumReport.setTmbCreditHistory(exSummaryView.getTmbCreditHistory());
        bizSupportExSumReport.setSupportReason(exSummaryView.getSupportReason());
        bizSupportExSumReport.setRm008Code(exSummaryView.getRm008Code());
        bizSupportExSumReport.setRm008Remark(exSummaryView.getRm008Remark());
        bizSupportExSumReport.setRm204Code(exSummaryView.getRm204Code());
        bizSupportExSumReport.setRm204Remark(exSummaryView.getRm204Remark());
        bizSupportExSumReport.setRm020Code(exSummaryView.getRm020Code());
        bizSupportExSumReport.setRm020Remark(exSummaryView.getRm020Remark());

//        log.info("fillBizSupport: {}" ,bizSupportExSumReport.toString());
        return bizSupportExSumReport;
    }

    public UWDecisionExSumReport fillUWDecision(){
        init();
        UWDecisionExSumReport uwDecisionExSumReport = new UWDecisionExSumReport();
//        List<AuthorizationDOA> authorizationDOAs = exSummaryView.
        List<ExSumReasonView> exSumReasonViews = exSummaryView.getDeviateCode();

        uwDecisionExSumReport.setUwCode(exSummaryView.getUwCode());
        uwDecisionExSumReport.setName(exSummaryView.getApproveAuthority().getName());
        uwDecisionExSumReport.setDecision(exSummaryView.getDecision());
        uwDecisionExSumReport.setUwComment(exSummaryView.getUwComment());

        for (ExSumReasonView sumReasonView : exSumReasonViews) {
            uwDecisionExSumReport.setCode(sumReasonView.getCode());
            uwDecisionExSumReport.setDescription(sumReasonView.getDescription());
        }

//        log.info("fillUWDecision: {}",uwDecisionExSumReport.toString());
        return uwDecisionExSumReport;
    }
}
