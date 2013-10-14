package com.clevel.selos.transform.business;

import com.clevel.selos.model.db.working.BizInfoSummary;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.transform.Transform;

public class BizInfoSummaryTransform extends Transform {

    public BizInfoSummary transformToModel(BizInfoSummaryView bizInfoSummaryView){
        BizInfoSummary bizInfoSummary;

        bizInfoSummary = new BizInfoSummary();

        if(bizInfoSummaryView.getId() != 0 ){
            bizInfoSummary.setId(bizInfoSummaryView.getId());
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

        bizInfoSummary.setPostCode(bizInfoSummaryView.getPostCode());
        bizInfoSummary.setPhoneNo(bizInfoSummaryView.getPhoneNo());
        bizInfoSummary.setExtension(bizInfoSummaryView.getExtension());

        bizInfoSummary.setRegistrationDate(bizInfoSummaryView.getRegistrationDate());
        bizInfoSummary.setEstablishDate(bizInfoSummaryView.getEstablishDate());

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

        return bizInfoSummary;
    }

    public BizInfoSummaryView transformToView(BizInfoSummary bizInfoSummary){
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

        bizInfoSummaryView.setPostCode(bizInfoSummary.getPostCode());
        bizInfoSummaryView.setPhoneNo(bizInfoSummary.getPhoneNo());
        bizInfoSummaryView.setExtension(bizInfoSummary.getExtension());

        bizInfoSummaryView.setRegistrationDate(bizInfoSummary.getRegistrationDate());
        bizInfoSummaryView.setEstablishDate(bizInfoSummary.getEstablishDate());

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

        bizInfoSummaryView.setNetFixAsset(bizInfoSummary.getNetFixAsset());

        bizInfoSummaryView.setNoOfEmployee(bizInfoSummary.getNoOfEmployee());

        bizInfoSummaryView.setSubDistrict(bizInfoSummary.getSubDistrict());

        return bizInfoSummaryView;
    }

}
