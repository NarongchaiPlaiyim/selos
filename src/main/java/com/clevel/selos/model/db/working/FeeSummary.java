package com.clevel.selos.model.db.working;

import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_fee_summary")
public class FeeSummary implements Serializable {
    private static final long serialVersionUID = -7513378088611151805L;

	@Id
    @SequenceGenerator(name="SEQ_WRK_FEE_SUM_ID", sequenceName="SEQ_WRK_FEE_SUM_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_FEE_SUM_ID")
    private long id;

    @Column(name="collect_completed",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private RadioValue collectCompleted;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RadioValue getCollectCompleted() {
		return collectCompleted;
	}

	public void setCollectCompleted(RadioValue collectCompleted) {
		this.collectCompleted = collectCompleted;
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
