package com.clevel.selos.model.report;


import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class AppraisalViewReport extends ReportModel {

    private int appraisalType;
    private String appraisalCompany;
    private String appraisalDivision;
    private String appraisalName;
    private Date receivedTaskDate;
    private String locationOfProperty;
    private String provinceOfProperty;
    private Date appraisalDate;
    private Date dueDate;
    private String AADAdminRemark;

    private Date appointmentDate;
    private String appointmentCusName;
    private String cancelAppointment;
    private String appointmentRemark;

    private String zoneLocation;

    public AppraisalViewReport() {
    }

    public int getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(int appraisalType) {
        this.appraisalType = appraisalType;
    }

    public String getAppraisalCompany() {
        return appraisalCompany;
    }

    public void setAppraisalCompany(String appraisalCompany) {
        this.appraisalCompany = appraisalCompany;
    }

    public String getAppraisalDivision() {
        return appraisalDivision;
    }

    public void setAppraisalDivision(String appraisalDivision) {
        this.appraisalDivision = appraisalDivision;
    }

    public String getAppraisalName() {
        return appraisalName;
    }

    public void setAppraisalName(String appraisalName) {
        this.appraisalName = appraisalName;
    }


    public String getLocationOfProperty() {
        return locationOfProperty;
    }

    public void setLocationOfProperty(String locationOfProperty) {
        this.locationOfProperty = locationOfProperty;
    }

    public String getProvinceOfProperty() {
        return provinceOfProperty;
    }

    public void setProvinceOfProperty(String provinceOfProperty) {
        this.provinceOfProperty = provinceOfProperty;
    }

    public Date getReceivedTaskDate() {
        return receivedTaskDate;
    }

    public void setReceivedTaskDate(Date receivedTaskDate) {
        this.receivedTaskDate = receivedTaskDate;
    }

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getAADAdminRemark() {
        return AADAdminRemark;
    }

    public void setAADAdminRemark(String AADAdminRemark) {
        this.AADAdminRemark = AADAdminRemark;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentCusName() {
        return appointmentCusName;
    }

    public void setAppointmentCusName(String appointmentCusName) {
        this.appointmentCusName = appointmentCusName;
    }

    public String getCancelAppointment() {
        return cancelAppointment;
    }

    public void setCancelAppointment(String cancelAppointment) {
        this.cancelAppointment = cancelAppointment;
    }

    public String getAppointmentRemark() {
        return appointmentRemark;
    }

    public void setAppointmentRemark(String appointmentRemark) {
        this.appointmentRemark = appointmentRemark;
    }

    public String getZoneLocation() {
        return zoneLocation;
    }

    public void setZoneLocation(String zoneLocation) {
        this.zoneLocation = zoneLocation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("appraisalType", appraisalType)
                .append("appraisalCompany", appraisalCompany)
                .append("appraisalDivision", appraisalDivision)
                .append("appraisalName", appraisalName)
                .append("receivedTaskDate", receivedTaskDate)
                .append("locationOfProperty", locationOfProperty)
                .append("provinceOfProperty", provinceOfProperty)
                .append("appraisalDate", appraisalDate)
                .append("dueDate", dueDate)
                .append("AADAdminRemark", AADAdminRemark)
                .append("appointmentDate", appointmentDate)
                .append("appointmentCusName", appointmentCusName)
                .append("cancelAppointment", cancelAppointment)
                .append("appointmentRemark", appointmentRemark)
                .append("zoneLocation", zoneLocation)
                .toString();
    }
}
