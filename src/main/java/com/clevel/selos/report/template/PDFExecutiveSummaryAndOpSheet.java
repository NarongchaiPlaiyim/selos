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
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RequestTypes;
import com.clevel.selos.model.db.working.ExSummary;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.report.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PDFExecutiveSummaryAndOpSheet implements Serializable {
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

    @Inject
    private BizInfoSummaryView bizInfoSummaryView;

    @Inject
    @NormalMessage
    Message msg;

    private long workCaseId;
    private long statusId;
    private final String SPACE = " ";
    private WorkCase workCase;

    @Inject
    DecisionControl decisionControl;

    private List<ProposeCreditInfoDetailView> newCreditDetailViewList;

    @Inject
    public PDFExecutiveSummaryAndOpSheet() {
    }

    public void init(){
        HttpSession session = FacesUtil.getSession(false);
        workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        statusId = Util.parseLong(session.getAttribute("statusId"), 0);
        exSummaryView = new ExSummaryView();
        if(workCaseId != 0){
            if (!Util.isNull(exSummaryDAO.findByWorkCaseId(workCaseId))){
                exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
            }
            if (!Util.isNull(decisionControl.findDecisionViewByWorkCaseId(workCaseId))){
                decisionView = decisionControl.findDecisionViewByWorkCaseId(workCaseId);
            }
            if (!Util.isNull(exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId, statusId))){
                exSummaryView  = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId, statusId);
            }
            if (Util.isNull(bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId))){
                bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
            }
        }else{
            log.debug("workCaseId is Null. {}", workCaseId);
        }
    }

    public List<BorrowerExsumReport> fillBorrowerRelatedProfile(){
        List<BorrowerExsumReport> reports = new ArrayList<BorrowerExsumReport>();
        List<CustomerInfoView> customerInfoViewList = exSummaryView.getBorrowerListView();

        int count = 1 ;
        if(Util.isSafetyList(customerInfoViewList)){
            log.debug("customerInfoViewList.size() {}",customerInfoViewList.size());
            for (CustomerInfoView view : customerInfoViewList){
                BorrowerExsumReport borrowerExsumReport = new BorrowerExsumReport();
                borrowerExsumReport.setNo(count++);
                borrowerExsumReport.setTitleTh(!Util.isNull(view.getTitleTh()) ? Util.checkNullString(view.getTitleTh().getTitleTh()) : SPACE);
                borrowerExsumReport.setFirstNameTh(Util.checkNullString(view.getFirstNameTh()));
                borrowerExsumReport.setLastNameTh(Util.checkNullString(view.getLastNameTh()));
                borrowerExsumReport.setCitizenId(Util.checkNullString(view.getCitizenId()));
                borrowerExsumReport.setRegistrationId(Util.checkNullString(view.getRegistrationId()));
                borrowerExsumReport.setTmbCustomerId(Util.checkNullString(view.getTmbCustomerId()));
                borrowerExsumReport.setRelation(!Util.isNull(view.getRelation()) ? Util.checkNullString(view.getRelation().getDescription()) : SPACE);
                if(view.getCollateralOwner() == 2){
                    borrowerExsumReport.setCollateralOwner("Y");
                } else {
                    borrowerExsumReport.setCollateralOwner("N");
                }

                borrowerExsumReport.setIndLv(Util.checkNullString(view.getIndLv()));
                borrowerExsumReport.setJurLv(Util.checkNullString(view.getJurLv()));
                borrowerExsumReport.setPercentShare(Util.convertNullToZERO(view.getPercentShare()));
                borrowerExsumReport.setAge(view.getAge());
                if (!Util.isNull(view.getKycLevel())){
                    borrowerExsumReport.setKycLevel(view.getKycLevel().getKycLevel());
                } else {
                    borrowerExsumReport.setKycLevel(0);
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

                List<CustomerCSIView> customerCSIList = view.getCustomerCSIList();

                if (Util.isSafetyList(customerCSIList)){
                    log.debug("customerCSIList.size() {}",customerCSIList.size());
                    for(CustomerCSIView csiView : customerCSIList){
                        borrowerExsumReport.setCustomerCSIList(!Util.isNull(csiView.getWarningCode()) ?
                                Util.checkNullString(csiView.getWarningCode().getCode()) : SPACE);
                    }
                } else {
                    borrowerExsumReport.setCustomerCSIList("-");
                }
                reports.add(borrowerExsumReport);
            }
        } else {
            BorrowerExsumReport borrowerExsumReport = new BorrowerExsumReport();
            reports.add(borrowerExsumReport);
            log.debug("customerInfoViewList in Method fillBorrowerRelatedProfile is Null. {}");
        }
        return reports;
    }

    public List<TradeFinanceExsumReport> fillTradeFinance(){
        List<TradeFinanceExsumReport> financeExsumReports = new ArrayList<TradeFinanceExsumReport>();
        List<ProposeLineView>  newCreditFacilityViewArrayList = new ArrayList<ProposeLineView>();
        ProposeLineView creditFacilityViews = exSummaryView.getTradeFinance();
        log.info("creditFacilityViews: {}",creditFacilityViews);

        if (!Util.isNull(creditFacilityViews)){
            newCreditFacilityViewArrayList.add(creditFacilityViews);
            log.debug("newCreditFacilityViewArrayList.size() {}",newCreditFacilityViewArrayList.size());

            if (Util.isSafetyList(newCreditFacilityViewArrayList)) {
                for (ProposeLineView facilityView : newCreditFacilityViewArrayList){
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
                TradeFinanceExsumReport tradeFinanceExsumReport = new TradeFinanceExsumReport();
                financeExsumReports.add(tradeFinanceExsumReport);
                log.debug("--newCreditFacilityViewArrayList is Null. {}");
            }
        } else {
            log.debug("newCreditFacilityViewArrayList in Method fillTradeFinance is Null. {}");
        }
        return financeExsumReports;
    }

    public List<NCBRecordExsumReport> fillNCBRecord(){
        List<NCBRecordExsumReport> recordExsumReports = new ArrayList<NCBRecordExsumReport>();
        List<NCBInfoView> ncbInfoViewList = exSummaryView.getNcbInfoListView();

        if (Util.isSafetyList(ncbInfoViewList)){
            log.debug("ncbInfoViewList.size() {}",ncbInfoViewList.size());
            for (NCBInfoView ncbInfoView : ncbInfoViewList){
                NCBRecordExsumReport ncbRecordExsumReport = new NCBRecordExsumReport();
                ncbRecordExsumReport.setNcbCusName(Util.checkNullString(ncbInfoView.getNcbCusName()));
                ncbRecordExsumReport.setCheckIn6Month(ncbInfoView.getCheckIn6Month());
                ncbRecordExsumReport.setCurrentPaymentType(Util.checkNullString(ncbInfoView.getCurrentPaymentType()));
                ncbRecordExsumReport.setHistoryPaymentType(Util.checkNullString(ncbInfoView.getHistoryPaymentType()));
                ncbRecordExsumReport.setNplFlagText(Util.checkNullString(ncbInfoView.getNplFlagText()));
                ncbRecordExsumReport.setTdrFlagText(Util.checkNullString(ncbInfoView.getTdrFlagText()));
                if (!Util.isNull(ncbInfoView.getTdrCondition())){
                    ncbRecordExsumReport.setDescription(Util.checkNullString(ncbInfoView.getTdrCondition().getDescription()));
                } else {
                    ncbRecordExsumReport.setDescription(SPACE);
                }
                ncbRecordExsumReport.setPaymentClass(Util.checkNullString(ncbInfoView.getPaymentClass()));

                recordExsumReports.add(ncbRecordExsumReport);
            }
        } else {
            NCBRecordExsumReport ncbRecordExsumReport = new NCBRecordExsumReport();
            recordExsumReports.add(ncbRecordExsumReport);
            log.debug("ncbInfoViewList in Method fillNCBRecord is Null. {}");
        }
        return recordExsumReports;
    }

    public BorrowerExsumReport fillBorrower(){
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
            characteristicExSumReport.setStartBusinessDate(DateTimeUtil.getCurrentDateTH(exSumCharacteristicView.getStartBusinessDate()));
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
            characteristicExSumReport.setExpiryDate(DateTimeUtil.getCurrentDateTH(exSummaryView.getExpiryDate()));
        } else {
            log.debug("exSummaryView in Method fillBorrowerCharacteristic is Null. {}",exSummaryView);
        }
        return characteristicExSumReport;
    }

    public AccountMovementExSumReport fillAccountMovement(){
        AccountMovementExSumReport movementExSumReport = new AccountMovementExSumReport();
        List<ExSumAccountMovementView> movementViewList = exSummaryView.getExSumAccMovementViewList();

        if(Util.isSafetyList(movementViewList)){
            log.debug("movementViewList.size() {}",movementViewList.size());
            for (ExSumAccountMovementView movementView : movementViewList){
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
        CreditRiskInfoExSumReport riskInfoExSumReport = new CreditRiskInfoExSumReport();
        ExSumCreditRiskInfoView exSumCreditRiskInfoView = exSummaryView.getExSumCreditRiskInfoView();

        if (!Util.isNull(exSumCreditRiskInfoView)){
            riskInfoExSumReport.setRiskCusType(Util.checkNullString(exSumCreditRiskInfoView.getRiskCusType()));
            riskInfoExSumReport.setBotClass(Util.checkNullString(exSumCreditRiskInfoView.getBotClass()));
            riskInfoExSumReport.setReason(Util.checkNullString(exSumCreditRiskInfoView.getReason()));
            riskInfoExSumReport.setLastReviewDate(DateTimeUtil.getCurrentDateTH(exSumCreditRiskInfoView.getLastReviewDate()));
            riskInfoExSumReport.setNextReviewDate(DateTimeUtil.getCurrentDateTH(exSumCreditRiskInfoView.getNextReviewDate()));
            riskInfoExSumReport.setExtendedReviewDate(DateTimeUtil.getCurrentDateTH(exSumCreditRiskInfoView.getExtendedReviewDate()));
            riskInfoExSumReport.setIndirectCountryName(Util.checkNullString(exSumCreditRiskInfoView.getIndirectCountryName()));
            riskInfoExSumReport.setPercentExport(Util.convertNullToZERO(exSumCreditRiskInfoView.getPercentExport()));
        } else {
            log.debug("exSumCreditRiskInfoView in Method fillCreditRisk is Null. {}",exSumCreditRiskInfoView);
        }
        return riskInfoExSumReport;
    }

    public List<DecisionExSumReport> fillDecision(){
        List<DecisionExSumReport> exSumReportList = new ArrayList<DecisionExSumReport>();
        List<ExSumDecisionView> exSumDecisionView = exSummaryView.getExSumDecisionListView();
        int id = 1;
        if(Util.isSafetyList(exSumDecisionView)){
            log.debug("exSumDecisionView.size() {}",exSumDecisionView.size());
            for (ExSumDecisionView decisionView : exSumDecisionView){
                DecisionExSumReport decisionExSumReport = new DecisionExSumReport();
                decisionExSumReport.setId(id++);

                if (!Util.isNull(decisionView.getFlag())){
                    if ("Y".equalsIgnoreCase(decisionView.getFlag().code())){
                        decisionExSumReport.setFlag("YELLOW");
                    } else  if ("R".equalsIgnoreCase(decisionView.getFlag().code())){
                        decisionExSumReport.setFlag("RED");
                    } else if ("G".equalsIgnoreCase(decisionView.getFlag().code())){
                        decisionExSumReport.setFlag("GREEN");
                    }
                }

                decisionExSumReport.setGroup(Util.checkNullString(decisionView.getGroup()));
                decisionExSumReport.setRuleName(Util.checkNullString(decisionView.getRuleName()));
                decisionExSumReport.setCusName(Util.checkNullString(decisionView.getCusName()));
                decisionExSumReport.setDeviationReason(Util.checkNullString(decisionView.getDeviationReason()));
                exSumReportList.add(decisionExSumReport);
            }
        } else {
            DecisionExSumReport decisionExSumReport = new DecisionExSumReport();
            exSumReportList.add(decisionExSumReport);
            log.debug("ExSumDecisionView in Method fillDecision is Null. {}");
        }
        return exSumReportList;
    }

    public BizSupportExSumReport fillBizSupport(){
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
        UWDecisionExSumReport uwDecisionExSumReport = new UWDecisionExSumReport();
        List<ExSumReasonView> exSumReasonViews = exSummaryView.getDeviateCode();

        if (!Util.isNull(exSummaryView)){
            uwDecisionExSumReport.setUwCode("6500000000");
            uwDecisionExSumReport.setName(!Util.isNull(exSummaryView.getApproveAuthority()) ? Util.checkNullString(exSummaryView.getApproveAuthority().getName()) : SPACE);
            uwDecisionExSumReport.setDecision(exSummaryView.getDecision());
            uwDecisionExSumReport.setUwComment(Util.checkNullString(exSummaryView.getUwComment()));
        } else {
            log.debug("exSummaryView in Method fillUWDecision is Null. {}",exSummaryView);
        }

        if(Util.isSafetyList(exSumReasonViews)){
            log.debug("exSumReasonViews.size() {}",exSumReasonViews.size());
            for (ExSumReasonView sumReasonView : exSumReasonViews) {
                uwDecisionExSumReport.setCode(Util.checkNullString(sumReasonView.getCode()));
                uwDecisionExSumReport.setDescription(Util.checkNullString(sumReasonView.getDescription()));
            }
        } else {
            log.debug("exSumReasonViews in Method fillUWDecision is Null. {}");
        }
        return uwDecisionExSumReport;
    }

    public HeaderAndFooterReport fillHeader(){
        HeaderAndFooterReport report = new HeaderAndFooterReport();

        HttpSession session = FacesUtil.getSession(false);
        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");
        workCase = workCaseDAO.findById(workCaseId);
        //Detail 1
        if (!Util.isNull(appHeaderView)){
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

            if (Util.isSafetyList(appHeaderView.getBorrowerHeaderViewList())){
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
        String userName =  decisionControl.getCurrentUserID();
        StringBuilder genFooter = new StringBuilder();
        genFooter = genFooter.append(userName).append(SPACE).append(date);
        report.setGenFooter(genFooter.toString());

        return report;
    }

    public List<BorrowerCreditDecisionReport> fillCreditBorrower(String pathsub){
        log.debug("on fillCreditBorrower. {}");
        List<ExistingCreditDetailView> existingCreditDetailViews = decisionView.getExtBorrowerComCreditList();
        List<BorrowerCreditDecisionReport> borrowerCreditDecisionReportList = new ArrayList<BorrowerCreditDecisionReport>();

        int count = 1;
        if (Util.isSafetyList(existingCreditDetailViews)){
            log.debug("existingCreditDetailViews.size() {}",existingCreditDetailViews.size());
            for (ExistingCreditDetailView detailView : existingCreditDetailViews){
                BorrowerCreditDecisionReport decisionReport = new BorrowerCreditDecisionReport();
                decisionReport.setCount(count++);
                decisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ");

                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append(SPACE);
                }
                decisionReport.setAccount(account.toString());

                if (!Util.isNull(detailView.getExistProductProgramView())){
                    decisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                } else {
                    decisionReport.setProductProgramName(SPACE);
                }

                if (!Util.isNull(detailView.getExistCreditTypeView())) {
                    decisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    decisionReport.setCreditTypeName(SPACE);
                }

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                decisionReport.setCode(code.toString());

                decisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                decisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                decisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                decisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));

                decisionReport.setExistingCreditTierDetailViewList(Util.safetyList(detailView.getExistingCreditTierDetailViewList()));
                decisionReport.setExistingSplitLineDetailViewList(Util.safetyList(detailView.getExistingSplitLineDetailViewList()));

                borrowerCreditDecisionReportList.add(decisionReport);
            }
        } else {
            BorrowerCreditDecisionReport decisionReport = new BorrowerCreditDecisionReport();
            decisionReport.setPath(pathsub);
            borrowerCreditDecisionReportList.add(decisionReport);
            log.debug("existingCreditDetailViews is Null by fillCreditBorrower. {}");
        }
        return borrowerCreditDecisionReportList;
    }

    public List<ConditionDecisionReport> fillCondition(){
        log.debug("on fillCondition. {}");
        List<ConditionDecisionReport> conditionDecisionReportList = new ArrayList<ConditionDecisionReport>();
        List<ExistingConditionDetailView> existingConditionDetailViews = decisionView.getExtConditionComCreditList();
        int count =1;

        if(Util.isSafetyList(existingConditionDetailViews)){
            log.debug("existingConditionDetailViews.size() {}",existingConditionDetailViews.size());
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
            log.debug("existingConditionDetailViews is Null by fillCondition. {}");
        }
        return conditionDecisionReportList;
    }

    public List<BorrowerRetailDecisionReport> fillBorrowerRetail(String pathsub){
        log.debug("on fillBorrowerRetail. {}");
        List<BorrowerRetailDecisionReport> retailDecisionReportList = new ArrayList<BorrowerRetailDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtBorrowerRetailCreditList();

        int count = 1;
        if(Util.isSafetyList(existingConditionDetailViews)){
            log.debug("existingConditionDetailViews.size() {}",existingConditionDetailViews.size());
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                BorrowerRetailDecisionReport borrowerRetailDecisionReport = new BorrowerRetailDecisionReport();
                borrowerRetailDecisionReport.setCount(count++);
                borrowerRetailDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ");

                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append(SPACE);
                }
                borrowerRetailDecisionReport.setAccount(account.toString());
                log.debug("accountLable by borrowerRetail. {}",account.toString());

                if(!Util.isNull(detailView.getExistProductProgramView())){
                    borrowerRetailDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                } else {
                    borrowerRetailDecisionReport.setProductProgramName(SPACE);
                }

                if (!Util.isNull(detailView.getExistCreditTypeView())){
                    borrowerRetailDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    borrowerRetailDecisionReport.setCreditTypeName(SPACE);
                }

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));
                borrowerRetailDecisionReport.setCode(code.toString());
                borrowerRetailDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerRetailDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                borrowerRetailDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerRetailDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));

                if (Util.isSafetyList(detailView.getExistingCreditTierDetailViewList())){
                    borrowerRetailDecisionReport.setExistingCreditTierDetailViewList(detailView.getExistingCreditTierDetailViewList());
                } else {
                    borrowerRetailDecisionReport.setExistingCreditTierDetailViewList(new ArrayList<ExistingCreditTierDetailView>());
                }
                retailDecisionReportList.add(borrowerRetailDecisionReport);
            }
        } else {
            BorrowerRetailDecisionReport borrowerRetailDecisionReport = new BorrowerRetailDecisionReport();
            borrowerRetailDecisionReport.setPath(pathsub);
            retailDecisionReportList.add(borrowerRetailDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillBorrowerRetail. {}");
        }
        return retailDecisionReportList;
    }

    public List<BorrowerAppInRLOSDecisionReport> fillAppInRLOS(String pathsub){
        log.debug("on fillAppInRLOS. {}");
        List<BorrowerAppInRLOSDecisionReport> borrowerAppInRLOSDecisionReportList = new ArrayList<BorrowerAppInRLOSDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtBorrowerAppInRLOSList();

        int count = 1;
        if(Util.isSafetyList(existingConditionDetailViews)){
            log.debug("existingConditionDetailViews.size() {}",existingConditionDetailViews.size());
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                BorrowerAppInRLOSDecisionReport borrowerAppInRLOSDecisionReport = new BorrowerAppInRLOSDecisionReport();
                borrowerAppInRLOSDecisionReport.setCount(count++);
                borrowerAppInRLOSDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ");
                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append(SPACE);
                }
                borrowerAppInRLOSDecisionReport.setAccount(account.toString());

                if (!Util.isNull(detailView.getExistProductProgramView())){
                    borrowerAppInRLOSDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                } else {
                    borrowerAppInRLOSDecisionReport.setProductProgramName(SPACE);
                }

                if (!Util.isNull(detailView.getExistCreditTypeView())){
                    borrowerAppInRLOSDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    borrowerAppInRLOSDecisionReport.setCreditTypeName(SPACE);
                }

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                borrowerAppInRLOSDecisionReport.setCode(code.toString());
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
            log.debug("existingConditionDetailViews is Null by fillAppInRLOS. {}");
        }
        return borrowerAppInRLOSDecisionReportList;
    }
            //TODO
    public List<RelatedCommercialDecisionReport> fillRelatedCommercial(String pathsub){
        List<RelatedCommercialDecisionReport> relatedCommercialDecisionReportList = new ArrayList<RelatedCommercialDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedComCreditList();

        int count = 1;
        if(Util.isSafetyList(existingConditionDetailViews)){
            log.debug("existingConditionDetailViews.size() {}",existingConditionDetailViews.size());
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
                relatedCommercialDecisionReport.setCount(count++);
                relatedCommercialDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ");

                if(!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append(SPACE);
                }
                relatedCommercialDecisionReport.setAccount(account.toString());

                if (!Util.isNull(detailView.getExistProductProgramView())){
                    relatedCommercialDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                } else {
                    relatedCommercialDecisionReport.setProductProgramName(SPACE);
                }

                if (!Util.isNull(detailView.getExistCreditTypeView())){
                    relatedCommercialDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    relatedCommercialDecisionReport.setCreditTypeName(SPACE);
                }

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                relatedCommercialDecisionReport.setCode(code.toString());
                relatedCommercialDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedCommercialDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedCommercialDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedCommercialDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                if (Util.isSafetyList(detailView.getExistingCreditTierDetailViewList())){
                    relatedCommercialDecisionReport.setExistingCreditTierDetailViewList(detailView.getExistingCreditTierDetailViewList());
                } else {
                    relatedCommercialDecisionReport.setExistingCreditTierDetailViewList(new ArrayList<ExistingCreditTierDetailView>());
                }
                if (Util.isSafetyList(detailView.getExistingSplitLineDetailViewList())){
                    relatedCommercialDecisionReport.setExistingSplitLineDetailViewList(Util.safetyList(detailView.getExistingSplitLineDetailViewList()));
                } else {
                    relatedCommercialDecisionReport.setExistingSplitLineDetailViewList(new ArrayList<ExistingSplitLineDetailView>());
                }
                relatedCommercialDecisionReportList.add(relatedCommercialDecisionReport);
            }
        } else {
            RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
            relatedCommercialDecisionReport.setPath(pathsub);
            relatedCommercialDecisionReportList.add(relatedCommercialDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedCommercial. {}");
        }

        return relatedCommercialDecisionReportList;
    }

    public List<RelatedRetailDecisionReport> fillRelatedRetail(String pathsub){
        List<RelatedRetailDecisionReport> relatedRetailDecisionReportList = new ArrayList<RelatedRetailDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedRetailCreditList();

        int count = 1;
        if(Util.isSafetyList(existingConditionDetailViews)){
            log.debug("existingConditionDetailViews.size() {}",existingConditionDetailViews.size());
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                RelatedRetailDecisionReport relatedRetailDecisionReport = new RelatedRetailDecisionReport();
                relatedRetailDecisionReport.setCount(count++);
                relatedRetailDecisionReport.setPath(pathsub);

                StringBuilder account = new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ");

                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append(SPACE);
                }
                relatedRetailDecisionReport.setAccount(account.toString());

                if (!Util.isNull(detailView.getExistProductProgramView())){
                    relatedRetailDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                } else {
                    relatedRetailDecisionReport.setProductProgramName(SPACE);
                }

                if (!Util.isNull(detailView.getExistCreditTypeView())){
                    relatedRetailDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    relatedRetailDecisionReport.setCreditTypeName(SPACE);
                }

                StringBuilder code = new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                relatedRetailDecisionReport.setCode(code.toString());

                relatedRetailDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedRetailDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedRetailDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedRetailDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                if (Util.isSafetyList(detailView.getExistingCreditTierDetailViewList())){
                    relatedRetailDecisionReport.setExistingCreditTierDetailViewList(detailView.getExistingCreditTierDetailViewList());
                } else {
                    relatedRetailDecisionReport.setExistingCreditTierDetailViewList(new ArrayList<ExistingCreditTierDetailView>());
                }
                relatedRetailDecisionReportList.add(relatedRetailDecisionReport);
            }
        } else {
            RelatedRetailDecisionReport relatedRetailDecisionReport = new RelatedRetailDecisionReport();
            relatedRetailDecisionReport.setPath(pathsub);
            relatedRetailDecisionReportList.add(relatedRetailDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedRetail. {}");
        }

        return relatedRetailDecisionReportList;
    }

    public List<RelatedAppInRLOSDecisionReport> fillRelatedAppInRLOS(String pathsub){
        List<RelatedAppInRLOSDecisionReport> relatedAppInRLOSDecisionReportArrayList = new ArrayList<RelatedAppInRLOSDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedAppInRLOSList();

        int count = 1;
        if(Util.isSafetyList(existingConditionDetailViews)){
            log.debug("existingConditionDetailViews.size() {}",existingConditionDetailViews.size());
            for (ExistingCreditDetailView detailView : decisionView.getExtRelatedAppInRLOSList()){
                RelatedAppInRLOSDecisionReport relatedAppInRLOSDecisionReport = new RelatedAppInRLOSDecisionReport();
                relatedAppInRLOSDecisionReport.setCount(count++);
                relatedAppInRLOSDecisionReport.setPath(pathsub);

                StringBuilder account =new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append("\n");
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber()));
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountNumber())).append("\n");
                account = account.append("Acc Status: ");

                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append(SPACE);
                }

                relatedAppInRLOSDecisionReport.setAccount(account.toString());
                relatedAppInRLOSDecisionReport.setProductProgramName(Util.checkNullString(detailView.getExistProductProgramView().getName()));
                relatedAppInRLOSDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));

                StringBuilder code =new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append("\n");
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode()));

                relatedAppInRLOSDecisionReport.setCode(code.toString());
                relatedAppInRLOSDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedAppInRLOSDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedAppInRLOSDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedAppInRLOSDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                if (Util.isSafetyList(detailView.getExistingCreditTierDetailViewList())){
                    relatedAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(detailView.getExistingCreditTierDetailViewList());
                } else {
                    relatedAppInRLOSDecisionReport.setExistingCreditTierDetailViewList(new ArrayList<ExistingCreditTierDetailView>());
                }
                relatedAppInRLOSDecisionReportArrayList.add(relatedAppInRLOSDecisionReport);
            }
        } else {
            RelatedAppInRLOSDecisionReport relatedAppInRLOSDecisionReport = new RelatedAppInRLOSDecisionReport();
            relatedAppInRLOSDecisionReport.setPath(pathsub);
            relatedAppInRLOSDecisionReportArrayList.add(relatedAppInRLOSDecisionReport);
            log.debug("existingConditionDetailViews is Null by fillRelatedAppInRLOS. {}");
        }

        return relatedAppInRLOSDecisionReportArrayList;
    }

    public List<ExistingCollateralBorrowerDecisionReport> fillExistingCollateralBorrower(String path) throws UnsupportedEncodingException {
        List<ExistingCollateralBorrowerDecisionReport> collateralBorrowerDecisionReportList = new ArrayList<ExistingCollateralBorrowerDecisionReport>();
        List<ExistingCollateralDetailView> conditionDetailViews = decisionView.getExtBorrowerCollateralList();

        int count = 1;
        if (Util.isSafetyList(conditionDetailViews)){
            log.debug("conditionDetailViews.size() {}",conditionDetailViews.size());
            for (ExistingCollateralDetailView detailView : conditionDetailViews){
                ExistingCollateralBorrowerDecisionReport collateralBorrowerDecisionReport = new ExistingCollateralBorrowerDecisionReport();
                collateralBorrowerDecisionReport.setCount(count++);
                collateralBorrowerDecisionReport.setPath(path);

                StringBuilder collateralType = new StringBuilder();
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.potential")).append((!Util.isNull(detailView.getPotentialCollateral()) ? (Util.checkNullString(detailView.getPotentialCollateral().getDescription())) : SPACE)).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralType")).append((!Util.isNull(detailView.getCollateralType()) ? (Util.checkNullString(detailView.getCollateralType().getDescription())) : SPACE)).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.owner")).append((Util.checkNullString(detailView.getOwner()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.relationship")).append((!Util.isNull(detailView.getRelation()) ? (Util.checkNullString(detailView.getRelation().getDescription())) : SPACE)).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.appraisalDate")).append(DateTimeUtil.convertToStringDDMMYYYY(DateTimeUtil.getCurrentDateTH((detailView.getAppraisalDate()))) == null ? SPACE : DateTimeUtil.convertToStringDDMMYYYY(DateTimeUtil.getCurrentDateTH(detailView.getAppraisalDate()))).append("\n");//TODO
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralNumber")).append((Util.checkNullString(detailView.getCollateralNumber()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralLocation")).append((Util.checkNullString(detailView.getCollateralLocation()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.remark")).append((Util.checkNullString(detailView.getRemark())));


                collateralBorrowerDecisionReport.setCollateralType(collateralType.toString());
                collateralBorrowerDecisionReport.setCusName(Util.checkNullString(detailView.getCusName()));
                if (Util.isSafetyList(detailView.getExistingCreditTypeDetailViewList())){
                    collateralBorrowerDecisionReport.setExistingCreditTypeDetailViews(detailView.getExistingCreditTypeDetailViewList());
                } else {
                    collateralBorrowerDecisionReport.setExistingCreditTypeDetailViews(new ArrayList<ExistingCreditTypeDetailView>());
                }
                collateralBorrowerDecisionReport.setProductProgram(Util.checkNullString(detailView.getProductProgram()));
                collateralBorrowerDecisionReport.setCreditFacility(Util.checkNullString(detailView.getCreditFacility()));
                collateralBorrowerDecisionReport.setMortgageType(!Util.isNull(detailView.getMortgageType()) ?
                        (Util.checkNullString(detailView.getMortgageType().getMortgage())) : SPACE);
                collateralBorrowerDecisionReport.setAppraisalValue(Util.convertNullToZERO(detailView.getAppraisalValue()));
                collateralBorrowerDecisionReport.setMortgageValue(Util.convertNullToZERO(detailView.getMortgageValue()));
                collateralBorrowerDecisionReportList.add(collateralBorrowerDecisionReport);
            }
        } else {
            ExistingCollateralBorrowerDecisionReport collateralBorrowerDecisionReport = new ExistingCollateralBorrowerDecisionReport();
            collateralBorrowerDecisionReport.setPath(path);
            collateralBorrowerDecisionReportList.add(collateralBorrowerDecisionReport);
            log.debug("conditionDetailViews is Null by fillExistingCollateralBorrower. {}");
        }

        return collateralBorrowerDecisionReportList;
    }

    public List<ExistingCollateralRelatedDecisionReport> fillExistingCollateralRelated(String path){
        List<ExistingCollateralRelatedDecisionReport> collateralRelatedDecisionReportArrayList = new ArrayList<ExistingCollateralRelatedDecisionReport>();
        List<ExistingCollateralDetailView> conditionDetailViews = decisionView.getExtRelatedCollateralList();

        int count = 1;
        if (Util.isSafetyList(conditionDetailViews)){
            log.debug("conditionDetailViews.size() {}",conditionDetailViews.size());
            for (ExistingCollateralDetailView detailView : conditionDetailViews){
                ExistingCollateralRelatedDecisionReport collateralRelatedDecisionReport = new ExistingCollateralRelatedDecisionReport();
                collateralRelatedDecisionReport.setCount(count++);
                collateralRelatedDecisionReport.setPath(path);

                StringBuilder collateralType = new StringBuilder();
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.potential")).append((!Util.isNull(detailView.getPotentialCollateral()) ? (Util.checkNullString(detailView.getPotentialCollateral().getDescription())) : SPACE)).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralType")).append((!Util.isNull(detailView.getCollateralType()) ? (Util.checkNullString(detailView.getCollateralType().getDescription())) : SPACE)).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.owner")).append((Util.checkNullString(detailView.getOwner()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.relationship")).append((!Util.isNull(detailView.getRelation()) ? (Util.checkNullString(detailView.getRelation().getDescription())) : SPACE)).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.appraisalDate")).append(DateTimeUtil.convertToStringDDMMYYYY(DateTimeUtil.getCurrentDateTH((detailView.getAppraisalDate()))) == null ? SPACE : DateTimeUtil.convertToStringDDMMYYYY(DateTimeUtil.getCurrentDateTH(detailView.getAppraisalDate()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralNumber")).append((Util.checkNullString(detailView.getCollateralNumber()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralLocation")).append((Util.checkNullString(detailView.getCollateralLocation()))).append("\n");
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.remark")).append((Util.checkNullString(detailView.getRemark())));

                collateralRelatedDecisionReport.setCollateralType(collateralType.toString());
                collateralRelatedDecisionReport.setCusName(Util.checkNullString(detailView.getCusName()));
                if (Util.isSafetyList(detailView.getExistingCreditTypeDetailViewList())){
                    collateralRelatedDecisionReport.setExistingCreditTypeDetailViews(Util.safetyList(detailView.getExistingCreditTypeDetailViewList()));
                } else {
                    collateralRelatedDecisionReport.setExistingCreditTypeDetailViews(new ArrayList<ExistingCreditTypeDetailView>());
                }
                collateralRelatedDecisionReport.setProductProgram(Util.checkNullString(detailView.getProductProgram()));
                collateralRelatedDecisionReport.setCreditFacility(Util.checkNullString(detailView.getCreditFacility()));
                collateralRelatedDecisionReport.setMortgageType(!Util.isNull(detailView.getMortgageType()) ? (Util.checkNullString(detailView.getMortgageType().getMortgage())) : SPACE);
                collateralRelatedDecisionReport.setAppraisalValue(Util.convertNullToZERO(detailView.getAppraisalValue()));
                collateralRelatedDecisionReport.setMortgageValue(Util.convertNullToZERO(detailView.getMortgageValue()));
                collateralRelatedDecisionReportArrayList.add(collateralRelatedDecisionReport);
            }
        } else {
            ExistingCollateralRelatedDecisionReport collateralRelatedDecisionReport = new ExistingCollateralRelatedDecisionReport();
            collateralRelatedDecisionReport.setPath(path);
            collateralRelatedDecisionReportArrayList.add(collateralRelatedDecisionReport);
            log.debug("conditionDetailViews is Null by fillExistingCollateralRelated. {}");
        }

        return collateralRelatedDecisionReportArrayList;
    }

    public List<GuarantorBorrowerDecisionReport> fillGuarantorBorrower(String pathsub){
        List<GuarantorBorrowerDecisionReport> guarantorBorrowerDecisionReportList = new ArrayList<GuarantorBorrowerDecisionReport>();
        List<ExistingGuarantorDetailView> extGuarantorList = decisionView.getExtGuarantorList();
        int count = 1;
        if (Util.isSafetyList(extGuarantorList)){
            log.debug("extGuarantorList.size() {}",extGuarantorList.size());
            for (ExistingGuarantorDetailView detailView : extGuarantorList){
                GuarantorBorrowerDecisionReport guarantorBorrowerDecisionReport = new GuarantorBorrowerDecisionReport();
                guarantorBorrowerDecisionReport.setCount(count++);
                guarantorBorrowerDecisionReport.setPath(pathsub);

                StringBuilder name = new StringBuilder();
                name = name.append(!Util.isNull(detailView.getGuarantorName()) ? !Util.isNull(detailView.getGuarantorName().getTitleTh()) ?
                        Util.checkNullString(detailView.getGuarantorName().getTitleTh().getTitleTh()) : SPACE : SPACE)
                        .append(!Util.isNull(detailView.getGuarantorName()) ? Util.checkNullString(detailView.getGuarantorName().getFirstNameTh()) : SPACE)
                        .append(SPACE).append(!Util.isNull(detailView.getGuarantorName()) ? Util.checkNullString(detailView.getGuarantorName().getLastNameTh()) : SPACE);
                guarantorBorrowerDecisionReport.setGuarantorName(name.toString());
                guarantorBorrowerDecisionReport.setTcgLgNo(Util.checkNullString(detailView.getTcgLgNo()));
                if (Util.isSafetyList(detailView.getExistingCreditTypeDetailViewList())){
                    guarantorBorrowerDecisionReport.setExistingCreditTypeDetailViewList(detailView.getExistingCreditTypeDetailViewList());
                } else {
                    guarantorBorrowerDecisionReport.setExistingCreditTypeDetailViewList(new ArrayList<ExistingCreditTypeDetailView>());
                }
                guarantorBorrowerDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(detailView.getTotalLimitGuaranteeAmount()));
                guarantorBorrowerDecisionReportList.add(guarantorBorrowerDecisionReport);
            }
        } else {
            GuarantorBorrowerDecisionReport guarantorBorrowerDecisionReport = new GuarantorBorrowerDecisionReport();
            guarantorBorrowerDecisionReport.setPath(pathsub);
            guarantorBorrowerDecisionReportList.add(guarantorBorrowerDecisionReport);
            log.debug("extGuarantorList is Null by fillGuarantorBorrower. {}");
        }
        return guarantorBorrowerDecisionReportList;
    }

    public List<ProposedCreditDecisionReport> fillProposedCredit(String pathsub){
        log.debug("on fillProposedCredit. {}");
        newCreditDetailViewList = decisionView.getProposeCreditList();
        List<ProposedCreditDecisionReport> proposedCreditDecisionReportList = new ArrayList<ProposedCreditDecisionReport>();

        int count = 1;
        if (Util.isSafetyList(newCreditDetailViewList)){
            log.debug("newCreditDetailViewList.size() {}",newCreditDetailViewList.size());
            for (ProposeCreditInfoDetailView detailView : newCreditDetailViewList){
                ProposedCreditDecisionReport proposedView = new ProposedCreditDecisionReport();
                proposedView.setPath(pathsub);
                proposedView.setCount(count++);
                proposedView.setProdName(!Util.isNull(detailView.getProductProgramView()) ?
                        Util.checkNullString( detailView.getProductProgramView().getName()) : SPACE);
                proposedView.setCredittypeName(!Util.isNull(detailView.getCreditTypeView()) ?
                        Util.checkNullString(detailView.getCreditTypeView().getName()) : SPACE);
                proposedView.setProdCode(Util.checkNullString(detailView.getProductCode()));
                proposedView.setProjectCode(Util.checkNullString(detailView.getProjectCode()));
                proposedView.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                proposedView.setFrontEndFee(Util.convertNullToZERO(detailView.getFrontEndFee()));
                if (Util.isSafetyList(detailView.getProposeCreditInfoTierDetailViewList())){
                    proposedView.setNewCreditTierDetailViews(detailView.getProposeCreditInfoTierDetailViewList());
                } else {
                    proposedView.setNewCreditTierDetailViews(new ArrayList<ProposeCreditInfoTierDetailView>());
                }

                StringBuilder builder = new StringBuilder();
                if (!Util.isNull(detailView.getRequestType())){
                    if (detailView.getRequestType() == RequestTypes.NEW.value()){
                        builder = builder.append("Request Type : New    ");
                    } else if (detailView.getRequestType() == RequestTypes.CHANGE.value()){
                        builder = builder.append("Request Type : Change    ");
                    }
                }

                if (!Util.isNull(detailView.getRefinance())){
                    if (detailView.getRefinance() == RadioValue.YES.value()){
                        builder = builder.append("Refinance : Yes").append("\n");
                        proposedView.setRefinance("Yes");
                    } else if (detailView.getRefinance() == RadioValue.NO.value()){
                        builder = builder.append("Refinance : No").append("\n");
                    }
                }

                builder = builder.append("Purpose : ").append(!Util.isNull(detailView.getLoanPurposeView()) ?
                        Util.checkNullString(detailView.getLoanPurposeView().getDescription()) : SPACE).append("\n");

                builder = builder.append("Purpose Detail : ").append(Util.checkNullString(detailView.getProposeDetail())).append("\n");

                builder = builder.append("Disbursement : ").append(!Util.isNull(detailView.getDisbursementTypeView()) ?
                        Util.checkNullString(detailView.getDisbursementTypeView().getDisbursement()) : SPACE)
                        .append(SPACE);

                builder = builder.append("Hold Amount : ").append(Util.convertNullToZERO(detailView.getHoldLimitAmount()));

                if (!Util.isNull(builder)){
                    proposedView.setProposedDetail(builder.toString());
                }
                proposedCreditDecisionReportList.add(proposedView);
            }
        } else {
            ProposedCreditDecisionReport proposedView = new ProposedCreditDecisionReport();
            proposedView.setPath(pathsub);
            proposedCreditDecisionReportList.add(proposedView);
            log.debug("newCreditDetailViewList is Null by fillProposedCredit. {}");
        }
        return proposedCreditDecisionReportList;
    }

    //ExSum
    public List<ProposedCreditDecisionReport> fillExSumApprovedCredit(String pathsub){
        log.debug("on fillExSumApprovedCredit. {}");
        newCreditDetailViewList = decisionView.getApproveCreditList();
        List<ProposedCreditDecisionReport> proposedCreditDecisionReportList = new ArrayList<ProposedCreditDecisionReport>();

        int count = 1;
        if (Util.isSafetyList(newCreditDetailViewList)){
            log.debug("newCreditDetailViewList.size() {}",newCreditDetailViewList.size());
            for (ProposeCreditInfoDetailView detailView : newCreditDetailViewList){
                ProposedCreditDecisionReport approvedView = new ProposedCreditDecisionReport();
                approvedView.setPath(pathsub);

                approvedView.setCount(count++);

                approvedView.setProdName(!Util.isNull(detailView.getProductProgramView()) ?
                        Util.checkNullString(detailView.getProductProgramView().getName()) : SPACE);

                if (!Util.isNull(detailView.getUwDecision())){
                    if (detailView.getUwDecision() == DecisionType.APPROVED){
                        approvedView.setUwDecision("APPROVED");
                    } else  if (detailView.getUwDecision() == DecisionType.REJECTED){
                        approvedView.setUwDecision("REJECTED");
                    } else {
                        approvedView.setUwDecision(SPACE);
                    }
                } else {
                    approvedView.setUwDecision(SPACE);
                }

                approvedView.setCredittypeName(!Util.isNull(detailView.getCreditTypeView()) ?
                        Util.checkNullString(detailView.getCreditTypeView().getName()) : SPACE);
                approvedView.setProdCode(Util.checkNullString(detailView.getProductCode()));
                approvedView.setProjectCode(Util.checkNullString(detailView.getProjectCode()));
                approvedView.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                approvedView.setFrontEndFee(Util.convertNullToZERO(detailView.getFrontEndFee()));
                if (Util.isSafetyList(detailView.getProposeCreditInfoTierDetailViewList())){
                    approvedView.setNewCreditTierDetailViews(detailView.getProposeCreditInfoTierDetailViewList());
                } else {
                    approvedView.setNewCreditTierDetailViews(new ArrayList<ProposeCreditInfoTierDetailView>());
                }

                StringBuilder builder = new StringBuilder();

                if (!Util.isNull(detailView.getRequestType())){
                    if (detailView.getRequestType() == RequestTypes.NEW.value()){
                        builder = builder.append("Request Type : New    ");
                    } else if (detailView.getRequestType() == RequestTypes.CHANGE.value()){
                        builder = builder.append("Request Type : Change    ");
                    }
                }

                if (!Util.isNull(detailView.getRefinance())){
                    if (detailView.getRefinance() == RadioValue.YES.value()){
                        builder = builder.append("Refinance : Yes").append("\n");
                        approvedView.setRefinance("Yes");
                    } else if (detailView.getRefinance() == RadioValue.NO.value()){
                        builder = builder.append("Refinance : No").append("\n");
                    }
                }

                builder = builder.append("Purpose : ").append(!Util.isNull(detailView.getLoanPurposeView()) ?
                        Util.checkNullString(detailView.getLoanPurposeView().getDescription()) : SPACE).append("\n");

                builder = builder.append("Purpose Detail : ").append(Util.checkNullString(detailView.getProposeDetail())).append("\n");

                builder = builder.append("Disbursement : ").append(!Util.isNull(detailView.getDisbursementTypeView()) ?
                        Util.checkNullString(detailView.getDisbursementTypeView().getDisbursement()) : SPACE)
                        .append(SPACE);
                builder = builder.append("Hold Amount : ").append(Util.convertNullToZERO(detailView.getHoldLimitAmount()));

                if (!Util.isNull(builder)){
                    approvedView.setProposedDetail(builder.toString());
                }
                proposedCreditDecisionReportList.add(approvedView);
            }
        } else {
            ProposedCreditDecisionReport approvedView = new ProposedCreditDecisionReport();
            approvedView.setPath(pathsub);
            proposedCreditDecisionReportList.add(approvedView);
            log.debug("newCreditDetailViewList is Null by fillExSumApprovedCredit. {}");
        }
        return proposedCreditDecisionReportList;
    }

    //Opsheet
    public List<ProposedCreditDecisionReport> fillApprovedCredit(String pathsub){
        log.debug("on fillApprovedCredit. {}");
        newCreditDetailViewList = decisionView.getApproveCreditList();
        List<ProposedCreditDecisionReport> proposedCreditDecisionReportList = new ArrayList<ProposedCreditDecisionReport>();

        int count = 1;
        if (Util.isSafetyList(newCreditDetailViewList)){
            log.debug("newCreditDetailViewList.size() {}",newCreditDetailViewList.size());
            for (ProposeCreditInfoDetailView detailView : newCreditDetailViewList){
                ProposedCreditDecisionReport approvedView = new ProposedCreditDecisionReport();
                approvedView.setPath(pathsub);

                if (!Util.isNull(detailView.getUwDecision())){
                    if (detailView.getUwDecision() == DecisionType.APPROVED){
                        approvedView.setCount(count++);

                        approvedView.setProdName(!Util.isNull(detailView.getProductProgramView()) ?
                                Util.checkNullString(detailView.getProductProgramView().getName()) : SPACE);

                        if (detailView.getUwDecision() == DecisionType.APPROVED){
                            approvedView.setUwDecision("APPROVED");
                        }

                        approvedView.setCredittypeName(!Util.isNull(detailView.getCreditTypeView()) ?
                                Util.checkNullString(detailView.getCreditTypeView().getName()) : SPACE);
                        approvedView.setProdCode(Util.checkNullString(detailView.getProductCode()));
                        approvedView.setProjectCode(Util.checkNullString(detailView.getProjectCode()));
                        approvedView.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                        approvedView.setFrontEndFee(Util.convertNullToZERO(detailView.getFrontEndFee()));

                        if (Util.isSafetyList(detailView.getProposeCreditInfoTierDetailViewList())){
                            approvedView.setNewCreditTierDetailViews(detailView.getProposeCreditInfoTierDetailViewList());
                        } else {
                            approvedView.setNewCreditTierDetailViews(new ArrayList<ProposeCreditInfoTierDetailView>());
                        }

                        StringBuilder builder = new StringBuilder();

                        if (!Util.isNull(detailView.getRequestType())){
                            if (detailView.getRequestType() == RequestTypes.NEW.value()){
                                builder = builder.append("Request Type : New    ");
                            } else if (detailView.getRequestType() == RequestTypes.CHANGE.value()){
                                builder = builder.append("Request Type : Change    ");
                            }
                        }

                        if (!Util.isNull(detailView.getRefinance())){
                            if (detailView.getRefinance() == RadioValue.YES.value()){
                                builder = builder.append("Refinance : Yes").append("\n");
                                approvedView.setRefinance("Yes");
                            } else if (detailView.getRefinance() == RadioValue.NO.value()){
                                builder = builder.append("Refinance : No").append("\n");
                            }
                        }

                        builder = builder.append("Purpose : ").append(!Util.isNull(detailView.getLoanPurposeView()) ?
                                Util.checkNullString(detailView.getLoanPurposeView().getDescription()) : SPACE).append("\n");

                        builder = builder.append("Purpose Detail : ").append(Util.checkNullString(detailView.getProposeDetail())).append("\n");

                        builder = builder.append("Disbursement : ").append(!Util.isNull(detailView.getDisbursementTypeView()) ?
                                Util.checkNullString(detailView.getDisbursementTypeView().getDisbursement()) : SPACE)
                                .append(SPACE);
                        builder = builder.append("Hold Amount : ").append(Util.convertNullToZERO(detailView.getHoldLimitAmount()));

                        if (!Util.isNull(builder)){
                            approvedView.setProposedDetail(builder.toString());
                        }
                        proposedCreditDecisionReportList.add(approvedView);
                    } else {
                        proposedCreditDecisionReportList.add(approvedView);
                    }
                } else {
                    proposedCreditDecisionReportList.add(approvedView);
                }
            }
        } else {
            ProposedCreditDecisionReport approvedView = new ProposedCreditDecisionReport();
            approvedView.setPath(pathsub);
            proposedCreditDecisionReportList.add(approvedView);
            log.debug("newCreditDetailViewList is Null by fillApprovedCredit. {}");
        }
        return proposedCreditDecisionReportList;
    }

    public List<ProposeFeeInformationDecisionReport> fillProposeFeeInformation(){
        List<ProposeFeeInformationDecisionReport> proposeFeeInformationDecisionReportList = new ArrayList<ProposeFeeInformationDecisionReport>();
        List<ProposeFeeDetailView> feeDetailViewList = decisionView.getApproveFeeDetailViewList();

        int count = 1;
        if (Util.isSafetyList(feeDetailViewList)){
            log.debug("feeDetailViewList.size() {}",feeDetailViewList.size());
            for (ProposeFeeDetailView view : feeDetailViewList){
                ProposeFeeInformationDecisionReport proposeFeeInformationDecisionReport = new ProposeFeeInformationDecisionReport();
                proposeFeeInformationDecisionReport.setCount(count++);
                proposeFeeInformationDecisionReport.setProductProgram(Util.checkNullString(view.getProductProgram()));

                StringBuilder standard = new StringBuilder();

                if (!Util.isNull(view.getStandardFrontEndFee())){
                    standard = standard.append(Util.convertNullToZERO(view.getStandardFrontEndFee().getPercentFee())).append(" % ").append(" , ")
                            .append(!Util.isNull(view.getStandardFrontEndFee()) ?
                             Util.convertNullToZERO(view.getStandardFrontEndFee().getFeeYear()) : " - ");
                    proposeFeeInformationDecisionReport.setStandardFront(standard.toString());
                    } else {
                    proposeFeeInformationDecisionReport.setStandardFront("0.00%, - ");
                }

                if (!Util.isNull(view.getCommitmentFee())){
                    StringBuilder commit = new StringBuilder();
                    commit = commit.append(Util.convertNullToZERO(view.getCommitmentFee().getPercentFee())).append(" % ").append(" , ")
                            .append(!Util.isNull(view.getCommitmentFee()) ?
                            Util.convertNullToZERO(view.getCommitmentFee().getFeeYear()) : " - ");
                    proposeFeeInformationDecisionReport.setCommitmentFee(commit.toString());
                } else {
                    proposeFeeInformationDecisionReport.setCommitmentFee("0.00%, - ");
                }

                if (!Util.isNull(view.getExtensionFee())){
                    StringBuilder extension = new StringBuilder();
                    extension = extension.append(Util.convertNullToZERO(view.getExtensionFee().getPercentFee())).append(" % ").append(" , ")
                            .append(!Util.isNull(view.getExtensionFee()) ?
                            Util.convertNullToZERO(view.getExtensionFee().getFeeYear()) : " - ");
                    proposeFeeInformationDecisionReport.setExtensionFee(extension.toString());
                } else {
                    proposeFeeInformationDecisionReport.setExtensionFee("0.00%, - ");
                }

                if (!Util.isNull(view.getPrepaymentFee())){
                    StringBuilder prepayment = new StringBuilder();
                    prepayment = prepayment.append(Util.convertNullToZERO(view.getPrepaymentFee().getPercentFee())).append(" % ").append(" , ")
                            .append(!Util.isNull(view.getPrepaymentFee()) ?
                            Util.convertNullToZERO(view.getPrepaymentFee().getFeeYear()) : " - ");
                    proposeFeeInformationDecisionReport.setPrepaymentFee(prepayment.toString());
                } else {
                    proposeFeeInformationDecisionReport.setPrepaymentFee("0.00%, - ");
                }

                if (!Util.isNull(view.getCancellationFee())) {
                    StringBuilder cancellation = new StringBuilder();
                    cancellation = cancellation.append(Util.convertNullToZERO(view.getCancellationFee().getPercentFee())).append(" % ").append(" , ")
                            .append(Util.convertNullToZERO(view.getCancellationFee().getFeeYear())).append(" Year");
                    proposeFeeInformationDecisionReport.setCancellationFee(cancellation.toString());
                } else {
                    proposeFeeInformationDecisionReport.setCancellationFee("0.00%, - ");
                }
                proposeFeeInformationDecisionReportList.add(proposeFeeInformationDecisionReport);
            }
        } else {
            ProposeFeeInformationDecisionReport proposeFeeInformationDecisionReport = new ProposeFeeInformationDecisionReport();
            proposeFeeInformationDecisionReportList.add(proposeFeeInformationDecisionReport);
            log.debug("feeDetailViewList is Null by fillProposeFeeInformation. {}");
        }
        return proposeFeeInformationDecisionReportList;
    }

    public List<ProposedCollateralDecisionReport> fillProposedCollateral(String pathsub){
        List<ProposedCollateralDecisionReport> proposedCollateralDecisionReportList = new ArrayList<ProposedCollateralDecisionReport>();
        List<ProposeCollateralInfoView> newCollateralViews = decisionView.getProposeCollateralList();
        List<ProposeCollateralInfoHeadView> collateralHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();

        if (Util.isSafetyList(newCollateralViews)){
            log.debug("newCollateralViews.size() {}",newCollateralViews.size());
            for (ProposeCollateralInfoView view : newCollateralViews){
                ProposedCollateralDecisionReport collateralDecisionReport = new ProposedCollateralDecisionReport();
                collateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                collateralDecisionReport.setPath(pathsub);
                collateralDecisionReport.setAppraisalDate(DateTimeUtil.getCurrentDateTH(view.getAppraisalDate()));
                collateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecision()));
                collateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                collateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));
                collateralDecisionReport.setUsage(Util.checkNullString(view.getUsage()));
                collateralDecisionReport.setTypeOfUsage(Util.checkNullString(view.getTypeOfUsage()));
                collateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                collateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));
                collateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));

                if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())) {
                    log.debug("getProposeCreditDetailViewList. {}",view.getProposeCreditInfoDetailViewList().size());
                    collateralDecisionReport.setDetailViewList(view.getProposeCreditInfoDetailViewList());
                } else {
                    log.debug("getProposeCreditDetailViewList is Null. {}");
                }

                collateralHeadViewList = view.getProposeCollateralInfoHeadViewList();
                if (Util.isSafetyList(collateralHeadViewList)){
                    log.debug("collateralHeadViewList.size() {}",collateralHeadViewList.size());
                    for (ProposeCollateralInfoHeadView headView : collateralHeadViewList){
                        collateralDecisionReport.setCollateralDescription(!Util.isNull(headView.getPotentialCollateral()) ?
                                (Util.checkNullString(headView.getPotentialCollateral().getDescription())) : SPACE);
                        collateralDecisionReport.setPercentLTVDescription(!Util.isNull(headView.getTcgCollateralType()) ?
                                (Util.checkNullString(headView.getTcgCollateralType().getDescription())): SPACE);
                        collateralDecisionReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                        collateralDecisionReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                        collateralDecisionReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                        collateralDecisionReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                        collateralDecisionReport.setHeadCollTypeDescription(!Util.isNull(headView.getHeadCollType()) ?
                                Util.checkNullString(headView.getHeadCollType().getDescription()) : SPACE);
                        if (!Util.isNull(headView.getInsuranceCompany())){
                            if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                                collateralDecisionReport.setInsuranceCompany("Partner");
                            } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                                collateralDecisionReport.setInsuranceCompany("Non Partner");
                            } else {
                                collateralDecisionReport.setInsuranceCompany(SPACE);
                            }
                        }
                        if (Util.isSafetyList(headView.getProposeCollateralInfoSubViewList())){
                            int count = 1;
                            List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
                            for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : headView.getProposeCollateralInfoSubViewList()){
                                ProposeCollateralInfoSubView subView = new ProposeCollateralInfoSubView();
                                subView.setPath(pathsub);
                                subView.setNo(count++);
                                subView.setSubCollateralType(proposeCollateralInfoSubView.getSubCollateralType());
                                subView.setAddress(Util.checkNullString(proposeCollateralInfoSubView.getAddress()));
                                subView.setLandOffice(Util.checkNullString(proposeCollateralInfoSubView.getLandOffice()));
                                subView.setTitleDeed(Util.checkNullString(proposeCollateralInfoSubView.getTitleDeed()));
                                subView.setCollateralOwnerAAD(Util.checkNullString(proposeCollateralInfoSubView.getCollateralOwnerAAD()));
                                subView.setAppraisalValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getAppraisalValue()));
                                subView.setMortgageValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getMortgageValue()));
                                subView.setCollateralOwnerUWList(Util.isSafetyList(proposeCollateralInfoSubView.getCollateralOwnerUWList()) ? proposeCollateralInfoSubView.getCollateralOwnerUWList() : new ArrayList<CustomerInfoView>());
                                subView.setMortgageList(Util.isSafetyList(proposeCollateralInfoSubView.getMortgageList()) ? proposeCollateralInfoSubView.getMortgageList() : new ArrayList<MortgageTypeView>());
                                subView.setRelatedWithList(Util.isSafetyList(proposeCollateralInfoSubView.getRelatedWithList()) ? proposeCollateralInfoSubView.getRelatedWithList() : new ArrayList<ProposeCollateralInfoSubView>());
                                proposeCollateralInfoSubViewList.add(subView);
                            }
                            collateralDecisionReport.setCollateralSubViewList(proposeCollateralInfoSubViewList);
                        }
//                        else {
//                            ProposeCollateralInfoSubView proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
//                            List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
//                            proposeCollateralInfoSubView.setPath(pathsub);
//                            proposeCollateralInfoSubViewList.add(proposeCollateralInfoSubView);
//                            collateralDecisionReport.setCollateralSubViewList(proposeCollateralInfoSubViewList);
//                        }
                    }
                } else {
                    log.debug("collateralHeadViewList is Null. {}");
                }
                proposedCollateralDecisionReportList.add(collateralDecisionReport);
            }
        }
//        else {
//            log.debug("newCollateralViews is Null by fillProposedCollateral. {}");
//            ProposedCollateralDecisionReport collateralDecisionReport = new ProposedCollateralDecisionReport();
//            List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
//            ProposeCollateralInfoSubView proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
//            collateralDecisionReport.setPath(pathsub);
//            proposeCollateralInfoSubView.setPath(pathsub);
//            proposeCollateralInfoSubViewList.add(proposeCollateralInfoSubView);
//            collateralDecisionReport.setCollateralSubViewList(proposeCollateralInfoSubViewList);
//            proposedCollateralDecisionReportList.add(collateralDecisionReport);
//        }
        return proposedCollateralDecisionReportList;
    }

    //ExSum
    public List<ApprovedCollateralDecisionReport> fillExSumApprovedCollaterral(final String pathsub){
        List<ApprovedCollateralDecisionReport> approvedCollateralDecisionReportArrayList = new ArrayList<ApprovedCollateralDecisionReport>();
        List<ProposeCollateralInfoView> newCollateralViews = decisionView.getApproveCollateralList();
        List<ProposeCollateralInfoHeadView> collateralHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();

        if (Util.isSafetyList(newCollateralViews)){
            log.debug("newCollateralViews.size() {}",newCollateralViews.size());
            for (ProposeCollateralInfoView view : newCollateralViews){
                ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
                approvedCollateralDecisionReport.setPath(pathsub);
                approvedCollateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                approvedCollateralDecisionReport.setAppraisalDate(DateTimeUtil.getCurrentDateTH(view.getAppraisalDate()));
                approvedCollateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecision()));
                approvedCollateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                approvedCollateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));
                approvedCollateralDecisionReport.setUsage(Util.checkNullString(view.getUsage()));
                approvedCollateralDecisionReport.setTypeOfUsage(Util.checkNullString(view.getTypeOfUsage()));
                approvedCollateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));

                if (!Util.isNull(view.getUwDecision())){
                    if(view.getUwDecision() == DecisionType.APPROVED ){
                        approvedCollateralDecisionReport.setApproved("Yes");
                    } else {
                        approvedCollateralDecisionReport.setApproved("No");
                    }
                } else {
                    approvedCollateralDecisionReport.setApproved("No");
                }

                approvedCollateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                approvedCollateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));

                if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())) {
                    log.debug("getProposeCreditDetailViewList. {}",view.getProposeCreditInfoDetailViewList().size());
                    approvedCollateralDecisionReport.setProposeCreditDetailViewList(view.getProposeCreditInfoDetailViewList());
                } else {
                    approvedCollateralDecisionReport.setProposeCreditDetailViewList(new ArrayList<ProposeCreditInfoDetailView>());
                }

                collateralHeadViewList = view.getProposeCollateralInfoHeadViewList();
                if (Util.isSafetyList(collateralHeadViewList)){
                    log.debug("collateralHeadViewList. {}",collateralHeadViewList.size());
                    for (ProposeCollateralInfoHeadView headView : collateralHeadViewList){
                        approvedCollateralDecisionReport.setCollateralDescription(!Util.isNull(headView.getPotentialCollateral()) ?
                                Util.checkNullString(headView.getPotentialCollateral().getDescription()) : SPACE);
                        approvedCollateralDecisionReport.setPercentLTVDescription(!Util.isNull(headView.getTcgCollateralType()) ?
                                Util.checkNullString(headView.getTcgCollateralType().getDescription()) : SPACE);
                        approvedCollateralDecisionReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                        approvedCollateralDecisionReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                        approvedCollateralDecisionReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                        approvedCollateralDecisionReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                        approvedCollateralDecisionReport.setHeadCollTypeDescription(!Util.isNull(headView.getHeadCollType()) ?
                                Util.checkNullString(headView.getHeadCollType().getDescription()) : SPACE);
                        if (!Util.isNull(headView.getInsuranceCompany())){
                            if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                                approvedCollateralDecisionReport.setInsuranceCompany("Partner");
                            } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                                approvedCollateralDecisionReport.setInsuranceCompany("Non Partner");
                            } else {
                                approvedCollateralDecisionReport.setInsuranceCompany(SPACE);
                            }
                        }
                        if (Util.isSafetyList(headView.getProposeCollateralInfoSubViewList())){
                            int count = 1;
                            List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
                            for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : headView.getProposeCollateralInfoSubViewList()){
                                ProposeCollateralInfoSubView subView = new ProposeCollateralInfoSubView();
                                subView.setPath(pathsub);
                                subView.setNo(count++);
                                subView.setSubCollateralType(proposeCollateralInfoSubView.getSubCollateralType());
                                subView.setAddress(Util.checkNullString(proposeCollateralInfoSubView.getAddress()));
                                subView.setLandOffice(Util.checkNullString(proposeCollateralInfoSubView.getLandOffice()));
                                subView.setTitleDeed(Util.checkNullString(proposeCollateralInfoSubView.getTitleDeed()));
                                subView.setCollateralOwnerAAD(Util.checkNullString(proposeCollateralInfoSubView.getCollateralOwnerAAD()));
                                subView.setAppraisalValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getAppraisalValue()));
                                subView.setMortgageValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getMortgageValue()));
                                subView.setCollateralOwnerUWList(Util.isSafetyList(proposeCollateralInfoSubView.getCollateralOwnerUWList()) ? proposeCollateralInfoSubView.getCollateralOwnerUWList() : new ArrayList<CustomerInfoView>());
                                subView.setMortgageList(Util.isSafetyList(proposeCollateralInfoSubView.getMortgageList()) ? proposeCollateralInfoSubView.getMortgageList() : new ArrayList<MortgageTypeView>());
                                subView.setRelatedWithList(Util.isSafetyList(proposeCollateralInfoSubView.getRelatedWithList()) ? proposeCollateralInfoSubView.getRelatedWithList() : new ArrayList<ProposeCollateralInfoSubView>());
                                proposeCollateralInfoSubViewList.add(subView);
                            }
                            approvedCollateralDecisionReport.setSubViewList(proposeCollateralInfoSubViewList);
                        }
//                        else {
//                            ProposeCollateralInfoSubView proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
//                            List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
//                            proposeCollateralInfoSubView.setPath(pathsub);
//                            proposeCollateralInfoSubViewList.add(proposeCollateralInfoSubView);
//                            approvedCollateralDecisionReport.setSubViewList(proposeCollateralInfoSubViewList);
//                        }
                    }
                } else {
                    log.debug("collateralHeadViewList is Null. {}");
                }
                approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
            }
        }
//        else {
//            ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
//            ProposeCollateralInfoSubView proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
//            List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
//            approvedCollateralDecisionReport.setPath(pathsub);
//            proposeCollateralInfoSubView.setPath(pathsub);
//            proposeCollateralInfoSubViewList.add(proposeCollateralInfoSubView);
//            approvedCollateralDecisionReport.setSubViewList(proposeCollateralInfoSubViewList);
//            approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
//            log.debug("newCollateralViews is Null by fillProposedCollateral. {}");
//        }
        return approvedCollateralDecisionReportArrayList;
    }

    //Opsheet
    public List<ApprovedCollateralDecisionReport> fillApprovedCollaterral(final String pathsub){
        List<ApprovedCollateralDecisionReport> approvedCollateralDecisionReportArrayList = new ArrayList<ApprovedCollateralDecisionReport>();
        List<ProposeCollateralInfoView> newCollateralViews = decisionView.getApproveCollateralList();
        List<ProposeCollateralInfoHeadView> collateralHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();

        if (Util.isSafetyList(newCollateralViews)){
            log.debug("newCollateralViews.size() by fillApprovedCollaterral. {}",newCollateralViews.size());
            for (ProposeCollateralInfoView view : newCollateralViews){
                ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
                    if((DecisionType.APPROVED).equals(view.getUwDecision())){
                        approvedCollateralDecisionReport.setPath(pathsub);
                        approvedCollateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                        approvedCollateralDecisionReport.setAppraisalDate(DateTimeUtil.getCurrentDateTH(view.getAppraisalDate()));
                        approvedCollateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecision()));
                        approvedCollateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                        approvedCollateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));
                        approvedCollateralDecisionReport.setUsage(Util.checkNullString(view.getUsage()));
                        approvedCollateralDecisionReport.setTypeOfUsage(Util.checkNullString(view.getTypeOfUsage()));
                        approvedCollateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));

                        approvedCollateralDecisionReport.setApproved("Yes");

                        approvedCollateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                        approvedCollateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));

                        if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())) {
                            log.debug("getProposeCreditDetailViewList. {}",view.getProposeCreditInfoDetailViewList().size());
                            approvedCollateralDecisionReport.setProposeCreditDetailViewList(view.getProposeCreditInfoDetailViewList());
                        } else {
                            approvedCollateralDecisionReport.setProposeCreditDetailViewList(new ArrayList<ProposeCreditInfoDetailView>());
                        }

                        collateralHeadViewList = view.getProposeCollateralInfoHeadViewList();
                        if (Util.isSafetyList(collateralHeadViewList)){
                            log.debug("collateralHeadViewList.size() {}",collateralHeadViewList.size());
                            for (ProposeCollateralInfoHeadView headView : collateralHeadViewList){
                                approvedCollateralDecisionReport.setCollateralDescription(!Util.isNull(headView.getPotentialCollateral()) ?
                                        Util.checkNullString(headView.getPotentialCollateral().getDescription()) : SPACE);
                                approvedCollateralDecisionReport.setPercentLTVDescription(!Util.isNull(headView.getTcgCollateralType()) ?
                                        Util.checkNullString(headView.getTcgCollateralType().getDescription()) : SPACE);
                                approvedCollateralDecisionReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                                approvedCollateralDecisionReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                                approvedCollateralDecisionReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                                approvedCollateralDecisionReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                                approvedCollateralDecisionReport.setHeadCollTypeDescription(!Util.isNull(headView.getHeadCollType()) ?
                                        Util.checkNullString(headView.getHeadCollType().getDescription()) : SPACE);
                                if (!Util.isNull(headView.getInsuranceCompany())){
                                    if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                                        approvedCollateralDecisionReport.setInsuranceCompany("Partner");
                                    } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                                        approvedCollateralDecisionReport.setInsuranceCompany("Non Partner");
                                    } else {
                                        approvedCollateralDecisionReport.setInsuranceCompany(SPACE);
                                    }
                                }

                                if (Util.isSafetyList(headView.getProposeCollateralInfoSubViewList())){
                                    int count = 1;
                                    List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
                                    for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : headView.getProposeCollateralInfoSubViewList()){
                                        ProposeCollateralInfoSubView subView = new ProposeCollateralInfoSubView();
                                        subView.setPath(pathsub);
                                        subView.setNo(count++);
                                        subView.setSubCollateralType(proposeCollateralInfoSubView.getSubCollateralType());
                                        subView.setAddress(Util.checkNullString(proposeCollateralInfoSubView.getAddress()));
                                        subView.setLandOffice(Util.checkNullString(proposeCollateralInfoSubView.getLandOffice()));
                                        subView.setTitleDeed(Util.checkNullString(proposeCollateralInfoSubView.getTitleDeed()));
                                        subView.setCollateralOwnerAAD(Util.checkNullString(proposeCollateralInfoSubView.getCollateralOwnerAAD()));
                                        subView.setAppraisalValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getAppraisalValue()));
                                        subView.setMortgageValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getMortgageValue()));
                                        subView.setCollateralOwnerUWList(Util.isSafetyList(proposeCollateralInfoSubView.getCollateralOwnerUWList()) ? proposeCollateralInfoSubView.getCollateralOwnerUWList() : new ArrayList<CustomerInfoView>());
                                        subView.setMortgageList(Util.isSafetyList(proposeCollateralInfoSubView.getMortgageList()) ? proposeCollateralInfoSubView.getMortgageList() : new ArrayList<MortgageTypeView>());
                                        subView.setRelatedWithList(Util.isSafetyList(proposeCollateralInfoSubView.getRelatedWithList()) ? proposeCollateralInfoSubView.getRelatedWithList() : new ArrayList<ProposeCollateralInfoSubView>());
                                        proposeCollateralInfoSubViewList.add(subView);
                                    }
                                    approvedCollateralDecisionReport.setSubViewList(proposeCollateralInfoSubViewList);
                                } else {
                                    ProposeCollateralInfoSubView proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
                                    List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
                                    proposeCollateralInfoSubView.setPath(pathsub);
                                    proposeCollateralInfoSubViewList.add(proposeCollateralInfoSubView);
                                    approvedCollateralDecisionReport.setSubViewList(proposeCollateralInfoSubViewList);
                                    log.debug("proposeCollateralInfoSubView in fillApprovedCollaterral {}",proposeCollateralInfoSubView);
                                }
                            }
                        } else {
                            log.debug("collateralHeadViewList is Null. {}");
                        }
                        approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
                    }
//                    else {
//                        ProposeCollateralInfoSubView proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
//                        List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
//                        proposeCollateralInfoSubView.setPath(pathsub);
//                        proposeCollateralInfoSubViewList.add(proposeCollateralInfoSubView);
//                        approvedCollateralDecisionReport.setSubViewList(proposeCollateralInfoSubViewList);
//                    }
            }
        }
//        else {
//            ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
//            List<ProposeCollateralInfoSubView> proposeCollateralInfoSubViewList = new ArrayList<ProposeCollateralInfoSubView>();
//            ProposeCollateralInfoSubView proposeCollateralInfoSubView = new ProposeCollateralInfoSubView();
//            approvedCollateralDecisionReport.setPath(pathsub);
//            proposeCollateralInfoSubView.setPath(pathsub);
//            proposeCollateralInfoSubViewList.add(proposeCollateralInfoSubView);
//            approvedCollateralDecisionReport.setSubViewList(proposeCollateralInfoSubViewList);
//            approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
//            log.debug("newCollateralViews is Null by fillProposedCollateral. {}");
//        }
        return approvedCollateralDecisionReportArrayList;
    }

    public List<ProposedGuarantorDecisionReport> fillProposedGuarantor(String pathsub){
        List<ProposedGuarantorDecisionReport> proposedGuarantorDecisionReportList = new ArrayList<ProposedGuarantorDecisionReport>();
        List<ProposeGuarantorInfoView> detailViews = decisionView.getApproveGuarantorList();

        int count = 1;
        if (Util.isSafetyList(detailViews)){
            log.debug("detailViews.size() {}",detailViews.size());
            for (ProposeGuarantorInfoView view : detailViews){
                ProposedGuarantorDecisionReport guarantorDecisionReport = new ProposedGuarantorDecisionReport();
                guarantorDecisionReport.setCount(count++);
                guarantorDecisionReport.setPath(pathsub);

                StringBuffer name = new StringBuffer();
                name = name.append((!Util.isNull(view.getGuarantorName()) ? !Util.isNull(view.getGuarantorName().getTitleTh()) ?
                        Util.checkNullString(view.getGuarantorName().getTitleTh().getTitleTh()) : SPACE : SPACE))
                        .append(Util.checkNullString(!Util.isNull(view.getGuarantorName()) ? view.getGuarantorName().getFirstNameTh() : SPACE))
                        .append(SPACE).append(!Util.isNull(view.getGuarantorName()) ?
                                Util.checkNullString(view.getGuarantorName().getLastNameTh()) : SPACE);

                guarantorDecisionReport.setName(name.toString());
                guarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));
                if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())){
                    guarantorDecisionReport.setProposeCreditDetailViewList(view.getProposeCreditInfoDetailViewList());
                } else {
                    guarantorDecisionReport.setProposeCreditDetailViewList(new ArrayList<ProposeCreditInfoDetailView>());
                }
                guarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(view.getGuaranteeAmount()));
                proposedGuarantorDecisionReportList.add(guarantorDecisionReport);
            }
        } else {
            log.debug("detailViews is Null by fillProposedGuarantor. {}");
            ProposedGuarantorDecisionReport guarantorDecisionReport = new ProposedGuarantorDecisionReport();
            guarantorDecisionReport.setPath(pathsub);
            proposedGuarantorDecisionReportList.add(guarantorDecisionReport);
        }
        return proposedGuarantorDecisionReportList;
    }

    //ExSum
    public List<ApprovedGuarantorDecisionReport> fillExSumApprovedGuarantor(String pathsub){
        List<ApprovedGuarantorDecisionReport> approvedGuarantorDecisionReportList = new ArrayList<ApprovedGuarantorDecisionReport>();
        List<ProposeGuarantorInfoView> newGuarantorDetails = decisionView.getApproveGuarantorList();

        int count = 1;
        if (Util.isSafetyList(newGuarantorDetails)){
            log.debug("newGuarantorDetails.size() {]",newGuarantorDetails.size());
            for (ProposeGuarantorInfoView view : newGuarantorDetails){
                ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();

                approvedGuarantorDecisionReport.setPath(pathsub);
                approvedGuarantorDecisionReport.setCount(count++);

                StringBuffer name = new StringBuffer();
                name = name.append(!Util.isNull(view.getGuarantorName()) ? !Util.isNull(view.getGuarantorName().getTitleTh()) ?
                        Util.checkNullString(view.getGuarantorName().getTitleTh().getTitleTh()) +
                                Util.checkNullString(view.getGuarantorName().getFirstNameTh()) : SPACE : SPACE)
                        .append(SPACE).append(!Util.isNull(view.getGuarantorName()) ?
                                Util.checkNullString(view.getGuarantorName().getLastNameTh()) : SPACE );

                approvedGuarantorDecisionReport.setName(name.toString());
                approvedGuarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));
                if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())){
                    approvedGuarantorDecisionReport.setProposeCreditDetailViewList(view.getProposeCreditInfoDetailViewList());
                } else {
                    approvedGuarantorDecisionReport.setProposeCreditDetailViewList(new ArrayList<ProposeCreditInfoDetailView>());
                }
                approvedGuarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(view.getGuaranteeAmount()));

                if (!Util.isNull(view.getUwDecision())){
                    if (view.getUwDecision() == DecisionType.APPROVED){
                        approvedGuarantorDecisionReport.setUwDecision("Approved");
                    } else if (view.getUwDecision() == DecisionType.REJECTED){
                        approvedGuarantorDecisionReport.setUwDecision("Rejected");
                    } else {
                        approvedGuarantorDecisionReport.setUwDecision(SPACE);
                    }
                } else {
                    approvedGuarantorDecisionReport.setUwDecision(SPACE);
                }

                if(Util.isNull(view.getGuaranteeAmount())){
                    approvedGuarantorDecisionReport.setGuarantorType(msg.get("report.lessamt"));
                } else {
                    approvedGuarantorDecisionReport.setGuarantorType(msg.get("report.moreamt"));
                }

                approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
            }
        } else {
            log.debug("newGuarantorDetails is Null by fillExSumApprovedGuarantor. {}");
            ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();
            approvedGuarantorDecisionReport.setPath(pathsub);
            approvedGuarantorDecisionReport.setProposeCreditDetailViewList(approvedGuarantorDecisionReport.getProposeCreditDetailViewList());
            approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
        }

        return approvedGuarantorDecisionReportList;
    }

    //Opsheet
    public List<ApprovedGuarantorDecisionReport> fillApprovedGuarantor(String pathsub){
        List<ApprovedGuarantorDecisionReport> approvedGuarantorDecisionReportList = new ArrayList<ApprovedGuarantorDecisionReport>();
        List<ProposeGuarantorInfoView> newGuarantorDetails = decisionView.getApproveGuarantorList();

        int count = 1;
        if (Util.isSafetyList(newGuarantorDetails)){
            log.debug("newGuarantorDetails.size() {}",newGuarantorDetails.size());
            for (ProposeGuarantorInfoView view : newGuarantorDetails){
                ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();

                approvedGuarantorDecisionReport.setPath(pathsub);
                if (!Util.isNull(view.getUwDecision())){
                    if (view.getUwDecision() == DecisionType.APPROVED) {
                        approvedGuarantorDecisionReport.setCount(count++);

                        StringBuffer name = new StringBuffer();
                        name = name.append(!Util.isNull(view.getGuarantorName()) ? !Util.isNull(view.getGuarantorName().getTitleTh()) ?
                                Util.checkNullString(view.getGuarantorName().getTitleTh().getTitleTh()) +
                                        Util.checkNullString(view.getGuarantorName().getFirstNameTh()) : SPACE : SPACE)
                                .append(SPACE).append(!Util.isNull(view.getGuarantorName()) ?
                                        Util.checkNullString(view.getGuarantorName().getLastNameTh()) : SPACE );

                        approvedGuarantorDecisionReport.setName(name.toString());
                        approvedGuarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));
                        approvedGuarantorDecisionReport.setProposeCreditDetailViewList(Util.safetyList(view.getProposeCreditInfoDetailViewList()));
                        approvedGuarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(view.getGuaranteeAmount()));
                        if (view.getUwDecision() == DecisionType.APPROVED){
                            approvedGuarantorDecisionReport.setUwDecision("Approved");
                        }

                        if(Util.isNull(view.getGuaranteeAmount())){
                            approvedGuarantorDecisionReport.setGuarantorType(msg.get("report.lessamt"));
                        } else {
                            approvedGuarantorDecisionReport.setGuarantorType(msg.get("report.moreamt"));
                        }

                        approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
                    } else {
                        approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
                    }
                }
            }
        } else {
            log.debug("newGuarantorDetails is Null by fillApprovedGuarantor. {}");
            ApprovedGuarantorDecisionReport approvedGuarantorDecisionReport = new ApprovedGuarantorDecisionReport();
            approvedGuarantorDecisionReport.setPath(pathsub);
            approvedGuarantorDecisionReport.setProposeCreditDetailViewList(approvedGuarantorDecisionReport.getProposeCreditDetailViewList());
            approvedGuarantorDecisionReportList.add(approvedGuarantorDecisionReport);
        }

        return approvedGuarantorDecisionReportList;
    }

    public List<FollowUpConditionDecisionReport> fillFollowUpCondition(){
        List<FollowUpConditionDecisionReport> followUpConditionDecisionReportList = new ArrayList<FollowUpConditionDecisionReport>();
        List<DecisionFollowConditionView> decisionFollowConditionViews = decisionView.getDecisionFollowConditionViewList();

        int count = 1;
        if (Util.isSafetyList(decisionFollowConditionViews)){
            log.debug("decisionFollowConditionViews.size() {}",decisionFollowConditionViews.size());
            for (DecisionFollowConditionView view : decisionFollowConditionViews){
                FollowUpConditionDecisionReport followUpConditionDecisionReport = new FollowUpConditionDecisionReport();
                followUpConditionDecisionReport.setCount(count++);
                followUpConditionDecisionReport.setConditionView(!Util.isNull(view.getConditionView()) ?
                        Util.checkNullString(view.getConditionView().getName()) : SPACE);
                followUpConditionDecisionReport.setDetail(Util.checkNullString(view.getDetail()));
                followUpConditionDecisionReport.setFollowDate(DateTimeUtil.getCurrentDateTH(view.getFollowDate()));
                followUpConditionDecisionReportList.add(followUpConditionDecisionReport);
            }
        } else {
            log.debug("decisionFollowConditionViews is Null by fillFollowUpCondition. {}");
            FollowUpConditionDecisionReport followUpConditionDecisionReport = new FollowUpConditionDecisionReport();
            followUpConditionDecisionReportList.add(followUpConditionDecisionReport);
        }
        return followUpConditionDecisionReportList;
    }

    public List<ApprovalHistoryDecisionReport> fillApprovalHistory(){
        List<ApprovalHistoryDecisionReport> approvalHistoryDecisionReportArrayList = new ArrayList<ApprovalHistoryDecisionReport>();
        List<ApprovalHistoryView> approvalHistoryViews = decisionView.getApprovalHistoryList();

        int count = 1;
        if (Util.isSafetyList(approvalHistoryViews)){
            log.debug("approvalHistoryViews.size() {}",approvalHistoryViews.size());
            for (ApprovalHistoryView view : approvalHistoryViews){
                ApprovalHistoryDecisionReport approvalHistoryDecisionReport = new ApprovalHistoryDecisionReport();
                approvalHistoryDecisionReport.setCount(count++);
                approvalHistoryDecisionReport.setDescription(!Util.isNull(view.getStepView()) ?
                        Util.checkNullString(view.getStepView().getDescription()) : SPACE);
                approvalHistoryDecisionReport.setUserName(!Util.isNull(view.getUserView()) ?
                        Util.checkNullString(view.getUserView().getUserName()) : SPACE);
                approvalHistoryDecisionReport.setRoleDescription(!Util.isNull(view.getUserView()) ?
                        Util.checkNullString(view.getUserView().getRoleDescription()) : SPACE);
                approvalHistoryDecisionReport.setTitleName(!Util.isNull(view.getUserView()) ?
                        Util.checkNullString(view.getUserView().getTitleName()) : SPACE);
                approvalHistoryDecisionReport.setSubmitDate(DateTimeUtil.getCurrentDateTH(view.getSubmitDate()));
                approvalHistoryDecisionReport.setComments(Util.checkNullString(view.getComments()));
                approvalHistoryDecisionReportArrayList.add(approvalHistoryDecisionReport);
            }
        } else {
            log.debug("approvalHistoryViews is Null by fillApprovalHistory. {}");
            ApprovalHistoryDecisionReport approvalHistoryDecisionReport = new ApprovalHistoryDecisionReport();
            approvalHistoryDecisionReportArrayList.add(approvalHistoryDecisionReport);
        }
        return approvalHistoryDecisionReportArrayList;
    }

    public TotalDecisionReport fillTotalMasterReport(String type){
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

        //Existing Collateral Borrower
        totalDecisionReport.setExtBorrowerTotalAppraisalValue(Util.convertNullToZERO(decisionView.getExtBorrowerTotalAppraisalValue()));
        totalDecisionReport.setExtBorrowerTotalMortgageValue(Util.convertNullToZERO(decisionView.getExtBorrowerTotalMortgageValue()));

        //Existing Related Person
        totalDecisionReport.setExtRelatedTotalAppraisalValue(Util.convertNullToZERO(decisionView.getExtRelatedTotalAppraisalValue()));
        totalDecisionReport.setExtRelatedTotalMortgageValue(Util.convertNullToZERO(decisionView.getExtRelatedTotalMortgageValue()));

        //Existing Guarantor
        //Borrower
        totalDecisionReport.setExtTotalGuaranteeAmount(Util.convertNullToZERO(decisionView.getExtTotalGuaranteeAmount()));
        //Propose Credit
        totalDecisionReport.setProposeTotalCreditLimit(Util.convertNullToZERO(decisionView.getProposeTotalCreditLimit()));

        //Approved Propose Credit
        newCreditDetailViewList = decisionView.getApproveCreditList();
        if (Util.isSafetyList(newCreditDetailViewList)){
            log.debug("newCreditDetailViewList.size() {}",newCreditDetailViewList.size());
            for (ProposeCreditInfoDetailView detailView : newCreditDetailViewList){
                if (!Util.isNull(detailView.getUwDecision())){
                    if ((DecisionType.APPROVED).equals(detailView.getUwDecision()) && ("opsheet").equals(type)){
                        totalDecisionReport.setApproveTotalCreditLimit(Util.convertNullToZERO(decisionView.getApproveTotalCreditLimit()));
                    } else if (("all").equals(type)){
                        totalDecisionReport.setApproveTotalCreditLimit(Util.convertNullToZERO(decisionView.getApproveTotalCreditLimit()));
                    } else {
                        totalDecisionReport.setApproveTotalCreditLimit(BigDecimal.ZERO);
                    }
                } else {
                    totalDecisionReport.setApproveTotalCreditLimit(BigDecimal.ZERO);
                }
            }
        }

        if (!Util.isNull(decisionView.getCreditCustomerType())){
            if (decisionView.getCreditCustomerType() == CreditCustomerType.NORMAL){
                totalDecisionReport.setCreditCusType(1);
            } else if (decisionView.getCreditCustomerType() == CreditCustomerType.PRIME){
                totalDecisionReport.setCreditCusType(2);
            } else {
                totalDecisionReport.setCreditCusType(0);
            }
        }

        if (!Util.isNull(decisionView.getInvestedCountry())){
            totalDecisionReport.setCountryName(Util.checkNullString(decisionView.getInvestedCountry().getName()));
        } else {
            totalDecisionReport.setCountryName(SPACE);
        }
        totalDecisionReport.setExistingSMELimit(Util.convertNullToZERO(decisionView.getExistingSMELimit()));
        totalDecisionReport.setMaximumSMELimit(Util.convertNullToZERO(decisionView.getMaximumSMELimit()));

        //Total Borrower
        totalDecisionReport.setApproveBrwTotalCommercial(Util.convertNullToZERO(decisionView.getApproveBrwTotalCommercial()));
        totalDecisionReport.setApproveBrwTotalComAndOBOD(Util.convertNullToZERO(decisionView.getApproveBrwTotalComAndOBOD()));
        totalDecisionReport.setApproveTotalExposure(Util.convertNullToZERO(decisionView.getApproveTotalExposure()));

        //Proposed Guarantor
        totalDecisionReport.setProposeTotalGuaranteeAmt(Util.convertNullToZERO(decisionView.getProposeTotalGuaranteeAmt()));

        //Approved Guarantor
        List<ProposeGuarantorInfoView> newGuarantorDetails = decisionView.getApproveGuarantorList();

        if (Util.isSafetyList(newGuarantorDetails)){
            log.debug("newGuarantorDetails.size() {}",newGuarantorDetails.size());
            for (ProposeGuarantorInfoView view : newGuarantorDetails){
                if (!Util.isNull(view.getUwDecision())){
                    if ((DecisionType.APPROVED).equals(view.getUwDecision()) && ("opsheet").equals(type)){
                        totalDecisionReport.setApproveTotalGuaranteeAmt(Util.convertNullToZERO(decisionView.getApproveTotalGuaranteeAmt()));
                    } else if (("all").equals(type)){
                        totalDecisionReport.setApproveTotalGuaranteeAmt(Util.convertNullToZERO(decisionView.getApproveTotalGuaranteeAmt()));
                    } else {
                        totalDecisionReport.setApproveTotalGuaranteeAmt(BigDecimal.ZERO);
                    }
                } else {
                    totalDecisionReport.setApproveTotalGuaranteeAmt(BigDecimal.ZERO);
                }
            }
        }

        //BizInfo Address
        if (!Util.isNull(bizInfoSummaryView)){
            totalDecisionReport.setBizLocationName(Util.checkNullString(bizInfoSummaryView.getBizLocationName()));
            totalDecisionReport.setRental(bizInfoSummaryView.getRental());
            totalDecisionReport.setOwnerName(Util.checkNullString(bizInfoSummaryView.getOwnerName()));
            totalDecisionReport.setExpiryDate(DateTimeUtil.getCurrentDateTH(bizInfoSummaryView.getExpiryDate()));
            totalDecisionReport.setAddressNo(Util.checkNullString(bizInfoSummaryView.getAddressNo()));
            totalDecisionReport.setAddressMoo(Util.checkNullString(bizInfoSummaryView.getAddressMoo()));
            totalDecisionReport.setAddressBuilding(Util.checkNullString(bizInfoSummaryView.getAddressBuilding()));
            totalDecisionReport.setAddressStreet(Util.checkNullString(bizInfoSummaryView.getAddressStreet()));
            totalDecisionReport.setProvinceName(!Util.isNull(bizInfoSummaryView.getProvince()) ?
                    Util.checkNullString(bizInfoSummaryView.getProvince().getName()) : SPACE);
            totalDecisionReport.setDistrictName(!Util.isNull(bizInfoSummaryView.getDistrict()) ?
                    Util.checkNullString(bizInfoSummaryView.getDistrict().getName()) : SPACE);
            totalDecisionReport.setSubDisName(!Util.isNull(bizInfoSummaryView.getSubDistrict()) ?
                    Util.checkNullString(bizInfoSummaryView.getSubDistrict().getName()) : SPACE);
            totalDecisionReport.setPostCode(Util.checkNullString(bizInfoSummaryView.getPostCode()));
            totalDecisionReport.setCountryBizName(!Util.isNull(bizInfoSummaryView.getCountry()) ?
                    Util.checkNullString(bizInfoSummaryView.getCountry().getName()) : SPACE);
            totalDecisionReport.setAddressEng(Util.checkNullString(bizInfoSummaryView.getAddressEng()));
        }
        return totalDecisionReport;
    }

    public FollowUpConditionDecisionReport fillFollowDetail(){
        FollowUpConditionDecisionReport followUpConditionDecisionReport = new FollowUpConditionDecisionReport();

        if (Util.isSafetyList(decisionView.getDecisionFollowConditionViewList())){
            log.debug("decisionView.getDecisionFollowConditionViewList().size() {}",decisionView.getDecisionFollowConditionViewList().size());
            for (DecisionFollowConditionView view : decisionView.getDecisionFollowConditionViewList()){
                followUpConditionDecisionReport.setConditionView(!Util.isNull(view.getConditionView()) ?
                        Util.checkNullString(view.getConditionView().getName()) : SPACE);
                followUpConditionDecisionReport.setDetail(Util.checkNullString(view.getDetail()));
                followUpConditionDecisionReport.setFollwDateDetaill(DateTimeUtil.getCurrentDateTH(view.getFollowDate()));
            }
        }
        return followUpConditionDecisionReport;
    }

    public PriceFeeDecisionReport fillPriceFee(){
        PriceFeeDecisionReport priceFeeDecisionReport = new PriceFeeDecisionReport();
        if (!Util.isNull(decisionView)){
            priceFeeDecisionReport.setIntFeeDOA(Util.convertNullToZERO(decisionView.getIntFeeDOA()));
            priceFeeDecisionReport.setFrontendFeeDOA(Util.convertNullToZERO(decisionView.getFrontendFeeDOA()));
            priceFeeDecisionReport.setGuarantorBA(Util.convertNullToZERO(decisionView.getGuarantorBA()));
            priceFeeDecisionReport.setReasonForReduction(Util.checkNullString(decisionView.getReasonForReduction()));
        }
        return priceFeeDecisionReport;
    }
}
