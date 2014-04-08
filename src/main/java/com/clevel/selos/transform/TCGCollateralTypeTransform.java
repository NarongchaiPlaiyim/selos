package com.clevel.selos.transform;

import com.clevel.selos.dao.master.TCGCollateralTypeDAO;
import com.clevel.selos.model.db.master.TCGCollateralType;
import com.clevel.selos.model.view.TCGCollateralTypeView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TCGCollateralTypeTransform extends Transform {
    @Inject
    private TCGCollateralTypeDAO tcgCollateralTypeDAO;

    @Inject
    public TCGCollateralTypeTransform() {
    }

    public TCGCollateralType transformToModel(TCGCollateralTypeView tcgCollateralTypeView) {
        if (tcgCollateralTypeView == null) {
            return null;
        }

        TCGCollateralType tcgCollateralType;
        if (tcgCollateralTypeView.getId() != 0) {
            tcgCollateralType = tcgCollateralTypeDAO.findById(tcgCollateralTypeView.getId());
        } else {
            tcgCollateralType = new TCGCollateralType();
        }
        tcgCollateralType.setName(tcgCollateralTypeView.getName());
        tcgCollateralType.setDescription(tcgCollateralTypeView.getDescription());
        return tcgCollateralType;
    }

    public List<TCGCollateralType> transformToModel(List<TCGCollateralTypeView> tcgCollateralTypeViewList) {
        List<TCGCollateralType> tcgCollateralTypeList = new ArrayList<TCGCollateralType>();
        if (tcgCollateralTypeViewList == null) {
            return tcgCollateralTypeList;
        }
        for (TCGCollateralTypeView tcgCollateralTypeView : tcgCollateralTypeViewList) {
            tcgCollateralTypeList.add(transformToModel(tcgCollateralTypeView));
        }
        return tcgCollateralTypeList;
    }

    public TCGCollateralTypeView transformToView(TCGCollateralType tcgCollateralType) {
        TCGCollateralTypeView tcgCollateralTypeView = new TCGCollateralTypeView();
        if (tcgCollateralType == null) {
            return tcgCollateralTypeView;
        }
        tcgCollateralTypeView.setId(tcgCollateralType.getId());
        tcgCollateralTypeView.setName(tcgCollateralType.getName());
        tcgCollateralTypeView.setDescription(tcgCollateralType.getDescription());
        return tcgCollateralTypeView;
    }

    public List<TCGCollateralTypeView> transformToView(List<TCGCollateralType> tcgCollateralTypeList) {
        List<TCGCollateralTypeView> tcgCollateralTypeViewList = new ArrayList<TCGCollateralTypeView>();
        if (tcgCollateralTypeList == null) {
            return tcgCollateralTypeViewList;
        }
        for (TCGCollateralType collateralType : tcgCollateralTypeList) {
            tcgCollateralTypeViewList.add(transformToView(collateralType));
        }
        return tcgCollateralTypeViewList;
    }
}
