package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.PrescreenFacility;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.FacilityView;
import com.clevel.selos.model.view.PrescreenView;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class PrescreenTransform extends Transform {

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
        prescreen.setProductGroup(prescreenView.getProductGroup());
        prescreen.setExpectedSubmitDate(prescreenView.getExpectedSubmitDate());
        prescreen.setBusinessLocation(prescreenView.getBusinessLocation());
        prescreen.setRegisterDate(prescreenView.getRegisterDate());
        prescreen.setReferredDate(prescreenView.getReferDate());
        prescreen.setReferredExperience(prescreenView.getReferredExperience());
        prescreen.setRefinance(prescreenView.isRefinance());
        prescreen.setRefinanceBank(prescreenView.getRefinanceBank());
        prescreen.setBorrowingType(prescreenView.getBorrowingType());
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


}
