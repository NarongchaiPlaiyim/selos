package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.ContactRecordDetail;
import com.clevel.selos.model.db.working.WorkCase;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AppraisalView implements Serializable {

    private long id;
    private String zoneLocation;
    private String bdmRemark;
    private int appraisalType;
    private AppraisalCompany appraisalCompany;
    private AppraisalDivision appraisalDivision;
    private String appraisalName;
    private Date receivedTaskDate;
    private LocationProperty locationOfProperty;
    private Province provinceOfProperty;
    private Date appraisalDate;
    private Date dueDate;
    private String AADAdminRemark;
    private Date appointmentDate;
    private String appointmentTime;
    private String appointmentCusName;
    private String cancelAppointment;
    private String appointmentRemark;
    private List<AppraisalDetailView> appraisalDetailViewList;
    private List<AppraisalContactDetailView> appraisalContactDetailViewList;
    private List<ContactRecordDetailView> contactRecordDetailViewList;
    private List<CollateralDetailView> collateralDetailViewList;
    private WorkCase workCase;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public AppraisalView(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getZoneLocation() {
        return zoneLocation;
    }

    public void setZoneLocation(String zoneLocation) {
        this.zoneLocation = zoneLocation;
    }

    public String getBdmRemark() {
        return bdmRemark;
    }

    public void setBdmRemark(String bdmRemark) {
        this.bdmRemark = bdmRemark;
    }

    public int getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(int appraisalType) {
        this.appraisalType = appraisalType;
    }

    public AppraisalCompany getAppraisalCompany() {
        return appraisalCompany;
    }

    public void setAppraisalCompany(AppraisalCompany appraisalCompany) {
        this.appraisalCompany = appraisalCompany;
    }

    public AppraisalDivision getAppraisalDivision() {
        return appraisalDivision;
    }

    public void setAppraisalDivision(AppraisalDivision appraisalDivision) {
        this.appraisalDivision = appraisalDivision;
    }

    public String getAppraisalName() {
        return appraisalName;
    }

    public void setAppraisalName(String appraisalName) {
        this.appraisalName = appraisalName;
    }

    public Date getReceivedTaskDate() {
        return receivedTaskDate;
    }

    public void setReceivedTaskDate(Date receivedTaskDate) {
        this.receivedTaskDate = receivedTaskDate;
    }

    public LocationProperty getLocationOfProperty() {
        return locationOfProperty;
    }

    public void setLocationOfProperty(LocationProperty locationOfProperty) {
        this.locationOfProperty = locationOfProperty;
    }

    public Province getProvinceOfProperty() {
        return provinceOfProperty;
    }

    public void setProvinceOfProperty(Province provinceOfProperty) {
        this.provinceOfProperty = provinceOfProperty;
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

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
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

    public List<AppraisalDetailView> getAppraisalDetailViewList() {
        return appraisalDetailViewList;
    }

    public void setAppraisalDetailViewList(List<AppraisalDetailView> appraisalDetailViewList) {
        this.appraisalDetailViewList = appraisalDetailViewList;
    }

    public List<AppraisalContactDetailView> getAppraisalContactDetailViewList() {
        return appraisalContactDetailViewList;
    }

    public void setAppraisalContactDetailViewList(List<AppraisalContactDetailView> appraisalContactDetailViewList) {
        this.appraisalContactDetailViewList = appraisalContactDetailViewList;
    }

    public List<ContactRecordDetailView> getContactRecordDetailViewList() {
        return contactRecordDetailViewList;
    }

    public void setContactRecordDetailViewList(List<ContactRecordDetailView> contactRecordDetailViewList) {
        this.contactRecordDetailViewList = contactRecordDetailViewList;
    }

    public List<CollateralDetailView> getCollateralDetailViewList() {
        return collateralDetailViewList;
    }

    public void setCollateralDetailViewList(List<CollateralDetailView> collateralDetailViewList) {
        this.collateralDetailViewList = collateralDetailViewList;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("zoneLocation", zoneLocation)
                .append("bdmRemark", bdmRemark)
                .append("appraisalType", appraisalType)
                .append("appraisalCompany", appraisalCompany)
                .append("appraisalDivision", appraisalDivision)
                .append("appraisalName", appraisalName)
                .append("receivedTaskDate", receivedTaskDate)
                .append("locationOfProperty", locationOfProperty)
                .append("provinceOfProperty", provinceOfProperty)
                .append("appraisalDate", appraisalDate)
                .append("dueDate", dueDate)
                .append("appointmentDate", appointmentDate)
                .append("appointmentTime", appointmentTime)
                .append("appointmentCusName", appointmentCusName)
                .append("cancelAppointment", cancelAppointment)
                .append("AADAdminRemark", AADAdminRemark)
                .append("appraisalDetailViewList", appraisalDetailViewList)
                .append("appraisalContactDetailViewList", appraisalContactDetailViewList)
                .append("contactRecordDetailViewList", contactRecordDetailViewList)
                .append("collateralDetailViewList", collateralDetailViewList)
                .append("workCase", workCase)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}