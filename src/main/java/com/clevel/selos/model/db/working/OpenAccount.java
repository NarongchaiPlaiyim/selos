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
@Table(name = "wrk_open_account")
public class OpenAccount implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_OPEN_ACC_ID", sequenceName = "SEQ_WRK_OPEN_ACC_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_OPEN_ACC_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "workcase_id")
    private WorkCase workCase;

    @Column(name = "request_type")
    private int requestType;

    @Column(name = "account_number")
    private String accountNumber;

    @OneToOne
    @JoinColumn(name = "bank_branch_id")
    private BankBranch bankBranch;

    @OneToOne
    @JoinColumn(name = "bank_account_type_id")
    private BankAccountType bankAccountType;

    @OneToOne
    @JoinColumn(name = "open_account_product_id")
    private BankAccountProduct bankAccountProduct;

    @Column(name = "term")
    private String term;

    @OneToMany(mappedBy = "openAccount")
    private List<OpenAccountName> openAccountNameList;

    @OneToMany(mappedBy = "openAccount")
    private List<OpenAccountPurpose> openAccountPurposeList;

    @Column(name = "number_of_dep")
    private int numberOfDep;

    @OneToMany(mappedBy = "openAccount")
    private List<OpenAccountDeposit> openAccountDepositList;

    @Column(name = "confirm_open_account")
    private int confirmOpenAccount;

    @OneToMany(mappedBy = "openAccount")
    private List<OpenAccountCredit> openAccountCreditList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BankBranch getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(BankBranch bankBranch) {
        this.bankBranch = bankBranch;
    }

    public BankAccountType getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(BankAccountType bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public BankAccountProduct getBankAccountProduct() {
        return bankAccountProduct;
    }

    public void setBankAccountProduct(BankAccountProduct bankAccountProduct) {
        this.bankAccountProduct = bankAccountProduct;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<OpenAccountName> getOpenAccountNameList() {
        return openAccountNameList;
    }

    public void setOpenAccountNameList(List<OpenAccountName> openAccountNameList) {
        this.openAccountNameList = openAccountNameList;
    }

    public List<OpenAccountPurpose> getOpenAccountPurposeList() {
        return openAccountPurposeList;
    }

    public void setOpenAccountPurposeList(List<OpenAccountPurpose> openAccountPurposeList) {
        this.openAccountPurposeList = openAccountPurposeList;
    }

    public int getNumberOfDep() {
        return numberOfDep;
    }

    public void setNumberOfDep(int numberOfDep) {
        this.numberOfDep = numberOfDep;
    }

    public List<OpenAccountDeposit> getOpenAccountDepositList() {
        return openAccountDepositList;
    }

    public void setOpenAccountDepositList(List<OpenAccountDeposit> openAccountDepositList) {
        this.openAccountDepositList = openAccountDepositList;
    }

    public int getConfirmOpenAccount() {
        return confirmOpenAccount;
    }

    public void setConfirmOpenAccount(int confirmOpenAccount) {
        this.confirmOpenAccount = confirmOpenAccount;
    }

    public List<OpenAccountCredit> getOpenAccountCreditList() {
        return openAccountCreditList;
    }

    public void setOpenAccountCreditList(List<OpenAccountCredit> openAccountCreditList) {
        this.openAccountCreditList = openAccountCreditList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("workCase", workCase).
                append("requestType", requestType).
                append("accountNumber", accountNumber).
                append("bankBranch", bankBranch).
                append("bankAccountType", bankAccountType).
                append("bankAccountProduct", bankAccountProduct).
                append("term", term).
                append("openAccountNameList", openAccountNameList).
                append("openAccountPurposeList", openAccountPurposeList).
                append("numberOfDep", numberOfDep).
                append("openAccountDepositList", openAccountDepositList).
                append("confirmOpenAccount", confirmOpenAccount).
                append("openAccountCreditList", openAccountCreditList).
                toString();
    }
}
