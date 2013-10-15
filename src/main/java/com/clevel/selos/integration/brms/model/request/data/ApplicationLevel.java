package com.clevel.selos.integration.brms.model.request.data;

import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.Province;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ApplicationLevel implements Serializable {
    private String applicationNumber;
    private Date processDate;
    private Date appInDate;
    private Date expectedSubmitDate;
    private CustomerEntity customerEntity;
    private boolean existingSMECustomer;
    private boolean sameSetOfBorrower;
    private boolean refinanceInFlag;
    private boolean refinanceOutFlag;
    private BigDecimal borrowerGroupSale;
    private BigDecimal totalGroupSale;
    private BigDecimal totalFacility;
    private BigDecimal contingentFacility;
    private Province bizLocation;
    private int yearInBusiness;
    private Country countryOfRegistration;
    private ProductGroup productGroup;

    List<CustomerLevel> customerLevelList;
    List<BankAccountLevel> bankAccountLevelList;
    List<BusinessLevel> businessLevelList;

    public ApplicationLevel() {
    }

    public ApplicationLevel(String applicationNumber, Date processDate, Date appInDate, Date expectedSubmitDate, CustomerEntity customerEntity, boolean existingSMECustomer, boolean sameSetOfBorrower, boolean refinanceInFlag, boolean refinanceOutFlag, BigDecimal borrowerGroupSale, BigDecimal totalGroupSale, BigDecimal totalFacility, BigDecimal contingentFacility, Province bizLocation, int yearInBusiness, Country countryOfRegistration, ProductGroup productGroup) {
        this.applicationNumber = applicationNumber;
        this.processDate = processDate;
        this.appInDate = appInDate;
        this.expectedSubmitDate = expectedSubmitDate;
        this.customerEntity = customerEntity;
        this.existingSMECustomer = existingSMECustomer;
        this.sameSetOfBorrower = sameSetOfBorrower;
        this.refinanceInFlag = refinanceInFlag;
        this.refinanceOutFlag = refinanceOutFlag;
        this.borrowerGroupSale = borrowerGroupSale;
        this.totalGroupSale = totalGroupSale;
        this.totalFacility = totalFacility;
        this.contingentFacility = contingentFacility;
        this.bizLocation = bizLocation;
        this.yearInBusiness = yearInBusiness;
        this.countryOfRegistration = countryOfRegistration;
        this.productGroup = productGroup;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Date getAppInDate() {
        return appInDate;
    }

    public void setAppInDate(Date appInDate) {
        this.appInDate = appInDate;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public boolean isExistingSMECustomer() {
        return existingSMECustomer;
    }

    public void setExistingSMECustomer(boolean existingSMECustomer) {
        this.existingSMECustomer = existingSMECustomer;
    }

    public boolean isSameSetOfBorrower() {
        return sameSetOfBorrower;
    }

    public void setSameSetOfBorrower(boolean sameSetOfBorrower) {
        this.sameSetOfBorrower = sameSetOfBorrower;
    }

    public boolean isRefinanceInFlag() {
        return refinanceInFlag;
    }

    public void setRefinanceInFlag(boolean refinanceInFlag) {
        this.refinanceInFlag = refinanceInFlag;
    }

    public boolean isRefinanceOutFlag() {
        return refinanceOutFlag;
    }

    public void setRefinanceOutFlag(boolean refinanceOutFlag) {
        this.refinanceOutFlag = refinanceOutFlag;
    }

    public BigDecimal getBorrowerGroupSale() {
        return borrowerGroupSale;
    }

    public void setBorrowerGroupSale(BigDecimal borrowerGroupSale) {
        this.borrowerGroupSale = borrowerGroupSale;
    }

    public BigDecimal getTotalGroupSale() {
        return totalGroupSale;
    }

    public void setTotalGroupSale(BigDecimal totalGroupSale) {
        this.totalGroupSale = totalGroupSale;
    }

    public BigDecimal getTotalFacility() {
        return totalFacility;
    }

    public void setTotalFacility(BigDecimal totalFacility) {
        this.totalFacility = totalFacility;
    }

    public BigDecimal getContingentFacility() {
        return contingentFacility;
    }

    public void setContingentFacility(BigDecimal contingentFacility) {
        this.contingentFacility = contingentFacility;
    }

    public Province getBizLocation() {
        return bizLocation;
    }

    public void setBizLocation(Province bizLocation) {
        this.bizLocation = bizLocation;
    }

    public int getYearInBusiness() {
        return yearInBusiness;
    }

    public void setYearInBusiness(int yearInBusiness) {
        this.yearInBusiness = yearInBusiness;
    }

    public Country getCountryOfRegistration() {
        return countryOfRegistration;
    }

    public void setCountryOfRegistration(Country countryOfRegistration) {
        this.countryOfRegistration = countryOfRegistration;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("applicationNumber", applicationNumber)
                .append("processDate", processDate)
                .append("appInDate", appInDate)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("customerEntity", customerEntity)
                .append("existingSMECustomer", existingSMECustomer)
                .append("sameSetOfBorrower", sameSetOfBorrower)
                .append("refinanceInFlag", refinanceInFlag)
                .append("refinanceOutFlag", refinanceOutFlag)
                .append("borrowerGroupSale", borrowerGroupSale)
                .append("totalGroupSale", totalGroupSale)
                .append("totalFacility", totalFacility)
                .append("contingentFacility", contingentFacility)
                .append("bizLocation", bizLocation)
                .append("yearInBusiness", yearInBusiness)
                .append("countryOfRegistration", countryOfRegistration)
                .append("productGroup", productGroup)
                .toString();
    }
}
