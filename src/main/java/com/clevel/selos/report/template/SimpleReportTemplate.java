package com.clevel.selos.report.template;

import com.clevel.selos.report.ReportService;
import com.clevel.selos.report.SimpleReport;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@SimpleReport
public class SimpleReportTemplate extends ReportService {
    @Inject
    Logger log;

    @Inject
    public SimpleReportTemplate() {
    }

    @PostConstruct
    public void onCreation() {
        // todo: for testing
        //reportFileName = "report.pdf";
        //reportTemplate = "d:/tmp/testReport.jrxml";
    }
}
