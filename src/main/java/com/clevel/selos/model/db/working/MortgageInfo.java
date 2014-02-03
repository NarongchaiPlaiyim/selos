package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.MortgageLandOffice;
import com.clevel.selos.model.db.master.MortgageOSCompany;
import com.clevel.selos.model.db.master.MortgageType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_mortgage_info")
public class MortgageInfo {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MORTGAGE_INFO_REQ", sequenceName = "SEQ_WRK_MORTGAGE_INFO_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MORTGAGE_INFO_REQ")
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mortgage_signing_date")
    private Date mortgageSigningDate;

    @OneToOne
    @JoinColumn(name = "mortgage_os_company_id")
    private MortgageOSCompany mortgageOSCompany;

    @OneToOne
    @JoinColumn(name = "mortgage_land_office_id")
    private MortgageLandOffice mortgageLandOffice;

    @OneToOne
    @JoinColumn(name = "mortgage_type_id")
    private MortgageType mortgageType;

    @Column(name = "mortgage_order")
    private int mortgageOrder;

    @OneToMany(mappedBy = "mortgageInfo")
    private List<MortgageInfoCredit> mortgageInfoCreditList;

    @OneToMany(mappedBy = "mortgageInfo")
    private List<MortgageInfoCollSub> mortgageInfoCollSubList;

    @OneToMany(mappedBy = "mortgageInfo")
    private List<MortgageInfoCollOwner> mortgageInfoCollOwnerList;

    @Column(name = "attorney_required")
    private int attorneyRequired;

    @Column(name = "attorney_relation")
    private int attorneyRelation;

    @OneToOne
    @JoinColumn(name = "poa_customer_id", nullable = true)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "customer_attorney_id", nullable = true)
    private CustomerAttorney customerAttorney;


}
