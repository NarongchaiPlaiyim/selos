package com.clevel.selos.report;

import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.ReportView;
import com.clevel.selos.report.template.*;
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
    @Config(name = "report.rejectletter")
    String pathRejectLetter;


    @Inject
    @Config(name = "report.appraisal")
    String pathAppraisal;

    @Inject
    @Config(name = "report.offerletter")
    String pathOfferLetter;


    @Inject
    private WorkCaseDAO workCaseDAO;

    WorkCase workCase; // ห้าม @Inject

    @Inject
    PDFExecutiveSummaryAndOpSheet pdfExecutiveSummary;

    @Inject
    PDFRejectLetter pdfReject_letter;

    @Inject
    PDFDecision pdfDecision;

    @Inject
    PDFAppraisalAppointment pdfAppraisalAppointment;

    @Inject
    PDFOfferLetter pdfOfferLetter;

    private ReportView reportView;

    long workCaseId;

    public GenPDF() {

    }

    public void init(){
        log.debug("init() {[]}");
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.debug("workCaseId. {}",workCaseId);
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
        log.debug("GenPDF onCreation ");
    }

    String pdfName;

    public void setNameReport(){
        init();
        log.info("On setNameReport()");
        String date = Util.createDateTime(new Date());
        String[] month = date.split("");
        log.debug("--month. {}",month);



        if(!Util.isNull(workCaseId)){
            workCase = workCaseDAO.findById(workCaseId);
            String appNumber = workCase.getAppNumber();
            reportView = new ReportView();

            StringBuilder nameOpShect =new StringBuilder();
            nameOpShect = nameOpShect.append(appNumber).append("_").append(date).append("_OpSheet.pdf");
            log.debug("--nameOpShect",nameOpShect);

            StringBuilder nameExSum =new StringBuilder();
            nameExSum = nameExSum.append(appNumber).append("_").append(date).append("_ExSum.pdf");
            log.debug("--nameExSum",nameExSum);

            StringBuilder nameRejectLetter =new StringBuilder();
            nameRejectLetter = nameRejectLetter.append(appNumber).append("_").append(date).append("_RejectLetter.pdf");
            log.debug("--nameRejectLetter",nameRejectLetter);

            StringBuilder nameAppraisal =new StringBuilder();
            nameAppraisal = nameAppraisal.append(appNumber).append("_").append(date).append("_AppraisalAppointment.pdf");
            log.debug("--nameAppraisal",nameAppraisal);

            StringBuilder nameOfferLetter =new StringBuilder();
            nameOfferLetter = nameOfferLetter.append(appNumber).append("_").append(date).append("_OfferLetter.pdf");
            log.debug("--nameOfferLetter",nameOfferLetter);

            reportView.setNameReportOpShect(nameOpShect.toString());
            reportView.setNameReportExSum(nameExSum.toString());
            reportView.setNameReportRejectLetter(nameRejectLetter.toString());
            reportView.setNameReportAppralsal(nameAppraisal.toString());
            reportView.setNameReportOfferLetter(nameOfferLetter.toString());
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
        map.put("fillApprovedCredit",pdfExecutiveSummary.fillApprovedCredit(pathsub));
        map.put("fillProposeFeeInformation",pdfExecutiveSummary.fillProposeFeeInformation());
        map.put("fillProposedCollateral",pdfExecutiveSummary.fillProposedCollateral(pathsub));
        map.put("fillApprovedCollaterral",pdfExecutiveSummary.fillApprovedCollaterral(pathsub));
        map.put("fillProposedGuarantor",pdfExecutiveSummary.fillProposedGuarantor(pathsub));
        map.put("fillApprovedGuarantor",pdfExecutiveSummary.fillApprovedGuarantor(pathsub));
        map.put("fillFollowUpCondition",pdfExecutiveSummary.fillFollowUpCondition());
        map.put("fillApprovalHistory",pdfExecutiveSummary.fillApprovalHistory());
        map.put("fillTotalMasterReport",pdfExecutiveSummary.fillTotalMasterReport());
        map.put("fillFollowDetail",pdfExecutiveSummary.fillFollowDetail());
        map.put("fillPriceFee",pdfExecutiveSummary.fillPriceFee());

//        pdfName = "Executive_Summary_Report_";

        generatePDF(pathExsum, map, reportView.getNameReportExSum());
    }

    public void onPrintDecisionReport() throws Exception {
        log.debug("onPrintDecisionReport");

        pdfDecision.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("fillCreditBorrower",pdfDecision.fillCreditBorrower(pathsub));
        map.put("fillCondition",pdfDecision.fillCondition());
        map.put("fillBorrowerRetail",pdfDecision.fillBorrowerRetail(pathsub));
//        map.put("fillAppInRLOS",pdfDecision.fillAppInRLOS(pathsub));
        map.put("fillRelatedCommercial",pdfDecision.fillRelatedCommercial(pathsub));
        map.put("fillRelatedRetail",pdfDecision.fillRelatedRetail(pathsub));
//        map.put("fillRelatedAppInRLOS",pdfDecision.fillRelatedAppInRLOS(pathsub));
        map.put("fillExistingCollateralBorrower",pdfDecision.fillExistingCollateralBorrower(pathsub));
        map.put("fillExistingCollateralRelated",pdfDecision.fillExistingCollateralRelated(pathsub));
        map.put("fillGuarantorBorrower",pdfDecision.fillGuarantorBorrower(pathsub));
        map.put("fillProposedCredit",pdfDecision.fillProposedCredit(pathsub));
        map.put("fillApprovedCredit",pdfDecision.fillApprovedCredit(pathsub));
        map.put("fillProposeFeeInformation",pdfDecision.fillProposeFeeInformation());
        map.put("fillProposedCollateral",pdfDecision.fillProposedCollateral(pathsub));
        map.put("fillApprovedCollaterral",pdfDecision.fillApprovedCollaterral(pathsub));
        map.put("fillProposedGuarantor",pdfDecision.fillProposedGuarantor(pathsub));
        map.put("fillApprovedGuarantor",pdfDecision.fillApprovedGuarantor(pathsub));
        map.put("fillFollowUpCondition",pdfDecision.fillFollowUpCondition());
        map.put("fillApprovalHistory",pdfDecision.fillApprovalHistory());
        map.put("fillTotalMasterReport",pdfDecision.fillTotalMasterReport());
        map.put("fillFollowDetail",pdfDecision.fillFollowDetail());
        map.put("fillPriceFee",pdfDecision.fillPriceFee());
        map.put("fillHeader",pdfDecision.fillHeader());
        map.put("fillFooter",pdfDecision.fillFooter());
        map.put("borrowerCharacteristic", pdfDecision.fillBorrowerCharacteristic());
//        map.put("fillBizInfoSum", pdfDecision.fillBizInfoSum());



        generatePDF(pathDecision, map, reportView.getNameReportOpShect());
    }

    public void onPrintRejectLetter() throws Exception {
        log.debug("--onPrintRejectLetter");
        pdfReject_letter.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("fillAllNameReject",pdfReject_letter.fillAllNameReject());
        map.put("fillRejectLetter",pdfReject_letter.fillRejectLetter());

        generatePDF(pathRejectLetter,map,reportView.getNameReportRejectLetter());
    }
    public void onPrintAppraisal() throws Exception {
        log.debug("--onPrintAppraisal");
        pdfAppraisalAppointment.init();

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("fillAppraisalDetailReport",pdfAppraisalAppointment.fillAppraisalDetailReport());
        map.put("fillAppraisalDetailViewReport",pdfAppraisalAppointment.fillAppraisalDetailViewReport());
        map.put("fillAppraisalContactDetailViewReport",pdfAppraisalAppointment.fillAppraisalContactDetailViewReport());
        map.put("fillContactRecordDetailViewReport",pdfAppraisalAppointment.fillContactRecordDetailViewReport());

        generatePDF(pathAppraisal,map,reportView.getNameReportAppralsal());
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

        generatePDF(pathOfferLetter, map, reportView.getNameReportOfferLetter());
    }

    public ReportView getReportView() {
        return reportView;
    }

    public void setReportView(ReportView reportView) {
        this.reportView = reportView;
    }
}
