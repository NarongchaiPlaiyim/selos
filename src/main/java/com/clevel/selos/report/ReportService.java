package com.clevel.selos.report;

import net.sf.jasperreports.engine.*;
import com.clevel.selos.integration.SELOS;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.Map;

public class ReportService implements Serializable {
    @Inject
    @SELOS
    Logger log;
    protected String reportTemplate;
    protected String reportFileName;
    protected StreamedContent reportFile;


    public void init(){

    }

    public void generatePDF(String fileName, Map<String,Object> parameters,String pdfName) throws JRException, IOException {
        log.debug("generate pdf.");
        JasperReport jasperReport = JasperCompileManager.compileReport(fileName);
        JasperPrint print ;

        log.info("parameters: {}",parameters);

        print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        try {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment; filename="+pdfName);
            ServletOutputStream servletOutputStream=response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(print, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
            log.debug("generatePDF completed.");

        } catch (JRException e) {
            log.error("Error generating pdf report!", e);
        }
    }
}
