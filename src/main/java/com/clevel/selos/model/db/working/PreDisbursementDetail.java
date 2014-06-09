package com.clevel.selos.model.db.working;


import com.clevel.selos.model.db.master.PreDisbursementData;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_pre_disbursement_detail")
public class PreDisbursementDetail implements Serializable {

    private static final long serialVersionUID = 5620522401365835268L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_PREDISBURSE_DETAIL_ID", sequenceName = "SEQ_WRK_PREDISBURSE_DETAIL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PREDISBURSE_DETAIL_ID")
    @Column(name = "id", nullable = false)
    private long id;

	@Column(name = "value")
	private int value;
		
	@Column(name = "submission_date")
    private Date submission_date;
	
	@ManyToOne
    @JoinColumn(name = "predisbursement_data_id")
    private PreDisbursementData preDisbursementData;
	
	@ManyToOne
    @JoinColumn(name = "predisbursement_id")
    private PreDisbursement preDisbursement;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("value", value)
                .append("preDisbursementData", preDisbursementData)
                .append("preDisbursement", preDisbursement)
                .toString();
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public PreDisbursementData getPreDisbursementData() {
		return preDisbursementData;
	}

	public void setPreDisbursementData(PreDisbursementData preDisbursementData) {
		this.preDisbursementData = preDisbursementData;
	}

	public PreDisbursement getPreDisbursement() {
		return preDisbursement;
	}

	public void setPreDisbursement(PreDisbursement preDisbursement) {
		this.preDisbursement = preDisbursement;
	}

	public Date getSubmission_date() {
		return submission_date;
	}

	public void setSubmission_date(Date submission_date) {
		this.submission_date = submission_date;
	}

    
}
