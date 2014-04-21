package com.clevel.selos.report;

import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.ReportView;
import com.clevel.selos.report.template.PDFAppraisalAppointment;
import com.clevel.selos.report.template.PDFDecision;
import com.clevel.selos.report.template.PDFExecutiveSummary;
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
    @Config(name = "report.rejectletter")
    String pathRejectLetter;


    @Inject
    @Config(name = "report.appraisal")
    String pathAppraisal;


    @Inject
    private WorkCaseDAO workCaseDAO;

    WorkCase workCase; // ห้าม @Inject

    @Inject
    PDFExecutiveSummary pdfExecutiveSummary;

    @Inject
    PDFRejectLetter pdfReject_letter;

    @Inject
    PDFDecision pdfDecision;

    @Inject
    PDFAppraisalAppointment pdfAppraisalAppointment;

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
        String nameOpShect = "_OpSheet.pdf";
        String nameExSum = "_ExSum.pdf";
        String nameRejectLetter = "_RejectLetter.pdf";
        String nameAppraisal = "_AppraisalAppointment.pdf";
        String date = Util.createDateTime(new Date());
        String[] month = date.split("");
        log.debug("--month. {}",month);

        if(!Util.isNull(workCaseId)){
            workCase = workCaseDAO.findById(workCaseId);
            String appNumber = workCase.getAppNumber();
            reportView = new ReportView();
            reportView.setNameReportOpShect(appNumber+"_"+date+nameOpShect);
            reportView.setNameReportExSum(appNumber + "_" + date + nameExSum);
            reportView.setNameReportRejectLetter(appNumber + "_" + date + nameRejectLetter);
            reportView.setNameReportAppralsal(appNumber + "_" + date + nameAppraisal);
        }
    }

    public void onPrintExsumReport() throws Exception {
        log.debug("onPrintExsumReport");

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
//        map.put("borrower", pdfExecutiveSummary.fillBorrowerRelatedProfile());
//        map.put("businessLocation", pdfExecutiveSummary.fillBorrower());
//        map.put("tradeFinance", pdfExecutiveSummary.fillTradeFinance());
//        map.put("borrowerCharacteristic", pdfExecutiveSummary.fillBorrowerCharacteristic());
//        map.put("ncbRecord", pdfExecutiveSummary.fillNCBRecord());
//        map.put("accountMovement", pdfExecutiveSummary.fillAccountMovement());
//        map.put("collateral", pdfExecutiveSummary.fillCollateral());
//        map.put("creditRisk", pdfExecutiveSummary.fillBorrowerRelatedProfile());
//        map.put("bizSupport", pdfExecutiveSummary.fillBizSupport());
//        map.put("uwDecision", pdfExecutiveSummary.fillUWDecision());
//        map.put("creditRisk", pdfExecutiveSummary.fillCreditRisk());
        map.put("decision", pdfExecutiveSummary.fillDecision());

//        pdfName = "Executive_Summary_Report_";

        generatePDF(pathExsum, map, reportView.getNameReportExSum());
    }

    public void onPrintDecisionReport() throws Exception {
        log.debug("onPrintDecisionReport");

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("fillCreditBorrower",pdfDecision.fillCreditBorrower(pathsub));
        map.put("fillCondition",pdfDecision.fillCondition());
        map.put("fillBorrowerRetail",pdfDecision.fillBorrowerRetail(pathsub));
        map.put("fillAppInRLOS",pdfDecision.fillAppInRLOS(pathsub));
        map.put("fillRelatedCommercial",pdfDecision.fillRelatedCommercial(pathsub));
        map.put("fillRelatedRetail",pdfDecision.fillRelatedRetail(pathsub));
        map.put("fillRelatedAppInRLOS",pdfDecision.fillRelatedAppInRLOS(pathsub));
        map.put("fillExistingCollateralBorrower",pdfDecision.fillExistingCollateralBorrower(pathsub));
        map.put("fillExistingCollateralRelated",pdfDecision.fillExistingCollateralRelated(pathsub));
        map.put("fillGuarantorBorrower",pdfDecision.fillGuarantorBorrower(pathsub));
        map.put("fillProposedCredit",pdfDecision.fillProposedCredit(pathsub));
        map.put("fillProposeFeeInformation",pdfDecision.fillProposeFeeInformation());
        map.put("fillProposedCollateral",pdfDecision.fillProposedCollateral(pathsub));
        map.put("fillApprovedCollaterral",pdfDecision.fillApprovedCollaterral(pathsub));
        map.put("fillProposedGuarantor",pdfDecision.fillProposedGuarantor(pathsub));
        map.put("fillApprovedCollateral",pdfDecision.fillApprovedGuarantor(pathsub));
        map.put("fillFollowUpCondition",pdfDecision.fillFollowUpCondition());
        map.put("fillApprovalHistory",pdfDecision.fillApprovalHistory());
        map.put("fillTotalMasterReport",pdfDecision.fillTotalMasterReport());
        map.put("fillFollowDetail",pdfDecision.fillFollowDetail());
        map.put("fillPriceFee",pdfDecision.fillPriceFee());


//        pdfName = "Decision_Report_";

        generatePDF(pathDecision, map, reportView.getNameReportOpShect());
    }

    public void onPrintRejectLetter() throws Exception {
        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("fillAllNameReject",pdfReject_letter.fillAllNameReject());
        map.put("fillRejectLetter",pdfReject_letter.fillRejectLetter());

        generatePDF(pathRejectLetter,map,reportView.getNameReportRejectLetter());
    }
    public void onPrintAppraisal() throws Exception {
        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("fillAppraisalDetailReport",pdfAppraisalAppointment.fillAppraisalDetailReport());
        map.put("fillAppraisalDetailViewReport",pdfAppraisalAppointment.fillAppraisalDetailViewReport());
        map.put("fillAppraisalContactDetailViewReport",pdfAppraisalAppointment.fillAppraisalContactDetailViewReport());
        map.put("fillContactRecordDetailViewReport",pdfAppraisalAppointment.fillContactRecordDetailViewReport());

        generatePDF(pathAppraisal,map,reportView.getNameReportAppralsal());
    }

    public ReportView getReportView() {
        return reportView;
    }

    public void setReportView(ReportView reportView) {
        this.reportView = reportView;
    }
}
