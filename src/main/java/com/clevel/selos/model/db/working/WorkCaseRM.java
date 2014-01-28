package com.clevel.selos.model.db.working;

import javax.persistence.*;

@Entity
@Table(name = "wrk_case_rm")
public class WorkCaseRM extends AbstractWorkCase {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_RM_ID", sequenceName = "SEQ_WRK_CASE_RM_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_RM_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

}
