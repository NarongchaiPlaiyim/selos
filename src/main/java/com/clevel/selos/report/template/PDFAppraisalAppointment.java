package com.clevel.selos.report.template;


import com.clevel.selos.businesscontrol.AppraisalAppointmentControl;
import com.clevel.selos.businesscontrol.AppraisalRequestControl;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.report.*;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PDFAppraisalAppointment implements Serializable {

    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @Config(name = "report.subreport")
    String pathsub;

    @Inject AppraisalAppointmentControl appraisalAppointmentControl;
    @Inject private AppHeaderView appHeaderView;
    @Inject private WorkCaseDAO workCaseDAO;
    @Inject private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject private AppraisalRequestControl appraisalRequestControl;

    private AppraisalView appraisalView;
    private long workCaseId;
    private long workCasePreScreenId;
    private long statusId;
    private final String SPACE = " ";
    private WorkCase workCase;
    private WorkCasePrescreen workCasePrescreen;

    public PDFAppraisalAppointment() {
    }

    public void init(){
        HttpSession session = FacesUtil.getSession(false);
//        appraisalView = new AppraisalView();

        workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        workCasePreScreenId = Util.parseLong(session.getAttribute("workCasePreScreenId"), 0);
        statusId = Util.parseLong(session.getAttribute("statusId"), 0);

        if (!Util.isNull(workCaseId) || !Util.isNull(workCasePreScreenId)){
            log.info("workCaseID: {}",workCaseId);

            appraisalView = appraisalAppointmentControl.getAppraisalAppointment(workCaseId, workCasePreScreenId, statusId);
            log.debug("--appraisalView. {}", appraisalView);

            if(appraisalView == null){
                appraisalView = new AppraisalView();
            }

            if (!Util.isZero(workCaseId)){
                workCase = workCaseDAO.findById(workCaseId);

            } else {
                workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            }
        } else {
            log.debug("--workcase is Null. {}", workCaseId);
        }
    }

    private String getZoneLocation(){
        String bdmUserId = "";
        if (!Util.isZero(workCaseId) && !Util.isNull(workCase)){
            bdmUserId = !Util.isNull(workCase.getCreateBy()) ? workCase.getCreateBy().getId() : "";
        } else if (!Util.isZero(workCasePreScreenId) && !Util.isNull(workCasePrescreen)){
            bdmUserId = !Util.isNull(workCasePrescreen.getCreateBy()) ? workCasePrescreen.getCreateBy().getId() : "";
        }

        return appraisalRequestControl.getZoneLocation(bdmUserId);
    }

    public AppraisalViewReport fillAppraisalDetailReport(){
        AppraisalViewReport report = new AppraisalViewReport();

        if (!Util.isNull(appraisalView)){
            report.setAppraisalType(appraisalView.getAppraisalType());
            report.setAppraisalCompany(Util.checkNullString(!Util.isNull(appraisalView.getAppraisalCompany()) ? appraisalView.getAppraisalCompany().getName() : SPACE));
            report.setAppraisalDivision(Util.checkNullString(!Util.isNull(appraisalView.getAppraisalDivision()) ? appraisalView.getAppraisalDivision().getName() : SPACE));
            report.setAppraisalName(Util.checkNullString(appraisalView.getAppraisalName()));
            report.setReceivedTaskDate(DateTimeUtil.getCurrentDateTH(appraisalView.getReceivedTaskDate()));
            report.setLocationOfProperty(Util.checkNullString(!Util.isNull(appraisalView.getLocationOfProperty()) ? appraisalView.getLocationOfProperty().getName() : SPACE));
            report.setProvinceOfProperty(Util.checkNullString(!Util.isNull(appraisalView.getProvinceOfProperty()) ? appraisalView.getProvinceOfProperty().getName() : SPACE));
            report.setAppraisalDate(DateTimeUtil.getCurrentDateTH(appraisalView.getAppraisalDate()));
            report.setDueDate(DateTimeUtil.getCurrentDateTH(appraisalView.getDueDate()));
            report.setAADAdminRemark(Util.checkNullString(appraisalView.getAADAdminRemark()));

            report.setAppointmentDate(DateTimeUtil.getCurrentDateTH(appraisalView.getAppointmentDate()));
            report.setAppointmentCusName(Util.checkNullString(appraisalView.getAppointmentCusName()));
            if ("0".equalsIgnoreCase(appraisalView.getCancelAppointment())){
                report.setCancelAppointment(msg.get("app.appraisal.label.cancelAppointment.select.postpone"));
            } else if("1".equalsIgnoreCase(appraisalView.getCancelAppointment())){
                report.setCancelAppointment(msg.get("app.appraisal.label.cancelAppointment.select.abort"));
            } else {
                report.setCancelAppointment(SPACE);
            }
            report.setAppointmentRemark(Util.checkNullString(appraisalView.getAppointmentRemark()));

            report.setZoneLocation(Util.checkNullString(getZoneLocation()));
        } else {
            log.debug("--fillAppraisalDetailReport is Null.");
        }
        return report;
    }

    public List<AppraisalDetailViewReport> fillAppraisalDetailViewReport(String pathsub){
        List<AppraisalDetailViewReport> appraisalDetailViewReportList = new ArrayList<AppraisalDetailViewReport>();

        int count = 1;
        if (Util.isSafetyList(appraisalView.getAppraisalDetailViewList())){
            log.debug("--AppraisalDetailViewList. {}",appraisalView.getAppraisalDetailViewList().size());
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

        if (Util.isSafetyList(appraisalView.getContactRecordDetailViewList())){
            log.debug("--appraisalView.getContactRecordDetailViewList(). {}",appraisalView.getContactRecordDetailViewList().size());
            for (ContactRecordDetailView view : appraisalView.getContactRecordDetailViewList()){
                ContactRecordDetailViewReport report = new ContactRecordDetailViewReport();
                report.setCount(count++);
                report.setPath(pathsub);
                report.setCallingDate(DateTimeUtil.getCurrentDateTimeTH(view.getCallingDate()));
                report.setCallingResult(view.getCallingResult());
                report.setAcceptResult(view.getAcceptResult());
                report.setNextCallingDate(DateTimeUtil.getCurrentDateTimeTH(view.getNextCallingDate()));
                report.setReasonDescription(Util.checkNullString(!Util.isNull(view.getReason()) ? view.getReason().getDescription() : SPACE));
                report.setRemark(Util.checkNullString(view.getRemark()));
                report.setStatusDescription(Util.checkNullString(!Util.isNull(view.getStatus()) ? view.getStatus().getDescription() : SPACE));
                report.setDisplayName(Util.checkNullString(view.getCreateBy().getDisplayName()));
                contactRecordDetailViewReports.add(report);
            }
        } else {
            ContactRecordDetailViewReport report = new ContactRecordDetailViewReport();
            contactRecordDetailViewReports.add(report);
            log.debug("--appraisalView.getContactRecordDetailViewList() is Null.");
        }
        return contactRecordDetailViewReports;
    }

    public HeaderAndFooterReport fillHeader(){
        HeaderAndFooterReport report = new HeaderAndFooterReport();

        HttpSession session = FacesUtil.getSession(false);
        appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");


        //Detail 1
        if (!Util.isNull(appHeaderView)){
            log.debug("--Header. {}",appHeaderView);
            report.setCaseStatus(Util.checkNullString(appHeaderView.getCaseStatus()));
            report.setBdmName(Util.checkNullString(appHeaderView.getBdmName()));
            report.setBdmPhoneNumber(Util.checkNullString(appHeaderView.getBdmPhoneNumber()));
            report.setBdmPhoneExtNumber(Util.checkNullString(appHeaderView.getBdmPhoneExtNumber()));
            report.setBdmZoneName(Util.checkNullString(appHeaderView.getBdmZoneName()));
            report.setBdmRegionName(Util.checkNullString(appHeaderView.getBdmRegionName()));
            report.setSubmitDate(Util.checkNullString(appHeaderView.getSubmitDate()));
            report.setUwName(Util.checkNullString(appHeaderView.getUwName()));
            report.setUwPhoneNumber(Util.checkNullString(appHeaderView.getUwPhoneNumber()));
            report.setUwPhoneExtNumber(Util.checkNullString(appHeaderView.getUwPhoneExtNumber()));
            report.setUwTeamName(Util.checkNullString(appHeaderView.getUwTeamName()));
            report.setRequestType(Util.checkNullString(appHeaderView.getRequestType()));
            report.setAppNo(Util.checkNullString(appHeaderView.getAppNo()));
            report.setAppRefNo(Util.checkNullString(appHeaderView.getAppRefNo()));
            report.setAppRefDate(Util.checkNullString(appHeaderView.getAppRefDate()));
            report.setProductGroup(Util.checkNullString(appHeaderView.getProductGroup()));
            report.setRefinance(Util.checkNullString(appHeaderView.getRefinance()));



            if (Util.isSafetyList(appHeaderView.getBorrowerHeaderViewList())){
                log.debug("--getBorrowerHeaderViewList Size. {}",appHeaderView.getBorrowerHeaderViewList().size());
                for (int i = 0;i < appHeaderView.getBorrowerHeaderViewList().size() && i < 5; i++){
                    switch (i){
                        case 0 : report.setBorrowerName(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName()));
                            report.setPersonalId(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId()));
                            break;
                        case 1 : report.setBorrowerName2(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName()));
                            report.setPersonalId2(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId()));
                            break;
                        case 2 : report.setBorrowerName3(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName()));
                            report.setPersonalId3(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId()));
                            break;
                        case 3 : report.setBorrowerName4(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName()));
                            report.setPersonalId4(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId()));
                            break;
                        case 4 : report.setBorrowerName5(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getBorrowerName()));
                            report.setPersonalId5(Util.checkNullString(appHeaderView.getBorrowerHeaderViewList().get(i).getPersonalId()));
                            break;
                    }
                }
            }

            report.setCreditDecision(Util.checkNullString(appHeaderView.getProductGroup()));

            if (!Util.isNull(workCase)){
                report.setApprovedDate(DateTimeUtil.getCurrentDateTH(workCase.getCompleteDate()));
            } else {
                report.setApprovedDate(DateTimeUtil.getCurrentDateTH(workCasePrescreen.getCompleteDate()));
            }

        } else {
            log.debug("--Header is Null. {}",appHeaderView);
        }
        return report;
    }
}
