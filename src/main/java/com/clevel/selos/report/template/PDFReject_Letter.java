package com.clevel.selos.report.template;


import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Month;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.report.RejectLetterReport;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PDFReject_Letter implements Serializable {
    @Inject
    @SELOS
    Logger log;

    long workCaseId;

    @Inject
    private WorkCaseDAO workCaseDAO;
//    @Inject
//    ExSummaryView exSummaryView;
//    @Inject
//    private ExSummaryControl exSummaryControl;

    @Inject
    private CustomerInfoControl customerInfoControl;

//    private AppHeaderView appHeaderView;
    private List<CustomerInfoView> customerInfoView;

    WorkCase workCase;

    public PDFReject_Letter() {
    }

    public void init(){
        log.debug("--on init()");
        HttpSession session = FacesUtil.getSession(true);

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
//            appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");
            log.debug("workCaseId. {}",workCaseId);
        }else{
            log.debug("workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
            }catch (Exception ex){
                log.error("Exception :: {}",ex);
            }
        }

        if (!Util.isNull(workCaseId)){
            customerInfoView = new ArrayList<CustomerInfoView>();
            customerInfoView = customerInfoControl.getAllCustomerByWorkCase(workCaseId);
        }
    }

    public List<RejectLetterReport> fillAllNameReject (){
        log.debug("fillAllNameReject. {}");
        List<RejectLetterReport> reportList = new ArrayList<RejectLetterReport>();
        RejectLetterReport report = null;
        StringBuilder stringName = new StringBuilder();

        if (Util.safetyList(customerInfoView).size() > 0 ){
            log.debug("customerInfoView. {}",customerInfoView);
            for (CustomerInfoView view : customerInfoView){
                report = new RejectLetterReport();
                stringName = stringName.append(view.getTitleTh());
                stringName = stringName.append(view.getFirstNameTh()).append(" ");
                stringName = stringName.append(view.getLastNameTh());
                report.setName(stringName.toString());
                reportList.add(report);
            }
        } else {
            log.debug("customerInfoView is Null. {}",customerInfoView.size());
            reportList.add(new RejectLetterReport());
        }
        return reportList;
    }

    public RejectLetterReport fillRejectLetter(){
        init();
        log.debug("fillRejectLetter. {}");
        RejectLetterReport letterReport = new RejectLetterReport();
        String date = Util.createDateTh(new Date());
        log.debug("--date. {}",date);
        String[] spDate = date.split(" ");
        int month = Integer.valueOf(spDate[1]);
        String setMonth = null;

        if(!Util.isNull(workCaseId)){
//            letterReport.setAppNumber(appHeaderView.getAppNo());
//
//            for (AppBorrowerHeaderView view : appHeaderView.getBorrowerHeaderViewList()){
//                letterReport.setName(view.getBorrowerName());
//                log.debug("--getBorrowerName. {}",view.getBorrowerName());
//            }

            switch (month){
                case 1: setMonth = Month.January.getNameTH(); break;
                case 2: setMonth = Month.February.getNameTH(); break;
                case 3: setMonth = Month.March.getNameTH(); break;
                case 4: setMonth = Month.April.getNameTH(); break;
                case 5: setMonth = Month.May.getNameTH(); break;
                case 6: setMonth = Month.June.getNameTH(); break;
                case 7: setMonth = Month.July.getNameTH(); break;
                case 8: setMonth = Month.August.getNameTH(); break;
                case 9: setMonth = Month.September.getNameTH(); break;
                case 10: setMonth = Month.October.getNameTH(); break;
                case 11: setMonth = Month.November.getNameTH(); break;
                case 12: setMonth = Month.December.getNameTH(); break;
                default:setMonth = "";

            }

            StringBuilder stringBuilder =new StringBuilder();
            if(!Util.isNull(spDate) && spDate.length == 3){
                stringBuilder = stringBuilder.append(spDate[0]).append(" ");
                stringBuilder = stringBuilder.append(setMonth).append(" ");
                stringBuilder =  stringBuilder.append(spDate[2]);
                letterReport.setDate(stringBuilder.toString());
            } else {
                letterReport.setDate("");
            }
            log.debug("--stringBuilder. {}",stringBuilder.toString());
        }
        return letterReport;
    }
}
