package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.view.BizInfoSummaryView;
import org.joda.time.DateTime;

public class BizInfoSummaryTransform extends Transform {

    public BizInfoSummary transformToModel(BizInfoSummaryView bizInfoSummaryView) {
        BizInfoSummary bizInfoSummary;

        bizInfoSummary = new BizInfoSummary();

        if (bizInfoSummaryView.getId() != 0) {
            bizInfoSummary.setId(bizInfoSummaryView.getId());
        } else if (bizInfoSummaryView.getId() == 0) {
            bizInfoSummary.setCreateBy(bizInfoSummaryView.getCreateBy());
            bizInfoSummary.setCreateDate(DateTime.now().toDate());
        }

        bizInfoSummary.setBizLocationName(bizInfoSummaryView.getBizLocationName());
        bizInfoSummary.setRental(bizInfoSummaryView.getRental());
        bizInfoSummary.setOwnerName(bizInfoSummaryView.getOwnerName());
        bizInfoSummary.setExpiryDate(bizInfoSummaryView.getExpiryDate());

        bizInfoSummary.setAddressNo(bizInfoSummaryView.getAddressNo());
        bizInfoSummary.setAddressMoo(bizInfoSummaryView.getAddressMoo());
        bizInfoSummary.setAddressBuilding(bizInfoSummaryView.getAddressBuilding());
        bizInfoSummary.setAddressStreet(bizInfoSummaryView.getAddressStreet());

        bizInfoSummary.setProvince(bizInfoSummaryView.getSubDistrict().getDistrict().getProvince());
        bizInfoSummary.setDistrict(bizInfoSummaryView.getSubDistrict().getDistrict());
        bizInfoSummary.setSubDistrict(bizInfoSummaryView.getSubDistrict());
        bizInfoSummary.setCountry(bizInfoSummaryView.getCountry());

        bizInfoSummary.setPostCode(bizInfoSummaryView.getPostCode());
        bizInfoSummary.setPhoneNo(bizInfoSummaryView.getPhoneNo());
        bizInfoSummary.setExtension(bizInfoSummaryView.getExtension());

        bizInfoSummary.setRegistrationDate(bizInfoSummaryView.getRegistrationDate());
        bizInfoSummary.setEstablishDate(bizInfoSummaryView.getEstablishDate());
        bizInfoSummary.setReferredExperience(bizInfoSummaryView.getReferredExperience());

        bizInfoSummary.setCirculationAmount(bizInfoSummaryView.getCirculationAmount());
        bizInfoSummary.setCirculationPercentage(bizInfoSummaryView.getCirculationPercentage());

        bizInfoSummary.setProductionCostsAmount(bizInfoSummaryView.getProductionCostsAmount());
        bizInfoSummary.setProductionCostsPercentage(bizInfoSummaryView.getProductionCostsPercentage());

        bizInfoSummary.setProfitMarginAmount(bizInfoSummaryView.getProfitMarginAmount());
        bizInfoSummary.setProfitMarginPercentage(bizInfoSummaryView.getProfitMarginPercentage());

        bizInfoSummary.setOperatingExpenseAmount(bizInfoSummaryView.getOperatingExpenseAmount());
        bizInfoSummary.setOperatingExpensePercentage(bizInfoSummaryView.getOperatingExpensePercentage());

        bizInfoSummary.setReduceTaxAmount(bizInfoSummaryView.getReduceTaxAmount());
        bizInfoSummary.setReduceTaxPercentage(bizInfoSummaryView.getReduceTaxPercentage());

        bizInfoSummary.setReduceInterestAmount(bizInfoSummaryView.getReduceInterestAmount());
        bizInfoSummary.setReduceInterestPercentage(bizInfoSummaryView.getReduceInterestAmount());


        bizInfoSummary.setEarningsBeforeTaxAmount(bizInfoSummaryView.getEarningsBeforeTaxAmount());
        bizInfoSummary.setEarningsBeforeTaxPercentage(bizInfoSummaryView.getEarningsBeforeTaxPercentage());

        bizInfoSummary.setNetMarginAmount(bizInfoSummaryView.getNetMarginAmount());
        bizInfoSummary.setNetMarginPercentage(bizInfoSummaryView.getNetMarginPercentage());

        bizInfoSummary.setNetFixAsset(bizInfoSummaryView.getNetFixAsset());
        bizInfoSummary.setNoOfEmployee(bizInfoSummaryView.getNoOfEmployee());

        bizInfoSummary.setSumIncomeAmount(bizInfoSummaryView.getSumIncomeAmount());
        bizInfoSummary.setSumIncomePercent(bizInfoSummaryView.getSumIncomePercent());
        bizInfoSummary.setSumWeightAR(bizInfoSummaryView.getSumWeightAR());
        bizInfoSummary.setSumWeightAP(bizInfoSummaryView.getSumWeightAP());
        bizInfoSummary.setSumWeightINV(bizInfoSummaryView.getSumWeightINV());
        bizInfoSummary.setSumWeightInterviewedIncomeFactorPercent(bizInfoSummaryView.getSumWeightInterviewedIncomeFactorPercent());


        bizInfoSummary.setModifyBy(bizInfoSummaryView.getModifyBy());
        bizInfoSummary.setModifyDate(DateTime.now().toDate());


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

        bizInfoSummaryView.setProvince(bizInfoSummary.getSubDistrict().getDistrict().getProvince());
        bizInfoSummaryView.setDistrict(bizInfoSummary.getSubDistrict().getDistrict());
        bizInfoSummaryView.setSubDistrict(bizInfoSummary.getSubDistrict());
        bizInfoSummaryView.setCountry(bizInfoSummary.getCountry());

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
