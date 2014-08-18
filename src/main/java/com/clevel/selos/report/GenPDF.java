package com.clevel.selos.report;

import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.ReportView;
import com.clevel.selos.report.template.PDFAppraisalAppointment;
import com.clevel.selos.report.template.PDFExecutiveSummaryAndOpSheet;
import com.clevel.selos.report.template.PDFOfferLetter;
import com.clevel.selos.report.template.PDFRejectLetter;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

@ViewScoped
@ManagedBean(name = "report")
public class GenPDF extends ReportService implements Serializable {

    @Inject
    @Config(name = "report.exsum")
    String pathExsum;

    @Inject
    @Config(name = "report.decision")
    String pathDecision;

    @Inject
    @Config(name = "report.subreport")
    String pathsub;

    @Inject
    @Config(name = "report.rejectletter.policy")
    String pathPolicyRejectLetter;

    @Inject
    @Config(name = "report.rejectletter.ncb")
    String pathNCBRejectLetter;

    @Inject
    @Config(name = "report.rejectletter.income")
    String pathIncomeRejectLetter;

    @Inject
    @Config(name = "report.rejectletter.policyincome")
    String pathPolicyIncomeRejectLetter;


    @Inject
    @Config(name = "report.appraisal")
    String pathAppraisal;

    @Inject
    @Config(name = "report.offerletter")
    String pathOfferLetter;

    @Inject private WorkCaseDAO workCaseDAO;
    @Inject private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject PDFExecutiveSummaryAndOpSheet pdfExecutiveSummary;
    @Inject PDFRejectLetter pdfReject_letter;
    @Inject PDFAppraisalAppointment pdfAppraisalAppointment;
    @Inject PDFOfferLetter pdfOfferLetter;

    private  WorkCase workCase; // ห้าม @Inject
    private WorkCasePrescreen workCasePrescreen;
    private ReportView reportView;
    private long workCaseId;
    private long workCasePreScreenId;
    private boolean rejectType;
    private boolean exsumType;
    private boolean opshectType;
    private boolean appraisalType;
    private User user;
    private boolean readonlyIsUW;
    private boolean readonlyIsBDM;
    private boolean readonlyIsABDM;
    private boolean readonlyIsZM;
    private boolean readonlyIsRGM;
    private boolean readonlyIsGH;
    private boolean readonlyIsCSSO;
    private boolean readonlyIsAAD_ADMIN;
    private boolean readonlyIsAAD_COMMITTEE;
    private boolean readonlyIsSSO;

    private boolean readonlyContec_Center;
    private boolean readonlyInsurance_Center;
    private boolean readonlyDoc_Check;
    private boolean readonlyTCG_Team;  //ยังไม่มี
    private boolean readonlyCDM;
    private boolean readonlyLAR_BC;
    private boolean readonlyCO1;
    private boolean readonlyCO2;
    private boolean readonlyLS_Setup_Limit; //ยังไม่มี
    private boolean readonlyLS_PreDisbursme; //ยังไม่มี
    private boolean readonlyLD;
    private boolean readonlyViewer;

    private String appNumber;



    @Inject
    private STPExecutor stpExecutor;

    public GenPDF() {

    }

    public void init(){
        log.debug("init() {[]}");
        HttpSession session = FacesUtil.getSession(false);

        if(!Util.isNull(session.getAttribute("workCaseId"))){
            workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
            log.debug("workCaseId. {}",workCaseId);
        }else if (!Util.isNull(session.getAttribute("workCasePreScreenId"))){
            workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
        }else{
            log.debug("workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
            }catch (Exception ex){
                log.error("Exception :: {}",ex);
            }
        }
    }

    @PostConstruct
    private void onCreation(){
        init();
        reportView = new ReportView();
        HttpSession session = FacesUtil.getSession(false);
        user = (User)session.getAttribute("user");
        log.debug("GenPDF onCreation and New ReportView");
        onCheckRole();
    }

    private void onCheckRole(){
        readonlyIsUW = user.getRole().getId() == RoleValue.UW.id();
        readonlyIsBDM = user.getRole().getId() == RoleValue.BDM.id();
        readonlyIsABDM = user.getRole().getId() == RoleValue.ABDM.id();
        readonlyIsZM = user.getRole().getId() == RoleValue.ZM.id();
        readonlyIsRGM = user.getRole().getId() == RoleValue.RGM.id();
        readonlyIsGH = user.getRole().getId() == RoleValue.GH.id();
        readonlyIsCSSO = user.getRole().getId() == RoleValue.CSSO.id();
        readonlyIsAAD_ADMIN = user.getRole().getId() == RoleValue.AAD_ADMIN.id();
        readonlyIsAAD_COMMITTEE = user.getRole().getId() == RoleValue.AAD_COMITTEE.id();
        readonlyIsSSO = user.getRole().getId() == RoleValue.SSO.id();
        readonlyContec_Center = user.getRole().getId() == RoleValue.CONTACT_CENTER.id();
        readonlyInsurance_Center = user.getRole().getId() == RoleValue.INSURANCE_CENTER.id();
        readonlyDoc_Check = user.getRole().getId() == RoleValue.DOC_CHECK.id();
        readonlyCDM = user.getRole().getId() == RoleValue.CDM.id();
        readonlyLAR_BC = user.getRole().getId() == RoleValue.LAR_BC.id();
        readonlyCO1 = user.getRole().getId() == RoleValue.CO1.id();
        readonlyCO2 = user.getRole().getId() == RoleValue.CO2.id();
        readonlyLD = user.getRole().getId() == RoleValue.LD.id();
        readonlyViewer = user.getRole().getId() == RoleValue.VIEWER.id();

    }

    public void setNameReport(){
        init();
        log.info("On setNameReport()");
        String date = Util.createDateTime(new Date());
        rejectType = false;
        exsumType = false;
        opshectType = false;
        appraisalType = false;
        readonlyContec_Center = false;
        readonlyInsurance_Center = false;
        readonlyDoc_Check = false;
        readonlyCDM = false;
        readonlyLAR_BC = false;
        readonlyCO1 = false;
        readonlyCO2 = false;
        readonlyLD = false;

        if(!Util.isNull(workCaseId) || !Util.isNull(workCasePreScreenId)){
            if (!Util.isZero(workCaseId)) {
                workCase = workCaseDAO.findById(workCaseId);
                appNumber = workCase.getAppNumber();
            } else if (!Util.isZero(workCasePreScreenId)){
                workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
                appNumber = workCasePrescreen.getAppNumber();
            }

            StringBuilder nameOpShect = new StringBuilder();
            nameOpShect = nameOpShect.append(appNumber).append("_").append(date).append("_OpSheet.pdf");

            StringBuilder nameExSum = new StringBuilder();
            nameExSum = nameExSum.append(appNumber).append("_").append(date).append("_ExSum.pdf");

            StringBuilder nameRejectLetter = new StringBuilder();
            nameRejectLetter = nameRejectLetter.append(appNumber).append("_").append(date).append("_RejectLetter.pdf");

            StringBuilder nameAppraisal = new StringBuilder();
            nameAppraisal = nameAppraisal.append(appNumber).append("_").append(date).append("_AADRequest.pdf");

            StringBuilder nameOfferLetter = new StringBuilder();
            nameOfferLetter = nameOfferLetter.append(appNumber).append("_").append(date).append("_OfferLetter.pdf");

            // ###### Role AAD Can not print Opshect And Exsum , Role UW Can not print Appraisal Request And Reject Letter ######
            if (readonlyIsAAD_ADMIN || readonlyIsAAD_COMMITTEE){
                exsumType = true;
                opshectType = true;
                log.debug("Is role AAD Admin. [{}], Is role AAD Committee. [{}]",readonlyIsAAD_ADMIN,readonlyIsAAD_COMMITTEE);
            } else if (readonlyIsUW || readonlyContec_Center || readonlyInsurance_Center || readonlyDoc_Check || readonlyCDM ||
                    readonlyLAR_BC || readonlyCO1 || readonlyCO2 || readonlyLD){
                appraisalType = true;
                rejectType = true;
                log.debug("Is role UW. [{}] ,Is role Contect Center. [{}] ,Is role Insurance Center. [{}] ,Is role Doc Check. [{}] ,Is role CDM. [{}] ,Is role LAR/BC. [{}] ," +
                        "Is role CO1. [{}] ,Is role CO2. [{}] ,Is role LD. [{}]",readonlyIsUW,readonlyContec_Center,readonlyInsurance_Center,readonlyDoc_Check,readonlyCDM,
                        readonlyLAR_BC,readonlyCO1,readonlyCO2,readonlyLD);
            }

            // ###### Request Appraisal is Zero in WorkCase OR WorkCasePrcescreen can not print Appraisal Request
            if (Util.isZero(workCase.getRequestAppraisal()) || Util.isZero(workCasePrescreen.getRequestAppraisal())){
                appraisalType = true;
                log.debug("No Submit Request Appraisal to WorkCase. [{}],No Submit Request Appraisal to WorkCasePreScreen. [{}]",workCase.getRequestAppraisal(),workCasePrescreen.getRequestAppraisal());
            }

            // ###### Reject_Group in UwresultDetail is Null ######
            pdfReject_letter.init();
            if(Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    Util.isZero(pdfReject_letter.typeReport().getTypeIncome())){
                log.debug("--Reject Group is Null. NCB [{}] , Policy [{}], Income [{}]",pdfReject_letter.typeReport().getTypeNCB(),pdfReject_letter.typeReport().getTypePolicy(),pdfReject_letter.typeReport().getTypeIncome());
                rejectType = true;
            }

            reportView.setNameReportOpShect(nameOpShect.toString());
            reportView.setNameReportExSum(nameExSum.toString());
            reportView.setNameReportAppralsal(nameAppraisal.toString());
            reportView.setNameReportOfferLetter(nameOfferLetter.toString());
            reportView.setNameReportRejectLetter(nameRejectLetter.toString());
        }
    }

    public void onPrintExsumReport() throws Exception {
        log.debug("onPrintExsumReport");

        pdfExecutiveSummary.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("borrower", pdfExecutiveSummary.fillBorrowerRelatedProfile());
        map.put("businessLocation", pdfExecutiveSummary.fillBorrower());
        map.put("tradeFinance", pdfExecutiveSummary.fillTradeFinance());
        map.put("borrowerCharacteristic", pdfExecutiveSummary.fillBorrowerCharacteristic());
        map.put("ncbRecord", pdfExecutiveSummary.fillNCBRecord());
        map.put("accountMovement", pdfExecutiveSummary.fillAccountMovement());
        map.put("collateral", pdfExecutiveSummary.fillCollateral());
        map.put("creditRisk", pdfExecutiveSummary.fillBorrowerRelatedProfile());
        map.put("bizSupport", pdfExecutiveSummary.fillBizSupport());
        map.put("uwDecision", pdfExecutiveSummary.fillUWDecision());
        map.put("creditRisk", pdfExecutiveSummary.fillCreditRisk());
        map.put("fillDecision", pdfExecutiveSummary.fillDecision());
        map.put("fillHeader",pdfExecutiveSummary.fillHeader());
        map.put("fillFooter",pdfExecutiveSummary.fillFooter());
        map.put("fillCreditBorrower",pdfExecutiveSummary.fillCreditBorrower(pathsub));
        map.put("fillCondition",pdfExecutiveSummary.fillCondition());
        map.put("fillBorrowerRetail",pdfExecutiveSummary.fillBorrowerRetail(pathsub));
        map.put("fillAppInRLOS",pdfExecutiveSummary.fillAppInRLOS(pathsub));
        map.put("fillRelatedCommercial",pdfExecutiveSummary.fillRelatedCommercial(pathsub));
        map.put("fillRelatedRetail",pdfExecutiveSummary.fillRelatedRetail(pathsub));
        map.put("fillRelatedAppInRLOS",pdfExecutiveSummary.fillRelatedAppInRLOS(pathsub));
        map.put("fillExistingCollateralBorrower",pdfExecutiveSummary.fillExistingCollateralBorrower(pathsub));
        map.put("fillExistingCollateralRelated",pdfExecutiveSummary.fillExistingCollateralRelated(pathsub));
        map.put("fillGuarantorBorrower",pdfExecutiveSummary.fillGuarantorBorrower(pathsub));
        map.put("fillProposedCredit",pdfExecutiveSummary.fillProposedCredit(pathsub));
        map.put("fillApprovedCredit",pdfExecutiveSummary.fillExSumApprovedCredit(pathsub));
        map.put("fillProposeFeeInformation",pdfExecutiveSummary.fillProposeFeeInformation());
        map.put("fillProposedCollateral",pdfExecutiveSummary.fillProposedCollateral(pathsub));
        map.put("fillApprovedCollaterral",pdfExecutiveSummary.fillExSumApprovedCollaterral(pathsub));
        map.put("fillProposedGuarantor",pdfExecutiveSummary.fillProposedGuarantor(pathsub));
        map.put("fillApprovedGuarantor",pdfExecutiveSummary.fillExSumApprovedGuarantor(pathsub));
        map.put("fillFollowUpCondition",pdfExecutiveSummary.fillFollowUpCondition());
        map.put("fillApprovalHistory",pdfExecutiveSummary.fillApprovalHistory());
        map.put("fillTotalMasterReport",pdfExecutiveSummary.fillTotalMasterReport("all"));
        map.put("fillFollowDetail",pdfExecutiveSummary.fillFollowDetail());
        map.put("fillPriceFee",pdfExecutiveSummary.fillPriceFee());

        generatePDF(pathExsum, map, reportView.getNameReportExSum(),null);
    }

    public void onPrintDecisionReport() throws Exception {
        log.debug("onPrintDecisionReport");

        pdfExecutiveSummary.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("borrower", pdfExecutiveSummary.fillBorrowerRelatedProfile());
        map.put("businessLocation", pdfExecutiveSummary.fillBorrower());
        map.put("tradeFinance", pdfExecutiveSummary.fillTradeFinance());
        map.put("borrowerCharacteristic", pdfExecutiveSummary.fillBorrowerCharacteristic());
        map.put("ncbRecord", pdfExecutiveSummary.fillNCBRecord());
        map.put("accountMovement", pdfExecutiveSummary.fillAccountMovement());
        map.put("collateral", pdfExecutiveSummary.fillCollateral());
        map.put("creditRisk", pdfExecutiveSummary.fillBorrowerRelatedProfile());
        map.put("uwDecision", pdfExecutiveSummary.fillUWDecision());
        map.put("creditRisk", pdfExecutiveSummary.fillCreditRisk());
        map.put("fillHeader",pdfExecutiveSummary.fillHeader());
        map.put("fillFooter",pdfExecutiveSummary.fillFooter());
        map.put("fillCreditBorrower",pdfExecutiveSummary.fillCreditBorrower(pathsub));
        map.put("fillCondition",pdfExecutiveSummary.fillCondition());
        map.put("fillBorrowerRetail",pdfExecutiveSummary.fillBorrowerRetail(pathsub));
        map.put("fillRelatedCommercial",pdfExecutiveSummary.fillRelatedCommercial(pathsub));
        map.put("fillRelatedRetail",pdfExecutiveSummary.fillRelatedRetail(pathsub));
        map.put("fillExistingCollateralBorrower",pdfExecutiveSummary.fillExistingCollateralBorrower(pathsub));
        map.put("fillExistingCollateralRelated",pdfExecutiveSummary.fillExistingCollateralRelated(pathsub));
        map.put("fillGuarantorBorrower",pdfExecutiveSummary.fillGuarantorBorrower(pathsub));
        map.put("fillApprovedCredit",pdfExecutiveSummary.fillApprovedCredit(pathsub));
        map.put("fillProposeFeeInformation",pdfExecutiveSummary.fillProposeFeeInformation());
        map.put("fillApprovedCollaterral",pdfExecutiveSummary.fillApprovedCollaterral(pathsub));
        map.put("fillApprovedGuarantor",pdfExecutiveSummary.fillApprovedGuarantor(pathsub));
        map.put("fillFollowUpCondition",pdfExecutiveSummary.fillFollowUpCondition());
        map.put("fillApprovalHistory",pdfExecutiveSummary.fillApprovalHistory());
        map.put("fillTotalMasterReport",pdfExecutiveSummary.fillTotalMasterReport("opsheet"));
        map.put("fillFollowDetail",pdfExecutiveSummary.fillFollowDetail());
        map.put("fillPriceFee",pdfExecutiveSummary.fillPriceFee());
        map.put("fillApprovalHistory",pdfExecutiveSummary.fillApprovalHistory());

        generatePDF(pathDecision, map, reportView.getNameReportOpShect(),null);
    }

    public void onPrintRejectLetter() throws Exception {
        log.debug("--onPrintRejectLetter");
        pdfReject_letter.init();
        String pathReportReject = null;
        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);

        if (!Util.isNull( pdfReject_letter.typeReport())){
            if (!Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && !Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    !Util.isZero(pdfReject_letter.typeReport().getTypeIncome()) ||
                    Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && !Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    !Util.isZero(pdfReject_letter.typeReport().getTypeIncome())){
                pathReportReject =  pathPolicyIncomeRejectLetter;
                log.debug("--path4. {}",pathReportReject);
            } else if (!Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    !Util.isZero(pdfReject_letter.typeReport().getTypeIncome()) ||
                    Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    !Util.isZero(pdfReject_letter.typeReport().getTypeIncome())){
                pathReportReject =  pathIncomeRejectLetter;
                log.debug("--path3. {}",pathReportReject);
            } else if(Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && !Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    Util.isZero(pdfReject_letter.typeReport().getTypeIncome()) ||
                    !Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && !Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    Util.isZero(pdfReject_letter.typeReport().getTypeIncome())){
                pathReportReject =  pathPolicyRejectLetter;
                log.debug("--path2. {}",pathReportReject);
            } else if (!Util.isZero(pdfReject_letter.typeReport().getTypeNCB()) && Util.isZero(pdfReject_letter.typeReport().getTypePolicy()) &&
                    Util.isZero(pdfReject_letter.typeReport().getTypeIncome())){
                pathReportReject = pathNCBRejectLetter;
                log.debug("--path1. {}",pathReportReject);
            }
            map.put("fillAllNameReject", pdfReject_letter.fillAllNameReject());
            map.put("fillRejectLetter",pdfReject_letter.fillRejectLetter());

            generatePDF(pathReportReject,map,reportView.getNameReportRejectLetter(),null);
        } else {
            log.debug("--RejectGroup is Null");
        }
    }
    public void onPrintAppraisal() throws Exception {
        log.debug("--onPrintAppraisal");
        pdfAppraisalAppointment.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("fillAppraisalDetailReport",pdfAppraisalAppointment.fillAppraisalDetailReport());
        map.put("fillAppraisalDetailViewReport",pdfAppraisalAppointment.fillAppraisalDetailViewReport(pathsub));
        map.put("fillAppraisalContactDetailViewReport",pdfAppraisalAppointment.fillAppraisalContactDetailViewReport());
        map.put("fillContactRecordDetailViewReport",pdfAppraisalAppointment.fillContactRecordDetailViewReport());

        generatePDF(pathAppraisal,map,reportView.getNameReportAppralsal(),null);
    }

    public void onPrintOfferletter() throws Exception {
        log.debug("--onPrintOfferletter");
        pdfOfferLetter.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path",pathsub);
        map.put("fillApprovedCredit",pdfOfferLetter.fillApprovedCredit());
        map.put("fillGuarantor",pdfOfferLetter.fillGuarantor(pathsub));
        map.put("fillFollowCondition",pdfOfferLetter.fillFollowCondition());
        map.put("fillMasterOfferLetter",pdfOfferLetter.fillMasterOfferLetter());
        map.put("fillFeecalculationAgreement",pdfOfferLetter.fillFeecalculationAgreement());
        map.put("fillFeecalculationNonAgreement",pdfOfferLetter.fillFeecalculationNonAgreement());
        map.put("fillDisbursment",pdfOfferLetter.fillDisbursment());

        generatePDF(pathOfferLetter, map, reportView.getNameReportOfferLetter(),null);
    }

    public ReportView getReportView() {
        return reportView;
    }

    public void setReportView(ReportView reportView) {
        this.reportView = reportView;
    }

    public boolean isRejectType() {
        return rejectType;
    }

    public void setRejectType(boolean rejectType) {
        this.rejectType = rejectType;
    }

    public boolean isAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(boolean appraisalType) {
        this.appraisalType = appraisalType;
    }

    public boolean isExsumType() {
        return exsumType;
    }

    public void setExsumType(boolean exsumType) {
        this.exsumType = exsumType;
    }

    public boolean isOpshectType() {
        return opshectType;
    }

    public void setOpshectType(boolean opshectType) {
        this.opshectType = opshectType;
    }
}
