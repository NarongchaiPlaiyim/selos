package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wrk_case_prescreen")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class WorkCasePrescreen extends AbstractWorkCase {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_PRESCREEN_ID", sequenceName = "SEQ_WRK_CASE_PRESCREEN_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_PRESCREEN_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "ca_number", length = 30, nullable = false)
    protected String caNumber;

    @Column(name = "ref_app_number")
    protected String refAppNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_type_id")
    private CustomerEntity borrowerType;

    @OneToMany(mappedBy = "workCasePrescreen", fetch = FetchType.LAZY)
    private List<Customer> customerList;

    @OneToOne
    @JoinColumn(name = "step_owner")
    private User stepOwner;

    @Column(name = "request_appraisal", columnDefinition = "int default 0")
    private int requestAppraisal;

    public WorkCasePrescreen() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(String caNumber) {
        this.caNumber = caNumber;
    }

    public String getRefAppNumber() {
        return refAppNumber;
    }

    public void setRefAppNumber(String refAppNumber) {
        this.refAppNumber = refAppNumber;
    }

    public CustomerEntity getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(CustomerEntity borrowerType) {
        this.borrowerType = borrowerType;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public User getStepOwner() {
        return stepOwner;
    }

    public void setStepOwner(User stepOwner) {
        this.stepOwner = stepOwner;
    }

    public int getRequestAppraisal() {
        return requestAppraisal;
    }

    public void setRequestAppraisal(int requestAppraisal) {
        this.requestAppraisal = requestAppraisal;
    }
}
