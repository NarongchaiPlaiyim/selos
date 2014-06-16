package com.clevel.selos.model.db.working;

import com.clevel.selos.model.MortgageConfirmedType;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_mortgage_summary")
public class MortgageSummary implements Serializable {
    private static final long serialVersionUID = -7019051594364765614L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_MORTGAGE_SUMMARY_ID", sequenceName = "SEQ_WRK_MORTGAGE_SUMMARY_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORTGAGE_SUMMARY_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;
    
    @Column(name="confirmed",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private MortgageConfirmedType confirmed;
    
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
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
	
	public MortgageConfirmedType getConfirmed() {
		return confirmed;
	}
	
	public void setConfirmed(MortgageConfirmedType confirmed) {
		this.confirmed = confirmed;
	}
    
}
