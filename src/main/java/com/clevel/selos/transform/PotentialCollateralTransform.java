package com.clevel.selos.transform;

import com.clevel.selos.dao.master.PotentialCollateralDAO;
import com.clevel.selos.model.db.master.PotentialCollateral;
import com.clevel.selos.model.view.PotentialCollateralView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PotentialCollateralTransform extends Transform {
    @Inject
    private PotentialCollateralDAO potentialCollateralDAO;

    @Inject
    public PotentialCollateralTransform() {
    }

    public PotentialCollateral transformToModel(PotentialCollateralView potentialCollateralView) {
        if (potentialCollateralView == null) {
            return null;
        }

        PotentialCollateral potentialCollateral;
        if (potentialCollateralView.getId() != 0) {
            potentialCollateral = potentialCollateralDAO.findById(potentialCollateralView.getId());
        } else {
            potentialCollateral = new PotentialCollateral();
        }
        potentialCollateral.setName(potentialCollateralView.getName());
        potentialCollateral.setDescription(potentialCollateralView.getDescription());
        return potentialCollateral;
    }

    public List<PotentialCollateral> transformToModel(List<PotentialCollateralView> potentialCollateralViewList) {
        List<PotentialCollateral> potentialCollateralList = new ArrayList<PotentialCollateral>();
        if (potentialCollateralViewList == null) {
            return potentialCollateralList;
        }

        for (PotentialCollateralView potentialCollateralView : potentialCollateralViewList) {
            potentialCollateralList.add(transformToModel(potentialCollateralView));
        }
        return potentialCollateralList;
    }

    public PotentialCollateralView transformToView(PotentialCollateral potentialCollateral) {
        PotentialCollateralView potentialCollateralView = new PotentialCollateralView();
        if (potentialCollateral == null) {
            return potentialCollateralView;
        }
        potentialCollateralView.setId(potentialCollateral.getId());
        potentialCollateralView.setName(potentialCollateral.getName());
        potentialCollateralView.setDescription(potentialCollateral.getDescription());
        return potentialCollateralView;
    }

    public List<PotentialCollateralView> transformToView(List<PotentialCollateral> potentialCollateralList) {
        List<PotentialCollateralView> potentialCollateralViewList = new ArrayList<PotentialCollateralView>();
        if (potentialCollateralList == null) {
            return potentialCollateralViewList;
        }

        for (PotentialCollateral potentialCollateral : potentialCollateralList) {
            potentialCollateralViewList.add(transformToView(potentialCollateral));
        }
        return potentialCollateralViewList;
    }
}
