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

//    long workCaseId = 147;

    long workCaseId;

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
        log.info("workCaseID: {}",workCaseId);

        if(!Util.isNull(workCaseId) && !Util.isZero(workCaseId)){
            exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
            exSummaryView  = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId);

            log.debug("exSummaryView: {}",exSummaryView);
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
        init();
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
        init();
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
        init();
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
        init();
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
        init();
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
        init();
        CollateralExSumReport collateralExSumReport = new CollateralExSumReport();

        if (!Util.isNull(exSummary)){
            collateralExSumReport.setCashCollateralValue(Util.convertNullToZERO(exSummary.getCashCollateralValue()));
            collateralExSumReport.setCoreAssetValue(Util.convertNullToZERO(exSummary.getCoreAssetValue()));
            collateralExSumReport.setNoneCoreAssetValue(Util.convertNullToZERO(exSummary.getNoneCoreAssetValue()));
            collateralExSumReport.setLimitApprove(Util.convertNullToZERO(exSummary.getLimitApprove()));
            collateralExSumReport.setPercentLTV(Util.convertNullToZERO(exSummary.getPercentLTV()));
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

    public DecisionExSumReport fillDecision(){
        init();
        DecisionExSumReport decisionExSumReport = new DecisionExSumReport();
        List<ExSumDecisionView> exSumDecisionView = exSummaryView.getExSumDecisionListView();

        if(!Util.isNull(exSumDecisionView)){
            for (ExSumDecisionView decisionView : exSumDecisionView){
                decisionExSumReport.setId(decisionView.getId());
//                decisionExSumReport.setFlag(Util.checkNullString(decisionView.getFlag()));
                decisionExSumReport.setGroup(Util.checkNullString(decisionView.getGroup()));
                decisionExSumReport.setRuleName(Util.checkNullString(decisionView.getRuleName()));
                decisionExSumReport.setCusName(Util.checkNullString(decisionView.getCusName()));
                decisionExSumReport.setDeviationReason(Util.checkNullString(decisionView.getDeviationReason()));
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
        init();
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
}
