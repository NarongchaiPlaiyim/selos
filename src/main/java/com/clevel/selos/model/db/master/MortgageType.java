package com.clevel.selos.model.db.master;

import com.clevel.selos.model.MortgageCategory;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mst_mortgage_type")
public class MortgageType implements Serializable {
    private static final long serialVersionUID = -1706683093964045342L;

	@Id
    @Column(name = "id")
    private int id;

    @Column(name = "mortgage_type", length = 255)
    private String mortgage;

    @Column(name = "mortgage_category", columnDefinition = "int default 0", length = 1)
    private MortgageCategory mortgageCategory;

    @Column(name = "mortgage_fee_flag", columnDefinition = "int default 0", length = 1)
    private boolean mortgageFeeFlag;

    @Column(name = "active")
    private int active;
    
    @Column(name="mortgage_flag",columnDefinition="int default 0")
    private boolean mortgageFlag;
    
    @Column(name="pledge_flag",columnDefinition="int default 0")
    private boolean pledgeFlag;
    
    @Column(name="guarantor_flag",columnDefinition="int default 0")
    private boolean guarantorFlag;
    
    @Column(name="tcg_flag",columnDefinition="int default 0")
    private boolean tcgFlag;
    
    @Column(name="referred_flag",columnDefinition="int default 0")
    private boolean referredFlag;

    public MortgageType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }

    public MortgageCategory getMortgageCategory() {
        return mortgageCategory;
    }

    public void setMortgageCategory(MortgageCategory mortgageCategory) {
        this.mortgageCategory = mortgageCategory;
    }

    public boolean isMortgageFeeFlag() {
        return mortgageFeeFlag;
    }

    public void setMortgageFeeFlag(boolean mortgageFeeFlag) {
        this.mortgageFeeFlag = mortgageFeeFlag;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
    
    public boolean isMortgageFlag() {
		return mortgageFlag;
	}

	public void setMortgageFlag(boolean mortgageFlag) {
		this.mortgageFlag = mortgageFlag;
	}

	public boolean isPledgeFlag() {
		return pledgeFlag;
	}

	public void setPledgeFlag(boolean pledgeFlag) {
		this.pledgeFlag = pledgeFlag;
	}

	public boolean isGuarantorFlag() {
		return guarantorFlag;
	}

	public void setGuarantorFlag(boolean guarantorFlag) {
		this.guarantorFlag = guarantorFlag;
	}

	public boolean isTcgFlag() {
		return tcgFlag;
	}

	public void setTcgFlag(boolean tcgFlag) {
		this.tcgFlag = tcgFlag;
	}

	public boolean isReferredFlag() {
		return referredFlag;
	}

	public void setReferredFlag(boolean referredFlag) {
		this.referredFlag = referredFlag;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("mortgage", mortgage)
                .append("mortgageCategory", mortgageCategory)
                .append("mortgageFeeFlag", mortgageFeeFlag)
                .append("active", active)
                .append("mortgageFlag", mortgageFlag)
                .append("pledgeFlag", pledgeFlag)
                .append("guarantorFlag", guarantorFlag)
                .append("tcgFlag", tcgFlag)
                .append("referredFlag", referredFlag)
                .toString();
    }
}
