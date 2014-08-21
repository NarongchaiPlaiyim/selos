package com.clevel.selos.report;

import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.dao.working.CancelRejectInfoDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.model.RoleValue;
import com.clevel.selos.model.StatusValue;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.CancelRejectInfo;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.report.RejectLetterCancelCodeByExSum;
import com.clevel.selos.model.report.RejectLetterReport;
import com.clevel.selos.model.view.ReportView;
import com.clevel.selos.report.template.PDFAppraisalAppointment;
import com.clevel.selos.report.template.PDFExecutiveSummaryAndOpSheet;
import com.clevel.selos.report.template.PDFOfferLetter;
import com.clevel.selos.report.template.PDFRejectLetter;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.ArrayUtils;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    @Inject CancelRejectInfoDAO cancelRejectInfoDAO;

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
    private CancelRejectInfo cancelRejectInfo;
    private long statusId;
    private  String pathReportReject;
    private String messageHeader;
    private String message;
    private RejectLetterReport rejectLetterReport;
    private RejectLetterCancelCodeByExSum codeByExSum;
    HttpSession session;

    @Inject
    private STPExecutor stpExecutor;

    public GenPDF() {

    }

    public void init(){
        log.debug("init() {[]}");
        cancelRejectInfo = new CancelRejectInfo();
        rejectLetterReport =  (RejectLetterReport)session.getAttribute("rejectLetterReport");
        codeByExSum = (RejectLetterCancelCodeByExSum)session.getAttribute("cancelCodeRejectByExSum");

        if(!Util.isNull(session.getAttribute("workCaseId"))){
            workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
            if (!Util.isZero(workCaseId)){
                cancelRejectInfo = cancelRejectInfoDAO.findByWorkCaseId(workCaseId);
            }
            log.debug("workCaseId. {}",workCaseId);
        }else if (!Util.isNull(session.getAttribute("workCasePreScreenId"))){
            workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
            if (!Util.isZero(workCasePreScreenId)){
                cancelRejectInfo = cancelRejectInfoDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            }
            log.debug("workCasePreScreenId. {}",workCasePreScreenId);
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
        session = FacesUtil.getSession(false);
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

        if(!Util.isNull(workCaseId) || !Util.isNull(workCasePreScreenId)){
            if (!Util.isZero(workCaseId)) {
                workCase = workCaseDAO.findById(workCaseId);
                if(!Util.isNull(workCase)){
                    appNumber = workCase.getAppNumber();
                    statusId = workCase.getStatus().getId();
                    log.debug("--statusId by workcase. {}",statusId);
                }
            } else {
                workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
                if(!Util.isNull(workCasePrescreen)){
                    appNumber = workCasePrescreen.getAppNumber();
                    statusId = workCasePrescreen.getStatus().getId();
                    log.debug("--statusId by workCasePrescreen. {}",statusId);
                }
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

//            pdfReject_letter.findRejectGroup();
            pdfReject_letter.init();
            checkButtomPrint();

            reportView.setNameReportOpShect(nameOpShect.toString());
            reportView.setNameReportExSum(nameExSum.toString());
            reportView.setNameReportAppralsal(nameAppraisal.toString());
            reportView.setNameReportOfferLetter(nameOfferLetter.toString());
            reportView.setNameReportRejectLetter(nameRejectLetter.toString());
        }
    }

    public void checkButtomPrint(){
        log.debug("On checkButtomPrint");
        String[][] cancelCode = {{"C034", "C034"},
                {"C035", "C035"},
                {"C036", "C036"},
                {"C037", "C037"},
                {"C038", "C038"},
                {"C039", "C039"},
                {"C040", "C040"}};
        Map code = ArrayUtils.toMap(cancelCode);

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
        if(!Util.isNull(workCase)){
            log.debug("No Submit Request Appraisal to WorkCase. [{}]", workCase.getRequestAppraisal());
            if (Util.isZero(workCase.getRequestAppraisal())){
                appraisalType = true;
            }
        } else if(!Util.isNull(workCasePrescreen)){
            log.debug("No Submit Request Appraisal to WorkCasePreScreen. [{}]", workCasePrescreen.getRequestAppraisal());
            if (Util.isZero(workCasePrescreen.getRequestAppraisal())){
                appraisalType = true;
            }
        }

        // ###### Disable Buttom Print Reject Letter ######
        if (statusId == StatusValue.CANCEL_CA.value()){
            if (!Util.isNull(cancelRejectInfo)){
                log.debug("--cancelRejectInfo is not null. {}",cancelRejectInfo);
                if (!Util.isZero(pdfReject_letter.getColorByUwRleResultSummary()) && !code.containsKey(cancelRejectInfo.getReason().getCode())){
                    rejectType = true;
                    log.debug("--Disable Buttom Print Reject Letter statusId = 90001 but can not red color and not reason code. {}",statusId);
                }
            }
        } else if (statusId == StatusValue.REJECT_UW1.value() || statusId == StatusValue.REJECT_UW2.value()){
            if (!Util.isNull(codeByExSum)){
                if (Util.isZero(codeByExSum.getExSumNCB()) && Util.isZero(codeByExSum.getExSumIncome()) && Util.isZero(codeByExSum.getExSumPolicy())){
                    log.debug("CancelCode by ExSum Not match");
                    rejectType = true;
                }
            } else {
                rejectType = false;
            }
            log.debug("--rejectType = {}",statusId);
        } else if (statusId == StatusValue.REVIEW_CA.value()){
            rejectType = false;
            log.debug("--rejectType = {}",statusId);
        }  else {
            rejectType = true;
            log.debug("--rejectType not in (90001,90002,90004,90007) {}",statusId);
        }
    }

    public void checkRejectGroupType(){
        log.debug("On checkRejectGroupType");
        if (!Util.isZero(rejectLetterReport.getTypeNCB()) && !Util.isZero(rejectLetterReport.getTypePolicy()) &&
                !Util.isZero(rejectLetterReport.getTypeIncome()) ||
                Util.isZero(rejectLetterReport.getTypeNCB()) && !Util.isZero(rejectLetterReport.getTypePolicy()) &&
                        !Util.isZero(rejectLetterReport.getTypeIncome())){
            templateRejectLetter(4);
            log.debug("--path4. {}",pathReportReject);
        } else if (!Util.isZero(rejectLetterReport.getTypeNCB()) && Util.isZero(rejectLetterReport.getTypePolicy()) &&
                !Util.isZero(rejectLetterReport.getTypeIncome()) ||
                Util.isZero(rejectLetterReport.getTypeNCB()) && Util.isZero(rejectLetterReport.getTypePolicy()) &&
                        !Util.isZero(rejectLetterReport.getTypeIncome())){
            templateRejectLetter(3);
            log.debug("--path3. {}",pathReportReject);
        } else if(Util.isZero(rejectLetterReport.getTypeNCB()) && !Util.isZero(rejectLetterReport.getTypePolicy()) &&
                Util.isZero(rejectLetterReport.getTypeIncome()) ||
                !Util.isZero(rejectLetterReport.getTypeNCB()) && !Util.isZero(rejectLetterReport.getTypePolicy()) &&
                Util.isZero(rejectLetterReport.getTypeIncome())){
            templateRejectLetter(2);
            log.debug("--path2. {}",pathReportReject);
        } else if (!Util.isZero(rejectLetterReport.getTypeNCB()) && Util.isZero(rejectLetterReport.getTypePolicy()) &&
                Util.isZero(rejectLetterReport.getTypeIncome())){
            templateRejectLetter(1);
            log.debug("--path1. {}",pathReportReject);
        }
        log.debug("--Reject Letter Type. {}",pathReportReject);
    }

    public void onPrintExsumReport() throws Exception {
        log.debug("onPrintExsumReport");

//        pdfExecutiveSummary.init();

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
        HashMap map = new HashMap<String, Object>();

        pdfReject_letter.init();
        if (pdfReject_letter.onCheckLogic() == 1){
            log.debug("--Type Reject Is Zero.",pdfReject_letter.onCheckLogic());
            checkRejectGroupType();
        } else if (pdfReject_letter.onCheckLogic() == 2){
            log.debug("--Type Reject Is Two Print Template Policy Only.",pdfReject_letter.onCheckLogic());
            templateRejectLetter(pdfReject_letter.onCheckLogic());
        }
        map.put("path", pathsub);
        map.put("fillAllNameReject", pdfReject_letter.fillAllNameReject());
        map.put("fillRejectLetter",pdfReject_letter.fillRejectLetter());

        generatePDF(pathReportReject,map,reportView.getNameReportRejectLetter(),null);
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

    public void templateRejectLetter(int type){
        log.debug("--Type Path. {}",type);
        switch (type) {
            case 1 : pathReportReject = pathNCBRejectLetter; break;
            case 2 : pathReportReject = pathPolicyRejectLetter; break;
            case 3 : pathReportReject = pathIncomeRejectLetter; break;
            case 4 : pathReportReject =  pathPolicyIncomeRejectLetter; break;
        }
        log.debug("--Path Reject Letter Report. {}",pathReportReject);
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }
}
