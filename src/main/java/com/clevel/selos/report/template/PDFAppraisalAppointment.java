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

    private final String SPACE = " ";

    public PDFAppraisalAppointment() {
    }

    public void init(){
        HttpSession session = FacesUtil.getSession(false);

        if((Long)session.getAttribute("workCaseId") != 0){
            workCaseId = Long.valueOf("" + session.getAttribute("workCaseId"));
        } else if ((Long)session.getAttribute("workCasePreScreenId") != 0){
            workCasePreScreenId = Long.valueOf(""+session.getAttribute("workCasePreScreenId"));
        }


        appraisalView = new AppraisalView();

        if (!Util.isNull(workCaseId)){
            log.info("workCaseID: {}",workCaseId);

            if (!Util.isNull(appraisalAppointmentControl.getAppraisalAppointment(workCaseId,workCasePreScreenId))) {
                appraisalView = appraisalAppointmentControl.getAppraisalAppointment(workCaseId,workCasePreScreenId);
            } else {
                log.debug("--appraisalView is Null",appraisalAppointmentControl.getAppraisalAppointment(workCaseId,workCasePreScreenId));
            }
            log.debug("--appraisalView. {}",appraisalView);
        } else {
            log.debug("--workcase is Null. {}",workCaseId);
        }

    }

    public AppraisalViewReport fillAppraisalDetailReport(){
        AppraisalViewReport report = new AppraisalViewReport();

        if (!Util.isNull(appraisalView)){
            report.setAppraisalType(appraisalView.getAppraisalType());
            report.setAppraisalCompany(Util.checkNullString(!Util.isNull(appraisalView.getAppraisalCompany()) ? appraisalView.getAppraisalCompany().getName() : SPACE));
            report.setAppraisalDivision(Util.checkNullString(!Util.isNull(appraisalView.getAppraisalDivision()) ? appraisalView.getAppraisalDivision().getName() : SPACE));
            report.setAppraisalName(Util.checkNullString(appraisalView.getAppraisalName()));
            report.setReceivedTaskDate(appraisalView.getReceivedTaskDate());
            report.setLocationOfProperty(Util.checkNullString(!Util.isNull(appraisalView.getLocationOfProperty()) ? appraisalView.getLocationOfProperty().getName() : SPACE));
            report.setProvinceOfProperty(Util.checkNullString(!Util.isNull(appraisalView.getProvinceOfProperty()) ? appraisalView.getProvinceOfProperty().getName() : SPACE));
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

    public List<AppraisalDetailViewReport> fillAppraisalDetailViewReport(String pathsub){
//        init();
        List<AppraisalDetailViewReport> appraisalDetailViewReportList = new ArrayList<AppraisalDetailViewReport>();

        int count = 1;
        if (!Util.isNull(appraisalView.getAppraisalDetailViewList()) && !Util.isZero(appraisalView.getAppraisalDetailViewList().size())){
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
            report.setPath(pathsub);
            appraisalDetailViewReportList.add(report);
            log.debug("--appraisalDetailViewReportList is Null.");
        }
        return appraisalDetailViewReportList;
    }

    public AppraisalContactDetailViewReport fillAppraisalContactDetailViewReport(){
        AppraisalContactDetailViewReport report = new AppraisalContactDetailViewReport();

        if (!Util.isNull(appraisalView.getAppraisalContactDetailView())){
            if (!Util.isNull(appraisalView.getAppraisalContactDetailView().getCustomerName1()) && !Util.isNull(appraisalView.getAppraisalContactDetailView().getContactNo1())){
                report.setCount1(1);
                report.setCustomerName1(Util.checkNullString(appraisalView.getAppraisalContactDetailView().getCustomerName1()));
                report.setContactNo1(Util.checkNullString(appraisalView.getAppraisalContactDetailView().getContactNo1()));
            } else if (!Util.isNull(appraisalView.getAppraisalContactDetailView().getCustomerName2()) && !Util.isNull(appraisalView.getAppraisalContactDetailView().getContactNo2())){
                report.setCount1(2);
                report.setCustomerName2(Util.checkNullString(appraisalView.getAppraisalContactDetailView().getCustomerName2()));
                report.setContactNo2(Util.checkNullString(appraisalView.getAppraisalContactDetailView().getContactNo2()));
            }  else if (!Util.isNull(appraisalView.getAppraisalContactDetailView().getCustomerName3()) && !Util.isNull(appraisalView.getAppraisalContactDetailView().getContactNo3())){
                report.setCount1(2);
                report.setCustomerName3(Util.checkNullString(appraisalView.getAppraisalContactDetailView().getCustomerName3()));
                report.setContactNo3(Util.checkNullString(appraisalView.getAppraisalContactDetailView().getContactNo3()));
            }
        }



        log.debug("--fillAppraisalContactDetailViewReport. {}",report);
        return report;
    }

    public List<ContactRecordDetailViewReport> fillContactRecordDetailViewReport(){
        List<ContactRecordDetailViewReport> contactRecordDetailViewReports = new ArrayList<ContactRecordDetailViewReport>();
        List<ContactRecordDetailView> detailViewList = new ArrayList<ContactRecordDetailView>();
        int count = 1;

        if (Util.safetyList(appraisalView.getContactRecordDetailViewList()).size() > 0){
            log.debug("--detailViewList. {}",detailViewList);
            for (ContactRecordDetailView view : appraisalView.getContactRecordDetailViewList()){
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
