package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Entity
@Table(name ="wrk_appraisal")
public class Appraisal implements Serializable {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_APPRAISAL_REQ", sequenceName = "SEQ_WRK_APPRAISAL_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_APPRAISAL_REQ")
    private long id;

    @Column(name = "zone_location")
    private String zoneLocation;

    @Column(name = "bdm_remark")
    private String bdmRemark;

    @Column(name = "appraisal_type")
    private int appraisalType;

    @OneToOne
    @JoinColumn(name="appraisal_company_id")
    private AppraisalCompany appraisalCompany;

    @OneToOne
    @JoinColumn(name = "appraisal_division_id")
    private AppraisalDivision appraisalDivision;

    @Column(name = "appraisal_name")
    private String appraisalName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "received_task_date")
    private Date receivedTaskDate;

    @OneToOne
    @JoinColumn(name = "location_property_id")
    private LocationProperty locationOfProperty;

    @OneToOne
    @JoinColumn(name = "province_id")
    private Province provinceOfProperty;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appraisal_date")
    private Date appraisalDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "due_date")
    private Date dueDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appointment_date")
    private Date appointmentDate;

    @Column(name = "appointment_time")
    private String appointmentTime;

    @Column(name = "appointment_cus_name")
    private String appointmentCusName;

    @Column(name = "cancel_appointment")
    private String  cancelAppointment;

    @Column(name = "appointment_remark")
    private String  appointmentRemark;

    @Column(name = "AAD_admin_remark")
    private String AADAdminRemark;

    @OneToMany(mappedBy = "appraisal")
    private List<AppraisalPurpose> appraisalDetailList;

    @OneToMany(mappedBy = "appraisal")
    private List<AppraisalContactDetail> appraisalContactDetailList;

    @OneToOne
    @JoinColumn(name="workcase_appraisal_id")
    private WorkCase workCase;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    public Appraisal(){

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

    public String getAADAdminRemark() {
        return AADAdminRemark;
    }

    public void setAADAdminRemark(String AADAdminRemark) {
        this.AADAdminRemark = AADAdminRemark;
    }

    public List<AppraisalPurpose> getAppraisalDetailList() {
        return appraisalDetailList;
    }

    public void setAppraisalDetailList(List<AppraisalPurpose> appraisalDetailList) {
        this.appraisalDetailList = appraisalDetailList;
    }

    public List<AppraisalContactDetail> getAppraisalContactDetailList() {
        return appraisalContactDetailList;
    }

    public void setAppraisalContactDetailList(List<AppraisalContactDetail> appraisalContactDetailList) {
        this.appraisalContactDetailList = appraisalContactDetailList;
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
                .append("appraisalDetailList", appraisalDetailList)
                .append("appraisalContactDetailList", appraisalContactDetailList)
                .append("workCase", workCase)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
