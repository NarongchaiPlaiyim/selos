package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_exist_credit_summary")
public class ExistingCreditSummary {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_EXISTING_CREDIT_SUM_ID", sequenceName = "SEQ_WRK_EXISTING_CREDIT_SUM_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EXISTING_CREDIT_SUM_ID")
    private long id;

    @Column(name = "total_brw_com_limit")
    private BigDecimal totalBorrowerComLimit;

    @Column(name = "total_brw_ret_limit")
    private BigDecimal totalBorrowerRetailLimit;

    @Column(name = "total_brw_rlos_limit")
    private BigDecimal totalBorrowerAppInRLOSLimit;

    @Column(name = "total_related_com_limit")
    private BigDecimal totalRelatedComLimit;

    @Column(name = "total_related_ret_limit")
    private BigDecimal totalRelatedRetailLimit;

    @Column(name = "total_related_rlos_limit")
    private BigDecimal totalRelatedAppInRLOSLimit;

    @Column(name = "total_group_com_limit")
    private BigDecimal totalGroupCom;

    @Column(name = "total_group_com_obod_limit")
    private BigDecimal totalGroupComOBOD;

    @Column(name = "total_group_ret_limit")
    private BigDecimal totalGroupExposure;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @ManyToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @ManyToOne
    @JoinColumn(name = "workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToMany(mappedBy = "existingCreditSummary", cascade = CascadeType.ALL)
    private List<ExistingCreditDetail> existingCreditDetailList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getTotalBorrowerComLimit() {
        return totalBorrowerComLimit;
    }

    public void setTotalBorrowerComLimit(BigDecimal totalBorrowerComLimit) {
        this.totalBorrowerComLimit = totalBorrowerComLimit;
    }

    public BigDecimal getTotalBorrowerRetailLimit() {
        return totalBorrowerRetailLimit;
    }

    public void setTotalBorrowerRetailLimit(BigDecimal totalBorrowerRetailLimit) {
        this.totalBorrowerRetailLimit = totalBorrowerRetailLimit;
    }

    public BigDecimal getTotalBorrowerAppInRLOSLimit() {
        return totalBorrowerAppInRLOSLimit;
    }

    public void setTotalBorrowerAppInRLOSLimit(BigDecimal totalBorrowerAppInRLOSLimit) {
        this.totalBorrowerAppInRLOSLimit = totalBorrowerAppInRLOSLimit;
    }

    public BigDecimal getTotalRelatedComLimit() {
        return totalRelatedComLimit;
    }

    public void setTotalRelatedComLimit(BigDecimal totalRelatedComLimit) {
        this.totalRelatedComLimit = totalRelatedComLimit;
    }

    public BigDecimal getTotalRelatedRetailLimit() {
        return totalRelatedRetailLimit;
    }

    public void setTotalRelatedRetailLimit(BigDecimal totalRelatedRetailLimit) {
        this.totalRelatedRetailLimit = totalRelatedRetailLimit;
    }

    public BigDecimal getTotalRelatedAppInRLOSLimit() {
        return totalRelatedAppInRLOSLimit;
    }

    public void setTotalRelatedAppInRLOSLimit(BigDecimal totalRelatedAppInRLOSLimit) {
        this.totalRelatedAppInRLOSLimit = totalRelatedAppInRLOSLimit;
    }

    public BigDecimal getTotalGroupCom() {
        return totalGroupCom;
    }

    public void setTotalGroupCom(BigDecimal totalGroupCom) {
        this.totalGroupCom = totalGroupCom;
    }

    public BigDecimal getTotalGroupComOBOD() {
        return totalGroupComOBOD;
    }

    public void setTotalGroupComOBOD(BigDecimal totalGroupComOBOD) {
        this.totalGroupComOBOD = totalGroupComOBOD;
    }

    public BigDecimal getTotalGroupExposure() {
        return totalGroupExposure;
    }

    public void setTotalGroupExposure(BigDecimal totalGroupExposure) {
        this.totalGroupExposure = totalGroupExposure;
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

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public List<ExistingCreditDetail> getExistingCreditDetailList() {
        return existingCreditDetailList;
    }

    public void setExistingCreditDetailList(List<ExistingCreditDetail> existingCreditDetailList) {
        this.existingCreditDetailList = existingCreditDetailList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("totalBorrowerComLimit", totalBorrowerComLimit)
                .append("totalBorrowerRetailLimit", totalBorrowerRetailLimit)
                .append("totalBorrowerAppInRLOSLimit", totalBorrowerAppInRLOSLimit)
                .append("totalRelatedComLimit", totalRelatedComLimit)
                .append("totalRelatedRetailLimit", totalRelatedRetailLimit)
                .append("totalRelatedAppInRLOSLimit", totalRelatedAppInRLOSLimit)
                .append("totalGroupCom", totalGroupCom)
                .append("totalGroupComOBOD", totalGroupComOBOD)
                .append("totalGroupExposure", totalGroupExposure)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("workCase", workCase)
                .append("workCasePrescreen", workCasePrescreen)
                .append("existingCreditDetailList", existingCreditDetailList)
                .toString();
    }
}
