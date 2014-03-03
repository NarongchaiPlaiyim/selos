package com.clevel.selos.transform;

import com.clevel.selos.dao.master.SpecialProgramDAO;
import com.clevel.selos.model.db.master.SpecialProgram;
import com.clevel.selos.model.view.SpecialProgramView;

import javax.inject.Inject;

public class SpecialProgramTransform extends Transform {
    @Inject
    private SpecialProgramDAO specialProgramDAO;

    @Inject
    public SpecialProgramTransform() {
    }

    public SpecialProgram transformToModel(SpecialProgramView specialProgramView) {
        if (specialProgramView == null) {
            return null;
        }

        SpecialProgram specialProgram;
        if (specialProgramView.getId() != 0) {
            specialProgram = specialProgramDAO.findById(specialProgramView.getId());
        } else {
            specialProgram = new SpecialProgram();
        }
        specialProgram.setActive(specialProgramView.getActive());
        specialProgram.setCode(specialProgramView.getCode());
        return specialProgram;
    }

    public SpecialProgramView transformToView(SpecialProgram specialProgram) {
        SpecialProgramView specialProgramView = new SpecialProgramView();
        if (specialProgram == null) {
            return specialProgramView;
        }
        specialProgramView.setId(specialProgram.getId());
        specialProgramView.setActive(specialProgram.getActive());
        specialProgramView.setCode(specialProgram.getCode());
        return specialProgramView;
    }
}
