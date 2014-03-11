package com.clevel.selos.report;

import com.clevel.selos.model.view.ExSummaryView;
import com.clevel.selos.report.template.PDFExecutive_Summary;
import com.clevel.selos.system.Config;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.util.HashMap;

@ViewScoped
@ManagedBean(name = "report")
public class ReportExSummary extends ReportService {

    @Inject
    @Config(name = "report.exsum")
    String path;

    @Inject
    @Config(name = "report.exsum.subreport")
    String pathsub;

    @Inject
    private ExSummaryView exSummaryViews;

    @Inject
    PDFExecutive_Summary pdfExecutiveSummary;

    @Inject
    public ReportExSummary() {

    }

    public void init(){
        log.debug("init() {[]}");
    }

    @PostConstruct
    private void onCreation(){
        log.debug("ReportExSummary onCreation ");


    }

    String pdfName;

    public void onPrintExsumReport() throws Exception {
        log.debug("onPrintReport");

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

        generatePDF(path, map, pdfName);
    }
}
