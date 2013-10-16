package com.clevel.selos.model.db.working;

import javax.persistence.*;

@Entity
@Table(name = "wrk_existing_credit")
public class ExistingCredit {

    @Id
    @SequenceGenerator(name="SEQ_WRK_EXISTING_CREDIT_ID", sequenceName="SEQ_WRK_EXISTING_CREDIT_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_EXISTING_CREDIT_ID")
    private long id;

    @Column(name="account_name")
    private String accountName;

    @Column(name="account_number")
    private String accountNumber;

    @Column(name="product_program")
    private String productProgram;

    @Column(name="project_code")
    private String projectCode;

    @Column(name="product_code")
    private String productCode;

    @ManyToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;

    @ManyToOne
    @JoinColumn(name="workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;
}
