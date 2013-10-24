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
@Table(name="wrk_biz_info_summary")
public class BizInfoSummary implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_BIZ_INFO_SUM_ID", sequenceName="SEQ_WRK_BIZ_INFO_SUM_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BIZ_INFO_SUM_ID")
    private long id;

    @Column(name="bizLocationName")
    private String bizLocationName;

    @Column(name="is_rental")
    private int isRental;

    @Column(name="owner_name")
    private String ownerName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="expiry_date")
    private Date expiryDate;

    @Column(name="address_no")
    private String addressNo;

    @Column(name="address_moo")
    private String addressMoo;

    @Column(name="address_building")
    private String addressBuilding;

    @Column(name="address_street")
    private String addressStreet;

    @OneToOne
    @JoinColumn(name="province_id")
    private Province province;

    @OneToOne
    @JoinColumn(name="district_id")
    private District district;

    @OneToOne
    @JoinColumn(name="subdistrict_id")
    private SubDistrict subDistrict;

    @Column(name="post_code")
    private String postCode;

    @OneToOne
    @JoinColumn(name="country_id")
    private Country country;

    @Column(name="address_eng")
    private String addressEng;

    @Column(name="phone_no")
    private String phoneNo;

    @Column(name="extension")
    private String extension;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="registration_date")
    private Date registrationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="establish_date")
    private Date establishDate;

    @OneToOne
    @JoinColumn(name="refer_exp_id")
    private ReferredExperience referredExperience;

    @Column(name="biz_interview_info")
    private String bizInterviewInfo;

    @Column(name="circulation_amount")
    private BigDecimal circulationAmount;

    @Column(name="circulation_percentage")
    private BigDecimal circulationPercentage;

    @Column(name="production_costs_amount")
    private BigDecimal productionCostsAmount;

    @Column(name="production_costs_percentage")
    private BigDecimal productionCostsPercentage;

    @Column(name="profit_margin_amount")
    private BigDecimal profitMarginAmount;

    @Column(name="profit_margin_percentage")
    private BigDecimal profitMarginPercentage;

    @Column(name="operating_expense_amount")
    private BigDecimal operatingExpenseAmount;

    @Column(name="operating_expense_percentage")
    private BigDecimal operatingExpensePercentage;

    @Column(name="earning_befor_tax_amount")
    private BigDecimal earningsBeforeTaxAmount;

    @Column(name="earning_befor_tax_percentage")
    private BigDecimal earningsBeforeTaxPercentage;

    @Column(name="reduce_interest_amount")
    private BigDecimal reduceInterestAmount;

    @Column(name="reduce_interest_percentage")
    private BigDecimal reduceInterestPercentage;

    @Column(name="reduce_tax_amount")
    private BigDecimal reduceTaxAmount;

    @Column(name="reduce_tax_percentage")
    private BigDecimal reduceTaxPercentage;

    @Column(name="net_margin_amount")
    private BigDecimal netMarginAmount;

    @Column(name="net_margin_percentage")
    private BigDecimal netMarginPercentage;

    @Column(name="net_fix_asset")
    private BigDecimal netFixAsset;

    @Column(name="no_of_employee")
    private int noOfEmployee;

    @Column(name="sum_income_amount")
    private BigDecimal  sumIncomeAmount;

    @Column(name="sum_income_percent")
    private BigDecimal sumIncomePercent;

    @Column(name="weight_sum_income_factor")
    private BigDecimal weightIncomeFactor;

    @Column(name="sum_weight_intv_income")
    private BigDecimal  sumWeightInterviewedIncomeFactorPercent;

    @Column(name="sum_weight_ar")
    private BigDecimal  sumWeightAR;

    @Column(name="sum_weight_ap")
    private BigDecimal sumWeightAP;

    @Column(name="sum_weight_inv")
    private BigDecimal sumWeightINV;

    @OneToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    @OneToMany(mappedBy="bizInfoSummary")
    private List<BizInfoDetail> bizInfoDetailList;

    public BizInfoSummary(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBizLocationName() {
        return bizLocationName;
    }

    public void setBizLocationName(String bizLocationName) {
        this.bizLocationName = bizLocationName;
    }

    public int getRental() {
        return isRental;
    }

    public void setRental(int rental) {
        isRental = rental;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public String getAddressMoo() {
        return addressMoo;
    }

    public void setAddressMoo(String addressMoo) {
        this.addressMoo = addressMoo;
    }

    public String getAddressBuilding() {
        return addressBuilding;
    }

    public void setAddressBuilding(String addressBuilding) {
        this.addressBuilding = addressBuilding;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public SubDistrict getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(SubDistrict subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getAddressEng() {
        return addressEng;
    }

    public void setAddressEng(String addressEng) {
        this.addressEng = addressEng;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(Date establishDate) {
        this.establishDate = establishDate;
    }

    public ReferredExperience getReferredExperience() {
        return referredExperience;
    }

    public void setReferredExperience(ReferredExperience referredExperience) {
        this.referredExperience = referredExperience;
    }

    public String getBizInterviewInfo() {
        return bizInterviewInfo;
    }

    public void setBizInterviewInfo(String bizInterviewInfo) {
        this.bizInterviewInfo = bizInterviewInfo;
    }

    public BigDecimal getCirculationAmount() {
        return circulationAmount;
    }

    public void setCirculationAmount(BigDecimal circulationAmount) {
        this.circulationAmount = circulationAmount;
    }

    public BigDecimal getCirculationPercentage() {
        return circulationPercentage;
    }

    public void setCirculationPercentage(BigDecimal circulationPercentage) {
        this.circulationPercentage = circulationPercentage;
    }

    public BigDecimal getProductionCostsAmount() {
        return productionCostsAmount;
    }

    public void setProductionCostsAmount(BigDecimal productionCostsAmount) {
        this.productionCostsAmount = productionCostsAmount;
    }

    public BigDecimal getProductionCostsPercentage() {
        return productionCostsPercentage;
    }

    public void setProductionCostsPercentage(BigDecimal productionCostsPercentage) {
        this.productionCostsPercentage = productionCostsPercentage;
    }

    public BigDecimal getProfitMarginAmount() {
        return profitMarginAmount;
    }

    public void setProfitMarginAmount(BigDecimal profitMarginAmount) {
        this.profitMarginAmount = profitMarginAmount;
    }

    public BigDecimal getProfitMarginPercentage() {
        return profitMarginPercentage;
    }

    public void setProfitMarginPercentage(BigDecimal profitMarginPercentage) {
        this.profitMarginPercentage = profitMarginPercentage;
    }

    public BigDecimal getOperatingExpenseAmount() {
        return operatingExpenseAmount;
    }

    public void setOperatingExpenseAmount(BigDecimal operatingExpenseAmount) {
        this.operatingExpenseAmount = operatingExpenseAmount;
    }

    public BigDecimal getOperatingExpensePercentage() {
        return operatingExpensePercentage;
    }

    public void setOperatingExpensePercentage(BigDecimal operatingExpensePercentage) {
        this.operatingExpensePercentage = operatingExpensePercentage;
    }

    public BigDecimal getEarningsBeforeTaxAmount() {
        return earningsBeforeTaxAmount;
    }

    public void setEarningsBeforeTaxAmount(BigDecimal earningsBeforeTaxAmount) {
        this.earningsBeforeTaxAmount = earningsBeforeTaxAmount;
    }

    public BigDecimal getEarningsBeforeTaxPercentage() {
        return earningsBeforeTaxPercentage;
    }

    public void setEarningsBeforeTaxPercentage(BigDecimal earningsBeforeTaxPercentage) {
        this.earningsBeforeTaxPercentage = earningsBeforeTaxPercentage;
    }

    public BigDecimal getReduceInterestAmount() {
        return reduceInterestAmount;
    }

    public void setReduceInterestAmount(BigDecimal reduceInterestAmount) {
        this.reduceInterestAmount = reduceInterestAmount;
    }

    public BigDecimal getReduceInterestPercentage() {
        return reduceInterestPercentage;
    }

    public void setReduceInterestPercentage(BigDecimal reduceInterestPercentage) {
        this.reduceInterestPercentage = reduceInterestPercentage;
    }

    public BigDecimal getReduceTaxAmount() {
        return reduceTaxAmount;
    }

    public void setReduceTaxAmount(BigDecimal reduceTaxAmount) {
        this.reduceTaxAmount = reduceTaxAmount;
    }

    public BigDecimal getReduceTaxPercentage() {
        return reduceTaxPercentage;
    }

    public void setReduceTaxPercentage(BigDecimal reduceTaxPercentage) {
        this.reduceTaxPercentage = reduceTaxPercentage;
    }

    public BigDecimal getNetMarginAmount() {
        return netMarginAmount;
    }

    public void setNetMarginAmount(BigDecimal netMarginAmount) {
        this.netMarginAmount = netMarginAmount;
    }

    public BigDecimal getNetMarginPercentage() {
        return netMarginPercentage;
    }

    public void setNetMarginPercentage(BigDecimal netMarginPercentage) {
        this.netMarginPercentage = netMarginPercentage;
    }

    public BigDecimal getNetFixAsset() {
        return netFixAsset;
    }

    public void setNetFixAsset(BigDecimal netFixAsset) {
        this.netFixAsset = netFixAsset;
    }

    public int getNoOfEmployee() {
        return noOfEmployee;
    }

    public void setNoOfEmployee(int noOfEmployee) {
        this.noOfEmployee = noOfEmployee;
    }

    public BigDecimal getSumIncomeAmount() {
        return sumIncomeAmount;
    }

    public void setSumIncomeAmount(BigDecimal sumIncomeAmount) {
        this.sumIncomeAmount = sumIncomeAmount;
    }

    public BigDecimal getSumIncomePercent() {
        return sumIncomePercent;
    }

    public void setSumIncomePercent(BigDecimal sumIncomePercent) {
        this.sumIncomePercent = sumIncomePercent;
    }

    public BigDecimal getWeightIncomeFactor() {
        return weightIncomeFactor;
    }

    public void setWeightIncomeFactor(BigDecimal weightIncomeFactor) {
        this.weightIncomeFactor = weightIncomeFactor;
    }

    public BigDecimal getSumWeightInterviewedIncomeFactorPercent() {
        return sumWeightInterviewedIncomeFactorPercent;
    }

    public void setSumWeightInterviewedIncomeFactorPercent(BigDecimal sumWeightInterviewedIncomeFactorPercent) {
        this.sumWeightInterviewedIncomeFactorPercent = sumWeightInterviewedIncomeFactorPercent;
    }

    public BigDecimal getSumWeightAR() {
        return sumWeightAR;
    }

    public void setSumWeightAR(BigDecimal sumWeightAR) {
        this.sumWeightAR = sumWeightAR;
    }

    public BigDecimal getSumWeightAP() {
        return sumWeightAP;
    }

    public void setSumWeightAP(BigDecimal sumWeightAP) {
        this.sumWeightAP = sumWeightAP;
    }

    public BigDecimal getSumWeightINV() {
        return sumWeightINV;
    }

    public void setSumWeightINV(BigDecimal sumWeightINV) {
        this.sumWeightINV = sumWeightINV;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
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

    public List<BizInfoDetail> getBizInfoDetailList() {
        return bizInfoDetailList;
    }

    public void setBizInfoDetailList(List<BizInfoDetail> bizInfoDetailList) {
        this.bizInfoDetailList = bizInfoDetailList;
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)

                .append("id", id)
                .append("bizLocationName", bizLocationName)
                .append("isRental", isRental)
                .append("ownerName", ownerName)
                .append("expiryDate", expiryDate)
                .append("addressNo", addressNo)
                .append("addressMoo", addressMoo)
                .append("addressBuilding", addressBuilding)
                .append("addressStreet", addressStreet)
                .append("province", province)
                .append("district", district)
                .append("subDistrict", subDistrict)
                .append("postCode", postCode)
                .append("country", country)
                .append("addressEng", addressEng)
                .append("phoneNo", phoneNo)
                .append("extension", extension)
                .append("registrationDate", registrationDate)
                .append("establishDate", establishDate)
                .append("referredExperience", referredExperience)
                .append("bizInterviewInfo", bizInterviewInfo)
                .append("circulationAmount", circulationAmount)
                .append("circulationPercentage", circulationPercentage)
                .append("productionCostsAmount", productionCostsAmount)
                .append("productionCostsPercentage", productionCostsPercentage)
                .append("profitMarginAmount", profitMarginAmount)
                .append("profitMarginPercentage", profitMarginPercentage)
                .append("operatingExpenseAmount", operatingExpenseAmount)
                .append("operatingExpensePercentage", operatingExpensePercentage)
                .append("earningsBeforeTaxAmount", earningsBeforeTaxAmount)
                .append("earningsBeforeTaxPercentage", earningsBeforeTaxPercentage)
                .append("reduceInterestAmount", reduceInterestAmount)
                .append("reduceInterestPercentage", reduceInterestPercentage)
                .append("reduceTaxAmount", reduceTaxAmount)
                .append("reduceTaxPercentage", reduceTaxPercentage)
                .append("netMarginAmount", netMarginAmount)
                .append("netMarginPercentage", netMarginPercentage)
                .append("netFixAsset", netFixAsset)
                .append("noOfEmployee", noOfEmployee)
                .append("sumIncomeAmount", sumIncomeAmount)
                .append("sumIncomePercent", sumIncomePercent)
                .append("weightIncomeFactor", weightIncomeFactor)
                .append("sumWeightInterviewedIncomeFactorPercent", sumWeightInterviewedIncomeFactorPercent)
                .append("sumWeightAR", sumWeightAR)
                .append("sumWeightAP", sumWeightAP)
                .append("workCase", workCase)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("bizInfoDetailList", bizInfoDetailList)
                .toString();
    }
}
