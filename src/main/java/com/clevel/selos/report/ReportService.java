package com.clevel.selos.report;

import com.clevel.selos.integration.SELOS;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class ReportService implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ReportService() {

    }

    public void init(){

    }

    public void generatePDF(String fileName, Map<String,Object> parameters,String pdfName,Collection reportList) throws JRException, IOException {
        log.debug("generate pdf.");
        JasperReport jasperReport = JasperCompileManager.compileReport(fileName);

        JasperPrint print ;

        log.info("parameters: {}",parameters);

//        JRDataSource dataSource = new JRBeanCollectionDataSource(reportList);
//        if(dataSource != null && reportList != null && reportList.size() > 0){
//            print = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//        } else {
             print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
//        }
        log.debug("--Pring report.");

        try {
//            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            response.addHeader("Content-disposition", "attachment; filename="+pdfName+".pdf");
//            ServletOutputStream servletOutputStream=response.getOutputStream();
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.addResponseHeader("Content-disposition", "attachment; filename="+pdfName+".pdf");
            OutputStream outputStream =  externalContext.getResponseOutputStream();
            JasperExportManager.exportReportToPdfStream(print, outputStream);
            FacesContext.getCurrentInstance().responseComplete();
            log.debug("generatePDF completed.");

        } catch (JRException e) {
            log.error("Error generating pdf report!", e);
        }
    }
}
