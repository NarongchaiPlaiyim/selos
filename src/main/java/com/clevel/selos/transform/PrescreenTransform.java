package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
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
        prescreen.setRefinance(prescreenView.isRefinance());
        prescreen.setModifyDate(new DateTime().now().toDate());
        prescreen.setModifyBy(user);
        return prescreen;
    }

    public PrescreenView transformToView(Prescreen prescreen){
        PrescreenView prescreenView = new PrescreenView();

        prescreenView.setId(prescreen.getId());
        prescreenView.setProductGroup(prescreen.getProductGroup());
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
