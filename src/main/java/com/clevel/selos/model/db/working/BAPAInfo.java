package com.clevel.selos.model.db.working;

import com.clevel.selos.model.BAPaymentMethodValue;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.InsuranceCompany;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_bapa_info")
public class BAPAInfo implements Serializable {
    private static final long serialVersionUID = -5625384763548621367L;

	@Id
    @SequenceGenerator(name = "SEQ_WRK_BAPA_INFO_REQ", sequenceName = "SEQ_WRK_BAPA_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BAPA_INFO_REQ")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name="apply_ba",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private RadioValue applyBA;

    @Column(name = "ba_payment_method")
    private BAPaymentMethodValue baPaymentMethod;

    @ManyToOne
    @JoinColumn(name = "insurance_company_id")
    private InsuranceCompany insuranceCompany;

    @Column(name = "payto_insurance_company",columnDefinition="int default 0")
    @Enumerated(EnumType.ORDINAL)
    private RadioValue payToInsuranceCompany;
    
    @Column(name = "total_limit")
    private BigDecimal totalLimit;

    @Column(name = "total_premium")
    private BigDecimal totalPremium;

    @Column(name = "total_expense")
    private BigDecimal totalExpense;
    

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

	public RadioValue getApplyBA() {
		return applyBA;
	}
	public void setApplyBA(RadioValue applyBA) {
		this.applyBA = applyBA;
	}

    public BAPaymentMethodValue getBaPaymentMethod() {
		return baPaymentMethod;
	}
    public void setBaPaymentMethod(BAPaymentMethodValue baPaymentMethod) {
		this.baPaymentMethod = baPaymentMethod;
	}

    public InsuranceCompany getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public RadioValue getPayToInsuranceCompany() {
		return payToInsuranceCompany;
	}
    
    public void setPayToInsuranceCompany(RadioValue payToInsuranceCompany) {
		this.payToInsuranceCompany = payToInsuranceCompany;
	}

    public BigDecimal getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(BigDecimal totalLimit) {
        this.totalLimit = totalLimit;
    }

    public BigDecimal getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(BigDecimal totalPremium) {
        this.totalPremium = totalPremium;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
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
                .append("workCase", workCase)
                .append("applyBA", applyBA)
                .append("baPaymentMethod", baPaymentMethod)
                .append("insuranceCompany", insuranceCompany)
                .append("payToInsuranceCompany", payToInsuranceCompany)
                .append("totalLimit", totalLimit)
                .append("totalPremium", totalPremium)
                .append("totalExpense", totalExpense)
                .append("createDate", createDate)
                .append("createBy", createBy)
                .append("modifyDate", modifyDate)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
