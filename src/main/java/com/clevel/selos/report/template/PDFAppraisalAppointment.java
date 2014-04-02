package com.clevel.selos.report.template;


import com.clevel.selos.businesscontrol.AppraisalAppointmentControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.report.AppraisalContactDetailViewReport;
import com.clevel.selos.model.report.AppraisalDetailViewReport;
import com.clevel.selos.model.report.AppraisalViewReport;
import com.clevel.selos.model.report.ContactRecordDetailViewReport;
import com.clevel.selos.model.view.AppraisalContactDetailView;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PDFAppraisalAppointment implements Serializable {

    @Inject
    @SELOS
    Logger log;

    @Inject
    AppraisalAppointmentControl appraisalAppointmentControl;

    @Inject
    @Config(name = "report.subreport")
    String pathsub;

    private AppraisalView appraisalView;

    long workCaseId;
    long workCasePreScreenId;

    public PDFAppraisalAppointment() {
    }

    public void init(){
        HttpSession session = FacesUtil.getSession(true);

        if((Long)session.getAttribute("workCaseId") != 0){
            workCaseId = Long.valueOf("" + session.getAttribute("workCaseId"));
        } else if ((Long)session.getAttribute("workCasePreScreenId") != 0){
            workCasePreScreenId = Long.valueOf(""+session.getAttribute("workCasePreScreenId"));
        }
        log.info("workCaseID: {}",workCaseId);


        if (!Util.isNull(workCaseId)){
            appraisalView = new AppraisalView();
            appraisalView = appraisalAppointmentControl.getAppraisalAppointment(workCaseId,workCasePreScreenId);
            log.debug("--appraisalView. {}",appraisalView);
        }

    }

    public AppraisalViewReport fillAppraisalDetailReport(){
        init();
        AppraisalViewReport report = new AppraisalViewReport();

        if (!Util.isNull(appraisalView)){
            report.setAppraisalType(appraisalView.getAppraisalType());
            report.setAppraisalCompany(Util.checkNullString(appraisalView.getAppraisalCompany().getName()));
            report.setAppraisalDivision(Util.checkNullString(appraisalView.getAppraisalDivision().getName()));
            report.setAppraisalName(Util.checkNullString(appraisalView.getAppraisalName()));
            report.setReceivedTaskDate(appraisalView.getReceivedTaskDate());
            report.setLocationOfProperty(Util.checkNullString(appraisalView.getLocationOfProperty().getName()));
            report.setProvinceOfProperty(Util.checkNullString(appraisalView.getProvinceOfProperty().getName()));
            report.setAppraisalDate( appraisalView.getAppraisalDate());
            report.setDueDate( appraisalView.getDueDate());
            report.setAADAdminRemark(Util.checkNullString(appraisalView.getAADAdminRemark()));

            report.setAppointmentDate(appraisalView.getAppointmentDate());
            report.setAppointmentCusName(Util.checkNullString(appraisalView.getAppointmentCusName()));
            report.setCancelAppointment(Util.checkNullString(appraisalView.getCancelAppointment()));
            report.setAppointmentRemark(Util.checkNullString(appraisalView.getAppointmentRemark()));

            report.setZoneLocation(Util.checkNullString(appraisalView.getZoneLocation()));
            log.debug("--fillAppraisalDetailReport. {}",report);
        } else {
            log.debug("--fillAppraisalDetailReport is Null.");
        }
        return report;
    }

    public List<AppraisalDetailViewReport> fillAppraisalDetailViewReport(){
        init();
        List<AppraisalDetailViewReport> appraisalDetailViewReportList = new ArrayList<AppraisalDetailViewReport>();

        int count = 1;
        log.debug("--AppraisalDetailViewList. {}",appraisalView.getAppraisalDetailViewList());
        if (Util.safetyList(appraisalView.getAppraisalDetailViewList()).size() > 0 && !Util.isNull(appraisalView.getAppraisalDetailViewList())){
            log.debug("--AppraisalDetailViewList. {}",appraisalView.getAppraisalDetailViewList());
            for (AppraisalDetailView view : appraisalView.getAppraisalDetailViewList()){
                AppraisalDetailViewReport report = new AppraisalDetailViewReport();
                report.setCount(count++);
                report.setPath(pathsub);
                report.setTitleDeed(Util.checkNullString(view.getTitleDeed()));
                report.setPurposeReviewAppraisalLabel(Util.checkNullString(view.getPurposeReviewAppraisalLabel()));
                report.setCharacteristic(view.getCharacteristic());
                report.setNumberOfDocuments(view.getNumberOfDocuments());
                appraisalDetailViewReportList.add(report);
            }
            log.debug("--appraisalDetailViewReportList. {}",appraisalDetailViewReportList);
        } else {
            AppraisalDetailViewReport report = new AppraisalDetailViewReport();
            appraisalDetailViewReportList.add(report);
            log.debug("--appraisalDetailViewReportList is Null.");
        }

        return appraisalDetailViewReportList;
    }

    public AppraisalContactDetailViewReport fillAppraisalContactDetailViewReport(){
        init();
        AppraisalContactDetailView view = new AppraisalContactDetailView();
        view = appraisalView.getAppraisalContactDetailView();
        AppraisalContactDetailViewReport report = new AppraisalContactDetailViewReport();

        if (!Util.isNull(view.getCustomerName1()) && !Util.isNull(view.getContactNo1())){
            report.setCount1(1);
            report.setCustomerName1(Util.checkNullString(view.getCustomerName1()));
            report.setContactNo1(Util.checkNullString(view.getContactNo1()));
        } else if (!Util.isNull(view.getCustomerName2()) && !Util.isNull(view.getContactNo2())){
            report.setCount1(2);
            report.setCustomerName2(Util.checkNullString(view.getCustomerName2()));
            report.setContactNo2(Util.checkNullString(view.getContactNo2()));
        }  else if (!Util.isNull(view.getCustomerName3()) && !Util.isNull(view.getContactNo3())){
            report.setCount1(2);
            report.setCustomerName3(Util.checkNullString(view.getCustomerName3()));
            report.setContactNo3(Util.checkNullString(view.getContactNo3()));
        }
        log.debug("--fillAppraisalContactDetailViewReport. {}",report);

        return report;
    }

    public List<ContactRecordDetailViewReport> fillContactRecordDetailViewReport(){
        init();
        List<ContactRecordDetailViewReport> contactRecordDetailViewReports = new ArrayList<ContactRecordDetailViewReport>();
        List<ContactRecordDetailView> detailViewList = new ArrayList<ContactRecordDetailView>();
        detailViewList = appraisalView.getContactRecordDetailViewList();
        int count = 1;

        if (Util.safetyList(detailViewList).size() > 0){
            log.debug("--detailViewList. {}",detailViewList);
            for (ContactRecordDetailView view : detailViewList){
                ContactRecordDetailViewReport report = new ContactRecordDetailViewReport();
                report.setCount(count++);
                report.setPath(pathsub);
                report.setCallingDate(view.getCallingDate());
                report.setCallingResult(view.getCallingResult());
                report.setAcceptResult(view.getAcceptResult());
                report.setNextCallingDate(view.getNextCallingDate());
                report.setReasonDescription(Util.checkNullString(view.getReason().getDescription()));
                report.setRemark(Util.checkNullString(view.getRemark()));
                report.setStatusDescription(Util.checkNullString(view.getStatus().getDescription()));
                report.setDisplayName(Util.checkNullString(view.getCreateBy().getDisplayName()));
                contactRecordDetailViewReports.add(report);
            }
        } else {
            ContactRecordDetailViewReport report = new ContactRecordDetailViewReport();
            contactRecordDetailViewReports.add(report);
            log.debug("--detailViewList is Null.");
        }

        return contactRecordDetailViewReports;
    }
}
