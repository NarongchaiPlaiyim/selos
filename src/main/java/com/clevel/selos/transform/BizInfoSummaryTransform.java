package com.clevel.selos.transform;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.view.BizInfoSummaryView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.math.BigDecimal;

public class BizInfoSummaryTransform extends Transform {

    @Inject
    ProvinceDAO provinceDAO;
    @Inject
    DistrictDAO districtDAO;
    @Inject
    SubDistrictDAO subDistrictDAO;
    @Inject
    CountryDAO countryDAO;
    @Inject
    ReferredExperienceDAO referredExperienceDAO;

    private Province province;
    private District district;
    private SubDistrict subDistrict;
    private Country country;
    private ReferredExperience referredExperience;

    public BizInfoSummary transformToModel(BizInfoSummaryView bizInfoSummaryView) {
        log.info("find transformToModel begin");
        BizInfoSummary bizInfoSummary;

        bizInfoSummary = new BizInfoSummary();

        if (bizInfoSummaryView.getId() != 0) {
            bizInfoSummary.setId(bizInfoSummaryView.getId());
        } else if (bizInfoSummaryView.getId() == 0) {
            bizInfoSummary.setCreateBy(bizInfoSummaryView.getCreateBy());
            bizInfoSummary.setCreateDate(DateTime.now().toDate());
        }

        log.info("find transformToModel xxx1");

        bizInfoSummary.setBizLocationName(bizInfoSummaryView.getBizLocationName());
        bizInfoSummary.setRental(bizInfoSummaryView.getRental());
        bizInfoSummary.setOwnerName(bizInfoSummaryView.getOwnerName());
        bizInfoSummary.setExpiryDate(bizInfoSummaryView.getExpiryDate());

        log.info("transformToModel bizInfoSummaryView getAddressMoo is ---- " + bizInfoSummaryView.getAddressMoo());
        log.info("transformToModel bizInfoSummaryView getAddressNo is ---- " + bizInfoSummaryView.getAddressNo());

        bizInfoSummary.setAddressNo(bizInfoSummaryView.getAddressNo());
        bizInfoSummary.setAddressMoo(bizInfoSummaryView.getAddressMoo());
        bizInfoSummary.setAddressBuilding(bizInfoSummaryView.getAddressBuilding());
        bizInfoSummary.setAddressStreet(bizInfoSummaryView.getAddressStreet());

        log.info("find transformToModel province" + bizInfoSummaryView.getSubDistrict().getDistrict().getProvince().getCode());

        if(bizInfoSummaryView.getProvince()!=null && bizInfoSummaryView.getProvince().getCode() != 0){
            province = provinceDAO.findById(bizInfoSummaryView.getSubDistrict().getDistrict().getProvince().getCode());
            log.info("find transformToModel findById  is " + province.toString());
            bizInfoSummary.setProvince(province);
        }else{
            bizInfoSummary.setProvince(null);
        }

        log.info("find transformToModel district" + bizInfoSummaryView.getSubDistrict().getDistrict().getId());

        if(bizInfoSummaryView.getDistrict()!=null && bizInfoSummaryView.getDistrict().getId() != 0){
            district = districtDAO.findById(bizInfoSummaryView.getSubDistrict().getDistrict().getId());
            log.info("find transformToModel findById  is " + district.toString());
            bizInfoSummary.setDistrict(district);
        }else{
            bizInfoSummary.setDistrict(null);
        }

        log.info("find transformToModel subDistrict" + bizInfoSummaryView.getSubDistrict().getCode());
        if(bizInfoSummaryView.getSubDistrict()!=null && bizInfoSummaryView.getSubDistrict().getCode() != 0){
            subDistrict = subDistrictDAO.findById(bizInfoSummaryView.getSubDistrict().getCode());
            log.info("find transformToModel findById  is " + subDistrict.toString());
            bizInfoSummary.setSubDistrict(subDistrict);
        }else{
            bizInfoSummary.setSubDistrict(null);
        }

        log.info("find transformToModel getCountry" + bizInfoSummaryView.getCountry().getId());
        if(bizInfoSummaryView.getCountry()!=null && bizInfoSummaryView.getCountry().getId() != 0){
            country = countryDAO.findById(bizInfoSummaryView.getCountry().getId());
            log.info("find transformToModel findById  is " + country.toString());
            bizInfoSummary.setCountry(country);
        }else{
            bizInfoSummary.setCountry(null);
        }

        bizInfoSummary.setAddressEng(bizInfoSummaryView.getAddressEng());

        bizInfoSummary.setPostCode(bizInfoSummaryView.getPostCode());
        bizInfoSummary.setPhoneNo(bizInfoSummaryView.getPhoneNo());
        bizInfoSummary.setExtension(bizInfoSummaryView.getExtension());

        bizInfoSummary.setRegistrationDate(bizInfoSummaryView.getRegistrationDate());
        bizInfoSummary.setEstablishDate(bizInfoSummaryView.getEstablishDate());

        log.info("find transformToModel getReferredExperience" + bizInfoSummaryView.getReferredExperience().getId());
        if(bizInfoSummaryView.getReferredExperience()!=null && bizInfoSummaryView.getReferredExperience().getId() != 0){
            referredExperience = referredExperienceDAO.findById(bizInfoSummaryView.getReferredExperience().getId());
            bizInfoSummary.setReferredExperience(referredExperience);
        }else{
            bizInfoSummary.setReferredExperience(null);
        }

        if(bizInfoSummaryView.getCirculationAmount() != null){
            bizInfoSummary.setCirculationAmount(bizInfoSummaryView.getCirculationAmount());
        }else{
            bizInfoSummary.setCirculationAmount(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getCirculationPercentage() != null){
            bizInfoSummary.setCirculationPercentage(bizInfoSummaryView.getCirculationPercentage());
        }else{
            bizInfoSummary.setCirculationPercentage(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getProductionCostsAmount() != null){
            bizInfoSummary.setProductionCostsAmount(bizInfoSummaryView.getProductionCostsAmount());
        }else{
            bizInfoSummary.setProductionCostsAmount(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getProductionCostsPercentage() != null){
            bizInfoSummary.setProductionCostsPercentage(bizInfoSummaryView.getProductionCostsPercentage());
        }else{
            bizInfoSummary.setProductionCostsPercentage(BigDecimal.ZERO);
        }


        if(bizInfoSummaryView.getProfitMarginAmount() != null){
            bizInfoSummary.setProfitMarginAmount(bizInfoSummaryView.getProfitMarginAmount());
        }else{
            bizInfoSummary.setProfitMarginAmount(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getProfitMarginPercentage() != null){
            bizInfoSummary.setProfitMarginPercentage(bizInfoSummaryView.getProfitMarginPercentage());
        }else{
            bizInfoSummary.setProfitMarginPercentage(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getOperatingExpenseAmount() != null){
            bizInfoSummary.setOperatingExpenseAmount(bizInfoSummaryView.getOperatingExpenseAmount());
        }else{
            bizInfoSummary.setOperatingExpenseAmount(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getOperatingExpensePercentage() != null){
            bizInfoSummary.setOperatingExpensePercentage(bizInfoSummaryView.getOperatingExpensePercentage());
        }else{
            bizInfoSummary.setOperatingExpensePercentage(BigDecimal.ZERO);
        }


        if(bizInfoSummaryView.getReduceTaxAmount() != null){
            bizInfoSummary.setReduceTaxAmount(bizInfoSummaryView.getReduceTaxAmount());
        }else{
            bizInfoSummary.setReduceTaxAmount(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getReduceTaxPercentage() != null){
            bizInfoSummary.setReduceTaxPercentage(bizInfoSummaryView.getReduceTaxPercentage());
        }else{
            bizInfoSummary.setReduceTaxPercentage(BigDecimal.ZERO);
        }


        if(bizInfoSummaryView.getReduceInterestAmount() != null){
            bizInfoSummary.setReduceInterestAmount(bizInfoSummaryView.getReduceInterestAmount());
        }else{
            bizInfoSummary.setReduceInterestAmount(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getReduceInterestPercentage() != null){
            bizInfoSummary.setReduceInterestPercentage(bizInfoSummaryView.getReduceInterestPercentage());
        }else{
            bizInfoSummary.setReduceInterestPercentage(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getEarningsBeforeTaxAmount() != null){
            bizInfoSummary.setEarningsBeforeTaxAmount(bizInfoSummaryView.getEarningsBeforeTaxAmount());
        }else{
            bizInfoSummary.setEarningsBeforeTaxAmount(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getEarningsBeforeTaxPercentage() != null){
            bizInfoSummary.setEarningsBeforeTaxPercentage(bizInfoSummaryView.getEarningsBeforeTaxPercentage());
        }else{
            bizInfoSummary.setEarningsBeforeTaxPercentage(BigDecimal.ZERO);
        }


        if(bizInfoSummaryView.getNetMarginAmount() != null){
            bizInfoSummary.setNetMarginAmount(bizInfoSummaryView.getNetMarginAmount());
        }else{
            bizInfoSummary.setNetMarginAmount(BigDecimal.ZERO);
        }

        if(bizInfoSummaryView.getNetMarginPercentage() != null){
            bizInfoSummary.setNetMarginPercentage(bizInfoSummaryView.getNetMarginPercentage());
        }else{
            bizInfoSummary.setNetMarginPercentage(BigDecimal.ZERO);
        }

        bizInfoSummary.setNetFixAsset(bizInfoSummaryView.getNetFixAsset());
        bizInfoSummary.setNoOfEmployee(bizInfoSummaryView.getNoOfEmployee());

        log.info("find transformToModel xxx4");

        bizInfoSummary.setSumIncomeAmount(bizInfoSummaryView.getSumIncomeAmount());
        bizInfoSummary.setSumIncomePercent(bizInfoSummaryView.getSumIncomePercent());
        bizInfoSummary.setSumWeightAR(bizInfoSummaryView.getSumWeightAR());
        bizInfoSummary.setSumWeightAP(bizInfoSummaryView.getSumWeightAP());
        bizInfoSummary.setSumWeightINV(bizInfoSummaryView.getSumWeightINV());
        bizInfoSummary.setSumWeightInterviewedIncomeFactorPercent(bizInfoSummaryView.getSumWeightInterviewedIncomeFactorPercent());


        bizInfoSummary.setModifyBy(bizInfoSummaryView.getModifyBy());
        bizInfoSummary.setModifyDate(DateTime.now().toDate());


        log.info("transformToModel bizInfoSummary getAddressMoo is ---- " + bizInfoSummaryView.getAddressMoo());
        log.info("transformToModel bizInfoSummary getAddressNo is ---- " + bizInfoSummaryView.getAddressNo());

        log.info("find transformToModel end");

        return bizInfoSummary;
    }

    public BizInfoSummaryView transformToView(BizInfoSummary bizInfoSummary) {
        BizInfoSummaryView bizInfoSummaryView;

        bizInfoSummaryView = new BizInfoSummaryView();

        bizInfoSummaryView.setId(bizInfoSummary.getId());

        bizInfoSummaryView.setBizLocationName(bizInfoSummary.getBizLocationName());
        bizInfoSummaryView.setRental(bizInfoSummary.getRental());
        bizInfoSummaryView.setOwnerName(bizInfoSummary.getOwnerName());
        bizInfoSummaryView.setExpiryDate(bizInfoSummary.getExpiryDate());

        bizInfoSummaryView.setAddressNo(bizInfoSummary.getAddressNo());
        bizInfoSummaryView.setAddressMoo(bizInfoSummary.getAddressMoo());
        bizInfoSummaryView.setAddressBuilding(bizInfoSummary.getAddressBuilding());
        bizInfoSummaryView.setAddressStreet(bizInfoSummary.getAddressStreet());


        bizInfoSummaryView.setProvince(bizInfoSummary.getProvince());
        bizInfoSummaryView.setDistrict(bizInfoSummary.getDistrict());
        bizInfoSummaryView.setSubDistrict(bizInfoSummary.getSubDistrict());
        bizInfoSummaryView.setCountry(bizInfoSummary.getCountry());
        bizInfoSummaryView.setAddressEng(bizInfoSummary.getAddressEng());

        bizInfoSummaryView.setPostCode(bizInfoSummary.getPostCode());
        bizInfoSummaryView.setPhoneNo(bizInfoSummary.getPhoneNo());
        bizInfoSummaryView.setExtension(bizInfoSummary.getExtension());

        bizInfoSummaryView.setRegistrationDate(bizInfoSummary.getRegistrationDate());
        bizInfoSummaryView.setEstablishDate(bizInfoSummary.getEstablishDate());
        bizInfoSummaryView.setReferredExperience(bizInfoSummary.getReferredExperience());

        bizInfoSummaryView.setCirculationAmount(bizInfoSummary.getCirculationAmount());
        bizInfoSummaryView.setCirculationPercentage(bizInfoSummary.getCirculationPercentage());

        bizInfoSummaryView.setProductionCostsAmount(bizInfoSummary.getProductionCostsAmount());
        bizInfoSummaryView.setProductionCostsPercentage(bizInfoSummary.getProductionCostsPercentage());

        bizInfoSummaryView.setProfitMarginAmount(bizInfoSummary.getProfitMarginAmount());
        bizInfoSummaryView.setProfitMarginPercentage(bizInfoSummary.getProfitMarginPercentage());

        bizInfoSummaryView.setOperatingExpenseAmount(bizInfoSummary.getOperatingExpenseAmount());
        bizInfoSummaryView.setOperatingExpensePercentage(bizInfoSummary.getOperatingExpensePercentage());

        bizInfoSummaryView.setReduceTaxAmount(bizInfoSummary.getReduceTaxAmount());
        bizInfoSummaryView.setReduceTaxPercentage(bizInfoSummary.getReduceTaxPercentage());

        bizInfoSummaryView.setReduceInterestAmount(bizInfoSummary.getReduceInterestAmount());
        bizInfoSummaryView.setReduceInterestPercentage(bizInfoSummary.getReduceInterestAmount());

        bizInfoSummaryView.setEarningsBeforeTaxAmount(bizInfoSummary.getEarningsBeforeTaxAmount());
        bizInfoSummaryView.setEarningsBeforeTaxPercentage(bizInfoSummary.getEarningsBeforeTaxPercentage());

        bizInfoSummaryView.setNetMarginAmount(bizInfoSummary.getNetMarginAmount());
        bizInfoSummaryView.setNetMarginPercentage(bizInfoSummary.getNetMarginPercentage());

        bizInfoSummaryView.setModifyBy(bizInfoSummary.getModifyBy());
        bizInfoSummaryView.setModifyDate(bizInfoSummary.getModifyDate());

        bizInfoSummaryView.setCreateBy(bizInfoSummary.getCreateBy());
        bizInfoSummaryView.setCreateDate(bizInfoSummary.getCreateDate());

        bizInfoSummaryView.setNetFixAsset(bizInfoSummary.getNetFixAsset());

        bizInfoSummaryView.setNoOfEmployee(bizInfoSummary.getNoOfEmployee());

        bizInfoSummaryView.setSubDistrict(bizInfoSummary.getSubDistrict());

        return bizInfoSummaryView;
    }

}
