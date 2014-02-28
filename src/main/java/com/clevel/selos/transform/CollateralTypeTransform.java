package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.view.CollateralTypeView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CollateralTypeTransform extends Transform {
    @Inject
    private CollateralTypeDAO collateralTypeDAO;

    @Inject
    public CollateralTypeTransform() {
    }

    public CollateralType transformToModel(CollateralTypeView collateralTypeView) {
        if (collateralTypeView == null) {
            return null;
        }

        CollateralType collateralType;
        if (collateralTypeView.getId() != 0) {
            collateralType = collateralTypeDAO.findById(collateralTypeView.getId());
        } else {
            collateralType = new CollateralType();
        }
        collateralType.setCode(collateralTypeView.getCode());
        collateralType.setDescription(collateralTypeView.getDescription());
        return collateralType;
    }

    public List<CollateralType> transformToModel(List<CollateralTypeView> collateralTypeViewList) {
        List<CollateralType> collateralTypeList = new ArrayList<CollateralType>();
        if (collateralTypeViewList == null) {
            return collateralTypeList;
        }
        for (CollateralTypeView collateralTypeView : collateralTypeViewList) {
            collateralTypeList.add(transformToModel(collateralTypeView));
        }
        return collateralTypeList;
    }

    public CollateralTypeView transformToView(CollateralType collateralType) {
        CollateralTypeView collateralTypeView = new CollateralTypeView();
        if (collateralType == null) {
            return collateralTypeView;
        }
        collateralTypeView.setId(collateralType.getId());
        collateralTypeView.setCode(collateralType.getCode());
        collateralTypeView.setDescription(collateralType.getDescription());
        return collateralTypeView;
    }

    public List<CollateralTypeView> transformToView(List<CollateralType> collateralTypeList) {
        List<CollateralTypeView> collateralTypeViewList = new ArrayList<CollateralTypeView>();
        if (collateralTypeList == null) {
            return collateralTypeViewList;
        }
        for (CollateralType collateralType : collateralTypeList) {
            collateralTypeViewList.add(transformToView(collateralType));
        }
        return collateralTypeViewList;
    }
}
