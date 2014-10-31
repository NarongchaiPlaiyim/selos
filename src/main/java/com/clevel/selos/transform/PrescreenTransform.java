package com.clevel.selos.transform;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.PrescreenDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.PrescreenResultView;
import com.clevel.selos.model.view.PrescreenView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PrescreenTransform extends Transform {

    @Inject
    ProductGroupDAO productGroupDAO;
    @Inject
    ProvinceDAO provinceDAO;
    @Inject
    ReferredExperienceDAO referredExperienceDAO;
    @Inject
    BankDAO bankDAO;
    @Inject
    BorrowingTypeDAO borrowingTypeDAO;
    @Inject
    CountryDAO countryDAO;

    @Inject
    PrescreenDAO prescreenDAO;

    @Inject
    @SELOS
    Logger log;


    public Prescreen transformToModel(PrescreenView prescreenView, WorkCasePrescreen workCasePrescreen, User user) {
        Prescreen prescreen = new Prescreen();

        if (prescreenView.getId() != 0) {
            prescreen.setId(prescreenView.getId());
            prescreen.setCreateDate(prescreenView.getCreateDate());
            prescreen.setCreateBy(prescreenView.getCreateBy());
        } else {
            prescreen.setCreateDate(DateTime.now().toDate());
            prescreen.setCreateBy(user);
        }
        prescreen.setWorkCasePrescreen(workCasePrescreen);
        if (prescreenView.getProductGroup() != null && prescreenView.getProductGroup().getId() != 0) {
            prescreen.setProductGroup(productGroupDAO.findById(prescreenView.getProductGroup().getId()));
        } else {
            prescreen.setProductGroup(null);
        }
        prescreen.setExpectedSubmitDate(prescreenView.getExpectedSubmitDate());
        if (prescreenView.getBusinessLocation() != null && prescreenView.getBusinessLocation().getCode() != 0) {
            prescreen.setBusinessLocation(provinceDAO.findById(prescreenView.getBusinessLocation().getCode()));
        } else {
            prescreen.setBusinessLocation(null);
        }
        prescreen.setRegisterDate(prescreenView.getRegisterDate());
        prescreen.setReferredDate(prescreenView.getReferDate());
        if (prescreenView.getReferredExperience() != null && prescreenView.getReferredExperience().getId() != 0) {
            prescreen.setReferredExperience(referredExperienceDAO.findById(prescreenView.getReferredExperience().getId()));
        } else {
            prescreen.setReferredExperience(null);
        }

        prescreen.setTcg(prescreenView.getTcg());

        prescreen.setRefinanceIN(prescreenView.getRefinanceIn());
        if(prescreenView.getRefinanceInBank() != null && prescreenView.getRefinanceInBank().getCode() != 0){
            prescreen.setRefinanceInValue(bankDAO.findById(prescreenView.getRefinanceInBank().getCode()));
        } else {
            prescreen.setRefinanceInValue(null);
        }

        prescreen.setRefinanceOUT(prescreenView.getRefinanceOut());
        if(prescreenView.getRefinanceOutBank() != null && prescreenView.getRefinanceOutBank().getCode() != 0){
            prescreen.setRefinanceOutValue(bankDAO.findById(prescreenView.getRefinanceOutBank().getCode()));
        } else {
            prescreen.setRefinanceOutValue(null);
        }

        prescreen.setBorrowingType(prescreenView.getBorrowingType());
        if (prescreenView.getBorrowingType() != null && prescreenView.getBorrowingType().getId() != 0) {
            prescreen.setBorrowingType(borrowingTypeDAO.findById(prescreenView.getBorrowingType().getId()));
        } else {
            prescreen.setBorrowingType(null);
        }
        prescreen.setModifyDate(DateTime.now().toDate());
        prescreen.setModifyBy(user);
        prescreen.setModifyFlag(prescreenView.getModifyFlag());

        if (prescreenView.getCountryOfRegister() != null && prescreenView.getCountryOfRegister().getId() != 0){
            prescreen.setCountryOfRegister(countryDAO.findById(prescreenView.getCountryOfRegister().getId()));
        } else {
            prescreen.setCountryOfRegister(null);
        }

        if(!Util.isNull(prescreenView.getGroupExposure())){
            prescreen.setGroupExposure(prescreenView.getGroupExposure());
        }

        if(!Util.isNull(prescreenView.getGroupIncome())){
            prescreen.setGroupIncome(prescreenView.getGroupIncome());
        }


        return prescreen;
    }

    public PrescreenView transformToView(Prescreen prescreen) {
        PrescreenView prescreenView = new PrescreenView();

        prescreenView.setId(prescreen.getId());
        prescreenView.setProductGroup(prescreen.getProductGroup());
        if (prescreenView.getProductGroup() == null) {
            prescreenView.setProductGroup(new ProductGroup());
        }
        prescreenView.setExpectedSubmitDate(prescreen.getExpectedSubmitDate());
        prescreenView.setBusinessLocation(prescreen.getBusinessLocation());
        if (prescreenView.getBusinessLocation() == null) {
            prescreenView.setBusinessLocation(new Province());
        }
        prescreenView.setRegisterDate(prescreen.getRegisterDate());
        prescreenView.setReferDate(prescreen.getReferredDate());
        prescreenView.setReferredExperience(prescreen.getReferredExperience());
        if (prescreenView.getReferredExperience() == null) {
            prescreenView.setReferredExperience(new ReferredExperience());
        }

        prescreenView.setTcg(prescreen.getTcg());

        prescreenView.setRefinanceIn(prescreen.getRefinanceIN());
        prescreenView.setRefinanceInBank(prescreen.getRefinanceInValue());
        if (prescreen.getRefinanceInValue() == null){
            prescreenView.setRefinanceInBank(new Bank());
        }

        prescreenView.setRefinanceOut(prescreen.getRefinanceOUT());
        prescreenView.setRefinanceOutBank(prescreen.getRefinanceOutValue());
        if (prescreen.getRefinanceOutValue() == null){
            prescreenView.setRefinanceOutBank(new Bank());
        }

        prescreenView.setBorrowingType(prescreen.getBorrowingType());
        if (prescreenView.getBorrowingType() == null) {
            prescreenView.setBorrowingType(new BorrowingType());
        }

        prescreenView.setGroupExposure(prescreen.getGroupExposure());
        prescreenView.setGroupIncome(prescreen.getGroupIncome());
        prescreenView.setCreateDate(prescreen.getCreateDate());
        prescreenView.setCreateBy(prescreen.getCreateBy());
        prescreenView.setModifyDate(prescreen.getModifyDate());
        prescreenView.setModifyBy(prescreen.getModifyBy());
        prescreenView.setModifyFlag(prescreen.getModifyFlag());

        prescreenView.setCountryOfRegister(prescreen.getCountryOfRegister());
        if(prescreen.getCountryOfRegister() == null){
            prescreenView.setCountryOfRegister(new Country());
        }

        return prescreenView;
    }

    public PrescreenResultView getPrescreenResultView(Prescreen prescreen) {
        PrescreenResultView prescreenResultView = new PrescreenResultView();
        prescreenResultView.setId(prescreen.getId());
        prescreenResultView.setBorrowingType(prescreen.getBorrowingType());
        prescreenResultView.setExpectedSubmitDate(prescreen.getExpectedSubmitDate());
        prescreenResultView.setGroupExposure(prescreen.getGroupExposure());
        prescreenResultView.setGroupIncome(prescreen.getGroupIncome());
        return prescreenResultView;
    }

    public Prescreen getPrescreen(PrescreenResultView prescreenResultView, User user) {
        Prescreen prescreen = null;
        if (prescreenResultView.getId() != 0) {
            prescreen = prescreenDAO.findById(prescreenResultView.getId());
            log.debug("preScreenResultView.getGroupExposure() : {}", prescreenResultView.getGroupExposure());
            log.debug("preScreenResultView.getGroupIncome() : {}", prescreenResultView.getGroupIncome());
            prescreen.setGroupExposure(prescreenResultView.getGroupExposure());
            prescreen.setGroupIncome(prescreenResultView.getGroupIncome());
            prescreen.setModifyBy(user);
            prescreen.setModifyDate(DateTime.now().toDate());
        }
        return prescreen;
    }
}
