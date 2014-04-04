package com.clevel.selos.report.template;


import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.ExSummaryControl;
import com.clevel.selos.dao.working.AddressDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Month;
import com.clevel.selos.model.db.working.Address;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.report.RejectLetterReport;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
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

    private List<CustomerInfoView> customerInfoView;
    private AppHeaderView appHeaderView;

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
            customerInfoView = customerInfoControl.getBorrowerByWorkCase(workCaseId);
        }
    }

    public List<RejectLetterReport> fillAllNameReject (){
        log.debug("fillAllNameReject. {}");
        init();
        List<RejectLetterReport> reportList = new ArrayList<RejectLetterReport>();
        StringBuilder stringName = new StringBuilder();

        int i = 0;
        HttpSession session = FacesUtil.getSession(true);
        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");

        if(Util.safetyList(appHeaderView.getBorrowerHeaderViewList()).size() > 0){
            log.debug("appHeaderView.getBorrowerHeaderViewList. {}",appHeaderView.getBorrowerHeaderViewList());
            for (AppBorrowerHeaderView view : appHeaderView.getBorrowerHeaderViewList()){
                RejectLetterReport report = new RejectLetterReport();
//                i++;
//                stringName = stringName.append(view.getBorrowerName());
//                if (i != appHeaderView.getBorrowerHeaderViewList().size()){
//                    stringName = stringName.append(", ").append("\n");
//                }
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
        init();
        log.debug("fillRejectLetter. {}");
        RejectLetterReport letterReport = new RejectLetterReport();
        String date = Util.createDateTh(new Date());
        log.debug("--date. {}",date);
        String[] spDate = date.split(" ");
        int month = Integer.valueOf(spDate[1]);
        String setMonth = null;
        StringBuilder addressTH = null;

        if(!Util.isNull(workCaseId)){
            log.debug("--customerInfoView. {}",customerInfoView.size());
            for (CustomerInfoView view : customerInfoView){
                Customer customer = customerDAO.findById(view.getId());
                log.debug("--getAddressesList. {}",customer.getAddressesList().size());

                if(!Util.isNull(customer.getAddressesList()) && customer.getAddressesList().size() > 0){
                    addressTH = new StringBuilder();
                    Address allAddress = customer.getAddressesList().get(0);
                    addressTH = addressTH.append(msg.get("app.bizInfoSummary.label.addressNo").concat(" "))
                        .append((allAddress.getAddressNo() != null ? allAddress.getAddressNo() : "-").concat(" "))
                        .append(msg.get("app.bizInfoSummary.label.addressMoo").concat(" "))
                        .append((allAddress.getMoo() != null ? allAddress.getMoo() : "-").concat(" "))
                        .append(msg.get("app.bizInfoSummary.label.addressBuilding").concat(" "))
                        .append((allAddress.getBuilding() != null ? allAddress.getBuilding() : "-").concat(" "))
                        .append(msg.get("app.bizInfoSummary.label.addressStreet").concat(" "))
                        .append((allAddress.getRoad() != null ? allAddress.getRoad() : "-").concat(" "))
                        .append(msg.get("app.bizInfoSummary.label.subdistrict").concat(" "))
                        .append((allAddress.getSubDistrict() != null ? allAddress.getSubDistrict().getCode() != 0 ? allAddress.getSubDistrict().getName() : "-" : "-").concat(" "))
                        .append(msg.get("app.bizInfoSummary.label.district").concat(" "))
                        .append((allAddress.getDistrict() != null ? allAddress.getDistrict().getId() != 0 ? allAddress.getDistrict().getName() : "-" : "-").concat(" "))
                        .append(msg.get("app.bizInfoSummary.label.province").concat(" "))
                        .append((allAddress.getProvince() != null ? allAddress.getProvince().getCode() != 0 ? allAddress.getProvince().getName() : "-" : "-").concat(" "))
                        .append(msg.get("app.bizInfoSummary.label.postCode").concat(" "))
                        .append((allAddress.getPostalCode() != null ? allAddress.getPostalCode() : "-").concat(" "))
                        .append(msg.get("app.bizInfoSummary.label.country").concat(" "))
                        .append((allAddress.getCountry() != null ? allAddress.getCountry().getId() != 0 ? allAddress.getCountry().getName() : "-" : "-").concat(" "));
                    break;
                } else {
                    addressTH = addressTH.append("");
                }
            }
            if(!Util.isNull(addressTH.toString())){
                letterReport.setAddress(addressTH.toString());
                log.debug("--addressTH. {}",addressTH.toString());
            } else {
                letterReport.setAddress("");
                log.debug("--addressTH. {}","");
            }


            switch (month){
                case 1: setMonth = msg.get("app.month.january"); break;
                case 2: setMonth = msg.get("app.month.february"); break;
                case 3: setMonth = msg.get("app.month.march"); break;
                case 4: setMonth = msg.get("app.month.april"); break;
                case 5: setMonth = msg.get("app.month.may"); break;
                case 6: setMonth = msg.get("app.month.june"); break;
                case 7: setMonth = msg.get("app.month.july"); break;
                case 8: setMonth = msg.get("app.month.august"); break;
                case 9: setMonth = msg.get("app.month.september"); break;
                case 10: setMonth = msg.get("app.month.october"); break;
                case 11: setMonth = msg.get("app.month.november"); break;
                case 12: setMonth = msg.get("app.month.december"); break;
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
