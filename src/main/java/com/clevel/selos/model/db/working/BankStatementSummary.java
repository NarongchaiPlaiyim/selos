package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_bankstatement_summary")
public class BankStatementSummary implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_BANKSTMT_SUM_ID", sequenceName="SEQ_WRK_BANKSTMT_SUM_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BANKSTMT_SUM_ID")
    private long id;


}
