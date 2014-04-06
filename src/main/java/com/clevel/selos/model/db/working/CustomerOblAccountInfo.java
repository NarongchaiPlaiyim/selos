package com.clevel.selos.model.db.working;

import com.clevel.selos.model.TMBTDRFlag;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="wrk_customer_obl_account_info")
public class CustomerOblAccountInfo implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_CUS_OBL_ACCOUNT_ID", sequenceName="SEQ_WRK_CUS_OBL_ACCOUNT_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_CUS_OBL_ACCOUNT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "account_active_flag", length = 1, columnDefinition = "int default 0")
    private boolean accountActiveFlag;

    @Column(name = "data_source", length = 2)
    private String dataSource;

    @Column(name = "account_ref", length = 17)
    private String accountRef;

    @Column(name = "cus_rel_account", length = 3)
    private String cusRelAccount;

    @Column(name = "tdr_flag", length = 1, columnDefinition = "int default 0")
    private TMBTDRFlag tdrFlag;

    @Column(name = "num_month_int_past_due", length = 5, precision = 0)
    private BigDecimal numMonthIntPastDue;

    @Column(name = "num_month_int_past_due_tdr_acc", length = 5, precision = 0)
    private BigDecimal numMonthIntPastDueTDRAcc;

    @Column(name = "tmb_del_pri_day", length = 5, precision = 0)
    private BigDecimal tmbDelPriDay;

    @Column(name = "tmb_del_int_day", length = 5, precision = 0)
    private BigDecimal tmbDelIntDay;

    @Column(name = "card_block_code", length = 1)
    private String cardBlockCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isAccountActiveFlag() {
        return accountActiveFlag;
    }

    public void setAccountActiveFlag(boolean accountActiveFlag) {
        this.accountActiveFlag = accountActiveFlag;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public String getCusRelAccount() {
        return cusRelAccount;
    }

    public void setCusRelAccount(String cusRelAccount) {
        this.cusRelAccount = cusRelAccount;
    }

    public TMBTDRFlag getTdrFlag() {
        return tdrFlag;
    }

    public void setTdrFlag(TMBTDRFlag tdrFlag) {
        this.tdrFlag = tdrFlag;
    }

    public BigDecimal getNumMonthIntPastDue() {
        return numMonthIntPastDue;
    }

    public void setNumMonthIntPastDue(BigDecimal numMonthIntPastDue) {
        this.numMonthIntPastDue = numMonthIntPastDue;
    }

    public BigDecimal getNumMonthIntPastDueTDRAcc() {
        return numMonthIntPastDueTDRAcc;
    }

    public void setNumMonthIntPastDueTDRAcc(BigDecimal numMonthIntPastDueTDRAcc) {
        this.numMonthIntPastDueTDRAcc = numMonthIntPastDueTDRAcc;
    }

    public BigDecimal getTmbDelPriDay() {
        return tmbDelPriDay;
    }

    public void setTmbDelPriDay(BigDecimal tmbDelPriDay) {
        this.tmbDelPriDay = tmbDelPriDay;
    }

    public BigDecimal getTmbDelIntDay() {
        return tmbDelIntDay;
    }

    public void setTmbDelIntDay(BigDecimal tmbDelIntDay) {
        this.tmbDelIntDay = tmbDelIntDay;
    }

    public String getCardBlockCode() {
        return cardBlockCode;
    }

    public void setCardBlockCode(String cardBlockCode) {
        this.cardBlockCode = cardBlockCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customer", customer)
                .append("accountActiveFlag", accountActiveFlag)
                .append("dataSource", dataSource)
                .append("accountRef", accountRef)
                .append("cusRelAccount", cusRelAccount)
                .append("tdrFlag", tdrFlag)
                .append("numMonthIntPastDue", numMonthIntPastDue)
                .append("numMonthIntPastDueTDRAcc", numMonthIntPastDueTDRAcc)
                .append("tmbDelPriDay", tmbDelPriDay)
                .append("tmbDelIntDay", tmbDelIntDay)
                .append("cardBlockCode", cardBlockCode)
                .toString();
    }
}
