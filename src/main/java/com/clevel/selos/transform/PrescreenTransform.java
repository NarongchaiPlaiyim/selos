package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.PrescreenView;

public class PrescreenTransform extends Transform {

    public Prescreen transform(PrescreenView prescreenView, WorkCasePrescreen workCasePrescreen){
        Prescreen prescreen = new Prescreen();

        if(prescreenView.getId() != 0){
            prescreen.setId(prescreenView.getId());
        }
        prescreen.setWorkCasePrescreen(workCasePrescreen);
        prescreen.setExpectedSubmitDate(prescreenView.getExpectedSubmitDate());
        prescreen.setBusinessLocation(prescreenView.getBusinessLocation());
        prescreen.setRegisterDate(prescreenView.getRegisterDate());
        prescreen.setRefinance(prescreenView.isRefinance());
        prescreen.setCreateDate(prescreenView.getCreateDate());
        prescreen.setCreateBy(prescreenView.getCreateBy());
        prescreen.setModifyDate(prescreenView.getModifyDate());
        prescreen.setModifyBy(prescreenView.getModifyBy());

        return prescreen;
    }

    public PrescreenView transform(Prescreen prescreen){
        PrescreenView prescreenView = new PrescreenView();

        prescreenView.setId(prescreen.getId());
        prescreenView.setExpectedSubmitDate(prescreen.getExpectedSubmitDate());
        prescreenView.setBusinessLocation(prescreen.getBusinessLocation());
        prescreenView.setRegisterDate(prescreen.getRegisterDate());
        prescreenView.setRefinance(prescreen.isRefinance());
        prescreenView.setCreateDate(prescreen.getCreateDate());
        prescreenView.setCreateBy(prescreen.getCreateBy());
        prescreenView.setModifyDate(prescreen.getModifyDate());
        prescreenView.setModifyBy(prescreen.getModifyBy());

        return prescreenView;
    }
}
