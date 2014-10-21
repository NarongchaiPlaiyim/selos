package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.CSVService;
import com.clevel.selos.businesscontrol.isa.DownloadService;
import com.clevel.selos.businesscontrol.isa.IsaBusinessControl;
import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ManageUserActive;
import com.clevel.selos.model.db.master.Role;
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
    private Date dateFrom;
    private Date dateTo;
    private final String  COMMA_DELIMITED = ",";
    private StreamedContent streamedContent;
    private String roleId;
    private List<Role> userRoleList;
    //dialogMessage
    private String messageHeader;
    private String message;
    private ISAViewReport isaViewReport;

    @Inject CsvExport csvExport;
//    private enum Result{Success};
    @Inject private DownloadService downloadService;
    @Inject private STPExecutor stpExecutor;

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
        isaViewReport = new ISAViewReport();
        init();
    }

    private void init(){
        log.debug("-- init()");
        dateFrom = DateTime.now().toDate();
        dateTo = DateTime.now().toDate();
        onLoadRole();
    }

    private void onLoadRole(){
        log.debug("-- onLoadRole()");
        userRoleList = isaBusinessControl.getAllRole();
        log.debug("userRoleList {}",userRoleList.size());
    }

    public void onSubmitExportCSV(){
        List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();
        StringBuilder nameISAUserProfile = new StringBuilder();
        nameISAUserProfile = nameISAUserProfile.append("USERPROFILE_").append(Util.getFileNameForISA());

        StringBuilder builder =  new StringBuilder();
        builder.append("Seq"); builder.append(COMMA_DELIMITED);
        builder.append("User Id"); builder.append(COMMA_DELIMITED);
        builder.append("User Name"); builder.append(COMMA_DELIMITED);
        builder.append("Active"); builder.append(COMMA_DELIMITED);
        builder.append("Role"); builder.append(COMMA_DELIMITED);
        builder.append("Team"); builder.append(COMMA_DELIMITED);
        builder.append("Department"); builder.append(COMMA_DELIMITED);
        builder.append("Division"); builder.append(COMMA_DELIMITED);
        builder.append("Region"); builder.append(COMMA_DELIMITED);
        builder.append("Title"); builder.append(COMMA_DELIMITED);
        builder.append("Cteate date"); builder.append(COMMA_DELIMITED);
        builder.append("Last sign on date"); builder.append('\n');

        try {
            viewReportList = isaBusinessControl.getUserProFileByUser();
        } catch (SQLException e) {
            log.debug("--on getuserProfile. {}",e.getMessage());
        }

        int rowNumber = 1;

        if (!Util.isNull(viewReportList)){
            log.debug("--viewReportList is not null. [{}]",viewReportList.size());
            for (ISAViewReport report : viewReportList){
                builder.append(rowNumber).append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getUserId()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getUserName()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getActive()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getRoleId()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getTeam()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getDepartment()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getDivision()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getRegion()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getTitle()) + '"').append(COMMA_DELIMITED);
                if (!Util.isNull(report.getCreateDate())){
                    builder.append('"' + DateTimeUtil.convertToStringDDMMYYYYHHmmss(report.getCreateDate()) + '"').append(COMMA_DELIMITED);
                } else {
                    builder.append(COMMA_DELIMITED);
                }
                if (!Util.isNull(report.getLastLogOn())){
                    builder.append('"' + DateTimeUtil.convertToStringDDMMYYYYHHmmss(report.getLastLogOn()) + '"').append('\n');
                } else {
                    builder.append(COMMA_DELIMITED).append('\n');
                }

                rowNumber++;
            }
            csvExport.exportCSV(nameISAUserProfile.toString(),builder.toString());
        }
    }

    public void onPrintActivity(){
        List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();
        StringBuilder nameISAUserProfile = new StringBuilder();
        HashMap map = new HashMap<String, Object>();
        nameISAUserProfile = nameISAUserProfile.append("Activity_").append(Util.getFileNameForISA());

        StringBuilder builder =  new StringBuilder();
        builder.append("No"); builder.append(COMMA_DELIMITED);
        builder.append("ADMIN_TASK"); builder.append(COMMA_DELIMITED);
        builder.append("EMP_ID"); builder.append(COMMA_DELIMITED);
        builder.append("EMP_NAME"); builder.append(COMMA_DELIMITED);
        builder.append("OLD_DATA"); builder.append(COMMA_DELIMITED);
        builder.append("NEW_DATA"); builder.append(COMMA_DELIMITED);
        builder.append("MODIFY_DATE"); builder.append(COMMA_DELIMITED);
        builder.append("MODIFY_BY"); builder.append(COMMA_DELIMITED);
        builder.append("ADMIN_NAME"); builder.append('\n');

        try {
            map.put("fromDate", DateTimeUtil.convertToStringDDMMYYYY(dateFrom,Locale.ENGLISH));
            map.put("toDate", DateTimeUtil.convertToStringDDMMYYYY(dateTo,Locale.ENGLISH));
            viewReportList = isaBusinessControl.getUserProFileByISA(map);
        } catch (SQLException e) {
            log.debug("--on getuserProfile. {}",e.getMessage());
        }

        int rowNumber = 1;

        if (!Util.isNull(viewReportList)){
            log.debug("--viewReportList is not null. [{}]",viewReportList.size());
            for (ISAViewReport report : viewReportList){
                builder.append(rowNumber).append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getAdminTask()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getEmpID()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getEmpName()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getOldData()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getNewData()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(DateTimeUtil.convertToStringDDMMYYYY(report.getModifyDate())) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getModifyBy()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getAdminName()) + '"').append('\n');

                rowNumber++;
            }
            csvExport.exportCSV(nameISAUserProfile.toString(),builder.toString());
        }
    }

    public void onPrintMatrix(){
        List<ISAViewReport> viewReportList = new ArrayList<ISAViewReport>();
        StringBuilder nameISAUserProfile = new StringBuilder();
        nameISAUserProfile = nameISAUserProfile.append("Matrix_").append(Util.getFileNameForISA());

        StringBuilder builder =  new StringBuilder();
        builder.append("STEP_ID").append(COMMA_DELIMITED);
        builder.append("STEP_NAME").append(COMMA_DELIMITED);
        builder.append("ROLE_ID").append(COMMA_DELIMITED);
        builder.append("ROLE_NAME").append('\n');

        try {
            log.debug("isaViewReport.getRole() {}",isaViewReport.getRole());
            viewReportList = isaBusinessControl.getUserAccessMatrix(isaViewReport.getRole());
        } catch (Exception e){
            log.debug("onPrintMatrix Exception : ",e);
        }

        if (Util.isSafetyList(viewReportList)){
            log.debug("viewReportList size() [{}]",viewReportList.size());
            for (ISAViewReport report : viewReportList){
                builder.append('"' + Util.EmptyString(report.getRoleId()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getRoleName()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getScreenId()) + '"').append(COMMA_DELIMITED);
                builder.append('"' + Util.EmptyString(report.getScreenName()) + '"').append('\n');
            }
            csvExport.exportCSV(nameISAUserProfile.toString(),builder.toString());
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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<Role> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<Role> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public ISAViewReport getIsaViewReport() {
        return isaViewReport;
    }

    public void setIsaViewReport(ISAViewReport isaViewReport) {
        this.isaViewReport = isaViewReport;
    }
}
