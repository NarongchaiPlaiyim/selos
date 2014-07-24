package com.clevel.selos.report.template;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.report.OfferLetterDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BorrowerType;
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
//    @Inject private PostCustomerInfoJurisControl postCustomerInfoJurisControl;
    @Inject private CustomerDAO customerDAO;
    @Inject private MortgageInfoDAO mortgageInfoDAO;
    @Inject private MortgageDetailControl mortgageDetailControl;
    @Inject private IndividualDAO individualDAO;
    @Inject private DisbursementMCDAO disbursementMCDAO;
    @Inject private DisbursementTRDAO disbursementTRDAO;
    @Inject private DisbursementBahtnetDAO disbursementBahtnetDAO;

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
                        approvedCredit.setInstallmentType(msg.get("report.credittype.one"));
                    } else if (view.getCreditTypeView().getCreditGroup() == 1){
                        approvedCredit.setInstallmentType(msg.get("report.credittype.two"));
                    } else if (view.getCreditTypeView().getCreditGroup() == 2){
                        approvedCredit.setInstallmentType(msg.get("report.credittype.three"));
                    } else if (view.getCreditTypeView().getCreditGroup() == 3){
                        approvedCredit.setInstallmentType(msg.get("report.credittype.three"));
                    } else if (view.getCreditTypeView().getCreditGroup() == 4){
                        approvedCredit.setInstallmentType(msg.get("report.credittype.three"));
                    } else if (view.getCreditTypeView().getCreditGroup() == 5){
                        approvedCredit.setInstallmentType(msg.get("report.credittype.three"));
                    } else if (view.getCreditTypeView().getCreditGroup() == 6){
                        approvedCredit.setInstallmentType(msg.get("report.credittype.three"));
                    } else if (view.getCreditTypeView().getCreditGroup() == 7){
                        approvedCredit.setInstallmentType(msg.get("report.credittype.one"));
                    } else if (view.getCreditTypeView().getCreditGroup() == 8){
                        approvedCredit.setInstallmentType(msg.get("report.credittype.three"));
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
                        calculationOfferLetterReport.setPaymentMethod(msg.get("report.feecalculation.paymentmethod"));
                        PaymentMethod = Util.checkNullString(detail.getPaymentMethod().getDescription());
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
                        calculationOfferLetterReport.setPaymentMethod(msg.get("report.feecalculation.paymentnonmethod"));
                        PaymentMethod = Util.checkNullString(detail.getPaymentMethod().getDescription());
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
        DisbursementInfoView disbursementInfoView = new DisbursementInfoView();
        Disbursement disbursement = disbursementDAO.findByWorkCaseId(workCaseId);

        if (!Util.isNull(disbursement)) {
            disbursementInfoView.setDisburseMcList(disbursementControl.getDisbursementMcDetailView(disbursement.getId()));
            List<DisbursementMC> disbursementMCList = disbursementMCDAO.findByDisbursementId(disbursement.getId());

            for (DisbursementMC name : disbursementMCList){
                DisbursementOfferLetterReport disbursementOfferLetterReport = new DisbursementOfferLetterReport();
                disbursementOfferLetterReport.setLimit(name.getPayeeName().getName());  //35

                for (DisbursementMcDetailView mc : disbursementInfoView.getDisburseMcList()){
                    disbursementOfferLetterReport.setName(msg.get("app.disbursement.detail.mc"));  //34
                    disbursementOfferLetterReport.setTotal(Util.convertNullToZERO(mc.getTotalAmount()));  //32

                    for (DisbursementCreditTypeView mcDetail : mc.getDisbursementCreditTypeView()){
                        disbursementOfferLetterReport.setProductProgram(Util.checkNullString(mcDetail.getProductProgram()));  //32
                    }
                    disbursementOfferLetterReports.add(disbursementOfferLetterReport);
                }
            }

            disbursementInfoView.setDisburseDepositList(disbursementControl.getDisbursementDepositBaDetailView(disbursement.getId()));
            List<DisbursementTR> disbursementTRList = disbursementTRDAO.findByDisbursementId(disbursement.getId());

            for (DisbursementTR tr : disbursementTRList){
                DisbursementOfferLetterReport disbursementOfferLetterReport = new DisbursementOfferLetterReport();
                List<OpenAccountName> openAccountNameList = tr.getOpenAccount().getOpenAccountNameList();
                for (OpenAccountName accountName : openAccountNameList){
                    disbursementOfferLetterReport.setLimit(Util.checkNullString(accountName.getCustomer().getNameEn()));  //35
                }

                for (DisbursementDepositBaDetailView deposit : disbursementInfoView.getDisburseDepositList()){
                    disbursementOfferLetterReport.setName(msg.get("app.disbursement.detail.deposit"));  //34
                    disbursementOfferLetterReport.setTotal(Util.convertNullToZERO(deposit.getTotalAmount()));  //32

                    for (DisbursementCreditTypeView depositDetail : deposit.getDisbursementCreditTypeView()){
                        disbursementOfferLetterReport.setProductProgram(Util.checkNullString(depositDetail.getProductProgram()));  //32
                    }
                    disbursementOfferLetterReports.add(disbursementOfferLetterReport);
                }
            }

            disbursementInfoView.setDisbursementBahtnetList(disbursementControl.getDisbursementBahtnetDetailView(disbursement.getId()));
            List<DisbursementBahtnet> disbursementBahtnetList = disbursementBahtnetDAO.findByDisbursementId(disbursement.getId());

            for (DisbursementBahtnet disbursementBahtnet : disbursementBahtnetList){
                DisbursementOfferLetterReport disbursementOfferLetterReport = new DisbursementOfferLetterReport();
                disbursementOfferLetterReport.setLimit(Util.checkNullString(disbursementBahtnet.getBeneficiaryName()));  //35

                for (DisbursementBahtnetDetailView bahtnet : disbursementInfoView.getDisbursementBahtnetList()){
                    disbursementOfferLetterReport.setName(msg.get("app.disbursement.detail.bahtnet"));  //34
                    disbursementOfferLetterReport.setTotal(Util.convertNullToZERO(bahtnet.getTotalAmount()));  //32

                    for (DisbursementCreditTypeView bahtnetDetail : bahtnet.getDisbursementCreditTypeView()){
                        disbursementOfferLetterReport.setProductProgram(Util.checkNullString(bahtnetDetail.getProductProgram()));  //32
                    }
                    disbursementOfferLetterReports.add(disbursementOfferLetterReport);
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
        String loanDate = SPACE;
        String loanTime = SPACE;
        String mortgageDate = SPACE;
        String mortgageTime = SPACE;
        String mortCustomerName = SPACE;
        StringBuilder customerName = new StringBuilder();
        long mortgageId = -1;
        StringBuilder customerAuthorized = new StringBuilder();

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
//            MortgageSummary mortgage = mortgageSummaryDAO.findByWorkCaseId(workCaseId);
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
            log.debug("--MortgageInfoCollSubView. {}",collSubViews.size());
            if (Util.safetyList(collSubViews).size() > 0) {
                mortCustomerName = Util.checkNullString(collSubViews.get(1).getOwner());
                report.setMortgageCustomerName(mortCustomerName); //30.2
            }

            List<Customer> customers = customerDAO.findCustomerCanBePOA(workCaseId);
            if (Util.isSafetyList(customers) && Util.safetyList(customers).size() > 0) {
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
