package com.clevel.selos.report;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.report.ReportService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewScoped
@ManagedBean(name = "report")
public class ReportExSummary extends ReportService {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    public ReportExSummary() {

    }
    JasperPrint jasperPrint;

    public void init(){

    }

    @PostConstruct
    private void onCreation(){
        init();
        log.debug("ReportExSummary onCreation ");
    }

    public void onPrintReport() throws IOException, JRException {
        log.debug("onPrintReport");
//        try {
//            InputStream reportFileName2 = ReportExSummary.class.getClassLoader().getResourceAsStream("ExSummary.jrxml");
////            System.out.println(reportFileName2.);
//        } catch (Exception e){
//            System.out.println(e);
//        }


        URL reportTemplate2 = this.getClass().getResource("\\com\\clevel\\selos\\report\\iReport\\ExSummary.jrxml");
        System.out.println("reportTemplate2:"+reportTemplate2);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", "Ex Summary");
        List<String> list = new ArrayList<String>();
        list.add("sss");
        list.add("bbb");
        list.add("2222");
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(list);
        log.info("reportTemplate: {}",reportTemplate);
        log.info("map: {}",map);
        log.info("jrBeanCollectionDataSource: {}",jrBeanCollectionDataSource);

        generatePDF(reportTemplate, map, jrBeanCollectionDataSource);
    }

}
