package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.OpenAccountProduct;
import com.clevel.selos.model.db.master.OpenAccountType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="wrk_open_account")
public class OpenAccount implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_OPEN_ACC_ID", sequenceName="SEQ_WRK_OPEN_ACC_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_OPEN_ACC_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="basic_info_id")
    private BasicInfo basicInfo;

    @Column(name="account_name")
    private String accountName;

    @OneToOne
    @JoinColumn(name="open_account_type_id")
    private OpenAccountType accountType;

    @OneToOne
    @JoinColumn(name="open_account_product_id")
    private OpenAccountProduct accountProduct;

    @OneToMany(mappedBy="openAccount")
    private List<OpenAccPurpose> openAccPurposeList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public OpenAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(OpenAccountType accountType) {
        this.accountType = accountType;
    }

    public OpenAccountProduct getAccountProduct() {
        return accountProduct;
    }

    public void setAccountProduct(OpenAccountProduct accountProduct) {
        this.accountProduct = accountProduct;
    }

    public List<OpenAccPurpose> getOpenAccPurposeList() {
        return openAccPurposeList;
    }

    public void setOpenAccPurposeList(List<OpenAccPurpose> openAccPurposeList) {
        this.openAccPurposeList = openAccPurposeList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("basicInfo", basicInfo).
                append("accountName", accountName).
                append("accountType", accountType).
                append("accountProduct", accountProduct).
                append("openAccPurposeList", openAccPurposeList).
                toString();
    }
}
