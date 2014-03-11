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


public class PDFExecutive_Summary implements Serializable {
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
    public PDFExecutive_Summary() {
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
        log.info("workCaseID: {}",workCaseId);

        if(!Util.isNull(workCaseId) && !Util.isZero(workCaseId)){
            exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
            exSummaryView  = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId);
        } else {
            log.debug("workCaseId is Null. {}",workCaseId);
        }

    }

    public List<BorrowerExsumReport> fillBorrowerRelatedProfile(){
        init();
        List<BorrowerExsumReport> reports = new ArrayList<BorrowerExsumReport>();
        List<CustomerInfoView> customerInfoViewList = exSummaryView.getBorrowerListView();

        int count = 1 ;
        if(!Util.isNull(customerInfoViewList)){
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


                if(view.getWorthiness() == 3){
                    borrowerExsumReport.setWorthiness("Pass");
                } else if (view.getWorthiness() == 4){
                    borrowerExsumReport.setWorthiness("Fail");
                } else if (view.getWorthiness() == 5){
                    borrowerExsumReport.setWorthiness("N/A");
                } else {
                    borrowerExsumReport.setWorthiness("-");
                }

                List<CustomerCSIView> customerCSIList = view.getCustomerCSIList();

                if (!Util.isNull(customerCSIList)){
                    for(CustomerCSIView csiView:customerCSIList){
                        borrowerExsumReport.setCustomerCSIList(csiView.getWarningCode().getCode());
                    }
                } else {
                    log.debug("customerCSIList is Null. {}",customerCSIList);
                }
                reports.add(borrowerExsumReport);
            }
        } else {
            log.debug("customerInfoViewList in Method fillBorrowerRelatedProfile is Null. {}",customerInfoViewList);
        }

        return reports;
    }

    public List<TradeFinanceExsumReport> fillTradeFinance(){
        init();
        List<TradeFinanceExsumReport> financeExsumReports = new ArrayList<TradeFinanceExsumReport>();

        List<NewCreditFacilityView>  newCreditFacilityViewArrayList = new ArrayList<NewCreditFacilityView>();
        NewCreditFacilityView creditFacilityViews = exSummaryView.getTradeFinance();

        if (!Util.isNull(creditFacilityViews)){
            log.info("creditFacilityViews: {}",creditFacilityViews);
            newCreditFacilityViewArrayList.add(creditFacilityViews);

            for (NewCreditFacilityView facilityView : newCreditFacilityViewArrayList){
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
        } else {
            log.debug("newCreditFacilityViewArrayList in Method fillTradeFinance is Null. {}",newCreditFacilityViewArrayList);
        }
        return financeExsumReports;
    }

    public List<NCBRecordExsumReport> fillNCBRecord(){
        init();
        List<NCBRecordExsumReport> recordExsumReports = new ArrayList<NCBRecordExsumReport>();
        List<NCBInfoView> ncbInfoViewList = exSummaryView.getNcbInfoListView();

        if (!Util.isNull(ncbInfoViewList)){
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
        } else {
            log.debug("ncbInfoViewList in Method fillNCBRecord is Null. {}",ncbInfoViewList);
        }
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
        } else {
            log.debug("exSummaryView in Method fillBorrower  is Null. {}",exSummaryView);
        }
        log.info("fillBorrower: {}",borrowerExsumReport.toString());
        return borrowerExsumReport;
    }

    public BorrowerCharacteristicExSumReport fillBorrowerCharacteristic(){
        init();
        BorrowerCharacteristicExSumReport characteristicExSumReport = new BorrowerCharacteristicExSumReport();
        ExSumCharacteristicView exSumCharacteristicView = exSummaryView.getExSumCharacteristicView();
        log.debug("exSumCharacteristicView: {}",exSumCharacteristicView);

        if(!Util.isNull(exSumCharacteristicView)){
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
        } else {
            log.debug("exSumCharacteristicView in Method fillBorrowerCharacteristic is Null. {}",exSumCharacteristicView);
        }


        ExSumBusinessInfoView exSumBusinessInfoView = exSummaryView.getExSumBusinessInfoView();

        if (!Util.isNull(exSumBusinessInfoView)){
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
        } else {
            log.debug("exSumBusinessInfoView in Mrthod fillBorrowerCharacteristic is Null. {}",exSumBusinessInfoView);
        }

        if(!Util.isNull(exSummaryView)){
            characteristicExSumReport.setBusinessOperationActivity(exSummaryView.getBusinessOperationActivity());
            characteristicExSumReport.setBusinessPermission(exSummaryView.getBusinessPermission());
            characteristicExSumReport.setExpiryDate(exSummaryView.getExpiryDate());
        } else {
            log.debug("exSummaryView in Method fillBorrowerCharacteristic is Null. {}",exSummaryView);
        }


        log.info("fillBorrowerCharacteristic: {}",characteristicExSumReport.toString());
        return characteristicExSumReport;
    }

    public AccountMovementExSumReport fillAccountMovement(){
        init();
        AccountMovementExSumReport movementExSumReport = new AccountMovementExSumReport();
        List<ExSumAccountMovementView> movementViewList = exSummaryView.getExSumAccMovementViewList();

        if(!Util.isNull(movementViewList)){
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
        } else {
            log.debug("movementViewList in Method fillAccountMovement id Null. {}",movementViewList);
        }
        return movementExSumReport;
    }

    public CollateralExSumReport fillCollateral(){
        init();
        CollateralExSumReport collateralExSumReport = new CollateralExSumReport();

        if (!Util.isNull(exSummary)){
            collateralExSumReport.setCashCollateralValue(exSummary.getCashCollateralValue());
            collateralExSumReport.setCoreAssetValue(exSummary.getCoreAssetValue());
            collateralExSumReport.setNoneCoreAssetValue(exSummary.getNoneCoreAssetValue());
            collateralExSumReport.setLimitApprove(exSummary.getLimitApprove());
            collateralExSumReport.setPercentLTV(exSummary.getPercentLTV());
        } else {
            log.debug("exSummary is Method fillCollateral is Null. {}",exSummary);
        }
        return collateralExSumReport;
    }

    public CreditRiskInfoExSumReport fillCreditRisk(){
        init();
        CreditRiskInfoExSumReport riskInfoExSumReport = new CreditRiskInfoExSumReport();
        ExSumCreditRiskInfoView exSumCreditRiskInfoView = exSummaryView.getExSumCreditRiskInfoView();

        if (!Util.isNull(exSumCreditRiskInfoView)){
            riskInfoExSumReport.setRiskCusType(exSumCreditRiskInfoView.getRiskCusType());
            riskInfoExSumReport.setBotClass(exSumCreditRiskInfoView.getBotClass());
            riskInfoExSumReport.setReason(exSumCreditRiskInfoView.getReason());
            riskInfoExSumReport.setLastReviewDate(exSumCreditRiskInfoView.getLastReviewDate());
            riskInfoExSumReport.setNextReviewDate(exSumCreditRiskInfoView.getNextReviewDate());
            riskInfoExSumReport.setExtendedReviewDate(exSumCreditRiskInfoView.getExtendedReviewDate());
            riskInfoExSumReport.setIndirectCountryName(exSumCreditRiskInfoView.getIndirectCountryName());
            riskInfoExSumReport.setPercentExport(exSumCreditRiskInfoView.getPercentExport());
        } else {
            log.debug("exSumCreditRiskInfoView in Method fillCreditRisk is Null. {}",exSumCreditRiskInfoView);
        }
        return riskInfoExSumReport;
    }

    public DecisionExSumReport fillDecision(){
        init();
        DecisionExSumReport decisionExSumReport = new DecisionExSumReport();
        List<ExSumDecisionView> exSumDecisionView = exSummaryView.getExSumDecisionListView();

        if(!Util.isNull(exSumDecisionView)){
            for (ExSumDecisionView decisionView : exSumDecisionView){
                decisionExSumReport.setId(decisionView.getId());
                decisionExSumReport.setFlag(decisionView.getFlag());
                decisionExSumReport.setGroup(decisionView.getGroup());
                decisionExSumReport.setRuleName(decisionView.getRuleName());
                decisionExSumReport.setCusName(decisionView.getCusName());
                decisionExSumReport.setDeviationReason(decisionView.getDeviationReason());
            }
        } else {
            log.debug("ExSumDecisionView in Method fillDecision is Null. {}",exSumDecisionView);
        }
        return decisionExSumReport;
    }

    public BizSupportExSumReport fillBizSupport(){
        init();
        BizSupportExSumReport bizSupportExSumReport = new BizSupportExSumReport();

        if (!Util.isNull(exSummaryView)){
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
        } else {
            log.debug("exSummaryView in Method fillBizSupport is Null. {}",exSummaryView);
        }
        return bizSupportExSumReport;
    }

    public UWDecisionExSumReport fillUWDecision(){
        init();
        UWDecisionExSumReport uwDecisionExSumReport = new UWDecisionExSumReport();
        List<ExSumReasonView> exSumReasonViews = exSummaryView.getDeviateCode();

        if (!Util.isNull(exSummaryView)){
            uwDecisionExSumReport.setUwCode(exSummaryView.getUwCode());
            uwDecisionExSumReport.setName(exSummaryView.getApproveAuthority().getName());
            uwDecisionExSumReport.setDecision(exSummaryView.getDecision());
            uwDecisionExSumReport.setUwComment(exSummaryView.getUwComment());
        } else {
            log.debug("exSummaryView in Method fillUWDecision is Null. {}",exSummaryView);
        }

        if(!Util.isNull(exSumReasonViews)){
            for (ExSumReasonView sumReasonView : exSumReasonViews) {
                uwDecisionExSumReport.setCode(sumReasonView.getCode());
                uwDecisionExSumReport.setDescription(sumReasonView.getDescription());
            }
        } else {
            log.debug("exSumReasonViews in Method fillUWDecision is Null. {}",exSumReasonViews);
        }
        return uwDecisionExSumReport;
    }
}
