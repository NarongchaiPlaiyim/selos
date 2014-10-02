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

    public void generatePDF(String fileName, Map<String,Object> parameters,String pdfName) throws JRException, IOException {
        log.debug("generate pdf.");
        JasperReport jasperReport = JasperCompileManager.compileReport(fileName);

        JasperPrint print ;

        log.info("parameters: {}",parameters);

        print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        log.debug("--Pring report.");

        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            String userAgent = getUserAgent(externalContext);

            if (userAgent.contains("MSIE 8")){
                externalContext.responseReset();
                externalContext.addResponseHeader("Cache-Control","max-age=0");
            }
            externalContext.addResponseHeader("Content-disposition", "attachment; filename="+pdfName);

            OutputStream outputStream =  externalContext.getResponseOutputStream();
            JasperExportManager.exportReportToPdfStream(print, outputStream);
            FacesContext.getCurrentInstance().responseComplete();
            log.debug("generatePDF completed.");

        } catch (JRException e) {
            log.error("Error generating pdf report!", e);
        }
    }

    private String getUserAgent(ExternalContext externalContext){
        Map<String, String> requestHeaderMap = externalContext.getRequestHeaderMap();
        for (String key : requestHeaderMap.keySet()){
            if (key.equalsIgnoreCase("User-Agent")){
                return requestHeaderMap.get(key);
            }
        }
        return "";
    }
}
