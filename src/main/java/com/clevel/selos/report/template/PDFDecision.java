package com.clevel.selos.report.template;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

public class PDFDecision implements Serializable {

    @Inject
    @SELOS
    Logger log;

    long workCaseId;

    public PDFDecision() {
    }

    public void init(){
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }else{
            log.debug("onCreation ::: workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
            }catch (Exception ex){
                log.error("Exception :: {}",ex);
            }
        }
        log.info("workCaseID: {}",workCaseId);

    }
}
