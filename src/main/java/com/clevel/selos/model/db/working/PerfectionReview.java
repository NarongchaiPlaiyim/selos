package com.clevel.selos.model.db.working;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.clevel.selos.model.PerfectReviewStatus;
import com.clevel.selos.model.PerfectReviewType;
import com.clevel.selos.model.db.master.User;

@Entity
@Table(name = "wrk_perfect_review")
public class PerfectionReview implements Serializable{
	private static final long serialVersionUID = 699466219673680739L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_PERFECT_REVIEW", sequenceName = "SEQ_WRK_PERFECT_REVIEW", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PERFECT_REVIEW")
    private long id;
	
	@Column(name="review_type",columnDefinition="int default 0")
	@Enumerated(EnumType.ORDINAL)
	private PerfectReviewType type;
	
	@Column(name="review_status",columnDefinition="int default 0")
	@Enumerated(EnumType.ORDINAL)
	private PerfectReviewStatus status;
	
	@Column(name="submit_date")
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Column(name="completed_date")
	@Temporal(TemporalType.DATE)
	private Date completedDate;
	
	@Column(name = "remark")
	private String remark;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "create_user_id")
	private User createBy;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "modify_user_id")
	private User modifyBy;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="workcase_id")
	private WorkCase workCase;
	
	public PerfectionReview() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PerfectReviewType getType() {
		return type;
	}

	public void setType(PerfectReviewType type) {
		this.type = type;
	}

	public PerfectReviewStatus getStatus() {
		return status;
	}

	public void setStatus(PerfectReviewStatus status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
}
