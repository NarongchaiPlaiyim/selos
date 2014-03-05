package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CreditRequestTypeDAO;
import com.clevel.selos.model.db.master.CreditRequestType;
import com.clevel.selos.model.view.CreditRequestTypeView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CreditRequestTypeTransform extends Transform {
    @Inject
    private CreditRequestTypeDAO creditRequestTypeDAO;

    @Inject
    public CreditRequestTypeTransform() {
    }

    public CreditRequestType transformToModel(CreditRequestTypeView creditRequestTypeView) {
        if (creditRequestTypeView == null) {
            return new CreditRequestType();
        }

        CreditRequestType creditRequestType;
        if (creditRequestTypeView.getId() != 0) {
            creditRequestType = creditRequestTypeDAO.findById(creditRequestTypeView.getId());
        } else {
            creditRequestType = new CreditRequestType();
        }
        creditRequestType.setCode(creditRequestTypeView.getCode());
        creditRequestType.setName(creditRequestTypeView.getName());
        creditRequestType.setDescription(creditRequestTypeView.getDescription());
        creditRequestType.setActive(creditRequestTypeView.getActive());
        return creditRequestType;
    }

    public List<CreditRequestType> transformToModel(List<CreditRequestTypeView> creditRequestTypeViewList) {
        List<CreditRequestType> creditRequestTypeList = new ArrayList<CreditRequestType>();
        if (creditRequestTypeViewList == null) {
            return creditRequestTypeList;
        }

        for (CreditRequestTypeView creditRequestTypeView : creditRequestTypeViewList) {
            creditRequestTypeList.add(transformToModel(creditRequestTypeView));
        }
        return creditRequestTypeList;
    }

    public CreditRequestTypeView transformToView(CreditRequestType creditRequestType) {
        CreditRequestTypeView creditRequestTypeView = new CreditRequestTypeView();
        if (creditRequestType == null) {
            return creditRequestTypeView;
        }
        creditRequestTypeView.setId(creditRequestType.getId());
        creditRequestTypeView.setCode(creditRequestType.getCode());
        creditRequestTypeView.setName(creditRequestType.getName());
        creditRequestTypeView.setDescription(creditRequestType.getDescription());
        creditRequestTypeView.setActive(creditRequestType.getActive());
        return creditRequestTypeView;
    }

    public List<CreditRequestTypeView> transformToView(List<CreditRequestType> creditRequestTypeList) {
        List<CreditRequestTypeView> creditRequestTypeViewList = new ArrayList<CreditRequestTypeView>();
        if (creditRequestTypeList == null) {
            return creditRequestTypeViewList;
        }

        for (CreditRequestType creditRequestType : creditRequestTypeList) {
            creditRequestTypeViewList.add(transformToView(creditRequestType));
        }
        return creditRequestTypeViewList;
    }
}
