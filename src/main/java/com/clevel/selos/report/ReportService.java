package com.clevel.selos.report;

import com.clevel.selos.integration.SELOS;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class ReportService implements Serializable {
    @Inject
    @SELOS
    Logger log;

    public void init(){
    }

    public void generatePDF(String fileName, Map<String,Object> parameters,String pdfName) throws JRException, IOException {
        log.debug("generate pdf.");
        JasperReport jasperReport = JasperCompileManager.compileReport(fileName);

        JasperPrint print ;

        log.info("parameters: {}",parameters);


        print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        log.debug("--Pring report.");

        try {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment; filename="+pdfName+".pdf");
            ServletOutputStream servletOutputStream=response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(print, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
            log.debug("generatePDF completed.");

        } catch (JRException e) {
            log.error("Error generating pdf report!", e);
        }
    }

    public void exportPDF(HttpServletRequest request, HttpServletResponse response, Map map, Collection reportList, String jasperName)
            throws Exception {

        ServletOutputStream servletOutputStream = response.getOutputStream();
        InputStream reportStream = request.getSession().getServletContext().getResourceAsStream("/reports/" + jasperName + ".jasper");
        response.setContentType("application/pdf");
        try {
            JRDataSource dataSource = null;
            dataSource = new JRBeanCollectionDataSource(reportList);
            if(dataSource != null && reportList != null && reportList.size() > 0)
                JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream, map, dataSource);
            else
                JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream, map, new JREmptyDataSource());

        } catch (Exception e) {
            log.debug(e.getMessage());
        } finally {
            reportStream.close();
            servletOutputStream.flush();
            servletOutputStream.close();

        }
    }
}
