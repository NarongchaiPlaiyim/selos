package com.clevel.selos.report;

import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.ExSummaryView;
import com.clevel.selos.report.template.PDFAction;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    PDFAction pdfAction;

    @Inject
    public ReportExSummary() {

    }
    JasperPrint jasperPrint;
    JRBeanCollectionDataSource beanCollectionDataSource;

    public void init(){
        log.debug("init() {[]}");
    }

    @PostConstruct
    private void onCreation(){
        log.debug("ReportExSummary onCreation ");


    }

    public void onPrintReport() throws Exception {
        log.debug("onPrintReport");

        HashMap map = new HashMap<String, Object>();
        map.put("path", pathsub);
        map.put("borrower",pdfAction.fillBorrowerRelatedProfile());
        map.put("businessLocation",pdfAction.fillBorrower());
        map.put("tradeFinance", pdfAction.fillTradeFinance());
        map.put("borrowerCharacteristic",pdfAction.fillBorrowerCharacteristic());
        map.put("ncbRecord",pdfAction.fillNCBRecord());
        map.put("accountMovement",pdfAction.fillAccountMovement());
        map.put("collateral",pdfAction.fillCollateral());
        map.put("creditRisk",pdfAction.fillBorrowerRelatedProfile());
        map.put("bizSupport",pdfAction.fillBizSupport());
        map.put("uwDecision",pdfAction.fillUWDecision());
        map.put("creditRisk",pdfAction.fillCreditRisk());
        map.put("decision",pdfAction.fillDecision());


        List<String> list = new ArrayList<String>();


        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(null);


//        fillReport(map);

        generatePDF(path, map);
    }
}
