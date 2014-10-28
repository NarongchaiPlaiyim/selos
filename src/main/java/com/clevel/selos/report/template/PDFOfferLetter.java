package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.report.OfferLetterDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.report.OfferLetter;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.report.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class PDFOfferLetter implements Serializable {

    @Inject
    @SELOS
    Logger log;

    @Inject private DecisionControl decisionControl;
    @Inject private HeaderControl headerControl;
    @Inject protected BaseRateDAO baseRateDAO;
    @Inject private FeeCollectionDetailDAO feeCollectionDetailDAO;
    @Inject private DisbursementDAO disbursementDAO;
    @Inject private DisbursementControl disbursementControl;
    @Inject private OfferLetterDAO offerLetterDAO;
    @Inject private MortgageSummaryDAO mortgageSummaryDAO;
    @Inject private AgreementInfoDAO agreementInfoDAO;
    @Inject private CustomerDAO customerDAO;
    @Inject private MortgageInfoDAO mortgageInfoDAO;
    @Inject private MortgageDetailControl mortgageDetailControl;
    @Inject private IndividualDAO individualDAO;
    @Inject private DisbursementMCDAO disbursementMCDAO;
    @Inject private DisbursementTRDAO disbursementTRDAO;
    @Inject private DisbursementBahtnetDAO disbursementBahtnetDAO;
    @Inject private ProposeCreditInfoDAO newCreditDetailDAO;
    @Inject private DisbursementMCCreditDAO disbursementMCCreditDAO;
    @Inject private DisbursementTRCreditDAO disbursementTRCreditDAO;
    @Inject private DisbursementBahtnetCreditDAO disbursementBahtnetCreditDAO;

    private DecisionView decisionView;
    private AppHeaderView appHeaderView;
    private List<BaseRate> baseRateList;
    private List<FeeCollectionDetail> feeCollectionDetails;
    private long customerId = -1;
    private long workCaseId;
    private final String SPACE = " ";
    private String minus = "-";
    private char enter = '\n';
    private int firstIndex = 0;
    private int noCalculation;

    @Inject
    @NormalMessage
    Message msg;

    public PDFOfferLetter() {
    }

    public void init(){
        HttpSession session = FacesUtil.getSession(false);

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

        decisionView = new DecisionView();
        log.debug("--Work Case. {}",workCaseId);
        if(!Util.isNull(workCaseId)){
            decisionView = decisionControl.findDecisionViewByWorkCaseId(workCaseId);
            feeCollectionDetails = feeCollectionDetailDAO.findAllByWorkCaseId(workCaseId);
        }

        noCalculation = 1;
    }

    //รายละเอียดผลการอนุมัติ 6,8-12
    public List<ApprovedCreditOfferLetterReport> fillApprovedCredit(){
        log.debug("--fillApprovedCredit()");
        List<ApprovedCreditOfferLetterReport> reports = new ArrayList<ApprovedCreditOfferLetterReport>();
        List<ProposeCreditInfoDetailView> newCreditDetailViews = decisionView.getApproveCreditList();

        if(Util.isSafetyList(newCreditDetailViews)){
            log.debug("--ApproveCreditList Size. {}",newCreditDetailViews.size());
            for (ProposeCreditInfoDetailView view : newCreditDetailViews){
                if (Util.isSafetyList(view.getProposeCreditInfoTierDetailViewList())){
                    log.debug("--tierDetailView Size. {}",view.getProposeCreditInfoTierDetailViewList().size());
                    for (ProposeCreditInfoTierDetailView tierDetailView : view.getProposeCreditInfoTierDetailViewList()){
                        ApprovedCreditOfferLetterReport approvedCredit = new ApprovedCreditOfferLetterReport();

                        if (!Util.isNull(view.getProductProgramView())){
                            approvedCredit.setProductProgramName(Util.checkNullString(view.getProductProgramView().getName()));
                        } else {
                            approvedCredit.setProductProgramName(minus);
                        }

                        if (!Util.isNull(view.getCreditTypeView())){
                            approvedCredit.setCreditTypeName(Util.checkNullString(view.getCreditTypeView().getName()));
                        } else {
                            approvedCredit.setCreditTypeName(minus);
                        }
                        approvedCredit.setLimit(Util.convertNullToZERO(view.getLimit()));
                        approvedCredit.setFinalPriceLabel(tierDetailView.getFinalPriceLabel());
                        approvedCredit.setInstallment(Util.convertNullToZERO(tierDetailView.getInstallment()));
                        approvedCredit.setTenor(tierDetailView.getTenor());

                        if (view.getCreditTypeView().getCreditGroup() == firstIndex){
                            approvedCredit.setInstallmentType(msg.get("report.offerletter.one"));
                        } else if (view.getCreditTypeView().getCreditGroup() == 1){
                            approvedCredit.setInstallmentType(msg.get("report.offerletter.two"));
                        } else if (view.getCreditTypeView().getCreditGroup() == 2){
                            approvedCredit.setInstallmentType(msg.get("report.offerletter.three"));
                        } else if (view.getCreditTypeView().getCreditGroup() == 3){
                            approvedCredit.setInstallmentType(msg.get("report.offerletter.three"));
                        } else if (view.getCreditTypeView().getCreditGroup() == 4){
                            approvedCredit.setInstallmentType(msg.get("report.offerletter.three"));
                        } else if (view.getCreditTypeView().getCreditGroup() == 5){
                            approvedCredit.setInstallmentType(msg.get("report.offerletter.three"));
                        } else if (view.getCreditTypeView().getCreditGroup() == 6){
                            approvedCredit.setInstallmentType(msg.get("report.offerletter.three"));
                        } else if (view.getCreditTypeView().getCreditGroup() == 7){
                            approvedCredit.setInstallmentType(msg.get("report.offerletter.one"));
                        } else if (view.getCreditTypeView().getCreditGroup() == 8){
                            approvedCredit.setInstallmentType(msg.get("report.offerletter.three"));
                        }

                        approvedCredit.setPurpose(view.getLoanPurposeView().getDescription());
                        reports.add(approvedCredit);
                    }
                }
            }
        } else {
            log.debug("--ApproveCreditList is Null. [{}]");
            ApprovedCreditOfferLetterReport approvedCredit = new ApprovedCreditOfferLetterReport();
            reports.add(approvedCredit);
        }

        return reports;
    }

    //หลักประกัน/การค้ำประกัน 17-22
    public List<ApprovedCollateralOfferLetterReport> fillGuarantor(String path){
        log.debug("--fillGuarantor");
        List<ApprovedCollateralOfferLetterReport> reports = new ArrayList<ApprovedCollateralOfferLetterReport>();
        List<ProposeCollateralInfoView> collateralViews = decisionView.getApproveCollateralList();
        StringBuilder collOwnerUW;
        StringBuilder guarantorName;

        //Approved Guarantor
        List<ProposeGuarantorInfoView> guarantorDetailViews = decisionView.getApproveGuarantorList();
        List<ApprovedGuarantorOfferLetterReport> approvedGuarantorOfferLetterReports = new ArrayList<ApprovedGuarantorOfferLetterReport>();

        // Approved Collateral
        if (Util.isSafetyList(collateralViews)){
            log.debug("--Approved Collateral Size. {}",collateralViews.size());
            for(ProposeCollateralInfoView view : collateralViews){
                for (ProposeCollateralInfoHeadView headView : view.getProposeCollateralInfoHeadViewList()){
                    log.debug("--Approved Collateral SubList Size. {}",headView.getProposeCollateralInfoSubViewList().size());
                    if (Util.isSafetyList(headView.getProposeCollateralInfoSubViewList())){
                        for (ProposeCollateralInfoSubView newCollateralSubView : headView.getProposeCollateralInfoSubViewList()){
                            ApprovedCollateralOfferLetterReport collateralAndGuarantorOfferLetterReport = new ApprovedCollateralOfferLetterReport();
                            collateralAndGuarantorOfferLetterReport.setPath(path);

                            if (!Util.isNull(newCollateralSubView.getSubCollateralType())){
                                collateralAndGuarantorOfferLetterReport.setSubCollateralTypeName(Util.checkNullString(newCollateralSubView.getSubCollateralType().getDescription()));
                            } else {
                                collateralAndGuarantorOfferLetterReport.setSubCollateralTypeName(minus);
                            }

                            collateralAndGuarantorOfferLetterReport.setTitleDeed(Util.checkNullString(newCollateralSubView.getTitleDeed()));
                            collateralAndGuarantorOfferLetterReport.setMortgageValue(Util.convertNullToZERO(newCollateralSubView.getMortgageValue()));

                            log.debug("Approved Collateral Owner Size. {}",newCollateralSubView.getCollateralOwnerUWList().size());
                            if (Util.isSafetyList(newCollateralSubView.getCollateralOwnerUWList())){
                                collOwnerUW = new StringBuilder();
                                for (CustomerInfoView customerInfoView : newCollateralSubView.getCollateralOwnerUWList()){

                                    if (!Util.isNull(customerInfoView.getTitleTh())){
                                        collOwnerUW = collOwnerUW.append(customerInfoView.getTitleTh().getTitleTh());
                                    }

                                    collOwnerUW = collOwnerUW.append(customerInfoView.getFirstNameTh()).append(SPACE).append(customerInfoView.getLastNameTh()).append(enter);
                                    collateralAndGuarantorOfferLetterReport.setCollateralOwnerUW(Util.checkNullString(collOwnerUW.toString()));

                                    log.debug("--collOwnerUW. {}",collOwnerUW.toString());
                                }
                            } else {
                                collateralAndGuarantorOfferLetterReport.setCollateralOwnerUW(minus);
                            }


                            collateralAndGuarantorOfferLetterReport.setAddress(Util.checkNullString(newCollateralSubView.getAddress()));

                            log.debug("--mortgageTypeView Size. {}",newCollateralSubView.getMortgageList().size());
                            for (MortgageTypeView mortgageTypeView : newCollateralSubView.getMortgageList()){
                                collateralAndGuarantorOfferLetterReport.setMortgage(Util.checkNullString(mortgageTypeView.getMortgage()));
                            }

                            log.debug("--Approved Guarantor Size. {}",collateralViews.size());
                            if (Util.isSafetyList(guarantorDetailViews)){
                                for(ProposeGuarantorInfoView guarantorDetailView : guarantorDetailViews){
                                    log.debug("--Propose Credit Size. {}",guarantorDetailView.getProposeCreditInfoDetailViewList());
                                    if (Util.isSafetyList(guarantorDetailView.getProposeCreditInfoDetailViewList())){
                                        guarantorName = new StringBuilder();
                                        for (ProposeCreditInfoDetailView detailView : guarantorDetailView.getProposeCreditInfoDetailViewList()){
                                            ApprovedGuarantorOfferLetterReport approvedGuarantorOfferLetterReport = new ApprovedGuarantorOfferLetterReport();

                                            if (!Util.isNull(guarantorDetailView.getGuarantorName()) && !Util.isNull(guarantorDetailView.getGuarantorName().getTitleTh())){
                                                guarantorName = guarantorName.append(guarantorDetailView.getGuarantorName().getTitleTh().getTitleTh());
                                            }

                                            if (!Util.isNull(guarantorDetailView.getGuarantorName())){
                                                guarantorName = guarantorName.append(guarantorDetailView.getGuarantorName().getFirstNameTh()).append(SPACE)
                                                        .append(guarantorDetailView.getGuarantorName().getLastNameTh()).append(enter);
                                            }

                                            approvedGuarantorOfferLetterReport.setGuarantorName(Util.checkNullString(guarantorName.toString()));
                                            approvedGuarantorOfferLetterReport.setAccountName(Util.checkNullString(detailView.getAccountName()));
                                            approvedGuarantorOfferLetterReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(guarantorDetailView.getGuaranteeAmount()));
                                            approvedGuarantorOfferLetterReports.add(approvedGuarantorOfferLetterReport);

                                            collateralAndGuarantorOfferLetterReport.setApprovedGuarantorOfferLetterReport(approvedGuarantorOfferLetterReports);
                                        }
                                    }
                                }
                            } else {
                                log.debug("--Approved Guarantor is Null. [{}]",guarantorDetailViews);
                                ApprovedGuarantorOfferLetterReport approvedGuarantorOfferLetterReport = new ApprovedGuarantorOfferLetterReport();
                                approvedGuarantorOfferLetterReports.add(approvedGuarantorOfferLetterReport);
                                collateralAndGuarantorOfferLetterReport.setApprovedGuarantorOfferLetterReport(approvedGuarantorOfferLetterReports);
                            }

                            reports.add(collateralAndGuarantorOfferLetterReport); ///
                        }
                    } else {
                        ApprovedCollateralOfferLetterReport collateralAndGuarantorOfferLetterReport = new ApprovedCollateralOfferLetterReport();
                        collateralAndGuarantorOfferLetterReport.setPath(path);
                        reports.add(collateralAndGuarantorOfferLetterReport); ///
                    }
                }
            }
        } else if (Util.isSafetyList(guarantorDetailViews)){
            log.debug("--Approved Guarantor Size. {}",guarantorDetailViews.size());
            for(ProposeGuarantorInfoView guarantorDetailView : guarantorDetailViews){
                log.debug("--Propose Credit Size. {}",guarantorDetailView.getProposeCreditInfoDetailViewList());
                if (Util.isSafetyList(guarantorDetailView.getProposeCreditInfoDetailViewList())){
                    guarantorName = new StringBuilder();
                    for (ProposeCreditInfoDetailView detailView : guarantorDetailView.getProposeCreditInfoDetailViewList()){
                        ApprovedGuarantorOfferLetterReport approvedGuarantorOfferLetterReport = new ApprovedGuarantorOfferLetterReport();
                        ApprovedCollateralOfferLetterReport collateralAndGuarantorOfferLetterReport = new ApprovedCollateralOfferLetterReport();
                        collateralAndGuarantorOfferLetterReport.setPath(path);

                        if (!Util.isNull(guarantorDetailView.getGuarantorName()) && !Util.isNull(guarantorDetailView.getGuarantorName().getTitleTh())){
                            guarantorName = guarantorName.append(guarantorDetailView.getGuarantorName().getTitleTh().getTitleTh());
                        }

                        if (!Util.isNull(guarantorDetailView.getGuarantorName())){
                            guarantorName = guarantorName.append(guarantorDetailView.getGuarantorName().getFirstNameTh())
                                    .append(SPACE).append(guarantorDetailView.getGuarantorName().getLastNameTh()).append(enter);
                        }

                        approvedGuarantorOfferLetterReport.setGuarantorName(Util.checkNullString(guarantorName.toString()));
                        approvedGuarantorOfferLetterReport.setAccountName(Util.checkNullString(detailView.getAccountName()));
                        approvedGuarantorOfferLetterReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(guarantorDetailView.getGuaranteeAmount()));
                        approvedGuarantorOfferLetterReports.add(approvedGuarantorOfferLetterReport);

                        collateralAndGuarantorOfferLetterReport.setApprovedGuarantorOfferLetterReport(approvedGuarantorOfferLetterReports);
                        reports.add(collateralAndGuarantorOfferLetterReport);
                    }
                }
            }
        } else {
            log.debug("--Approved Collateral Size is Empty. [{}],Approved Guarantor Size is Empty. [{}]",collateralViews.size(),guarantorDetailViews.size());
            ApprovedCollateralOfferLetterReport collateralAndGuarantorOfferLetterReport = new ApprovedCollateralOfferLetterReport();
            ApprovedGuarantorOfferLetterReport approvedGuarantorOfferLetterReport = new ApprovedGuarantorOfferLetterReport();
            collateralAndGuarantorOfferLetterReport.setPath(path);
            approvedGuarantorOfferLetterReports.add(approvedGuarantorOfferLetterReport);
            collateralAndGuarantorOfferLetterReport.setApprovedGuarantorOfferLetterReport(approvedGuarantorOfferLetterReports);
            reports.add(collateralAndGuarantorOfferLetterReport);
        }

        return reports;
    }

    //เงื่อนไขติดตามหลังอนุมัติสินเชื่อ 16
    public List<FollowConditionOfferletterReport> fillFollowCondition(){
        List<FollowConditionOfferletterReport> reports = new ArrayList<FollowConditionOfferletterReport>();
        int count = 1;

        if (Util.isSafetyList(decisionView.getDecisionFollowConditionViewList())){
            log.debug("--DecisionFollowConditionViewList Size. {}", decisionView.getDecisionFollowConditionViewList().size());

            for (DecisionFollowConditionView followConditionView : decisionView.getDecisionFollowConditionViewList()){
                FollowConditionOfferletterReport conditionOfferletterReport = new FollowConditionOfferletterReport();
                conditionOfferletterReport.setCount(count++);
                conditionOfferletterReport.setName(Util.checkNullString(followConditionView.getConditionView().getName()));
                reports.add(conditionOfferletterReport);
            }
        } else {
            log.debug("--DecisionFollowConditionViewList Size is Empty. {}", decisionView.getDecisionFollowConditionViewList().size());
            FollowConditionOfferletterReport conditionOfferletterReport = new FollowConditionOfferletterReport();
            reports.add(conditionOfferletterReport);

        }

        return reports;
    }

    //fillFeecalculationAgreement 15/1
    public List<FeeCalculationOfferLetterReport> fillFeecalculationAgreement(){
        List<FeeCalculationOfferLetterReport> detailsAgreement = new ArrayList<FeeCalculationOfferLetterReport>();
        String PaymentMethod = "";
        String type;
        String feeDecrition;

        log.debug("feeCollectionDetails Size. {}",feeCollectionDetails.size());
        if (Util.isSafetyList(feeCollectionDetails)){
            for (FeeCollectionDetail detail : feeCollectionDetails){
                if (detail.getPaymentMethod().getId() == 1){
                    FeeCalculationOfferLetterReport calculationOfferLetterReport = new FeeCalculationOfferLetterReport();
                    calculationOfferLetterReport.setId(noCalculation++);

                    if (!Util.isNull(detail.getPaymentMethod()) && !PaymentMethod.equalsIgnoreCase(msg.get("report.offerletter.paymentmethod"))){
                        PaymentMethod = msg.get("report.offerletter.paymentmethod");
                        calculationOfferLetterReport.setPaymentMethod(msg.get("report.offerletter.paymentmethod"));
                    }

                    if (!Util.isNull(detail.getFeeType())){
                        if (detail.getFeeType().getId() == 1){
                            type = msg.get("report.offerletter.frontendfee");
                            feeDecrition = msg.get("report.offerletter.oneagreement");
                        } else if (detail.getFeeType().getId() == 7){
                            type = msg.get("report.offerletter.dutystamp");
                            feeDecrition = msg.get("report.offerletter.oneagreement");
                        } else if (detail.getFeeType().getId() == 10){
                            type = msg.get("report.offerletter.mrtgageservice");
                            feeDecrition = msg.get("report.offerletter.oneagreement");
                        } else if (detail.getFeeType().getId() == 12){
                            type = msg.get("report.offerletter.insurance");
                            feeDecrition = msg.get("report.offerletter.firstagreement");
                        } else if (detail.getFeeType().getId() == 13){
                            type = msg.get("report.offerletter.ba");
                            feeDecrition = msg.get("report.offerletter.firstagreement");
                        } else {
                            type = minus;
                            feeDecrition = minus;
                        }
                    } else {
                        type = minus;
                        feeDecrition = minus;
                    }

                    calculationOfferLetterReport.setPaymentType(type);
                    calculationOfferLetterReport.setFeeType(feeDecrition);
                    calculationOfferLetterReport.setAmount(Util.convertNullToZERO(detail.getAmount()));

                    detailsAgreement.add(calculationOfferLetterReport);
                }
            }
            log.debug("--fillFeecalculationAgreement. {}",detailsAgreement);
        } else {
            FeeCalculationOfferLetterReport calculationOfferLetterReport = new FeeCalculationOfferLetterReport();
            detailsAgreement.add(calculationOfferLetterReport);
            log.debug("--fillFeecalculationAgreement Size is Empty. {}",detailsAgreement.size());
        }

        return detailsAgreement;
    }

    //fillFeecalculationNonAgreement 15/2
    public List<FeeCalculationOfferLetterReport> fillFeecalculationNonAgreement(){
        List<FeeCalculationOfferLetterReport> detailsAgreement = new ArrayList<FeeCalculationOfferLetterReport>();
        String PaymentNoMethod = "";
        String type = "";
        String feeDecrition;

        if (Util.isSafetyList(feeCollectionDetails)) {
            for (FeeCollectionDetail detail : feeCollectionDetails){
                if (detail.getPaymentMethod().getId() == 3){
                    FeeCalculationOfferLetterReport calculationOfferLetterReport = new FeeCalculationOfferLetterReport();
                    calculationOfferLetterReport.setId(noCalculation++);

                    if (!Util.isNull(detail.getPaymentMethod()) && !PaymentNoMethod.equalsIgnoreCase(msg.get("report.offerletter.paymentnonmethod"))){
                        PaymentNoMethod = msg.get("report.offerletter.paymentnonmethod");
                        calculationOfferLetterReport.setPaymentMethod(msg.get("report.offerletter.paymentnonmethod"));
                    }

                    if (!Util.isNull(detail.getFeeType())){
                        if (detail.getFeeType().getId() == 9){
                            type = msg.get("report.offerletter.mortgageregistrationfee");
                            feeDecrition = msg.get("report.offerletter.onemortgage");
                        } else if (detail.getFeeType().getId() == 6){
                            type = msg.get("report.offerletter.tcgfee");
                            feeDecrition = msg.get("report.offerletter.beforeoneagreement");
                        }  else {
                            type = minus;
                            feeDecrition = minus;
                        }
                    } else {
                        type = minus;
                        feeDecrition = minus;
                    }

                    calculationOfferLetterReport.setPaymentType(type);
                    calculationOfferLetterReport.setFeeType(feeDecrition);
                    calculationOfferLetterReport.setAmount(Util.convertNullToZERO(detail.getAmount()));
                    detailsAgreement.add(calculationOfferLetterReport);
                }
            }
        } else {
            FeeCalculationOfferLetterReport calculationOfferLetterReport = new FeeCalculationOfferLetterReport();
            detailsAgreement.add(calculationOfferLetterReport);
            log.debug("--fillFeecalculationNonAgreement Size is Empty. {}",detailsAgreement.size());
        }

        return detailsAgreement;
    }

    //การเบิกใช้วงเงินกรณี Refinance หรือ ซื้อ-ขาย
    public List<DisbursementOfferLetterReport> fillDisbursment(){
        List<DisbursementOfferLetterReport> disbursementOfferLetterReports = new ArrayList<DisbursementOfferLetterReport>();
        Disbursement disbursement = disbursementDAO.findByWorkCaseId(workCaseId);

        if (!Util.isNull(disbursement)) {
            List<DisbursementMC> disbursementMCList = disbursementMCDAO.findByDisbursementId(disbursement.getId());
            log.debug("--disbursementMCList Size. {}",disbursementMCList.size());
            if (Util.isSafetyList(disbursementMCList)){
                for (DisbursementMC name : disbursementMCList){
                    List<DisbursementMCCredit> mcCreditsDetail = disbursementMCCreditDAO.findByDisbursementMCId(name.getId());
                    log.debug("--mcCreditsDetail Size. {}",mcCreditsDetail.size());
                    if (Util.isSafetyList(mcCreditsDetail)){
                        for (DisbursementMCCredit mcCredit : mcCreditsDetail){
                            log.debug("--DisbursementMCCreditList Size. {}",mcCredit.getDisbursementMC().getDisbursementMCCreditList().size());
                            if (!Util.isNull(mcCredit.getDisbursementMC())){
                                if (Util.isSafetyList(mcCredit.getDisbursementMC().getDisbursementMCCreditList())){
                                    log.debug("Disbursement MC Credir Size. {}",mcCredit.getDisbursementMC().getDisbursementMCCreditList().size());
                                    if (Util.isSafetyList(mcCredit.getDisbursementMC().getDisbursementMCCreditList())){
                                        for (DisbursementMCCredit credit : mcCredit.getDisbursementMC().getDisbursementMCCreditList()){
                                            DisbursementOfferLetterReport disbursementOfferLetterReport = new DisbursementOfferLetterReport();

                                            if (!Util.isNull(credit.getCreditDetail())){
                                                if (!Util.isNull(!Util.isNull(credit.getCreditDetail().getLoanPurpose()))){
                                                    disbursementOfferLetterReport.setLoanPurPose(Util.checkNullString(credit.getCreditDetail().getLoanPurpose().getDescription())); //32
                                                } else {
                                                disbursementOfferLetterReport.setLoanPurPose(minus);
                                                }

                                                if (!Util.isNull(credit.getCreditDetail().getProductProgram())){
                                                    disbursementOfferLetterReport.setProductProgram(Util.checkNullString(credit.getCreditDetail().getProductProgram().getName())); //32
                                                } else {
                                                    disbursementOfferLetterReport.setProductProgram(minus);
                                                }
                                            } else {
                                                disbursementOfferLetterReport.setLoanPurPose(minus);
                                                disbursementOfferLetterReport.setProductProgram(minus);
                                            }

                                            disbursementOfferLetterReport.setTotal(Util.convertNullToZERO(credit.getDisburseAmount())); //33
                                            disbursementOfferLetterReport.setName(msg.get("report.offerletter.mc"));  //34

                                            if (!Util.isNull(name.getPayeeName())){
                                                disbursementOfferLetterReport.setLimit(name.getPayeeName().getName());  //35
                                            } else {
                                                disbursementOfferLetterReport.setLimit(minus);
                                            }
                                            disbursementOfferLetterReports.add(disbursementOfferLetterReport);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            List<DisbursementTR> disbursementTRList = disbursementTRDAO.findByDisbursementId(disbursement.getId());
            log.debug("--disbursementTRList Size. {}",disbursementTRList.size());
            if (Util.isSafetyList(disbursementTRList)){
                for (DisbursementTR tr : disbursementTRList){
                    List<DisbursementTRCredit> trCredits = disbursementTRCreditDAO.findByDisbursementTRId(tr.getId());
                    log.debug("--trCredits Size. {}",trCredits.size());
                    if (Util.isSafetyList(trCredits)){
                        for (DisbursementTRCredit credit : trCredits){
                            if (!Util.isNull(tr.getOpenAccount())){
                                log.debug("--OpenAccountNameList(). {}",tr.getOpenAccount().getOpenAccountNameList());
                                if (Util.isSafetyList(tr.getOpenAccount().getOpenAccountNameList())){
                                    for (OpenAccountName accountName : tr.getOpenAccount().getOpenAccountNameList()){
                                        if (Util.isSafetyList(credit.getDisbursementTR().getDisbursementTRCreditList())){
                                            log.debug("--DisbursementTRCreditList. {}",credit.getDisbursementTR().getDisbursementTRCreditList());
                                            for (DisbursementTRCredit trCredit : credit.getDisbursementTR().getDisbursementTRCreditList()){
                                                DisbursementOfferLetterReport disbursementOfferLetterReport = new DisbursementOfferLetterReport();

                                                if (!Util.isNull(trCredit.getCreditDetail())){
                                                    if (!Util.isNull(trCredit.getCreditDetail().getLoanPurpose())){
                                                        disbursementOfferLetterReport.setLoanPurPose(Util.checkNullString(trCredit.getCreditDetail().getLoanPurpose().getDescription()));  //32
                                                    } else {
                                                        disbursementOfferLetterReport.setLoanPurPose(minus);
                                                    }

                                                    if (!Util.isNull(trCredit.getCreditDetail().getProductProgram())){
                                                        disbursementOfferLetterReport.setProductProgram(Util.checkNullString(trCredit.getCreditDetail().getProductProgram().getName())); //32
                                                    } else {
                                                        disbursementOfferLetterReport.setProductProgram(minus);
                                                    }
                                                } else {
                                                    disbursementOfferLetterReport.setLoanPurPose(minus);
                                                    disbursementOfferLetterReport.setProductProgram(minus);
                                                }

                                                disbursementOfferLetterReport.setTotal(credit.getDisburseAmount()); //33
                                                disbursementOfferLetterReport.setName(msg.get("report.offerletter.tr"));  //34

                                                if (!Util.isNull(accountName.getCustomer())){
                                                    disbursementOfferLetterReport.setLimit(Util.checkNullString(accountName.getCustomer().getNameEn())); //35
                                                } else {
                                                    disbursementOfferLetterReport.setLimit(minus);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            List<DisbursementBahtnet> disbursementBahtnetList = disbursementBahtnetDAO.findByDisbursementId(disbursement.getId());
            log.debug("--disbursementBahtnetList Size. {}",disbursementBahtnetList.size());
            if (Util.isSafetyList(disbursementBahtnetList)){
                for (DisbursementBahtnet bahtnets : disbursementBahtnetList){
                    List<DisbursementBahtnetCredit> bahtnetCredits = disbursementBahtnetCreditDAO.findByDisbursementBahtnetId(bahtnets.getId());
                    log.debug("--bahtnetCredits Size. {}",bahtnetCredits.size());
                    if (Util.isSafetyList(bahtnetCredits)){
                        for (DisbursementBahtnetCredit credit : bahtnetCredits){
                            if (!Util.isNull(credit.getDisbursementBahtnet())){
                                log.debug("--DisburseBahtnetCreditList Size. {}",credit.getDisbursementBahtnet().getDisburseBahtnetCreditList().size());
                                if (Util.isSafetyList(credit.getDisbursementBahtnet().getDisburseBahtnetCreditList())){
                                    for (DisbursementBahtnetCredit bahtnetCredit : credit.getDisbursementBahtnet().getDisburseBahtnetCreditList()){
                                        DisbursementOfferLetterReport disbursementOfferLetterReport = new DisbursementOfferLetterReport();

                                        if (!Util.isNull(credit.getCreditDetail())){
                                            if (!Util.isNull(credit.getCreditDetail().getLoanPurpose())){
                                                disbursementOfferLetterReport.setLoanPurPose(Util.checkNullString(credit.getCreditDetail().getLoanPurpose().getDescription())); //32
                                            } else {
                                                disbursementOfferLetterReport.setLoanPurPose(minus);
                                            }

                                            if (!Util.isNull(credit.getCreditDetail().getProductProgram())){
                                                disbursementOfferLetterReport.setProductProgram(Util.checkNullString(bahtnetCredit.getCreditDetail().getProductProgram().getName())); //32
                                            } else {
                                                disbursementOfferLetterReport.setProductProgram(minus);
                                            }
                                        }

                                        disbursementOfferLetterReport.setTotal(Util.convertNullToZERO(credit.getDisburseAmount())); //33
                                        disbursementOfferLetterReport.setName(msg.get("report.offerletter.bahtnet"));  //34
                                        disbursementOfferLetterReport.setLimit(Util.checkNullString(bahtnets.getBeneficiaryName())); //35
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            DisbursementOfferLetterReport disbursementOfferLetterReport = new DisbursementOfferLetterReport();
            disbursementOfferLetterReports.add(disbursementOfferLetterReport);
            log.debug("--fillDisbursment is Null",disbursementOfferLetterReports);
        }
        return disbursementOfferLetterReports;
    }

    //Master Report OfferLetter
    public OfferLetterReport fillMasterOfferLetter(){
        OfferLetterReport report = new OfferLetterReport();
        BigDecimal sumTotalAmt = BigDecimal.ZERO;
        BigDecimal sumTotalNonAmt = BigDecimal.ZERO;
        String loanDate;
        String loanTime;
        String mortgageDate;
        String mortgageTime;
        String mortCustomerName;
        StringBuilder customerName = new StringBuilder();
        long mortgageId = -1;
        StringBuilder customerAuthorized = new StringBuilder();
        String setMonth;
        StringBuilder dateValue = new StringBuilder();

        HttpSession session = FacesUtil.getSession(false);
        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");

        if (!Util.isNull(appHeaderView)){
            report.setBdmName(Util.checkNullString(appHeaderView.getBdmName()));  //3
            report.setTel(Util.checkNullString(Util.checkNullString(appHeaderView.getBdmPhoneNumber()))); //5
            report.setZone(Util.checkNullString(appHeaderView.getBdmZoneName()));  //4

            log.debug("--BorrowerHeaderViewList Size. {}",appHeaderView.getBorrowerHeaderViewList().size());
            if (Util.isSafetyList(appHeaderView.getBorrowerHeaderViewList())){
                for (AppBorrowerHeaderView view : appHeaderView.getBorrowerHeaderViewList()){
                    report.setBorrowName(Util.checkNullString(view.getBorrowerName()));  //1
                }
            }
        }

        //TotalLimit 13
        report.setTotalLimit(Util.convertNullToZERO(decisionView.getApproveTotalCreditLimit()));

        //FeeCalculation 15
        log.debug("--feeCollectionDetails. {}",feeCollectionDetails.size());
        for (FeeCollectionDetail detail : feeCollectionDetails){

            if (!Util.isNull(detail.getPaymentMethod())){
                if (detail.getPaymentMethod().getId() == 1){
                    sumTotalAmt = Util.add(sumTotalAmt,detail.getAmount());
                } else  if (detail.getPaymentMethod().getId() == 3){
                    sumTotalNonAmt = Util.add(sumTotalNonAmt,detail.getAmount());
                }
            }
        }
        report.setSumFeecalculation(sumTotalAmt); //15/1
        report.setSumNonFeecalculation(sumTotalNonAmt); //15/2

        //14
        baseRateList = baseRateDAO.findAll();
        log.debug("--baseRateList Size. {}",baseRateList.size());
        if (Util.isSafetyList(baseRateList)){
            for (int i = firstIndex;i < baseRateList.size();i++){
                switch (i){
                    case 0 : report.setValueMLR(baseRateList.get(i).getValue()); break;
                    case 1 : report.setValueMOR(baseRateList.get(i).getValue()); break;
                    case 2 : report.setValueMRR(baseRateList.get(i).getValue()); break;
                }
            }
        }

        String addOfDate = Util.createDateTh(baseRateList.get(1).getAddOfDate());
        String[] spDate = addOfDate.split("/");
        int month = Integer.valueOf(spDate[1]);

        switch (month){
            case 1: setMonth = msg.get("app.report.month.january"); break;
            case 2: setMonth = msg.get("app.report.month.february"); break;
            case 3: setMonth = msg.get("app.report.month.march"); break;
            case 4: setMonth = msg.get("app.report.month.april"); break;
            case 5: setMonth = msg.get("app.report.month.may"); break;
            case 6: setMonth = msg.get("app.report.month.june"); break;
            case 7: setMonth = msg.get("app.report.month.july"); break;
            case 8: setMonth = msg.get("app.report.month.august"); break;
            case 9: setMonth = msg.get("app.report.month.september"); break;
            case 10: setMonth = msg.get("app.report.month.october"); break;
            case 11: setMonth = msg.get("app.report.month.november"); break;
            case 12: setMonth = msg.get("app.report.month.december"); break;
            default:setMonth = SPACE;

        }
        dateValue = dateValue.append(spDate[firstIndex]).append(SPACE).append(setMonth).append(SPACE).append(spDate[2]);
        report.setDateValue(dateValue.toString());

        //23,24,25
        List<OfferLetter> offerLetter = offerLetterDAO.findAll();
        log.debug("--offerLetter. {}",offerLetter.size());
        if (Util.isSafetyList(offerLetter)){
            for (OfferLetter letter : offerLetter){
                report.setTeamName(Util.checkNullString(letter.getTeamName()));
                report.setTelPhone(Util.checkNullString(letter.getTelPhone()));
                report.setTelFax(Util.checkNullString(letter.getTelFax()));
            }
        }

        if (Util.isZero(workCaseId)){
            AgreementInfo agreementInfo = agreementInfoDAO.findByWorkCaseId(workCaseId);
            customerId = Util.parseLong(FacesUtil.getFlash().get("customerId"),-1L);

            if (!Util.isNull(agreementInfo)) {
                if (!Util.isNull(agreementInfo.getLoanContractDate())) {
                    loanDate =  Util.checkNullString(Util.createDateTh(agreementInfo.getLoanContractDate()));
                    loanTime = Util.checkNullString(Util.createTime(agreementInfo.getLoanContractDate()));
                    report.setLoanDate(loanDate); //27.1
                    report.setLoanTime(loanTime); //28.1
                }

                //29.1
                if (!Util.isNull(agreementInfo.getSigningLocation())) {
                    switch (agreementInfo.getSigningLocation()) {
                        case BRANCH :
                            if (!Util.isNull(agreementInfo.getBankBranch()))
                                report.setLoanLocation(Util.checkNullString(agreementInfo.getBankBranch().getName()));
                            break;
                        case ZONE :
                            if (!Util.isNull(agreementInfo.getUserZone()))
                                report.setLoanLocation(Util.checkNullString(agreementInfo.getUserZone().getName()));
                            break;
                        default : //DO NOTHING
                            break;
                    }
                }
            }

            //30.1
            List<Customer> list = customerDAO.findCustomerByCommitteeId(customerId);
            log.debug("Customer findCustomerByCommitteeId Size. {}",list.size());
            if (Util.isSafetyList(list)){
                if (!Util.isNull(list.get(firstIndex).getTitle())){
                    customerName = customerName.append(Util.checkNullString(list.get(firstIndex).getTitle().getTitleTh()));
                }

                customerName = customerName.append(Util.checkNullString(list.get(firstIndex).getNameTh())).append(Util.checkNullString(list.get(firstIndex).getLastNameTh()));
                report.setLoanCustomerName(Util.checkNullString(customerName.toString()));
            }

            List<MortgageInfo> mortgageInfoList = mortgageInfoDAO.findAllByWorkCaseId(workCaseId);
            log.debug("--mortgageInfoList Size. {}",mortgageInfoList.size());
            if (Util.isSafetyList(mortgageInfoList)){
                if (!Util.isNull(mortgageInfoList.get(firstIndex).getMortgageSigningDate())) {
                    mortgageDate = Util.checkNullString(Util.createDateTh(mortgageInfoList.get(firstIndex).getMortgageSigningDate()));
                    mortgageTime = Util.checkNullString(Util.createTime(mortgageInfoList.get(firstIndex).getMortgageSigningDate()));
                    report.setMortgageDate(mortgageDate); //27.2
                    report.setMortgageTime(mortgageTime); //28.2
                }

                if (!Util.isNull(mortgageInfoList.get(firstIndex).getMortgageLandOffice())){
                    report.setMortgageLocation(Util.checkNullString(mortgageInfoList.get(firstIndex).getMortgageLandOffice().getName())); //29.2
                } else {
                    report.setMortgageLocation(minus);
                }
            }

            Map<String,Object> params =  FacesUtil.getParamMapFromFlash("mortgageParams");
            mortgageId = Util.parseLong(params.get("mortgageId"),-1);
            if (mortgageId <= firstIndex) {
                mortgageId = Util.parseLong(FacesUtil.getParameter("mortgageId"),-1);
            }
            log.debug("--mortgageId. {}",mortgageId);

            List<MortgageInfoCollSubView> collSubViews = mortgageDetailControl.getMortgageInfoCollSubList(mortgageId);
            log.debug("collSubViews Size. {}",collSubViews.size());
            if (Util.isSafetyList(collSubViews)) {
                mortCustomerName = Util.checkNullString(collSubViews.get(1).getOwner());
                report.setMortgageCustomerName(mortCustomerName); //30.2
            }

            List<Customer> customers = customerDAO.findCustomerCanBePOA(workCaseId);
            log.debug("--customers findCustomerCanBePOA Size.{}",customers.size());
            if (Util.isSafetyList(customers)) {
                for (Customer view : customers) {
                    Individual individual = view.getIndividual();
                    if (!Util.isNull(individual)){
                        if (individual.getAttorneyRequired().equals(RadioValue.YES)) {
                            if (!Util.isNull(view.getTitle())){
                                customerAuthorized = customerAuthorized.append(Util.checkNullString(view.getTitle().getTitleTh()));
                            }
                            customerAuthorized = customerAuthorized.append(Util.checkNullString(view.getNameTh())).append(Util.checkNullString(view.getLastNameTh()));
                            report.setMortgageAuthorized(customerAuthorized.toString()); //31.2
                        }
                    }
                }
            }
        }

        log.debug("--fillMaster",report.toString());
        return report;
    }
}
