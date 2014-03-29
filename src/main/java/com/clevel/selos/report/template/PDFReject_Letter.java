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
        RejectLetterReport report = null;
        StringBuilder stringName = new StringBuilder();

        int i = 0;
        if (Util.safetyList(customerInfoView).size() > 0 ){
            log.debug("customerInfoView. {}",customerInfoView);
            for (CustomerInfoView view : customerInfoView){
                i++;
                report = new RejectLetterReport();
                stringName = stringName.append(view.getTitleTh().getTitleTh());
                stringName = stringName.append(view.getFirstNameTh()).append(" ");
                stringName = stringName.append(view.getLastNameTh());
                if(i != customerInfoView.size()){
                    stringName = stringName.append(", ");
                }
                report.setName(stringName.toString());
                log.debug("--getName. {} i {}",report.getName(),i++);

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
