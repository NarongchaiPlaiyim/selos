package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_exist_collateral_detail")
public class ExistingCollateralDetail implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_EXT_COLL_DETAIL_ID", sequenceName = "SEQ_WRK_EXT_COLL_DETAIL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EXT_COLL_DETAIL_ID")
    private long id;

    @Column(name = "no")
    private int no ;

    @Column(name = "borrower_type")
    private int borrowerType;

    @Column(name = "collateral_number")
    private String collateralNumber;

    @Column(name = "collateral_location")
    private String collateralLocation;

    @Column(name = "remark")
    private String remark;

    @Column(name = "cus_name")
    private String cusName;

    @Column(name = "appraisal_value")
    private BigDecimal appraisalValue;

    @Column(name = "mortgage_value")
    private BigDecimal mortgageValue;

    @OneToOne
    @JoinColumn(name = "collateral_type")
    private CollateralType collateralType;

    @OneToOne
    @JoinColumn(name = "potentail_collateral")
    private PotentialCollateral potentialCollateral ;

    @OneToOne
    @JoinColumn(name = "relation")
    private Relation relation;

    @Column(name = "owner")
    private String owner;

    @Column(name = "account_number")
    private String accountNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appraisal_date")
    private Date appraisalDate;

    @OneToOne
    @JoinColumn(name = "mortgage_type")
    private MortgageType mortgageType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @ManyToOne
    @JoinColumn(name = "existing_credit_facility_id")
    private ExistingCreditFacility existingCreditFacility;

    @OneToMany(mappedBy = "existingCollateralDetail", cascade = CascadeType.ALL)
    private List<ExistingCollateralCredit> existingCollateralCreditList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(int borrowerType) {
        this.borrowerType = borrowerType;
    }

    public String getCollateralNumber() {
        return collateralNumber;
    }

    public void setCollateralNumber(String collateralNumber) {
        this.collateralNumber = collateralNumber;
    }

    public String getCollateralLocation() {
        return collateralLocation;
    }

    public void setCollateralLocation(String collateralLocation) {
        this.collateralLocation = collateralLocation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public BigDecimal getMortgageValue() {
        return mortgageValue;
    }

    public void setMortgageValue(BigDecimal mortgageValue) {
        this.mortgageValue = mortgageValue;
    }

    public CollateralType getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(CollateralType collateralType) {
        this.collateralType = collateralType;
    }

    public PotentialCollateral getPotentialCollateral() {
        return potentialCollateral;
    }

    public void setPotentialCollateral(PotentialCollateral potentialCollateral) {
        this.potentialCollateral = potentialCollateral;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public MortgageType getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(MortgageType mortgageType) {
        this.mortgageType = mortgageType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public ExistingCreditFacility getExistingCreditFacility() {
        return existingCreditFacility;
    }

    public void setExistingCreditFacility(ExistingCreditFacility existingCreditFacility) {
        this.existingCreditFacility = existingCreditFacility;
    }

    public List<ExistingCollateralCredit> getExistingCollateralCreditList() {
        return existingCollateralCreditList;
    }

    public void setExistingCollateralCreditList(List<ExistingCollateralCredit> existingCollateralCreditList) {
        this.existingCollateralCreditList = existingCollateralCreditList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("collateralNumber", collateralNumber)
                .append("collateralLocation", collateralLocation)
                .append("remark", remark)
                .append("cusName", cusName)
                .append("appraisalValue", appraisalValue)
                .append("mortgageValue", mortgageValue)
                .append("collateralType", collateralType)
                .append("potentialCollateral", potentialCollateral)
                .append("relation", relation)
                .append("mortgageType", mortgageType)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("existingCreditFacility", existingCreditFacility)
                .append("existingCollateralCreditList", existingCollateralCreditList)
                .toString();
    }
}
