package com.clevel.selos.transform;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.PrescreenDAO;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.PrescreenResultView;
import com.clevel.selos.model.view.PrescreenView;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;

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
    PrescreenDAO prescreenDAO;


    public Prescreen transformToModel(PrescreenView prescreenView, WorkCasePrescreen workCasePrescreen, User user){
        Prescreen prescreen = new Prescreen();

        if(prescreenView.getId() != 0){
            prescreen.setId(prescreenView.getId());
            prescreen.setCreateDate(prescreenView.getCreateDate());
            prescreen.setCreateBy(prescreenView.getCreateBy());
        } else {
            prescreen.setCreateDate(new DateTime().now().toDate());
            prescreen.setCreateBy(user);
        }
        prescreen.setWorkCasePrescreen(workCasePrescreen);
        if(prescreenView.getProductGroup() != null && prescreenView.getProductGroup().getId() != 0){
            prescreen.setProductGroup(productGroupDAO.findById(prescreenView.getProductGroup().getId()));
        } else {
            prescreen.setProductGroup(null);
        }
        prescreen.setExpectedSubmitDate(prescreenView.getExpectedSubmitDate());
        if(prescreenView.getBusinessLocation() != null && prescreenView.getBusinessLocation().getCode() != 0){
            prescreen.setBusinessLocation(provinceDAO.findById(prescreenView.getBusinessLocation().getCode()));
        } else {
            prescreen.setBusinessLocation(null);
        }
        prescreen.setRegisterDate(prescreenView.getRegisterDate());
        prescreen.setReferredDate(prescreenView.getReferDate());
        if(prescreenView.getReferredExperience() != null && prescreenView.getReferredExperience().getId() != 0){
            prescreen.setReferredExperience(referredExperienceDAO.findById(prescreenView.getReferredExperience().getId()));
        } else {
            prescreen.setReferredExperience(null);
        }
        prescreen.setRefinance(prescreenView.isRefinance());
        if(prescreenView.getRefinanceBank() != null && prescreenView.getRefinanceBank().getCode() != 0){
            prescreen.setRefinanceBank(bankDAO.findById(prescreenView.getRefinanceBank().getCode()));
        } else {
            prescreen.setRefinanceBank(null);
        }
        prescreen.setBorrowingType(prescreenView.getBorrowingType());
        if(prescreenView.getBorrowingType() != null && prescreenView.getBorrowingType().getId() != 0){
            prescreen.setBorrowingType(borrowingTypeDAO.findById(prescreenView.getBorrowingType().getId()));
        } else {
            prescreen.setBorrowingType(null);
        }
        prescreen.setModifyDate(new DateTime().now().toDate());
        prescreen.setModifyBy(user);
        return prescreen;
    }

    public PrescreenView transformToView(Prescreen prescreen){
        PrescreenView prescreenView = new PrescreenView();

        prescreenView.setId(prescreen.getId());
        prescreenView.setProductGroup(prescreen.getProductGroup());
        if(prescreenView.getProductGroup() == null){
            prescreenView.setProductGroup(new ProductGroup());
        }
        prescreenView.setExpectedSubmitDate(prescreen.getExpectedSubmitDate());
        prescreenView.setBusinessLocation(prescreen.getBusinessLocation());
        if(prescreenView.getBusinessLocation() == null){
            prescreenView.setBusinessLocation(new Province());
        }
        prescreenView.setRegisterDate(prescreen.getRegisterDate());
        prescreenView.setReferDate(prescreen.getReferredDate());
        prescreenView.setReferredExperience(prescreen.getReferredExperience());
        if(prescreenView.getReferredExperience() == null){
            prescreenView.setReferredExperience(new ReferredExperience());
        }
        prescreenView.setRefinance(prescreen.isRefinance());
        prescreenView.setRefinanceBank(prescreen.getRefinanceBank());
        if(prescreenView.getRefinanceBank() == null){
            prescreenView.setRefinanceBank(new Bank());
        }
        prescreenView.setBorrowingType(prescreen.getBorrowingType());
        if(prescreenView.getBorrowingType() == null){
            prescreenView.setBorrowingType(new BorrowingType());
        }

        prescreenView.setCreateDate(prescreen.getCreateDate());
        prescreenView.setCreateBy(prescreen.getCreateBy());
        prescreenView.setModifyDate(prescreen.getModifyDate());
        prescreenView.setModifyBy(prescreen.getModifyBy());

        return prescreenView;
    }

    public PrescreenResultView getPrescreenResultView(Prescreen prescreen){
        PrescreenResultView prescreenResultView = new PrescreenResultView();
        prescreenResultView.setId(prescreen.getId());
        prescreenResultView.setBorrowingType(prescreen.getBorrowingType());
        prescreenResultView.setExpectedSubmitDate(prescreen.getExpectedSubmitDate());
        prescreenResultView.setGroupExposure(prescreen.getGroupExposure());
        prescreenResultView.setGroupIncome(prescreen.getGroupIncome());
        return prescreenResultView;
    }

    public Prescreen getPrescreen(PrescreenResultView prescreenResultView, User user){
        Prescreen prescreen = null;
        if(prescreenResultView.getId() != 0) {
            prescreen = prescreenDAO.findById(prescreenResultView.getId());
            prescreen.setGroupExposure(prescreenResultView.getGroupExposure());
            prescreen.setGroupIncome(prescreenResultView.getGroupIncome());
            prescreen.setModifyBy(user);
            prescreen.setModifyDate(new Date());
        }
        return prescreen;
    }
}
