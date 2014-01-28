package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wrk_disburse_tr")
public class DisbursementTR {

    @Id
    @SequenceGenerator(name = "SEQ_WRK_DISBURSE_TR_REQ", sequenceName = "SEQ_WRK_DISBURSE_TR_REQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_DISBURSE_TR_REQ")
    private long id;

    @ManyToOne
    @JoinColumn(name = "disbursement_id")
    private Disbursement disbursement;

    @OneToOne
    @JoinColumn(name = "open_account_id")
    private OpenAccount openAccount;

    @OneToMany(mappedBy = "disbursementTR")
    private List<DisbursementTRCredit> disbursementTRCreditList;


}
