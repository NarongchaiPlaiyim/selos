package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.dao.master.TitleDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.ExSummaryDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.UWResultColor;
import com.clevel.selos.model.db.working.ExSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.report.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PDFExecutiveSummary implements Serializable {
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

    @Inject
    private AppHeaderView appHeaderView;

    @Inject
    private WorkCaseDAO workCaseDAO;

    @Inject
    DecisionView decisionView;

    long workCaseId;
    private final String SPACE = " ";
    WorkCase workCase;

    @Inject
    DecisionControl decisionControl;

    @Inject
    public PDFExecutiveSummary() {
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

        exSummaryView = new ExSummaryView();

        if(!Util.isNull(workCaseId) && !Util.isZero(workCaseId)){
            exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
            decisionView = decisionControl.getDecisionView(workCaseId);
            exSummaryView  = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId);

            log.debug("exSummaryView: {}, decision: {}",exSummaryView,decisionView);
        } else {
            log.debug("workCaseId is Null. {}",workCaseId);
        }
    }

    public List<BorrowerExsumReport> fillBorrowerRelatedProfile(){
//        init();
        List<BorrowerExsumReport> reports = new ArrayList<BorrowerExsumReport>();
        List<CustomerInfoView> customerInfoViewList = exSummaryView.getBorrowerListView();

        int count = 1 ;
        if(!Util.isNull(customerInfoViewList)){
            for (CustomerInfoView view:customerInfoViewList){
                BorrowerExsumReport borrowerExsumReport = new BorrowerExsumReport();
                borrowerExsumReport.setNo(count++);
                borrowerExsumReport.setTitleTh(Util.checkNullString(view.getTitleTh().getTitleTh()));
                borrowerExsumReport.setFirstNameTh(Util.checkNullString(view.getFirstNameTh()));
                borrowerExsumReport.setLastNameTh(Util.checkNullString(view.getLastNameTh()));
                borrowerExsumReport.setCitizenId(Util.checkNullString(view.getCitizenId()));
                borrowerExsumReport.setRegistrationId(Util.checkNullString(view.getRegistrationId()));
                borrowerExsumReport.setTmbCustomerId(Util.checkNullString(view.getTmbCustomerId()));
                borrowerExsumReport.setRelation(Util.checkNullString(view.getRelation().getDescription()));
                if(view.getCollateralOwner() == 2){
                    borrowerExsumReport.setCollateralOwner("Y");
                } else {
                    borrowerExsumReport.setCollateralOwner("N");
                }

                borrowerExsumReport.setIndLv(Util.checkNullString(view.getIndLv()));
                borrowerExsumReport.setJurLv(Util.checkNullString(view.getJurLv()));
                borrowerExsumReport.setPercentShare(Util.convertNullToZERO(view.getPercentShare()));
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

                if (Util.safetyList(customerCSIList).size() > 0){
                    for(CustomerCSIView csiView:customerCSIList){
                        borrowerExsumReport.setCustomerCSIList(Util.checkNullString(csiView.getWarningCode().getCode()));
                    }
                } else {
                    borrowerExsumReport.setCustomerCSIList("-");
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
//        init();
        List<TradeFinanceExsumReport> financeExsumReports = new ArrayList<TradeFinanceExsumReport>();

        List<NewCreditFacilityView>  newCreditFacilityViewArrayList = new ArrayList<NewCreditFacilityView>();
        NewCreditFacilityView creditFacilityViews = exSummaryView.getTradeFinance();

        if (!Util.isNull(creditFacilityViews)){
            log.info("creditFacilityViews: {}",creditFacilityViews);
            newCreditFacilityViewArrayList.add(creditFacilityViews);

            if (Util.safetyList(newCreditFacilityViewArrayList).size() > 0) {
                for (NewCreditFacilityView facilityView : newCreditFacilityViewArrayList){
                    TradeFinanceExsumReport tradeFinanceExsumReport = new TradeFinanceExsumReport();
                    tradeFinanceExsumReport.setContactName(Util.checkNullString(facilityView.getContactName()));
                    tradeFinanceExsumReport.setContactPhoneNo(Util.checkNullString(facilityView.getContactPhoneNo()));
                    tradeFinanceExsumReport.setInterService(Util.checkNullString(facilityView.getInterService()));
                    tradeFinanceExsumReport.setCurrentAddress(Util.checkNullString(facilityView.getCurrentAddress()));
                    tradeFinanceExsumReport.setImportMail(Util.checkNullString(facilityView.getImportMail()));
                    tradeFinanceExsumReport.setExportMail(Util.checkNullString(facilityView.getExportMail()));
                    tradeFinanceExsumReport.setDepositBranchCode(Util.checkNullString(facilityView.getDepositBranchCode()));
                    tradeFinanceExsumReport.setOwnerBranchCode(Util.checkNullString(facilityView.getOwnerBranchCode()));
                    financeExsumReports.add(tradeFinanceExsumReport);
                }
            } else {
                log.debug("--newCreditFacilityViewArrayList. {}",newCreditFacilityViewArrayList.size());
            }

        } else {
            log.debug("newCreditFacilityViewArrayList in Method fillTradeFinance is Null. {}",newCreditFacilityViewArrayList);
        }
        return financeExsumReports;
    }

    public List<NCBRecordExsumReport> fillNCBRecord(){
//        init();
        List<NCBRecordExsumReport> recordExsumReports = new ArrayList<NCBRecordExsumReport>();
        List<NCBInfoView> ncbInfoViewList = exSummaryView.getNcbInfoListView();

        if (!Util.isNull(ncbInfoViewList)){
            for (NCBInfoView ncbInfoView:ncbInfoViewList){
                NCBRecordExsumReport ncbRecordExsumReport = new NCBRecordExsumReport();
                ncbRecordExsumReport.setNcbCusName(Util.checkNullString(ncbInfoView.getNcbCusName()));
                ncbRecordExsumReport.setCheckIn6Month(ncbInfoView.getCheckIn6Month());
                ncbRecordExsumReport.setCurrentPaymentType(Util.checkNullString(ncbInfoView.getCurrentPaymentType()));
                ncbRecordExsumReport.setHistoryPaymentType(Util.checkNullString(ncbInfoView.getHistoryPaymentType()));
                ncbRecordExsumReport.setNplFlagText(Util.checkNullString(ncbInfoView.getNplFlagText()));
                ncbRecordExsumReport.setTdrFlagText(Util.checkNullString(ncbInfoView.getTdrFlagText()));
                ncbRecordExsumReport.setDescription(Util.checkNullString(ncbInfoView.getTdrCondition().getDescription()));
                ncbRecordExsumReport.setPaymentClass(Util.checkNullString(ncbInfoView.getPaymentClass()));

                recordExsumReports.add(ncbRecordExsumReport);
            }
        } else {
            log.debug("ncbInfoViewList in Method fillNCBRecord is Null. {}",ncbInfoViewList);
        }
        return recordExsumReports;
    }

    public BorrowerExsumReport fillBorrower(){
//        init();
        BorrowerExsumReport borrowerExsumReport = new BorrowerExsumReport();

        if(!Util.isNull(exSummaryView)){
            borrowerExsumReport.setBusinessLocationName(Util.checkNullString(exSummaryView.getBusinessLocationName()));
            borrowerExsumReport.setBusinessLocationAddress(Util.checkNullString(exSummaryView.getBusinessLocationAddress()));
            borrowerExsumReport.setBusinessLocationAddressEN(Util.checkNullString(exSummaryView.getBusinessLocationAddressEN()));
            borrowerExsumReport.setOwner(Util.checkNullString(exSummaryView.getOwner()));
        } else {
            log.debug("exSummaryView in Method fillBorrower  is Null. {}",exSummaryView);
        }
        log.info("fillBorrower: {}",borrowerExsumReport.toString());
        return borrowerExsumReport;
    }

    public BorrowerCharacteristicExSumReport fillBorrowerCharacteristic(){
//        init();
        BorrowerCharacteristicExSumReport characteristicExSumReport = new BorrowerCharacteristicExSumReport();
        ExSumCharacteristicView exSumCharacteristicView = exSummaryView.getExSumCharacteristicView();
        log.debug("exSumCharacteristicView: {}",exSumCharacteristicView);

        if(!Util.isNull(exSumCharacteristicView)){
            characteristicExSumReport.setCustomer(Util.checkNullString(exSumCharacteristicView.getCustomer()));
            characteristicExSumReport.setCurrentDBR(Util.convertNullToZERO(exSumCharacteristicView.getCurrentDBR()));
            characteristicExSumReport.setFinalDBR(Util.convertNullToZERO(exSumCharacteristicView.getFinalDBR()));
            characteristicExSumReport.setIncome(Util.convertNullToZERO(exSumCharacteristicView.getIncome()));
            characteristicExSumReport.setRecommendedWCNeed(Util.convertNullToZERO(exSumCharacteristicView.getRecommendedWCNeed()));
            characteristicExSumReport.setActualWC(Util.convertNullToZERO(exSumCharacteristicView.getActualWC()));
            characteristicExSumReport.setStartBusinessDate(exSumCharacteristicView.getStartBusinessDate());
            characteristicExSumReport.setYearInBusiness(Util.checkNullString(exSumCharacteristicView.getYearInBusiness()));
            characteristicExSumReport.setSalePerYearBDM(Util.convertNullToZERO(exSumCharacteristicView.getSalePerYearBDM()));
            characteristicExSumReport.setSalePerYearUW(Util.convertNullToZERO(exSumCharacteristicView.getSalePerYearUW()));
            characteristicExSumReport.setGroupSaleBDM(Util.convertNullToZERO(exSumCharacteristicView.getGroupSaleBDM()));
            characteristicExSumReport.setGroupSaleUW(Util.convertNullToZERO(exSumCharacteristicView.getGroupSaleUW()));
            characteristicExSumReport.setGroupExposureBDM(Util.convertNullToZERO(exSumCharacteristicView.getGroupExposureBDM()));
            characteristicExSumReport.setGroupExposureUW(Util.convertNullToZERO(exSumCharacteristicView.getGroupExposureUW()));
        } else {
            log.debug("exSumCharacteristicView in Method fillBorrowerCharacteristic is Null. {}",exSumCharacteristicView);
        }


        ExSumBusinessInfoView exSumBusinessInfoView = exSummaryView.getExSumBusinessInfoView();

        if (!Util.isNull(exSumBusinessInfoView)){
            characteristicExSumReport.setNetFixAsset(Util.convertNullToZERO(exSumBusinessInfoView.getNetFixAsset()));
            characteristicExSumReport.setNoOfEmployee(Util.convertNullToZERO(exSumBusinessInfoView.getNoOfEmployee()));
            characteristicExSumReport.setBizProvince(Util.checkNullString(exSumBusinessInfoView.getBizProvince()));
            characteristicExSumReport.setBizType(Util.checkNullString(exSumBusinessInfoView.getBizType()));
            characteristicExSumReport.setBizGroup(Util.checkNullString(exSumBusinessInfoView.getBizGroup()));
            characteristicExSumReport.setBizCode(Util.checkNullString(exSumBusinessInfoView.getBizCode()));
            characteristicExSumReport.setBizDesc(Util.checkNullString(exSumBusinessInfoView.getBizDesc()));
            characteristicExSumReport.setQualitativeClass(Util.checkNullString(exSumBusinessInfoView.getQualitativeClass()));
            characteristicExSumReport.setBizSize(Util.convertNullToZERO(exSumBusinessInfoView.getBizSize()));
            characteristicExSumReport.setBDM(Util.convertNullToZERO(exSumBusinessInfoView.getBDM()));
            characteristicExSumReport.setUW(Util.convertNullToZERO(exSumBusinessInfoView.getUW()));
            characteristicExSumReport.setAR(Util.convertNullToZERO(exSumBusinessInfoView.getAR()));
            characteristicExSumReport.setAP(Util.convertNullToZERO(exSumBusinessInfoView.getAP()));
            characteristicExSumReport.setINV(Util.convertNullToZERO(exSumBusinessInfoView.getINV()));
        } else {
            log.debug("exSumBusinessInfoView in Mrthod fillBorrowerCharacteristic is Null. {}",exSumBusinessInfoView);
        }

        if(!Util.isNull(exSummaryView)){
            characteristicExSumReport.setBusinessOperationActivity(Util.checkNullString(exSummaryView.getBusinessOperationActivity()));
            characteristicExSumReport.setBusinessPermission(Util.checkNullString(exSummaryView.getBusinessPermission()));
            characteristicExSumReport.setExpiryDate(exSummaryView.getExpiryDate());
        } else {
            log.debug("exSummaryView in Method fillBorrowerCharacteristic is Null. {}",exSummaryView);
        }


        log.info("fillBorrowerCharacteristic: {}",characteristicExSumReport.toString());
        return characteristicExSumReport;
    }

    public AccountMovementExSumReport fillAccountMovement(){
//        init();
        AccountMovementExSumReport movementExSumReport = new AccountMovementExSumReport();
        List<ExSumAccountMovementView> movementViewList = exSummaryView.getExSumAccMovementViewList();

        if(!Util.isNull(movementViewList)){
            for (ExSumAccountMovementView movementView:movementViewList){
                movementExSumReport.setOdLimit(Util.convertNullToZERO(movementView.getOdLimit()));
                movementExSumReport.setUtilization(Util.convertNullToZERO(movementView.getUtilization()));
                movementExSumReport.setSwing(Util.convertNullToZERO(movementView.getSwing()));
                movementExSumReport.setOverLimitTimes(Util.convertNullToZERO(movementView.getOverLimitTimes()));
                movementExSumReport.setOverLimitDays(Util.convertNullToZERO(movementView.getOverLimitDays()));
                movementExSumReport.setChequeReturn(Util.convertNullToZERO(movementView.getChequeReturn()));
                movementExSumReport.setCashFlow(Util.convertNullToZERO(movementView.getCashFlow()));
                movementExSumReport.setCashFlowLimit(Util.convertNullToZERO(movementView.getCashFlowLimit()));
                movementExSumReport.setTradeChequeReturnAmount(Util.convertNullToZERO(movementView.getTradeChequeReturnPercent()));
                movementExSumReport.setTradeChequeReturnPercent(Util.convertNullToZERO(movementView.getTradeChequeReturnPercent()));
            }
        } else {
            log.debug("movementViewList in Method fillAccountMovement id Null. {}",movementViewList);
        }
        return movementExSumReport;
    }

    public CollateralExSumReport fillCollateral(){
//        init();
        CollateralExSumReport collateralExSumReport = new CollateralExSumReport();
        ExSumCollateralView exSumCollateralView = exSummaryView.getExSumCollateralView();

        if (!Util.isNull(exSumCollateralView)){
            collateralExSumReport.setCashCollateralValue(Util.convertNullToZERO(exSumCollateralView.getCashCollateralValue()));
            collateralExSumReport.setCoreAssetValue(Util.convertNullToZERO(exSumCollateralView.getCoreAssetValue()));
            collateralExSumReport.setNoneCoreAssetValue(Util.convertNullToZERO(exSumCollateralView.getNoneCoreAssetValue()));
            collateralExSumReport.setLimitApprove(Util.convertNullToZERO(exSumCollateralView.getLimitApprove()));
            collateralExSumReport.setPercentLTV(Util.convertNullToZERO(exSumCollateralView.getPercentLTV()));
        } else {
            log.debug("exSummary is Method fillCollateral is Null. {}",exSumCollateralView);
        }
        return collateralExSumReport;
    }

    public CreditRiskInfoExSumReport fillCreditRisk(){
//        init();
        CreditRiskInfoExSumReport riskInfoExSumReport = new CreditRiskInfoExSumReport();
        ExSumCreditRiskInfoView exSumCreditRiskInfoView = exSummaryView.getExSumCreditRiskInfoView();

        if (!Util.isNull(exSumCreditRiskInfoView)){
            riskInfoExSumReport.setRiskCusType(Util.checkNullString(exSumCreditRiskInfoView.getRiskCusType()));
            riskInfoExSumReport.setBotClass(Util.checkNullString(exSumCreditRiskInfoView.getBotClass()));
            riskInfoExSumReport.setReason(Util.checkNullString(exSumCreditRiskInfoView.getReason()));
            riskInfoExSumReport.setLastReviewDate(exSumCreditRiskInfoView.getLastReviewDate());
            riskInfoExSumReport.setNextReviewDate(exSumCreditRiskInfoView.getNextReviewDate());
            riskInfoExSumReport.setExtendedReviewDate(exSumCreditRiskInfoView.getExtendedReviewDate());
            riskInfoExSumReport.setIndirectCountryName(Util.checkNullString(exSumCreditRiskInfoView.getIndirectCountryName()));
            riskInfoExSumReport.setPercentExport(Util.convertNullToZERO(exSumCreditRiskInfoView.getPercentExport()));
        } else {
            log.debug("exSumCreditRiskInfoView in Method fillCreditRisk is Null. {}",exSumCreditRiskInfoView);
        }
        return riskInfoExSumReport;
    }

    public List<DecisionExSumReport> fillDecision(){
//        init();
        List<DecisionExSumReport> exSumReportList = new ArrayList<DecisionExSumReport>();
        List<ExSumDecisionView> exSumDecisionView = exSummaryView.getExSumDecisionListView();
        int id = 0;
        if(!Util.isNull(exSumDecisionView)){
            for (ExSumDecisionView decisionView : exSumDecisionView){
                DecisionExSumReport decisionExSumReport = new DecisionExSumReport();
                decisionExSumReport.setId(id++);

                if (decisionView.getFlag().code() == "Y"){
                    decisionExSumReport.setFlag("YELLOW");
                } else  if (decisionView.getFlag().code() == "R"){
                    decisionExSumReport.setFlag("RED");
                } else {
                    decisionExSumReport.setFlag("GREEN");
                }
//                decisionExSumReport.setFlag(Util.checkNullString(decisionView.getFlag()));
                log.debug("--Flag. {}", decisionView.getFlag().code());
                log.debug("--GREEN. {}", UWResultColor.GREEN.code());
                log.debug("--RED. {}",UWResultColor.RED.getResultClass());
                log.debug("--YELLOW. {}",UWResultColor.YELLOW.colorCode());

                decisionExSumReport.setGroup(Util.checkNullString(decisionView.getGroup()));
                decisionExSumReport.setRuleName(Util.checkNullString(decisionView.getRuleName()));
                decisionExSumReport.setCusName(Util.checkNullString(decisionView.getCusName()));
                decisionExSumReport.setDeviationReason(Util.checkNullString(decisionView.getDeviationReason()));
                exSumReportList.add(decisionExSumReport);
            }
        } else {
            log.debug("ExSumDecisionView in Method fillDecision is Null. {}",exSumReportList);
        }
        return exSumReportList;
    }

    public BizSupportExSumReport fillBizSupport(){
//        init();
        BizSupportExSumReport bizSupportExSumReport = new BizSupportExSumReport();

        if (!Util.isNull(exSummaryView)){
            bizSupportExSumReport.setNatureOfBusiness(Util.checkNullString(exSummaryView.getNatureOfBusiness()));
            bizSupportExSumReport.setHistoricalAndReasonOfChange(Util.checkNullString(exSummaryView.getHistoricalAndReasonOfChange()));
            bizSupportExSumReport.setTmbCreditHistory(Util.checkNullString(exSummaryView.getTmbCreditHistory()));
            bizSupportExSumReport.setSupportReason(Util.checkNullString(exSummaryView.getSupportReason()));
            bizSupportExSumReport.setRm008Code(exSummaryView.getRm008Code());
            bizSupportExSumReport.setRm008Remark(Util.checkNullString(exSummaryView.getRm008Remark()));
            bizSupportExSumReport.setRm204Code(exSummaryView.getRm204Code());
            bizSupportExSumReport.setRm204Remark(Util.checkNullString(exSummaryView.getRm204Remark()));
            bizSupportExSumReport.setRm020Code(exSummaryView.getRm020Code());
            bizSupportExSumReport.setRm020Remark(Util.checkNullString(exSummaryView.getRm020Remark()));
        } else {
            log.debug("exSummaryView in Method fillBizSupport is Null. {}",exSummaryView);
        }
        return bizSupportExSumReport;
    }

    public UWDecisionExSumReport fillUWDecision(){
//        init();
        UWDecisionExSumReport uwDecisionExSumReport = new UWDecisionExSumReport();
        List<ExSumReasonView> exSumReasonViews = exSummaryView.getDeviateCode();

        if (!Util.isNull(exSummaryView)){
            uwDecisionExSumReport.setUwCode(Util.checkNullString(exSummaryView.getUwCode()));
            uwDecisionExSumReport.setName(Util.checkNullString(exSummaryView.getApproveAuthority().getName()));
            uwDecisionExSumReport.setDecision(exSummaryView.getDecision());
            uwDecisionExSumReport.setUwComment(Util.checkNullString(exSummaryView.getUwComment()));
        } else {
            log.debug("exSummaryView in Method fillUWDecision is Null. {}",exSummaryView);
        }

        if(!Util.isNull(exSumReasonViews)){
            for (ExSumReasonView sumReasonView : exSumReasonViews) {
                uwDecisionExSumReport.setCode(Util.checkNullString(sumReasonView.getCode()));
                uwDecisionExSumReport.setDescription(Util.checkNullString(sumReasonView.getDescription()));
            }
        } else {
            log.debug("exSumReasonViews in Method fillUWDecision is Null. {}",exSumReasonViews);
        }
        return uwDecisionExSumReport;
    }

    public HeaderAndFooterReport fillHeader(){
        HeaderAndFooterReport report = new HeaderAndFooterReport();

        HttpSession session = FacesUtil.getSession(true);
        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");
        workCase = workCaseDAO.findById(workCaseId);
        //Detail 1
        if (!Util.isNull(appHeaderView)){
            log.debug("--Header. {}",appHeaderView);
            report.setCaseStatus(Util.checkNullString(appHeaderView.getCaseStatus()));
            report.setBdmName(Util.checkNullString(appHeaderView.getBdmName()));
            report.setBdmPhoneNumber(Util.checkNullString(appHeaderView.getBdmPhoneNumber()));
            report.setBdmPhoneExtNumber(Util.checkNullString(appHeaderView.getBdmPhoneExtNumber()));
            report.setBdmZoneName(Util.checkNullString(appHeaderView.getBdmZoneName()));
            report.setBdmRegionName(Util.checkNullString(appHeaderView.getBdmRegionName()));
            report.setSubmitDate(Util.checkNullString(appHeaderView.getSubmitDate()));
            report.setUwName(Util.checkNullString(appHeaderView.getUwName()));
            report.setUwPhoneNumber(Util.checkNullString(appHeaderView.getUwPhoneNumber()));
            report.setUwPhoneExtNumber(Util.checkNullString(appHeaderView.getUwPhoneExtNumber()));
            report.setUwTeamName(Util.checkNullString(appHeaderView.getUwTeamName()));
            report.setRequestType(Util.checkNullString(appHeaderView.getRequestType()));
            report.setAppNo(Util.checkNullString(appHeaderView.getAppNo()));
            report.setAppRefNo(Util.checkNullString(appHeaderView.getAppRefNo()));
            report.setAppRefDate(Util.checkNullString(appHeaderView.getAppRefDate()));
            report.setProductGroup(Util.checkNullString(appHeaderView.getProductGroup()));
            report.setRefinance(Util.checkNullString(appHeaderView.getRefinance()));


            log.debug("--getBorrowerHeaderViewList Size. {}",appHeaderView.getBorrowerHeaderViewList().size());

            for (int i = 0;i < appHeaderView.getBorrowerHeaderViewList().size() && i < 5; i++){
                switch (i){
                    case 0 : report.setBorrowerName(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName()));
                        report.setPersonalId(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId()));
                        break;
                    case 1 : report.setBorrowerName2(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName()));
                        report.setPersonalId2(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId()));
                        break;
                    case 2 : report.setBorrowerName3(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName()));
                        report.setPersonalId3(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId()));
                        break;
                    case 3 : report.setBorrowerName4(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName()));
                        report.setPersonalId4(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId()));
                        break;
                    case 4 : report.setBorrowerName5(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName()));
                        report.setPersonalId5(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId()));
                        break;
                }
            }

            report.setCreditDecision(Util.checkNullString(appHeaderView.getProductGroup()));
            report.setApprovedDate(workCase.getCompleteDate());

        } else {
            log.debug("--Header is Null. {}",appHeaderView);
        }
        return report;
    }


    //Decision
    public HeaderAndFooterReport fillFooter(){
        HeaderAndFooterReport report = new HeaderAndFooterReport();

        String date = Util.createDateAndTimeTh(new Date());
        log.debug("--Date. {}",date);

        String userName =  decisionControl.getCurrentUserID();
        log.debug("---------- {}",userName);

        StringBuilder genFooter = new StringBuilder();
        genFooter = genFooter.append(userName).append(SPACE).append(date);
        report.setGenFooter(genFooter.toString());




        return report;
    }

    public List<BorrowerCreditDecisionReport> fillCreditBorrower(String pathsub){
        log.debug("on fillCreditBorrower. {}");
//        init();
        List<ExistingCreditDetailView> existingCreditDetailViews = decisionView.getExtBorrowerComCreditList();
        List<BorrowerCreditDecisionReport> borrowerCreditDecisionReportList = new ArrayList<BorrowerCreditDecisionReport>();

        int count = 1;
        if (Util.safetyList(existingCreditDetailViews).size() > 0){
            log.debug("existingCreditDetailViews by fillCreditBorrower. {}",existingCreditDetailViews);
            for (ExistingCreditDetailView detailView : existingCreditDetailViews){
                BorrowerCreditDecisionReport decisionReport = new BorrowerCreditDecisionReport();
                decisionReport.setCount(count++);
                decisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                decisionReport.setAccount(account.toString());
                log.debug("accountLable by creditBorrower. {}",account.toString());

                decisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                decisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                decisionReport.setCode(code.toString());
                log.debug("codeLable by creditBorrower. {}", code.toString());

                decisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                decisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                decisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                decisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));

                decisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                log.debug("--ExistingCreditTierDetailViewList. {}",detailView.getExistingCreditTierDetailViewList());
                decisionReport.setExistingSplitLineDetailViewList(Util.safetyList(detailView.getExistingSplitLineDetailViewList()));
                log.debug("--ExistingSplitLineDetailViewList. {}",detailView.getExistingSplitLineDetailViewList());

                borrowerCreditDecisionReportList.add(decisionReport);
            }
        } else {
            BorrowerCreditDecisionReport decisionReport = new BorrowerCreditDecisionReport();
            decisionReport.setPath(pathsub);
            borrowerCreditDecisionReportList.add(decisionReport);
            log.debug("existingCreditDetailViews is Null by fillCreditBorrower. {}",existingCreditDetailViews);
        }
        return borrowerCreditDecisionReportList;
    }

    public List<ConditionDecisionReport> fillCondition(){
        log.debug("on fillCondition. {}");
//        init();
        List<ConditionDecisionReport> conditionDecisionReportList = new ArrayList<ConditionDecisionReport>();
        List<ExistingConditionDetailView> existingConditionDetailViews = decisionView.getExtConditionComCreditList();
        int count =1;

        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillCondition. {}",existingConditionDetailViews);
            for (ExistingConditionDetailView view : existingConditionDetailViews){
                ConditionDecisionReport conditionDecisionReport = new ConditionDecisionReport();
                conditionDecisionReport.setCount(count++);
                conditionDecisionReport.setLoanType(Util.checkNullString(view.getLoanType()));
                conditionDecisionReport.setConditionDesc(Util.checkNullString(view.getConditionDesc()));
                conditionDecisionReportList.add(conditionDecisionReport);
            }
        } else {
            ConditionDecisionReport conditionDecisionReport = new ConditionDecisionReport();
            conditionDecisionReportList.add(conditionDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillCondition. {}",existingConditionDetailViews);
        }
        return conditionDecisionReportList;
    }

    public List<BorrowerRetailDecisionReport> fillBorrowerRetail(String pathsub){
        log.debug("on fillBorrowerRetail. {}");
//        init();
        List<BorrowerRetailDecisionReport> retailDecisionReportList = new ArrayList<BorrowerRetailDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtBorrowerRetailCreditList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillBorrowerRetail. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                BorrowerRetailDecisionReport borrowerRetailDecisionReport = new BorrowerRetailDecisionReport();
                borrowerRetailDecisionReport.setCount(count++);
                borrowerRetailDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                borrowerRetailDecisionReport.setAccount(account.toString());
                log.debug("accountLable by borrowerRetail. {}",account.toString());

                borrowerRetailDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                borrowerRetailDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));
                borrowerRetailDecisionReport.setCode(code.toString());
                log.debug("codeLable by borrowerRetail. {}",code.toString());

                borrowerRetailDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerRetailDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                borrowerRetailDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerRetailDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                borrowerRetailDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                log.debug("--ExistingCreditTierDetailViewList. {}",detailView.getExistingCreditTierDetailViewList());
                retailDecisionReportList.add(borrowerRetailDecisionReport);
            }
        } else {
            BorrowerRetailDecisionReport borrowerRetailDecisionReport = new BorrowerRetailDecisionReport();
            borrowerRetailDecisionReport.setPath(pathsub);
            retailDecisionReportList.add(borrowerRetailDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillBorrowerRetail. {}",existingConditionDetailViews);
        }
        return retailDecisionReportList;
    }

    public List<BorrowerAppInRLOSDecisionReport> fillAppInRLOS(String pathsub){
//        init();
        List<BorrowerAppInRLOSDecisionReport> borrowerAppInRLOSDecisionReportList = new ArrayList<BorrowerAppInRLOSDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtBorrowerAppInRLOSList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillAppInRLOS. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                BorrowerAppInRLOSDecisionReport borrowerAppInRLOSDecisionReport = new BorrowerAppInRLOSDecisionReport();
                borrowerAppInRLOSDecisionReport.setCount(count++);
                borrowerAppInRLOSDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                borrowerAppInRLOSDecisionReport.setAccount(account.toString());
                log.debug("account by borrowerAppInRLOSDecisionReport. {}",account.toString());

                borrowerAppInRLOSDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                borrowerAppInRLOSDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                borrowerAppInRLOSDecisionReport.setCode(code.toString());
                log.debug("codeLable by borrowerAppInRLOSDecisionReport. {}",code.toString());

                borrowerAppInRLOSDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerAppInRLOSDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                borrowerAppInRLOSDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerAppInRLOSDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                borrowerAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                borrowerAppInRLOSDecisionReportList.add(borrowerAppInRLOSDecisionReport);
            }
        } else {
            BorrowerAppInRLOSDecisionReport borrowerAppInRLOSDecisionReport = new BorrowerAppInRLOSDecisionReport();
            borrowerAppInRLOSDecisionReport.setPath(pathsub);
            borrowerAppInRLOSDecisionReportList.add(borrowerAppInRLOSDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillAppInRLOS. {}",existingConditionDetailViews);
        }
        return borrowerAppInRLOSDecisionReportList;
    }

    public List<RelatedCommercialDecisionReport> fillRelatedCommercial(String pathsub){
        List<RelatedCommercialDecisionReport> relatedCommercialDecisionReportList = new ArrayList<RelatedCommercialDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedComCreditList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillRelatedCommercial. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
                relatedCommercialDecisionReport.setCount(count++);
                relatedCommercialDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                relatedCommercialDecisionReport.setAccount(account.toString());
                log.debug("account by relatedCommercialDecisionReport. {}",account.toString());

                relatedCommercialDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                relatedCommercialDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                relatedCommercialDecisionReport.setCode(code.toString());
                log.debug("codeLable by relatedCommercialDecisionReport. {}",code.toString());

                relatedCommercialDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedCommercialDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedCommercialDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedCommercialDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                relatedCommercialDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                log.debug("--ExistingCreditTierDetailViewList. {}",detailView.getExistingCreditTierDetailViewList());
                relatedCommercialDecisionReport.setExistingSplitLineDetailViewList(Util.safetyList(detailView.getExistingSplitLineDetailViewList()));
                log.debug("--ExistingSplitLineDetailViewList. {}",detailView.getExistingSplitLineDetailViewList());
                relatedCommercialDecisionReportList.add(relatedCommercialDecisionReport);
            }
        } else {
            RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
            relatedCommercialDecisionReport.setPath(pathsub);
            relatedCommercialDecisionReportList.add(relatedCommercialDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedCommercial. {}",existingConditionDetailViews);
        }

        return relatedCommercialDecisionReportList;
    }

    public List<RelatedRetailDecisionReport> fillRelatedRetail(String pathsub){
        List<RelatedRetailDecisionReport> relatedRetailDecisionReportList = new ArrayList<RelatedRetailDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedRetailCreditList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillRelatedRetail. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                RelatedRetailDecisionReport relatedRetailDecisionReport = new RelatedRetailDecisionReport();
                relatedRetailDecisionReport.setCount(count++);
                relatedRetailDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                relatedRetailDecisionReport.setAccount(account.toString());
                log.debug("account by relatedRetailDecisionReport. {}",account.toString());

                relatedRetailDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                relatedRetailDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                relatedRetailDecisionReport.setCode(code.toString());
                log.debug("codeLable by relatedRetailDecisionReport. {}",code.toString());

                relatedRetailDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedRetailDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedRetailDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedRetailDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));

                relatedRetailDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                log.debug("--ExistingCreditTierDetailViewList. {}",detailView.getExistingCreditTierDetailViewList());

                relatedRetailDecisionReportList.add(relatedRetailDecisionReport);
            }
        } else {
            RelatedRetailDecisionReport relatedRetailDecisionReport = new RelatedRetailDecisionReport();
            relatedRetailDecisionReport.setPath(pathsub);
            relatedRetailDecisionReportList.add(relatedRetailDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedRetail. {}",existingConditionDetailViews);
        }

        return relatedRetailDecisionReportList;
    }

    public List<RelatedAppInRLOSDecisionReport> fillRelatedAppInRLOS(String pathsub){
        List<RelatedAppInRLOSDecisionReport> relatedAppInRLOSDecisionReportArrayList = new ArrayList<RelatedAppInRLOSDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedAppInRLOSList();

        int count = 1;
        if(Util.safetyList(existingConditionDetailViews).size() > 0){
            log.debug("existingConditionDetailViews by fillRelatedAppInRLOS. {}",existingConditionDetailViews);
            for (ExistingCreditDetailView detailView : decisionView.getExtRelatedAppInRLOSList()){
                RelatedAppInRLOSDecisionReport relatedAppInRLOSDecisionReport = new RelatedAppInRLOSDecisionReport();
                relatedAppInRLOSDecisionReport.setCount(count++);
                relatedAppInRLOSDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));

                relatedAppInRLOSDecisionReport.setAccount(account.toString());
                log.debug("account by relatedAppInRLOSDecisionReport. {}",account.toString());

                relatedAppInRLOSDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                relatedAppInRLOSDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                relatedAppInRLOSDecisionReport.setCode(code.toString());
                log.debug("codeLable by relatedAppInRLOSDecisionReport. {}",code.toString());

                relatedAppInRLOSDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedAppInRLOSDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedAppInRLOSDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedAppInRLOSDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                relatedAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                relatedAppInRLOSDecisionReportArrayList.add(relatedAppInRLOSDecisionReport);
            }
        } else {
            RelatedAppInRLOSDecisionReport relatedAppInRLOSDecisionReport = new RelatedAppInRLOSDecisionReport();
            relatedAppInRLOSDecisionReport.setPath(pathsub);
            relatedAppInRLOSDecisionReportArrayList.add(relatedAppInRLOSDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedAppInRLOS. {}",existingConditionDetailViews);
        }

        return relatedAppInRLOSDecisionReportArrayList;
    }

    public TotalDecisionReport fillTotalMasterReport(){
//        init();
        TotalDecisionReport totalDecisionReport = new TotalDecisionReport();

        //Existing Credit Borrower
        //Commercial Credit
        totalDecisionReport.setExtBorrowerTotalComLimit(Util.convertNullToZERO(decisionView.getExtBorrowerTotalComLimit()));
        //Retail Credit
        totalDecisionReport.setExtBorrowerTotalRetailLimit(Util.convertNullToZERO(decisionView.getExtBorrowerTotalRetailLimit()));
        //App in RLOS Process
        totalDecisionReport.setExtBorrowerTotalAppInRLOSLimit(Util.convertNullToZERO(decisionView.getExtBorrowerTotalAppInRLOSLimit()));
        //Borrower
        totalDecisionReport.setExtBorrowerTotalCommercial(Util.convertNullToZERO(decisionView.getExtBorrowerTotalCommercial()));
        totalDecisionReport.setExtBorrowerTotalComAndOBOD(Util.convertNullToZERO(decisionView.getExtBorrowerTotalComAndOBOD()));
        totalDecisionReport.setExtBorrowerTotalExposure(Util.convertNullToZERO(decisionView.getExtBorrowerTotalExposure()));

        //Existing Credit Related Person
        //Commercial Credit
        totalDecisionReport.setExtRelatedTotalComLimit(Util.convertNullToZERO(decisionView.getExtRelatedTotalComLimit()));
        //Retail Credit
        totalDecisionReport.setExtRelatedTotalRetailLimit(Util.convertNullToZERO(decisionView.getExtRelatedTotalRetailLimit()));
        //App in RLOS Process
        totalDecisionReport.setExtRelatedTotalAppInRLOSLimit(Util.convertNullToZERO(decisionView.getExtRelatedTotalAppInRLOSLimit()));

        //Total Related
        totalDecisionReport.setExtRelatedTotalCommercial(Util.convertNullToZERO(decisionView.getExtRelatedTotalCommercial()));
        totalDecisionReport.setExtRelatedTotalComAndOBOD(Util.convertNullToZERO(decisionView.getExtRelatedTotalComAndOBOD()));
        totalDecisionReport.setExtRelatedTotalExposure(Util.convertNullToZERO(decisionView.getExtRelatedTotalExposure()));

        //Total Group
        totalDecisionReport.setExtGroupTotalCommercial(Util.convertNullToZERO(decisionView.getExtGroupTotalCommercial()));
        totalDecisionReport.setExtGroupTotalComAndOBOD(Util.convertNullToZERO(decisionView.getExtGroupTotalComAndOBOD()));
        totalDecisionReport.setExtGroupTotalExposure(Util.convertNullToZERO(decisionView.getExtGroupTotalExposure()));

//        //Existing Collateral Borrower
//        totalDecisionReport.setExtBorrowerTotalAppraisalValue(Util.convertNullToZERO(decisionView.getExtBorrowerTotalAppraisalValue()));
//        totalDecisionReport.setExtBorrowerTotalMortgageValue(Util.convertNullToZERO(decisionView.getExtBorrowerTotalMortgageValue()));
//
//        //Existing Related Person
//        totalDecisionReport.setExtRelatedTotalAppraisalValue(Util.convertNullToZERO(decisionView.getExtRelatedTotalAppraisalValue()));
//        totalDecisionReport.setExtRelatedTotalMortgageValue(Util.convertNullToZERO(decisionView.getExtRelatedTotalMortgageValue()));
//
//        //Existing Guarantor
//        //Borrower
//        totalDecisionReport.setExtTotalGuaranteeAmount(Util.convertNullToZERO(decisionView.getExtTotalGuaranteeAmount()));
//        //Propose Credit
//        totalDecisionReport.setProposeTotalCreditLimit(Util.convertNullToZERO(decisionView.getProposeTotalCreditLimit()));
//
//        //Approved Propose Credit
//        totalDecisionReport.setApproveTotalCreditLimit(Util.convertNullToZERO(decisionView.getApproveTotalCreditLimit()));
//        if (decisionView.getCreditCustomerType() == CreditCustomerType.NORMAL){
//            totalDecisionReport.setCreditCusType(1);
//        } else if (decisionView.getCreditCustomerType() == CreditCustomerType.PRIME){
//            totalDecisionReport.setCreditCusType(2);
//        } else {
//            totalDecisionReport.setCreditCusType(0);
//        }
//        totalDecisionReport.setCrdRequestTypeName(Util.checkNullString(decisionView.getLoanRequestType().getName()));
//        totalDecisionReport.setCountryName(Util.checkNullString(decisionView.getInvestedCountry().getName()));
//        totalDecisionReport.setExistingSMELimit(Util.convertNullToZERO(decisionView.getExistingSMELimit()));
//        totalDecisionReport.setMaximumSMELimit(Util.convertNullToZERO(decisionView.getMaximumSMELimit()));
//
//        //Total Borrower
//        totalDecisionReport.setApproveBrwTotalCommercial(Util.convertNullToZERO(decisionView.getApproveBrwTotalCommercial()));
//        totalDecisionReport.setApproveBrwTotalComAndOBOD(Util.convertNullToZERO(decisionView.getApproveBrwTotalComAndOBOD()));
//        totalDecisionReport.setApproveTotalExposure(Util.convertNullToZERO(decisionView.getApproveTotalExposure()));
//
//        //Proposed Guarantor
//        totalDecisionReport.setProposeTotalGuaranteeAmt(Util.convertNullToZERO(decisionView.getProposeTotalGuaranteeAmt()));
//
//        //Approved Guarantor
//        totalDecisionReport.setApproveTotalGuaranteeAmt(Util.convertNullToZERO(decisionView.getApproveTotalGuaranteeAmt()));

        return totalDecisionReport;
    }

    public List<ApprovedCollateralDecisionReport> fillApprovedCollaterral(String pathsub){
//        init();
        List<ApprovedCollateralDecisionReport> approvedCollateralDecisionReportArrayList = new ArrayList<ApprovedCollateralDecisionReport>();
        List<NewCollateralView> newCollateralViews = decisionView.getApproveCollateralList();
        List<NewCollateralHeadView> collateralHeadViewList = new ArrayList<NewCollateralHeadView>();

        if (Util.safetyList(newCollateralViews).size() > 0){
            log.debug("newCollateralViews by fillProposedCollateral. {}",newCollateralViews);
            for (NewCollateralView view : newCollateralViews){
                ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
                approvedCollateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                approvedCollateralDecisionReport.setPath(pathsub);
                approvedCollateralDecisionReport.setAppraisalDate(DateTimeUtil.getCurrentDateTH(view.getAppraisalDate()));
                approvedCollateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecision()));
                approvedCollateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                approvedCollateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));
                approvedCollateralDecisionReport.setUsage(Util.checkNullString(view.getUsage()));
                approvedCollateralDecisionReport.setTypeOfUsage(Util.checkNullString(view.getTypeOfUsage()));
                approvedCollateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));
//                approvedCollateralDecisionReport.setUwDecision(Util.checkNullString(view.getUwDecision().getValue()));
                if (view.getUwDecision().equals("APPROVED")){
                    approvedCollateralDecisionReport.setApproved("Approved");
                } else if(view.getUwDecision().equals("REJECTED")){
                    approvedCollateralDecisionReport.setApproved("Rejected");
                } else {
                    approvedCollateralDecisionReport.setApproved("");
                }

                approvedCollateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                approvedCollateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));

                if (Util.safetyList(view.getProposeCreditDetailViewList()).size() > 0) {
                    log.debug("getProposeCreditDetailViewList. {}",view.getProposeCreditDetailViewList());
                    approvedCollateralDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditDetailViewList()));
                } else {
                    log.debug("getProposeCreditDetailViewList is Null. {}",view.getProposeCreditDetailViewList());
                }

                collateralHeadViewList = view.getNewCollateralHeadViewList();
                if (Util.safetyList(collateralHeadViewList).size() > 0){
                    log.debug("collateralHeadViewList. {}",collateralHeadViewList);
                    for (NewCollateralHeadView headView : collateralHeadViewList){
                        approvedCollateralDecisionReport.setCollateralDescription(Util.checkNullString(headView.getPotentialCollateral().getDescription()));
                        approvedCollateralDecisionReport.setPercentLTVDescription(Util.checkNullString(headView.getCollTypePercentLTV().getDescription()));
                        approvedCollateralDecisionReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                        approvedCollateralDecisionReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                        approvedCollateralDecisionReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                        approvedCollateralDecisionReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                        approvedCollateralDecisionReport.setHeadCollTypeDescription(Util.checkNullString(headView.getHeadCollType().getDescription()));
                        if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                            approvedCollateralDecisionReport.setInsuranceCompany("Partner");
                        } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                            approvedCollateralDecisionReport.setInsuranceCompany("Non Partner");
                        } else {
                            approvedCollateralDecisionReport.setInsuranceCompany("");
                        }
                        approvedCollateralDecisionReport.setSubViewList(Util.safetyList(headView.getNewCollateralSubViewList()));
                    }
                } else {
                    log.debug("collateralHeadViewList is Null. {}",collateralHeadViewList);
                }

                approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
            }

        } else {
            ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
            approvedCollateralDecisionReport.setPath(pathsub);
            approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
            log.debug("newCollateralViews is Null by fillProposedCollateral. {}",newCollateralViews);
        }
        return approvedCollateralDecisionReportArrayList;
    }

    public List<ApprovedGuarantorDecisionReport> fillApprovedGuarantor(String pathsub){
//        init();
        List<ApprovedGuarantorDecisionReport> approvedGuarantorDecisionReportList = new ArrayList<ApprovedGuarantorDecisionReport>();
        List<NewGuarantorDetailView> newGuarantorDetails = decisionView.getApproveGuarantorList();

        int count = 1;
        if (Util.safetyList(newGuarantorDetails).size() > 0){
            log.debug("newGuarantorDetails by fillApprovedGuarantor. {}",newGuarantorDetails);
            for (NewGuarantorDetailView view : newGuarantorDetails){
                ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();
                approvedGuarantorDecisionReport.setCount(count++);
                approvedGuarantorDecisionReport.setPath(pathsub);
                approvedGuarantorDecisionReport.setName(Util.checkNullString(view.getGuarantorName().getTitleTh().getTitleTh()+view.getGuarantorName().getFirstNameTh()+" "+view.getGuarantorName().getLastNameTh()));
                approvedGuarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));
                approvedGuarantorDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditDetailViewList()));
                approvedGuarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(view.getTotalLimitGuaranteeAmount()));
                if (view.getUwDecision().equals("APPROVED")){
                    approvedGuarantorDecisionReport.setUwDecision("Approved");
                } else if (view.getUwDecision().equals("REJECTED")){
                    approvedGuarantorDecisionReport.setUwDecision("Rejected");
                } else {
                    approvedGuarantorDecisionReport.setUwDecision("");
                }

                if(Util.isNull(view.getTotalLimitGuaranteeAmount())){
                    approvedGuarantorDecisionReport.setGuarantorType("ลด/ยกเลิกการค้ำประกัน");
                } else {
                    approvedGuarantorDecisionReport.setGuarantorType("บุคคลค้ำประกัน/นิติบุคคลค้ำประกัน");
                }

                approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
            }
        } else {
            log.debug("newGuarantorDetails is Null by fillApprovedGuarantor. {}",newGuarantorDetails);
            ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();
            approvedGuarantorDecisionReport.setPath(pathsub);
            approvedGuarantorDecisionReport.setProposeCreditDetailViewList(approvedGuarantorDecisionReport.getProposeCreditDetailViewList());
            approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
        }

        return approvedGuarantorDecisionReportList;
    }
}
