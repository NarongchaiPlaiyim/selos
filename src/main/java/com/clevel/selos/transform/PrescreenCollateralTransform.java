package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.db.working.PrescreenCollateral;
import com.clevel.selos.model.view.CollateralView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PrescreenCollateralTransform extends Transform {
    @Inject
    public PrescreenCollateralTransform(){

    }

    public PrescreenCollateral transformToModel(CollateralView collateralView, Prescreen prescreen){
        PrescreenCollateral prescreenCollateral = new PrescreenCollateral();

        prescreenCollateral.setPrescreen(prescreen);
        prescreenCollateral.setCollateralType(collateralView.getCollateralType());

        return prescreenCollateral;
    }

    public List<PrescreenCollateral> transformToModelList(List<CollateralView> collateralViews, Prescreen prescreen){
        List<PrescreenCollateral> prescreenCollateralList = new ArrayList<PrescreenCollateral>();

        for(CollateralView collateralView : collateralViews){
            PrescreenCollateral prescreenCollateral = new PrescreenCollateral();
            prescreenCollateral = transformToModel(collateralView, prescreen);
            prescreenCollateralList.add(prescreenCollateral);
        }

        return prescreenCollateralList;
    }
}
