package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.SpecialProgramDAO;
import com.clevel.selos.model.db.master.SpecialProgram;
import com.clevel.selos.model.view.master.SpecialProgramView;
import com.clevel.selos.transform.Transform;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public Map<Integer, SpecialProgramView> transformToCache(List<SpecialProgram> specialProgramList){
        if(specialProgramList == null || specialProgramList.size() == 0)
            return null;
        Map<Integer, SpecialProgramView> _tmpMap = new ConcurrentHashMap<Integer, SpecialProgramView>();
        for(SpecialProgram specialProgram : specialProgramList){
            SpecialProgramView specialProgramView = transformToView(specialProgram);
            _tmpMap.put(specialProgramView.getId(), specialProgramView);
        }
        return _tmpMap;
    }
}
