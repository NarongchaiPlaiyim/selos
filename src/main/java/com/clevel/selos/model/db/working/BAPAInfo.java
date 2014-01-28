package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BAPaymentMethod;
import com.clevel.selos.model.db.master.InsuranceCompany;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "wrk_bapa_info")
public class BAPAInfo {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_BAPA_INFO_REQ", sequenceName = "SEQ_WRK_BAPA_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BAPA_INFO_REQ")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "apply_ba")
    private int applyBA;

    @Column(name = "ba_payment_method")
    private int baPaymentMethod;

    @OneToMany(mappedBy = "bapaInfo")
    private List<BAPAInfoCustomer> bapaInfoCustomerList;

    @OneToMany(mappedBy = "bapaInfo")
    private List<BAPAInfoCredit> bapaInfoCreditList;

    @OneToOne
    @JoinColumn(name = "insurance_company_id")
    private InsuranceCompany insuranceCompany;

    @Column(name = "payto_insurance_company")
    private int payToInsuranceCompany;

    @Column(name = "total_limit")
    private BigDecimal totalLimit;

    @Column(name = "total_premium")
    private BigDecimal totalPremium;

    @Column(name = "total_expense")
    private BigDecimal totalExpense;

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

    public int getApplyBA() {
        return applyBA;
    }

    public void setApplyBA(int applyBA) {
        this.applyBA = applyBA;
    }

    public int getBaPaymentMethod() {
        return baPaymentMethod;
    }

    public void setBaPaymentMethod(int baPaymentMethod) {
        this.baPaymentMethod = baPaymentMethod;
    }

    public List<BAPAInfoCustomer> getBapaInfoCustomerList() {
        return bapaInfoCustomerList;
    }

    public void setBapaInfoCustomerList(List<BAPAInfoCustomer> bapaInfoCustomerList) {
        this.bapaInfoCustomerList = bapaInfoCustomerList;
    }

    public List<BAPAInfoCredit> getBapaInfoCreditList() {
        return bapaInfoCreditList;
    }

    public void setBapaInfoCreditList(List<BAPAInfoCredit> bapaInfoCreditList) {
        this.bapaInfoCreditList = bapaInfoCreditList;
    }

    public InsuranceCompany getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public int getPayToInsuranceCompany() {
        return payToInsuranceCompany;
    }

    public void setPayToInsuranceCompany(int payToInsuranceCompany) {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("applyBA", applyBA)
                .append("baPaymentMethod", baPaymentMethod)
                .append("bapaInfoCustomerList", bapaInfoCustomerList)
                .append("bapaInfoCreditList", bapaInfoCreditList)
                .append("insuranceCompany", insuranceCompany)
                .append("payToInsuranceCompany", payToInsuranceCompany)
                .append("totalLimit", totalLimit)
                .append("totalPremium", totalPremium)
                .append("totalExpense", totalExpense)
                .toString();
    }
}
