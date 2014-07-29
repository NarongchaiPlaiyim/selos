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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private List<ApprovedGuarantorOfferLetterReport> approvedGuarantorOfferLetterReports;
    private List<BaseRate> baseRateList;
    private List<FeeCollectionDetail> feeCollectionDetails;
    private long customerId = -1;
    private long workCaseId;
    private final String SPACE = " ";

    @Inject
    @NormalMessage
    Message msg;

    public PDFOfferLetter() {
    }

    public void init(){
        HttpSession session = FacesUtil.getSession(false);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
//            workCaseId = 128;
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
            log.debug("--decisionView. {[]}",decisionView);
            log.debug("--feeCollectionDetails. {}",feeCollectionDetails);
        } else {
            log.debug("--workcaseId is Null. {}",workCaseId);
        }
    }

    //รายละเอียดผลการอนุมัติ 6,8-12
    public List<ApprovedCreditOfferLetterReport> fillApprovedCredit(){
        log.debug("--fillApprovedCredit()");
        List<ApprovedCreditOfferLetterReport> reports = new ArrayList<ApprovedCreditOfferLetterReport>();
        List<ProposeCreditInfoDetailView> newCreditDetailViews = decisionView.getApproveCreditList();

        if(Util.safetyList(newCreditDetailViews).size() > 0){
            log.debug("--ApproveCreditList. [{}],Size. {}",newCreditDetailViews,newCreditDetailViews.size());
            for (ProposeCreditInfoDetailView view : newCreditDetailViews){
                log.debug("--tierDetailView Size. {}",view.getProposeCreditInfoTierDetailViewList().size());
                for (ProposeCreditInfoTierDetailView tierDetailView : view.getProposeCreditInfoTierDetailViewList()){
                    ApprovedCreditOfferLetterReport approvedCredit = new ApprovedCreditOfferLetterReport();
                    approvedCredit.setProductProgramName(Util.checkNullString(view.getProductProgramView().getName()));
                    approvedCredit.setCreditTypeName(Util.checkNullString(view.getCreditTypeView().getName()));
                    approvedCredit.setLimit(Util.convertNullToZERO(view.getLimit()));
                    approvedCredit.setFinalPriceLabel(tierDetailView.getFinalPriceLabel());
                    approvedCredit.setInstallment(Util.convertNullToZERO(tierDetailView.getInstallment()));
                    approvedCredit.setTenor(tierDetailView.getTenor());

                    if (view.getCreditTypeView().getCreditGroup() == 0){
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
        } else {
            log.debug("--ApproveCreditList is Null. [{}]",newCreditDetailViews);
            ApprovedCreditOfferLetterReport approvedCredit = new ApprovedCreditOfferLetterReport();
            reports.add(approvedCredit);
        }

        log.debug("--Data fillApprovedCredit. {}",reports);
        return reports;
    }

    //หลักประกัน/การค้ำประกัน 17-22
    public List<ApprovedCollateralOfferLetterReport> fillGuarantor(String path){
        log.debug("--fillGuarantor");
        List<ApprovedCollateralOfferLetterReport> reports = new ArrayList<ApprovedCollateralOfferLetterReport>();
        List<ProposeCollateralInfoView> collateralViews = decisionView.getApproveCollateralList();
        StringBuilder collOwnerUW = null;
        StringBuilder guarantorName = null;

        //Approved Guarantor
        List<ProposeGuarantorInfoView> guarantorDetailViews = decisionView.getApproveGuarantorList();
        approvedGuarantorOfferLetterReports = new ArrayList<ApprovedGuarantorOfferLetterReport>();

        // Approved Collateral
        if (Util.safetyList(collateralViews).size() > 0){
            log.debug("--Approved Collateral Size. {}",collateralViews.size());

            for(ProposeCollateralInfoView view : collateralViews){
                for (ProposeCollateralInfoHeadView headView : view.getProposeCollateralInfoHeadViewList()){
                    for (ProposeCollateralInfoSubView newCollateralSubView : headView.getProposeCollateralInfoSubViewList()){
                        ApprovedCollateralOfferLetterReport collateralAndGuarantorOfferLetterReport = new ApprovedCollateralOfferLetterReport();
                        collateralAndGuarantorOfferLetterReport.setPath(path);
                        collateralAndGuarantorOfferLetterReport.setSubCollateralTypeName(Util.checkNullString(newCollateralSubView.getSubCollateralType().getDescription()));
                        collateralAndGuarantorOfferLetterReport.setTitleDeed(Util.checkNullString(newCollateralSubView.getTitleDeed()));
                        collateralAndGuarantorOfferLetterReport.setMortgageValue(Util.convertNullToZERO(newCollateralSubView.getMortgageValue()));


                        for (CustomerInfoView customerInfoView : newCollateralSubView.getCollateralOwnerUWList()){
                            collOwnerUW = new StringBuilder();
                            collOwnerUW = collOwnerUW.append(customerInfoView.getTitleTh().getTitleTh())
                                    .append(customerInfoView.getFirstNameTh()).append(SPACE)
                                    .append(customerInfoView.getLastNameTh());
                            collateralAndGuarantorOfferLetterReport.setCollateralOwnerUW(Util.checkNullString(collOwnerUW.toString()));

                            log.debug("--collOwnerUW. {},TitleTh. {},FirstNameTh. {},LastNameTh. {}", collOwnerUW.toString(), customerInfoView.getTitleTh().getTitleTh()
                                    , customerInfoView.getFirstNameTh(), customerInfoView.getLastNameTh());
                        }

                        collateralAndGuarantorOfferLetterReport.setAddress(Util.checkNullString(newCollateralSubView.getAddress()));

                        log.debug("--mortgageTypeView. {},Size. {}",newCollateralSubView.getMortgageList(),newCollateralSubView.getMortgageList().size());
                        for (MortgageTypeView mortgageTypeView : newCollateralSubView.getMortgageList()){
                            collateralAndGuarantorOfferLetterReport.setMortgage(Util.checkNullString(mortgageTypeView.getMortgage()));
                        }

                        if (Util.safetyList(guarantorDetailViews).size() > 0){
                        log.debug("--Approved Guarantor Size. {}",collateralViews.size());
                            for(ProposeGuarantorInfoView guarantorDetailView : guarantorDetailViews){
                                ApprovedGuarantorOfferLetterReport approvedGuarantorOfferLetterReport = new ApprovedGuarantorOfferLetterReport();

                                guarantorName = new StringBuilder();
                                guarantorName = guarantorName.append(guarantorDetailView.getGuarantorName().getTitleTh().getTitleTh())
                                        .append(guarantorDetailView.getGuarantorName().getFirstNameTh())
                                        .append(SPACE).append(guarantorDetailView.getGuarantorName().getLastNameTh());

                                approvedGuarantorOfferLetterReport.setGuarantorName(Util.checkNullString(guarantorName.toString()));
                                approvedGuarantorOfferLetterReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(guarantorDetailView.getGuaranteeAmount()));
                                approvedGuarantorOfferLetterReports.add(approvedGuarantorOfferLetterReport);

                                collateralAndGuarantorOfferLetterReport.setApprovedGuarantorOfferLetterReport(approvedGuarantorOfferLetterReports);
                            }
                        } else {
                            log.debug("--Approved Guarantor is Null. [{}]",guarantorDetailViews);
                            ApprovedGuarantorOfferLetterReport approvedGuarantorOfferLetterReport = new ApprovedGuarantorOfferLetterReport();
                            approvedGuarantorOfferLetterReports.add(approvedGuarantorOfferLetterReport);
                            collateralAndGuarantorOfferLetterReport.setApprovedGuarantorOfferLetterReport(approvedGuarantorOfferLetterReports);
                        }

                        reports.add(collateralAndGuarantorOfferLetterReport); ///
                    }
                }
            }
        } else if (Util.safetyList(guarantorDetailViews).size() > 0){
            if (Util.safetyList(guarantorDetailViews).size() > 0){
            log.debug("--Approved Guarantor Size. {}",guarantorDetailViews.size());

            for(ProposeGuarantorInfoView guarantorDetailView : guarantorDetailViews){
                ApprovedGuarantorOfferLetterReport approvedGuarantorOfferLetterReport = new ApprovedGuarantorOfferLetterReport();
                ApprovedCollateralOfferLetterReport collateralAndGuarantorOfferLetterReport = new ApprovedCollateralOfferLetterReport();
                guarantorName = new StringBuilder();
                guarantorName = guarantorName.append(guarantorDetailView.getGuarantorName().getTitleTh().getTitleTh()).append(guarantorDetailView.getGuarantorName().getFirstNameTh())
                        .append(SPACE).append(guarantorDetailView.getGuarantorName().getLastNameTh());

                approvedGuarantorOfferLetterReport.setGuarantorName(Util.checkNullString(guarantorName.toString()));
                approvedGuarantorOfferLetterReport.setTotalLimitGuaranteeAmount(Util.convertNullToZERO(guarantorDetailView.getGuaranteeAmount()));
                approvedGuarantorOfferLetterReports.add(approvedGuarantorOfferLetterReport);

                collateralAndGuarantorOfferLetterReport.setApprovedGuarantorOfferLetterReport(approvedGuarantorOfferLetterReports);

                reports.add(collateralAndGuarantorOfferLetterReport);
                }
            }
        } else {
            log.debug("--Approved Collateral is Null. [{}],Approved Guarantor is Null. [{}]",collateralViews,guarantorDetailViews);
            ApprovedCollateralOfferLetterReport collateralAndGuarantorOfferLetterReport = new ApprovedCollateralOfferLetterReport();
            reports.add(collateralAndGuarantorOfferLetterReport);
            log.debug("-----------. {}",approvedGuarantorOfferLetterReports.toString());
        }

        log.debug("--Data fillGuarantor. {}",reports);
        return reports;
    }

    //เงื่อนไขติดตามหลังอนุมัติสินเชื่อ 16
    public List<FollowConditionOfferletterReport> fillFollowCondition(){
        List<FollowConditionOfferletterReport> reports = new ArrayList<FollowConditionOfferletterReport>();
        int count = 1;

        if (Util.safetyList(decisionView.getDecisionFollowConditionViewList()).size() > 0){
            log.debug("--DecisionFollowConditionViewList Size. {}", decisionView.getDecisionFollowConditionViewList().size());

            for (DecisionFollowConditionView followConditionView : decisionView.getDecisionFollowConditionViewList()){
                FollowConditionOfferletterReport conditionOfferletterReport = new FollowConditionOfferletterReport();
                conditionOfferletterReport.setCount(count++);
                conditionOfferletterReport.setName(Util.checkNullString(followConditionView.getConditionView().getName()));
                reports.add(conditionOfferletterReport);
            }
        } else {
            log.debug("--DecisionFollowConditionViewList is Null. {}", decisionView.getDecisionFollowConditionViewList().size());
            FollowConditionOfferletterReport conditionOfferletterReport = new FollowConditionOfferletterReport();
            reports.add(conditionOfferletterReport);

        }

        log.debug("--Data fillFollowCondition. {}",reports);
        return reports;
    }

    //fillFeecalculationAgreement 15/1
    public List<FeeCalculationOfferLetterReport> fillFeecalculationAgreement(){
        List<FeeCalculationOfferLetterReport> detailsAgreement = new ArrayList<FeeCalculationOfferLetterReport>();
        String PaymentMethod = "";
        int i = 1;

        if (Util.isSafetyList(feeCollectionDetails)){
            for (FeeCollectionDetail detail : feeCollectionDetails){
                FeeCalculationOfferLetterReport calculationOfferLetterReport = new FeeCalculationOfferLetterReport();

                if (detail.getPaymentMethod().getId() == 1){
                    calculationOfferLetterReport.setId(i++);
                    if (detail.getPaymentMethod().getDescription().indexOf(PaymentMethod) == -1) {
                        calculationOfferLetterReport.setPaymentMethod(msg.get("report.offerletter.paymentmethod"));
                    } else {
                        calculationOfferLetterReport.setPaymentMethod(SPACE);
                    }
                    calculationOfferLetterReport.setPaymentType(Util.checkNullString(detail.getFeeType().getDescription()));
                    calculationOfferLetterReport.setAmount(Util.convertNullToZERO(detail.getAmount()));
                }

                detailsAgreement.add(calculationOfferLetterReport);
            }
            log.debug("--fillFeecalculationAgreement. {}",detailsAgreement);
        } else {
            FeeCalculationOfferLetterReport calculationOfferLetterReport = new FeeCalculationOfferLetterReport();
            detailsAgreement.add(calculationOfferLetterReport);
            log.debug("--fillFeecalculationAgreement is Null. {}",detailsAgreement);
        }


        return detailsAgreement;
    }

    //fillFeecalculationNonAgreement 15/2
    public List<FeeCalculationOfferLetterReport> fillFeecalculationNonAgreement(){
        List<FeeCalculationOfferLetterReport> detailsAgreement = new ArrayList<FeeCalculationOfferLetterReport>();
        String PaymentMethod = "";
        int i = 1;

        if (Util.isSafetyList(feeCollectionDetails)) {
            for (FeeCollectionDetail detail : feeCollectionDetails){
                FeeCalculationOfferLetterReport calculationOfferLetterReport = new FeeCalculationOfferLetterReport();

                if (detail.getPaymentMethod().getId() == 3){
                    calculationOfferLetterReport.setId(i++);
                    if (detail.getPaymentMethod().getDescription().indexOf(PaymentMethod) == -1) {
                        calculationOfferLetterReport.setPaymentMethod(msg.get("report.offerletter.paymentnonmethod"));
                    } else {
                        calculationOfferLetterReport.setPaymentMethod(SPACE);
                    }
                    calculationOfferLetterReport.setPaymentType(Util.checkNullString(detail.getFeeType().getDescription()));
                    calculationOfferLetterReport.setAmount(Util.convertNullToZERO(detail.getAmount()));
                }

                detailsAgreement.add(calculationOfferLetterReport);
            }
            log.debug("--fillFeecalculationNonAgreement. {}",detailsAgreement);
        } else {
            FeeCalculationOfferLetterReport calculationOfferLetterReport = new FeeCalculationOfferLetterReport();
            detailsAgreement.add(calculationOfferLetterReport);
            log.debug("--fillFeecalculationNonAgreement is Null. {}",detailsAgreement);
        }

        return detailsAgreement;
    }

    //การเบิกใช้วงเงินกรณี Refinance หรือ ซื้อ-ขาย
    public List<DisbursementOfferLetterReport> fillDisbursment(){
        List<DisbursementOfferLetterReport> disbursementOfferLetterReports = new ArrayList<DisbursementOfferLetterReport>();
        Disbursement disbursement = disbursementDAO.findByWorkCaseId(workCaseId);

        if (!Util.isNull(disbursement)) {
            List<DisbursementMC> disbursementMCList = disbursementMCDAO.findByDisbursementId(disbursement.getId());
            if (Util.safetyList(disbursementMCList).size() > 0){
                log.debug("--disbursementMCList. {}",disbursementMCList);
                for (DisbursementMC name : disbursementMCList){
                    List<DisbursementMCCredit> mcCreditsDetail = disbursementMCCreditDAO.findByDisbursementMCId(name.getId());
                    if (Util.safetyList(mcCreditsDetail).size() > 0){
                        log.debug("--mcCreditsDetail. {}",mcCreditsDetail);
                        for (DisbursementMCCredit mcCredit : mcCreditsDetail){
                            if (Util.safetyList(mcCredit.getDisbursementMC().getDisbursementMCCreditList()).size() > 0){
                                log.debug("--DisbursementMCCreditList. {}",mcCredit.getDisbursementMC().getDisbursementMCCreditList());
                                for (DisbursementMCCredit credit : mcCredit.getDisbursementMC().getDisbursementMCCreditList()){
                                    DisbursementOfferLetterReport disbursementOfferLetterReport = new DisbursementOfferLetterReport();
                                    disbursementOfferLetterReport.setLoanPurPose(Util.checkNullString(credit.getCreditDetail().getLoanPurpose().getDescription())); //32
                                    disbursementOfferLetterReport.setProductProgram(Util.checkNullString(credit.getCreditDetail().getProductProgram().getName())); //32
                                    disbursementOfferLetterReport.setTotal(Util.convertNullToZERO(credit.getDisburseAmount())); //33
                                    disbursementOfferLetterReport.setName(msg.get("report.offerletter.mc"));  //34
                                    disbursementOfferLetterReport.setLimit(name.getPayeeName().getName());  //35
                                    disbursementOfferLetterReports.add(disbursementOfferLetterReport);
                                }
                            }
                        }
                    }
                }
            }
            List<DisbursementTR> disbursementTRList = disbursementTRDAO.findByDisbursementId(disbursement.getId());
            if (Util.safetyList(disbursementTRList).size() > 0){
                log.debug("--disbursementTRList. {}",disbursementTRList);
                for (DisbursementTR tr : disbursementTRList){
                    List<DisbursementTRCredit> trCredits = disbursementTRCreditDAO.findByDisbursementTRId(tr.getId());
                    if (Util.safetyList(trCredits).size() > 0){
                        log.debug("--trCredits. {}",trCredits);
                        for (DisbursementTRCredit credit : trCredits){
                            if (Util.safetyList(tr.getOpenAccount().getOpenAccountNameList()).size() > 0){
                                for (OpenAccountName accountName : tr.getOpenAccount().getOpenAccountNameList()){
                                    log.debug("--OpenAccountNameList(). {}",tr.getOpenAccount().getOpenAccountNameList());
                                    if (Util.safetyList(credit.getDisbursementTR().getDisbursementTRCreditList()).size() > 0){
                                        log.debug("--DisbursementTRCreditList. {}",credit.getDisbursementTR().getDisbursementTRCreditList());
                                        for (DisbursementTRCredit trCredit : credit.getDisbursementTR().getDisbursementTRCreditList()){
                                            DisbursementOfferLetterReport disbursementOfferLetterReport = new DisbursementOfferLetterReport();
                                            disbursementOfferLetterReport.setLoanPurPose(Util.checkNullString(trCredit.getCreditDetail().getLoanPurpose().getDescription()));  //32
                                            disbursementOfferLetterReport.setProductProgram(Util.checkNullString(trCredit.getCreditDetail().getProductProgram().getName())); //32
                                            disbursementOfferLetterReport.setTotal(credit.getDisburseAmount()); //33
                                            disbursementOfferLetterReport.setName(msg.get("report.offerletter.tr"));  //34
                                            disbursementOfferLetterReport.setLimit(Util.checkNullString(accountName.getCustomer().getNameEn())); //35
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            List<DisbursementBahtnet> disbursementBahtnetList = disbursementBahtnetDAO.findByDisbursementId(disbursement.getId());
            if (Util.safetyList(disbursementBahtnetList).size() > 0){
                log.debug("--disbursementBahtnetList. {}",disbursementBahtnetList);
                for (DisbursementBahtnet bahtnets : disbursementBahtnetList){
                    List<DisbursementBahtnetCredit> bahtnetCredits = disbursementBahtnetCreditDAO.findByDisbursementBahtnetId(bahtnets.getId());
                    if (Util.safetyList(bahtnetCredits).size() > 0){
                        log.debug("--bahtnetCredits. {}",bahtnetCredits);
                        for (DisbursementBahtnetCredit credit : bahtnetCredits){
                            if (Util.safetyList(credit.getDisbursementBahtnet().getDisburseBahtnetCreditList()).size() > 0){
                                log.debug("--DisburseBahtnetCreditList. {}",credit.getDisbursementBahtnet().getDisburseBahtnetCreditList());
                                for (DisbursementBahtnetCredit bahtnetCredit : credit.getDisbursementBahtnet().getDisburseBahtnetCreditList()){
                                    DisbursementOfferLetterReport disbursementOfferLetterReport = new DisbursementOfferLetterReport();
                                    disbursementOfferLetterReport.setLoanPurPose(Util.checkNullString(credit.getCreditDetail().getLoanPurpose().getDescription())); //32
                                    disbursementOfferLetterReport.setProductProgram(Util.checkNullString(bahtnetCredit.getCreditDetail().getProductProgram().getName())); //32
                                    disbursementOfferLetterReport.setTotal(Util.convertNullToZERO(credit.getDisburseAmount())); //33
                                    disbursementOfferLetterReport.setName(msg.get("report.offerletter.bahtnet"));  //34
                                    disbursementOfferLetterReport.setLimit(Util.checkNullString(bahtnets.getBeneficiaryName())); //35
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

            if (Util.safetyList(appHeaderView.getBorrowerHeaderViewList()).size() > 0){
                log.debug("--BorrowerHeaderViewList Size. {}",appHeaderView.getBorrowerHeaderViewList().size());

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

            if (detail.getPaymentMethod().getId() == 1){
                sumTotalAmt = Util.add(sumTotalAmt,detail.getAmount());
            } else  if (detail.getPaymentMethod().getId() == 3){
                sumTotalNonAmt = Util.add(sumTotalNonAmt,detail.getAmount());
            }
        }
        report.setSumFeecalculation(sumTotalAmt); //15/1
        report.setSumNonFeecalculation(sumTotalNonAmt); //15/2

        //14
        baseRateList = baseRateDAO.findAll();
        log.debug("--baseRateList. {}",baseRateList.size());
        for (int i = 1;i < baseRateList.size();i++){
            switch (i){
                case 1 : report.setValueMLR(baseRateList.get(i).getValue()); break;
                case 2 : report.setValueMOR(baseRateList.get(i).getValue()); break;
                case 3 : report.setValueMRR(baseRateList.get(i).getValue()); break;
            }
        }

        String[] spDate = Util.checkNullString(Util.createDateTh(baseRateList.get(1).getAddOfDate())).split("/");
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
        dateValue = dateValue.append(spDate[0]).append(SPACE).append(setMonth).append(SPACE).append(spDate[2]);
        log.debug("--DATE. {}",dateValue.toString());
        report.setDateValue(dateValue.toString());

        //2,24,25
        List<OfferLetter> offerLetter = offerLetterDAO.findAll();
        log.debug("--offerLetter. {}",offerLetter.size());
        if (Util.isSafetyList(offerLetter)){
            log.debug("--offerLetter. {}",offerLetter);
            for (OfferLetter letter : offerLetter){
                report.setTeamName(Util.checkNullString(letter.getTeamName()));
                report.setTelPhone(Util.checkNullString(letter.getTelPhone()));
                report.setTelFax(Util.checkNullString(letter.getTelFax()));
            }
        } else {
            log.debug("--offerLetter is Null",offerLetter);
        }

        if (workCaseId > 0){
            AgreementInfo agreementInfo = agreementInfoDAO.findByWorkCaseId(workCaseId);
            customerId = Util.parseLong(FacesUtil.getFlash().get("customerId"),-1L);

            if (!Util.isNull(agreementInfo)) {
                log.debug("--agreementInfo. {}",agreementInfo);
                if (!Util.isNull(agreementInfo.getLoanContractDate())) {
                    loanDate =  Util.checkNullString(Util.createDateTh(agreementInfo.getLoanContractDate()));
                    report.setLoanDate(loanDate); //27.1
                }

                if (!Util.isNull(agreementInfo.getLoanContractDate())) {
                    loanTime = Util.checkNullString(Util.createTime(agreementInfo.getLoanContractDate()));
                    report.setLoanTime(loanTime); //28.1
                }

                //29.1
                if (!Util.isNull(agreementInfo.getSigningLocation())) {
                    log.debug("SigningLocation(). {}",agreementInfo.getSigningLocation());
                    switch (agreementInfo.getSigningLocation()) {
                        case BRANCH :
                            if (agreementInfo.getBankBranch() != null)
                                report.setLoanLocation(Util.checkNullString(agreementInfo.getBankBranch().getName()));
                            break;
                        case ZONE :
                            if (agreementInfo.getUserZone() != null)
                                report.setLoanLocation(Util.checkNullString(agreementInfo.getUserZone().getName()));
                            break;
                        default : //DO NOTHING
                            break;
                    }
                }             }

            //30.1
            List<Customer> list = customerDAO.findCustomerByCommitteeId(customerId);
            log.debug("Customer findCustomerByCommitteeId. {}",list);

            if (Util.isSafetyList(list)){
                customerName = customerName.append(Util.checkNullString(list.get(1).getTitle().getTitleTh()))
                        .append(Util.checkNullString(list.get(1).getNameTh())).append(Util.checkNullString(list.get(1).getLastNameTh()));
                report.setLoanCustomerName(Util.checkNullString(customerName.toString()));
            } else {
                log.debug("Customer findCustomerByCommitteeId. {}",list);
            }

            List<MortgageInfo> mortgageInfoList = mortgageInfoDAO.findAllByWorkCaseId(workCaseId);
            log.debug("--mortgageInfoList. {}",mortgageInfoList.size());
            if (Util.isSafetyList(mortgageInfoList)){
                if (!Util.isNull(mortgageInfoList.get(1).getMortgageSigningDate())) {
                    mortgageDate = Util.checkNullString(Util.createDateTh(mortgageInfoList.get(1).getMortgageSigningDate()));
                    report.setMortgageDate(mortgageDate); //27.2
                }

                if (!Util.isNull(mortgageInfoList.get(1).getMortgageSigningDate())) {
                    mortgageTime = Util.checkNullString(Util.createTime(mortgageInfoList.get(1).getMortgageSigningDate()));
                    report.setMortgageTime(mortgageTime); //28.2
                }
                report.setMortgageLocation(Util.checkNullString(mortgageInfoList.get(1).getMortgageLandOffice().getName())); //29.2
            }

            Map<String,Object> params =  FacesUtil.getParamMapFromFlash("mortgageParams");
            mortgageId = Util.parseLong(params.get("mortgageId"),-1);
            if (mortgageId <= 0) {
                mortgageId = Util.parseLong(FacesUtil.getParameter("mortgageId"),-1);
            }
            log.debug("--mortgageId. {}",mortgageId);

            List<MortgageInfoCollSubView> collSubViews = mortgageDetailControl.getMortgageInfoCollSubList(mortgageId);
            if (Util.safetyList(collSubViews).size() > 0) {
                mortCustomerName = Util.checkNullString(collSubViews.get(1).getOwner());
                report.setMortgageCustomerName(mortCustomerName); //30.2
            }

            List<Customer> customers = customerDAO.findCustomerCanBePOA(workCaseId);
            if (Util.isSafetyList(customers)) {
                log.debug("--customers findCustomerCanBePOA.{}",customers);
                for (Customer view : customers) {
                    Individual individual = view.getIndividual();
                    if (individual.getAttorneyRequired().equals(RadioValue.YES)) {
                        customerAuthorized = customerAuthorized.append(Util.checkNullString(view.getTitle().getTitleTh()))
                                .append(Util.checkNullString(view.getNameTh())).append(Util.checkNullString(view.getLastNameTh()));
                        report.setMortgageAuthorized(customerAuthorized.toString()); //31.2
                        log.debug("--customerAuthorized. {}",customerAuthorized);
                    }
                }
            }
        }

        log.debug("--fillMaster",report.toString());
        return report;
    }
}
