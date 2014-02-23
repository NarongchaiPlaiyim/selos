package com.clevel.selos.model.db.working;


import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_pre_disbursement")
public class PreDisbursement implements Serializable {

    private static final long serialVersionUID = 5620522401365835268L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_PRE_DISBURSEMENT_ID", sequenceName = "SEQ_WRK_PRE_DISBURSEMENT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PRE_DISBURSEMENT_ID")
    @Column(name = "id", nullable = false)
    private long id;

	@Column(name = "remark")
    private String remark;
	
    
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

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;
        
    @OneToMany(mappedBy = "preDisbursement", cascade = CascadeType.ALL)
    private List<PreDisbursementDetail> preDisbursementDetailList;


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
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("workCase", workCase)
                .append("remark", remark)
                .toString();
    }

    public List<PreDisbursementDetail> getPreDisbursementDetailList() {
		return preDisbursementDetailList;
	}

    public void setPreDisbursementDetailList(List<PreDisbursementDetail> preDisbursementDetailList) {
		this.preDisbursementDetailList = preDisbursementDetailList;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


    
}
