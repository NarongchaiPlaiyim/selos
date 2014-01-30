package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BankAccountProduct;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.master.BankBranch;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "wrk_account_info_detail")
public class AccountInfoDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_ACCOUNT_INFO_DETAIL_ID", sequenceName = "SEQ_WRK_ACCOUNT_INFO_DETAIL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_ACCOUNT_INFO_DETAIL_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @ManyToOne
    @JoinColumn(name = "account_info_id")
    private AccountInfo accountInfo;

    @Column(name = "request_account_type")
    private int requestAccountType;

    @Column(name = "account_number")
    private String accountNumber;

    @OneToOne
    @JoinColumn(name = "branch_id")
    private BankBranch branch;

    @OneToOne
    @JoinColumn(name = "account_type_id")
    private BankAccountType accountType;

    @OneToOne
    @JoinColumn(name = "account_product_id")
    private BankAccountProduct productType;

    @Column(name = "term")
    private String term;

    @OneToMany(mappedBy = "accountInfoDetailAccountName")
    private List<AccountInfoDetailAccountName> accountNameList;

    @OneToMany(mappedBy = "accountInfoDetailPurpose")
    private List<AccountInfoDetailPurpose> purposeList;

    @Column(name = "open_account")
    private int openAccount;

    @OneToMany(mappedBy = "accountInfoDetailCreditType")
    private List<AccountInfoDetailCreditType> creditTypeList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public int getRequestAccountType() {
        return requestAccountType;
    }

    public void setRequestAccountType(int requestAccountType) {
        this.requestAccountType = requestAccountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BankBranch getBranch() {
        return branch;
    }

    public void setBranch(BankBranch branch) {
        this.branch = branch;
    }

    public BankAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(BankAccountType accountType) {
        this.accountType = accountType;
    }

    public BankAccountProduct getProductType() {
        return productType;
    }

    public void setProductType(BankAccountProduct productType) {
        this.productType = productType;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<AccountInfoDetailAccountName> getAccountNameList() {
        return accountNameList;
    }

    public void setAccountNameList(List<AccountInfoDetailAccountName> accountNameList) {
        this.accountNameList = accountNameList;
    }

    public List<AccountInfoDetailPurpose> getPurposeList() {
        return purposeList;
    }

    public void setPurposeList(List<AccountInfoDetailPurpose> purposeList) {
        this.purposeList = purposeList;
    }

    public List<AccountInfoDetailCreditType> getCreditTypeList() {
        return creditTypeList;
    }

    public int getOpenAccount() {
        return openAccount;
    }

    public void setOpenAccount(int openAccount) {
        this.openAccount = openAccount;
    }

    public void setCreditTypeList(List<AccountInfoDetailCreditType> creditTypeList) {
        this.creditTypeList = creditTypeList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("accountInfo", accountInfo)
                .append("requestAccountType", requestAccountType)
                .append("accountNumber", accountNumber)
                .append("branch", branch)
                .append("accountType", accountType)
                .append("productType", productType)
                .append("term", term)
                .append("accountNameList", accountNameList)
                .append("purposeList", purposeList)
                .append("openAccount", openAccount)
                .append("creditTypeList", creditTypeList)
                .toString();
    }
}
