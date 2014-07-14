package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CreditRequestTypeDAO;
import com.clevel.selos.model.db.master.CreditRequestType;
import com.clevel.selos.model.view.CreditRequestTypeView;
import com.clevel.selos.util.Util;

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
        CreditRequestType creditRequestType = new CreditRequestType();
        if(!Util.isNull(creditRequestTypeView) && !Util.isZero(creditRequestTypeView.getId())){
            creditRequestType = creditRequestTypeDAO.findById(creditRequestTypeView.getId());
        }
        creditRequestType.setCode(creditRequestTypeView.getCode());
        creditRequestType.setName(creditRequestTypeView.getName());
        creditRequestType.setDescription(creditRequestTypeView.getDescription());
        creditRequestType.setActive(creditRequestTypeView.getActive());

        return creditRequestType;
    }

    public CreditRequestTypeView transformToView(CreditRequestType creditRequestType) {
        CreditRequestTypeView creditRequestTypeView = new CreditRequestTypeView();
        if(creditRequestType != null){
            creditRequestTypeView.setId(creditRequestType.getId());
            creditRequestTypeView.setCode(creditRequestType.getCode());
            creditRequestTypeView.setName(creditRequestType.getName());
            creditRequestTypeView.setDescription(creditRequestType.getDescription());
            creditRequestTypeView.setActive(creditRequestType.getActive());
        }
        return creditRequestTypeView;
    }

    public List<CreditRequestType> transformToModelList(List<CreditRequestTypeView> creditRequestTypeViewList) {
        List<CreditRequestType> creditRequestTypeList = new ArrayList<CreditRequestType>();
        if (creditRequestTypeViewList != null) {
            for (CreditRequestTypeView crtv : creditRequestTypeViewList) {
                CreditRequestType creditRequestType = transformToModel(crtv);
                creditRequestTypeList.add(creditRequestType);
            }
        }
        return creditRequestTypeList;
    }

    public List<CreditRequestTypeView> transformToViewList(List<CreditRequestType> creditRequestTypeList) {
        List<CreditRequestTypeView> creditRequestTypeViewList = new ArrayList<CreditRequestTypeView>();
        if (creditRequestTypeList != null) {
            for (CreditRequestType crt : creditRequestTypeList) {
                CreditRequestTypeView creditRequestTypeView = transformToView(crt);
                creditRequestTypeViewList.add(creditRequestTypeView);
            }
        }
        return creditRequestTypeViewList;
    }

    public CreditRequestType transformSelectToModel(CreditRequestTypeView creditRequestTypeView) {
        CreditRequestType creditRequestType = new CreditRequestType();
        if(!Util.isNull(creditRequestTypeView) && !Util.isZero(creditRequestTypeView.getId())){
            creditRequestType = creditRequestTypeDAO.findById(creditRequestTypeView.getId());
        }
        return creditRequestType;
    }
}
