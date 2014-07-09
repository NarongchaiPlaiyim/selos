package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.DownloadService;
import com.clevel.selos.businesscontrol.isa.IsaBusinessControl;
import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.report.ISAViewReport;
import com.clevel.selos.model.view.isa.IsaAuditLogView;
import com.clevel.selos.model.view.isa.IsaUserDetailView;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.CsvExport;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private StreamedContent streamedContent;
    //dialogMessage
    private String messageHeader;
    private String message;
    @Inject
    CsvExport csvExport;
    private enum Result{Success};
    @Inject
    private DownloadService downloadService;
    @Inject
    private STPExecutor stpExecutor;

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
        init();
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

//    public void onSubmitReport(){
//        log.debug("--On onSubmitReport. fromdate. {}, and todate.{}",dateFrom,dateTo);
//        onPrintViolation(dateFrom, dateTo);
//    }

    public void onPrintLogonOver90(){
        log.debug("--on onPrintLogonOver90.");
//        reportView.setNameISAReportLogonOver90(nameLogonOver90.toString());

        try {
//            HashMap map = new HashMap<String, Object>();
            List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();
            ResultSet rs = stpExecutor.getLogonOver90();
            StringBuilder nameLogonOver90 = new StringBuilder();
            nameLogonOver90 = nameLogonOver90.append("NotLogonOver_90_").append(Util.getFileNameForISA());
            int i = 1;

            if (!Util.isNull(rs)){
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
                generatePDF(pathISALogonOver90, new HashMap<String, Object>(), nameLogonOver90.toString(), viewReportList);
            } else {
                ISAViewReport viewReport = new ISAViewReport();
                viewReportList.add(viewReport);
            }
        } catch (SQLException e) {
            log.debug("on getLogonOver90. {}",e);
        } catch (Exception e) {
            log.debug("onPrintLogonOver90. {}",e);
        }
    }

    public void onPrintViolation(){
        log.debug("--on onPrintViolation.");
        try {
            StringBuilder nameISAViolation = new StringBuilder();
            nameISAViolation = nameISAViolation.append("Violation_").append(Util.getFileNameForISA());
            HashMap map = new HashMap<String, Object>();
            List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();
            map.put("fromDate", DateTimeUtil.convertToStringDDMMYYYY(dateFrom));
            map.put("toDate", DateTimeUtil.convertToStringDDMMYYYY(dateTo));
            ResultSet rs = stpExecutor.getViolation(map);

            if (!Util.isNull(rs)){
                while (rs.next()){
                    ISAViewReport viewReport = new ISAViewReport();
                    viewReport.setUserId(rs.getString("USER_ID"));
                    viewReport.setIpAddress(rs.getString("IP_ADDRESS"));
                    viewReport.setLogin(rs.getTimestamp("LOGIN_DATE"));
                    viewReport.setStatus(rs.getString("STATUS"));
                    viewReport.setDescrition(rs.getString("DESCRIPTION"));
                    viewReportList.add(viewReport);
                }
                generatePDF(pathISAViolation, map, nameISAViolation.toString(), viewReportList);
            } else {
                ISAViewReport viewReport = new ISAViewReport();
                viewReportList.add(viewReport);
            }
        } catch (SQLException e) {
            log.debug("----on getViolation. {}",e);
        } catch (Exception e) {
            log.debug("onPrintViolation. {}",e);
        }
    }

    public void onPrintUserProfile(){

        try {
            StringBuilder nameISAUserProfile = new StringBuilder();
            nameISAUserProfile = nameISAUserProfile.append("UserProfile_").append(Util.getFileNameForISA());
//            HashMap map = new HashMap<String, Object>();
            List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();
            ResultSet rs = stpExecutor.getUserProfile();
            int i = 1;

            if (!Util.isNull(rs)){
                while (rs.next()){
                    ISAViewReport viewReport = new ISAViewReport();
                    viewReport.setRow(i++);
                    viewReport.setUserId(rs.getString("USER_ID"));
                    viewReport.setUserName(rs.getString("USER_NAME"));
                    viewReport.setBuCode(rs.getString("BU_CODE"));
                    viewReport.setTeam(rs.getString("TEAM"));
                    viewReport.setRole(rs.getString("ROLE_NAME"));
                    viewReport.setStatus(rs.getString("STATUS"));
                    viewReport.setLogin(rs.getTimestamp("LAST_DATE"));
                    viewReport.setCreateDate(rs.getTimestamp("CREATE_DATE"));
                    viewReport.setCreateBy(rs.getString("CREATE_BY"));
                    viewReport.setModifyDate(rs.getTimestamp("MODIFY_DATE"));
                    viewReport.setModifyBy(rs.getString("MODIFY_BY"));
                    viewReportList.add(viewReport);
                }
                generatePDF(pathISAUserProfile, new HashMap<String, Object>(), nameISAUserProfile.toString(), viewReportList);
            } else {
                ISAViewReport viewReport = new ISAViewReport();
                viewReportList.add(viewReport);
            }
        } catch (SQLException e) {
            log.debug("----on getuserProfile. {}",e);
        } catch (Exception e) {
            log.debug("onPrintUserProfile. {}",e);
        }
    }

    private void generatePDF(String fileName, Map<String,Object> parameters,String pdfName,Collection reportList) throws JRException, IOException {
        log.debug("generate pdf.");
        JasperReport jasperReport = JasperCompileManager.compileReport(fileName);

        JasperPrint print ;

        log.info("parameters: {}",parameters);

        JRDataSource dataSource = new JRBeanCollectionDataSource(reportList);
        if(dataSource != null && reportList != null && reportList.size() > 0){
            print = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        } else {
            print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        }
        log.debug("--Pring report.");

        try {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment; filename="+pdfName+".pdf");
            ServletOutputStream servletOutputStream=response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(print, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
            log.debug("generatePDF completed.");

        } catch (JRException e) {
            log.error("Error generating pdf report!", e);
        }
    }



    public void onSubmitExportCSV(){
        log.debug("-- onSubmitExportCSV()");
        RequestContext context = RequestContext.getCurrentInstance();
        messageHeader = "Export to CSV";
        try {
            final String fullPath = isaBusinessControl.exportProcess();
            if(!Util.isNull(fullPath)){
                streamedContent = downloadService.process(fullPath);
            }
            message = Result.Success.toString();
        } catch (Exception e){
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
            } else {
                message = e.getMessage();
            }
        }
        context.execute("msgBoxSystemMessageDlg.show()");
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

    public StreamedContent getStreamedContent() {
        return streamedContent;
    }

    public void setStreamedContent(StreamedContent streamedContent) {
        this.streamedContent = streamedContent;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
