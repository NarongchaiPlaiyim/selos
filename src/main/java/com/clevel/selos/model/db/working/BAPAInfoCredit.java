package com.clevel.selos.model.db.working;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_bapa_credit")
public class BAPAInfoCredit {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_BAPA_CREDIT_REQ", sequenceName = "SEQ_WRK_BAPA_CREDIT_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BAPA_CREDIT_REQ")
    private long id;

    @Column(name = "bapa_type")
    private int bapaType;

    @Column(name = "pay_by_customer")
    private int payByCustomer;

    @Column(name = "limit")
    private BigDecimal limit;

    @Column(name = "premium_amount")
    private BigDecimal premiumAmount;

    @Column(name = "expense_amount")
    private BigDecimal expenseAmount;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail creditDetail;

    @OneToOne
    @JoinColumn(name = "bapa_info_id")
    private BAPAInfo bapaInfo;


}
