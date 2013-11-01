package com.clevel.selos.transform;

import com.clevel.selos.dao.master.PotentialCollateralDAO;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.PrescreenCollateral;
import com.clevel.selos.model.view.PrescreenCollateralView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PrescreenCollateralTransform extends Transform {
    @Inject
    PotentialCollateralDAO potentialCollateralDAO;

    @Inject
    public PrescreenCollateralTransform(){

    }

    public PrescreenCollateral transformToModel(PrescreenCollateralView prescreenCollateralView, Prescreen prescreen){
        PrescreenCollateral prescreenCollateral = new PrescreenCollateral();

        prescreenCollateral.setPrescreen(prescreen);
        PotentialCollateral potentialCollateral = potentialCollateralDAO.findById(prescreenCollateralView.getPotentialCollateral().getId());
        prescreenCollateral.setPotentialCollateral(potentialCollateral);

        return prescreenCollateral;
    }

    public List<PrescreenCollateral> transformToModelList(List<PrescreenCollateralView> prescreenCollateralViews, Prescreen prescreen){
        List<PrescreenCollateral> prescreenCollateralList = new ArrayList<PrescreenCollateral>();

        for(PrescreenCollateralView prescreenCollateralView : prescreenCollateralViews){
            PrescreenCollateral prescreenCollateral = new PrescreenCollateral();
            prescreenCollateral = transformToModel(prescreenCollateralView, prescreen);
            prescreenCollateralList.add(prescreenCollateral);
        }

        return prescreenCollateralList;
    }

    public PrescreenCollateralView transformToView(PrescreenCollateral prescreenCollateral){
        PrescreenCollateralView prescreenCollateralView = new PrescreenCollateralView();
        prescreenCollateralView.setId(prescreenCollateral.getId());
        prescreenCollateralView.setPotentialCollateral(prescreenCollateral.getPotentialCollateral());

        return prescreenCollateralView;
    }

    public List<PrescreenCollateralView> transformToViewList(List<PrescreenCollateral> prescreenCollaterals){
        List<PrescreenCollateralView> prescreenCollateralViewList = new ArrayList<PrescreenCollateralView>();

        for(PrescreenCollateral prescreenCollateral : prescreenCollaterals){
            PrescreenCollateralView prescreenCollateralView = new PrescreenCollateralView();
            prescreenCollateralView = transformToView(prescreenCollateral);
            prescreenCollateralViewList.add(prescreenCollateralView);
        }

        return prescreenCollateralViewList;
    }
}
