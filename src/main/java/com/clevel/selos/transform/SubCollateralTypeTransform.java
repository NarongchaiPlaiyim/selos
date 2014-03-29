package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CollateralTypeDAO;
import com.clevel.selos.dao.master.SubCollateralTypeDAO;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.view.SubCollateralTypeView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class SubCollateralTypeTransform extends Transform {
    @Inject
    private SubCollateralTypeDAO subCollateralTypeDAO;
    @Inject
    private CollateralTypeDAO collateralTypeDAO;
    @Inject
    private CollateralTypeTransform collateralTypeTransform;

    @Inject
    public SubCollateralTypeTransform() {
    }

    public SubCollateralType transformToModel(SubCollateralTypeView subCollateralTypeView) {
        if (subCollateralTypeView == null) {
            return null;
        }

        SubCollateralType subCollateralType;
        if (subCollateralTypeView.getId() != 0) {
            subCollateralType = subCollateralTypeDAO.findById(subCollateralTypeView.getId());
        } else {
            subCollateralType = new SubCollateralType();
        }

        if (subCollateralTypeView.getCollateralTypeView() != null && subCollateralTypeView.getCollateralTypeView().getId() != 0) {
            subCollateralType.setCollateralType(collateralTypeDAO.findById(subCollateralTypeView.getCollateralTypeView().getId()));
        } else {
            subCollateralType.setCollateralType(null);
        }

        subCollateralType.setActive(subCollateralTypeView.getActive());
        subCollateralType.setCode(subCollateralTypeView.getCode());
        subCollateralType.setDescription(subCollateralTypeView.getDescription());
        subCollateralType.setDefaultType(subCollateralTypeView.getDefaultType());
        return subCollateralType;
    }

    public List<SubCollateralType> transformToModel(List<SubCollateralTypeView> subCollateralTypeViewList) {
        List<SubCollateralType> subCollateralTypeList = new ArrayList<SubCollateralType>();
        if (subCollateralTypeViewList == null) {
            return subCollateralTypeList;
        }
        for (SubCollateralTypeView subCollateralTypeView : subCollateralTypeViewList) {
            subCollateralTypeList.add(transformToModel(subCollateralTypeView));
        }
        return subCollateralTypeList;
    }

    public SubCollateralTypeView transformToView(SubCollateralType subCollateralType) {
        SubCollateralTypeView subCollateralTypeView = new SubCollateralTypeView();
        if (subCollateralType == null) {
            return subCollateralTypeView;
        }
        subCollateralTypeView.setId(subCollateralType.getId());
        subCollateralTypeView.setActive(subCollateralType.getActive());
        subCollateralTypeView.setCode(subCollateralType.getCode());
        subCollateralTypeView.setDescription(subCollateralType.getDescription());
        subCollateralTypeView.setDefaultType(subCollateralType.getDefaultType());
        subCollateralTypeView.setCollateralTypeView(collateralTypeTransform.transformToView(subCollateralType.getCollateralType()));
        return subCollateralTypeView;
    }

    public List<SubCollateralTypeView> transformToView(List<SubCollateralType> subCollateralTypeList) {
        List<SubCollateralTypeView> subCollateralTypeViewList = new ArrayList<SubCollateralTypeView>();
        if (subCollateralTypeList == null) {
            return subCollateralTypeViewList;
        }
        for (SubCollateralType subCollateralType : subCollateralTypeList) {
            subCollateralTypeViewList.add(transformToView(subCollateralType));
        }
        return subCollateralTypeViewList;
    }
}
