package com.clevel.selos.report;

import com.clevel.selos.model.view.ExSummaryView;
import com.clevel.selos.report.template.PDFDecision;
import com.clevel.selos.report.template.PDFExecutive_Summary;
import com.clevel.selos.system.Config;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.util.HashMap;

@ViewScoped
@ManagedBean(name = "report")
public class GenPDF extends ReportService {

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
    private ExSummaryView exSummaryViews;

    @Inject
    PDFExecutive_Summary pdfExecutiveSummary;

    @Inject
    PDFDecision pdfDecision;

    @Inject
    public GenPDF() {

    }

    public void init(){
        log.debug("init() {[]}");
    }

    @PostConstruct
    private void onCreation(){
        log.debug("GenPDF onCreation ");


    }

    String pdfName;


    public void onPrintExsumReport() throws Exception {
        log.debug("onPrintExsumReport");

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
        map.put("decision", pdfExecutiveSummary.fillDecision());

        pdfName = "Executive_Summary_Report_";

        generatePDF(pathExsum, map, pdfName);
    }

    public void onPrintDecisionReport() throws Exception {
        log.debug("onPrintDecisionReport");

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("fillCreditBorrower",pdfDecision.fillCreditBorrower());
        map.put("fillCondition",pdfDecision.fillCondition());
        map.put("fillBorrowerRetail",pdfDecision.fillBorrowerRetail());
        map.put("fillAppInRLOS",pdfDecision.fillAppInRLOS());
        map.put("fillRelatedCommercial",pdfDecision.fillRelatedCommercial());
        map.put("fillRelatedRetail",pdfDecision.fillRelatedRetail());
        map.put("fillRelatedAppInRLOS",pdfDecision.fillRelatedAppInRLOS());
        map.put("fillExistingCollateralBorrower",pdfDecision.fillExistingCollateralBorrower());
        map.put("fillExistingCollateralRelated",pdfDecision.fillExistingCollateralRelated());
        map.put("fillGuarantorBorrower",pdfDecision.fillGuarantorBorrower());
        map.put("fillProposedCredit",pdfDecision.fillProposedCredit());
        map.put("fillProposeFeeInformation",pdfDecision.fillProposeFeeInformation());
        map.put("fillProposedCollateral",pdfDecision.fillProposedCollateral());
        map.put("fillApprovedCollaterral",pdfDecision.fillApprovedCollaterral());
        map.put("fillProposedGuarantor",pdfDecision.fillProposedGuarantor());
        map.put("fillApprovedCollateral",pdfDecision.fillApprovedCollateral());
        map.put("fillFollowUpCondition",pdfDecision.fillFollowUpCondition());
        map.put("fillApprovalHistory",pdfDecision.fillApprovalHistory());


        pdfName = "Decision_Report_";

        generatePDF(pathDecision, map, pdfName);
    }
}
