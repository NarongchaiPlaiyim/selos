package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name="wrk_appraisal_detail")
public class AppraisalPurpose {
    @Id
    @SequenceGenerator(name="SEQ_WRK_APPRAISAL_DETAIL_ID", sequenceName="SEQ_WRK_APPRAISAL_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_APPRAISAL_DETAIL_ID")
    private long id;
    @Column(name="no")
    private int no;
    @Column(name="title_deed")
    private String titleDeed;
    @Column(name="purpose_review_Appraisal")
    private int purposeReviewAppraisal;
    @Column(name="purpose_new_Appraisal")
    private int purposeNewAppraisal;
    @Column(name="purpose_review_building")
    private int purposeReviewBuilding;
    @Column(name="characteristic")
    private int characteristic;
    @Column(name="number_of_documents")
    private BigDecimal numberOfDocuments;

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

    @ManyToOne
    @JoinColumn(name = "appraisal_id")
    private Appraisal appraisal;

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

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public int getPurposeReviewAppraisal() {
        return purposeReviewAppraisal;
    }

    public void setPurposeReviewAppraisal(int purposeReviewAppraisal) {
        this.purposeReviewAppraisal = purposeReviewAppraisal;
    }

    public int getPurposeNewAppraisal() {
        return purposeNewAppraisal;
    }

    public void setPurposeNewAppraisal(int purposeNewAppraisal) {
        this.purposeNewAppraisal = purposeNewAppraisal;
    }

    public int getPurposeReviewBuilding() {
        return purposeReviewBuilding;
    }

    public void setPurposeReviewBuilding(int purposeReviewBuilding) {
        this.purposeReviewBuilding = purposeReviewBuilding;
    }

    public int getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(int characteristic) {
        this.characteristic = characteristic;
    }

    public BigDecimal getNumberOfDocuments() {
        return numberOfDocuments;
    }

    public void setNumberOfDocuments(BigDecimal numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
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

    public Appraisal getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(Appraisal appraisal) {
        this.appraisal = appraisal;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("titleDeed", titleDeed)
                .append("purposeReviewAppraisal", purposeReviewAppraisal)
                .append("purposeNewAppraisal", purposeNewAppraisal)
                .append("purposeReviewBuilding", purposeReviewBuilding)
                .append("characteristic", characteristic)
                .append("numberOfDocuments", numberOfDocuments)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("appraisal", appraisal)
                .toString();
    }
}
