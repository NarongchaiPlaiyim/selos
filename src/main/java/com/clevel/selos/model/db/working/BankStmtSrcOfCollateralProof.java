package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_bankstatement_socp")
public class BankStmtSrcOfCollateralProof implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_BANKSTMT_SOCP_ID", sequenceName = "SEQ_WRK_BANKSTMT_SOCP_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_BANKSTMT_SOCP_ID")
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_of_max_balance")
    private Date dateOfMaxBalance;

    @Column(name = "max_balance")
    private BigDecimal maxBalance;

    @ManyToOne
    @JoinColumn(name = "bank_stmt_id")
    private BankStatement bankStatement;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateOfMaxBalance() {
        return dateOfMaxBalance;
    }

    public void setDateOfMaxBalance(Date dateOfMaxBalance) {
        this.dateOfMaxBalance = dateOfMaxBalance;
    }

    public BigDecimal getMaxBalance() {
        return maxBalance;
    }

    public void setMaxBalance(BigDecimal maxBalance) {
        this.maxBalance = maxBalance;
    }

    public BankStatement getBankStatement() {
        return bankStatement;
    }

    public void setBankStatement(BankStatement bankStatement) {
        this.bankStatement = bankStatement;
    }
}
