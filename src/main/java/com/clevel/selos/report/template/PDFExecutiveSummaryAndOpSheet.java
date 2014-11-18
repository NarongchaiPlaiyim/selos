package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.DecisionControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.dao.master.TitleDAO;
import com.clevel.selos.dao.master.UsagesDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.Usages;
import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.db.working.ExSummary;
import com.clevel.selos.model.db.working.ProposeLine;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.report.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.ProposeLineTransform;
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
    @Inject private ExSummaryControl exSummaryControl;
    @Inject CustomerInfoControl customerInfoControl;
    @Inject private TitleDAO titleDAO;
    @Inject CustomerDAO customerDAO;

    @Inject
    @SELOS
    Logger log;
    @Inject ExSummaryView exSummaryView;
    @Inject BizInfoSummaryControl bizInfoSummaryControl;
    @Inject ExSummaryDAO exSummaryDAO;
    @Inject ExSummary exSummary;
    @Inject private AppHeaderView appHeaderView;
    @Inject  private WorkCaseDAO workCaseDAO;
    @Inject DecisionView decisionView;
    @Inject private BizInfoSummaryView bizInfoSummaryView;
    @Inject private ProposeLineDAO proposeLineDAO;
    @Inject private ProposeLineTransform proposeLineTransform;
    @Inject private UsagesDAO usagesDAO;
    @Inject BasicInfoDAO basicInfoDAO;
    @Inject
    @NormalMessage
    Message msg;

    private long workCaseId;
    private long statusId;
    private final String SPACE = " ";
    private WorkCase workCase;
    private String minus = "-";
    private String add = "+";
    private char enter = '\n';

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

        if(workCaseId != 0){
            exSummary = exSummaryDAO.findByWorkCaseId(workCaseId);
            if (Util.isNull(exSummary)){
                exSummary = new ExSummary();
            }

            decisionView = decisionControl.findDecisionViewByWorkCaseId(workCaseId);
            if (Util.isNull(decisionView)){
                decisionView = new DecisionView();
            }

            exSummaryView  = exSummaryControl.getExSummaryViewByWorkCaseId(workCaseId, statusId);
            if (Util.isNull(exSummaryView)){
                exSummaryView = new ExSummaryView();
            }

            bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
            if (Util.isNull(bizInfoSummaryView)){
                bizInfoSummaryView = new BizInfoSummaryView();
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

                StringBuilder customerName = new StringBuilder();
                if (!Util.isNull(view.getTitleTh())){
                    customerName = customerName.append(Util.checkNullString(view.getTitleTh().getTitleTh()));
                }

                customerName = customerName.append(Util.checkNullString(view.getFirstNameTh())).append(SPACE).append(Util.checkNullString(view.getLastNameTh()));

                borrowerExsumReport.setCustomerName(Util.checkNullString(customerName.toString()));

                if (!Util.isNull(view.getCitizenId())){
                    borrowerExsumReport.setCitizenId(Util.checkNullString(view.getCitizenId()));
                } else {
                    borrowerExsumReport.setCitizenId(Util.checkNullString(view.getRegistrationId()));
                }

                borrowerExsumReport.setTmbCustomerId(Util.checkNullString(view.getTmbCustomerId()));
                borrowerExsumReport.setRelation(!Util.isNull(view.getRelation()) ? Util.checkNullString(view.getRelation().getDescription()) : minus);
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
                    borrowerExsumReport.setWorthiness(minus);
                }

                List<CustomerCSIView> customerCSIList = view.getCustomerCSIList();

                if (Util.isSafetyList(customerCSIList)){
                    log.debug("customerCSIList.size() {}",customerCSIList.size());
                    for(CustomerCSIView csiView : customerCSIList){
                        borrowerExsumReport.setCustomerCSIList(!Util.isNull(csiView.getWarningCode()) ?
                                Util.checkNullString(csiView.getWarningCode().getCode()) : minus);
                    }
                } else {
                    borrowerExsumReport.setCustomerCSIList(minus);
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
                    ncbRecordExsumReport.setDescription(minus);
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
            log.debug("exSummaryView in Method fillBorrowerCharacteristic is Null. {}", exSummaryView);
        }
        return characteristicExSumReport;
    }

    public List<AccountMovementExSumReport> fillAccountMovement(){
        List<AccountMovementExSumReport> accountMovementExSumReportList = new ArrayList<AccountMovementExSumReport>();
        List<ExSumAccountMovementView> movementViewList = exSummaryView.getExSumAccMovementViewList();

        if(Util.isSafetyList(movementViewList)){
            log.debug("movementViewList.size() {}",movementViewList.size());
            for (ExSumAccountMovementView movementView : movementViewList){
                AccountMovementExSumReport movementExSumReport = new AccountMovementExSumReport();
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
                accountMovementExSumReportList.add(movementExSumReport);
            }
        } else {
            AccountMovementExSumReport movementExSumReport = new AccountMovementExSumReport();
            accountMovementExSumReportList.add(movementExSumReport);
            log.debug("movementViewList in Method fillAccountMovement id Null. {}",movementViewList);
        }
        return accountMovementExSumReportList;
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

                if (!Util.isNull(exSummaryView.getApplicationColorResult())){
                    if ("Y".equalsIgnoreCase(exSummaryView.getApplicationColorResult().code())){
                        decisionExSumReport.setColorResult("YELLOW");
                    } else  if ("R".equalsIgnoreCase(exSummaryView.getApplicationColorResult().code())){
                        decisionExSumReport.setColorResult("RED");
                    } else if ("G".equalsIgnoreCase(exSummaryView.getApplicationColorResult().code())){
                        decisionExSumReport.setColorResult("GREEN");
                    }
                }

                decisionExSumReport.setApplicationResult(exSummaryView.getApplicationResult());

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
            uwDecisionExSumReport.setName(!Util.isNull(exSummaryView.getApproveAuthority()) ? Util.checkNullString(exSummaryView.getApproveAuthority().getName()) : minus);
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
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCase.getId());
        log.debug("################################### {}",workCase.getId());
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

            if (!Util.isNull(basicInfo)){
                report.setLastDecisionDate(DateTimeUtil.convertToStringDDMMYYYY(basicInfo.getLastDecisionDate()));
                log.debug("------------------------ {}",basicInfo.getLimitSetupExpiryDate());
                report.setLimitSetupExpireDate(DateTimeUtil.convertToStringDDMMYYYY(basicInfo.getLimitSetupExpiryDate()));
                log.debug("-------------------------------- {}",report.getLimitSetupExpireDate());
            }

            report.setApprovedDate(DateTimeUtil.getCurrentDateTH(workCase.getCompleteDate()));

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
        genFooter = genFooter.append(userName).append(minus).append(date);
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
                List<ExistingCreditTierDetailReport> existingCreditTierDetailReportList = new ArrayList<ExistingCreditTierDetailReport>();
                List<ExistingSplitLineDetailReport> existingSplitLineDetailReportList = new ArrayList<ExistingSplitLineDetailReport>();

                decisionReport.setCount(count++);
                decisionReport.setPath(pathsub);

                StringBuilder account = new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append(enter);
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber())).append(enter);
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountSuf())).append(enter);

                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append("Acc Status : ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append("Acc Status : ").append(minus);
                }
                decisionReport.setAccount(account.toString());

                StringBuilder prodProgramName = new StringBuilder();
                if (!Util.isNull(detailView.getExistProductProgramView())){
                    prodProgramName = prodProgramName.append(Util.checkNullString(detailView.getExistProductProgramView().getName())).append(enter);
                } else {
                    prodProgramName = prodProgramName.append(minus);
                }
                decisionReport.setProductProgramName(prodProgramName.toString());

                if (!Util.isNull(detailView.getExistCreditTypeView())) {
                    decisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    decisionReport.setCreditTypeName(minus);
                }

                StringBuilder code = new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append(enter);
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode())).append(enter);

                decisionReport.setCode(code.toString());

                decisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                decisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                decisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                decisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));

                if (Util.isSafetyList(detailView.getExistingCreditTierDetailViewList())){
                    for (ExistingCreditTierDetailView existingCreditTierDetailView : detailView.getExistingCreditTierDetailViewList()){
                        ExistingCreditTierDetailReport existingCreditTierDetailReport = new ExistingCreditTierDetailReport();
                        existingCreditTierDetailReport.setInstallment(Util.convertNullToZERO(existingCreditTierDetailView.getInstallment()));

                        StringBuilder finalBasePriceAndInterest = new StringBuilder();
                        if (!Util.isNull(existingCreditTierDetailView.getFinalBasePrice())){
                            finalBasePriceAndInterest = finalBasePriceAndInterest.append(Util.checkNullString(existingCreditTierDetailView.getFinalBasePrice().getName()));
                        } else {
                            finalBasePriceAndInterest = finalBasePriceAndInterest.append(SPACE);
                        }

                        if (!Util.isZero(existingCreditTierDetailView.getFinalInterest()) && !Util.isNull(existingCreditTierDetailView.getFinalInterest())){
                            if ((existingCreditTierDetailView.getFinalInterest()).compareTo(BigDecimal.ZERO) > 0){
                                finalBasePriceAndInterest = finalBasePriceAndInterest.append(add).append(Util.formatNumber(Util.convertNullToZERO(existingCreditTierDetailView.getFinalInterest()))).append(enter);
                            } else {
                                finalBasePriceAndInterest = finalBasePriceAndInterest.append(SPACE).append(Util.formatNumber(Util.convertNullToZERO(existingCreditTierDetailView.getFinalInterest()))).append(enter);
                            }
                        } else {
                            finalBasePriceAndInterest = finalBasePriceAndInterest.append(SPACE).append(enter);
                        }
                        existingCreditTierDetailReport.setFinalBasePriceAndInterest(finalBasePriceAndInterest.toString());
                        existingCreditTierDetailReport.setTenor(Util.convertNullToZERO(existingCreditTierDetailView.getTenor()));
                        existingCreditTierDetailReportList.add(existingCreditTierDetailReport);
                    }
                } else {
                    ExistingCreditTierDetailReport existingCreditTierDetailReport = new ExistingCreditTierDetailReport();
                    existingCreditTierDetailReport.setInstallment(BigDecimal.ZERO);
                    existingCreditTierDetailReport.setFinalBasePriceAndInterest(minus);
                    existingCreditTierDetailReport.setTenor(0);
                    existingCreditTierDetailReportList.add(existingCreditTierDetailReport);
                }
                decisionReport.setExistingCreditTierDetailReports(existingCreditTierDetailReportList);

                if (Util.isSafetyList(detailView.getExistingSplitLineDetailViewList())){
                    for (ExistingSplitLineDetailView existingSplitLineDetailView : detailView.getExistingSplitLineDetailViewList()){
                        ExistingSplitLineDetailReport existingSplitLineDetailReport = new ExistingSplitLineDetailReport();

                        StringBuilder productProgram = new StringBuilder();
                        if (!Util.isNull(existingSplitLineDetailView.getProductProgram())){
                            productProgram = productProgram.append(Util.checkNullString(existingSplitLineDetailView.getProductProgram().getName())).append(enter);
                        } else {
                            productProgram = productProgram.append(minus).append(enter);
                        }
                        existingSplitLineDetailReport.setProductProgramName(productProgram.toString());
                        existingSplitLineDetailReport.setLimit(Util.convertNullToZERO(existingSplitLineDetailView.getLimit()));
                        existingSplitLineDetailReportList.add(existingSplitLineDetailReport);
                    }
                }
                decisionReport.setExistingSplitLineDetailReports(existingSplitLineDetailReportList);

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
                List<ExistingCreditTierDetailReport> existingCreditTierDetailReportList = new ArrayList<ExistingCreditTierDetailReport>();

                borrowerRetailDecisionReport.setCount(count++);
                borrowerRetailDecisionReport.setPath(pathsub);

                StringBuilder account = new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append(enter);
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber())).append(enter);
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountSuf())).append(enter);

                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription()));
                } else {
                    account = account.append("Acc Status: ").append(minus);
                }
                borrowerRetailDecisionReport.setAccount(account.toString());
                log.debug("accountLable by borrowerRetail. {}",account.toString());

                StringBuilder prodProgramName = new StringBuilder();
                if (!Util.isNull(detailView.getExistProductProgramView())){
                    prodProgramName = prodProgramName.append(Util.checkNullString(detailView.getExistProductProgramView().getName())).append(enter);
                } else {
                    prodProgramName = prodProgramName.append(minus);
                }
                borrowerRetailDecisionReport.setProductProgramName(prodProgramName.toString());

                if (!Util.isNull(detailView.getExistCreditTypeView())){
                    borrowerRetailDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    borrowerRetailDecisionReport.setCreditTypeName(minus);
                }

                StringBuilder code = new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append(enter);
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode())).append(enter);
                borrowerRetailDecisionReport.setCode(code.toString());
                borrowerRetailDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerRetailDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                borrowerRetailDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                borrowerRetailDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                borrowerRetailDecisionReport.setInstallment(Util.convertNullToZERO(detailView.getInstallment()));
                borrowerRetailDecisionReport.setIntFeePercent(Util.convertNullToZERO(detailView.getIntFeePercent()));
                borrowerRetailDecisionReport.setTenor(Util.convertNullToZERO(detailView.getTenor()));

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
            //TODO
    public List<RelatedCommercialDecisionReport> fillRelatedCommercial(String pathsub){
        List<RelatedCommercialDecisionReport> relatedCommercialDecisionReportList = new ArrayList<RelatedCommercialDecisionReport>();
        List<ExistingCreditDetailView> existingConditionDetailViews = decisionView.getExtRelatedComCreditList();

        int count = 1;
        if(Util.isSafetyList(existingConditionDetailViews)){
            log.debug("existingConditionDetailViews.size() {}",existingConditionDetailViews.size());
            for (ExistingCreditDetailView detailView : existingConditionDetailViews){
                RelatedCommercialDecisionReport relatedCommercialDecisionReport = new RelatedCommercialDecisionReport();
                List<ExistingCreditTierDetailReport> existingCreditTierDetailReportList = new ArrayList<ExistingCreditTierDetailReport>();
                List<ExistingSplitLineDetailReport> existingSplitLineDetailReportList = new ArrayList<ExistingSplitLineDetailReport>();
                relatedCommercialDecisionReport.setCount(count++);
                relatedCommercialDecisionReport.setPath(pathsub);

                StringBuilder account = new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append(enter);
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber())).append(enter);
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountSuf())).append(enter);

                if(!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription())).append(enter);
                } else {
                    account = account.append("Acc Status: ").append(minus).append(enter);
                }
                relatedCommercialDecisionReport.setAccount(account.toString());

                StringBuilder prodProgramName = new StringBuilder();
                if (!Util.isNull(detailView.getExistProductProgramView())){
                    prodProgramName = prodProgramName.append(Util.checkNullString(detailView.getExistProductProgramView().getName())).append(enter);
                } else {
                    prodProgramName = prodProgramName.append(minus);
                }
                relatedCommercialDecisionReport.setProductProgramName(prodProgramName.toString());

                if (!Util.isNull(detailView.getExistCreditTypeView())){
                    relatedCommercialDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    relatedCommercialDecisionReport.setCreditTypeName(minus);
                }

                StringBuilder code = new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append(enter);
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode())).append(enter);

                relatedCommercialDecisionReport.setCode(code.toString());
                relatedCommercialDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedCommercialDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedCommercialDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedCommercialDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));

                if (Util.isSafetyList(detailView.getExistingCreditTierDetailViewList())){
                    for (ExistingCreditTierDetailView existingCreditTierDetailView : detailView.getExistingCreditTierDetailViewList()){
                        ExistingCreditTierDetailReport existingCreditTierDetailReport = new ExistingCreditTierDetailReport();
                        existingCreditTierDetailReport.setInstallment(Util.convertNullToZERO(existingCreditTierDetailView.getInstallment()));

                        StringBuilder finalBasePriceAndInterest = new StringBuilder();
                        if (!Util.isNull(existingCreditTierDetailView.getFinalBasePrice())){
                            finalBasePriceAndInterest = finalBasePriceAndInterest.append(Util.checkNullString(existingCreditTierDetailView.getFinalBasePrice().getName()));
                        } else {
                            finalBasePriceAndInterest = finalBasePriceAndInterest.append(SPACE);
                        }

                        if (!Util.isZero(existingCreditTierDetailView.getFinalInterest()) && !Util.isNull(existingCreditTierDetailView.getFinalInterest())){
                            if ((existingCreditTierDetailView.getFinalInterest()).compareTo(BigDecimal.ZERO) > 0){
                                finalBasePriceAndInterest = finalBasePriceAndInterest.append(add).append(Util.formatNumber(Util.convertNullToZERO(existingCreditTierDetailView.getFinalInterest()))).append(enter);
                            } else {
                                finalBasePriceAndInterest = finalBasePriceAndInterest.append(SPACE).append(Util.formatNumber(Util.convertNullToZERO(existingCreditTierDetailView.getFinalInterest()))).append(enter);
                            }
                        } else {
                            finalBasePriceAndInterest = finalBasePriceAndInterest.append(SPACE).append(enter);
                        }
                        existingCreditTierDetailReport.setFinalBasePriceAndInterest(finalBasePriceAndInterest.toString());
                        existingCreditTierDetailReport.setTenor(Util.convertNullToZERO(existingCreditTierDetailView.getTenor()));
                        existingCreditTierDetailReportList.add(existingCreditTierDetailReport);
                    }
                } else {
                    ExistingCreditTierDetailReport existingCreditTierDetailReport = new ExistingCreditTierDetailReport();
                    existingCreditTierDetailReport.setInstallment(BigDecimal.ZERO);
                    existingCreditTierDetailReport.setFinalBasePriceAndInterest(minus);
                    existingCreditTierDetailReport.setTenor(0);
                    existingCreditTierDetailReportList.add(existingCreditTierDetailReport);
                }
                relatedCommercialDecisionReport.setExistingCreditTierDetailReports(existingCreditTierDetailReportList);

                if (Util.isSafetyList(detailView.getExistingSplitLineDetailViewList())){
                    for (ExistingSplitLineDetailView existingSplitLineDetailView : detailView.getExistingSplitLineDetailViewList()){
                        ExistingSplitLineDetailReport existingSplitLineDetailReport = new ExistingSplitLineDetailReport();

                        StringBuilder productProgran = new StringBuilder();
                        if (!Util.isNull(existingSplitLineDetailView.getProductProgram())){
                            productProgran = productProgran.append(Util.checkNullString(existingSplitLineDetailView.getProductProgram().getName())).append(enter);
                        } else {
                            productProgran = productProgran.append(minus).append(enter);
                        }
                        existingSplitLineDetailReport.setProductProgramName(productProgran.toString());
                        existingSplitLineDetailReport.setLimit(Util.convertNullToZERO(existingSplitLineDetailView.getLimit()));
                        existingSplitLineDetailReportList.add(existingSplitLineDetailReport);
                    }
                }
                relatedCommercialDecisionReport.setExistingSplitLineDetailReports(existingSplitLineDetailReportList);

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
                List<ExistingCreditTierDetailReport> existingCreditTierDetailReportList = new ArrayList<ExistingCreditTierDetailReport>();

                relatedRetailDecisionReport.setCount(count++);
                relatedRetailDecisionReport.setPath(pathsub);

                StringBuilder account = new StringBuilder();
                account = account.append(Util.checkNullString(detailView.getAccountName())).append(enter);
                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber())).append(enter);
                account = account.append(" Suf.: ").append(Util.checkNullString(detailView.getAccountSuf())).append(enter);

                if (!Util.isNull(detailView.getExistAccountStatusView())){
                    account = account.append("Acc Status: ").append(Util.checkNullString(detailView.getExistAccountStatusView().getDescription())).append(enter);
                } else {
                    account = account.append("Acc Status: ").append(minus).append(enter);
                }
                relatedRetailDecisionReport.setAccount(account.toString());

                StringBuilder prodProgramName = new StringBuilder();
                if (!Util.isNull(detailView.getExistProductProgramView())){
                    prodProgramName = prodProgramName.append(Util.checkNullString(detailView.getExistProductProgramView().getName())).append(enter);
                } else {
                    prodProgramName = prodProgramName.append(minus);
                }
                relatedRetailDecisionReport.setProductProgramName(prodProgramName.toString());

                if (!Util.isNull(detailView.getExistCreditTypeView())){
                    relatedRetailDecisionReport.setCreditTypeName(Util.checkNullString(detailView.getExistCreditTypeView().getName()));
                } else {
                    relatedRetailDecisionReport.setCreditTypeName(minus);
                }

                StringBuilder code = new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append(enter);
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode())).append(enter);

                relatedRetailDecisionReport.setCode(code.toString());

                relatedRetailDecisionReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedRetailDecisionReport.setPcePercent(Util.convertNullToZERO(detailView.getPceLimit()));
                relatedRetailDecisionReport.setPceLimit(Util.convertNullToZERO(detailView.getLimit()));
                relatedRetailDecisionReport.setOutstanding(Util.convertNullToZERO(detailView.getOutstanding()));
                relatedRetailDecisionReport.setInstallment(Util.convertNullToZERO(detailView.getInstallment()));
                relatedRetailDecisionReport.setIntFeePercent(Util.convertNullToZERO(detailView.getIntFeePercent()));
                relatedRetailDecisionReport.setTenor(Util.convertNullToZERO(detailView.getTenor()));

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

    public List<ExistingCollateralBorrowerDecisionReport> fillExistingCollateralBorrower(String path) throws UnsupportedEncodingException {
        List<ExistingCollateralBorrowerDecisionReport> collateralBorrowerDecisionReportList = new ArrayList<ExistingCollateralBorrowerDecisionReport>();
        List<ExistingCollateralDetailView> conditionDetailViews = decisionView.getExtBorrowerCollateralList();

        int count = 1;
        if (Util.isSafetyList(conditionDetailViews)){
            log.debug("conditionDetailViews.size() {}",conditionDetailViews.size());
            for (ExistingCollateralDetailView detailView : conditionDetailViews){
                ExistingCollateralBorrowerDecisionReport collateralBorrowerDecisionReport = new ExistingCollateralBorrowerDecisionReport();
                List<ExistingCreditTypeDetailReport> creditTypeDetailReportList = new ArrayList<ExistingCreditTypeDetailReport>();

                collateralBorrowerDecisionReport.setCount(count++);
                collateralBorrowerDecisionReport.setPath(path);

                StringBuilder collateralType = new StringBuilder();
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.potential")).append((!Util.isNull(detailView.getPotentialCollateral()) ? (Util.checkNullString(detailView.getPotentialCollateral().getDescription())) : minus)).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralType")).append((!Util.isNull(detailView.getCollateralType()) ? (Util.checkNullString(detailView.getCollateralType().getDescription())) : minus)).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.owner")).append((Util.checkNullString(detailView.getOwner()))).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.relationship")).append((!Util.isNull(detailView.getRelation()) ? (Util.checkNullString(detailView.getRelation().getDescription())) : minus)).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.appraisalDate")).append(DateTimeUtil.convertToStringDDMMYYYY(DateTimeUtil.getCurrentDateTH((detailView.getAppraisalDate()))) == null ? minus : DateTimeUtil.convertToStringDDMMYYYY(DateTimeUtil.getCurrentDateTH(detailView.getAppraisalDate()))).append(enter);//TODO
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralNumber")).append((Util.checkNullString(detailView.getCollateralNumber()))).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralLocation")).append((Util.checkNullString(detailView.getCollateralLocation()))).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.remark")).append((Util.checkNullString(detailView.getRemark()))).append(enter);


                collateralBorrowerDecisionReport.setCollateralType(collateralType.toString());
                collateralBorrowerDecisionReport.setCusName(Util.checkNullString(detailView.getCusName()));

                if (Util.isSafetyList(detailView.getExistingCreditTypeDetailViewList())){
                     for (ExistingCreditTypeDetailView existingCreditTypeDetailView : detailView.getExistingCreditTypeDetailViewList()){
                         ExistingCreditTypeDetailReport existingCreditTypeDetailReport = new ExistingCreditTypeDetailReport();
                         StringBuilder creditType = new StringBuilder();

                         creditType = creditType.append(Util.checkNullString(existingCreditTypeDetailView.getAccountName())).append(enter);
                         creditType = creditType.append("Acc No.: ").append(Util.checkNullString(existingCreditTypeDetailView.getAccountNumber())).append(enter);
                         creditType = creditType.append("Suf.: ").append(Util.checkNullString(existingCreditTypeDetailView.getAccountSuf())).append(enter);
                         creditType = creditType.append(Util.checkNullString(existingCreditTypeDetailView.getProductProgram())).append(enter);
                         creditType = creditType.append(Util.checkNullString(existingCreditTypeDetailView.getCreditFacility())).append(SPACE)
                                 .append(Util.formatNumber(Util.convertNullToZERO(existingCreditTypeDetailView.getLimit()))).append(enter);

                         existingCreditTypeDetailReport.setCreditType(creditType.toString());
                         creditTypeDetailReportList.add(existingCreditTypeDetailReport);
                     }
                }
                collateralBorrowerDecisionReport.setExistingCreditTypeDetailReports(creditTypeDetailReportList);

                collateralBorrowerDecisionReport.setProductProgram(Util.checkNullString(detailView.getProductProgram()));
                collateralBorrowerDecisionReport.setCreditFacility(Util.checkNullString(detailView.getCreditFacility()));
                collateralBorrowerDecisionReport.setMortgageType(!Util.isNull(detailView.getMortgageType()) ?
                        (Util.checkNullString(detailView.getMortgageType().getMortgage())) : minus);
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
                List<ExistingCreditTypeDetailReport> creditTypeDetailReportList = new ArrayList<ExistingCreditTypeDetailReport>();

                collateralRelatedDecisionReport.setCount(count++);
                collateralRelatedDecisionReport.setPath(path);

                StringBuilder collateralType = new StringBuilder();
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.potential")).append((!Util.isNull(detailView.getPotentialCollateral()) ? (Util.checkNullString(detailView.getPotentialCollateral().getDescription())) : minus)).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralType")).append((!Util.isNull(detailView.getCollateralType()) ? (Util.checkNullString(detailView.getCollateralType().getDescription())) : minus)).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.owner")).append((Util.checkNullString(detailView.getOwner()))).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.relationship")).append((!Util.isNull(detailView.getRelation()) ? (Util.checkNullString(detailView.getRelation().getDescription())) : minus)).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.appraisalDate")).append(DateTimeUtil.convertToStringDDMMYYYY(DateTimeUtil.getCurrentDateTH((detailView.getAppraisalDate()))) == null ? minus : DateTimeUtil.convertToStringDDMMYYYY(DateTimeUtil.getCurrentDateTH(detailView.getAppraisalDate()))).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralNumber")).append((Util.checkNullString(detailView.getCollateralNumber()))).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.collateralLocation")).append((Util.checkNullString(detailView.getCollateralLocation()))).append(enter);
                collateralType = collateralType.append(msg.get("app.decision.tb.td.label.remark")).append((Util.checkNullString(detailView.getRemark()))).append(enter);

                collateralRelatedDecisionReport.setCollateralType(collateralType.toString());
                collateralRelatedDecisionReport.setCusName(Util.checkNullString(detailView.getCusName()));

                if (Util.isSafetyList(detailView.getExistingCreditTypeDetailViewList())){
                    if (Util.isSafetyList(detailView.getExistingCreditTypeDetailViewList())){
                        for (ExistingCreditTypeDetailView existingCreditTypeDetailView : detailView.getExistingCreditTypeDetailViewList()){
                            ExistingCreditTypeDetailReport existingCreditTypeDetailReport = new ExistingCreditTypeDetailReport();
                            StringBuilder creditType = new StringBuilder();

                            creditType = creditType.append(Util.checkNullString(existingCreditTypeDetailView.getAccountName())).append(enter);
                            creditType = creditType.append("Acc No.: ").append(Util.checkNullString(existingCreditTypeDetailView.getAccountNumber())).append(enter);
                            creditType = creditType.append("Suf.: ").append(Util.checkNullString(existingCreditTypeDetailView.getAccountSuf())).append(enter);
                            creditType = creditType.append(Util.checkNullString(existingCreditTypeDetailView.getProductProgram())).append(enter);
                            creditType = creditType.append(Util.checkNullString(existingCreditTypeDetailView.getCreditFacility())).append(SPACE)
                                    .append(Util.formatNumber(Util.convertNullToZERO(existingCreditTypeDetailView.getLimit()))).append(enter);

                            existingCreditTypeDetailReport.setCreditType(creditType.toString());
                            creditTypeDetailReportList.add(existingCreditTypeDetailReport);
                        }
                    }
                }
                collateralRelatedDecisionReport.setExistingCreditTypeDetailReports(creditTypeDetailReportList);
                collateralRelatedDecisionReport.setProductProgram(Util.checkNullString(detailView.getProductProgram()));
                collateralRelatedDecisionReport.setCreditFacility(Util.checkNullString(detailView.getCreditFacility()));
                collateralRelatedDecisionReport.setMortgageType(!Util.isNull(detailView.getMortgageType()) ? (Util.checkNullString(detailView.getMortgageType().getMortgage())) : minus);
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
                List<ExistingCreditTypeDetailReport> creditTypeDetailReportList = new ArrayList<ExistingCreditTypeDetailReport>();

                if (Util.isSafetyList(detailView.getExistingCreditTypeDetailViewList())){
                    if (Util.isSafetyList(detailView.getExistingCreditTypeDetailViewList())){
                        for (ExistingCreditTypeDetailView existingCreditTypeDetailView : detailView.getExistingCreditTypeDetailViewList()){
                            ExistingCreditTypeDetailReport existingCreditTypeDetailReport = new ExistingCreditTypeDetailReport();
                            StringBuilder creditType = new StringBuilder();

                            creditType = creditType.append(Util.checkNullString(existingCreditTypeDetailView.getAccountName())).append(enter);
                            creditType = creditType.append("Acc No.: ").append(Util.checkNullString(existingCreditTypeDetailView.getAccountNumber())).append(enter);
                            creditType = creditType.append("Suf.: ").append(Util.checkNullString(existingCreditTypeDetailView.getAccountSuf())).append(enter);

                            existingCreditTypeDetailReport.setCreditType(creditType.toString());

                            StringBuilder prodNameAndCreditFac = new StringBuilder();

                            prodNameAndCreditFac = prodNameAndCreditFac.append(Util.checkNullString(existingCreditTypeDetailView.getProductProgram())).append(enter);
                            prodNameAndCreditFac = prodNameAndCreditFac.append(Util.checkNullString(existingCreditTypeDetailView.getCreditFacility())).append(enter);

                            existingCreditTypeDetailReport.setProductProgramNameAndCreditFacility(prodNameAndCreditFac.toString());
                            existingCreditTypeDetailReport.setLimit(Util.convertNullToZERO(existingCreditTypeDetailView.getLimit()));

                            StringBuilder guaranteeAmt = new StringBuilder();
                            guaranteeAmt = guaranteeAmt.append("Guarantee Amount: ").append(enter)
                                    .append(Util.formatNumber(Util.convertNullToZERO(existingCreditTypeDetailView.getGuaranteeAmount())));

                            existingCreditTypeDetailReport.setGuaranteeAmount(guaranteeAmt.toString());
                            creditTypeDetailReportList.add(existingCreditTypeDetailReport);
                        }
                    }
                }

                guarantorBorrowerDecisionReport.setCount(count++);
                guarantorBorrowerDecisionReport.setPath(pathsub);

                StringBuilder name = new StringBuilder();

                name = name.append(!Util.isNull(detailView.getGuarantorName()) ? !Util.isNull(detailView.getGuarantorName().getTitleTh()) ?
                        Util.checkNullString(detailView.getGuarantorName().getTitleTh().getTitleTh()) : minus : minus)
                        .append(!Util.isNull(detailView.getGuarantorName()) ? Util.checkNullString(detailView.getGuarantorName().getFirstNameTh()) : minus)
                        .append(minus).append(!Util.isNull(detailView.getGuarantorName()) ? Util.checkNullString(detailView.getGuarantorName().getLastNameTh()) : minus);
                guarantorBorrowerDecisionReport.setGuarantorName(name.toString());
                guarantorBorrowerDecisionReport.setTcgLgNo(Util.checkNullString(detailView.getTcgLgNo()));
                guarantorBorrowerDecisionReport.setExistingCreditTypeDetailReports(creditTypeDetailReportList);
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
                List<ProposeCreditInfoTierDetailReport> creditInfoTierDetailReportList = new ArrayList<ProposeCreditInfoTierDetailReport>();

                if (Util.isSafetyList(detailView.getProposeCreditInfoTierDetailViewList())){
                    log.debug("detailView.getProposeCreditInfoTierDetailViewList() {}",detailView.getProposeCreditInfoTierDetailViewList().size());
                    for (ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView : detailView.getProposeCreditInfoTierDetailViewList()){
                        ProposeCreditInfoTierDetailReport proposeCreditInfoTierDetailReport = new ProposeCreditInfoTierDetailReport();

                        proposeCreditInfoTierDetailReport.setStandardPriceLabel(Util.checkNullString(proposeCreditInfoTierDetailView.getStandardPriceLabel()));
                        proposeCreditInfoTierDetailReport.setSuggestPriceLabel(Util.checkNullString(proposeCreditInfoTierDetailView.getSuggestPriceLabel()));
                        proposeCreditInfoTierDetailReport.setFinalPriceLabel(Util.checkNullString(proposeCreditInfoTierDetailView.getFinalPriceLabel()));
                        proposeCreditInfoTierDetailReport.setInstallment(Util.convertNullToZERO(proposeCreditInfoTierDetailView.getInstallment()));
                        proposeCreditInfoTierDetailReport.setTenor(proposeCreditInfoTierDetailView.getTenor());
                        creditInfoTierDetailReportList.add(proposeCreditInfoTierDetailReport);
                    }
                }

                proposedView.setPath(pathsub);
                proposedView.setCount(count++);

                if (!Util.isNull(detailView.getProductProgramView())){
                    proposedView.setProdName(Util.checkNullString( detailView.getProductProgramView().getName()));
                } else {
                    proposedView.setProdName(minus);
                }

                if (!Util.isNull(detailView.getCreditTypeView())){
                    proposedView.setCredittypeName(Util.checkNullString(detailView.getCreditTypeView().getName()));
                } else {
                    proposedView.setCredittypeName(minus);
                }

                StringBuilder code = new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append(enter);
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode())).append(enter);

                proposedView.setProductAndProject(code.toString());
                proposedView.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                proposedView.setFrontEndFee(Util.convertNullToZERO(detailView.getFrontEndFee()));

                proposedView.setProposeCreditInfoTierDetailReports(creditInfoTierDetailReportList);

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
                        builder = builder.append("Refinance : Yes").append(enter);
                        proposedView.setRefinance("Yes");
                    } else if (detailView.getRefinance() == RadioValue.NO.value()){
                        builder = builder.append("Refinance : No").append(enter);
                    }
                }

                builder = builder.append("Purpose : ").append(!Util.isNull(detailView.getLoanPurposeView()) ?
                        Util.checkNullString(detailView.getLoanPurposeView().getDescription()) : minus).append(enter);
                builder = builder.append("Purpose Detail : ").append(Util.checkNullString(detailView.getProposeDetail())).append(enter);
                builder = builder.append("Disbursement : ").append(!Util.isNull(detailView.getDisbursementTypeView()) ?
                        Util.checkNullString(detailView.getDisbursementTypeView().getDisbursement()) : minus).append(SPACE);
                builder = builder.append("Hold Amount : ").append(Util.formatNumber(Util.convertNullToZERO(detailView.getHoldLimitAmount())));

                proposedView.setProposedDetail(builder.toString());
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
                List<ProposeCreditInfoTierDetailReport> creditInfoTierDetailReportList = new ArrayList<ProposeCreditInfoTierDetailReport>();
                approvedView.setPath(pathsub);

                if (Util.isSafetyList(detailView.getProposeCreditInfoTierDetailViewList())){
                    for (ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView : detailView.getProposeCreditInfoTierDetailViewList()){
                        ProposeCreditInfoTierDetailReport proposeCreditInfoTierDetailReport = new ProposeCreditInfoTierDetailReport();
                        proposeCreditInfoTierDetailReport.setStandardPriceLabel(Util.checkNullString(proposeCreditInfoTierDetailView.getStandardPriceLabel()));
                        proposeCreditInfoTierDetailReport.setSuggestPriceLabel(Util.checkNullString(proposeCreditInfoTierDetailView.getSuggestPriceLabel()));
                        proposeCreditInfoTierDetailReport.setFinalPriceLabel(Util.checkNullString(proposeCreditInfoTierDetailView.getFinalPriceLabel()));
                        proposeCreditInfoTierDetailReport.setInstallment(Util.convertNullToZERO(proposeCreditInfoTierDetailView.getInstallment()));
                        proposeCreditInfoTierDetailReport.setTenor(proposeCreditInfoTierDetailView.getTenor());
                        creditInfoTierDetailReportList.add(proposeCreditInfoTierDetailReport);
                    }
                }

                approvedView.setCount(count++);

                if (!Util.isNull(detailView.getUwDecision())){
                    if ((DecisionType.APPROVED.equals(detailView.getUwDecision()))){
                        approvedView.setUwDecision("APPROVED");
                    } else  if ((DecisionType.REJECTED).equals(detailView.getUwDecision())){
                        approvedView.setUwDecision("REJECTED");
                    } else {
                        approvedView.setUwDecision(minus);
                    }
                } else {
                    approvedView.setUwDecision(minus);
                }

                if (!Util.isNull(detailView.getProductProgramView())){
                    approvedView.setProdName(Util.checkNullString(detailView.getProductProgramView().getName()));
                } else {
                    approvedView.setProdName(minus);
                }

                if (!Util.isNull(detailView.getCreditTypeView())){
                    approvedView.setCredittypeName(Util.checkNullString(detailView.getCreditTypeView().getName()));
                } else {
                    approvedView.setCredittypeName(minus);
                }

                StringBuilder code = new StringBuilder();
                code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append(enter);
                code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode())).append(enter);

                approvedView.setProductAndProject(code.toString());
                approvedView.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                approvedView.setFrontEndFee(Util.convertNullToZERO(detailView.getFrontEndFee()));
                approvedView.setProposeCreditInfoTierDetailReports(creditInfoTierDetailReportList);

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
                        builder = builder.append("Refinance : Yes").append(enter);
                        approvedView.setRefinance("Yes");
                    } else if (detailView.getRefinance() == RadioValue.NO.value()){
                        builder = builder.append("Refinance : No").append(enter);
                    }
                }

                builder = builder.append("Purpose : ").append(!Util.isNull(detailView.getLoanPurposeView()) ?
                        Util.checkNullString(detailView.getLoanPurposeView().getDescription()) : minus).append(enter);
                builder = builder.append("Purpose Detail : ").append(Util.checkNullString(detailView.getProposeDetail())).append(enter);
                builder = builder.append("Disbursement : ").append(!Util.isNull(detailView.getDisbursementTypeView()) ?
                        Util.checkNullString(detailView.getDisbursementTypeView().getDisbursement()) : minus).append(SPACE);
                builder = builder.append("Hold Amount : ").append(Util.formatNumber(Util.convertNullToZERO(detailView.getHoldLimitAmount())));

                approvedView.setProposedDetail(builder.toString());
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

        List<ProposedCreditDecisionReport> proposedCreditDecisionReportList = new ArrayList<ProposedCreditDecisionReport>();

        ProposeLine proposeLine = null;
        if(!Util.isZero(workCaseId)) {
            proposeLine = proposeLineDAO.findByWorkCaseId(workCaseId);
        }

        ProposeLineView approveLineView = proposeLineTransform.transformProposeLineToReport(proposeLine, ProposeType.A);

        int count = 1;
        if (!Util.isNull(approveLineView)){
            log.debug("approveLineView is not null.");
            if (Util.isSafetyList(approveLineView.getProposeCreditInfoDetailViewList())){
                log.debug("newCreditDetailViewList.size() {}",approveLineView.getProposeCreditInfoDetailViewList().size());
                for (ProposeCreditInfoDetailView detailView : approveLineView.getProposeCreditInfoDetailViewList()){
                    ProposedCreditDecisionReport approvedView = new ProposedCreditDecisionReport();
                    List<ProposeCreditInfoTierDetailReport> creditInfoTierDetailReportList = new ArrayList<ProposeCreditInfoTierDetailReport>();
                    approvedView.setPath(pathsub);

                    if ((DecisionType.APPROVED).equals(detailView.getUwDecision())){
                        if (Util.isSafetyList(detailView.getProposeCreditInfoTierDetailViewList())){
                            for (ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView : detailView.getProposeCreditInfoTierDetailViewList()){
                                ProposeCreditInfoTierDetailReport proposeCreditInfoTierDetailReport = new ProposeCreditInfoTierDetailReport();
                                proposeCreditInfoTierDetailReport.setStandardPriceLabel(Util.checkNullString(proposeCreditInfoTierDetailView.getStandardPriceLabel()));
                                proposeCreditInfoTierDetailReport.setSuggestPriceLabel(Util.checkNullString(proposeCreditInfoTierDetailView.getSuggestPriceLabel()));
                                proposeCreditInfoTierDetailReport.setFinalPriceLabel(Util.checkNullString(proposeCreditInfoTierDetailView.getFinalPriceLabel()));
                                proposeCreditInfoTierDetailReport.setInstallment(Util.convertNullToZERO(proposeCreditInfoTierDetailView.getInstallment()));
                                proposeCreditInfoTierDetailReport.setTenor(proposeCreditInfoTierDetailView.getTenor());
                                creditInfoTierDetailReportList.add(proposeCreditInfoTierDetailReport);
                            }
                        }

                        approvedView.setCount(count++);

                        if (!Util.isNull(detailView.getProductProgramView())){
                            approvedView.setProdName(Util.checkNullString(detailView.getProductProgramView().getName()));
                        } else {
                            approvedView.setProdName(minus);
                        }

                        if ((DecisionType.APPROVED).equals(detailView.getUwDecision())){
                            approvedView.setUwDecision("APPROVED");
                        }

                        if (!Util.isNull(detailView.getCreditTypeView())){
                            approvedView.setCredittypeName(Util.checkNullString(detailView.getCreditTypeView().getName()));
                        } else {
                            approvedView.setCredittypeName(minus);
                        }

                        StringBuilder code = new StringBuilder();
                        code = code.append("Product: ").append(Util.checkNullString(detailView.getProductCode())).append(enter);
                        code = code.append("Project: ").append(Util.checkNullString(detailView.getProjectCode())).append(enter);

                        approvedView.setProductAndProject(code.toString());
                        approvedView.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                        approvedView.setFrontEndFee(Util.convertNullToZERO(detailView.getFrontEndFee()));
                        approvedView.setProposeCreditInfoTierDetailReports(creditInfoTierDetailReportList);

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
                                builder = builder.append("Refinance : Yes").append(enter);
                                approvedView.setRefinance("Yes");
                            } else if (detailView.getRefinance() == RadioValue.NO.value()){
                                builder = builder.append("Refinance : No").append(enter);
                            }
                        }

                        builder = builder.append("Purpose : ").append(!Util.isNull(detailView.getLoanPurposeView()) ?
                                Util.checkNullString(detailView.getLoanPurposeView().getDescription()) : minus).append(enter);
                        builder = builder.append("Purpose Detail : ").append(Util.checkNullString(detailView.getProposeDetail())).append(enter);
                        builder = builder.append("Disbursement : ").append(!Util.isNull(detailView.getDisbursementTypeView()) ?
                                Util.checkNullString(detailView.getDisbursementTypeView().getDisbursement()) : minus).append(SPACE);
                        builder = builder.append("Hold Amount : ").append(Util.formatNumber(Util.convertNullToZERO(detailView.getHoldLimitAmount())));

                        approvedView.setProposedDetail(builder.toString());
                    }
                    proposedCreditDecisionReportList.add(approvedView);
                }
            } else {
                ProposedCreditDecisionReport approvedView = new ProposedCreditDecisionReport();
                approvedView.setPath(pathsub);
                proposedCreditDecisionReportList.add(approvedView);
                log.debug("newCreditDetailViewList is Null by fillApprovedCredit. {}");
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
                            .append(!Util.isNull(view.getStandardFrontEndFee()) ? Util.convertNullToZERO(view.getStandardFrontEndFee().getFeeYear()) : minus);
                    proposeFeeInformationDecisionReport.setStandardFront(standard.toString());
                    } else {
                    proposeFeeInformationDecisionReport.setStandardFront("0.00%, - ");
                }

                if (!Util.isNull(view.getCommitmentFee())){
                    StringBuilder commit = new StringBuilder();

                    commit = commit.append(Util.convertNullToZERO(view.getCommitmentFee().getPercentFee())).append(" % ").append(" , ")
                            .append(!Util.isNull(view.getCommitmentFee()) ? Util.convertNullToZERO(view.getCommitmentFee().getFeeYear()) : minus);
                    proposeFeeInformationDecisionReport.setCommitmentFee(commit.toString());
                } else {
                    proposeFeeInformationDecisionReport.setCommitmentFee("0.00%, - ");
                }

                if (!Util.isNull(view.getExtensionFee())){
                    StringBuilder extension = new StringBuilder();

                    extension = extension.append(Util.convertNullToZERO(view.getExtensionFee().getPercentFee())).append(" % ").append(" , ")
                            .append(!Util.isNull(view.getExtensionFee()) ? Util.convertNullToZERO(view.getExtensionFee().getFeeYear()) : minus);
                    proposeFeeInformationDecisionReport.setExtensionFee(extension.toString());
                } else {
                    proposeFeeInformationDecisionReport.setExtensionFee("0.00%, - ");
                }

                if (!Util.isNull(view.getPrepaymentFee())){
                    StringBuilder prepayment = new StringBuilder();

                    prepayment = prepayment.append(Util.convertNullToZERO(view.getPrepaymentFee().getPercentFee())).append(" % ").append(" , ")
                            .append(!Util.isNull(view.getPrepaymentFee()) ?
                            Util.convertNullToZERO(view.getPrepaymentFee().getFeeYear()) : minus);
                    proposeFeeInformationDecisionReport.setPrepaymentFee(prepayment.toString());
                } else {
                    proposeFeeInformationDecisionReport.setPrepaymentFee("0.00%, - ");
                }

                if (!Util.isNull(view.getCancellationFee())) {
                    StringBuilder cancellation = new StringBuilder();

                    cancellation = cancellation.append(Util.convertNullToZERO(view.getCancellationFee().getPercentFee())).append(" % ").append(" , ")
                            .append(!Util.isNull(view.getCancellationFee()) ? Util.convertNullToZERO(view.getCancellationFee().getFeeYear()) : minus);
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

    //TODO
    public List<ProposedCollateralDecisionReport> fillProposedCollateral(String pathsub){
        List<ProposedCollateralDecisionReport> proposedCollateralDecisionReportList = new ArrayList<ProposedCollateralDecisionReport>();
        List<ProposeCollateralInfoView> newCollateralViews = decisionView.getProposeCollateralList();

        if (Util.isSafetyList(newCollateralViews)){
            log.debug("newCollateralViews.size() {}",newCollateralViews.size());
            for (ProposeCollateralInfoView view : newCollateralViews){
                ProposedCollateralDecisionReport collateralDecisionReport = new ProposedCollateralDecisionReport();
                List<ProposeCollateralInfoHeadView> collateralHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();
                List<ProposeCreditInfoDetailReport> proposeCreditInfoDetailReportList = new ArrayList<ProposeCreditInfoDetailReport>();
                List<ProposeCollateralInfoSubReport> proposeCollateralInfoSubReportList = new ArrayList<ProposeCollateralInfoSubReport>();

                collateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                collateralDecisionReport.setPath(pathsub);
                collateralDecisionReport.setAppraisalDate(DateTimeUtil.getCurrentDateTH(view.getAppraisalDate()));
                collateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecisionLabel()));
                collateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                collateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));
                collateralDecisionReport.setUsage(Util.checkNullString(view.getUsage()));

                if(view.getUsage()!=null && !view.getUsage().trim().equalsIgnoreCase("")){
                    Usages aadDecision = usagesDAO.getByCode(view.getUsage());
                    collateralDecisionReport.setUsage(aadDecision.getDescription());
                } else {
                    collateralDecisionReport.setUsage("-");
                }

                collateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                collateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));
                collateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));

                if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())){
                    log.debug("getProposeCreditDetailViewList by fillProposedCollateral. {}",view.getProposeCreditInfoDetailViewList().size());
                    int count = 1;

                    for (ProposeCreditInfoDetailView detailView : view.getProposeCreditInfoDetailViewList()){
                        ProposeCreditInfoDetailReport proposeCreditInfoDetailReport = new ProposeCreditInfoDetailReport();
                        StringBuilder account = new StringBuilder();

                        proposeCreditInfoDetailReport.setNo(count++);

                        account = account.append(Util.checkNullString(detailView.getAccountName())).append(enter);
                        account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber())).append(enter);
                        account = account.append("Suf.: ").append(Util.checkNullString(detailView.getAccountSuf())).append(enter);

                        proposeCreditInfoDetailReport.setAccount(account.toString());

                        if (detailView.getRequestType() == 1){
                            proposeCreditInfoDetailReport.setRequsestType("Change");
                        } else if (detailView.getRequestType() == 2){
                            proposeCreditInfoDetailReport.setRequsestType("New");
                        } else {
                            proposeCreditInfoDetailReport.setRequsestType(minus);
                        }

                        if (!Util.isNull(detailView.getProductProgramView())){
                            proposeCreditInfoDetailReport.setProductProgramViewName(Util.checkNullString(detailView.getProductProgramView().getName()));
                        } else {
                            proposeCreditInfoDetailReport.setProductProgramViewName(minus);
                        }

                        if (!Util.isNull(detailView.getCreditTypeView())){
                            proposeCreditInfoDetailReport.setCreditTypeViewName(Util.checkNullString(detailView.getCreditTypeView().getName()));
                        } else {
                            proposeCreditInfoDetailReport.setCreditTypeViewName(minus);
                        }

                        proposeCreditInfoDetailReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                        proposeCreditInfoDetailReportList.add(proposeCreditInfoDetailReport);
                    }
                }
                collateralDecisionReport.setProposeCreditInfoDetailReports(proposeCreditInfoDetailReportList);

                collateralHeadViewList = view.getProposeCollateralInfoHeadViewList();
                if (Util.isSafetyList(collateralHeadViewList)){
                    log.debug("collateralHeadViewList.size() {}",collateralHeadViewList.size());
                    for (ProposeCollateralInfoHeadView headView : collateralHeadViewList){
                        collateralDecisionReport.setCollateralDescription(!Util.isNull(headView.getPotentialCollateral()) ?
                                (Util.checkNullString(headView.getPotentialCollateral().getDescription())) : minus);
                        collateralDecisionReport.setPercentLTVDescription(!Util.isNull(headView.getTcgCollateralType()) ?
                                (Util.checkNullString(headView.getTcgCollateralType().getDescription())): minus);
                        collateralDecisionReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                        collateralDecisionReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                        collateralDecisionReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                        collateralDecisionReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                        collateralDecisionReport.setHeadCollTypeDescription(!Util.isNull(headView.getHeadCollType()) ?
                                Util.checkNullString(headView.getHeadCollType().getDescription()) : minus);
                        if (!Util.isNull(headView.getInsuranceCompany())){
                            if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                                collateralDecisionReport.setInsuranceCompany("Partner");
                            } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                                collateralDecisionReport.setInsuranceCompany("Non Partner");
                            } else {
                                collateralDecisionReport.setInsuranceCompany(minus);
                            }
                        }

                        if (Util.isSafetyList(headView.getProposeCollateralInfoSubViewList())){
                            int count = 1;

                            for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : headView.getProposeCollateralInfoSubViewList()){
                                ProposeCollateralInfoSubReport proposeCollateralInfoSubReport = new ProposeCollateralInfoSubReport();

                                proposeCollateralInfoSubReport.setPath(pathsub);
                                proposeCollateralInfoSubReport.setNo(count++);

                                StringBuilder subCollType = new StringBuilder();

                                if (!Util.isNull(proposeCollateralInfoSubView.getSubCollateralType())){
                                    subCollType = subCollType.append(Util.checkNullString(proposeCollateralInfoSubView.getSubCollateralType().getDescription())).append(enter);
                                } else {
                                    subCollType = subCollType.append(minus).append(enter);
                                }

                                subCollType = subCollType.append("Address : ").append(Util.checkNullString(proposeCollateralInfoSubView.getAddress())).append(enter);
                                subCollType = subCollType.append("Type of Usage : ").append(Util.checkNullString(proposeCollateralInfoSubView.getTypeOfUsage())).append(enter);
                                subCollType = subCollType.append("Land Office : ").append(Util.checkNullString(proposeCollateralInfoSubView.getLandOffice())).append(enter);

                                proposeCollateralInfoSubReport.setDeceptionSubCollType(subCollType.toString());
                                proposeCollateralInfoSubReport.setTitleDeed(Util.checkNullString(proposeCollateralInfoSubView.getTitleDeed()));
                                proposeCollateralInfoSubReport.setCollateralOwnerAAD(Util.checkNullString(proposeCollateralInfoSubView.getCollateralOwnerAAD()));
                                proposeCollateralInfoSubReport.setAppraisalValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getAppraisalValue()));
                                proposeCollateralInfoSubReport.setMortgageValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getMortgageValue()));

                                if (Util.isSafetyList(proposeCollateralInfoSubView.getCollateralOwnerUWList())){
                                    StringBuilder collateralIwner = new StringBuilder();
                                    for (CustomerInfoView customerInfoView : proposeCollateralInfoSubView.getCollateralOwnerUWList()){
                                        if (!Util.isNull(customerInfoView.getTitleTh())){
                                            collateralIwner = collateralIwner.append(Util.checkNullString(customerInfoView.getTitleTh().getTitleTh()));
                                        }

                                        if (!Util.isNull(customerInfoView.getFirstNameTh())){
                                            collateralIwner = collateralIwner.append(Util.checkNullString(customerInfoView.getFirstNameTh())).append(SPACE);
                                        }

                                        if (!Util.isNull(customerInfoView.getLastNameTh())){
                                            collateralIwner = collateralIwner.append(Util.checkNullString(customerInfoView.getLastNameTh())).append(enter);
                                        } else {
                                            collateralIwner = collateralIwner.append(enter);
                                        }
                                        proposeCollateralInfoSubReport.setCollateralOwner(collateralIwner.toString());
                                    }
                                } else {
                                    proposeCollateralInfoSubReport.setCollateralOwner(minus);
                                }

                                if (Util.isSafetyList(proposeCollateralInfoSubView.getMortgageList())){
                                    StringBuilder mortgageType = new StringBuilder();
                                    for (MortgageTypeView mortgageTypeView : proposeCollateralInfoSubView.getMortgageList()){
                                        mortgageType = mortgageType.append(Util.checkNullString(mortgageTypeView.getMortgage())).append(enter);
                                        proposeCollateralInfoSubReport.setMortgage(mortgageType.toString());
                                    }
                                } else {
                                    proposeCollateralInfoSubReport.setMortgage(minus);
                                }

                                if (Util.isSafetyList(proposeCollateralInfoSubView.getRelatedWithList())){
                                    StringBuilder relatedWith = new StringBuilder();
                                    for (ProposeCollateralInfoSubView relatedWithView : proposeCollateralInfoSubView.getRelatedWithList()){
                                        relatedWith = relatedWith.append(Util.checkNullString(relatedWithView.getTitleDeed())).append(enter);
                                        proposeCollateralInfoSubReport.setRelatedWith(relatedWith.toString());
                                    }
                                } else {
                                    proposeCollateralInfoSubReport.setRelatedWith(minus);
                                }
                                proposeCollateralInfoSubReportList.add(proposeCollateralInfoSubReport);
                            }
                        }
                        collateralDecisionReport.setProposeCollateralInfoSubReports(proposeCollateralInfoSubReportList);
                    }
                } else {
                    log.debug("collateralHeadViewList is Null. {}");
                }
                proposedCollateralDecisionReportList.add(collateralDecisionReport);
            }
        }
        return proposedCollateralDecisionReportList;
    }

    //ExSum       //TODO
    public List<ApprovedCollateralDecisionReport> fillExSumApprovedCollaterral(final String pathsub){
        List<ApprovedCollateralDecisionReport> approvedCollateralDecisionReportArrayList = new ArrayList<ApprovedCollateralDecisionReport>();
        List<ProposeCollateralInfoView> newCollateralViews = decisionView.getApproveCollateralList();

        if (Util.isSafetyList(newCollateralViews)){
            log.debug("newCollateralViews.size() {}",newCollateralViews.size());
            for (ProposeCollateralInfoView view : newCollateralViews){
                ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
                List<ProposeCollateralInfoHeadView> collateralHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();
                List<ProposeCreditInfoDetailReport> proposeCreditInfoDetailReportList = new ArrayList<ProposeCreditInfoDetailReport>();
                List<ProposeCollatealInfoHeadSubReport> proposeCollatealInfoHeadSubReports = new ArrayList<ProposeCollatealInfoHeadSubReport>();

                approvedCollateralDecisionReport.setPath(pathsub);
                approvedCollateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                approvedCollateralDecisionReport.setAppraisalDate(DateTimeUtil.getCurrentDateTH(view.getAppraisalDate()));
                approvedCollateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecisionLabel()));
                approvedCollateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                approvedCollateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));

                if(view.getUsage()!=null && !view.getUsage().trim().equalsIgnoreCase("")){
                    Usages aadDecision = usagesDAO.getByCode(view.getUsage());
                    approvedCollateralDecisionReport.setUsage(aadDecision.getDescription());
                } else {
                    approvedCollateralDecisionReport.setUsage("-");
                }

                approvedCollateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));

                if (!Util.isNull(view.getUwDecision())){
                    if((DecisionType.APPROVED ).equals(view.getUwDecision())){
                        approvedCollateralDecisionReport.setApproved("Yes");
                    } else if ((DecisionType.REJECTED).equals(view.getUwDecision())){
                        approvedCollateralDecisionReport.setApproved("No");
                    } else {
                        approvedCollateralDecisionReport.setApproved(minus);
                    }
                } else {
                    approvedCollateralDecisionReport.setApproved(minus);
                }

                approvedCollateralDecisionReport.setUwRemark(Util.checkNullString(view.getUwRemark()));
                approvedCollateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                approvedCollateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));

                if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())) {
                    log.debug("getProposeCreditDetailViewList by fillExSumApprovedCollaterral. {}",view.getProposeCreditInfoDetailViewList().size());

                    int count = 1;
                    for (ProposeCreditInfoDetailView detailView : view.getProposeCreditInfoDetailViewList()){
                        ProposeCreditInfoDetailReport proposeCreditInfoDetailReport = new ProposeCreditInfoDetailReport();
                        StringBuilder account = new StringBuilder();

                        proposeCreditInfoDetailReport.setNo(count++);

                        account = account.append(Util.checkNullString(detailView.getAccountName())).append(enter);
                        account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber())).append(enter);
                        account = account.append("Suf.: ").append(Util.checkNullString(detailView.getAccountSuf())).append(enter);

                        proposeCreditInfoDetailReport.setAccount(account.toString());

                        if (detailView.getRequestType() == 1){
                            proposeCreditInfoDetailReport.setRequsestType("Change");
                        } else if (detailView.getRequestType() == 2){
                            proposeCreditInfoDetailReport.setRequsestType("New");
                        } else {
                            proposeCreditInfoDetailReport.setRequsestType(minus);
                        }

                        if (!Util.isNull(detailView.getProductProgramView())){
                            proposeCreditInfoDetailReport.setProductProgramViewName(Util.checkNullString(detailView.getProductProgramView().getName()));
                        } else {
                            proposeCreditInfoDetailReport.setProductProgramViewName(minus);
                        }

                        if (!Util.isNull(detailView.getCreditTypeView())){
                            proposeCreditInfoDetailReport.setCreditTypeViewName(Util.checkNullString(detailView.getCreditTypeView().getName()));
                        } else {
                            proposeCreditInfoDetailReport.setCreditTypeViewName(minus);
                        }

                        proposeCreditInfoDetailReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                        proposeCreditInfoDetailReportList.add(proposeCreditInfoDetailReport);
                    }
                }
                approvedCollateralDecisionReport.setProposeCreditInfoDetailReports(proposeCreditInfoDetailReportList);

                collateralHeadViewList = view.getProposeCollateralInfoHeadViewList();
                if (Util.isSafetyList(collateralHeadViewList)){
                    log.debug("collateralHeadViewList. {}",collateralHeadViewList.size());
                    for (ProposeCollateralInfoHeadView headView : collateralHeadViewList){
                        List<ProposeCollateralInfoSubReport> proposeCollateralInfoSubReportList = new ArrayList<ProposeCollateralInfoSubReport>();
                        ProposeCollatealInfoHeadSubReport proposeCollatealInfoHeadSubReport = new ProposeCollatealInfoHeadSubReport();

                        proposeCollatealInfoHeadSubReport.setPath(pathsub);
                        proposeCollatealInfoHeadSubReport.setCollateralDescription(!Util.isNull(headView.getPotentialCollateral()) ?
                                Util.checkNullString(headView.getPotentialCollateral().getDescription()) : minus);
                        proposeCollatealInfoHeadSubReport.setPercentLTVDescription(!Util.isNull(headView.getTcgCollateralType()) ?
                                Util.checkNullString(headView.getTcgCollateralType().getDescription()) : minus);
                        proposeCollatealInfoHeadSubReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                        proposeCollatealInfoHeadSubReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                        proposeCollatealInfoHeadSubReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                        proposeCollatealInfoHeadSubReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                        proposeCollatealInfoHeadSubReport.setHeadCollTypeDescription(!Util.isNull(headView.getHeadCollType()) ?
                                Util.checkNullString(headView.getHeadCollType().getDescription()) : minus);
                        if (!Util.isNull(headView.getInsuranceCompany())){
                            if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                                proposeCollatealInfoHeadSubReport.setInsuranceCompany("Partner");
                            } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                                proposeCollatealInfoHeadSubReport.setInsuranceCompany("Non Partner");
                            } else {
                                proposeCollatealInfoHeadSubReport.setInsuranceCompany(minus);
                            }
                        }

                        if (Util.isSafetyList(headView.getProposeCollateralInfoSubViewList())){
                            int count = 1;

                            for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : headView.getProposeCollateralInfoSubViewList()){
                                ProposeCollateralInfoSubReport proposeCollateralInfoSubReport = new ProposeCollateralInfoSubReport();
                                proposeCollateralInfoSubReport.setPath(pathsub);
                                proposeCollateralInfoSubReport.setNo(count++);

                                StringBuilder subCollType = new StringBuilder();
                                if (!Util.isNull(proposeCollateralInfoSubView.getSubCollateralType())){
                                    subCollType = subCollType.append(Util.checkNullString(proposeCollateralInfoSubView.getSubCollateralType().getDescription())).append(enter);
                                } else {
                                    subCollType = subCollType.append(minus).append(enter);
                                }

                                subCollType = subCollType.append("Address : ").append(Util.checkNullString(proposeCollateralInfoSubView.getAddress())).append(enter);
                                subCollType = subCollType.append("Type of Usage : ").append(Util.checkNullString(proposeCollateralInfoSubView.getTypeOfUsage())).append(enter);
                                subCollType = subCollType.append("Land Office : ").append(Util.checkNullString(proposeCollateralInfoSubView.getLandOffice())).append(enter);

                                proposeCollateralInfoSubReport.setDeceptionSubCollType(subCollType.toString());
                                proposeCollateralInfoSubReport.setTitleDeed(Util.checkNullString(proposeCollateralInfoSubView.getTitleDeed()));
                                proposeCollateralInfoSubReport.setCollateralOwnerAAD(Util.checkNullString(proposeCollateralInfoSubView.getCollateralOwnerAAD()));
                                proposeCollateralInfoSubReport.setAppraisalValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getAppraisalValue()));
                                proposeCollateralInfoSubReport.setMortgageValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getMortgageValue()));

                                if (Util.isSafetyList(proposeCollateralInfoSubView.getCollateralOwnerUWList())){
                                    StringBuilder collateralIwner = new StringBuilder();
                                    for (CustomerInfoView customerInfoView : proposeCollateralInfoSubView.getCollateralOwnerUWList()){
                                        if (!Util.isNull(customerInfoView.getTitleTh())){
                                            collateralIwner = collateralIwner.append(Util.checkNullString(customerInfoView.getTitleTh().getTitleTh()));
                                        }

                                        if (!Util.isNull(customerInfoView.getFirstNameTh())){
                                            collateralIwner = collateralIwner.append(Util.checkNullString(customerInfoView.getFirstNameTh())).append(SPACE);
                                        }

                                        if (!Util.isNull(customerInfoView.getLastNameTh())){
                                            collateralIwner = collateralIwner.append(Util.checkNullString(customerInfoView.getLastNameTh())).append(enter);
                                        } else {
                                            collateralIwner = collateralIwner.append(enter);
                                        }
                                        proposeCollateralInfoSubReport.setCollateralOwner(collateralIwner.toString());
                                    }
                                } else {
                                    proposeCollateralInfoSubReport.setCollateralOwner(minus);
                                }

                                if (Util.isSafetyList(proposeCollateralInfoSubView.getMortgageList())){
                                    StringBuilder mortgageType = new StringBuilder();
                                    for (MortgageTypeView mortgageTypeView : proposeCollateralInfoSubView.getMortgageList()){
                                        mortgageType = mortgageType.append(Util.checkNullString(mortgageTypeView.getMortgage())).append(enter);
                                        proposeCollateralInfoSubReport.setMortgage(mortgageType.toString());
                                    }
                                } else {
                                    proposeCollateralInfoSubReport.setMortgage(minus);
                                }

                                if (Util.isSafetyList(proposeCollateralInfoSubView.getRelatedWithList())){
                                    StringBuilder relatedWith = new StringBuilder();
                                    for (ProposeCollateralInfoSubView relatedWithView : proposeCollateralInfoSubView.getRelatedWithList()){
                                        relatedWith = relatedWith.append(Util.checkNullString(relatedWithView.getTitleDeed())).append(enter);
                                        proposeCollateralInfoSubReport.setRelatedWith(relatedWith.toString());
                                    }
                                } else {
                                    proposeCollateralInfoSubReport.setRelatedWith(minus);
                                }
                                proposeCollateralInfoSubReportList.add(proposeCollateralInfoSubReport);
                            }
                        }
                        proposeCollatealInfoHeadSubReport.setProposeCollateralInfoSubReports(proposeCollateralInfoSubReportList);
                        proposeCollatealInfoHeadSubReports.add(proposeCollatealInfoHeadSubReport);
                    }
                    approvedCollateralDecisionReport.setProposeCollatealInfoHeadSubReports(proposeCollatealInfoHeadSubReports);
                } else {
                    log.debug("collateralHeadViewList is Null. {}");
                }
                approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
            }
        }
        return approvedCollateralDecisionReportArrayList;
    }

    //Opsheet
    public List<ApprovedCollateralDecisionReport> fillApprovedCollaterral(final String pathsub){
        List<ApprovedCollateralDecisionReport> approvedCollateralDecisionReportArrayList = new ArrayList<ApprovedCollateralDecisionReport>();
        List<ProposeCollateralInfoView> newCollateralViews = decisionView.getApproveCollateralList();

        if (Util.isSafetyList(newCollateralViews)){
            log.debug("newCollateralViews.size() by fillApprovedCollaterral. {}",newCollateralViews.size());
            for (ProposeCollateralInfoView view : newCollateralViews){
                ApprovedCollateralDecisionReport approvedCollateralDecisionReport = new ApprovedCollateralDecisionReport();
                List<ProposeCollateralInfoHeadView> collateralHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();
                List<ProposeCreditInfoDetailReport> proposeCreditInfoDetailReportList = new ArrayList<ProposeCreditInfoDetailReport>();
                List<ProposeCollatealInfoHeadSubReport> proposeCollatealInfoHeadSubReports = new ArrayList<ProposeCollatealInfoHeadSubReport>();

                    if((DecisionType.APPROVED).equals(view.getUwDecision())){
                        approvedCollateralDecisionReport.setPath(pathsub);
                        approvedCollateralDecisionReport.setJobID(Util.checkNullString(view.getJobID()));
                        approvedCollateralDecisionReport.setAppraisalDate(DateTimeUtil.getCurrentDateTH(view.getAppraisalDate()));
                        approvedCollateralDecisionReport.setAadDecision(Util.checkNullString(view.getAadDecisionLabel()));
                        approvedCollateralDecisionReport.setAadDecisionReason(Util.checkNullString(view.getAadDecisionReason()));
                        approvedCollateralDecisionReport.setAadDecisionReasonDetail(Util.checkNullString(view.getAadDecisionReasonDetail()));

                        if(view.getUsage()!=null && !view.getUsage().trim().equalsIgnoreCase("")){
                            Usages aadDecision = usagesDAO.getByCode(view.getUsage());
                            approvedCollateralDecisionReport.setUsage(aadDecision.getDescription());
                        } else {
                            approvedCollateralDecisionReport.setUsage("-");
                        }

                        approvedCollateralDecisionReport.setBdmComments(Util.checkNullString(view.getBdmComments()));
                        approvedCollateralDecisionReport.setApproved("Yes");
                        approvedCollateralDecisionReport.setUwRemark(Util.checkNullString(view.getUwRemark()));
                        approvedCollateralDecisionReport.setMortgageCondition(Util.checkNullString(view.getMortgageCondition()));
                        approvedCollateralDecisionReport.setMortgageConditionDetail(Util.checkNullString(view.getMortgageConditionDetail()));


                        if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())){
                            log.debug("getProposeCreditDetailViewList by fillApprovedCollaterral. {}",view.getProposeCreditInfoDetailViewList().size());

                            int count = 1;
                            for (ProposeCreditInfoDetailView detailView : view.getProposeCreditInfoDetailViewList()){
                                ProposeCreditInfoDetailReport proposeCreditInfoDetailReport = new ProposeCreditInfoDetailReport();
                                StringBuilder account = new StringBuilder();

                                proposeCreditInfoDetailReport.setNo(count++);
                                account = account.append(Util.checkNullString(detailView.getAccountName())).append(enter);
                                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber())).append(enter);
                                account = account.append("Suf.: ").append(Util.checkNullString(detailView.getAccountSuf())).append(enter);

                                proposeCreditInfoDetailReport.setAccount(account.toString());

                                if (detailView.getRequestType() == 1){
                                    proposeCreditInfoDetailReport.setRequsestType("Change");
                                } else if (detailView.getRequestType() == 2){
                                    proposeCreditInfoDetailReport.setRequsestType("New");
                                } else {
                                    proposeCreditInfoDetailReport.setRequsestType(minus);
                                }

                                if (!Util.isNull(detailView.getProductProgramView())){
                                    proposeCreditInfoDetailReport.setProductProgramViewName(Util.checkNullString(detailView.getProductProgramView().getName()));
                                } else {
                                    proposeCreditInfoDetailReport.setProductProgramViewName(minus);
                                }

                                if (!Util.isNull(detailView.getCreditTypeView())){
                                    proposeCreditInfoDetailReport.setCreditTypeViewName(Util.checkNullString(detailView.getCreditTypeView().getName()));
                                } else {
                                    proposeCreditInfoDetailReport.setCreditTypeViewName(minus);
                                }

                                proposeCreditInfoDetailReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));
                                proposeCreditInfoDetailReportList.add(proposeCreditInfoDetailReport);
                            }
                        }
                        approvedCollateralDecisionReport.setProposeCreditInfoDetailReports(proposeCreditInfoDetailReportList);

                        collateralHeadViewList = view.getProposeCollateralInfoHeadViewList();
                        if (Util.isSafetyList(collateralHeadViewList)){
                            log.debug("collateralHeadViewList.size() {}",collateralHeadViewList.size());
                            for (ProposeCollateralInfoHeadView headView : collateralHeadViewList){
                                List<ProposeCollateralInfoSubReport> proposeCollateralInfoSubReportList = new ArrayList<ProposeCollateralInfoSubReport>();
                                ProposeCollatealInfoHeadSubReport proposeCollatealInfoHeadSubReport = new ProposeCollatealInfoHeadSubReport();

                                proposeCollatealInfoHeadSubReport.setPath(pathsub);
                                proposeCollatealInfoHeadSubReport.setCollateralDescription(!Util.isNull(headView.getPotentialCollateral()) ?
                                        Util.checkNullString(headView.getPotentialCollateral().getDescription()) : minus);
                                proposeCollatealInfoHeadSubReport.setPercentLTVDescription(!Util.isNull(headView.getTcgCollateralType()) ?
                                        Util.checkNullString(headView.getTcgCollateralType().getDescription()) : minus);
                                proposeCollatealInfoHeadSubReport.setExistingCredit(Util.convertNullToZERO(headView.getExistingCredit()));
                                proposeCollatealInfoHeadSubReport.setTitleDeed(Util.checkNullString(headView.getTitleDeed()));
                                proposeCollatealInfoHeadSubReport.setCollateralLocation(Util.checkNullString(headView.getCollateralLocation()));
                                proposeCollatealInfoHeadSubReport.setAppraisalValue(Util.convertNullToZERO(headView.getAppraisalValue()));
                                proposeCollatealInfoHeadSubReport.setHeadCollTypeDescription(!Util.isNull(headView.getHeadCollType()) ?
                                        Util.checkNullString(headView.getHeadCollType().getDescription()) : minus);
                                if (!Util.isNull(headView.getInsuranceCompany())){
                                    if (headView.getInsuranceCompany() == RadioValue.YES.value()){
                                        proposeCollatealInfoHeadSubReport.setInsuranceCompany("Partner");
                                    } else if (headView.getInsuranceCompany() == RadioValue.NO.value()){
                                        proposeCollatealInfoHeadSubReport.setInsuranceCompany("Non Partner");
                                    } else {
                                        proposeCollatealInfoHeadSubReport.setInsuranceCompany(minus);
                                    }
                                }

                                if (Util.isSafetyList(headView.getProposeCollateralInfoSubViewList())){
                                    int count = 1;

                                    for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : headView.getProposeCollateralInfoSubViewList()){
                                        ProposeCollateralInfoSubReport proposeCollateralInfoSubReport = new ProposeCollateralInfoSubReport();
                                        proposeCollateralInfoSubReport.setPath(pathsub);
                                        proposeCollateralInfoSubReport.setNo(count++);

                                        StringBuilder subCollType = new StringBuilder();

                                        if (!Util.isNull(proposeCollateralInfoSubView.getSubCollateralType())){
                                            subCollType = subCollType.append(Util.checkNullString(proposeCollateralInfoSubView.getSubCollateralType().getDescription())).append(enter);
                                        } else {
                                            subCollType = subCollType.append(minus).append(enter);
                                        }

                                        subCollType = subCollType.append("Address : ").append(Util.checkNullString(proposeCollateralInfoSubView.getAddress())).append(enter);
                                        subCollType = subCollType.append("Type of Usage : ").append(Util.checkNullString(proposeCollateralInfoSubView.getTypeOfUsage())).append(enter);
                                        subCollType = subCollType.append("Land Office : ").append(Util.checkNullString(proposeCollateralInfoSubView.getLandOffice())).append(enter);

                                        proposeCollateralInfoSubReport.setDeceptionSubCollType(subCollType.toString());
                                        proposeCollateralInfoSubReport.setTitleDeed(Util.checkNullString(proposeCollateralInfoSubView.getTitleDeed()));
                                        proposeCollateralInfoSubReport.setCollateralOwnerAAD(Util.checkNullString(proposeCollateralInfoSubView.getCollateralOwnerAAD()));
                                        proposeCollateralInfoSubReport.setAppraisalValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getAppraisalValue()));
                                        proposeCollateralInfoSubReport.setMortgageValue(Util.convertNullToZERO(proposeCollateralInfoSubView.getMortgageValue()));

                                        if (Util.isSafetyList(proposeCollateralInfoSubView.getCollateralOwnerUWList())){
                                            StringBuilder collateralIwner = new StringBuilder();
                                            for (CustomerInfoView customerInfoView : proposeCollateralInfoSubView.getCollateralOwnerUWList()){
                                                if (!Util.isNull(customerInfoView.getTitleTh())){
                                                    collateralIwner = collateralIwner.append(Util.checkNullString(customerInfoView.getTitleTh().getTitleTh()));
                                                }
                                                if (!Util.isNull(customerInfoView.getFirstNameTh())){
                                                    collateralIwner = collateralIwner.append(Util.checkNullString(customerInfoView.getFirstNameTh())).append(SPACE);
                                                }

                                                if (!Util.isNull(customerInfoView.getLastNameTh())){
                                                    collateralIwner = collateralIwner.append(Util.checkNullString(customerInfoView.getLastNameTh())).append(enter);
                                                } else {
                                                    collateralIwner = collateralIwner.append(enter);
                                                }
                                                proposeCollateralInfoSubReport.setCollateralOwner(collateralIwner.toString());
                                            }
                                        } else {
                                            proposeCollateralInfoSubReport.setCollateralOwner(minus);
                                        }

                                        if (Util.isSafetyList(proposeCollateralInfoSubView.getMortgageList())){
                                            StringBuilder mortgageType = new StringBuilder();
                                            for (MortgageTypeView mortgageTypeView : proposeCollateralInfoSubView.getMortgageList()){
                                                mortgageType = mortgageType.append(Util.checkNullString(mortgageTypeView.getMortgage())).append(enter);
                                                proposeCollateralInfoSubReport.setMortgage(mortgageType.toString());
                                            }
                                        } else {
                                            proposeCollateralInfoSubReport.setMortgage(minus);
                                        }

                                        if (Util.isSafetyList(proposeCollateralInfoSubView.getRelatedWithList())){
                                            StringBuilder relatedWith = new StringBuilder();
                                            for (ProposeCollateralInfoSubView relatedWithView : proposeCollateralInfoSubView.getRelatedWithList()){
                                                relatedWith = relatedWith.append(Util.checkNullString(relatedWithView.getTitleDeed())).append(enter);
                                                proposeCollateralInfoSubReport.setRelatedWith(relatedWith.toString());
                                            }
                                        } else {
                                            proposeCollateralInfoSubReport.setRelatedWith(minus);
                                        }
                                        proposeCollateralInfoSubReportList.add(proposeCollateralInfoSubReport);
                                        proposeCollatealInfoHeadSubReport.setProposeCollateralInfoSubReports(proposeCollateralInfoSubReportList);
                                    }
                                }
                                proposeCollatealInfoHeadSubReports.add(proposeCollatealInfoHeadSubReport);
                                approvedCollateralDecisionReport.setProposeCollatealInfoHeadSubReports(proposeCollatealInfoHeadSubReports);
                            }
                        } else {
                            log.debug("collateralHeadViewList is Null. {}");
                        }
                        approvedCollateralDecisionReportArrayList.add(approvedCollateralDecisionReport);
                    }
            }
        }
        return approvedCollateralDecisionReportArrayList;
    }

    public List<ProposedGuarantorDecisionReport> fillProposedGuarantor(String pathsub){
        List<ProposedGuarantorDecisionReport> proposedGuarantorDecisionReportList = new ArrayList<ProposedGuarantorDecisionReport>();
        List<ProposeGuarantorInfoView> detailViews = decisionView.getProposeGuarantorList();

        int count = 1;
        if (Util.isSafetyList(detailViews)){
            log.debug("detailViews.size() {}",detailViews.size());
            for (ProposeGuarantorInfoView view : detailViews){
                ProposedGuarantorDecisionReport guarantorDecisionReport = new ProposedGuarantorDecisionReport();
                List<ProposeCreditInfoDetailReport> proposeCreditInfoDetailReportList = new ArrayList<ProposeCreditInfoDetailReport>();

                guarantorDecisionReport.setCount(count++);
                guarantorDecisionReport.setPath(pathsub);

                StringBuilder name = new StringBuilder();

                if (!Util.isNull(view.getGuarantorName())){
                    if (!Util.isNull(view.getGuarantorName().getTitleTh())){
                        name = name.append(Util.checkNullString(view.getGuarantorName().getTitleTh().getTitleTh())).append(SPACE);
                    }
                    name = name.append(Util.checkNullString(view.getGuarantorName().getFirstNameTh())).append(SPACE);
                    name = name.append(Util.checkNullString(view.getGuarantorName().getLastNameTh()));
                } else {
                    name = name.append(minus);
                }

                guarantorDecisionReport.setName(name.toString());
                guarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));

                if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())){
                    log.debug("getProposeCreditDetailViewList by fillProposedGuarantor. {}",view.getProposeCreditInfoDetailViewList().size());

                    for (ProposeCreditInfoDetailView detailView : view.getProposeCreditInfoDetailViewList()){
                        ProposeCreditInfoDetailReport proposeCreditInfoDetailReport = new ProposeCreditInfoDetailReport();
                        StringBuilder account = new StringBuilder();
                        account = account.append(Util.checkNullString(detailView.getAccountName())).append(enter);
                        account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber())).append(enter);
                        account = account.append("Suf.: ").append(Util.checkNullString(detailView.getAccountSuf())).append(enter);

                        proposeCreditInfoDetailReport.setAccount(account.toString());

                        StringBuilder prodAndCreditType = new StringBuilder();

                        if (!Util.isNull(detailView.getProductProgramView())){
                            prodAndCreditType = prodAndCreditType.append(Util.checkNullString(detailView.getProductProgramView().getName())).append(enter);
                        } else {
                            prodAndCreditType = prodAndCreditType.append(minus).append(enter);
                        }

                        if (!Util.isNull(detailView.getCreditTypeView())){
                            prodAndCreditType = prodAndCreditType.append(Util.checkNullString(detailView.getCreditTypeView().getName()));
                        } else {
                            prodAndCreditType = prodAndCreditType.append(minus);
                        }

                        proposeCreditInfoDetailReport.setProductProgramAndCreditType(prodAndCreditType.toString());
                        proposeCreditInfoDetailReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));

                        StringBuilder guaranteeAmount = new StringBuilder();
                        guaranteeAmount = guaranteeAmount.append("Guarantee Amount: ").append(Util.formatNumber(Util.convertNullToZERO(detailView.getGuaranteeAmount())));

                        proposeCreditInfoDetailReport.setGuaranteeAmount(guaranteeAmount.toString());
                        proposeCreditInfoDetailReportList.add(proposeCreditInfoDetailReport);
                    }
                }
                guarantorDecisionReport.setProposeCreditInfoDetailReports(proposeCreditInfoDetailReportList);
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
                List<ProposeCreditInfoDetailReport> proposeCreditInfoDetailReportList = new ArrayList<ProposeCreditInfoDetailReport>();

                approvedGuarantorDecisionReport.setPath(pathsub);
                approvedGuarantorDecisionReport.setCount(count++);

                StringBuilder name = new StringBuilder();

                if (!Util.isNull(view.getGuarantorName())){
                    if (!Util.isNull(view.getGuarantorName().getTitleTh())){
                        name = name.append(Util.checkNullString(view.getGuarantorName().getTitleTh().getTitleTh())).append(SPACE);
                    }
                    name = name.append(Util.checkNullString(view.getGuarantorName().getFirstNameTh())).append(SPACE);
                    name = name.append(Util.checkNullString(view.getGuarantorName().getLastNameTh()));
                } else {
                    name = name.append(minus);
                }

                approvedGuarantorDecisionReport.setName(name.toString());
                approvedGuarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));
                if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())){
                    log.debug("getProposeCreditDetailViewList by fillExSumApprovedGuarantor. {}",view.getProposeCreditInfoDetailViewList().size());

                    for (ProposeCreditInfoDetailView detailView : view.getProposeCreditInfoDetailViewList()){
                        ProposeCreditInfoDetailReport proposeCreditInfoDetailReport = new ProposeCreditInfoDetailReport();
                        StringBuilder account = new StringBuilder();
                        account = account.append(Util.checkNullString(detailView.getAccountName())).append(enter);
                        account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber())).append(enter);
                        account = account.append("Suf.: ").append(Util.checkNullString(detailView.getAccountSuf())).append(enter);

                        proposeCreditInfoDetailReport.setAccount(account.toString());

                        StringBuilder prodAndCreditType = new StringBuilder();
                        if (!Util.isNull(detailView.getProductProgramView())){
                            prodAndCreditType = prodAndCreditType.append(Util.checkNullString(detailView.getProductProgramView().getName())).append(enter);
                        } else {
                            prodAndCreditType = prodAndCreditType.append(minus).append(enter);
                        }

                        if (!Util.isNull(detailView.getCreditTypeView())){
                            prodAndCreditType = prodAndCreditType.append(Util.checkNullString(detailView.getCreditTypeView().getName())).append(enter);
                        } else {
                            prodAndCreditType = prodAndCreditType.append(minus).append(enter);
                        }

                        proposeCreditInfoDetailReport.setProductProgramAndCreditType(prodAndCreditType.toString());
                        proposeCreditInfoDetailReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));

                        StringBuilder guaranteeAmount = new StringBuilder();
                        guaranteeAmount = guaranteeAmount.append("Guarantee Amount: ").append(Util.formatNumber(Util.convertNullToZERO(detailView.getGuaranteeAmount())));

                        proposeCreditInfoDetailReport.setGuaranteeAmount(guaranteeAmount.toString());
                        proposeCreditInfoDetailReportList.add(proposeCreditInfoDetailReport);
                    }
                }
                approvedGuarantorDecisionReport.setProposeCreditInfoDetailReports(proposeCreditInfoDetailReportList);
                approvedGuarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(view.getGuaranteeAmount()));

                if (!Util.isNull(view.getUwDecision())){
                    if ((DecisionType.APPROVED).equals(view.getUwDecision())){
                        approvedGuarantorDecisionReport.setUwDecision("Approved");
                    } else if ((DecisionType.REJECTED).equals(view.getUwDecision())){
                        approvedGuarantorDecisionReport.setUwDecision("Rejected");
                    } else {
                        approvedGuarantorDecisionReport.setUwDecision(minus);
                    }
                } else {
                    approvedGuarantorDecisionReport.setUwDecision(minus);
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
                List<ProposeCreditInfoDetailReport> proposeCreditInfoDetailReportList = new ArrayList<ProposeCreditInfoDetailReport>();

                approvedGuarantorDecisionReport.setPath(pathsub);
                if (!Util.isNull(view.getUwDecision())){
                    if ((DecisionType.APPROVED).equals(view.getUwDecision())) {
                        approvedGuarantorDecisionReport.setCount(count++);

                        StringBuilder name = new StringBuilder();

                        if (!Util.isNull(view.getGuarantorName())){
                            if (!Util.isNull(view.getGuarantorName().getTitleTh())){
                                name = name.append(Util.checkNullString(view.getGuarantorName().getTitleTh().getTitleTh())).append(SPACE);
                            }
                            name = name.append(Util.checkNullString(view.getGuarantorName().getFirstNameTh())).append(SPACE);
                            name = name.append(Util.checkNullString(view.getGuarantorName().getLastNameTh()));
                        } else {
                            name = name.append(minus);
                        }

                        approvedGuarantorDecisionReport.setName(name.toString());
                        approvedGuarantorDecisionReport.setTcgLgNo(Util.checkNullString(view.getTcgLgNo()));
                        if (Util.isSafetyList(view.getProposeCreditInfoDetailViewList())){
                            log.debug("getProposeCreditDetailViewList by fillApprovedGuarantor. {}",view.getProposeCreditInfoDetailViewList().size());

                            for (ProposeCreditInfoDetailView detailView : view.getProposeCreditInfoDetailViewList()){
                                ProposeCreditInfoDetailReport proposeCreditInfoDetailReport = new ProposeCreditInfoDetailReport();
                                StringBuilder account = new StringBuilder();
                                account = account.append(Util.checkNullString(detailView.getAccountName())).append(enter);
                                account = account.append("Acc No.: ").append(Util.checkNullString(detailView.getAccountNumber())).append(enter);
                                account = account.append("Suf.: ").append(Util.checkNullString(detailView.getAccountSuf())).append(enter);

                                proposeCreditInfoDetailReport.setAccount(account.toString());

                                StringBuilder prodAndCreditType = new StringBuilder();

                                if (!Util.isNull(detailView.getProductProgramView())){
                                    prodAndCreditType = prodAndCreditType.append(Util.checkNullString(detailView.getProductProgramView().getName())).append(enter);
                                } else {
                                    prodAndCreditType = prodAndCreditType.append(minus).append(enter);
                                }

                                if (!Util.isNull(detailView.getCreditTypeView())){
                                    prodAndCreditType = prodAndCreditType.append(Util.checkNullString(detailView.getCreditTypeView().getName())).append(enter);
                                } else {
                                    prodAndCreditType = prodAndCreditType.append(minus).append(enter);
                                }

                                proposeCreditInfoDetailReport.setProductProgramAndCreditType(prodAndCreditType.toString());
                                proposeCreditInfoDetailReport.setLimit(Util.convertNullToZERO(detailView.getLimit()));

                                StringBuilder guaranteeAmount = new StringBuilder();
                                guaranteeAmount = guaranteeAmount.append("Guarantee Amount: ").append(Util.formatNumber(Util.convertNullToZERO(detailView.getGuaranteeAmount())));

                                proposeCreditInfoDetailReport.setGuaranteeAmount(guaranteeAmount.toString());
                                proposeCreditInfoDetailReportList.add(proposeCreditInfoDetailReport);
                            }
                        }
                        approvedGuarantorDecisionReport.setProposeCreditInfoDetailReports(proposeCreditInfoDetailReportList);
                        approvedGuarantorDecisionReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(view.getGuaranteeAmount()));
                        if ((DecisionType.APPROVED).equals(view.getUwDecision())){
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
                        Util.checkNullString(view.getConditionView().getName()) : minus);
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
                approvalHistoryDecisionReport.setAction(Util.checkNullString(view.getAction()));
                approvalHistoryDecisionReport.setUserName(!Util.isNull(view.getUserView()) ?
                        Util.checkNullString(view.getUserView().getUserName()) : minus);
                approvalHistoryDecisionReport.setRoleDescription(!Util.isNull(view.getUserView()) ?
                        Util.checkNullString(view.getUserView().getRoleDescription()) : minus);
                approvalHistoryDecisionReport.setTitleName(!Util.isNull(view.getUserView()) ?
                        Util.checkNullString(view.getUserView().getPositionName()) : minus);
                approvalHistoryDecisionReport.setSubmitDate(DateTimeUtil.getCurrentDateTimeTH(view.getSubmitDate()));
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
            if ((CreditCustomerType.NORMAL).equals(decisionView.getCreditCustomerType())){
                totalDecisionReport.setCreditCusType(1);
            } else if ((CreditCustomerType.PRIME).equals(decisionView.getCreditCustomerType())){
                totalDecisionReport.setCreditCusType(2);
            } else {
                totalDecisionReport.setCreditCusType(0);
            }
        }

        if (!Util.isNull(decisionView.getInvestedCountry())){
            totalDecisionReport.setCountryName(Util.checkNullString(decisionView.getInvestedCountry().getName()));
        } else {
            totalDecisionReport.setCountryName(minus);
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
                    Util.checkNullString(bizInfoSummaryView.getProvince().getName()) : minus);
            totalDecisionReport.setDistrictName(!Util.isNull(bizInfoSummaryView.getDistrict()) ?
                    Util.checkNullString(bizInfoSummaryView.getDistrict().getName()) : minus);
            totalDecisionReport.setSubDisName(!Util.isNull(bizInfoSummaryView.getSubDistrict()) ?
                    Util.checkNullString(bizInfoSummaryView.getSubDistrict().getName()) : minus);
            totalDecisionReport.setPostCode(Util.checkNullString(bizInfoSummaryView.getPostCode()));
            totalDecisionReport.setCountryBizName(!Util.isNull(bizInfoSummaryView.getCountry()) ?
                    Util.checkNullString(bizInfoSummaryView.getCountry().getName()) : minus);
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
                        Util.checkNullString(view.getConditionView().getName()) : minus);
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
