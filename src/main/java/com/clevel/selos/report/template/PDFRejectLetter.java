package com.clevel.selos.report.template;


import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.report.RejectLetterReport;
import com.clevel.selos.model.view.AppBorrowerHeaderView;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PDFRejectLetter implements Serializable {
    @Inject
    @SELOS
    Logger log;

    long workCaseId;
    private long workCasePreScreenId;

    @Inject
    @NormalMessage
    Message msg;

    @Inject
    CustomerDAO customerDAO;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private AddressDAO addressDAO;
    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private UWRuleResultSummaryDAO uwRuleResultSummaryDAO;
    @Inject
    private UWRuleResultDetailDAO uwRuleResultDetailDAO;

    private List<CustomerInfoView> customerInfoView;
    private AppHeaderView appHeaderView;
    private List<UWRuleResultDetail> uwRuleResultDetails;

    private UWRuleResultSummary uwRuleResultSummary;

    WorkCase workCase;

    private final String SPACE = " ";

    public PDFRejectLetter() {
    }

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if(( (Long)session.getAttribute("workCaseId") != 0 || (Long)session.getAttribute("workCasePreScreenId") != 0 ) &&
                (Long)session.getAttribute("stepId") != 0){
            checkSession = true;
        }

        return checkSession;
    }

    public void init(){
        log.debug("--on init()");
        HttpSession session = FacesUtil.getSession(true);

        if(checkSession(session)){
            if((Long)session.getAttribute("workCaseId") != 0){
                workCaseId = (Long)session.getAttribute("workCaseId");
            } else if ((Long)session.getAttribute("workCasePreScreenId") != 0){
                workCasePreScreenId = (Long)session.getAttribute("workCasePreScreenId");
            } else {
                log.debug("workCaseId is null.");
                try{
                    FacesUtil.redirect("/site/inbox.jsf");
                }catch (Exception ex){
                    log.error("Exception :: {}",ex);
                }
            }
        }

//        if(session.getAttribute("workCaseId") != null){
//            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
//            log.debug("workCaseId. {}",workCaseId);
//        }else{
//            log.debug("workCaseId is null.");
//            try{
//                FacesUtil.redirect("/site/inbox.jsf");
//            }catch (Exception ex){
//                log.error("Exception :: {}",ex);
//            }
//        }

        if (!Util.isNull(workCaseId)){
            customerInfoView = new ArrayList<CustomerInfoView>();
            customerInfoView = customerInfoControl.getBorrowerByWorkCase(workCaseId);
            workCase = workCaseDAO.findById(workCaseId);
        }
    }

//    public static void main(String[] args) {
//         System.out.println(new RejectLetterReport());
//    }

    public RejectLetterReport typeReport(){
        log.debug("--On typeReport.");
        uwRuleResultSummary = new UWRuleResultSummary();
        if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
            uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcaseId(workCaseId);
        } else if (!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            uwRuleResultSummary = uwRuleResultSummaryDAO.findByWorkcasePrescreenId(workCasePreScreenId);
        }
        log.debug("--uwRuleResultSummary. {}",uwRuleResultSummary);

        uwRuleResultDetails = new ArrayList<UWRuleResultDetail>();
        uwRuleResultDetails = uwRuleResultDetailDAO.findByUWRuleSummaryId(uwRuleResultSummary.getId());
        log.debug("--uwRuleResultDetails. {}",uwRuleResultDetails);

        RejectLetterReport rejectLetterReport = new RejectLetterReport();
        for (UWRuleResultDetail ruleResultDetail : uwRuleResultDetails){
            if (!Util.isNull(ruleResultDetail.getRejectGroup())){
                if (ruleResultDetail.getRejectGroup().getId() == 1){
                    rejectLetterReport.setTypeNCB(ruleResultDetail.getRejectGroup().getId());
                } else if (ruleResultDetail.getRejectGroup().getId() == 2){
                    rejectLetterReport.setTypePolicy(ruleResultDetail.getRejectGroup().getId());
                } else if (ruleResultDetail.getRejectGroup().getId() == 3){
                    rejectLetterReport.setTypeIncome(ruleResultDetail.getRejectGroup().getId());
                }
            } else {
                log.debug("--RejectGroup is Null.");
            }
        }
        log.debug("--rejectLetterReport. {}",rejectLetterReport);

        return rejectLetterReport;
    }

    public List<RejectLetterReport> fillAllNameReject (){
        log.debug("fillAllNameReject. {}");
        List<RejectLetterReport> reportList = new ArrayList<RejectLetterReport>();
        HttpSession session = FacesUtil.getSession(true);
        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");

        if(Util.safetyList(appHeaderView.getBorrowerHeaderViewList()).size() > 0){
            log.debug("appHeaderView.getBorrowerHeaderViewList. {}",appHeaderView.getBorrowerHeaderViewList());
            for (AppBorrowerHeaderView view : appHeaderView.getBorrowerHeaderViewList()){
                RejectLetterReport report = new RejectLetterReport();
                report.setName(view.getBorrowerName());
                reportList.add(report);
                log.debug("--reportList. {}",reportList);
            }
        } else {
            RejectLetterReport report = new RejectLetterReport();
            reportList.add(report);
            log.debug("appHeaderView.getBorrowerHeaderViewList is Null. {}");
        }

        return reportList;
    }

    public RejectLetterReport fillRejectLetter(){
        log.debug("fillRejectLetter. {}");
        RejectLetterReport letterReport = new RejectLetterReport();
        String date = Util.createDateTh(new Date());
        String[] spDate = date.split("/");
        int month = Integer.valueOf(spDate[1]);
        String setMonth;
        StringBuilder addressTH = null;

        if(!Util.isNull(workCaseId)){
            log.debug("--customerInfoView. {}",customerInfoView.size());
            letterReport.setAppNumber(workCase.getAppNumber());

            for (CustomerInfoView view : customerInfoView){
                Customer customer = customerDAO.findById(view.getId());
                log.debug("--getAddressesList. {}",customer.getAddressesList().size());

                if(!Util.isNull(customer.getAddressesList()) && customer.getAddressesList().size() > 0){
                    addressTH = new StringBuilder();
                    Address allAddress = customer.getAddressesList().get(0);
                    addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.addressNo").concat(SPACE))
                        .append((allAddress.getAddressNo() != null ? allAddress.getAddressNo() : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.addressMoo").concat(SPACE))
                        .append((allAddress.getMoo() != null ? allAddress.getMoo() : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.addressBuilding").concat(SPACE))
                        .append((allAddress.getBuilding() != null ? allAddress.getBuilding() : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.addressStreet").concat(SPACE))
                        .append((allAddress.getRoad() != null ? allAddress.getRoad() : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.subdistrict").concat(SPACE))
                        .append((allAddress.getSubDistrict() != null ? allAddress.getSubDistrict().getCode() != 0 ? allAddress.getSubDistrict().getName() : "-" : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.district").concat(SPACE))
                        .append((allAddress.getDistrict() != null ? allAddress.getDistrict().getId() != 0 ? allAddress.getDistrict().getName() : "-" : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.province").concat(SPACE))
                        .append((allAddress.getProvince() != null ? allAddress.getProvince().getCode() != 0 ? allAddress.getProvince().getName() : "-" : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.postCode").concat(SPACE))
                        .append((allAddress.getPostalCode() != null ? allAddress.getPostalCode() : "-").concat(SPACE))
                        .append(msg.get("app.bizInfoSummary.label.country").concat(SPACE))
                        .append((allAddress.getCountry() != null ? allAddress.getCountry().getId() != 0 ? allAddress.getCountry().getName() : "-" : "-").concat(SPACE));
                    break;
                } else {
                    addressTH = addressTH.append(SPACE);
                }
            }
            if(!Util.isNull(addressTH.toString())){
                letterReport.setAddress(addressTH.toString());
            } else {
                letterReport.setAddress(SPACE);
            }


            switch (month){
                case 1: setMonth = msg.get("app.report.month.january"); break;
                case 2: setMonth = msg.get("app.report.month.february"); break;
                case 3: setMonth = msg.get("app.report.month.march"); break;
                case 4: setMonth = msg.get("app.report.month.april"); break;
                case 5: setMonth = msg.get("app.report.month.may"); break;
                case 6: setMonth = msg.get("app.report.month.june"); break;
                case 7: setMonth = msg.get("app.report.month.july"); break;
                case 8: setMonth = msg.get("app.report.month.august"); break;
                case 9: setMonth = msg.get("app.report.month.september"); break;
                case 10: setMonth = msg.get("app.report.month.october"); break;
                case 11: setMonth = msg.get("app.report.month.november"); break;
                case 12: setMonth = msg.get("app.report.month.december"); break;
                default:setMonth = SPACE;

            }

            StringBuilder stringBuilder =new StringBuilder();
            if(!Util.isNull(spDate) && spDate.length == 3){
                stringBuilder = stringBuilder.append(spDate[0]).append(SPACE);
                stringBuilder = stringBuilder.append(setMonth).append(SPACE);
                stringBuilder =  stringBuilder.append(spDate[2]);
                letterReport.setDate(stringBuilder.toString());
            } else {
                letterReport.setDate(SPACE);
            }
            log.debug("--stringBuilder. {}",stringBuilder.toString());
        }
        return letterReport;
    }
}
