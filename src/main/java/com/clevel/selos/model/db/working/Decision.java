package com.clevel.selos.model.db.working;


import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_decision")
public class Decision implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_DECISION_ID", sequenceName = "SEQ_WRK_DECISION_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_DECISION_ID")
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "save_flag")
    private int saveFlag;

    @Column(name = "tot_appr_credit")
    private BigDecimal totalApproveCredit;

    @Column(name = "tot_appr_commercial")
    private BigDecimal totalApproveCommercial;

    @Column(name = "tot_appr_com_and_obod")
    private BigDecimal totalApproveComAndOBOD;

    @Column(name = "tot_appr_exposure")
    private BigDecimal totalApproveExposure;

    @Column(name = "tot_appr_od_limit")
    private BigDecimal totalApprovedODLimit;

    @Column(name = "tot_appr_num_new_od")
    private BigDecimal totalApproveNumOfNewOD;

    @Column(name = "tot_appr_num_propose_crd_fac")
    private BigDecimal totalApproveNumProposeCreditFac;

    @Column(name = "tot_appr_num_contigent_propose")
    private BigDecimal totalApproveNumContingentPropose;

    @Column(name = "tot_appr_num_core_asset")
    private BigDecimal totalApproveNumOfCoreAsset;

    @Column(name = "tot_appr_num_non_core_asset")
    private BigDecimal totalApproveNumOfNonCoreAsset;

    @Column(name = "tot_appr_guarantee_amt")
    private BigDecimal totalApproveGuaranteeAmt;

    @Column(name = "tot_appr_tcg_guarantee_amt")
    private BigDecimal totalApproveTCGGuaranteeAmt;

    @Column(name = "tot_appr_indi_guarantee_amt")
    private BigDecimal totalApproveIndiGuaranteeAmt;

    @Column(name = "tot_appr_juri_guarantee_amt")
    private BigDecimal totalApproveJuriGuaranteeAmt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
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

    public int getSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(int saveFlag) {
        this.saveFlag = saveFlag;
    }

    public BigDecimal getTotalApproveCredit() {
        return totalApproveCredit;
    }

    public void setTotalApproveCredit(BigDecimal totalApproveCredit) {
        this.totalApproveCredit = totalApproveCredit;
    }

    public BigDecimal getTotalApproveCommercial() {
        return totalApproveCommercial;
    }

    public void setTotalApproveCommercial(BigDecimal totalApproveCommercial) {
        this.totalApproveCommercial = totalApproveCommercial;
    }

    public BigDecimal getTotalApproveComAndOBOD() {
        return totalApproveComAndOBOD;
    }

    public void setTotalApproveComAndOBOD(BigDecimal totalApproveComAndOBOD) {
        this.totalApproveComAndOBOD = totalApproveComAndOBOD;
    }

    public BigDecimal getTotalApproveExposure() {
        return totalApproveExposure;
    }

    public void setTotalApproveExposure(BigDecimal totalApproveExposure) {
        this.totalApproveExposure = totalApproveExposure;
    }

    public BigDecimal getTotalApprovedODLimit() {
        return totalApprovedODLimit;
    }

    public void setTotalApprovedODLimit(BigDecimal totalApprovedODLimit) {
        this.totalApprovedODLimit = totalApprovedODLimit;
    }

    public BigDecimal getTotalApproveNumOfNewOD() {
        return totalApproveNumOfNewOD;
    }

    public void setTotalApproveNumOfNewOD(BigDecimal totalApproveNumOfNewOD) {
        this.totalApproveNumOfNewOD = totalApproveNumOfNewOD;
    }

    public BigDecimal getTotalApproveNumProposeCreditFac() {
        return totalApproveNumProposeCreditFac;
    }

    public void setTotalApproveNumProposeCreditFac(BigDecimal totalApproveNumProposeCreditFac) {
        this.totalApproveNumProposeCreditFac = totalApproveNumProposeCreditFac;
    }

    public BigDecimal getTotalApproveNumContingentPropose() {
        return totalApproveNumContingentPropose;
    }

    public void setTotalApproveNumContingentPropose(BigDecimal totalApproveNumContingentPropose) {
        this.totalApproveNumContingentPropose = totalApproveNumContingentPropose;
    }

    public BigDecimal getTotalApproveNumOfCoreAsset() {
        return totalApproveNumOfCoreAsset;
    }

    public void setTotalApproveNumOfCoreAsset(BigDecimal totalApproveNumOfCoreAsset) {
        this.totalApproveNumOfCoreAsset = totalApproveNumOfCoreAsset;
    }

    public BigDecimal getTotalApproveNumOfNonCoreAsset() {
        return totalApproveNumOfNonCoreAsset;
    }

    public void setTotalApproveNumOfNonCoreAsset(BigDecimal totalApproveNumOfNonCoreAsset) {
        this.totalApproveNumOfNonCoreAsset = totalApproveNumOfNonCoreAsset;
    }

    public BigDecimal getTotalApproveGuaranteeAmt() {
        return totalApproveGuaranteeAmt;
    }

    public void setTotalApproveGuaranteeAmt(BigDecimal totalApproveGuaranteeAmt) {
        this.totalApproveGuaranteeAmt = totalApproveGuaranteeAmt;
    }

    public BigDecimal getTotalApproveTCGGuaranteeAmt() {
        return totalApproveTCGGuaranteeAmt;
    }

    public void setTotalApproveTCGGuaranteeAmt(BigDecimal totalApproveTCGGuaranteeAmt) {
        this.totalApproveTCGGuaranteeAmt = totalApproveTCGGuaranteeAmt;
    }

    public BigDecimal getTotalApproveIndiGuaranteeAmt() {
        return totalApproveIndiGuaranteeAmt;
    }

    public void setTotalApproveIndiGuaranteeAmt(BigDecimal totalApproveIndiGuaranteeAmt) {
        this.totalApproveIndiGuaranteeAmt = totalApproveIndiGuaranteeAmt;
    }

    public BigDecimal getTotalApproveJuriGuaranteeAmt() {
        return totalApproveJuriGuaranteeAmt;
    }

    public void setTotalApproveJuriGuaranteeAmt(BigDecimal totalApproveJuriGuaranteeAmt) {
        this.totalApproveJuriGuaranteeAmt = totalApproveJuriGuaranteeAmt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("createDate", createDate)
                .append("createBy", createBy)
                .append("modifyDate", modifyDate)
                .append("modifyBy", modifyBy)
                .append("workCase", workCase)
                .append("saveFlag", saveFlag)
                .append("totalApproveCredit", totalApproveCredit)
                .append("totalApproveCommercial", totalApproveCommercial)
                .append("totalApproveComAndOBOD", totalApproveComAndOBOD)
                .append("totalApproveExposure", totalApproveExposure)
                .append("totalApprovedODLimit", totalApprovedODLimit)
                .append("totalApproveNumOfNewOD", totalApproveNumOfNewOD)
                .append("totalApproveNumProposeCreditFac", totalApproveNumProposeCreditFac)
                .append("totalApproveNumContingentPropose", totalApproveNumContingentPropose)
                .append("totalApproveNumOfCoreAsset", totalApproveNumOfCoreAsset)
                .append("totalApproveNumOfNonCoreAsset", totalApproveNumOfNonCoreAsset)
                .append("totalApproveGuaranteeAmt", totalApproveGuaranteeAmt)
                .append("totalApproveTCGGuaranteeAmt", totalApproveTCGGuaranteeAmt)
                .append("totalApproveIndiGuaranteeAmt", totalApproveIndiGuaranteeAmt)
                .append("totalApproveJuriGuaranteeAmt", totalApproveJuriGuaranteeAmt)
                .toString();
    }
}
