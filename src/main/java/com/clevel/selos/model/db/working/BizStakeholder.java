package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 23/9/2556
 * Time: 9:58 น.
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "wrk_biz_stake_holder")
public class BizStakeholder {

    @Id
    @SequenceGenerator(name="SEQ_WRK_BIZ_STAKE_HOLDER_ID", sequenceName="SEQ_WRK_BIZ_STAKE_HOLDER_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BIZ_STAKE_HOLDER_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="biz_info_detail_id")
    private BizInfoDetail bizInfoDetail;

    @Column(name = "stake_holder_type")
    private BigDecimal stakeholderType;

    @Column(name = "no")
    private BigDecimal no;

    @Column(name = "name")
    private String name;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "contact_year")
    private String contactYear;

    @Column(name = "precent_sale_volume")
    private BigDecimal percentSalesVolume;

    @Column(name = "percent_cash")
    private BigDecimal percentCash;

    @Column(name = "precent_credit")
    private BigDecimal percentCredit;

    @Column(name = "credit_term")
    private BigDecimal creditTerm;

    public BizStakeholder() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getStakeholderType() {
        return stakeholderType;
    }

    public void setStakeholderType(BigDecimal stakeholderType) {
        this.stakeholderType = stakeholderType;
    }

    public BigDecimal getNo() {
        return no;
    }

    public void setNo(BigDecimal no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getContactYear() {
        return contactYear;
    }

    public void setContactYear(String contactYear) {
        this.contactYear = contactYear;
    }

    public BigDecimal getPercentSalesVolume() {
        return percentSalesVolume;
    }

    public void setPercentSalesVolume(BigDecimal percentSalesVolume) {
        this.percentSalesVolume = percentSalesVolume;
    }

    public BigDecimal getPercentCash() {
        return percentCash;
    }

    public void setPercentCash(BigDecimal percentCash) {
        this.percentCash = percentCash;
    }

    public BigDecimal getPercentCredit() {
        return percentCredit;
    }

    public void setPercentCredit(BigDecimal percentCredit) {
        this.percentCredit = percentCredit;
    }

    public BigDecimal getCreditTerm() {
        return creditTerm;
    }

    public void setCreditTerm(BigDecimal creditTerm) {
        this.creditTerm = creditTerm;
    }
}
