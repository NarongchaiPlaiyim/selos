package com.clevel.selos.model.view;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DisbursementMcDetailView implements Serializable{

	private long id;
    private int issuedBy;
    private Date issuedDate;
    private int payeeName;
    private String payeeSubname;
    private int crossType;
    private List<DisbursementCreditTypeView> disbursementCreditTypeView;
    private BigDecimal totalAmount;
    
    public int getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(int issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public int getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(int payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeSubname() {
        return payeeSubname;
    }

    public void setPayeeSubname(String payeeSubname) {
        this.payeeSubname = payeeSubname;
    }

    public int getCrossType() {
        return crossType;
    }

    public void setCrossType(int crossType) {
        this.crossType = crossType;
    }

    

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("issuedBy", issuedBy)
                .append("issuedDate", issuedDate)
                .append("payeeName", payeeName)
                .append("payeeSubname",payeeSubname)
                .append("crossType", crossType)
                .append("creditType", disbursementCreditTypeView)
                .append("totalAmount",totalAmount)
                .toString();
    }

	public List<DisbursementCreditTypeView> getDisbursementCreditTypeView() {
		return disbursementCreditTypeView;
	}

	public void setDisbursementCreditTypeView(List<DisbursementCreditTypeView> disbursementCreditTypeView) {
		this.disbursementCreditTypeView = disbursementCreditTypeView;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

}
