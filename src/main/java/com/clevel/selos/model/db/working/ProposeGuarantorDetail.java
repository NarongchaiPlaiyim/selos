package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_propose_guarantor_detail")
public class ProposeGuarantorDetail  implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_PROP_GRT_DET_ID", sequenceName = "SEQ_WRK_PROP_GRT_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PROP_GRT_DET_ID")
    private long id;

    @Column(name = "no")
    private int no;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer  guarantorName;

    @Column(name = "tcg_lg_no")
    private String tcgLgNo;

    @Column(name = "total_limit_guarantee_amount")
    private BigDecimal totalLimitGuaranteeAmount;

    @ManyToOne
    @JoinColumn(name = "credit_facility_propose_id")
    private CreditFacilityPropose creditFacilityPropose;

    @OneToMany(mappedBy = "proposeGuarantorDetail", cascade = CascadeType.ALL)
    private List<CreditTypeDetail> creditTypeDetailList;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Customer getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(Customer guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getTcgLgNo() {
        return tcgLgNo;
    }

    public void setTcgLgNo(String tcgLgNo) {
        this.tcgLgNo = tcgLgNo;
    }

    public BigDecimal getTotalLimitGuaranteeAmount() {
        return totalLimitGuaranteeAmount;
    }

    public void setTotalLimitGuaranteeAmount(BigDecimal totalLimitGuaranteeAmount) {
        this.totalLimitGuaranteeAmount = totalLimitGuaranteeAmount;
    }

    public CreditFacilityPropose getCreditFacilityPropose() {
        return creditFacilityPropose;
    }

    public void setCreditFacilityPropose(CreditFacilityPropose creditFacilityPropose) {
        this.creditFacilityPropose = creditFacilityPropose;
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

    public List<CreditTypeDetail> getCreditTypeDetailList() {
        return creditTypeDetailList;
    }

    public void setCreditTypeDetailList(List<CreditTypeDetail> creditTypeDetailList) {
        this.creditTypeDetailList = creditTypeDetailList;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("guarantorName", guarantorName)
                .append("tcgLgNo", tcgLgNo)
                .append("totalLimitGuaranteeAmount", totalLimitGuaranteeAmount)
                .append("creditFacilityPropose", creditFacilityPropose)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}