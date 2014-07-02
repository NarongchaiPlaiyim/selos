package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.IsaBusinessControl;
import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.report.ISAViewReport;
import com.clevel.selos.model.view.ReportView;
import com.clevel.selos.model.view.isa.IsaAuditLogView;
import com.clevel.selos.model.view.isa.IsaUserDetailView;
import com.clevel.selos.report.ReportService;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.CsvExport;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ViewScoped
@ManagedBean(name = "isaReport")
public class IsaReport implements Serializable {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private IsaBusinessControl isaBusinessControl;
    private int notLogonOverDay;
    private Date dateFrom;
    private Date dateTo;
    private Date currentDate;
    private final String  COMMA_DELIMITED = ",";
    private final static SimpleDateFormat dateFormatFile = new SimpleDateFormat("dd_mm_yyyy");
    private ReportView reportView;

    @Inject
    CsvExport csvExport;

    @Inject
    private STPExecutor stpExecutor;

    private ReportService reportService;

    @Inject
    @Config(name = "reportisa.violation")
    String pathISAViolation;

    @Inject
    @Config(name = "reportisa.userprofile")
    String pathISAUserProfile;

    @Inject
    @Config(name = "reportisa.logonover90")
    String pathISALogonOver90;

    @Inject
    public IsaReport(){

    }

    @PostConstruct
    public void onCreate(){

    }

    private void init(){
        log.debug("-- init()");
        dateFrom = DateTime.now().toDate();
        dateTo = DateTime.now().toDate();
    }






    public void notLogonOver() {
        log.debug("notLogonOver()");

        List<IsaUserDetailView> list=new ArrayList<IsaUserDetailView>();
        StringBuilder builder = new StringBuilder();
        builder.append("No");builder.append(COMMA_DELIMITED);
        builder.append("User ID");builder.append(COMMA_DELIMITED);
        builder.append("User Name");builder.append(COMMA_DELIMITED);
        builder.append("Email Address");builder.append(COMMA_DELIMITED);
        builder.append("Bu Code");builder.append(COMMA_DELIMITED);
        builder.append("Last IP");builder.append(COMMA_DELIMITED);
        builder.append("Last Logon");builder.append(COMMA_DELIMITED);
        builder.append("Phone Ext");builder.append(COMMA_DELIMITED);
        builder.append("Phone Number");builder.append(COMMA_DELIMITED);
        builder.append("Role");builder.append(COMMA_DELIMITED);
        builder.append("Department");builder.append(COMMA_DELIMITED);
        builder.append("Division");builder.append(COMMA_DELIMITED);
        builder.append("Region");builder.append(COMMA_DELIMITED);
        builder.append("Team");builder.append(COMMA_DELIMITED);
        builder.append("Title");builder.append(COMMA_DELIMITED);
        builder.append("Zone");builder.append(COMMA_DELIMITED);
        builder.append("Active");builder.append(COMMA_DELIMITED);
        builder.append("Status");builder.append("\n");

        try{
            list=isaBusinessControl.getUserNotLogonOver(notLogonOverDay);
        }catch (Exception e){
            log.debug("Excepion : {}",e.getMessage());
        }
        int number=1;
        for(IsaUserDetailView isaUserDetailView :list){
            builder.append("'"+number+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getUserId()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getUserName()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getEmailAddress()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getBuCode()+"'");builder.append(COMMA_DELIMITED);
//            builder.append("'"+ isaUserDetailView.getLastIp()+"'");builder.append(COMMA_DELIMITED);
//            builder.append("'"+ isaUserDetailView.getLastLogon()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getPhoneExt()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getPhoneNumber()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getRole()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getDepartment()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getDivision()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getRegion()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getTeam()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getTitle()+"'");builder.append(COMMA_DELIMITED);
//            builder.append("'"+ isaUserDetailView.getZone()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getActive()+"'");builder.append(COMMA_DELIMITED);
            builder.append("'"+ isaUserDetailView.getUserStatus()+"'");builder.append("\n");

            number++;
        }
        System.out.println(builder.toString());

        csvExport.exportCSV("notLogonOver_"+dateFormatFile.format(new Date()),builder.toString());


    }


    public void userReport() {
        log.debug("userReport()");

        List<IsaUserDetailView> list=new ArrayList<IsaUserDetailView>();
        StringBuilder builder = new StringBuilder();
        builder.append("No");builder.append(COMMA_DELIMITED);
        builder.append("User ID");builder.append(COMMA_DELIMITED);
        builder.append("User Name");builder.append(COMMA_DELIMITED);
        builder.append("Email Address");builder.append(COMMA_DELIMITED);
        builder.append("Bu Code");builder.append(COMMA_DELIMITED);
        builder.append("Last IP");builder.append(COMMA_DELIMITED);
        builder.append("Last Logon");builder.append(COMMA_DELIMITED);
        builder.append("Phone Ext");builder.append(COMMA_DELIMITED);
        builder.append("Phone Number");builder.append(COMMA_DELIMITED);
        builder.append("Role");builder.append(COMMA_DELIMITED);
        builder.append("Department");builder.append(COMMA_DELIMITED);
        builder.append("Division");builder.append(COMMA_DELIMITED);
        builder.append("Region");builder.append(COMMA_DELIMITED);
        builder.append("Team");builder.append(COMMA_DELIMITED);
        builder.append("Title");builder.append(COMMA_DELIMITED);
        builder.append("Zone");builder.append(COMMA_DELIMITED);
        builder.append("Active");builder.append(COMMA_DELIMITED);
        builder.append("Status");builder.append("\n");

        try{
            list=isaBusinessControl.getUserReportList();
        }catch (Exception e){
            log.debug("Excepion : {}",e.getMessage());
        }
        int number=1;
        if(list!=null){
            for(IsaUserDetailView isaUserDetailView :list){
                builder.append("'"+number+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getUserId()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getUserName()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getEmailAddress()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getBuCode()+"'");builder.append(COMMA_DELIMITED);
//                builder.append("'"+ isaUserDetailView.getLastIp()+"'");builder.append(COMMA_DELIMITED);
//                builder.append("'"+ isaUserDetailView.getLastLogon()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getPhoneExt()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getPhoneNumber()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getRole()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getDepartment()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getDivision()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getRegion()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getTeam()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getTitle()+"'");builder.append(COMMA_DELIMITED);
//                builder.append("'"+ isaUserDetailView.getZone()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getActive()+"'");builder.append(COMMA_DELIMITED);
                builder.append("'"+ isaUserDetailView.getUserStatus()+"'");builder.append("\n");

                number++;
            }
        }
        System.out.println(builder.toString());

        csvExport.exportCSV("userReport_"+dateFormatFile.format(new Date()),builder.toString());

    }


    public void violationReport() {
        log.debug("userReport()");

        List<IsaAuditLogView> list=new ArrayList<IsaAuditLogView>();
        StringBuilder builder =  new StringBuilder();
        builder.append("No"); builder.append(COMMA_DELIMITED);
        builder.append("User ID"); builder.append(COMMA_DELIMITED);
        builder.append("User Name"); builder.append(COMMA_DELIMITED);
        builder.append("IP Address"); builder.append(COMMA_DELIMITED);
        builder.append("Login Date"); builder.append(COMMA_DELIMITED);
        builder.append("Status"); builder.append(COMMA_DELIMITED);
        builder.append("Description"); builder.append('\n');

        try{
            list=isaBusinessControl.getViolationReport();
        }catch (Exception e){
             log.debug("Excepion : {}",e.getMessage());
        }
        int number=1;
        if(list!=null){
            for(IsaAuditLogView isaAuditLogView :list){
                builder.append('"'+number+'"');builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getUserId()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getUserName()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getIpAddress()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getActionDate()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getResult()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getResultDesc()+'"'); builder.append('\n');
                System.out.println(isaAuditLogView.getActionDate());
                number++;
            }
        }
        System.out.println(builder.toString());

        csvExport.exportCSV("violationReport_"+dateFormatFile.format(new Date()),builder.toString());

    }

    public void userMaintenanceReport() {
        log.debug("userMaintenanceReport()");

        List<IsaAuditLogView> list=new ArrayList<IsaAuditLogView>();
        StringBuilder builder =  new StringBuilder();
        builder.append("No"); builder.append(COMMA_DELIMITED);
        builder.append("User ID"); builder.append(COMMA_DELIMITED);
        builder.append("User Name"); builder.append(COMMA_DELIMITED);
        builder.append("Action"); builder.append(COMMA_DELIMITED);
        builder.append("Action Desc"); builder.append(COMMA_DELIMITED);
        builder.append("IP Address"); builder.append(COMMA_DELIMITED);
        builder.append("Login Date"); builder.append(COMMA_DELIMITED);
        builder.append("Status"); builder.append(COMMA_DELIMITED);
        builder.append("Description"); builder.append('\n');

        try{
            System.out.println(dateFrom +" "+ dateTo);
            list=isaBusinessControl.getUserMaintenanceReport(dateFrom,dateTo);
        }catch (Exception e){
            log.debug("Excepion : {}",e.getMessage());
        }
        int number=1;
        if(list!=null){
            for(IsaAuditLogView isaAuditLogView :list){
                builder.append('"'+number+'"');builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getUserId()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getUserName()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getAction()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getActionDesc()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getIpAddress()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getActionDate()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getResult()+'"'); builder.append(COMMA_DELIMITED);
                builder.append('"'+ isaAuditLogView.getResultDesc()+'"'); builder.append('\n');

                number++;
            }
        }
        System.out.println(builder.toString());

        csvExport.exportCSV("userMaintenanceReport_"+dateFormatFile.format(new Date()),builder.toString());

    }

    public void onSubmitReport(){
        log.debug("--On onSubmitReport. fromdate. {}, and todate.{}",dateFrom,dateTo);
        onPrintViolation(dateFrom, dateTo);
    }

    public void onPrintLogonOver90(){
        log.debug("--on onPrintLogonOver90.");
//        reportView.setNameISAReportLogonOver90(nameLogonOver90.toString());

        try {
            HashMap map = new HashMap<String, Object>();
            List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();
            ResultSet rs = stpExecutor.getLogonOver90();
            StringBuilder nameLogonOver90 = new StringBuilder();
            nameLogonOver90 = nameLogonOver90.append("NotLogonOver_90_").append(Util.getFileNameForISA());
            int i = 1;

            while (rs.next()){
                ISAViewReport viewReport = new ISAViewReport();
                viewReport.setRow(i++);
                viewReport.setUserId(rs.getString("USER_ID"));
                viewReport.setUserName(rs.getString("USER_NAME"));
                viewReport.setCreateDate(rs.getTimestamp("CREATE_DATE"));
                viewReport.setLogin(rs.getTimestamp("LAST_LOGIN"));
                viewReport.setStatus(rs.getString("STATUS"));
                viewReport.setNumberOfDay(rs.getString("NUMBER_OF_DAY"));
                viewReportList.add(viewReport);
            }
            reportService.generatePDF(pathISALogonOver90, map, nameLogonOver90.toString(), viewReportList);
        } catch (SQLException e) {
            log.debug("on getLogonOver90. {}",e);
        } catch (Exception e) {
            log.debug("onPrintLogonOver90. {}",e);
        }
    }

    public void onPrintViolation(final Date form, final Date to){
        log.debug("--on onPrintViolation.");
//        reportView.setNameISAReportViolation(nameISAViolation.toString());
//        StringBuilder nameISAUserProfile = new StringBuilder();
//        nameISAUserProfile = nameISAUserProfile.append("UserProfile_").append(Util.getFileNameForISA());
//        reportView.setNameISAReportUserProfile(nameISAUserProfile.toString());

        try {
            StringBuilder nameISAViolation = new StringBuilder();
            nameISAViolation = nameISAViolation.append("Violation_").append(Util.getFileNameForISA());
            HashMap map = new HashMap<String, Object>();
            List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();
            map.put("fromDate", DateTimeUtil.convertToStringDDMMYYYY(form));
            map.put("toDate", DateTimeUtil.convertToStringDDMMYYYY(to));
            ResultSet rs = stpExecutor.getViolation(map);
            log.debug("--rs in onPrintViolation. {}",rs);

            while (rs.next()){
                ISAViewReport viewReport = new ISAViewReport();
                viewReport.setUserId(rs.getString("USER_ID"));
                viewReport.setIpAddress(rs.getString("IP_ADDRESS"));
                viewReport.setLogin(rs.getTimestamp("LOGIN_DATE"));
                viewReport.setStatus(rs.getString("STATUS"));
                viewReport.setDescrition(rs.getString("DESCRIPTION"));
                viewReportList.add(viewReport);
            }
            log.debug("--Path Report. {},--NameReport. {}",pathISAViolation,reportView.getNameISAReportViolation());
            reportService.generatePDF(pathISAViolation, map, nameISAViolation.toString(), viewReportList);
        } catch (SQLException e) {
            log.debug("----on getViolation. {}",e);
        } catch (Exception e) {
            log.debug("onPrintLogonOver90. {}",e);
        }
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
