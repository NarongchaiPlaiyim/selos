package com.clevel.selos.transform;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.BizInfoSummaryDAO;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.view.BizInfoSummaryView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.math.BigDecimal;

public class BizInfoSummaryTransform extends Transform {

    @Inject
    private ProvinceDAO provinceDAO;
    @Inject
    private DistrictDAO districtDAO;
    @Inject
    private SubDistrictDAO subDistrictDAO;
    @Inject
    private CountryDAO countryDAO;
    @Inject
    private ReferredExperienceDAO referredExperienceDAO;
    @Inject
    private BizInfoSummaryDAO bizInfoSummaryDAO;

    private Province province;
    private District district;
    private SubDistrict subDistrict;
    private Country country;
    private ReferredExperience referredExperience;

    public BizInfoSummary transformToModel(BizInfoSummaryView bizInfoSummaryView) {
        log.debug("find transformToModel begin");
        log.debug("BizInfoSummaryView : {}", bizInfoSummaryView);
        BizInfoSummary bizInfoSummary;

        bizInfoSummary = new BizInfoSummary();

        if (bizInfoSummaryView.getId() != 0) {
            bizInfoSummary = bizInfoSummaryDAO.findById(bizInfoSummaryView.getId());
        } else if (bizInfoSummaryView.getId() == 0) {
            bizInfoSummary.setCreateBy(bizInfoSummaryView.getCreateBy());
            bizInfoSummary.setCreateDate(DateTime.now().toDate());
        }

        log.debug("find transformToModel xxx1");

        bizInfoSummary.setBizLocationName(bizInfoSummaryView.getBizLocationName());
        bizInfoSummary.setRental(bizInfoSummaryView.getRental());
        bizInfoSummary.setOwnerName(bizInfoSummaryView.getOwnerName());
        bizInfoSummary.setExpiryDate(bizInfoSummaryView.getExpiryDate());

        log.debug("transformToModel bizInfoSummaryView getAddressMoo is ---- {}",bizInfoSummaryView.getAddressMoo());
        log.debug("transformToModel bizInfoSummaryView getAddressNo is ---- {}",bizInfoSummaryView.getAddressNo());

        bizInfoSummary.setAddressNo(bizInfoSummaryView.getAddressNo());
        bizInfoSummary.setAddressMoo(bizInfoSummaryView.getAddressMoo());
        bizInfoSummary.setAddressBuilding(bizInfoSummaryView.getAddressBuilding());
        bizInfoSummary.setAddressStreet(bizInfoSummaryView.getAddressStreet());

//        log.debug("find transformToModel province" + bizInfoSummaryView.getProvince().getCode());

        if(bizInfoSummaryView.getProvince()!=null && bizInfoSummaryView.getProvince().getCode() != 0){
            province = provinceDAO.findById(bizInfoSummaryView.getProvince().getCode());
            log.debug("find transformToModel findById  is {}",province);
            bizInfoSummary.setProvince(province);
        }else{
            bizInfoSummary.setProvince(null);
        }

//        log.debug("find transformToModel district" + bizInfoSummaryView.getDistrict().getId());

        if(bizInfoSummaryView.getDistrict()!=null && bizInfoSummaryView.getDistrict().getId() != 0){
            district = districtDAO.findById(bizInfoSummaryView.getDistrict().getId());
            log.debug("find transformToModel findById  is {}",district.toString());
            bizInfoSummary.setDistrict(district);
        }else{
            bizInfoSummary.setDistrict(null);
        }

//        log.debug("find transformToModel subDistrict" + bizInfoSummaryView.getSubDistrict().getCode());
        if(bizInfoSummaryView.getSubDistrict()!=null && bizInfoSummaryView.getSubDistrict().getCode() != 0){
            subDistrict = subDistrictDAO.findById(bizInfoSummaryView.getSubDistrict().getCode());
            log.debug("find transformToModel findById  is {}",subDistrict);
            bizInfoSummary.setSubDistrict(subDistrict);
        }else{
            bizInfoSummary.setSubDistrict(null);
        }

//        log.debug("find transformToModel getCountry" + bizInfoSummaryView.getCountry().getId());
        if(bizInfoSummaryView.getCountry()!=null && bizInfoSummaryView.getCountry().getId() != 0){
            country = countryDAO.findById(bizInfoSummaryView.getCountry().getId());
            log.debug("find transformToModel findById  is {}",country);
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

        // fixed: due to NPE when getReferredExperience is null.
//        log.debug("find transformToModel getReferredExperience" + bizInfoSummaryView.getReferredExperience().getId());

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

        log.debug("find transformToModel xxx4");

        bizInfoSummary.setSumIncomeAmount(bizInfoSummaryView.getSumIncomeAmount());
        bizInfoSummary.setSumIncomePercent(bizInfoSummaryView.getSumIncomePercent());
        bizInfoSummary.setSumWeightAR(bizInfoSummaryView.getSumWeightAR());
        bizInfoSummary.setSumWeightAP(bizInfoSummaryView.getSumWeightAP());
        bizInfoSummary.setSumWeightINV(bizInfoSummaryView.getSumWeightINV());
        bizInfoSummary.setSumWeightInterviewedIncomeFactorPercent(bizInfoSummaryView.getSumWeightInterviewedIncomeFactorPercent());
        bizInfoSummary.setWeightIncomeFactor(bizInfoSummaryView.getWeightIncomeFactor());



        bizInfoSummary.setModifyBy(bizInfoSummaryView.getModifyBy());
        bizInfoSummary.setModifyDate(DateTime.now().toDate());


        log.debug("transformToModel bizInfoSummary getAddressMoo is ---- {}",bizInfoSummaryView.getAddressMoo());
        log.debug("transformToModel bizInfoSummary getAddressNo is ---- {}",bizInfoSummaryView.getAddressNo());

        log.debug("find transformToModel end");

        return bizInfoSummary;
    }

    public BizInfoSummaryView transformToView(BizInfoSummary bizInfoSummary) {
        log.debug("find transformToView begin");
        log.debug("bizInfoSummary : {}", bizInfoSummary);
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

        log.debug("Province : {}",bizInfoSummary.getProvince());
        if(bizInfoSummary.getProvince() != null && bizInfoSummary.getProvince().getCode() != 0){
            bizInfoSummaryView.setProvince(bizInfoSummary.getProvince());
        } else {
            bizInfoSummaryView.setProvince(new Province());
        }

        log.debug("District : {}",bizInfoSummary.getDistrict());
        if(bizInfoSummary.getDistrict() != null && bizInfoSummary.getDistrict().getId() != 0){
            bizInfoSummaryView.setDistrict(bizInfoSummary.getDistrict());
        } else {
            bizInfoSummaryView.setDistrict(new District());
        }

        log.debug("Sub District : {}",bizInfoSummary.getSubDistrict());
        if(bizInfoSummary.getSubDistrict() != null && bizInfoSummary.getSubDistrict().getCode() != 0){
            bizInfoSummaryView.setSubDistrict(bizInfoSummary.getSubDistrict());
        } else {
            bizInfoSummaryView.setSubDistrict(new SubDistrict());
        }

        if(bizInfoSummary.getCountry() != null){
            bizInfoSummaryView.setCountry(bizInfoSummary.getCountry());
        } else {
            bizInfoSummaryView.setCountry(new Country());
        }
        bizInfoSummaryView.setAddressEng(bizInfoSummary.getAddressEng());

        bizInfoSummaryView.setPostCode(bizInfoSummary.getPostCode());
        bizInfoSummaryView.setPhoneNo(bizInfoSummary.getPhoneNo());
        bizInfoSummaryView.setExtension(bizInfoSummary.getExtension());

        bizInfoSummaryView.setRegistrationDate(bizInfoSummary.getRegistrationDate());
        bizInfoSummaryView.setEstablishDate(bizInfoSummary.getEstablishDate());

        if(bizInfoSummary.getReferredExperience() != null){
            bizInfoSummaryView.setReferredExperience(bizInfoSummary.getReferredExperience());
        } else {
            bizInfoSummaryView.setReferredExperience(new ReferredExperience());
        }

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

        bizInfoSummaryView.setSumIncomeAmount(bizInfoSummary.getSumIncomeAmount());
        bizInfoSummaryView.setSumIncomePercent(bizInfoSummary.getSumIncomePercent());
        bizInfoSummaryView.setSumWeightAR(bizInfoSummary.getSumWeightAR());
        bizInfoSummaryView.setSumWeightAP(bizInfoSummary.getSumWeightAP());
        bizInfoSummaryView.setSumWeightINV(bizInfoSummary.getSumWeightINV());
        bizInfoSummaryView.setSumWeightInterviewedIncomeFactorPercent(bizInfoSummary.getSumWeightInterviewedIncomeFactorPercent());
        bizInfoSummaryView.setWeightIncomeFactor(bizInfoSummary.getWeightIncomeFactor());

        return bizInfoSummaryView;
    }

}
