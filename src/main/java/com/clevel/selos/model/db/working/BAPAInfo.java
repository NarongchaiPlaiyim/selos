package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BAPaymentMethod;
import com.clevel.selos.model.db.master.InsuranceCompany;

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
}
