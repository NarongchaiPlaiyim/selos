package com.clevel.selos.model.db.working;

import javax.persistence.*;

@Entity
@Table(name = "wrk_case_mortgage_confirm")
public class WorkCaseMortgageConfirm extends AbstractWorkCase{
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_MORTGAGE_CON_ID", sequenceName = "SEQ_WRK_CASE_MORTGAGE_CON_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_MORTGAGE_CON_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "wrk_case_id")
    private WorkCase workCase;


}
