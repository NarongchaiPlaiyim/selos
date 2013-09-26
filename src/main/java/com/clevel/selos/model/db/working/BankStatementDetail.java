package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_bankstatement_detail")
public class BankStatementDetail implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_BANKSTMT_DETAIL_ID", sequenceName="SEQ_WRK_BANKSTMT_DETAIL_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BANKSTMT_DETAIL_ID")
    private long id;
}
