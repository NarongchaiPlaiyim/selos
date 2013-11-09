package com.clevel.selos.report;

/*import net.sf.jasperreports.engine.*;
import com.clevel.selos.integration.SELOS;
import net.sf.jasperreports.engine.*;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;*/

/*import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;*/
import java.io.Serializable;
//import java.util.Map;

public class ReportService implements Serializable {
    /*@Inject
    @SELOS
    Logger log;
    protected String reportTemplate;
    protected String reportFileName;
    protected StreamedContent reportFile;

    private void generatePDF(Map<String,Object> parameters) {
        log.debug("generate pdf.");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] pdfData = null;
        try {
            log.debug("report template: {}",reportTemplate);
            String jasperReport = JasperCompileManager.compileReportToFile(reportTemplate);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(print, os);

            pdfData = os.toByteArray();
            InputStream is = new ByteArrayInputStream(pdfData);
            reportFile = new DefaultStreamedContent(is, "application/pdf", reportFileName);
            log.debug("generatePDF completed.");
        } catch (JRException e) {
            log.error("Error generating pdf report!", e);
        }
    }

    public StreamedContent getReportFile(Map<String,Object> parameters) {
        generatePDF(parameters);
        return reportFile;
    }*/

}
